package org.phpsemantics.debug.ui.views;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchListener;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jface.viewers.ITreePathContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.PlatformUI;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.phpsemantics.debug.core.launching.IKPHPConstants;
import org.phpsemantics.debug.core.model.KPHPDebugElement;

public class ConfigTreePathContentProvider implements ITreePathContentProvider, IDebugEventSetListener, ILaunchListener{

	private static final Object[] EMPTY_ARRAY = new Object[0];
	
	private Viewer viewer;
		
	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.IDebugEventSetListener#handleDebugEvents(org.eclipse.debug.core.DebugEvent[])
	 */
	public void handleDebugEvents(DebugEvent[] events) {
		for (int i = 0; i < events.length; i++) {
			DebugEvent event = events[i];
			if (event.getKind() == DebugEvent.CHANGE) {
					Object object = event.getSource();
					if(object instanceof KPHPDebugElement){
						File file = ((KPHPDebugElement) object).getCurrentConfigurationPath().toFile();
						refresh(file);
					}
			}
		}		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.ILaunchListener#launchRemoved(ILaunch launch))
	 */
	public void launchRemoved(ILaunch launch) {
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.ILaunchListener#launchAdded(ILaunch launch)
	 */
	public void launchAdded(ILaunch launch) {
		if(launch.getLaunchMode().equals(ILaunchManager.DEBUG_MODE)){
			ILaunchConfiguration configuration = launch.getLaunchConfiguration();
			String kphp = "";
			try {
				kphp = configuration.getAttribute(
						IKPHPConstants.ATTR_KPHP_ROOT, 
						IKPHPConstants.DEFAULT_KPHP_ROOT);
			} catch (CoreException e) {
				e.printStackTrace();
			}
			Path path = Paths.get(kphp);
			try {
				String init = configuration.getAttribute(
						IKPHPConstants.ATTR_INIT_CONFIG_FILE,
						IKPHPConstants.DEFAULT_INIT_CONFIG_FILE);
				path = path.resolve(init);
				
			} catch (CoreException e) {
				e.printStackTrace();
			}
			File initConfig = path.toFile();
			//TODO ensure it is an xml file
			if(initConfig.isFile()){
				refresh(initConfig);
				
			}
		}

	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.core.ILaunchListener#launchChanged(ILaunch launch)
	 */
	public void launchChanged(ILaunch launch) {
		
	}
	
	private void refresh(File file){
		SAXBuilder jdomBuilder = new SAXBuilder();
		Document doc = new Document();
		try {
			doc = jdomBuilder.build(file);
			System.out.println(doc.getRootElement().getName());
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
		
		if(doc.hasRootElement()){
			final Document document = doc;
			PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
		        public void run() {
		        	viewer.setInput(document);
		        	viewer.refresh();
		        }
		    });
			
		}
			
	}
	
	public ConfigTreePathContentProvider(){
		DebugPlugin.getDefault().addDebugEventListener(this);
		DebugPlugin.getDefault().getLaunchManager().addLaunchListener(this);
	}
		
	@Override
	public void dispose() {
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		 this.viewer = viewer;
	}

	@Override
	public Object[] getElements(Object inputElement) {

		if (inputElement instanceof Document){
			Document doc = (Document) inputElement;
			Element[] elements = new Element[]{doc.getRootElement()};
	        return elements;
		}else{
			
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
