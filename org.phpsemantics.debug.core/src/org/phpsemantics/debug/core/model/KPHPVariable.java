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
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

/**
 * A variable in a KPHP stack frame
 */
public class KPHPVariable extends KPHPDebugElement implements IVariable {
	
	private String name;
	
	private IValue value;

	public KPHPVariable(IDebugTarget target, String name) {
		super(target);
		this.name = name;
	}

	@Override
	public void setValue(String expression) throws DebugException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(IValue value) throws DebugException {
		this.value = value; 
	}

	@Override
	public boolean supportsValueModification() {
		return true;
	}

	@Override
	public boolean verifyValue(String expression) throws DebugException {
		return true;
	}

	@Override
	public boolean verifyValue(IValue value) throws DebugException {
		return true;
	}

	@Override
	public IValue getValue() throws DebugException {
		return value;
	}

	@Override
	public String getName() throws DebugException {
		return name;
	}

	@Override
	public String getReferenceTypeName() throws DebugException {
		return "integer";
	}

	@Override
	public boolean hasValueChanged() throws DebugException {
		// TODO Auto-generated method stub
		return false;
	}

}
