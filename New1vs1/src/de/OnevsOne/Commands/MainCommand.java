package de.OnevsOne.Commands;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import de.OnevsOne.main;
import de.OnevsOne.Arena.Manager.ArenaEvents;
import de.OnevsOne.Arena.Reseter.ResetMethoden;
import de.OnevsOne.Arena.Reseter.Builder.CopyArena;
import de.OnevsOne.Arena.Reseter.Builder.DeleteArena;
import de.OnevsOne.Commands.VariableCommands.Kit;
import de.OnevsOne.Guide.Inv_Opener;
import de.OnevsOne.Kit_Methods.loadKitEdit;
import de.OnevsOne.Listener.Manager.ChallangeManager;
import de.OnevsOne.Listener.Manager.Preferences_Manager;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.InventoryOpener;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.KitStands;
import de.OnevsOne.Methods.ScoreBoardManager;
import de.OnevsOne.Methods.SignMethods;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.Methods.configMgr;
import de.OnevsOne.Methods.getItems;
import de.OnevsOne.Methods.openArenaCheckInv;
import de.OnevsOne.Methods.saveErrorMethod;
import de.OnevsOne.Methods.Messenger.TitleAPI;
import de.OnevsOne.Methods.Mobs.spawnBlackDealer;
import de.OnevsOne.Methods.Mobs.spawnPrefVillager;
import de.OnevsOne.Methods.Mobs.spawnQueque;
import de.OnevsOne.Methods.Tournament.TournamentManager;
import de.OnevsOne.States.AllErrors;
import de.OnevsOne.States.OneVsOnePlayer;
import de.OnevsOne.States.PlayerPrefs;
import de.OnevsOne.States.PlayerState;
import net.md_5.bungee.api.ChatColor;

/**
 * Der Code ist von JHammer
 *
 * 05.05.2016 um 20:54:11 Uhr
 */
public class MainCommand implements Listener, CommandExecutor {

	private static main plugin;
	
