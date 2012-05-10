package festivalnauke.rni.napravisvojsajt.views;

import java.util.Iterator;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.INullSelectionListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import de.timpietrusky.leetedit.LeetEdit;
import festivalnauke.rni.napravisvojsajt.model.Page;

public class PageViewPart extends ViewPart implements ISelectionListener, INullSelectionListener {
	
	public static final String ID = "festivalnauke.rni.napravisvojsajt.pageview";

	private FormToolkit toolkit;
	private ScrolledForm form;
	private Section sctnNewSection;
	private Label lblNewLabel;
	private Text txtNewText;
	private Section sctnNewSection_1;
	private Label lblNewLabel_1;
	private LeetEdit txtNewText_1;
	private Composite composite;
	private Composite composite_1;
	
	private DataBindingContext ctx;
	private WritableValue currentPage; // page for databinding

	public PageViewPart() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		
		// We are listenting to the Site element selection changes.
		getSite().getWorkbenchWindow().getSelectionService().addPostSelectionListener(SiteStructureView.ID, this);
				
		toolkit = new FormToolkit(parent.getDisplay());
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		form = toolkit.createScrolledForm(parent);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 10;
		gridLayout.marginLeft = 5;
		form.getBody().setLayout(gridLayout);
		TableWrapData twd_sctnNewSection = new TableWrapData(TableWrapData.LEFT, TableWrapData.TOP, 1, 1);
		twd_sctnNewSection.grabHorizontal = true;
		
		composite = new Composite(form.getBody(), SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		toolkit.adapt(composite);
		toolkit.paintBordersFor(composite);
		composite.setLayout(new GridLayout(1, false));
		
		sctnNewSection = toolkit.createSection(composite, Section.TITLE_BAR);
		sctnNewSection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		toolkit.paintBordersFor(sctnNewSection);
		sctnNewSection.setText("Наслов");
		sctnNewSection.setExpanded(true);
		
		lblNewLabel = toolkit.createLabel(composite, "Упишите наслов странице.", SWT.NONE);
		lblNewLabel.setForeground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
		
		txtNewText = toolkit.createText(composite, "New Text", SWT.BORDER);
		txtNewText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		composite_1 = new Composite(form.getBody(), SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		toolkit.adapt(composite_1);
		toolkit.paintBordersFor(composite_1);
		composite_1.setLayout(new GridLayout(1, false));
		
		sctnNewSection_1 = toolkit.createSection(composite_1, Section.TITLE_BAR);
		sctnNewSection_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		toolkit.paintBordersFor(sctnNewSection_1);
		sctnNewSection_1.setText("Садржај");
		
		lblNewLabel_1 = toolkit.createLabel(composite_1, "Овде упишите текст који ће бити главни садржај стране, испод наслова.", SWT.WRAP);
		lblNewLabel_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
		
//		txtNewText_1 = toolkit.createText(composite_1, "New Text", SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		txtNewText_1 = new LeetEdit(composite_1, SWT.NONE);
		txtNewText_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		// Databinding
		ctx = new DataBindingContext();
		currentPage = new WritableValue();
		IObservableValue targetTitle = WidgetProperties.text(SWT.Modify).observe(txtNewText);
		IObservableValue pageTitle = PojoProperties.value("title").observeDetail(currentPage);
		ctx.bindValue(targetTitle, pageTitle);
		
	}

	@Override
	public void setFocus() {
		form.setFocus();
	}

	@Override
	public void dispose() {
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
		toolkit.dispose();
		super.dispose();
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		
		// We have to synchronize node selection with the page properties
		if(part instanceof SiteStructureView){

			syncPageContent();
			
			// Count selected Pages and get a reference to selected Page
			IStructuredSelection s = (IStructuredSelection) selection;
			Page selectedPage = null;
			int selPageCount = 0;
			for(@SuppressWarnings("rawtypes")
				Iterator selIter = s.iterator(); selIter.hasNext(); ){
				Object selObj = selIter.next();
				if(selObj instanceof Page){
					selPageCount++;
					selectedPage = (Page) selObj;
				}
			}
			
			if(selPageCount == 1){
					this.txtNewText_1.setText(selectedPage.getContent());
					currentPage.setValue(selectedPage);
			}else{
				this.txtNewText_1.setText("");
				currentPage.setValue(null);
			}
		}
	}

	public Page getCurrentPage() {
		return (Page) currentPage.getValue();
	}

	public void syncPageContent() {
		// LeetEdit does not support databinding so we have to sync with model.
		Page cPage = (Page) currentPage.getValue();
		if(cPage != null){
			cPage.setContent(this.txtNewText_1.getText());
		}
		
	}
}
