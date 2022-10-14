package com.hotel.management.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.hotel.management.model.BookingInfo;

@EnableJpaRepositories
@EnableJpaAuditing
public interface BookingRepo extends JpaRepository<BookingInfo, Long>{

}
