package org.phpsemantics.debug.ui;

import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

public class KPHPPerspectiveFactory implements IPerspectiveFactory{

	@Override
	public void createInitialLayout(IPageLayout layout) {
		IFolderLayout right  = layout.createFolder("right", IPageLayout.RIGHT, 0.7f, layout.getEditorArea());		
		right.addView(IKPHPUIConstant.VARIABLE_GRAPH_VIEW);
		
		IFolderLayout bottom = layout.createFolder("bottomLeft", IPageLayout.BOTTOM, 0.6f, layout.getEditorArea());
		bottom.addView(IKPHPUIConstant.CONFIG_VIEW);
		bottom.addView(IConsoleConstants.ID_CONSOLE_VIEW);

		IFolderLayout bottomRight = layout.createFolder("bottomLeft", IPageLayout.RIGHT, 0.6f, IKPHPUIConstant.CONFIG_VIEW);
		bottomRight.addView(IDebugUIConstants.ID_DEBUG_VIEW);


		IFolderLayout middle = layout.createFolder("middle", IPageLayout.RIGHT, 0.5f, layout.getEditorArea());
		middle.addView(IKPHPUIConstant.HEAP_VIEW);
		middle.addView(IDebugUIConstants.ID_VARIABLE_VIEW);
		middle.addView(IDebugUIConstants.ID_BREAKPOINT_VIEW);		
		
		layout.addActionSet(IDebugUIConstants.LAUNCH_ACTION_SET);
		layout.addActionSet(IDebugUIConstants.DEBUG_ACTION_SET);
	}

}
