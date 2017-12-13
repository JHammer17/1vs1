package de.OnevsOne.Commands.VariableCommands.Manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import de.OnevsOne.main;

public class Command_Manager implements Listener {

	private main plugin;

	public Command_Manager(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onCommandProz(PlayerCommandPreprocessEvent e) {
		String command = e.getMessage().replaceFirst("/", "").split(" ")[0];
		String[] cmd = e.getMessage().replaceFirst("/", "").split(" ");
		
		if(plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			
		 if(plugin.useAsWhitelist) {
			
			 if(!plugin.blockedCommands.contains(command.toLowerCase())) {
				  e.setCancelled(true);
				  e.getPlayer().sendMessage("§cDiesen Befehl darfst du hier nicht ausführen!");
				  return;
				 }
		 } else {
			 
			 if(plugin.blockedCommands.contains(command.toLowerCase())) {
				  e.setCancelled(true);
				  e.getPlayer().sendMessage("§cDiesen Befehl darfst du hier nicht ausführen!");
				  return;
				 }
		 }
		 
		}
		
		String a = "";
		
		for(int i = 1; cmd.length > i  ;i++) {
			if(i == 1) {
				a = a+cmd[i];
			} else {
				a = a+" "+cmd[i];
			}
		}
		
		String[] args = a.split(" ");
		if(a.equalsIgnoreCase("")) args = new String[0];
		Player sender = e.getPlayer();
		
		CommandTrigger1vs1 trigger = new CommandTrigger1vs1(sender, args, command);
		Bukkit.getServer().getPluginManager().callEvent(trigger);
	
		e.setCancelled(trigger.isCancelled());
	}

}
