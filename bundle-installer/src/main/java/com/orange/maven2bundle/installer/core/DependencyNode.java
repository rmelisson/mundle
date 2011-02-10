package com.orange.maven2bundle.installer.core;

import java.util.ArrayList;

import com.orange.maven2bundle.installer.osgi.MundleOSGiManifest;

public class DependencyNode {

	MundleOSGiManifest manifest;
	private ArrayList<DependencyNode> dependencies;
	
	public DependencyNode(MundleOSGiManifest manifest){
		this.manifest = manifest;
		this.dependencies = new ArrayList<DependencyNode>();
	}
	
	public MundleOSGiManifest getManifest(){
		return this.manifest;
	}
	
	public void append(DependencyNode node){
		this.dependencies.add(node);
	}
	
	public ArrayList<DependencyNode> getDependencies(){
		return this.dependencies;
	}
}
