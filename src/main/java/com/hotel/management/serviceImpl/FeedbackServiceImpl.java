package com.hotel.management.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.hotel.management.model.Feedback;
import com.hotel.management.repo.FeedbackRepo;
import com.hotel.management.response.AppResponse;
import com.hotel.management.service.FeedbackService;

@Service
public class FeedbackServiceImpl implements FeedbackService{

	@Autowired
	private FeedbackRepo feedbackRepo;
	
	
	@Override
	public AppResponse<String> saveFeedback(Feedback feedback) {
		feedbackRepo.save(feedback);
		return new AppResponse<String>(HttpStatus.CREATED, 201, "saved successfully");
	}


	@Override
	public AppResponse<Feedback> getFeedback(Long id) {
		Feedback existFeedback = feedbackRepo.findById(id).get();
		return new AppResponse<Feedback>(HttpStatus.OK, 200, existFeedback);
	}
	

}
