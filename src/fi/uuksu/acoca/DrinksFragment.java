package fi.uuksu.acoca;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DrinksFragment extends Fragment implements OnClickListener {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View v = inflater.inflate(R.layout.drinks_fragment, container, false);
		
		Button b = (Button) v.findViewById(R.id.addNewDrinkButton);
		b.setOnClickListener(this);
		
		return v;
	}
	
	@Override
	public void onClick(View v) {
		
		Activity activity = getActivity();
		
		switch(v.getId()) 
		{
		case R.id.addNewDrinkButton:
			Intent intent = new Intent(activity, AddNewDrinkActivity.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		AcocaDatabase db = new AcocaDatabase(getActivity());
		ArrayList<Drink> drinks = db.getDrinks();
		Drink[] drinksArray = drinks.toArray(new Drink[drinks.size()]);
		
		ListView drinkListView = (ListView) getView().findViewById(R.id.drinkHistoryListView);
		drinkListView.setAdapter(new DrinkListViewAdapter(getActivity(), drinksArray));
		drinkListView.setOnItemClickListener(new ListClickHandler());
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
			TextView alcoholLevelTextView = (TextView) rowView.findViewById(R.id.alcoholLevelTextView);
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
	
	private class ListClickHandler implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, final int position, long arg3) {
			
			final DrinkSession session = DrinkSession.GetCurrentDrinkSession(getActivity());
			
			if (session == null) {
				Toast.makeText(getActivity(), R.string.drink_mode_notice, Toast.LENGTH_LONG).show();
			} else {
				new AlertDialog.Builder(getActivity())
				.setMessage(R.string.drink_confirmation)
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						AcocaDatabase db = new AcocaDatabase(getActivity());
						
						// Get current drink
						Drink drink = db.getDrinks().get(position);
						
						// Create new consumed drink
						ConsumedDrink consumedDrink = new ConsumedDrink(new Date(), drink.getId(), session.getId());
						consumedDrink.saveToDatabase(getActivity());
					}
				})
				.setNegativeButton(R.string.no, null)
				.show();
			}
			

		}
		
	}
	
	

}
