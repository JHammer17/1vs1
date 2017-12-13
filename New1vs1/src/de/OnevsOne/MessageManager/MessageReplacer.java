package de.OnevsOne.MessageManager;

import de.OnevsOne.main;
import net.md_5.bungee.api.ChatColor;

/**
 * Der Code ist von JHammer
 *
 * 28.05.2016 um 12:40:43 Uhr
 */
public class MessageReplacer {

	private static main plugin;
	
	@SuppressWarnings("static-access")
	public MessageReplacer(main plugin) {
		this.plugin = plugin;
	}

	public static String replaceT(String msg) {
		
		msg = msg.replaceAll("%Prefix%", plugin.prefix);
		msg = msg.replaceAll("%TPrefix%", plugin.tournamentPrefix);
		msg = msg.replaceAll("%n", "\n");
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		return msg;
	}
	
	public static String replaceStrings(String msg) {
		
		msg = msg.replaceAll("%Prefix%", plugin.prefix);
		msg = msg.replaceAll("%n", "\n");
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		return msg;
	}
	
	public static String replaceStrings(String msg, String p, String p2, String p3, String Arena) {
		if(msg == null) {
			return null;
		}
		
		if(p != null && msg.contains("%Player%")) {
		 msg = msg.replaceAll("%Player%", p);
		}
		
		if(p2 != null && msg.contains("%Player2%")) {
		 msg = msg.replaceAll("%Player2%", p2);
		}
		
		if(p2 != null && msg.contains("%Player3%")) {
		 msg = msg.replaceAll("%Player3%", p3);
		}
		
		if(Arena != null) {
		 if(msg.contains("%Arena%")) {
		  msg = msg.replaceAll("%Arena%", Arena);	
		 }
		}
		
		if(plugin.prefix != null && msg.contains("%Prefix%")) {
		 msg = msg.replaceAll("%Prefix%", "" + plugin.prefix);
		}
		
		if(msg.contains("&")) {
			msg = ChatColor.translateAlternateColorCodes('&', msg);
		}
		
		
		return msg;
	}
	
	public static String replaceStrings(String msg, String p, String p2, String Arena) {
		if(msg == null) {
			return null;
		}
		
		if(p != null && msg.contains("%Player%")) {
		 msg = msg.replaceAll("%Player%", p);
		}
		
		if(p2 != null && msg.contains("%Player2%")) {
		 msg = msg.replaceAll("%Player2%", p2);
		}
		
		if(Arena != null) {
		 if(msg.contains("%Arena%")) {
		  msg = msg.replaceAll("%Arena%", Arena);	
		 }
		}
		
		if(plugin.prefix != null && msg.contains("%Prefix%")) {
		 msg = msg.replaceAll("%Prefix%", "" + plugin.prefix);
		}
		
		if(msg.contains("&")) {
			msg = ChatColor.translateAlternateColorCodes('&', msg);
		}
		
		
		return msg;
	}
	
	public static String replaceStrings(String msg, String p, String Arena) {
		if(msg == null) {
			return null;
		}
		
		if(p != null && msg.contains("%Player%")) {
		 msg = msg.replaceAll("%Player%", p);
		}
		
		
		if(Arena != null) {
		 if(msg.contains("%Arena%")) {
		  msg = msg.replaceAll("%Arena%", Arena);	
		 }
		}
		
		if(plugin.prefix != null && msg.contains("%Prefix%")) {
		 msg = msg.replaceAll("%Prefix%", "" + plugin.prefix);
		}
		
		if(msg.contains("&")) {
			msg = ChatColor.translateAlternateColorCodes('&', msg);
		}
		
		
		return msg;
	}
	
	public static String replaceStrings(String msg, String p) {
		if(msg == null) {
			return null;
		}
		
		if(p != null && msg.contains("%Player%")) {
		 msg = msg.replaceAll("%Player%", p);
		}
		
		if(plugin.prefix != null && msg.contains("%Prefix%")) {
		 msg = msg.replaceAll("%Prefix%", "" + plugin.prefix);
		}
		
		if(msg.contains("&")) {
			msg = ChatColor.translateAlternateColorCodes('&', msg);
		}
		
		
		return msg;
	}
	
