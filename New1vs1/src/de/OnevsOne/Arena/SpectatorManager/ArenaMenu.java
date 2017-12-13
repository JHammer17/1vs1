package de.OnevsOne.Arena.SpectatorManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.OnevsOne.main;
import de.OnevsOne.Kit_Methods.KitManager;

import de.OnevsOne.MessageManager.MessageReplacer;

/**
 * Der Code ist von JHammer
 *
 * 23.05.2016 um 16:45:07 Uhr
 */
public class ArenaMenu implements Listener {

	private static main plugin;
	
	@SuppressWarnings("static-access")
	public ArenaMenu(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public static HashMap<Integer, String> ArenaSlots = new HashMap<Integer, String>();
	
	
	@SuppressWarnings("deprecation")
	public static void openMenu(Player p) {
		Inventory inv = Bukkit.createInventory(null, 54, plugin.msgs.getMsg("spectatorArenaInvTitle"));
		
		YamlConfiguration cfg = plugin.getYaml("Arenen");
		
		if (cfg.getConfigurationSection("Arenen") == null) {
			p.sendMessage(plugin.msgs.getMsg("spectatorNoArenas"));
			return;
		}
		
		ItemStack Free = new ItemStack(Material.BARRIER);
		
		for(int i = 53; i >= 0; i--) {
			ItemMeta meta = Free.getItemMeta();
			int number = i;
			number++;
			meta.setDisplayName(plugin.msgs.getMsg("spectateMenuArenaNotDefined").replaceAll("%Number%", "" + number));
			ArrayList<String> Lore = new ArrayList<String>();
		    Lore.add(MessageReplacer.replaceStrings(plugin.msgs.getMsg("spectatorArenaNotUsed")));
		    meta.setLore(Lore);
			Free.setItemMeta(meta);
			inv.setItem(i, Free);
			Free.setItemMeta(null);
		}
		
		int Slot = 0;
		boolean useName = plugin.showArenaNamesSpectatorGUI;
		ArenaSlots.clear();
		
		for(String Arenen : cfg.getConfigurationSection("Arenen").getKeys(false)) {
		 if(cfg.getBoolean("Arenen." + Arenen)) {
		 
		  
		  
		 if(plugin.getAState().isFree(Arenen)) {
		  if(Slot < 54) {
			 
		  
		   ItemMeta FreeMeta = Free.getItemMeta();
		   if(useName) {
			if(plugin.getRAMMgr().isACSArena(Arenen)) {
			 int s = Slot;
			 s++;
			 FreeMeta.setDisplayName(MessageReplacer.replaceArenaName(plugin.msgs.getMsg("spectatorArenaItemName"), "Arena " + s));
			} else 
			 FreeMeta.setDisplayName(MessageReplacer.replaceArenaName(plugin.msgs.getMsg("spectatorArenaItemName"), Arenen));
		    } else {
			 int ShowSlot = Slot;
			 ShowSlot++;
			 FreeMeta.setDisplayName(MessageReplacer.replaceArenaID(plugin.msgs.getMsg("spectatorArenaItemNameID"), ShowSlot));
		    }
				  
		    ArrayList<String> Lore = new ArrayList<String>();
		    Lore.add(MessageReplacer.replaceArenaName(plugin.msgs.getMsg("spectatorArenaNotUsed"), Arenen));
		    FreeMeta.setLore(Lore);
				  
		    Free.setItemMeta(FreeMeta);
		    if(Slot < 54) ArenaSlots.put(Slot, Arenen);
		    inv.setItem(Slot, Free);
		   }  
		  } else {
		   if(Slot < 54) {
			   
			  
			   String layout = plugin.getPositions().getLayout(Arenen);
			   YamlConfiguration layoutFile = plugin.getYaml("ArenaLayouts/" + layout);
			   int id = layoutFile.getInt("Arena.ItemID");
			   int subId = layoutFile.getInt("Arena.SubID");
			   ItemStack Ingame = new ItemStack(Material.GOLD_BLOCK);
			   if(id != 0) Ingame = new ItemStack(id, 1, (short) subId);
			   
			   ItemMeta IngameMeta = Ingame.getItemMeta();
			   if(useName) {
				  if(plugin.getRAMMgr().isACSArena(Arenen)) {
				   int s = Slot;
				   s++;
				   IngameMeta.setDisplayName(MessageReplacer.replaceArenaName(plugin.msgs.getMsg("spectatorArenaItemName"), "Arena " + s));
				  } else 
				   IngameMeta.setDisplayName(MessageReplacer.replaceArenaName(plugin.msgs.getMsg("spectatorArenaItemName"), Arenen));
				  // IngameMeta.setDisplayName(MessageReplacer.replaceArenaName(plugin.msgs.getMsg("spectatorArenaItemName"), Arenen));
			   } else {
				int ShowSlot = Slot;
				ShowSlot++;
				IngameMeta.setDisplayName(MessageReplacer.replaceArenaID(plugin.msgs.getMsg("spectatorArenaItemNameID"), ShowSlot));
			   }
			   //-------------------------------------
			   //NAME1 <> NAME2
			   //Kit: Kit
			   //Map: Layout
			   //-------------------------------------
			   
			   String Name1 = "-";
			   String Name2 = "-"; 
			   String Kit = "-";
			   String Map = "-";
			   if(plugin.ArenaPlayersP1.containsKey(Arenen) && plugin.ArenaPlayersP1.get(Arenen).size() >= 1 &&
					   plugin.ArenaPlayersP2.containsKey(Arenen) && plugin.ArenaPlayersP2.get(Arenen).size() >= 1) {
				   
				   if(plugin.arenaTeams.containsKey(Arenen)) {
					   Name1 = plugin.arenaTeams.get(Arenen).get(0).getTeamNameDiff();
					   Name2 = plugin.arenaTeams.get(Arenen).get(1).getTeamNameDiff();
				   } else {
					   Name1 = plugin.ArenaPlayersP1.get(Arenen).get(0).getDisplayName();
					   Name2 = plugin.ArenaPlayersP2.get(Arenen).get(0).getDisplayName();
				   }
				   
			   }
			   if(plugin.ArenaKit.containsKey(Arenen)) {
				   try {
					   Kit = plugin.ArenaKit.get(Arenen);
						String[] kitS = Kit.split(":");
						
						UUID.fromString(kitS[0]);
						Kit = new KitManager(plugin).getkitAuthor(kitS[0]);
				   } catch (Exception e) {
					  Kit = plugin.ArenaKit.get(Arenen).split(":")[0];
				   }
				  
			   }
			   
			   if(plugin.getPositions().getLayout(Arenen) != null) Map = plugin.getPositions().getLayout(Arenen);
			   
			   
			   ArrayList<String> Lore = new ArrayList<String>();
			   
			   String[] Lores = MessageReplacer.replaceArenaInfo(plugin.msgs.getMsg("spectatorArenaInfos"), Name1, Name2, Kit, Map, Arenen).split("\n");
			   for(int i = 0; i < Lores.length; i++) {
				   Lore.add(Lores[i].replaceAll("\n", ""));
			   }
			   IngameMeta.setLore(Lore);
				  
			   Ingame.setItemMeta(IngameMeta);
			   if(Slot < 54) ArenaSlots.put(Slot, Arenen);
			   inv.setItem(Slot, Ingame);		 
		   }
		 } 
		  Slot++;
		 }
		}
		p.openInventory(inv);
			
	}
}
