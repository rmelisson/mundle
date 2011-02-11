package com.orange.maven2bundle.installer.core.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleException;

import com.orange.maven2bundle.installer.core.DependencyNode;
import com.orange.maven2bundle.installer.core.Resolver;
import com.orange.maven2bundle.installer.exception.BndException;
import com.orange.maven2bundle.installer.exception.DependencyNodeException;
import com.orange.maven2bundle.installer.exception.MavenArtifactUnavailableException;
import com.orange.maven2bundle.installer.maven.MavenFacilities;
import com.orange.maven2bundle.installer.osgi.MundleOSGiManifest;
import com.orange.maven2bundle.installer.osgi.OSGiFacilities;
import com.orange.maven2bundle.installer.osgi.OSGiManifest;
import com.orange.maven2bundle.installer.test.Resources;

public class ResolverTest {
	
	private MavenFacilities mavenFacilities;
	private OSGiFacilities oSGiFacilities;
	
	private Resolver resolver;
	
	public ResolverTest() throws BundleException{
		this.mavenFacilities = new MavenFacilities(Resources.testingRepositoryRootPath);;
		this.oSGiFacilities = new OSGiFacilities(Resources.initBundleTestingContext());
	}
	
	@Before
	public void initResolver(){
		this.resolver = new Resolver(mavenFacilities, oSGiFacilities);
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
	public void testResolveDefaultArtifact() throws IOException, BndException{
		try {
			MundleOSGiManifest rootManifest = resolver.createRootManifest(Resources.DefaultArtifactCoordinates);
			assertNotNull(rootManifest);
			DependencyNode rootNode = resolver.resolveDependencyTree(rootManifest);
			assertTrue(rootNode.getDependencies().size() == 3);			
		} catch (MavenArtifactUnavailableException e){
			e.printStackTrace();
			fail();
		} catch (DependencyNodeException e) {
			e.printStackTrace();
			fail();
		}
	}
	
//	@Test
	public void testFraSCAti() throws IOException, BndException, BundleException{
		try {
			MundleOSGiManifest rootManifest = resolver.createRootManifest("org.ow2.frascati:frascati-sca-parser:1.3");
//			deployEclipseRuntime();
//			MundleOSGiManifest rootManifest = resolver.createRootManifest("org.eclipse.stp.sca:sca-model:2.0.1.2");
//			MundleOSGiManifest rootManifest = resolver.createRootManifest("org.eclipse.core:runtime:3.4.0");
			
			assertNotNull(rootManifest);
			DependencyNode rootNode = resolver.resolveDependencyTree(rootManifest);
			System.out.println(rootNode.getDependencies().size());
//			assertTrue(rootNode.getDependencies().size() == 3);			
		} catch (MavenArtifactUnavailableException e){
			e.printStackTrace();
			fail();
		} catch (DependencyNodeException e) {
			System.out.println(e.getMessage());
			fail();
		}
	}
	
	public void deployEclipseRuntime() throws MavenArtifactUnavailableException, IOException, BndException, BundleException{
		String coreCoords = "org.eclipse.core:runtime:3.4.0";
		MundleOSGiManifest rootManifest = resolver.createRootManifest(coreCoords);
		File file = mavenFacilities.getMavenArtifactFile(coreCoords);
		oSGiFacilities.deployMundle(file);
	}
	
}
