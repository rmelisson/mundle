package com.orange.maven2bundle.installer;

import java.io.File;

public class Resources {
	
	public static final String getLocalRepositoryLocation() throws Exception{
//		Properties ps = System.getProperties();
//		for (Entry<Object, Object> p : ps.entrySet()){
//			System.out.println(p.getKey().toString() + " -> " + p.getValue());
//		}
//		
//		  Map<String, String> env = System.getenv();
//	        for (String envName : env.keySet()) {
//	            System.out.format("%s=%s%n", envName, env.get(envName));
//	        }

		String mavenLocalRepository = System.getProperty("localRepository");
		if (mavenLocalRepository == null){
			mavenLocalRepository = System.getenv("HOME") + "/.m2/repository/";
			if (!(new File(mavenLocalRepository)).exists()){
				throw new Exception("Maven local repository cannot be found.");
			}
		}
		
		return mavenLocalRepository;
	}
	
}
