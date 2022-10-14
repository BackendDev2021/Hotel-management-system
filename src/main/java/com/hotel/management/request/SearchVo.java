package com.hotel.management.request;

import lombok.Data;

@Data
public class SearchVo {

	private String hotelId;
	
	private Long cityId;
	
	private String hotelName;
	
	private String cityName;
	
	private String contactNumber;
	
	private String typeOfHotel;
	
	private Long typeOfRoom;
	
	private 	Integer tarrif;
}
