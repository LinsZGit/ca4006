package com.vogella.jersey.first;

public class Property {
	private int id;   
	
	
	private String district;   
	private String bedroom;   
	private String price;   
 
	public int getId() { 
		return id; 
	}   
	
	public void setId(int id) {
		this.id = id;   
	} 
 
	public String getDistrict() {   
		return district; 
	}   
	
	public void setDistrict(String district) { 
		this.district = district; 
	} 
 
	public String getBedroom() {   
		return bedroom; 
	}   
	
	public void setBedroom(String bedroom) { 
		this.bedroom = bedroom; 
	}
	
	public String getPrice() { 
		return price; 
	}   
	
	public void setPrice(String price) { 
		this.price = price; 
	} 
}
