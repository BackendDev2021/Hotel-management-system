package com.hotel.management.request;

import java.util.Date;

import lombok.Data;

@Data
public class BookingVo {

	private Long bookingId;
	
	private Long userId;
	
	private Long roomId;
	
	private Long hotelId;
	
	private Long roomTypeId;
	
	private Date startDate;
	
	private Date endDate;
	
	private Integer payment;
	
	private String paymentStatus;
}
