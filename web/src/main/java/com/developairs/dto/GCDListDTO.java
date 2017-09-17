package com.developairs.dto;

import java.util.List;
/**
 * 
 * @author Ali Abazari
 *
 */
public class GCDListDTO extends BaseDTO {
	
	private List<Integer> gcdList;
	
	public List<Integer> getGcdList() {
		return gcdList;
	}
	public void setGcdList(List<Integer> gcdList) {
		this.gcdList = gcdList;
	}
	
	public GCDListDTO() {
	}

	public GCDListDTO(String responseCode, List<Integer> gcdList) {
		this.setResponse(responseCode);
		this.setGcdList(gcdList);
	}
}
