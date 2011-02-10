package com.orange.maven2bundle.example.defaultartifact;

import com.orange.maven2bundle.example.osgi.UselessClass;

public class API {
	
	public static DirectoryLimiter createDirectoryLimiter(){	
		UselessClass uc = new UselessClass();
		uc.uselessMethod();
		return new DirectoryLimiter(42000);
	}
}
