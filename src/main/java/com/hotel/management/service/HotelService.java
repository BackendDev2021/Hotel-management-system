package com.hotel.management.service;

import java.util.Collection;

import org.springframework.data.domain.Page;

import com.hotel.management.model.Hotel;
import com.hotel.management.request.SearchVo;
import com.hotel.management.response.AppResponse;

import javassist.NotFoundException;

public interface HotelService {

	public AppResponse<String> addHotelDetails(Hotel hotel);

	public AppResponse<String> removeHotelDetails(String hotelId) throws NotFoundException;

	public AppResponse<Hotel> editHotelDetails(String hotelId, Hotel hotel);

	public AppResponse<Page<Hotel>> allHotelDetails(Integer pageNo, Integer offset);

	public AppResponse<Collection<Hotel>> getHotelbySearchDatas(SearchVo search);

}
