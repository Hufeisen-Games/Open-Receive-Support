package com.gitlab.hufeisen_games.receiveSupport.listener;

import java.sql.SQLException;

import com.gitlab.hufeisen_games.receiveSupport.MainBot;
import com.gitlab.hufeisen_games.receiveSupport.manage.AdminManager;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	
	

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String message = event.getMessage().getContentDisplay();
		
		if(event.isFromType(ChannelType.TEXT)) {
			TextChannel channel = event.getTextChannel();
			try {
				if(message.startsWith(AdminManager.getPrefix(event.getGuild()))) {
					String[] args = message.replace(AdminManager.getPrefix(event.getGuild()), "").split(" ");
						
					if(args.length > 0) {
						try {
							
							if(!MainBot.INSTANCE.getCmdMan().perform(args[0], event.getMember(), channel, event.getMessage())) {
								
							}
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
