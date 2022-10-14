package com.hotel.management.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.management.exception.AppException;
import com.hotel.management.exception.ErrorDetails;
import com.hotel.management.model.User;
import com.hotel.management.repo.UserRepo;
import com.hotel.management.service.Utility;

@Service
public class UtilityImpl implements Utility {

	@Autowired
	private UserRepo<User> userRepo;

	public User getUser(String emailId) {
		try {

			Optional<User> existingUser = userRepo.findByEmailId(emailId);

			if (existingUser.isPresent()) {
				return existingUser.get();
			}
			throw new AppException(ErrorDetails.USER_NOT_FOUND);

		} catch (Exception e) {
			throw new AppException(ErrorDetails.USER_NOT_FOUND);
		}

	}

}
