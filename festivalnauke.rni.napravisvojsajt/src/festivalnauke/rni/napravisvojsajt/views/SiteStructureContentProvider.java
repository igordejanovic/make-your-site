package festivalnauke.rni.napravisvojsajt.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

import festivalnauke.rni.napravisvojsajt.model.Page;
import festivalnauke.rni.napravisvojsajt.model.Site;
import festivalnauke.rni.napravisvojsajt.model.SiteElement;

/**
 * ContentProvider for the main view that shows site structure.
 */

class SiteStructureContentProvider implements IGraphEntityContentProvider {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}
	
	protected List<SiteElement> getAllSiteElements(SiteElement element){
		List<SiteElement> elements = new ArrayList<SiteElement>();
		elements.add(element);
		if (element instanceof Page){
			SiteElement page = (SiteElement) element;
			for(SiteElement e: page.getSiteElements()){
				elements.addAll(getAllSiteElements(e));
			}
		}
		return elements;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof Site){
			Site site = (Site) inputElement;
			return getAllSiteElements(site.getRootPage()).toArray();
		}else
			return new Object[]{inputElement};					
	}

	@Override
	public Object[] getConnectedTo(Object entity) {
		if(entity instanceof Site){
			Site site = (Site) entity;
			return site.getRootPage().getSiteElements().toArray();
		}else if(entity instanceof Page){
			SiteElement page = (SiteElement) entity;
			return page.getSiteElements().toArray();
		}else{
			return new Object[]{};
		}
	}
}