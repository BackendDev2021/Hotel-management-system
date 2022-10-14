package com.hotel.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.management.model.BookingInfo;
import com.hotel.management.response.AppResponse;
import com.hotel.management.service.BookingService;

/**
 * 
 * @author Mohanlal
 *
 */
@RestController
@RequestMapping(value = "/hotel-management-application")
@Validated
public class BookingController {

	@Autowired
	BookingService bookingService;
	
	//@Secured(value = Constants.SECURITY_AN)
	@PostMapping("/booking")
	public ResponseEntity<AppResponse<Boolean>> bookRoom(@RequestBody BookingInfo bookingRequest){
		return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.bookRoom(bookingRequest));
	}
	
}
