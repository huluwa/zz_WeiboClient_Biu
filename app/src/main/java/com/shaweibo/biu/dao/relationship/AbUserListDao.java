package com.shaweibo.biu.dao.relationship;

import android.database.Cursor;

import com.shaweibo.biu.dao.timeline.ITimelineBaseDao;
import com.shaweibo.biu.db.tables.HomeTimeLineTable;
import com.shaweibo.biu.global.Constants;
import com.shaweibo.biu.model.BaseListModel;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2015-7-21.
 */
public abstract class AbUserListDao<T extends BaseListModel>   implements ITimelineBaseDao  {
    public Constants.LOADING_STATUS mStatus;
    protected long cursor;
    protected  T mListModel;


    @Override
    public Constants.LOADING_STATUS getStatus() {
        return mStatus;
    }
    @Override
    public T getList() {
        return mListModel;
    }
    @Override
    public void loadFromCache() {
        Cursor cursor = query();

        if (cursor!=null&&cursor.getCount() == 1) {
            cursor.moveToFirst();
            mListModel = (T) new Gson().fromJson(cursor.getString(cursor.getColumnIndex(HomeTimeLineTable.JSON)),getListClass());
        } else {
            try {
                mListModel = (T) getListClass().newInstance();
            } catch (Exception e) {

            }
        }
    }


    @Override
    public void load(boolean isRefresh) {

        if(mStatus== Constants.LOADING_STATUS.FINISH){
            return;
        }
        if (isRefresh) {
            cursor = 0;
        }

        BaseListModel list = load();
        dealStatus(list);
        if(list==null ){
            return;
        }
        if (isRefresh) {
            mListModel.getList().clear();
        }
        if(list.next_cursor!=0){
                cursor=list.next_cursor;
        }
        else {
            mStatus= Constants.LOADING_STATUS.FINISH;
        }


        mListModel.addAll(false, list);


    }

    protected void dealStatus(BaseListModel model){
        if(model!=null){
            int count =model.getList().size();
            if(count<Constants.HOME_TIMELINE_PAGE_SIZE){
                mStatus= Constants.LOADING_STATUS.FINISH;
            }
            else {
                mStatus= Constants.LOADING_STATUS.NORMAL;
            }
        }
        else{
            mStatus= Constants.LOADING_STATUS.FAIL;
        }
    }

    public abstract BaseListModel load();


    protected abstract Class<? extends T> getListClass();
}
