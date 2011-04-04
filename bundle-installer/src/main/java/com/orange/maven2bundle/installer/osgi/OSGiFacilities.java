package com.orange.maven2bundle.installer.osgi;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import aQute.lib.osgi.Analyzer;

import com.orange.maven2bundle.installer.exception.BndException;

public class OSGiFacilities {
	
	public static String artifactDirectory = "lib";
	private BundleContext bundleContext; 
	private ArrayList<String> registredExports;
	
	public OSGiFacilities(BundleContext bundleContext){
		this.bundleContext = bundleContext;
		this.updateAvailablePackage();
	}
	
	public void updateAvailablePackage(){
		registredExports = new ArrayList<String>();
		for (Bundle bundle : bundleContext.getBundles()){
			registerExports(bundle);
		}
	}
	
	private void registerExports(Bundle bundle){
		Exporter exporter = new Exporter((String) bundle.getHeaders().get("Export-Package"));
		for (String export : exporter.exportPackages){
			//FIXME it might be a bug which introduce a blank character, but where ?
			if (export.startsWith(" ")) {
				export = export.substring(1);
			}
			registredExports.add(export);
		}
	}

	private boolean hasOSGiManifest(JarFile jarFile) throws IOException {
		Manifest manifest = jarFile.getManifest();
		if (manifest == null) {
			return false;
		}
		// Bundle-SymbolicName is the only required attribute for OSGi manifest
		// TODO ensure that's really true (OSGi R4)		
		
		// It seems that the map isn't correctly used, we expected to use : 
		// return manifest.getMainAttributes().containsKey("Bundle-SymbolicName");
		
		// rather than
		Attributes attributes = manifest.getMainAttributes();
		return (attributes.getValue("Bundle-SymbolicName") != null);
		
	}

	public MundleOSGiManifest getOSGiManifest(File file) throws IOException, BndException {
		
		// an OSGi manifest maybe already available
		JarFile jarFile = new JarFile(file);
		if (this.hasOSGiManifest(jarFile)) {
			return new MundleOSGiManifest(jarFile.getManifest(), false);
		}
		else {
			// if not, we use Bnd for generate it -> mundle
			try {
				
				// Should be than kind of call, but it does not
				// resolve import and export, don't understand why
				/*
				Analyzer analyzer = new Analyzer();
				analyzer.setJar(file);
				Manifest manifest = analyzer.calcManifest();
				Properties properties = new Properties();
				properties.put(Analyzer.IMPORT_PACKAGE, "*");
				properties.put(Analyzer.EXPORT_PACKAGE, "*");
				analyzer.setProperties(properties);*/
				
				Properties properties = Analyzer.getManifest(file);
				
			    Manifest manifest = new Manifest();
			    for (Entry<?, ?> property : properties.entrySet()){
			    	Name name = new Name( (String) property.getKey());
			    	manifest.getMainAttributes().put( name , property.getValue());
			    }
			    
			    // we also put the File path as a bundle-classpath  
			    manifest.getMainAttributes().putValue("Bundle-ClassPath", 
			    		this.getInsideLibPath(file.getName()));
			    
				return new MundleOSGiManifest(manifest, true);
			} catch (Exception e){
				throw new BndException(e);
			}
		}
	}

	public void deployMundle(File bundleFile) throws BundleException, IOException {
		String path = bundleFile.toURI().toString();
		Bundle bundle = this.bundleContext.installBundle(path);
		bundle.start();
		registerExports(bundle);
	}

	public boolean isAvailable(String neededPackage) {
		// FIXME we don't use version... we only check start with at now
		// we need a better structure for exports
		for (String export : registredExports){
			if (export.startsWith(neededPackage)) {
				return true;
			}
		}
		return false;
//		return registredExports.contains(neededPackage);
	}
	
	public String getInsideLibPath(String libName){
		return File.separator +
			OSGiFacilities.artifactDirectory + 
			File.separator + 
			libName;
	}

}
