package fi.uuksu.acoca.fragments;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fi.uuksu.acoca.AlcoholTools;
import fi.uuksu.acoca.BACWarningMessage;
import fi.uuksu.acoca.R;
import fi.uuksu.acoca.Sex;
import fi.uuksu.acoca.R.id;
import fi.uuksu.acoca.R.layout;
import fi.uuksu.acoca.R.string;
import fi.uuksu.acoca.database.AcocaDatabase;
import fi.uuksu.acoca.database.Drink;
import fi.uuksu.acoca.database.DrinkSession;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
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
		
		AcocaDatabase db = new AcocaDatabase(getActivity());
		
		// Checking if needed information if given
		if (db.getSetting("weight") == null || db.getSetting("sex") == null) {
			drinkModeButton.setEnabled(false);
		}
		
		TextView currentAlcoholLevelTextView = (TextView) v.findViewById(R.id.alcoholLevelTextView);
		currentAlcoholLevelTextView.setOnClickListener(this);
		
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
		
		loadMainboard();
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
		
		loadMainboard();
	}
	
	private double getBAC(DrinkSession session, AcocaDatabase db) {
		
		// Calculating blood alcohol content
		double weight = Double.parseDouble(db.getSetting("weight"));
		Sex sex;
		
		if (db.getSetting("sex") == "0") {
			sex = Sex.Male;
		} else {
			sex = Sex.Female;
		}
		
		ArrayList<Drink> consumedDrinks = db.getSessionDrinks(session.getId());
		
		double totalAlcoholVolumeAsGrams = AlcoholTools.CalculateTotalGramsOfAlcohol(consumedDrinks);
		
		double hours = session.getTotalDrinkingTime();
		
		double BAC = AlcoholTools.CalculateBAC(weight, totalAlcoholVolumeAsGrams, sex, hours);
		
		if (BAC < 0) {
			BAC = 0;
		}
		
		return BAC;
	}
	
	public void loadMainboard() {
		
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
			
			// Calculating total costs
			double totalCosts = AlcoholTools.CalculateTotalCosts(consumedDrinks);
			double BAC = getBAC(session, db);
			
			TextView currentAlcoholLevelTextView = (TextView) getView().findViewById(R.id.alcoholLevelTextView);
			TextView totalCostsTextView = (TextView) getView().findViewById(R.id.totalCostsTextView);
			
			DecimalFormat f = new DecimalFormat("##.##");
			
			currentAlcoholLevelTextView.setText(f.format(BAC));
			totalCostsTextView.setText(f.format(totalCosts));
		}
	}
	
	@Override
	public void onClick(View view) {
		
		final Activity activity = getActivity();
		
		switch(view.getId()) 
		{
		case R.id.drinkModeButton:
			boolean on = ((ToggleButton) view).isChecked();
			
			if (on) {
				DrinkSession session = new DrinkSession(-1, new Date(), null, null);
				session.startSession(activity);
				//Toast.makeText(getActivity(), "Drink mode on", Toast.LENGTH_SHORT).show();
			} else {
				
				final EditText input = new EditText(activity);
				
				new AlertDialog.Builder(activity)
				.setView(input)
				.setMessage(R.string.enter_name)
				.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						DrinkSession session = DrinkSession.GetCurrentDrinkSession(activity);
						
						String value = input.getText().toString();
						
						if (value.length() == 0) {
							DateFormat df = DateFormat.getDateInstance();//new SimpleDateFormat("dd/MM/yyyy");
							final String defaultName = df.format(session.getStartTime());
							value = defaultName;
						}
						
						session.setName(value);
						session.setEndTime(new Date());
						session.endSession(activity);
					}
				})
				.setCancelable(false)
				.show();
				break;
				//Toast.makeText(getActivity(), "Drink mode off", Toast.LENGTH_SHORT).show();
			}
		case R.id.alcoholLevelTextView:
			
			DrinkSession session = DrinkSession.GetCurrentDrinkSession(getActivity());
			AcocaDatabase db = new AcocaDatabase(activity);
			
			if (session != null) {
				double BAC = getBAC(session, db);
				int messageId = BACWarningMessage.GetWarningMessage(BAC);
				
				// If there is message
				if (messageId != -1) {
					new AlertDialog.Builder(getActivity())
					.setMessage(messageId)
					.show();
				}

				
			}

			break;
		}
	}
}
