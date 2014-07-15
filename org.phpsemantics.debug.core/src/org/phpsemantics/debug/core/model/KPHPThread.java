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

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.jdom2.Document;

/**
 * 
 */
public class KPHPThread extends KPHPDebugElement implements IThread {
	
	
	private boolean fSuspended = true;
	
	private boolean fStepping = false;
	
	private boolean fTerminated = false;
	
	public KPHPThread(IDebugTarget target) {
		super(target);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canResume() {
		return !isTerminated() && !isSuspended() ;
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
	}

	@Override
	public void suspend() throws DebugException {
		fSuspended = true;
	}

	@Override
	public boolean canStepInto() {
		return true;
	}

	@Override
	public boolean canStepOver() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canStepReturn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStepping() {
		return fStepping;
	}

	@Override
	public void stepInto() throws DebugException {
		suspend();
		
	}

	@Override
	public void stepOver() throws DebugException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stepReturn() throws DebugException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canTerminate() {
		return !isTerminated();
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
	public IStackFrame[] getStackFrames() throws DebugException {
		return new IStackFrame[]{new KPHPStackFrame(getDebugTarget(), (IThread)this)};
	}

	@Override
	public boolean hasStackFrames() throws DebugException {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getPriority() throws DebugException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IStackFrame getTopStackFrame() throws DebugException {
		// TODO Auto-generated method stub
		return getStackFrames()[0];
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IThread#getName()
	 */
	public String getName() throws DebugException {
		// TODO Auto-generated method stub
		return "Thread 1";
	}

	@Override
	public IBreakpoint[] getBreakpoints() {
		// TODO Auto-generated method stub
		return  DebugPlugin.getDefault().getBreakpointManager().getBreakpoints();
	}
	
}
