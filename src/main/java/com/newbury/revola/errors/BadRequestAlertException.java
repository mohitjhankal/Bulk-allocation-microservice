package com.newbury.revola.errors;


public class BadRequestAlertException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final String entityName;
	private final String errorKey;
	private String path;
	
	public BadRequestAlertException(String defaultMessage, String entityName, String errorKey, String path) {
		super(String.format("%s %s : %s", entityName, defaultMessage, errorKey));
		this.entityName = entityName;
		this.errorKey = errorKey;
		this.path = path;
	}

	public String getEntityName() {
		return entityName;	}

	public String getErrorKey() {
		return errorKey;
	}

	public String getPath() {
		return path;
	}

}