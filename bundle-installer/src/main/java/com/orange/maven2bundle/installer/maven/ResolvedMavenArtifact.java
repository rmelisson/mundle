package com.orange.maven2bundle.installer.maven;

import org.sonatype.aether.repository.LocalArtifactResult;
import org.sonatype.aether.resolution.ArtifactDescriptorResult;

public class ResolvedMavenArtifact {

	private LocalArtifactResult localArtifactResult;
	private ArtifactDescriptorResult artifactDescriptorResult;

	public ResolvedMavenArtifact(LocalArtifactResult lAR, ArtifactDescriptorResult aDR){
		this.setLocalArtifactResult(lAR);
		this.setArtifactDescriptorResult(aDR); 
	}

	private void setLocalArtifactResult(LocalArtifactResult localArtifactResult) {
		this.localArtifactResult = localArtifactResult;
	}

	public LocalArtifactResult getLocalArtifactResult() {
		return localArtifactResult;
	}

	private void setArtifactDescriptorResult(ArtifactDescriptorResult artifactDescriptorResult) {
		this.artifactDescriptorResult = artifactDescriptorResult;
	}

	public ArtifactDescriptorResult getArtifactDescriptorResult() {
		return artifactDescriptorResult;
	}
	
	
}
