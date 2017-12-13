package de.OnevsOne.Listener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;

import de.OnevsOne.main;
import de.OnevsOne.Arena.SpectatorManager.SpectateArena;
import de.OnevsOne.Methods.ScoreBoardManager;
import de.OnevsOne.Methods.FightEnder.FightEnd;
import de.OnevsOne.Methods.FightEnder.FightEndTeam;
import de.OnevsOne.States.OneVsOnePlayer;
import de.OnevsOne.States.PlayerPrefs;
import de.OnevsOne.States.PlayerState;

/**
 * Der Code ist von JHammer
 *
 * 07.05.2016 um 16:00:30 Uhr
 */
public class KillEvent implements Listener {

	private static main plugin;
	
	@SuppressWarnings("static-access")
	public KillEvent(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		
		
		Player Loser = e.getEntity();
		Player killer = e.getEntity().getKiller();
		
		if(!plugin.isInOneVsOnePlayers(Loser.getUniqueId())) return;
		e.setDeathMessage(null);
		
		
		
		plugin.getDBMgr().updateRankPoints(Loser.getUniqueId(), plugin.rankPointsLose); 
		
		if(killer != null && killer instanceof Player) plugin.getDBMgr().updateRankPoints(killer.getUniqueId(), plugin.rankPointsWins);
		
		
		
		final String Arena = plugin.getOneVsOnePlayer(Loser).getArena();
		
		
		if(plugin.getAState().checkState(plugin.getOneVsOnePlayer(Loser).getArena(), "Pref."+ PlayerPrefs.NoItemDrops) != null 
		   && plugin.getAState().checkState(plugin.getOneVsOnePlayer(Loser).getArena(),"Pref." + PlayerPrefs.NoItemDrops).equalsIgnoreCase("true")) {
			e.setKeepInventory(true);
			e.setKeepLevel(true);
			Loser.getInventory().clear();
			Loser.setLevel(0);
		}
		
		ArrayList<Player> sended = new ArrayList<>();
		
		if(plugin.arenaTeams.containsKey(Arena)) {
			if(e.getEntity().getKiller() instanceof Player) {
				sended.addAll(plugin.arenaTeams.get(Arena).get(0).getAll());
				sended.addAll(plugin.arenaTeams.get(Arena).get(1).getAll());
				for(Player players : plugin.arenaTeams.get(Arena).get(0).getAll()) players.sendMessage(plugin.prefix + Loser.getDisplayName() + " wurde von " + e.getEntity().getKiller().getDisplayName() + " getötet.");
				for(Player players : plugin.arenaTeams.get(Arena).get(1).getAll()) players.sendMessage(plugin.prefix + Loser.getDisplayName() + " wurde von " + e.getEntity().getKiller().getDisplayName() + " getötet.");
			} else {
				sended.addAll(plugin.arenaTeams.get(Arena).get(0).getAll());
				sended.addAll(plugin.arenaTeams.get(Arena).get(1).getAll());
				for(Player players : plugin.arenaTeams.get(Arena).get(0).getAll()) players.sendMessage(plugin.prefix + Loser.getDisplayName() + " ist gestorben.");
				for(Player players : plugin.arenaTeams.get(Arena).get(1).getAll()) players.sendMessage(plugin.prefix + Loser.getDisplayName() + " ist gestorben.");
			}
		}
		
		for(final OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) {
			  if(players.getpState() == PlayerState.Spec) {
			   if(players.getSpecator().equalsIgnoreCase(Arena)) {
				   
				   if(!sended.contains(players.getPlayer())) {
					   if(e.getEntity().getKiller() instanceof Player) {
							players.getPlayer().sendMessage(plugin.prefix + Loser.getDisplayName() + " wurde von " + e.getEntity().getKiller().getDisplayName() + " getötet.");
							
						} else {
							players.getPlayer().sendMessage(plugin.prefix + Loser.getDisplayName() + " ist gestorben.");
							
						}
				   }
				   
			   }
			  }
		}
		
		
		
		
		simulateDeath(e.getEntity(), true);
	}
	
	public static void removePlayerArenaTeam(final Player p, String Arena) {
		
		OneVsOnePlayer player = plugin.getOneVsOnePlayer(p);
		
		player.setEnemy(null);
		player.setArena(null);
		player.setPosition(0);
		player.setDoubleJumpUsed(false);
		player.setpState(PlayerState.Spec);
		

		
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
		 p.setMaximumNoDamageTicks(20);
		 
		 for(PotionEffect effect : p.getActivePotionEffects())  p.removePotionEffect(effect.getType());
	}
	
