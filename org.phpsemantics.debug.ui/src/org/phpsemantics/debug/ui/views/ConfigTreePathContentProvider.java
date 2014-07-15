package org.phpsemantics.debug.ui.views;


import org.eclipse.jface.viewers.ITreePathContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.Viewer;
import org.jdom2.Document;
import org.jdom2.Element;

public class ConfigTreePathContentProvider implements ITreePathContentProvider{
	
	private static final Object[] EMPTY_ARRAY = new Object[0];

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
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
	public Object[] getChildren(TreePath parentPath) {

		if(parentPath.getLastSegment() instanceof Element){
			return ((Element)parentPath.getLastSegment()).getChildren().toArray();
		}
		return null;
	}

	@Override
	public boolean hasChildren(TreePath path) {
		
		if(path.getLastSegment() instanceof Element){
			
			return ((Element)path.getLastSegment()).getChildren().size() > 0;
		}		
		
		return false;
	}

	@Override
	public TreePath[] getParents(Object element) {
		
		if(element instanceof Element){
			
			Element parentElement = ((Element)element).getParentElement();
			
			if( parentElement != null){
				
				TreePath[] grandParents = getParents(parentElement);
				
				int numberOfPaths = grandParents.length;
				
				//TreePath[] to be returned
				TreePath[] parents = new TreePath[numberOfPaths];				
				
				for(int i = 0; i<numberOfPaths; i++){
					
					 parents[i] = grandParents[i].createChildPath(parentElement);
					
				}
				
				return parents;

			}
						
		}
		
		return new TreePath[]{TreePath.EMPTY};
	}

}
