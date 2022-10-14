package com.hotel.management.exception;

public enum ErrorDetails {

	// User Error codes
	USER_EXISTS(001, "User already exists with same email. Please login", "error_user_email_mobile_exists"),
	USER_NOT_FOUND(002, "User not found", "error_user_not_found"),
	MOBILE_NOT_VALID(003, "Please enter valid mobile number", "mobile_not_valid"),
	VALUES_NOT_FOUND(004, "Please enter the credentials or values", "values_not_found"),
	REQUEST_VALUE_NOT_FOUND(005, "Value cannot be blank", "request_value_not_found"),
	RESOURCE_NOT_FOUND(007, "Requested resource not found", "resource_not_found"),

	// Hotel Error codes
	HOTEL_ALREADY_EXISTS(101, "Hotel name already exists", "hotel_name_exists"),
	HOTEL_NOT_FOUND(102, "Hotel not found", "hotel_not_found");



	private final int code;
	private final String message;
	private final String uiErrorKey;

	private ErrorDetails(int code, String description, String uiErrorKey) {
		this.code = code;
		this.message = description;
		this.uiErrorKey = uiErrorKey;
	}

	public String getMessage() {
		return message;
	}

	public int getCode() {
		return code;
	}

	public String getUiErrorKey() {
		return uiErrorKey;
	}

}
