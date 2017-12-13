package de.OnevsOne.Commands.VariableCommands.Tournament;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.OnevsOne.main;
import de.OnevsOne.Commands.VariableCommands.Manager.CommandTrigger1vs1;

import de.OnevsOne.Methods.Tournament.Tournament_InvCreator;
import net.md_5.bungee.api.ChatColor;

public class Tournament implements Listener {

	private main plugin;

	public Tournament(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onCommandExec(CommandTrigger1vs1 e) {
		if(e.getCMD().equalsIgnoreCase("t") && plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
			Player p = (Player) e.getPlayer();
			if(p.hasPermission("1vs1.command.t") || p.hasPermission("1vs1.User") || p.hasPermission("1vs1.*")) {
			 if(plugin.isInOneVsOnePlayers(p.getUniqueId())) {
				  if(plugin.getOneVsOnePlayer(p).getPlayertournament() != null) {
				   UUID Turnier = plugin.getOneVsOnePlayer(p).getPlayertournament();
				   
				   Tournament_InvCreator c = new Tournament_InvCreator(plugin);
				   c.creatInfoInv(Turnier, p);
				  } else {
					  p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentnotInTournament").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix)));
				  }
				 } else {
					 p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("notIn1vs1").replaceAll("%Prefix%", plugin.prefix)));
				 }
		 }
		}
	}
	

}
