package org.phpsemantics.debug.core.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

public class KPHPValue extends KPHPDebugElement implements IValue{
	
	private List<IVariable> variables;

	public KPHPValue(IDebugTarget target, IVariable variable) {
		super(target);
		variables = new ArrayList<IVariable>();
		variables.add(variable);
	}

	@Override
	public String getReferenceTypeName() throws DebugException {
		return "String";
	}

	@Override
	public String getValueString() throws DebugException {
		return "valueString";
	}

	@Override
	public boolean isAllocated() throws DebugException {
		return true;
	}

	@Override
	public IVariable[] getVariables() throws DebugException {
		return (IVariable[]) variables.toArray();
	}

	@Override
	public boolean hasVariables() throws DebugException {
		return true;
	}

}
