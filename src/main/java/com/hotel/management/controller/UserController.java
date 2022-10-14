package com.hotel.management.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.management.request.LoginRequest;
import com.hotel.management.request.UserVo;
import com.hotel.management.response.AppResponse;
import com.hotel.management.response.LoginResponse;
import com.hotel.management.response.SignUpResponse;
import com.hotel.management.service.UserService;

@RestController
@RequestMapping(value = "/hotel-management-application")
@Validated
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<AppResponse<LoginResponse>> login(@RequestBody LoginRequest request) throws Exception {
		return ResponseEntity.status(HttpStatus.OK).body(userService.login(request.getEmailId(), request.getPassword()));
	}

	@PostMapping("/signup")
	public  ResponseEntity<AppResponse<SignUpResponse>> signUp(@Valid @RequestBody UserVo request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(request));
	}

}
