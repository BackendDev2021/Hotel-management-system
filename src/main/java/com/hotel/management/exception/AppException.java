package com.hotel.management.exception;

public class AppException  extends RuntimeException{

private static final long serialVersionUID = 1L;
	
	private ErrorDetails responseDetail;
	private String customMessage;

    public AppException(ErrorDetails responseDetail) {
        this.responseDetail = responseDetail;
    }
    
    
    public AppException(String customMessage) {
        this.customMessage = customMessage;
    }

	public ErrorDetails getResponseDetail() {
		return responseDetail;
	}


	public String getCustomMessage() {
		return customMessage;
	}


	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}
	
	
}
