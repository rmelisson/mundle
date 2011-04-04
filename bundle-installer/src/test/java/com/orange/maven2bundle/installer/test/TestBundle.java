package com.orange.maven2bundle.installer.test;

import java.io.File;
import java.util.Properties;
import java.util.ServiceLoader;

import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

import com.orange.maven2bundle.installer.exception.ArtifactInstallationException;
import com.orange.maven2bundle.installer.service.InstallService;

public class TestBundle {

	private BundleContext bundleContext;

	public TestBundle() throws BundleException{

		String tmpDir = System.getProperty("java.io.tmpdir") + File.separator;

		// -- OGSi platform creation

		// we start the OSGi framework
		String cachePath = tmpDir + "eclipse-cache";
		File cacheDirectory = new File(cachePath);
		// FrameworkFactory ff = new FrameworkFactory();
		FrameworkFactory ff = ServiceLoader.load(FrameworkFactory.class)
				.iterator().next();
		Properties configurationProperties = new Properties();
		configurationProperties.setProperty(Constants.FRAMEWORK_STORAGE,
				cacheDirectory.getAbsolutePath());
		configurationProperties.setProperty(Constants.FRAMEWORK_STORAGE_CLEAN,
				Constants.FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT);
		Framework fm = ff.newFramework(configurationProperties);
		fm.init();
		fm.start();
		this.bundleContext = fm.getBundleContext();

		// --
		
		// then we deploy the bundle installer
		bundleContext.installBundle("file://" + 
				"/home/remi/dev/osgi/workspace/maven2bundle/bundle-installer/" +
				"/target/bundle-installer-0.0.1.SNAPSHOT-osgi.jar")
				.start();
		
	}
	
	//@Test
	public void testInstallService() throws ArtifactInstallationException{
		
		ServiceReference serviceReference = bundleContext.getServiceReference(InstallService.class.getName());
		
		InstallService installService = (InstallService) bundleContext.getService(serviceReference);
		
		installService.installMavenArtifactAsBundle(TResources.DefaultArtifactCoordinates);
	}
	
	@Test
	public void test(){
		// waiting for real test...
	}
}
