package org.phpsemantics.debug.core.launching;

import java.io.File;
import java.io.IOException;
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
import org.phpsemantics.debug.core.model.KPHPDebugTarget;

public class FakeLaunchDelegate implements ILaunchConfigurationDelegate{

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

			
			
			IDebugTarget target;

				target = new KPHPDebugTarget(launch);
				launch.addDebugTarget(target);

			
		}
	}

}
