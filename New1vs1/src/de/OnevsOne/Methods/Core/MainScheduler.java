package de.OnevsOne.Methods.Core;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.OnevsOne.main;
import de.OnevsOne.Arena.Manager.RemoveEntitys;
import de.OnevsOne.Arena.Manager.ACS.Manager;
import de.OnevsOne.DataBases.MySQL.MySQLManager;
import de.OnevsOne.DataBases.SQLite.Database;
import de.OnevsOne.Kit_Methods.KitManager;
import de.OnevsOne.Kit_Methods.KitMessages;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.KitStands;
import de.OnevsOne.Methods.ScoreBoardManager;
import de.OnevsOne.Methods.SignMethods;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.Methods.configMgr;
import de.OnevsOne.Methods.openArenaCheckInv;
import de.OnevsOne.Methods.FightEnder.FightEnd;
import de.OnevsOne.Methods.FightEnder.FightEndTeam;
import de.OnevsOne.Methods.Messenger.Hotbar;
import de.OnevsOne.Methods.Mobs.spawnBlackDealer;
import de.OnevsOne.Methods.Mobs.spawnPrefVillager;
import de.OnevsOne.Methods.Mobs.spawnQueque;
import de.OnevsOne.Methods.Queue.QueueManager;
import de.OnevsOne.Methods.Tournament.FightEndManager;
import de.OnevsOne.Methods.Tournament.TournamentManager;
import de.OnevsOne.Methods.Tournament.Tournament_InvCreator;
import de.OnevsOne.States.OneVsOnePlayer;
import de.OnevsOne.States.PlayerPrefs;
import de.OnevsOne.States.PlayerState;
import de.OnevsOne.States.TournamentState;
import net.md_5.bungee.api.ChatColor;

/**
 * Der Code ist von JHammer
 *
 * 07.05.2016 um 12:20:24 Uhr
 */
public class MainScheduler {

	private static main plugin;
	
	@SuppressWarnings("static-access")
	public MainScheduler(main plugin) {
		this.plugin = plugin;
	}

	private static int checkArenas = 0;
	private static int ArenaMessage = 0;
	private static int reloadSigns = 0;
	private static int refreshTop5Signs = 30;
	private static int refreshJoinSigns = 2;
	private static int refreshKitStands = 0;
	private static int resetTimedStatsChecker = 0;
	
	private static int updateCheckInvs = 0;
	
	
	
	@SuppressWarnings("deprecation")
	public static void startMainSchedule() {
		
		
		
		checkArenas = plugin.ArenaCheckTimer;
		ArenaMessage = plugin.NoFreeArenaMessageTimer;
		reloadSigns = 60;
		refreshTop5Signs = 0;
		refreshJoinSigns = 2;
		refreshKitStands = 0;

		/*Scoreboard Task*/
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				for(OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) ScoreBoardManager.updateBoard(players.getPlayer(), false);
			}
		}, 0, 20);
		/*---------------*/
		
		
		
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				
				
				if(resetTimedStatsChecker == 0) {
					long current = System.currentTimeMillis();
					long saved = plugin.lastTimedStatReset;
					
					
					if(current>(saved+plugin.timedStatResetTime)) {
						System.out.println("Zeitliche Statistiken werden zurueckgesetzt...");
						
						YamlConfiguration cfg = plugin.defaultYML();
						cfg.set("config.lastTimedStatReset", (long)System.currentTimeMillis());
						try {
							plugin.timedStatResetTime = (long)System.currentTimeMillis();
							new configMgr(plugin).reloadData(true);
							plugin.getDBMgr().reset30DayStats();
						} catch (Exception e) {}
					}
					resetTimedStatsChecker = 3600;
				} else {
					resetTimedStatsChecker--;
				}
				
				if(Bukkit.getOnlinePlayers().size() == 0) return;
				
				
				
				
//				
						/*Schilder updaten*/
						
						if(refreshJoinSigns <= 0) {
							Bukkit.getScheduler().runTask(plugin, new Runnable() {
								
								@Override
								public void run() {
									new SignMethods(plugin).refreshJoinSigns();
									
								}
							});
							
							refreshJoinSigns = 2;
						} else {
							refreshJoinSigns--;
						}
						
						if(reloadSigns <= 0) {
							Bukkit.getScheduler().runTask(plugin, new Runnable() {
								
								@Override
								public void run() {
									new SignMethods(plugin).reloadJoinSigns();
								}
							});
							
						} else {
							reloadSigns--;
						}
						/*------*/
