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
			installService.installMavenArtifactAsBundle(Resources.ArtifactWithActivatorManifestCoordinates);
			assertTrue(bundleContext.getBundles().length == 5);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
}
