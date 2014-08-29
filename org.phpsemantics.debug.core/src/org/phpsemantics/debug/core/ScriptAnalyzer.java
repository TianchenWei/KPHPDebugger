package org.phpsemantics.debug.core;

import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * function declare should have function and functionName in the same line 
 * */

public class ScriptAnalyzer {
	
	private static final Pattern functionDecl = Pattern.compile("function\\s*(.+?)\\s*\\(\\s*(.+?)\\s*\\)\\s*\\{");
	private static final String MAIN = "main*";
	
	private HashMap<String, Integer[]> functionsMap = new HashMap <String, Integer[]>();
	private String script;
	
	/**
	 * constructor
	 * */
	public ScriptAnalyzer(String script){
		this.script = script;
	}
	
	/**
	 * build the function map for the script
	 * */
	public void initFunctionMap() throws IOException{
		ArrayList<Integer> mainLineNumbers = new ArrayList<Integer>();
		ArrayList<Integer> functionLineNumbers = new ArrayList<Integer>();

		LineNumberReader lr = new LineNumberReader(
				Files.newBufferedReader((new File(script)).toPath(), Charset.defaultCharset()));

		String line;
		String functionName = MAIN;
		boolean inFunction = false;
		
		while ((line = lr.readLine ()) != null){
			Matcher matcher = functionDecl.matcher(line);
			//enter a function
			if(matcher.find()){
				inFunction = true;
				int count = StringUtils.countMatches(matcher.group(2), "$");
				functionName = matcher.group(1)+"$"+count;
			}
			//determine which function this line belongs to
			if(line.length()>0){
				if(functionName.equals(MAIN)){
					mainLineNumbers.add(lr.getLineNumber());
				}else{
					functionLineNumbers.add(lr.getLineNumber());
				}
			}
			//leave a function
			if(inFunction && line.contains("}")){
				inFunction = false;
				Integer[] lineNumbers = 
						functionLineNumbers.toArray(new Integer[functionLineNumbers.size()]);
				functionsMap.put(functionName, lineNumbers);
				functionLineNumbers.clear();
				functionName = MAIN;
			}
		}
		lr.close();
		Integer[] lineNumbers = 
				mainLineNumbers.toArray(new Integer[functionLineNumbers.size()]);
		functionsMap.put(MAIN, lineNumbers);	
	}
	
	/**
	 * get all function calls at specified line
	 * */
	public String[] getFunctionCall(int lineNumber){
		// check if functionMap has initialized
		if(functionsMap.size()<1){
			try {
				initFunctionMap();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// get content of that line
		String line = "";
		try {
			line = getLine(lineNumber);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<String> calledNames = new ArrayList<String>();
		//check if the line contains a function
		for (String key : functionsMap.keySet()) {
			String functionName = key.substring(0,key.indexOf("$"));
			Pattern functionCall = Pattern.compile(functionName+"\\s*\\(\\s*(.+?)\\s*\\)");
			Matcher matcher = functionCall.matcher(line);
			if(matcher.find()){
				int count = StringUtils.countMatches(matcher.group(1), ",");
				if(key.equals(functionName+"$"+count)){
					calledNames.add(key);
				}
				
			}

		}
		return calledNames.toArray(new String[calledNames.size()]);
	}
	
	/**
	 * get line numbers belong to specified function
	 * */
	public ArrayList<Integer> getFunction(String functionName) throws IOException{

		LineNumberReader lr = new LineNumberReader(
					Files.newBufferedReader((new File(script)).toPath(), Charset.defaultCharset()));
		
		String line;
		ArrayList<Integer> lineNumbers = new ArrayList<Integer>();
		boolean inFunction = false;
		while ((line = lr.readLine ()) != null){
			
			if(line.contains("function")&&line.contains(functionName))
				inFunction = true;		
			
			if(inFunction && line.length()>=1)
				lineNumbers.add(lr.getLineNumber());
			
			if(inFunction && line.contains("}")){
					lr.close();
					break;
			}
		}
		return lineNumbers;
	}
	
	/**
	 * get content at specified line
	 * */
	public String getLine(int number) throws IOException{
		LineNumberReader lr = new LineNumberReader(
				Files.newBufferedReader((new File(script)).toPath(), Charset.defaultCharset()));
	
		String line;	
		while ((line = lr.readLine ()) != null){
			if(lr.getLineNumber()==number)
				return line;
		}
		lr.close();
		return null;
	}
	
	/**
	 * get line numbers belong to main function
	 * */
	public ArrayList<Integer> getMain() throws IOException{

		LineNumberReader lr = new LineNumberReader(
					Files.newBufferedReader((new File(script)).toPath(), Charset.defaultCharset()));
		
		String line;
		ArrayList<Integer> lineNumbers = new ArrayList<Integer>();
		boolean inFunction = false;
		
		while ((line = lr.readLine ()) != null){
			
			if(line.contains("function"))
				inFunction = true;		
			
			if(!inFunction && line.length()>=1)
				lineNumbers.add(lr.getLineNumber());
			
			if(line.contains("}")){
				inFunction = false;
			}
		}
		lr.close();
		return lineNumbers;
	}
	
}
