package org.phpsemantics.debug.core.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

import org.eclipse.debug.core.model.DebugElement;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.phpsemantics.debug.core.launching.IKPHPConstants;

public abstract class KPHPDebugElement extends DebugElement {
	
	public static final int RESUME = 0;
	public static final int STEP = 1;
	protected  LinkedHashMap<String, String> heapMap = new LinkedHashMap<String,String>();
	
	public Map<String, String> getHeapMap(){
		return heapMap;
	}
	protected LinkedList<String> configurationAbsolutePaths = new LinkedList<String>();
	public LinkedList<String> getConfigurationAbsolutePaths() {
		return configurationAbsolutePaths;
	}

	public void setConfigurationAbsolutePaths(
			LinkedList<String> configurationAbsolutePaths) {
		this.configurationAbsolutePaths = configurationAbsolutePaths;
	}

	// containing target 
	private KPHPDebugTarget fTarget;
	protected Document configuration;
	protected int linePointer = 0;

	
	public KPHPDebugTarget getDebugTarget() {
        return fTarget;
    }
	
	public Document getConfiguration(){
		return configuration;
	}
	
	public void setConfiguration(Document doc){
		configuration = doc;
	}
	
	/**
	 * Constructs a new debug element contained in the given
	 * debug target.
	 * 
	 * @param target debug target (PDA VM)
	 */
	public KPHPDebugElement(KPHPDebugTarget target) {
		super(target);
		fTarget = target;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.IDebugElement#getModelIdentifier()
	 */
	public String getModelIdentifier() {
		
		return IKPHPConstants.ID_KPHP_DEBUG_MODEL;
	}
	
	/**
	 * line Pointer
	 * */
	public int getLinePointer(){
		return linePointer;
	}
	
	public void setLinePointer(int lineNumber){
		linePointer = lineNumber;
	}
	
	public int getNextLinePointer(ConcurrentSkipListSet<Integer> set){
		
		if(linePointer<0||set.isEmpty()||set.higher(linePointer)==null)
			return -1;
		else
			return set.higher(linePointer);
		
	}
	
	/**
	 * Configuration Absolute Paths
	 * */
	
/*	protected String[] getConfigurationAbsolutePaths() {
		return (String[]) configurationAbsolutePaths.toArray();
	}*/
	
	public Path getCurrentConfigurationPath() {
		return Paths.get(configurationAbsolutePaths.getLast());
	}
	
	public Document buildConfiguration(String configPath) {
		File file = new File(configPath);	     
        SAXBuilder jdomBuilder = new SAXBuilder();
		Document configuration;
		try {
			configuration = jdomBuilder.build(file);
			return configuration;
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
