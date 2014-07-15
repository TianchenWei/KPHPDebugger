package org.phpsemantics.debug.core.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentSkipListSet;

import org.eclipse.debug.core.model.IDebugElement;
import org.phpsemantics.debug.core.launching.IKPHPLaunchConfigurationConstants;

public class InternalScript {
	

	private final String prefix = IKPHPLaunchConfigurationConstants.INTERNAL_FILE_PREFIX;
	
	private final String suffix = IKPHPLaunchConfigurationConstants.INTERNAL_FILE_SUFFIX;
	
	private final String breakpoint = IKPHPLaunchConfigurationConstants.INTERNAL_FILE_BREAKPOINT;

	private File original;
	
	private Path internal;
	
	private final Path tempDir;
	
	private ConcurrentSkipListSet<Integer> breakpointsSet;
	
	private boolean isValid = true;
	
	public InternalScript(Path filePath, Path dirPath) throws IOException{
		
		original = filePath.toFile();
		tempDir = dirPath;
		breakpointsSet = new ConcurrentSkipListSet<Integer>();
		internal = createScript();
	}
	
	public Path getInternalScript() throws IOException{
		
		if (!isValid){
			internal = createScript();
			isValid =true;
		}
		
		return internal;
	}
	
/*	public boolean isValid(){
		if(breakpointsChanged)
			return false;
		else 
			return true;
		
	}
	*/
	private Path createScript() throws IOException{
		
		File tempFile = Files.createTempFile(tempDir,prefix, suffix).toFile();
		
		try {
			LineNumberReader lr = new LineNumberReader(
					Files.newBufferedReader(original.toPath(), Charset.defaultCharset()));
			
			BufferedWriter out = new BufferedWriter(new FileWriter(tempFile));           
			out.write(breakpoint);
			String line;
			while ((line = lr.readLine ()) != null){
				if(breakpointsSet.contains(lr.getLineNumber()))
					line = breakpoint+line; 
			
				out.write(line);
				out.newLine();    	
			}

			lr.close();
			out.close();	
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tempFile.toPath();
	}
	
	public void addBreakpoint(int number){

		breakpointsSet.add(number);
		isValid = false;
	}
	
	public void removeBreakpoint(int number){
		
		breakpointsSet.remove(number);
		isValid = false;

	}

}
