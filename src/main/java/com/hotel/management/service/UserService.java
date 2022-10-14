package com.hotel.management.service;

import javax.validation.Valid;

import com.hotel.management.request.UserVo;
import com.hotel.management.response.AppResponse;
import com.hotel.management.response.LoginResponse;
import com.hotel.management.response.SignUpResponse;

public interface UserService {

	public AppResponse<SignUpResponse> signUp(@Valid UserVo request);

	public AppResponse<LoginResponse> login(String emailId, String password) throws Exception;

}
