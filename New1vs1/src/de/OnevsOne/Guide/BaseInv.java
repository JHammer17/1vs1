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

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.Methods.getItems;
import de.OnevsOne.States.PlayerState;

public class BaseInv implements Listener {

	private static main plugin;


	@SuppressWarnings("static-access")
	public BaseInv(main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	public static void openInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27, MessageReplacer.replaceStrings(plugin.msgs.getMsg("baseInvTitle")));
		
		ItemStack setExit = getItems.createItem(Material.REDSTONE_BLOCK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("baseSetExitName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("baseSetExitLore")));
		ItemStack setLobby = getItems.createItem(Material.EMERALD_BLOCK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("baseSetLobbyName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("baseSetLobbyLore")));
		ItemStack getAxe = getItems.createItem(Material.STONE_AXE, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("baseGetAxeName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("baseGetAxeLore")));
		ItemStack setZombie = getItems.createItem(Material.MONSTER_EGG , 54, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("baseSetQueueName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("baseSetQueueLore")));
		ItemStack setVillager = getItems.createItem(Material.MONSTER_EGG, 120, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("baseSetSettingsVillagerName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("baseSetSettingsVillagerLore")));
		ItemStack setKitEdit = getItems.createItem(Material.STAINED_CLAY, 5, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("baseSaveKitZoneName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("baseSaveKitZoneLore")));
		ItemStack setDefaultKit = getItems.createItem(Material.IRON_CHESTPLATE, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("baseSetDefaultKitName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("baseSetDefaultKitLore")));
		ItemStack back = getItems.createItem(Material.BARRIER, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("goBack")), null);
		
		inv.setItem(10, setExit);
		inv.setItem(11, setLobby);
		inv.setItem(12, setZombie);
		inv.setItem(13, setVillager);
		inv.setItem(14, getAxe);
		inv.setItem(15, setKitEdit);
		inv.setItem(16, setDefaultKit);
		inv.setItem(18, back);
		
		
		
		p.openInventory(inv);
	}

	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getInventory() != null && e.getCurrentItem() != null && e.getInventory().getName().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("baseInvTitle")))) {
		 e.setCancelled(true);
		 if(e.getClickedInventory().getName().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("baseInvTitle")))) {
		  Player p = (Player) e.getWhoClicked();
		  if(e.getSlot() == 10) {
			  Bukkit.dispatchCommand(p, "1vs1 setExit");
			  p.closeInventory();
			  sendClick(p);
		  }
		  if(e.getSlot() == 11) {
			  Bukkit.dispatchCommand(p, "1vs1 setLobby");
			  p.closeInventory();
			  sendClick(p);
		  }
		  if(e.getSlot() == 12) {
			  Bukkit.dispatchCommand(p, "1vs1 setQueue");
			  p.closeInventory();
			  sendClick(p);
		  }
		  if(e.getSlot() == 13) {
			  Bukkit.dispatchCommand(p, "1vs1 setSettingsVillager");
			  p.closeInventory();
			  sendClick(p);
		  }
		  if(e.getSlot() == 14) {
			  ItemStack stack = getItems.createItem(Material.STONE_AXE, 0, 1, "", null);
			  if(plugin.getOneVsOnePlayer(p).getpState() != PlayerState.Edit) {
				  Bukkit.dispatchCommand(p, "1vs1 edit");
			  }
			  if(plugin.getOneVsOnePlayer(p).getpState() == null) {
				  Bukkit.dispatchCommand(p, "1vs1 edit");
			  }
			  p.getInventory().addItem(stack);
			  p.closeInventory();
			  sendClick(p);
			  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("baseEditWandInfo")));
		  }
		  if(e.getSlot() == 15) {
			  Bukkit.dispatchCommand(p, "1vs1 setKitEdit");
			  sendClick(p);
			  p.closeInventory();
		  }
		  if(e.getSlot() == 16) {
			  p.closeInventory();
			  Bukkit.dispatchCommand(p, "1vs1 setDefaultKit");
			  sendClick(p);
			
		  }
		  if(e.getSlot() == 18) {
			  Inv_Opener.openMainInv(p);
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
