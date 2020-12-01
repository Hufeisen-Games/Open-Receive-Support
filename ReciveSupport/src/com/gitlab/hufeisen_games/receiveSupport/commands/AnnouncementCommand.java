package com.gitlab.hufeisen_games.receiveSupport.commands;

import java.awt.Color;
import java.util.ArrayList;

import com.gitlab.hufeisen_games.receiveSupport.MainBot;
import com.gitlab.hufeisen_games.receiveSupport.commands.types.ServerCommand;
import com.gitlab.hufeisen_games.receiveSupport.manage.AdminManager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class AnnouncementCommand implements ServerCommand{

	public ArrayList<Long> calledOwners = new ArrayList<Long>();
	
	public void performCommand(Member m, TextChannel channel, Message message) {
		if(AdminManager.isAdmin(m)) {
			for(Guild guild : MainBot.getShardMan().getGuilds()) {
				if(!calledOwners.contains(guild.getOwnerIdLong())) {
					guild.getOwner().getUser().openPrivateChannel().queue((ch) -> {
						String[] args = message.getContentDisplay().split(" ");
						String update = "";
						for(String s : args) {
							if(!s.equalsIgnoreCase(".announcement")) {
								update+= s +" ";
							}
						}
						calledOwners.add(guild.getOwnerIdLong());
						try {
							if(AdminManager.getLanguage(channel.getGuild()) == "de") {
								EmbedBuilder builder = new EmbedBuilder();
								builder.setTitle("Ankündigung");
								builder.setDescription(update);
								builder.setColor(Color.blue);
								builder.setFooter(AdminManager.EmbedFooterGerman);
								ch.sendMessage(builder.build()).queue();
							} else if(AdminManager.getLanguage(channel.getGuild()) == "en") {
								EmbedBuilder builder = new EmbedBuilder();
								builder.setTitle("Announcement");
								builder.setDescription(update);
								builder.setColor(Color.blue);
								builder.setFooter(AdminManager.EmbedFooterEnglish);
								ch.sendMessage(builder.build()).queue();
							}
						} catch(Exception e) {
							System.out.println("Can not send to: "+guild.getOwner().getUser().getName());
						}
					});
				}	
				
			}
			message.delete().queue();
		}
		
	}
	
}
