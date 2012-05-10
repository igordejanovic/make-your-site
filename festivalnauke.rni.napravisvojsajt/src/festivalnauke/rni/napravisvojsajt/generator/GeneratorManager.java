package festivalnauke.rni.napravisvojsajt.generator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.RegistryFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton for managing generator instances.
 * 
 * @author igor
 * 
 */
public class GeneratorManager {

	private final Logger log = LoggerFactory.getLogger(GeneratorManager.class);

	public static final String GENERATOR_EP_ID = "festivalnauke.rni.napravisvojsajt.generator";
	private static GeneratorManager manager = null;

	protected List<GeneratorDesc> generators = new ArrayList<GeneratorDesc>();

	private GeneratorManager() {
		super();
	}

	public static GeneratorManager getInstance() {
		if (manager == null) {
			manager = new GeneratorManager();
			manager.initialize();
		}
		return manager;
	}

	/**
	 * Scans the plugin registry for the generator extensions.
	 */
	public void initialize() {
		IConfigurationElement[] extensions = 
				RegistryFactory.getRegistry().getConfigurationElementsFor(GENERATOR_EP_ID);
		for (IConfigurationElement extension : extensions) {
			generators.add(new GeneratorDesc(extension.getAttribute("id"),
					extension.getAttribute("Name")));
		}
	}

	public List<GeneratorDesc> getGenerators() {
		return generators;
	}

	/**
	 * @param id the id of the extension.
	 * @return ISiteGenerator instance
	 */
	public ISiteGenerator createGenerator(String id) {
		IConfigurationElement[] extensions = 
				RegistryFactory.getRegistry().getConfigurationElementsFor(GENERATOR_EP_ID);
		try {
			for (IConfigurationElement extension : extensions) {
				if(id.equals(extension.getAttribute("id"))){
					return (ISiteGenerator) extension.createExecutableExtension("class");
				}
			}
		} catch (CoreException e) {
			log.error(e.getStackTrace().toString());
			e.printStackTrace();
		}
		return null;
	}

}
