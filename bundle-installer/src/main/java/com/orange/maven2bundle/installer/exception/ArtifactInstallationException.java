package com.orange.maven2bundle.installer.exception;

public class ArtifactInstallationException extends Exception {

	private static final long serialVersionUID = 2658889160824525608L;
	
	public ArtifactInstallationException(Exception e) {
		this.initCause(e);
	}
	
}
