package fi.uuksu.acoca;

import java.util.Date;

public class DrinkSession {
	private int id;
	private Date startTime;
	private Date endTime;
	
	public DrinkSession(Date startTime, Date endTime) {
		this.setStartTime(startTime);
		this.setEndTime(endTime);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	
	public Date getEndTime() {
		return endTime;
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}
