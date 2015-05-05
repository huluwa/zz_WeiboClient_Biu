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

package com.dudutech.weibo.global;

public class Constants
{
	public static final int DB_CACHE_DAYS = 10;
	public static final int FILE_CACHE_DAYS = 3;
	
	// File Cache Types
	public static final String FILE_CACHE_AVATAR_SMALL = "avatar_small";
	public static final String FILE_CACHE_AVATAR_LARGE = "avatar_large";
	public static final String FILE_CACHE_COVER = "cover";
	public static final String FILE_CACHE_PICS_SMALL = "pics_small";
	public static final String FILE_CACHE_PICS_LARGE = "pics_large";
	
	// Statuses
	public static final int HOME_TIMELINE_PAGE_SIZE = 20;
	
	// SQL
	public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS ";
	private static final String APP_ID = "211160679";
	private static final String APP_KEY_HASH = "1e6e33db08f9192306c4afa0a61ad56c";
	private static final String REDIRECT_URI = "http://oauth.weico.cc";
	private static final String PACKAGE_NAME = "com.eico.weico";
	private static final String SCOPE = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write";
}
