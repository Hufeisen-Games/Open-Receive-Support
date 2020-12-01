package com.gitlab.hufeisen_games.receiveSupport.manage;

import java.util.concurrent.TimeUnit;

public class DeleteTime {
	
	int time;
	TimeUnit unit;
	
	public DeleteTime(int time, TimeUnit unit) {
		this.time = time;
		this.unit = unit;
	}
	
	public int getTime() {
		return time;
	}
	
	public TimeUnit getUnit() {
		return unit;
	}
	
}
