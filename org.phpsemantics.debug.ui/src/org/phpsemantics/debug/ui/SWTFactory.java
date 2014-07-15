package org.phpsemantics.debug.ui;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

public abstract class SWTFactory {
	
	public static Composite createComposite(Composite parent){
		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(1, true));
		comp.setFont(parent.getFont());
		return comp;
		
	}
	
	public static Group createGroup(Composite parent, String text, int columns){
    	Group g = new Group(parent, SWT.NONE);
    	g.setLayout(new GridLayout(columns, false));
    	g.setText(text);
    	g.setFont(parent.getFont());
    	g.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    	return g;	
	}
	
	public static Text createText(Composite parent, int columns){
		Text t = new Text(parent, SWT.SINGLE | SWT.BORDER);
    	GridData gd = new GridData(GridData.FILL_HORIZONTAL);
   		gd.horizontalSpan = columns;	
    	t.setLayoutData(gd);
    	return t;
	}

}
