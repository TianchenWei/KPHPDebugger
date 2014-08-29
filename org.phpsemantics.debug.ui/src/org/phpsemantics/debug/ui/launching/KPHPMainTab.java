package org.phpsemantics.debug.ui.launching;


import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.WorkingDirectoryBlock;
import org.eclipse.php.internal.debug.ui.launching.LaunchUtil;
import org.eclipse.php.internal.debug.ui.launching.LaunchUtilities;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.phpsemantics.debug.core.launching.IKPHPConstants;
import org.phpsemantics.debug.ui.ControlAccessibleListener;
import org.phpsemantics.debug.ui.Messages;
import org.phpsemantics.debug.ui.SWTFactory;

@SuppressWarnings("restriction")
public class KPHPMainTab extends AbstractLaunchConfigurationTab {

	// Working directory
	//protected WorkingDirectoryBlock fWorkingDirectoryBlock;
	
	/**
	 * Creates a control to specify a working directory.
	 * 
	 * @return the new {@link WorkingDirectoryBlock}
	 * @since 3.4
	 */
	/*protected WorkingDirectoryBlock createWorkingDirBlock() {
		return new KPHPWorkingDirectory();
	}*/
	
	private Text fKToolText;
	private Text fKPHPText;
	private Text fPHPFileText;

	private Button fKToolButton;
	private Button fKPHPButton;
	private Button fPHPFileButton;

	private WidgetListener fListener = new WidgetListener();
	

	private class WidgetListener implements ModifyListener, SelectionListener {
		
		public void modifyText(ModifyEvent e) {
			updateLaunchConfigurationDialog();

		}
		
		public void widgetDefaultSelected(SelectionEvent e) {/*do nothing*/}
		
		public void widgetSelected(SelectionEvent e) {
			Object source = e.getSource();
			
			if (source == fKToolButton) 
				handleChangeKPath(fKToolText);				
			else if(source == fKPHPButton) 
				handleChangeKPHProot(fKPHPText);		
			else if (source == fPHPFileButton) 
				handleChangeFileToDebug(fPHPFileText);
			else 
				updateLaunchConfigurationDialog();
			
		}
	}

	public KPHPMainTab(){
	//	fWorkingDirectoryBlock = createWorkingDirBlock();

	}
	
