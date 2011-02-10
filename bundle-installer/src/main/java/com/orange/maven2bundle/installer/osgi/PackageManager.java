package com.orange.maven2bundle.installer.osgi;

import java.util.ArrayList;
import java.util.List;

public abstract class PackageManager {
	
	// FIXME use reg ex or a parser according to specifications
	protected List<String> transformStringToPackageList(String importPackageString){
		ArrayList<String> packageList = new ArrayList<String>(); 
		if (importPackageString != null) {
			String[] packages = importPackageString.split(",");
			for (String p : packages){
				packageList.add(p);
			}
		}
		
		return packageList;
	}
}
