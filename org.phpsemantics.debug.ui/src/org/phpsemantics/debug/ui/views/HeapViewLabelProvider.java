package org.phpsemantics.debug.ui.views;

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
import org.phpsemantics.debug.core.ConfigAnalyser;
import org.phpsemantics.debug.ui.views.HeapViewContentProvider.Element;

public class HeapViewLabelProvider extends LabelProvider implements IColorProvider,ITableLabelProvider, ITableColorProvider, ITableFontProvider {
	
	/**There are five columns;
	 *symLoc,Value,Type,RefCount,IsRef
	 **/
	private static final int SYMLOC = 0;
	private static final int VALUE = 1;
	private static final int TYPE = 2;
	private static final int REFCOUNT = 3;
	private static final int ISREF = 4;
	//private static final String TRUE = "true";
	//private static final String FALSE = "false";
	
	protected static final Display display = Display.getCurrent();
	@Override
	public Font getFont(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		switch(columnIndex){
			case SYMLOC:
				return display.getSystemColor(SWT.COLOR_DARK_GREEN);
			case VALUE:
				return getColumnText(element, columnIndex).startsWith("\"")?
						display.getSystemColor(SWT.COLOR_DARK_BLUE):null;
			case TYPE:
				return display.getSystemColor(SWT.COLOR_DARK_MAGENTA);
			case REFCOUNT: 
				break;
			case ISREF: 
				return display.getSystemColor(SWT.COLOR_DARK_RED);
				default: 
					return display.getSystemColor(SWT.COLOR_BLACK);

		}
		return display.getSystemColor(SWT.COLOR_BLACK);

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
			String content = ((Element)element).getContent();
			if(ConfigAnalyser.isSymLoc(content)){
				return (ConfigAnalyser.isArrayVarible(content))?
							columnIndex==1 && ConfigAnalyser.hasVariables(content)?
									"Array":ConfigAnalyser.getArrayValue(content)[columnIndex]:
							ConfigAnalyser.getScalarValue(content)[columnIndex];
			}else{
				// show value
				if(columnIndex == 1){
					return content;
				}
			}
		}
		return null;
	}

	@Override
	public Color getForeground(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getBackground(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

}
