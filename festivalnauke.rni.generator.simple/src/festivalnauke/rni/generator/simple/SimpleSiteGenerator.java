package festivalnauke.rni.generator.simple;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

import festivalnauke.rni.napravisvojsajt.generator.ISiteGenerator;
import festivalnauke.rni.napravisvojsajt.model.Page;
import festivalnauke.rni.napravisvojsajt.model.Picture;
import festivalnauke.rni.napravisvojsajt.model.Site;
import festivalnauke.rni.napravisvojsajt.model.SiteElement;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class SimpleSiteGenerator implements ISiteGenerator {
	
	private static Logger log = LoggerFactory.getLogger(SimpleSiteGenerator.class); 
	
	private static final String TEMPLATES_ROOT = "templates";
	
	private String outputFolder;
	private String siteTemplateName;
	private Template pageTemplate;
	private Site site;
	private List<String> mainLinks;
	
	public enum SiteTemplate {
		RED("Црвена - за девојчице", "crvena"),
		BLUE("Плава - за дечаке", "plava");
		
		private String title;
		private String path;
		
		private SiteTemplate(String title, String path){
			this.title = title;
			this.path = path;
		}

		@Override
		public String toString() {
			return this.title;
		}
		
		public String getPath() {
			return this.path;
		}
	}

	@Override
	public void generate(Shell shell, Site site) {
		log.info("Simple generator run.");
		this.site = site;

		SimpleSiteGeneratorDialog dialog = new SimpleSiteGeneratorDialog(shell);
		if(dialog.open() == IDialogConstants.OK_ID){
			String ofs = dialog.getOutputFolder();
			if(ofs != null && !"".equals(ofs)){
				outputFolder = ofs;
				siteTemplateName = dialog.getSiteTemplatePath();
				try {
					internalGenerate();
					MessageDialog d = new MessageDialog(shell, "Честитамо!", null, 
								"Креирање сајта успешно завршено! Сајт можете отворити у вашем веб читачу.",
								MessageDialog.INFORMATION, new String[]{"Отвори у веб читачу", "Одустани"}, 0);
					if(d.open()==0){
						File index = new File(outputFolder, "index.html");
						PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(index.toURI().toURL());
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.toString());
					MessageDialog.openError(shell, "Грешка!", "Дошло је до грешке приликом креирања сајта!");
				}
			}
			
		}
	}
	
	protected String getSiteTemplateFullPath(){
		return TEMPLATES_ROOT + "/" + this.siteTemplateName;
	}
	
	
	protected Template getPageTemplate(){
		try{
			if(this.pageTemplate == null){
				Configuration cfg = new Configuration();
				cfg.setClassForTemplateLoading(SimpleSiteGenerator.class, "/");
				cfg.setEncoding(Locale.getDefault(), "UTF-8");
				cfg.setDefaultEncoding("UTF-8");
				cfg.setObjectWrapper(new DefaultObjectWrapper());
				// Get page template. For now we will use only one.
				// TODO Support for multiple templates
				this.pageTemplate = cfg.getTemplate(getSiteTemplateFullPath() + "/page.html.ftl");
			}
			return this.pageTemplate;
		} catch (IOException e1) {
			log.error(e1.toString());
		}
		return null;
	}
	/**
	 * Recursive copy of the given bundle directory to the file system.
	 * @param bundlePath
	 * @param destination	file system path
	 * @throws IOException 
	 */
	protected void copyFromBundle(String bundlePath, String destination) throws IOException{
		final Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		Enumeration<String> paths = bundle.getEntryPaths(bundlePath);
		// destination path must end with /
		if(!destination.endsWith("/"))
			destination += "/";
		while(paths.hasMoreElements()){
			final String path = paths.nextElement();
			// we shall skip freemarker templates
			if(path.endsWith("ftl")){
				continue;
			}
			String prefix = Strings.commonPrefix(bundlePath, path);
			String subDir = path.substring(prefix.length());
			String newDest = destination + subDir;
			if(path.endsWith("/")){ // path is a directory -- recurse
				copyFromBundle(path, newDest);
			}else{
				// it is a file -- copy
				File destFile = new File(newDest);
				Files.createParentDirs(destFile);
				Files.copy( new InputSupplier<InputStream>() {
					public InputStream getInput() throws IOException {
				        return bundle.getEntry(path).openStream();
				    }
				} , destFile);
			}
		}
		
	}
	
	protected void generateSiteElements(SiteElement element, boolean isRoot) throws IOException, TemplateException{
		if(element instanceof Page){
			if(!isRoot)
				generatePage((Page)element, false);
			for(SiteElement e: element.getSiteElements()){
				generateSiteElements(e, false);
			}
		}else{
			copyPicture((Picture)element);
		}
	}
	
	protected void generatePage(Page page, boolean isRoot) throws IOException, TemplateException{
		Template t = getPageTemplate();
		File pageFile;
		if(isRoot){
			pageFile = new File(outputFolder, "index.html");
		}else{
			pageFile = new File(outputFolder, page.getTitle() + ".html");
		}
		OutputStreamWriter out = new OutputStreamWriter(
		        new FileOutputStream(pageFile), "UTF-8");
		
		Map<String, Object> model = new HashMap<String, Object>();
		List<String> sidebarLinks = new ArrayList<String>();
		
		for(SiteElement element: page.getSiteElements()){
			if(element instanceof Page){
				Page linkPage = (Page) element;
				sidebarLinks.add(linkPage.getTitle());
			}
		}
		
		model.put("isRoot", isRoot);
		model.put("rootPage", site.getRootPage());
		model.put("page", page);
		model.put("sidebarLinks", sidebarLinks);
		model.put("mainLinks", this.mainLinks);
		t.process(model, out);
		
	}
	
	protected void copyPicture(Picture element) throws IOException{
		File picFile = new File(element.getPicturePath());
		String imagesPath = new File(outputFolder, "images").toString();
		File destFile = new File(imagesPath,  picFile.getName());

		Files.createParentDirs(destFile);
		Files.copy(picFile, destFile);
	}
	
	protected void internalGenerate() throws IOException, TemplateException{
		log.info("Simple generator -- internalGenerate.");
		try {
			copyFromBundle(getSiteTemplateFullPath(), outputFolder);
		} catch (IOException e) {
			log.error(e.toString());
		}
		
		// Create main navigation
		mainLinks = new ArrayList<String>();
		for(SiteElement element: site.getRootPage().getSiteElements()){
			if(element instanceof Page){
				mainLinks.add(((Page)element).getTitle());
			}
		}
		
		// generate root page
		generatePage((Page)site.getRootPage(), true);
		// Recursive source generation
		generateSiteElements(site.getRootPage(), true);
		
	}
}
