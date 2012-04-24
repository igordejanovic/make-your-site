package festivalnauke.rni.napravisvojsajt.handlers;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import festivalnauke.rni.napravisvojsajt.model.Page;
import festivalnauke.rni.napravisvojsajt.model.Picture;
import festivalnauke.rni.napravisvojsajt.model.SiteElement;
import festivalnauke.rni.napravisvojsajt.views.SiteStructureView;

public class CreatePictureHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		FileDialog fileDialog = new FileDialog(window.getShell(), SWT.OPEN);
		fileDialog.setFilterExtensions(new String[]{"*.png;*.PNG;*.gif;*.GIF;*.jpg;*.JPG"});
		String fileName = fileDialog.open();
		Image image;

		if(fileName == null)
			return null;
		try{
			image = new Image(window.getShell().getDisplay(), fileName);
			
		}catch(RuntimeException e){
			ErrorDialog.openError(window.getShell(), "Грешка", e.getLocalizedMessage(), Status.CANCEL_STATUS);
			return null;
		}
		
		ISelection selection = window.getSelectionService().getSelection(SiteStructureView.ID);
		if (selection instanceof IStructuredSelection && !selection.isEmpty()){
			IStructuredSelection ssel = (IStructuredSelection) selection;
			@SuppressWarnings("rawtypes")
			Iterator i = ssel.iterator();
			while(i.hasNext()){
				Object selectedObject = i.next();
				if(selectedObject instanceof Page){
					new Picture((SiteElement)selectedObject, image);
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
