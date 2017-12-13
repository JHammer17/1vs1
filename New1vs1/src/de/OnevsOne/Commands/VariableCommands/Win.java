package de.OnevsOne.Commands.VariableCommands;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.OnevsOne.main;
import de.OnevsOne.Commands.VariableCommands.Manager.CommandTrigger1vs1;
import de.OnevsOne.Methods.FightEnder.FightEnd;
import de.OnevsOne.Methods.FightEnder.FightEndTeam;
import de.OnevsOne.States.ArenaTeamPlayer;
import de.OnevsOne.States.PlayerState;
import de.OnevsOne.States.TeamPlayer;

public class Win implements Listener {

	private main plugin;

	public Win(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onCommandExec(CommandTrigger1vs1 e) {
		if(e.getCMD().equalsIgnoreCase("win") && plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
			Player p = (Player) e.getPlayer();
			
			if(p.hasPermission("1vs1.command.win") || p.hasPermission("1vs1.*") || p.hasPermission("1vs1.Admin")) {
				  if(plugin.isInOneVsOnePlayers(p.getUniqueId())) {
				   if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena && plugin.getOneVsOnePlayer(p).getArena() != null) {
					   if(plugin.ArenaPlayersP1.containsKey(plugin.getOneVsOnePlayer(p).getArena()) && plugin.ArenaPlayersP1.get(plugin.getOneVsOnePlayer(p).getArena()).size() >= 1 &&
							   plugin.ArenaPlayersP2.containsKey(plugin.getOneVsOnePlayer(p).getArena()) && plugin.ArenaPlayersP2.get(plugin.getOneVsOnePlayer(p).getArena()).size() >= 1) {
						  
					   if(!plugin.getAState().isEnded(plugin.getOneVsOnePlayer(p).getArena())) {
						if(plugin.getAState().isStarted(plugin.getOneVsOnePlayer(p).getArena())) {
							
							if(plugin.arenaTeams.containsKey(plugin.getOneVsOnePlayer(p).getArena())) {
								TeamPlayer winTeam = plugin.getOneVsOnePlayer(p).getPlayerTeam();
								ArenaTeamPlayer loserTeam = null;
								
								if(plugin.arenaTeams.get(plugin.getOneVsOnePlayer(p).getArena()).get(0).getPlayer().getUniqueId().equals(p.getUniqueId())) {
									loserTeam = plugin.arenaTeams.get(plugin.getOneVsOnePlayer(p).getArena()).get(1);
								} else {
									loserTeam = plugin.arenaTeams.get(plugin.getOneVsOnePlayer(p).getArena()).get(0);
								}
								ArenaTeamPlayer arenaWinTeam = new ArenaTeamPlayer(winTeam.getPlayer(), winTeam.getTeamMates());
								FightEndTeam.EndGame(arenaWinTeam, loserTeam, plugin.getOneVsOnePlayer(p).getArena());
								
							} else {
								   Player p1 = plugin.ArenaPlayersP1.get(plugin.getOneVsOnePlayer(p).getArena()).get(0);
								   Player p2 = plugin.ArenaPlayersP2.get(plugin.getOneVsOnePlayer(p).getArena()).get(0);
								   
								   if(p.getUniqueId().toString().equalsIgnoreCase(p1.getUniqueId().toString())) {
									   FightEnd.EndGame(p, p2, plugin.getOneVsOnePlayer(p).getArena());
								   } else {
									   FightEnd.EndGame(p, p1, plugin.getOneVsOnePlayer(p).getArena());
								   }
							}
								   return;
								   
						} else {
							 p.sendMessage(plugin.msgs.getMsg("gameNotStarted").replaceAll("%Prefix%", plugin.prefix));
						} 
						   } else {
							  
							   p.sendMessage(plugin.msgs.getMsg("gameEnded").replaceAll("%Prefix%", plugin.prefix));
						   }
						   
						   
					   } else {
						   p.sendMessage(plugin.msgs.getMsg("error").replaceAll("%Prefix%", plugin.prefix));
						   
					   }
				   } else {
					   p.sendMessage(plugin.msgs.getMsg("notInAArena").replaceAll("%Prefix%", plugin.prefix));
				   }
				  } else {
					  p.sendMessage(plugin.msgs.getMsg("notIn1vs1").replaceAll("%Prefix%", plugin.prefix));
				  }
				 } else {
				   p.sendMessage(plugin.noPerms);	 
				 }
		}
	}
}
