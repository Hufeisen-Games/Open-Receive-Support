package com.gitlab.hufeisen_games.receiveSupport;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import com.gitlab.hufeisen_games.receiveSupport.commands.AddChannelCommand;
import com.gitlab.hufeisen_games.receiveSupport.commands.AddRoleCommand;
import com.gitlab.hufeisen_games.receiveSupport.commands.HelpCommand;
import com.gitlab.hufeisen_games.receiveSupport.commands.RemoveChannelCommand;
import com.gitlab.hufeisen_games.receiveSupport.commands.RemoveRoleCommand;
import com.gitlab.hufeisen_games.receiveSupport.commands.SetLanguageCommand;
import com.gitlab.hufeisen_games.receiveSupport.commands.SetPrefix;
import com.gitlab.hufeisen_games.receiveSupport.commands.SetWaitingMusic;
import com.gitlab.hufeisen_games.receiveSupport.commands.SupportCommand;
import com.gitlab.hufeisen_games.receiveSupport.commands.ToggleJoinMessage;
import com.gitlab.hufeisen_games.receiveSupport.commands.ToggleWaitingMusik;
import com.gitlab.hufeisen_games.receiveSupport.commands.AnnouncementCommand;
import com.gitlab.hufeisen_games.receiveSupport.commands.types.ServerCommand;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;



public class CommandManager {
	public ConcurrentHashMap<String, ServerCommand> commands;
	
	public CommandManager() {
		this.commands = new ConcurrentHashMap<>();
		
		this.commands.put("addchannel", new AddChannelCommand());
		this.commands.put("removechannel", new RemoveChannelCommand());
		this.commands.put("addrole", new AddRoleCommand());
		this.commands.put("removerole", new RemoveRoleCommand());
		this.commands.put("togglewelcomemessage", new ToggleJoinMessage());
		this.commands.put("help", new HelpCommand());
		this.commands.put("support", new SupportCommand());
		this.commands.put("announcement", new AnnouncementCommand());
		this.commands.put("togglewaitingmusic", new ToggleWaitingMusik());
		this.commands.put("setwaitingmusic", new SetWaitingMusic());
		this.commands.put("setlanguage", new SetLanguageCommand());
		this.commands.put("setprefix", new SetPrefix());
		
	}
	
	public boolean perform(String command, Member m, TextChannel channel, Message message) throws SQLException {
		
		ServerCommand cmd;
		if((cmd = this.commands.get(command.toLowerCase())) != null) {
			cmd.performCommand(m, channel, message);
			return true;
		}
		
		return false;
	}
}

