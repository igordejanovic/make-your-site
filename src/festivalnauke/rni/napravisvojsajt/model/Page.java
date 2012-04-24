package festivalnauke.rni.napravisvojsajt.model;

import java.util.ArrayList;

import org.eclipse.ui.services.IDisposable;

import festivalnauke.rni.napravisvojsajt.FestivalMessages;

public class Page extends SiteElement implements IDisposable {

	protected String title;
	protected String content;
	protected Page(){
		this(null);
		title = FestivalMessages.Page_Default_Title_Root;
	}

	public Page(SiteElement parent) {
		super();
		this.parent = parent;
		siteElements = new ArrayList<SiteElement>();
		title = FestivalMessages.Page_Default_Title;
		content = FestivalMessages.Page_Default_Content;
		if (parent!=null)
			parent.addSiteElement(this);
	}
	
	public void dispose(){
		// Disposing of all subitems.
		for(SiteElement s: siteElements){
			s.dispose();
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		Site.getSite().fireSiteChanged();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


}
