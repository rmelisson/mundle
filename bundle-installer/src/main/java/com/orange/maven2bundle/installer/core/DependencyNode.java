package com.orange.maven2bundle.installer.core;

import java.util.ArrayList;

import com.orange.maven2bundle.installer.osgi.MundleOSGiManifest;

public class DependencyNode {

	private MundleOSGiManifest manifest;
	private ArrayList<DependencyNode> dependencies;
	
	public DependencyNode(MundleOSGiManifest manifest){
		this.manifest = manifest;
		this.dependencies = new ArrayList<DependencyNode>();
	}
	
	public MundleOSGiManifest getManifest(){
		return this.manifest;
	}
	
	public void append(DependencyNode node){
		if ( ! isAlreadyHere(node)) {
			this.dependencies.add(node);
		}
	}
	
	private boolean isAlreadyHere(DependencyNode node){
		for (DependencyNode dN : dependencies){
			if (dN.getManifest().getSymbolicName().equals(node.getManifest().getSymbolicName())){
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<DependencyNode> getDependencies(){
		return this.dependencies;
	}
}
