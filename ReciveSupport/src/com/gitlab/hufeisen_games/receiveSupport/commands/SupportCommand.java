package com.gitlab.hufeisen_games.receiveSupport.commands;

import java.awt.Color;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import com.gitlab.hufeisen_games.receiveSupport.commands.types.ServerCommand;
import com.gitlab.hufeisen_games.receiveSupport.manage.AdminManager;
import com.gitlab.hufeisen_games.receiveSupport.manage.DeleteTime;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SupportCommand implements ServerCommand{
	
	public void performCommand(Member m, TextChannel channel, Message message) throws SQLException {
		
		AdminManager.sendMessage(channel, 
								 "https://dc.hufeisen-games.de", 
								 "https://dc.hufeisen-games.de", 
								 Color.blue, 
								 new DeleteTime(5, TimeUnit.MINUTES));
		message.delete().queue();
		
	}

}
