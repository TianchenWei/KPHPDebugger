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

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IRegisterGroup;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;
import org.jdom2.Document;
import org.phpsemantics.debug.core.ConfigAnalyser;



/**
 * KPHP stack frame.
 */
public class KPHPStackFrame extends KPHPDebugElement implements IStackFrame {

	private boolean fStepping = false;
	
	private boolean modified = false;

	private KPHPThread fThread;
		
	private String functionName;

	protected boolean getModified(){
		return modified;
	}
	protected void setModified(boolean flag){
		modified = flag;			
	}
	
	protected void modifyHeapMap(String key, String newValue){
		ConfigAnalyser.updateHeapMap(heapMap,key, newValue );
		modified = true;
	}
		
	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	private int symLocId;
	
/*	private int steps = 0;//same as size of the configurationAbsolutePaths
*/		
	private ArrayList<IVariable> fVariables = new ArrayList<IVariable>();
	
	private ArrayList<Integer> effectiveLines;


	public ArrayList<Integer> getEffectiveLines() {
		return effectiveLines;
	}

	public void setEffectiveLines(ArrayList<Integer> effectiveLines) {
		this.effectiveLines = effectiveLines;
	}

	private int startLine =-1;
	
	private int endLine =-1;
	
	public KPHPStackFrame(KPHPDebugTarget target, String functionName, Document configuration) {
		super(target);
		fThread = target.getThread();
		this.functionName = functionName;
		setConfiguration(configuration);
		init();
	}
	
	public KPHPStackFrame(KPHPDebugTarget target,String functionName, Document configuration, int symLocId) {
		super(target);
		fThread = target.getThread();
		this.functionName = functionName;
		setConfiguration(configuration);
		this.symLocId = symLocId;
		init();
	}
	
	private void init(){
		configurationAbsolutePaths.add(configuration.getBaseURI().replace("file:", ""));
		//configuration = fThread.getConfiguration();
		linePointer = fThread.getLinePointer();
		heapMap = ConfigAnalyser.getHeapMap(configuration);

		String symLoc;
		if (functionName.equals("main")){
			symLoc = ConfigAnalyser.getSuperGlobalScope(configuration);
			symLocId =ConfigAnalyser.getSymLocID(symLoc);
		}
		
		symLoc = ConfigAnalyser.getSymLocAt(configuration, symLocId);
		if(ConfigAnalyser.hasVariables(symLoc)){
			String[] listItems = ConfigAnalyser.getArrayListItemsAt(symLoc);
			for(String listItem: listItems){
				KPHPVariable var = new KPHPVariable(getDebugTarget(),this,listItem);
				fVariables.add(var);
			}
		}
		
		try {
			effectiveLines = getDebugTarget().getFunction(functionName);
			Integer[] eff = effectiveLines.toArray(new Integer[effectiveLines.size()]);
			if(eff.length>=1){
				startLine = eff[0];
				endLine = eff[eff.length-1];
			}
		} catch (IOException e) {
			effectiveLines = new ArrayList<Integer>();
			e.printStackTrace();
		}
	}
	
/*	
	private void updateVariables(){
		
		fVariables.clear();
		String symLoc;
		if (functionName.equals("main")){
			symLoc = ConfigAnalyser.getSuperGlobalScope(configuration);
			symLocId =ConfigAnalyser.getSymLocID(symLoc);
		}
		
		symLoc = ConfigAnalyser.getSymLocAt(configuration, symLocId);
		if(ConfigAnalyser.hasVariables(symLoc)){
			String[] listItems = ConfigAnalyser.getArrayListItemsAt(symLoc);
			for(String listItem: listItems){
				KPHPVariable var = new KPHPVariable(getDebugTarget(),this,listItem);
				fVariables.add(var);
			}
		}
	}*/
	
	protected String[] getValueAttributes(int symLocId){
		String symLoc = ConfigAnalyser.getSymLocAt(
				configuration, symLocId);
		if(ConfigAnalyser.isArrayVarible(symLoc)){
			return ConfigAnalyser.getArrayValue(symLoc);
		}else{
			return ConfigAnalyser.getScalarValue(symLoc); 
		}	
	}
	
	
	@Override
	public boolean canStepInto() {
		return false;
	}

