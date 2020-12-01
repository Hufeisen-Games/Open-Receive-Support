package com.gitlab.hufeisen_games.receiveSupport.music;

import java.util.concurrent.ConcurrentHashMap;

import com.gitlab.hufeisen_games.receiveSupport.MainBot;

public class PlayerManager {

	public ConcurrentHashMap<Long, MusicController> controller;
	
	public PlayerManager() {
		this.controller = new ConcurrentHashMap<Long, MusicController>();
	}
	
	@SuppressWarnings("static-access")
	public MusicController getController(long guildid) {
		MusicController mc = null;
		
		if(this.controller.containsKey(guildid)) {
			mc = this.controller.get(guildid);
		} else {
			mc = new MusicController(MainBot.INSTANCE.shardMan.getGuildById(guildid));
			
			this.controller.put(guildid, mc);
		}
		
		return mc;
	}
	
}
