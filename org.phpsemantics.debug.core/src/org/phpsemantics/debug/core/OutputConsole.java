package org.phpsemantics.debug.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.phpsemantics.debug.core.model.KPHPDebugElement;

public class OutputConsole implements IDebugEventSetListener{
	
	private MessageConsole console;
	
	public OutputConsole(String consoleName){

		DebugPlugin.getDefault().addDebugEventListener(this);
		console = findConsole(consoleName);
	}

	/**
	 * show output of a process
	 * */
	public void showOutput(Process process) throws IOException, PartInitException{

		console.clearConsole();
		MessageConsoleStream out = console.newMessageStream();
        
		InputStreamReader ir=new
		InputStreamReader(process.getInputStream());
		           
		BufferedReader input = new BufferedReader (ir);
		           
		String line;
		           
		while ((line = input.readLine ()) != null){

			out.println(line);
	       
		}
	
	}
	
	
	/**
	 * show the IO/out of a configuration file
	 * */
	public void showOutput(File config) throws IOException, PartInitException{
		
		console.clearConsole();
		MessageConsoleStream out = console.newMessageStream();
		for(String line: ConfigAnalyser.getOutput(ConfigAnalyser.buildConfig(config))){
	        out.println(line);
		}
		out.close();
	}

	/**
	 * find the console with given name
	 * */
	public MessageConsole findConsole(String name) {
	
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];
		
		//no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[]{myConsole});
		return myConsole;
	}
	
	public void handleDebugEvents(DebugEvent[] events) {
		for (int i = 0; i < events.length; i++) {
			DebugEvent event = events[i];
			//if (event.getKind() == DebugEvent.CHANGE) {
					Object object = event.getSource();
					if(object instanceof KPHPDebugElement){
						File file = ((KPHPDebugElement) object).getCurrentConfigurationPath().toFile();
						try {
							showOutput(file);
						} catch (PartInitException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
			}
		//}
	}

}
