package org.phpsemantics.debug.ui.views;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.DOMBuilder;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.DOMBuilder;
import org.xml.sax.SAXException;

public class ConfigurationFile {
	
	public Document jdomDocument;
	
	public ConfigurationFile(File xmlSource) throws ParserConfigurationException, SAXException, IOException{
		File file = xmlSource;
        DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		DOMBuilder jdomBuilder = new DOMBuilder();
		
		org.w3c.dom.Document doc = docBuilder.parse(file);
		jdomDocument = jdomBuilder.build(doc);
	}
	
	public Document getJdomDocument() {
		return jdomDocument;
	}

	public void setJdomDocument(Document jdomDocument) {
		this.jdomDocument = jdomDocument;
	}

	
	// TODO delete this function later 
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException{
		File file = new File("examples/config.xml");
		System.out.println(file.getAbsolutePath().toString());
		ConfigurationFile config = new ConfigurationFile(file);
		Document doc = config.getJdomDocument();
		//doc = new Document();
		Element root = doc.getRootElement();

		
		String[] value = new String[]{root.getChild("trace").getValue()};
		
		//root.getChild("instrumentation").getValue()
		
		System.out.println(root.getChild("trace").getParentElement().toString());

		
		//String[]{((Element) parentElement).getValue()
	
		//System.out.println(value[0].charAt(0));

		//System.out.println(root.getChild("trace").getChildren().size());

		System.out.println("text " + root.getChild("gc").getText());
		
		System.out.println(root.getChild("gc").getName());
		String value2 = root.getChild("gc").getValue();
		//value2 = value2.substring(2, value2.length() - 4);
		String lines[] = value2.split("\\r?\\n");
		ArrayList<String> newLines = new ArrayList<String>();
		int i = 0;
			for (String line:lines){
				lines[i]= line.trim();
				if (lines[i].length()>0)
					newLines.add(lines[i]);
				i++;
				
			}
			Object[] mama = newLines.toArray();
		System.out.println(root.getChild("gc").getValue());



	}

}
