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

public class SetLanguageCommand implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) throws SQLException {
		
		Guild guild = m.getGuild();
		
		if(m.hasPermission(Permission.MANAGE_CHANNEL) || AdminManager.isAdmin(m)) {
			
			String[] args = message.getContentDisplay().split(" ");
			
			if(args.length == 2) {
				if(args[1].equalsIgnoreCase("de")) {
				
					LiteSQL.onUpdate("INSERT INTO settings(guildid, language) VALUES("+guild.getIdLong()+", "+ 1 +")");
					AdminManager.sendMessage(channel, 
											 "Die Sprache wurde zu `de` geändert.", 
											 "The Language was Updatet to `de`.", 
											 Color.green, 
											 new DeleteTime(30, TimeUnit.SECONDS));
				}
				
				else if(args[1].equalsIgnoreCase("en")) {
					
					LiteSQL.onUpdate("DELETE FROM settings WHERE guildid = " + guild.getIdLong());
					AdminManager.sendMessage(channel, 
							 "Die Sprache wurde zu `en` geändert.", 
							 "The Language was Updatet to `en`.", 
							 Color.green, 
							 new DeleteTime(30, TimeUnit.SECONDS));
					
				} else {
					
					AdminManager.sendMessage(channel, 
											 "Diesen Wert gibt es nicht. Bitte benutze `en` oder `de`", 
											 "There is no such value. Please use `en` or `de`", 
											 Color.red, 
											 new DeleteTime(30, TimeUnit.SECONDS));
					
				}
				message.delete().queue();

			}
			
		}
		
	}

}
