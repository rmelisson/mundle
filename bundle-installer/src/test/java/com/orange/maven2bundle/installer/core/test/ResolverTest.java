package com.orange.maven2bundle.installer.core.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.codehaus.plexus.util.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import aQute.lib.osgi.Analyzer;

import com.google.common.io.Files;
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
	private long frameworkBundle;
	private Resolver resolver;
	private BundleContext bundleContext;
	
	public ResolverTest() throws BundleException{
		this.bundleContext = Resources.initBundleTestingContext();
		this.mavenFacilities = new MavenFacilities(Resources.testingRepositoryRootPath);;
		this.oSGiFacilities = new OSGiFacilities(this.bundleContext);
	}
	
	@Before
	public void reinitResolver() throws BundleException{
		// we remove all bundle 
		for (Bundle bundle : bundleContext.getBundles()){
			if ( ! (bundle.getBundleId() == frameworkBundle) ) {
				bundle.stop();
				bundle.uninstall();
			}
		}
		Resources.cleanCache();
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
	
	@Test
	public void testFraSCAti() throws IOException, BndException, BundleException{
		try {
			deployEclipseRuntime();
			deployEclipseSCAModel();
//			MundleOSGiManifest rootManifest = resolver.createRootManifest("org.eclipse.stp.sca:sca-model:2.0.1.2");
			MundleOSGiManifest rootManifest = resolver.createRootManifest("org.ow2.frascati:frascati-sca-parser:1.4-SNAPSHOT");
//			MundleOSGiManifest rootManifest = resolver.createRootManifest("org.ow2.frascati:frascati-component-factory:1.3");

//			MundleOSGiManifest rootManifest = resolver.createRootManifest("org.eclipse.core:runtime:3.4.0");
//			StringBuilder sB = new StringBuilder();
//			for (String eP : rootManifest.getImportPackages()){
//				sB.append(eP + ",");
//				System.out.println(eP);
//			}
//			System.out.println("\n" + sB);
			DependencyNode rootNode = resolver.resolveDependencyTree(rootManifest);
			installNode(rootNode);
			System.out.println(rootNode.getDependencies().size());
			for (Bundle b : bundleContext.getBundles()){
				System.out.println(b.getSymbolicName());
			}
			assertTrue(rootNode.getDependencies().size() == 6);
		} catch (MavenArtifactUnavailableException e){
			e.printStackTrace();
			fail();
		} catch (DependencyNodeException e) {
			System.out.println(e.getMessage());
			fail();
		}
	}
	
	public void deployEclipseRuntime() throws BundleException, IOException{
		oSGiFacilities.deployMundle(new File("/home/remi/dev/fraSCAti/platform/workspace/frascati-osgi/eclipse/eclipse.jar"));
	}
	
	public void deployEclipseSCAModel() throws MavenArtifactUnavailableException, IOException, BndException, DependencyNodeException, BundleException{
		MundleOSGiManifest rootManifest = resolver.createRootManifest("org.eclipse.stp.sca.introspection:sca-model-introspection:2.0.1.2");
		DependencyNode rootNode = resolver.resolveDependencyTree(rootManifest);
		installNode(rootNode);
	}
	
	public void installNode(DependencyNode parentNode) throws BundleException, IOException{
		for (DependencyNode node : parentNode.getDependencies()){
			installNode(node);
		}
		File f = createMumbleJar(parentNode.getManifest()); 
		oSGiFacilities.deployMundle(f);
	}
	
	public File createMumbleJar(MundleOSGiManifest manifest) throws IOException{
		String dirPath = ( System.getProperty("java.io.tmpdir")) + 
			File.separator + manifest.getSymbolicName() + File.separator;
		String filePath = dirPath + 
			File.separator + "META-INF" + 
			File.separator + "MANIFEST.MF";
		String jarPath = dirPath + manifest.getSymbolicName() + ".jar";
		
		File f = new File(filePath);
		Files.createParentDirs(f);
		FileOutputStream fOS = new FileOutputStream(f);
		manifest.getManifest().write(fOS);
		fOS.close();
		
		File jar = createJar(jarPath, f);
		
		return jar;
	}
	
	public File createJar(String jarPath, File manifest) throws IOException {
		
		File jarFile = new File(jarPath);
		
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(jarFile));
		out.setMethod(ZipOutputStream.DEFLATED);
		
		ZipEntry ze = new ZipEntry("META-INF" + File.separator + "MANIFEST.MF");
		out.putNextEntry(ze);
		byte[] buffer = new byte[8 * 1024];
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				manifest), buffer.length);

		try {
			int count = 0;
			while ((count = in.read(buffer, 0, buffer.length)) >= 0) {
				if (count != 0) {
					out.write(buffer, 0, count);
				}
			}
		} finally {
			in.close();
		}
		
		out.close();
		return jarFile;
	}
	
