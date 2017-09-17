package com.developairs.dto;
/**
 * 
 * @author Ali Abazari
 *
 */
public class GCD_DTO extends BaseDTO {
	
	private int gcd;
	public int getGcd() {
		return gcd;
	}
	public void setGcd(int gcd) {
		this.gcd = gcd;
	}
	
	public GCD_DTO() {
	}
	
	public GCD_DTO(String responseCode, int gcd) {
		this.setResponse(responseCode);
		this.setGcd(gcd);
	}
	
}
