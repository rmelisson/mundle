package com.orange.maven2bundle.installer.core.test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.HashSet;
import java.util.Properties;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;

import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import aQute.lib.osgi.Analyzer;

import com.orange.maven2bundle.installer.core.DependencyNode;
import com.orange.maven2bundle.installer.core.Deployer;
import com.orange.maven2bundle.installer.core.Resolver;
import com.orange.maven2bundle.installer.exception.BndException;
import com.orange.maven2bundle.installer.exception.DependencyNodeException;
import com.orange.maven2bundle.installer.exception.MavenArtifactUnavailableException;
import com.orange.maven2bundle.installer.maven.MavenFacilities;
import com.orange.maven2bundle.installer.osgi.MundleOSGiManifest;
import com.orange.maven2bundle.installer.osgi.OSGiFacilities;
import com.orange.maven2bundle.installer.test.Resources;

public class FraSCAtiTest {
	
	private MavenFacilities mavenFacilities;
	private OSGiFacilities oSGiFacilities;
	private Resolver resolver;
	private BundleContext bundleContext;
	private Deployer deployer;
	
	public FraSCAtiTest() throws BundleException{
		this.bundleContext = Resources.initBundleTestingContext();
		this.mavenFacilities = new MavenFacilities(Resources.testingRepositoryRootPath);;
		this.oSGiFacilities = new OSGiFacilities(this.bundleContext);
		this.resolver = new Resolver(mavenFacilities, oSGiFacilities);
		this.deployer = new Deployer(oSGiFacilities);
	}
	
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
			deployer.installNode(rootNode);
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
		deployer.installNode(rootNode);
	}

}
