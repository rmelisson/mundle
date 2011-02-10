package com.orange.maven2bundle.installer.exception;

public class BndException extends Exception {
	
	private static final long serialVersionUID = 6130844032153490882L;

	public BndException(Exception e) {
		this.initCause(e);
	}
	
}
