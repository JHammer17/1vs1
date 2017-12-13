package de.OnevsOne.Commands.VariableCommands.Tournament;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.OnevsOne.main;
import de.OnevsOne.Commands.VariableCommands.Manager.CommandTrigger1vs1;
import de.OnevsOne.Listener.Manager.ChallangeManager;

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.Methods.Tournament.TournamentManager;
import de.OnevsOne.Methods.Tournament.Tournament_InvCreator;
import net.md_5.bungee.api.ChatColor;

public class Join implements Listener {

	private main plugin;

	public Join(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onCommandExec(CommandTrigger1vs1 e) {
		if(e.getCMD().equalsIgnoreCase("join") && plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
			Player p = (Player) e.getPlayer();
			String[] args = e.getArgs();
			 if(!plugin.isInOneVsOnePlayers(p.getUniqueId())) {
				 p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("notIn1vs1").replaceAll("%Prefix%", plugin.prefix)));
				 return;
			 }
			 if(args.length == 1) {
			  if(p.hasPermission("1vs1.command.join") || p.hasPermission("1vs1.User") || p.hasPermission("1vs1.*")) {
			   if(Bukkit.getPlayer(args[0]) != null) {
				UUID uuid = Bukkit.getPlayer(args[0]).getUniqueId();
				TournamentManager tMgr = plugin.tournaments.get(uuid);
				if(!tMgr.getPassword().equalsIgnoreCase("")) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentwrongPassword").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix)));
					   return;
				}
				 if(tMgr.getMaxPlayers() > -1 && tMgr.getPlayerList().size() >= tMgr.getMaxPlayers()) {
					 p.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentIsFull")));
					 SoundManager manager = new SoundManager(JSound.DRUM, p, 10.0F, 1.0F);
					 manager.play();
					 return;
				 }
				 if(!tMgr.isOpened()) {
					 p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentwrongPassword").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix)));
					 return;
				 }
					 join(p, tMgr);
					 ChallangeManager.removePlayerComplete(p);
				 
				   Tournament_InvCreator creator = new Tournament_InvCreator(plugin);
				   creator.reGenerateInv(tMgr.getOwnerUUID());
				  
			   } else {
				   p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentNotExists").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix)));
			   }
			  } else {
				  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noPerms")));
			  }
			  
			  
			 } else if(args.length == 2) {
			  if(p.hasPermission("1vs1.command.join") || p.hasPermission("1vs1.User") || p.hasPermission("1vs1.*")) {
			   if(Bukkit.getPlayer(args[0]) != null) {
				UUID uuid = Bukkit.getPlayer(args[0]).getUniqueId();
				TournamentManager tMgr = plugin.tournaments.get(uuid);
				 if(tMgr.getPassword().equalsIgnoreCase(args[1])) {
					 if((tMgr.getMaxPlayers() > -1 && tMgr.getPlayerList().size() >= tMgr.getMaxPlayers())) {
						 p.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentIsFull")));
						 SoundManager manager = new SoundManager(JSound.DRUM, p, 10.0F, 1.0F);
						 manager.play();
						 return;
					 }
					 if(!tMgr.isOpened()) {
						 p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentwrongPassword").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix)));
						 return;
					 }
					 
						 join(p, tMgr);
						 ChallangeManager.removePlayerComplete(p);
					 
				 } else {
					 p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentwrongPassword").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix)));
				 }
				} else {
				  p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentNotExists").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix)));
				}
				 
				 
			  } else {
				p.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentJoinWrongUsage")));
						
			  }
			 } else {
				 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noPerms")));
			 }
		}
	}
	
	public void join(Player p, TournamentManager state) {
		if(state.isStarted()) {
			p.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentAlreadyStarted")));
			return;
		}
		if(plugin.getOneVsOnePlayer(p).getPlayertournament() != null) {
			p.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentAlreadyInTournament")));
			return;
		}
		if(!state.isInTournament(p.getUniqueId())) {
			p.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentJoined")));
			state.addPlayer(p);
			
		} else {
			p.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentAlreadyInTournament")));
		}

	}
}