	@Override
	public void createControl(Composite parent) {
		Composite comp = SWTFactory.createComposite(parent);
		
		createKPHPEditor(comp);
		createKToolEditor(comp);
		createScriptEditor(comp);

	//	fWorkingDirectoryBlock.createControl(comp);	
		
		setControl(comp);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), getHelpContextId());

	}
		
	private void createKToolEditor(Composite parent) {
		
		Group group = SWTFactory.createGroup(parent,Messages.SemanticsToolsTab_KTool, 3);
		fKToolText = SWTFactory.createText(group,2);
		fKToolText.addModifyListener(fListener);
		ControlAccessibleListener.addListener(fKToolText, group.getText());
		
		fKToolButton = createPushButton(group, Messages.SemanticsToolsTab_1, null); 
		fKToolButton.addSelectionListener(fListener);
	}

	
	private void createKPHPEditor(Composite parent) {
		
		Group group = SWTFactory.createGroup(parent,Messages.SemanticsToolsTab_KPHP, 3);

		fKPHPText = SWTFactory.createText(group,2);
		fKPHPText.addModifyListener(fListener);
		ControlAccessibleListener.addListener(fKPHPText, group.getText());
		
		fKPHPButton = createPushButton(group, Messages.SemanticsToolsTab_1, null); 
		fKPHPButton.addSelectionListener(fListener);
      
	}
	/**
	 * Creates the controls needed to edit the argument and prompt for argument
	 * attributes of an external tool
	 * 
	 * @param parent
	 *            the composite to create the controls in
	 */
	private void createScriptEditor(Composite parent) {
		
		Group group = SWTFactory.createGroup(parent,Messages.PHP_File, 3);

		fPHPFileText = SWTFactory.createText(group,2);
		fPHPFileText.addModifyListener(fListener);
		ControlAccessibleListener.addListener(fPHPFileText, group.getText());
		
		fPHPFileButton = createPushButton(group, Messages.SemanticsToolsTab_1, null);
		fPHPFileButton.addSelectionListener(fListener);
		//ControlAccessibleListener.addListener(fPHPFileButton,
		//		fPHPFileButton.getText()); // need to strip the
													// mnemonic from buttons
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		
		//IPath path = Utilities.searchActiveScript();
		//IPath path = null;
		//configuration.setAttribute(IKPHPConstants.ATTR_FILE_TO_DEBUG, "");	
		//configuration.setAttribute(IKPHPConstants.ATTR_KTOOL_BIN, "");
		//configuration.setAttribute(IKPHPConstants.ATTR_KPHP_ROOT, "");
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		
		try {
			fKPHPText.setText(
					configuration.getAttribute(
							IKPHPConstants.ATTR_KPHP_ROOT,"")); //$NON-NLS-1$
			fKToolText.setText(
					configuration.getAttribute(
							IKPHPConstants.ATTR_KTOOL_BIN, "")); //$NON-NLS-1$
			fPHPFileText.setText(
					configuration.getAttribute(
							IKPHPConstants.ATTR_FILE_TO_DEBUG, "")); //$NON-NLS-1$
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
	
		if((new File(fKPHPText.getText())).isDirectory())
			configuration.setAttribute(
					IKPHPConstants.ATTR_KPHP_ROOT, fKPHPText.getText());
		if((new File(fKToolText.getText())).isDirectory())

			configuration.setAttribute(
					IKPHPConstants.ATTR_KTOOL_BIN, fKToolText.getText());
		if((new File(fPHPFileText.getText())).isFile())

			configuration.setAttribute(
					IKPHPConstants.ATTR_FILE_TO_DEBUG, fPHPFileText.getText());
	}

	@Override
	public String getName() {

		return Messages.SemanticsToolsTab_0;

	}
	
	/**
	 * A callback method when changing the file to debug via 'Browse'
	 */
	private void handleChangeFileToDebug(final Text textField) {

		
		final IResource resource = LaunchUtilities.getFileFromDialog(null,
				getShell(), LaunchUtil.getFileExtensions(),
				LaunchUtil.getRequiredNatures(), true);
		
		if (resource instanceof IFile) {
			textField.setText(resource.getFullPath().toString());

			String fileLocation = ""; //$NON-NLS-1$
			
			IPath location = resource.getLocation();
			System.out.println(resource.getFullPath().toString());

			if (location != null) {
				fileLocation = location.toOSString();
				textField.setText(fileLocation);
				
			} else {
				fileLocation = resource.getFullPath().toString();
			}
			textField.setData(fileLocation);
		}
	}
	
	/**
	 * A callback method when changing the path of 
	 * k directory which contains the 'bin' folder via 'Browse'
	 */
	private void handleChangeKPath(Text textField){
		
		String selectedDirectory = textField.getText();
		DirectoryDialog dialog = new DirectoryDialog(getShell());
		dialog.setMessage(Messages.SemanticsToolsTab_KTool_Prompt);         	
		selectedDirectory = dialog.open();
		if (selectedDirectory != null) {
			textField.setText(selectedDirectory);
		}	
	}
	
	/**
	 * A callback method when changing the path of 
	 * kphp directory which contains the 'scripts' folder via 'Browse'
	 */
	private void handleChangeKPHProot(Text textField){
		
		String selectedDirectory = textField.getText();
		DirectoryDialog dialog = new DirectoryDialog(getShell());
		dialog.setMessage(Messages.SemanticsToolsTab_KPHP_Prompt);         	
		selectedDirectory = dialog.open();
		if (selectedDirectory != null) {
			textField.setText(selectedDirectory);
		}
	}


}
