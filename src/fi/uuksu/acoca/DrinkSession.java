package fi.uuksu.acoca;

import java.util.Date;

import android.content.Context;

public class DrinkSession {
	private int id;
	private Date startTime;
	private Date endTime;
	
	public DrinkSession(int id, Date startTime, Date endTime) {
		this.setId(id);
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
	
	public void startSession(Context context)
	{
		AcocaDatabase db = new AcocaDatabase(context);
		db.addNewSession(getStartTime().getTime() / 1000, -1);
	}
	
	public void endSession(Context context) {
		AcocaDatabase db = new AcocaDatabase(context);
		db.updateSession(String.valueOf(getId()), getEndTime().getTime() / 1000);
	}
	
	public static DrinkSession GetCurrentDrinkSession(Context context) {
		AcocaDatabase db = new AcocaDatabase(context);
		return db.getActiveDrinkSession();
	}
	
}
