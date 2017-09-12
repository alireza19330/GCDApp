package com.developairs.rest.dto;

import java.util.List;

public class ListDTO {
	private List<Integer> numbers;
	
	public ListDTO(List<Integer> numbers) {
		this.numbers = numbers;
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
