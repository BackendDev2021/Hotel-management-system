package com.hotel.management.service;

import com.hotel.management.model.Feedback;
import com.hotel.management.response.AppResponse;

public interface FeedbackService {

	public AppResponse<String> saveFeedback(Feedback feedback);

	public AppResponse<Feedback> getFeedback(Long id);

}
