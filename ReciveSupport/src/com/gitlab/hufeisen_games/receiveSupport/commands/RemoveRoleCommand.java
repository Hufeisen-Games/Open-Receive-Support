package com.gitlab.hufeisen_games.receiveSupport.commands;

import java.awt.Color;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import com.gitlab.hufeisen_games.receiveSupport.commands.types.ServerCommand;
import com.gitlab.hufeisen_games.receiveSupport.manage.AdminManager;
import com.gitlab.hufeisen_games.receiveSupport.manage.DeleteTime;
import com.gitlab.hufeisen_games.receiveSupport.manage.LiteSQL;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class RemoveRoleCommand implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) throws SQLException {
		
		if(m.hasPermission(Permission.MANAGE_CHANNEL) || AdminManager.isAdmin(m)) {
			
			String[] args = message.getContentDisplay().split(" ");
			
			if(args.length == 2) {
						
				LiteSQL.onUpdate("DELETE FROM supportroles WHERE roleid = " + args[1]);
				AdminManager.sendMessage(channel, 
										 "Die Rolle mit der ID `" + args[1] + "` wurde entfernt.", 
										 "The role with the ID `" + args[1] + "` was removed.", 
										 Color.green, 
										 new DeleteTime(10, TimeUnit.SECONDS));
				message.delete().queue();
				
			}
			
		}
		
	}

}

