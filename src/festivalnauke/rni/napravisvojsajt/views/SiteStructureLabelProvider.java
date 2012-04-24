package festivalnauke.rni.napravisvojsajt.views;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.viewers.IEntityConnectionStyleProvider;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;
import org.eclipse.zest.core.viewers.ISelfStyleProvider;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;

import festivalnauke.rni.napravisvojsajt.Activator;
import festivalnauke.rni.napravisvojsajt.model.Page;
import festivalnauke.rni.napravisvojsajt.model.Picture;
import festivalnauke.rni.napravisvojsajt.model.Site;
import festivalnauke.rni.napravisvojsajt.model.SiteElement;

class SiteStructureLabelProvider extends LabelProvider 
	implements ISelfStyleProvider, IEntityConnectionStyleProvider, IEntityStyleProvider {

	private Image home;
	private Image page;

	public SiteStructureLabelProvider() {
		super();
		home = Activator.getImageDescriptor("icons/community_48.png").createImage();
		page = Activator.getImageDescriptor("icons/paper_content_48.png").createImage();
	}
	
	@Override
	public String getText(Object element) {
		if (element instanceof Site){
			return "Сајт";
		}else if(element instanceof Page){
			return ((Page)element).getTitle();
		}else
			return "";
	}

	@Override
	  public void selfStyleConnection(Object element,
	          GraphConnection connection) {
	  
	        connection.setLineStyle(SWT.LINE_CUSTOM);
	        PolylineConnection pc = (PolylineConnection) connection
	            .getConnectionFigure();

	        pc.setTargetDecoration(createDecoration(ColorConstants.white));
	  }

	  private PolygonDecoration createDecoration(final Color color) {
	    PolygonDecoration decoration = new PolygonDecoration() {
	      protected void fillShape(Graphics g) {
	        g.setBackgroundColor(color);
	        super.fillShape(g);
	      }
	    };
	    decoration.setScale(10, 5);
	    decoration.setBackgroundColor(color);
	    return decoration;
	  }

	@Override
	public void selfStyleNode(Object element, GraphNode node) {
	  // TODO Auto-generated method stub
	  
	}

	  
	@Override
	public Image getImage(Object element) {
		if(element instanceof Page){
			SiteElement page = (SiteElement) element;
			if(page.getParent() == null)
				return this.home;
			else
				return this.page;
		}else if(element instanceof Picture){
			return ((Picture)element).getScalledImage();
		}
		return null;
	}

	@Override
	public void dispose() {
		home.dispose();
		home = null;
		page.dispose();
		page = null;
		
		super.dispose();
	}

	@Override
	public int getConnectionStyle(Object src, Object dest) {
		if(dest instanceof Picture){
			return ZestStyles.CONNECTIONS_SOLID;
		}else
			return ZestStyles.CONNECTIONS_DOT;
	}

	@Override
	public Color getColor(Object src, Object dest) {
		if(dest instanceof Picture)
			return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		return null;
	}

	@Override
	public Color getHighlightColor(Object src, Object dest) {
		return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
	}

	@Override
	public int getLineWidth(Object src, Object dest) {
		if(dest instanceof Picture)
			return 3;
		return 0;
	}

	@Override
	public IFigure getTooltip(Object src, Object dest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConnectionRouter getRouter(Object src, Object dest) {
		return null;
	}

	@Override
	public Color getNodeHighlightColor(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getBorderColor(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getBorderHighlightColor(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBorderWidth(Object entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Color getBackgroundColour(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getForegroundColour(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IFigure getTooltip(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean fisheyeNode(Object entity) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
