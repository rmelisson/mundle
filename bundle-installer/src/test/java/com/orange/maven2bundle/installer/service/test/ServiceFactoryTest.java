package com.orange.maven2bundle.installer.service.test;

import junit.framework.TestCase;

import org.junit.Test;

import com.orange.maven2bundle.installer.service.InstallService;
import com.orange.maven2bundle.installer.service.ServicesFactory;

public class ServiceFactoryTest extends TestCase {

	private ServicesFactory servicesFactory;

	public ServiceFactoryTest(){
		servicesFactory = new ServicesFactory("~/.m2/repository");
	}
	
	@Test
	public void testInstallService(){
		InstallService installService = servicesFactory.initInstallService();
		
		assertNotNull(installService);
	}
}
