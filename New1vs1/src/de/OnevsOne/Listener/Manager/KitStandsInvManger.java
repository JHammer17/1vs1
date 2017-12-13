package de.OnevsOne.Listener.Manager;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.OnevsOne.main;
import de.OnevsOne.Kit_Methods.KitManager;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.KitStands;
import de.OnevsOne.Methods.getItems;
import de.OnevsOne.Methods.saveErrorMethod;
import de.OnevsOne.States.AllErrors;

public class KitStandsInvManger implements Listener {

	private main plugin;

	public HashMap<Player, UUID> viewStand = new HashMap<>();
	
	public KitStandsInvManger(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onEditKitStand(PlayerInteractAtEntityEvent e) {
	 if(plugin.getOneVsOnePlayer(e.getPlayer()).isEditMode()) {
	  if(e.getPlayer().hasPermission("1vs1.editKitStand")) {
       if(e.getRightClicked() instanceof ArmorStand) {
    	if(plugin.kitStands.containsKey(e.getRightClicked().getUniqueId())) {
    		 openBaseInv(e.getPlayer(), e.getRightClicked().getUniqueId());
    		 while(viewStand.containsKey(e.getPlayer())) viewStand.remove(e.getPlayer());
    		 viewStand.put(e.getPlayer(), e.getRightClicked().getUniqueId());
    	}
       } 
	  }
	 }
	}
	
	public void openBaseInv(Player p, UUID standID) {
		
		
		Inventory inv = Bukkit.createInventory(null, 9*3, "KitStand bearbeiten");
		
		
		
		ItemStack info = getItems.createItem(Material.BOOK, 0, 1, "§6Infos", "&7Kit: &6" + plugin.kitStandsKit.get(standID) + "\n&7Name: &6" + plugin.kitStandsName.get(standID));
		ItemStack small = getItems.createItem(Material.GOLD_NUGGET, 0, 1, "§aKlein/Groß", "&7Ändert die Größe des Rüstungs(Ständer)s");
		ItemStack reload = getItems.createItem(Material.TRIPWIRE_HOOK, 0, 1, "§aNeu laden", "&7Lädt alle Stands neu");
		ItemStack changeItemInHand = getItems.createItem(Material.FIREWORK_CHARGE, 0, 1, "§aItem in der Hand ändern", "&7Ändert das Item in der Hand");
		ItemStack delete = getItems.createItem(Material.REDSTONE_BLOCK, 0, 1, "§4Löschen", "&7Löscht den KitStand");
		
		
		
		/*0*/ ItemStack statsShowTypeAllTime = getItems.createItem(Material.DIAMOND_HELMET, 0, 1, "§7Stat Typ auf §6All-Time §7setzen", isActive(0, standID));
		/*1*/ ItemStack statsShowType30Days = getItems.createItem(Material.IRON_HELMET, 0, 1, "§7Stat Typ auf §630 Tage §7setzen", isActive(1, standID));
		/*2*/ ItemStack statsShowType24h = getItems.createItem(Material.GOLD_HELMET, 0, 1, "§7Stat Typ auf §624h §7setzen", isActive(2, standID));
		
		
		
		
		
		
		
		
		
		
		
		inv.setItem(4, info);
		inv.setItem(10, small);
		inv.setItem(11, reload);
		inv.setItem(12, changeItemInHand);
		inv.setItem(14, statsShowTypeAllTime);
		inv.setItem(15, statsShowType30Days);
		inv.setItem(16, statsShowType24h);
		inv.setItem(26, delete);
		
		
		
		
		p.openInventory(inv);
	}
	
	private String isActive(int id, UUID uuid) {
		YamlConfiguration cfg = plugin.getYaml("spawns");
		int type = cfg.getInt("KitStands." + plugin.kitStandsName.get(uuid) + ".statsType");
		if(type > 2 || type < 0) type = 0;
		
		if(type == id) return "\n§a§lAktuell Aktiv";
		
		
		return "\n§c§lAktuell nicht Aktiv";
	}
	
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			if(e.getInventory().getTitle().equalsIgnoreCase("KitStand bearbeiten") &&   e.getClickedInventory() != null &&
			   e.getClickedInventory().getTitle().equalsIgnoreCase("KitStand bearbeiten")) {
				
				if(e.getCurrentItem() == null) return;
				
				if(plugin.getOneVsOnePlayer(p).isEditMode()) {
					
					UUID stand = viewStand.get(p);
					
					if(plugin.topKitStands.containsKey(stand)) {
						String path = plugin.kitStandsName.get(stand);
						
						
						YamlConfiguration cfg = plugin.getYaml("spawns");
						
						e.setCancelled(true);
						
						if(e.getSlot() == 10) {
							if(cfg.getBoolean(path + ".Small")) {
								cfg.set(path + ".Small", false);
							} else {
								cfg.set(path + ".Small", true);
							}
							try {
		    					cfg.save(plugin.getPluginFile("spawns"));
		    				} catch (IOException ee) {
		    					saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
		    					ee.printStackTrace();
		    					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), null, null, null));
		    					return;
		    				} 
							KitStands stands = new KitStands(plugin);
							stands.spawnStands();
							p.closeInventory();
						} else if(e.getSlot() == 11) {
							KitStands stands = new KitStands(plugin);
							stands.spawnStands();
							p.closeInventory();
						} else if(e.getSlot() == 12) {
							openItemInHandInv(p);
						} else if(e.getSlot() == 14) {
							cfg.set(path + ".statsType", 0);
							
							try {
								cfg.save(plugin.getPluginFile("spawns"));
							} catch (IOException e1) {
								saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
		    					e1.printStackTrace();
		    					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), null, null, null));
		    					return;
							}
							KitStands stands = new KitStands(plugin);
							stands.spawnStands();
							p.closeInventory();
						} else if(e.getSlot() == 15) {
							cfg.set(path + ".statsType", 1);
							
							try {
								cfg.save(plugin.getPluginFile("spawns"));
							} catch (IOException e1) {
								saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
		    					e1.printStackTrace();
		    					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), null, null, null));
		    					return;
							}
							KitStands stands = new KitStands(plugin);
							stands.spawnStands();
							
							p.closeInventory();
						} else if(e.getSlot() == 16) {
							cfg.set(path + ".statsType", 2);
							
							try {
								cfg.save(plugin.getPluginFile("spawns"));
							} catch (IOException e1) {
								saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
		    					e1.printStackTrace();
		    					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), null, null, null));
		    					return;
							}
							KitStands stands = new KitStands(plugin);
							stands.spawnStands();
							
							p.closeInventory();
						} else if(e.getSlot() == 26) {
							
							cfg.set(path, null);
							try {
		    					cfg.save(plugin.getPluginFile("spawns"));
		    				} catch (IOException ee) {
		    					saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
		    					ee.printStackTrace();
		    					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), null, null, null));
		    					return;
		    				}    
							p.closeInventory();
							KitStands stands = new KitStands(plugin);
							stands.spawnStands();
						}
						
						try {
	    					cfg.save(plugin.getPluginFile("spawns"));
	    				} catch (IOException ee) {
	    					saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
	    					ee.printStackTrace();
	    					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), null, null, null));
	    					return;
	    				}    
						
						return;
					}
					String Name = plugin.kitStandsName.get(stand);
					
					
					YamlConfiguration cfg = plugin.getYaml("spawns");
					
					e.setCancelled(true);
					
					if(e.getSlot() == 10) {
						if(cfg.getBoolean("KitStands." + Name + ".Small")) {
							cfg.set("KitStands." + Name + ".Small", false);
						} else {
							cfg.set("KitStands." + Name + ".Small", true);
						}
						try {
	    					cfg.save(plugin.getPluginFile("spawns"));
	    				} catch (IOException ee) {
	    					saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
	    					ee.printStackTrace();
	    					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), null, null, null));
	    					return;
	    				} 
						KitStands stands = new KitStands(plugin);
						stands.spawnStands();
						p.closeInventory();
					} else if(e.getSlot() == 11) {
						KitStands stands = new KitStands(plugin);
						stands.spawnStands();
						p.closeInventory();
					} else if(e.getSlot() == 12) {
						openItemInHandInv(p);
					} else if(e.getSlot() == 14) {
						cfg.set("KitStands." + plugin.kitStandsName.get(stand) + ".statsType", 0);
						
						try {
							cfg.save(plugin.getPluginFile("spawns"));
						} catch (IOException e1) {
							saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
	    					e1.printStackTrace();
	    					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), null, null, null));
	    					return;
						}
						KitStands stands = new KitStands(plugin);
						stands.spawnStands();
						p.closeInventory();
					} else if(e.getSlot() == 15) {
						cfg.set("KitStands." + plugin.kitStandsName.get(stand) + ".statsType", 1);
						
						try {
							cfg.save(plugin.getPluginFile("spawns"));
						} catch (IOException e1) {
							saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
	    					e1.printStackTrace();
	    					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), null, null, null));
	    					return;
						}
						KitStands stands = new KitStands(plugin);
						stands.spawnStands();
						
						p.closeInventory();
					} else if(e.getSlot() == 16) {
						cfg.set("KitStands." + plugin.kitStandsName.get(stand) + ".statsType", 2);
						
						try {
							cfg.save(plugin.getPluginFile("spawns"));
						} catch (IOException e1) {
							saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
	    					e1.printStackTrace();
	    					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), null, null, null));
	    					return;
						}
						KitStands stands = new KitStands(plugin);
						stands.spawnStands();
						
						p.closeInventory();
					} else if(e.getSlot() == 26) {
						
						cfg.set("KitStands." + Name, null);
						try {
	    					cfg.save(plugin.getPluginFile("spawns"));
	    				} catch (IOException ee) {
	    					saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
	    					ee.printStackTrace();
	    					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), null, null, null));
	    					return;
	    				}    
						p.closeInventory();
						KitStands stands = new KitStands(plugin);
						stands.spawnStands();
					}
					
					try {
    					cfg.save(plugin.getPluginFile("spawns"));
    				} catch (IOException ee) {
    					saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
    					ee.printStackTrace();
    					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), null, null, null));
    					return;
    				}    
				} else {
					e.setCancelled(true);
					p.closeInventory();
				}
			} else if(e.getInventory().getTitle().equalsIgnoreCase(plugin.msgs.getMsg("clickOnItemToChange")) && e.getClickedInventory() != null &&
					   e.getClickedInventory().getTitle().equalsIgnoreCase(plugin.msgs.getMsg("clickOnItemToChange"))) {//TODO
				
						if(e.getCurrentItem() == null) return;
				YamlConfiguration cfg = plugin.getYaml("spawns");
				
				UUID stand = viewStand.get(p);
				
				if(plugin.topKitStands.containsKey(stand)) {
					
					String path = plugin.kitStandsName.get(stand);
					
					cfg.set(path + ".customSlot", e.getSlot());
					
					
					
					try {
						cfg.save(plugin.getPluginFile("spawns"));
					} catch (IOException ee) {
						saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
						ee.printStackTrace();
						p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), null, null, null));
						return;
					}   
					
					KitStands stands = new KitStands(plugin);
					stands.fillStands();

					p.closeInventory();
					return;
				}
				
				
				String Name = plugin.kitStandsName.get(stand);
				
				cfg.set("KitStands." + Name + ".customSlot", e.getSlot());
				
				
				
				try {
					cfg.save(plugin.getPluginFile("spawns"));
				} catch (IOException ee) {
					saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die Spawns.yml konnte nicht gespeichert werden!");
					ee.printStackTrace();
					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName(), null, null, null));
					return;
				}   
				
				KitStands stands = new KitStands(plugin);
				stands.fillStands();

				p.closeInventory();
			}
			
		}
		
	}
	
	public void openItemInHandInv(Player p) {
		Inventory nInv = Bukkit.createInventory(null, 9*4, plugin.msgs.getMsg("clickOnItemToChange"));
		
		String kit = plugin.kitStandsKit.get(viewStand.get(p));
		
		if(plugin.getDBMgr().isCustomKitExists(kit.split(":")[0]) == 0) {
			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("kitNotFound")));
			return;
		}
		
		
		
		String subID = "d";
		
		if(kit.contains(":")) {
			if(kit.split(":").length >= 2) subID = kit.split(":")[1];
			kit = kit.split(":")[0];
		}
		
		//TODO Msgs
	
		
		if(plugin.getDBMgr().isCustomKitExists(kit.split(":")[0]) == 2) {
			
			try {
				if(subID.equalsIgnoreCase("d")) subID = plugin.getDBMgr().getDefaultKit(plugin.getDBMgr().getUUID(kit));
				if(subID.equalsIgnoreCase("1")) subID = "";
				
				ItemStack[] cont = new KitManager(plugin).fromBase64(plugin.getDBMgr().getKit(plugin.getDBMgr().getUUID(kit), false, subID)).getContents();
				
				if(cont != null) nInv.setContents(cont);
			} catch (Exception e) {
				
			}
		} else {
			try {
				
				ItemStack[] cont = new KitManager(plugin).fromBase64(plugin.getDBMgr().loadCustomKitInv(kit)).getContents();
				
				
				if(cont != null) nInv.setContents(cont);
				
			} catch (Exception e) {
				
			}
		}
		
		
		p.openInventory(nInv);
	}
	
	
}
