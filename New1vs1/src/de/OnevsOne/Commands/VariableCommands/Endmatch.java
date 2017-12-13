package de.OnevsOne.Commands.VariableCommands;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.OnevsOne.main;
import de.OnevsOne.Commands.VariableCommands.Manager.CommandTrigger1vs1;

import de.OnevsOne.States.PlayerState;
import net.md_5.bungee.api.ChatColor;

public class Endmatch implements Listener {

	private main plugin;

	public Endmatch(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onCommandExec(CommandTrigger1vs1 e) {
		if(e.getCMD().equalsIgnoreCase("endmatch") && plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
			Player p = (Player) e.getPlayer();
			if(p.hasPermission("1vs1.command.endmatch") || p.hasPermission("1vs1.User") || p.hasPermission("1vs1.*")) {
				   if(plugin.isInOneVsOnePlayers(p.getUniqueId())) {
					  
					if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena &&
						plugin.getOneVsOnePlayer(p).getArena() != null) {
						
					 if(plugin.ArenaPlayersP1.containsKey(plugin.getOneVsOnePlayer(p).getArena()) && plugin.ArenaPlayersP1.get(plugin.getOneVsOnePlayer(p).getArena()).size() >= 1 &&
						plugin.ArenaPlayersP2.containsKey(plugin.getOneVsOnePlayer(p).getArena()) && plugin.ArenaPlayersP2.get(plugin.getOneVsOnePlayer(p).getArena()).size() >= 1) {		
					  if(!plugin.getAState().isEnded(plugin.getOneVsOnePlayer(p).getArena())) {
					   if(plugin.getAState().isStarted(plugin.getOneVsOnePlayer(p).getArena())) {
					    if(!plugin.getAState().isEndMatch(plugin.getOneVsOnePlayer(p).getArena())) {
					    	plugin.getAState().setEndMatch(plugin.getOneVsOnePlayer(p).getArena(), 60);
					    } else {
					    	if(plugin.getAState().getEndMatch(plugin.getOneVsOnePlayer(p).getArena()) > 60) { 
					    		plugin.getAState().setEndMatch(plugin.getOneVsOnePlayer(p).getArena(), 60);
					    	    return;
					    	}
					    	p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("endMatchAlreadyStarted")).replaceAll("%Prefix%", plugin.prefix)); 
					    }
					   } else {
						   p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("gameNotStarted")).replaceAll("%Prefix%", plugin.prefix)); 
					   }
					  } else {
						  p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("gameEnded")).replaceAll("%Prefix%", plugin.prefix)); 
					  }
					 } else {
						 p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("error")).replaceAll("%Prefix%", plugin.prefix)); 
					 }
				    } else {
				    	p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("notInAArena")).replaceAll("%Prefix%", plugin.prefix)); 
				    }
				   } else {
					   p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("notIn1vs1")).replaceAll("%Prefix%", plugin.prefix)); 
				   }
				 } else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.noPerms).replaceAll("%Prefix%", plugin.prefix)); 
				 }
		}
	}

}
