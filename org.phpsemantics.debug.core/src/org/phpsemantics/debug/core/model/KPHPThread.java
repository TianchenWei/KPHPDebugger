/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Bjorn Freeman-Benson - initial API and implementation
 *******************************************************************************/
package org.phpsemantics.debug.core.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.phpsemantics.debug.core.ConfigAnalyser;

/**
 * 
 */
public class KPHPThread extends KPHPDebugElement implements IThread {
	
	//if the configuration is modified
	private boolean modified = false;
	
	private boolean fSuspend = true;
	
	private boolean fStepping = false;

	private KPHPStackFrame fStackFrame;
	
	private KPHPStackFrame[] fStackFrames;
	
	//breakpoints set
	private ConcurrentSkipListSet<Integer> set = new ConcurrentSkipListSet<Integer>();
			
	public KPHPThread(KPHPDebugTarget target) {
		super(target);
		init();
	}

	private void init(){
		//empty the list of configuration files 
		set = getDebugTarget().getBreakpointLineNumbers();
		linePointer = getNextLinePointer(set);
		try {
			String configPath = getDebugTarget().createConfig(RESUME, linePointer, null);

			//build Document object
			//generate heapMap
			configuration = buildConfiguration(configPath);
			linePointer = ConfigAnalyser.getLineNumber(configuration);
			heapMap = ConfigAnalyser.getHeapMap(configuration);
			configurationAbsolutePaths.add(configPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		fireChangeEvent(0);
		//notify the config view
	}
	
	private void updateStackFrames(){
		ArrayList<IStackFrame> stackFrames = new ArrayList<IStackFrame> ();	
		
		String[] functionStackItems = 
				ConfigAnalyser.getFunctionStackItems(configuration);
		
		int stackSymLocId = ConfigAnalyser.getSymLocID(ConfigAnalyser.getCurrentScope(configuration));

		for(String item: functionStackItems){
			String[] stackAttr = ConfigAnalyser.getFunctionStackItemAttr(item);
			String stackName = stackAttr[0];
			fStackFrame = new KPHPStackFrame(getDebugTarget(), stackName, getConfiguration(),stackSymLocId);
			stackFrames.add(fStackFrame);
			stackSymLocId = Integer.parseInt(stackAttr[1]);
		}

		fStackFrame = new KPHPStackFrame(getDebugTarget(), "main", getConfiguration());
		stackFrames.add(fStackFrame);
		fStackFrames = stackFrames.toArray(new KPHPStackFrame[stackFrames.size()]);
		//
		fireSuspendEvent(0);

	}
	
	protected KPHPStackFrame getCallingStack(KPHPStackFrame currentStackFrame){
		for(int i=0; i<fStackFrames.length-1; i++){
			if(fStackFrames[i].getFunctionName().equals(currentStackFrame.getFunctionName())){
				return fStackFrames[i+1];
			}
		}
		return fStackFrames[fStackFrames.length-1];
		
	}
	// can only be updated by the topStackframe
	protected void updateStackFrames(KPHPStackFrame stackframe){
		ArrayList<IStackFrame> stackFrames = new ArrayList<IStackFrame> ();	
		
		String[] functionStackItems = 
				ConfigAnalyser.getFunctionStackItems(stackframe.getConfiguration());
		
		int stackSymLocId = ConfigAnalyser.getSymLocID(ConfigAnalyser.getCurrentScope(stackframe.getConfiguration()));

		for(String item: functionStackItems){
			String[] stackAttr = ConfigAnalyser.getFunctionStackItemAttr(item);
			String stackName = stackAttr[0];
			fStackFrame = new KPHPStackFrame(getDebugTarget(), stackName, stackframe.getConfiguration(), stackSymLocId);
			if(fStackFrame.getFunctionName().equals(stackframe.getFunctionName())){
				fStackFrame.setLinePointer(stackframe.getLinePointer());
				fStackFrame.setConfigurationAbsolutePaths(stackframe.getConfigurationAbsolutePaths());
			}
		
			stackFrames.add(fStackFrame);
			stackSymLocId = Integer.parseInt(stackAttr[1]);
		}

		fStackFrame = new KPHPStackFrame(getDebugTarget(), "main", stackframe.getConfiguration());
		if(fStackFrame.getFunctionName().equals("main")){
			fStackFrame.setLinePointer(stackframe.getLinePointer());
			fStackFrame.setConfigurationAbsolutePaths(stackframe.getConfigurationAbsolutePaths());
		}
		stackFrames.add(fStackFrame);
		fStackFrames = stackFrames.toArray(new KPHPStackFrame[stackFrames.size()]);
		fireSuspendEvent(0);
	}
	
	protected boolean getModified(){
		return modified;
	}
	protected void setModified(boolean flag){
		modified = flag;			
	}
	
	protected int getNextLinePointer(){
		return getNextLinePointer(set);
	}
	
	protected void modifyHeapMap(String key, String newValue){
		ConfigAnalyser.updateHeapMap(heapMap,key, newValue );
		modified = true;
	}
	
	@Override
	public boolean canResume(){
		return !ConfigAnalyser.isKCellEmpty(configuration)&&!getDebugTarget().isTerminated() && isSuspended();
	}
	
	@Override
	public boolean canSuspend() {
		return false;
	}
	
	@Override
	public boolean isSuspended() {
		return fSuspend;
	}

	@Override
	public void resume() throws DebugException {
		fSuspend = false;
		int nextLinePointer = getNextLinePointer(set);

		ConcurrentSkipListSet<Integer> currentSet = getDebugTarget().getBreakpointLineNumbers();
		int newNextLinePointer = getNextLinePointer(currentSet);
		if(modified){
			
			try {
				String updatedConfiguration = getDebugTarget().getUpdateConfiguration();
				configurationAbsolutePaths.add(updatedConfiguration);
				configuration = buildConfiguration(updatedConfiguration);
				modified = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		String newConfiguration;
		if(nextLinePointer == newNextLinePointer){
			try {
				newConfiguration = getDebugTarget().getNextConfig(getCurrentConfigurationPath().toString());
				configurationAbsolutePaths.add(newConfiguration);
				configuration = buildConfiguration(newConfiguration);
				linePointer = newNextLinePointer;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			try {
				newConfiguration = getDebugTarget().createConfig(RESUME, newNextLinePointer, null);
				configurationAbsolutePaths.add(newConfiguration);
				configuration = buildConfiguration(newConfiguration);
				linePointer = newNextLinePointer;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		fireChangeEvent(0);
		updateStackFrames();
		suspend();
	}

	@Override
	public void suspend() throws DebugException {
		fSuspend = true;
		fStepping = false;
		fireSuspendEvent(0);
	}

	@Override
	public boolean canStepInto() {	
		return false ;	
	}

	@Override
	public boolean canStepOver() {		
		return false;
	}

	@Override
	public boolean canStepReturn() {
		return false;
	}

	@Override
	public boolean isStepping() {
		return false;
	}

	@Override
	public void stepInto() throws DebugException {
	}

	@Override
	public void stepOver() throws DebugException {
	}

	@Override
	public void stepReturn() throws DebugException {
	}

	@Override
	public boolean canTerminate() {
		return getDebugTarget().canTerminate();
	}

	@Override
	public boolean isTerminated() {
		return getDebugTarget().isTerminated();
	}

	@Override
	public void terminate() throws DebugException {
		getDebugTarget().terminate();
	}

	@Override
	public IStackFrame[] getStackFrames() throws DebugException {	
		if (isSuspended()) {
			if(fStackFrames==null)
				updateStackFrames();				
			return fStackFrames;	
		} else {
			return new IStackFrame[0];
		}
		
		
	}

	@Override
	public boolean hasStackFrames() throws DebugException {
		//return fStackFrame != null;
		return true;
	}

	@Override
	public int getPriority() throws DebugException {
		return 0;
	}

	@Override
	public IStackFrame getTopStackFrame() throws DebugException {
		IStackFrame[] frames = getStackFrames();
		if (frames.length > 0) {
			return frames[0];
		}
		return null;
		/*getStackFrames();
		if (fStackFrames.length > 0) {
			return fStackFrames[0];
		}
		return null;*/
		//return fStackFrame;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IThread#getName()
	 */
	public String getName() throws DebugException {
		return "Thread[0]";
	}

	@Override
	public IBreakpoint[] getBreakpoints() {
		return getDebugTarget().getBreakpoints();
	}
	
	public boolean isfSuspend() {
		return fSuspend;
	}

	public void setfSuspend(boolean fSuspend) {
		this.fSuspend = fSuspend;
	}

	public boolean isfStepping() {
		return fStepping;
	}

	public void setfStepping(boolean fStepping) {
		this.fStepping = fStepping;
	}
}
