package festivalnauke.rni.generator.simple;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Strings;

import festivalnauke.rni.generator.simple.SimpleSiteGenerator.SiteTemplate;

public class SimpleSiteGeneratorDialog extends Dialog {
	private String outputFolder = null;
	private String siteTemplatePath = null;
	
	private static final String OUTPUTPATH_KEY = "outputPath";
	private static final String SITETEMPLATE_KEY = "siteTemplate";
	
	private ComboViewer cvSiteTemplate;
	private Text txtOutputFolder;
	

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public SimpleSiteGeneratorDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		final IDialogSettings settings = Activator.getDefault().getDialogSettings();

		container.setLayout(new FormLayout());
		
		txtOutputFolder = new Text(container, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.left = new FormAttachment(0, 133);
		fd_text.right = new FormAttachment(100, -105);
		fd_text.bottom = new FormAttachment(100, -16);
		txtOutputFolder.setLayoutData(fd_text);
		txtOutputFolder.setText(Strings.nullToEmpty(settings.get(OUTPUTPATH_KEY)));
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.left = new FormAttachment(0, 13);
		fd_lblNewLabel.bottom = new FormAttachment(100, -20);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("Излазни фолдер");
		
		Button btnOdaberi = new Button(container, SWT.NONE);
		btnOdaberi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog directoryDialog = new DirectoryDialog(getShell());
				directoryDialog.setText("Изаберите излазни фолдер");
				directoryDialog.setFilterPath(txtOutputFolder.getText());
				String dir = directoryDialog.open();
				if(dir != null){
					txtOutputFolder.setText(dir);
				}
				
			}
		});
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.left = new FormAttachment(100, -99);
		fd_btnNewButton.right = new FormAttachment(100, -13);
		fd_btnNewButton.bottom = new FormAttachment(100, -16);
		btnOdaberi.setLayoutData(fd_btnNewButton);
		btnOdaberi.setText("Одабери...");
		
		Label label = new Label(container, SWT.NONE);
		FormData fd_label = new FormData();
		fd_label.bottom = new FormAttachment(lblNewLabel, -14);
		fd_label.left = new FormAttachment(0, 13);
		label.setLayoutData(fd_label);
		label.setText("Боја сајта");
		
		cvSiteTemplate = new ComboViewer(container, SWT.READ_ONLY);
		Combo combo = cvSiteTemplate.getCombo();
		FormData fd_combo = new FormData();
		fd_combo.bottom = new FormAttachment(txtOutputFolder, -5);
		fd_combo.left = new FormAttachment(label, 53);
		combo.setLayoutData(fd_combo);
		cvSiteTemplate.setContentProvider(ArrayContentProvider.getInstance());
		cvSiteTemplate.setInput(SimpleSiteGenerator.SiteTemplate.values());
		String siteTemplate = settings.get(SITETEMPLATE_KEY);
		int siteTemplateOrd = 0;
		if(siteTemplate != null){
			siteTemplateOrd = Integer.parseInt(siteTemplate);
		}
		cvSiteTemplate.setSelection(new StructuredSelection(
				SimpleSiteGenerator.SiteTemplate.values()[siteTemplateOrd]), true);
		
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, "Настави", false);
		Button button_1 = createButton(parent, IDialogConstants.CANCEL_ID, "Прекини", false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(563, 159);
	}
	
	public String getOutputFolder(){
		return this.outputFolder;
	}
	
	public String getSiteTemplatePath() {
		return siteTemplatePath;
	}

	@Override
	protected void okPressed() {
		final IDialogSettings settings = Activator.getDefault().getDialogSettings();
		this.outputFolder = this.txtOutputFolder.getText();
		settings.put(OUTPUTPATH_KEY, txtOutputFolder.getText());
		IStructuredSelection sel = (IStructuredSelection) cvSiteTemplate.getSelection();
		SiteTemplate t = (SimpleSiteGenerator.SiteTemplate)sel.getFirstElement();
		this.siteTemplatePath = t.getPath();
		settings.put(SITETEMPLATE_KEY, t.ordinal());
		super.okPressed();
	}
}
