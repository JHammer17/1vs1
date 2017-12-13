package de.OnevsOne.Listener.Manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachmentInfo;

import de.OnevsOne.main;

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.States.PlayerState;
import de.OnevsOne.States.TeamPlayer;

public class TeamManager implements Listener {

	private static main plugin;

	@SuppressWarnings("static-access")
	public TeamManager(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	HashMap<UUID, Long> timeout = new HashMap<>();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onTeam(PlayerInteractEntityEvent e) {
	 if(plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
	  if(e.getRightClicked() instanceof Player) {
	   Player clicker = e.getPlayer();
	   Player clicked = (Player) e.getRightClicked();
	   if(plugin.isInOneVsOnePlayers(clicked.getUniqueId())) {
		if(plugin.getOneVsOnePlayer(clicker).getpState() == PlayerState.InLobby && plugin.getOneVsOnePlayer(clicked).getpState() == PlayerState.InLobby) {
		   
			
			if(timeout.containsKey(clicker.getUniqueId()) && System.currentTimeMillis()-timeout.get(clicker.getUniqueId()) <= 250)
				return;
			
			
			timeout.put(clicker.getUniqueId(), System.currentTimeMillis());
			
		   if(clicker.getItemInHand().getTypeId() == plugin.ChallangerItemID) {
			ItemStack stack = clicker.getItemInHand();
			if(stack.hasItemMeta() && 
			   stack.getItemMeta().hasDisplayName() && 
			   stack.getItemMeta().getDisplayName().equalsIgnoreCase(plugin.msgs.getMsg("challangeItemLobbyName"))) {
				
				if(plugin.getOneVsOnePlayer(clicker).getTeam() != null && !plugin.getOneVsOnePlayer(clicker).getPlayer().getUniqueId().equals(clicker.getUniqueId())) {
				 clicker.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamNoLeader")));
				 return;	
				}
				
				
				if(plugin.getOneVsOnePlayer(clicker).getTeamInvitedBy() == null || plugin.getOneVsOnePlayer(clicker).getTeamInvitedBy().isEmpty()) {
				   /*Anfrage senden*/
				   if(plugin.getOneVsOnePlayer(clicked).getTeam() != null) {
						 clicker.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamPlayerAlreadyHasTeam")));
						 return;
						}
						
				   if(plugin.getOneVsOnePlayer(clicker).getTeamInvited() != null && !plugin.getOneVsOnePlayer(clicker).getTeamInvited().isEmpty() && plugin.getOneVsOnePlayer(clicker).getTeamInvited().contains(clicked)) {
					   /*Team Anfrage entfernen*/
					   removeTeam(clicker, clicked);
					   
					   clicked.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamQueryRemovedReceiver")).replaceAll("%Player%", clicker.getDisplayName()));
					   clicker.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamQueryRemovedSender").replaceAll("%Player%", clicked.getDisplayName())));
				   } else {
					   /*Team Anfrage senden*/
					   sendTeam(clicker, clicked);
					   
					   clicker.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamQuerySendSender")).replaceAll("%Player%", clicked.getDisplayName()));
					   clicked.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamQuerySendReceiver")).replaceAll("%Player%", clicker.getDisplayName()));
				   }
				   
				  
				   
			   } else {
				   /*Team angenommen!*/
				   
				   if(plugin.getOneVsOnePlayer(clicker).getTeamInvitedBy().contains(clicked)) {
					   
					   if(canJoinTeam(plugin.getOneVsOnePlayer(clicked).getPlayerTeam(), clicked)) {
						   acceptTeam(clicked, clicker);
						   clicker.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamTeamJoined")));
					   } else {
						   clicked.sendMessage(plugin.msgs.getMsg("teamFull"));
						   clicker.sendMessage(plugin.msgs.getMsg("teamFull"));
						   removeTeam(clicked, clicker);
						   
					   }
					   
					   
				   } else {
					   if(plugin.getOneVsOnePlayer(clicked).getTeam() != null) {
							 clicker.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamPlayerAlreadyHasTeam")));
							 return;
							}
							
					   if(plugin.getOneVsOnePlayer(clicker).getTeamInvited() != null && plugin.getOneVsOnePlayer(clicker).getTeamInvited().contains(clicked)) {
						   /*Team Anfrage entfernen*/
						   removeTeam(clicker, clicked);
						   
						   clicked.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamQueryRemovedReceiver")).replaceAll("%Player%", clicker.getDisplayName()));
						   clicker.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamQueryRemovedSender").replaceAll("%Player%", clicked.getDisplayName())));
					   } else {
						   /*Team Anfrage senden*/
						   sendTeam(clicker, clicked);
						   
						   clicker.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamQuerySendSender").replaceAll("%Player%", clicked.getDisplayName())));
						   clicked.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamQuerySendReceiver").replaceAll("%Player%", clicker.getDisplayName())));
					   }
				   }
				   
			   }
				
				
			}
		   } 
		}
	   }
	  }
	 }
		
	}
	
	public static void sendTeam(Player sender, Player receiver) {
		   ArrayList<Player> teamChallangedList = new ArrayList<>();
		   if(plugin.getOneVsOnePlayer(sender).getTeamInvited() != null) {
			teamChallangedList = plugin.getOneVsOnePlayer(sender).getTeamInvited();
		   }
		   teamChallangedList.add(receiver);
		   
		   plugin.getOneVsOnePlayer(sender).setTeamInvited(teamChallangedList);
		  
		   
		   ArrayList<Player> teamChallangedByList = new ArrayList<>();
		   if(plugin.getOneVsOnePlayer(receiver).getTeamInvitedBy() != null) {
			   teamChallangedByList = plugin.getOneVsOnePlayer(receiver).getTeamInvitedBy();
		   }
		   teamChallangedByList.add(sender);
		   
		   plugin.getOneVsOnePlayer(receiver).setTeamInvitedBy(teamChallangedByList);
	}
	
	public static void removeTeam(Player sender, Player receiver) {
		   ArrayList<Player> teamChallangedList = new ArrayList<>();
		   if(plugin.getOneVsOnePlayer(sender).getTeamInvited() != null) teamChallangedList = plugin.getOneVsOnePlayer(sender).getTeamInvited();
		   teamChallangedList.remove(receiver);
		   
		   plugin.getOneVsOnePlayer(sender).setTeamInvited(teamChallangedList);
		   
		   
		   
		   ArrayList<Player> teamChallangedByList = new ArrayList<>();
		   if(plugin.getOneVsOnePlayer(receiver).getTeamInvitedBy() != null) teamChallangedByList = plugin.getOneVsOnePlayer(receiver).getTeamInvitedBy();
		   teamChallangedByList.remove(sender);
		  
		   plugin.getOneVsOnePlayer(receiver).setTeamInvitedBy(teamChallangedByList);
		   
	}

	public static void acceptTeam(Player owner, Player player) {
		TeamPlayer tp = null;
		ArrayList<Player> teammates = new ArrayList<>();
		if(plugin.getOneVsOnePlayer(owner).getPlayerTeam() != null) {
		 tp = plugin.getOneVsOnePlayer(owner).getPlayerTeam();
		 teammates = tp.getTeamMates();
		}
		
		if(tp == null) tp = new TeamPlayer(owner, teammates);
			
		
		
		 if(plugin.getOneVsOnePlayer(owner).getPlayerTeam() == null || owner.hasPermission("1vs1.Premium")) {
			 tp.addPlayer(player);
			 
			 for(Player players : tp.getAll()) {
			  plugin.getOneVsOnePlayer(players).setPlayerTeam(tp);
			  
			  if(!players.getUniqueId().equals(player.getUniqueId())) {
				  players.sendMessage(plugin.msgs.getMsg("teamEntered").replaceAll("%Player%", player.getDisplayName()));
			  }
			 }
			 removeTeam(owner, player);
			 
			 
		 } else {
			 owner.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamAlreadyInTeam")).replaceAll("%Player%", player.getDisplayName()));
			 player.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamAlreadyInTeam")));
		 }
		
	}
	
	
	public static void removePlayerTeam(Player p) {
		if(plugin.getOneVsOnePlayer(p).getPlayerTeam() == null) return;
		
		 TeamPlayer tp = plugin.getOneVsOnePlayer(p).getPlayerTeam();
		 
		 
		 tp.removePlayer(p);
			 
		 for(Player players : tp.getAll()) {
		  plugin.getOneVsOnePlayer(players).setPlayerTeam(tp);
		  players.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamPlayerLeftTeamOthers").replaceAll("%Player%", p.getDisplayName())));
		 }
		 plugin.getOneVsOnePlayer(p).setPlayerTeam(null);
	}
	
	public static void deleteTeam(Player owner) {
		if(plugin.getOneVsOnePlayer(owner).getPlayerTeam() == null) return;
		
		TeamPlayer tp = plugin.getOneVsOnePlayer(owner).getPlayerTeam();
		
		if(!tp.getPlayer().getUniqueId().equals(owner.getUniqueId())) return;
		
		for(Player players : tp.getAll()) {
		 plugin.getOneVsOnePlayer(players).setPlayerTeam(null);
		 players.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamDeleted")));
		}
	}
	
	public static boolean canJoinTeam(TeamPlayer player, Player owner) {
		
		
		
		if(player == null && owner.hasPermission("1vs1.User")) return true;
		if(player == null && owner.hasPermission("1vs1.Premium")) return true;
		if(player == null && owner.hasPermission("1vs1.Admin")) return true;
		if(player == null && owner.hasPermission("1vs1.Team.Special")) return true;
		if(player == null && owner.hasPermission("1vs1.Team.User")) return true;
		if(player == null && owner.hasPermission("1vs1.Team.Premium")) return true;
		if(player == null) return false;
		
		
		//if(owner.hasPermission("1vs1.Team.*") || owner.hasPermission("1vs1.Admin")) return true;
		
		int check = player.getAll().size();
		check++;
		int result = checkTeamFullPermission(owner, check);
		if(result == 2) return true;
		if(result == 1) return false;
		
		
		
		if(owner.hasPermission("1vs1.Team.*") || owner.hasPermission("1vs1.Admin")) {
			return true;
		} else if(owner.hasPermission("1vs1.Team.Special")) {
			if(plugin.maxTeamSizeSpecial > player.getAll().size()) {
				return true;
			}
			
			
			
			
			
			
		} else if(owner.hasPermission("1vs1.Team.Premium")  || owner.hasPermission("1vs1.Premium")) {
			if(plugin.maxTeamSizePremium > player.getAll().size()) {
				return true;
			}
		} else if(owner.hasPermission("1vs1.Team.User") || owner.hasPermission("1vs1.User")) {
			if(plugin.maxTeamSizeUser > player.getAll().size()) {
				return true;
			}
		} 
		return false;
	}
	
	
	
	/**
	 * 
	 * 
	 * @param p
	 * @param toCheck
	 * @return 0: Team not Full 1: Team Full 2: Team Inf.
	 */
	private static int checkTeamFullPermission(Player p, int toCheck) {
		for(PermissionAttachmentInfo at : p.getEffectivePermissions()) {
			if(at == null || at.getPermission() == null) continue;
			
			if(at.getPermission().toLowerCase().contains("1vs1.team.size.")) {
				if(at.getPermission().toLowerCase().split("1vs1.team.size.").length >= 0) {
					try {
						int max = Integer.parseInt(at.getPermission().toLowerCase().split("1vs1.team.size.")[1]);
						if(toCheck > max) return 1;
					} catch (NumberFormatException e) {
						if(at.getPermission().toLowerCase().split("1vs1.team.size.")[1].equalsIgnoreCase("*")) {
							return 2;
						}
					}
					
					
				}
				  
				break;
			}
		}
		return 0;
	}
	
	
}
