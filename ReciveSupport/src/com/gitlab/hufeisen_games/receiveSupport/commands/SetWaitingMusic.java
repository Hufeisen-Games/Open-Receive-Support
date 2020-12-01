package com.gitlab.hufeisen_games.receiveSupport.commands;

import java.awt.Color;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import com.gitlab.hufeisen_games.receiveSupport.commands.types.ServerCommand;
import com.gitlab.hufeisen_games.receiveSupport.manage.AdminManager;
import com.gitlab.hufeisen_games.receiveSupport.manage.DeleteTime;
import com.gitlab.hufeisen_games.receiveSupport.manage.LiteSQL;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SetWaitingMusic implements ServerCommand{

	public void performCommand(Member m, TextChannel channel, Message message) throws SQLException {
		
		Guild guild = m.getGuild();
		
		if(m.hasPermission(Permission.MANAGE_CHANNEL) || AdminManager.isAdmin(m)) {
			
			String[] args = message.getContentDisplay().split(" ");
			
			if(args.length == 2) {
				LiteSQL.onUpdate("DELETE FROM waitingmusic WHERE guildid = " + guild.getIdLong());
				LiteSQL.onUpdate("INSERT INTO waitingmusic(guildid, url) VALUES("+guild.getIdLong()+",'"+args[1]+"')");
				AdminManager.sendMessage(channel,
										 "Die Musik mit der URL `" + args[1] + "` wurde gesetzt.",
										 "The music with the URL `" + args[1] + "` was set.",
										 Color.GREEN,
										 new DeleteTime(10, TimeUnit.SECONDS));
				message.delete().queue();
			}
			
		}
		
	}

}
