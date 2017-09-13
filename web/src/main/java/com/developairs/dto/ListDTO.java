package com.developairs.dto;

import java.util.List;

public class ListDTO extends BaseDTO{
	
	private List<Integer> numbers;
	
	public ListDTO(List<Integer> numbers, String responseCode) {
		this.numbers = numbers;
		this.setResponse(responseCode);
	}
	
	public ListDTO() {
	}
	
	public List<Integer> getNumbers() {
		return numbers;
	}
	public void setNumbers(List<Integer> numbers) {
		this.numbers = numbers;
	}
}
