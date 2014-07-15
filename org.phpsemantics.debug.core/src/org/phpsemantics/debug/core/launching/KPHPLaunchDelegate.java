/**
 *       
 * [run mode]:
 * 	dir:kphp/src
 * 	command: kphp run initial-config.xml .php
 * 			 kphp .php
 * 
 * [debug mode] 
 * 	dir:kphp/src
 * 	       
 */
package org.phpsemantics.debug.core.launching;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.console.actions.ClearOutputAction;
import org.phpsemantics.debug.core.model.KPHPDebugTarget;

/**
 * @author tianchen
 *
 */
public class KPHPLaunchDelegate implements ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		
		List<String> commandList = new ArrayList();		
		String ktool = null;
		String kphp = null;
		String file = null;
		String tempDir = null;
		
		try{
			ktool = configuration.getAttribute(
					IKPHPLaunchConfigurationConstants.ATTR_KTOOL_BIN, "");
			kphp = configuration.getAttribute(
					IKPHPLaunchConfigurationConstants.ATTR_KPHP_ROOT, "");
			file = configuration.getAttribute(
					IKPHPLaunchConfigurationConstants.ATTR_FILE_TO_DEBUG, "");
			tempDir =configuration.getAttribute(
					IKPHPLaunchConfigurationConstants.ATTR_TEMP_DIR, 
							IKPHPLaunchConfigurationConstants.DEFAULT_TEMP_DIR);
		
		}catch(CoreException e){
			
		}
		
		commandList.add("scripts/kphp");
		
		//executable directory
		File dir = new File(kphp).getAbsoluteFile();
		
		/**  scripts/kphp example.php --config
		 *   scripts/kphp example.php
		 **/
		if(mode.equals(ILaunchManager.RUN_MODE)){
	
			commandList.add(file);
			commandList.add("--config");
			String[] commandLine = (String[]) commandList.toArray(new String[commandList.size()]);

			try {
				Process process = DebugPlugin.exec(commandLine, dir);
				//showOutputInConsole(process,"KPHP");
				File configFile = new File(file + "_config.xml");
				writeToFile(process,configFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		
		/**  scripts/kphp run init-config.xml example.php --config 
		 **/
		if(mode.equals(ILaunchManager.DEBUG_MODE)){
			commandList.add("run");
			// TODO allow users to specify config file
			commandList.add("init-config.xml");
			commandList.add(file);
			commandList.add("--config");

			String[] commandLine = (String[]) commandList.toArray(new String[commandList.size()]);
			Process process = DebugPlugin.exec(commandLine, dir);
			
			try {
				File configFile = new File(file + "_config.xml");
				writeToFile(process, configFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			IDebugTarget target;
		
				target = new KPHPDebugTarget(launch);
				launch.addDebugTarget(target);
		}
			
	}
	
	public void writeToFile(Process process, File file) throws IOException{
		
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
	public void validateKPath(String path){
		// TODO
	}
	public boolean isValidKPath(String path){
		// TODO
		return true;
	}
	public boolean isVaildKPHP(String path){
		// TODO
		return true;
	} 

	public void showOutputInConsole(Process process, String consoleName) throws IOException, PartInitException{

		MessageConsole myConsole = findConsole(consoleName);
		myConsole.clearConsole();
		MessageConsoleStream out = myConsole.newMessageStream();
        
		InputStreamReader ir=new
		InputStreamReader(process.getInputStream());
		           
		BufferedReader input = new BufferedReader (ir);
		           
		String line;
		           
		while ((line = input.readLine ()) != null){

			out.println(line);
	       
		}
	
	}

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
	

}
