package com.orange.maven2bundle.installer;

import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.orange.maven2bundle.installer.service.InstallService;
import com.orange.maven2bundle.installer.service.ServicesFactory;

public class Activator implements BundleActivator {

	// FIXME should not be defined here
	public static String mavenRepositoryLocation = "/home/remi/.m2/repository/";
	
	public void start(BundleContext bundleContext) {
		ServicesFactory servicesFactory = new ServicesFactory(mavenRepositoryLocation, bundleContext);
		InstallService installService = servicesFactory.initInstallService();
		
		// we deploy the install service as an OSGi service
		bundleContext.registerService(InstallService.class.getName(), installService, new Properties());
		
	}

	public void stop(BundleContext bundleContext) {

	}

}
