package com.hotel.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.management.model.Feedback;
import com.hotel.management.response.AppResponse;
import com.hotel.management.service.FeedbackService;
import com.hotel.management.utils.Constants;

@RestController
@RequestMapping("/hotel-management-application")
@Validated
public class FeedbackController {

	@Autowired
	private FeedbackService feedbackService;
	
	@Secured(value = Constants.SECURITY_UR)
	@PostMapping("/feedback")
	public ResponseEntity<AppResponse<String>> saveFeedback(@RequestBody Feedback feedback){
		return ResponseEntity.status(HttpStatus.CREATED).body(feedbackService.saveFeedback(feedback));
	}
	
	@Secured(value = Constants.SECURITY_UR)
	@GetMapping("feedback")
	public ResponseEntity<AppResponse<Feedback>> getFeedback(@RequestParam Long id){
		return ResponseEntity.status(HttpStatus.OK).body(feedbackService.getFeedback(id));
	}
}
