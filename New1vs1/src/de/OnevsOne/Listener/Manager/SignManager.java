package de.OnevsOne.Listener.Manager;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import de.OnevsOne.main;
import de.OnevsOne.Commands.MainCommand;

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.SignMethods;
import de.OnevsOne.Methods.saveErrorMethod;
import de.OnevsOne.States.AllErrors;
import net.md_5.bungee.api.ChatColor;

/**
 * Der Code ist von JHammer
 *
 * 24.05.2016 um 18:16:41 Uhr
 */
public class SignManager implements Listener {

	private main plugin;
	
	
	public SignManager(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		if(e.getLine(0).equalsIgnoreCase("[1vs1]") && e.getLine(1).equalsIgnoreCase("join")) {
			Player p = e.getPlayer();
			if(p.hasPermission("1vs1.signs.Create.Join")) {
			 Location signLoc = e.getBlock().getLocation();
			 YamlConfiguration cfg = plugin.getYaml("Signs");
			 
			 
			 e.setLine(0, plugin.msgs.getMsg("joinSignLine1").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
			 e.setLine(1, plugin.msgs.getMsg("joinSignLine2").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
			 e.setLine(2, plugin.msgs.getMsg("joinSignLine3").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
			 e.setLine(3, plugin.msgs.getMsg("joinSignLine4").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
			 
			 int next = 1;
			 
			 if(cfg.get("Signs.join.id") == null) {
				 if(cfg.getConfigurationSection("Signs.join") == null) {
					 cfg.set("Signs.join.id", 1);
					 try {
						cfg.save(plugin.getPluginFile("Signs"));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				 } else {
					 next = cfg.getConfigurationSection("Signs.join").getKeys(false).size();
					 next++;
					 cfg.set("Signs.join.id", next);
					 try {
						cfg.save(plugin.getPluginFile("Signs"));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				 }
			 } else {
				 next = cfg.getInt("Signs.join.id", next);
				 next++;
				 cfg.set("Signs.join.id", next);
				 try {
					cfg.save(plugin.getPluginFile("Signs"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			 }
			 

			 
			 
			 cfg.set("Signs.join." + next + ".X", signLoc.getBlockX());
			 cfg.set("Signs.join." + next + ".Y", signLoc.getBlockY());
			 cfg.set("Signs.join." + next + ".Z", signLoc.getBlockZ());
			 cfg.set("Signs.join." + next + ".World", signLoc.getWorld().getName());
			 
			 
			 try {
				cfg.save(plugin.getPluginFile("Signs"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			 
			 new SignMethods(plugin).reloadJoinSigns();
			 new SignMethods(plugin).refreshJoinSigns();
			 
			 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("signRegistered"), p.getDisplayName()));
			}
		} else if(e.getLine(0).equalsIgnoreCase("[1vs1]") && e.getLine(1).equalsIgnoreCase("leave")) {
			Player p = e.getPlayer();
			if(p.hasPermission("1vs1.signs.Create.Leave") || p.hasPermission("1vs1.Admin") || p.hasPermission("1vs1.*")) {
			 e.setLine(0, plugin.msgs.getMsg("leaveSignLine1").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
			 e.setLine(1, plugin.msgs.getMsg("leaveSignLine2").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
			 e.setLine(2, plugin.msgs.getMsg("leaveSignLine3").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
			 e.setLine(3, plugin.msgs.getMsg("leaveSignLine4").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
			 
			 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("signRegistered"), p.getDisplayName()));
			}
		} else if((e.getLine(0).equalsIgnoreCase("[1vs1Top5]") || e.getLine(0).equalsIgnoreCase("[1vs1Top]")) && !e.getLine(1).equalsIgnoreCase("")) {
			Player p = e.getPlayer();
			if(p.hasPermission("1vs1.signs.Create.Top5") || p.hasPermission("1vs1.Admin") || p.hasPermission("1vs1.*")) {
			 try {
				int Top = Integer.parseInt(e.getLine(1));
				if(Top <= 0) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("wrongNumberSkull")).replaceAll("%Prefix%", plugin.prefix));
					return;
				}
				
				Location loc = e.getBlock().getLocation();
				
				int X = loc.getBlockX();
				int Y = loc.getBlockY();
				int Z = loc.getBlockZ();
				
				String world = loc.getWorld().getName();
				
				YamlConfiguration cfg = plugin.getYaml("Signs");
				
				cfg.set("Top5.Signs." + Top + ".X", X);
				cfg.set("Top5.Signs." + Top + ".Y", Y);
				cfg.set("Top5.Signs." + Top + ".Z", Z);
				cfg.set("Top5.Signs." + Top + ".world", world);
				
				try {
					cfg.save(plugin.getPluginFile("Signs"));
				  } catch (IOException ee) {
					saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Ein Fehler beim schreiben in die signs.yml ist aufgetreten");
					ee.printStackTrace();
					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName()));
					return;
				  }
				
			 } catch (NumberFormatException ee) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noNumber")).replaceAll("%Prefix%", plugin.prefix));
				return;
			 }
			 new BukkitRunnable() {
				
				@Override
				public void run() {
					new SignMethods(plugin).refreshTop5();
			    	new SignMethods(plugin).reloadJoinSigns();
			    	new SignMethods(plugin).refreshJoinSigns();
					
				}
			}.runTaskLater(plugin, 1);
			 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("signRegistered"), p.getDisplayName()));
			}
			
		} else if((e.getLine(0).equalsIgnoreCase("[1vs1Top530]") || e.getLine(0).equalsIgnoreCase("[1vs1Top30]")) && !e.getLine(1).equalsIgnoreCase("")) {
			Player p = e.getPlayer();
			if(p.hasPermission("1vs1.signs.Create.Top5") || p.hasPermission("1vs1.Admin") || p.hasPermission("1vs1.*")) {
			 try {
				int Top = Integer.parseInt(e.getLine(1));
				if(Top <= 0) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("wrongNumberSkull")).replaceAll("%Prefix%", plugin.prefix));
					return;
				}
				
				Location loc = e.getBlock().getLocation();
				
				int X = loc.getBlockX();
				int Y = loc.getBlockY();
				int Z = loc.getBlockZ();
				
				String world = loc.getWorld().getName();
				
				YamlConfiguration cfg = plugin.getYaml("Signs");
				
				cfg.set("Top5.Signs30." + Top + ".X", X);
				cfg.set("Top5.Signs30." + Top + ".Y", Y);
				cfg.set("Top5.Signs30." + Top + ".Z", Z);
				cfg.set("Top5.Signs30." + Top + ".world", world);
				
				try {
					cfg.save(plugin.getPluginFile("Signs"));
				  } catch (IOException ee) {
					saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Ein Fehler beim schreiben in die signs.yml ist aufgetreten");
					ee.printStackTrace();
					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName()));
					return;
				  }
				
			 } catch (NumberFormatException ee) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noNumber")).replaceAll("%Prefix%", plugin.prefix));
				return;
			 }
			 new BukkitRunnable() {
				
				@Override
				public void run() {
					new SignMethods(plugin).refreshTop5();
			    	new SignMethods(plugin).reloadJoinSigns();
			    	new SignMethods(plugin).refreshJoinSigns();
					
				}
			}.runTaskLater(plugin, 1);
			 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("signRegistered"), p.getDisplayName()));
			}
			
		}
	}
	
	@EventHandler
	public void onInterAct(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
		 if(e.getClickedBlock().getType() == Material.SIGN_POST || e.getClickedBlock().getType() == Material.WALL_SIGN) {
		  Sign s = (Sign) e.getClickedBlock().getState();
		  String Line1 = s.getLine(0);
		  String Line2 = s.getLine(1);
		  String Line3 = s.getLine(2);
		  String Line4 = s.getLine(3);
		  
		  
		  if(plugin.joinSigns.contains(e.getClickedBlock().getLocation())) {
			  e.setCancelled(true);
			  
			  if(!e.getPlayer().hasPermission("1vs1.signs.use") && !e.getPlayer().hasPermission("1vs1.User") && !e.getPlayer().hasPermission("1vs1.*")) {
				  e.getPlayer().sendMessage(plugin.noPerms);
				  return;
			  }
			  if(!plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
				  MainCommand.toggle1vs1(e.getPlayer(), true, false);
			  } else {
				  e.getPlayer().sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("alreadyIn1vs1"), e.getPlayer().getDisplayName()));
			  }
		  }
		  

		  
		 
		  
		  if(Line1.equalsIgnoreCase(plugin.msgs.getMsg("leaveSignLine1").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize())) && 
				  Line2.equalsIgnoreCase(plugin.msgs.getMsg("leaveSignLine2").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize())) && 
				  Line3.equalsIgnoreCase(plugin.msgs.getMsg("leaveSignLine3").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize())) && 
				  Line4.equalsIgnoreCase(plugin.msgs.getMsg("leaveSignLine4").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()))) {
			  e.setCancelled(true);
			  if(!e.getPlayer().hasPermission("1vs1.signs.use") && !e.getPlayer().hasPermission("1vs1.User") && !e.getPlayer().hasPermission("1vs1.*")) {
				  e.getPlayer().sendMessage(plugin.noPerms);
				  return;
			  }
			  if(plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
				  MainCommand.toggle1vs1(e.getPlayer(), false, false);
			  } else {
				  e.getPlayer().sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("notIn1vs1"), e.getPlayer().getDisplayName()));
			  }
			 
		  }
		  
		 }
		}
	}
	
	@EventHandler
	public void onRemoveJoinSign(BlockBreakEvent e) {
		
		Player p = e.getPlayer();
		if((e.getBlock().getType() == Material.WALL_SIGN || e.getBlock().getType() == Material.SIGN_POST)) {
		 
			YamlConfiguration cfg = plugin.getYaml("Signs");
			
			if(cfg.getConfigurationSection("Signs.join") != null) {
			 for(String Schilder : cfg.getConfigurationSection("Signs.join").getKeys(false)) {
			   
			  Location signLoc = null;
					  
			  int X = cfg.getInt("Signs.join." + Schilder + ".X");
			  int Y = cfg.getInt("Signs.join." + Schilder + ".Y");
			  int Z = cfg.getInt("Signs.join." + Schilder + ".Z");
					  
			  String worldName = cfg.getString("Signs.join." + Schilder + ".World");
			  
			  if(worldName != null && Bukkit.getWorld(worldName) != null) {
			   signLoc = new Location(Bukkit.getWorld(worldName), X, Y, Z);
			   
			   if(signLoc.equals(e.getBlock().getLocation())) {
				   if(!p.hasPermission("1vs1.remJoinSign")) {
					   e.setCancelled(true);
					   plugin.sendNoPermsMessage(p);
					   break;
				   }
				   if(!plugin.getOneVsOnePlayer(p).isEditMode()) {
						 p.sendMessage(plugin.msgs.getMsg("joinSignsEditMode"));
						 e.setCancelled(true);
						 return;
					 }
					 
					 if(cfg.get("Signs.join.id") == null) {
						 if(cfg.getConfigurationSection("Signs.join") == null) {
							 cfg.set("Signs.join.id", 1);
							 try {
								cfg.save(plugin.getPluginFile("Signs"));
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						 } else {
							 int next = cfg.getConfigurationSection("Signs.join").getKeys(false).size();
							 next++;
							 cfg.set("Signs.join.id", next);
							 try {
								cfg.save(plugin.getPluginFile("Signs"));
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						 }
					 }
				   
				   cfg.set("Signs.join." + Schilder, null);
				   
				   try {
					cfg.save(plugin.getPluginFile("signs"));
				   } catch (IOException e1) {e1.printStackTrace();}
				   
				   plugin.joinSigns.remove(signLoc);
				   p.sendMessage(plugin.prefix + "Join-Schild entfernt!");
				   break;
			   }
			  }
			 }
			}
		}
		
		
	}
	
}
