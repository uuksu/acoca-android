package fi.uuksu.acoca;

import android.content.Context;

public class Drink {
	private int id;
	private String name;
	private double value;
	private double alcoholLevel;
	private double amount;

	public Drink(int id, String name, double value, double alcoholLevel, double amount) {
		this.setId(id);
		this.setName(name);
		this.setValue(value);
		this.setAlcoholLevel(alcoholLevel);
		this.setAmount(amount);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public double getAlcoholLevel() {
		return alcoholLevel;
	}

	public void setAlcoholLevel(double alcoholLevel) {
		this.alcoholLevel = alcoholLevel;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void saveToDatabase(Context context) {
		AcocaDatabase db = new AcocaDatabase(context);
		
		db.addNewDrink(getName(), getValue(), getAlcoholLevel(), getAmount());
		
	}
	
	public void deleteFromDatabase(Context context) {
		AcocaDatabase db = new AcocaDatabase(context);
		db.deleteDrink(getId());
	}
}
