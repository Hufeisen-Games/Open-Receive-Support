package com.gitlab.hufeisen_games.receiveSupport.commands.types;

import java.sql.SQLException;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public interface ServerCommand {

	public void performCommand(Member m, TextChannel channel, Message message) throws SQLException;
	
}