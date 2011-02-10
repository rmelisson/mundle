package com.orange.maven2bundle.installer.osgi;

import java.util.jar.Manifest;

import org.sonatype.aether.artifact.Artifact;

public class MundleOSGiManifest extends OSGiManifest {

	private Artifact artifact;
	private boolean hasBeenGenerated;

	public MundleOSGiManifest(Manifest manifest, boolean hasBeenGenerated) {
		super(manifest);
		this.hasBeenGenerated = hasBeenGenerated;
	}

	public void setArtifact(Artifact artifact) {
		this.artifact= artifact;
	}
	
	public Artifact getArtifact(){
		return this.artifact;
	}
	
	public boolean hasBeenGenerated(){
		return this.hasBeenGenerated;
	}

	public boolean export(String packageName) {
		for (String export : exportPackages){
			//FIXME we should resolve version, ignore uses...
			if (export.startsWith(packageName)){
				return true;
			}
		}
			
		return false;
	}

}