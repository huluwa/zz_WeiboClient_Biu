/* 
 * Copyright (C) 2014 Peter Cai
 *
 * This file is part of BlackLight
 *
 * BlackLight is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BlackLight is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BlackLight.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dudutech.weibo.ui.timeline;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dudutech.weibo.R;
import com.dudutech.weibo.Utils.Settings;
import com.dudutech.weibo.adapter.TimelineAdapter;
import com.dudutech.weibo.cache.HomeTimeLineApiCache;
import com.dudutech.weibo.model.MessageModel;
import com.dudutech.weibo.ui.MainActivity;
import com.dudutech.weibo.ui.ToolbarActivity;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.dudutech.weibo.ui.MainActivity.Refresher;


public  class TimeLineFragment extends Fragment implements
		SwipeRefreshLayout.OnRefreshListener, Refresher {
	
	private static final String TAG = TimeLineFragment.class.getSimpleName();

    @InjectView(R.id.home_timeline)
	protected RecyclerView mList;
//	protected View mShadow;
	private TimelineAdapter mAdapter;
	private LinearLayoutManager mManager;
	protected HomeTimeLineApiCache mCache;

	private Settings mSettings;
	
//	protected ActionBar mActionBar = null;
//	protected Toolbar mToolbar = null;
	private int mActionBarHeight = 0;
	private int mTranslationY = 0;
	private int mLastY = 0;

	// Pull To Refresh
	private SwipeRefreshLayout mSwipeRefresh;

	private boolean mRefreshing = false;

	protected boolean mBindOrig = true;
	protected boolean mShowCommentStatus = true;
	protected boolean mAllowHidingActionBar = true;
	private boolean mFABShowing = true;

	private int mLastCount = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

//		mActionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
//		mToolbar = ((ActionBarActivity) getActivity()).getToolbar();
		initTitle();


		mSettings = Settings.getInstance(getActivity().getApplicationContext());

		final View v = inflater.inflate(R.layout.fragment_timeline, null);
        ButterKnife.inject(this,v);
		// Initialize views


		mCache = bindApiCache();
		mCache.loadFromCache();

		mList.setDrawingCacheEnabled(true);
		mList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
		mList.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE
				| ViewGroup.PERSISTENT_SCROLLING_CACHE);
		mManager = new LinearLayoutManager(getActivity());
		mList.setLayoutManager(mManager);

		// Swipe To Refresh
		bindSwipeToRefresh((ViewGroup) v);

		if (mCache.mMessages.getSize() == 0) {
			new Refresher().execute(true);
		}
		
		// Adapter
		mAdapter = new TimelineAdapter(getActivity(),(List<MessageModel>) mCache.mMessages.getList()
				  );

		mAdapter.setBottomCount(1);
		// Content Margin
//		if (getActivity() instanceof MainActivity && mAllowHidingActionBar) {
//			View header = new View(getActivity());
////			RecyclerView.LayoutParams p = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
////					Utility.getDecorPaddingTop(getActivity()));
////			header.setLayoutParams(p);
////			mAdapter.setHeaderView(header);
////			mSwipeRefresh.setProgressViewOffset(false, 0, (int) ((p.height + Utility.dp2px(getActivity(), 20)) * 1.2));
//		}
		mList.setAdapter(mAdapter);



		mList.addOnScrollListener(new RecyclerView.OnScrollListener() {


			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);

				if (newState == RecyclerView.SCROLL_STATE_IDLE) {
					//获取最后一个完全显示的ItemPosition
					int lastVisibleItem = mManager.findLastCompletelyVisibleItemPosition();
					int totalItemCount = mManager.getItemCount();

					// 判断是否滚动到底部，并且是向右滚动
					if (lastVisibleItem == (totalItemCount -1)) {
						//加载更多功能的代码
						new TimeLineFragment.Refresher().execute(false);

					}
				}

			}
		});


		// Listener
//		if (getActivity() instanceof MainActivity) {
//			mAdapter.addOnScrollListener(new RecyclerView.OnScrollListener() {
//				@Override
//				public void onScrolled(RecyclerView view, int dx, int dy) {
//					int deltaY = -dy;
//					boolean shouldShow = deltaY > 0;
//					if (shouldShow != mFABShowing) {
//						if (shouldShow) {
//							showFAB();
//						} else {
//							hideFAB();
//						}
//					}
//
//					if (mAllowHidingActionBar) {
//						if ((mTranslationY > -mActionBarHeight && deltaY < 0)
//							|| (mTranslationY < 0 && deltaY > 0)) {
//
//							mTranslationY += deltaY;
//						}
//
//						if (mTranslationY < -mActionBarHeight) {
//							mTranslationY = -mActionBarHeight;
//						} else if (mTranslationY > 0) {
//							mTranslationY = 0;
//						}
//
//						updateTranslation();
//					}
//
//					if (!mRefreshing && mManager.findLastVisibleItemPosition() >= mAdapter.getItemCount() - 5) {
//						new TimeLineFragment.Refresher().execute(false);
//					}
//
//					mFABShowing = shouldShow;
//					mLastY = dy;
//				}
//			});
//		}

//		mShadow.bringToFront();

//		v.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//			@Override
//			public void onGlobalLayout() {
//				if (getActivity() instanceof MainActivity && mAllowHidingActionBar) {
//					mActionBarHeight = mToolbar.getHeight();
//					mShadow.setTranslationY(mActionBarHeight);
//					RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) mAdapter.getHeaderView().getLayoutParams();
//					lp.height = mActionBarHeight;
//					mAdapter.getHeaderView().setLayoutParams(lp);
//					mSwipeRefresh.setProgressViewOffset(false, 0, (int) (mActionBarHeight * 1.2));
//					mSwipeRefresh.invalidate();
//					v.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//				}
//			}
//		});




		return v;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		if (!hidden) {
			initTitle();
//			showFAB();
//			if (this instanceof HomeTimeLineFragment) {
//				((MainActivity) getActivity()).setShowSpinner(true);
//			} else {
//				((MainActivity) getActivity()).setShowSpinner(false);
//			}
//			updateTranslation();
		} else {
//			hideFAB();
		}
	}

	@Override
	public void doRefresh() {
		mList.smoothScrollToPosition(0);
		mList.post(new Runnable() {
			@Override
			public void run() {
				onRefresh();
			}
		});
	}

	@Override
	public void onRefresh() {
		if (!mRefreshing) {
			new Refresher().execute(true);
		}
	}
	
	protected void updateTranslation() {
//		mToolbar.setTranslationY(mTranslationY);
//		mShadow.setTranslationY(mActionBarHeight + mTranslationY);
		mSwipeRefresh.setProgressViewOffset(false, 0, (int) ((mActionBarHeight + mTranslationY) * 1.2));
		mSwipeRefresh.invalidate();
	}

	protected HomeTimeLineApiCache bindApiCache() {
		return new HomeTimeLineApiCache(getActivity());
	}

	protected void initTitle() {
//		mActionBar.setTitle(R.string.timeline);
	}


	protected void bindSwipeToRefresh(ViewGroup v) {
		mSwipeRefresh = new SwipeRefreshLayout(getActivity());

		// Move child to SwipeRefreshLayout, and add SwipeRefreshLayout to root
		// view
		v.removeViewInLayout(mList);
		v.addView(mSwipeRefresh, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		mSwipeRefresh.addView(mList, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		mSwipeRefresh.setOnRefreshListener(this);
		mSwipeRefresh.setColorSchemeColors(R.color.ptr_green, R.color.ptr_orange,
				R.color.ptr_red, R.color.ptr_blue);
	}

	protected void newPost() {
		Intent i = new Intent();
		i.setAction(Intent.ACTION_MAIN);
//		i.setClass(getActivity(), NewPostActivity.class);
		startActivity(i);
	}

	protected void load(boolean param) {
		mCache.load(param);
		mCache.cache();
	}

	private class Refresher extends AsyncTask<Boolean, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			Utility.clearOngoingUnreadCount(getActivity());
			mLastCount = mCache.mMessages.getSize();
			mRefreshing = true;
//			if (mSwipeRefresh != null) {
//				mSwipeRefresh.setRefreshing(true);
//				mSwipeRefresh.invalidate();
//			}
		}

		@Override
		protected Boolean doInBackground(Boolean... params) {
			load(params[0]);
			return params[0];
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			
			if (result) {
				mList.smoothScrollToPosition(0);
			}
			
			mAdapter.notifyDataSetChanged();
			mRefreshing = false;
			if (mSwipeRefresh != null) {
				mSwipeRefresh.setRefreshing(false);
			}
		}

	}
}
