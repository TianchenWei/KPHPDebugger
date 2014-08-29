package org.phpsemantics.debug.core.launching;

/**
 * Constant definitions for PHP Semantics launch configurations.
 * <p>
 * Constant definitions only
 * </p>
 */

public interface IKPHPConstants {
	
	public static final String ID_KPHP_DEBUG_MODEL = "org.phpsemantics.debug";
	//#1
	public static final String ATTR_PROJECT_NAME = "PROJECT_NAME";
	//#2
	public static final String ATTR_FILE_TO_DEBUG = "FILE_TO_DEBUG";
	//#3
	public static final String ATTR_KTOOL_BIN = "KTOOL_BIN";
	//#4
	public static final String ATTR_KPHP_ROOT = "KPHP_ROOT";
	//#5
	public static final String ATTR_INIT_CONFIG_FILE = "INIT_CONFIG_FILE";
	
	public static final String ATTR_CONFIG_FILE = "CONFIG_FILE";

	public static final String ATTR_TEMP_DIR = "TEMP_DIR";
	
	public static final String DEFAULT_INIT_CONFIG_FILE = "init-config.xml";
	
	//only valid for ubuntu now
	public static final String DEFAULT_KPHP_ROOT = "/home/tianchen/kphp.git/src";
	
	public static final String EXAMPLE_PHP = "/examples/hello-world/hello-world.php";
	
	public static final String DEFAULT_TEMP_DIR = "/tmp/kphp";

	public static final String BREAKPOINT_MARKER = "breakpoint()";
	
	public static final String CONFIG_FILE_PREFIX = "Config_";

	public static final String CONFIG_FILE_SUFFIX = ".xml";

	public static final String INTERNAL_FILE_PREFIX = "Internal_";

	public static final String INTERNAL_FILE_SUFFIX = ".php";
	
	public static final String SCRIPT_SUFFIX = ".php";

	public static final String INTERNAL_FILE_BREAKPOINT_L = "breakpoint(";
	public static final String INTERNAL_FILE_BREAKPOINT_R = ");";
	public static final String CONSOLE_NAME = "KPHP";


}
