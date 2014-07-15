package org.phpsemantics.debug.ui.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.ILineBreakpoint;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.phpsemantics.debug.core.launching.IKPHPLaunchConfigurationConstants;
import org.phpsemantics.debug.core.model.KPHPLineBreakpoint;

public class KPHPLineBreakpointAdapter implements IToggleBreakpointsTarget {
	@Override
	public void toggleLineBreakpoints(IWorkbenchPart part, ISelection selection)
			throws CoreException {
		System.out.println("get into the adapter toggleLineBreakpoints");

			ITextEditor textEditor = getEditor(part);
			if (textEditor != null) {
	          IResource resource = (IResource) textEditor.getEditorInput()
	                                              .getAdapter(IResource.class);
	          ITextSelection textSelection = (ITextSelection) selection;
	          int lineNumber = textSelection.getStartLine();
	          IBreakpoint[] breakpoints = DebugPlugin.getDefault().getBreakpointManager()
	                         .getBreakpoints(IKPHPLaunchConfigurationConstants.ID_KPHP_DEBUG_MODEL);
	          for (int i = 0; i < breakpoints.length; i++) {
	             IBreakpoint breakpoint = breakpoints[i];
	             if (resource.equals(breakpoint.getMarker().getResource())) {
	                if (((ILineBreakpoint)breakpoint).getLineNumber() == (lineNumber + 1)) {
	                breakpoint.delete();
	                   return;
	                }
	             }
	          }
	    KPHPLineBreakpoint lineBreakpoint = new KPHPLineBreakpoint(resource, lineNumber + 1);
	          DebugPlugin.getDefault().getBreakpointManager().addBreakpoint(lineBreakpoint);
	       }
	   
		// TODO Auto-generated method stub
		
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#canToggleLineBreakpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public boolean canToggleLineBreakpoints(IWorkbenchPart part, ISelection selection) {
		System.out.println("get into the adapter canToggleLinebreakpoints");

		return getEditor(part) != null;
	}
	
	/**
	 * Returns the editor being used to edit a PDA file, associated with the
	 * given part, or <code>null</code> if none.
	 *  
	 * @param part workbench part
	 * @return the editor being used to edit a PDA file, associated with the
	 * given part, or <code>null</code> if none
	 */
	private ITextEditor getEditor(IWorkbenchPart part) {
		System.out.println("get into the adapter getEditor");
		

		if (part instanceof ITextEditor) {
			System.out.println("get into the adapter getEditor: yes instance of ItextEditor");

			ITextEditor editorPart = (ITextEditor) part;
			IResource resource = (IResource) editorPart.getEditorInput().getAdapter(IResource.class);
			if (resource != null) {
				System.out.println("get into the adapter getEditor: yes the resource is not null");

				String extension = resource.getFileExtension();
				if (extension != null && extension.equals("php")) {
					System.out.println("get into the adapter getEditor: yes the extension is php");

					return editorPart;
				}
			}
		}
		return null;		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#toggleMethodBreakpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void toggleMethodBreakpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
		System.out.println("get into the adapter toggleMethodsBreakpoints");

	}
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#canToggleMethodBreakpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public boolean canToggleMethodBreakpoints(IWorkbenchPart part, ISelection selection) {
		System.out.println("get into the adapter canToggleMethodsBreakpoints");

		return false;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#toggleWatchpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public void toggleWatchpoints(IWorkbenchPart part, ISelection selection) throws CoreException {
		System.out.println("get into the adapter toggleWatchpoints");

	}
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.actions.IToggleBreakpointsTarget#canToggleWatchpoints(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
	 */
	public boolean canToggleWatchpoints(IWorkbenchPart part, ISelection selection) {
		System.out.println("get into the adapter canToggleWatchpoints");

		return false;
	}
	
}
