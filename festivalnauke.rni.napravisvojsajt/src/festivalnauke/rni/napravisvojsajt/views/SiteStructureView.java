package festivalnauke.rni.napravisvojsajt.views;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphItem;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import festivalnauke.rni.napravisvojsajt.model.ISiteChangeListener;
import festivalnauke.rni.napravisvojsajt.model.Site;

public class SiteStructureView extends ViewPart 
		implements IZoomableWorkbenchPart, ISiteChangeListener {

	public static final String ID = "festivalnauke.rni.napravisvojsajt.sitestructureview";

	private GraphViewer viewer;

	public enum Layout {
		RADIAL("У круг"),
		TREE("Као стабло"),
		GRID("Као мрежа"),
		SPRING("Разбацано");
		
		private String title;

		private Layout(String title){
			this.title = title;
		}
		
		@Override
		public String toString(){
			return this.title;
		}
	}
	
	
	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new GraphViewer(parent, SWT.NONE);
		viewer.setContentProvider(new SiteStructureContentProvider());
		viewer.setLabelProvider(new SiteStructureLabelProvider());
		viewer.setLayoutAlgorithm(new TreeLayoutAlgorithm());
		viewer.setInput(Site.getSite());
		getSite().setSelectionProvider(viewer);
		Site.getSite().addSiteChangeListener(this);
		fillToolBar();
		parent.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				refresh();
			}
		});
		
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				highlightConnections();
			}
		});
		
		
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	private void refresh(){
		viewer.applyLayout();
		viewer.refresh();		
	}
	
	@SuppressWarnings("unchecked")
	public void highlightConnections(){
		Graph graph = viewer.getGraphControl();
		List<GraphItem> selection = graph.getSelection();
		Set<GraphItem> newSelection = new HashSet<GraphItem>();
		for(GraphItem g: selection){
			newSelection.add(g);
			if (g instanceof GraphNode){
				GraphNode node = (GraphNode) g;
				for(GraphConnection con: (List<GraphConnection>)node.getSourceConnections()){
					newSelection.add(con);
				}
			}
		}
		graph.setSelection(newSelection.toArray(new GraphItem[]{}));
	}

	@Override
	public void siteChanged() {
		refresh();
	}

	private void fillToolBar() {
		// TODO
		ZoomContributionViewItem toolbarZoomContributionViewItem = 
					new ZoomContributionViewItem(this);
	}

	
	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return viewer;
	}

	public void setLayout(Layout firstElement) {
		switch(firstElement){
		case RADIAL: viewer.setLayoutAlgorithm(new RadialLayoutAlgorithm()); break;
		case TREE: viewer.setLayoutAlgorithm(new TreeLayoutAlgorithm()); break;
		case GRID: viewer.setLayoutAlgorithm(new GridLayoutAlgorithm()); break;
		case SPRING: viewer.setLayoutAlgorithm(new SpringLayoutAlgorithm()); break;
		}
		refresh();
	}

}