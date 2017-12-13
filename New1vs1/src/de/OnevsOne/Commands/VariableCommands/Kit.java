package de.OnevsOne.Commands.VariableCommands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import de.OnevsOne.main;
import de.OnevsOne.Commands.VariableCommands.Manager.CommandTrigger1vs1;
import de.OnevsOne.Kit_Methods.KitManager;
import de.OnevsOne.Kit_Methods.KitMessages;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.ScoreBoardManager;
import de.OnevsOne.Methods.getItems;
import de.OnevsOne.States.PlayerState;

public class Kit implements Listener {

	private main plugin;

	public Kit(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public static ArrayList<Player> hasKit = new ArrayList<Player>();
	public static HashMap<Player, Location> savedLoc = new HashMap<Player, Location>();
	
	
	
	@EventHandler
	public void onCommandExec(CommandTrigger1vs1 e) {
		if(e.getCMD().equalsIgnoreCase("kit") && plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
			
			Player p = (Player) e.getPlayer();
			String[] args = e.getArgs();
			
			  if(!p.hasPermission("1vs1.command.kit") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.User")) {
		    		plugin.sendNoPermsMessage(p);
		    		return;
		    	}
			  if(plugin.getOneVsOnePlayer(p).getPlayertournament() != null) {
				  p.sendMessage(plugin.msgs.getMsg("tournamentinTournament"));
				  return;
			  }
			  
			  
			  
			
			   if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit || 
				  plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby) {
				     if(args.length == 1) {
				    	 
						 while(hasKit.contains(p)) hasKit.remove(p);
						 while(savedLoc.containsKey(p)) savedLoc.remove(p);
						 
						 
						 String Player = args[0];
						 
						 int subIDI = 1;
						 boolean hasSubId = false;
						 
						 if(Player.contains(":")) {
						  String[] subID = Player.split(":");
						  if(subID.length == 2) {
						   try {
							   subIDI = Integer.parseInt(subID[1]);
							   hasSubId = true;
							   args[0] = subID[0];
						   } catch(NumberFormatException ee) {
							   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("numberAsSubID"), p.getName()));
							   return;
						   }
						  }
						 }
				       	   
				    	  
				    	 
						 try {
				    		 int id = Integer.parseInt(args[0]);
				    		 if(!(id > 5 || id <= 0)) {
				    			 delKit(p);
				    			 new KitManager(plugin).Kitload(p, p.getUniqueId().toString(),"" + id); 
								 KitMessages.sendAllPrefs(p.getUniqueId(), p,"" + id);
								 if(plugin.getOneVsOnePlayer(p).getpState() != PlayerState.InKitEdit) {
						    		   while(hasKit.contains(p)) {
											 hasKit.remove(p);
										 }
										 while(savedLoc.containsKey(p)) {
											 savedLoc.remove(p);
										 }
							    	   hasKit.add(p);
							    	   savedLoc.put(p, p.getLocation());
							    	   
						    	   }
								 
								 plugin.getOneVsOnePlayer(p).setInQueue(false);
								 if(plugin.getOneVsOnePlayer(p).getpState() != PlayerState.InKitEdit) {
						    		   plugin.getOneVsOnePlayer(p).setKitLoaded(p.getName() + ":" + id);
						    		   ScoreBoardManager.updateBoard(p, true);
						    	   }
				    		 } else {
				    			 if(plugin.getOneVsOnePlayer(p).getpState() != PlayerState.InKitEdit) {
						    		   while(hasKit.contains(p)) 
											 hasKit.remove(p);
										 
										 while(savedLoc.containsKey(p)) 
											 savedLoc.remove(p);
										 
							    	   hasKit.add(p);
							    	   savedLoc.put(p, p.getLocation());
							    	   
						    	   }
								 
				    			 plugin.getOneVsOnePlayer(p).setInQueue(false);
				    			 
				    			 p.sendMessage("§cGebe eine Zahl zwischen 1 und 5 ein!");
				    		 }
				    		 
				    		 
				    		 return;
				       } catch (Exception ee) {}
						 
				    		   if(plugin.getDBMgr().isNameRegistered(args[0])) {
				    			   UUID useid = null;
					    		   try{
					    			   useid = plugin.getDBMgr().getUUID(args[0]);
					    		   } catch(Exception ee) {
					    			   
					    			  
					    			   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("kitNotFound"), p.getDisplayName(), args[0], null, null));
					    			   if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby) {
					    				   getItems.getLobbyItems(p, true);
					    			   }
					    			   return;
					    		   }
					    		   
					    		   
					    		   
