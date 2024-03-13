package com.newbury.revola.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import javax.mail.MessagingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.newbury.revola.domain.DeviceAllocation;
import com.newbury.revola.domain.ErrorDetails;
import com.newbury.revola.domain.MailModel;
import com.newbury.revola.config.DmmConfigurations;

import freemarker.template.TemplateException;

@Service
public class ProcessCsvFiles {
	
	private final Logger log = LoggerFactory.getLogger(ProcessCsvFiles.class);

	@Value("${spring.mail.username}")
	private String fromEmail;

	@Value("${dmm.error.email.to}")
	private String toEmail;

	@Value("${dmm.error.email.cc}")
	private String ccEmail;

	@Value("${dmm.api.username}")
	private String dmmApiUserName;

	@Value("${dmm.api.password}")
	private String dmmApiPassword;

	@Value("${dmm.uri.external}")
	private String dmmUri;

	@Value("${dmm.order.data.card.import.endpoint}")
	private String dmmAssetAllocationUri;

	@Value("${dmm.service.request.data.card.import.endpoint}")
	private String dmmDataCardImportApi;
	
    @Value("${dmm.general.error.topic}")
    private String generalErrorTopic;
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private DmmConfigurations dmmConfigurations;

	@Autowired
	private EmailService emailService;
	
	

	private static final String AUTHORIZATION = "Authorization";
	private static final String BASIC = "Basic";
	private static final String SERIAL_Number = "Serial Number";
	private static final String FIRST_NAME = "First Name";
	private static final String LAST_NAME = "Last Name";
	private static final String EMAIL_ADDRESS = "Email Address ";
	private static final String CONTACT_NUMBER = "Contact Number";
	private static final String QUANTITY = "Quantity";
	private static final String SERVICEREQUESTID = "serviceRequestId";
	private static final String BULKALLOCATIONPROCESSED = "39 - Bulk Allocation Processed";
	private static final String BULKALLOCATIONFAILED = "41 - Bulk Allocation Failed";
	private static final String BULKALLOCATIONPROCESSEDWITHERROR = "40 - Bulk Allocation Processed with Error";
	private static final String STATUS_CODE = "statusCode";
	private static final String MESSAGE = "message";
	private static final String INVALID_ROWS = "invalidRows";
	private static final String SERVICE = "service";
	private static final String ERROR = "error";
	private static final String DMM = "dmm";
	private static final String PAYLOAD = "payload";
	private static final String CREATED = "Created";
	private static final String VFLAAS = "_VFLAAS";

	@Autowired
	RestTemplate restTemplate;

