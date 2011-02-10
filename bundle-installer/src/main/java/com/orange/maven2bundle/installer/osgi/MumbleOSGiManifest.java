package com.orange.maven2bundle.installer.osgi;

import java.util.jar.Manifest;

import org.sonatype.aether.artifact.Artifact;

public class MumbleOSGiManifest extends OSGiManifest {

	private Artifact artifact;

	public MumbleOSGiManifest(Manifest manifest) {
		super(manifest);
	}

	public void setArtifact(Artifact artifact) {
		this.artifact= artifact;
	}
	
	public Artifact getArtifact(){
		return this.artifact;
	}

}