//	@Test
	public void generateEclipseManifest() throws Exception {
		
		String coreCoords = "org.eclipse.core:runtime:3.4.0";
		String exports = "";
		String bundleClasspath = "";
		
		File file = mavenFacilities.getMavenArtifactFile(coreCoords);
		Properties properties = Analyzer.getManifest(file);
        Manifest m = new Manifest();
        for (Entry<?, ?> property : properties.entrySet()){
        	Name name = new Name( (String) property.getKey());
        	m.getMainAttributes().put( name , property.getValue());
        }
        
        m.getMainAttributes().putValue("Export-Package", "org.eclipse.core.resources,org.eclipse.core.runtime,org.eclipse.emf.common.notify,org.eclipse.emf.common.notify.impl,org.eclipse.emf.common.util,org.eclipse.emf.ecore,org.eclipse.emf.ecore.impl,org.eclipse.emf.ecore.plugin,org.eclipse.emf.ecore.resource,org.eclipse.emf.ecore.resource.impl,org.eclipse.emf.ecore.util,org.eclipse.emf.ecore.xmi,org.eclipse.emf.ecore.xmi.impl,org.eclipse.emf.ecore.xmi.util,org.eclipse.emf.ecore.xml.type,org.eclipse.emf.ecore.xml.type.util,org.eclipse.osgi.util");
        m.getMainAttributes().putValue("Bundle-Classpath", "/home/remi/.m2/repository/org/eclipse/equinox/common/3.4.0/common-3.4.0.jar,/home/remi/.m2/repository/org/eclipse/equinox/common/3.4.0/common-3.4.0.jar,/home/remi/.m2/repository/org/eclipse/emf/common/2.4.0/common-2.4.0.jar,/home/remi/.m2/repository/org/eclipse/emf/ecore/2.4.0/ecore-2.4.0.jar,/home/remi/.m2/repository/org/eclipse/emf/ecore/xmi/2.4.0/xmi-2.4.0.jar");
        m.getMainAttributes().putValue("Bundle-SymbolicName", "eclipse");
        m.getMainAttributes().putValue("Bundle-Name", "eclipse");
        m.getMainAttributes().putValue("Import-Package", "");
        m.getMainAttributes().putValue("Require-Bundle", "");
        m.getMainAttributes().putValue("Bundle-Activator", "");
        
        /*
		System.out.println("\n----");
		for (Entry<?, ?> e : m.getMainAttributes().entrySet()){
			System.out.println(e.getKey().toString() +": " + e.getValue().toString());
		}
		System.out.println("----\n");
		*/
		
		FileOutputStream fos = new FileOutputStream(new File (System.getProperty("java.io.tmpdir") + "/ECLIPSE.MF" ));
		m.write(fos);
		
        /*
		System.out.println(properties.getProperty("Import-Package"));
		for (Entry<Object, Object> property : properties.entrySet()){
			System.out.println( "--> " + property.getKey().toString() + " " + property.getValue().toString());
		}*/
		
		/*
		String coreCoords = "org.eclipse.core:runtime:3.4.0";
		MundleOSGiManifest rootManifest = resolver.createRootManifest(coreCoords);
		File file = mavenFacilities.getMavenArtifactFile(coreCoords);
		oSGiFacilities.deployMundle(file);*/
	}
	
}
