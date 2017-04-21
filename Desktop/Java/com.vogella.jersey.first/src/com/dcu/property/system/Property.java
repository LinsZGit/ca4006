package com.dcu.property.system;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Property {
	private int id;

	private String type;
	private String district;
	private String bedroom;
	private String price;
	private String startTime;
	private String endTime;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private String highestBidder = "NULL";
	private int highestBid = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getStart() {

		LocalDate start = LocalDate.parse(startTime,formatter);
		return start;
	}

	public void setStart(String startTime) {
		this.startTime = startTime;
	}
	public LocalDate getEnd() {
		LocalDate end = LocalDate.parse(endTime,formatter);
		return end;
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
		if(highestBid < Integer.parseInt(price)){
			this.highestBid = Integer.parseInt(price);
		}
	}

	public boolean makeBid(int value)
	{
		if(value < highestBid)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public void setBid(int bid)
	{
		highestBid = bid;
	}
	
	public int getBid()
	{
		return highestBid;
	}
	
	public void setBidder(String bidder)
	{
		highestBidder = bidder;
	}
	
	public String getBidder()
	{
		return highestBidder;
	}
}
