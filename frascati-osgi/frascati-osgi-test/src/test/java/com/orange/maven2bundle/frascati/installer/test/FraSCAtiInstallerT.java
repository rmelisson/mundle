package com.orange.maven2bundle.frascati.installer.test;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.felix.framework.FrameworkFactory;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;

import com.google.common.io.Files;
import com.orange.maven2bundle.installer.exception.MavenArtifactUnavailableException;
import com.orange.maven2bundle.installer.maven.MavenFacilities;

public class FraSCAtiInstallerT {

	@Test
	public void testFraSCAtiInstaller() throws BundleException, IOException, MavenArtifactUnavailableException {

			// -- OGSi platform creation
		
			// we start the Felix OSGi framework
			String cachePath = System.getProperty("java.io.tmpdir") + "/felix-cache";
			File cacheDirectory = new File(cachePath);
			FrameworkFactory ff = new FrameworkFactory();
			Properties configurationProperties = new Properties();
			configurationProperties.setProperty(Constants.FRAMEWORK_STORAGE, cacheDirectory.getAbsolutePath());
			
			Framework fm = ff.newFramework(configurationProperties);
			fm.init();
			fm.start();
			BundleContext bundleContext = fm.getBundleContext();
			
			// we remove all bundle 
			for (Bundle bundle : bundleContext.getBundles()){
				if ( ! (bundle.getSymbolicName().equals("org.apache.felix.framework")) ) {
					bundle.stop();
					bundle.uninstall();
				}
			}
			Files.deleteDirectoryContents(new File(cachePath));
			
			// --
			
			
			// we first deploy the mundle installer
			String mundlePath = "file:///home/remi/dev/osgi/workspace/maven2bundle/bundle-installer/target/bundle-installer-0.0.1.SNAPSHOT-osgi.jar"; 
			bundleContext.installBundle(mundlePath).start();

			// then we deploy the required eclipse bundles
			String eclipsePath = "file:///home/remi/dev/osgi/workspace/maven2bundle/frascati-osgi/frascati-bundles/eclipse/eclipse.jar";
			bundleContext.installBundle(eclipsePath).start();
			
			// and juliac runtime (problem with DynamicImport-Package, because a lot is now contained into tinfi runtime membrane oo)
//			String juliacR = "file:///home/remi/dev/osgi/workspace/maven2bundle/frascati-osgi/frascati-bundles/juliacr/juliacr.jar";
//			bundleContext.installBundle(juliacR).start();

			// FraSCAti all, quand y'en a marre...
			String frascatiA = "file:///home/remi/dev/osgi/workspace/maven2bundle/frascati-osgi/frascati-bundles/frascatia/frascatia.jar";
			bundleContext.installBundle(frascatiA).start();
			
			// then we try to deploy the FraSCAti installer
			MavenFacilities mF = new MavenFacilities("/home/remi/.m2/repository");
			String fIPath = mF.getMavenArtifactFile("com.orange:frascati-installer:0.0.1.SNAPSHOT").toURI().toString();
			Bundle fraSCAtiInstaller = bundleContext.installBundle(fIPath);
			
			fraSCAtiInstaller.start();
			
			// finally we assert that FraSCAti is deployed
			for (Bundle bundle : bundleContext.getBundles()){
				System.out.println(bundle.getSymbolicName());
			}
	}
	
}
