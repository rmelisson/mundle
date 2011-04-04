package com.orange.maven2bundle.installer.core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.graph.Dependency;

import com.orange.maven2bundle.installer.exception.BndException;
import com.orange.maven2bundle.installer.exception.DependencyNodeException;
import com.orange.maven2bundle.installer.exception.MavenArtifactUnavailableException;
import com.orange.maven2bundle.installer.exception.UnresolvedDependencyException;
import com.orange.maven2bundle.installer.maven.MavenFacilities;
import com.orange.maven2bundle.installer.osgi.MundleOSGiManifest;
import com.orange.maven2bundle.installer.osgi.OSGiFacilities;

public class Resolver {

	private OSGiFacilities oSGIFacilities;
	private MavenFacilities mavenFacilities;
	private HashSet<String> inProgressExportedPackage;

	public Resolver(MavenFacilities mavenFacilities,
			OSGiFacilities oSGiFacilities) {
		this.mavenFacilities = mavenFacilities;
		this.oSGIFacilities = oSGiFacilities;
		inProgressExportedPackage = new HashSet<String>();
	}

	public MundleOSGiManifest createRootManifest(String artifactcoordinates)
			throws MavenArtifactUnavailableException, IOException, BndException {
		File artifactFile = mavenFacilities
				.getMavenArtifactFile(artifactcoordinates);
		MundleOSGiManifest manifest = oSGIFacilities
				.getOSGiManifest(artifactFile);
		if (manifest instanceof MundleOSGiManifest) {
			Artifact artifact = mavenFacilities
					.getArtifact(artifactcoordinates);
			((MundleOSGiManifest) manifest).setArtifact(artifact, artifactFile);
		}
		return manifest;
	}

	public DependencyNode resolveDependencyTree(MundleOSGiManifest rootManifest)
			throws DependencyNodeException, MavenArtifactUnavailableException, IOException, BndException {

		List<MundleOSGiManifest> mundleOSGiManifestList = null;
		DependencyNode root = new DependencyNode(rootManifest);
		// for every import package dependency
		for (String importPackage : rootManifest.getImportPackages()) {
			
			// if we cannot find it into the OSGi environnement
			// or into the already resolved (but not already deployed) exports,
			// it means that it should possible to find this import in one of
			// its maven dependencies
			if (oSGIFacilities.isAvailable(importPackage)
					|| inProgressExportedPackage.contains(importPackage)
					|| shouldBeIgnored(importPackage)) {
				continue;
			} else {
				
				if (mundleOSGiManifestList == null) {
					mundleOSGiManifestList = constructOSGiManifestOfMavenDependencies(rootManifest.getArtifact());					
				}
				
				try {
					// we try to resolve the dependency
					MundleOSGiManifest dependencyManifest = getExporter(mundleOSGiManifestList, importPackage);
					// and of course, recursively...
					DependencyNode node	= resolveDependencyTree(dependencyManifest);
					
					root.append(node);
				}
				catch (UnresolvedDependencyException e){
					DependencyNodeException dne = new DependencyNodeException(e);
					dne.addNode(rootManifest.getArtifact().toString());
					throw dne;
				} catch (DependencyNodeException dne) {
					dne.addNode(rootManifest.getArtifact().toString());
				}
			}

			// in all case we remember this resolution for cyclic resolution
			// FIXME should we really do that ?
			inProgressExportedPackage.add(importPackage);
		}

		return root;
	}
	
	// FIXME
	public void initInProgress(MundleOSGiManifest manifest){
		for (String pack : manifest.getExportPackages()){
			int i = pack.indexOf(";");
			if (i > 0){
				pack = pack.substring(0, i);
			}
			System.out.println(pack);
			inProgressExportedPackage.add(pack);
		}
		
		//FIXME shouldn't be here, but we have to update OSGi exported packages before computation
		oSGIFacilities.updateAvailablePackage();
	}
	
	//FIXME create a configuration file for ignored packages
	private boolean shouldBeIgnored(String importPackage) {
		return importPackage.startsWith("sun.misc") ||
			importPackage.startsWith("javax") || 
			importPackage.equals("org.osgi.framework");
	}

	public List<MundleOSGiManifest> constructOSGiManifestOfMavenDependencies(Artifact artifact) throws MavenArtifactUnavailableException, IOException, BndException{
		List<MundleOSGiManifest> mundleOSGiManifest = new ArrayList<MundleOSGiManifest>();
		List<Dependency> mavenDependencies = mavenFacilities.getDependencies(artifact);

		for (Dependency dependency : mavenDependencies){
			if (dependency.getScope().equals("test")){
				continue;
			}
			String coordinates = dependency.getArtifact().toString();
			File file = mavenFacilities.getMavenArtifactFile(coordinates);
			MundleOSGiManifest manifest = oSGIFacilities.getOSGiManifest(file);
			manifest.setArtifact(mavenFacilities.getArtifact(coordinates), file);
			mundleOSGiManifest.add(manifest);
		}
		
		return mundleOSGiManifest;
	}

	public MundleOSGiManifest getExporter(List<MundleOSGiManifest> manifestList, String importPackage) throws UnresolvedDependencyException{
		for (MundleOSGiManifest manifest : manifestList){
			if (manifest.export(importPackage)) {
				inProgressExportedPackage.add(importPackage);
				return manifest;
			}
		}
		
		throw new UnresolvedDependencyException(importPackage);
	}
}
