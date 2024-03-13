package com.newbury.revola.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.newbury.revola.util.LocalDateTimeDeserializer;
import com.newbury.revola.util.LocalDateTimeSerializer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ErrorDetails {

	@JsonProperty(value = "time_stamp")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime timeStamp;

	@JsonProperty(value = "payload")
	private String payload;

	@JsonProperty(value = "service")
	private String service;

	@JsonProperty(value = "path")
	private String path;

	@JsonProperty(value = "error_description")
	private String errorDescription;

	@JsonProperty(value = "resolution_status")
	private String resolutionStatus;

    @JsonProperty(value = "appdirect_opco")
    private String appdirectOpco;
    
	public ErrorDetails() {
		super();
	}

	public ErrorDetails(LocalDateTime timeStamp, String payload, String service, String path,
                        String errorDescription, String resolutionStatus, String appdirectOpco) {
		super();
		this.timeStamp = timeStamp;
		this.payload = payload;
		this.service = service;
		this.path = path;
		this.errorDescription = errorDescription;
		this.resolutionStatus = resolutionStatus;
        this.appdirectOpco = appdirectOpco;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String string) {
		this.payload = string;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public String getResolutionStatus() {
		return resolutionStatus;
	}

	public void setResolutionStatus(String resolutionStatus) {
		this.resolutionStatus = resolutionStatus;
	}

    public String getAppdirectOpco() {
        return appdirectOpco;
    }

    public void setAppdirectOpco(String appdirectOpco) {
        this.appdirectOpco = appdirectOpco;
    }

	@Override
	public String toString() {
		return "{" + "\n\"time_stamp\":" + "\"" + timeStamp + "\"" + ",\n\"payload\":" + "\"" + payload
				+ "\"" + ",\n\"service\":" + "\"" + service + "\"" + ",\n\"path\":" + "\"" + path + "\"" +
				",\n\"error_description\":" + "\"" + errorDescription
				+ "\"" + ",\n\"resolution_status\":" + "\"" + resolutionStatus
				+ "\"" + ",\n\"appdirect_opco\":" + "\"" + appdirectOpco
				+ "\"" + "}";
	}
}
