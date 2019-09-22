package com.batch.item;

import java.io.Serializable;

public class ItemPojo implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String firstName,lastName,friend1,friend2,friend3;
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
	public String getFriend1() {
		return friend1;
	}
	public void setFriend1(String friend1) {
		this.friend1 = friend1;
	}
	public String getFriend2() {
		return friend2;
	}
	public void setFriend2(String friend2) {
		this.friend2 = friend2;
	}
	public String getFriend3() {
		return friend3;
	}
	public void setFriend3(String friend3) {
		this.friend3 = friend3;
	}
	
	public ItemPojo() {
		
	}
	public ItemPojo(String firstName, String lastName, String friend1, String friend2, String friend3) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.friend1 = friend1;
		this.friend2 = friend2;
		this.friend3 = friend3;
	}
	@Override
	public String toString() {
		return "ItemPojo [firstName=" + firstName + ", lastName=" + lastName + ", friend1=" + friend1 + ", friend2="
				+ friend2 + ", friend3=" + friend3 + "]";
	}

	
	
	
}
