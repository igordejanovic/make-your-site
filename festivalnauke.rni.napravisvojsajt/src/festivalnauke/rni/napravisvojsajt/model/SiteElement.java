package festivalnauke.rni.napravisvojsajt.model;

import java.util.Iterator;
import java.util.List;

import org.eclipse.ui.services.IDisposable;

public abstract class SiteElement implements IDisposable {

	protected List<SiteElement> siteElements;
	protected SiteElement parent;

	public void addSiteElement(SiteElement siteItem) {
		// only one Picture is allowed
		if(siteItem instanceof Picture)
			removePicture();
		siteElements.add(siteItem);
		Site.getSite().fireSiteChanged();
	}

	public void removeSiteElement(SiteElement siteItem) {
		siteElements.remove(siteItem);
		siteItem.dispose();
		Site.getSite().fireSiteChanged();
	}

	public void removeAllSiteElements() {
		for(Iterator<SiteElement> it = siteElements.iterator(); it.hasNext();){
			SiteElement elem = it.next();
			it.remove();
			elem.dispose();
		}
		
		Site.getSite().fireSiteChanged();
	}

	public void removePicture() {
		Picture existingPicture = null;
		for(SiteElement s: siteElements){
			if(s instanceof Picture){
				existingPicture = (Picture) s;
				break;
			}
		}
		if(existingPicture != null){
			siteElements.remove(existingPicture);
			existingPicture.dispose();
		}		
	}

	public List<SiteElement> getSiteElements() {
		return siteElements;
	}

	public SiteElement getParent() {
		return parent;
	}

}