	@SuppressWarnings("static-access")
	public MainCommand(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	
	
	private HashMap<Player, Integer> skullMode = new HashMap<Player, Integer>();
	private HashMap<Player, Integer> skullMode30 = new HashMap<Player, Integer>();
	
	@SuppressWarnings({ "static-access" })
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
	 
	  final Player p = (Player) sender;
	  
	 
	  if(args.length == 0) {
		  
		 
		  if(p.hasPermission("1vs1.help.*") || p.hasPermission("1vs1.*") || p.hasPermission("1vs1.Admin")) {
			  sendHelp(p, 1);
		  } else {
			  plugin.sendNoPermsMessage(p);
		  }
		  return true;
	  }
	  
      if(args.length == 1) {
       if(args[0].equalsIgnoreCase("help")) {
    	   if(!p.hasPermission("1vs1.command.help") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
          		plugin.sendNoPermsMessage(p);
          		return true;
       	   }
    	   sendHelp(p, 1);
    	   return true;
       }
       if(args[0].equalsIgnoreCase("guide")) {
    	   if(!p.hasPermission("1vs1.command.guide") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
    		   plugin.sendNoPermsMessage(p);
          		return true;
       	   }
    	   Inv_Opener.openMainInv(p);
    	   return true;
       }
       if(args[0].equalsIgnoreCase("version")) {
    	   if(!p.hasPermission("1vs1.command.version") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
    		   plugin.sendNoPermsMessage(p);
          		return true;
       	   }
    	   
    	   p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("pluginVersion")).replaceAll("%Prefix%", plugin.prefix).replaceAll("%Version%", plugin.getDescription().getVersion()));
    	   return true;
       }
       if(args[0].equalsIgnoreCase("in1vs1")) {
    	   if(!p.hasPermission("1vs1.command.inMode") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
    		   plugin.sendNoPermsMessage(p);
          		return true;
       	   }
    	   p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("in1vs1")).replaceAll("%In1vs1%", "" + plugin.getOneVsOnePlayerSize()).replaceAll("%Prefix%", plugin.prefix));
    	   String ac = "";
    	   int a = 1;
    	   for(OneVsOnePlayer player : plugin.getOneVsOnePlayersCopy().values()) {
    		if(a >= plugin.getOneVsOnePlayerSize()) {
    			ac = ac+ "" +player.getPlayer().getName() + "";
    		} else {
    			ac = ac+ "" +player.getPlayer().getDisplayName() + ", ";
    			a++;
    		}
    	   }
    	   p.sendMessage("§6" + ac);
    	   
    	   return true;
       }
       if(args[0].equalsIgnoreCase("queue")) {
    	   if(!p.hasPermission("1vs1.command.queue") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
    		   plugin.sendNoPermsMessage(p);
          		return true;
       	   }
    	   
    	   InventoryOpener opener = new InventoryOpener(plugin);
    	   opener.openInv(p);
    	   
    	   return true;
       }
       if(args[0].equalsIgnoreCase("reloadSigns")) {
    	   if(!p.hasPermission("1vs1.command.reloadSigns") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
    		   plugin.sendNoPermsMessage(p);
          		return true;
       	   }
    	   p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("allSignsReloaded")).replaceAll("%Prefix%", plugin.prefix));
    	   
    	   new SignMethods(plugin).refreshTop5();
    	   new SignMethods(plugin).reloadJoinSigns();
    	   new SignMethods(plugin).refreshJoinSigns();
    	   return true;
       }
       if(args[0].equalsIgnoreCase("resetList")) {
    	   if(!p.hasPermission("1vs1.command.resetList") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
    		   plugin.sendNoPermsMessage(p);
          		return true;
       	   }
    	   
    	   p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("allResets")).replaceAll("%Resets%", plugin.ResetingArenas.size() + ""));
    	   
    	   for(String Arena : plugin.ResetingArenas) {
    		
    		if(Arena.contentEquals(CopyArena.Name)) {
    		 if(plugin.ArenaPlayersP1.containsKey(Arena) && plugin.ArenaPlayersP1.get(Arena).size() > 0 && plugin.ArenaPlayersP2.containsKey(Arena) && plugin.ArenaPlayersP2.get(Arena).size() > 0) {
    		  //p.sendMessage("§b- §a" + Arena + " §8[§aAufbauen§8]");
    		  p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("buildUsingArena")).replaceAll("%Arena%", Arena));
    		 } else {
    		  p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("buildUnusedArena")).replaceAll("%Arena%", Arena)); 
    		  //p.sendMessage("§b- §c" + Arena + " §8[§aAufbauen§8]");
    		 }
    		
    		} else
    		
    		if(Arena.contentEquals(DeleteArena.Name)) {
    		 if(plugin.ArenaPlayersP1.containsKey(Arena) && plugin.ArenaPlayersP1.get(Arena).size() > 0 && plugin.ArenaPlayersP2.containsKey(Arena) && plugin.ArenaPlayersP2.get(Arena).size() > 0) {
    			 p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("destroyUsingArena")).replaceAll("%Arena%", Arena));
       		 } else {
       			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("destroyUnusedArena")).replaceAll("%Arena%", Arena));
       		 }
    		
    		} else {
    		 if(plugin.ArenaPlayersP1.containsKey(Arena) && plugin.ArenaPlayersP1.get(Arena).size() > 0 && plugin.ArenaPlayersP2.containsKey(Arena) && plugin.ArenaPlayersP2.get(Arena).size() > 0) {
    			 p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("waitUsingArena")).replaceAll("%Arena%", Arena));
      		 } else {
      			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("waitUnusedArena")).replaceAll("%Arena%", Arena));
      		 }
   		
    		 
    		}
    	   }
    	   
    	   
    	   
    	   return true;
       }
       if(args[0].equalsIgnoreCase("toggle")) {
    	if(plugin.getOneVsOnePlayer(p).isEditMode()) {
    		p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("inEditMode"), p.getDisplayName(), null, null, null));
    		return true;
    	}
    	
    	
    	
    	if(p.hasPermission("1vs1.command.toggle") || p.hasPermission("1vs1.*") || p.hasPermission("1vs1.User")) {
    		
    		
        	
    		if(plugin.BungeeMode) {
    		 if(!p.hasPermission("1vs1.command.toggle.Bungee") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
    			 p.sendMessage("" + plugin.noPerms);
    			 return true;
    		 }
        	}
    		
        	if(plugin.isInOneVsOnePlayers(p.getUniqueId())) {
//        		if(plugin.getOneVsOnePlayer(p).getpState() != PlayerState.InLobby) {
//        			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("notInLobby"), p.getDisplayName(), null, null, null));
//        			return true;
//        		}
        		
        		plugin.getOneVsOnePlayer(p).setpState(null);
        		
        		toggle1vs1(p, false, false);
        		p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("toggle1vs1Mode2"), p.getDisplayName(), null, null, null));
        		
        	} else {
        		
        		plugin.getOneVsOnePlayer(p).setpState(null);
        		toggle1vs1(p, true, false);
        		p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("toggle1vs1Mode1"), p.getDisplayName(), null, null, null));
        	}
        	
    	} else {
    		p.sendMessage("" + plugin.noPerms);
    	}
    	
    	
		
       } else if(args[0].equalsIgnoreCase("reloadConfig")) {
    	   
    	   if(!p.hasPermission("1vs1.command.reloadConfig") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
       		plugin.sendNoPermsMessage(p);
       		return true;
       	}
    	   
    	   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("startReloadConfig"), p.getDisplayName(), null, null, null));
    	   new configMgr(plugin).reloadData(false);
    	   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("finishReloadConfig"), p.getDisplayName(), null, null, null));
    	   
    	   return true; 
       } else if(args[0].equalsIgnoreCase("reloadMessages") || args[0].equalsIgnoreCase("reloadMsgs")) {
    	   
    	   if(!p.hasPermission("1vs1.command.reloadMessages") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
       		plugin.sendNoPermsMessage(p);
       		return true;
       	}
    	   
    	   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("startReloadMessageData"), p.getDisplayName(), null, null, null));
    	   plugin.msgs.reloadAllMessages();
    	   plugin.reloadBook();
    	   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("finishReloadMessageData"), p.getDisplayName(), null, null, null));
    	   return true; 
       } else if(args[0].equalsIgnoreCase("setLobby")) {
    	   
    	   if(!p.hasPermission("1vs1.command.setLobby") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
       		plugin.sendNoPermsMessage(p);
       		return true;
       	   }
    	   
   	    	Location loc = p.getLocation();
			
			double x = loc.getX();
			double y = loc.getY();
			double z = loc.getZ();
			double yaw = loc.getYaw();
			double pitch = loc.getPitch();
			String worldname = loc.getWorld().getName();
			
			YamlConfiguration cfg = plugin.getYaml("spawns");
			cfg.set("MainSpawn.X", x);
			cfg.set("MainSpawn.Y", y);
			cfg.set("MainSpawn.Z", z);
			cfg.set("MainSpawn.Pitch", pitch);
			cfg.set("MainSpawn.Yaw", yaw);
			cfg.set("MainSpawn.world", worldname);
			try {
				cfg.save(plugin.getPluginFile("spawns"));
			} catch (IOException ee) {
				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
				ee.printStackTrace();
				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), null, null, null));
				return true;
			}    
			
			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("mainSpawnSetted"), p.getDisplayName(), null, null, null));
      } else
       
       if(args[0].equalsIgnoreCase("setExit")) {
    	   
    	   if(!p.hasPermission("1vs1.command.setExit") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
       		plugin.sendNoPermsMessage(p);
       		return true;
       	   }
    	   
      	    Location loc = p.getLocation();
   			
   			double x = loc.getX();
   			double y = loc.getY();
   			double z = loc.getZ();
   			double yaw = loc.getYaw();
   			double pitch = loc.getPitch();
   			String worldname = loc.getWorld().getName();
   			
   			YamlConfiguration cfg = plugin.getYaml("spawns");
   			cfg.set("ExitSpawn.X", x);
   			cfg.set("ExitSpawn.Y", y);
   			cfg.set("ExitSpawn.Z", z);
   			cfg.set("ExitSpawn.Pitch", pitch);
   			cfg.set("ExitSpawn.Yaw", yaw);
   			cfg.set("ExitSpawn.world", worldname);
   			try {
   				cfg.save(plugin.getPluginFile("spawns"));
   			} catch (IOException ee) {
   				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
   				ee.printStackTrace();
   				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), null, null, null));
				return true;
   			}    
   			
   			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("exitSpawnSetted"), p.getDisplayName(), null, null, null));
       } else 
    	
       if(args[0].equalsIgnoreCase("setDefaultKit")) {
    	if(p.hasPermission("1vs1.command.setDefaultKit") || p.hasPermission("1vs1.*") || p.hasPermission("1vs1.Admin")) {
    		
    			if(!plugin.getDBMgr().isConnected()) {
    				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
    				return true;
    			}
    			new BukkitRunnable() {
					
					@Override
					public void run() {
						if(!plugin.getDBMgr().isDefaultExists()) {
							
		    				plugin.getDBMgr().addDefault();
		    			}
		    			
		    			String Inv = plugin.getDBMgr().getKit(p.getUniqueId(), false, "");
		    			String Armor = plugin.getDBMgr().getKit(p.getUniqueId(), true, "");
		    			
		    			plugin.getDBMgr().setDefaultKit(Inv, false);
		    			plugin.getDBMgr().setDefaultKit(Armor, true);
		    			
		    			for(PlayerPrefs pref : PlayerPrefs.values()) {
		    				
		    				String prefs = "" + plugin.getDBMgr().getPref(p.getUniqueId(), Preferences_Manager.getPrefID(pref),"");
		    				
		    				boolean state = false;
		    				if(prefs.equalsIgnoreCase("true") || prefs.equalsIgnoreCase("t")) {
		    					state = true;
		    				}
		    				
		    				plugin.getDBMgr().setPrefDefault(Preferences_Manager.getPrefID(pref), state);
		    				
		    				
		    			}
		    			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("defaultKitSet"), p.getName()));
						
					}
				}.runTaskAsynchronously(plugin);
    			
    			
    		
    	} else {
    		plugin.sendNoPermsMessage(p);
    	}
    		
    	   
    	   
       } else
       
       if(args[0].equalsIgnoreCase("edit")) {
    	   
    	   if(!p.hasPermission("1vs1.command.edit") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
       		plugin.sendNoPermsMessage(p);
       		return true;
       	}
    	   
    	   
    	   plugin.getOneVsOnePlayer(p).setpState(null);
    	   if(plugin.getOneVsOnePlayer(p).isEditMode()) {
    		   
    		   plugin.removePlayer(p.getUniqueId());
    		  
    		   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("toggleEditMode1"), p.getDisplayName(), null, null, null));
    	   } else {
    		   
    		   plugin.removePlayer(p.getUniqueId());
    		   plugin.addPlayer(p.getUniqueId());
    		   
    		   plugin.getOneVsOnePlayer(p).setpState(PlayerState.Edit);
       		   plugin.getOneVsOnePlayer(p).setIn1vs1(false);
    		   
    		   
   			   for(Player players : Bukkit.getOnlinePlayers()) {
   				   players.showPlayer(p);
   				   p.showPlayer(players);
   			   }
    		   
    		   
    		   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("toggleEditMode2"), p.getDisplayName(), null, null, null));
    	   }
  			
      } else
       
      if(args[0].equalsIgnoreCase("setKitEdit")) {
    	  
    	  if(!p.hasPermission("1vs1.command.setKitEdit") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
      		plugin.sendNoPermsMessage(p);
      		return true;
      	} 
    	 
    	  
    	if(plugin.getOneVsOnePlayer(p).getPos1() == null || plugin.getOneVsOnePlayer(p).getPos2() == null) {
    	 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("positionsMissing"), p.getDisplayName(), null, null, null));	
    	 return true;
    	}
    	
    	if(!plugin.getOneVsOnePlayer(p).getPos1().getWorld().getName().equalsIgnoreCase(plugin.getOneVsOnePlayer(p).getPos2().getWorld().getName())) {
    	 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("differentWorlds"), p.getDisplayName(), null, null, null));
    	 return true;
    	}
    	
    	plugin.minX = Math.min(plugin.getOneVsOnePlayer(p).getPos1().getBlockX(), plugin.getOneVsOnePlayer(p).getPos2().getBlockX());
    	plugin.minY = Math.min(plugin.getOneVsOnePlayer(p).getPos1().getBlockY(), plugin.getOneVsOnePlayer(p).getPos2().getBlockY());
    	plugin.minZ = Math.min(plugin.getOneVsOnePlayer(p).getPos1().getBlockZ(), plugin.getOneVsOnePlayer(p).getPos2().getBlockZ());
  		
    	plugin.maxX = Math.max(plugin.getOneVsOnePlayer(p).getPos1().getBlockX(), plugin.getOneVsOnePlayer(p).getPos2().getBlockX());
    	plugin.maxY = Math.max(plugin.getOneVsOnePlayer(p).getPos1().getBlockY(), plugin.getOneVsOnePlayer(p).getPos2().getBlockY());
    	plugin.maxZ = Math.max(plugin.getOneVsOnePlayer(p).getPos1().getBlockZ(), plugin.getOneVsOnePlayer(p).getPos2().getBlockZ());
    	
    	YamlConfiguration cfg = plugin.getYaml("spawns");
		cfg.set("KitEdit.HX", plugin.maxX);
		cfg.set("KitEdit.HY", plugin.maxY);
		cfg.set("KitEdit.HZ", plugin.maxZ);
		
		cfg.set("KitEdit.LX", plugin.minX);
		cfg.set("KitEdit.LY", plugin.minY);
		cfg.set("KitEdit.LZ", plugin.minZ);
		cfg.set("KitEdit.world", plugin.getOneVsOnePlayer(p).getPos1().getWorld().getName());
		try {
			cfg.save(plugin.getPluginFile("spawns"));
		} catch (IOException ee) {
			saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
			ee.printStackTrace();
			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), null, null, null));
			return true;
		}    
		
		loadKitEdit.loadKitEditRegion();
		
		p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("kitEditRegionSet"), p.getDisplayName()));
    	
      } else
      
      if(args[0].equalsIgnoreCase("setQueue")) {
    	  
    	  if(!p.hasPermission("1vs1.command.setQueue") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
      		plugin.sendNoPermsMessage(p);
      		return true;
      	}
    	  
    	  Location loc = p.getLocation();
 			
 			double x = loc.getX();
 			double y = loc.getY();
 			double z = loc.getZ();
 			String worldname = loc.getWorld().getName();
 			
 			YamlConfiguration cfg = plugin.getYaml("spawns");
 			cfg.set("Queque.X", x);
 			cfg.set("Queque.Y", y);
 			cfg.set("Queque.Z", z);
 			cfg.set("Queque.world", worldname);
 			try {
 				cfg.save(plugin.getPluginFile("spawns"));
 			} catch (IOException ee) {
 				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
 				ee.printStackTrace();
 				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName()));
				return true;
 			}    
 			
 			spawnQueque.respawnZombie();
 			
 			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("quequeSetted"), p.getDisplayName()));
      } else if(args[0].equalsIgnoreCase("setBlackDealer")) {
    	  
    	  if(!p.hasPermission("1vs1.command.setDealer") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
      		plugin.sendNoPermsMessage(p);
      		return true;
      	}
    	  
    	  Location loc = p.getLocation();
 			
 			double x = loc.getX();
 			double y = loc.getY();
 			double z = loc.getZ();
 			String worldname = loc.getWorld().getName();
 			
 			YamlConfiguration cfg = plugin.getYaml("spawns");
 			cfg.set("BlackDealer.X", x);
 			cfg.set("BlackDealer.Y", y);
 			cfg.set("BlackDealer.Z", z);
 			cfg.set("BlackDealer.world", worldname);
 			try {
 				cfg.save(plugin.getPluginFile("spawns"));
 			} catch (IOException ee) {
 				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
 				ee.printStackTrace();
 				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName()));
				return true;
 			}    
 			spawnBlackDealer.respawnVillager();
 			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerSetted")));
      } else if(args[0].equalsIgnoreCase("setSettingsVillager")) {
    	  
    	  if(!p.hasPermission("1vs1.command.setSettingsVillager") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
      		plugin.sendNoPermsMessage(p);
      		return true;
      	}
    	  
    	  Location loc = p.getLocation();
 			
 			double x = loc.getX();
 			double y = loc.getY();
 			double z = loc.getZ();
 			String worldname = loc.getWorld().getName();
 			
 			YamlConfiguration cfg = plugin.getYaml("spawns");
 			cfg.set("PrefVillager.X", x);
 			cfg.set("PrefVillager.Y", y);
 			cfg.set("PrefVillager.Z", z);
 			cfg.set("PrefVillager.world", worldname);
 			try {
 				cfg.save(plugin.getPluginFile("spawns"));
 			} catch (IOException ee) {
 				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
 				ee.printStackTrace();
 				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName()));
				return true;
 			}    
 			spawnPrefVillager.respawnVillager();
 			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("kitSettingsVillagerSetted"), p.getDisplayName()));
      } else if(args[0].equalsIgnoreCase("listArenas")) {
   	   if(p.hasPermission("1vs1.command.listArenas") || p.hasPermission("1vs1.*") || p.hasPermission("1vs1.Admin")) {		   	
			YamlConfiguration cfg = plugin.getYaml("Arenen");
			
			String Arenen = "";
			int number = 1;
			int amount = 0;
			
			if(cfg.getConfigurationSection("Arenen") != null) {
			 amount = cfg.getConfigurationSection("Arenen").getKeys(false).size();
			 for(String arenas : cfg.getConfigurationSection("Arenen").getKeys(false)) {
			  if(cfg.getBoolean("Arenen." + arenas)) {
			   if(number >= cfg.getConfigurationSection("Arenen").getKeys(false).size()) {
				Arenen = Arenen+MessageReplacer.replaceStrings(plugin.msgs.getMsg("listArenasLastArenaEnabled"), p.getDisplayName(), arenas);
				continue;
			   }
			   
			   
			   Arenen = Arenen+MessageReplacer.replaceStrings(plugin.msgs.getMsg("listArenasArenaEnabled"), p.getDisplayName(), arenas);
			   
			  } else {
				  
				  if(number >= cfg.getConfigurationSection("Arenen").getKeys(false).size()) {
					  Arenen = Arenen+MessageReplacer.replaceStrings(plugin.msgs.getMsg("listArenasLastArenaDisabled"), p.getDisplayName(), arenas);
					  continue;
				  }
				 
				  Arenen = Arenen+MessageReplacer.replaceStrings(plugin.msgs.getMsg("listArenasArenaDisabled"), p.getDisplayName(), arenas);
			  }
			  number++;
			 }
			 
			}
			
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("listArenasMessage1")).replaceAll("%Amount%", "" + amount));
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("listArenasMessage2")).replaceAll("%Arenas%", Arenen));
			//p.sendMessage("Test [" + amount +"]" + ChatColor.translateAlternateColorCodes('&', Arenen));
			
	    } else {
		   p.sendMessage(plugin.noPerms);
	    }
      } else if(args[0].equalsIgnoreCase("resetAllArenas")) {
    	  
    	  if(!p.hasPermission("1vs1.command.resetAllArenas") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
      		plugin.sendNoPermsMessage(p);
      		return true;
      	}
    	  new ResetMethoden(plugin).resetAllArenas();
    	  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("resetingAllArenas"), p.getDisplayName()));
    	  
      } else {
    	  if(!p.hasPermission("1vs1.command.seeHelp") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
      		plugin.sendNoPermsMessage(p);
      		return true;
      	}
    	  sendWrongUsage(p);
      }
       
      
      } else if(args.length == 2) {
       if(args[0].equalsIgnoreCase("help")) {
    	   if(!p.hasPermission("1vs1.command.help") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
       		plugin.sendNoPermsMessage(p);
       		return true;
    	   }
    	   try {
    		  int page = Integer.parseInt(args[1]);
    		  if(page <= 0 || page > 10) {
    			  sendWrongUsage(p);
    		  } else {
    			  sendHelp(p, page);
    		  }
    	   } catch (Exception ee) {
			  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noNumber")));
    		  return true;
    	   }
    	   
    	   
    	
       	
       	 return true;
       } else if(args[0].equalsIgnoreCase("check")) {
    	   if(!p.hasPermission("1vs1.command.check") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
       		plugin.sendNoPermsMessage(p);
       		return true;
       	}// /1vs1 deleteCustomKit [Name]
	   		// /1vs1 setArmorStand [Kit]:{SubID} 
    	   openArenaCheckInv.openInv(p, args[1]);
       } else if(args[0].equalsIgnoreCase("deleteKit") || args[0].equalsIgnoreCase("delKit")) {
    	   if(!p.hasPermission("1vs1.command.deleteKit") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
         		plugin.sendNoPermsMessage(p);
         		return true;
         	}
   	   
   	   final Player fPlayer = p;
   	   final String[] fArgs = args;
   	   Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				String Kit = fArgs[1];
				String subID = "d";
				if(fArgs[1].contains(":")) {
					subID = fArgs[1].split(":")[1];
					Kit = fArgs[1].split(":")[0];
				}
				
				if(subID.equalsIgnoreCase("1") || 
				   subID.equalsIgnoreCase("2") || 
				   subID.equalsIgnoreCase("3") || 
				   subID.equalsIgnoreCase("4") || 
				   subID.equalsIgnoreCase("5") || 
				   subID.equalsIgnoreCase("*") || 
				   subID.equalsIgnoreCase("d")) {
					
					if(subID.equalsIgnoreCase("*")) {
						
						fPlayer.sendMessage(plugin.msgs.getMsg("thisMayTakeAMoment"));
						
						for(int i = 1; i <= 5; i++) {
							if(plugin.getDBMgr().isCustomKitExists(Kit) == 2) {
							      plugin.getDBMgr().deleteKit(plugin.getDBMgr().getUUID(Kit), "" + i);
							     
							     } else {
							      fPlayer.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("kitNotFound")));
							      return;
							     }
						}
						 fPlayer.sendMessage(plugin.msgs.getMsg("kitSuccesfullDeleted"));
						return;
					}
					
					 if(plugin.getDBMgr().isCustomKitExists(Kit) == 2) {
					      plugin.getDBMgr().deleteKit(plugin.getDBMgr().getUUID(Kit), subID);
					      fPlayer.sendMessage(plugin.msgs.getMsg("kitSuccesfullDeleted"));
					     } else {
					      fPlayer.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("kitNotFound")));
					      return;
					     }
				}
				
				
			
				
			}
   	   });
   	   t.start();
   	   
   	   
   	   
      } else if(args[0].equalsIgnoreCase("resetStats")) { 
    	 
    	if(!p.hasPermission("1vs1.command.resetStats") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
       		plugin.sendNoPermsMessage(p);
       		return true;
       	}
    	  
    	if(plugin.getDBMgr().isNameRegistered(args[1])) {
    		plugin.getDBMgr().setStats(plugin.getDBMgr().getUUID(args[1]), Integer.parseInt(plugin.getDBMgr().getStats(plugin.getDBMgr().getUUID(args[1]), "FightsWon", false))*-1, "FightsWon", false);
        	plugin.getDBMgr().setStats(plugin.getDBMgr().getUUID(args[1]), Integer.parseInt(plugin.getDBMgr().getStats(plugin.getDBMgr().getUUID(args[1]), "Fights", false))*-1, "Fights", false);
        	
        	p.sendMessage(plugin.msgs.getMsg("statsDeleted")); 
    	} else {
    		p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("playerNotFound")));
    	}
    	
    	
    	
      }  else if(args[0].equalsIgnoreCase("deleteCustomKit") || args[0].equalsIgnoreCase("delCustomKit")) {
      
    	   if(!p.hasPermission("1vs1.command.deleteCustomKit") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
          		plugin.sendNoPermsMessage(p);
          		return true;
          	}
    	   
    	   final Player fPlayer = p;
    	   final String[] fArgs = args;
    	   Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
			 if(plugin.getDBMgr().isCustomKitExists(fArgs[1]) == 1) {
		      plugin.getDBMgr().deleteCustomKit(fArgs[1]);
		      fPlayer.sendMessage(plugin.msgs.getMsg("kitSuccesfullDeleted"));
		     } else {
		      fPlayer.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("kitNotFound")));
		      return;
		     }
				
			}
    	   });
    	   t.start();
    	   
    	   
    	   
       } else  if(args[0].equalsIgnoreCase("tpArena") || args[0].equalsIgnoreCase("teleportArena")) {
       
       
    	   
    	   if(!p.hasPermission("1vs1.command.tpArena") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
          		plugin.sendNoPermsMessage(p);
          		return true;
          	}
    	   
    	   if(plugin.getAState().exists(args[1])) {
    		   String arena = args[1];
    		   
    		   if(plugin.getPositions().getArenaPos3(arena) != null) {
    			   p.teleport(plugin.getPositions().getArenaPos3(arena));
    		   } else if(plugin.getPositions().getArenaPos2(arena) != null) {
    			   p.teleport(plugin.getPositions().getArenaPos2(arena));
    		   } else if(plugin.getPositions().getArenaPos1(arena) != null) {
    			   p.teleport(plugin.getPositions().getArenaPos1(arena));
    		   } else {
    			   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("errorPos3NotFound")));
    		   }
    		   
    	   } else {
    		   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaNotFound")));
    	   }
    		   
       } else if(args[0].equalsIgnoreCase("saveArenaLayout")) {
    	   if(!p.hasPermission("1vs1.command.saveArenaLayout") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
       		plugin.sendNoPermsMessage(p);
       		return true;
       	   }
    	        if(plugin.getOneVsOnePlayer(p).getPos1() == null || plugin.getOneVsOnePlayer(p).getPos2() == null) {
    	    	 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("positionsMissing"), p.getDisplayName()));	
    	    	 return true;
    	    	}
    	    	
    	    	if(!plugin.getOneVsOnePlayer(p).getPos1().getWorld().getName().equalsIgnoreCase(plugin.getOneVsOnePlayer(p).getPos2().getWorld().getName())) {
    	    	 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("differentWorlds"), p.getDisplayName()));
    	    	 return true;
    	    	}
    	    	
    	    	boolean exists = plugin.existFile("ArenaLayouts/" + args[1]);
    	    	
    	    	plugin.minX = Math.min(plugin.getOneVsOnePlayer(p).getPos1().getBlockX(), plugin.getOneVsOnePlayer(p).getPos2().getBlockX());
    	    	plugin.minY = Math.min(plugin.getOneVsOnePlayer(p).getPos1().getBlockY(), plugin.getOneVsOnePlayer(p).getPos2().getBlockY());
    	    	plugin.minZ = Math.min(plugin.getOneVsOnePlayer(p).getPos1().getBlockZ(), plugin.getOneVsOnePlayer(p).getPos2().getBlockZ());
    	  		
    	    	plugin.maxX = Math.max(plugin.getOneVsOnePlayer(p).getPos1().getBlockX(), plugin.getOneVsOnePlayer(p).getPos2().getBlockX());
    	    	plugin.maxY = Math.max(plugin.getOneVsOnePlayer(p).getPos1().getBlockY(), plugin.getOneVsOnePlayer(p).getPos2().getBlockY());
    	    	plugin.maxZ = Math.max(plugin.getOneVsOnePlayer(p).getPos1().getBlockZ(), plugin.getOneVsOnePlayer(p).getPos2().getBlockZ());
    	    	
    	    	YamlConfiguration cfg = plugin.getYaml("ArenaLayouts/" + args[1]);
    			cfg.set("Arena.HX", plugin.maxX);
    			cfg.set("Arena.HY", plugin.maxY);
    			cfg.set("Arena.HZ", plugin.maxZ);
    			
    			cfg.set("Arena.LX", plugin.minX);
    			cfg.set("Arena.LY", plugin.minY);
    			cfg.set("Arena.LZ", plugin.minZ);
    			cfg.set("Arena.world", plugin.getOneVsOnePlayer(p).getPos1().getWorld().getName());
    			try {
    				cfg.save(plugin.getPluginFile("ArenaLayouts/" + args[1]));
    			} catch (IOException ee) {
    				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
    				ee.printStackTrace();
    				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName()));
    				return true;
    			}    
    			
    			if(exists) {
    				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaLayoutChanged"), p.getDisplayName(), args[1]));
    			} else {
    				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaLayoutSetted"), p.getDisplayName(), args[1]));
    			}
    			
       } else
       
       if(args[0].equalsIgnoreCase("create")) {
    	   if(!p.hasPermission("1vs1.command.create") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
       		plugin.sendNoPermsMessage(p);
       		return true;
       	}
    	   
    	    if(plugin.existFile("Arenen/" + args[1] + "/config")) {
    	    	p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaAlreadyExists"), p.getDisplayName(), args[1]));
    	    	return true;
    	    }
	    	
	    	YamlConfiguration cfg = plugin.getYaml("Arenen/" + args[1] + "/config");
			
			try {
				cfg.save(plugin.getPluginFile("Arenen/" + args[1] + "/config"));
			} catch (IOException ee) {
				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Config.yml konnte nicht gespeichert werden!");
				ee.printStackTrace();
				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), args[1]));
				return true;
			}    
			
			YamlConfiguration cfg2 = plugin.getYaml("Arenen");
			
			cfg2.set("Arenen." + args[1], true);
			
			
			try {
				cfg2.save(plugin.getPluginFile("Arenen"));
			} catch (IOException ee) {
				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Arenen.yml konnte nicht gespeichert werden!");
				ee.printStackTrace();
				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), args[1]));
				return true;
			}    
			
			
			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaSuccesfullyCreated"), p.getDisplayName(), args[1]));
       } else if(args[0].equalsIgnoreCase("delete")) {
    	   if(p.hasPermission("1vs1.command.deleteArena") || p.hasPermission("1vs1.*") || p.hasPermission("1vs1.Admin")) {
    		   
    		   if(!plugin.existFile("Arenen/" + args[1] + "/config")) {
       	    	p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaNotFound"), p.getDisplayName(), args[1]));
       	    	return true;
       	    }
   	    	
    		   File folder = new File("plugins/1vs1/Arenen/" + args[1]);
   	    	 this.deleteFolder(folder);
   			
   			YamlConfiguration cfg2 = plugin.getYaml("Arenen");
   			
   			cfg2.set("Arenen." + args[1], null);
   			
   			
   			try {
   				cfg2.save(plugin.getPluginFile("Arenen"));
   			} catch (IOException ee) {
   				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Arenen.yml konnte nicht gespeichert werden!");
   				ee.printStackTrace();
   				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), args[1]));
   				return true;
   			}    
   			
   			
   			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaSuccesfullyDeleted"), p.getDisplayName(), args[1]));
    	   } else {
    		   p.sendMessage(plugin.noPerms);
    	   }
       } else if(args[0].equalsIgnoreCase("deleteLayout")) {
    	   if(p.hasPermission("1vs1.command.deleteLayout") || p.hasPermission("1vs1.*") || p.hasPermission("1vs1.Admin")) {
    		   
    		   if(!plugin.existFile("ArenaLayouts/" + args[1])) {
       	    	p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaNotFound"), p.getDisplayName(), args[1]));
       	    	return true;
       	    }
   	    	
    		   File file = plugin.getPluginFile("ArenaLayouts/" + args[1]);
   			   file.delete();
    		   
    		   
   		
   			
   			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("layoutSuccesfullyDeleted").replaceAll("%Layout%", args[1]), p.getDisplayName()));
    	   } else {
    		   p.sendMessage(plugin.noPerms);
    	   }
       } else if(args[0].equalsIgnoreCase("toggleArena")) {
    	   if(p.hasPermission("1vs1.command.toggleArena") || p.hasPermission("1vs1.*") || p.hasPermission("1vs1.Admin")) {
		   if(!plugin.existFile("Arenen/" + args[1] + "/config")) {
   	    		p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaNotFound"), p.getDisplayName(), args[1]));
   	    		return true;
   	    	}
	    	
		   	
			YamlConfiguration cfg = plugin.getYaml("Arenen");
			boolean enabled = false;
			if(!plugin.getAState().isDisabled(args[1])) {
				cfg.set("Arenen." + args[1], false);
				enabled = false;
			} else {
				enabled = true;
				cfg.set("Arenen." + args[1], true);
			}
			
			
			try {
				cfg.save(plugin.getPluginFile("Arenen"));
			} catch (IOException ee) {
				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Arenen.yml konnte nicht gespeichert werden!");
				ee.printStackTrace();
				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), args[1]));
				return true;
			}    
			
			
			if(enabled) {
				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaSuccesfullyEnabled"), p.getDisplayName(), args[1]));
			} else {
				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaSuccesfullyDisabled"), p.getDisplayName(), args[1]));
			}
	    } else {
		   p.sendMessage(plugin.noPerms);
	    }
       } else if(args[0].equalsIgnoreCase("setSkull")) {
    	   if(p.hasPermission("1vs1.command.setSkull") || p.hasPermission("1vs1.*") || p.hasPermission("1vs1.Admin")) {
		  
    		
    		   
    		if(skullMode.containsKey(p)) {
    			while(this.skullMode.containsKey(p)) {
        			this.skullMode.remove(p);
        		}
    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noLongerInSkullAddMode")).replaceAll("%Prefix%", plugin.prefix));
    			return true;
    		}
    		
    		
    		
    		
    		
    		try {
    			int Spawn = Integer.parseInt(args[1]);
    			if(Spawn <= 0) {
    				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("wrongNumberSkull")).replaceAll("%Prefix%", plugin.prefix));
    			    return true;
    			}
    			skullMode.put(p, Spawn);
    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("nowInSkullAddMode")).replaceAll("%Prefix%", plugin.prefix));
    		} catch (NumberFormatException ee) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noNumber")));
				return true;
			}
    		   
	    } else {
		   p.sendMessage(plugin.noPerms);
	    }
       } else if(args[0].equalsIgnoreCase("setSkull30")) {
    	   if(p.hasPermission("1vs1.command.setSkull30") || p.hasPermission("1vs1.*") || p.hasPermission("1vs1.Admin")) {
		  
    		   
    		if(skullMode.containsKey(p)) {
    			while(this.skullMode30.containsKey(p)) this.skullMode30.remove(p);
    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noLongerInSkullAddMode")).replaceAll("%Prefix%", plugin.prefix));
    			return true;
    		}
    		
    		try {
    			int Spawn = Integer.parseInt(args[1]);
    			if(Spawn <= 0) {
    				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("wrongNumberSkull")).replaceAll("%Prefix%", plugin.prefix));
    			    return true;
    			}
    			skullMode30.put(p, Spawn);
    			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("nowInSkullAddMode")).replaceAll("%Prefix%", plugin.prefix));
    		} catch (NumberFormatException ee) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noNumber")));
				return true;
			}
    		   
	    } else {
		   p.sendMessage(plugin.noPerms);
	    }
       } else if(args[0].equalsIgnoreCase("reset")) {
    	   if(!p.hasPermission("1vs1.command.reset") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
       		plugin.sendNoPermsMessage(p);
       		return true;
       	}
    	if(plugin.existFile("Arenen/" + args[1] + "/config")) {
    	 
    		
    		
    	 String Layout = plugin.getPositions().getLayout(args[1]);
    	 if(!Layout.equalsIgnoreCase("null")) {
    		 Location pos1 = plugin.getPositions().getPos1(Layout);
    		 Location pos2 = plugin.getPositions().getPos2(Layout);
    		 Location pos3 = plugin.getPositions().getPos3(args[1]);
    		 
    		 if(pos1 == null) {
    			 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("errorPos1NotFound"), p.getDisplayName(), args[1]));
    			 return true;
    		 }
    		 
    		 if(pos2 == null) {
    			 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("errorPos2NotFound"), p.getDisplayName(), args[1]));
    			 return true;
    		 }

			 if(pos3 == null) {
				 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("errorPos3NotFound"), p.getDisplayName(), args[1]));
				 return true;
			 }
    		  
			 DeleteArena.startReset(pos1, pos2, pos3, args[1]);
			 
			 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("startingReset"), p.getDisplayName(), args[1]));
    		         
    	 } else {
    		 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("layoutNotFound"), p.getDisplayName(), args[1]));
    	 }
    	 
    	 
    	 
   	    } else {
   	    	p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaNotFound"), p.getDisplayName(), args[1]));
   	    	
   	    }
       } else {
    	   sendWrongUsage(p);
       }
       
       
      } else if(args.length == 4 || args.length == 3) {
    	if(args.length == 4) {
    	 if(args[0].equalsIgnoreCase("setting") || args[0].equalsIgnoreCase("settings")) {
    		 if(!p.hasPermission("1vs1.command.setting") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
    	    		plugin.sendNoPermsMessage(p);
    	    		return true;
    	    	}
    	  //1vs1 setting LayoutItemID [Layout] [ID]
    	  //X    0       1 	          2        3  
    	  if(args[1].equalsIgnoreCase("LayoutItemID")) {
    		  if(plugin.existFile("ArenaLayouts/" + args[2])) {
    			  
    			  YamlConfiguration cfg = plugin.getYaml("ArenaLayouts/" + args[2]);
    			  
    			  
    			  String id = args[3];
    			  
    			  if(id.contains(":")) {
    				  String[] idS;
    				  idS = id.split(":");
    				  int mainID = 0;
    				  int subID = 0;
    				  
    				  try {
    					
    					mainID = Integer.parseInt(idS[0]);  
    					subID = Integer.parseInt(idS[1]);  
    				  } catch (Exception ee) {
    					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noNumber"), p.getDisplayName(), args[3]));
    					return true;
					}
    				  
    				  cfg.set("Arena.ItemID", mainID);
    				  cfg.set("Arena.SubID", subID);
    			  } else {
    				  int mainID = 0;
    				  int subID = 0;
    				  
    				  try {
    					  mainID = Integer.parseInt(id);   
    				  } catch (Exception ee) {
    					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noNumber"), p.getDisplayName(), args[3]));
      					return true;
    				  }
    				  
    				  cfg.set("Arena.ItemID", mainID);
    				  cfg.set("Arena.SubID", subID);
    			  }
      			  
      			try {
      				cfg.save(plugin.getPluginFile("ArenaLayouts/" + args[2]));
      			} catch (IOException ee) {
      				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die ArenaLayouts/" + args[2] +".yml konnte nicht gespeichert werden!");
      				ee.printStackTrace();
      				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName()));
      				return true;
      			}    
      			
      			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaSettingChanged"), p.getDisplayName(), args[1]));
    			  
    			  return true;
    			  
    		  } else {
    			  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("layoutNotFound"), p.getDisplayName(), args[2]));
    		  }
    		  return true;
    	  }
    	  //1vs1 setting LayoutItemID [Layout] [Author]
    	  //X    0       1 	          2        3  
    	  if(args[1].equalsIgnoreCase("LayoutAuthor")) {
    		  if(plugin.existFile("ArenaLayouts/" + args[2])) {
    			  
    			  YamlConfiguration cfg = plugin.getYaml("ArenaLayouts/" + args[2]);
    			  
    			  cfg.set("Arena.Author", args[3]);
      			  
      			try {
      				cfg.save(plugin.getPluginFile("ArenaLayouts/" + args[2]));
      			} catch (IOException ee) {
      				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Arenalayouts" +  args[2] +".yml konnte nicht gespeichert werden!");
      				ee.printStackTrace();
      				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName()));
      				return true;
      			}    
      			
      			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaSettingChanged"), p.getDisplayName(), args[1]));
    			  
    			  return true;
    			  
    		  } else {
    			  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("layoutNotFound"), p.getDisplayName(), args[2]));
    		  }
    		  return true;
    	  }
    	  if(plugin.existFile("Arenen/" + args[1] + "/config")) {
    	   if(args[2].equalsIgnoreCase("LayOut")) {
    		if(plugin.existFile("ArenaLayouts/" + args[3])) {
    			YamlConfiguration cfg = plugin.getYaml("Arenen/" + args[1] + "/config");
    			
    			cfg.set("config.LayOut", args[3]);
    			
    			try {
    				cfg.save(plugin.getPluginFile("Arenen/" + args[1] + "/config"));
    			} catch (IOException ee) {
    				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die " + args[1] + "/config.yml konnte nicht gespeichert werden!");
    				ee.printStackTrace();
    				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), args[1]));
    				return true;
    			}    
    			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaSettingChanged"), p.getDisplayName(), args[1]));
    			
    		} else {
    			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("layoutNotFound"), p.getDisplayName(), args[1]));
    			
    		}
    	   } else {
    		   sendWrongUsage(p);
    	   }
    	  } else {
    		  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaNotFound"), p.getDisplayName(), args[1]));
    	  }
    	 } else {
    		 if(!p.hasPermission("1vs1.command.seeHelp") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
    	    		plugin.sendNoPermsMessage(p);
    	    		return true;
    	    	}
    		 sendWrongUsage(p);
    	 }
    	} else if(args.length == 3) {
    		
    		if(args[0].equalsIgnoreCase("addACSArena")) {
    			if(!p.hasPermission("1vs1.command.addACSArena") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
    				plugin.sendNoPermsMessage(p);
    				return true;
    			}
    			
    			
    			if(plugin.getAState().exists(args[1])) {
    			 	YamlConfiguration cfg = plugin.getYaml("/ACS/" + args[2]);
    				
    			 	String arena = args[1];
    			 	
    			 	Location spawn1 = plugin.getPositions().getArenaPos1(arena);
     				Location spawn2 = plugin.getPositions().getArenaPos2(arena);
     				Location spawn3 = plugin.getPositions().getArenaPos3(arena);
     				
     				
     				Location resetPos = plugin.getPositions().getPos3(arena);
     				String layout = plugin.getPositions().getLayout(arena);
     				
     				int disSpawn1ResetX = spawn1.getBlockX()-resetPos.getBlockX();
     				int disSpawn1ResetY = spawn1.getBlockY()-resetPos.getBlockY();
     				int disSpawn1ResetZ = spawn1.getBlockZ()-resetPos.getBlockZ();
     				
     				int disSpawn2ResetX = spawn2.getBlockX()-resetPos.getBlockX();
     				int disSpawn2ResetY = spawn2.getBlockY()-resetPos.getBlockY();
     				int disSpawn2ResetZ = spawn2.getBlockZ()-resetPos.getBlockZ();
     				
     				int disSpawn3ResetX = spawn3.getBlockX()-resetPos.getBlockX();
     				int disSpawn3ResetY = spawn3.getBlockY()-resetPos.getBlockY();
     				int disSpawn3ResetZ = spawn3.getBlockZ()-resetPos.getBlockZ();
     				
     				
     				
     				
            		   cfg.set("Arena.Position1.X", disSpawn1ResetX);
            		   cfg.set("Arena.Position1.Y", disSpawn1ResetY);
        			   cfg.set("Arena.Position1.Z", disSpawn1ResetZ);
        			   cfg.set("Arena.Position1.Yaw", spawn1.getYaw());
        			   cfg.set("Arena.Position1.Pitch", spawn1.getPitch());
        			  
        			   
        			   cfg.set("Arena.Position2.X", disSpawn2ResetX);
            		   cfg.set("Arena.Position2.Y", disSpawn2ResetY);
        			   cfg.set("Arena.Position2.Z", disSpawn2ResetZ);
        			   cfg.set("Arena.Position2.Yaw", spawn2.getYaw());
        			   cfg.set("Arena.Position2.Pitch", spawn2.getPitch());
        			   
        			   cfg.set("Arena.Spectator.X", disSpawn3ResetX);
            		   cfg.set("Arena.Spectator.Y", disSpawn3ResetY);
        			   cfg.set("Arena.Spectator.Z", disSpawn3ResetZ);
        			   cfg.set("Arena.Spectator.Yaw", spawn3.getYaw());
        			   cfg.set("Arena.Spectator.Pitch", spawn3.getPitch());
        			 
        			   
        			   cfg.set("config.LayOut", layout);
        			   
        			  
    			 	
    				try {
						cfg.save(plugin.getPluginFile("/ACS/" + args[2]));
					} catch (IOException e1) {
						p.sendMessage(plugin.msgs.getMsg("checkConsoleError"));
						e1.printStackTrace();
						return true;
					}
    				p.sendMessage(plugin.msgs.getMsg("clonCreated"));
    				
    			} else {
    				p.sendMessage(plugin.msgs.getMsg("arenaNotFoundString").replaceAll("%Name%", args[1]));
    			}
    		} else if(args[0].equalsIgnoreCase("cloneArena")) {
    			if(!p.hasPermission("1vs1.command.cloneArena") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
    				plugin.sendNoPermsMessage(p);
    				return true;
    			}
    			
    			final String arena = args[1];
    			final String newArena = args[2];
    			
    			
    			
    			if(plugin.getAState().exists(arena)) {
    			 if(plugin.getOneVsOnePlayer(p).getPos3() != null) {

    				if(plugin.getAState().exists(newArena)) {	
    					p.sendMessage(plugin.msgs.getMsg("arenaAlreadyExists"));
    					return true;
    				}
    				
     				Location spawn1 = plugin.getPositions().getArenaPos1(arena);
     				Location spawn2 = plugin.getPositions().getArenaPos2(arena);
     				Location spawn3 = plugin.getPositions().getArenaPos3(arena);
     				
     				Location to = plugin.getOneVsOnePlayer(p).getPos3();
     				
     				Location resetPos = plugin.getPositions().getPos3(arena);
     				String layout = plugin.getPositions().getLayout(arena);
     				
     				int disSpawn1ResetX = spawn1.getBlockX()-resetPos.getBlockX();
     				int disSpawn1ResetY = spawn1.getBlockY()-resetPos.getBlockY();
     				int disSpawn1ResetZ = spawn1.getBlockZ()-resetPos.getBlockZ();
     				
     				int disSpawn2ResetX = spawn2.getBlockX()-resetPos.getBlockX();
     				int disSpawn2ResetY = spawn2.getBlockY()-resetPos.getBlockY();
     				int disSpawn2ResetZ = spawn2.getBlockZ()-resetPos.getBlockZ();
     				
     				int disSpawn3ResetX = spawn3.getBlockX()-resetPos.getBlockX();
     				int disSpawn3ResetY = spawn3.getBlockY()-resetPos.getBlockY();
     				int disSpawn3ResetZ = spawn3.getBlockZ()-resetPos.getBlockZ();
     				
     				Location to1 = to.clone().add(disSpawn1ResetX,disSpawn1ResetY,disSpawn1ResetZ);
     				Location to2 = to.clone().add(disSpawn2ResetX,disSpawn2ResetY,disSpawn2ResetZ);
     				Location to3 = to.clone().add(disSpawn3ResetX,disSpawn3ResetY,disSpawn3ResetZ);
     				
     				YamlConfiguration cfg = plugin.getYaml("Arenen/" + newArena + "/config");
     				
            		   cfg.set("Arena.Position1.X", to1.getBlockX());
            		   cfg.set("Arena.Position1.Y", to1.getBlockY());
        			   cfg.set("Arena.Position1.Z", to1.getBlockZ());
        			   cfg.set("Arena.Position1.Yaw", spawn1.getYaw());
        			   cfg.set("Arena.Position1.Pitch", spawn1.getPitch());
        			   cfg.set("Arena.Position1.World", spawn1.getWorld().getName());
        			   
        			   cfg.set("Arena.Position2.X", to2.getBlockX());
            		   cfg.set("Arena.Position2.Y", to2.getBlockY());
        			   cfg.set("Arena.Position2.Z", to2.getBlockZ());
        			   cfg.set("Arena.Position2.World", to2.getWorld().getName());
        			   cfg.set("Arena.Position2.Yaw", spawn2.getYaw());
        			   cfg.set("Arena.Position2.Pitch", spawn2.getPitch());
        			   
        			   cfg.set("Arena.Spectator.X", to3.getBlockX());
            		   cfg.set("Arena.Spectator.Y", to3.getBlockY());
        			   cfg.set("Arena.Spectator.Z", to3.getBlockZ());
        			   cfg.set("Arena.Spectator.World", to3.getWorld().getName());
        			   cfg.set("Arena.Spectator.Yaw", spawn3.getYaw());
        			   cfg.set("Arena.Spectator.Pitch", spawn3.getPitch());
        			   
        			   cfg.set("Arena.ResetX", to.getBlockX());
        			   cfg.set("Arena.ResetY", to.getBlockY());
        			   cfg.set("Arena.ResetZ", to.getBlockZ());
        			   cfg.set("Arena.ResetWorld", to.getWorld().getName());
        			   
        			   cfg.set("config.LayOut", layout);
        			   
        			   try {
						cfg.save(plugin.getPluginFile("Arenen/" + newArena + "/config"));
        			   } catch (IOException e1) {
						e1.printStackTrace();
						p.sendMessage("§cEin Error beim Speichern der Datei ist aufgetreten! Checke die Console!");
						return true;
        			   }
        			   
        			   cfg = plugin.getYaml("Arenen");
        			   
        			   cfg.set("Arenen." + newArena, true);
        			   
        			   try {
   						cfg.save(plugin.getPluginFile("Arenen"));
           			   } catch (IOException e1) {
   						e1.printStackTrace();
   						p.sendMessage("§cEin Error beim Speichern der Datei ist aufgetreten! Checke die Console!");
   						return true;
           			   }
        			   
        			   new ResetMethoden(plugin).resetArena(newArena);
        			   
        			p.sendMessage(plugin.msgs.getMsg("arenaSuccesfullyCopied"));
     				
    			 } else {
    				p.sendMessage(plugin.msgs.getMsg("point3Missing")); 
    			 }
    			} else {
    				p.sendMessage(plugin.msgs.getMsg("arenaNotFound"));
    			}
    			
    			
    			
    		} else if(args[0].equalsIgnoreCase("setRankPoints")) {
    			if(!p.hasPermission("1vs1.command.setRankPoints") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
	         		plugin.sendNoPermsMessage(p);
	         		return true;
	         	}
    			

 	    	   final String[] fArgs = args;
 	    	   final Player fPlayer = p;
 	    	   
 	    	   
 	    	   
 	    	   Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						if(plugin.getDBMgr().isUserExists(plugin.getDBMgr().getUUID(fArgs[1]))) {
		    	    		   int amount = 0;
							   try {
								   amount = Integer.parseInt(fArgs[2]);
							   } catch (NumberFormatException ee) {
								   fPlayer.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noNumber")));
								   return;
							   }
							
							   UUID uuid = plugin.getDBMgr().getUUID(fArgs[1]);
									   
								
							   plugin.getDBMgr().updateRankPoints(uuid, ((plugin.getDBMgr().getRankPoints(uuid)*-1)+amount));
		    	    		   
		    	    		   fPlayer.sendMessage(plugin.msgs.getMsg("rankingPointsSuccesfullySetted"));
		    	    		    
		    	    	   } else {
		    	    		   fPlayer.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("playerNotFound")));
		    	    		  
		    	    	   }
		    	    	   
						
					}
 	    	   	});
 	    	   
 	    	   t.start();
    			
    		} else
    		
    		if(args[0].equalsIgnoreCase("addCustomKit")) {
    	    	   if(!p.hasPermission("1vs1.command.addCustomKit") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
    	         		plugin.sendNoPermsMessage(p);
    	         		return true;
    	         	}
    	    	   
    	    	   final String[] fArgs = args;
    	    	   final Player fPlayer = p;
    	    	   
    	    	   if(fArgs[1].contains(":")) {
    	    		   p.sendMessage("§cUngültiger Name! (Der Name darf keine : enthalten!)");
    	    		   return true;
    	    	   }
    	    	   
    	    	 
    	    	   
    	    	   
    	    	   Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
					
					@Override
					public void run() {
						if(!plugin.getDBMgr().checkAllRowsExists()) {
							p.sendMessage(plugin.prefix + "§cEs wurden nicht alle Spalten in deiner (My)SQL-Datenbank erkannt!");
							p.sendMessage(plugin.prefix + "§7Folgende Spalten konnten nicht gefunden werden: §6" + plugin.getDBMgr().getNotExistingRows());
							return;
						}
						
						if(plugin.getDBMgr().isCustomKitExists(fArgs[1]) != 2) {
		    	    		   
		    	    		   String Kit = fArgs[2];
		    	    		   String subID = "d";
		    	    		   
		    	    		   
		    	    		   
		    	    		   if(Kit.contains(":")) {
		    	    			   String[] Id = Kit.split(":");
		    	    			   Kit = Id[0];
		    	    			   subID = Id[1];
		    	    		   }
		    	    		   
		    	    		  
		    	    		   
		    	    		   if(plugin.getDBMgr().isNameRegistered(Kit)) {
		    	    			   if(subID.equalsIgnoreCase("d")) {
			    	    			   subID = plugin.getDBMgr().getDefaultKit(plugin.getDBMgr().getUUID(Kit));
			    	    		   }
		    	    			   if(subID.equalsIgnoreCase("1")) subID = "";
		    	    			   plugin.getDBMgr().createCustomKit(fArgs[1], plugin.getDBMgr().getKit(plugin.getDBMgr().getUUID(Kit), false, subID), plugin.getDBMgr().getKit(plugin.getDBMgr().getUUID(Kit), true, subID), plugin.getDBMgr().getRawPref(plugin.getDBMgr().getUUID(Kit), subID));
		    	    		   } else if( plugin.getDBMgr().isCustomKitExists(Kit) == 1) {
		    	    			   plugin.getDBMgr().createCustomKit(fArgs[1],  plugin.getDBMgr().loadCustomKitInv(Kit), plugin.getDBMgr().loadCustomKitArmor(Kit), plugin.getDBMgr().loadCustomKitRawPref(Kit));
		    	    		   } else {
		    	    			   fPlayer.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("kitNotFound")));
		    	    			   return;
		    	    		   }
		    	    		   
		    	    		   fPlayer.sendMessage(plugin.msgs.getMsg("customKitCreated")); 
		    	    		   
		    	    	   } else {
		    	    		   fPlayer.sendMessage(plugin.msgs.getMsg("kitNameAlreadyInUseUser"));
		    	    		  
		    	    	   }
		    	    	   
						
					}
    	    	   	});
    	    	   
    	    	  
    	    	   
    	   		  
    	    } else if(args[0].equalsIgnoreCase("setKitStand")) {
    	    	   if(!p.hasPermission("1vs1.command.setKitStand") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
   	         		plugin.sendNoPermsMessage(p);
   	         		return true;
    	    	   }
    	    	   
    	    	   if(args[1].contains(":")) {
    	    		   p.sendMessage(plugin.msgs.getMsg("invalidCustomKitName"));
    	    		   return true;
    	    	   }
    	    	   
    	    	   
    	    	   
    	    	    Location loc = p.getLocation();
    				
    				double x = loc.getBlockX();
    				double y = loc.getBlockY();
    				double z = loc.getBlockZ();
    				x = x+0.5;
    				z = z+0.5;
    				double yaw = loc.getYaw();
    				double pitch = loc.getPitch();
    				String worldname = loc.getWorld().getName();
    				
    				YamlConfiguration cfg = plugin.getYaml("spawns");
    				cfg.set("KitStands." + args[1]  + ".X", x);
    				cfg.set("KitStands." + args[1]  + ".Y", y);
    				cfg.set("KitStands." + args[1]  + ".Z", z);
    				cfg.set("KitStands." + args[1]  + ".Pitch", pitch);
    				cfg.set("KitStands." + args[1]  + ".Yaw", yaw);
    				cfg.set("KitStands." + args[1]  + ".World", worldname);
    				
    				cfg.set("KitStands." + args[1] + ".Kit", args[2]);
    				try {
    					cfg.save(plugin.getPluginFile("spawns"));
    				} catch (IOException ee) {
    					saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
    					ee.printStackTrace();
    					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), null, null, null));
    					return true;
    				}    
    				new KitStands(plugin).spawnStands();
    				
    				p.sendMessage(plugin.msgs.getMsg("kitStandSet"));
    	    	   
    	    } else if(args[0].equalsIgnoreCase("setTopKitStand")) {
 	    	   if(!p.hasPermission("1vs1.command.setTopKitStand") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
  	         		plugin.sendNoPermsMessage(p);
  	         		return true;
   	    	   }
   	    	   
 	    	   //  /1vs1 setTopKitStand [24h/30d/All] [Place]
 	    	   //        [args0]  		[args1]		  [args2]
   	    	   
 	    	   int place = 0;
 	    	   
 	    	   
   	    	   try {
   	    		place = Integer.parseInt(args[2]);
   	    		if(place <= 0) {
   	    			p.sendMessage(plugin.prefix + "Die Zahl muss größer als 0 sein!");//TODO Msgs
   	    			return true;
   	    		}
   	    	   } catch (NumberFormatException e) {
   	    		p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noNumber")));
   	    		return true;
   	    	   }
 	    	   
   	    	   if(args[1].equalsIgnoreCase("24h") || 
   	    		  args[1].equalsIgnoreCase("30d") ||
   	    		  args[1].equalsIgnoreCase("All")) {
   	    		   
   	    		   Location loc = p.getLocation();
   				
   				double x = loc.getBlockX();
   				double y = loc.getBlockY();
   				double z = loc.getBlockZ();
   				x = x+0.5;
   				z = z+0.5;
   				double yaw = loc.getYaw();
   				double pitch = loc.getPitch();
   				String worldname = loc.getWorld().getName();
   				
   				YamlConfiguration cfg = plugin.getYaml("spawns");
   				cfg.set("TopKitStands." + args[1].toLowerCase() + "." + args[2]  + ".X", x);
   				cfg.set("TopKitStands." + args[1].toLowerCase() + "." + args[2]  + ".Y", y);
   				cfg.set("TopKitStands." + args[1].toLowerCase() + "." + args[2]  + ".Z", z);
   				cfg.set("TopKitStands." + args[1].toLowerCase() + "." + args[2]  + ".Pitch", pitch);
   				cfg.set("TopKitStands." + args[1].toLowerCase() + "." + args[2]  + ".Yaw", yaw);
   				cfg.set("TopKitStands." + args[1].toLowerCase() + "." + args[2]  + ".World", worldname);
   				
   				if(args[1].equalsIgnoreCase("all")) {
   					cfg.set("TopKitStands." + args[1].toLowerCase() + "." + args[2]  + ".statsType", 0);
   				} else if(args[1].equalsIgnoreCase("30d")) {
   					cfg.set("TopKitStands." + args[1].toLowerCase() + "." + args[2]  + ".statsType", 1);
   				} else {
   					cfg.set("TopKitStands." + args[1].toLowerCase() + "." + args[2]  + ".statsType", 2);
   				}  
   				
   				
   				try {
   					cfg.save(plugin.getPluginFile("spawns"));
   				} catch (IOException ee) {
   					saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
   					ee.printStackTrace();
   					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), null, null, null));
   					return true;
   				}    
   				new KitStands(plugin).spawnStands();
   				
   				p.sendMessage(plugin.msgs.getMsg("kitStandSet"));
   	    		   
   	    		   
   	    		   
   	    	   } else {
   	    		   p.sendMessage(plugin.prefix + "§cFalsches Argument: /1vs1 setTopKitStand [All/30d/24h] [Platz]");
   	    		   return true;
   	    	   }
 	    	   
   	    	   
   	    	   
   	    	    
   	    	   
   	    } else if(args[0].equalsIgnoreCase("setting") || args[0].equalsIgnoreCase("settings")) {
    		 if(!p.hasPermission("1vs1.command.setting") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
    	    		plugin.sendNoPermsMessage(p);
    	    		return true;
    	    	}
    	  if(plugin.existFile("Arenen/" + args[1] + "/config")) {
    		  
    	   if(args[2].equalsIgnoreCase("setReset")) {
    		
    		if(plugin.getOneVsOnePlayer(p).getPos3() == null) {
    			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("point3Missing"), p.getDisplayName(), args[1]));
    			return true;
    		}
    		
    		YamlConfiguration cfg = plugin.getYaml("Arenen/" + args[1] + "/config");
    		
    		cfg.set("Arena.ResetX", plugin.getOneVsOnePlayer(p).getPos3().getBlockX());
			cfg.set("Arena.ResetY", plugin.getOneVsOnePlayer(p).getPos3().getBlockY());
			cfg.set("Arena.ResetZ", plugin.getOneVsOnePlayer(p).getPos3().getBlockZ());
			cfg.set("Arena.ResetWorld", plugin.getOneVsOnePlayer(p).getPos3().getWorld().getName());
			
			try {
				cfg.save(plugin.getPluginFile("Arenen/" + args[1] + "/config"));
			} catch (IOException ee) {
				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die " + args[1] + "/config.yml konnte nicht gespeichert werden!");
				ee.printStackTrace();
				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), args[1]));
				return true;
			}   
    		p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaSettingChanged"), p.getDisplayName(), args[1]));
    	   } else
    	   if(args[2].equalsIgnoreCase("Spawn1")) {
    		   YamlConfiguration cfg = plugin.getYaml("Arenen/" + args[1] + "/config");
       		
       		   cfg.set("Arena.Position1.X", p.getLocation().getX());
       		   cfg.set("Arena.Position1.Y", p.getLocation().getY());
   			   cfg.set("Arena.Position1.Z", p.getLocation().getZ());
   			   cfg.set("Arena.Position1.Yaw", p.getLocation().getYaw());
   			   cfg.set("Arena.Position1.Pitch", p.getLocation().getPitch());
   			   cfg.set("Arena.Position1.World", p.getLocation().getWorld().getName());
   			
   			   
   			   if(ArenaEvents.getArena(p.getLocation()) == null) {
   				messageError(p, args[1]);
   			   } else if(!ArenaEvents.getArena(p.getLocation()).equalsIgnoreCase(args[1])) {
   				messageError(p, args[1]);
   			   }
   			   
   			   
   			   try {
   				   cfg.save(plugin.getPluginFile("Arenen/" + args[1] + "/config"));
   			   } catch (IOException ee) {
   				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die " + args[1] + "/config.yml konnte nicht gespeichert werden!");
   				ee.printStackTrace();
   				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), args[1]));
				return true;
   			   }   
       		p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaSettingChanged"), p.getDisplayName(), args[1]));   
    	   } else
    	   if(args[2].equalsIgnoreCase("Spawn2")) {
    		   YamlConfiguration cfg = plugin.getYaml("Arenen/" + args[1] + "/config");
          		
       		   cfg.set("Arena.Position2.X", p.getLocation().getX());
       		   cfg.set("Arena.Position2.Y", p.getLocation().getY());
   			   cfg.set("Arena.Position2.Z", p.getLocation().getZ());
   			   cfg.set("Arena.Position2.Yaw", p.getLocation().getYaw());
   			   cfg.set("Arena.Position2.Pitch", p.getLocation().getPitch());
   			   cfg.set("Arena.Position2.World", p.getLocation().getWorld().getName());
   			
   			   if(ArenaEvents.getArena(p.getLocation()) == null) {
   				messageError(p, args[1]);
   			   } else if(!ArenaEvents.getArena(p.getLocation()).equalsIgnoreCase(args[1])) {
   				messageError(p, args[1]);
   			   }
   			   
   			   try {
   				   cfg.save(plugin.getPluginFile("Arenen/" + args[1] + "/config"));
   			   } catch (IOException ee) {
   				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die " + args[1] + "/config.yml konnte nicht gespeichert werden!");
   				ee.printStackTrace();
   				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), args[1]));
				return true;
   			   }   
   			   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaSettingChanged"), p.getDisplayName(), args[1]));   
    	   } else
    	   if(args[2].equalsIgnoreCase("setMiddle")) {
    		   YamlConfiguration cfg = plugin.getYaml("Arenen/" + args[1] + "/config");
          		
       		   cfg.set("Arena.Spectator.X", p.getLocation().getX());
       		   cfg.set("Arena.Spectator.Y", p.getLocation().getY());
   			   cfg.set("Arena.Spectator.Z", p.getLocation().getZ());
   			   cfg.set("Arena.Spectator.Yaw", p.getLocation().getYaw());
   			   cfg.set("Arena.Spectator.Pitch", p.getLocation().getPitch());
   			   cfg.set("Arena.Spectator.World", p.getLocation().getWorld().getName());
   			
   			   if(ArenaEvents.getArena(p.getLocation()) == null) {
   				
   				   messageError(p, args[1]);
   			   } else if(!ArenaEvents.getArena(p.getLocation()).equalsIgnoreCase(args[1])) {
   				messageError(p, args[1]);
   			   }
   			   
   			   try {
   				   cfg.save(plugin.getPluginFile("Arenen/" + args[1] + "/config"));
   			   } catch (IOException ee) {
   				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die " + args[1]  +"/config.yml konnte nicht gespeichert werden!");
   				ee.printStackTrace();
   				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), args[1]));
				return true;
   			   }   
   			   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaSettingChanged"), p.getDisplayName(), args[1]));   
    	   } else {
    		   if(!p.hasPermission("1vs1.command.seeHelp") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
   	    		plugin.sendNoPermsMessage(p);
   	    		return true;
   	    	}
    		   sendWrongUsage(p);
    	   }
    	  } else {
    		  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaNotFound"), p.getDisplayName(), args[1]));
    	  }
    	 } else {
    		 if(!p.hasPermission("1vs1.command.seeHelp") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
    	    		plugin.sendNoPermsMessage(p);
    	    		return true;
    	    	}
    		 sendWrongUsage(p);
    	 }
    	}  else {
    		if(!p.hasPermission("1vs1.command.seeHelp") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
	    		plugin.sendNoPermsMessage(p);
	    		return true;
	    	}
       	 sendWrongUsage(p);
        }
    	
      } else {
    	  if(!p.hasPermission("1vs1.command.seeHelp") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Admin")) {
	    		plugin.sendNoPermsMessage(p);
	    		return true;
	    	}
    	 sendWrongUsage(p);
      }
	 
		
		
	 return true;
	}

	
	
	private void sendHelp(Player p, int Page) {
		
		int maxPage = 10;
		
		
		p.sendMessage("§f-§6-§f-§6-§f-§6-§f-§6-§f-§6-§f[§9§l1vs1§r§f]§6-§f-§6-§f-§6-§f-§6-§f-§6-§f-");
		p.sendMessage("§c");
		
		if(Page == 1) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bArenen:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7● §6/1vs1 resetAllArenas");
			p.sendMessage(" §7● §f/1vs1 create [Arena]");
			p.sendMessage(" §7● §6/1vs1 setting [Arena] Spawn1");
			p.sendMessage(" §7● §f/1vs1 setting [Arena] Spawn2");
			p.sendMessage(" §7● §6/1vs1 setting [Arena] setMiddle");
			
		}
		
		if(Page == 2) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bArenen:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7● §f/1vs1 setting [Arena] setMiddle");
			p.sendMessage(" §7● §6/1vs1 setting [Arena] setReset");
			p.sendMessage(" §7● §f/1vs1 setting [Arena] Layout [Layout]");
			p.sendMessage(" §7● §6/1vs1 addACSArena [Arena] [ACS-Name]");
			p.sendMessage(" §7● §f/1vs1 cloneArena [Arena] [NewArena]");
			
		}
		
		if(Page == 3) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bArenen:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7● §6/1vs1 toggleArena [Arena]");
			p.sendMessage(" §7● §f/1vs1 delete [Arena]");
			p.sendMessage(" §7● §6/1vs1 tpArena [Arena]");
			p.sendMessage(" §7● §f/1vs1 reset [Arena]");
		}
		
		if(Page == 4) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bConfigs:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7● §6/1vs1 reloadSigns");
			p.sendMessage(" §7● §f/1vs1 reloadConfig");
			p.sendMessage(" §7● §6/1vs1 reloadMessages");
		}
		
		if(Page == 5) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bEinrichtung:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7● §f/1vs1 setLobby");
			p.sendMessage(" §7● §6/1vs1 setExit");
			p.sendMessage(" §7● §f/1vs1 guide");
			p.sendMessage(" §7● §6/1vs1 setKitEdit");
			p.sendMessage(" §7● §f/1vs1 setQueue");
		}
		
		if(Page == 6) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bConfigs:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7● §6/1vs1 setBlackDealer");
			p.sendMessage(" §7● §f/1vs1 setSkull");
			p.sendMessage(" §7● §6/1vs1 setSkull30");
			p.sendMessage(" §7● §f/1vs1 setSettingsVillager");
		}
		
		if(Page == 7) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bKits & Kitstands:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7● §6/1vs1 deleteLayout [Layout]");
			p.sendMessage(" §7● §f/1vs1 saveArenaLayout [Name]");
			p.sendMessage(" §7● §6/1vs1 setting LayoutItemID [Layout] [ID]:{SubID}");
			p.sendMessage(" §7● §f/1vs1 setting LayoutAuthor [Layout] [Autor]");
		}
		
		if(Page == 8) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bSpieler:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7● §6/1vs1 toggle");
			p.sendMessage(" §7● §f/1vs1 edit");
			p.sendMessage(" §7● §6/1vs1 setting LayoutItemID [Layout] [ID]:{SubID}");
			p.sendMessage(" §7● §f/1vs1 setRankPoints [Name] [Punkte]");
		}
		
		if(Page == 9) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bSonstiges:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7● §6/1vs1 version");
			p.sendMessage(" §7● §f/1vs1 in1vs1");
			p.sendMessage(" §7● §6/1vs1 queue");
			p.sendMessage(" §7● §f/1vs1 resetList");
			p.sendMessage(" §7● §6/1vs1 check");
		}
		
		if(Page == 10) {
			p.sendMessage("     §7[§c" + Page + "/" + maxPage + "§7] §bSonstiges:");
			p.sendMessage("§c");
			
			p.sendMessage(" §7● §f/1vs1 resetStats [Name]");
			p.sendMessage(" §7● §6/1vs1 listArenas");
			p.sendMessage(" §7● §f/1vs1 help {1-X}");
		}
		
		p.sendMessage("§c");
		p.sendMessage("§f-§6-§f-§6-§f-§6-§f-§6-§f-§6-§f[§9§l1vs1§r§f]§6-§f-§6-§f-§6-§f-§6-§f-§6-§f-");
	}
	
	public static void toggle1vs1(Player p, boolean in1vs1, boolean shutdown) {
		if(!in1vs1) {
			
			
			TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
			if(!shutdown) if(tMgr != null) tMgr.delete();
			
			
			ChallangeManager.removePlayerComplete(p);
	   		
			
			//ScoreBoardManager.updateBoard(p);
			
			p.setHealth(20);
			p.setFoodLevel(20);
			
			
			
			p.setAllowFlight(false);
			p.setFlying(false);
    		p.getInventory().setArmorContents(null);
    		p.getInventory().clear();
    		
    		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
    		p.setScoreboard(board);
    		ScoreBoardManager.removeBoard(p);
    		Kit.hasKit.remove(p);
    		
    		for(Player players : Bukkit.getOnlinePlayers()) {
    			players.showPlayer(p);
    			p.showPlayer(players);
    		}
    		
    		plugin.getTeleporter().teleportExit(p);
    		
    		
    		
    		
    		if(plugin.saveOldScoreboard) 
				if(plugin.getOneVsOnePlayer(p).getOldBoard() != null) p.setScoreboard(plugin.getOneVsOnePlayer(p).getOldBoard());
			
    		
    		if(plugin.saveInvs) {
    			if(plugin.getOneVsOnePlayer(p).getPlayerInv() != null) {
    				p.getInventory().setContents(plugin.getOneVsOnePlayer(p).getPlayerInv());
        			p.updateInventory();
        		}
        		if(plugin.getOneVsOnePlayer(p).getPlayerArmor() != null) 
        			p.getInventory().setArmorContents(plugin.getOneVsOnePlayer(p).getPlayerArmor());
        		
        		
        		p.setExp(plugin.getOneVsOnePlayer(p).getPlayerXP());
        		p.setLevel(plugin.getOneVsOnePlayer(p).getPlayerLvl());
        		
        		
        		
        		p.updateInventory();
    		}
    		

    		p.setGameMode(GameMode.SURVIVAL);
    		plugin.scoreAPI.removeBoard(p);
    		
    		plugin.removePlayer(p.getUniqueId());
    		
		} else {
			
			plugin.addPlayer(p.getUniqueId());
			
			OneVsOnePlayer player = plugin.getOneVsOnePlayer(p);
			
			if(plugin.saveOldScoreboard) 
    		 if(p.getScoreboard() != null) plugin.getOneVsOnePlayer(p).setOldBoard(p.getScoreboard());;
			
			
			if(plugin.saveInvs) {
    			player.setPlayerInv(p.getInventory().getContents());
    			player.setPlayerArmor(p.getInventory().getArmorContents());
    			player.setPlayerXP(p.getExp());
    			player.setPlayerLvl(p.getLevel());
    		}
			
			plugin.getTeleporter().teleportMainSpawn(p);
			
    		
    		
    		p.getInventory().setArmorContents(null);
    		p.setAllowFlight(false);
			p.setFlying(false);
    		
    		p.setHealth(20);
    		p.setMaxHealth(20);
    		p.setFoodLevel(20);
    		p.setExp(0);
    		p.setLevel(0);
    		p.setNoDamageTicks(0);
    		
    		getItems.getLobbyItems(p, true);
    		p.updateInventory();
    		for(PotionEffect effect : p.getActivePotionEffects())  p.removePotionEffect(effect.getType());

    		
    		plugin.getDBMgr().loadUserData(p);
    	
    		
    		p.setGameMode(GameMode.ADVENTURE);
		}
	}
	
	private void sendWrongUsage(Player p) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("wrongUsage")));
	}
	
	public static void deleteFolder(File folder)
	  {
	    File[] files = folder.listFiles();
	    
	    if (files != null) {
	    	
	      for (File file : files) {
	        if (file.isDirectory()) {
	          deleteFolder(file);
	        } else {
	          file.delete();
	        }
	      }
	    }
	    folder.delete();
	  }
	
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
		 if(this.skullMode.containsKey(e.getPlayer())) {
		  
		 Player p = e.getPlayer();	 
		 YamlConfiguration cfg = plugin.getYaml("Signs");
				
		  int Place = this.skullMode.get(p);	
		  
		  Location loc = e.getClickedBlock().getLocation();
		  
		  int X = loc.getBlockX();
		  int Y = loc.getBlockY();
		  int Z = loc.getBlockZ();
		  
		  String world = loc.getWorld().getName();
		  
		  cfg.set("Top5.Skulls." + Place + ".X", X);
		  cfg.set("Top5.Skulls." + Place + ".Y", Y);
		  cfg.set("Top5.Skulls." + Place + ".Z", Z);
		  cfg.set("Top5.Skulls." + Place + ".world", world);
		  
		  while(this.skullMode.containsKey(p)) 
			  this.skullMode.remove(p);
		  
		  
				
		  try {
			cfg.save(plugin.getPluginFile("Signs"));
		  } catch (IOException eee) {
			saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Signs.yml konnte nicht gespeichert werden!");
			eee.printStackTrace();
			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName()));
			return;
		  }
		  p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("skullSet")).replaceAll("%Prefix%", plugin.prefix));
		 } else if(this.skullMode30.containsKey(e.getPlayer())) {
			  
			 Player p = e.getPlayer();	 
			 YamlConfiguration cfg = plugin.getYaml("Signs");
					
			  int Place = this.skullMode30.get(p);	
			  
			  Location loc = e.getClickedBlock().getLocation();
			  
			  int X = loc.getBlockX();
			  int Y = loc.getBlockY();
			  int Z = loc.getBlockZ();
			  
			  String world = loc.getWorld().getName();
			  
			  cfg.set("Top5.Skulls30." + Place + ".X", X);
			  cfg.set("Top5.Skulls30." + Place + ".Y", Y);
			  cfg.set("Top5.Skulls30." + Place + ".Z", Z);
			  cfg.set("Top5.Skulls30." + Place + ".world", world);
			  
			  while(this.skullMode30.containsKey(p)) 
				  this.skullMode30.remove(p);
			  
			  
					
			  try {
				cfg.save(plugin.getPluginFile("Signs"));
			  } catch (IOException eee) {
				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Signs.yml konnte nicht gespeichert werden!");
				eee.printStackTrace();
				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName()));
				return;
			  }
			  p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("skullSet")).replaceAll("%Prefix%", plugin.prefix));
			 }
		}
		
	}
	
	
	
	private void messageError(final Player p, String Arena) {
		if(!plugin.msgMeWhenIStupid) return ;
		TitleAPI.sendTitle(p, 0, 20*3, 0, plugin.msgs.getMsg("spawnPointErrorTitleL1"), plugin.msgs.getMsg("spawnPointErrorTitleL2"));
		int count = 5;
		while(count > 0) {
			
			
			Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
				
				@Override
				public void run() {
					SoundManager mgr = new SoundManager(JSound.ORB_PLING, p, 1000F, 1.0F);
					mgr.play();
					
				}
			}, count*4);
			
			count--;
		}
		
		p.sendMessage(plugin.msgs.getMsg("spawnPointErrorL1"));
		p.sendMessage(plugin.msgs.getMsg("spawnPointErrorL2"));
		p.sendMessage(plugin.msgs.getMsg("spawnPointErrorL3"));
		p.sendMessage(plugin.msgs.getMsg("spawnPointErrorL4"));
	}




	
}
