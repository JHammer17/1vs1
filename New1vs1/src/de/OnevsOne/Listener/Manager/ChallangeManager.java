package de.OnevsOne.Listener.Manager;

import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import de.OnevsOne.main;
import de.OnevsOne.Arena.Manager.ArenaJoin;
import de.OnevsOne.Kit_Methods.KitManager;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.ScoreBoardManager;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.Methods.Queue.QueueManager;
import de.OnevsOne.States.PlayerState;
import de.OnevsOne.States.ArenaTeamPlayer;
import net.md_5.bungee.api.ChatColor;

public class ChallangeManager implements Listener {

	private static main plugin;

	@SuppressWarnings("static-access")
	public ChallangeManager(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChallange(EntityDamageByEntityEvent e) {
	 if((e.getEntity() instanceof Player) && (e.getDamager() instanceof Player)) {
			
		 Player p1 = (Player) e.getDamager();
		 Player p2 = (Player) e.getEntity();
			  
	  if(plugin.isInOneVsOnePlayers(p1.getUniqueId()) && plugin.isInOneVsOnePlayers(p2.getUniqueId())) {
	   if(plugin.getOneVsOnePlayer(p1).getpState() == PlayerState.InLobby &&
			   plugin.getOneVsOnePlayer(p2).getpState() == PlayerState.InLobby) {
		   
		  if(plugin.getOneVsOnePlayer(p1).getPlayertournament() != null) {
 		   p1.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentinTournament")));
 		   return;
 		  }
 		  if(plugin.getOneVsOnePlayer(p2).getPlayertournament() != null) {
 		   return;
 		  }
 		  if(plugin.getOneVsOnePlayer(p1).getPlayerTeam() != null) {
 			  if(!plugin.getOneVsOnePlayer(p1).getPlayerTeam().getPlayer().getUniqueId().equals(p1.getUniqueId())) {
 				  p1.sendMessage("§cDu bist nicht der Leiter deines Teams!");
 				  return;
 			  }
 		  }
 		  
 		 if(plugin.getOneVsOnePlayer(p2).getPlayerTeam() != null) {
			  if(!plugin.getOneVsOnePlayer(p2).getPlayerTeam().getPlayer().getUniqueId().equals(p2.getUniqueId())) {
				  p1.sendMessage("§c" + p2.getDisplayName() +" ist nicht der Leiter seines Teams!");
				  return;
			  }
		  }
 		  
		   
 		  if(plugin.getOneVsOnePlayer(p1).getPlayerTeam() != null && 
 			 plugin.getOneVsOnePlayer(p1).getPlayerTeam().getAll().contains(p2)) {
 			 p1.sendMessage(plugin.msgs.getMsg("cannotChallangeTeamMates"));
 			  return;
 		  }
 		  
 		 if(plugin.getOneVsOnePlayer(p2).getPlayerTeam() != null && 
 	 		plugin.getOneVsOnePlayer(p2).getPlayerTeam().getAll().contains(p1)) {
 	 	  p1.sendMessage(plugin.msgs.getMsg("cannotChallangeTeamMates"));
 	 	  return;
 	 	 }
 		  
 		  
 		  p1.setNoDamageTicks(0);
 		  p2.setNoDamageTicks(0);
 		  
 		 if(p1.getItemInHand().getTypeId() == plugin.ChallangerItemID && 
 			p1.getItemInHand().hasItemMeta() && p1.getItemInHand().getItemMeta().hasDisplayName() && 
 			p1.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(plugin.msgs.getMsg("challangeItemLobbyName"))) {
 					    
 				   e.setCancelled(true);
 				   e.setDamage(0);
 		  
 		  /* 								Challange Angenommen       								  */		   
 		  if(plugin.getOneVsOnePlayer(p1).getChallangedBy() != null && 
 			 plugin.getOneVsOnePlayer(p1).getChallangedBy().contains(p2)) {
 			 
 			 if(plugin.FreeArenas.size() == 0) {
		   		 p1.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noFreeArenas"), p1.getDisplayName(), p2.getDisplayName()));
		   		 p2.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noFreeArenas"), p2.getDisplayName(), p1.getDisplayName()));
		   		 removePlayerComplete(p1);
		   		 removePlayerComplete(p2);
		   		 return;
		   	 }
		   	 
		   	 
		   	 
		   	 if(QueueManager.getRandomMap(p1, p2) == null) {
		   	  String Map = QueueManager.getRandomMap(p2);
		   	  
		   	  if(Map == null) {
		   		removePlayerComplete(p1);
		   		 removePlayerComplete(p2);
		   		 p1.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noFreeArenas")));
		   		 p2.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noFreeArenas")));
		   		 return;
		   	  }
		   	  
		   	  
		   	  if(plugin.getOneVsOnePlayer(p1).getPlayerTeam() != null && 
		   	     plugin.getOneVsOnePlayer(p2).getPlayerTeam() != null) {
		   		
		   		  
		   		ArenaTeamPlayer playerP1 = new ArenaTeamPlayer(plugin.getOneVsOnePlayer(p1).getPlayerTeam().getPlayer(), plugin.getOneVsOnePlayer(p1).getPlayerTeam().getTeamMates());
		   		ArenaTeamPlayer playerP2 = new ArenaTeamPlayer(plugin.getOneVsOnePlayer(p2).getPlayerTeam().getPlayer(), plugin.getOneVsOnePlayer(p2).getPlayerTeam().getTeamMates());
		   		
		   		String Kit = plugin.getOneVsOnePlayer(p2).getKitLoaded();
		   		String subID = "d";
		   		
		   		if(plugin.getOneVsOnePlayer(p2).getKitLoaded() == null) Kit = p2.getName();
		   		
		   		if(Kit.contains(":")) subID = Kit.split(":")[1];
		   		
		   		
		   		if(plugin.getDBMgr().isNameRegistered(Kit.split(":")[0])) 
		   	     Kit = plugin.getDBMgr().getUUID(Kit.split(":")[0]).toString() + ":" + Kit.split(":")[1];
		   		
		   		
		   		ArenaJoin.joinArena(playerP2, playerP1, Map, false, Kit, false, subID);
		   	  } else {
		   		if(plugin.getOneVsOnePlayer(p1).getPlayerTeam() != null || 
		   		   plugin.getOneVsOnePlayer(p2).getPlayerTeam() != null) {
		   			if(plugin.getOneVsOnePlayer(p1).getPlayerTeam() != null) {
		   				removePlayerComplete(p1);
				   		 removePlayerComplete(p2);
				   		 
				   		 p1.sendMessage("§cDein Gegner hat kein Team!");
				   		 p2.sendMessage("§cDu hast kein Team!");
		   				return;
		   			}
		   			if(plugin.getOneVsOnePlayer(p2).getPlayerTeam() != null) {
		   				 removePlayerComplete(p1);
				   		 removePlayerComplete(p2);
				   		 p2.sendMessage("§cDein Gegner hat kein Team!");
				   		 p1.sendMessage("§cDu hast kein Team!");
		   				return;
		   			}
		   			
		   			String Kit = plugin.getOneVsOnePlayer(p2).getKitLoaded();
			   		String subID = "d";
			   		
			   		if(plugin.getOneVsOnePlayer(p2).getKitLoaded() == null) Kit = p2.getName();
			   		
			   		if(Kit.contains(":")) subID = Kit.split(":")[1];
			   		
			   		
			   		if(plugin.getDBMgr().isNameRegistered(Kit.split(":")[0])) 
			   			Kit = plugin.getDBMgr().getUUID(Kit.split(":")[0]).toString() + ":" + Kit.split(":")[1];
			   		
		   			ArenaJoin.joinArena(p2, p1, Map, false, Kit, false, subID);
		   		}
		   		
		   		
		   		String Kit = plugin.getOneVsOnePlayer(p2).getKitLoaded();
		   		String subID = "d";
		   		
		   		if(plugin.getOneVsOnePlayer(p2).getKitLoaded() == null) Kit = p2.getName();
		   		
		   		if(Kit.contains(":")) subID = Kit.split(":")[1];
		   		
		   		
		   		if(plugin.getDBMgr().isNameRegistered(Kit.split(":")[0])) {
		   			if(Kit.split(":").length >= 2) {
		   				Kit = plugin.getDBMgr().getUUID(Kit.split(":")[0]).toString() + ":" + Kit.split(":")[1];
		   			} else {
		   				Kit = plugin.getDBMgr().getUUID(Kit.split(":")[0]).toString();
		   			}
		   			
		   		}
		   		ArenaJoin.joinArena(p2, p1, Map, false, Kit, false, subID);
		   	  }
		   	 } else {
		   		String Map = QueueManager.getRandomMap(p1, p2);
		   		 
		   		if(plugin.getOneVsOnePlayer(p1).getPlayerTeam() != null && 
		   		   plugin.getOneVsOnePlayer(p2).getPlayerTeam() != null ) {
			   		
			   		  
			   		ArenaTeamPlayer playerP1 = new ArenaTeamPlayer(plugin.getOneVsOnePlayer(p1).getPlayerTeam().getPlayer(), plugin.getOneVsOnePlayer(p1).getPlayerTeam().getTeamMates());
			   		ArenaTeamPlayer playerP2 = new ArenaTeamPlayer(plugin.getOneVsOnePlayer(p2).getPlayerTeam().getPlayer(), plugin.getOneVsOnePlayer(p2).getPlayerTeam().getTeamMates());
			   		
			   		String Kit = plugin.getOneVsOnePlayer(p2).getKitLoaded();
			   		String subID = "d";
			   		
			   		if(plugin.getOneVsOnePlayer(p2).getKitLoaded() == null) Kit = p2.getName();
			   		
			   		if(Kit.contains(":")) subID = Kit.split(":")[1];
			   		
			   		
			   		if(plugin.getDBMgr().isNameRegistered(Kit.split(":")[0])) {
			   			if(Kit.split(":").length >= 2) {
			   				Kit = plugin.getDBMgr().getUUID(Kit.split(":")[0]).toString() + ":" + Kit.split(":")[1];
			   			} else {
			   				Kit = plugin.getDBMgr().getUUID(Kit.split(":")[0]).toString();
			   			}
			   			
			   		}
			   		
			   		
			   		
			   		ArenaJoin.joinArena(playerP2, playerP1, Map, false, Kit, false, subID);
			   	  } else {
			   		if(plugin.getOneVsOnePlayer(p1).getPlayerTeam() != null || 
			   		   plugin.getOneVsOnePlayer(p2).getPlayerTeam() != null) {
			   			if(plugin.getOneVsOnePlayer(p1) != null) {
			   				removePlayerComplete(p1);
					   		 removePlayerComplete(p2);
					   		 
					   		 p1.sendMessage(plugin.msgs.getMsg("teamEnemyHasNoTeam"));
					   		 p2.sendMessage(plugin.msgs.getMsg("teamYouHaveNoTeam"));
			   				return;
			   			}
			   			if(plugin.getOneVsOnePlayer(p2) != null) {
			   				 removePlayerComplete(p1);
					   		 removePlayerComplete(p2);
					   		 p2.sendMessage(plugin.msgs.getMsg("teamEnemyHasNoTeam"));
					   		 p1.sendMessage(plugin.msgs.getMsg("teamYouHaveNoTeam"));
			   				return;
			   			}
			   			String Kit = plugin.getOneVsOnePlayer(p2).getKitLoaded();
				   		String subID = "d";
				   		
				   		if(plugin.getOneVsOnePlayer(p2).getKitLoaded() == null) Kit = p2.getName();
				   		
				   		if(Kit.contains(":")) subID = Kit.split(":")[1];
				   		
				   		
				   		if(plugin.getDBMgr().isNameRegistered(Kit.split(":")[0])) 
				   			Kit = plugin.getDBMgr().getUUID(Kit.split(":")[0]).toString() + ":" + Kit.split(":")[1];
				   		
			   			ArenaJoin.joinArena(p2, p1, Map, false, Kit, false, subID);
			   			
			   		}
			   		
			   		
			   		
			   		String Kit = plugin.getOneVsOnePlayer(p2).getKitLoaded();
			   		String subID = "d";
			   		
			   		if(Kit == null) Kit = p2.getName() + ":" + "d";
			   		
			   		if(Kit.contains(":")) subID = Kit.split(":")[1];
			   		
			   		
			   		if(plugin.getDBMgr().isNameRegistered(Kit.split(":")[0])) 
			   			Kit = plugin.getDBMgr().getUUID(Kit.split(":")[0]).toString() + ":" + Kit.split(":")[1];
			   		
			   		
			   		
			   		ArenaJoin.joinArena(p2, p1, Map, false, Kit, false, subID);
			   	  }
			  	
		   	 }
		   	 
		   	 removePlayerComplete(p1);
	   		 removePlayerComplete(p2);
		   	 
			 
			 ScoreBoardManager.updateBoard(p1, false);
			 ScoreBoardManager.updateBoard(p2, false);
 		  } else {
 			 
 		    if(plugin.getOneVsOnePlayer(p1).getChallanged() != null && 
 		       plugin.getOneVsOnePlayer(p1).getChallanged().contains(p2)) {
 		  		 
 		  	  revokeChallange(p1, p2);
 		  	  
 		   	  p1.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("challangeRevoked"), p1.getDisplayName(), p2.getDisplayName(), null));
		   	  p2.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("challangeRevoked2"), p2.getDisplayName(), p1.getDisplayName(), null));
		   	  SoundManager manager = new SoundManager(JSound.DRUM, p1, 30.0F, 1.0F);
			  manager.play();
			  manager = new SoundManager(JSound.DRUM, p2, 30.0F, 1.0F);
		      manager.play();
		   	  
		   	  ScoreBoardManager.updateBoard(p1, false);
		   	  ScoreBoardManager.updateBoard(p2, false);	 
		   	  return;
 		    }
 		    
 		    
 		    	
 		    	
 		    	sendChallange(p1, p2);
 		    	final Player fP1 = p1;
 		    	final Player fP2 = p2;
 		    	p1.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("challangeSended"), p1.getDisplayName(), p2.getDisplayName(), null));
 			    Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						String sKit = new KitManager(plugin).getSelectedKit(fP1);
						
						if(sKit.endsWith(":")) {
							fP2.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("challangeReceived")).replaceAll("%Player2%", fP1.getDisplayName()).replaceAll("%Kit%", sKit.split(":")[0]));
						} else {
							fP2.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("challangeReceived")).replaceAll("%Player2%", fP1.getDisplayName()).replaceAll("%Kit%", sKit));
						}
						
						
						
					}
				});
 			    t.start();
 				SoundManager manager = new SoundManager(JSound.ORB_PLING, p1, 30.0F, 1.0F);
 				manager.play();
 				manager = new SoundManager(JSound.ORB_PLING, p2, 30.0F, 1.0F);
 				manager.play();
 				
 				
 				ScoreBoardManager.updateBoard(p1, false);
 				ScoreBoardManager.updateBoard(p2, false);
 		    }
 		   
 		  
 		  
 		  
 		 }
	   }
	  }
	 
	 }
	}
	
	@SuppressWarnings("unchecked")
	public static void removePlayerComplete(Player p) {
		
		ArrayList<Player> saveList = (ArrayList<Player>) plugin.getOneVsOnePlayer(p).getChallangedBy().clone();
		
		for(Player challanged : saveList) plugin.getOneVsOnePlayer(challanged).getChallanged().remove(p);
		
		saveList = (ArrayList<Player>) plugin.getOneVsOnePlayer(p).getChallanged().clone();
		
		for(Player challangedBy : saveList) plugin.getOneVsOnePlayer(challangedBy).getChallangedBy().remove(p);
		saveList.clear();
		plugin.getOneVsOnePlayer(p).setChallanged(saveList);
		plugin.getOneVsOnePlayer(p).setChallangedBy(saveList);
	}
	
	
	public static void revokeChallange(Player sender, Player receiver) {
		   ArrayList<Player> saveList = new ArrayList<Player>();
		   /*Herausforderung aus Herausforderer Liste entfernen*/
		   if(plugin.getOneVsOnePlayer(sender) != null) 
			   saveList = plugin.getOneVsOnePlayer(sender).getChallanged();
		   saveList.remove(receiver);
		   
		   
		   plugin.getOneVsOnePlayer(sender).setChallanged(saveList);
		   
		   
		   
		   /*Herausforderung aus Herausforderten Liste entfernen*/
		   saveList = new ArrayList<Player>();
		   if(plugin.getOneVsOnePlayer(receiver).getChallangedBy() != null) saveList = plugin.getOneVsOnePlayer(receiver).getChallangedBy();
		   saveList.remove(sender);
		   
		   plugin.getOneVsOnePlayer(receiver).setChallangedBy(saveList);
	}
	
	public static void sendChallange(Player sender, Player receiver) {
		   ArrayList<Player> saveList = new ArrayList<Player>();
		   
		   
		   
		   if(plugin.getOneVsOnePlayer(sender).getChallanged() != null) saveList = plugin.getOneVsOnePlayer(sender).getChallanged();
		   saveList.add(receiver);
		   plugin.getOneVsOnePlayer(sender).setChallanged(saveList);
		   
		   
		   saveList = new ArrayList<Player>();
		   if(plugin.getOneVsOnePlayer(receiver).getChallangedBy() != null) saveList = plugin.getOneVsOnePlayer(receiver).getChallangedBy();
		   saveList.add(sender);
		   plugin.getOneVsOnePlayer(receiver).setChallangedBy(saveList);
	}
}