					    		   if(plugin.getDBMgr().isUserExists(useid)) {
					    			   if(hasSubId) {
					    				   new KitManager(plugin).Kitload(p, "" + useid, "" + subIDI);
					    			   } else {
					    				   new KitManager(plugin).Kitload(p, "" + useid, "d");
					    			   }
							    	  
					    			   delKit(p);
					    			   
							    	   KitMessages.sendAllPrefs(useid, p, "" + subIDI);
							    	   if(plugin.getOneVsOnePlayer(p).getpState() != PlayerState.InKitEdit) {
							    		   while(hasKit.contains(p)) {
												 hasKit.remove(p);
											 }
											 while(savedLoc.containsKey(p)) {
												 savedLoc.remove(p);
											 }
								    	   hasKit.add(p);
								    	   savedLoc.put(p, p.getLocation());
								    	   
							    	   }
							    	  
							    	   plugin.getOneVsOnePlayer(p).setInQueue(false);
							    	   
							    	   if(plugin.getOneVsOnePlayer(p).getpState() != PlayerState.InKitEdit) {
							    		   plugin.getOneVsOnePlayer(p).setKitLoaded(args[0] + ":" + subIDI);
							    		   ScoreBoardManager.updateBoard(p, true);
							    	   }
							    	   return;
							       }
				    		   }  else {
				    			   
				    			  
				    			   
				    			   if(plugin.getDBMgr().isCustomKitExists(args[0]) == 1) {
					    			   
				    				 
							    	   if(plugin.getOneVsOnePlayer(p).getpState() != PlayerState.InKitEdit) {
							    		   while(hasKit.contains(p)) {
												 hasKit.remove(p);
											 }
											 while(savedLoc.containsKey(p)) {
												 savedLoc.remove(p);
											 }
								    	   hasKit.add(p);
								    	   savedLoc.put(p, p.getLocation());
								    	   
							    	   }
							    	  
							    	   plugin.getOneVsOnePlayer(p).setInQueue(false);
							    	   
							    	   if(plugin.getOneVsOnePlayer(p).getpState() != PlayerState.InKitEdit) {
							    		   plugin.getOneVsOnePlayer(p).setKitLoaded(args[0] + ":" + subIDI);
							    		   ScoreBoardManager.updateBoard(p, true);
							    	   }
					    			   new KitManager(plugin).kitLoadCustomKit(args[0], p);
					    			   KitMessages.sendAllPrefsCustomKit(args[0], p);
					    			   
					    			   
					    			   
					    			   return;
					    		   }
				    			   
				    			   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("kitNotFound"), p.getDisplayName(), args[0], null, null));
				    			   if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby) {
				    				   getItems.getLobbyItems(p, true);
				    			   }
				    		   }
				    		  
				    	   
					 } else if(args.length == 0) {
						 
						 delKit(p);
						 
						 new KitManager(plugin).Kitload(p, p.getUniqueId().toString(),"d"); 
						 KitMessages.sendAllPrefs(p.getUniqueId(), p,"d");
						 if(plugin.getOneVsOnePlayer(p).getpState() != PlayerState.InKitEdit) {
				    		   while(hasKit.contains(p)) {
									 hasKit.remove(p);
								 }
								 while(savedLoc.containsKey(p)) {
									 savedLoc.remove(p);
								 }
					    	   hasKit.add(p);
					    	   savedLoc.put(p, p.getLocation());
					    	   
				    	   }
						 
						 plugin.getOneVsOnePlayer(p).setInQueue(false);
						 if(plugin.getOneVsOnePlayer(p).getpState() != PlayerState.InKitEdit) {
				    		   plugin.getOneVsOnePlayer(p).setKitLoaded(p.getName() + ":d");
				    		   ScoreBoardManager.updateBoard(p, true);
				    	   }
					 } else {
						 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("kitWrongUse"), p.getDisplayName(), null, null, null));
					 }
			   } else {
				   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("notInLobby"), p.getDisplayName(), null, null, null));
			   }
				  
			  
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getWhoClicked() instanceof Player) {
			
		 if(hasKit.contains(e.getWhoClicked()) && e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
		  e.setCancelled(true);
		 }
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(hasKit.contains(e.getPlayer())) {
			if(savedLoc.get(e.getPlayer()).distance(e.getPlayer().getLocation()) > 1) {
				if(plugin.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.InKitEdit) {
					while(hasKit.contains(e.getPlayer())) hasKit.remove(e.getPlayer());
					
				} else {
					 while(hasKit.contains(e.getPlayer())) hasKit.remove(e.getPlayer());
					 while(savedLoc.containsKey(e.getPlayer())) savedLoc.remove(e.getPlayer());
					 if(plugin.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.InLobby) 
	    			  getItems.getLobbyItems(e.getPlayer(), true);
	    			   
					e.getPlayer().getInventory().setArmorContents(null);
				}	
			}
		}
	}
	
	private void delKit(Player player) {
		if(plugin.getOneVsOnePlayer(player).getpState() != PlayerState.InKitEdit) {
			plugin.getOneVsOnePlayer(player).setKitLoaded(null);
			ScoreBoardManager.updateBoard(player, false);
 	    }
		
	}
}
