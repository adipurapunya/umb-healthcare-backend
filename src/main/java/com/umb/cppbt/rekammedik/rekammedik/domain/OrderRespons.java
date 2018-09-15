package com.umb.cppbt.rekammedik.rekammedik.domain;

import org.apache.http.HttpStatus;

public class OrderRespons {
	
	private String message;
	
	private org.springframework.http.HttpStatus httpStatus;
	
	private String date;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public org.springframework.http.HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(org.springframework.http.HttpStatus created) {
		this.httpStatus = created;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	

}
