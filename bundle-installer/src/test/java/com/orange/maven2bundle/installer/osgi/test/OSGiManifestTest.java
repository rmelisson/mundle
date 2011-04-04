package com.orange.maven2bundle.installer.osgi.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import com.orange.maven2bundle.installer.exception.BndException;
import com.orange.maven2bundle.installer.exception.MavenArtifactUnavailableException;
import com.orange.maven2bundle.installer.maven.MavenFacilities;
import com.orange.maven2bundle.installer.osgi.MundleOSGiManifest;
import com.orange.maven2bundle.installer.osgi.OSGiFacilities;
import com.orange.maven2bundle.installer.test.TResources;

public class OSGiManifestTest {

	private MavenFacilities manifestFacilities;
	private BundleContext bundleContext;

	public OSGiManifestTest() throws BundleException{
		manifestFacilities = new MavenFacilities(TResources.getLocalRepositoryLocation());	
		bundleContext = TResources.initBundleTestingContext();
	}
	
	@Test
	public void testSimpleOSGiBundle() throws Exception{
		File f = manifestFacilities.getMavenArtifactFile(TResources.ArtifactWithOSGiManifestCoordinates);
		
		MundleOSGiManifest oM = (new OSGiFacilities(bundleContext)).getOSGiManifest(f);
		
		assertFalse(oM.hasBeenGenerated());
		
		assertTrue(oM.getImportPackages().size() == TResources.ArtifactWithOSGiManifestImportPackageNumber);
		assertTrue(oM.getExportPackages().size()== TResources.ArtifactWithOSGiManifestExportPackageNumber);
		
	}
	
	@Test
	public void testMumbleManifest() throws MavenArtifactUnavailableException, IOException, BndException {
		File f = manifestFacilities.getMavenArtifactFile(TResources.DefaultArtifactCoordinates);
		
		MundleOSGiManifest oM = (new OSGiFacilities(bundleContext)).getOSGiManifest(f);
		
		assertTrue(oM.hasBeenGenerated());
		assertTrue(oM.getImportPackages().size() == TResources.DefaultArtifactImportPackageNumber);
		assertTrue(oM.getExportPackages().size()== TResources.DefaultArtifactExportPackageNumber);
	}
	
	//TODO add more cases
}
