package com.hotel.management.response;

import lombok.Data;

@Data
public class LoginResponse {

	private String token;
	private String userName;
	private String emailId;
	private String role;
	

	public LoginResponse() {
		super();
	}
}
