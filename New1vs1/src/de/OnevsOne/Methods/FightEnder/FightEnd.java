package de.OnevsOne.Methods.FightEnder;


import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import de.OnevsOne.main;
import de.OnevsOne.Arena.Manager.ArenaJoin;
import de.OnevsOne.Arena.Manager.RemoveEntitys;
import de.OnevsOne.Arena.Reseter.ResetMethoden;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.ScoreBoardManager;
import de.OnevsOne.Methods.getItems;
import de.OnevsOne.Methods.moneyTitles;
import de.OnevsOne.Methods.Messenger.TitleAPI;
import de.OnevsOne.Methods.Queue.QueueManager;
import de.OnevsOne.Methods.Tournament.FightEndManager;
import de.OnevsOne.States.OneVsOnePlayer;
import de.OnevsOne.States.PlayerBestOfsPrefs;
import de.OnevsOne.States.PlayerState;
import net.md_5.bungee.api.ChatColor;

/**
 * Der Code ist von JHammer
 *
 * 07.05.2016 um 15:55:14 Uhr
 */

/*
 * In dieser Klase wird das Ende jedes Kampfes eingeleitet!
 * 
 * Methoden:
 * 
 * EndGame(Player Winner, Player Loser, String Arena); //Leitet das Ende eines Kampfes ein.
 * resetPlayer(Player p, boolean clear); //Löscht alle Spielerdaten.
 * addWarteschlange(final Player p); //Fügt Spieler nach 3 Sekunden zur Warteschlange hinzu.
 * 
 * 
 */
public class FightEnd {
     
