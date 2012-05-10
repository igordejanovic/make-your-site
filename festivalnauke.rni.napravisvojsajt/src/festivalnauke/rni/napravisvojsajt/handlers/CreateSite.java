package festivalnauke.rni.napravisvojsajt.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import festivalnauke.rni.napravisvojsajt.generator.GeneratorDesc;
import festivalnauke.rni.napravisvojsajt.generator.GeneratorManager;
import festivalnauke.rni.napravisvojsajt.generator.ISiteGenerator;
import festivalnauke.rni.napravisvojsajt.model.Site;
import festivalnauke.rni.napravisvojsajt.views.PageViewPart;

public class CreateSite extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		GeneratorManager gm = GeneratorManager.getInstance();
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		// LeetEdit does not support databinding so we have to do some sinhronization with model before
		// code generation.

		PageViewPart pageView = (PageViewPart) window.getActivePage().findView(PageViewPart.ID);
		if(pageView != null){
			pageView.syncPageContent();
		}		
		
		List<GeneratorDesc> gens = gm.getGenerators();
		
		if(gens.size()>0){
			// For now we will always use the first generator from the list.
			ISiteGenerator generator = gm.createGenerator(gens.get(0).getId());
			generator.generate(window.getShell(), Site.getSite());
		}
		
		return null;
	}



}
