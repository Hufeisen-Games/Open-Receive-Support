package com.gitlab.hufeisen_games.receiveSupport.manage;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class AdminManager {
	
	public static String token;
	public static ArrayList<String> admins = new ArrayList<String>();
	public static String EmbedFooterEnglish;
	public static String EmbedFooterGerman;
	public static ArrayList<String> activityText = new ArrayList<String>();
	
	
	public static boolean isAdmin(Member m) {
		if(admins.contains(m.getUser().getId())){return true;}
		
		return false;
	}
	
	public static String getLanguage(Guild guild) throws SQLException {
		ResultSet set100 = LiteSQL.onQuery("SELECT language FROM settings WHERE guildid = "+guild.getIdLong());
		if(set100.next()) {
			return "de";
		} else {
			return "en";
		}
	}
	
	public static String getPrefix(Guild guild) throws SQLException {
		ResultSet set100 = LiteSQL.onQuery("SELECT prefix FROM prefix WHERE guildid = "+guild.getIdLong());
		if(set100.next()) {
			return set100.getString(1);
		} else {
			return ".";
		}
	}
	
	public static void sendMessage(TextChannel channel, String german, String english, Color color, DeleteTime dt) throws SQLException {
		if(getLanguage(channel.getGuild()) == "de") {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setDescription(german);
			builder.setColor(color);
			builder.setFooter(EmbedFooterGerman);
			channel.sendMessage(builder.build()).complete().delete().queueAfter(dt.getTime(), dt.getUnit());
		} else if(getLanguage(channel.getGuild()) == "en") {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setDescription(english);
			builder.setColor(color);
			builder.setFooter(EmbedFooterEnglish);
			channel.sendMessage(builder.build()).complete().delete().queueAfter(dt.getTime(), dt.getUnit());
		}
	}
	
	public static void CheckSettings() {
		File settings = new File("config.properties");
		if(!settings.exists()) {
			try {
				settings.createNewFile();
				try (OutputStream output = new FileOutputStream("config.properties")) {

		            Properties prop = new Properties();
		            Properties prop2 = new Properties();
		            Properties prop3 = new Properties();
		            Properties prop4 = new Properties();

		            // set the properties value
		            prop.setProperty("token", "");
		            prop.store(output, "Here you can enter your bot token. You can find it on the Discord dveloper portal.");
		            prop2.setProperty("admins", "824276496245349698;428537861943460923");
		            prop2.store(output, "Here you can enter the bot admins. When you have more than one you must add a ';' between theire ids.");
		            prop3.setProperty("embedFooterEnglish", "Open Receive Support - For help or support visit our discord server: https://dc.hufeisen-games.de");
		            prop3.setProperty("embedFooterGerman", "Open Receive Support - Besuche für Hilfe und Support unseren Discord Server: https://dc.hufeisen-games.de");
		            prop3.store(output, "Here you can enter the embed footers in English and Germany. You can add more lines with \\n.");
		            prop4.setProperty("activityText", "on the Support;.help;Thank you for using!;");
		            prop4.store(output, "Here you can enter the activity text. You must split them with a ';'.");

		            // save properties to project root folder

		            System.out.println("The default settings was generated. Please enter your bot token in the config.properties file.");
		            System.exit(0);

		        } catch (IOException io) {
		            io.printStackTrace();
		        }
			} catch (IOException e) {
				System.out.println("Was not able to create settings.json");
				System.exit(0);
			}
		}
		
        try (InputStream input = new FileInputStream("config.properties")) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            token = prop.getProperty("token");
            String[] localAdmins = prop.getProperty("admins").split(";");
            for (String admin : localAdmins) {
				admins.add(admin);
			}
            EmbedFooterEnglish = prop.getProperty("embedFooterEnglish");
            EmbedFooterGerman = prop.getProperty("embedFooterGerman");
            String[] activities = prop.getProperty("activityText").split(";");
            for (String activity : activities) {
            	activityText.add(activity);
			}

        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	
}
