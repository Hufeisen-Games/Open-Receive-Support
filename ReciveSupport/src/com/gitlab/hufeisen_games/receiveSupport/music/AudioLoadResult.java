package com.gitlab.hufeisen_games.receiveSupport.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class AudioLoadResult implements AudioLoadResultHandler{

	
	private final String uri;
	private final MusicController controller;
	TrackScheduler trackScheduler = new TrackScheduler();
	
	public AudioLoadResult(String uri, MusicController controller) {
		this.uri = uri;
		this.controller = controller;
		controller.getPlayer().addListener(trackScheduler);
	}
	
	
	@Override
	public void trackLoaded(AudioTrack track) {
		controller.getPlayer().playTrack(track);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
	    /*for (AudioTrack track : playlist.getTracks()) {
	        trackScheduler.queue(track);
	      }*/
	}

	@Override
	public void noMatches() {
		System.out.println("No Matches URI: " + uri);
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		// TODO Auto-generated method stub
		
	}

}
