package org.phpsemantics.debug.ui.views;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.PlatformUI;
import org.phpsemantics.debug.core.ConfigAnalyser;
import org.phpsemantics.debug.core.model.KPHPDebugElement;
import org.phpsemantics.debug.core.model.KPHPThread;

public class HeapViewContentProvider implements ITreeContentProvider, IDebugEventSetListener{
	
	private static final Object[] EMPTY_ARRAY = new Object[0];
	
	private String configuration;
	

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	private LinkedHashMap<String, String> heapMap;
	
	private Viewer viewer;
	
	private boolean ignoreEmptyArray = true;
	
	
	public boolean isIgnoreEmptyArray() {
		return ignoreEmptyArray;
	}

	public void setIgnoreEmptyArray(boolean ignoreEmptyArray) {
		this.ignoreEmptyArray = ignoreEmptyArray;
	}

	protected class Element{
		
		private String content;

		private ArrayList<Element> children = new ArrayList<Element>();
		private ArrayList<Element> parents = new ArrayList<Element>();

		public Element(String content){
			this.content = content;
		}

		public String getContent(){
			return content;
		}
		
		public boolean hasChildren(){
			return ConfigAnalyser.hasVariables(content);
		}
		
		public boolean isEmptyArray(){
			return ConfigAnalyser.isArrayVarible(content)&&!hasChildren();
		}
		
		public void addChild(Element child){
			children.add(child);
		}
		
		public void addParent(Element parent){
			parents.add(parent);
		}
		
		public Object[] getChildren(){
			if(hasChildren()){
				String[] spawn = ConfigAnalyser.getArrayListItemsAt(content);
				for(String infant: spawn){
					Element child = new Element(infant);
					addChild(child);
					child.addParent(this);
				}
				return children.toArray(new Object[children.size()]);
			}
			return null;
		}
		
		public Object[] getParents(){
			
			return parents.toArray(new Object[children.size()]);
		}
	}

	public HeapViewContentProvider(){
		DebugPlugin.getDefault().addDebugEventListener(this);
	}

	@Override
	public void dispose() {
		DebugPlugin.getDefault().removeDebugEventListener(this);		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = viewer;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		
		if(heapMap != null){
				
			ArrayList<Element> elements = new ArrayList<Element>();
			if(ignoreEmptyArray){
				for(String value: heapMap.values()){
					if(!ConfigAnalyser.isArrayVarible(value)||ConfigAnalyser.hasVariables(value)){
						elements.add(new Element(value));
					}
				}
			}else{
				for(String value: heapMap.values())
					elements.add(new Element(value));
			}
			
			return elements.toArray(new Element[elements.size()]);
		}
		return EMPTY_ARRAY;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if(parentElement instanceof Element){
			return ((Element)parentElement).getChildren();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		if(element instanceof Element){
			return ((Element)element).getParents()[0];
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		
		if(element instanceof Element){
			return ((Element)element).hasChildren();
		}		
		return false;
	}

	@Override
	public void handleDebugEvents(DebugEvent[] events) {
		for (int i = 0; i < events.length; i++) {
			DebugEvent event = events[i];
			if (event.getKind() == DebugEvent.CHANGE) {
					Object object = event.getSource();
					if(object instanceof KPHPThread){
						heapMap = (LinkedHashMap<String, String>) ((KPHPThread)object).getHeapMap();
						configuration = ((KPHPThread)object).getCurrentConfigurationPath().toString();
						PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
					        public void run() {
					        	viewer.refresh();
					        }
					    });
					}else if(object instanceof KPHPDebugElement){
						configuration = ((KPHPDebugElement)object).getCurrentConfigurationPath().toString();
						heapMap = ConfigAnalyser.getHeapMap(ConfigAnalyser.buildConfig( new File(configuration)));
						PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
					        public void run() {
					        	viewer.refresh();
					        }
					    });
					}
			}
		}			
	}	

}
