package de.OnevsOne.Commands.VariableCommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import de.OnevsOne.main;
import de.OnevsOne.Commands.VariableCommands.Manager.CommandTrigger1vs1;
import de.OnevsOne.Listener.Manager.TeamManager;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.States.TeamPlayer;

public class Team implements Listener{

	private main plugin;

	public Team(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onCommandExec(CommandTrigger1vs1 e) {
		if(e.getCMD().equalsIgnoreCase("team") && plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
			Player p = (Player) e.getPlayer();
			
			if(p.hasPermission("1vs1.team") || p.hasPermission("1vs1.*") || p.hasPermission("1vs1.User")) {
			
			  if(e.getArgs().length <= 0) {
				  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamCommandUsage")));
				  return;
			  }
 			  if(e.getArgs()[0].equalsIgnoreCase("list") || e.getArgs()[0].equalsIgnoreCase("info")) {
 				 if(plugin.getOneVsOnePlayer(p).getPlayerTeam() == null) {
 					 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamYouHaveNoTeam")));
 					 return;
 				 }
			   	TeamPlayer tp = plugin.getOneVsOnePlayer(p).getPlayerTeam();
			   	p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamCommandInfoOwner").replaceAll("%Leader%", Bukkit.getPlayer(tp.getPlayer().getUniqueId()).getDisplayName())));
			   	int i = 1;
			   	for(Player mates : tp.getTeamMates()) {
			   	 
			   	 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamCommandInfoMates").replaceAll("%Number%", "" + i).replaceAll("%Name%", Bukkit.getPlayer(mates.getUniqueId()).getDisplayName())));
			   	 i++;
			   	}
			   	
			  } else if(e.getArgs()[0].equalsIgnoreCase("leave")) {
				  if(plugin.getOneVsOnePlayer(p).getPlayerTeam() == null) return;
				 
				  if(plugin.getOneVsOnePlayer(p).getPlayerTeam().getPlayer().getUniqueId().equals(p.getUniqueId())) {
					  
					  TeamManager.deleteTeam(p);
				  } else {
					  TeamManager.removePlayerTeam(p);
				  }
				 
				 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("youLeftTheTeam")));
			  } else if(e.getArgs()[0].equalsIgnoreCase("invite")) {
				  
				  if(e.getArgs().length != 2) {
					  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamCommandInfoMates")));
					  return;
				  }
				  
				  if(Bukkit.getPlayer(e.getArgs()[1]) == null) {
					  
				   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("playerNotFound")));
				   return;
				  }
				  if(plugin.getOneVsOnePlayer(p).getPlayerTeam() != null) {
				   if(!plugin.getOneVsOnePlayer(p).getPlayerTeam().getPlayer().getUniqueId().equals(e.getPlayer().getUniqueId())) {
					   e.getPlayer().sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamNoLeader")));
					   return;
				   }
				  }
				  
				  Player sendTo = Bukkit.getPlayer(e.getArgs()[1]);
				  
				  if(sendTo.getUniqueId().equals(p.getUniqueId())) {
					  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamYouCannotInviteYou")));
					  return;
				  }
				  
				  if(plugin.getOneVsOnePlayer(sendTo).getPlayerTeam() != null) {
					  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamPlayerAlreadyHasTeam")));
					  return;
				  }
				  
				  if(plugin.getOneVsOnePlayer(p).getTeamInvited().contains(sendTo)) {
					   
					   
						   /*Team Anfrage entfernen*/
						   TeamManager.removeTeam(p, sendTo);
						   
						   sendTo.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamQueryRemovedReceiver")).replaceAll("%Player%", p.getDisplayName()));
						   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamQueryRemovedSender")).replaceAll("%Player%", sendTo.getDisplayName()));
					   
					   
				   } else {
					   
						   /*Team Anfrage senden*/
						   TeamManager.sendTeam(p, sendTo);
						   
						   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamQuerySendSender")).replaceAll("%Player%", sendTo.getDisplayName()));
						   sendTo.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamQuerySendReceiver")).replaceAll("%Player%", p.getDisplayName()));
					   //TODO KEINE DOPPEL EINLADUNGEN!
					   
				   }
			  } else if(e.getArgs()[0].equalsIgnoreCase("accept")) {
				  //TODO CHECK OB ANFRAGE GESTELLT WURDE
				  
				  if(e.getArgs().length != 2) {
					  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamCommandAcceptUsage")));
					  return;
				  }
				  
				  if(Bukkit.getPlayer(e.getArgs()[1]) == null) {
					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("playerNotFound")));
					return;
				  }
					
				  
				  
				  Player owner = Bukkit.getPlayer(e.getArgs()[1]);
				  
				  if(owner.getUniqueId().equals(p.getUniqueId())) {
					  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamYouCannotInviteYou")));
					  return;
				  }
				  
				  if(plugin.getOneVsOnePlayer(p).getTeamInvitedBy().contains(owner)) {
					  if(TeamManager.canJoinTeam(plugin.getOneVsOnePlayer(owner).getPlayerTeam(), owner)) {
						  TeamManager.acceptTeam(owner, p);
						  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamTeamJoined")));
					   } else {
						   owner.sendMessage("§cDas Team ist voll!");
						   p.sendMessage("§cDas Team ist voll!");
						   TeamManager.removeTeam(owner, p);
						   //TODO
					   }
					  
				   
				   plugin.getOneVsOnePlayer(p).setInQueue(false);
				  } else {
				   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamYouGotNoTeamRequest")));
				   return;
				  }
				  
			  } else if(e.getArgs()[0].equalsIgnoreCase("kick")) {
				  if(e.getArgs().length == 2) {
					  if(plugin.getOneVsOnePlayer(e.getPlayer()).getPlayerTeam() != null) {
					   if(plugin.getOneVsOnePlayer(e.getPlayer()).getPlayerTeam().getPlayer().getUniqueId().equals(e.getPlayer().getUniqueId())) {
						   TeamPlayer team = plugin.getOneVsOnePlayer(e.getPlayer()).getPlayerTeam();
						   
						   if(Bukkit.getPlayer(e.getArgs()[1]) == null) {
							   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("playerNotFound")));
							   return;
						   }
						   
						   Player target = Bukkit.getPlayer(e.getArgs()[1]);
						   
						   if(team.getTeamMates().contains(target)) {
							   	  if(plugin.getOneVsOnePlayer(e.getPlayer()).getPlayerTeam() == null) return;
								 
								  if(plugin.getOneVsOnePlayer(target).getPlayerTeam().getPlayer().getUniqueId().equals(target.getUniqueId())) {
									  
									  TeamManager.deleteTeam(target);
								  } else {
									  TeamManager.removePlayerTeam(target);
								  }
								  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamYouGotKicked").replaceAll("%Player%", target.getDisplayName())));
								  target.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamPlayerKicked")));
						   } else {
							   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamPlayerIsNotInYourTeam")).replaceAll("%Player%", p.getDisplayName()));
						   }
						   
					   } else {
						   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamNoLeader")));
					   }
					  } else {
						 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamYouHaveNoTeam"))); 
					  }
				  } else {
					  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamKickUsage")));
				  }
			  } else {
				  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamCommandUsage")));
			  }
 			
			} else {
				p.sendMessage(plugin.noPerms);
			}
		}
	}
}