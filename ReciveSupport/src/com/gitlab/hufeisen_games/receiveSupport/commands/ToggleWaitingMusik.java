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

public class ToggleWaitingMusik implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) throws SQLException {
		
		Guild guild = m.getGuild();
		
		if(m.hasPermission(Permission.MANAGE_CHANNEL) || AdminManager.isAdmin(m)) {
			
			String[] args = message.getContentDisplay().split(" ");
			
			if(args.length == 2) {
				if(args[1].equalsIgnoreCase("true")) {
				
					LiteSQL.onUpdate("INSERT INTO joinvoice(guildid, value) VALUES("+guild.getIdLong()+", "+ 1 +")");
					AdminManager.sendMessage(channel, 
											 "Die Wartemusik wurde aktiviert", 
											 "The waitingmusic was activated.", 
											 Color.green, 
											 new DeleteTime(10, TimeUnit.SECONDS));
				}
				
				else if(args[1].equalsIgnoreCase("false")) {
					
					LiteSQL.onUpdate("DELETE FROM joinvoice WHERE guildid = " + guild.getIdLong());
					AdminManager.sendMessage(channel, 
							 "Die Wartemusik wurde deaktiviert", 
							 "The waitingmusic was deactivated.", 
							 Color.green, 
							 new DeleteTime(10, TimeUnit.SECONDS));
					
				} else {
					
					AdminManager.sendMessage(channel, 
							 "Diesen Wert gibt es nicht. Bitte benutze `true` oder `false`", 
							 "There is no such value. Please use `true` or `false`", 
							 Color.red, 
							 new DeleteTime(10, TimeUnit.SECONDS));
					
				}
				message.delete().queue();

			}
			
		}
		
	}
}
