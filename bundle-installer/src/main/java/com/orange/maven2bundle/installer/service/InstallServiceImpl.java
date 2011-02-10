package com.orange.maven2bundle.installer.service;

import java.io.File;

import com.orange.maven2bundle.installer.exception.ArtifactInstallationException;
import com.orange.maven2bundle.installer.maven.MavenFacilities;
import com.orange.maven2bundle.installer.osgi.OSGiFacilities;
import com.orange.maven2bundle.installer.osgi.OSGiManifest;

public class InstallServiceImpl implements InstallService {
		
	private MavenFacilities mavenFacilities;
	private OSGiFacilities oSGiFacilities;

	public InstallServiceImpl(MavenFacilities mavenFacilities) {
		this.mavenFacilities = mavenFacilities;
//		this.oSGiFacilities = new OSGiFacilities(null);
	}
	
	public void installMavenArtifactAsBundle(String artifactCoordinates)
			throws ArtifactInstallationException{
		
		try {
			// we retrieve the artifact file
			File artifactFile = mavenFacilities.getMavenArtifactFile(artifactCoordinates);
			
			// then, the manifest
			OSGiManifest manifest = oSGiFacilities.getOSGiManifest(artifactFile);
					
			// we check the non available import-package
			
			
			// and try to find them into the maven dependencies
			
			// then we deploy the tree of OSGi manifest
		
		} catch (Exception e){
			throw new ArtifactInstallationException(e);
		}
				
	}

}
