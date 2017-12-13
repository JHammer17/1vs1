package de.OnevsOne.Kit_Methods;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.OnevsOne.main;
import de.OnevsOne.Listener.Manager.Preferences_Manager;

import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.States.PlayerState;
import net.md_5.bungee.api.ChatColor;

/**
 * Der Code ist von JHammer
 *
 * 16.08.2016 um 14:22:40 Uhr
 */
public class Multi_Kit_Manager implements Listener {

	private static main plugin;
	
	
	@SuppressWarnings("static-access")
	public Multi_Kit_Manager(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	private HashMap<UUID, Long> lastChange = new HashMap<UUID, Long>();
	
	public static void genKitSelector(final Player p) {
		
	
		 if(!plugin.getDBMgr().isConnected()) {
		  p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
		  return;
		 }
		 
				int defaultKit = 1;
				defaultKit = Integer.parseInt(plugin.getDBMgr().getDefaultKit(p.getUniqueId()));
					
					
					Inventory inv = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("selectKitInvTitle")));
					

					ItemStack loadKit = new ItemStack(Material.PRISMARINE_CRYSTALS);
					ItemMeta loadKitMeta = loadKit.getItemMeta();
					loadKitMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("loadKitItem")));
					loadKit.setItemMeta(loadKitMeta);
					
					ItemStack KitSettings = new ItemStack(Material.IRON_PLATE);
					ItemMeta KitSettingsMeta = KitSettings.getItemMeta();
					KitSettingsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("editKitSettingsItem")));
					KitSettings.setItemMeta(KitSettingsMeta);
					
					ItemStack Activated = new ItemStack(Material.INK_SACK);
					ItemMeta ActivatedMeta = Activated.getItemMeta();
					ActivatedMeta.setDisplayName(plugin.msgs.getMsg("activated").replaceAll("&", "§"));
					Activated.setDurability((short) 10);
					Activated.setItemMeta(ActivatedMeta);
					
					ItemStack Deactivated = new ItemStack(Material.INK_SACK);
					ItemMeta DeactivatedMeta = Deactivated.getItemMeta();
					DeactivatedMeta.setDisplayName(plugin.msgs.getMsg("disabled").replaceAll("&", "§"));
					Deactivated.setDurability((short) 8);
					Deactivated.setItemMeta(DeactivatedMeta);
					
					ItemStack save = new ItemStack(Material.REDSTONE_TORCH_ON);
					ItemMeta saveMeta = save.getItemMeta();
					saveMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("saveKitItem")));
					save.setItemMeta(saveMeta);
					
					ItemStack clearKit = new ItemStack(Material.BLAZE_POWDER);
					ItemMeta clearKitMeta = clearKit.getItemMeta();
					clearKitMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("deleteKitItem")));
					clearKit.setItemMeta(clearKitMeta);
					
					inv.setItem(20, KitSettings);
					inv.setItem(21, KitSettings);
					inv.setItem(22, KitSettings);
					inv.setItem(23, KitSettings);
					inv.setItem(24, KitSettings);
					
					inv.setItem(29, Deactivated);
					inv.setItem(30, Deactivated);
					inv.setItem(31, Deactivated);
					inv.setItem(32, Deactivated);
					inv.setItem(33, Deactivated);
					
					
					if(defaultKit == 1) {
					 inv.setItem(29, Activated);
					} else
					
					if(defaultKit == 2) {
					 inv.setItem(30, Activated);
					} else
					
					if(defaultKit == 3) {
			         inv.setItem(31, Activated);
					} else
					
					if(defaultKit == 4) {
					 inv.setItem(32, Activated);
					} else
					
					if(defaultKit == 5) {
					 inv.setItem(33, Activated);
					} else {
					 inv.setItem(29, Activated);
					}

					if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit) {
					 inv.setItem(11, loadKit);
					 inv.setItem(12, loadKit);
					 inv.setItem(13, loadKit);
					 inv.setItem(14, loadKit);
					 inv.setItem(15, loadKit);
						
					 inv.setItem(48, save);
					 inv.setItem(50, clearKit);
					}
					
					p.openInventory(inv);
				
			
		
		
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
    
	 if(e.getInventory().getName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("selectKitInvTitle"))) && e.getCurrentItem() != null) {
	  e.setCancelled(true);
	  if(e.getWhoClicked() instanceof Player) {
	   Player p = (Player) e.getWhoClicked();
	   
	   if(e.isRightClick()) {
		   Preferences_Manager.genSettingInv(p);
		   
			return;
		}
	   
	   if(lastChange.containsKey(p.getUniqueId())) {
		if(System.currentTimeMillis()-lastChange.get(p.getUniqueId()) < plugin.toggleCoolDown) {
		 p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("toggleCoolDown")));
		 return;
		}
	   }
		  
	   lastChange.put(p.getUniqueId(), System.currentTimeMillis());
	   
	   if(e.getClickedInventory().getName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("selectKitInvTitle")))) {
		if(e.getSlot() == 29) {
	     SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
		 manager.play();
		 setDefaultKit(p.getUniqueId(), "1");
		} else
		if(e.getSlot() == 30) {
		 if(!p.hasPermission("1vs1.MultiplyKits.useKit2") && !p.hasPermission("1vs1.MultiplyKits.*") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Premium")) {
		  p.sendMessage(plugin.noPermsUseThisKit);
		  return;
		 }
			
		 SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
		 manager.play();
		 setDefaultKit(p.getUniqueId(), "2");
		} else
		if(e.getSlot() == 31) {
		 if(!p.hasPermission("1vs1.MultiplyKits.useKit3") && !p.hasPermission("1vs1.MultiplyKits.*") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Premium")) {
		  p.sendMessage(plugin.noPermsUseThisKit);
		  return;
		 }
		 playClick(p);
		 setDefaultKit(p.getUniqueId(), "3");
		} else if(e.getSlot() == 32) {
		 if(!p.hasPermission("1vs1.MultiplyKits.useKit4") && !p.hasPermission("1vs1.MultiplyKits.*") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Premium")) {
		  p.sendMessage(plugin.noPermsUseThisKit);
		  return;
		 }
		 playClick(p);
		 setDefaultKit(p.getUniqueId(), "4");
		} else if(e.getSlot() == 33) {
		 if(!p.hasPermission("1vs1.MultiplyKits.useKit5") && !p.hasPermission("1vs1.MultiplyKits.*") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.Premium")) {
		  p.sendMessage(plugin.noPermsUseThisKit);
		  return;
		 }
		 playClick(p);
		 setDefaultKit(p.getUniqueId(), "5");
		}
		
		if(e.getSlot() == 20) {	
		 playClick(p);
		 p.openInventory(Preferences_Manager.genPrefsInv(p, "",null));
		} else if(e.getSlot() == 21) {
		 playClick(p);
		 p.openInventory(Preferences_Manager.genPrefsInv(p, "2",null));
		} else
		if(e.getSlot() == 22) {	
		 playClick(p);
		 p.openInventory(Preferences_Manager.genPrefsInv(p, "3",null));
		} else if(e.getSlot() == 23) {
		 playClick(p);
		 p.openInventory(Preferences_Manager.genPrefsInv(p, "4",null));
		} else if(e.getSlot() == 24) {
		 playClick(p);
		 p.openInventory(Preferences_Manager.genPrefsInv(p, "5",null));
		}
		
		if(e.getSlot() >= 11 && e.getSlot() <= 15) {
			if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit) {
				if(e.getSlot() == 11) {
					playClick(p);
					new KitManager(plugin).Kitload(p, p.getUniqueId().toString(), "1");
				} else
				if(e.getSlot() == 12) {
					playClick(p);
					new KitManager(plugin).Kitload(p, p.getUniqueId().toString(), "2");
				} else
				if(e.getSlot() == 13) {
					playClick(p);
					new KitManager(plugin).Kitload(p, p.getUniqueId().toString(), "3");
				} else
				if(e.getSlot() == 14) {
					playClick(p);
					new KitManager(plugin).Kitload(p, p.getUniqueId().toString(), "4");
				} else
				if(e.getSlot() == 15) {
					playClick(p);
					new KitManager(plugin).Kitload(p, p.getUniqueId().toString(), "5");
				}
			} 
		}
		
		if(e.getSlot() == 48) {
			new KitManager(plugin).KitSave(p.getInventory(), p.getInventory().getArmorContents(), p.getUniqueId().toString(), "d");
		}
		
		if(e.getSlot() == 50) {
			new KitManager(plugin).delKit(p.getUniqueId().toString(), "d");
		}
		
		regenerateInv(p, e.getClickedInventory());
	   }
	  }
	 }
	}
	
	private void setDefaultKit(UUID uuid, String ID) {
		
	 
		 if(!plugin.getDBMgr().isConnected()) {
				System.out.println(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
				return;
			}
		 plugin.getDBMgr().setDefaultKit(uuid, ID);
	 
	}
	
	private void regenerateInv(Player p, Inventory inv) {
		
		int defaultKit = 1;
		
			if(!plugin.getDBMgr().isConnected()) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
				return;
			}
			defaultKit = Integer.parseInt(plugin.getDBMgr().getDefaultKit(p.getUniqueId()));
		
		
		ItemStack loadKit = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta loadKitMeta = loadKit.getItemMeta();
		loadKitMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("loadKitItem")));
		loadKit.setItemMeta(loadKitMeta);
		
		ItemStack KitSettings = new ItemStack(Material.IRON_PLATE);
		ItemMeta KitSettingsMeta = KitSettings.getItemMeta();
		KitSettingsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("editKitSettingsItem")));
		KitSettings.setItemMeta(KitSettingsMeta);
		
		ItemStack Activated = new ItemStack(Material.INK_SACK);
		ItemMeta ActivatedMeta = Activated.getItemMeta();
		ActivatedMeta.setDisplayName(plugin.msgs.getMsg("activated").replaceAll("&", "§"));
		Activated.setDurability((short) 10);
		Activated.setItemMeta(ActivatedMeta);
		
		ItemStack Deactivated = new ItemStack(Material.INK_SACK);
		ItemMeta DeactivatedMeta = Deactivated.getItemMeta();
		DeactivatedMeta.setDisplayName(plugin.msgs.getMsg("disabled").replaceAll("&", "§"));
		Deactivated.setDurability((short) 8);
		Deactivated.setItemMeta(DeactivatedMeta);
		
		ItemStack save = new ItemStack(Material.REDSTONE_TORCH_ON);
		ItemMeta saveMeta = save.getItemMeta();
		saveMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("saveKitItem")));
		save.setItemMeta(saveMeta);
		
		ItemStack clearKit = new ItemStack(Material.BLAZE_POWDER);
		ItemMeta clearKitMeta = clearKit.getItemMeta();
		clearKitMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("deleteKitItem")));
		clearKit.setItemMeta(clearKitMeta);
		
		
		
		inv.setItem(20, KitSettings);
		inv.setItem(21, KitSettings);
		inv.setItem(22, KitSettings);
		inv.setItem(23, KitSettings);
		inv.setItem(24, KitSettings);
		
		inv.setItem(29, Deactivated);
		inv.setItem(30, Deactivated);
		inv.setItem(31, Deactivated);
		inv.setItem(32, Deactivated);
		inv.setItem(33, Deactivated);
		
		if(defaultKit == 1) {
			inv.setItem(29, Activated);
		} else
		
		if(defaultKit == 2) {
			inv.setItem(30, Activated);
		} else
		
		if(defaultKit == 3) {
			inv.setItem(31, Activated);
		} else
		
		if(defaultKit == 4) {
			inv.setItem(32, Activated);
		} else
		
		if(defaultKit == 5) {
			inv.setItem(33, Activated);
		} else {
			inv.setItem(29, Activated);
		}
		
		if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit) {
			inv.setItem(11, loadKit);
			inv.setItem(12, loadKit);
			inv.setItem(13, loadKit);
			inv.setItem(14, loadKit);
			inv.setItem(15, loadKit);
			
			inv.setItem(48, save);
			inv.setItem(50, clearKit);
		}
		
		
		p.updateInventory();
	}
	
	private void playClick(Player p) {
		SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
		manager.play();
	}
	 
}
