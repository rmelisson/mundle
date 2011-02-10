package com.orange.maven2bundle.installer.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sonatype.aether.artifact.Artifact;

import com.orange.maven2bundle.installer.exception.BndException;
import com.orange.maven2bundle.installer.exception.MavenArtifactUnavailableException;
import com.orange.maven2bundle.installer.maven.MavenFacilities;
import com.orange.maven2bundle.installer.osgi.MumbleOSGiManifest;
import com.orange.maven2bundle.installer.osgi.OSGiFacilities;
import com.orange.maven2bundle.installer.osgi.OSGiManifest;

public class Resolver {
	
	private OSGiFacilities oSGIFacilities;
	private MavenFacilities mavenFacilities;

	public Resolver(MavenFacilities mavenFacilities, OSGiFacilities oSGiFacilities){
		this.mavenFacilities = mavenFacilities;
		this.oSGIFacilities = oSGiFacilities;
	}
	
	public OSGiManifest createRootManifest(String artifactcoordinates) throws MavenArtifactUnavailableException, IOException, BndException {
		File artifactFile = mavenFacilities.getMavenArtifactFile(artifactcoordinates);
		OSGiManifest manifest = oSGIFacilities.getOSGiManifest(artifactFile);
		if (manifest instanceof MumbleOSGiManifest){
			Artifact artifact = mavenFacilities.getArtifact(artifactcoordinates);
			((MumbleOSGiManifest) manifest).setArtifact(artifact);
		}
		
		return manifest;
	}

	public List<OSGiManifest> resolveDependencies(OSGiManifest manifest) {
		
		ArrayList<OSGiManifest> list = new ArrayList<OSGiManifest>(); 
		
		if (manifest instanceof MumbleOSGiManifest){
			
		}
		
		return list;
	}
}
