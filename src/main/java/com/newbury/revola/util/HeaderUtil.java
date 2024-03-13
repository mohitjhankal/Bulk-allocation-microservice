package com.newbury.revola.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;


/**
 * @author chengalvaraya.swami
 *
 */
public final class HeaderUtil {
	private static final Logger log = LoggerFactory.getLogger(HeaderUtil.class);
	private HeaderUtil() {
	}
	public static HttpHeaders createAlert(String message, String param) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-productsApp-alert", message);
		headers.add("X-productsApp-params", param);
		return headers;
	}

	public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
		return createAlert("A new " + entityName + " is created with identifier " + param, param);
	}

	public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
		return createAlert("A " + entityName + " is updated with identifier " + param, param);
	}

	public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
		return createAlert("A " + entityName + " is deleted with identifier " + param, param);
	}

	public static HttpHeaders createFailureAlert(String entityName, String defaultMessage) {
		log.error("Entity processing failed, {}", defaultMessage);
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-productsApp-error", defaultMessage);
		headers.add("X-productsApp-params", entityName);
		return headers;
	}

}