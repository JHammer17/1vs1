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
import de.OnevsOne.Guide.Inv_Opener;
import de.OnevsOne.Listener.Manager.Tournament.AnvilGUI;
import de.OnevsOne.Listener.Manager.Tournament.AnvilGUI.AnvilClickEvent;

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.Methods.getItems;

public class OtherInv implements Listener {
	
	private static main plugin;


	@SuppressWarnings("static-access")
	public OtherInv(main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	
	
	
	public static void openInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27, MessageReplacer.replaceStrings(plugin.msgs.getMsg("otherInvTitle")));
		
		ItemStack deleteArena = getItems.createItem(Material.TNT, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("otherDeleteArenaName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("otherDeleteArenaLore")));
		ItemStack deleteLayout = getItems.createItem(Material.REDSTONE_BLOCK , 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("otherDeleteLayoutName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("otherDeleteArenaLore")));
		ItemStack toggleEdit = getItems.createItem(Material.REDSTONE_TORCH_ON, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("otherToggleEditModeName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("otherToggleEditModeLore")));
		ItemStack toggle1vs1 = getItems.createItem(Material.STAINED_CLAY, 5, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("otherToggle1vs1ModeName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("otherToggle1vs1ModeLore")));
		ItemStack signCreator = getItems.createItem(Material.SIGN, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("otherSignName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("otherSignLore")));
		ItemStack skullCreator = getItems.createItem(Material.SKULL_ITEM, 3, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("otherSkullsName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("otherSkullLore")));
		
		ItemStack back = getItems.createItem(Material.BARRIER, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("goBack")), null);
		
		inv.setItem(10, deleteArena);
		inv.setItem(11, deleteLayout);
		inv.setItem(12, toggleEdit);
		inv.setItem(13, toggle1vs1);
		inv.setItem(14, signCreator);
		inv.setItem(15, skullCreator);
		inv.setItem(18, back);
		
		p.openInventory(inv);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getInventory() != null && e.getCurrentItem() != null && e.getInventory().getName().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("otherInvTitle")))) {
		 e.setCancelled(true);
		 if(e.getClickedInventory().getName().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("otherInvTitle")))) {
		  final Player p = (Player) e.getWhoClicked();
		  
		  if(e.getSlot() == 10) {
			  AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {

				@Override
				public void onAnvilClick(final AnvilClickEvent event) {
					if(event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT){
                        event.setWillClose(true);
                        event.setWillDestroy(true);
                        
                        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
							
							@Override
							public void run() {
			                   	 
								 String Layout = event.getName();
								 Bukkit.dispatchCommand(p, "1vs1 delete " + Layout);
								 
								 openInv(p);
								 sendClick(p);
							}
						}, 1);
					}else{
                        event.setWillClose(false);
                        event.setWillDestroy(false);
					}
				}
          });
			  ItemStack i = getItems.createItem(Material.PAPER, 0, 1, "Name", null);
              
             
              gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, i);
              SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1F);
			  manager.play();
              gui.open();	
			  
			  
			 
		  }
		  
		  if(e.getSlot() == 11) {
			  AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {

				@Override
				public void onAnvilClick(final AnvilClickEvent event) {
					if(event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT){
                        event.setWillClose(true);
                        event.setWillDestroy(true);
                        
                        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
							
							@Override
							public void run() {
			                   	 
								 String Layout = event.getName();
								 Bukkit.dispatchCommand(p, "1vs1 deleteLayout " + Layout);
								 
								 openInv(p);
								 sendClick(p);
							}
						}, 1);
					}else{
                        event.setWillClose(false);
                        event.setWillDestroy(false);
					}
				}
          });
			  ItemStack i = getItems.createItem(Material.PAPER, 0, 1, "Name", null);
              
             
              gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, i);
              SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1F);
			  manager.play();
              gui.open();	
			  
			  
			 
		  }
		  if(e.getSlot() == 12) {
			Bukkit.dispatchCommand(p, "1vs1 edit"); 
			sendClick(p);
		  }
		  if(e.getSlot() == 13) {
			  p.closeInventory();
			  Bukkit.dispatchCommand(p, "1vs1 toggle");
			  sendClick(p);
			 
		  }
		  if(e.getSlot() == 14) {
			  OtherSignInv.openSign(p);
			  sendClick(p);
		  }
		  if(e.getSlot() == 15) {
			  OtherSkullInv.openInv(p);
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
