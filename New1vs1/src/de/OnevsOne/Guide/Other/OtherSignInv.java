package de.OnevsOne.Guide.Other;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.OnevsOne.main;

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.SignMethods;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.Methods.getItems;
import de.OnevsOne.Methods.saveErrorMethod;
import de.OnevsOne.States.AllErrors;

public class OtherSignInv implements Listener {

	private static main plugin;

	@SuppressWarnings("static-access")
	public OtherSignInv(main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	HashMap<UUID, Integer> sign = new HashMap<>();

	public static void openSign(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27, MessageReplacer.replaceStrings(plugin.msgs.getMsg("signsInvTitle")));
		
		ItemStack Top5_1 = getItems.createItem(Material.SIGN, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("signsTop5P1Name")),MessageReplacer.replaceStrings(plugin.msgs.getMsg("signsTop5P1Lore")));
		ItemStack Top5_2 = getItems.createItem(Material.SIGN, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("signsTop5P2Name")),MessageReplacer.replaceStrings(plugin.msgs.getMsg("signsTop5P2Lore")));
		ItemStack Top5_3 = getItems.createItem(Material.SIGN, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("signsTop5P3Name")),MessageReplacer.replaceStrings(plugin.msgs.getMsg("signsTop5P3Lore")));
		ItemStack Top5_4 = getItems.createItem(Material.SIGN, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("signsTop5P4Name")),MessageReplacer.replaceStrings(plugin.msgs.getMsg("signsTop5P4Lore")));
		ItemStack Top5_5 = getItems.createItem(Material.SIGN, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("signsTop5P5Name")),MessageReplacer.replaceStrings(plugin.msgs.getMsg("signsTop5P5Lore")));
		
		ItemStack join = getItems.createItem(Material.SIGN, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("signsJoinName")),MessageReplacer.replaceStrings(plugin.msgs.getMsg("signsJoinLore")));
		ItemStack leave = getItems.createItem(Material.SIGN, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("signsLeaveName")),MessageReplacer.replaceStrings(plugin.msgs.getMsg("signsLeaveLore")));
		
		ItemStack back = getItems.createItem(Material.BARRIER, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("goBack")), null);
		
		inv.setItem(10, Top5_1);
		inv.setItem(11, Top5_2);
		inv.setItem(12, Top5_3);
		inv.setItem(13, Top5_4);
		inv.setItem(14, Top5_5);
		
		inv.setItem(16, join);
		inv.setItem(17, leave);
		inv.setItem(18, back);
		
		
		p.openInventory(inv);
		
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getInventory() != null && e.getCurrentItem() != null && e.getInventory().getName().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("signsInvTitle")))) {
		 e.setCancelled(true);
		 if(e.getClickedInventory().getName().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("signsInvTitle")))) {
		  final Player p = (Player) e.getWhoClicked();
		  while(sign.containsKey(p.getUniqueId())) {
			  sign.remove(p.getUniqueId());
		  }
		  if(e.getSlot() == 10) {
			  sign.put(p.getUniqueId(), 1);
			  sendClick(p);
		  } else 
		  if(e.getSlot() == 11) {
			  sign.put(p.getUniqueId(), 2);
			  sendClick(p);
		  } else
		  if(e.getSlot() == 12) {
			  sign.put(p.getUniqueId(), 3);
			  sendClick(p);
		  } else
		  if(e.getSlot() == 13) {
			  sign.put(p.getUniqueId(), 4);
			  sendClick(p);
		  } else
		  if(e.getSlot() == 14) {
			  sign.put(p.getUniqueId(), 5);
			  sendClick(p);
		  } else
		  if(e.getSlot() == 16) {
			  sign.put(p.getUniqueId(), 11);
			  sendClick(p);
		  } else 
		  if(e.getSlot() == 17) {
			  sign.put(p.getUniqueId(), 12);
			  sendClick(p);
		  } else
		  if(e.getSlot() == 18) {
			  OtherInv.openInv(p);
			  sendClick(p);
			  return;
		  } else {
			  return;
		  }
		  p.closeInventory();
		  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("signsInfo")));

		 } 
		}
	}
	
	@EventHandler
	public void onSet(PlayerInteractEvent e) {
	 if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
	  if(sign.containsKey(e.getPlayer().getUniqueId())) {
	   if(e.getClickedBlock().getType() == Material.SIGN_POST || e.getClickedBlock().getType() == Material.WALL_SIGN) {
	    e.setCancelled(true);
	    
	    int type = sign.get(e.getPlayer().getUniqueId());
	    
	    Player p = e.getPlayer();
	    Location loc = e.getClickedBlock().getLocation();
	    
	    if(type == 1 ||
	       type == 2 ||
	       type == 3 ||
	       type == 4 ||
	       type == 5) {
	    	creatTop(p, loc, type);
	    }
	   
	    if(type == 11) {
	    	createJoin(p, loc);
	    }
	    if(type == 12) {
	    	createLeave(p, loc);
	    }
	    
	    
	    while(sign.containsKey(e.getPlayer().getUniqueId())) {
	    	sign.remove(e.getPlayer().getUniqueId());
	    }
	   } else {
		   e.getPlayer().sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("signsNoSign")));
	   }
	  }
	 }
	}
	
	public void createJoin(Player p, Location signLoc) {
		 YamlConfiguration cfg = plugin.getYaml("signs");
		 
		 Sign s = (Sign) signLoc.getBlock().getState();
		 
		 s.setLine(0, plugin.msgs.getMsg("joinSignLine1").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
		 s.setLine(1, plugin.msgs.getMsg("joinSignLine2").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
		 s.setLine(2, plugin.msgs.getMsg("joinSignLine3").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
		 s.setLine(3, plugin.msgs.getMsg("joinSignLine4").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
		 
		 s.update();
		 
		 int next = 1;
		 
		 if (cfg.getConfigurationSection("Signs.join") != null) {
		  for (@SuppressWarnings("unused") String Schilder : cfg.getConfigurationSection("Signs.join").getKeys(false)) {
		   next++;
		  }
		 }

		 
		 
		 cfg.set("Signs.join." + next + ".X", signLoc.getBlockX());
		 cfg.set("Signs.join." + next + ".Y", signLoc.getBlockY());
		 cfg.set("Signs.join." + next + ".Z", signLoc.getBlockZ());
		 cfg.set("Signs.join." + next + ".World", signLoc.getWorld().getName());
		 
		 
		 try {
			cfg.save(plugin.getPluginFile("signs"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		 
		 new SignMethods(plugin).reloadJoinSigns();
		 new SignMethods(plugin).refreshJoinSigns();
		 
		 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("signRegistered"), p.getDisplayName()));

	}
	
	public void createLeave(Player p, Location loc) {
		 Sign s = (Sign) loc.getBlock().getState();
		 
		 s.setLine(0, plugin.msgs.getMsg("leaveSignLine1").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
		 s.setLine(1, plugin.msgs.getMsg("leaveSignLine2").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
		 s.setLine(2, plugin.msgs.getMsg("leaveSignLine3").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
		 s.setLine(3, plugin.msgs.getMsg("leaveSignLine4").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
		 
		 s.update();
		 
		 new SignMethods(plugin).reloadJoinSigns();
		 new SignMethods(plugin).refreshJoinSigns();
		 
		 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("signRegistered"), p.getDisplayName()));
	}
	
	public void creatTop(Player p, Location loc, int Top) {
		try {
			if(Top > 5 || Top <= 0) {
				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("wrongNumberSkull")));
				return;
			}
			
			
			
			int X = loc.getBlockX();
			int Y = loc.getBlockY();
			int Z = loc.getBlockZ();
			
			String world = loc.getWorld().getName();
			
			YamlConfiguration cfg = plugin.getYaml("Signs");
			
			cfg.set("Top5.Signs." + Top + ".X", X);
			cfg.set("Top5.Signs." + Top + ".Y", Y);
			cfg.set("Top5.Signs." + Top + ".Z", Z);
			cfg.set("Top5.Signs." + Top + ".world", world);
			
			try {
				cfg.save(plugin.getPluginFile("Signs"));
			  } catch (IOException ee) {
				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "Die File signs.yml konnte nicht gespeichert werden");
				ee.printStackTrace();
				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName()));
				return;
			  }
			   new SignMethods(plugin).refreshTop5();
	    	   new SignMethods(plugin).reloadJoinSigns();
	    	   new SignMethods(plugin).refreshJoinSigns();
			 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("signRegistered"), p.getDisplayName()));
		 } catch (NumberFormatException ee) {
			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noNumber")));
			return;
		 }
	}
	
	private void sendClick(Player p) {
		SoundManager sound = new SoundManager(JSound.CLICK, p, 20.0F, 1.0F);
		sound.play();
	}
}
