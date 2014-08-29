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
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.phpsemantics.debug.core.ConfigAnalyser;

/**
 * A variable in a KPHP stack frame
 */
public class KPHPVariable extends KPHPDebugElement implements IVariable {	
	public static final String KPHPboolean = "bool";
	public static final String KPHPinteger = "int";
	public static final String KPHPfloat = "float";
	public static final String KPHPstring = "string";
	
	public static final String KPHParray = "array";
	public static final String KPHPobject = "object";
	public static final String KPHPresource = "resource";
	/**
	 * PHP integer regex
	 * decimal     : [1-9][0-9]*|0
	 * hexadecimal : 0[xX][0-9a-fA-F]+
	 * octal       : 0[0-7]+
	 * binary      : 0b[01]+
	 * integer     : [+-]?decimal
	 * 				| [+-]?hexadecimal
	 * 			    | [+-]?octal
	 * 			    | [+-]?binary
	 * */
	public static final String DECIMAL = "^[+-]?[1-9][0-9]*|0";
	public static final String HEXADECIMAL = "^[+-]?0[xX][0-9a-fA-F]+";
	public static final String OCTAL = "^[+-]?0[0-7]+";
	public static final String BINARY = "^[+-]?0b[01]+";
	
	/**
	 * TODO any sign before LNUM or DNUM?
	 * PHP float regex
	 * LNUM          [0-9]+
	 * DNUM          ([0-9]*[\.]{LNUM}) | ({LNUM}[\.][0-9]*)
	 * EXPONENT_DNUM [+-]?(({LNUM} | {DNUM}) [eE][+-]? {LNUM})
	 **/
	public static final String LNUM = "[0-9]+";
	public static final String DNUM = "([0-9]*."+LNUM+")|("+LNUM+".[0-9]*)";
	public static final String EXPONENT_DNUM  = "^[+-]?(("+LNUM+"|"+DNUM+")[eE][+-]?"+LNUM+")";
	/**
	 * TODO the following regex have not been tested
	 * PHP boolean
	 **/
	public static final String BOOLEAN = "^true|false";

	
	public static final int NAME = 0;
	public static final int VISIBILITY = 1;
	public static final int LOCATION = 2;
		
	private KPHPStackFrame fStackFrame;
			
	private String[] attributes;
	
	public String[] getAttributes(){
		return attributes;
	}
	
	private KPHPValue value;
	
	private int symLocId;

	public KPHPVariable(KPHPDebugTarget target, KPHPStackFrame stack, String listItem){
		super(target);
		this.fStackFrame = stack;
		attributes = ConfigAnalyser.getVariable(listItem);
		symLocId = Integer.parseInt(attributes[LOCATION]);
		value = new KPHPValue(target, stack, symLocId);
		configuration = fStackFrame.getConfiguration();
	}

	@Override
	public void setValue(String expression) throws DebugException {
		//TODO not finished yet
		
		if(fStackFrame.getThread() instanceof KPHPThread){
			((KPHPThread)fStackFrame.getThread()).
				modifyHeapMap(attributes[LOCATION], expression);
			value = new KPHPValue(getDebugTarget(), fStackFrame, 
					Integer.parseInt(attributes[LOCATION]), expression);

		}
		
	}

	@Override
	public void setValue(IValue value) throws DebugException {
		if(value instanceof KPHPValue)
			this.value = (KPHPValue) value;
	}

	@Override
	public boolean supportsValueModification() {
		return true;

	/*	String type = value.getReferenceTypeName();
		boolean isValueValidate = false;
		if(type.equals(KPHPstring)
				||type.equals(KPHPinteger)
				||type.equals(KPHPfloat)
				||type.equals(KPHPboolean)){
			return true;
		}
		return false;*/
	}

	@Override
	public boolean verifyValue(String expression) throws DebugException {
		String type = value.getReferenceTypeName();
		boolean isValueValidate = false;
		if(type.equals(KPHPstring))
			return true;
		if(type.equals(KPHPinteger)){
			isValueValidate = (expression.matches(DECIMAL)
					||expression.matches(HEXADECIMAL)
					||expression.matches(OCTAL)
					||expression.matches(BINARY))?true:false;
		
		}else if(type.equals(KPHPfloat)){
			isValueValidate = (expression.matches(LNUM)
					||expression.matches(DNUM)
					||expression.matches(EXPONENT_DNUM))?true:false;
		}else if(type.equals(KPHPboolean)){
			isValueValidate = expression.matches(BOOLEAN)?true:false;
		}
		
		return isValueValidate;
	}

	@Override
	public boolean verifyValue(IValue value) throws DebugException {
		//TODO when is this invoked?
		return true;
	}

	@Override
	public IValue getValue() throws DebugException {
		return value;
	}

	@Override
	public String getName() throws DebugException {
		String name = "$"+attributes[NAME].replaceAll("\"", "");
		String id = "   (id="+attributes[LOCATION]+")";
		return name+id; 
	}

	@Override
	public String getReferenceTypeName() throws DebugException {
		return "NoType";
	}

	@Override
	public boolean hasValueChanged() throws DebugException {
		return false;
	}

}
