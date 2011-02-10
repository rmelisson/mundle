package com.orange.maven2bundle.installer.test;

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
	public final static int ArtifactWithOSGiManifestImportPackageNumber = 0;
	public final static int ArtifactWithOSGiManifestExportPackageNumber = 1;

	public static final Object GroupId = "com.orange.maven2bundle";
}
