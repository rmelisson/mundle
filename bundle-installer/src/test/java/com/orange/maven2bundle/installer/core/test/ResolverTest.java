package com.orange.maven2bundle.installer.core.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleException;

import com.orange.maven2bundle.installer.core.DependencyNode;
import com.orange.maven2bundle.installer.core.Resolver;
import com.orange.maven2bundle.installer.exception.BndException;
import com.orange.maven2bundle.installer.exception.MavenArtifactUnavailableException;
import com.orange.maven2bundle.installer.exception.UnresolvedDependencyException;
import com.orange.maven2bundle.installer.maven.MavenFacilities;
import com.orange.maven2bundle.installer.osgi.MundleOSGiManifest;
import com.orange.maven2bundle.installer.osgi.OSGiFacilities;
import com.orange.maven2bundle.installer.osgi.OSGiManifest;
import com.orange.maven2bundle.installer.test.Resources;

public class ResolverTest {
	
	private MavenFacilities mavenFacilities;
	private OSGiFacilities osGiFacilities;
	
	private Resolver resolver;
	
	public ResolverTest() throws BundleException{
		this.mavenFacilities = new MavenFacilities(Resources.testingRepositoryRootPath);;
		this.osGiFacilities = new OSGiFacilities(Resources.initBundleTestingContext());
	}
	
	@Before
	public void initResolver(){
		this.resolver = new Resolver(mavenFacilities, osGiFacilities);
	}
	
	@Test
	public void testInitRoot() throws MavenArtifactUnavailableException, IOException, BndException{
		OSGiManifest manifest = resolver.createRootManifest(Resources.DefaultArtifactCoordinates);
		assertTrue(manifest instanceof MundleOSGiManifest);
		assertTrue(((MundleOSGiManifest) manifest).getArtifact().getGroupId().equals(Resources.GroupId));
	}
	
	@Test
	public void testInitRootOSGiArtifact() throws MavenArtifactUnavailableException, IOException, BndException{
		MundleOSGiManifest manifest = resolver.createRootManifest(Resources.ArtifactWithOSGiManifestCoordinates);
		assertFalse(manifest.hasBeenGenerated());
	}
	
	@Test
	public void testResolveDefaultArtifact() throws MavenArtifactUnavailableException, IOException, BndException{
		MundleOSGiManifest rootManifest = resolver.createRootManifest(Resources.DefaultArtifactCoordinates);
		assertNotNull(rootManifest);
		try {
			DependencyNode rootNode = resolver.resolveDependencyTree(rootManifest);
			assertTrue(rootNode.getDependencies().size() == 3);			
		} catch (UnresolvedDependencyException e){
			e.printStackTrace();
			fail();
		}
	}
	
}
