package org.phpsemantics.debug.core.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IValue;
import org.jdom2.Document;
import org.jdom2.input.DOMBuilder;
import org.phpsemantics.debug.core.launching.IKPHPLaunchConfigurationConstants;
import org.xml.sax.SAXException;

public class KPHPDebugTarget extends KPHPDebugElement implements IDebugTarget{
	private Document configuration;

	private ILaunch fLaunch;
	private KPHPDebugTarget fTarget;
	private IProcess fProcess;
	private KPHPThread fThread;
	private IThread[] fThreads;
	// suspend state
	private boolean fSuspended = true;
	
	// terminated state
	private boolean fTerminated = false;
	
	private int count = 3;


	public KPHPDebugTarget(ILaunch launch) {
		super(null);
		setLaunch(launch);
		fTarget = this;
		//fProcess = process;
		fThread = new KPHPThread(this);
		fThreads = new IThread[] {fThread};
		DebugPlugin.getDefault().getBreakpointManager().addBreakpointListener(this);
		init();
		
	}
	
	private void init(){

	}
	
	/**
	 * 
	 **/
	public synchronized Document getConfiguration() {
		return configuration;
	}
	
	/**
	 * 
	 **/
	public void setConfiguration(Document configuration) {
		this.configuration = configuration;
	}

	@Override
	public boolean canTerminate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTerminated() {
		return fTerminated;
	}

	@Override
	public void terminate() throws DebugException {
		fTerminated = true;
	}

	@Override
	public boolean canResume() {

		return !isTerminated() && isSuspended();
	}

	@Override
	public boolean canSuspend() {
		return !(isTerminated() || isSuspended());
	}

	@Override
	public boolean isSuspended() {
		return fSuspended;
	}

	@Override
	public void resume() throws DebugException {
		fSuspended = false;
		System.out.println("resume");
		suspend();
	}

	@Override
	public void suspend() throws DebugException {
		fSuspended = true;
		System.out.println("suspend triggered");
	}

	@Override
	public void breakpointAdded(IBreakpoint breakpoint) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IThread[] getThreads() throws DebugException {
		return fThreads;
	}

	@Override
	public boolean hasThreads() throws DebugException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getName() throws DebugException {
		// TODO Auto-generated method stub
		return "debug target";
	}

	@Override
	public boolean supportsBreakpoint(IBreakpoint breakpoint) {
		// TODO Auto-generated method stub
		return false;
	}

	public ILaunch getLaunch() {
		return fLaunch;
	}

	public void setLaunch(ILaunch fLaunch) {
		this.fLaunch = fLaunch;
	}
	
}
