package org.phpsemantics.debug.ui.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

public class HeapView extends ViewPart {
	
	private TreeViewer viewer;
	private HeapViewContentProvider contentProvider =  new HeapViewContentProvider();
	public HeapViewContentProvider getContentProvider() {
		return contentProvider;
	}

	public void setContentProvider(HeapViewContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}

	private HeapViewLabelProvider labelProvider = new HeapViewLabelProvider();
	
	private DrillDownAdapter drillDownAdapter;
	private Action filter;
	private Action action2;
	private Action doubleClickAction;
	
	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);

		viewer.setContentProvider(contentProvider);
		viewer.setLabelProvider(labelProvider);
		viewer.setInput(getViewSite());
		
      	Tree tree = viewer.getTree();
        tree.setHeaderVisible(true);
        tree.setLinesVisible(true);

        TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
        column1.setText("SymLoc");
        column1.setWidth(70);
        TreeColumn column2 = new TreeColumn(tree, SWT.LEFT);
        column2.setText("Value");
        column2.setWidth(200);
        TreeColumn column3 = new TreeColumn(tree, SWT.LEFT);
        column3.setText("Type");
        column3.setWidth(50);
        TreeColumn column4 = new TreeColumn(tree, SWT.LEFT);
        column4.setText("RefCount");
        column4.setWidth(80);
        TreeColumn column5 = new TreeColumn(tree, SWT.LEFT);
        column5.setText("IsRef");
        column5.setWidth(15);
		makeActions();
        hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	    getSite().setSelectionProvider(viewer);

	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				HeapView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(filter);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(filter);
		manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(filter);
		manager.add(action2);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		filter = new Action() {
			public void run() {
				contentProvider.setIgnoreEmptyArray(true);
				viewer.refresh();
			}
		};
		filter.setText("Remove All Empty Arrays");
		filter.setToolTipText("Remove all symLoc with empty array");
		if(contentProvider.isIgnoreEmptyArray()){

			filter.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
					getImageDescriptor(ISharedImages.IMG_ELCL_REMOVEALL_DISABLED));	
		}else{
			filter.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
					getImageDescriptor(ISharedImages.IMG_ELCL_REMOVEALL));	
		}
		
		action2 = new Action() {
			public void run() {
				//showMessage("Action 2 executed");
			}
		};
		
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage("Double-click detected on "+obj.toString());
			}
		};
	}
	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Config View",
			message);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
		
	}

	
}
