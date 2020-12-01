package com.gitlab.hufeisen_games.receiveSupport.listener;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.gitlab.hufeisen_games.receiveSupport.MainBot;
import com.gitlab.hufeisen_games.receiveSupport.manage.LiteSQL;
import com.gitlab.hufeisen_games.receiveSupport.music.AudioLoadResult;
import com.gitlab.hufeisen_games.receiveSupport.music.MusicController;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinListener extends ListenerAdapter {
	
	ArrayList<String> calledPlayers;
	public ArrayList<Long> playingChannels = new ArrayList<Long>();
	
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		onJoin(event.getChannelJoined(), event.getEntity());
	}
	
	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		ArrayList<Long> supporter = new ArrayList<Long>();
		onJoin(event.getChannelJoined(), event.getEntity());
		onLeave(event.getChannelLeft(), event.getEntity());
		
		for(Member m : event.getChannelJoined().getMembers()) {
			supporter.add(m.getIdLong());
		}
		
	}
	
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		onLeave(event.getChannelLeft(), event.getEntity());
	}
	
	@SuppressWarnings("unused")
	public void onJoin(VoiceChannel joined, Member member) {
		calledPlayers = new ArrayList<String>();
		Guild guild = member.getGuild();
		ResultSet set = LiteSQL.onQuery("SELECT channelid FROM supportchannels WHERE guildid = " + guild.getIdLong() + " AND channelid = " + joined.getIdLong());
		try {
			if(set.next()) {
			
				for(Member memb : guild.getMembers()) {
					if(!memb.getUser().isBot() && !member.getUser().isBot()) {
						try {
							memb.getUser().openPrivateChannel().queue((ch) -> {
								
								for(int i=0; i<guild.getRoles().size(); i++) {
									if(memb.getRoles().contains(guild.getRoles().get(i))) {
									
										ResultSet set2 = LiteSQL.onQuery("SELECT roleid FROM supportroles WHERE guildid = " + guild.getIdLong() + " AND roleid = " + guild.getRoles().get(i).getIdLong());
	
										try {
											if(set.next()) {
												int tmembers = joined.getMembers().size();
												if(!calledPlayers.contains(memb.getEffectiveName())) {
													for(Member m : joined.getMembers()) {
														if(m.getUser().isBot()) {
															tmembers -= 1;
														}
													}
													ResultSet set100 = LiteSQL.onQuery("SELECT language FROM settings WHERE guildid = "+guild.getIdLong());
													if(set100.next()) {
														ch.sendMessage("["+guild.getName()+"/"+joined.getName()+"] Der Benutzer `" + member.getEffectiveName() + "` wartet im Support Warteraum (`"+tmembers+"` Insgesamt).").queue();
													} else {
														ch.sendMessage("["+guild.getName()+"/"+joined.getName()+"] The user `" + member.getEffectiveName() + "` is waiting in the support waiting room (`"+tmembers+"` total).").queue();
													}
													
													calledPlayers.add(memb.getEffectiveName());
													
												}
											}
										} catch (SQLException e) {
											System.out.println("Error Code 102#2");
										}
										
									}
								}
							});
						} catch(Exception e) {
						}
					}
				}
				
				if(!member.getUser().isBot() && joined.getMembers().size() == 1) {
					ResultSet set3 = LiteSQL.onQuery("SELECT value FROM joinvoice WHERE guildid = " + guild.getIdLong() + " AND value = 1");
					
					try {
						if(set3.next()) {
						
							String[] args = "https://www.youtube.com/watch?v=xDbiz2Mxys4".split(" ");
							ResultSet set4 = LiteSQL.onQuery("SELECT url FROM waitingmusic WHERE guildid = "+guild.getIdLong());
							if(set4.next()) {
								String s = "!play "+set4.getString(1);
								args = s.split(" ");
							}
							
							MusicController controller = MainBot.INSTANCE.playerManager.getController(guild.getIdLong());
						
							AudioPlayerManager apm = MainBot.INSTANCE.audioPlayerManager;
						
							AudioManager manager = joined.getGuild().getAudioManager();
							manager.openAudioConnection(joined);
							playingChannels.add(joined.getIdLong());
						
							StringBuilder strBuilder = new StringBuilder();
							for(int i = 1; i < args.length; i++) strBuilder.append(args[i] + " ");
						
							String url = strBuilder.toString().trim();
							if(!url.startsWith("https")) {
								url = "ytsearch: " + url;
							}
						
						
							apm.loadItem(url, new AudioLoadResult(url, controller));
						
						}
					} catch (SQLException e) {
						System.out.println("Error Code 103");
					}
				
				}
				if(!member.getUser().isBot()) {
					member.getUser().openPrivateChannel().queue((ch) -> {
					
						ResultSet set2 = LiteSQL.onQuery("SELECT value FROM joinmessage WHERE guildid = " + guild.getIdLong() + " AND value = 1");
					
						try {
							if(set2.next()) {
								ResultSet set100 = LiteSQL.onQuery("SELECT language FROM settings WHERE guildid = "+guild.getIdLong());
								if(set100.next()) {
									ch.sendMessage("Willkommen im Support. Zurzeit ist leider kein Supporter verfügbar. Bitte warte einen Moment.").queue();
								} else {
									ch.sendMessage("Welcome to support. No supporter is currently available. Please wait a moment.").queue();
								}
							}
						} catch (SQLException e) {
							System.out.println("Error Code 103");
						}
					
					});
				}
			}
		} catch (SQLException e) {
			System.out.println("Error Code 102#1");
		}

	}
	
	@SuppressWarnings("unused")
	public void onLeave(VoiceChannel leaved, Member member) {
		
		calledPlayers = new ArrayList<String>();
		Guild guild = member.getGuild();
		ResultSet set = LiteSQL.onQuery("SELECT channelid FROM supportchannels WHERE guildid = " + guild.getIdLong() + " AND channelid = " + leaved.getIdLong());
		try {
			if(set.next()) {
			
				for(Member memb : guild.getMembers()) {
					if(!memb.getUser().isBot() && !member.getUser().isBot()) {
						memb.getUser().openPrivateChannel().queue((ch) -> {
							
							for(int i=0; i<guild.getRoles().size(); i++) {
								if(memb.getRoles().contains(guild.getRoles().get(i))) {
								
									ResultSet set2 = LiteSQL.onQuery("SELECT roleid FROM supportroles WHERE guildid = " + guild.getIdLong() + " AND roleid = " + guild.getRoles().get(i).getIdLong());

									try {
										if(set.next()) {
											if(!calledPlayers.contains(memb.getEffectiveName())) {
												
												int tmembers = leaved.getMembers().size();
												if(!calledPlayers.contains(memb.getEffectiveName())) {
													for(Member m : leaved.getMembers()) {
														if(m.getUser().isBot()) {
															tmembers -= 1;
														}
													}
												}
												ResultSet set100 = LiteSQL.onQuery("SELECT language FROM settings WHERE guildid = "+guild.getIdLong());
												if(set100.next()) {
													ch.sendMessage("["+guild.getName()+"/"+leaved.getName()+"] Der Benutzer `" + member.getEffectiveName() + "` hat den Support Warteraum verlassen. (`"+tmembers+"` Ã¼brig).").queue();
												} else {
													ch.sendMessage("["+guild.getName()+"/"+leaved.getName()+"] The user `" + member.getEffectiveName() + "` has left the support waiting room (`"+tmembers+"` left).").queue();
												}
												
												calledPlayers.add(memb.getEffectiveName());
												
											}
										}
									} catch (SQLException e) {
										System.out.println("Error Code 102#2");
									}
								}
							}
						});
					}
				}
				int leavedMembers = 0;
				for(Member m : leaved.getMembers()) {
					if(m.getUser().isBot()) {
						leavedMembers += 1;
					}
				}
				if(!member.getUser().isBot() && leaved.getMembers().size() == leavedMembers) {
					MusicController controller = MainBot.INSTANCE.playerManager.getController(guild.getIdLong());
					AudioManager manager = leaved.getGuild().getAudioManager();
					controller.getPlayer().stopTrack();
					manager.closeAudioConnection();
					playingChannels.remove(leaved.getIdLong());
				}
			}
			
		} catch (SQLException e) {
			System.out.println("Error Code 102#1");
		}
		
	}
}
