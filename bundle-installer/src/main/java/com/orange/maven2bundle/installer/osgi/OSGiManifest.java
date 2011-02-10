package com.orange.maven2bundle.installer.osgi;

import java.util.List;
import java.util.jar.Manifest;

public class OSGiManifest extends Exporter {

	private Manifest manifest;
	private List<String> importPackages;
	
	public OSGiManifest(Manifest manifest){

		super(manifest.getMainAttributes().getValue("Export-Package"));
		
		// save a reference to the manifest
		this.manifest = manifest;
		
		// then we register import packages
		String importPackageString = manifest.getMainAttributes().getValue("Import-Package");
		
		importPackages = transformStringToPackageList(importPackageString);
		
	}

	public List<String> getImportPackages() {
		return importPackages;
	}
	
	public List<String> getExportPackages() {
		return exportPackages;
	}
		
	public Manifest getManifest() {
		return this.manifest;
	}
	
}
