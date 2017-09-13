package com.developairs.exception;

public enum ResponseCode {
	SUCCESSFUL(0),
	ERR_DB(1),
	ERR_SEND_QUEUE(2),
	ERR_OBJECT_MSG(3),
	ERR_CALCULATE(4),
	ERR_UNKNOW(5), 
	ERR_RECEIVE_QUEUE(6);
	
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
