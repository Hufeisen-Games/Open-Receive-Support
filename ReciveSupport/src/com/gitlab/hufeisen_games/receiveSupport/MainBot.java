package com.gitlab.hufeisen_games.receiveSupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import javax.security.auth.login.LoginException;

import com.gitlab.hufeisen_games.receiveSupport.listener.CommandListener;
import com.gitlab.hufeisen_games.receiveSupport.listener.JoinListener;
import com.gitlab.hufeisen_games.receiveSupport.manage.AdminManager;
import com.gitlab.hufeisen_games.receiveSupport.manage.LiteSQL;
import com.gitlab.hufeisen_games.receiveSupport.manage.SQLManager;
import com.gitlab.hufeisen_games.receiveSupport.music.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class MainBot {

	public static MainBot INSTANCE;
	
	public static ShardManager shardMan;
	
	public static String lizenz;
	
	private CommandManager cmdMan;
	
	public AudioPlayerManager audioPlayerManager;
	
	public PlayerManager playerManager;
	
	public Thread loop;
	
	public static void main(String[] args) {
		AdminManager.CheckSettings();
		try {
			new MainBot();
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public MainBot() throws LoginException, IllegalArgumentException {
		INSTANCE = this;
		
		DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
		
		builder.setToken(AdminManager.token);
		
		builder.setEnabledIntents(GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGE_TYPING, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES);
		
		builder.disableCache(CacheFlag.EMOTE, CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS);
		
		builder.setStatus(OnlineStatus.ONLINE);
		
		this.audioPlayerManager = new DefaultAudioPlayerManager();
		this.playerManager = new PlayerManager();
		
		builder.addEventListeners(new CommandListener());
		builder.addEventListeners(new JoinListener());
		
		this.cmdMan = new CommandManager();
		
		LiteSQL.connect();
		SQLManager.onCreate();
		
		shardMan = builder.build();
		
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		
		shutdown();
		runLoop();
	
		System.out.println("Bot sucessfully started. Use 'exit' to shutdown.");
	}
	
	public void shutdown() {
		
		new Thread(() -> {
			
			String line = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			try {
				while((line = reader.readLine()) != null) {
					
					if(line.equalsIgnoreCase("exit")) {
						shutdown = true;
						if(shardMan != null) {
							
							shardMan.setStatus(OnlineStatus.OFFLINE);
							shardMan.shutdown();
							System.out.println("Bot was deactivated.");
							LiteSQL.disconnect();
						}
						
						if(loop != null) {
							loop.interrupt();
						}
						
						
						reader.close();
						break;
					} else if(line.equalsIgnoreCase("info")) {
						int members = 0;
						int mw = 0;
						String mws = "";
						System.out.println("--------------------------------");
						System.out.println("The Bot is on "+shardMan.getGuilds().size()+" guilds.");
						System.out.println(" ");
						for(Guild guild : shardMan.getGuilds()) {
							if(guild.getIdLong() != 264445053596991498l) {
								members+=guild.getMemberCount();
								if(guild.getMemberCount() > mw) {
									mw = guild.getMemberCount();
									mws = guild.getName();
								}
							}
							System.out.println(guild.getName() + " " + guild.getIdLong());
						}
						System.out.println("");
						System.out.println("Member: "+members);
						System.out.println("Best Server: "+mws+" | "+mw);
						System.out.println("Shards: "+shardMan.getShardsTotal());
						System.out.println("--------------------------------");
					} else {
						String[] cm = line.split(" ");
						if(cm[0].equalsIgnoreCase("info") && cm.length == 2) {
							Guild g = shardMan.getGuildById(cm[1]);
							System.out.println("--------------------------------");
							System.out.println("Info by: "+g.getName()+" | "+g.getId());
							System.out.println("");
							System.out.println("Owner: "+g.getOwner().getUser().getName()+" | "+g.getOwnerId());
							System.out.println("Members: "+g.getMemberCount());
							System.out.println("Nitro level: "+g.getBoostTier());
						} else {
							System.out.println("Use 'exit' to shutdown.");
						}
					}
				}
			} catch (IOException e) {
				//e.printStackTrace();
			}
			
		}).start();
	}
	
	public boolean shutdown = false;
	public boolean hasStarted = false;
	
	public void runLoop() {
		this.loop = new Thread(() -> {
			
			long time = System.currentTimeMillis();
			
			while(!shutdown) {
				if(System.currentTimeMillis() >= time + 1000) {
					time = System.currentTimeMillis();
					onSecond();
				}
			}
		});
		this.loop.setName("Loop");
		this.loop.start();
	}
	
	int next = 30;
	
	public void onSecond() {
		
		if(next%5 == 0) {
			
			if(next == 0) {
				next = 60;
				
				ArrayList<String> ak = AdminManager.activityText;
				
				ak.add("Version 1.0 Pre Release");
				ak.add("Open Receive Support by Hufeisen-Games.de");
				
				shardMan.setActivity(Activity.watching(ak.get(new Random().nextInt(ak.size()-1))));

			}
			else {
				next--;
			}
		}
		else {
			next--;
		}
	}
	
	public CommandManager getCmdMan() {
		return cmdMan;
	}
	
	public static ShardManager getShardMan() {
		return shardMan;
	}
	
}
