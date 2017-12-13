package de.OnevsOne.Commands.VariableCommands;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.OnevsOne.main;
import de.OnevsOne.Commands.VariableCommands.Manager.CommandTrigger1vs1;
import de.OnevsOne.States.PlayerState;

public class Surrender implements Listener {

	private main plugin;

	public Surrender(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onCommandExec(CommandTrigger1vs1 e) {
		if(e.getCMD().equalsIgnoreCase("surrender") && plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
			Player p = (Player) e.getPlayer();
			
			if(p.hasPermission("1vs1.command.surrender") || p.hasPermission("1vs1.*") || p.hasPermission("1vs1.User")) {
				  if(plugin.isInOneVsOnePlayers(p.getUniqueId())) {
				   if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena && plugin.getOneVsOnePlayer(p).getArena() != null) {
					   if(plugin.ArenaPlayersP1.containsKey(plugin.getOneVsOnePlayer(p).getArena()) && plugin.ArenaPlayersP1.get(plugin.getOneVsOnePlayer(p).getArena()).size() >= 1 &&
						  plugin.ArenaPlayersP2.containsKey(plugin.getOneVsOnePlayer(p).getArena()) && plugin.ArenaPlayersP2.get(plugin.getOneVsOnePlayer(p).getArena()).size() >= 1) {
						   
						if(!plugin.getAState().isEnded(plugin.getOneVsOnePlayer(p).getArena())) {
						 if(plugin.getAState().isStarted(plugin.getOneVsOnePlayer(p).getArena())) {
							p.setHealth(0);
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
