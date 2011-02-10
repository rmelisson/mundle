package com.orange.maven2bundle.installer.service;

import com.orange.maven2bundle.installer.exception.ArtifactInstallationException;
import com.orange.maven2bundle.installer.exception.MavenArtifactUnavailableException;

public interface InstallService {

	public void installMavenArtifactAsBundle(String artifactDescription)
			throws ArtifactInstallationException, MavenArtifactUnavailableException;

}
