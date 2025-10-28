package edu.inbugwethrust.premier.suite.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ExampleEntity {
	
	@Column
	int number;
	
	@Column
	int anotherNumber;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	
	public int getNumber() {
		return number;
	}
	public int getAnotherNumber() {
		return anotherNumber;
	}
	public ExampleEntity(int number, int anotherNumber){
		this.number = number;
		this.anotherNumber = anotherNumber;
	}
	public ExampleEntity() {
		
	}
	public int getId() {
		return this.id;
	}
}
