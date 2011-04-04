package com.orange.maven2bundle.frascati.installer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.orange.maven2bundle.installer.service.InstallService;

public class Activator implements BundleActivator {

	// FIXME Should not be here
	public static String FraSCAtiActivatorCoords = "com.orange:frascati-activator:jar:0.0.1.SNAPSHOT";
	public static String FraSCAtiTinfiMembOO = "org.ow2.frascati.tinfi:frascati-tinfi-runtime-membranes-oo:1.4.3-SNAPSHOT";
	public static String FraSCAtiAssemblyFactoryCoords = "org.ow2.frascati:frascati-assembly-factory:1.4-SNAPSHOT";
	
	public void start(BundleContext bundleContext) throws Exception {
		// we retrieve the mundle service
		ServiceReference installServiceReference = bundleContext.getServiceReference(InstallService.class.getName());
		InstallService installService = (InstallService) bundleContext.getService(installServiceReference);
		
		// and try to use it in order to launch the FraSCAti activator
//		installService.installMavenArtifactAsBundle(FraSCAtiTinfiMembOO);
//		installService.installMavenArtifactAsBundle(FraSCAtiAssemblyFactoryCoords);
		installService.installMavenArtifactAsBundle(FraSCAtiActivatorCoords);

		// then, job is done and we can unget the service reference
		bundleContext.ungetService(installServiceReference);
	}

	public void stop(BundleContext arg0) throws Exception {
		
	}

}
