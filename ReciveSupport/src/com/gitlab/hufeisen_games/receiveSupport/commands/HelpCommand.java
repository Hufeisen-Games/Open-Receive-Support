package com.gitlab.hufeisen_games.receiveSupport.commands;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

import com.gitlab.hufeisen_games.receiveSupport.commands.types.ServerCommand;
import com.gitlab.hufeisen_games.receiveSupport.manage.AdminManager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class HelpCommand implements ServerCommand{
	
	public void performCommand(Member m, TextChannel channel, Message message) throws SQLException {
		
		if(AdminManager.getLanguage(m.getGuild()) == "de") {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setDescription("**Help** \n"
					+ "**.help** - *diese Nachricht*\n"
					+ "**.support** - *Der Invite link zum Support Server*\n"
					+ "**.setprefix <prefix>** - *Setzt ein neues Prefix*\n"
					+ "**.addrole `<id>`** - *Fügt eine Support Rolle hinzu*\n"
					+ "**.addchannel `<id>`** - *Fügt einen Support Warteraum hinzu*\n"
					+ "**.removerole `<id>`** - *Entfernt eine Support Rolle*\n"
					+ "**.removechannel `<id>`** - *Entfernt einen Support Warteraum*\n"
					+ "**.setwaitingmusic `<url>`** - *Setzt die Support Warteraum Musik*\n"
					+ "**.togglewaitingmusic `<true/false>`** - *Aktiviert/deaktiviert die Support Warteraum Musik*\n"
					+ "**.setlanguage `<de/en>`** - *Ändert die Sprache*\n"
					+ "**.togglewelcomemessage `<true/false>`** - *Deaktiviert die Begrüßungsnachricht für den Benutzer*\n");
			builder.setColor(0x23cba7);
			builder.setFooter(AdminManager.EmbedFooterGerman+"\n"
					+ "Diese Nachricht wird nach 2 Minuten selbst gelöscht .");
			builder.setTimestamp(OffsetDateTime.now());
			
			channel.sendMessage(builder.build()).complete().delete().queueAfter(2, TimeUnit.MINUTES);
		} else {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setDescription("**Help** \n"
					+ "**.help** - *recive this message*\n"
					+ "**.support** - *invite link to the support server*\n"
					+ "**.setprefix <prefix>** - *set a new prefix*\n"
					+ "**.addrole `<id>`** - *add a role*\n"
					+ "**.addchannel `<id>`** - *add a channel*\n"
					+ "**.removerole `<id>`** - *remove a role*\n"
					+ "**.removechannel `<id>`** - *remove a channel*\n"
					+ "**.setwaitingmusic `<url>`** - *set the waiting music*\n"
					+ "**.togglewaitingmusic `<true/false>`** - *aktivate/deaktivate the waiting music*\n"
					+ "**.setlanguage `<de/en>`** - *set the language*\n"
					+ "**.togglewelcomemessage `<true/false>`** - *toggle the recive message to the user*\n");
			builder.setColor(0x23cba7);
			builder.setFooter(AdminManager.EmbedFooterEnglish+"\n"
					+ "This message will be delete automatical after 2 minutes.");
			builder.setTimestamp(OffsetDateTime.now());
			
			channel.sendMessage(builder.build()).complete().delete().queueAfter(2, TimeUnit.MINUTES);
		}
		message.delete().queue();
		
	}

}