	@Override
	public boolean canStepOver() {
		return endLine>0 && startLine>0 
				&&  ConfigAnalyser.getLineNumber(configuration)>=startLine 
				&&  ConfigAnalyser.getLineNumber(configuration)<=endLine;
	}

	@Override
	public boolean canStepReturn() {
		return false;
	}

	@Override
	public boolean isStepping() {
		return fStepping;
	}

	@Override
	public void stepInto() throws DebugException {
	/*	fStepping = true;
		int nextResumeLineNumber = fThread.getNextLinePointer();

		if(nextResumeLineNumber>0 && linePointer+1>=nextResumeLineNumber)
			fThread.resume();
			
		try {
			String newConfig = fTarget.getNextConfig(getCurrentConfigurationPath().toString());
			configurationAbsolutePaths.add(newConfig);
			linePointer++;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fStepping =false;

		}
		fStepping =false;*/
	}

	@Override
	public void stepOver() throws DebugException {
	
		if(ConfigAnalyser.getLineNumber(configuration)==endLine){
			//fThread.getCallingStack(this).stepOver();
			//fThread.stepReturn();
			fThread.resume();
			//if (fThread.linePointer>startLine&& fThread.linePointer<endLine)

		}else{
			if(configurationAbsolutePaths.size()<=1){
				try {
					String newConfiguration = getDebugTarget().createConfig(STEP, ConfigAnalyser.getLineNumber(configuration)+1, effectiveLines);
					configurationAbsolutePaths.add(newConfiguration);
					configuration = buildConfiguration(newConfiguration);
					heapMap = ConfigAnalyser.getHeapMap(configuration);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				if(modified){
					
					try {
						String updatedConfiguration = getDebugTarget().getUpdateConfiguration2(this);
						configurationAbsolutePaths.removeLast();
						configurationAbsolutePaths.add(updatedConfiguration);
						configuration = buildConfiguration(updatedConfiguration);
						modified = false;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					String newConfiguration = getDebugTarget().getNextConfig(configurationAbsolutePaths.getLast());
					configurationAbsolutePaths.add(newConfiguration);
					configuration = buildConfiguration(newConfiguration);
					heapMap = ConfigAnalyser.getHeapMap(configuration);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			fThread.updateStackFrames(this);
//			fThread.setLinePointer(ConfigAnalyser.getLineNumber(configuration));
			fireChangeEvent(0);
		}
		
	}

	@Override
	public void stepReturn() throws DebugException {
		fThread.resume();
	}

	@Override
	public boolean canResume() {
		return fThread.canResume();
	}

	@Override
	public boolean canSuspend() {
		return false;
	}

	@Override
	public boolean isSuspended() {
		return fStepping;
	}

	@Override
	public void resume() throws DebugException {
		fThread.resume();
	}

	@Override
	public void suspend() throws DebugException {
	}

	@Override
	public boolean canTerminate() {
		return fThread.canTerminate();
	}

	@Override
	public boolean isTerminated() {
		return fThread.isTerminated();
	}

	@Override
	public void terminate() throws DebugException {
		fThread.terminate();
	}

	@Override
	public IThread getThread() {
		return fThread;
	}
	
	@Override
	public IVariable[] getVariables() throws DebugException {
		return fVariables.toArray(new IVariable[fVariables.size()]);
		//return getVariables(symLocId);
	}

	@Override
	public boolean hasVariables() throws DebugException {
		return (fVariables.size()>0)?true:false;
	}

	@Override
	public int getLineNumber() throws DebugException {
		int newLinePointer = ConfigAnalyser.getLineNumber(configuration);
		if(newLinePointer >0){
			return newLinePointer;
		}
		return fThread.linePointer;
//		return ConfigAnalyser.getLineNumber(configuration);
//		return fThread.linePointer;
	}

	@Override
	public int getCharStart() throws DebugException {
		return -1;
	}

	@Override
	public int getCharEnd() throws DebugException {
		return -1;
	}

	@Override
	public String getName() throws DebugException {
		return getDebugTarget().getName()+"<"+functionName+">"+" (id="+symLocId+")";
	}

	@Override
	public IRegisterGroup[] getRegisterGroups() throws DebugException {
		return null;
	}

	@Override
	public boolean hasRegisterGroups() throws DebugException {
		return false;
	}


}
