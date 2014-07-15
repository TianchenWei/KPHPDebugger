package org.phpsemantics.debug.core.launching;

/**
 * Constant definitions for PHP Semantics launch configurations.
 * <p>
 * Constant definitions only
 * </p>
 */

public interface IKPHPLaunchConfigurationConstants {
	
	/**
	 * Launch configuration attribute key. The value is a name of
	 * a PHP project associated with a PHP Semantics launch configuration.
	 */
	public static final String ATTR_PROJECT_NAME = "";
	
	/**
	 * Launch configuration attribute key. The value is a fully qualified name
	 * of a main type to launch.
	 */
	public static final String ATTR_MAIN_TYPE_NAME = "";	 //$NON-NLS-1$
	
	/**
	 * Launch configuration attribute key. The value is a string specifying a
	 * path to the working directory to use when launching a local VM.
	 * When specified as an absolute path, the path represents a path in the local
	 * file system. When specified as a full path, the path represents a workspace
	 * relative path. When unspecified, the working directory defaults to the project
	 * associated with a launch configuration. When no project is associated with a
	 * launch configuration, the working directory is inherited from the current
	 * process.
	 */
	public static final String ATTR_WORKING_DIRECTORY = ".WORKING_DIRECTORY";	 //$NON-NLS-1$	
		
	/**
	 * Launch configuration attribute key. 
	 */
	public static final String ATTR_KTOOL_BIN = "KTOOL_BIN";
	
	/**
	 * Launch configuration attribute key. 
	 */
	public static final String ATTR_KPHP_ROOT = "KPHP_ROOT";
	
	/**
	 * Launch configuration attribute key. 
	 */
	public static final String ATTR_FILE_TO_DEBUG = "FILE_TO_DEBUG";
	
	/**
	 * Launch configuration attribute key. 
	 */
	public static final String ATTR_INIT_CONFIG_FILE = "INIT_CONFIG_FILE";

	public static final String ATTR_FILE_NAME = "FILE_NAME";

	public static final String ID_KPHP_DEBUG_MODEL = "org.phpsemantics.debug";

	public static final String BREAKPOINT_MARKER = "breakpoint()";

	public static final String ATTR_TEMP_DIR = "TEMP_DIR";
	
	public static final String DEFAULT_TEMP_DIR = "/home/tianchen";

	public static final String INTERNAL_FILE_PREFIX = "Internal_";

	public static final String INTERNAL_FILE_SUFFIX = ".php";

	public static final String INTERNAL_FILE_BREAKPOINT = "breakpoint();";





}
