package com.newbury.revola.domain;

public class DeviceAllocation {
	private String serialNumber;
    private String imei1;
    private String imei2;
    private String productDescription;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String contactNumber;
    private String department;
    private String additionalInfo1;
    private String additionalInfo2;
    
	public DeviceAllocation() {
		super();
		// TODO Auto-generated constructor stub
	}
	


	public DeviceAllocation(String serialNumber, String imei1, String imei2, String productDescription,
			String firstName, String lastName, String emailAddress, String contactNumber, String department,
			String additionalInfo1, String additionalInfo2) {
		super();
		this.serialNumber = serialNumber;
		this.imei1 = imei1;
		this.imei2 = imei2;
		this.productDescription = productDescription;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
		this.contactNumber = contactNumber;
		this.department = department;
		this.additionalInfo1 = additionalInfo1;
		this.additionalInfo2 = additionalInfo2;
	}

	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getImei1() {
		return imei1;
	}
	public void setImei1(String imei1) {
		this.imei1 = imei1;
	}
	public String getImei2() {
		return imei2;
	}
	public void setImei2(String imei2) {
		this.imei2 = imei2;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getAdditionalInfo1() {
		return additionalInfo1;
	}
	public void setAdditionalInfo1(String additionalInfo1) {
		this.additionalInfo1 = additionalInfo1;
	}
	public String getAdditionalInfo2() {
		return additionalInfo2;
	}
	public void setAdditionalInfo2(String additionalInfo2) {
		this.additionalInfo2 = additionalInfo2;
	}
	
	@Override
	public String toString() {
		return "DeviceAllocations [serialNumber=" + serialNumber + ", imei1=" + imei1 + ", imei2=" + imei2
				+ ", productDescription=" + productDescription + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", emailAddress=" + emailAddress + ", contactNumber=" + contactNumber + ", department=" + department
				+ ", additionalInfo1=" + additionalInfo1 + ", additionalInfo2=" + additionalInfo2 + "]";
	}
    
    
}
