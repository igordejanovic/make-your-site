package festivalnauke.rni.napravisvojsajt.model;

import org.eclipse.core.commands.common.EventManager;

public class Site extends EventManager {
	
	private static Site instance;
	protected SiteElement rootPage;

	protected Site() {
	}

	protected void initSite(){
		rootPage = new Page();
	}
	
	public void dispose(){
		rootPage.dispose();
	}
	
	public SiteElement getRootPage() {
		return rootPage;
	}

	public void setRootPage(SiteElement rootPage) {
		this.rootPage = rootPage;
	}
	
	public static Site createSite(){
		if(Site.instance!=null){
			Site.instance.dispose();
			Site.instance = null;
		}
		return getSite();
	}
	
	public static Site getSite(){
		if(Site.instance==null){
			Site.instance = new Site();
			Site.instance.initSite();
		}
		return Site.instance;
	}
	
	public void addSiteChangeListener(ISiteChangeListener listener){
		this.addListenerObject(listener);
	}
	
	public void removeSiteChangeListener(ISiteChangeListener listener){
		this.removeListenerObject(listener);
	}
	
	public void fireSiteChanged(){
		Object[] listeners = getListeners();
		for(int i=0; i<listeners.length; i++){
			((ISiteChangeListener)listeners[i]).siteChanged();
		}
	}
	
}
