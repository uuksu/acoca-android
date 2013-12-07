package fi.uuksu.acoca;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsFragment extends Fragment implements OnClickListener {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View v = inflater.inflate(R.layout.settings_fragment, container, false);
		
		Button b = (Button) v.findViewById(R.id.saveSettingsButton);
		b.setOnClickListener(this);
		
		return v;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		AcocaDatabase db = new AcocaDatabase(getActivity());
		
		TextView weightTextView = (TextView) getView().findViewById(R.id.bodyWeightTextBox);
		ListView sexListView = (ListView) getView().findViewById(R.id.sexListView);
		
		String weight = db.getSetting("weight");
		String sex = db.getSetting("sex");
		
		if (weight != null) {
			weightTextView.setText(weight);
		}
		
		if (sex != null) {
			sexListView.setSelection(Integer.parseInt(sex));
		}
	}
	
	@Override
	public void onClick(View v) {
		
		Activity activity = getActivity();
		
		switch(v.getId()) 
		{
		case R.id.saveSettingsButton:
			
			TextView weightTextView = (TextView) getView().findViewById(R.id.bodyWeightTextBox);
			ListView sexListView = (ListView) getView().findViewById(R.id.sexListView);
			
			String sex;
			
			if (sexListView.getSelectedItemPosition() == 0) {
				sex = "0";
			} else {
				sex = "1";
			}
			
			AcocaDatabase db = new AcocaDatabase(activity);
			
			db.updateSetting("sex", sex);
			db.updateSetting("weight", weightTextView.getText().toString());
			
			Toast.makeText(activity, "Settings saved!", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
