package com.orange.maven2bundle.installer.service.test;

import org.junit.Test;

import com.orange.maven2bundle.installer.service.InstallService;
import com.orange.maven2bundle.installer.service.ServicesFactory;

public class InstallServicesTest {

	private InstallService installService;

	public InstallServicesTest(){
		ServicesFactory sF = new ServicesFactory("");
		installService = sF.initInstallService();
	}	
	
	@Test
	public void test(){
		
	}
	/*
	@Test
	public void testInstallWithNonValidName() {
				
		// we try to launch an artifact with a non valid name
		try {
			installService.installMavenArtifactAsBundle("blabla");
			fail();
		} catch (ArtifactInstallationException e) {
			// To nothing, expected error
			assertTrue(e.getCause() instanceof IllegalArgumentException);
		}
				
	}
	
	@Test
	public void testInstallNonExistingArtifact(){
		String nonExistingArtifactCoordinates = "com.orange.blabla:artifact-blabla:0.0.1-SNAPSHOT";
		
		// we try to launch an unknown artifact
		try {
			installService.installMavenArtifactAsBundle(nonExistingArtifactCoordinates);
			fail();
		} catch (ArtifactInstallationException e) {
			// To nothing, expected error			
		}
	}
	
	@Test
	public void testExistingArtifact(){
		String qualifiedArtifactName = "com.orange.maven2bundle:artifact-example:0.0.1-SNAPSHOT";
		try {
			installService.installMavenArtifactAsBundle(qualifiedArtifactName);
			
			// here we should test that the artifact has been resolved and deployed
			
			
		} catch (ArtifactInstallationException e){
			e.printStackTrace();
			fail();
		}
	}*/
}
