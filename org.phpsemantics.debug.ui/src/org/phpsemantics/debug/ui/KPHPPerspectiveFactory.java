package org.phpsemantics.debug.ui;

import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class KPHPPerspectiveFactory implements IPerspectiveFactory{

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.createFolder("left", IPageLayout.LEFT, 0.2f, layout.getEditorArea());		
		layout.createFolder("right", IPageLayout.RIGHT, 0.6f, layout.getEditorArea());		
		layout.createFolder("bottom", IPageLayout.BOTTOM, 0.6f, layout.getEditorArea());
		layout.createFolder("top", IPageLayout.TOP, 0.6f, layout.getEditorArea());
		

		layout.addActionSet(IDebugUIConstants.LAUNCH_ACTION_SET);
		layout.addActionSet(IDebugUIConstants.DEBUG_ACTION_SET);
	}

}
