package fi.uuksu.acoca;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
	public void onClick(View view) {
		
		Activity activity = getActivity();
		
		switch(view.getId()) 
		{
		case R.id.drinkModeButton:
			boolean on = ((ToggleButton) view).isChecked();
			
			if (on) {
				Toast.makeText(getActivity(), "Drink mode on", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getActivity(), "Drink mode off", Toast.LENGTH_SHORT).show();
			}
			
			break;
		}
	}
}
