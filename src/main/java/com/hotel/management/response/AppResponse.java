package com.hotel.management.response;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppResponse<T>{

	private HttpStatus status;
	private Integer statusCode;
	private T data;

}
