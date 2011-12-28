package com.weibo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import weibo4android.Count;
import weibo4android.Status;
import weibo4android.User;

public class WeiboData {
	private static List<Status> status;
	private static HashMap<Long, Count> counts;
	private static User user;
	private static ArrayList<String> homeNames;
	private static WeiboData weiboData;

	public synchronized static WeiboData getInstance() {
		if (weiboData == null) {
			weiboData = new WeiboData();
		}
		return weiboData;
	}

	public static List<Status> getStatus() {
		return status;
	}

	public static void setStatus(List<Status> status) {
		WeiboData.status = status;
	}

	public static HashMap<Long, Count> getCounts() {
		return counts;
	}

	public static void setCounts(HashMap<Long, Count> counts) {
		WeiboData.counts = counts;
	}

	public static User getUser() {
		return user;
	}

	public static void setUser(User user) {
		WeiboData.user = user;
	}

	public static String[] getHomeNames() {
		HashSet<String> set = new HashSet<String>();
		List<Status> sList = getStatus();
		if (sList != null) {
			for (int i = 0; i < sList.size(); i++) {
				String name = sList.get(i).getUser().getScreenName();
				set.add(name);
			}
		}
		return set.toArray(new String[] {});
	}
}
