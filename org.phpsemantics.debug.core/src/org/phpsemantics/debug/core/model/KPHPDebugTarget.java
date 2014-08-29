package org.phpsemantics.debug.core.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.ILineBreakpoint;
import org.eclipse.debug.core.model.IMemoryBlock;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IThread;
import org.phpsemantics.debug.core.ConfigAnalyser;
import org.phpsemantics.debug.core.ScriptAnalyzer;
import org.phpsemantics.debug.core.launching.IKPHPConstants;
import org.phpsemantics.debug.core.launching.KPHPInterpreter;

public class KPHPDebugTarget extends KPHPDebugElement implements IDebugTarget{
	
	private static final String INTERNAL = IKPHPConstants.INTERNAL_FILE_PREFIX;
	private static final String PHP = IKPHPConstants.INTERNAL_FILE_SUFFIX;
	private static final String BREAKPOINT_L = IKPHPConstants.INTERNAL_FILE_BREAKPOINT_L;
	private static final String BREAKPOINT_R = IKPHPConstants.INTERNAL_FILE_BREAKPOINT_R;

	private static final String CONFIG = IKPHPConstants.CONFIG_FILE_PREFIX; 
	private static final String XML = IKPHPConstants.CONFIG_FILE_SUFFIX;
/*	private static final String GRAPH = IKPHPConstants.DOT_FILE_PREFIX;
	private static final String DOT = IKPHPConstants.DOT_FILE_SUFFIX;*/
	
	private static final int TEMP_DIR = 0;
	private static final int SCRIPT = 1;
	
	private ILaunch fLaunch;
	private KPHPThread fThread;
	private IThread[] fThreads;
	
	private String[] args;
		
	// suspend state
	private boolean fSuspended = true;
	
	// terminated state
	private boolean fTerminated = false;
	
	private KPHPInterpreter interpreter;
	
	private ScriptAnalyzer sa;
	
	private ArrayList<IBreakpoint> breakpoints = new ArrayList<IBreakpoint>();
	
	public KPHPDebugTarget(ILaunch launch, KPHPInterpreter ki, String[] args) {
		super(null);
		fLaunch = launch;
		interpreter = ki;
		sa =  new ScriptAnalyzer(args[SCRIPT]);
		this.args = args;
		//the order is important
		init();
		fThread = new KPHPThread(this);
		fThreads = new IThread[] {fThread};
		DebugPlugin.getDefault().getBreakpointManager().addBreakpointListener(this);
	}
	
	private void init(){
		//add existing breakpoints	
		IBreakpoint[] bp = DebugPlugin.getDefault().getBreakpointManager().getBreakpoints();
		for(IBreakpoint breakpoint: bp ){
			
			if(supportsBreakpoint(breakpoint)){
				breakpoints.add(breakpoint);
			}
		}
		
	}
	
	private String createInternalScript(int type, int startLine, ArrayList<Integer> stepLineNumbers) throws IOException{
		if(startLine>=0){
			Set<Integer>lineNumbers = getBreakpointLineNumbers();
			// create internal script
			File internalScript = Files.createTempFile(Paths.get(args[TEMP_DIR]),INTERNAL,PHP).toFile();
						
			
			LineNumberReader lr = new LineNumberReader(
						Files.newBufferedReader((new File(args[SCRIPT])).toPath(), Charset.defaultCharset()));
				
			BufferedWriter out = new BufferedWriter(new FileWriter(internalScript));           
				//out.write(breakpoint);
			String line;
			int lineNumber = 1;

			if(type == STEP){
				while ((line = lr.readLine ()) != null){
					if(lr.getLineNumber()>=startLine && stepLineNumbers.contains(lr.getLineNumber()))
							line = BREAKPOINT_L+lineNumber+BREAKPOINT_R+line; 
					out.write(line);
					out.newLine();
					lineNumber++;
				}
	
				lr.close();
				out.close();
			/*	while ((line = lr.readLine ()) != null){
					if(lr.getLineNumber()>=startLine && lineNumbers.contains(lr.getLineNumber())
							||lr.getLineNumber()==startLine )
							
						line = BREAKPOINT_L+lineNumber+BREAKPOINT_R+line; 
					out.write(line);
					out.newLine();  
					lineNumber++;

				}
	
				lr.close();
				out.close();*/
			
			}else if(type == RESUME){
				while ((line = lr.readLine ()) != null){
					if(lr.getLineNumber()>=startLine && lineNumbers.contains(lr.getLineNumber()) )
							line = BREAKPOINT_L+lineNumber+BREAKPOINT_R+line; 
					out.write(line);
					out.newLine();  
					lineNumber++;

				}
	
				lr.close();
				out.close();
			}
				
			//return absolute path of the inner script
			return internalScript.getAbsolutePath();
		}
		return args[SCRIPT];
		
	}

