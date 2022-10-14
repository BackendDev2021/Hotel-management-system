package com.hotel.management.utils;

public enum HotelType {
	
	TWO_STAR(2, "Star"), THREE_STAR(3, "Star"), FOUR_STAR(4, "Star"), FIVE_STAR(5, "Star");

	private final Integer type;

	private final String name;

	public Integer getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	private HotelType(Integer type, String name) {
		this.type = type;
		this.name = name;
	}
}
