package de.OnevsOne.Methods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

import de.OnevsOne.main;
import de.OnevsOne.Kit_Methods.KitManager;
import de.OnevsOne.Methods.Tournament.TournamentManager;
import de.OnevsOne.States.OneVsOnePlayer;
import de.OnevsOne.States.PlayerState;

public class ScoreBoardManager implements Listener {

	
	//private static HashMap<Player, Scoreboard> boards = new HashMap<Player, Scoreboard>();
	private static main plugin;
	
	
	
	@SuppressWarnings("static-access")
	public ScoreBoardManager(main plugin) {
		this.plugin = plugin;
		
	}
	
	HashMap<String, String> msgs = new HashMap<>();
	
	public static void loadMsgs() {
		
	}

	public static void updateBoard(final Player p, final boolean fromLobby) {
		Bukkit.getScheduler().runTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				if(!plugin.useScoreboard) return; 
				 if(!plugin.isInOneVsOnePlayers(p.getUniqueId())) return;
				 
				 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit || 
					plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby) updateBoardLobbyX(p);
				 else if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena || 
						 plugin.getOneVsOnePlayer(p).getpState() == PlayerState.Spec)updateBoardArenaX(p);
				 
				 Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
					
					@Override
					public void run() {
						
						if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit || 
						   plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby ||
						   plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena || 
						   plugin.getOneVsOnePlayer(p).getpState() == PlayerState.Spec)
						   reloadTeams();
						
					}
				});
			}
		});
		
		 
	}
	
	/**
	 * 
	 * Reloads all Tablist prefixes and suffixes
	 * 
	 */
	private static void reloadTeams() {
		/*Check ob in der Token Groups da ist*/
		if(plugin.getScoreboardData().getConfigurationSection("Groups") == null) return;
		
		 boolean found = false;
		 
		 /*Alle Spieler im 1vs1-Modus werden durch gegangen*/
		 for(final OneVsOnePlayer p : plugin.getOneVsOnePlayersCopy().values()) {
		   /*Alle Spieler die Online sind werden durch gegangen*/
		   for(final Player players : Bukkit.getOnlinePlayers()) { 
			   
			   boolean foundP = false;
			   /*Alle Gruppen werden durch gegangen*/
			   for(final String group : plugin.getScoreboardData().getConfigurationSection("Groups").getKeys(false)) {
				
				   
				   
				   
				/*Permissionsplugin Gruppe für die Gruppe wird geladen*/
				String permgroup = plugin.getScoreboardData().getString("Groups." + group + ".group");
				
				/*Prefix und Suffix wird geladen*/
				final String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getScoreboardData().getString("Groups." + group + ".prefix"));
				final String suffix = ChatColor.translateAlternateColorCodes('&', plugin.getScoreboardData().getString("Groups." + group + ".suffix"));
				
				/*Check, ob das Permissions Plugin aktiv ist*/
				if(plugin.permissionmgr != null && permgroup != null) {
					/*Liste aller Gruppen erstellt*/
					ArrayList<String> groupList = new ArrayList<String>(Arrays.asList(plugin.permissionmgr.getPlayerGroups(players)));
					
					/*Es wird geschaut, ob der Spieler in der entsprechenden Gruppe ist*/
					if(groupList.contains(permgroup)) {
						
						
						try {
							/*Spieler wird in Tablist hinzugefügt*/
							new BukkitRunnable() {
								
								@Override
								public void run() {
									if(p == null || p.getPlayer().getScoreboard() == null) return;
									if(p.getPlayer().getScoreboard().getObjectives() == null || p.getPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).getName() == null) return;
									if(plugin == null) return;
									plugin.scoreAPI.addTablistTeams(p.getPlayer(), p.getPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).getName(), group, prefix, players.getName(), suffix, plugin);
									
								}
							}.runTask(plugin);
						} catch (NullPointerException e) {}
						
						/*Wenn der Spieler der durchgangen wurde gleich den Hauptspieler ist
						 * wird er als gefunden markiert*/
						if(p.getPlayer().getUniqueId().equals(players.getUniqueId())) found = true;
						foundP = true;
						break;
					}
				 	
				}
			   }
			   
			   /*Wenn er gefunden wurde, kann abgebrochen werden*/
			   if(found) continue;
			   if(foundP) continue;
			   
			   /*Alle Gruppen werden durch gegangen*/
			   for(String group : plugin.getScoreboardData().getConfigurationSection("Groups").getKeys(false)) {
				    
				    /*Permission für die Gruppe wird geladen*/
				    String permission = plugin.getScoreboardData().getString("Groups." + group + ".permission");
					
				    /*Prefix und Suffix werden geladen*/
					String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getScoreboardData().getString("Groups." + group + ".prefix"));
					String suffix = ChatColor.translateAlternateColorCodes('&', plugin.getScoreboardData().getString("Groups." + group + ".suffix"));
					
					
				/*Wenn die Permission für diese Gruppe "null" ist, wird die nächste geladen*/
				if(permission == null) continue;
				
				/*Check, ob der Spieler die Permission hat*/
				if(players.hasPermission(permission)) {
					try {
						/*Spieler wird in das Scoreboard hinzugefügt*/
						plugin.scoreAPI.addTablistTeams(p.getPlayer(), p.getPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).getName(), group, prefix, players.getName(), suffix, plugin);
						/*Wenn der Spieler der durchgangen wurde gleich den Hauptspieler ist
						 * wird er als gefunden markiert*/
						if(p.getPlayer().getUniqueId().equals(players.getUniqueId())) found = true;
					} catch (NullPointerException e) {}
					break;
				}
				
			   }
			
				
			 }
		  
		   /*Check, ob der Spieler in eine Gruppe hinzugefügt wird*/
		   if(!found) {
			   /*Der Spieler wurde nicht gefunden und wird nun zur "Default" Gruppe hinzugefügt*/
			   /*Alle Gruppen werden durchgegangen*/
				for(String group : plugin.getScoreboardData().getConfigurationSection("Groups").getKeys(false)) {
				 /*Check, ob die Gruppe den Default Token hat*/
				 if(plugin.getScoreboardData().getBoolean("Groups." + group + ".default")) {
					 
					 /*Prefix und Suffix werden geladen*/
					 String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getScoreboardData().getString("Groups." + group + ".prefix"));
					 String suffix = ChatColor.translateAlternateColorCodes('&', plugin.getScoreboardData().getString("Groups." + group + ".suffix"));
						
					 try {
						 /*Alle Spieler im 1vs1-Modus werden durch gegangen*/
						 for(OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) 
							 /*Für alle Spieler im 1vs1-Modus wird der Spieler, welcher vorher
							  * keine Gruppe hatte, in die 1vs1-Gruppe hinzugefügt*/
							 plugin.scoreAPI.addTablistTeams(players.getPlayer(), players.getPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).getName(), group, prefix, p.getPlayer().getName(), suffix, plugin);
						 
						
					 } catch (NullPointerException e) {}
					 /*"Found" Token wird zurückgesetzt*/
					 found = false;
					 break;
				 }	 
				}
			
			}
		    /*"Found" Token wird zurückgesetzt*/
			found = false;
		 }
		}
	
	
	
	public static void removeBoard(Player p) {
		plugin.scoreAPI.removeBoard(p);
	}
	
	private static void updateBoardLobbyX(Player p) {
		if(p.getScoreboard().getObjective("Arena") != null) plugin.scoreAPI.removeBoard(p);
		
		String title = plugin.msgs.getMsg("scoreBoardLobbyTitle");
		
		if(plugin.getOneVsOnePlayer(p).getKitLoaded() != null && !plugin.getOneVsOnePlayer(p).getKitLoaded().equalsIgnoreCase("")) {
			String kit = plugin.getOneVsOnePlayer(p).getKitLoaded();
			if(kit.contains(":d")) kit = kit.replaceAll(":d", "");
			if(kit.contains(":1")) kit = kit.replaceAll(":1", "");
			title = plugin.msgs.getMsg("scoreBoardLobbyTitle").replaceAll("%Kit%", kit);
		} else {
			title = plugin.msgs.getMsg("scoreBoardLobbyTitle").replaceAll("%Kit%", p.getName());
		}
		
		plugin.scoreAPI.setDisplayName(p, "Lobby", ChatColor.translateAlternateColorCodes('&', title), plugin);
	
		
		String line3P = "";
		String line3S = "";
		
		String line6P = "";
		String line6S = "";
		
		if(plugin.getOneVsOnePlayer(p).isInQueue()) {
			line3P = plugin.msgs.getMsg("scoreBoardLobbyInQueue");
		 if(plugin.getOneVsOnePlayer(p).getChallanged().size() >= 1) {
			 line3S = plugin.msgs.getMsg("scoreBoardLobbyMore");
		 }
		} else {
			if(plugin.getOneVsOnePlayer(p).getChallanged().size() >= 1) {
				line3P = plugin.getOneVsOnePlayer(p).getChallanged().get(0).getDisplayName();
				if(plugin.getOneVsOnePlayer(p).getChallanged().size() > 1) {
				 line3S = plugin.msgs.getMsg("scoreBoardLobbyMore");	
				}
			} else {
				line3P = plugin.msgs.getMsg("scoreBoardLobbyNothing");
			}
		}
		
		
			if(plugin.getOneVsOnePlayer(p).getChallangedBy().size() >= 1) {
				line6P = plugin.getOneVsOnePlayer(p).getChallangedBy().get(0).getDisplayName();
				if(plugin.getOneVsOnePlayer(p).getChallangedBy().size() > 1) {
				 line6S = plugin.msgs.getMsg("scoreBoardLobbyMore");	
				}
			} else {
				line6P = plugin.msgs.getMsg("scoreBoardLobbyNothing");
			}
		
		
		
		
		if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line1")) 
		   plugin.scoreAPI.updateLine(p, "Lobby", "Line1", plugin.msgs.getMsg("scoreBoardLobbyLine1First").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), plugin.msgs.getMsg("scoreBoardLobbyLine1Middle"), plugin.msgs.getMsg("scoreBoardLobbyLine1Last").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), 16, plugin);
		else plugin.scoreAPI.addLine(p, "Lobby", "Line1", plugin.msgs.getMsg("scoreBoardLobbyLine1First").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), plugin.msgs.getMsg("scoreBoardLobbyLine1Middle"), plugin.msgs.getMsg("scoreBoardLobbyLine1Last").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), 16, plugin);
		
		if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line2")) 
		   plugin.scoreAPI.updateLine(p, "Lobby", "Line2", plugin.msgs.getMsg("scoreBoardLobbyLine2First").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), plugin.msgs.getMsg("scoreBoardLobbyLine2Middle"), plugin.msgs.getMsg("scoreBoardLobbyLine2Last").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), 15, plugin);
		else plugin.scoreAPI.addLine(p, "Lobby", "Line2", plugin.msgs.getMsg("scoreBoardLobbyLine2First").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), plugin.msgs.getMsg("scoreBoardLobbyLine2Middle"), plugin.msgs.getMsg("scoreBoardLobbyLine2Last").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), 15, plugin);
		
		if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line3"))
		   plugin.scoreAPI.updateLine(p, "Lobby", "Line3", plugin.msgs.getMsg("scoreBoardLobbyLine3First").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), plugin.msgs.getMsg("scoreBoardLobbyLine3Middle"), plugin.msgs.getMsg("scoreBoardLobbyLine3Last").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), 14, plugin);
		else plugin.scoreAPI.addLine(p, "Lobby", "Line3", plugin.msgs.getMsg("scoreBoardLobbyLine3First").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), plugin.msgs.getMsg("scoreBoardLobbyLine3Middle"), plugin.msgs.getMsg("scoreBoardLobbyLine3Last").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), 14, plugin);
		
		if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line4")) 
		   plugin.scoreAPI.updateLine(p, "Lobby", "Line4", plugin.msgs.getMsg("scoreBoardLobbyLine4First").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), plugin.msgs.getMsg("scoreBoardLobbyLine4Middle"), plugin.msgs.getMsg("scoreBoardLobbyLine4Last").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), 13, plugin);
		else plugin.scoreAPI.addLine(p, "Lobby", "Line4", plugin.msgs.getMsg("scoreBoardLobbyLine4First").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), plugin.msgs.getMsg("scoreBoardLobbyLine4Middle"), plugin.msgs.getMsg("scoreBoardLobbyLine4Last").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), 13, plugin);
		
		if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line5")) 
		   plugin.scoreAPI.updateLine(p, "Lobby", "Line5", plugin.msgs.getMsg("scoreBoardLobbyLine5First").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), plugin.msgs.getMsg("scoreBoardLobbyLine5Middle"), plugin.msgs.getMsg("scoreBoardLobbyLine5Last").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), 12, plugin);
		else plugin.scoreAPI.addLine(p, "Lobby", "Line5", plugin.msgs.getMsg("scoreBoardLobbyLine5First").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), plugin.msgs.getMsg("scoreBoardLobbyLine5Middle"), plugin.msgs.getMsg("scoreBoardLobbyLine5Last").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), 12, plugin);
		
		if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line6")) 
		   plugin.scoreAPI.updateLine(p, "Lobby", "Line6", plugin.msgs.getMsg("scoreBoardLobbyLine6First").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), plugin.msgs.getMsg("scoreBoardLobbyLine6Middle"), plugin.msgs.getMsg("scoreBoardLobbyLine6Last").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), 11, plugin);
		else plugin.scoreAPI.addLine(p, "Lobby", "Line6", plugin.msgs.getMsg("scoreBoardLobbyLine6First").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), plugin.msgs.getMsg("scoreBoardLobbyLine6Middle"), plugin.msgs.getMsg("scoreBoardLobbyLine6Last").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), 11, plugin);
		
		if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line7")) 
		   plugin.scoreAPI.updateLine(p, "Lobby", "Line7", plugin.msgs.getMsg("scoreBoardLobbyLine7First").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), plugin.msgs.getMsg("scoreBoardLobbyLine7Middle"), plugin.msgs.getMsg("scoreBoardLobbyLine7Last").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), 10, plugin);
		else plugin.scoreAPI.addLine(p, "Lobby", "Line7", plugin.msgs.getMsg("scoreBoardLobbyLine7First").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), plugin.msgs.getMsg("scoreBoardLobbyLine7Middle"), plugin.msgs.getMsg("scoreBoardLobbyLine7Last").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), 10, plugin);
		
		if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line8")) 
		   plugin.scoreAPI.updateLine(p, "Lobby", "Line8", plugin.msgs.getMsg("scoreBoardLobbyLine8First").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), plugin.msgs.getMsg("scoreBoardLobbyLine8Middle"), plugin.msgs.getMsg("scoreBoardLobbyLine8Last").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), 9, plugin);
		else plugin.scoreAPI.addLine(p, "Lobby", "Line8", plugin.msgs.getMsg("scoreBoardLobbyLine8First").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), plugin.msgs.getMsg("scoreBoardLobbyLine8Middle"), plugin.msgs.getMsg("scoreBoardLobbyLine8Last").replaceAll("%PlayersChallanged%", line3P).replaceAll("%AdvancedChallanged%", line3S).replaceAll("%PlayersChallangedBy%", line6P).replaceAll("%AdvancedChallangedBy%", line6S), 9, plugin);
		
		
		@SuppressWarnings("unchecked")
		ArrayList<String> starting = (ArrayList<String>) tournamentsStarting().clone();
		if(starting.size() == 0) {
			if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line9")) 
			   plugin.scoreAPI.updateLine(p, "Lobby", "Line9", plugin.msgs.getMsg("scoreBoardLobbyNothing"), plugin.msgs.getMsg("scoreBoardLobbyLine9Middle"), "", 8, plugin);
		    else plugin.scoreAPI.addLine(p, "Lobby", "Line9", plugin.msgs.getMsg("scoreBoardLobbyNothing"), plugin.msgs.getMsg("scoreBoardLobbyLine9Middle"), "", 8, plugin);
			
			if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line10")) plugin.scoreAPI.removeLine(p, "Lobby", "Line10", plugin);
			if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line11")) plugin.scoreAPI.removeLine(p, "Lobby", "Line11", plugin);
		} else if(starting.size() == 1) {
			if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line9")) 
				plugin.scoreAPI.updateLine(p, "Lobby", "Line9", plugin.msgs.getMsg("scoreBoardLobbyLine9First").replaceAll("%T1%", starting.get(0)), plugin.msgs.getMsg("scoreBoardLobbyLine9Middle"), "", 8, plugin);
			else plugin.scoreAPI.addLine(p, "Lobby", "Line9", plugin.msgs.getMsg("scoreBoardLobbyLine9First").replaceAll("%T1%", starting.get(0)), plugin.msgs.getMsg("scoreBoardLobbyLine9Middle"), "", 8, plugin);
			
			if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line10"))  plugin.scoreAPI.removeLine(p, "Lobby", "Line10", plugin);
			if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line11"))  plugin.scoreAPI.removeLine(p, "Lobby", "Line11", plugin);
		} else if(starting.size() == 2) {
			if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line9")) 
			   plugin.scoreAPI.updateLine(p, "Lobby", "Line9", plugin.msgs.getMsg("scoreBoardLobbyLine9First").replaceAll("%T1%", starting.get(0)).replaceAll("%T2%", starting.get(1)).replaceAll("%T2%", starting.get(2)), plugin.msgs.getMsg("scoreBoardLobbyLine9Middle"), "", 8, plugin);
			else plugin.scoreAPI.addLine(p, "Lobby", "Line9", plugin.msgs.getMsg("scoreBoardLobbyLine9First").replaceAll("%T1%", starting.get(0)).replaceAll("%T2%", starting.get(1)).replaceAll("%T2%", starting.get(2)), plugin.msgs.getMsg("scoreBoardLobbyLine9Middle"), "", 8, plugin);
			
			if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line10")) 
			   plugin.scoreAPI.updateLine(p, "Lobby", "Line10", plugin.msgs.getMsg("scoreBoardLobbyLine10First").replaceAll("%T1%", starting.get(0)).replaceAll("%T2%", starting.get(1)), plugin.msgs.getMsg("scoreBoardLobbyLine10Middle"), "", 7, plugin);
			else plugin.scoreAPI.addLine(p, "Lobby", "Line10", plugin.msgs.getMsg("scoreBoardLobbyLine10First").replaceAll("%T1%", starting.get(0)).replaceAll("%T2%", starting.get(1)), plugin.msgs.getMsg("scoreBoardLobbyLine10Middle"), "", 7, plugin);
			
			if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line11"))  plugin.scoreAPI.removeLine(p, "Lobby", "Line11", plugin);
		} else if(starting.size() >= 2) {
			if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line9")) 
			   plugin.scoreAPI.updateLine(p, "Lobby", "Line9", plugin.msgs.getMsg("scoreBoardLobbyLine9First").replaceAll("%T1%", starting.get(0)).replaceAll("%T2%", starting.get(1)).replaceAll("%T3%", starting.get(2)), plugin.msgs.getMsg("scoreBoardLobbyLine9Middle"), "", 8, plugin);
			else plugin.scoreAPI.addLine(p, "Lobby", "Line9", plugin.msgs.getMsg("scoreBoardLobbyLine9First").replaceAll("%T1%", starting.get(0)).replaceAll("%T2%", starting.get(1)).replaceAll("%T3%", starting.get(2)), plugin.msgs.getMsg("scoreBoardLobbyLine9Middle"), "", 8, plugin);
			
			if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line10")) 
			   plugin.scoreAPI.updateLine(p, "Lobby", "Line10", plugin.msgs.getMsg("scoreBoardLobbyLine10First").replaceAll("%T1%", starting.get(0)).replaceAll("%T2%", starting.get(1)).replaceAll("%T3%", starting.get(2)), plugin.msgs.getMsg("scoreBoardLobbyLine10Middle"), "", 7, plugin);
			else plugin.scoreAPI.addLine(p, "Lobby", "Line10", plugin.msgs.getMsg("scoreBoardLobbyLine10First").replaceAll("%T1%", starting.get(0)).replaceAll("%T2%", starting.get(1)).replaceAll("%T3%", starting.get(2)), plugin.msgs.getMsg("scoreBoardLobbyLine10Middle"), "", 7, plugin);
			
			if(plugin.scoreAPI.isLineExists(p, "Lobby", "Line11")) 
			   plugin.scoreAPI.updateLine(p, "Lobby", "Line11", plugin.msgs.getMsg("scoreBoardLobbyLine11First").replaceAll("%T1%", starting.get(0)).replaceAll("%T2%", starting.get(1)).replaceAll("%T3%", starting.get(2)), plugin.msgs.getMsg("scoreBoardLobbyLine11Middle"), "", 6, plugin);
			else plugin.scoreAPI.addLine(p, "Lobby", "Line11", plugin.msgs.getMsg("scoreBoardLobbyLine11First").replaceAll("%T1%", starting.get(0)).replaceAll("%T2%", starting.get(1)).replaceAll("%T3%", starting.get(2)), plugin.msgs.getMsg("scoreBoardLobbyLine11Middle"), "", 6, plugin);
		}
	}
	
	private static void updateBoardArenaX(Player p) {
		if(p.getScoreboard().getObjective("Lobby") != null) plugin.scoreAPI.removeBoard(p);
		
		String kit = "-";
		String Arena = "";
		//Bukkit.broadcastMessage("" + p.getName() + " " + plugin.getOneVsOnePlayer(p).getSpecator());
		if(plugin.getOneVsOnePlayer(p).getSpecator() != null && !plugin.getOneVsOnePlayer(p).getSpecator().equalsIgnoreCase("")) {
			
			try {
				
				if(plugin.ArenaKit.containsKey(plugin.getOneVsOnePlayer(p).getSpecator())) {
					Arena = plugin.getOneVsOnePlayer(p).getSpecator();
					kit = plugin.ArenaKit.get(plugin.getOneVsOnePlayer(p).getSpecator());
					if(kit != null) {
						String[] kitS = kit.split(":");
						
						UUID.fromString(kitS[0]);
						kit = new KitManager(plugin).getkitAuthor(kitS[0]);
						if(kitS.length >= 2) {
							kit = kit+":"+kitS[1];
						}
					}
				}
				
				
			} catch (Exception e) {
				
				if(kit != null) {
					
					 String[] kitS = kit.split(":");
					 kit = new StringBuilder(kitS[0]).append(":").append(kitS[1]).toString();
					
				}
				
				
			}
			String title = plugin.msgs.getMsg("scoreBoardArenaTitle").replaceAll("%Kit%", kit);
			if(title.length() >= 26) title = title.substring(0,26);
				
		} else {
			try {
				
				if(plugin.getOneVsOnePlayer(p).getArena() != null && !plugin.getOneVsOnePlayer(p).getArena().equalsIgnoreCase("") && plugin.ArenaKit.containsKey(plugin.getOneVsOnePlayer(p).getArena())) {
					kit = plugin.ArenaKit.get(plugin.getOneVsOnePlayer(p).getArena());
					Arena = plugin.getOneVsOnePlayer(p).getArena();
					if(kit != null) {
						String[] kitS = kit.split(":");
						
						UUID.fromString(kitS[0]);
						kit = new KitManager(plugin).getkitAuthor(kitS[0]);
						if(kitS.length >= 2) 
							kit = new StringBuilder(kit).append(":").append(kitS[1]).toString();
					}
				}
				
			} catch (Exception e) {
				
				if(kit != null) {
					String[] kitS = kit.split(":");
					kit = new StringBuilder(kitS[0]).append(":").append(kitS[1]).toString();
				}
				
			}
			
			String title = plugin.msgs.getMsg("scoreBoardArenaTitle").replaceAll("%Kit%", kit);
			if(title.length() >= 26) title = title.substring(0,26);
		}
		
		plugin.scoreAPI.setDisplayName(p, "Arena", "§7Kit: " + kit, plugin);
		
		if(!plugin.scoreAPI.isLineExists(p, "Arena", "Line1"))
			plugin.scoreAPI.addLine(p, "Arena", "Line1", "", "§0", "", 16, plugin);
		else plugin.scoreAPI.updateLine(p, "Arena", "Line1", "", "§0", "", 16, plugin);
		
		
		
		
		if(plugin.allPlayersArenaP1.containsKey(Arena) &&
		   plugin.allPlayersArenaP1.get(Arena).size() > 0 &&
		   plugin.allPlayersArenaP2.containsKey(Arena) &&
		   plugin.allPlayersArenaP2.get(Arena).size() > 0) {
			
			
			String pos1 = "§8-";
			String pos1_2 = "§8-";
			String pos1_3 = "§8-";
			
			String pos2 = "§8-";
			String pos2_2 = "§8-";
			String pos2_3 = "§8-";
			
			int tSize1 = plugin.allPlayersArenaP1.get(Arena).size();
			int tSize2 = plugin.allPlayersArenaP2.get(Arena).size();
			
			//Position 1
			
			if(tSize1 >= 1 && Bukkit.getPlayer(plugin.allPlayersArenaP1.get(Arena).get(0)) != null) {
				pos1 = Bukkit.getPlayer(plugin.allPlayersArenaP1.get(Arena).get(0)).getDisplayName();
			}
			
			if(tSize1 >= 2 && Bukkit.getPlayer(plugin.allPlayersArenaP1.get(Arena).get(1)) != null) {
				pos1_2 = Bukkit.getPlayer(plugin.allPlayersArenaP1.get(Arena).get(1)).getDisplayName();
			}
			
			if(tSize1 >= 3 && Bukkit.getPlayer(plugin.allPlayersArenaP1.get(Arena).get(2)) != null) {
				pos1_3 = Bukkit.getPlayer(plugin.allPlayersArenaP1.get(Arena).get(2)).getDisplayName();
			}
			
			//Position 2
			
			if(tSize2 >= 1 && Bukkit.getPlayer(plugin.allPlayersArenaP2.get(Arena).get(0)) != null) {
				pos2 = Bukkit.getPlayer(plugin.allPlayersArenaP2.get(Arena).get(0)).getDisplayName();
			}
			
			if(tSize1 >= 2 && Bukkit.getPlayer(plugin.allPlayersArenaP2.get(Arena).get(1)) != null) {
				pos2_2 = Bukkit.getPlayer(plugin.allPlayersArenaP2.get(Arena).get(1)).getDisplayName();
			}
			
			if(tSize1 >= 3 && Bukkit.getPlayer(plugin.allPlayersArenaP2.get(Arena).get(2)) != null) {
				pos2_3 = Bukkit.getPlayer(plugin.allPlayersArenaP2.get(Arena).get(2)).getDisplayName();
			}
			
			//---
			
			
			// 3 Linien für Team 1
			if(!plugin.scoreAPI.isLineExists(p, "Arena", "Line2"))
			 plugin.scoreAPI.addLine(p, "Arena", "Line2", "", "§1§c", "" + pos1, 15, plugin);
			else {
				if(!plugin.getAState().isOut(Arena, plugin.allPlayersArenaP1.get(Arena).get(0))) 
				  plugin.scoreAPI.updateLine(p, "Arena", "Line2", "", "§1§c", "" + pos1, 15, plugin);
				else 
				 plugin.scoreAPI.updateLine(p, "Arena", "Line2", "", "§1§8§m", "" + pos1, 15, plugin);
			}
			
			
			if(tSize1 >= 2) {
				if(!plugin.scoreAPI.isLineExists(p, "Arena", "Line3"))
					 plugin.scoreAPI.addLine(p, "Arena", "Line3", "", "§2§c", "" + pos1_2, 14, plugin);
				else {
					if(!plugin.getAState().isOut(Arena, plugin.allPlayersArenaP1.get(Arena).get(1))) 
					  plugin.scoreAPI.updateLine(p, "Arena", "Line3", "", "§2§c", "" + pos1_2, 14, plugin);
					else 
					 plugin.scoreAPI.updateLine(p, "Arena", "Line3", "", "§2§8§m", "" + pos1_2, 14, plugin);
				}
			}
			
			
			if(tSize1 >= 3) {
				if(!plugin.scoreAPI.isLineExists(p, "Arena", "Line4"))
					 plugin.scoreAPI.addLine(p, "Arena", "Line4", "", "§3§c", "" + pos1_3, 13, plugin);
				else {
					if(!plugin.getAState().isOut(Arena, plugin.allPlayersArenaP1.get(Arena).get(2))) 
					  plugin.scoreAPI.updateLine(p, "Arena", "Line4", "", "§3§c", "" + pos1_3, 13, plugin);
					else 
					 plugin.scoreAPI.updateLine(p, "Arena", "Line4", "", "§3§8§m", "" + pos1_3, 13, plugin);
				}
			}
			//---
			
			if(!plugin.scoreAPI.isLineExists(p, "Arena", "Line5"))
				 plugin.scoreAPI.addLine(p, "Arena", "Line5", "", "§f§lvs.", "", 12, plugin);
			else plugin.scoreAPI.updateLine(p, "Arena", "Line5", "", "§f§lvs.", "", 12, plugin);
			
			
			// 3 Linien für Team 2
				if(!plugin.scoreAPI.isLineExists(p, "Arena", "Line6"))
				 plugin.scoreAPI.addLine(p, "Arena", "Line6", "", "§4§9", "" + pos2, 11, plugin);
				else {
					if(!plugin.getAState().isOut(Arena, plugin.allPlayersArenaP2.get(Arena).get(0))) 
					  plugin.scoreAPI.updateLine(p, "Arena", "Line6", "", "§4§9", "" + pos2, 11, plugin);
					else 
					 plugin.scoreAPI.updateLine(p, "Arena", "Line6", "", "§4§8§m", "" + pos2, 11, plugin);
				}
				
				
				if(tSize2 >= 2) {
					if(!plugin.scoreAPI.isLineExists(p, "Arena", "Line7"))
						 plugin.scoreAPI.addLine(p, "Arena", "Line7", "", "§5§9", "" + pos2_2, 10, plugin);
					else {
						if(!plugin.getAState().isOut(Arena, plugin.allPlayersArenaP2.get(Arena).get(1))) 
						  plugin.scoreAPI.updateLine(p, "Arena", "Line7", "", "§5§9", "" + pos2_2, 10, plugin);
						else 
						 plugin.scoreAPI.updateLine(p, "Arena", "Line7", "", "§5§8§m", "" + pos2_2, 10, plugin);
					}
				}
				
				
				if(tSize2 >= 3) {
					if(!plugin.scoreAPI.isLineExists(p, "Arena", "Line8"))
						 plugin.scoreAPI.addLine(p, "Arena", "Line8", "", "§3§9", "" + pos2_3, 9, plugin);
					else {
						if(!plugin.getAState().isOut(Arena, plugin.allPlayersArenaP2.get(Arena).get(2))) 
						  plugin.scoreAPI.updateLine(p, "Arena", "Line8", "", "§3§9", "" + pos2_3, 9, plugin);
						else 
						 plugin.scoreAPI.updateLine(p, "Arena", "Line8", "", "§3§8§m", "" + pos2_3, 9, plugin);
					}
				}
			//---
			
			
			
		}
		
	}
	
	private static ArrayList<String> tournamentsStarting() {
		ArrayList<String> starting = new ArrayList<>();
		
		for(TournamentManager mgr : plugin.tournaments.values()) {
			if(mgr.getName().equalsIgnoreCase("null")) continue;
			if(!mgr.isStarted() && mgr.isOpened() && mgr.getPassword().equalsIgnoreCase("")) starting.add(mgr.getName());
		}
		
		return starting;
	}	
	
}