//						
						if(plugin.getOneVsOnePlayerSize() == 0) return;
						
						Bukkit.getScheduler().runTask(plugin, new Runnable() {
							
							@Override
							public void run() {
								if(!new KitStands(plugin).standsExits()) {
									new KitStands(plugin).spawnStands();
									new KitStands(plugin).fillStands();
								}
								if(refreshKitStands <= 0) {
									new KitStands(plugin).spawnStands();
									new KitStands(plugin).fillStands();
									refreshKitStands = 60;
								} else {
									refreshKitStands--;
								}
								
							}
						});
												
						if(refreshTop5Signs <= 0) {
							Bukkit.getScheduler().runTask(plugin, new Runnable() {
								
								@Override
								public void run() {
									if(plugin.getDBMgr().isConnected()) new SignMethods(plugin).refreshTop5();
								}
							});
							
							refreshTop5Signs = 30;
						} else {
							refreshTop5Signs--;
						}
						
						Bukkit.getScheduler().runTask(plugin, new Runnable() {
							
							@Override
							public void run() {
								spawnBlackDealer.respawner();
								spawnPrefVillager.respawner();
								spawnQueque.respawner();
							}
						});
						
				
				Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
					
					@Override
					public void run() {
						/*Arenen Checken lassen*/
						if(checkArenas <= 0) {
							plugin.getAState().checkAllArenas();
							checkArenas = plugin.ArenaCheckTimer;
						} else {
							checkArenas--;
						}
						
						/*-----------*/
						
						/*Checkinventare updaten*/
						if(updateCheckInvs <= 0) {
							
							
							
							for(OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) {
							 if(players.getPlayer().getOpenInventory() != null && players.getPlayer().getOpenInventory().getTitle().equalsIgnoreCase("ArenaCheck")) {
							  
							  openArenaCheckInv.resetArenaView(players.getPlayer(), players.getArenaView(), players.getPlayer().getOpenInventory());
							 } else {
								 players.setArenaView(null);
							 }
							}
							updateCheckInvs = 3;
						} else {
							
							updateCheckInvs--;
						}
						/*------*/
						
						/*Turnier Countdown*/
						FightEndManager endMgr = new FightEndManager(plugin);
						for(TournamentManager tMgr : plugin.tournaments.values()) {
							if(endMgr.tournamentEnded(tMgr)) continue;
							if(tMgr.getState() == TournamentState.STARTING && !tMgr.isStarted()) {
								
							 if(tMgr.getStartCounter() > 60 && tMgr.getStartCounter()%60 == 0) {
							  for(OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) {
							   if(players.getPlayertournament() != null && players.getPlayertournament() == tMgr.getOwnerUUID()) {
								players.getPlayer().sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentStartInfoMins").replaceAll("%Counter%", "" + tMgr.getStartCounter()/60)));
							   }
							  }
							 }
								
							 if(tMgr.getStartCounter() != 0 && tMgr.getStartCounter()%10 == 0 && tMgr.getStartCounter() <= 60) {
								 for(OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) {
								  if(players.getPlayertournament() != null && players.getPlayertournament() == tMgr.getOwnerUUID()) {
								   players.getPlayer().sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentStartInfoSecs").replaceAll("%Counter%", "" + tMgr.getStartCounter())));
							   }
							  }
							 }
							 
							 if(tMgr.getStartCounter() > 1 && tMgr.getStartCounter() <= 5) {
							   for(OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) {
								 if(players.getPlayertournament() != null && players.getPlayertournament() == tMgr.getOwnerUUID()) {
								   players.getPlayer().sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentStartInfoSecs").replaceAll("%Counter%", "" + tMgr.getStartCounter())));
							   }
							  }
							 }
							 if(tMgr.getStartCounter() == 1) {
							   for(OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) {
								 if(players.getPlayertournament() != null && players.getPlayertournament() == tMgr.getOwnerUUID()) {
								   players.getPlayer().sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentStartInfoSecs")).replaceAll("%Counter%", "" + tMgr.getStartCounter()));
							   }
							  }
							 }
							 if(tMgr.getStartCounter() <= 0) {
							  
							  if(tMgr.getRemainingPlayers() <= 1) {
								  
								  tMgr.setStarted(false);
								  tMgr.setState(TournamentState.WAITING);
								  for(Player players : tMgr.getPlayerList2()) {
									  players.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentNotEnougPlayers")));
								  }
								  return;
							  }
								 
								 
								 
							  for(OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) {
							   if(players.getPlayertournament() != null && players.getPlayertournament() == tMgr.getOwnerUUID()) {
								  players.getPlayer().sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentStarted")));
							   }
							  }
							  
							   tMgr.setStarted(true);
							   tMgr.setState(TournamentState.QUALLI);
							   
							   tMgr.setRound(1);
							   Tournament_InvCreator creator1 = new Tournament_InvCreator(plugin);
					           creator1.reGenerateInv(tMgr.getOwnerUUID());
							  
					           final TournamentManager fTmgr = tMgr;
					           
					           Bukkit.getScheduler().runTask(plugin, new Runnable() {
								
								@Override
								public void run() {
									fTmgr.joinAll();
									
								}
					           });
					           
							 }
							 
							 tMgr.setStartCounter(tMgr.getStartCounter()-1);
							 while(plugin.tournaments.containsKey(tMgr.getOwnerUUID())) plugin.tournaments.remove(tMgr.getOwnerUUID());
							 plugin.tournaments.put(tMgr.getOwnerUUID(), tMgr);
							}
							
						}
						/*------*/
					}
				});
				/*--------*/
				
				
				
				
				/*Check ob MySQL/SQL Connection vorhanden ist*/
				Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
					
					@Override
					public void run() {
						if(plugin.useMySQL) {
							plugin.connected = MySQLManager.isConnected();
						} else {
							plugin.connected = Database.isConnected();
						}
						
						
					}
				});
				/*-------*/

				/*Checken ob alle Spieler in ihrer Arena sind -> Schaden*/
				
				for(OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) { // Alle Spieler durchgehen
				 if(players.getArena() != null && players.getpState() == PlayerState.InArena) {
					 if(!plugin.getAState().isEnded(players.getArena())) { //Prüfen ob die Arena beendet ist
					  if(plugin.ArenaCorner1.containsKey(players.getArena()) && 
						 plugin.ArenaCorner2.containsKey(players.getArena())) { // Beide Corner Checken
					   if(!checkRegion(players.getPlayer().getLocation(), plugin.ArenaCorner1.get(players.getArena()),
							  plugin.ArenaCorner2.get(players.getArena()))) { //Check ob der Spieler in der Arena ist
							 
							 /*Spieler ist nicht in der Arena und erhält Schaden*/
							 players.getPlayer().sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaRegionLeft"), players.getPlayer().getDisplayName(), players.getArena()));
							 players.getPlayer().damage(plugin.ArenaRegionLeaveDamage);
						}
					   }
					 }
				 }
				 
					
				}
				/*---------*/
				
				/*Zeug mit allen Spielern & Arenen*/
				ArrayList<String> counted = new ArrayList<>();
				for(final OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) { // Alle Spieler durchgehen
				 if(players.getpState() == PlayerState.InArena) { //Prüfen ob Spieler in Arena
				  
					 
					 
				  String Arena = players.getArena(); //Arena des Spielers
				  
				  if(players.getPosition() == 1) { //Spieler ist Position 1 & somit wichtig
					  
					  /*Endmatch Counter manager*/
					  if(plugin.getAState().isEndMatch(Arena) && !plugin.getAState().isEnded(Arena)) {
					   int Counter = plugin.getAState().getEndMatch(Arena); //Aktueller Counter
					   
					   
					   /*Counter Message wird gesendet, wenn Counter größer 60 ist und durch 60 Teilbar*/
					   
					   if(Counter > 60 && Counter%60 == 0) {
						for(Player arenaP : plugin.ArenaPlayersP1.get(Arena)) {
						 //arenaP.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("endMatchCounterMin.replaceAll("%Timer%", "" + Counter).replaceAll("%TimerMin%", "" + Counter/60).replaceAll("%Prefix%", plugin.prefix)));
						 Hotbar.send(arenaP, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("endMatchCounterMin").replaceAll("%Timer%", "" + Counter).replaceAll("%TimerMin%", "" + Counter/60)));
						 SoundManager manager = new SoundManager(JSound.ORB_PLING, arenaP, 20, 1);
						 manager.play();
						}
						for(Player arenaP : plugin.ArenaPlayersP2.get(Arena)) {
							 //arenaP.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("endMatchCounterMin.replaceAll("%Timer%", "" + Counter).replaceAll("%TimerMin%", "" + Counter/60).replaceAll("%Prefix%", plugin.prefix)));
							 Hotbar.send(arenaP, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("endMatchCounterMin").replaceAll("%Timer%", "" + Counter).replaceAll("%TimerMin%", "" + Counter/60)));
							 SoundManager manager = new SoundManager(JSound.ORB_PLING, arenaP, 20, 1);
							 manager.play();
							}
					   }
					   
					   
					   /*Counter Message wird gesendet, wenn Counter durch 10 Teilbar ist und kleiner 60 ist*/
					   if(Counter%10 == 0 && Counter > 0 && Counter <= 60) {
						for(Player arenaP : plugin.ArenaPlayersP1.get(Arena)) {
						 //arenaP.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("endMatchCounter.replaceAll("%Timer%", "" + Counter).replaceAll("%Prefix%", plugin.prefix)));
						 Hotbar.send(arenaP, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("endMatchCounter").replaceAll("%Timer%", "" + Counter).replaceAll("%TimerMin%", "" + Counter/60)));
						 SoundManager manager = new SoundManager(JSound.ORB_PLING, arenaP, 20, 1);
						 manager.play();
						}
						for(Player arenaP : plugin.ArenaPlayersP2.get(Arena)) {
							 //arenaP.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("endMatchCounter.replaceAll("%Timer%", "" + Counter).replaceAll("%Prefix%", plugin.prefix)));
							 Hotbar.send(arenaP, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("endMatchCounter").replaceAll("%Timer%", "" + Counter).replaceAll("%TimerMin%", "" + Counter/60)));
							 SoundManager manager = new SoundManager(JSound.ORB_PLING, arenaP, 20, 1);
							 manager.play();
							}
					   }
					   /*------*/
					   
					   /*Sonstige Zahlen*/
					   if(Counter == 5 || Counter == 4 || Counter == 3 || Counter == 2) {
						for(Player arenaP : plugin.ArenaPlayersP1.get(Arena)) {
						 //arenaP.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("endMatchCounter.replaceAll("%Timer%", "" + Counter).replaceAll("%Prefix%", plugin.prefix)));
						 Hotbar.send(arenaP, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("endMatchCounter").replaceAll("%Timer%", "" + Counter).replaceAll("%TimerMin%", "" + Counter/60)));
						 SoundManager manager = new SoundManager(JSound.ORB_PLING, arenaP, 20, 1);
						 manager.play();
						}
						for(Player arenaP : plugin.ArenaPlayersP2.get(Arena)) {
						 Hotbar.send(arenaP, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("endMatchCounter").replaceAll("%Timer%", "" + Counter).replaceAll("%TimerMin%", "" + Counter/60)));
						 SoundManager manager = new SoundManager(JSound.ORB_PLING, arenaP, 20, 1);
						 manager.play();
						}
					   }
					   /*-----*/
					   
					   /*Zahl 1*/
					   if(Counter == 1) {
						for(Player arenaP : plugin.ArenaPlayersP1.get(Arena)) {
						 //arenaP.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("endMatchCounterSecond.replaceAll("%Prefix%", plugin.prefix)));
						 Hotbar.send(arenaP, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("endMatchCounterSecond")));
						 SoundManager manager = new SoundManager(JSound.ORB_PLING, arenaP, 20, 1);
						 manager.play();
						}
						for(Player arenaP : plugin.ArenaPlayersP2.get(Arena)) {
						 //arenaP.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("endMatchCounterSecond.replaceAll("%Prefix%", plugin.prefix)));
						 Hotbar.send(arenaP, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("endMatchCounterSecond")));
						 SoundManager manager = new SoundManager(JSound.ORB_PLING, arenaP, 20, 1);
						 manager.play();
						}
					   }
					   /*------*/
					   
					   
					   if(Counter == 0) {
						double distance1 = 0;
						double distance2 = 0;
						
						Player p1 = null;
						Player p2 = null;
						
						Location middle = plugin.getPositions().getArenaPos3(Arena);
						
						for(Player arenaP : plugin.ArenaPlayersP1.get(Arena)) {
						 
						  if(distance1 > arenaP.getLocation().distance(middle) || distance1 == 0) {
						   p1 = arenaP;
						   distance1 = arenaP.getLocation().distance(middle); 
						   continue;
						 }
						} 
						
						for(Player arenaP : plugin.ArenaPlayersP2.get(Arena)) {
							 
							  if(distance2 > arenaP.getLocation().distance(middle) || distance2 == 0) {
							   p2 = arenaP;
							   distance2 = arenaP.getLocation().distance(middle); 
							   continue;
							 }
							} 
						
						if(!p1.isOnline() || !p2.isOnline()) continue;
						
						if(!plugin.arenaTeams.containsKey(Arena)) {
							SoundManager managerP1 = new SoundManager(JSound.LEVEL, p1, 20.0F, 1.0F);
							SoundManager managerP2 = new SoundManager(JSound.LEVEL, p2, 20.0F, 1.0F);
							managerP1.play();
							managerP2.play();
							if(distance1 >= distance2) {
								FightEnd.EndGame(p2, p1, Arena);
							} else {
								FightEnd.EndGame(p1, p2, Arena);
							}
							
						} else {
							for(Player playersP1 : plugin.arenaTeams.get(Arena).get(0).getAll()) {
								SoundManager managerP1 = new SoundManager(JSound.LEVEL, playersP1, 20.0F, 1.0F);
								managerP1.play();
							}
							for(Player playersP2 : plugin.arenaTeams.get(Arena).get(1).getAll()) {
								SoundManager managerP2 = new SoundManager(JSound.LEVEL, playersP2, 20.0F, 1.0F);
								managerP2.play();
							}
							if(distance1 >= distance2) {
								FightEndTeam.EndGame(plugin.arenaTeams.get(Arena).get(1), plugin.arenaTeams.get(Arena).get(0), Arena);
							} else {
								FightEndTeam.EndGame(plugin.arenaTeams.get(Arena).get(0), plugin.arenaTeams.get(Arena).get(1), Arena);
							}
							
						}
						
						
						
						p1.hidePlayer(p2);
						p1.showPlayer(p2);
						p2.hidePlayer(p1);
						p2.showPlayer(p1);
						
					   }
					   
					   
					   if(!counted.contains(Arena)) {
						   Counter--;
						   counted.add(Arena);
						   plugin.getAState().setEndMatch(Arena, Counter);
					   }
					  
					  
					  }
					  /*-------*/
					  
					  /*Check ob die Arena aufgebaut ist*/
					  if(plugin.getAState().isReady(Arena)) {
						   if(plugin.getAState().checkState(Arena, "Started") != null && 
							   plugin.getAState().checkState(Arena, "Started").equalsIgnoreCase("false")) {
							   if(plugin.getAState().checkState(players.getArena(), "Counter") == null) {
								plugin.getRAMMgr().saveRAM(players.getArena(), "Counter", "" + plugin.ArenaStartTimer);
							   }
								  
								  int Counter = Integer.parseInt(plugin.getAState().checkState(players.getArena(), "Counter"));
								 
								  /*Wenn der Counter beginnt werden ALLE Kit-Einstellungen für die Arena übernommen und den Spieler gesendet*/
								  if(Counter == plugin.ArenaStartTimer) {
									  String sub = "d";
									  if(plugin.ArenaKit.containsKey(Arena) && plugin.ArenaKit.get(Arena).split(":").length == 2) {
										   sub = plugin.ArenaKit.get(Arena).split(":")[1];
									   }
									  if(players.getPlayer().isOnline() && players.getEnemy().isOnline()) {
									   if(plugin.ArenaKit.containsKey(Arena)) {
										   
										   if(plugin.arenaTeams.containsKey(Arena)) {
											   try {
												   for(Player playersP1 : plugin.arenaTeams.get(Arena).get(0).getAll()) {
													   KitMessages.sendAllPrefs(UUID.fromString(plugin.ArenaKit.get(Arena).split(":")[0]), playersP1,sub);
												   }
												   for(Player playersP1 : plugin.arenaTeams.get(Arena).get(1).getAll()) {
													   KitMessages.sendAllPrefs(UUID.fromString(plugin.ArenaKit.get(Arena).split(":")[0]), playersP1,sub);   
												   }
											   }catch (Exception e) {
												   for(Player playersP1 : plugin.arenaTeams.get(Arena).get(0).getAll()) {
													   KitMessages.sendAllPrefsCustomKit(plugin.ArenaKit.get(Arena).split(":")[0], playersP1);
												   }
												   for(Player playersP1 : plugin.arenaTeams.get(Arena).get(1).getAll()) {
													   KitMessages.sendAllPrefsCustomKit(plugin.ArenaKit.get(Arena).split(":")[0], playersP1);   
												   }
											   }
											   
											   
											   
										   } else {
											   try {
												   KitMessages.sendAllPrefs(UUID.fromString(plugin.ArenaKit.get(Arena).split(":")[0]), players.getPlayer(),sub);
												   KitMessages.sendAllPrefs(UUID.fromString(plugin.ArenaKit.get(Arena).split(":")[0]),  players.getEnemy(),sub);   
											   } catch (Exception e) {
												   KitMessages.sendAllPrefsCustomKit(plugin.ArenaKit.get(Arena).split(":")[0], players.getPlayer());
												   KitMessages.sendAllPrefsCustomKit(plugin.ArenaKit.get(Arena).split(":")[0],  players.getPlayer());   
											   }
											   
										   }
										   
									   }
									  }
									  
									  try {
										  new KitManager(plugin).setSettingsArena(UUID.fromString(plugin.ArenaKit.get(Arena).split(":")[0]), Arena,sub); 
									   } catch (Exception e) {
										   new KitManager(plugin).setSettingsArenaCustomKit(plugin.ArenaKit.get(Arena).split(":")[0], Arena); 
									   }
									 
									  
								  }
								  
								  String allPlayerP1 = "";
								  String allPlayerP2 = "";
								  if(plugin.arenaTeams.containsKey(Arena)) {
								  	    allPlayerP1 = plugin.arenaTeams.get(Arena).get(0).getTeamName(false);
								  	    allPlayerP2 = plugin.arenaTeams.get(Arena).get(1).getTeamName(false);
								  }
								 
								  /*1 Sekunde vorm Start*/
								  
								  if(Counter == 1) {
									  
									  if(plugin.arenaTeams.containsKey(Arena)) {
										  for(Player playersP1 : plugin.arenaTeams.get(Arena).get(0).getAll()) {
											  Hotbar.send(playersP1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("oneSecondBeforStart"), allPlayerP1, allPlayerP2, Arena));
											  SoundManager manager = new SoundManager(JSound.ORB_PLING, playersP1, 10.0F, 1.0F);
											  manager.play();
											  if(playersP1.isOnline() && plugin.getOneVsOnePlayer(playersP1).getArena() != null && /*TODO Players wurde zu playerP1 geändert*/playersP1.getLocation().distance(plugin.getPositions().getArenaPos1(plugin.getOneVsOnePlayer(playersP1).getArena())) >= 1) {
												  playersP1.teleport(plugin.getPositions().getArenaPos1(plugin.getOneVsOnePlayer(playersP1).getArena()));
												  playersP1.setAllowFlight(false);
												  playersP1.setFlying(false);
											  }
											  
										  }
										   for(Player playersP1 : plugin.arenaTeams.get(Arena).get(1).getAll()) {
											   Hotbar.send(playersP1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("oneSecondBeforStart"), allPlayerP2, allPlayerP1, Arena));
											   SoundManager manager = new SoundManager(JSound.ORB_PLING, playersP1, 10.0F, 1.0F);
												  manager.play();
												  if(playersP1.isOnline() && plugin.getOneVsOnePlayer(playersP1).getArena() != null && playersP1.getLocation().distance(plugin.getPositions().getArenaPos2(plugin.getOneVsOnePlayer(playersP1).getArena())) >= 1) {
													  playersP1.teleport(plugin.getPositions().getArenaPos1(plugin.getOneVsOnePlayer(playersP1).getArena()));
													  playersP1.setAllowFlight(false);
													  playersP1.setFlying(false);
												  }
												  
												  if(plugin.asyncMapReset) {
													  playersP1.getLocation().add(3000,200,-3000).getChunk().load();
													  playersP1.teleport(playersP1.getLocation().add(3000,+200,-3000));
													  
													  
												  }
										   }
									  } else {
										  Hotbar.send(players.getPlayer(), MessageReplacer.replaceStrings(plugin.msgs.getMsg("oneSecondBeforStart"), players.getPlayer().getDisplayName(), players.getEnemy().getDisplayName(), Arena));
										  Hotbar.send(players.getEnemy(), MessageReplacer.replaceStrings(plugin.msgs.getMsg("oneSecondBeforStart"), players.getEnemy().getDisplayName(), players.getPlayer().getDisplayName(), Arena));
										  
										  SoundManager manager = new SoundManager(JSound.ORB_PLING, players.getPlayer(), 10.0F, 1.0F);
										  manager.play();
										  manager = new SoundManager(JSound.ORB_PLING, players.getEnemy(), 10.0F, 1.0F);
										  manager.play();
										 
										  
										  if(players.getPlayer().isOnline() && players.getPlayer().getLocation().distance(plugin.getPositions().getArenaPos1(players.getArena())) >= 1) {
											  players.getPlayer().teleport(plugin.getPositions().getArenaPos1(players.getArena()));
											  players.getPlayer().setAllowFlight(false);
											  players.getPlayer().setFlying(false);
										  }
										  
										  Player enemie = players.getEnemy();
										  
										  if(enemie != null && enemie.isOnline() && enemie.getWorld().getName().equalsIgnoreCase(plugin.getPositions().getArenaPos2(plugin.getOneVsOnePlayer(enemie).getArena()).getWorld().getName()) && enemie.getLocation().distance(plugin.getPositions().getArenaPos2(plugin.getOneVsOnePlayer(enemie).getArena())) >= 1) {
											  enemie.teleport(plugin.getPositions().getArenaPos2(plugin.getOneVsOnePlayer(enemie).getArena())); 
										      enemie.setAllowFlight(false);
										      enemie.setFlying(false);
										  }
									  }
									  
									  
									  
								  /*X Sekunden vor dem Start*/
								  } else if(Counter > 1){
									  if(plugin.arenaTeams.containsKey(Arena)) {
										  for(Player playersP1 : plugin.arenaTeams.get(Arena).get(0).getAll()) {
											  Hotbar.send(playersP1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("fightStartCounter"), allPlayerP1, allPlayerP2, Counter));
											  SoundManager manager = new SoundManager(JSound.ORB_PLING, playersP1, 10.0F, 1.0F);
											  manager.play();
										  }
										  for(Player playersP1 : plugin.arenaTeams.get(Arena).get(1).getAll()) {
											  Hotbar.send(playersP1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("fightStartCounter"), allPlayerP2, allPlayerP1, Counter));
											  SoundManager manager = new SoundManager(JSound.ORB_PLING, playersP1, 10.0F, 1.0F);
											  manager.play();
										  }
										  RemoveEntitys.removeArenaEntitys(Arena,plugin.getPositions().getArenaPos1(players.getArena()).getWorld());
									  } else {
										  Hotbar.send(players.getPlayer(), MessageReplacer.replaceStrings(plugin.msgs.getMsg("fightStartCounter"), players.getPlayer().getDisplayName(), players.getEnemy().getDisplayName(), Counter));
										  Hotbar.send(players.getEnemy(), MessageReplacer.replaceStrings(plugin.msgs.getMsg("fightStartCounter"), players.getEnemy().getDisplayName(), players.getPlayer().getDisplayName(), Counter));
										  
										  SoundManager manager = new SoundManager(JSound.ORB_PLING, players.getPlayer(), 10.0F, 1.0F);
										  manager.play();
										  
										  manager = new SoundManager(JSound.ORB_PLING, players.getEnemy(), 10.0F, 1.0F);
										  manager.play();
										  
										  RemoveEntitys.removeArenaEntitys(Arena,plugin.getPositions().getArenaPos1(players.getArena()).getWorld());
									  }
									  
									 
									  
									  
								  /*Spiel beginnt*/
								  } else if(Counter <= 0) {
									  if(plugin.arenaTeams.containsKey(Arena)) {
										  for(final Player playersP1 : plugin.arenaTeams.get(Arena).get(0).getAll()) {
											  Hotbar.send(playersP1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("fightStart"), allPlayerP1, allPlayerP2));
											  SoundManager manager = new SoundManager(JSound.LEVEL, playersP1, 10.0F, 1.0F);
											  manager.play();
											  if(plugin.getAState().checkState(plugin.getOneVsOnePlayer(playersP1).getArena(), "Pref." + PlayerPrefs.DoubleJump) != null
														 && plugin.getAState().checkState(plugin.getOneVsOnePlayer(playersP1).getArena(),"Pref." + PlayerPrefs.DoubleJump).equalsIgnoreCase("true")) {
														  	
												  Bukkit.getScheduler().runTask(plugin, new Runnable() {
														
														@Override
														public void run() {
															playersP1.setAllowFlight(true);
													  		  playersP1.setFlying(false);
															  playersP1.setAllowFlight(true);
															  playersP1.setFlying(false);
														}
													});
												  
													  } else {
														  Bukkit.getScheduler().runTask(plugin, new Runnable() {
															
															@Override
															public void run() {
																playersP1.setAllowFlight(false);
																  playersP1.setFlying(false);
																  playersP1.setAllowFlight(false);
																  playersP1.setFlying(false);
															}
														});
																  
																  
													  }
											  
											  playersP1.updateInventory();
											  
											  
											  
											  Bukkit.getScheduler().runTask(plugin, new Runnable() {
												
												@Override
												public void run() {
													playersP1.teleport(plugin.getPositions().getArenaPos1(players.getArena()));
													for(OneVsOnePlayer all : plugin.getOneVsOnePlayersCopy().values()) {
														   for(OneVsOnePlayer p2 : plugin.getOneVsOnePlayersCopy().values()) {
															if(all.getPlayer().canSee(p2.getPlayer())) {
																all.getPlayer().hidePlayer(p2.getPlayer());
																all.getPlayer().showPlayer(p2.getPlayer());
															}
														   }
														  }
												}
											});
											  			
										  }
										  for(final Player playersP1 : plugin.arenaTeams.get(Arena).get(1).getAll()) {
											  Hotbar.send(playersP1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("fightStart"), allPlayerP2, allPlayerP1));
											  SoundManager manager = new SoundManager(JSound.LEVEL, playersP1, 10.0F, 1.0F);
											  manager.play();
											  
											  Bukkit.getScheduler().runTask(plugin, new Runnable() {
													
													@Override
													public void run() {
														 if(plugin.getAState().checkState(plugin.getOneVsOnePlayer(playersP1).getArena(), "Pref." + PlayerPrefs.DoubleJump) != null
																 && plugin.getAState().checkState(plugin.getOneVsOnePlayer(playersP1).getArena(),"Pref." + PlayerPrefs.DoubleJump).equalsIgnoreCase("true")) {
																  		  playersP1.setAllowFlight(true);
																  		  playersP1.setFlying(false);
																		  playersP1.setAllowFlight(true);
																		  playersP1.setFlying(false);
															  } else {
																		  playersP1.setAllowFlight(false);
																		  playersP1.setFlying(false);
																		  playersP1.setAllowFlight(false);
																		  playersP1.setFlying(false);
															  }	
													}
											  });
											 
											  
											  playersP1.updateInventory();

											  
											  
											  Bukkit.getScheduler().runTask(plugin, new Runnable() {
													
													@Override
													public void run() {
														playersP1.teleport(plugin.getPositions().getArenaPos2(players.getArena()));
														for(OneVsOnePlayer all : plugin.getOneVsOnePlayersCopy().values()) {
															   for(OneVsOnePlayer p2 : plugin.getOneVsOnePlayersCopy().values()) {
																if(all.getPlayer().canSee(p2.getPlayer())) {
																	all.getPlayer().hidePlayer(p2.getPlayer());
																	all.getPlayer().showPlayer(p2.getPlayer());
																}
															   }
															  }
													}
											  });
											  			
											  
										  } 
									  
										  plugin.getRAMMgr().saveRAM(players.getArena(), "Started", "true");
										  
										  /*Wenn Double-Jump aktiviert ist, können die Spieler fliegen*/
										  
										  if(plugin.autoEndmatch != -1 && !plugin.getAState().isEndMatch(players.getArena())) {  
											  if(players.getPlayertournament() == null) {
												  plugin.getAState().setEndMatch(players.getArena(), plugin.autoEndmatch);
											  }
										  }
										  
										  if(players.getPlayertournament() != null) {
											  
											  TournamentManager mgr = plugin.tournaments.get(players.getPlayertournament());
											  int secs;
											  if(mgr.getState() == TournamentState.QUALLI) {
												   secs = mgr.getMaxTimeQuMins()*60+mgr.getMaxTimeQuSecs();
											  } else {
												  secs = mgr.getMaxTimeKoMins()*60+mgr.getMaxTimeKoSecs();
											  }
											  plugin.getAState().setEndMatch(players.getArena(), secs);
											  
										  }
									  
									  } else {
										  
										  
										  
										  Hotbar.send(players.getPlayer(), MessageReplacer.replaceStrings(plugin.msgs.getMsg("fightStart"), players.getPlayer().getDisplayName(), players.getEnemy().getDisplayName()));
										  Hotbar.send(players.getEnemy(), MessageReplacer.replaceStrings(plugin.msgs.getMsg("fightStart"), players.getEnemy().getDisplayName(), players.getPlayer().getDisplayName()));
										 
										  players.getPlayer().updateInventory();
										  players.getEnemy().updateInventory();
										  
										  new SoundManager(JSound.LEVEL, players.getPlayer(), 10.0F, 1.0F).play();
										  
										  new SoundManager(JSound.LEVEL, players.getEnemy(), 10.0F, 1.0F).play();
										  
										  
										  plugin.getRAMMgr().saveRAM(players.getArena(), "Started", "true");
										  /*Wenn Double-Jump aktiviert ist, können die Spieler fliegen*/
										  if(plugin.getAState().checkState(players.getArena(), "Pref." + PlayerPrefs.DoubleJump) != null
										   && plugin.getAState().checkState(players.getArena(),"Pref." + PlayerPrefs.DoubleJump).equalsIgnoreCase("true")) {
											 
											  Bukkit.getScheduler().runTask(plugin, new Runnable() {
												
												@Override
												public void run() {
													  players.getPlayer().setAllowFlight(true);
													  players.getPlayer().setFlying(false);
													  players.getEnemy().setAllowFlight(true);
													  players.getEnemy().setFlying(false);
													
												}
											});
											  
										  } else {
											  Bukkit.getScheduler().runTask(plugin, new Runnable() {
													
													@Override
													public void run() {
														  players.getPlayer().setAllowFlight(false);
														  players.getPlayer().setFlying(false);
														  players.getEnemy().setAllowFlight(false);
														  players.getEnemy().setFlying(false);
													}
											  });
											  
										  }
										  if(plugin.autoEndmatch != -1 && !plugin.getAState().isEndMatch(players.getArena())) {  
											  if(players.getPlayertournament() == null) {
												  plugin.getAState().setEndMatch(players.getArena(), plugin.autoEndmatch);
											  }
										  }
										  

										  Bukkit.getScheduler().runTask(plugin, new Runnable() {
												
												@Override
												public void run() {
													players.getPlayer().teleport(plugin.getPositions().getArenaPos1(players.getArena()));
													players.getEnemy().teleport(plugin.getPositions().getArenaPos2(players.getArena()));
													for(OneVsOnePlayer all : plugin.getOneVsOnePlayersCopy().values()) {
														   for(OneVsOnePlayer p2 : plugin.getOneVsOnePlayersCopy().values()) {
															if(all.getPlayer().canSee(p2.getPlayer())) {
																all.getPlayer().hidePlayer(p2.getPlayer());
																all.getPlayer().showPlayer(p2.getPlayer());
															}
														   }
														  }
												}
										  });
										  
													
											
													if(players.getPlayertournament() != null) {
														  TournamentManager mgr = plugin.tournaments.get(players.getPlayertournament());
														  int secs;
														  if(mgr.getState() == TournamentState.QUALLI) {
															   secs = mgr.getMaxTimeQuMins()*60+mgr.getMaxTimeQuSecs();
														  } else {
															  secs = mgr.getMaxTimeKoMins()*60+mgr.getMaxTimeKoSecs();
														  }
														  plugin.getAState().setEndMatch(players.getArena(), secs);
														  
													  }
										  
										  
									  }
									  
									  
									  
									  
								  }
								  
								  if(!counted.contains(Arena)) {
									  if(Counter <= 0) continue;
									   Counter--;
									   counted.add(Arena);
									   
									   plugin.getRAMMgr().saveRAM(players.getArena(), "Counter", "" + Counter);
								  }
								  
								  
						   } else {
							/*Wenn der boolean Started nicht gefunden werden kann wird er automatisch auf "false" gesetzt*/
							if(plugin.getAState().checkState(Arena, "Started") == null) 
							 plugin.getRAMMgr().saveRAM(players.getArena(), "Started", "false");
							
						   }
						  } else {
						   /*Die Arena ist noch nicht aufgebaut und den Spielern in der Arena wird eine Nachricht gesendet*/  
							  final String fArena = Arena;
							  Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
									
									@Override
									public void run() {
										
										if(plugin.resetMgrArena.containsKey(players.getArena())) {
											 int max = plugin.resetMgrArena.get(players.getArena()).getMax();
											  int placed = plugin.resetMgrArena.get(players.getArena()).getPlaced();
											  
											  if(plugin.arenaTeams.containsKey(players.getArena())) {
												 
												  for(Player playersP1 : plugin.arenaTeams.get(fArena).get(0).getAll()) 
													  Hotbar.send(playersP1, plugin.colorByPercent(plugin.calcPerc(placed, max), 100, plugin.msgs.getMsg("arenaIsNotReadyYet").replaceAll("%perc%", "" + plugin.calcPerc(placed, max)), "§7", "§a"));
													  //hotbar.send(Bukkit.getPlayer(aplayers), plugin.colorByPercent(100, 100, "⬛⬛⬛⬛⬛⬛⬛⬛ Jump'n'Run lädt... ⬛⬛⬛ 100% ⬛⬛⬛⬛⬛⬛⬛⬛", "§7", "§a"));
													  
													  //Hotbar.send(playersP1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaIsNotReadyYet").replaceAll("%perc%", "" + plugin.calcPerc(placed, max)), playersP1.getDisplayName(), playersP1.getDisplayName(), fArena));
												  
												  for(Player playersP1 : plugin.arenaTeams.get(fArena).get(1).getAll()) {
													  Hotbar.send(playersP1, plugin.colorByPercent(plugin.calcPerc(placed, max), 100, plugin.msgs.getMsg("arenaIsNotReadyYet").replaceAll("%perc%", "" + plugin.calcPerc(placed, max)), "§7", "§a"));
													  //Hotbar.send(playersP1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaIsNotReadyYet").replaceAll("%perc%", "" + plugin.calcPerc(placed, max)), playersP1.getDisplayName(), playersP1.getDisplayName(), fArena));
												  }
											  } else {
												  Hotbar.send(players.getPlayer(), plugin.colorByPercent(plugin.calcPerc(placed, max), 100, plugin.msgs.getMsg("arenaIsNotReadyYet").replaceAll("%perc%", "" + plugin.calcPerc(placed, max)), "§7", "§a"));
												  Hotbar.send(players.getEnemy(), plugin.colorByPercent(plugin.calcPerc(placed, max), 100, plugin.msgs.getMsg("arenaIsNotReadyYet").replaceAll("%perc%", "" + plugin.calcPerc(placed, max)), "§7", "§a"));
												  //Hotbar.send(players, MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaIsNotReadyYet").replaceAll("%perc%", "" + plugin.calcPerc(placed, max)), players.getDisplayName(), players.getDisplayName(), fArena));
												  //Hotbar.send(plugin.Gegner.get(players), MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaIsNotReadyYet").replaceAll("%perc%", "" + plugin.calcPerc(placed, max)), players.getDisplayName(), players.getDisplayName(), fArena));
											  }	
										}
										
										 
									}
							  });
							  
						   
						   
						  }
				  }
				 }
				}
				/*-------*/
				
				
				/*Warteschlange wird auf zwei oder mehr Spieler geprüft*/
				int cPlayers = 0;
				for(@SuppressWarnings("unused") OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) {
					cPlayers++;
				}
						
				if(cPlayers >= 2) {
					if(ArenaMessage == 0) {
					 QueueManager.checkQueue(true);
					 ArenaMessage = plugin.NoFreeArenaMessageTimer;
					} else {
					 ArenaMessage--;
					 QueueManager.checkQueue(false);
					}
				}
				
				
				
			}
		}, 0, 20);
	}
	
	//checkRegion(Check, Min, Max): Prüft ob Location Check zwischen Min und Max liegt]
		public static boolean checkRegion(Location Check, Location Min, Location Max) {
			int minX,minY,minZ;
			int maxX,maxY,maxZ;
			
			minX = Math.min(Min.getBlockX(), Max.getBlockX());
			minY = Math.min(Min.getBlockY(), Max.getBlockY());
			minZ = Math.min(Min.getBlockZ(), Max.getBlockZ());
			
			maxX = Math.max(Min.getBlockX(), Max.getBlockX());
			maxY = Math.max(Min.getBlockY(), Max.getBlockY());
			maxZ = Math.max(Min.getBlockZ(), Max.getBlockZ());
			
			World w = Min.getWorld();
			
			Min = new Location(w, minX, minY, minZ);
			Max = new Location(w, maxX, maxY, maxZ);
			
			
			if (Min == null || Max == null) return false;

			if (Min.getBlockY() > Check.getBlockY() 
				|| Max.getBlockY() < Check.getBlockY()) return false;

			if (!Check.getWorld().getName()
				.equalsIgnoreCase(Min.getWorld().getName())) return false;

			int hx = Check.getBlockX();
			int hz = Check.getBlockZ();
			if (hx < Min.getBlockX()) return false;
			if (hx > Max.getBlockX()) return false;
			if (hz < Min.getBlockZ()) return false;
			if (hz > Max.getBlockZ()) return false;
				

			return true;

		}
	//--------------------------------------------------
	
		@SuppressWarnings("deprecation")
		public static void ACSScheduler() {
			final Manager ACSMgr = new Manager(plugin);
			
			Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
				
				@Override
				public void run() {
					for(String type : ACSMgr.getACSArenaTypes()) {					
						int i = ACSMgr.getFreeACSArenasType(type);
						
						
						
						
						if(i <= 0 && ACSMgr.getACSArenasType(type).size() < plugin.ACSMax) 
						 ACSMgr.generateNext(type);
						 
						
					}
					
				}
			}, 0, 3);
			
		}
		
		
}