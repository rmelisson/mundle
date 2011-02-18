package com.orange.maven2bundle.installer.core.test;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import com.orange.maven2bundle.installer.core.DependencyNode;
import com.orange.maven2bundle.installer.core.Deployer;
import com.orange.maven2bundle.installer.core.Resolver;
import com.orange.maven2bundle.installer.exception.ArtifactInstallationException;
import com.orange.maven2bundle.installer.exception.BndException;
import com.orange.maven2bundle.installer.exception.DependencyNodeException;
import com.orange.maven2bundle.installer.exception.MavenArtifactUnavailableException;
import com.orange.maven2bundle.installer.maven.MavenFacilities;
import com.orange.maven2bundle.installer.osgi.MundleOSGiManifest;
import com.orange.maven2bundle.installer.osgi.OSGiFacilities;
import com.orange.maven2bundle.installer.service.InstallService;
import com.orange.maven2bundle.installer.service.InstallServiceImpl;
import com.orange.maven2bundle.installer.test.Resources;

public class FraSCAtiTest {
	
	private OSGiFacilities oSGiFacilities;
	private InstallService installService;
	private BundleContext bundleContext;
	private Resolver resolver;
	private Deployer deployer;
	
	public FraSCAtiTest() throws BundleException{
		this.bundleContext = Resources.initBundleTestingContext();
		MavenFacilities mavenFacilities = new MavenFacilities(Resources.testingRepositoryRootPath);;
		oSGiFacilities = new OSGiFacilities(this.bundleContext);
		this.resolver = new Resolver(mavenFacilities, oSGiFacilities);
		this.deployer = new Deployer(oSGiFacilities);
		installService = new InstallServiceImpl(resolver, deployer);
	}
	
	@After
	public void clean() throws BundleException, IOException{
		Resources.cleanCache(bundleContext);
	}
	
