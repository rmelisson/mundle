package com.orange.maven2bundle.installer.osgi;

import java.util.ArrayList;
import java.util.List;

public class Exporter extends PackageManager {

	protected List<String> exportPackages;
	
	public Exporter(String exportPackages){
		List<String> exportPackagesList = transformStringToPackageList(exportPackages);
		this.exportPackages = resolveUsesProblem(exportPackagesList);
	}
	
	// TODO no more use this hack
	private List<String> resolveUsesProblem(List<String> _exportPackages) {
		
		// we start by copying the list
		ArrayList<String> repaired = new ArrayList<String>();
		
		// number of clauses to ignore
		int p = 0;
		
		for (String clause : _exportPackages){
			
			if (p>0){
				p--;
				continue;
			}
			
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
					int j = i;
					while (!_exportPackages.get(++j).contains("\"")){
						new_clause += ",";
						new_clause += _exportPackages.get(j);
						p++;
					}
					new_clause += ",";
					new_clause += _exportPackages.get(j);
					p++;
					clause = new_clause;
				}
			}
			
			repaired.add(clause);
		}
		
		return repaired;
	}

}
