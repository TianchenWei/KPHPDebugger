package org.phpsemantics.debug.core.launching;

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
	
	// KPHP interpreter directory
	private final File kphp_dir;
	// Absolute path of init-config.xml file
	private final String initConfig;

	/**
	 * constructor
	 * */
	public KPHPInterpreter(String kphp, String init){
		kphp_dir = new File(kphp).getAbsoluteFile();
		initConfig = init;
	}	
	
	public File getKphp_dir() {
		return kphp_dir;
	}
	
	public String getInitConfig() {
		return initConfig;
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
	public Process debug(String file){
		
		Process process = null;
		
		List<String> commandList = new ArrayList<String>();	
		
		commandList.add("scripts/kphp");
		commandList.add("run");
		commandList.add(initConfig);
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
	
	/**
	 *  scripts/kphp resume config.xml --config 
	 **/
	public Process resume(String config){
		Process process = null;
		
		List<String> commandList = new ArrayList<String>();	
		
		commandList.add("scripts/kphp");
		commandList.add("resume");
		commandList.add(config);
		commandList.add("--config");	
		String[] commandLine = (String[]) commandList.toArray(new String[commandList.size()]);

		try {
			process = DebugPlugin.exec(commandLine, kphp_dir);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
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
