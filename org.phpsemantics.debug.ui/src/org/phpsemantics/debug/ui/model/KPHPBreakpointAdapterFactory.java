package org.phpsemantics.debug.ui.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.texteditor.ITextEditor;

public class KPHPBreakpointAdapterFactory implements IAdapterFactory{

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		System.out.println("boject to string"+adaptableObject.toString());
		System.out.println("object name"+adaptableObject.getClass().getName());
		System.out.println("object simple name"+adaptableObject.getClass().getSimpleName());
		if (adaptableObject instanceof ITextEditor) {
			System.out.println("get into the adapterfactory");
	          ITextEditor editorPart = (ITextEditor) adaptableObject;
	          IResource resource = (IResource) editorPart.getEditorInput().getAdapter(IResource.class);
	          if (resource != null) {
	             String extension = resource.getFileExtension();
	             if (extension != null && extension.equals("php")) {
	             return new KPHPLineBreakpointAdapter();
	             }
	          } 
	       }
	       return null;	
	}

	@Override
	public Class[] getAdapterList() {
		System.out.println("get into the adapterfactory list");

		// TODO Auto-generated method stub
		return null;
	}


}
