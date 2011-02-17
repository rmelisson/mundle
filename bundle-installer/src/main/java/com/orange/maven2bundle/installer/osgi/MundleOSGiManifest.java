package com.orange.maven2bundle.installer.osgi;

import java.io.File;
import java.util.jar.Manifest;

import org.sonatype.aether.artifact.Artifact;

public class MundleOSGiManifest extends OSGiManifest {

	private Artifact artifact;
	private File artifactFile;
	private boolean hasBeenGenerated;

	public MundleOSGiManifest(Manifest manifest, boolean hasBeenGenerated) {
		super(manifest);
		this.hasBeenGenerated = hasBeenGenerated;
	}

	// FIXME should be in the constructor
	public void setArtifact(Artifact artifact, File artifactFile) {
		this.artifact = artifact;
		this.artifactFile = artifactFile;
		this.getManifest().getMainAttributes().putValue("Bundle-SymbolicName", artifact.getArtifactId()+"-"+artifact.getBaseVersion());
	}
	
	public String getSymbolicName(){
		return this.getManifest().getMainAttributes().getValue("Bundle-SymbolicName");
	}
	
	public Artifact getArtifact(){
		return this.artifact;
	}
	
	public File getArtifactFile(){
		return this.artifactFile;
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
