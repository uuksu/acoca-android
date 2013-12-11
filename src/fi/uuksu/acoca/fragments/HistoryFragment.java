package fi.uuksu.acoca.fragments;

import java.text.DecimalFormat;
import java.util.ArrayList;

import fi.uuksu.acoca.AlcoholTools;
import fi.uuksu.acoca.R;
import fi.uuksu.acoca.database.AcocaDatabase;
import fi.uuksu.acoca.database.Drink;
import fi.uuksu.acoca.database.DrinkSession;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class HistoryFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View v = inflater.inflate(R.layout.history_fragment, container, false);
		
		AcocaDatabase db = new AcocaDatabase(getActivity());
		Spinner sessionNameSpinner = (Spinner) v.findViewById(R.id.sessionNameSpinner);
		ArrayList<DrinkSession> drinkSessions = db.getAllDrinkSessions();
		sessionNameSpinner.setAdapter(new DrinkSessionAdapter(getActivity(), drinkSessions.toArray(new DrinkSession[drinkSessions.size()])));
		sessionNameSpinner.setOnItemSelectedListener(new SessionNameSpinnerOnItemSelectedHandler());
		
		return v;
	}
	
	private class DrinkSessionAdapter extends ArrayAdapter<DrinkSession> {

		Context context;
		DrinkSession[] objects;
		
		public DrinkSessionAdapter(Context context, DrinkSession[] objects) {
			super(context, R.layout.drink_session_spinner_row, objects);
			this.context = context;
			this.objects = objects;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.objects.length;
		}
		
		@Override
		public DrinkSession getItem(int position) {
			// TODO Auto-generated method stub
			return this.objects[position];
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return this.objects[position].getId();
		}
		
		@Override
		public View getDropDownView(int position, View view, ViewGroup parent) {
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			// Finding row layout
			View rowView = inflater.inflate(R.layout.drink_session_spinner_row, parent, false);
			
			TextView drinkSessionNameTextView = (TextView) rowView.findViewById(R.id.drinkSessionNameTextView);
			drinkSessionNameTextView.setText(this.objects[position].getName());
			
			return rowView;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			// Finding row layout
			View rowView = inflater.inflate(R.layout.drink_session_spinner_row, parent, false);
			
			// Set drink session name of spinner text view
			TextView drinkSessionNameTextView = (TextView) rowView.findViewById(R.id.drinkSessionNameTextView);
			drinkSessionNameTextView.setText(this.objects[position].getName());
			
			return rowView;
		}
		
	}
	
	private class SessionNameSpinnerOnItemSelectedHandler implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
			AcocaDatabase db = new AcocaDatabase(getActivity());
			
			ArrayList<Drink> drinks = db.getSessionDrinks((int) id);
			Drink[] drinksArray = drinks.toArray(new Drink[drinks.size()]);
			
			// Set selected drinks to history list view
			ListView drinkListView = (ListView) getView().findViewById(R.id.historyListView);
			drinkListView.setAdapter(new DrinkListViewAdapter(getActivity(), drinksArray));
			
			TextView totalCostsTextView = (TextView) getView().findViewById(R.id.historyTotalCostsTextView);
			double totalCosts = AlcoholTools.CalculateTotalCosts(drinks);
			DecimalFormat f = new DecimalFormat("##.##");
			totalCostsTextView.setText(f.format(totalCosts));
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class DrinkListViewAdapter extends ArrayAdapter<Drink> {
		
		private final Context context;
		private final Drink[] objects;

		public DrinkListViewAdapter(Context context, Drink[] objects) {
			super(context, R.layout.drink_list_view, objects);
			this.context = context;
			this.objects = objects;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			// Finding row layout
			View rowView = inflater.inflate(R.layout.drink_list_view, parent, false);
			
			// Getting text view elements from layout
			TextView nameTextView = (TextView) rowView.findViewById(R.id.nameTextView);
			TextView priceTextView = (TextView) rowView.findViewById(R.id.priceTextView);
			TextView alcoholLevelTextView = (TextView) rowView.findViewById(R.id.historyAlcoholLevelTextView);
			TextView amountTextView = (TextView) rowView.findViewById(R.id.amountTextView);
			
			Drink drink = objects[position];
			
			// Settings parameters for layout views
			nameTextView.setText(drink.getName());
			priceTextView.setText(String.valueOf(drink.getValue()));
			alcoholLevelTextView.setText(String.valueOf(drink.getAlcoholLevel()));
			amountTextView.setText(String.valueOf(drink.getAmount()));
			
			return rowView;
		}
		
		
	}
}
