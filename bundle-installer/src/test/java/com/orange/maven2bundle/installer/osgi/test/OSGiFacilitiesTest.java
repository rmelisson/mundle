package com.orange.maven2bundle.installer.osgi.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.felix.framework.FrameworkFactory;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;

import com.orange.maven2bundle.installer.exception.MavenArtifactUnavailableException;
import com.orange.maven2bundle.installer.maven.MavenFacilities;
import com.orange.maven2bundle.installer.osgi.MumbleOSGiManifest;
import com.orange.maven2bundle.installer.osgi.OSGiFacilities;
import com.orange.maven2bundle.installer.osgi.OSGiManifest;
import com.orange.maven2bundle.installer.test.Resources;

public class OSGiFacilitiesTest {
	
	private OSGiFacilities oSGiFacilities;
	private MavenFacilities manifestFacilities;
	private BundleContext bundleContext;
	private long frameworkBundle;
	private final File cacheDirectory = new File(System.getProperty("java.io.tmpdir") + "/felix-cache");
	
	public OSGiFacilitiesTest() throws BundleException {
		manifestFacilities = new MavenFacilities(Resources.testingRepositoryRootPath);	
		
		FrameworkFactory ff = new FrameworkFactory();
		Properties configurationProperties = new Properties();
		configurationProperties.setProperty(Constants.FRAMEWORK_STORAGE, cacheDirectory.getAbsolutePath());
		
		Framework fm = ff.newFramework(configurationProperties);
		fm.init();
		fm.start();
		bundleContext = fm.getBundleContext();
	}
	
	@Before
	public void initOSGiFacilities() throws BundleException {
		
		// we remove all bundle 
		for (Bundle bundle : bundleContext.getBundles()){
			if ( ! (bundle.getBundleId() == frameworkBundle) ) {
				bundle.stop();
				bundle.uninstall();
			}
		}
		
		// and the Felix cache
		cacheDirectory.delete();
		
		oSGiFacilities = new OSGiFacilities(bundleContext);
	}
	
	@Test
	public void testGetOSGiManifest() throws Exception {
		
		// Case of an artifact with no OSGi manifest
		File f = manifestFacilities.getMavenArtifactFile(Resources.DefaultArtifactCoordinates);
		OSGiManifest m = oSGiFacilities.getOSGiManifest(f);
		assertTrue(m instanceof MumbleOSGiManifest);
		
		// Case of an artifact with an OSGi manifest		
		f = manifestFacilities.getMavenArtifactFile(Resources.ArtifactWithOSGiManifestCoordinates);
		m = oSGiFacilities.getOSGiManifest(f);
		assertFalse(m instanceof MumbleOSGiManifest);
		
		// Case of an artifact with no manifest at all
		// TODO
	}
	
	@Test
	public void tryInstallBundle() throws BundleException, MavenArtifactUnavailableException, IOException{
		
		File f = manifestFacilities.getMavenArtifactFile(Resources.ArtifactWithOSGiManifestCoordinates);
		oSGiFacilities.addPackagesExporterBundle(f);
		
		assertTrue(bundleContext.getBundles().length > 1);
	}

}
