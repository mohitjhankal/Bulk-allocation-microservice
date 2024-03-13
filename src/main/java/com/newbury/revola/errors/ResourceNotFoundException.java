package com.newbury.revola.errors;


public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 473478625986770672L;
	private String resourceName;
	private String fieldName;
	private Object fieldValue;
	private String path;

	public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue, String path) {
		super(String.format("%s %s %s", resourceName, fieldName, fieldValue));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
		this.path = path;
	}

	public String getResourceName() {
		return resourceName;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Object getFieldValue() {
		return fieldValue;
	}
	
	@Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
	
	public String getPath() {
		return path;
	}


}