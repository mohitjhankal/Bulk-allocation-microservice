package com.newbury.revola.controller;

import java.io.IOException;
import java.net.URISyntaxException;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.newbury.revola.service.ProcessCsvFiles;
import com.newbury.revola.util.HeaderUtil;

@RestController
@RequestMapping("/api")
public class BulkAllocationProcessAPI {

	private final Logger log = LoggerFactory.getLogger(BulkAllocationProcessAPI.class);

	@Value("${dmm.general.error.topic}")
	String generalErrorTopic;

	@Value("${dmm.general.error.resolution.status}")
	String errorDetailResolutionStatus;

	@Autowired
	private ProcessCsvFiles processCsvFiles;

	private static final String ENTITY_NAME = "NB-Bulk-Allocation";
	private static final Integer STATUS_200 = 200;
	private static final String STATUS_CODE = "statusCode";
	private static final String MESSAGE = "message";

	@PostMapping("/bulk-allocation-process/v1/csv")
	public ResponseEntity<JSONObject> processBulkDeviceAllocationCsvFile(@RequestParam("file") MultipartFile csv,
			@RequestParam("serviceRequestDetail") String payload) throws URISyntaxException, IOException {
		log.info(" processBulkDeviceAllocationCsvFile() method executed ======> ");
		JSONObject response = processCsvFiles.createBulkDeviceAllocationCsvFile(csv, payload);
		if (response.get(STATUS_CODE).equals(STATUS_200)) {
			return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, "")).body(response);
		} else {
			Integer status_code = (Integer) response.get(STATUS_CODE);
			String message = response.get(MESSAGE).toString();
			return ResponseEntity.status(status_code).headers(HeaderUtil.createFailureAlert(ENTITY_NAME, message))
					.body(response);
		}
	}
}