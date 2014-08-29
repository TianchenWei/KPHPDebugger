package org.phpsemantics.debug.core.launching;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.phpsemantics.debug.core.model.KPHPDebugTarget;

public class KPHPLaunchDelegate implements ILaunchConfigurationDelegate{

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {

		//String ktool = configuration.getAttribute(
		//		IKPHPConstants.ATTR_KTOOL_BIN, "");
		String kphp = configuration.getAttribute(
				IKPHPConstants.ATTR_KPHP_ROOT, 
				IKPHPConstants.DEFAULT_KPHP_ROOT);
		String init = configuration.getAttribute(
				IKPHPConstants.ATTR_INIT_CONFIG_FILE,
				IKPHPConstants.DEFAULT_INIT_CONFIG_FILE);
		
		KPHPInterpreter interpreter = new KPHPInterpreter(kphp, init);
		
		String file = configuration.getAttribute(
				IKPHPConstants.ATTR_FILE_TO_DEBUG,
				kphp+IKPHPConstants.EXAMPLE_PHP);
		
		String tempDir = configuration.getAttribute(
				IKPHPConstants.ATTR_TEMP_DIR, 
				IKPHPConstants.DEFAULT_TEMP_DIR);
		
		File temp = new File(tempDir);
		if(!temp.isDirectory())
			temp = new File(IKPHPConstants.DEFAULT_TEMP_DIR);
		
		temp.mkdir();
					
		if(mode.equals(ILaunchManager.RUN_MODE)){
			
			try {
				showOutputInConsole(interpreter.run(file), "KPHP");
			} catch (IOException e) {
				e.printStackTrace();
			}
	
		}
		
	
		if(mode.equals(ILaunchManager.DEBUG_MODE)){
				
			KPHPDebugTarget target = new KPHPDebugTarget(launch, interpreter, new String[]{tempDir, file});
			launch.addDebugTarget(target);
			
		}
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
