package de.OnevsOne.Guide;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.OnevsOne.main;
import de.OnevsOne.Guide.Other.OtherInv;

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.Methods.getItems;
import net.md_5.bungee.api.ChatColor;

public class Inv_Opener implements Listener {

	private static main plugin;
	@SuppressWarnings("static-access")
	public Inv_Opener(main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	public static void openMainInv(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "" + MessageReplacer.replaceStrings(plugin.msgs.getMsg("basicInvTitle"))));
		
		ItemStack Basic = getItems.createItem(Material.EMERALD, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("basicsItemName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("basicsItemLore")));
		ItemStack Layout = getItems.createItem(Material.STORAGE_MINECART, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("layoutItemName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("layoutItemLore")));
		ItemStack Arena = getItems.createItem(Material.IRON_SWORD, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaItemName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaItemLore")));
		ItemStack Other = getItems.createItem(Material.NETHER_STAR, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("miscItemName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("miscItemLore")));
		
		inv.setItem(10, Basic);
		inv.setItem(12, Layout);
		inv.setItem(14, Arena);
		inv.setItem(16, Other);
		
		p.openInventory(inv);
		
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getInventory() != null && e.getCurrentItem() != null && e.getInventory().getName().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("basicInvTitle")))) {
		 e.setCancelled(true);
		 if(e.getClickedInventory().getName().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("basicInvTitle")))) {
		  Player p = (Player) e.getWhoClicked();
		  if(e.getSlot() == 10) {
			  BaseInv.openInv(p);
			  sendClick(p);
		  }
		  if(e.getSlot() == 12) {
			  LayoutInv.openInv(p);
			  sendClick(p);
		  }
		  if(e.getSlot() == 14) {
			  ArenaInv.openInv(p);
			  sendClick(p);
		  }
		  if(e.getSlot() == 16) {
			  OtherInv.openInv(p);
			  sendClick(p);
		  }
		 } 
		}
	}
	private void sendClick(Player p) {
		SoundManager sound = new SoundManager(JSound.CLICK, p, 20.0F, 1.0F);
		sound.play();
	}
	
}
