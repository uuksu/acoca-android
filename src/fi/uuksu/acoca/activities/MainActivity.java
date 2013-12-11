package fi.uuksu.acoca.activities;

import java.util.ArrayList;
import java.util.Locale;

import fi.uuksu.acoca.R;
import fi.uuksu.acoca.R.id;
import fi.uuksu.acoca.R.layout;
import fi.uuksu.acoca.R.menu;
import fi.uuksu.acoca.R.string;
import fi.uuksu.acoca.fragments.DrinksFragment;
import fi.uuksu.acoca.fragments.HistoryFragment;
import fi.uuksu.acoca.fragments.MainboardFragment;
import fi.uuksu.acoca.fragments.SettingsFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements DrinksFragment.OnDrinkConsumedListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}
	
	@Override
	protected void onStart() {
		
		super.onStart();

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		/*
		AcocaDatabase db = new AcocaDatabase(this);
		ArrayList<Drink> drinks = db.getDrinks();
		Drink[] drinksArray = drinks.toArray(new Drink[drinks.size()]);
		
		ListView drinkListView = (ListView) findViewById(R.id.drinkListView);
		drinkListView.setAdapter(new DrinkListViewAdapter(this, drinksArray));
		*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			
			Fragment fragment = null;
			
			// Selecting right fragment based on tab position
			switch(position) {
			case 0:
				fragment = new MainboardFragment();
				break;
			case 1:
				fragment = new DrinksFragment();
				break;
			case 2:
				fragment = new HistoryFragment();
				break;
			case 3:
				fragment = new SettingsFragment();
				break;
			}
			
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.mainboard_title).toUpperCase(l);
			case 1:
				return getString(R.string.drinks_title).toUpperCase(l);
			case 2:
				return getString(R.string.history_title).toUpperCase(l);
			case 3:
				return getString(R.string.settings_title).toUpperCase(l);
				
			}
			return null;
		}
	}
	
	private static String makeFragmentName(int viewId, int index) {
	     return "android:switcher:" + viewId + ":" + index;
	}

	@Override
	public void onDrinkConsumed() {
		String tag = makeFragmentName(R.id.pager, 0);
		
		MainboardFragment mainboardFragment = (MainboardFragment) getSupportFragmentManager().findFragmentByTag(tag);
		
		if (mainboardFragment != null) {
			mainboardFragment.loadMainboard();
		}
		
	}

}
