package com.hotel.management.service;

import com.hotel.management.model.BookingInfo;
import com.hotel.management.response.AppResponse;

public interface BookingService {

	public AppResponse<Boolean> bookRoom(BookingInfo bookingRequest);

}
