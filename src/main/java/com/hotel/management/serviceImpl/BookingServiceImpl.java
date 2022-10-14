package com.hotel.management.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.hotel.management.model.BookingInfo;
import com.hotel.management.repo.BookingRepo;
import com.hotel.management.response.AppResponse;
import com.hotel.management.service.BookingService;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	BookingRepo bookingRepo;

	@Override
	public AppResponse<Boolean> bookRoom(BookingInfo bookingRequest) {
		bookingRepo.save(bookingRequest);
		return new AppResponse<Boolean>(HttpStatus.CREATED, 201, true);
	}

}