	public JSONObject createBulkDeviceAllocationCsvFile(MultipartFile csv, String payload) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonBody = objectMapper.readTree(payload);
		JSONObject response = new JSONObject();
		String opco = jsonBody.get("opco").asText();
		String dataCardId = jsonBody.get("dataCardId").asText();
		if (opco != null) {
			updateDmmConfiguration(opco);
		}
		try {
		BufferedReader reader = new BufferedReader(new InputStreamReader(csv.getInputStream()));
		CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
		boolean skipFirstRow = true; // Flag to skip the first row
		List<DeviceAllocation> deviceAllocationList = new ArrayList<>();
		List<DeviceAllocation> validDeviceAllocationList = new ArrayList<>();
		List<CSVRecord> invalidRecords = new ArrayList<>();
		StringBuilder errorDetails = new StringBuilder();
		String status = null;

		for (CSVRecord record : csvParser) {
			if (skipFirstRow) {
				skipFirstRow = false; // Set the flag to false after skipping the first row
				continue; // Skip processing the first row
			}
			DeviceAllocation deviceAllocation = new DeviceAllocation();

			deviceAllocation.setSerialNumber(record.get(0));
			deviceAllocation.setImei1(record.get(1));
			deviceAllocation.setImei2(record.get(2));
			deviceAllocation.setProductDescription(record.get(3));
			deviceAllocation.setFirstName(record.get(4));
			deviceAllocation.setLastName(record.get(5));
			deviceAllocation.setEmailAddress(record.get(6));
			deviceAllocation.setContactNumber(record.get(7));
			deviceAllocation.setDepartment(record.get(8));
			deviceAllocation.setAdditionalInfo1(record.get(9));
			deviceAllocation.setAdditionalInfo2(record.get(10));

			deviceAllocationList.add(deviceAllocation);

		}
		for (int i = 0; i < deviceAllocationList.size(); i++) {
			if (deviceAllocationList.get(i).getSerialNumber().isEmpty()
					|| deviceAllocationList.get(i).getSerialNumber().isBlank()) {
				errorDetails.append("File Error : Row no ").append(i + 1).append(" : Invalid or Empty data in column ")
						.append(SERIAL_Number).append(",");

			} else if (deviceAllocationList.get(i).getFirstName().isEmpty()
					|| deviceAllocationList.get(i).getFirstName().isBlank()) {
				errorDetails.append("File Error : Row no ").append(i + 1).append(" : Invalid or Empty data in column ")
						.append(FIRST_NAME).append(",");
			} else if (deviceAllocationList.get(i).getLastName().isEmpty()
					|| deviceAllocationList.get(i).getLastName().isBlank()) {
				errorDetails.append("File Error : Row no ").append(i + 1).append(" : Invalid or Empty data in column ")
						.append(LAST_NAME).append(",");
			} else if (deviceAllocationList.get(i).getEmailAddress().isEmpty()
					|| deviceAllocationList.get(i).getEmailAddress().isBlank()
					|| !isValidEmail(deviceAllocationList.get(i).getEmailAddress())) {
				errorDetails.append("File Error : Row no ").append(i + 1).append(" : Invalid or Empty data in column ")
						.append(EMAIL_ADDRESS).append(",");
			} else if (deviceAllocationList.get(i).getContactNumber().isEmpty()
					|| deviceAllocationList.get(i).getContactNumber().isBlank()) {
				errorDetails.append("File Error : Row no ").append(i + 1).append(" : Invalid or Empty data in column ")
						.append(CONTACT_NUMBER).append(",");
				;
			} else if (!isSerialNumbrExistinDmm(deviceAllocationList.get(i).getSerialNumber())) {
				errorDetails.append("File Error : Row no ").append(i + 1).append(" : ").append(SERIAL_Number)
						.append(" not present in DMM").append(",");
			} else {
				validDeviceAllocationList.add(deviceAllocationList.get(i));
			}
		}
		if (errorDetails.length() > 0 && errorDetails.charAt(errorDetails.length() - 1) == ',') {
			errorDetails.deleteCharAt(errorDetails.length() - 1);
		}
		for (DeviceAllocation deviceAllocation : validDeviceAllocationList) {
			dmmDeviceAllocation(deviceAllocation);
		}

		if (!errorDetails.isEmpty() && !(errorDetails == null)) {
			String errorXMLString = errorDataCardXMl(errorDetails.toString(), dataCardId);
			dmmSrRequestStatusErrorUpdateApi(errorXMLString);
		}
		if (validDeviceAllocationList.size() == deviceAllocationList.size()) {
			String XMLDatacard = statusChangeDataCardXMl(BULKALLOCATIONPROCESSED, dataCardId);
			dmmSrRequestStatusErrorUpdateApi(XMLDatacard);
			String mssg = "Bulk allocation success.";
			generateResponse(200, mssg, " ", response);
		}
		else if (validDeviceAllocationList.isEmpty()) {
			String XMLDatacard = statusChangeDataCardXMl(BULKALLOCATIONFAILED, dataCardId);
			dmmSrRequestStatusErrorUpdateApi(XMLDatacard);
			String mssg = "Bulk allocation failed.";
			generateResponse(200, mssg, " ", response);
		}
		else {
			String XMLDatacard = statusChangeDataCardXMl(BULKALLOCATIONPROCESSEDWITHERROR, dataCardId);
			dmmSrRequestStatusErrorUpdateApi(XMLDatacard);
			String mssg = "Bulk allocation processed with error";
			generateResponse(200, mssg, " ", response);
		}
		}
		catch (Exception exception) {
            if (exception instanceof HttpStatusCodeException) {
                try {
                    int errorStatusCode = ((HttpStatusCodeException) exception).getStatusCode().value();
                    if (errorStatusCode == 401 || errorStatusCode == 503) {
                        generateEmail(dataCardId , (HttpStatusCodeException) exception);
                        String msg = "Email generated successfully for dataCard : " + dataCardId;
                        generateResponse(500, msg, " ", response);
                    }
                } catch (MessagingException | TemplateException | IOException ex) {
                    String msg = "Email generation failed for dataCard : " + dataCardId;
                    generateResponse(500, msg, " ", response);
                }
            } else {
                String errorMsg = "Error while processing uploaded file : " + exception.getMessage();
                sendErrorDetailsToKafkaTopic("Error while processing uploaded file of serviceRequest : " +dataCardId + " | error : " + exception.getMessage());
                generateResponse(500, exception.getMessage(), " ", response);
            }
        }
        return response;
    }
	

	private boolean isValidEmail(String email) {
		// Simple email validation using regex
		String regex = "^(.+)@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	private boolean isSerialNumbrExistinDmm(String serialNumber) {
		String url = dmmUri
				+ "/api/itsm/search.ws?&query=SELECT count(*) FROM entity WHERE entity.template.code='ASSET' AND $ASSET_SERIALNUMBER$='"
				+ serialNumber + "'";
		HttpHeaders headers = createHeaders(dmmApiUserName, dmmApiPassword);
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
		int result = Integer.parseInt((responseEntity.getBody()).replaceAll("<[^>]*>", "").trim());
		if (result == 1) {
			return true;

		} else {
			return false;
		}
	}

	private HttpHeaders createHeaders(String username, String password) {
		HttpHeaders headers = new HttpHeaders();
		String auth = username + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
		String authHeader = BASIC + " " + new String(encodedAuth);
		headers.set(AUTHORIZATION, authHeader);
		return headers;
	}

	private void updateDmmConfiguration(String opco) {
		DmmConfigurations.Dmm dmm = findDmmByOpco(opco);
		if (dmm != null) {
			dmmUri = dmm.getUri().getExternal();
			dmmApiUserName = dmm.getApi().getUsername();
			dmmApiPassword = dmm.getApi().getPassword();
		}
	}

	private DmmConfigurations.Dmm findDmmByOpco(String opco) {
		if (dmmConfigurations != null && dmmConfigurations.getDmm() != null) {
			return dmmConfigurations.getDmm().stream().filter(d -> d.getName().equals(opco)).findFirst().orElse(null);
		}
		return null;
	}

	private void dmmDeviceAllocation(DeviceAllocation deviceAllocation) {
		StringBuilder deviceAllocationXMLData = new StringBuilder();
		deviceAllocationXMLData.append("<entity>");
		deviceAllocationXMLData.append("<template code='" + "ASSET" + "'/>");
		deviceAllocationXMLData.append("<attribute code='ASSET_SERIALNUMBER'>");
		deviceAllocationXMLData.append("<value>").append(deviceAllocation.getSerialNumber()).append("</value>");
		deviceAllocationXMLData.append("</attribute>");
		deviceAllocationXMLData.append("<attribute code='ASSET_USER_FIRSTNAME'>");
		deviceAllocationXMLData.append("<value>").append(deviceAllocation.getFirstName()).append("</value>");
		deviceAllocationXMLData.append("</attribute>");
		deviceAllocationXMLData.append("<attribute code='ASSET_USER_LASTNAME'>");
		deviceAllocationXMLData.append("<value>").append(deviceAllocation.getLastName()).append("</value>");
		deviceAllocationXMLData.append("</attribute>");
		deviceAllocationXMLData.append("<attribute code='ASSET_USER_EMAIL'>");
		deviceAllocationXMLData.append("<value>").append(deviceAllocation.getEmailAddress()).append("</value>");
		deviceAllocationXMLData.append("</attribute>");
		deviceAllocationXMLData.append("<attribute code='ASSET_USER_CONTACTNO'>");
		deviceAllocationXMLData.append("<value>").append(deviceAllocation.getContactNumber()).append("</value>");
		deviceAllocationXMLData.append("</attribute>");
		deviceAllocationXMLData.append("<attribute code='ASSET_USER_DEPARTMENT'>");
		deviceAllocationXMLData.append("<value>").append(deviceAllocation.getDepartment()).append("</value>");
		deviceAllocationXMLData.append("</attribute>");
		deviceAllocationXMLData.append("<attribute code='ASSET_USER_ADDINFO1'>");
		deviceAllocationXMLData.append("<value>").append(deviceAllocation.getAdditionalInfo1()).append("</value>");
		deviceAllocationXMLData.append("</attribute>");
		deviceAllocationXMLData.append("<attribute code='ASSET_USER_ADDINFO2'>");
		deviceAllocationXMLData.append("<value>").append(deviceAllocation.getAdditionalInfo2()).append("</value>");
		deviceAllocationXMLData.append("</attribute>");
		deviceAllocationXMLData.append("</entity>");

		String url = dmmUri + dmmAssetAllocationUri;
		HttpHeaders headers = createHeaders(dmmApiUserName, dmmApiPassword);
		HttpEntity<String> requestEntity = new HttpEntity<>(deviceAllocationXMLData.toString(), headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	private String errorDataCardXMl(String errorDetails, String dataCardId) {
		StringBuilder updateErrorDataCard = new StringBuilder();
		updateErrorDataCard.append("<?xml version='1.0' encoding='utf-8'?>");
		updateErrorDataCard.append("<entityset>");
		updateErrorDataCard.append("<entity>");
		updateErrorDataCard.append("<template code='").append("ServiceRequest").append("'/>");
		updateErrorDataCard.append("<attribute code='efecte_id'>");
		updateErrorDataCard.append("<value>").append(dataCardId).append("</value>");
		updateErrorDataCard.append("</attribute>");
		JSONObject externalComment = new JSONObject();
		externalComment.put("date", LocalDateTime.now().toString());
		externalComment.put("author", "WebAPI_Integration");
		externalComment.put("message", errorDetails);
		updateErrorDataCard.append("<attribute code='external_comments'>");
		updateErrorDataCard.append("<value>").append(externalComment).append("</value>");
		updateErrorDataCard.append("</attribute>");
		updateErrorDataCard.append("</entity>");
		updateErrorDataCard.append("</entityset>");

		return updateErrorDataCard.toString();
	}

	private String statusChangeDataCardXMl(String status, String dataCardId) {
		StringBuilder updateErrorDataCard = new StringBuilder();
		updateErrorDataCard.append("<?xml version='1.0' encoding='utf-8'?>");
		updateErrorDataCard.append("<entityset>");
		updateErrorDataCard.append("<entity>");
		updateErrorDataCard.append("<template code='").append("ServiceRequest").append("'/>");
		updateErrorDataCard.append("<attribute code='efecte_id'>");
		updateErrorDataCard.append("<value>").append(dataCardId).append("</value>");
		updateErrorDataCard.append("</attribute>");
		updateErrorDataCard.append("<attribute code='status'>");
		updateErrorDataCard.append("<value>").append(status).append("</value>");
		updateErrorDataCard.append("</attribute>");
		updateErrorDataCard.append("</entity>");
		updateErrorDataCard.append("</entityset>");

		return updateErrorDataCard.toString();
	}

	private void dmmSrRequestStatusErrorUpdateApi(String XMLdatacard) {
		String url = dmmUri + dmmDataCardImportApi;
		HttpHeaders headers = createHeaders(dmmApiUserName, dmmApiPassword);
		HttpEntity<String> requestEntity = new HttpEntity<>(XMLdatacard, headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				String.class);
	}

	private void generateResponse(int statusCode, String msg, String invalidRow, JSONObject response) {
		response.put(STATUS_CODE, statusCode);
		response.put(MESSAGE, msg);
		response.put(INVALID_ROWS, invalidRow);
	}

	private void generateEmail(String payload, HttpStatusCodeException exception) throws MessagingException, IOException, TemplateException {
        String subject = "In NB-Bulk_Order microservice - bulk-order-excel api received " + exception.getStatusCode() + " from DMM ";
        String service = "NB-Bulk_Order microservice - bulk-order-excel api";
        Map<String, String> model = generateModelForMailModel(service, exception.getStatusCode());
        model.put(PAYLOAD, payload);
        MailModel mailModel = getMailModel(subject, model);
        emailService.sendMail(mailModel);
    }

	private Map<String, String> generateModelForMailModel(String service, HttpStatus statusCode) {
        Map<String, String> model = new HashMap<>();
        model.put(SERVICE, service);
        model.put(ERROR, statusCode.toString());
        model.put(DMM, dmmUri);
        return model;
    }

	private MailModel getMailModel(String subject, Map<String, String> model) {
        MailModel mailModel = new MailModel();
        mailModel.setFrom(fromEmail);
        mailModel.setTo(toEmail);
        mailModel.setCc(ccEmail);
        mailModel.setModel(model);
        mailModel.setSubject(subject);
        return mailModel;
    }

	private void sendErrorDetailsToKafkaTopic(String errorDescription) {
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setTimeStamp(LocalDateTime.now());
        errorDetails.setPayload("");
        errorDetails.setResolutionStatus(CREATED);
        errorDetails.setService("NB-Bulk-Order");
        errorDetails.setErrorDescription(errorDescription);
        errorDetails.setPath("/api/bulk-device-allocation/v1/csv");
        log.info(" kafka errorDetails =====> " + errorDetails);
        kafkaTemplate.send(generalErrorTopic, (UUID.randomUUID() + VFLAAS), errorDetails);
    }

}
