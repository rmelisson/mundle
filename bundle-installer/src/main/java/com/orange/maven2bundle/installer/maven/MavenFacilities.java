package com.orange.maven2bundle.installer.maven;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.maven.repository.internal.DefaultArtifactDescriptorReader;
import org.apache.maven.repository.internal.DefaultServiceLocator;
import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.connector.wagon.WagonRepositoryConnectorFactory;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.impl.ArtifactDescriptorReader;
import org.sonatype.aether.repository.LocalArtifactRequest;
import org.sonatype.aether.repository.LocalArtifactResult;
import org.sonatype.aether.repository.LocalRepository;
import org.sonatype.aether.repository.LocalRepositoryManager;
import org.sonatype.aether.resolution.ArtifactDescriptorException;
import org.sonatype.aether.resolution.ArtifactDescriptorRequest;
import org.sonatype.aether.resolution.ArtifactDescriptorResult;
import org.sonatype.aether.spi.connector.RepositoryConnectorFactory;
import org.sonatype.aether.util.artifact.DefaultArtifact;

import com.orange.maven2bundle.installer.exception.MavenArtifactUnavailableException;

public class MavenFacilities {
	
	private RepositorySystem repositorySystem;
	private MavenRepositorySystemSession mavenRepositorySystemsession;
	private HashMap<String, ResolvedMavenArtifact> resolvedArtifactMap;
	private LocalRepositoryManager localRepositoryManager;
	
	/**
	 * Create the Maven facilities object
	 * @param localRepositoryRoot The root path of the local Maven repository 
	 * @throws Exception
	 */
	public MavenFacilities(String localRepositoryRoot) {
		DefaultServiceLocator locator = new DefaultServiceLocator();
        locator.addService( RepositoryConnectorFactory.class, WagonRepositoryConnectorFactory.class );
        locator.addService( ArtifactDescriptorReader.class, DefaultArtifactDescriptorReader.class );
        repositorySystem = locator.getService( RepositorySystem.class );
		        
		LocalRepository localRepository = new LocalRepository(localRepositoryRoot);
		
        mavenRepositorySystemsession = new MavenRepositorySystemSession();
        localRepositoryManager = repositorySystem.newLocalRepositoryManager(localRepository);
        mavenRepositorySystemsession.setLocalRepositoryManager(localRepositoryManager);
        
        // FIXME create a limited map rather than an unconstrained one
        resolvedArtifactMap = new HashMap<String, ResolvedMavenArtifact>();
	}

	/**
	 * Assert that a Maven artifact is available into the repository
	 * @param artifactCoordinates
	 * @return
	 * @throws IllegalArgumentException if the coordinates are not correctly expressed
	 */
	public boolean isAvailable(String artifactCoordinates) throws IllegalArgumentException {
		// already resolved ?
		if (resolvedArtifactMap.containsKey(artifactCoordinates)) {
			return true;
		}
		// if not, we try do resolve it
		else {
			LocalArtifactResult localArtifactResult = getLocalArtifact(artifactCoordinates);
			if (localArtifactResult.isAvailable()) {
				// we save this reference into a map for further uses
				ArtifactDescriptorResult artifactDescriptorResult = getArtifactDescriptor(artifactCoordinates);
				ResolvedMavenArtifact resolvedMavenArtifact = new ResolvedMavenArtifact(localArtifactResult, artifactDescriptorResult);
				this.resolvedArtifactMap.put(artifactCoordinates, resolvedMavenArtifact);
			}
			
			return localArtifactResult.isAvailable();
		}
	}
		
	private ArtifactDescriptorResult getArtifactDescriptor(String artifactCoordinates) throws IllegalArgumentException {
		DefaultArtifact defaultArtifact = new DefaultArtifact(artifactCoordinates);			
		ArtifactDescriptorRequest artifactDescriptorRequest = new ArtifactDescriptorRequest(defaultArtifact, null, null);
		
		try {
			return repositorySystem.readArtifactDescriptor(
					mavenRepositorySystemsession,
					artifactDescriptorRequest);
		} catch (ArtifactDescriptorException e) {
			// FIXME what is an artifact descriptor exception ?
			throw new IllegalArgumentException(e);
		}
	}
	
	private LocalArtifactResult getLocalArtifact(String artifactCoordinates){
		DefaultArtifact defaultArtifact = new DefaultArtifact(artifactCoordinates);
		
		return localRepositoryManager.find(
					mavenRepositorySystemsession, 
					new LocalArtifactRequest(defaultArtifact, null, null) );
	}

	/**
	 * 
	 * @param artifactCoordinates
	 * @return
	 * @throws MavenArtifactUnavailableException
	 */
	public File getMavenArtifactFile(String artifactCoordinates) throws MavenArtifactUnavailableException {
		if (isAvailable(artifactCoordinates)){
			LocalArtifactResult lAR = resolvedArtifactMap.get(artifactCoordinates).getLocalArtifactResult();
			return lAR.getFile();
		} else {
			throw new MavenArtifactUnavailableException(artifactCoordinates);
		}
	}

	/**
	 * Get the dependencies of a Maven artifact 
	 * @param artifactCoordinates
	 * @return artifact dependencies
	 * @throws MavenArtifactUnavailableException
	 */
	public  List<Dependency> getDependencies(String artifactCoordinates) throws MavenArtifactUnavailableException {
		if (isAvailable(artifactCoordinates)){
			ArtifactDescriptorResult aDR = resolvedArtifactMap.get(artifactCoordinates).getArtifactDescriptorResult();
			return aDR.getDependencies();
		} else {
			throw new MavenArtifactUnavailableException(artifactCoordinates);
		}
	}
	

	public List<Dependency> getDependencies(Artifact artifact) throws MavenArtifactUnavailableException {
		return this.getDependencies(artifact.toString());
	}
	
	/**
	 * Get the artifact which contains pom.xml informations
	 * @param artifactCoordinates
	 * @return
	 */
	public Artifact getArtifact(String artifactCoordinates){
		return this.getArtifactDescriptor(artifactCoordinates).getArtifact();
	}

	
}
