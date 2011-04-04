package com.orange.maven2bundle.installer.service.test;

import junit.framework.TestCase;

import org.junit.Test;
import org.osgi.framework.BundleException;

import com.orange.maven2bundle.installer.service.InstallService;
import com.orange.maven2bundle.installer.service.ServicesFactory;
import com.orange.maven2bundle.installer.test.TResources;

public class ServiceFactoryTest extends TestCase {

	private ServicesFactory servicesFactory;

	public ServiceFactoryTest() throws BundleException{
		servicesFactory = new ServicesFactory(
				TResources.getLocalRepositoryLocation(), 
				TResources.initBundleTestingContext());
	}
	
	@Test
	public void testInstallService(){
		InstallService installService = servicesFactory.initInstallService();
		
		assertNotNull(installService);
	}
}
