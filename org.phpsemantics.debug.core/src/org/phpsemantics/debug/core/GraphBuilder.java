package org.phpsemantics.debug.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;

public class GraphBuilder {

	
	public GraphBuilder(Path dir){
	}
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
	
	//mac wrapper
	public static String Tgif2(String dotFile){
		File classPath= new File( GraphBuilder.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		Path path = classPath.toPath();
	
		while(path.getParent()!=null && !path.endsWith("dropins")){
			path = path.getParent();	
		}
		if(path.endsWith("dropins")){
			File tools_dir = path.resolve("tools").toFile();
			String graphPath = dotFile.replace(".dot", ".gif");
			List<String> commandList = new ArrayList<String>();	
			commandList.add("./makeGraph");
			commandList.add(dotFile);
			commandList.add(graphPath);	
			String[] commandLine = (String[]) commandList.toArray(new String[commandList.size()]);

			try {
				Process process = DebugPlugin.exec(commandLine, tools_dir);
				process.waitFor();
			} catch (CoreException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return graphPath;
		}
		
		return null;
	
	}

}
