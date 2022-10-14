package com.hotel.management.communication;

import java.util.Map;

import lombok.Data;

@Data
public class Mail {

	private String from;

	private String mailTo;

	private String subject;

	private java.util.List<Object> attachments;

	private Map<String, Object> props;

	public Object setProps(Object put) {
		// TODO Auto-generated method stub
		return null;
	}
}
