package com.hotel.management.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.hotel.management.model.Feedback;

@EnableJpaRepositories
public interface FeedbackRepo extends JpaRepository<Feedback, Long>{

}
