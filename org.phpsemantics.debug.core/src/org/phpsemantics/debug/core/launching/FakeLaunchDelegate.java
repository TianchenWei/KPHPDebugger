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
import org.phpsemantics.debug.core.KPHPInterpreter;
import org.phpsemantics.debug.core.model.KPHPDebugTarget;

public class FakeLaunchDelegate implements ILaunchConfigurationDelegate{

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
				
		String ktool = configuration.getAttribute(
				IKPHPLaunchConfigurationConstants.ATTR_KTOOL_BIN, "");
		String kphp = configuration.getAttribute(
				IKPHPLaunchConfigurationConstants.ATTR_KPHP_ROOT, "");
		
		KPHPInterpreter ki = new KPHPInterpreter(ktool, kphp);
		
	
		if(mode.equals(ILaunchManager.RUN_MODE)){
		
		String file = configuration.getAttribute(
				IKPHPLaunchConfigurationConstants.ATTR_FILE_TO_DEBUG, "");
		String tempDir =configuration.getAttribute(
				IKPHPLaunchConfigurationConstants.ATTR_TEMP_DIR, 
						IKPHPLaunchConfigurationConstants.DEFAULT_TEMP_DIR);
	
		}
		
	
		if(mode.equals(ILaunchManager.DEBUG_MODE)){
				
			IDebugTarget target;

			target = new KPHPDebugTarget(launch, ki);
			launch.addDebugTarget(target);

			
		}
	}

}
