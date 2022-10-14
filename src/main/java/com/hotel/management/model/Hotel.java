package com.hotel.management.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "hotel_details")
public class Hotel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String hotelid;
	
	@NotBlank(message = "Hotel name cannot be empty")
	private String hotelName;
	
	@NotBlank(message = "Hotel address cannot be empty")
	private String hotelAddress;
	
	@NotBlank(message = "Hotel contact cannot be empty")
	private String hotelContact;
	
	//@Enumerated(EnumType.STRING)
	private String typeOfHotel;
	
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
	@JoinColumn(name = "city_id",referencedColumnName = "id")
	private City city;
}
