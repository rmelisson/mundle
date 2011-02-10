package com.orange.maven2bundle.installer.service;

public class ServicesFactory {

	private String mavenLocalRepository;

	public ServicesFactory(String mavenLocalRepositoryLocation){
		this.mavenLocalRepository = mavenLocalRepositoryLocation;
	}
	
	public InstallService initInstallService(){		
		return new InstallServiceImpl(null);
	}
}
