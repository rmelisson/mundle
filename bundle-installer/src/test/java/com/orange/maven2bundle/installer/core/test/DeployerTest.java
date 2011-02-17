package com.orange.maven2bundle.installer.core.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.After;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import com.orange.maven2bundle.installer.core.Deployer;
import com.orange.maven2bundle.installer.core.Resolver;
import com.orange.maven2bundle.installer.maven.MavenFacilities;
import com.orange.maven2bundle.installer.osgi.OSGiFacilities;
import com.orange.maven2bundle.installer.service.InstallService;
import com.orange.maven2bundle.installer.service.InstallServiceImpl;
import com.orange.maven2bundle.installer.test.Resources;

public class DeployerTest {
	
	private OSGiFacilities oSGiFacilities;
	private InstallService installService;
	private BundleContext bundleContext;
	
	public DeployerTest() throws BundleException{
		this.bundleContext = Resources.initBundleTestingContext();
		MavenFacilities mavenFacilities = new MavenFacilities(Resources.testingRepositoryRootPath);;
		oSGiFacilities = new OSGiFacilities(this.bundleContext);
		Resolver resolver = new Resolver(mavenFacilities, oSGiFacilities);
		Deployer deployer = new Deployer(oSGiFacilities);
		installService = new InstallServiceImpl(resolver, deployer);
	}
	
	@After
	public void clean() throws BundleException, IOException {
		Resources.cleanCache(bundleContext);
	}
	
	@Test
	public void testDeploy() {
		try {
//			String apa = "org.apache.commons:commons-io:1.3.2";
//			installService.installMavenArtifactAsBundle(apa);
			
//			installService.installMavenArtifactAsBundle(Resources.ArtifactWithOSGiManifestCoordinates);
			
//			File df = new File("/tmp/artifact-default-0.0.1.SNAPSHOT/artifact-default-0.0.1.SNAPSHOT.jar");
//			oSGiFacilities.deployMundle(new File("/tmp/ad.jar"));
//			installService.installMavenArtifactAsBundle(Resources.DefaultArtifactCoordinates);
			
//			File file = new File("/home/remi/dev/osgi/workspace/maven2bundle/artifact-examples/artifact-activator/target/artifact-activator-0.0.1.SNAPSHOT.jar");
//			oSGiFacilities.deployMundle(file);
			installService.installMavenArtifactAsBundle(Resources.ArtifactWithActivatorManifestCoordinates);
			assertTrue(bundleContext.getBundles().length == 4);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