	protected String createConfig(int type, int startLine, ArrayList<Integer> stepLineNumbers) throws IOException{
		
		String path = createInternalScript(type, startLine, stepLineNumbers);
			
		File configuration = Files.createTempFile(Paths.get(args[TEMP_DIR]),CONFIG,XML).toFile();
	
		interpreter.writeOutputToFile(interpreter.debug(path), configuration);
		return configuration.getAbsolutePath();
	}
	
	protected String getNextConfig(String config) throws IOException{
		File newConfig = Files.createTempFile(Paths.get(args[TEMP_DIR]),CONFIG,XML).toFile();
		interpreter.writeOutputToFile(interpreter.resume(config), newConfig);
		return newConfig.getAbsolutePath();	
	}
	
	protected String getUpdateConfiguration() throws IOException{
		
		File configuration = Files.createTempFile(Paths.get(args[TEMP_DIR]),CONFIG,XML).toFile();
		File configurationEnhanced = Files.createTempFile(Paths.get(args[TEMP_DIR]),CONFIG,XML).toFile();
		ConfigAnalyser.updateConfigurationHeap(fThread.getConfiguration(), fThread.getHeapMap());
		
		ConfigAnalyser.outputXML(fThread.getConfiguration(), configuration.getAbsolutePath());
		
		LineNumberReader lr = new LineNumberReader(
					Files.newBufferedReader(configuration.toPath(), Charset.defaultCharset()));
			
		BufferedWriter out = new BufferedWriter(new FileWriter(configurationEnhanced));           
			//out.write(breakpoint);
		
		//skip<?xml version="1.0" encoding="UTF-8"?>
		String line = lr.readLine();

			while ((line = lr.readLine ()) != null){
				line = line.replaceAll("&gt;", ">");
				out.write(line);
				out.newLine();    	
			}

			lr.close();
			out.close();
		return configurationEnhanced.getAbsolutePath();
	}
	
	@Override
	public boolean canTerminate() {
		return true;
	}

	@Override
	public boolean isTerminated() {
		return fTerminated;
	}

	@Override
	public void terminate() throws DebugException {
		fTerminated = true;
		fSuspended = false;
		DebugPlugin.getDefault().getBreakpointManager().removeBreakpointListener(this);
		fireTerminateEvent();		
	}

	@Override
	public boolean canResume() {
		
		return fThread.canResume();
	}

	@Override
	public boolean canSuspend() {
		return !(isSuspended() || isTerminated()) ;
 	}

	@Override
	public boolean isSuspended() {
		return fSuspended;
	}

	@Override
	public void resume() throws DebugException {
		fSuspended = false;
		fireResumeEvent(0);
		fThread.resume();
		fSuspended = true;
		fireSuspendEvent(0);
	}

	@Override
	public void suspend() throws DebugException {
		fSuspended = true;
		fireSuspendEvent(0);
	}

	@Override
	public void breakpointAdded(IBreakpoint breakpoint) {
		if(supportsBreakpoint(breakpoint))
			breakpoints.add(breakpoint);

	}

	@Override
	public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta) {
		if(supportsBreakpoint(breakpoint))
			breakpoints.remove(breakpoint);
	}

	@Override
	public void breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canDisconnect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void disconnect() throws DebugException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDisconnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportsStorageRetrieval() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IMemoryBlock getMemoryBlock(long startAddress, long length)
			throws DebugException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IProcess getProcess() {
		return null;
	}

	@Override
	public IThread[] getThreads() throws DebugException {
		return fThreads;
	}

	@Override
	public boolean hasThreads() throws DebugException {
		return true;
	}

	@Override
	public String getName() throws DebugException {
		
		return Paths.get(args[SCRIPT]).getFileName().toString();
	}

	@Override
	public boolean supportsBreakpoint(IBreakpoint breakpoint) {
	
		if (breakpoint instanceof ILineBreakpoint)
			return Paths.get(args[SCRIPT]).endsWith(
					breakpoint.getMarker().getResource().getFullPath().lastSegment());
		
		return false;
		
	}
	
	/**
	 * 
	 * */
	public KPHPThread getThread(){
		return fThread;
	}
	/**
	 * 
	 * */
	public ILaunch getLaunch() {
		return fLaunch;
	}
	
	public IBreakpoint[] getBreakpoints(){
		return breakpoints.toArray(new IBreakpoint[breakpoints.size()]); 
	}

	/**
	 * 
	 * */
	public ConcurrentSkipListSet<Integer> getBreakpointLineNumbers(){
		ConcurrentSkipListSet<Integer> set = new ConcurrentSkipListSet<Integer>();
		
		for(IBreakpoint bp: breakpoints){
			
			if(bp instanceof ILineBreakpoint){
				try {
					int lineNumber = ((ILineBreakpoint) bp).getLineNumber();
					set.add(lineNumber);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}

		}
		
		return set;
		
	}
	/**
	 * 
	 * */
	public ArrayList<Integer> getFunction(String functionName) throws IOException{
		if(functionName=="main")
			return sa.getMain();
		else
			return sa.getFunction(functionName);
	}
		
}
