package de.OnevsOne.Methods;

import java.io.File;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import de.OnevsOne.main;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.States.OneVsOnePlayer;
import de.OnevsOne.States.PlayerBestOfsPrefs;
import de.OnevsOne.States.PlayerQuequePrefs;
import net.md_5.bungee.api.ChatColor;

public class InventoryOpener implements Listener {




	private main plugin;

	public InventoryOpener(main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	public void openInv(final Player p) {
		
		final Inventory inv = Bukkit.createInventory(null, 9*6, MessageReplacer.replaceStrings(plugin.msgs.getMsg("queueInfoInvTitle")));
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			
			@Override
			public void run() {
				
				
				
				
				int playersInQueue = 0;
				
				for(OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) 
					if(players.isInQueue()) playersInQueue++;
				
				
				
				ItemStack infos = getItems.createItem(Material.PAPER, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("infosItemName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("infosItemLore")).replaceAll("%Players%", "" + playersInQueue));
				inv.setItem(0, infos);
				
				int slot = 9;
				
				
				for(OneVsOnePlayer queued : plugin.getOneVsOnePlayersCopy().values()) {
				
				 if(!queued.isInQueue()) break;
					
				 String uuid = queued.getPlayer().getUniqueId().toString();
				 String nick = queued.getPlayer().getDisplayName();
				 
				 PlayerQuequePrefs pref;
				 
				 
						if(!plugin.getDBMgr().isConnected()) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
							return;
						}
						pref = plugin.getDBMgr().getQuequePrefState(queued.getPlayer().getUniqueId());
					
				 
				 PlayerBestOfsPrefs Bpref = PlayerBestOfsPrefs.BestOf1;
					
						if(!plugin.getDBMgr().isConnected()) {
							System.out.println(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
							return;
						}
						Bpref = plugin.getDBMgr().getQueuePrefState2(queued.getPlayer().getUniqueId());
					
				 
					ArrayList<String> allMaps = new ArrayList<>();
					
					String enabledLore = "";
					
					
					
					
					File[] files = new File("plugins/1vs1/ArenaLayouts/").listFiles();
					
					
					if(files != null) {
					 for(int i = 0; i < files.length; i++) {
					  if(!files[i].getName().endsWith(".yml")) {
						  continue;
					  }
					   
					 
					    if(!plugin.getDBMgr().isMapDisabled(queued.getPlayer().getUniqueId(), files[i].getName().replaceAll(".yml", ""))) {
						 allMaps.add(files[i].getName().replaceAll(".yml", ""));   
					    }
					 
					 }
					}
				
					for(int i = 0; i < allMaps.size(); i++) {
						if(i == 0) {
							enabledLore = enabledLore+"§7- §6"+allMaps.get(i); 
						} else {
							enabledLore = enabledLore+"\n§7- §6"+allMaps.get(i); 
						}
						
					}
					
					
				 	//String lore = "\n§7UUID: §8" + uuid + "\n§7Nick: §6" + nick + "\n§7Kitsetting: §6" + KitPrefName(pref) + "\n§7BestOf: §6" + BestOfName(Bpref) + "\n§7Aktivierte Maps: \n"+enabledLore;
					String lore = MessageReplacer.replaceStrings(plugin.msgs.getMsg("playerInfoItemLore")).replaceAll("%UUID%", uuid).replaceAll("%Nick%", nick).replaceAll("%KitSettings%", KitPrefName(pref)).replaceAll("%BestOf%", BestOfName(Bpref)).replaceAll("%Enabled%", enabledLore);
					
					
				 	
				 ItemStack skull = getItems.createItem(Material.SKULL_ITEM, 3, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("playerInfoItemName")).replaceAll("%PlayerName%", queued.getPlayer().getName()), lore);
				 SkullMeta skullM = (SkullMeta) skull.getItemMeta();
				 skullM.setOwner(queued.getPlayer().getName());
				 skull.setItemMeta(skullM);
				 
				 inv.setItem(slot, skull);
				 slot++;
				}
				
				
				
				
				p.openInventory(inv);
				
			}
		});
		
		
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getInventory().getName().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("queueInfoInvTitle")))) {
			e.setCancelled(true);
		}
	}
	
	public String BestOfName(PlayerBestOfsPrefs pref) {
		if(pref == PlayerBestOfsPrefs.BestOf1) {
			return MessageReplacer.replaceStrings(plugin.msgs.getMsg("bestOf1ItemName"));
		} else if(pref == PlayerBestOfsPrefs.BestOf3) {
			return MessageReplacer.replaceStrings(plugin.msgs.getMsg("bestOf3ItemName"));
		} else if(pref == PlayerBestOfsPrefs.BestOf5) {
			return MessageReplacer.replaceStrings(plugin.msgs.getMsg("bestOf5ItemName"));
		} else {
			return MessageReplacer.replaceStrings(plugin.msgs.getMsg("error"));
		}
	}
	
	public String KitPrefName(PlayerQuequePrefs pref) {
		if(pref == PlayerQuequePrefs.RandomKit) {
			return MessageReplacer.replaceStrings(plugin.msgs.getMsg("quequeRandomKitSettingName"));
		} else if(pref == PlayerQuequePrefs.ownKit) {
			return MessageReplacer.replaceStrings(plugin.msgs.getMsg("quequeOwnKitSettingName"));
		} else if(pref == PlayerQuequePrefs.EnemieKit) {
			return MessageReplacer.replaceStrings(plugin.msgs.getMsg("quequeEnemieKitSettingName"));
		} else {
			return MessageReplacer.replaceStrings(plugin.msgs.getMsg("error"));
		}
	}
}
