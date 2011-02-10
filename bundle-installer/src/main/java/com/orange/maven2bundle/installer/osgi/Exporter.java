package com.orange.maven2bundle.installer.osgi;

import java.util.List;

public class Exporter extends PackageManager {

	protected List<String> exportPackages;
	
	public Exporter(String exportPackages){
		this.exportPackages = transformStringToPackageList(exportPackages);
		resolveUsesProblem(this.exportPackages);
	}
	
	// TODO no more use this hack
	private void resolveUsesProblem(List<String> _exportPackages) {
		for (String clause : _exportPackages){
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
					int j = i + 1;
					while (!_exportPackages.get(j).contains("\"")){
						new_clause += _exportPackages.get(j);
						_exportPackages.remove(j);
					}
					new_clause += _exportPackages.get(j);
					_exportPackages.remove(j);
					_exportPackages.remove(i);
					_exportPackages.add(new_clause);
				}
			}
		}
	}

}
