package de.OnevsOne.Guide.Other;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.OnevsOne.main;

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.Methods.getItems;

public class OtherSkullInv implements Listener {

	private static main plugin;
	
	@SuppressWarnings("static-access")
	public OtherSkullInv(main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	public static void openInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27, MessageReplacer.replaceStrings(plugin.msgs.getMsg("skullInvTitle")));
		
		ItemStack SkullTop5_1 = getItems.createItem(Material.SKULL_ITEM, 3, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("skullTop5P1Name")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("skullTop5P1Lore")));
		ItemStack SkullTop5_2 = getItems.createItem(Material.SKULL_ITEM, 3, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("skullTop5P2Name")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("skullTop5P2Lore")));
		ItemStack SkullTop5_3 = getItems.createItem(Material.SKULL_ITEM, 3, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("skullTop5P3Name")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("skullTop5P3Lore")));
		ItemStack SkullTop5_4 = getItems.createItem(Material.SKULL_ITEM, 3, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("skullTop5P4Name")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("skullTop5P4Lore")));
		ItemStack SkullTop5_5 = getItems.createItem(Material.SKULL_ITEM, 3, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("skullTop5P5Name")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("skullTop5P5Lore")));
		
		ItemStack back = getItems.createItem(Material.BARRIER, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("goBack")), null);
		
		
		inv.setItem(11, SkullTop5_1);
		inv.setItem(12, SkullTop5_2);
		inv.setItem(13, SkullTop5_3);
		inv.setItem(14, SkullTop5_4);
		inv.setItem(15, SkullTop5_5);

		inv.setItem(18, back);

		
		p.openInventory(inv);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getInventory() != null && e.getCurrentItem() != null && e.getInventory().getName().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("skullInvTitle")))) {
		 e.setCancelled(true);
		 if(e.getClickedInventory().getName().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("skullInvTitle")))) {
		  final Player p = (Player) e.getWhoClicked();
		  if(e.getSlot() == 11) {
			Bukkit.dispatchCommand(p, "1vs1 setSkull 1");  
			p.closeInventory();
			sendClick(p);
		  }
		  if(e.getSlot() == 12) {
				Bukkit.dispatchCommand(p, "1vs1 setSkull 2");  
				p.closeInventory();
				sendClick(p);
			  }
		  if(e.getSlot() == 13) {
				Bukkit.dispatchCommand(p, "1vs1 setSkull 3"); 
				p.closeInventory();
				sendClick(p);
			  }
		  if(e.getSlot() == 14) {
				Bukkit.dispatchCommand(p, "1vs1 setSkull 4");  
				p.closeInventory();
				sendClick(p);
			  }
		  if(e.getSlot() == 15) {
				Bukkit.dispatchCommand(p, "1vs1 setSkull 5"); 
				p.closeInventory();
				sendClick(p);
			  }
		  if(e.getSlot() == 18) {
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
