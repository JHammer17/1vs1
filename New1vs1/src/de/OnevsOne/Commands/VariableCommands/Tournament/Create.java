package de.OnevsOne.Commands.VariableCommands.Tournament;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import de.OnevsOne.main;
import de.OnevsOne.Commands.VariableCommands.Manager.CommandTrigger1vs1;

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.Tournament.TournamentManager;
import de.OnevsOne.Methods.Tournament.Tournament_InvCreator;
import de.OnevsOne.States.PlayerState;
import net.md_5.bungee.api.ChatColor;

public class Create implements Listener {

	private main plugin;

	public Create(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onCommandExec(CommandTrigger1vs1 e) {
		if(!e.getCMD().equalsIgnoreCase("create") && !e.getCMD().equalsIgnoreCase("modify")) {
			return;
		}
		if(plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
			Player p = (Player) e.getPlayer();
			
			  if(p.hasPermission("1vs1.command.createT") || p.hasPermission("1vs1.Premium") || p.hasPermission("1vs1.*")) {
			   if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby) {	  
			    if(plugin.getOneVsOnePlayer(p).getPlayertournament() != null) {
			    	TournamentManager tMgr = plugin.tournaments.get(plugin.getOneVsOnePlayer(p).getPlayertournament());
			    	if(tMgr != null && tMgr.isStarted()) {
			    		p.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentAlreadyStarted")));
			    		return;
			    	}
			    }
			    	
			    	if(!plugin.tournaments.containsKey(p.getUniqueId())) {
			    		TournamentManager mgr = new TournamentManager(p.getUniqueId(), p.getName(), plugin);
				    	plugin.tournaments.put(p.getUniqueId(), mgr);
			    	}
			    
			    	
			    
			    	Tournament_InvCreator creator = new Tournament_InvCreator(plugin);
				    creator.openInv(p);
			    
			    
			   } else {
				   p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("notInLobby").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix)));
			   }
			  } else {
				  p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentnoPermissions").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix)));
			  }
		 
		}
		
	}
}
