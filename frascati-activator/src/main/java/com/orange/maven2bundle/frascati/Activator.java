package com.orange.maven2bundle.frascati;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.ow2.frascati.OSGiFraSCAti;

public class Activator implements BundleActivator {
	
	private OSGiFraSCAti fraSCAti;

	public void start(BundleContext bundleContext) throws Exception {
		
		BundleContext afBundleContext = null;
		for (Bundle bundle : bundleContext.getBundles()){
			if (bundle.getSymbolicName().equals("frascati-assembly-factory-1.4-SNAPSHOT")){
				// assembly factory bundle context
				afBundleContext = bundle.getBundleContext();
				break;
			}
		}
		
		if (afBundleContext == null){
			throw new Exception("Assembly Factory bundle cannot be found");
		}
		this.fraSCAti = OSGiFraSCAti.newFraSCAti(afBundleContext);
	}

	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub	
		
	}

}
