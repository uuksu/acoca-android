package fi.uuksu.acoca;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainboardFragment extends Fragment implements OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.mainboard_fragment, container, false);
		
		ToggleButton drinkModeButton = (ToggleButton) v.findViewById(R.id.drinkModeButton);
		drinkModeButton.setOnClickListener(this);
		
		return v;
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		DrinkSession session = DrinkSession.GetCurrentDrinkSession(getActivity());
		
		if (session != null) {
			ToggleButton drinkModeButton = (ToggleButton) getView().findViewById(R.id.drinkModeButton);
			drinkModeButton.setChecked(true);
		}
		
		loadMainBoard();
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		loadMainBoard();
	}
	
	public void loadMainBoard() {
		
		DrinkSession session = DrinkSession.GetCurrentDrinkSession(getActivity());
		
		if (session != null) {
			
			// Loading consumed drink history
			ListView historyListView = (ListView) getView().findViewById(R.id.drinkHistoryListView);
			AcocaDatabase db = new AcocaDatabase(getActivity());
			ArrayList<Drink> consumedDrinks = db.getSessionDrinks(session.getId());
			ArrayList<String> drinkStrings = new ArrayList<String>();
			
			for (int i = 0; i < consumedDrinks.size(); i++) {
				Drink drink = consumedDrinks.get(i);
				String row = drink.getName() + ", " + drink.getValue() + " €";
				drinkStrings.add(row);
			}
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.drink_history_list_view, R.id.drinkName, drinkStrings.toArray(new String[drinkStrings.size()]));
			historyListView.setAdapter(adapter);
			
			// Calculating blood alcohol content
			double weight = Double.parseDouble(db.getSetting("weight"));
			Sex sex;
			
			if (db.getSetting("sex") == "0") {
				sex = Sex.Male;
			} else {
				sex = Sex.Female;
			}
			
			double totalAlcoholVolumeAsGrams = AlcoholTools.CalculateTotalGramsOfAlcohol(consumedDrinks);
			
			double hours = session.getTotalDrinkingTime();
			
			double BAC = AlcoholTools.CalculateBAC(weight, totalAlcoholVolumeAsGrams, sex, hours);
			
			// Calculating total costs
			double totalCosts = AlcoholTools.CalculateTotalCosts(consumedDrinks);
			
			TextView currentAlcoholLevelTextView = (TextView) getView().findViewById(R.id.alcoholLevelTextView);
			TextView totalCostsTextView = (TextView) getView().findViewById(R.id.totalCostsTextView);
			
			DecimalFormat f = new DecimalFormat("##.##");
			
			currentAlcoholLevelTextView.setText(f.format(BAC));
			totalCostsTextView.setText(f.format(totalCosts));
		}
	}
	
	@Override
	public void onClick(View view) {
		
		Activity activity = getActivity();
		
		switch(view.getId()) 
		{
		case R.id.drinkModeButton:
			boolean on = ((ToggleButton) view).isChecked();
			
			if (on) {
				DrinkSession session = new DrinkSession(-1, new Date(), null);
				session.startSession(activity);
				//Toast.makeText(getActivity(), "Drink mode on", Toast.LENGTH_SHORT).show();
			} else {
				DrinkSession session = DrinkSession.GetCurrentDrinkSession(activity);
				session.setEndTime(new Date());
				session.endSession(activity);
				//Toast.makeText(getActivity(), "Drink mode off", Toast.LENGTH_SHORT).show();
			}
			
			break;
		}
	}
}
