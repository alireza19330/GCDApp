package com.developairs.dto;
/**
 * 
 * @author Ali Abazari
 *
 */
public class GCDSumDTO extends BaseDTO{
	
	private Long sum;
	
	public Long getSum() {
		return sum;
	}
	public void setSum(Long sum) {
		this.sum = sum;
	}
	
	public GCDSumDTO(){
	}
	
	public GCDSumDTO(String responseCode, Long sum){
		this.setResponse(responseCode);
		this.setSum(sum);
	}

}
