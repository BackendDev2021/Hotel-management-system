package com.hotel.management.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.hotel.management.model.User;

public interface UserRepo<T extends User> extends CrudRepository<T, Long>, JpaSpecificationExecutor<User> {

	Optional<User> findByEmailId(String emailId);

}
