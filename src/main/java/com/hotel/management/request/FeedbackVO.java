package com.hotel.management.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackVO {

	private Long userId;
	
	private Integer feedback;
	
	private String comments;
}
