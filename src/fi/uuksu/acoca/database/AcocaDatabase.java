package fi.uuksu.acoca.database;

import java.util.ArrayList;
import java.util.Date;

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
		
		query = "CREATE TABLE DrinkSession (id INTEGER PRIMARY KEY, startTime INTEGER, endTime INTEGER, name TEXT)";
		db.execSQL(query);
		
		query = "CREATE TABLE ConsumedDrink (id INTEGER PRIMARY KEY, addTime INTEGER, drinkId INTEGER, drinkSessionId INTEGER)";
		db.execSQL(query);
		
		query = "CREATE TABLE Setting (key TEXT PRIMARY KEY, value TEXT)";
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
		
		query = "DROP TABLE IF EXISTS Setting";
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
	}
	
	public void addNewSession(long startTimeUnixTimestamp, long endTimeUnixTimestamp) {
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put("startTime", startTimeUnixTimestamp);
		values.put("endTime", endTimeUnixTimestamp);
		
		database.insert("DrinkSession", null, values);
	}
	
	public void addNewConsumedDrink(long addTimeUnixTimestamp, int drinkId, int drinkSessionId) {
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		
		values.put("addTime", addTimeUnixTimestamp);
		values.put("drinkId", drinkId);
		values.put("drinkSessionId", drinkSessionId);
		
		database.insert("ConsumedDrink", null, values);
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
	
	public ArrayList<Drink> getSessionDrinks(int sessionDrinkId) {
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		ArrayList<Drink> drinks = new ArrayList<Drink>();
		
		String query = "SELECT Drink.id, Drink.name, Drink.value, Drink.alcoholLevel, Drink.amount FROM ConsumedDrink " +
					   "JOIN Drink ON ConsumedDrink.drinkId = Drink.id " +
					   "WHERE ConsumedDrink.drinkSessionId = '" + Integer.toString(sessionDrinkId) + "' " +
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
		
		return drinks;
	}
	
	public DrinkSession getActiveDrinkSession() {
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		String query = "SELECT * FROM DrinkSession WHERE endTime < 0";
		
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor.getCount() > 0)
		{
			cursor.moveToFirst();
			
			DrinkSession session = new DrinkSession(cursor.getInt(0), new Date(cursor.getLong(1) * 1000), new Date(0), null);
			
			return session;
			
		} else {
			
			return null;
		}
	}
	
	public void deleteDrink(int id) {
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		String query = "DELETE FROM Drink WHERE id = " + id;
		
		database.execSQL(query);
	}
	
	public void updateSession(String id, long endTimeUnixTimestamp, String name) {
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("endTime", endTimeUnixTimestamp);
		values.put("name", name);
		
		database.update("DrinkSession", values, "id = ?", new String[] { id });
	}
	
	public void updateSetting(String key, String value) {
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		String query = "SELECT * FROM Setting WHERE key = '" + key + "'";
		
		Cursor cursor = database.rawQuery(query, null);
		
		ContentValues values = new ContentValues();
		values.put("key", key);
		values.put("value", value);
		
		if (cursor.getCount() > 0)
		{
			database.update("Setting", values, "key = '?'", new String[] { key });
		} else {
			database.insert("Setting", null, values);
		}
	}
	
	public String getSetting(String key) {
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		String query = "SELECT * FROM Setting WHERE key = '" + key + "'";
		
		Cursor cursor = database.rawQuery(query, null);
		
		if (cursor.getCount() == 0)
		{
			return null;
		}
		
		cursor.moveToFirst();
		
		return cursor.getString(1);
	}

}
