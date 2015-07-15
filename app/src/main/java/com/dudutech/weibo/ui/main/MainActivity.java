package com.dudutech.weibo.ui.main;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import com.dudutech.weibo.R;
import com.dudutech.weibo.Utils.SystemBarUtils;

import com.dudutech.weibo.dao.login.LoginDao;
import com.dudutech.weibo.dao.user.UserDao;
import com.dudutech.weibo.model.UserModel;
import com.dudutech.weibo.ui.common.BaseActivity;
import com.dudutech.weibo.ui.post.NewPostActivity;
import com.dudutech.weibo.ui.timeline.HomeTimelineFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends BaseActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks ,View.OnClickListener{



	public final static  String TAG = "MainActivity";

	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private NavigationDrawerFragment mNavigationDrawerFragment;

	private HomeTimelineFragment mTimelineFragment;

	private CharSequence mTitle;

	private ActionBarHelper mActionBar;

	private UserDao mUserCache;
	private UserModel mUser;

	private LoginDao mLoginCache;

	@InjectView(R.id.toolbar)
	Toolbar toolbar;
	@InjectView(R.id.fab)
	FloatingActionButton  fab;

	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch (id){
			case R.id.fab:
				if(mTimelineFragment!=null){
					mTimelineFragment.doRefresh();
				}

				break;
		}

	}

	public static interface Refresher {
        void doRefresh();
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);
		mNavigationDrawerFragment = new NavigationDrawerFragment();

		setSupportActionBar(toolbar);
		mActionBar = new ActionBarHelper();
		mActionBar.init();
		initStatusBar();

		getFragmentManager()
				.beginTransaction()
				.replace(R.id.navigation_drawer, mNavigationDrawerFragment)
				.commit();

		setUpDrawer();

		mUserCache = new UserDao(this);
		mLoginCache = new LoginDao(this);


		new InitializerTask().execute();

		fab.setOnClickListener(this);
	}


	private void initStatusBar(){
		if (Build.VERSION.SDK_INT >= 19) {
			ViewGroup drawerRoot = (ViewGroup) findViewById(R.id.fl_drawer_root);
			drawerRoot.setPadding(drawerRoot.getPaddingLeft(),
					SystemBarUtils.getStatusBarHeight(this),
					drawerRoot.getPaddingRight(),
					drawerRoot.getBottom());
		}
		if (Build.VERSION.SDK_INT >= 19) {
			ViewGroup rootMain = (ViewGroup) findViewById(R.id.rl_main_root);
			rootMain.setPadding(rootMain.getPaddingLeft(),
					rootMain.getPaddingTop(),
					rootMain.getPaddingRight(),
					rootMain.getBottom() + SystemBarUtils.getNavigationBarHeight(this));
		}

	}

	private void setUpDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerLayout.setDrawerListener(new MyDrawerListener());
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		mDrawerToggle = new ActionBarDrawerToggle(this,
				mDrawerLayout,
				R.string.navigation_drawer_open,
				R.string.navigation_drawer_close
		) {
			@Override
			public void onDrawerClosed(View drawerView) {
				super.onDrawerClosed(drawerView);
				invalidateOptionsMenu();
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	public boolean isDrawerOpen() {
		return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.LEFT);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {

		if(position==0){
			mTimelineFragment=new HomeTimelineFragment() ;

			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.container, mTimelineFragment)
					.commit();
		}
		else{
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager
					.beginTransaction()
					.replace(R.id.container,
							PlaceholderFragment.newInstance(position + 1)).commit();
		}

		try {
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		} catch (NullPointerException e) {

		}
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		mActionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		if (!isDrawerOpen()) {
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		int id = item.getItemId();
		switch (id ){
			case R.id.action_settings :
				return true;
			case R.id.action_new_post :

				NewPostActivity.start(this,null,null,NewPostActivity.FLAG_POST);

				return true;
		}



		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public static class PlaceholderFragment extends Fragment {

		private static final String ARG_SECTION_NUMBER = "section_number";

		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

	private class MyDrawerListener implements DrawerLayout.DrawerListener {
		@Override
		public void onDrawerOpened(View drawerView) {
			mDrawerToggle.onDrawerOpened(drawerView);
			mActionBar.onDrawerOpened();
		}

		@Override
		public void onDrawerClosed(View drawerView) {
			mDrawerToggle.onDrawerClosed(drawerView);
			mActionBar.onDrawerClosed();
		}

		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
		}

		@Override
		public void onDrawerStateChanged(int newState) {
			mDrawerToggle.onDrawerStateChanged(newState);
		}
	}

	private class ActionBarHelper {
		private final ActionBar mActionBar;
		private CharSequence mDrawerTitle;
		private CharSequence mTitle;

		ActionBarHelper() {
			mActionBar = getSupportActionBar();
		}

		public void init() {
			mActionBar.setDisplayHomeAsUpEnabled(true);
			mActionBar.setDisplayShowHomeEnabled(false);
			mTitle = mDrawerTitle = getTitle();
		}

		public void onDrawerClosed() {
			mActionBar.setTitle(mTitle);
		}

		public void onDrawerOpened() {
			mActionBar.setTitle(mDrawerTitle);
		}

		public void setTitle(CharSequence title) {
			mTitle = title;
		}
	}

	private class InitializerTask extends AsyncTask<Void, Object, Void> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected Void doInBackground(Void[] params) {
			// Username first
			mUser = mUserCache.getUser(mLoginCache.getUid());


			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);

			if(mUser!=null){
				Log.i(TAG,mUser.getName());
				initNavigationFragment();
			}
		}
	}

	private void initNavigationFragment(){

		if(mNavigationDrawerFragment==null){
			return;
		}
		mNavigationDrawerFragment.setHeadView(mUser);

	}

	protected void newPost() {
		Intent i = new Intent();
		i.setAction(Intent.ACTION_MAIN);
//		i.setClass(getActivity(), NewPostActivity.class);
		startActivity(i);
	}


}
