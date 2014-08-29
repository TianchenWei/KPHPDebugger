package org.phpsemantics.debug.core;

import java.io.IOException;

public class GraphBuilder {
	
	public GraphBuilder(){}
	/**
	 * Generate GIF image from an dot File
	 * returns the absolute path of generated graph
	 * @param dotFile absolute path
	 * */
	public static String Tgif(String dotFile) throws IOException, InterruptedException{
		String graphPath = dotFile.replace(".dot", ".gif");
		String command = "dot -Tgif "+dotFile
				+" -o "+graphPath;
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(command);
		//wait until the graph is generated
		pr.waitFor();
		return graphPath;
	}

}