	@Test
	public void testFraSCAti() {
		try {
			Resources.cleanCache(bundleContext);
			
			oSGiFacilities.deployMundle(new File("/home/remi/dev/osgi/workspace/maven2bundle/frascati-osgi/eclipse/eclipse.jar"));

			installService.installMavenArtifactAsBundle("org.eclipse.stp.sca.introspection:sca-model-introspection:2.0.1.2");
			installService.installMavenArtifactAsBundle("org.ow2.frascati:frascati-component-factory:1.4-SNAPSHOT");
			oSGiFacilities.deployMundle(new File("/home/remi/dev/osgi/workspace/maven2bundle/frascati-osgi/fcftoo/fcftoo.jar"));
//			installService.installMavenArtifactAsBundle("org.ow2.frascati:frascati-sca-parser:1.4-SNAPSHOT");
//			oSGiFacilities.deployMundle(new File("/home/remi/dev/osgi/workspace/maven2bundle/frascati-osgi/fa/fa.jar"));
//			oSGiFacilities.deployMundle(new File("/tmp/af.jar"));
			deployCustomAssemblyFactory();
//			oSGiFacilities.deployMundle(new File("/home/remi/dev/osgi/workspace/maven2bundle/frascati-osgi/fact/fact.jar"));
//			oSGiFacilities.deployMundle(new File("/home/remi/dev/osgi/workspace/maven2bundle/frascati-activator/target/frascati-activator-0.0.1.SNAPSHOT.jar"));
//			installService.installMavenArtifactAsBundle("org.ow2.frascati:frascati-assembly-factory:1.4-SNAPSHOT");
			installService.installMavenArtifactAsBundle("com.orange:frascati-activator:0.0.1.SNAPSHOT");
		/*	DependencyNode rootNode = resolver.resolveDependencyTree(rootManifest);
			deployer.installNode(rootNode);
			System.out.println(rootNode.getDependencies().size());*/
//			for (Bundle b : bundleContext.getBundles()){
////				if (b.getSymbolicName().equals("frascati-util-1.4-SNAPSHOT")){
//				if (b.getSymbolicName().equals("frascati-assembly-factory-1.4-SNAPSHOT")){
//					System.out.println(b.getHeaders().get("Export-Package").toString());
////					URL r = b.getResource("org.ow2.frascati.util.AbstractFractalLoggeable");
//					URL r = b.getResource("org.ow2.frascati.FraSCAti");
//					System.out.println(r.toString());
//				}
//				System.out.println(b.getSymbolicName());
//			}
			//assertTrue(rootNode.getDependencies().size() == 6);
		} catch (BundleException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (ArtifactInstallationException e) {
			e.printStackTrace();
			fail();
		} catch (MavenArtifactUnavailableException e) {
			e.printStackTrace();
			fail();
		} catch (BndException e) {
			e.printStackTrace();
			fail();
		} catch (DependencyNodeException e) {
			e.printStackTrace();
			fail();
		}
	}

	private void deployCustomAssemblyFactory() throws ArtifactInstallationException, BundleException, IOException, MavenArtifactUnavailableException, BndException, DependencyNodeException {
		MundleOSGiManifest rootManifest = resolver.createRootManifest("org.ow2.frascati:frascati-assembly-factory:1.4-SNAPSHOT");
		rootManifest.getManifest().getMainAttributes().putValue("Export-Package", "org.ow2.frascati");
		DependencyNode rootNode = resolver.resolveDependencyTree(rootManifest);
		deployer.installNode(rootNode);
	}
	
//	
//	public void generateEclipseManifest() throws Exception {
//		
//		String coreCoords = "org.eclipse.core:runtime:3.4.0";
//		String exports = "";
//		String bundleClasspath = "";
//		
//		File file = mavenFacilities.getMavenArtifactFile(coreCoords);
//		Properties properties = Analyzer.getManifest(file);
//        Manifest m = new Manifest();
//        for (Entry<?, ?> property : properties.entrySet()){
//        	Name name = new Name( (String) property.getKey());
//        	m.getMainAttributes().put( name , property.getValue());
//        }
//        
//        m.getMainAttributes().putValue("Export-Package", "org.eclipse.core.resources,org.eclipse.core.runtime,org.eclipse.emf.common.notify,org.eclipse.emf.common.notify.impl,org.eclipse.emf.common.util,org.eclipse.emf.ecore,org.eclipse.emf.ecore.impl,org.eclipse.emf.ecore.plugin,org.eclipse.emf.ecore.resource,org.eclipse.emf.ecore.resource.impl,org.eclipse.emf.ecore.util,org.eclipse.emf.ecore.xmi,org.eclipse.emf.ecore.xmi.impl,org.eclipse.emf.ecore.xmi.util,org.eclipse.emf.ecore.xml.type,org.eclipse.emf.ecore.xml.type.util,org.eclipse.osgi.util");
//        m.getMainAttributes().putValue("Bundle-Classpath", "/home/remi/.m2/repository/org/eclipse/equinox/common/3.4.0/common-3.4.0.jar,/home/remi/.m2/repository/org/eclipse/equinox/common/3.4.0/common-3.4.0.jar,/home/remi/.m2/repository/org/eclipse/emf/common/2.4.0/common-2.4.0.jar,/home/remi/.m2/repository/org/eclipse/emf/ecore/2.4.0/ecore-2.4.0.jar,/home/remi/.m2/repository/org/eclipse/emf/ecore/xmi/2.4.0/xmi-2.4.0.jar");
//        m.getMainAttributes().putValue("Bundle-SymbolicName", "eclipse");
//        m.getMainAttributes().putValue("Bundle-Name", "eclipse");
//        m.getMainAttributes().putValue("Import-Package", "");
//        m.getMainAttributes().putValue("Require-Bundle", "");
//        m.getMainAttributes().putValue("Bundle-Activator", "");
//        
//        /*
//		System.out.println("\n----");
//		for (Entry<?, ?> e : m.getMainAttributes().entrySet()){
//			System.out.println(e.getKey().toString() +": " + e.getValue().toString());
//		}
//		System.out.println("----\n");
//		*/
//		
//		FileOutputStream fos = new FileOutputStream(new File (System.getProperty("java.io.tmpdir") + "/ECLIPSE.MF" ));
//		m.write(fos);
//		
//        /*
//		System.out.println(properties.getProperty("Import-Package"));
//		for (Entry<Object, Object> property : properties.entrySet()){
//			System.out.println( "--> " + property.getKey().toString() + " " + property.getValue().toString());
//		}*/
//		
//		/*
//		String coreCoords = "org.eclipse.core:runtime:3.4.0";
//		MundleOSGiManifest rootManifest = resolver.createRootManifest(coreCoords);
//		File file = mavenFacilities.getMavenArtifactFile(coreCoords);
//		oSGiFacilities.deployMundle(file);*/
//	}

}
