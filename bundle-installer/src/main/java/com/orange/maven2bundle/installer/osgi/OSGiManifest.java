package com.orange.maven2bundle.installer.osgi;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Manifest;

public class OSGiManifest {

	private Manifest manifest;
	private List<String> importPackages;
	private List<String> exportPackages;
	
	public OSGiManifest(Manifest manifest){
		
		// save a reference to the manifest
		this.manifest = manifest;
		
		// then we register import and export packages
		String importPackageString = manifest.getMainAttributes().getValue("Import-Package");
		
		importPackages = transformStringToPackageList(importPackageString);
		
		String exportPackageString = manifest.getMainAttributes().getValue("Export-Package");
		exportPackages = transformStringToPackageList(exportPackageString);
		// hack for resolve error with split
		resolveUsesProblem(exportPackages);
	}

	public List<String> getImportPackages() {
		return importPackages;
	}
	
	public List<String> getExportPackages() {
		return exportPackages;
	}
	
	// FIXME use reg ex or a parser according to specifications
	private List<String> transformStringToPackageList(String importPackageString){
		ArrayList<String> packageList = new ArrayList<String>(); 
		if (importPackageString != null) {
			String[] packages = importPackageString.split(",");
			for (String p : packages){
				packageList.add(p);
			}
		}
		
		return packageList;
	}

	// TODO no more use this hack
	private void resolveUsesProblem(List<String> _exportPackages) {
		for (String clause : _exportPackages){
			// we try to join separated exports
			if (clause.contains("uses")){
				// we should assert that uses is closed
				// index after uses 
				int uses_idx = clause.indexOf("uses");
				
				// we assert that the two " are present
				int _1 = clause.indexOf("\"", uses_idx);
				int _2 = clause.indexOf("\"", ++_1 );
				
				// if not
				if (_2 < 0){
					String new_clause = clause;
					int i = _exportPackages.indexOf(clause);
					int j = i + 1;
					while (!_exportPackages.get(j).contains("\"")){
						new_clause += _exportPackages.get(j);
						_exportPackages.remove(j);
					}
					new_clause += _exportPackages.get(j);
					_exportPackages.remove(j);
					_exportPackages.remove(i);
					_exportPackages.add(new_clause);
				}
			}
		}
	}

	public Manifest getManifest() {
		return this.manifest;
	}
	
}
