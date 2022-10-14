package com.hotel.management.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignUpResponse {

	private String emailId;
	
	private String name;
	
	private String message;
	
	private String role;
}
