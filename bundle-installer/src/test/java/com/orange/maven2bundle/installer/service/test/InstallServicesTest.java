package com.orange.maven2bundle.installer.service.test;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.osgi.framework.BundleException;

import com.orange.maven2bundle.installer.exception.ArtifactInstallationException;
import com.orange.maven2bundle.installer.service.InstallService;
import com.orange.maven2bundle.installer.service.ServicesFactory;
import com.orange.maven2bundle.installer.test.TResources;

public class InstallServicesTest {

	private InstallService installService;

	public InstallServicesTest() throws BundleException{
		ServicesFactory sF = new ServicesFactory(TResources.getLocalRepositoryLocation(), TResources.initBundleTestingContext());
		installService = sF.initInstallService();
	}	
	
	@Test
	public void testInstall() {
		try {
			installService.installMavenArtifactAsBundle(TResources.DefaultArtifactCoordinates);
		} catch (ArtifactInstallationException e) {
			fail();
		}
	}
}
