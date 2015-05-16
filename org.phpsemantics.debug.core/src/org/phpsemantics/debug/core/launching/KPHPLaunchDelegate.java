package org.phpsemantics.debug.core.launching;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.phpsemantics.debug.core.OutputConsole;
import org.phpsemantics.debug.core.model.KPHPDebugTarget;

public class KPHPLaunchDelegate implements ILaunchConfigurationDelegate{

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate
	 * #launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor);
	 */
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
		
		/*String tempDir = configuration.getAttribute(
				IKPHPConstants.ATTR_TEMP_DIR, 
				IKPHPConstants.DEFAULT_TEMP_DIR);*/
		String tempDir = configuration.getAttribute(
				IKPHPConstants.ATTR_TEMP_DIR, 
				"");
		File temp = new File(tempDir);
		if(!temp.isDirectory())
			temp = (new File(file)).getParentFile();
	
		if(!temp.isDirectory())
			temp = new File(IKPHPConstants.DEFAULT_TEMP_DIR);
		
		if(!temp.exists()){
			temp.mkdir();
		}
		
		tempDir = temp.getAbsolutePath();
		
		OutputConsole out = new OutputConsole(IKPHPConstants.CONSOLE_NAME);

					
		if(mode.equals(ILaunchManager.RUN_MODE)){
			
			try {
				File config = new File(file.replace(IKPHPConstants.SCRIPT_SUFFIX, IKPHPConstants.CONFIG_FILE_SUFFIX));
				interpreter.writeOutputToFile(interpreter.run(file), config);
				out.showOutput(config);
				//showOutputInConsole(interpreter.run(file), "KPHP-config");
			} catch (IOException e) {
				e.printStackTrace();
			}
	
		}
		
		
		
	
		if(mode.equals(ILaunchManager.DEBUG_MODE)){
				
			KPHPDebugTarget target = new KPHPDebugTarget(launch, interpreter, new String[]{tempDir, file});
			launch.addDebugTarget(target);
			
		}
	}
	


}
