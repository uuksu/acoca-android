package fi.uuksu.acoca.database;

import java.util.Date;

import android.content.Context;

public class DrinkSession {
	private int id;
	private Date startTime;
	private Date endTime;
	private String name;
	
	public DrinkSession(int id, Date startTime, Date endTime, String name) {
		this.setId(id);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setName(name);
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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void startSession(Context context)
	{
		AcocaDatabase db = new AcocaDatabase(context);
		db.addNewSession(getStartTime().getTime() / 1000, -1);
	}
	
	public void endSession(Context context) {
		AcocaDatabase db = new AcocaDatabase(context);
		db.updateSession(String.valueOf(getId()), this.getEndTime().getTime() / 1000, this.getName());
	}
	
	public double getTotalDrinkingTime() {
		Date now = new Date();
		long diff = now.getTime() - getStartTime().getTime();
		
		return diff / (60 * 60 * 1000);
	}
	
	public static DrinkSession GetCurrentDrinkSession(Context context) {
		AcocaDatabase db = new AcocaDatabase(context);
		return db.getActiveDrinkSession();
	}


	
}