	public static void simulateDeath(final Player Loser, boolean event) {
		/*Spieler stirbt*/
		if(!plugin.isInOneVsOnePlayers(Loser.getUniqueId())) return;
		
		
		
		Player Winner = plugin.getOneVsOnePlayer(Loser).getEnemy();
		
		
		if(!event) plugin.getDBMgr().updateRankPoints(Loser.getUniqueId(), plugin.rankPointsLose); 
		
		final String Arena = plugin.getOneVsOnePlayer(Loser).getArena();
		
		
		plugin.getRAMMgr().saveRAM(Arena, "Out." + Loser.getUniqueId(), true);
		ScoreBoardManager.updateBoard(Loser, false);
		ScoreBoardManager.updateBoard(Winner, false);
		
		if(Winner == null || !Winner.isOnline()) return;
		
		if(!Winner.isOnline() && !plugin.arenaTeams.containsKey(Arena)) return;
		
		
		
		
		if(plugin.voidTeleport) {
		 Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
		  @Override
		  public void run() {
		   Loser.spigot().respawn();
		   removePlayerArenaTeam(Loser, Arena);
		   if(!plugin.ArenaPlayersP1.containsKey(Arena) || !plugin.ArenaPlayersP2.containsKey(Arena)) return;
		   if(plugin.ArenaPlayersP1.get(Arena).size() <= 0 || plugin.ArenaPlayersP2.get(Arena).size() <= 0) {
			   return;
		   }
		   
		   SpectateArena.specArena(Loser, Arena, true);
		  }
		 }, 2);
		} else {
		 Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
		  @Override
		  public void run() {
		   Loser.spigot().respawn();
		   removePlayerArenaTeam(Loser, Arena);
		   if(!plugin.ArenaPlayersP1.containsKey(Arena) || !plugin.ArenaPlayersP2.containsKey(Arena)) return;
		   if(plugin.ArenaPlayersP1.get(Arena).size() <= 0 || plugin.ArenaPlayersP2.get(Arena).size() <= 0) {
			   return;
		   }
		   
		   SpectateArena.specArena(Loser, Arena, true);
		  }
		 }, 20*2-10);
		}
		
		
		
		if(plugin.ArenaPlayersP1.containsKey(Arena) && plugin.ArenaPlayersP1.get(Arena).contains(Loser)) {
			
			//Pos1
			ArrayList<Player> playersP1 = plugin.ArenaPlayersP1.get(Arena);
			playersP1.remove(Loser);
			plugin.ArenaPlayersP1.remove(Arena);
			plugin.ArenaPlayersP1.put(Arena, playersP1);
			
			if(plugin.ArenaPlayersP1.get(Arena).size() <= 0) {
			 if(plugin.arenaTeams.containsKey(Arena)) {
				 FightEndTeam.EndGame(plugin.arenaTeams.get(Arena).get(1), plugin.arenaTeams.get(Arena).get(0), Arena); //Spiel wird beendet
			 } else {
				 FightEnd.EndGame(Winner, Loser, Arena);
			 }
				
			}
		} else if(plugin.ArenaPlayersP2.containsKey(Arena) && plugin.ArenaPlayersP2.get(Arena).contains(Loser)){
			//Pos2
			ArrayList<Player> playersP2 = plugin.ArenaPlayersP2.get(Arena);
			playersP2.remove(Loser);
			plugin.ArenaPlayersP2.remove(Arena);
			plugin.ArenaPlayersP2.put(Arena, playersP2);
			
			
			if(plugin.ArenaPlayersP2.get(Arena).size() <= 0) {
				if(plugin.arenaTeams.containsKey(Arena)) {
					FightEndTeam.EndGame(plugin.arenaTeams.get(Arena).get(0), plugin.arenaTeams.get(Arena).get(1), Arena); //Spiel wird beendet
				 } else {
					 FightEnd.EndGame(Winner, Loser, Arena);
				 }
			}
		}
		
		
		
	}
	
	@EventHandler
	public void onVoidSpawn(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena) {
				String arena = plugin.getOneVsOnePlayer(p).getArena();
				if(plugin.getAState().isEnded(arena)) e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		if(!plugin.voidTeleport) {
			if(plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId()) && plugin.getOneVsOnePlayer(e.getPlayer()).getArena() != null) {
				String Arena = plugin.getOneVsOnePlayer(e.getPlayer()).getArena();
				e.setRespawnLocation(plugin.getPositions().getArenaPos3(Arena));
				
			}
		}
	}
	

}
