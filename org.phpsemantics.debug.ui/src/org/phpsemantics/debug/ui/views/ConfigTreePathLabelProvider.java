package org.phpsemantics.debug.ui.views;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreePathLabelProvider;
import org.eclipse.jface.viewers.TreeColumnViewerLabelProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.ViewerLabel;

public class ConfigTreePathLabelProvider extends TreeColumnViewerLabelProvider{
	
	private ITreePathLabelProvider treePathProvider = new ITreePathLabelProvider() {

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void updateLabel(ViewerLabel label, TreePath elementPath) {
			// TODO Auto-generated method stub
			
		}
		
	};
	

	public ConfigTreePathLabelProvider(IBaseLabelProvider labelProvider) {
		super(labelProvider);
		super.setProviders(treePathProvider);
		// TODO Auto-generated constructor stub
	}


	

	


}
