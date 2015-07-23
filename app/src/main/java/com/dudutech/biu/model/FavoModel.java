package com.dudutech.biu.model;/*
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



import android.os.Parcel;
import android.os.Parcelable;

public class FavoModel implements Parcelable
{
    // Json map
    public MessageModel status;
    public String favorited_time;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(status, flags);
        dest.writeString(favorited_time);
    }

    public static final Parcelable.Creator<FavoModel> CREATOR = new Parcelable.Creator<FavoModel>() {

        @Override
        public FavoModel createFromParcel(Parcel in) {
            FavoModel ret = new FavoModel();
            ret.status = in.readParcelable(MessageModel.class.getClassLoader());
            ret.favorited_time = in.readString();
            return ret;
        }

        @Override
        public FavoModel[] newArray(int size) {
            return new FavoModel[size];
        }


    };

}
