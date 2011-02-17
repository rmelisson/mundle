package com.orange.maven2bundle.installer.osgi.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import com.orange.maven2bundle.installer.exception.MavenArtifactUnavailableException;
import com.orange.maven2bundle.installer.maven.MavenFacilities;
import com.orange.maven2bundle.installer.osgi.MundleOSGiManifest;
import com.orange.maven2bundle.installer.osgi.OSGiFacilities;
import com.orange.maven2bundle.installer.test.Resources;

public class OSGiFacilitiesTest {
	
	private OSGiFacilities oSGiFacilities;
	private MavenFacilities manifestFacilities;
	private BundleContext bundleContext;
	
	public OSGiFacilitiesTest() throws BundleException {
		manifestFacilities = new MavenFacilities(Resources.testingRepositoryRootPath);	
		bundleContext = Resources.initBundleTestingContext();
	}
	
	@Before
	public void initOSGiFacilities() throws BundleException, IOException {
		Resources.cleanCache(bundleContext);
		oSGiFacilities = new OSGiFacilities(bundleContext);
	}
	
	@Test
	public void testGetOSGiManifest() throws Exception {
		
		// Case of an artifact with no OSGi manifest
		File f = manifestFacilities.getMavenArtifactFile(Resources.DefaultArtifactCoordinates);
		MundleOSGiManifest m = oSGiFacilities.getOSGiManifest(f);
		assertTrue(m.hasBeenGenerated());
		
		// Case of an artifact with an OSGi manifest		
		f = manifestFacilities.getMavenArtifactFile(Resources.ArtifactWithOSGiManifestCoordinates);
		m = oSGiFacilities.getOSGiManifest(f);
		assertFalse(m.hasBeenGenerated());
		
		// Case of an artifact with no manifest at all
		// TODO
	}

	@Test
	public void testAvailablePackages(){
		assertTrue(oSGiFacilities.isAvailable(Resources.shouldBeAvailablePackage));
		assertFalse(oSGiFacilities.isAvailable(Resources.shouldNotBeAvailablePackage));
	}
	
	@Test
	public void testDeployMundle() throws MavenArtifactUnavailableException{
		File file = manifestFacilities.getMavenArtifactFile(Resources.ArtifactWithOSGiManifestCoordinates);
		try {
			oSGiFacilities.deployMundle(file);
			assertTrue(bundleContext.getBundles().length > 1);
			assertTrue(oSGiFacilities.isAvailable(Resources.ArtifactWithOSGiManifestExportPackage));
		} catch (Exception e) {
			fail();
		}
		
	}
}
