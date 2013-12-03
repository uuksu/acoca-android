package fi.uuksu.acoca;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AcocaDatabase extends SQLiteOpenHelper {

	public AcocaDatabase(Context context) {
		super(context, "acoca.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String query = "CREATE TABLE Drink (id INTEGER PRIMARY KEY, name TEXT, value REAL, alcoholLevel REAL, amount REAL)";
		db.execSQL(query);
		
		query = "CREATE TABLE DrinkSession (id INTEGER PRIMARY KEY, startTime INTEGER, endTime INTEGER)";
		db.execSQL(query);
		
		query = "CREATE TABLE ConsumedDrink (id INTEGER PRIMARY KEY, addTime INTEGER, drinkId INTEGER, drinkSessionId INTEGER, " +
				"FOREIGN KEY(drinkId) REFERENCES Drink(id), " +
				"FOREIGN KEY(drinkSessionId) REFERENCES DrinkSession(id)";
		db.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String query = "DROP TABLE IF EXISTS Drink";
		db.execSQL(query);
		
		query = "DROP TABLE IF EXISTS DrinkSession";
		db.execSQL(query);
		
		query = "DROP TABLE IF EXISTS ConsumedDrink";
		db.execSQL(query);
		
		this.onCreate(db);
	}
	
	public void addNewDrink(String name, double value, double alcoholLevel, double amount) {
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put("name", name);
		values.put("value", value);
		values.put("alcoholLevel", alcoholLevel);
		values.put("amount", amount);
		
		database.insert("Drink", null, values);
		
		database.close();
	}
	
	public void addNewSession(long startTimeUnixTimestamp, long endTimeUnixTimestamp) {
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put("startTime", startTimeUnixTimestamp);
		values.put("endTime", endTimeUnixTimestamp);
		
		database.insert("DrinkSession", null, values);
		
		database.close();
	}
	
	public void addNewConsumedDrink(long addTimeUnixTimestamp, int drinkId, int drinkSessionId) {
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put("startTime", addTimeUnixTimestamp);
		values.put("drinkId", drinkId);
		values.put("drinkSessionId", drinkSessionId);
		
		database.insert("ConsumedDrink", null, values);
		
		database.close();
	}
	
	public ArrayList<Drink> getDrinks() {
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		ArrayList<Drink> drinks = new ArrayList<Drink>();
		
		String query = "SELECT * FROM Drink ORDER BY name";
		
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor.moveToFirst()) {
			do {
				
				Drink drink = new Drink(
						cursor.getInt(0),
						cursor.getString(1),
						cursor.getDouble(2),
						cursor.getDouble(3),
						cursor.getDouble(4)
						);
				
				drinks.add(drink);
				
			} while(cursor.moveToNext());
		}
		
		return drinks;
	}
	
	public ArrayList<ConsumedDrink> getSessionDrinks(int sessionDrinkId) {
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		ArrayList<Drink> drinks = new ArrayList<Drink>();
		
		String query = "SELECT Drink.id, Drink.name, Drink.value, Drink.alcoholLevel, Drink.amount FROM ConsumedDrink " +
					   "LEFT JOIN Drink ON ConsumedDrink.drinkId = Drink.id " +
					   "WHERE ConsumedDrink.drinkSessionId = " + Integer.toString(sessionDrinkId) + "' " +
					   "ORDER BY name";
		
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor.moveToFirst()) {
			do {
				
				Drink drink = new Drink(
						cursor.getInt(0),
						cursor.getString(1),
						cursor.getDouble(2),
						cursor.getDouble(3),
						cursor.getDouble(4)
						);
				
				drinks.add(drink);
				
			} while(cursor.moveToNext());
		}
		return null;
	}

}
