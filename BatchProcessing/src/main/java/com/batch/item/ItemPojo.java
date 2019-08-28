package com.batch.item;

import java.io.Serializable;

public class ItemPojo implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String firstName,lastName;

	public ItemPojo() {	}
	public ItemPojo(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "ItemPojo [firstName=" + firstName + ", lastName=" + lastName + "]";
	}
	
	
}
