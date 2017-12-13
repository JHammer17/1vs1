package de.OnevsOne.Commands.VariableCommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.OnevsOne.main;
import de.OnevsOne.Arena.SpectatorManager.SpectateArena;
import de.OnevsOne.Commands.VariableCommands.Manager.CommandTrigger1vs1;
import de.OnevsOne.MessageManager.MessageReplacer;

public class Spec implements Listener {

	private main plugin;

	public Spec(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onCommandExec(CommandTrigger1vs1 e) {
		if(e.getCMD().equalsIgnoreCase("spec") && plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
			Player p = (Player) e.getPlayer();
			String[] args = e.getArgs();
			
			 if(!p.hasPermission("1vs1.command.spec") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.User")) {
		    		plugin.sendNoPermsMessage(p);
		    		return;
		    	}
			 
			 if(plugin.getOneVsOnePlayer(p).getPlayertournament() != null) {
			  p.sendMessage(plugin.msgs.getMsg("tournamentinTournament"));	
			  return;
			 }
			 if(plugin.isInOneVsOnePlayers(p.getUniqueId())) {
				 if(args.length == 1) {
					 if(Bukkit.getPlayer(args[0]) != null) {
					  SpectateArena.specArena(p, plugin.getOneVsOnePlayer(Bukkit.getPlayer(args[0])).getArena(), false); 
					 } else {
						 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("playerNotFound"), p.getDisplayName(), args[0], null));
					 }
					} else {
					  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("wrongUsageSpecCommand"), p.getDisplayName()));
					}
			 } else {
				 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("notIn1vs1"), p.getDisplayName()));
			 }
		}
	}

}
