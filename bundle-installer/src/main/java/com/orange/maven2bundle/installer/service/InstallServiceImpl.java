package com.orange.maven2bundle.installer.service;

import com.orange.maven2bundle.installer.core.DependencyNode;
import com.orange.maven2bundle.installer.core.Deployer;
import com.orange.maven2bundle.installer.core.Resolver;
import com.orange.maven2bundle.installer.exception.ArtifactInstallationException;
import com.orange.maven2bundle.installer.osgi.MundleOSGiManifest;
import com.orange.maven2bundle.installer.test.Resources;

public class InstallServiceImpl implements InstallService {
		
	private Resolver resolver;
	private Deployer deployer;

	public InstallServiceImpl(Resolver resolver, Deployer deployer) {
		this.resolver = resolver;
		this.deployer = deployer;
	}
	
	public void installMavenArtifactAsBundle(String artifactCoordinates)
			throws ArtifactInstallationException {
		try {
			MundleOSGiManifest rootManifest = resolver.createRootManifest(artifactCoordinates);
			DependencyNode rootNode = resolver.resolveDependencyTree(rootManifest);
			deployer.installNode(rootNode);
			
		} catch (Exception e) {
			throw new ArtifactInstallationException(e);
		}			
	}

}