	public static String replaceStrings(String msg, String p, String p2, double Herzen) {
		if(msg == null) {
			return null;
		}
		
		if(p != null && msg.contains("%Player%")) {
		 msg = msg.replaceAll("%Player%", p);
		}
		if(p2 != null && msg.contains("%Player2%")) {
		 msg = msg.replaceAll("%Player2%", p2);
		}
		
		if(plugin.prefix != null && msg.contains("%Prefix%")) {
		 msg = msg.replaceAll("%Prefix%", "" + plugin.prefix);
		}
		
		if(msg.contains("%Hearths%")) {
			msg = msg.replaceAll("%Hearths%", "" + Herzen);
		}
		
		if(msg.contains("&")) {
			msg = ChatColor.translateAlternateColorCodes('&', msg);
		}
		
		
		return msg;
	}
	
	public static String replaceStringsKit(String msg, String p, String Kitauthor, String Prefs) {
		if(msg == null) {
			return null;
		}
		
		if(p != null && msg.contains("%Player%")) {
		 msg = msg.replaceAll("%Player%", p);
		}
		if(Kitauthor != null && msg.contains("%Author%")) {
		 msg = msg.replaceAll("%Author%", Kitauthor);
		}
		
		if(plugin.prefix != null && msg.contains("%Prefix%")) {
		 msg = msg.replaceAll("%Prefix%", "" + plugin.prefix);
		}
		
		if(msg.contains("%Prefs%")) {
			msg = msg.replaceAll("%Prefs%", Prefs);
		}
		
		if(msg.contains("&")) {
			msg = ChatColor.translateAlternateColorCodes('&', msg);
		}
		
		
		return msg;
	}
	
	public static String replaceStrings(String msg, String p, String p2, int Counter) {
		if(msg == null) {
			return null;
		}
		
		if(p != null && msg.contains("%Player%")) {
		 msg = msg.replaceAll("%Player%", p);
		}
		if(p2 != null && msg.contains("%Player2%")) {
		 msg = msg.replaceAll("%Player2%", p2);
		}
		
		if(plugin.prefix != null && msg.contains("%Prefix%")) {
		 msg = msg.replaceAll("%Prefix%", "" + plugin.prefix);
		}
		
		if(msg.contains("%Counter%")) {
			msg = msg.replaceAll("%Counter%", "" + Counter);
		}
		
		if(msg.contains("&")) {
			msg = ChatColor.translateAlternateColorCodes('&', msg);
		}
		
		
		return msg;
	}
	
	public static String replaceArenaName(String msg, String ArenaName) {
		if(msg == null) {
			return null;
		}
		
		if(ArenaName != null && msg.contains("%Arena%")) {
		 msg = msg.replaceAll("%Arena%", ArenaName);
		}
		
		if(msg.contains("&")) {
			msg = ChatColor.translateAlternateColorCodes('&', msg);
		}
		
		
		return msg;
	}
	
	public static String replaceArenaID(String msg, int ID) {
		if(msg == null) {
			return null;
		}
		
		if(msg.contains("%ID%")) {
		 msg = msg.replaceAll("%ID%", "" + ID);
		}
		
		if(msg.contains("&")) {
			msg = ChatColor.translateAlternateColorCodes('&', msg);
		}
		
		
		return msg;
	}

	public static String replaceArenaInfo(String msg, String Name, String Name2, String Kit, String Layout, String ArenaID) {
		if(msg == null) {
			return null;
		}
		
		if(msg.contains("%Player%")) {
		 msg = msg.replaceAll("%Player%", "" + Name);
		}
		
		if(msg.contains("%Player2%")) {
		 msg = msg.replaceAll("%Player2%", "" + Name2);
		}
		
		if(msg.contains("%Kit%")) {
		 msg = msg.replaceAll("%Kit%", "" + Kit);
		}
		
		if(msg.contains("%Layout%")) {
		 msg = msg.replaceAll("%Layout%", "" + Layout);
		}
		
		if(msg.contains("%Arena%")) {
		 msg = msg.replaceAll("%Arena%", "" + ArenaID);
		}
		
		if(msg.contains("&")) {
			msg = ChatColor.translateAlternateColorCodes('&', msg);
		}
		
		
		return msg;
	}
	
	
}
