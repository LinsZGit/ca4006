package com.vogella.jersey.first;

public class Property {
	private int id;   
	
	private String type;
	private String district;   
	private String bedroom;   
	private String price;
	private String startTime;
	private String endTime;
 
	public int getId() { 
		return id; 
	}   
	
	public void setId(int id) {
		this.id = id;   
	} 
 
	public String getStart() {   
		return startTime; 
	}   
	
	public void setStart(String startTime) { 
		this.startTime = startTime; 
	} 
	public String getEnd() {   
		return endTime; 
	}   
	
	public void setEnd(String endTime) { 
		this.endTime = endTime; 
	} 
	public String getType() {   
		return type; 
	}   
	
	public void setType(String type) { 
		this.type = type; 
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
