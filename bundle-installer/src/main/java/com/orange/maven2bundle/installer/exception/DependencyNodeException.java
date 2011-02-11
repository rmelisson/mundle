package com.orange.maven2bundle.installer.exception;

import java.util.ArrayList;

public class DependencyNodeException extends Exception {
	
	private static final long serialVersionUID = 5457653799542154547L;

	private ArrayList<String> nodeOrder;
	private UnresolvedDependencyException rootCause; 
	
	public DependencyNodeException(UnresolvedDependencyException rootCause){
		this.nodeOrder = new ArrayList<String>();
		this.rootCause = rootCause;
	}
	
	public void addNode(String description){
		this.nodeOrder.add(description);
	}
	
	public String getMessage(){
		String ret = rootCause.getMessage();
		ret += " (";
		for (String node : nodeOrder){
			ret += node + " ";
		}
		ret +=")";
		return ret;
	}
}
