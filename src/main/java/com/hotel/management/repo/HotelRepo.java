package com.hotel.management.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.hotel.management.model.Hotel;

@EnableJpaRepositories
public interface HotelRepo extends JpaRepository<Hotel, Long> {

	@Query("from Hotel where hotel_name = ?1")
	Hotel findByName(String hotelName);

	@Query("from Hotel where hotelid = ?1")
	Hotel findByHotelId(String hotelId);

	@Query("delete Hotel where hotelid = ?1")
	void deleteByHotelId(String hotelId);
}
