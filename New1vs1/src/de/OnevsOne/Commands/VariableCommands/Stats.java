package de.OnevsOne.Commands.VariableCommands;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.OnevsOne.main;
import de.OnevsOne.Commands.VariableCommands.Manager.CommandTrigger1vs1;
import de.OnevsOne.MessageManager.MessageReplacer;
import net.md_5.bungee.api.ChatColor;

public class Stats implements Listener {

	private main plugin;

	public Stats(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onCommandExec(CommandTrigger1vs1 e) {		
		if(e.getCMD().equalsIgnoreCase("stats") && plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
			
		
			final Player p = (Player) e.getPlayer();
			final String[] args = e.getArgs();
			
			if(!p.hasPermission("1vs1.command.stats") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.User")) {
	    		plugin.sendNoPermsMessage(p);
	    		return;
	    	}
		 
		 
		 if(args.length == 0) {
			 Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
					
					@Override
					public void run() {
						int Wins = 0;
						 int Played = 0;
						 double KD = 0;
						 String Rank = "1";	
						 
						 
							 if(!plugin.getDBMgr().isConnected()) {
				 				p.sendMessage(plugin.msgs.getMsg("noMySQLConnection").replaceAll("%Prefix%", plugin.prefix));
				 				return;
				 			}
							 Wins = Integer.parseInt(plugin.getDBMgr().getStats(p.getUniqueId(), "FightsWon", false));
							 Played = Integer.parseInt(plugin.getDBMgr().getStats(p.getUniqueId(), "Fights", false));
							 
							 
							 double saveWins = Wins;
							 double saveLose = (Played-saveWins);
							 double KD30 = 0;
							 
							 if(saveLose == 0 && saveWins != 0) {
								 saveLose = 1;
							 }
							 
							 if(saveLose == 0 && saveWins == 0) {
								 KD = 0;
							 } else {
								 KD = saveWins/saveLose;
							 }
							 
							 
							
							 
							 DecimalFormat format = new DecimalFormat("#.##");
							 
							 
							 Rank = "" + plugin.getDBMgr().getPosition(p.getUniqueId(), false);
							 
							 if(Rank.equalsIgnoreCase("-1")) {
								 Rank = ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("error"));
							 }
						 
							 
							 int Wins30 = 0;
							 String Wins30S = plugin.getDBMgr().getStats(p.getUniqueId(), "FightsWon", true);
							 if(Wins30S != null) 
								Wins30 = Integer.parseInt(Wins30S);
							 
							 int Played30 = 0;
							 String Played30S = plugin.getDBMgr().getStats(p.getUniqueId(), "Fights", true);
							 if(Played30S != null) 
								 Played30 = Integer.parseInt(Played30S);
							 
							 
							 double saveWins30 = Wins30;
							 double saveLose30 = (Played30-Wins30);
							 
							 
							 if(saveLose30 == 0 && saveWins30 != 0) {
								 saveLose30 = 1;
							 }
							 
							 if(saveLose30 == 0 && saveWins30 == 0) 
								 KD30 = 0;
							  else 
								
								 KD30 = saveWins30/saveLose30;

							 String Rank30 = plugin.getDBMgr().getPosition(p.getUniqueId(), true) + "";
							 ;
							 
						 String[] data = plugin.msgs.getMsg("statsLayout").split("\n");
						 
						 for(int i = 0; i < data.length; i++) {
							 p.sendMessage(ChatColor.translateAlternateColorCodes('&', data[i])
										.replaceAll("%Played%", "" + Played).replaceAll("%Wins%", "" + Wins)
										.replaceAll("%Rank%", Rank)
										.replaceAll("%KD%", format.format(KD))
										.replaceAll("%Played30%", "" + Played30)
										.replaceAll("%Wins30%", "" + Wins30)
										.replaceAll("%Rank30%", Rank30)
										.replaceAll("%KD30%", format.format(KD30)));
						  }
							
						
					}
				});
		 } else
		 
		 if(args.length == 1) {
			 Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
					
					@Override
					public void run() {
						int Wins = 0;
						 int Played = 0;
						 double KD = 0;
						 double KD30 = 0;
						 String Rank = "1";	
						 
						 
							 if(!plugin.getDBMgr().isConnected()) {
				 				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
				 				return;
				 			}
							 if(plugin.getDBMgr().getUUID(args[0]) == null || !plugin.getDBMgr().isUserExists(plugin.getDBMgr().getUUID(args[0]))) {
									p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("playerNotFound")));
									return;
								}
							 Wins = Integer.parseInt(plugin.getDBMgr().getStats(plugin.getDBMgr().getUUID(args[0]), "FightsWon", false));
							 Played = Integer.parseInt(plugin.getDBMgr().getStats(plugin.getDBMgr().getUUID(args[0]), "Fights", false));
							 Rank = plugin.getDBMgr().getPosition(plugin.getDBMgr().getUUID(args[0]), false) + "";
							 if(Rank.equalsIgnoreCase("-1")) {
								 Rank = ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("error"));
							 }
						 
							 double saveWins = Wins;
							 double saveLose = (Played-saveWins);
							 
							 
							 if(saveLose == 0 && saveWins != 0) {
								 saveLose = 1;
							 }
							 
							 if(saveLose == 0 && saveWins == 0) {
								 KD = 0;
							 } else {
								
								 KD = saveWins/saveLose;
							 }
							 
							 DecimalFormat format = new DecimalFormat("#.##");
							 
							 
							 int Wins30 = 0;
							 String Wins30S = plugin.getDBMgr().getStats(plugin.getDBMgr().getUUID(args[0]), "FightsWon", true);
							 if(Wins30S != null) 
								Wins30 = Integer.parseInt(Wins30S);
							 
							 int Played30 = 0;
							 String Played30S = plugin.getDBMgr().getStats(plugin.getDBMgr().getUUID(args[0]), "Fights", true);
							 if(Played30S != null) 
								 Played30 = Integer.parseInt(Played30S);
							 
							 
							 double saveWins30 = Wins30;
							 double saveLose30 = (Played30-Wins30);
							 
							 
							 if(saveLose30 == 0 && saveWins30 != 0) {
								 saveLose30 = 1;
							 }
							 
							 if(saveLose30 == 0 && saveWins30 == 0) 
								 KD30 = 0;
							  else 
								
								 KD30 = saveWins30/saveLose30;
							 
							 DecimalFormat format30 = new DecimalFormat("#.##");
							 
							 
						 String[] data = plugin.msgs.getMsg("statsOtherLayout").split("\n");
						 
						 String Rank30 = plugin.getDBMgr().getPosition(plugin.getDBMgr().getUUID(args[0]), true) + "";
						 if(Rank.equalsIgnoreCase("-1")) {
							 Rank = ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("error"));
						 }
						 
						 
						 for(int i = 0; i < data.length; i++) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', data[i])
									.replaceAll("%Played%", "" + Played).replaceAll("%Wins%", "" + Wins)
									.replaceAll("%Rank%", Rank).replaceAll("%Player%", args[0])
									.replaceAll("%KD%", format.format(KD))
									.replaceAll("%Played30%", "" + Played30)
									.replaceAll("%Wins30%", "" + Wins30)
									.replaceAll("%Rank30%", Rank30).replaceAll("%Player30%", args[0])
									.replaceAll("%KD30%", format30.format(KD30)));
						 }
							
						
					}
				});
		 } else {
			 p.sendMessage("§cNutze: /stats [Name]");
		 }
		}
	}
	
	
}
