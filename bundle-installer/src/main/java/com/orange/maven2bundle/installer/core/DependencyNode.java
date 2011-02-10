package com.orange.maven2bundle.installer.core;

import com.orange.maven2bundle.installer.osgi.OSGiManifest;

public class DependencyNode {

	OSGiManifest manifest;
	
	public DependencyNode(OSGiManifest manifest){
		this.manifest = manifest;
	}
}
