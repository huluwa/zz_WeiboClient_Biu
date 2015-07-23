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

package com.dudutech.biu.model;

import android.os.Parcelable;

import java.util.List;

/* Type of all weibo json lists */
public abstract class BaseListModel<I, L> implements Parcelable
{
	public int total_number = 0;
	public long previous_cursor = 0, next_cursor = 0;
	
	public abstract int getSize();
	public abstract I get(int position);
	public abstract List<? extends I> getList();
	
	/*
	  @param toTop If true, add to top, else add to bottom
	  @param values All values needed to be added
	*/
	public abstract void addAll(boolean toTop, L values);

	@Override
	public L clone() {
		try {
			BaseListModel<I, L> ret = this.getClass().newInstance();
			ret.addAll(false, (L) this);
			return (L) ret;
		} catch (Exception e) {
			return null;
		}
	}
}