	private static main plugin;
	@SuppressWarnings("static-access")
	public FightEnd(main plugin) {
		this.plugin = plugin;
	}
    
	
	public static void EndGame(final Player Winner, final Player Loser, final String Arena) {
		
		boolean wasTournament = false;
		
		//Set ArenaState to Ended
		if(plugin.getAState().checkState(Arena, "Ended") == null 
			|| plugin.getAState().checkState(Arena, "Ended").equalsIgnoreCase("false")) {
			plugin.getRAMMgr().saveRAM(Arena, "Ended", "true");
		} else return;
		//-------
		
		/**/
		
		

		//
		
		plugin.getRAMMgr().saveRAM(Arena, "Out." + Loser.getUniqueId(), true);
		
		ScoreBoardManager.updateBoard(Loser, false);
		ScoreBoardManager.updateBoard(Winner, false);
		
		
		//Remove ArenaKit and Entitys
		
		plugin.ArenaPlayersP1.remove(Arena);
		plugin.ArenaPlayersP2.remove(Arena);
		
		
		
		
		while(plugin.arenaTeams.containsKey(Arena)) plugin.arenaTeams.remove(Arena);
		//---------------------------
		
		
		//Herzen berechnen
		
		double health = 0;
  	    double live = Winner.getHealth();//HEALTH
  	  
  	    if(live%2 != 0) {
  		 for(int i = 0; i < (live-1)/2;i++) health++;
  		 health += 0.5;
  	     } else {
  		 for(int i = 0; i < (live-1)/2;i++) health++;
  	    }
		//-------------
		
		
		//Nachrichten senden
		Winner.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("fightWon"), Winner.getDisplayName(), Loser.getDisplayName(), health));
		Loser.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("fightLost"), Loser.getDisplayName(), Winner.getDisplayName(), health));
		//------------------
		
		int delay = 4*20;
		if(!plugin.voidTeleport) delay = 2*20;
		
		int rDelay = delay+10;
	
		if(plugin.getOneVsOnePlayer(Winner).getPlayertournament() != null && plugin.getOneVsOnePlayer(Loser).getPlayertournament() != null) {
			wasTournament = true;
		}
		
		//Nach Verögerung Arena zum Löschen freigeben
		
		if(!wasTournament) {
			
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				
				
				@Override
				public void run() {
					
				 RemoveEntitys.removeArenaEntitys(Arena, plugin.getPositions().getArenaPos1(Arena).getWorld());
				 
				 
				 plugin.getRAMMgr().deleteRAM(Arena);
				 plugin.getAState().checkArena(Arena);
				 
				 plugin.EntityCount.remove(Arena);
				 plugin.Entitys.remove(Arena);
				 
				 new ResetMethoden(plugin).resetArena(Arena);
				}
			}, rDelay);
		}
		
		
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			
			@Override
			public void run() {
				while(plugin.allPlayersArenaP1.containsKey(Arena))
					plugin.allPlayersArenaP1.remove(Arena);
				while(plugin.allPlayersArenaP2.containsKey(Arena))
					plugin.allPlayersArenaP2.remove(Arena);
				
				plugin.getOneVsOnePlayer(Winner).setArena(null);
				plugin.getOneVsOnePlayer(Loser).setArena(null);
				
				plugin.ArenaKit.remove(Arena);
				
			}
		}, delay);
		
		
		//--------------
		
		
		
	 	
	 /*Bestof System*/
	 
		boolean moreFights = false;
		 boolean wasBestOf = false;
		 
		 String[] Datas = {};
		 boolean freeArena = false;
		 
		 
		 if(wasTournament) {
			 moneyTitles.sendTournamentKill(Winner, plugin);
		 } else {
			 moneyTitles.send(Winner, plugin);
		 }
		 
		
	 if(!wasTournament) {
		 
	 if(Winner.isOnline() && Loser.isOnline() 
		&& plugin.isInOneVsOnePlayers(Winner.getUniqueId()) && plugin.isInOneVsOnePlayers(Loser.getUniqueId())) {
		 if(plugin.BestOfSystem.containsKey(Winner.getUniqueId())) {
		  if(plugin.BestOfSystem.get(Winner.getUniqueId()).length >= 3) {
			  
				 UUID use = Winner.getUniqueId();
				 Datas = plugin.BestOfSystem.get(use);
				
				 if(!Datas[2].equalsIgnoreCase("1")) {
					  wasBestOf = true;
					  
					  int Player1 = Integer.parseInt(Datas[3]);
					  int Player2 = Integer.parseInt(Datas[4]);
					  
					  int Games = Player1+Player2;
					  Games++;
					  
					  	  /*Abfrage, ob es mehr Kämpfe geben wird*/
						  moreFights = true;
						  if(Datas[2].equalsIgnoreCase("" + Games)) {
						   moreFights = false;
						  }
						  /*-----*/
						  
						  /*Abfrage, wieviele Wins gemacht werden müssen, bis ein klarer Gewinner fest steht*/
						  int minGames = 2;
						  if(Datas[2].equalsIgnoreCase("5")) {
							  minGames = 3;
						  }
						  /*-----*/
						 
						  				  
						  
						  if(plugin.FreeArenas.size() == 0) freeArena = true;
							  UUID pos1 = UUID.fromString(Datas[1]);
							  
							  if(pos1.toString().equalsIgnoreCase(Loser.getUniqueId().toString())) {
								//Gewinner ist Pos2
								int wins = Integer.parseInt(Datas[3]);
								wins++;
								Datas[3]= "" + wins;
								
								plugin.BestOfSystem.remove(use);
								plugin.BestOfSystem.put(use, Datas);
							  } else {
								//Gewinner Pos1
								int wins = Integer.parseInt(Datas[4]);
								wins++;
								Datas[4] = "" + wins;
								
								plugin.BestOfSystem.remove(use);
								plugin.BestOfSystem.put(use, Datas);
							  }
							  
							  /*Abfrage, ob ein gewinner bereits feststeht*/
							  if(!plugin.playAllBestOfGames) {
								  if(plugin.BestOfSystem.get(use)[4].equalsIgnoreCase("" + minGames)) {
									  moreFights = false;
								  }	
								  
								  if(plugin.BestOfSystem.get(use)[3].equalsIgnoreCase("" + minGames)) {
									  moreFights = false;
								  }	
							  }
							  /*-----*/
							  
							  /*Abfrage ob mehr Kämpfe gemacht werden müssen*/
							  if(!moreFights) {
								  plugin.BestOfSystem.remove(use);
							  }
							 
						  
						  
				 } else {
					 /*User wird aus dem BestOfSystem entfernt*/
					 plugin.BestOfSystem.remove(use);
				 }
		  }

		 } else {
			 if(plugin.BestOfSystem.containsKey(Loser.getUniqueId()) && plugin.BestOfSystem.get(Loser.getUniqueId()).length >= 3) {
				 UUID use = Loser.getUniqueId();
				 Datas = plugin.BestOfSystem.get(use);
				 if(!Datas[2].equalsIgnoreCase("1")) {
				  wasBestOf = true;
				  int Player1 = Integer.parseInt(Datas[3]);
				  int Player2 = Integer.parseInt(Datas[4]);
				  int Games = Player1+Player2;
				  Games++;
				  
				  
					  moreFights = true;
					  if(Datas[2].equalsIgnoreCase("" + Games)) {
					   moreFights = false;
					  }
					  
					  /*Abfrage, wieviele Wins gemacht werden müssen, bis ein klarer Gewinner fest steht*/
					  int minGames = 2;
					  if(Datas[2].equalsIgnoreCase("5")) {
						  minGames = 3;
					  }
					  /*-----*/
					  
					  if(plugin.FreeArenas.size() == 0) freeArena = true;
					  
						  UUID pos1 = UUID.fromString(Datas[1]);
						  //UUID pos2 = UUID.fromString(Datas[2]);
						  
						  if(pos1.toString().equalsIgnoreCase(Loser.getUniqueId().toString())) {
							//Gewinner ist Pos2
							int wins = Integer.parseInt(Datas[3]);
							wins++;
							Datas[3]= "" + wins;
							plugin.BestOfSystem.remove(use);
							plugin.BestOfSystem.put(use, Datas);
						  } else {
							//Gewinner Pos1
							int wins = Integer.parseInt(Datas[4]);
							wins++;
							Datas[4] = "" + wins;
							plugin.BestOfSystem.remove(use);
							plugin.BestOfSystem.put(use, Datas);
						  }
						  
						  /*Abfrage, ob ein gewinner bereits feststeht*/
						  if(!plugin.playAllBestOfGames) {
							  if(plugin.BestOfSystem.get(use)[4].equalsIgnoreCase("" + minGames)) {
								  moreFights = false;
							  }	
							  
							  if(plugin.BestOfSystem.get(use)[3].equalsIgnoreCase("" + minGames)) {
								  moreFights = false;
							  }	
						  }
						  /*-----*/
						  
						  if(!moreFights) {
							  plugin.BestOfSystem.remove(use);
						  }
						 
				  
				  
				  
				 
				 } else {
					 plugin.BestOfSystem.remove(use);
				 }
			 }

		 }
	 }
	 
	 final boolean moreFights2 = moreFights;
	 final String[]  Datas2 = Datas;
	 final boolean wasBestOf2 = wasBestOf;
	 
	 Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			
			@SuppressWarnings("unused")
			@Override
			public void run() {
				
				if(!moreFights2 && wasBestOf2) {
				 UUID pos1 = UUID.fromString(Datas2[0]);
				 UUID pos2 = UUID.fromString(Datas2[1]);
				  
				 int wins1 = Integer.parseInt(Datas2[3]);
				 int wins2 = Integer.parseInt(Datas2[4]);
				 
				 if(wins1 > wins2) {
					 /*Spieler 1 hat gewonnen*/
					 PlayerBestOfsPrefs pref = PlayerBestOfsPrefs.BestOf3;
					 int prefGame = Integer.parseInt(Datas2[2]);
					 String BestofType = ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("bestOf3Name"));
					 
					 
					 if(prefGame == 3){
						 pref = PlayerBestOfsPrefs.BestOf3;
						 BestofType = ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("bestOf3Name"));
					 } else if(prefGame == 5) {
						 pref = PlayerBestOfsPrefs.BestOf5;
						 BestofType = ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("bestOf5Name"));
					 }
					 
					 TitleAPI.sendTitle(Bukkit.getPlayer(pos1), 10, 20*2, 10, BestofType, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("bestOfWon")).replaceAll("%Wins%", "" + wins1));
					 TitleAPI.sendTitle(Bukkit.getPlayer(pos2), 10, 20*2, 10, BestofType, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("bestOfLost")).replaceAll("%Wins%", "" + wins2));
				 } else {
					 /*Spieler 2 hat gewonnen*/
					 PlayerBestOfsPrefs pref = PlayerBestOfsPrefs.BestOf3;
					 int prefGame = Integer.parseInt(Datas2[2]);
					 String BestofType = "§cBest of 3";
					 
					 
					 if(prefGame == 3){
						 pref = PlayerBestOfsPrefs.BestOf3;
						 BestofType = ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("bestOf3Name"));
					 } else if(prefGame == 5) {
						 pref = PlayerBestOfsPrefs.BestOf5;
						 BestofType = ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("bestOf5Name"));
					 }
					 
					 TitleAPI.sendTitle(Bukkit.getPlayer(pos1), 10, 20*2, 10, BestofType, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("bestOfLost")).replaceAll("%Wins%", "" + wins1));
					 TitleAPI.sendTitle(Bukkit.getPlayer(pos2), 10, 20*2, 10, BestofType, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("bestOfWon")).replaceAll("%Wins%", "" + wins2));
				 }
				
			    }
			}
	  }, delay+20);
	 }
	 
	 /*Spectator aus der Arena teleportieren*/
	 for(final OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) {
	  if(players.getpState() == PlayerState.Spec) {
	   if(players.getSpecator() != null && players.getSpecator().equalsIgnoreCase(Arena)) {
		players.getPlayer().sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("fightSpec"), Loser.getDisplayName(), Winner.getDisplayName(), health));
		
		if(players.getPlayertournament() != null) continue;
	    
	    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			
			@Override
			public void run() {
				if(players.getPlayer().isOnline() && plugin.isInOneVsOnePlayers(players.getPlayer().getUniqueId())) {
					
					if(players.getPlayertournament() == null) {
						plugin.getTeleporter().teleportMainSpawn(players.getPlayer());
					    players.getPlayer().getInventory().clear();
					    players.getPlayer().getInventory().setArmorContents(null);
					    players.getPlayer().setFoodLevel(20);
					    players.getPlayer().setMaxHealth(20);
					    players.getPlayer().setHealth(20);
					    players.getPlayer().setAllowFlight(false);
					    players.getPlayer().setFlying(false);
					    players.getPlayer().spigot().setCollidesWithEntities(true);
					    for(PotionEffect effect : players.getPlayer().getActivePotionEffects()) players.getPlayer().removePotionEffect(effect.getType());
					    getItems.getLobbyItems(players.getPlayer(), true);
					    for(Player Online : Bukkit.getOnlinePlayers()) Online.showPlayer(players.getPlayer());
					    
					    players.setSpecator(null);
					    players.setpState(PlayerState.InLobby);
					    
						ScoreBoardManager.updateBoard(players.getPlayer(), false);
						    
					}
					
				}
			}
		  }, delay);
	    
	    
	    
	   }
	  }
	 }
	 /*-----*/
	 
	 /*Loser wird teleportiert*/
	 if(Loser.isOnline() && plugin.isInOneVsOnePlayers(Loser.getUniqueId())) {
	  
	
	  /*Loser kommt in die Lobby*/
	  final boolean nfreeArena = freeArena;
	  final boolean nwasTournament = wasTournament;
	  final String[]  Datas2 = Datas;
	  final boolean moreFights2 = moreFights;
	  Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				
		@SuppressWarnings("unused")
		@Override
		public void run() {
		 
		 Loser.setFallDistance(0);
		 Loser.setFallDistance(0);		
		
		 
		 if(!nwasTournament) {
			 resetPlayer(Loser, true);
				  if(moreFights2 && Winner.isOnline() && Loser.isOnline() && 
						  plugin.isInOneVsOnePlayers(Winner.getUniqueId()) && plugin.isInOneVsOnePlayers(Loser.getUniqueId())) {
					  PlayerBestOfsPrefs pref = PlayerBestOfsPrefs.BestOf3;
						 int prefGame = Integer.parseInt(Datas2[2]);
						 int Wins1 = 0;
						 int Wins2 = 0;
						 String BestofType = ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("bestOf3Name"));
						 
						 
						 if(prefGame == 3){
							 pref = PlayerBestOfsPrefs.BestOf3;
							 BestofType = ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("bestOf3Name"));
						 } else if(prefGame == 5) {
							 pref = PlayerBestOfsPrefs.BestOf5;
							 BestofType = ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("bestOf5Name"));
						 } else {
							 plugin.getTeleporter().teleportMainSpawn(Loser);
							 
							 ScoreBoardManager.updateBoard(Loser, false);
							  getItems.getLobbyItems(Loser, true);
							  
							  if(plugin.getOneVsOnePlayer(Loser).isWasInQueue()) {
							   TitleAPI.sendTitle(Loser, 10, 20*2, 10, MessageReplacer.replaceStrings(plugin.msgs.getMsg("autoQuequeInfoL1"), Loser.getDisplayName()), MessageReplacer.replaceStrings(plugin.msgs.getMsg("autoQuequeInfoL2"), Loser.getDisplayName()));
							   addQueue(Loser); 
							   Winner.hidePlayer(Loser);
								  Loser.hidePlayer(Winner);
								  Winner.showPlayer(Loser);
								  Loser.showPlayer(Winner);
							  }
						 }
						 
						 Wins1 = Integer.parseInt(Datas2[3]);
						 Wins2 = Integer.parseInt(Datas2[4]);
						 
						 String Name1 = Bukkit.getPlayer(UUID.fromString(Datas2[0])).getDisplayName();
						 String Name2 = Bukkit.getPlayer(UUID.fromString(Datas2[1])).getDisplayName();
						 
						 String sub = ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("bestOfInfo")).replaceAll("%Player1%", Name1).replaceAll("%Wins1%", "" + Wins1).replaceAll("%Player2%", Name2).replaceAll("%Wins2%", "" + Wins2);
						 
						 TitleAPI.sendTitle(Loser, 20, 20*2, 20, BestofType, sub);
						 Loser.getInventory().clear();
						 
						 
						
						 
						 if(plugin.FreeArenas.size() > 0 || nfreeArena) {
							 
							 String Kit = Datas2[5];
							 
							 
							 
							 
							 if(QueueManager.getRandomMap(Bukkit.getPlayer(UUID.fromString(Datas2[0])), Bukkit.getPlayer(UUID.fromString(Datas2[1]))) == null) {
								 
								 ArenaJoin.joinArena(Bukkit.getPlayer(UUID.fromString(Datas2[0])), Bukkit.getPlayer(UUID.fromString(Datas2[1])), Arena, false, "" + Kit, false, "d");
							 } else {
								 Random r = new Random();
									
								 int useArena = r.nextInt(plugin.FreeArenas.size());
								 useArena = useArena+1;
								
								 QueueManager.joinGood(Bukkit.getPlayer(UUID.fromString(Datas2[0])), Bukkit.getPlayer(UUID.fromString(Datas2[1])), Datas2[5]);
								 
							 }
							 
							 
						 } else {
							 plugin.getTeleporter().teleportMainSpawn(Winner);
							  
								ScoreBoardManager.updateBoard(Winner, false);
							  getItems.getLobbyItems(Winner, true);
							  
							  if(plugin.getOneVsOnePlayer(Winner).isWasInQueue()) {
							   TitleAPI.sendTitle(Winner, 10, 20*2, 10, MessageReplacer.replaceStrings(plugin.msgs.getMsg("autoQuequeInfoL1"), Winner.getDisplayName()), MessageReplacer.replaceStrings(plugin.msgs.getMsg("autoQuequeInfoL2"), Winner.getDisplayName()));
							   addQueue(Winner);
							  }
						 }
						 
				  } else {
					plugin.getTeleporter().teleportMainSpawn(Loser);
					
					ScoreBoardManager.updateBoard(Loser, false);
					getItems.getLobbyItems(Loser, true);
					if(plugin.getOneVsOnePlayer(Loser).isWasInQueue()) {
						addQueue(Loser);
					 TitleAPI.sendTitle(Loser, 10, 20*2, 10, MessageReplacer.replaceStrings(plugin.msgs.getMsg("autoQuequeInfoL1"), Loser.getDisplayName()), MessageReplacer.replaceStrings(plugin.msgs.getMsg("autoQuequeInfoL2"), Loser.getDisplayName()));
					}
					
				  }
					 
				 
		 }
		 

		 
		 
		}
	  }, delay+4);	 
	 }
	 /*-----*/
	 
	 /*Gewinner wird teleportiert*/
	 final String[]  Datas2 = Datas;
	 
	 
	 if(Winner.isOnline() && plugin.isInOneVsOnePlayers(Winner.getUniqueId())) {
		 
		 
		 final boolean nfreeArena = freeArena;
		 final boolean nwasTournament = wasTournament;
		 final boolean nMoreFights2 = moreFights;
		 Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
		
			 
			 
		@SuppressWarnings("unused")
		@Override
		public void run() {
			 Winner.setFallDistance(0);
			 Winner.setFallDistance(0);
			
			 if(!nwasTournament) {
				 resetPlayer(Winner, true);
				
					 
					 if(nMoreFights2 && Winner.isOnline() && Loser.isOnline() && 
							 plugin.isInOneVsOnePlayers(Winner.getUniqueId()) && plugin.isInOneVsOnePlayers(Loser.getUniqueId())) {
						
						 PlayerBestOfsPrefs pref = PlayerBestOfsPrefs.BestOf3;
						 int prefGame = Integer.parseInt(Datas2[2]);
						 int Wins1 = 0;
						 int Wins2 = 0;
						 String BestofType = "";
						 
						 if(prefGame == 3){
							 pref = PlayerBestOfsPrefs.BestOf3;
							 BestofType = ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("bestOf3Name"));
						 } else if(prefGame == 5) {
							 pref = PlayerBestOfsPrefs.BestOf5;
							 BestofType = ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("bestOf5Name"));
						 } else {
							 plugin.getTeleporter().teleportMainSpawn(Winner);
							  
								ScoreBoardManager.updateBoard(Winner, false);
							  getItems.getLobbyItems(Winner, true);
							  if(plugin.getOneVsOnePlayer(Winner).isWasInQueue()) {
							   TitleAPI.sendTitle(Winner, 10, 20*2, 10, MessageReplacer.replaceStrings(plugin.msgs.getMsg("autoQuequeInfoL1"), Winner.getDisplayName()), MessageReplacer.replaceStrings(plugin.msgs.getMsg("autoQuequeInfoL2"), Winner.getDisplayName()));
							   addQueue(Winner); 
							  }
						 }
						 
						 Wins1 = Integer.parseInt(Datas2[3]);
						 Wins2 = Integer.parseInt(Datas2[4]);
						 
						 String Name1 = Bukkit.getPlayer(UUID.fromString(Datas2[0])).getDisplayName();
						 String Name2 = Bukkit.getPlayer(UUID.fromString(Datas2[1])).getDisplayName();
						 
						 String sub = ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("bestOfInfo")).replaceAll("%Player1%", Name1).replaceAll("%Wins1%", "" + Wins1).replaceAll("%Player2%", Name2).replaceAll("%Wins2%", "" + Wins2);
						 
						 TitleAPI.sendTitle(Winner, 20, 20*2, 20, BestofType, sub);
						 Winner.getInventory().clear();
						 
						 if(plugin.FreeArenas.size() <= 0) {
						  if(!nfreeArena) {
							  plugin.getTeleporter().teleportMainSpawn(Winner);
							  
								ScoreBoardManager.updateBoard(Winner, false);
							  getItems.getLobbyItems(Winner, true);
							  
							  if(plugin.getOneVsOnePlayer(Winner).isWasInQueue()) {
							   TitleAPI.sendTitle(Winner, 10, 20*2, 10, MessageReplacer.replaceStrings(plugin.msgs.getMsg("autoQuequeInfoL1"), Loser.getDisplayName()), MessageReplacer.replaceStrings(plugin.msgs.getMsg("autoQuequeInfoL2"), Loser.getDisplayName()));
							   addQueue(Winner);
							  }  
						  }
							  
						 }
					  } else {
						  plugin.getTeleporter().teleportMainSpawn(Winner);
						  
							ScoreBoardManager.updateBoard(Winner, false);
						  getItems.getLobbyItems(Winner, true);
						 
						  if(plugin.getOneVsOnePlayer(Winner).isWasInQueue()) {
						   TitleAPI.sendTitle(Winner, 10, 20*2, 10, MessageReplacer.replaceStrings(plugin.msgs.getMsg("autoQuequeInfoL1"), Winner.getDisplayName()), MessageReplacer.replaceStrings(plugin.msgs.getMsg("autoQuequeInfoL2"), Winner.getDisplayName()));
						   addQueue(Winner);
						  }
						  Winner.hidePlayer(Loser);
						  Loser.hidePlayer(Winner);
						  Winner.showPlayer(Loser);
						  Loser.showPlayer(Winner);
					  }
					
				 
			 }
			 
			 
			 
		}
	  }, delay);
	 }
	 /*-----*/
	 
	 /*Stats werden gespeichert*/
	 if(plugin.saveStats) {
		 
			 
			 if(!plugin.getDBMgr().isConnected()) {
					Loser.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
					Winner.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
					return;
				}
			 Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
				
				@Override
				public void run() {
					plugin.getDBMgr().setStats(Winner.getUniqueId(), 1, "Fights", false);
					plugin.getDBMgr().setStats(Winner.getUniqueId(), 1, "FightsWon", false);
					plugin.getDBMgr().setStats(Loser.getUniqueId(), 1, "Fights", false);
					
					plugin.getDBMgr().setStats(Winner.getUniqueId(), 1, "Fights", true);
					plugin.getDBMgr().setStats(Winner.getUniqueId(), 1, "FightsWon", true);
					plugin.getDBMgr().setStats(Loser.getUniqueId(), 1, "Fights", true);
					
				}
			 });

		 
		 /*------*/
	 }
	 
	 
	 
	 if(wasTournament) {
		 Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				
				@Override
				public void run() {
					FightEndManager manager = new FightEndManager(plugin);
					
					 manager.endFight(Winner, Loser, Arena, plugin.tournaments.get(plugin.getOneVsOnePlayer(Winner).getPlayertournament()));
				}
		 },delay);
		 
	 }
	 
	}
	
	public static void resetPlayer(Player p, boolean clear) {
		
		plugin.getOneVsOnePlayer(p).setEnemy(null);
		plugin.getOneVsOnePlayer(p).setArena(null);
		plugin.getOneVsOnePlayer(p).setPosition(0);
		plugin.getOneVsOnePlayer(p).setpState(PlayerState.InLobby);
		plugin.getOneVsOnePlayer(p).setDoubleJumpUsed(false);
		plugin.getOneVsOnePlayer(p).setSpecator(null);
		plugin.getOneVsOnePlayer(p).setChallanged(new ArrayList<Player>());
		plugin.getOneVsOnePlayer(p).setChallangedBy(new ArrayList<Player>());
		
		if(clear) {
		 p.setExp(0);
		 p.setLevel(0);
		 
		 p.setAllowFlight(false);
		 p.setFlying(false);
		 
		 p.getInventory().clear();
		 p.getInventory().setArmorContents(null);
		 p.setMaxHealth(20);
		 p.setHealth(20);
		 p.setFoodLevel(20);
		 
		 p.setFireTicks(1);
		 
		 
		 for(PotionEffect effect : p.getActivePotionEffects())  p.removePotionEffect(effect.getType());
		 
		}
		
	}
	
	
	
	private static void addQueue(final Player p) {
	 
	 int delay = 4*20;
	
	 
	 Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
		
		@Override
		public void run() {
			if(plugin.getOneVsOnePlayer(p).isWasInQueue()) {
			 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby 
				&& !plugin.getOneVsOnePlayer(p).isInQueue()) {
				 
				 plugin.getOneVsOnePlayer(p).setWasInQueue(false);
				 plugin.getOneVsOnePlayer(p).setInQueue(true);
				 
				 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("youAreNowInQueque"), p.getDisplayName()));
			 }	
			}
		}
	 }, delay);
	 
	 
	}
	
	
	
}
