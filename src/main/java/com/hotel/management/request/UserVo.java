package com.hotel.management.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {

	private Long userId;

	private String name;

	private String emailId;

	private String password;

	private String address;

	private String mobile;
	
	private Date registeredDate;

}
