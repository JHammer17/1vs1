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
import de.OnevsOne.States.PlayerState;

public class LayoutInv implements Listener {

	private static main plugin;


	@SuppressWarnings("static-access")
	public LayoutInv(main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}

	HashMap<UUID, String> layoutName = new HashMap<>();
	
	
	public static void openInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27, MessageReplacer.replaceStrings(plugin.msgs.getMsg("layoutInvTitle")));
		
		ItemStack getAxe = getItems.createItem(Material.STONE_AXE, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("layoutGetWandName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("layoutGetWandLore")));
		ItemStack setName = getItems.createItem(Material.NAME_TAG , 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("layoutSetNameName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("layoutSetNameLore")));
		ItemStack saveLayout = getItems.createItem(Material.STAINED_CLAY, 5, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("layoutSaveLayoutName")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("layoutSaveLayoutLore")));
		ItemStack back = getItems.createItem(Material.BARRIER, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("goBack")), null);
		
		inv.setItem(10, getAxe);
		inv.setItem(11, setName);
		inv.setItem(12, saveLayout);
		inv.setItem(18, back);
		
		p.openInventory(inv);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getInventory() != null && e.getCurrentItem() != null && e.getInventory().getName().equalsIgnoreCase("Layout")) {
		 e.setCancelled(true);
		 if(e.getClickedInventory().getName().equalsIgnoreCase("Layout")) {
		  final Player p = (Player) e.getWhoClicked();
		  if(e.getSlot() == 10) {
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
			  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("layoutWandInfo")));
		  }
		  if(e.getSlot() == 11) {
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
			                   	 
								 String Layout = event.getName();
								 
								 layoutName.put(p.getUniqueId(), Layout);
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
			  if(layoutName.containsKey(p.getUniqueId())) {
				  Bukkit.dispatchCommand(p, "1vs1 saveArenaLayout " + layoutName.get(p.getUniqueId()));
				  p.closeInventory();
				  sendClick(p);
			  } else {
				  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("layoutNoNameSet")));
				  p.closeInventory();
			  }
			 
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
