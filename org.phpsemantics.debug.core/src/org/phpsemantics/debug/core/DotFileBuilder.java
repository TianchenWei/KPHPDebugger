package org.phpsemantics.debug.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.jdom2.Document;
import org.phpsemantics.debug.core.ConfigAnalyser;


public class DotFileBuilder{
	//symLoc
	private static final int LOCATION_ID = 0;
	private static final int VALUE = 1;
	private static final int TYPE = 2;
	private static final int REF_COUNT = 3;
	//private static final int IS_REF = 4;
	
	//listItem
	public static final int NAME = 0;
	public static final int VISIBILITY = 1;
	public static final int LOCATION = 2;
	
	private static final String CHARTREUSE = "chartreuse"; 
	private static final String GREY = "grey"; 

	
	private Document configuration;
	
	//default fontSize for graph is 12
	private String fontSize = "12";
	
	private String color = CHARTREUSE;

	private ArrayList<String> links = new ArrayList<String>();
	
	/**
	 * constructor
	 */
	public DotFileBuilder() {
	}
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public Document getConfiguration() {
		return configuration;
	}
	
	public void setConfiguration(Document configuration) {
		this.configuration = configuration;
	}
	
	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}
	
	public String writeDotFile(File file, int startSymLoc) throws FileNotFoundException{
		links.clear();
		color = CHARTREUSE;
		if(configuration!=null){
			PrintWriter writer = new PrintWriter(file);
			writer.println("digraph example{");
			writer.println("node [shape = plaintext fontsize = "+fontSize+"];");
			String symLoc = ConfigAnalyser.getSymLocAt(configuration, startSymLoc);
			writeNode(writer,symLoc);
			writeLinks(writer);
			writer.println("}");
			writer.close();	
			return file.getAbsolutePath();
		}
		return null;
	}
	
	public String writeDotFile(int startSymLoc) throws FileNotFoundException{
		String path = configuration.getBaseURI().replace("file:", "");
		File file = new File(path.replace(".xml", "symLoc"+startSymLoc+".dot"));
		return writeDotFile(file, startSymLoc);
	}
	
	private void writeNode(PrintWriter writer, String symLoc){
		int port = 1;
		ArrayList<String> lines = new ArrayList<String>();
		String[] attributes = ConfigAnalyser.isArrayVarible(symLoc)?
				ConfigAnalyser.getArrayValue(symLoc)
				:ConfigAnalyser.getScalarValue(symLoc);
		lines.add("symLoc"+attributes[LOCATION_ID]
				+"[color="+color+" label=<<TABLE>");
		color = GREY;
		lines.add("<TR>");
		lines.add("<TD PORT=\""+port+"\" >"
				+attributes[LOCATION_ID]
						+"</TD>");
		port++;//increase port id
		lines.add("<TD BGCOLOR=\"bisque\">"
				+attributes[TYPE]
						+"</TD>");
		lines.add("</TR>");
		lines.add("<TR><TD BGCOLOR=\"bisque\" COLSPAN=\"2\">RC="
				+attributes[REF_COUNT]
						+"</TD></TR>");
		if(ConfigAnalyser.hasVariables(symLoc)){
			String[] listItems = ConfigAnalyser.getArrayListItemsAt(symLoc);
			for(String listItem: listItems){
				String itemAttr[] = ConfigAnalyser.getVariable(listItem);
				String itemSymLoc = ConfigAnalyser.getSymLocAt(configuration, Integer.parseInt(itemAttr[LOCATION]));
				writeNode(writer, itemSymLoc);
				lines.add("<TR><TD PORT=\""+port+"\" COLSPAN=\"2\">"+itemAttr[NAME]+"</TD></TR>");
				links.add("symLoc"+attributes[LOCATION_ID]+":"+port
						+"->"+"symLoc"+itemAttr[LOCATION]+":1;");
				port++;
			}
			
		}else{
			lines.add("<TR><TD COLSPAN=\"2\">"
					+attributes[VALUE]
							+"</TD></TR>");
		}
		lines.add("</TABLE>>];");

		//print
		for(String line: lines){
			writer.println(line);
		}
		
	}
	
	private void writeLinks(PrintWriter writer){
		for(String link:links){
			writer.println(link);
		}
	}

}
