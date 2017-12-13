package de.OnevsOne.Guide;

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

import de.OnevsOne.main;
import de.OnevsOne.Listener.Manager.Tournament.AnvilGUI;
import de.OnevsOne.Listener.Manager.Tournament.AnvilGUI.AnvilClickEvent;

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.Methods.getItems;

public class ArenaInv implements Listener {

	private static main plugin;


	@SuppressWarnings("static-access")
	public ArenaInv(main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	HashMap<UUID, String> arenaName = new HashMap<>();
	HashMap<UUID, String> layoutName = new HashMap<>();
	
	
	public static void openInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27, MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaInvTitle")));
		
		ItemStack setName = getItems.createItem(Material.NAME_TAG , 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("setNameArenaName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("setNameArenaLore")));
		ItemStack createArena = getItems.createItem(Material.STAINED_CLAY, 5, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("createArenaName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("createArenaLore")));
		ItemStack setReset = getItems.createItem(Material.DIAMOND , 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("setResetPosName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("setResetPosLore")));
		ItemStack setLayout = getItems.createItem(Material.EYE_OF_ENDER, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("setArenaLayoutName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("setArenaLayoutLore")));
		ItemStack resetArena = getItems.createItem(Material.ENDER_PEARL, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("resetArenaName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("resetArenaLore")));
		ItemStack setPos1 = getItems.createItem(Material.STAINED_CLAY , 14, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("setArenaSpawn1Name")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("setArenaSpawn1Lore")));
		ItemStack setPos2 = getItems.createItem(Material.STAINED_CLAY, 11, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("setArenaSpawn2Name")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("setArenaSpawn2Lore")));
		ItemStack setMiddle = getItems.createItem(Material.STAINED_CLAY, 4, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("setArenaSpawn3Name")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("setArenaSpawn3Lore")));
		ItemStack back = getItems.createItem(Material.BARRIER, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("goBack")), null);
		
		
		inv.setItem(10, setName);
		inv.setItem(11, createArena);
		inv.setItem(12, setReset);
		inv.setItem(13, setLayout);
		inv.setItem(14, resetArena);
		inv.setItem(15, setPos1);
		inv.setItem(16, setPos2);
		inv.setItem(19, setMiddle);
		inv.setItem(18, back);

		
		p.openInventory(inv);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getInventory() != null && e.getCurrentItem() != null && e.getInventory().getName().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaInvTitle")))) {
		 e.setCancelled(true);
		 if(e.getClickedInventory().getName().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaInvTitle")))) {
		  final Player p = (Player) e.getWhoClicked();
		  if(e.getSlot() == 10) {
			  while(arenaName.containsKey(p.getUniqueId())) {
				  arenaName.remove(p.getUniqueId());
			  }
			  AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {

				@Override
				public void onAnvilClick(final AnvilClickEvent event) {
					if(event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT){
                        event.setWillClose(true);
                        event.setWillDestroy(true);
                        
                        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
							
							@Override
							public void run() {
			                   	 
								 String Arena = event.getName();
								 
								 arenaName.put(p.getUniqueId(), Arena);
								 sendClick(p);
								 openInv(p);
								
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
			  if(arenaName.containsKey(p.getUniqueId())) {
				  Bukkit.dispatchCommand(p, "1vs1 create " + arenaName.get(p.getUniqueId()));
				  sendClick(p);
			  } else {
				  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noArenaNameSetted")));
			  }
		  }
		  if(e.getSlot() == 12) {
			 plugin.getOneVsOnePlayer(p).setPos3(p.getLocation());
			 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("pos3Setted")));
			 Bukkit.dispatchCommand(p, "1vs1 setting " + arenaName.get(p.getUniqueId()) + " setReset");
			 sendClick(p);
		  }
		  if(e.getSlot() == 13) {
			  while(layoutName.containsKey(p.getUniqueId())) {
				  layoutName.remove(p.getUniqueId());
			  }
			  AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {

				@Override
				public void onAnvilClick(final AnvilClickEvent event) {
					if(event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT){
                        event.setWillClose(true);
                        event.setWillDestroy(true);
                        
                        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
							
							@Override
							public void run() {
			                   	 
								 String Arena = event.getName();
								 
								 
								 
								 layoutName.put(p.getUniqueId(), Arena);
								 
								 if(arenaName.containsKey(p.getUniqueId())) {
									 Bukkit.dispatchCommand(p, "1vs1 setting " + arenaName.get(p.getUniqueId()) + " Layout " + layoutName.get(p.getUniqueId()));
									 sendClick(p);
								 } else {
									 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noArenaNameSetted")));
								 }
								
								 openInv(p);
								
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
		  
		  if(e.getSlot() == 14) {
			  if(arenaName.containsKey(p.getUniqueId())) {
				   Bukkit.dispatchCommand(p, "1vs1 reset " + arenaName.get(p.getUniqueId()));
				   sendClick(p);
			  } else {
				  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noArenaNameSetted")));
			  }
		  }
		  if(e.getSlot() == 15) {
			  if(arenaName.containsKey(p.getUniqueId())) {
				   Bukkit.dispatchCommand(p, "1vs1 setting " + arenaName.get(p.getUniqueId()) + " Spawn1");
				   p.closeInventory();
				   sendClick(p);
			  } else {
				  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noArenaNameSetted")));
			  }
		  }
		  if(e.getSlot() == 16) {
			  if(arenaName.containsKey(p.getUniqueId())) {
				   Bukkit.dispatchCommand(p, "1vs1 setting " + arenaName.get(p.getUniqueId()) + " Spawn2");
				   p.closeInventory();
				   sendClick(p);
			  } else {
				  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noArenaNameSetted")));
			  }
		  }
		  if(e.getSlot() == 18) {
			  Inv_Opener.openMainInv(p);
			  sendClick(p);
		  }
		  if(e.getSlot() == 19) {
			  if(arenaName.containsKey(p.getUniqueId())) {
				   Bukkit.dispatchCommand(p, "1vs1 setting " + arenaName.get(p.getUniqueId()) + " setMiddle");
				   p.closeInventory();
				   sendClick(p);
			  } else {
				  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noArenaNameSetted")));
			  }
		  }
		  
		  
		 } 
		}
	}
	
	private void sendClick(Player p) {
		SoundManager sound = new SoundManager(JSound.CLICK, p, 20.0F, 1.0F);
		sound.play();
	}
	
}
