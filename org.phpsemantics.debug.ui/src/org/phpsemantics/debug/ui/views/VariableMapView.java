package org.phpsemantics.debug.ui.views;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.debug.internal.ui.views.variables.VariablesView;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.phpsemantics.debug.core.ConfigAnalyser;
import org.phpsemantics.debug.core.GraphBuilder;
import org.phpsemantics.debug.core.model.KPHPVariable;
import org.phpsemantics.debug.core.DotFileBuilder;
import org.phpsemantics.debug.ui.views.HeapViewContentProvider.Element;

import uky.article.imageviewer.views.SWTImageCanvas;

@SuppressWarnings("restriction")
public class VariableMapView extends ViewPart {

public SWTImageCanvas imageCanvas;

private DotFileBuilder dfb = new DotFileBuilder();

// the listener we register with the selection service 
	private ISelectionListener listener = new ISelectionListener() {
		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if(selection instanceof IStructuredSelection){
				String graph = "";
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				
				if(part instanceof HeapView && obj instanceof Element){
					String configuration = ((HeapView)part).getContentProvider().getConfiguration();
					if(configuration != null){ 
						try {
							dfb.setConfiguration((ConfigAnalyser.buildConfig(new File(configuration))));
							int startSymLoc = ConfigAnalyser.getSymLocID(((Element)obj).getContent());
							String dotFile = dfb.writeDotFile(startSymLoc);
							graph = GraphBuilder.Tgif2(dotFile);
						
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
				if(part instanceof VariablesView && obj instanceof KPHPVariable){
					dfb.setConfiguration(((KPHPVariable)obj).getConfiguration());
					int startSymLoc = Integer.parseInt(
							((KPHPVariable)obj).getAttributes()[KPHPVariable.LOCATION]);
					String dotFile;
					try {
						dotFile = dfb.writeDotFile(startSymLoc);
							graph = GraphBuilder.Tgif2(dotFile);

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					
				}
				
				
				if((new File(graph)).isFile()){

					imageCanvas.loadImage(graph);
					imageCanvas.update();
				}


			}
		}
	};
	
	/**
	 * The constructor.
	 */
	public VariableMapView() {
	}
	
	/**
	 * Create the GUI.
	 * @param frame The Composite handle of parent
	 */
	public void createPartControl(Composite frame) {
		
		imageCanvas=new SWTImageCanvas(frame);
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(listener);

	}

	/**
	 * Called when we must grab focus.
	 * @see org.eclipse.ui.part.ViewPart#setFocus
	 */
	public void setFocus() {
		imageCanvas.setFocus();
	}

	/**
	 * Called when the View is to be disposed
	 */
	public void dispose() {
		imageCanvas.dispose();
		super.dispose();
	}
	


}
