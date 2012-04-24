package festivalnauke.rni.napravisvojsajt.contributions;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

import festivalnauke.rni.napravisvojsajt.views.SiteStructureView;

public class LayoutSelection extends ControlContribution {

	public LayoutSelection(){
		super(null);
	}

	public LayoutSelection(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createControl(Composite parent) {
		Composite c = new Composite(parent, SWT.NONE);
		GridLayout gl_c = new GridLayout(2,false);
		gl_c.marginHeight = 0;
		c.setLayout(gl_c);
		Label label = new Label(c, SWT.NONE);
		label.setText("Распоређивање");
		ComboViewer algCombo = new ComboViewer(c, SWT.DROP_DOWN|SWT.READ_ONLY);
		algCombo.setContentProvider(ArrayContentProvider.getInstance());
		algCombo.setInput(SiteStructureView.Layout.values());
		algCombo.setSelection(new StructuredSelection(SiteStructureView.Layout.RADIAL));
		
		algCombo.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				final SiteStructureView siteView = (SiteStructureView) PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage().findView(SiteStructureView.ID);

					siteView.setLayout(
						(SiteStructureView.Layout)((IStructuredSelection)event.getSelection()).getFirstElement());
			}
		});		
		
		return c;
	}


}
