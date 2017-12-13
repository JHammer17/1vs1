package de.OnevsOne.Listener.Manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import de.OnevsOne.main;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.Methods.getItems;
import net.md_5.bungee.api.ChatColor;

public class DisableMapsManager implements Listener{

	private static main plugin;

	
	static HashMap<UUID, HashMap<Integer,String>> invs = new HashMap<>();
	static HashMap<UUID, Integer> offsetInv = new HashMap<>();
	
	@SuppressWarnings("static-access")
	public DisableMapsManager(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public static int getMaxOffset() {
		File[] files = new File("plugins/1vs1/ArenaLayouts/").listFiles();
		
		int maps = 0;
		int max = 0;
		
		if(files != null) {
		 for(int i = 0; i < files.length; i++) {
		  if(!files[i].getName().endsWith(".yml")) {
		   continue;
		  }
		  maps++;
		 }
		}
		
		if(maps > 24) {
			maps -= 24;
			while(maps > 0) {
				maps -= 4;
				max++;
			}
		}
		
		return max;
	}
	
	public static void openInv(final Player p, int offset) {
		final Inventory inv = Bukkit.createInventory(null, 54, MessageReplacer.replaceStrings(plugin.msgs.getMsg("mapInvTitle")));
		
		final int offsetF = offset*4;
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			
			@SuppressWarnings({ "deprecation" })
			@Override
			public void run() {
				int slot = 0;
				int num = 1;
				int com = 0;
				
				
				
				File[] files = new File("plugins/1vs1/ArenaLayouts/").listFiles();
				invs.remove(p.getUniqueId());
				HashMap<Integer, String> maps = new HashMap<>();
				if(files != null) {
				 for(int i = 0; i < files.length; i++) {
				  if(!files[i].getName().endsWith(".yml")) {
					  continue;
				  }
				  
				  
				  if(offsetF > com) {
					  com++;
					  
					  int offset = 0;
					  if(offsetInv.containsKey(p.getUniqueId())) {
						  offset = offsetInv.get(p.getUniqueId());
					  }
					  ItemStack down = getItems.createItem(Material.STAINED_GLASS_PANE, 0, getMaxOffset()-offset, plugin.msgs.getMsg("scrollDown"), null);
					  ItemStack up = getItems.createItem(Material.STAINED_GLASS_PANE, 0, offset, plugin.msgs.getMsg("scrollUp"), null);
					  
					  if(offsetInv.containsKey(p.getUniqueId()) && offsetInv.get(p.getUniqueId()) != 0) {
						  inv.setItem(4, up);
					  }
					  if(getMaxOffset()-offset > 0) {
						  inv.setItem(4+9*5, down);
					  }
					 
					  
					  continue;
				  }
				  if(com >= 24+offsetF) {
					  
					  int offset = 0;
					  if(offsetInv.containsKey(p.getUniqueId())) {
						  offset = offsetInv.get(p.getUniqueId());
					  }
					  
					  ItemStack down = getItems.createItem(Material.STAINED_GLASS_PANE, 0, getMaxOffset()-offset, plugin.msgs.getMsg("scrollDown"), null);
					  ItemStack up = getItems.createItem(Material.STAINED_GLASS_PANE, 0, offset, plugin.msgs.getMsg("scrollUp"), null);
					  
					  
					  if(getMaxOffset()-offset > 0) {
						  inv.setItem(4+9*5, down);
					  }
					  
					  if(offsetInv.containsKey(p.getUniqueId()) && offsetInv.get(p.getUniqueId()) != 0) {
						  inv.setItem(4, up);
					  }
					  break;
				  }
				  
				   ItemStack map = new ItemStack(Material.STONE);
				   YamlConfiguration cfg = plugin.getYaml("ArenaLayouts/" + files[i].getName().replaceAll(".yml", ""));
				   if(cfg.getInt("Arena.ItemID") != 0) {
					   map = new ItemStack(cfg.getInt("Arena.ItemID"));
					   map.setDurability((short) cfg.getInt("Arena.SubID"));
				   }
				   
				   ItemMeta mapMeta = map.getItemMeta();
				   ArrayList<String> lore = new ArrayList<>();
				   if(cfg.getString("Arena.Author") != null) {
					   lore.add("§c");
					   lore.add(MessageReplacer.replaceStrings(plugin.msgs.getMsg("disableAuthor")).replaceAll("%Author%", cfg.getString("Arena.Author")));
				   } else {
					   lore.add("§c");
					   lore.add(MessageReplacer.replaceStrings(plugin.msgs.getMsg("disableNoAuthor")));
				   }
				   
				   mapMeta.setLore(lore);
				   mapMeta.setDisplayName("§6" + files[i].getName().replaceAll(".yml", ""));
				   map.setItemMeta(mapMeta);
				   
				   ItemStack enabled = new ItemStack(Material.INK_SACK);
				   ItemMeta enabledMeta = enabled.getItemMeta();
				   
				   
					   if(plugin.getDBMgr().isMapDisabled(p.getUniqueId(), files[i].getName().replaceAll(".yml", ""))) {
						   enabled.setDurability((short) 8);
						   enabledMeta.setDisplayName(MessageReplacer.replaceStrings(plugin.msgs.getMsg("disabled")));
					   } else {
						   enabled.setDurability((short) 10);
						   enabledMeta.setDisplayName(MessageReplacer.replaceStrings(plugin.msgs.getMsg("activated")));
					   }
				   
				   
				   
				  
				   enabled.setItemMeta(enabledMeta);
				   
				   
				   
				   
				   
				   if(num == 1 || num == 3) {
					   inv.setItem(slot, map);
					   maps.put(slot, files[i].getName().replaceAll(".yml", ""));
					   slot++;
					   inv.setItem(slot, enabled);
					   maps.put(slot, files[i].getName().replaceAll(".yml", ""));
				   }
				   if(num == 2 || num == 4) {
					   inv.setItem(slot, enabled);
					   maps.put(slot, files[i].getName().replaceAll(".yml", ""));
					   slot++;
					   inv.setItem(slot, map);
					   maps.put(slot, files[i].getName().replaceAll(".yml", ""));
					   if(num == 2) {
						slot++;
					   }
				   }
				   
				   slot++;
				   num++;
				   com++;
				   if(num > 4) {
					   num = 1;
				   }
				  
				 }
				}

				invs.put(p.getUniqueId(), maps);
				
				ItemStack stack = getItems.createItem(Material.STAINED_GLASS_PANE, 7, 1, "§c", null);
				if(inv.getItem(4) == null) {
					inv.setItem(4, stack);
				}
				
				inv.setItem(4+9, stack);
				inv.setItem(4+9*2, stack);
				inv.setItem(4+9*3, stack);
				inv.setItem(4+9*4, stack);
				if(inv.getItem(4+9*5) == null) {
					inv.setItem(4+9*5, stack);
				}
				
				
				
				
				Bukkit.getScheduler().runTask(plugin, new Runnable() {
					
					@Override
					public void run() {
						p.openInventory(inv);
						
					}
				});
				
				
			}
		});
		
	}
	
	public static void openInv(final Player p, int offset, final Inventory inv) {
		
		
		final int offsetF = offset*4;
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				int slot = 0;
				int num = 1;
				int com = 0;
				
				
				
				
				File[] files = new File("plugins/1vs1/ArenaLayouts/").listFiles();
				invs.remove(p.getUniqueId());
				HashMap<Integer, String> maps = new HashMap<>();
				if(files != null) {
				 for(int i = 0; i < files.length; i++) {
				  if(!files[i].getName().endsWith(".yml")) {
					  continue;
				  }
				  
				  
				  if(offsetF > com) {
					  com++;
					  
					  int offset = 0;
					  if(offsetInv.containsKey(p.getUniqueId())) {
						  offset = offsetInv.get(p.getUniqueId());
					  }
					  
					  ItemStack down = getItems.createItem(Material.STAINED_GLASS_PANE, 0, getMaxOffset()-offset, MessageReplacer.replaceStrings(plugin.msgs.getMsg("scrollDown")), null);
					  ItemStack up = getItems.createItem(Material.STAINED_GLASS_PANE, 0, offset, MessageReplacer.replaceStrings(plugin.msgs.getMsg("scrollUp")), null);
					  
					  if(getMaxOffset()-offset > 0) {
						  inv.setItem(4+9*5, down);
					  }
					  
					  if(offsetInv.containsKey(p.getUniqueId()) && offsetInv.get(p.getUniqueId()) != 0) {
						  inv.setItem(4, up);
					  } else {
						 inv.setItem(4, null);
					  }
					  continue;
				  }
				  if(com >= 24+offsetF) {
					  
					  int offset = 0;
					  if(offsetInv.containsKey(p.getUniqueId())) {
						  offset = offsetInv.get(p.getUniqueId());
					  }
					  
					  ItemStack down = getItems.createItem(Material.STAINED_GLASS_PANE, 0, getMaxOffset()-offset, MessageReplacer.replaceStrings(plugin.msgs.getMsg("scrollDown")), null);
					  ItemStack up = getItems.createItem(Material.STAINED_GLASS_PANE, 0, offset, MessageReplacer.replaceStrings(plugin.msgs.getMsg("scrollUp")), null);
					  
					  if(getMaxOffset()-offset > 0) {
						  inv.setItem(4+9*5, down);
					  }
					  
					  if(offsetInv.containsKey(p.getUniqueId()) && offsetInv.get(p.getUniqueId()) != 0) {
						  inv.setItem(4, up);
					  } else {
						  inv.setItem(4, null);
					  }
					  break;
				  }
				  
				   ItemStack map = new ItemStack(Material.STONE);
				   YamlConfiguration cfg = plugin.getYaml("ArenaLayouts/" + files[i].getName().replaceAll(".yml", ""));
				   if(cfg.getInt("Arena.ItemID") != 0) {
					   map = new ItemStack(cfg.getInt("Arena.ItemID"));
					   map.setDurability((short) cfg.getInt("Arena.SubID"));
				   }
				   
				   ItemMeta mapMeta = map.getItemMeta();
				   ArrayList<String> lore = new ArrayList<>();
				   if(cfg.getString("Arena.Author") != null) {
					   lore.add("§c");
					   lore.add(MessageReplacer.replaceStrings(plugin.msgs.getMsg("disableAuthor")).replaceAll("%Author%", cfg.getString("Arena.Author")));
				   } else {
					   lore.add("§c");
					   lore.add(MessageReplacer.replaceStrings(plugin.msgs.getMsg("disableNoAuthor")));
				   }
				   
				   mapMeta.setLore(lore);
				   mapMeta.setDisplayName("§6" + files[i].getName().replaceAll(".yml", ""));
				   map.setItemMeta(mapMeta);
				   
				   ItemStack enabled = new ItemStack(Material.INK_SACK);
				   ItemMeta enabledMeta = enabled.getItemMeta();
				   
				   
					   if(plugin.getDBMgr().isMapDisabled(p.getUniqueId(), files[i].getName().replaceAll(".yml", ""))) {
						   enabled.setDurability((short) 8);
						   enabledMeta.setDisplayName(MessageReplacer.replaceStrings(plugin.msgs.getMsg("disabled")));
					   } else {
						   enabled.setDurability((short) 10);
						   enabledMeta.setDisplayName(MessageReplacer.replaceStrings(plugin.msgs.getMsg("activated")));
					   }
				   
				   
				   
				  
				   enabled.setItemMeta(enabledMeta);
				   
				   
				   
				   
				   
				   if(num == 1 || num == 3) {
					   inv.setItem(slot, map);
					   maps.put(slot, files[i].getName().replaceAll(".yml", ""));
					   slot++;
					   inv.setItem(slot, enabled);
					   maps.put(slot, files[i].getName().replaceAll(".yml", ""));
				   }
				   if(num == 2 || num == 4) {
					   inv.setItem(slot, enabled);
					   maps.put(slot, files[i].getName().replaceAll(".yml", ""));
					   slot++;
					   inv.setItem(slot, map);
					   maps.put(slot, files[i].getName().replaceAll(".yml", ""));
					   if(num == 2) {
						   slot++;
					   }
				   }
				   
				   slot++;
				   num++;
				   com++;
				   
				   if(num > 4) {
					   num = 1;
				   }
				  
				 }
				}

				slot--;
				while(slot < 53) {
					slot++;
					ItemStack st = new ItemStack(Material.AIR);
					inv.setItem(slot, st);
				}
				
				invs.put(p.getUniqueId(), maps);
				
				ItemStack stack = getItems.createItem(Material.STAINED_GLASS_PANE, 7, 1, "§c", null);
				if(inv.getItem(4) == null) {
					inv.setItem(4, stack);
				}
				
				inv.setItem(4+9, stack);
				inv.setItem(4+9*2, stack);
				inv.setItem(4+9*3, stack);
				inv.setItem(4+9*4, stack);
				if(inv.getItem(4+9*5) == null) {
					inv.setItem(4+9*5, stack);
				}
				
				
				
			}
		});
		
	}
	
	HashMap<UUID, Long> cooldown = new HashMap<>();
	
	@EventHandler
	public void onClick(final InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getInventory().getName().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("mapInvTitle")))) {
		 if(e.getCurrentItem() != null) {
		  if(e.getInventory().getTitle().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("mapInvTitle")))) {
		   if(plugin.isInOneVsOnePlayers(e.getWhoClicked().getUniqueId())) {
			e.setCancelled(true);
			if(!e.getClickedInventory().getTitle().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("mapInvTitle")))) return;
			Player p = (Player) e.getWhoClicked();
			
			if(e.isRightClick()) {
				Preferences_Manager.genSettingInv(p);
				return;
			}
			
			
			
			final int slot = e.getSlot();
			
			final HashMap<Integer, String> maps = invs.get(e.getWhoClicked().getUniqueId());
			
			if(slot == 4) {
				 int offset = 0;
				 if(offsetInv.containsKey(p.getUniqueId())) offset = offsetInv.get(p.getUniqueId());
				 
				 if(offset > 0) {
					while(offsetInv.containsKey(p.getUniqueId())) offsetInv.remove(p.getUniqueId());
					offset--;
					offsetInv.put(p.getUniqueId(), offset);
					SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
					manager.play();
				 } else {
					 SoundManager manager = new SoundManager(JSound.DRUM, p, 10.0F, 1.0F);
						manager.play();
				 }
				 
				 
				 openInv(p,offset,e.getInventory());
				return;
			}
			if(slot == 4+9*5) {
				 
				 int offset = 0;
				 if(offsetInv.containsKey(p.getUniqueId())) offset = offsetInv.get(p.getUniqueId());
				 if(offset < getMaxOffset()) {
				  while(offsetInv.containsKey(p.getUniqueId())) offsetInv.remove(p.getUniqueId());
				  offset++;
				  offsetInv.put(p.getUniqueId(), offset);
				  SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
				  manager.play();
				 } else {
					 SoundManager manager = new SoundManager(JSound.DRUM, p, 10.0F, 1.0F);
						manager.play();
				 }
				 
				 
				 openInv((Player) e.getWhoClicked(),offset,e.getInventory());
				return;
			}
			
			
			if(maps == null) {
				return;
			}
			
			Player player = (Player) e.getWhoClicked();
			if(cooldown.containsKey(player.getUniqueId()) && System.currentTimeMillis()-cooldown.get(player.getUniqueId()) < plugin.toggleCoolDown) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("toggleCoolDown")));
			 return;
			}
			
			while(cooldown.containsKey(player.getUniqueId())) cooldown.remove(player.getUniqueId());
			
			cooldown.put(player.getUniqueId(), System.currentTimeMillis());
			if(maps.containsKey(slot)) {
			
			  Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
				
				@Override
				public void run() {
					if(plugin.getDBMgr().isMapDisabled(e.getWhoClicked().getUniqueId(), maps.get(slot))) {
						plugin.getDBMgr().setMapDisabled(e.getWhoClicked().getUniqueId(), maps.get(slot), false);
					  } else {
						  plugin.getDBMgr().setMapDisabled(e.getWhoClicked().getUniqueId(), maps.get(slot), true);
					  }
					  if(checkAllMapsDisabled(e.getWhoClicked().getUniqueId())) {
						  plugin.getDBMgr().setMapDisabled(e.getWhoClicked().getUniqueId(), maps.get(slot), false);
						  e.getWhoClicked().sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noMapEnabled")));
					  }
					 Bukkit.getScheduler().runTask(plugin, new Runnable() {
						
						@Override
						public void run() {
							 Player p = (Player) e.getWhoClicked();
							 int offset = 0;
							 if(offsetInv.containsKey(p.getUniqueId())) offset = offsetInv.get(p.getUniqueId());
							 SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
							 manager.play();
							 openInv((Player) e.getWhoClicked(),offset,e.getInventory());
							
						}
					});
					
				}
			});
			 
			  
			 
			}
		   }
		  }
		 }
		}
	}
	
	public static boolean checkAllMapsDisabled(UUID uuid) {
		File[] files = new File("plugins/1vs1/ArenaLayouts/").listFiles();
		
		if(plugin.useMySQL) {
			if(files != null) {
				 for(int i = 0; i < files.length; i++) {
				  if(!files[i].getName().endsWith(".yml")) {
					  continue;
				  }
				  String name = files[i].getName().replaceAll(".yml", "");
				  
				  if(!plugin.getDBMgr().isMapDisabled(uuid, name)) return false;
				  
				  
				  
				 }
				 return true;
				}
		} else {
			if(files != null) {
				 for(int i = 0; i < files.length; i++) {
				  if(!files[i].getName().endsWith(".yml")) {
					  continue;
				  }
				  String name = files[i].getName().replaceAll(".yml", "");
				  
				  if(!plugin.getDBMgr().isMapDisabled(uuid, name)) return false;
				  
				 }
				 return true;
				}
		}
		
		
		return false;
	}
	
	@SuppressWarnings("static-access")
	@EventHandler
	public void onInvClose(InventoryCloseEvent e) {
	 if(e.getInventory().getName().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("mapInvTitle")))) {
	  if(e.getPlayer() instanceof Player) {
	   Player p = (Player) e.getPlayer();		
	   
	   if(plugin.isInOneVsOnePlayers(p.getUniqueId())) {
		while(this.offsetInv.containsKey(p.getUniqueId())) this.offsetInv.remove(p.getUniqueId());
	   }
	  }
	 }
	}
	
	
}
