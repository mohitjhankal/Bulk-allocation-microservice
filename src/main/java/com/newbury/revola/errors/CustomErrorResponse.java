package com.newbury.revola.errors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.newbury.revola.util.LocalDateTimeDeserializer;
import com.newbury.revola.util.LocalDateTimeSerializer;
import java.io.Serializable;
import java.time.LocalDateTime;

public class CustomErrorResponse implements Serializable {

	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	@JsonProperty(value = "TimeStamp")
	private LocalDateTime timestamp;
	@JsonProperty(value = "Status_Code")
	private int statusCode;
	@JsonProperty(value = "Status_Message")
	private String statusMessage;
	@JsonProperty(value = "Error_Description")
	private String errorDescription;

	@JsonProperty(value = "Path")
	private String path;

	public CustomErrorResponse() {
	}

	public CustomErrorResponse(int statusCode, String statusMessage, String errorDescription, String path) {
		super();
		this.statusCode = statusCode;
		this.statusMessage = statusMessage;
		this.errorDescription = errorDescription;
		this.path = path;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "{\n \"Timestamp\":" + "\"" + timestamp + "\"" + ", \n \"Status_Code\":" + statusCode
				+ ", \n \"Status_Message\":" + "\"" + statusMessage + "\"" + ", \n \"Error_Description\":" + "\""
				+ errorDescription + "\"" + "\n}";
	}
}