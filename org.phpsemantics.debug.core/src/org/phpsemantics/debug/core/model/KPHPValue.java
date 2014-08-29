package org.phpsemantics.debug.core.model;


import java.util.ArrayList;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.phpsemantics.debug.core.ConfigAnalyser;

public class KPHPValue extends KPHPDebugElement implements IValue{
	
	//private static final int LOCATION_ID = 0;
	private static final int VALUE = 1;
	private static final int TYPE = 2;
	//private static final int REF_COUNT = 3;
	//private static final int IS_REF = 4;
		
	private KPHPStackFrame fStackFrame;
	
	private int symLocId;
	
	private String[] attributes;

	// THE ARRAY LIST COULD BE EMPTY
	private ArrayList<IVariable> fVariables = new ArrayList<IVariable>();
	
	public KPHPValue(KPHPDebugTarget target, KPHPStackFrame stack, int symLocId) {
		super(target);
		fStackFrame = stack;
		this.symLocId = symLocId; 
		attributes = fStackFrame.getValueAttributes(symLocId);
		init();
	}
	
	//Constructor to create value
	public KPHPValue(KPHPDebugTarget target, KPHPStackFrame stack, int symLocId, String newValue) {
		super(target);
		fStackFrame = stack;
		this.symLocId = symLocId; 
		attributes = fStackFrame.getValueAttributes(symLocId);
		attributes[VALUE] = newValue;
		init();
	}
	
	private void init(){
		/*if(fStackFrame.getThread() instanceof KPHPThread){
			configuration = ((KPHPThread)fStackFrame.getThread()).getConfiguration();
		}*/
		configuration = fStackFrame.getConfiguration();
		String symLoc = ConfigAnalyser.getSymLocAt(configuration, symLocId);
		if(ConfigAnalyser.hasVariables(symLoc)){
			String[] listItems = ConfigAnalyser.getArrayListItemsAt(symLoc);
			for(String listItem: listItems){
				KPHPVariable var = new KPHPVariable(getDebugTarget(),fStackFrame,listItem);
				fVariables.add(var);
			}
		}
	}

	@Override
	public String getReferenceTypeName() throws DebugException {
		return attributes[TYPE];
	}

	@Override
	public String getValueString() throws DebugException {
		if(attributes[TYPE].equals("array"))
			return null; 
		return attributes[VALUE];
	}

	@Override
	public boolean isAllocated() throws DebugException {
		return true;
	}

	@Override
	public IVariable[] getVariables() throws DebugException {

		return fVariables.toArray(new IVariable[fVariables.size()]);
	}

	@Override
	public boolean hasVariables() throws DebugException {
		
		return (fVariables.size()>0)?true:false;
		
	}


}
