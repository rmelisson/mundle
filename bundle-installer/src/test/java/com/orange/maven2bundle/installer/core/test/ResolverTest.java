package com.orange.maven2bundle.installer.core.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.orange.maven2bundle.installer.core.Resolver;
import com.orange.maven2bundle.installer.exception.BndException;
import com.orange.maven2bundle.installer.exception.MavenArtifactUnavailableException;
import com.orange.maven2bundle.installer.maven.MavenFacilities;
import com.orange.maven2bundle.installer.osgi.MumbleOSGiManifest;
import com.orange.maven2bundle.installer.osgi.OSGiFacilities;
import com.orange.maven2bundle.installer.osgi.OSGiManifest;
import com.orange.maven2bundle.installer.test.Resources;

public class ResolverTest {
	
	private MavenFacilities mavenFacilities;
	private OSGiFacilities osGiFacilities;
	
	private Resolver resolver;
	
	public ResolverTest(){
		this.mavenFacilities = new MavenFacilities(Resources.testingRepositoryRootPath);;
		this.osGiFacilities = new OSGiFacilities(null);
	}
	
	@Before
	public void initResolver(){
		this.resolver = new Resolver(mavenFacilities, osGiFacilities);
	}
	
	@Test
	public void testInitRoot() throws MavenArtifactUnavailableException, IOException, BndException{
		OSGiManifest manifest = resolver.createRootManifest(Resources.DefaultArtifactCoordinates);
		assertTrue(manifest instanceof MumbleOSGiManifest);
		assertTrue(((MumbleOSGiManifest) manifest).getArtifact().getGroupId().equals(Resources.GroupId));
	}
	
	@Test
	public void testInitRootOSGiArtifact() throws MavenArtifactUnavailableException, IOException, BndException{
		OSGiManifest manifest = resolver.createRootManifest(Resources.ArtifactWithOSGiManifestCoordinates);
		assertFalse(manifest instanceof MumbleOSGiManifest);
	}
	
	@Test
	public void testResolveDefaultArtifact() throws MavenArtifactUnavailableException, IOException, BndException{
		OSGiManifest manifest = resolver.createRootManifest(Resources.DefaultArtifactCoordinates);
		List<OSGiManifest> list = resolver.resolveDependencies(manifest);
//		assertTrue(dependencies.size() == 3);
	}
	
}
