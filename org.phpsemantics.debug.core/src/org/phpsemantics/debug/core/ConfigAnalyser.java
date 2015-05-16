package org.phpsemantics.debug.core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ConfigAnalyser {
	public static final Pattern symLocLabel = Pattern.compile("#symLoc\\((\\d+)\\)");
	public static final	Pattern ListItem = Pattern.compile("ListItem\\((.+?)\\)");  
	public static final	Pattern arrayListItem = Pattern.compile("ListItem\\(\\[(.+?),(.+?),#symLoc\\((\\d+)\\)\\]\\)");  
	//public static final Pattern array = Pattern.compile("Array\\(\\[(.+?),(.+?)\\],(.+?)\\)");
	public static final	Pattern symLocTypeArray = Pattern.compile("#symLoc\\((\\d+)\\)\\|->zval\\((.+?),array,(\\d+),(true|false)\\)");  
	public static final	Pattern symLocTypeScalar = Pattern.compile("#symLoc\\((\\d+)\\)\\|->zval\\((.+?),(.+?),(\\d+),(true|false)\\)");  
	public static final	Pattern symLoc = Pattern.compile("#symLoc\\((\\d+)\\)\\|->zval\\((.+?),(.+?),(\\d+),(true|false)\\)");  
	public static final	Pattern breakpoint = Pattern.compile("BREAKPOINT\\((\\d+)\\)");  

	public static Document buildConfig(File configuration){
		SAXBuilder jdomBuilder = new SAXBuilder();
		Document doc;
		try {
			doc = jdomBuilder.build(configuration);
			return doc;
		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * check whether it is a valid configuration
	 * @param configuration 
	 * */
	@SuppressWarnings("unused")
	private static boolean isValid(Document configuration){
	//TODO this is not complete way to ensure the Document is 
		if(configuration.getRootElement() != null && 
				configuration.getRootElement().getName().contains("kphp")){
			return true;
		}
		return false;
	}
	
	/**
	 * return true if a leaf element under a sequence of tags
	 * has no content
	 * @param configuration
	 * @param tags 
	 * */
	public static boolean isCellEmpty(Document configuration, String[] tags){
		Element element = getElement(configuration, tags);
		if(isLeaf(element)){
			String[] value = getTrimValue(element);
			if(value[0].charAt(0)=='.'){
				return true;
			}
		}
		return false;
	}
	public static int getLineNumber(Document configuration){
		Element element = getElement(configuration, new String[]{"script","k"});
		String value = clean(element.getValue());
		Matcher matcher = breakpoint.matcher(value);
		if(matcher.find()){
			return Integer.parseInt(matcher.group(1));
		}
		return -1;
	}

	
	/**
	 * returns true if k cell is empty
	 * @param configuration
	 * */
	public static boolean isKCellEmpty(Document configuration){
		
		return isCellEmpty(configuration, new String[]{"script","k"});
	}
	
	public static boolean isSymLoc(String input){
		Matcher matcher = symLoc.matcher(input);
		return matcher.find();	
	}
	
	public static boolean isArrayVarible(String symLoc){
		if(symLoc.matches(symLocTypeArray.toString())){
			return true;
		}
		return false;
	}
	
	public static boolean isEmptyArray(String[] arrayValue){
		if(arrayValue[1].equals("Array(none,.List)"))
			return true;
		return false;
	}
	
	public static boolean isArrayListItem(String input){
		Matcher matcher = arrayListItem.matcher(input);
		return matcher.find();
	}
	
	/**
	 * return symLoc id of the first #symLoc()
	 * @param symLoc: any string
	 * 
	 * */
	public static int getSymLocID(String symLoc){
		Matcher matcher = symLocLabel.matcher(symLoc);
		return matcher.find()? Integer.parseInt(matcher.group(1)):null;
	}
		
	/**
	 * delete all line separators and whitespace
	 * to match defined regular expressions
	 * @param string: string needs to be cleaned
	 * */
	public static String clean(String string){
		String regex = "\\r\\n|\\r|\\n|\\s*";
		return string.replaceAll(regex, "");
	}
	
	/**
	 * disassemble value of the element into meaningful parts
	 * @param leaf: the input element should have no child, 
	 * otherwise return null
	 * */
	public static String[] getTrimValue(Element leaf){
		if(isLeaf(leaf)){
			String raw = leaf.getValue();
			String[] items = raw.split("\\n\\s*");
			List<String> value = new ArrayList<String>();
			for (String item: items){
				//filter out "" item
				if(!item.isEmpty()){
					value.add(item);
				}
			}
			return value.toArray(new String[value.size()]);
		}
		return null;
	}
	
	private static Element getElement(Document configuration, String[] tags){
		Element[] elements = new Element[tags.length+1];
		if(configuration.hasRootElement()){
			elements[0] = configuration.getRootElement();
		}else{
			return null;
		}
		
		for(int i = 1; i<=tags.length; i++){
				elements[i] = elements[i-1].getChild(tags[i-1]);
				if (elements[i]== null)
					return null;
		}
		
		return elements[tags.length];	
	}
	
	/**
	 * an element is a leaf if it has no child
	 * @param element
	 * */
	private static boolean isLeaf(Element element){
		if(element.getChildren().size() == 0)
			return true;
		else 
			return false;
	}
	
	public static String[] getOutput(Document configuration){
		ArrayList<String> output = new ArrayList<String>();
		Element element = getElement(configuration, new String[]{"IO","out"});
		String value = element.getValue();
		Pattern ListItem = Pattern.compile("ListItem\\(\"(.+?)\"\\)");  
		Matcher matcher = ListItem.matcher(value);
		while(matcher.find()){
			//if(matcher.group(1).equals("\n"))
			output.add(matcher.group(1));
		}
		return output.toArray(new String[output.size()]);
	}

	/**
	 * returns the location of global static scope
	 * @param configuration
	 * */
	//TODO
	public static String getGlobalScope(Document configuration){
		Element element = getElement(configuration, new String[]{"scopes","globalScope"});
		return clean(element.getValue());
	}
	/**
	 * returns the location of global static scope
	 * @param configuration
	 * */
	//TODO
	public static String getSuperGlobalScope(Document configuration){
		Element element = getElement(configuration, new String[]{"scopes","superGlobalScope"});
		return clean(element.getValue());
	}
	/**
	 * returns the location of global static scope
	 * @param configuration
	 * */
	//TODO
	public static String getGlobalStaticScope(Document configuration){
		Element element = getElement(configuration, new String[]{"scopes","globalStaticScope"});
		return clean(element.getValue());
	}
	
	/**
	 * returns the location of current scope
	 * @param configuration
	 * */
	public static String getCurrentScope(Document configuration){
		Element element = getElement(configuration, new String[]{"scopes","currentScope"});
		return clean(element.getValue());
	}
	
	/**
	 * returns value at symLoc(i) on the heap without 
	 * line separators and whitespace
	 * which is something like: 
	 *  #symLoc(2)|->zval(Array(none,.List),array,1,false)
	 *  @param configuration
	 *  @param i: symLoc(i)
	 * */
/*	public static String getSymLocAt(Document configuration, int i){
		String value = getElement(configuration, new String[]{"heap"}).getValue();
		int beginIndex = value.indexOf("#symLoc("+i+") |->");
		value = value.substring(beginIndex).split("\\n\\s*#",2)[0];
	    return  clean(value);
	}
	*/
	public static String getSymLocAt(Document configuration, int i){
		String value = getElement(configuration, new String[]{"heap"}).getValue();
		value = clean(value);
		Matcher matcher = symLocTypeScalar.matcher(value);
		while(matcher.find()){
			if(Integer.parseInt(matcher.group(1))==i)
				return matcher.group(0);
		}
	    return  null;
	}
	
	public static String setSymLocAt(Document configuration, int i, String newValue){
		String value = getElement(configuration, new String[]{"heap"}).getValue();
		value = clean(value);
		String regex = "#symLoc\\("+i+"\\)\\|->zval\\((.+?),(.+?),(\\d+),(true|false)\\)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		while(matcher.find()){
			String oldSymLoc = matcher.group(0);
			String newSymLoc = "#symLoc("+i+
					")|->zval("+newValue+
					","+matcher.group(2)+
					","+matcher.group(3)+
					","+matcher.group(4)+")";
			value = value.replaceFirst(oldSymLoc,newSymLoc);
		}
		
		return value;
	}
	

	
	public static String[] getArrayValue(String symLoc){
		Matcher matcher= symLocTypeArray.matcher(symLoc);
		matcher.find();
		return new String[]{matcher.group(1),matcher.group(2),"array",matcher.group(3),matcher.group(4)};
	}

	public static String[] getScalarValue(String symLoc){
		Matcher matcher= symLocTypeScalar.matcher(symLoc);
		matcher.find();
		return new String[]{matcher.group(1),matcher.group(2),matcher.group(3),matcher.group(4),matcher.group(5)};		
	}
	

	
	/**
	 * returns an array of ListItems 
	 * line separators and whitespace
	 * which is something like: 
	 * #symLoc(3)|->zval(Array(["x",public],ListItem([ "x" , 
          public , #symLoc(13) ]) ListItem([ "y" , public , #symLoc(14) ]) ) ,
           array , 1 , false )
	 *  @param configuration
	 *  @param symLoc: should match regex symLocArray 
	 * 
	 *  "#symLoc\\((\\d+)\\)\\|->zval\\((.+?),array,(\\d+),(true|false)\\)"
	 * */
	//TODO maybe changed to private later
	public static String[] getArrayListItemsAt(String symLoc){
		Matcher matcher = arrayListItem.matcher(symLoc);
		ArrayList<String> items = new ArrayList<String>();
		   while(matcher.find()){  
			   items.add(matcher.group(0));
		   }  
		return items.toArray(new String[items.size()]);
	}

	//
	public static String[] getArrayListItemsAt(Document configuration, int i){
		String symLoc = getSymLocAt(configuration, i);
		return getArrayListItemsAt(symLoc);
	}
	
	public static int VarName = 0;
	public static int VarVisibility =1;
	public static int VarLoc = 2;
	
	public static String[] getVariable(String listItem){
		Matcher matcher = arrayListItem.matcher(listItem);
		matcher.find();
		return new String[]{matcher.group(1),matcher.group(2),matcher.group(3)};
	}
	
	
	public static String[] getFunctionStackItems(Document configuration){
		ArrayList<String> items = new ArrayList<String>();
		Element functionStack = getElement(configuration, new String[]{"control","functionStack"});
		String functionStackValue = clean(functionStack.getValue());
		//TODO change pattern
		Pattern functionStackListItem = Pattern.compile("ListItem\\(sf\\(\"(.+?)\",(.+?),#symLoc\\((\\d+)\\),(.+?),(.+?),@byValue,(.+?)\\)\\)");  
		Matcher matcher = functionStackListItem.matcher(functionStackValue);
		while(matcher.find()){
			items.add(matcher.group(0));
		}
		return items.toArray(new String[items.size()]);
	}
	
	public static String[] getFunctionStackItemAttr(String ListItem){
		Pattern functionStackListItem = Pattern.compile("ListItem\\(sf\\(\"(.+?)\",(.+?),#symLoc\\((\\d+)\\),(.+?),(.+?),@byValue,(.+?)\\)\\)");  
		Matcher matcher = functionStackListItem.matcher(ListItem);
		if(matcher.find()){
			return new String[]{matcher.group(1),matcher.group(3)};

		}
		return null;
	}
	
	public static boolean hasVariables(String symLoc){
		if(ConfigAnalyser.isArrayVarible(symLoc) &&
				!ConfigAnalyser.isEmptyArray(
					ConfigAnalyser.getArrayValue(symLoc)))
			return true;
		return false;
		
	}
	public static boolean hasVariables(Document configuration, int symLocId){
		String symLoc = ConfigAnalyser.getSymLocAt(
				configuration,symLocId);
		if(ConfigAnalyser.isArrayVarible(symLoc) &&
				!ConfigAnalyser.isEmptyArray(
					ConfigAnalyser.getArrayValue(symLoc)))
			return true;
		return false;
		
	}

	/**
	 * formatted for view
	 * */
	
	public static LinkedHashMap<String, String> getHeapMap(Document configuration){
		String cellValue = getElement(configuration, new String[]{"heap"}).getValue();
	    cellValue = clean(cellValue);
	    Matcher matcher = symLoc.matcher(cellValue); 
	    LinkedHashMap<String, String> heap = new LinkedHashMap<String,String>(); 
	    
	    while(matcher.find()){
	    	heap.put(matcher.group(1),matcher.group(0));
	    }
		return heap;
	}
	
	public static String getHeapValue(Map<String, String> heapMap){
		String heapValue = "";
		for (String value : heapMap.values()) {
			heapValue +=value;
			heapValue +="\n";
		}
		return heapValue;
	}
	
	public static void updateHeapMap(Map<String, String> heapMap, String key, String newValue){
		String regex = "#symLoc\\("+key+"\\)\\|->zval\\((.+?),(.+?),(\\d+),(true|false)\\)";
		Pattern pattern = Pattern.compile(regex);
		if(heapMap.containsKey(key)){
			String oldSymLoc = heapMap.get(key);
			Matcher matcher = pattern.matcher(oldSymLoc);
			while(matcher.find()){
				String newSymLoc = "#symLoc("+key+
						")|->zval("+newValue+
						","+matcher.group(2)+
						","+matcher.group(3)+
						","+matcher.group(4)+")";
				heapMap.put(key, newSymLoc);
			}	
		}
		
	}
	
	public static void updateConfigurationHeap(Document configuration, Map<String, String> heapMap){
		String heapValue = getHeapValue(heapMap);
		getElement(configuration, new String[]{"heap"}).setText(heapValue);		
	}
	
	
	public static void outputXML(Document configuration, String filePath) throws IOException{
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getRawFormat());
		xmlOutput.output(configuration, new FileWriter(filePath));
 
	}


}
