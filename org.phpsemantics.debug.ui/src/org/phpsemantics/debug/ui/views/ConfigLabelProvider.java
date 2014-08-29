package org.phpsemantics.debug.ui.views;

import java.util.HashMap;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.jdom2.Element;

public class ConfigLabelProvider extends LabelProvider implements IColorProvider,ITableLabelProvider, ITableColorProvider, ITableFontProvider{
	
	protected static final Display display = Display.getCurrent();
	
/*	private static final String CONTROL = "control";
	private static final String GC = "gc";
	private static final String HEAP = "heap";
	private static final String INSTRUMENTATION = "instrumentation";
	private static final String IO = "IO";
	private static final String SCOPES = "scopes";
	private static final String SCRIPT = "script";
	private static final String TABLE = "table";
	private static final String TRACE = "trace";*/
	

	private static HashMap<String, Integer> colorMap = new HashMap<String, Integer>();
	
	public ConfigLabelProvider(){
		super();
		colorMap.put("control",SWT.COLOR_DARK_GREEN);
		colorMap.put("gc",SWT.COLOR_DARK_BLUE);
		colorMap.put("heap",SWT.COLOR_RED);
		colorMap.put("instrumentation",SWT.COLOR_DARK_CYAN);
		colorMap.put("IO",SWT.COLOR_DARK_MAGENTA);
		colorMap.put("scopes",SWT.COLOR_DARK_RED);
		colorMap.put("script",SWT.COLOR_DARK_YELLOW);
		colorMap.put("tables",SWT.COLOR_DARK_MAGENTA);
		colorMap.put("trace",SWT.COLOR_DARK_GRAY);
	}

	@Override
	public Font getFont(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		if(columnIndex == 0){
			if(colorMap.containsKey(getColumnText(element, columnIndex))){
				return display.getSystemColor(colorMap.get(getColumnText(element, columnIndex)));
			}
		}
		return null;
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof Element){
			Element e = (Element) element;
			
			if(columnIndex == 0){
				return e.getName();
			}
			
			if(e.getChildren().size() == 0 && columnIndex == 1){
				if(e.getName().equals("trace"))
					return "...";
				String value = e.getValue().replaceAll("\\n\\s*","\n");			
				return value.substring(1,value.length()-1);
			}
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(Object element)
	 */
	public String getText(Object element) {
		
		if (element instanceof Element){
			return ((Element) element).getName();
		
		}
		return element == null ? "" : element.toString();//$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IColorProvider#getForeground(Object element)
	 */
	public Color getForeground(Object element) {
			
		return display.getSystemColor(SWT.COLOR_DARK_GREEN);
			
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IColorProvider#getBackground(Object element)
	 */
	public Color getBackground(Object element) {

		return null;
	
	}
	

}
