package com.orange.maven2bundle.installer.exception;

public class MavenArtifactUnavailableException extends Exception {

	private static final long serialVersionUID = 6820151195162189079L;

	public MavenArtifactUnavailableException(String artifactCoordinates){
		super("Artifact with coordinates : " + artifactCoordinates + " cannot be found.");
	}
}
