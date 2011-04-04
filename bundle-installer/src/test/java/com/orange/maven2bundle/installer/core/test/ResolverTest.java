package com.orange.maven2bundle.installer.core.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import com.orange.maven2bundle.installer.core.DependencyNode;
import com.orange.maven2bundle.installer.core.Resolver;
import com.orange.maven2bundle.installer.exception.BndException;
import com.orange.maven2bundle.installer.exception.DependencyNodeException;
import com.orange.maven2bundle.installer.exception.MavenArtifactUnavailableException;
import com.orange.maven2bundle.installer.maven.MavenFacilities;
import com.orange.maven2bundle.installer.osgi.MundleOSGiManifest;
import com.orange.maven2bundle.installer.osgi.OSGiFacilities;
import com.orange.maven2bundle.installer.test.TResources;

public class ResolverTest {
	
	private MavenFacilities mavenFacilities;
	private OSGiFacilities oSGiFacilities;
	private Resolver resolver;
	private BundleContext bundleContext;
	
	public ResolverTest() throws BundleException{
		this.bundleContext = TResources.initBundleTestingContext();
		this.mavenFacilities = new MavenFacilities(TResources.getLocalRepositoryLocation());;
		this.oSGiFacilities = new OSGiFacilities(this.bundleContext);
	}
	
	@Before
	public void reinitResolver() throws BundleException, IOException{
		TResources.cleanCache(bundleContext);
		this.resolver = new Resolver(mavenFacilities, oSGiFacilities);
	}
	
	@Test
	public void testInitRoot() throws MavenArtifactUnavailableException, IOException, BndException{
		MundleOSGiManifest manifest = resolver.createRootManifest(TResources.DefaultArtifactCoordinates);
		assertTrue(manifest.getArtifact().getGroupId().equals(TResources.GroupId));
		assertNotNull(manifest.getArtifactFile());
	}
	
	@Test
	public void testInitRootOSGiArtifact() throws MavenArtifactUnavailableException, IOException, BndException{
		MundleOSGiManifest manifest = resolver.createRootManifest(TResources.ArtifactWithOSGiManifestCoordinates);
		assertFalse(manifest.hasBeenGenerated());
	}
	
	@Test
	public void testResolveDefaultArtifact() throws IOException, BndException{
		try {
			MundleOSGiManifest rootManifest = resolver.createRootManifest(TResources.DefaultArtifactCoordinates);
			assertNotNull(rootManifest);
			DependencyNode rootNode = resolver.resolveDependencyTree(rootManifest);
			assertTrue(rootNode.getDependencies().size() == 2);			
		} catch (MavenArtifactUnavailableException e){
			e.printStackTrace();
			fail();
		} catch (DependencyNodeException e) {
			e.printStackTrace();
			fail();
		}
	}
	
}
