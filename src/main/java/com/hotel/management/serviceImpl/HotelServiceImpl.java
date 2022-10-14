package com.hotel.management.serviceImpl;

import java.util.Collection;
import java.util.Random;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.hotel.management.exception.AppException;
import com.hotel.management.exception.ErrorDetails;
import com.hotel.management.model.City;
import com.hotel.management.model.Hotel;
import com.hotel.management.model.Room;
import com.hotel.management.repo.HotelRepo;
import com.hotel.management.request.SearchVo;
import com.hotel.management.response.AppResponse;
import com.hotel.management.service.HotelService;

import javassist.NotFoundException;

@Service
public class HotelServiceImpl implements HotelService {

	@Autowired
	private HotelRepo hotelRepo;

	@Override
	public AppResponse<String> addHotelDetails(Hotel hotel) {
		Hotel existHotelName = hotelRepo.findByName(hotel.getHotelName());
		if (ObjectUtils.isNotEmpty(existHotelName)) {
			throw new AppException(ErrorDetails.HOTEL_ALREADY_EXISTS);
		}
		hotel.setHotelid(hotelIdGenerator());
		hotelRepo.save(hotel);
		return new AppResponse<String>(HttpStatus.CREATED, 201, "Hotel saved successfully");
	}

	@Override
	public AppResponse<String> removeHotelDetails(String hotelId) throws NotFoundException {
		try {
			hotelRepo.deleteByHotelId(hotelId);
			return new AppResponse<String>(HttpStatus.NO_CONTENT, 204, "Removed successfully");
		} catch (ResourceNotFoundException ex) {
			throw new AppException(ErrorDetails.HOTEL_NOT_FOUND);
		}
	}

	@Override
	public AppResponse<Hotel> editHotelDetails(String hotelId, Hotel hotel) {
		Hotel existHotel = hotelRepo.findByHotelId(hotelId);
		if (ObjectUtils.isEmpty(existHotel)) {
			throw new AppException(ErrorDetails.HOTEL_ALREADY_EXISTS);
		}
		existHotel.setHotelName(hotel.getHotelName());
		existHotel.setHotelAddress(hotel.getHotelAddress());
		existHotel.setHotelContact(hotel.getHotelContact());
		existHotel.setTypeOfHotel(hotel.getTypeOfHotel());
		existHotel.setCity(hotel.getCity());
		hotelRepo.save(existHotel);
		return new AppResponse<Hotel>(HttpStatus.ACCEPTED, 406, existHotel);
	}

	private String hotelIdGenerator() {
		Random integer = new Random();
		return "H" + integer.nextInt(999);
	}

	@Override
	public AppResponse<Page<Hotel>> allHotelDetails(Integer pageNo, Integer offset) {
		Pageable pageHotel = PageRequest.of(pageNo, 20, Sort.by(Direction.ASC, "hotelName"));
		return new AppResponse<Page<Hotel>>(HttpStatus.OK, 200, hotelRepo.findAll(pageHotel));
	}

	@Override
	public AppResponse<Collection<Hotel>> getHotelbySearchDatas(SearchVo search) {
		Hotel searchHotel = new Hotel();
		City searchCity = new City();
		Room searchRoom = new Room();
		searchHotel.setHotelid(search.getHotelId());
		searchHotel.setHotelName(search.getHotelName());
		searchHotel.setTypeOfHotel(search.getTypeOfHotel());
		searchRoom.setId(search.getTypeOfRoom());
		searchRoom.setTarrifAmount(search.getTarrif());
		searchCity.setId(search.getCityId());
		ExampleMatcher matcher = ExampleMatcher.matchingAny()
				.withMatcher(search.getHotelId(), ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher(search.getHotelName(), ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher(search.getTypeOfHotel(), ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
		.withMatcher(search.getCityName(), ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
		.withMatcher(search.getCityId().toString(), ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		Example<Hotel> hotelExample = Example.of(searchHotel, matcher);
		return new AppResponse<Collection<Hotel>>(HttpStatus.OK, 200, hotelRepo.findAll(hotelExample));
	}
}
