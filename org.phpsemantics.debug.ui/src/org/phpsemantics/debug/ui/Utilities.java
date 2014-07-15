package org.phpsemantics.debug.ui;


import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class Utilities {
	
	public static IPath searchActiveScript(){
		IWorkbench workb = PlatformUI.getWorkbench();
		if (workb == null) return null;
		System.out.println("yes workbench");
		
		IWorkbenchWindow[] wins = workb.getWorkbenchWindows();
		if (wins.length <= 0) return null;
		System.out.println(wins.length);

		int i = 0; 
		while ( (i < wins.length) 
				&& (! (wins[i].getPartService().getActivePart() instanceof IEditorPart))
		)
		i++;
		
		if (i >= wins.length) return null;

		IEditorPart editor = (IEditorPart) wins[i].getPartService().getActivePart();

		IEditorInput input = editor.getEditorInput();
		if (input == null) return null;
		
		
		System.out.println(input.getName());
		
		IPath path = ((FileEditorInput)input).getPath();
/*		System.out.println("os");
		System.out.println(path.toOSString());
		System.out.println("portable");
		System.out.println(path.toPortableString());
		System.out.println("string");
		System.out.println(path.toString());
		System.out.println("file");
		System.out.println(path.toFile().getAbsolutePath());*/

		if (path == null) 
		return null;
		
		return path;
	}
	
	/**
	 * Returns a selected {@link IResource} from a user dialog.
	 * 
	 * @param project
	 *            The project to display
	 * @param shell
	 *            A Shell
	 * @param fileExtensions
	 *            The required file extension
	 * @param requiredNatures
	 *            The required nature
	 * @param allowExternalFiles
	 *            Allow selection from an external files that are currently
	 *            opened in the editor
	 * @return A selected IResource
	 */
}