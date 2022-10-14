package com.hotel.management.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_details")
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@NotNull(message = "Field is mandatory")
	@Size(min = 3, max = 30, message = "size must be 30")
	@Pattern(regexp = "[a-zA-Z0-9]+", message = "must not contain special characters")
	@Column(name = "name")
	private String name;

	@Email(message = "Email is not valid", regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
	@NotNull(message = "Email cannot be empty")
	@Column(name = "email_id")
	private String emailId;

	@NotBlank(message = "Field is mandatory")
	//@Size(min = 6, max = 20, message = "size must be 20")
	@Column(name = "user_pass")
	private String password;

	@NotNull(message = "Field is mandatory")
	@Size(max = 100)
	@Column(name = "address")
	private String address;

	@NotNull(message = "Field cannot be blank")
	@Size(min = 10, max = 10, message = "size must be 10")
	@Pattern(regexp = "[0-9]+", message = "allows only numeric values")
	@Column(name = "ph_no")
	private String mobile;
	
	@Column(name = "date_of_reg")
	private Date registeredDate;
}
