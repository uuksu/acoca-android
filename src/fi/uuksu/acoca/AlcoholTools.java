package fi.uuksu.acoca;

import java.util.ArrayList;

public class AlcoholTools {
	
	public static double CalculateBAC(double bodyWeight, double alcoholVolumeAsGrams, Sex sex, double hours) {
		
		double alcoholAfterBurning = alcoholVolumeAsGrams - (hours * (0.1 * bodyWeight));
		
		double sexMultiplier = 0;
		
		if (sex == Sex.Male) {
			sexMultiplier = 0.75;
		} else {
			sexMultiplier = 0.66;
		}
		
		double BAC = alcoholAfterBurning / (sexMultiplier * bodyWeight);
		
		return BAC;
	}
	
	public static double CalculateGramsOfAlcoholInDrink(double alcoholVolumeAsLiters, double ABVPercent) {
		return (alcoholVolumeAsLiters * ABVPercent) * 0.798;
	}
	
	public static double CalculateTotalGramsOfAlcohol(ArrayList<Drink> drinks) {
		
		double total = 0;
		
		for (int i = 0; i < drinks.size(); i++) {
			
			Drink drink = drinks.get(i);
			
			total += CalculateGramsOfAlcoholInDrink(drink.getAmount(), drink.getAlcoholLevel());
		}
		
		return total * 10;
	}
	
	public static double CalculateTotalCosts(ArrayList<Drink> drinks) {
		
		double total = 0;
		
		for (int i = 0; i < drinks.size(); i++) {
			
			Drink drink = drinks.get(i);
			
			total += drink.getValue();
		}
		
		return total;
	}
	
	
}
