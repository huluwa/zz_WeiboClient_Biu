package com.dudutech.weibo.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.dudutech.weibo.R;

public class NavigationDrawerFragment extends Fragment {

	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	private NavigationDrawerCallbacks mCallbacks;

	private ListView mDrawerListView;

	private int mCurrentSelectedPosition = 0;

	public NavigationDrawerFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			mCurrentSelectedPosition = savedInstanceState
					.getInt(STATE_SELECTED_POSITION);
		}

		selectItem(mCurrentSelectedPosition);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_drawer,container, false);
		mDrawerListView = (ListView) v.findViewById(R.id.drawer_list);
		mDrawerListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						selectItem(position);
					}
				});
		mDrawerListView.setAdapter(new ArrayAdapter<String>(getActionBar()
				.getThemedContext(),
				android.R.layout.simple_list_item_activated_1,
				android.R.id.text1, new String[] {
						getString(R.string.title_section1),
						getString(R.string.title_section2),
						getString(R.string.title_section3), }));
		mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);
		return v;
	}

	private void selectItem(int position) {
		mCurrentSelectedPosition = position;
		if (mDrawerListView != null) {
			mDrawerListView.setItemChecked(position, true);
		}
		if (mCallbacks != null) {
			mCallbacks.onNavigationDrawerItemSelected(position);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
	}

	private ActionBar getActionBar() {
		return ((AppCompatActivity) getActivity()).getSupportActionBar();
	}

	public static interface NavigationDrawerCallbacks {
		void onNavigationDrawerItemSelected(int position);
	}

}
