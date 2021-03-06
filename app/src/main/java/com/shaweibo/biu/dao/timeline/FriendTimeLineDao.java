/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.shaweibo.biu.dao.timeline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.shaweibo.biu.dao.UrlConstants;
import com.shaweibo.biu.dao.HttpClientUtils;
import com.shaweibo.biu.db.tables.UserTimeLineTable;
import com.shaweibo.biu.global.Constants;
import com.shaweibo.biu.model.MessageListModel;
import com.shaweibo.biu.dao.WeiboParameters;
import com.google.gson.Gson;


/* Cache api for exact user timeline */
public class FriendTimeLineDao extends StatusTimeLineDao
{
	private String mUid;
	
	public FriendTimeLineDao(Context context, String uid) {
		super(context,"");
		mUid = uid;
	}

	@Override
	public void cache() {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		db.beginTransaction();
		db.delete(UserTimeLineTable.NAME, UserTimeLineTable.UID + "=?", new String[]{mUid});
		ContentValues values = new ContentValues();
		values.put(UserTimeLineTable.UID, mUid);
		values.put(UserTimeLineTable.JSON, new Gson().toJson(mListModel));
		db.insert(UserTimeLineTable.NAME, null, values);
		db.setTransactionSuccessful();
		db.endTransaction();
		
	}

	@Override
	public Cursor query() {
		return mHelper.getReadableDatabase().query(UserTimeLineTable.NAME, new String[]{
			UserTimeLineTable.UID,
			UserTimeLineTable.JSON
		}, UserTimeLineTable.UID + "=?", new String[]{mUid}, null, null, null);
	}

	@Override
	public MessageListModel load() {
		WeiboParameters params = new WeiboParameters();
		params.put("uid", mUid);
		params.put("count",  Constants.HOME_TIMELINE_PAGE_SIZE);
		params.put("page", ++mCurrentPage);

		try {
			String json = HttpClientUtils.doGetRequstWithAceesToken(UrlConstants.USER_TIMELINE, params);
			return new Gson().fromJson(json, MessageListModel.class);
		} catch (Exception e) {
			return null;
		}

	}
}
