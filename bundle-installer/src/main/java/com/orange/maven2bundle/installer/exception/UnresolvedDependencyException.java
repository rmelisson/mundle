package com.orange.maven2bundle.installer.exception;

public class UnresolvedDependencyException extends Exception {

	private static final long serialVersionUID = 5970505187851937352L;

	public UnresolvedDependencyException(String dependency) {
		super("A dependency cannot be resolved : " + dependency );
	}
	
}
