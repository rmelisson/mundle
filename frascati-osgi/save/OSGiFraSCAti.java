package org.ow2.frascati;

import org.osgi.framework.BundleContext;
import org.ow2.frascati.util.FrascatiException;
import org.objectweb.fractal.api.Component;
import org.objectweb.fractal.api.factory.Factory;

public class OSGiFraSCAti extends FraSCAti {
	
	private static BundleContext bundleContext;
	
	public static final String OSGi_FRASCATI_CLASS_DEFAULT_VALUE =
      OSGiFraSCAti.class.getName();

	private Component frascatiComposite;
	
	protected OSGiFraSCAti(){
	}
	
	public static OSGiFraSCAti newFraSCAti(BundleContext bundleContext) throws FrascatiException
	  {
		OSGiFraSCAti.bundleContext  = bundleContext;
		  
		OSGiFraSCAti frascati;
	    try {
	    	frascati = loadAndInstantiate(
	          System.getProperty(FRASCATI_CLASS_PROPERTY_NAME, OSGi_FRASCATI_CLASS_DEFAULT_VALUE));
	    } catch (Exception exc) {
	      throw new FrascatiException("Cannot instantiate the OW2 FraSCAti class", exc);
	    }
	    frascati.initFrascatiCompositeMyWay();
	    return frascati;
	  }
	  
	  /**
	   * Load and instantiate a Java class.
	   */
	  @SuppressWarnings("unchecked")
	  protected static <T> T loadAndInstantiate(String classname)
	    throws  ClassNotFoundException, InstantiationException, IllegalAccessException
	  {
	    Class<T> clazz = bundleContext.getBundle().loadClass(classname);
	    return (T)clazz.newInstance();
	  }
  
	  /**
	   * Init the OW2 FraSCAti bootstrap composite.
	   * 
	   * @return The assembly factory component
	   * @throws FrascatiException 
	   */
	  protected final void initFrascatiCompositeMyWay() throws FrascatiException
	  {
	    // Instantiate the OW2 FraSCAti bootstrap factory.
	    Factory bootstrapFactory;
	    try {
	      bootstrapFactory = loadAndInstantiate(
	          System.getProperty(FRASCATI_BOOTSTRAP_PROPERTY_NAME, FRASCATI_BOOTSTRAP_DEFAULT_VALUE));
	    } catch (Exception exc) {
	      severe(new FrascatiException("Cannot instantiate the OW2 FraSCAti bootstrap class", exc));
	      return;
	    }

	    // Instantiate the OW2 FraSCAti bootstrap composite.
	    try {
	      this.frascatiComposite = bootstrapFactory.newFcInstance();
	    } catch (Exception exc) {
	      severe(new FrascatiException("Cannot instantiate the OW2 FraSCAti bootstrap composite", exc));
	      return;
	    }

	    // Start the OW2 FraSCAti bootstrap composite.
	    try {
	      startFractalComponent(this.frascatiComposite);
	    } catch (Exception exc) {
	      severe(new FrascatiException("Cannot start the OW2 FraSCAti Assembly Factory bootstrap composite", exc));
	      return;
	    }

	    // At this time, variable 'frascati' refers to the OW2 FraSCAti generated with Juliac.
	    // Now reload the OW2 FraSCAti composite with the OW2 FraSCAti bootstrap composite.
	    try {
	      this.frascatiComposite = getCompositeManager().getComposite(
	          System.getProperty(FRASCATI_COMPOSITE_PROPERTY_NAME, FRASCATI_COMPOSITE_DEFAULT_VALUE)
	          .replace('.', '/'));
	    } catch (Exception exc) {
	      severe(new FrascatiException("Cannot load the OW2 FraSCAti composite", exc));
	      return;
	    }

	    // At this time, variable 'frascati' refers to the OW2 FraSCAti composite
	    // dynamically loaded by the OW2 FraSCAti bootstrap composite.
	 
	    try {
	      // Add OW2 FraSCAti into its top level domain composite.
	      getCompositeManager().addComposite(this.frascatiComposite);
	    } catch(Exception exc) {
	      severe(new FrascatiException("Cannot add the OW2 FraSCAti composite", exc));
	      return;
	    }
	  }
}
