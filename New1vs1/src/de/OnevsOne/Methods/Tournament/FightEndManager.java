package de.OnevsOne.Methods.Tournament;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import de.OnevsOne.main;
import de.OnevsOne.Arena.Manager.ArenaJoin;
import de.OnevsOne.Arena.Manager.RemoveEntitys;
import de.OnevsOne.Arena.Reseter.ResetMethoden;
import de.OnevsOne.Arena.SpectatorManager.SpectateArena;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.Methods.getItems;
import de.OnevsOne.Methods.FightEnder.FightEnd;
import de.OnevsOne.Methods.Queue.QueueManager;
import de.OnevsOne.States.OneVsOnePlayer;
import de.OnevsOne.States.PlayerState;
import de.OnevsOne.States.TournamentState;

public class FightEndManager {

	private static main plugin;

	@SuppressWarnings("static-access")
	public FightEndManager(main plugin) {
		this.plugin = plugin;
	}

	public void endFight(final Player winner, final Player loser, final String Arena, TournamentManager tMgr) {
		
		/*
		 * -> Reset des Users (Verzögert) [Ok!]
		 * -> Stats setzen
		 * -> Check ob mehr Kämpfe kommen
		 *  -> Ja: User erneut zusammen in Arena Porten
		 *  -> Nein: User in Spectator Mode
		 *   -> Check ob alle Kämpfe beendet sind
		 *    -> Ja: Neue Runde
		 *    -> Nein: Spectaten
		 */
		if(tMgr == null) return;
		final TournamentManager fTMgr = tMgr;
		int gamesPlayed = 0;
		 
		
		RemoveEntitys.removeArenaEntitys(Arena,plugin.getPositions().getArenaPos1(Arena).getWorld());
		 
		 
		 plugin.getRAMMgr().deleteRAM(Arena);
		 plugin.getAState().checkArena(Arena);
		 
		 plugin.EntityCount.remove(Arena);
		 plugin.Entitys.remove(Arena);
		 
		 new ResetMethoden(plugin).resetArena(Arena);
		
		 
				
		 if(tMgr.getState() == TournamentState.QUALLI) {
		  if(Bukkit.getPlayer(winner.getUniqueId()) != null && Bukkit.getPlayer(loser.getUniqueId()) != null && !tMgr.isCompleteOut(winner.getUniqueId()) && !tMgr.isCompleteOut(loser.getUniqueId())) {
		   tMgr.setStatsWinsRound(tMgr.getRound(), winner.getUniqueId(), tMgr.getStatsWinsRound(tMgr.getRound(), winner.getUniqueId())+1);
		   tMgr.setStatsLosesRound(tMgr.getRound(), loser.getUniqueId(), tMgr.getStatsLosesRound(tMgr.getRound(), loser.getUniqueId())+1);	 
		   
		   tMgr.setQPointsWins(winner.getUniqueId(), tMgr.getQPointsWins(winner.getUniqueId())+1);
		   tMgr.setQPointsLoses(loser.getUniqueId(), tMgr.getQPointsLoses(loser.getUniqueId())+1);
		  
		   
		   reSetTournament(tMgr.getOwnerUUID(), tMgr);
		   
		   gamesPlayed = tMgr.getStatsWinsRound(tMgr.getRound(), winner.getUniqueId())+tMgr.getStatsLosesRound(tMgr.getRound(), winner.getUniqueId());
		   
		   if(gamesPlayed >= tMgr.getFightsQu()) {
			   //Kampf beendet
			   winner.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentAllFightsEnded")));
			   loser.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentAllFightsEnded")));
			   
			   tMgr.removeFight(winner.getUniqueId());
			   
			   
		   } else {
			   Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
					
					@Override
					public void run() {
						if(Bukkit.getPlayer(winner.getUniqueId()) != null) resetPlayer(winner, true);
						if(Bukkit.getPlayer(loser.getUniqueId()) != null) resetPlayer(loser, true);
						restartFight(winner, loser, fTMgr, Arena);
					}
				}, 2);
			   
		   }
		  
		  } else {
			  
			  if(tMgr.isCompleteOut(winner.getUniqueId())) {
				  tMgr.setOut(winner.getUniqueId(), true);
				  tMgr.removeFight(winner.getUniqueId());
				  gamesPlayed = 5;
			  }
			  
			  if(tMgr.isCompleteOut(loser.getUniqueId())) {
				  tMgr.setOut(loser.getUniqueId(), true);
				  tMgr.removeFight(loser.getUniqueId());
				  gamesPlayed = 5;
			  }
			  
			  if(Bukkit.getPlayer(winner.getUniqueId()) == null) {
				  tMgr.setOut(winner.getUniqueId(), true);
				  tMgr.removeFight(winner.getUniqueId());
				  gamesPlayed = 5;
			  }
			  if(Bukkit.getPlayer(loser.getUniqueId()) == null) {
				  tMgr.setOut(loser.getUniqueId(), true);
				  tMgr.removeFight(loser.getUniqueId());
				  gamesPlayed = 5;
			  }
		  }
		  
		  
		 } else if(tMgr.getState() == TournamentState.KO) {
			   tMgr.setStatsWinsRound(tMgr.getRound(), winner.getUniqueId(), tMgr.getStatsWinsRound(tMgr.getRound(), winner.getUniqueId())+1);
			   tMgr.setStatsLosesRound(tMgr.getRound(), loser.getUniqueId(), tMgr.getStatsLosesRound(tMgr.getRound(), loser.getUniqueId())+1);	 
			   
			   
			   gamesPlayed = tMgr.getStatsWinsRound(tMgr.getRound(), winner.getUniqueId())+tMgr.getStatsLosesRound(tMgr.getRound(), winner.getUniqueId());
			   
			   if(Bukkit.getPlayer(winner.getUniqueId()) == null || Bukkit.getPlayer(loser.getUniqueId()) == null || tMgr.isCompleteOut(winner.getUniqueId()) || tMgr.isCompleteOut(loser.getUniqueId())) {
				   
					  if(tMgr.isCompleteOut(winner.getUniqueId())) {
						  kickTournament(winner, tMgr);
						  tMgr.setOut(winner.getUniqueId(), true);
						  tMgr.removeFight(winner.getUniqueId());
						  gamesPlayed = 5;
					  }
					  
					  if(tMgr.isCompleteOut(loser.getUniqueId())) {
						  kickTournament(loser, tMgr);
						  tMgr.setOut(loser.getUniqueId(), true);
						  tMgr.removeFight(loser.getUniqueId());
						  gamesPlayed = 5;
					  }
					  
					  if(Bukkit.getPlayer(winner.getUniqueId()) == null) {
						  kickTournament(winner, tMgr);
						  tMgr.setOut(winner.getUniqueId(), true);
						  tMgr.removeFight(winner.getUniqueId());
						  gamesPlayed = 5;
					  }
					  if(Bukkit.getPlayer(loser.getUniqueId()) == null) {
						  kickTournament(loser, tMgr);
						  tMgr.setOut(loser.getUniqueId(), true);
						  tMgr.removeFight(loser.getUniqueId());
						  gamesPlayed = 5;
					  }
					 
			   } else {
				   if(gamesPlayed >= tMgr.getFightsKo()) {
					   //Kampf beendet
					   winner.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentAllFightsEnded")));
					   loser.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentAllFightsEnded")));
					   
					   tMgr.removeFight(winner.getUniqueId());
					   
					   int winsP1 = tMgr.getStatsWinsRound(tMgr.getRound(), winner.getUniqueId());
					   int winsP2 = tMgr.getStatsWinsRound(tMgr.getRound(), loser.getUniqueId());

					   if(winsP1 > winsP2) {
						   //Pos2 scheidet aus
						   kickTournament(loser, tMgr);
					   } else {
						   //Pos1 scheidet aus
						   kickTournament(winner, tMgr);
					   }
					   
				   } else {
					   
					   Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
							
							@Override
							public void run() {
								if(Bukkit.getPlayer(winner.getUniqueId()) != null) resetPlayer(winner, true);
								if(Bukkit.getPlayer(loser.getUniqueId()) != null) resetPlayer(loser, true);
								restartFight(winner, loser, fTMgr, Arena);
							}
						}, 2);
				   }
			   }
			   
			   
			   
		 }
		
		 reSetTournament(tMgr.getOwnerUUID(), tMgr);
		 
		 
		 tMgr = plugin.tournaments.get(tMgr.getOwnerUUID());
		 
		
		 
		 
		
					if(tMgr.allFightsEnded()) {
						 //Alle Kämpfe beendet => Nächste Runde!
						 startNextRound(tMgr, winner, loser);
					 } else {
						 
						 
						 if(tMgr.isEnded()) return;
								
						 final int fGamesPlayed = gamesPlayed;
						
						 final TournamentManager fTMgr1 = tMgr;
						 
						Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
									
							@Override
							public void run() {
								
								
								
								for(OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) {
									if(players.getSpecator() != null && !players.getSpecator().equalsIgnoreCase(""))
									 if(players.getpState() == PlayerState.Spec) {
									  if(players.getSpecator().equalsIgnoreCase(Arena)) {
									   letSpec(players.getPlayer(), fTMgr1, plugin);   
									  }
									}
										
								}
								
								
								
								if(fTMgr1.allFightsEnded()) return;
								
								if(fTMgr1.getState() == TournamentState.QUALLI) {
									 if(fGamesPlayed >= fTMgr1.getFightsQu()) {
										 if(Bukkit.getPlayer(winner.getUniqueId()) != null) resetPlayer(winner, true);
										 if(Bukkit.getPlayer(loser.getUniqueId()) != null) resetPlayer(loser, true);
										 if(Bukkit.getPlayer(winner.getUniqueId()) != null) letSpec(winner, fTMgr1, plugin);
										 if(Bukkit.getPlayer(loser.getUniqueId()) != null)  letSpec(loser, fTMgr1, plugin);
										 
										
									 }
								 } else {
									 if(fGamesPlayed >= fTMgr1.getFightsKo()) {
										 if(Bukkit.getPlayer(winner.getUniqueId()) != null) resetPlayer(winner, true);
										 if(Bukkit.getPlayer(loser.getUniqueId()) != null) resetPlayer(loser, true);
										 if(Bukkit.getPlayer(winner.getUniqueId()) != null) letSpec(winner, fTMgr1, plugin);
										 if(Bukkit.getPlayer(loser.getUniqueId()) != null)  letSpec(loser, fTMgr1, plugin);
									 }
								 }
								
							}
						}, 5);
					 }
	}
	
	public static void restartFight(Player pos1, Player pos2, TournamentManager tMgr, String Arena) {
	
		
		String subId = "d";
		String kit = tMgr.getKit();
		if(kit.contains(":") && kit.split(":").length >= 2) {
			subId = kit.split(":")[1];
			kit = kit.split(":")[0];
		} else if(kit.split(":").length == 1) kit = kit.replaceAll(":", "");
		
		String map = QueueManager.getRandomMap(pos1, pos2);
		
		if(map == null) map = QueueManager.getRandomMap(pos1);
		
		if(plugin.FreeArenas.size() == 0) {
			plugin.FreeArenas.add(Arena);
			if(plugin.getDBMgr().isCustomKitExists(kit) == 1) {
				ArenaJoin.joinArena(pos1, pos2, Arena, false, kit, true, subId);
				plugin.getRAMMgr().saveRAM(Arena, "Used", "true");
			} else {
				ArenaJoin.joinArena(pos1, pos2, Arena, false, plugin.getDBMgr().getUUID(kit).toString(), true, subId);
				plugin.getRAMMgr().saveRAM(Arena, "Used", "true");
			}
			
			
			
			new ResetMethoden(plugin).resetArena(Arena);
			return;
			
			
			
		}
		
		
		
		
		if(plugin.getDBMgr().isCustomKitExists(kit) == 1) {
			ArenaJoin.joinArena(pos1, pos2, map, false, kit, true, subId);
		} else {
			ArenaJoin.joinArena(pos1, pos2, map, false, plugin.getDBMgr().getUUID(kit).toString(), true, subId);
		}
	}
	
	public static void resetPlayer(Player p, boolean clear) {
		if(plugin.getOneVsOnePlayer(p).getPlayertournament() != null) return;
		if(!plugin.tournaments.containsKey(plugin.getOneVsOnePlayer(p).getPlayertournament())) return;
		if(plugin.tournaments.get(plugin.getOneVsOnePlayer(p).getPlayertournament()).isEnded()) return;
		if(plugin.tournaments.get(plugin.getOneVsOnePlayer(p).getPlayertournament()).getRemainingPlayers() <= 1) return;
		plugin.getOneVsOnePlayer(p).setEnemy(null);;
		plugin.getOneVsOnePlayer(p).setArena(null);
		plugin.getOneVsOnePlayer(p).setPosition(0);
		plugin.getOneVsOnePlayer(p).setDoubleJumpUsed(false);
		plugin.getOneVsOnePlayer(p).setpState(PlayerState.Spec);
		plugin.getOneVsOnePlayer(p).setSpecator(null);
		
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
	
	public static void letSpec(Player player, TournamentManager mgr, main plugin) {
		if(mgr.isEnded()) return;
		for(TournamentFight fights : mgr.getAllFights()) {
			
		  if(mgr.isCompleteOut(player.getUniqueId())) return;
		  if(Bukkit.getPlayer(fights.getPos1()) != null) {
			 SpectateArena.specArena(player, plugin.getOneVsOnePlayer(fights.getPos1()).getArena(), true);
			 if(plugin.getOneVsOnePlayer(player).getpState() == PlayerState.Spec) continue;
		  } else {
			 SpectateArena.specArena(player, plugin.getOneVsOnePlayer(fights.getPos2()).getArena(), true);
			 if(plugin.getOneVsOnePlayer(player).getpState() == PlayerState.Spec) continue;
		  }
			
		}
	}
	
	public void reSetTournament(UUID tournament, TournamentManager mgr) {
		while(plugin.tournaments.containsKey(tournament)) plugin.tournaments.remove(tournament);
		plugin.tournaments.put(tournament, mgr);
		new Tournament_InvCreator(plugin).reGenerateInv(mgr.getOwnerUUID());
	}
	
	private void startNextRound(final TournamentManager tMgr, final Player winner, final Player loser) {
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			
			@Override
			public void run() {
				
				 
				 
				 if(tMgr.getRemainingPlayers() == 1) {
					 for(Player players : tMgr.getPlayerList2()) {
					  if(tMgr.isCompleteOut(players.getUniqueId())) continue;
						 for(Player Online : Bukkit.getOnlinePlayers()) Online.showPlayer(players);
						 resetPlayer(players, true);
					 }
					 endTournament(tMgr.getPlayerList2().get(0), tMgr);
					 return;
				 } else if(tMgr.getRemainingPlayers() <= 0) {
					 for(Player players : tMgr.getPlayerList2()) {
						 if(tMgr.isCompleteOut(players.getUniqueId())) continue;
						 for(Player Online : Bukkit.getOnlinePlayers()) Online.showPlayer(players);
						 resetPlayer(players, true);
					 }
					 endTournament(null, tMgr);
					 return;
				 }
				 
				 if(tMgr.isEnded()) return;
				 
				 
				 plugin.getOneVsOnePlayer(winner).setpState(PlayerState.Spec);
				 plugin.getOneVsOnePlayer(loser).setpState(PlayerState.Spec);
				 
				 
				 tMgr.setRound(tMgr.getRound()+1);
				 
				 
				 
				 if(tMgr.getRoundsQu() < tMgr.getRound() && tMgr.getState() == TournamentState.QUALLI) {
					 tMgr.setState(TournamentState.KO);
					 
					 tMgr.sortMap();
					 
					 int toKick = tMgr.getStatsQ().size()/2;
					 
					 while(tMgr.getStatsQ().size()-toKick <= 0) toKick = 0;
					 
					 int ignore = tMgr.getStatsQ().size()-toKick;
					 int ac = 0;
					 
					 for(UUID uuid : tMgr.getStatsQ().keySet()) {
						 if(ignore > ac)  ac++; else kickTournament(tMgr.toPlayer(uuid), tMgr);
					 }
					 
					 tournamentEnded(tMgr);
					 
				 }
				 
				 for(Player players : tMgr.getPlayerList2()) {
					 if(players == null) continue;
					 if(tMgr.isCompleteOut(players.getUniqueId())) continue;
					 if(tMgr.getRemainingPlayers() <= 1) return; 
					 if(tMgr.getState() == TournamentState.QUALLI) {
						 players.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentNextRoundQ1")).replaceAll("%Round%", "" + tMgr.getRound()));
					 } else {
						 players.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentNextRoundKO1")).replaceAll("%Round%", "" + tMgr.getRound()));
					 }
					
					 
					 players.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentNextRoundL2")).replaceAll("%Round%", "" + tMgr.getRound()).replaceAll("%Remaining%", "" + tMgr.getRemainingPlayers()).replaceAll("%PlayerT%", "" + tMgr.getPlayerList().size()));
					 for(Player Online : Bukkit.getOnlinePlayers()) Online.showPlayer(players);
					 resetPlayer(players, true);
				 }
				 
				 
				 tMgr.joinAll();
				 Tournament_InvCreator cr = new Tournament_InvCreator(plugin);
				 cr.reGenerateInv(tMgr.getOwnerUUID());
			}
		}, 7);
		
	}
	
	public void kickTournament(Player p, TournamentManager mgr) {
		if(mgr != null && p != null) {
			if(!mgr.isOut(p.getUniqueId()) && mgr.getPlayerList().contains(p.getUniqueId())) {
				if(mgr.isCompleteOut(p.getUniqueId())) return;
				 if(mgr.getRemainingPlayers() == 4) mgr.setPlace(p.getName(), 4);
				 if(mgr.getRemainingPlayers() == 3) mgr.setPlace(p.getName(), 3);
				 if(mgr.getRemainingPlayers() == 2) mgr.setPlace(p.getName(), 2);
				 
				 mgr.setOut(p.getUniqueId(), true);
				 p.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentYouAreOut")));
				 new SoundManager(JSound.WITHER_DEATH, p, 20, 1).play();
				 reSetTournament(mgr.getOwnerUUID(), mgr);
					
				}
		}
		
	}
	
	public boolean tournamentEnded(TournamentManager mgr) {
		if(!mgr.isStarted()) return false;
		if(mgr.getRemainingPlayers() == 1) {
			 endTournament(mgr.getPlayerList2().get(0), mgr);
			 return true;
		 } else if(mgr.getRemainingPlayers() <= 0) {
			 endTournament(null, mgr);
			 return true;
		 }
		
		return false;
	}
	
	public void endTournament(Player winner, TournamentManager mgr) {
		
		
		if(mgr.isEnded()) return;
		mgr.setEnded(true);
		
		
//		if(winner != null) {
//			for(Player players : mgr.getPlayerList2()) players.sendMessage(plugin.prefix + "§6" + winner.getName() + " §7hat das Turnier gewonnen!");
//		} else {
//			for(Player players : mgr.getPlayerList2()) players.sendMessage(plugin.prefix + "§cNiemand §7hat das Turnier gewonnen!");
//		}
		
		if(winner != null) mgr.setPlace(winner.getName(), 1);
		
		
		String pl1 = mgr.getPlace(1);
		String pl2 = mgr.getPlace(2);
		String pl3 = mgr.getPlace(3);
		String pl3_1 = mgr.getPlace(4);
		
		for(Player players : mgr.getPlayerList2()) {
			if(mgr.isCompleteOut(players.getUniqueId())) continue;
			players.sendMessage("§f① §r§6" + pl1);
			players.sendMessage("§f② §r§6" + pl2);
			players.sendMessage("§f③ §r§6" + pl3);
			players.sendMessage("§f╚ §6" + pl3_1);//TODO Language.yml
		}
		
		//① ② ③
		
		for(final Player players : mgr.getPlayerList2()) {
			if(mgr.isCompleteOut(players.getUniqueId())) continue;
			Bukkit.getScheduler().runTask(plugin, new Runnable() {
				
				@Override
				public void run() {
					plugin.getTeleporter().teleportMainSpawn(players);
					
					FightEnd.resetPlayer(players, true);
					getItems.getLobbyItems(players, true);
					
				}
			});
			
			
			
		}
		
		
		mgr.delete();
		
	}
	
	
	
}
