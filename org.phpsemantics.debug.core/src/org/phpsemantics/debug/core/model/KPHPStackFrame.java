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

import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IRegisterGroup;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.jdom2.Element;

/**
 * KPHP stack frame.
 */
public class KPHPStackFrame extends KPHPDebugElement implements IStackFrame {

	private IThread fThread;
	
	private IVariable[] fVariable;
	
	public KPHPStackFrame(IDebugTarget target, IThread thread) {
		super(target);
		fThread = thread;
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canStepInto() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canStepOver() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canStepReturn() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStepping() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void stepInto() throws DebugException {
		// TODO Auto-generated method stub
		
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
	public boolean canResume() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canSuspend() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSuspended() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resume() throws DebugException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void suspend() throws DebugException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canTerminate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTerminated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void terminate() throws DebugException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IThread getThread() {
		// TODO Auto-generated method stub
		return fThread;
	}

	@Override
	public IVariable[] getVariables() throws DebugException {
		IVariable var1 = new KPHPVariable(getDebugTarget(), "var1");
		IValue value1 = new KPHPValue(getDebugTarget(), var1);
		var1.setValue(value1);
		IVariable var2 = new KPHPVariable(getDebugTarget(), "var2");
		IValue value2 = new KPHPValue(getDebugTarget(), var2);
		var2.setValue(value2);
		IVariable var3 = new KPHPVariable(getDebugTarget(), "var3");
		var3.setValue(value2);
		fVariable = new IVariable[]{var1,var2,var3};


		return fVariable;
	}

	@Override
	public boolean hasVariables() throws DebugException {
		return true;
	}

	@Override
	public int getLineNumber() throws DebugException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCharStart() throws DebugException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCharEnd() throws DebugException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() throws DebugException {
		// TODO Auto-generated method stub
		return "stack 1";
	}

	@Override
	public IRegisterGroup[] getRegisterGroups() throws DebugException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasRegisterGroups() throws DebugException {
		// TODO Auto-generated method stub
		return false;
	}

}
