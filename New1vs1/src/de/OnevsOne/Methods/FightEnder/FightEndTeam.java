package de.OnevsOne.Methods.FightEnder;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.OnevsOne.main;
import de.OnevsOne.Arena.Manager.RemoveEntitys;
import de.OnevsOne.Arena.Reseter.ResetMethoden;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.ScoreBoardManager;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.Methods.getItems;
import de.OnevsOne.Methods.moneyTitles;
import de.OnevsOne.Methods.Messenger.TitleAPI;
import de.OnevsOne.States.ArenaTeamPlayer;
import de.OnevsOne.States.OneVsOnePlayer;
import de.OnevsOne.States.PlayerState;

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
public class FightEndTeam {
     
	private static main plugin;
	@SuppressWarnings("static-access")
	public FightEndTeam(main plugin) {
		this.plugin = plugin;
	}
    
	
	public static void EndGame(final ArenaTeamPlayer Winner, final ArenaTeamPlayer Loser, final String Arena) {
		
		for(Player players : Winner.getAll()) moneyTitles.send(players, plugin);
		
		
		//Set ArenaState to Ended
		if(plugin.getAState().checkState(Arena, "Ended") == null || plugin.getAState().checkState(Arena, "Ended").equalsIgnoreCase("false")) {
			plugin.getRAMMgr().saveRAM(Arena, "Ended", "true");
		} else {
			return;
		}
		//-------
		
		/**/
		
		
		
		
		
		//Remove ArenaKit and Entitys
		
		plugin.ArenaPlayersP1.remove(Arena);
		plugin.ArenaPlayersP2.remove(Arena);
		
		while(plugin.arenaTeams.containsKey(Arena)) plugin.arenaTeams.remove(Arena);
		//---------------------------
		
		
		
		
		
		//Herzen berechnen
		
		double health = 0;
  	    double live = 0;
  	  
  	    for(Player players : Winner.getAll()) {
  	     if(plugin.getOneVsOnePlayer(players).getpState() == PlayerState.InArena) {
  	    	 live += players.getPlayer().getHealth();
  	     }
  	    }
  	    
  	    if(live%2 != 0) {
  		 for(int i = 0; i < (live-1)/2;i++) {
  		  health++;
  		 }
  		 health += 0.5;
  	     } else {
  		 for(int i = 0; i < (live-1)/2;i++) {
  		 health++;
  		 }
  	    }
		//-------------
		
		
		//Nachrichten senden
  	    
  	    String allWinners = "";
  	    for(int i = 0; i < Winner.getAll().size(); i++) {
  	    	if(i >= (Winner.getAll().size()-1)) {
  	    		allWinners = allWinners+Winner.getAll().get(i).getDisplayName();
  	    	} else {
  	    		allWinners = allWinners+Winner.getAll().get(i).getDisplayName() + " & ";
  	    	}
  	    	
  	    }
  	    
  	    String allLosers = "";
	    for(int i = 0; i < Loser.getAll().size(); i++) {
	    	if(i >= (Loser.getAll().size()-1)) {
	    		allLosers = new StringBuilder().append(allLosers).append(Loser.getAll().get(i).getDisplayName()).toString();
	    	} else {
	    		allLosers = new StringBuilder().append(allLosers).append(Loser.getAll().get(i).getDisplayName()).append(" & ").toString();
	    	}
	    	
	    }
	    
	   
	    for(Player players : Loser.getAll()) {
	    	plugin.getRAMMgr().saveRAM(Arena, "Out." + players.getUniqueId(), true);
	    	ScoreBoardManager.updateBoard(players, false);
	    }
	    
	    for(Player players : Winner.getAll()) 
	    	ScoreBoardManager.updateBoard(players, false);
	    
	    
	    
	    if(Winner.big() || Loser.big()) {
	    	for(Player players : Winner.getAll())players.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("fightWonTeam"), Winner.getTeamName(false), Loser.getTeamName(false), null)); 
			for(Player players : Loser.getAll())players.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("fightLostTeam2"), Loser.getTeamName(true), Winner.getTeamName(true), health));
	    } else {
	    	for(Player players : Winner.getAll())players.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("fightWonTeam"), Winner.getTeamName(false), Loser.getTeamName(false), null)); 
			for(Player players : Loser.getAll())players.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("fightLostTeam"), Loser.getTeamName(false), Winner.getTeamName(false), health));
	    }
		
		//------------------
		
		int delay = 4*20;
		if(!plugin.voidTeleport) delay = 2*20;
		
		
		
		
		//Nach Verögerung Arena zum Löschen freigeben
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			
		
		@Override
		public void run() {
			
		 RemoveEntitys.removeArenaEntitys(Arena,plugin.getPositions().getArenaPos1(Arena).getWorld());
		 
		 
		 

		 plugin.getRAMMgr().deleteRAM(Arena);
		 plugin.getAState().checkArena(Arena);
		 
		 plugin.EntityCount.remove(Arena);
		 plugin.Entitys.remove(Arena);
		 
		 new ResetMethoden(plugin).resetArena(Arena);
		 }
		}, delay+20+10);
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			
			@Override
			public void run() {
				while(plugin.allPlayersArenaP1.containsKey(Arena))
					plugin.allPlayersArenaP1.remove(Arena);
				while(plugin.allPlayersArenaP2.containsKey(Arena))
					plugin.allPlayersArenaP2.remove(Arena);
				plugin.ArenaKit.remove(Arena);
			}
		}, 20);
		//--------------
		
		
	 	
	
	 
	 /*Spectator aus der Arena teleportieren*/
	 for(final OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) {
	  if(players.getpState() == PlayerState.Spec) {
	   if(players.getSpecator() != null && players.getSpecator().equalsIgnoreCase(Arena)) {
		if(!Loser.getAll().contains(players.getPlayer()) && !Winner.getAll().contains(players.getPlayer())) {
			if(Winner.big()) {
				players.getPlayer().sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("fightSpecTeam2"), Loser.getTeamName(false), Winner.getTeamName(true), health));
			} else {
				players.getPlayer().sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("fightSpecTeam"), Loser.getTeamName(false), Winner.getTeamName(true), health));
			}
			
		}
		
		
		if(players.getPlayertournament() != null) continue;
		
		
	    
	    Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			
			@Override
			public void run() {
				if(players.getPlayer().isOnline() && players.isIn1vs1()) {
					
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
					    for(PotionEffect effekt : players.getPlayer().getActivePotionEffects()) {
					    	players.getPlayer().removePotionEffect(effekt.getType());
					    }
					    getItems.getLobbyItems(players.getPlayer(), true);
					    for(Player Online : Bukkit.getOnlinePlayers()) {
					     Online.getPlayer().showPlayer(players.getPlayer());
					    }
					    
					    
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
	 
	 /*Loser wird ins nichts teleportiert*/
	 for(final Player loserPlayers : Loser.getAll()) {
		 
		 if(loserPlayers.isOnline() && plugin.isInOneVsOnePlayers(loserPlayers.getUniqueId())) {
			
			  
			  if(plugin.voidTeleport) {
			   Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
						
				@Override
				public void run() {
					
					loserPlayers.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*100, 500, true, true));
					loserPlayers.teleport(new Location(Bukkit.getWorld(loserPlayers.getWorld().getName()), 0, -1000, 0));
				 SoundManager manager = new SoundManager(JSound.DEATH, loserPlayers, 10.0F, 1.0F);
				 manager.play();
							
				}
				
			   }, 10);
			  }
			  
		 
		 Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				
				@Override
				public void run() {
					
					
					loserPlayers.setFallDistance(0);
					loserPlayers.setFallDistance(0);		
				
				 
				 
					 resetPlayer(loserPlayers, true);
					
					
					 plugin.getTeleporter().teleportMainSpawn(loserPlayers);
							
							ScoreBoardManager.updateBoard(loserPlayers, false);
							getItems.getLobbyItems(loserPlayers, true);
							
							if(plugin.getOneVsOnePlayer(loserPlayers).isWasInQueue()) {
							 addWarteschlange(loserPlayers);
							 TitleAPI.sendTitle(loserPlayers, 10, 20*2, 10, MessageReplacer.replaceStrings(plugin.msgs.getMsg("autoQuequeInfoL1"), loserPlayers.getDisplayName()), MessageReplacer.replaceStrings(plugin.msgs.getMsg("autoQuequeInfoL2"), loserPlayers.getDisplayName()));
							}
				 
				}
			  }, delay+4);	 
		 }
	 }
	
	
		
			 
		 
	  
	 /*-----*/
	 
	 /*Gewinner wird teleportiert*/
	
	 
	 for(final Player winnerPlayers : Winner.getAll()) {
		 if(winnerPlayers.isOnline() && plugin.isInOneVsOnePlayers(winnerPlayers.getUniqueId())) {
			 
			 
			 Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			
				 
				 
			@Override
			public void run() {
				winnerPlayers.setFallDistance(0);
				winnerPlayers.setFallDistance(0);
				
				 
					 resetPlayer(winnerPlayers, true);
					 
						 
						
					 plugin.getTeleporter().teleportMainSpawn(winnerPlayers);
							  
								ScoreBoardManager.updateBoard(winnerPlayers, false);
							  getItems.getLobbyItems(winnerPlayers, true);
							 
							  if(plugin.getOneVsOnePlayer(winnerPlayers).isWasInQueue()) {
							   TitleAPI.sendTitle(winnerPlayers, 10, 20*2, 10, MessageReplacer.replaceStrings(plugin.msgs.getMsg("autoQuequeInfoL1"), winnerPlayers.getDisplayName()), MessageReplacer.replaceStrings(plugin.msgs.getMsg("autoQuequeInfoL2"), winnerPlayers.getDisplayName()));
							   addWarteschlange(winnerPlayers);
							  }
							 
						  
						
					 
				 }
				 
				 
				 
			
		  }, delay);
		 }
		 
	 }
	
	 /*-----*/
	 
	 
	 
	}
	
	public static void resetPlayer(Player p, boolean clear) {
		
		plugin.getOneVsOnePlayer(p).setEnemy(null);
		plugin.getOneVsOnePlayer(p).setArena(null);
		plugin.getOneVsOnePlayer(p).setPosition(0);
		plugin.getOneVsOnePlayer(p).setpState(PlayerState.InLobby);
		plugin.getOneVsOnePlayer(p).setDoubleJumpUsed(false);
		
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
		 
		 
		 for(PotionEffect effect : p.getActivePotionEffects()) {
			 p.removePotionEffect(effect.getType());
		 }
		 
		}
		
	}
	
	
	
	private static void addWarteschlange(final Player p) {
	 
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
