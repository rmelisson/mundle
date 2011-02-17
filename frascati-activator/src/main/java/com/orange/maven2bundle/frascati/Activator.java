package com.orange.maven2bundle.frascati;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.ow2.frascati.FraSCAti;

public class Activator implements BundleActivator {
	
	private FraSCAti fraSCAti;

	public void start(BundleContext arg0) throws Exception {
		this.fraSCAti = FraSCAti.newFraSCAti();
	}

	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
