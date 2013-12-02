package fi.uuksu.acoca;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SettingsFragment extends Fragment implements OnClickListener {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View v = inflater.inflate(R.layout.settings_fragment, container, false);
		
		Button b = (Button) v.findViewById(R.id.button1);
		b.setOnClickListener(this);
		
		return v;
	}
	
	@Override
	public void onClick(View v) {
		
		Activity activity = getActivity();
		
		switch(v.getId()) 
		{
		case R.id.button1:
			Toast.makeText(activity, "Hello World", Toast.LENGTH_SHORT).show();
			break;
		}
	}
}
