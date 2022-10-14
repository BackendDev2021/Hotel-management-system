package com.hotel.management.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.management.model.Hotel;
import com.hotel.management.request.SearchVo;
import com.hotel.management.response.AppResponse;
import com.hotel.management.service.HotelService;
import com.hotel.management.utils.Constants;

import javassist.NotFoundException;

/**
 * 
 * @author Mohanlal
 *
 */
@RestController
@RequestMapping(value = "/hotel-management-application")
@Validated
public class HotelController {

	@Autowired
	HotelService hotelService;

	/**
	 * 
	 * @param hotel
	 * @return
	 */
	@Secured(value = Constants.SECURITY_UR)
	@PostMapping("/hotel-details")
	public ResponseEntity<AppResponse<String>> addHotelDetails(@RequestBody Hotel hotel) {
		return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.addHotelDetails(hotel));
	}

	/**
	 * 
	 * @param hotelId
	 * @param hotel
	 * @return
	 */
	@Secured(value = Constants.SECURITY_UR)
	@PatchMapping("/hotel-details")
	public ResponseEntity<AppResponse<Hotel>> editHotelDetails(@RequestParam String hotelId, @RequestBody Hotel hotel) {
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(hotelService.editHotelDetails(hotelId, hotel));
	}

	@Secured(value = Constants.SECURITY_UR)
	@DeleteMapping("/hotel-details")
	public ResponseEntity<AppResponse<String>> removeHotelDetails(@RequestParam String hotelId)
			throws NotFoundException {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(hotelService.removeHotelDetails(hotelId));
	}

	/**
	 * 
	 * @param pageNo
	 * @param offset
	 * @return
	 */
	@Secured(value = Constants.SECURITY_UR)
	@GetMapping("/hotel-details")
	public ResponseEntity<AppResponse<Page<Hotel>>> allHotelDetails(@RequestParam Integer pageNo,Integer offset){
		return ResponseEntity.status(HttpStatus.OK).body(hotelService.allHotelDetails(pageNo,offset));
	}
	
	/**
	 * 
	 * @param search
	 * @return
	 */
	@Secured(value = Constants.SECURITY_UR)
	@PostMapping("/search-hotel-details")
	public ResponseEntity<AppResponse<Collection<Hotel>>> getHotelbySearchDatas(@RequestBody SearchVo search){
		return ResponseEntity.status(HttpStatus.OK).body(hotelService.getHotelbySearchDatas(search));
	}
	
	
}
