package org.phpsemantics.debug.ui.launching;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.WorkingDirectoryBlock;
import org.phpsemantics.debug.core.launching.IKPHPLaunchConfigurationConstants;

public class KPHPWorkingDirectory extends WorkingDirectoryBlock{

	protected KPHPWorkingDirectory() {
		super(IKPHPLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IProject getProject(ILaunchConfiguration configuration)
			throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

}
