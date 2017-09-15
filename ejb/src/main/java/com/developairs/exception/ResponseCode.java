package com.developairs.exception;

public enum ResponseCode {
	SUCCESSFUL(0),
	ERR_DB(1),
	ERR_SEND_QUEUE(2),
	ERR_OBJECT_MSG(3),
	ERR_CALCULATE(4),
	ERR_RECEIVE_QUEUE(5),
	ERR_AUTHENTICATION(6),
	ERR_AUTHORIZATION(7),
	ERR_UNKNOW(10); 
	
	ResponseCode(){
	}
	ResponseCode(int i){
		this.value = i;
	}
	
	private int value;
	public int getValue() {
		return value;
	}
	

}
