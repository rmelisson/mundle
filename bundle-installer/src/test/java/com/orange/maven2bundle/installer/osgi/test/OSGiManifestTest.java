package com.orange.maven2bundle.installer.osgi.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.orange.maven2bundle.installer.exception.BndException;
import com.orange.maven2bundle.installer.exception.MavenArtifactUnavailableException;
import com.orange.maven2bundle.installer.maven.MavenFacilities;
import com.orange.maven2bundle.installer.osgi.MumbleOSGiManifest;
import com.orange.maven2bundle.installer.osgi.OSGiFacilities;
import com.orange.maven2bundle.installer.osgi.OSGiManifest;
import com.orange.maven2bundle.installer.test.Resources;


public class OSGiManifestTest {

	private MavenFacilities manifestFacilities;

	public OSGiManifestTest(){
		manifestFacilities = new MavenFacilities(Resources.testingRepositoryRootPath);	
	}
	
	@Test
	public void testSimpleOSGiBundle() throws Exception{
		File f = manifestFacilities.getMavenArtifactFile(Resources.ArtifactWithOSGiManifestCoordinates);
		
		OSGiManifest oM = (new OSGiFacilities(null)).getOSGiManifest(f);
		
		assertFalse(oM instanceof MumbleOSGiManifest);
		
		assertTrue(oM.getImportPackages().size() == Resources.ArtifactWithOSGiManifestImportPackageNumber);
		assertTrue(oM.getExportPackages().size()== Resources.ArtifactWithOSGiManifestExportPackageNumber);
		
	}
	
	@Test
	public void testMumbleManifest() throws MavenArtifactUnavailableException, IOException, BndException {
		File f = manifestFacilities.getMavenArtifactFile(Resources.DefaultArtifactCoordinates);
		
		OSGiManifest oM = (new OSGiFacilities(null)).getOSGiManifest(f);
		
		assertTrue(oM instanceof MumbleOSGiManifest);
		assertTrue(oM.getImportPackages().size() == Resources.DefaultArtifactImportPackageNumber);
		assertTrue(oM.getExportPackages().size()== Resources.DefaultArtifactExportPackageNumber);
	}
	
	//TODO add more cases
}
