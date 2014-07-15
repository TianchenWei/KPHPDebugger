package org.phpsemantics.debug.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;


public class KPHPInterpreter {
	
	private String ktool;
	private File kphp_dir;
	
	public KPHPInterpreter(String ktool, String kphp){
		
		kphp_dir = new File(kphp).getAbsoluteFile();
		this.ktool = ktool;
	}
	
	/**  scripts/kphp example.php --config
	 *   scripts/kphp example.php
	 **/
	public Process run(String file){
		
		Process process = null;
		
		List<String> commandList = new ArrayList<String>();		
		
		commandList.add("scripts/kphp");
		commandList.add(file);
		commandList.add("--config");
		String[] commandLine = (String[]) commandList.toArray(new String[commandList.size()]);

		try {
			process = DebugPlugin.exec(commandLine, kphp_dir);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return process;
	}
	/**  scripts/kphp run init-config.xml example.php --config
	 * @param file:  
	 **/
	public Process debug(String init, String file){
		
		Process process = null;
		
		List<String> commandList = new ArrayList<String>();	
		
		commandList.add("scripts/kphp");
		commandList.add("run");
		commandList.add("init-config.xml");
		commandList.add(file);
		commandList.add("--config");	
		String[] commandLine = (String[]) commandList.toArray(new String[commandList.size()]);

		try {
			process = DebugPlugin.exec(commandLine, kphp_dir);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return process;	
	}
	
	public Process resume(String file, String tempDir){
		Process process = null;
		return process;
	}

	
	public void writeOutputToFile(Process process, File file) throws IOException{
		
		InputStreamReader ir=new
		InputStreamReader(process.getInputStream());
				           
		BufferedReader input = new BufferedReader (ir);
		BufferedWriter out = new BufferedWriter(new FileWriter(file));           
		
		String line;		           
		while ((line = input.readLine ()) != null){

			out.write(line);
			out.newLine();    
			
		}
		
		out.close();

	}
	
	public void printOutput(Process process) throws IOException{
		InputStreamReader ir=new
		InputStreamReader(process.getInputStream());
						           
		BufferedReader input = new BufferedReader (ir);
				
		String line;		           
		while ((line = input.readLine ()) != null){
			System.out.println(line);
		}
	}
				
}
