package festivalnauke.rni.napravisvojsajt.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import festivalnauke.rni.napravisvojsajt.model.Page;
import festivalnauke.rni.napravisvojsajt.model.SiteElement;
import festivalnauke.rni.napravisvojsajt.views.SiteStructureView;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class CreatePageHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public CreatePageHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		ISelection selection = window.getSelectionService().getSelection(SiteStructureView.ID);
		if (selection instanceof IStructuredSelection && !selection.isEmpty()){
			IStructuredSelection ssel = (IStructuredSelection) selection;
			@SuppressWarnings("rawtypes")
			Iterator i = ssel.iterator();
			while(i.hasNext()){
				Object selectedObject = i.next();
				if(selectedObject instanceof Page){
					new Page((SiteElement) selectedObject);
					break;
				}
			}
		}
		SiteStructureView siteView = (SiteStructureView) window.getActivePage().findView(SiteStructureView.ID);
		if(siteView != null)
			siteView.highlightConnections();
		
		return null;
	}
}
