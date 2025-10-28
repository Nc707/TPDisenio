package edu.inbugwethrust.premier.suite.dto;

public class ExampleDTO {
	public ExampleDTO() {
		super();
	}
	public ExampleDTO(int number, int anotherNumber) {
		super();
		this.number = number;
		this.anotherNumber = anotherNumber;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getAnotherNumber() {
		return anotherNumber;
	}
	public void setAnotherNumber(int anotherNumber) {
		this.anotherNumber = anotherNumber;
	}
	private int number;
	private int anotherNumber;
}
