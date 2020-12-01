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

public class SetPrefix implements ServerCommand{
	
	public void performCommand(Member m, TextChannel channel, Message message) throws SQLException {
		
		Guild guild = m.getGuild();
		
		if(m.hasPermission(Permission.MANAGE_CHANNEL) || AdminManager.isAdmin(m)) {
			
			String[] args = message.getContentDisplay().split(" ");
			
			if(args.length == 2) {
				LiteSQL.onUpdate("DELETE FROM prefix WHERE guildid = " + guild.getIdLong());
				LiteSQL.onUpdate("INSERT INTO prefix(guildid, prefix) VALUES("+guild.getIdLong()+",'"+args[1]+"')");
				message.delete().queue();
				AdminManager.sendMessage(channel,
										 "Das Prefix wurde zu `" + args[1] + "` geändert.",
										 "The Prefix was set to `" + args[1] + "`",
										 Color.GREEN,
										 new DeleteTime(10, TimeUnit.SECONDS));
				message.delete().queue();
			}
			
		}
		
	}

}
