package festivalnauke.rni.generator.simple;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import festivalnauke.rni.napravisvojsajt.model.Site;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SimpleSiteGenerator s = new SimpleSiteGenerator();
		
		Site site = Site.getSite();
		
		
		Display display = new Display();
		Shell shell = new Shell(display);
		
		s.generate(shell, site);
		
		System.out.println("Gotovo!");
		
		shell.open();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();

	}

}
