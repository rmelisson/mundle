package com.orange.maven2bundle.installer;

import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.orange.maven2bundle.installer.service.InstallService;
import com.orange.maven2bundle.installer.service.ServicesFactory;

public class Activator implements BundleActivator {

	public InstallService installService;
	
	public void start(BundleContext bundleContext) throws Exception {
		String mavenRepositoryLocation = Resources.getLocalRepositoryLocation();
		
		ServicesFactory servicesFactory = new ServicesFactory(mavenRepositoryLocation , bundleContext);
		installService = servicesFactory.initInstallService();
		
		// we deploy the install service as an OSGi service
		bundleContext.registerService(InstallService.class.getName(), installService, new Properties());
		
	}

	public void stop(BundleContext bundleContext) {
		
	}

}
