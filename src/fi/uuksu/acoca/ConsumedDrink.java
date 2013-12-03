package fi.uuksu.acoca;

import java.util.Date;

public class ConsumedDrink {
	private int id;
	private Date AddTime;
	private int drinkId;
	private int DrinkSessionId;
	
	public ConsumedDrink(Date addTime, int drinkId, int drinkSessionId) {
		this.setAddTime(addTime);
		this.setDrinkId(drinkId);
		this.setDrinkSessionId(drinkSessionId);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getAddTime() {
		return AddTime;
	}
	public void setAddTime(Date addTime) {
		AddTime = addTime;
	}
	public int getDrinkId() {
		return drinkId;
	}
	public void setDrinkId(int drinkId) {
		this.drinkId = drinkId;
	}
	public int getDrinkSessionId() {
		return DrinkSessionId;
	}
	public void setDrinkSessionId(int drinkSessionId) {
		DrinkSessionId = drinkSessionId;
	}
}
