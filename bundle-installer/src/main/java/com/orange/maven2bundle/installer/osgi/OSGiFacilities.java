package com.orange.maven2bundle.installer.osgi;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.jar.Attributes.Name;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import com.orange.maven2bundle.installer.exception.BndException;

import aQute.lib.osgi.Analyzer;

public class OSGiFacilities {
	
	private BundleContext bundleContext; 
	private HashSet<String> registredExports;

	public OSGiFacilities(BundleContext bundleContext){
		this.bundleContext = bundleContext;
		initializeExportedPackageMap();
	}
	
	private void initializeExportedPackageMap() {
		registredExports = new HashSet<String>();
		for (Bundle bundle : bundleContext.getBundles()){
			registerExports(bundle);
		}
	}
	
	private void registerExports(Bundle bundle){
		Exporter exporter = new Exporter((String) bundle.getHeaders().get("Export-Package"));
		for (String export : exporter.exportPackages){
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

	public boolean isAvailable(String shouldBeAvailablePackage) {
		return registredExports.contains(shouldBeAvailablePackage);
	}

}
