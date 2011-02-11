package com.orange.maven2bundle.installer.test;

import java.io.File;
import java.util.Properties;

import org.apache.felix.framework.FrameworkFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;

public final class Resources {
	
	// location of the Maven repository for testing
	public final static String testingRepositoryRootPath = "/home/remi/.m2/repository/"; 
	
	// artifact which doesn't exist
	public final static String imaginaryArtifactCoordinates = "com.orange.maven2bundle:alice:0.0.7";
		
	// artifact with dependencies
	public final static String DefaultArtifactCoordinates = "com.orange.maven2bundle:artifact-default:0.0.1.SNAPSHOT";
	public final static int DefaultArtifactImportPackageNumber = 3;
	public final static int DefaultArtifactExportPackageNumber = 1;
	
	// artifact with no dependencies
	//public final static String ArtifactWithNoDependenciesCoordinates = "";
	
	// artifact with an OSGi manifest
	public final static String ArtifactWithOSGiManifestCoordinates = "com.orange.maven2bundle:artifact-osgi:0.0.1.SNAPSHOT";
	public final static String ArtifactWithOSGiManifestExportPackage = "com.orange.maven2bundle.example.osgi;uses:=\"com.orange.maven2bundle.example.defaultartifact\"";
	public final static int ArtifactWithOSGiManifestImportPackageNumber = 0;
	public final static int ArtifactWithOSGiManifestExportPackageNumber = 1;

	public static final Object GroupId = "com.orange.maven2bundle";
	
	public static String shouldBeAvailablePackage = "org.osgi.framework; version=\"1.5.0\"";
	public static String shouldNotBeAvailablePackage = "org.blabla.toto;";
	
	private static String cachePath = System.getProperty("java.io.tmpdir") + "/felix-cache";
	public static BundleContext initBundleTestingContext() throws BundleException{
		File cacheDirectory = new File(cachePath);
		
		FrameworkFactory ff = new FrameworkFactory();
		Properties configurationProperties = new Properties();
		configurationProperties.setProperty(Constants.FRAMEWORK_STORAGE, cacheDirectory.getAbsolutePath());
		
		Framework fm = ff.newFramework(configurationProperties);
		fm.init();
		fm.start();
		return fm.getBundleContext();
	}

	public static void cleanCache() {
		(new File(cachePath)).delete();
	}
}
