package com.orange.maven2bundle.installer.service;

import org.osgi.framework.BundleContext;

import com.orange.maven2bundle.installer.core.Deployer;
import com.orange.maven2bundle.installer.core.Resolver;
import com.orange.maven2bundle.installer.maven.MavenFacilities;
import com.orange.maven2bundle.installer.osgi.OSGiFacilities;

public class ServicesFactory {

//	private BundleContext bundleContext;
//	private MavenFacilities mavenFacilities;
//	private OSGiFacilities oSGiFacilities;
	private Resolver resolver;
	private Deployer deployer;

	public ServicesFactory(String mavenLocalRepositoryLocation, BundleContext bundleContext){
		//this.bundleContext = bundleContext;
		MavenFacilities mavenFacilities = new MavenFacilities(mavenLocalRepositoryLocation);
		OSGiFacilities oSGiFacilities = new OSGiFacilities(bundleContext);
		resolver = new Resolver(mavenFacilities, oSGiFacilities);
		deployer = new Deployer(oSGiFacilities);
		
	}
	
	public InstallService initInstallService(){		
		return new InstallServiceImpl(resolver, deployer);
	}
}
