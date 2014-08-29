package org.phpsemantics.debug.ui;


import org.eclipse.osgi.util.NLS;

public class Messages extends NLS{
	
	private static final String BUNDLE_NAME = "org.phpsemantics.debug.ui.Messages";//$NON-NLS-1$
	
	public static String SemanticsToolsTab_0;

	public static String SemanticsToolsTab_1;

/*	public static String SemanticsToolsTab_KTool;
	
	public static String SemanticsToolsTab_KTool_Prompt;*/
	
	public static String SemanticsToolsTab_TempDir;
	
	public static String SemanticsToolsTab_TempDir_Prompt;

	public static String SemanticsToolsTab_KPHP;

	public static String SemanticsToolsTab_KPHP_Prompt;

	public static String PHP_File;

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

}
