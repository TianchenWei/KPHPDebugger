package org.phpsemantics.debug.ui.views;


import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.jdom2.Document;
import org.jdom2.Element;


public class ConfigContentProvider implements ITreeContentProvider{

	private static final Object[] EMPTY_ARRAY = new Object[0];
	
	@Override
	public void dispose() {
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {

		if (inputElement instanceof Document){
			Document doc = (Document) inputElement;
			Element[] elements = new Element[]{doc.getRootElement()};
	        return elements;
		}
			
	    return EMPTY_ARRAY;
	    
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		
		if(parentElement instanceof Element){
			
			List<Element> children = ((Element) parentElement).getChildren();
			
			//if(children.size() > 0)
				
				return children.toArray();
		}
		
		return EMPTY_ARRAY;
	}

	@Override
	public Object getParent(Object element) {
		
		if(element instanceof Element){
			
			return ((Element) element).getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		
		if(element instanceof Element){
			
			if(((Element) element).getChildren().size() > 0){	
					
				return true;	
			}
		
		}
		return false;
	}
	
}
