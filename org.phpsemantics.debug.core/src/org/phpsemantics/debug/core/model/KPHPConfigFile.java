package org.phpsemantics.debug.core.model;

import java.io.File;
import java.io.IOException;
import org.eclipse.debug.core.model.IDebugTarget;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


public class KPHPConfigFile extends KPHPDebugElement{
	
	private Document configuration;
	
	public KPHPConfigFile(IDebugTarget target) {
		super(target);
		init();
	}
	
	
	private void init(){
		
		File file = new File("/home/tianchen/git/KPHP/org.phpsemantics.debug.ui/examples/config.xml");
     
        SAXBuilder jdomBuilder = new SAXBuilder();
  
        try {
			setConfiguration(jdomBuilder.build(file));
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}


	public Document getConfiguration() {
		return configuration;
	}


	public void setConfiguration(Document configuration) {
		this.configuration = configuration;
	}
	




}
