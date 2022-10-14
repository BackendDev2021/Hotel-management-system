package com.hotel.management.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.amazonaws.util.StringUtils;
import com.hotel.management.communication.EmailServiceConfig;
import com.hotel.management.exception.AppException;
import com.hotel.management.exception.ErrorDetails;
import com.hotel.management.repo.UserRepo;
import com.hotel.management.request.UserVo;
import com.hotel.management.response.AppResponse;
import com.hotel.management.response.LoginResponse;
import com.hotel.management.response.SignUpResponse;
import com.hotel.management.security.JwtTokenUtil;
import com.hotel.management.service.UserService;
import com.hotel.management.service.Utility;
import com.hotel.management.utils.Constants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService, UserService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	Utility utilityService;

	@Autowired
	UserRepo<com.hotel.management.model.User> userRepo;

	@Autowired
	EmailServiceConfig emailService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {

		com.hotel.management.model.User user = utilityService.getUser(emailId);
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("user"));
		return new User(user.getEmailId(), user.getPassword(), authorities);
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	@Override
	public AppResponse<LoginResponse> login(String emailId, String password) throws Exception {

		if (StringUtils.isNullOrEmpty(emailId) && StringUtils.isNullOrEmpty(password)) {
			log.error(Constants.VALUES_NOT_FOUND);
			throw new AppException(ErrorDetails.VALUES_NOT_FOUND);
		}
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		com.hotel.management.model.User user = utilityService.getUser(emailId);
		if (passwordEncoder.matches(password, user.getPassword())) {
			authenticate(emailId, password);
			return new AppResponse<>(HttpStatus.OK, 200, getLoginResponse(user));
		}

		throw new AppException(ErrorDetails.USER_NOT_FOUND);
	}

	@Override
	public AppResponse<SignUpResponse> signUp(@Valid UserVo request) {
		Optional<com.hotel.management.model.User> userRequest = userRepo.findByEmailId(request.getEmailId());
		if (userRequest.isPresent()) {
			log.error(Constants.USER_ALREADY_EXISTS);
			throw new AppException(ErrorDetails.USER_EXISTS);
		}
		// Encoding password
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		com.hotel.management.model.User user = new com.hotel.management.model.User();
		user.setName(request.getName());
		user.setEmailId(request.getEmailId());
		user.setAddress(request.getAddress());
		user.setMobile(request.getMobile());
		user.setRegisteredDate(request.getRegisteredDate());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		userRepo.save(user);
		return new AppResponse<>(HttpStatus.CREATED, 201, signUpResponse(user));
	}

	private LoginResponse getLoginResponse(com.hotel.management.model.User user) {

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("user"));

		Map<String, Object> claims = new HashMap<>();
		claims.put("name", user.getName());
		claims.put("role", "USER");

		final String token = jwtTokenUtil.generateToken(new User(user.getEmailId(), user.getPassword(), authorities),
				claims);

		LoginResponse response = new LoginResponse();
		response.setToken(token);
		response.setUserName(user.getName());
		response.setEmailId(user.getEmailId());
		response.setRole("USER");
		return response;
	}

	private SignUpResponse signUpResponse(com.hotel.management.model.User user) {
		return new SignUpResponse(Constants.REGISTERED_SUCCESSFULLY, user.getEmailId(), user.getName(), "USER");
	}

}
