package fi.uuksu.acoca;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AddNewDrinkActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_drink_page);
	}
	
	public void addDrinkButtonOnClick(View view) {
		TextView nameBox = (TextView) findViewById(R.id.nameTextBox);
		TextView alcoholLevelBox = (TextView) findViewById(R.id.alcoholLevelTextBox);
		TextView amountBox = (TextView) findViewById(R.id.amountTextBox);
		TextView priceBox = (TextView) findViewById(R.id.priceTextBox);
		
		double price = Double.parseDouble(priceBox.getText().toString());
		double alcoholLevel = Double.parseDouble(alcoholLevelBox.getText().toString());
		double amount = Double.parseDouble((String) amountBox.getText().toString());
		
		
		Drink drink = new Drink(-1, nameBox.getText().toString(), price, alcoholLevel, amount);
		drink.saveToDatabase(getApplicationContext());
		
		// Return back from activity
		finish();
	}
}
