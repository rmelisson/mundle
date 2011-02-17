package com.orange.maven2bundle.example.activator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.orange.maven2bundle.example.defaultartifact.API;
import com.orange.maven2bundle.example.defaultartifact.DirectoryLimiter;

public class Activator implements BundleActivator{

	public void start(BundleContext context) throws Exception {
		DirectoryLimiter dl = API.createDirectoryLimiter();
	}

	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
