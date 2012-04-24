package festivalnauke.rni.napravisvojsajt.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import festivalnauke.rni.napravisvojsajt.model.SiteElement;
import festivalnauke.rni.napravisvojsajt.views.SiteStructureView;

public class DeleteHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		MessageDialog dialog = new MessageDialog(window.getShell(), "Брисање", null, 
				"Да ли заиста желите да обришете селектоване елементе и све поделементе?", MessageDialog.QUESTION, 
					new String[]{"Да", "Не"}, 0);
		
		if(dialog.open()==0){
			
			ISelectionService sservice = window.getSelectionService();
			ISelection selection = sservice.getSelection(SiteStructureView.ID);
			
			if (selection instanceof IStructuredSelection){
				boolean deleted = true;
				while(!selection.isEmpty() && deleted){
					deleted = false;
					IStructuredSelection ssel = (IStructuredSelection) selection;
					
					@SuppressWarnings("rawtypes")
					Iterator i = ssel.iterator();
					while(i.hasNext()){
						Object selectedObject = i.next();
						if(selectedObject instanceof SiteElement){
							SiteElement siteElement = (SiteElement) selectedObject;
							if(siteElement.getParent() != null){
								siteElement.getParent().removeSiteElement(siteElement);
								deleted = true;
								break;
							}else{
								// Root page
								siteElement.removeAllSiteElements();
								break;
							}
						}
					}
					// Get updated selection
					selection = sservice.getSelection(SiteStructureView.ID);
				}
		
			}
		}
		return null;
	}

}
