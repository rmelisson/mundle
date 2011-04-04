package com.orange.maven2bundle.installer.maven.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.jar.Manifest;

import org.junit.Before;
import org.junit.Test;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.resolution.ArtifactDescriptorException;

import com.orange.maven2bundle.installer.exception.MavenArtifactUnavailableException;
import com.orange.maven2bundle.installer.maven.MavenFacilities;
import com.orange.maven2bundle.installer.osgi.OSGiFacilities;
import com.orange.maven2bundle.installer.osgi.OSGiManifest;
import com.orange.maven2bundle.installer.test.TResources;

public class MavenFacilitiesTest {

	private MavenFacilities mavenFacilities;
	
	@Before
	public void initMavenFacilities() throws Exception {
		mavenFacilities = new MavenFacilities(TResources.getLocalRepositoryLocation());
	}
	
	/*
	@Test
	public void testContructorWithUnknownLocalRepository(){
		try {
			MavenFacilities mF = new MavenFacilities("42");
			mavenFacilities.getArtifactDescriptor("42");
			fail();
		} catch (Exception e) {}
	}*/
	
	@Test
	public void testGetDescriptorWithIncorrectCoords() throws ArtifactDescriptorException {
		try {
			mavenFacilities.isAvailable("42");
			fail();
		} catch (IllegalArgumentException e){}
	}
	
	@Test
	public void testGetDescriptorFromNotInstalledArtifact(){
		assertFalse(mavenFacilities.isAvailable(TResources.imaginaryArtifactCoordinates));			
	}
	
	@Test
	public void testExistingArtifact(){
		// we also measure time in order to assert that it has been mapped
		
		Date start = new Date();
		assertTrue(mavenFacilities.isAvailable(TResources.DefaultArtifactCoordinates));
		long timeFirstCall = (new Date()).getTime() - start.getTime();

		start = new Date();
		assertTrue(mavenFacilities.isAvailable(TResources.DefaultArtifactCoordinates));
		long timeSecondCall = (new Date()).getTime() - start.getTime();
		assertTrue(timeSecondCall < timeFirstCall);
	}
	
	@Test
	public void testExistingJarFile(){
		try {
			mavenFacilities.getMavenArtifactFile(TResources.DefaultArtifactCoordinates);
		} catch (MavenArtifactUnavailableException e) {
			fail();
		}
	}
	
	@Test
	public void testGetterForNonExisting(){
		try {
			mavenFacilities.getMavenArtifactFile(TResources.imaginaryArtifactCoordinates);
			fail();
		} catch (MavenArtifactUnavailableException e) {}
		
		try {
			mavenFacilities.getDependencies(TResources.imaginaryArtifactCoordinates);
			fail();
		} catch (MavenArtifactUnavailableException e) {}
		
	}
	
	@Test
	public void testDependencies(){
		try {
			List<Dependency> deps = mavenFacilities.getDependencies(TResources.DefaultArtifactCoordinates);
			assertTrue(deps.size() > 0);
		} catch (MavenArtifactUnavailableException e) {
			fail();
		}
	}
	
	/*
	@Test
	public void printer() throws MavenArtifactUnavailableException{
		File f = mavenFacilities.getMavenArtifactFile("org.eclipse.emf.ecore:xmi:2.4.0");
		System.out.println(f.getAbsolutePath());
	}
	
	
	public void blabla() throws Exception {
		File f = mavenFacilities.getMavenArtifactFile(Resources.ArtifactWithOSGiManifestCoordinates);
		Manifest m = (new OSGiFacilities(null)).getOSGiManifest(f).getManifest();
		System.out.println("\n----");
		for (Entry<?, ?> e : m.getMainAttributes().entrySet()){
			System.out.println(e.getKey().toString() +": " + e.getValue().toString());
		}
		System.out.println("----\n");
	}
	*/
}
