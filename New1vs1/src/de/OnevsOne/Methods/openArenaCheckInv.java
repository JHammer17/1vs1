package de.OnevsOne.Methods;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.OnevsOne.main;
import de.OnevsOne.MessageManager.MessageReplacer;

/**
 * Der Code ist von JHammer
 *
 * 15.05.2016 um 11:03:16 Uhr
 */
public class openArenaCheckInv {

	private static main plugin;
	
	@SuppressWarnings("static-access")
	public openArenaCheckInv(main plugin) {
		this.plugin = plugin;
	}
	
	public static void openInv(Player p, String Arena) {
 	   if(!plugin.existFile("Arenen/" + Arena + "/config")) {
  	    	p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaNotFound"), p.getDisplayName(), Arena));
  	    	return;
   	   }
 	   
 	   
 	   if(plugin.getAState().isDisabled(Arena)) {
 		   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaDisabled"), p.getDisplayName(), Arena));
 		   return;
 	   }
 	   
 	   
 	  
 	   plugin.getOneVsOnePlayer(p).setArena(Arena);
 	   
 	   
 	  
   	   Inventory inv = Bukkit.createInventory(null, 27, plugin.msgs.getMsg("arenaCheckInvTitle"));
   	   
   	   if(true) {
   		   ItemStack State = new ItemStack(Material.BOOK);
   		   ItemMeta StateMeta = State.getItemMeta();
   		   if(plugin.getAState().isFree(Arena) && plugin.getAState().isReady(Arena)) {
   			   StateMeta.setDisplayName(plugin.msgs.getMsg("arenaCheckStateReady"));
   		   } else if(!plugin.getAState().isFree(Arena) && plugin.getAState().isReady(Arena)) {
   			   StateMeta.setDisplayName(plugin.msgs.getMsg("arenaCheckStateIngame"));
   		   } else if(plugin.getAState().isFree(Arena) && !plugin.getAState().isReady(Arena)) {
   			   StateMeta.setDisplayName(plugin.msgs.getMsg("arenaCheckStateResetting"));
   		   } else {
   			   StateMeta.setDisplayName(plugin.msgs.getMsg("arenaCheckStateResetting"));
   		   }
   		   State.setItemMeta(StateMeta);
   		   inv.setItem(0, State);
   	   }
   	   
   	   if(true) {
   		   ItemStack Infos = new ItemStack(Material.PAPER);
   		   ItemMeta InfosMeta = Infos.getItemMeta();
   		   InfosMeta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemInfo"));
   		   ArrayList<String> InfoLore = new ArrayList<String>();
   		   
   		   String Datas = "";
   		   
   		   if(!plugin.getAState().isFree(Arena)) {
   			if(plugin.ArenaPlayersP1.containsKey(Arena) && plugin.ArenaPlayersP1.get(Arena).size() > 0) {
   			 String first = plugin.ArenaPlayersP1.get(Arena).get(0).getName();
   			 if(first != null && plugin.ArenaPlayersP2.get(Arena).size() > 0) {
   	   		  String Second = plugin.ArenaPlayersP2.get(Arena).get(0).getName();
   	   		  Datas = MessageReplacer.replaceArenaInfo(plugin.msgs.getMsg("arenaCheckItemInfoInfos"), first, Second, null, plugin.getPositions().getLayout(Arena), Arena);
   	   		 }
   			}
   		   } else {
   			Datas = MessageReplacer.replaceArenaInfo(plugin.msgs.getMsg("arenaCheckItemInfoInfos"), "-", "-", null, plugin.getPositions().getLayout(Arena), Arena);
   		   }
   		   
   		   String[] SplitDatas = Datas.split("\n");
   		   for(int i = 0; i < SplitDatas.length; i++) {
   			   InfoLore.add(SplitDatas[i]);
   		   }
   		   
   		   InfosMeta.setLore(InfoLore);
   		   Infos.setItemMeta(InfosMeta);
   		   
   		   inv.setItem(8, Infos);
   	   }
   	   
   	   if(true) {
   		ItemStack LayOutCheck = new ItemStack(Material.IRON_BLOCK);
   		if(plugin.getPositions().getLayout(Arena) == null) {
   		 LayOutCheck = new ItemStack(Material.REDSTONE_BLOCK);
   		 ItemMeta LayOutMeta = LayOutCheck.getItemMeta();
   		 LayOutMeta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemLayoutNotFound"));
   		 LayOutCheck.setItemMeta(LayOutMeta);
   		} else {
   		     LayOutCheck = new ItemStack(Material.EMERALD_BLOCK);
      		 ItemMeta LayOutMeta = LayOutCheck.getItemMeta();
      		 LayOutMeta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemLayoutFound"));
      		 LayOutCheck.setItemMeta(LayOutMeta);
   		}
   		inv.setItem(11, LayOutCheck);
   	   }
   	   
   	   if(true) {
      		ItemStack CopyPos = new ItemStack(Material.IRON_BLOCK);
      		if(plugin.getPositions().getPos3(Arena) == null) {
      		 CopyPos = new ItemStack(Material.REDSTONE_BLOCK);
      		 ItemMeta CopyPosMeta = CopyPos.getItemMeta();
      		 CopyPosMeta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemResetPosNotFound"));
      		 CopyPos.setItemMeta(CopyPosMeta);
      		} else {
      			CopyPos = new ItemStack(Material.EMERALD_BLOCK);
         		ItemMeta CopyPosMeta = CopyPos.getItemMeta();
         		CopyPosMeta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemResetPosFound"));
         		CopyPos.setItemMeta(CopyPosMeta);
      		}
      		inv.setItem(12, CopyPos);
      	   }
   	   
   	   if(true) {
      		ItemStack Pos1 = new ItemStack(Material.IRON_BLOCK);
      		if(plugin.getPositions().getArenaPos1(Arena) == null) {
      			Pos1 = new ItemStack(Material.REDSTONE_BLOCK);
      		 ItemMeta Pos1Meta = Pos1.getItemMeta();
      		Pos1Meta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemPos1NotFound"));
      		Pos1.setItemMeta(Pos1Meta);
      		} else {
      			Pos1 = new ItemStack(Material.EMERALD_BLOCK);
         		 ItemMeta Pos1Meta = Pos1.getItemMeta();
         		Pos1Meta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemPos1Found"));
         		Pos1.setItemMeta(Pos1Meta);
      		}
      		inv.setItem(13, Pos1);
      	   }
   	   
   	   if(true) {
   		   ItemStack Pos2 = new ItemStack(Material.IRON_BLOCK);
         		if(plugin.getPositions().getArenaPos2(Arena) == null) {
         			Pos2 = new ItemStack(Material.REDSTONE_BLOCK);
         		 ItemMeta Pos2Meta = Pos2.getItemMeta();
         		Pos2Meta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemPos2NotFound"));
         		Pos2.setItemMeta(Pos2Meta);
         		} else {
         			Pos2 = new ItemStack(Material.EMERALD_BLOCK);
            		 ItemMeta Pos2Meta = Pos2.getItemMeta();
            		Pos2Meta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemPos2Found"));
            		Pos2.setItemMeta(Pos2Meta);
         		}
         		inv.setItem(14, Pos2);
      	   }
   	   
   	   if(true) {
   		   ItemStack Pos3 = new ItemStack(Material.IRON_BLOCK);
        		if(plugin.getPositions().getArenaPos3(Arena) == null) {
        			Pos3 = new ItemStack(Material.REDSTONE_BLOCK);
        		 ItemMeta Pos3Meta = Pos3.getItemMeta();
        		Pos3Meta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemPos3NotFound"));
        		Pos3.setItemMeta(Pos3Meta);
        		} else {
        			Pos3 = new ItemStack(Material.EMERALD_BLOCK);
           		 ItemMeta Pos3Meta = Pos3.getItemMeta();
           		 Pos3Meta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemPos3Found"));
           		Pos3.setItemMeta(Pos3Meta);
        		}
        		inv.setItem(15, Pos3);
         	   }
   	   
   	   
   	   p.openInventory(inv);
	}
	
	public static void resetArenaView(Player p, String Arena, InventoryView inventoryView) {
	 	   if(!plugin.existFile("Arenen/" + Arena + "/config")) {
	 		   p.closeInventory();
	  	    	p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaNotFound"), p.getDisplayName(), Arena));
	  	    	return;
	   	   }
	   	   
	 	   
	 	   plugin.getOneVsOnePlayer(p).setArenaView(Arena);

	   	   
	   	   if(true) {
	   		   ItemStack State = new ItemStack(Material.BOOK);
	   		   ItemMeta StateMeta = State.getItemMeta();
	   		   if(plugin.getAState().isFree(Arena) && plugin.getAState().isReady(Arena)) {
	   			   StateMeta.setDisplayName(plugin.msgs.getMsg("arenaCheckStateReady"));
	   		   } else if(!plugin.getAState().isFree(Arena) && plugin.getAState().isReady(Arena)) {
	   			   StateMeta.setDisplayName(plugin.msgs.getMsg("arenaCheckStateIngame"));
	   		   } else if(plugin.getAState().isFree(Arena) && !plugin.getAState().isReady(Arena)) {
	   			   StateMeta.setDisplayName(plugin.msgs.getMsg("arenaCheckStateResetting"));
	   		   } else {
	   			   StateMeta.setDisplayName(plugin.msgs.getMsg("arenaCheckStateResetting"));
	   		   }
	   		   State.setItemMeta(StateMeta);
	   		inventoryView.setItem(0, State);
	   	   }
	   	   
	   	   if(true) {
	   		   ItemStack Infos = new ItemStack(Material.PAPER);
	   		   ItemMeta InfosMeta = Infos.getItemMeta();
	   		   InfosMeta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemInfo"));
	   		   ArrayList<String> InfoLore = new ArrayList<String>();
	   		   
	   		   
	   		   
	   		   String Datas = "";
	   		   
	   		   if(!plugin.getAState().isFree(Arena)) {
	   			Player first = null;
	   			
	   			first = plugin.ArenaPlayersP1.get(Arena).get(0);
	   			
	   			if(first != null) {
	   				String Second = plugin.ArenaPlayersP2.get(Arena).get(0).getDisplayName();
	   				//InfoLore.add("§6" + first.getName() + " §7vs. §6" + Second);
	   				Datas = MessageReplacer.replaceArenaInfo(plugin.msgs.getMsg("arenaCheckItemInfoInfos"), first.getName(), Second, null, plugin.getPositions().getLayout(Arena), Arena);
	   			}
	   			
	   		   } else {
	   			//InfoLore.add("§6- §7vs. §6-");
	   			Datas = MessageReplacer.replaceArenaInfo(plugin.msgs.getMsg("arenaCheckItemInfoInfos"), "-", "-", null, plugin.getPositions().getLayout(Arena), Arena);
	   		   }
	   		   
	   		   String[] SplitDatas = Datas.split("\n");
	   		   for(int i = 0; i < SplitDatas.length; i++) {
	   			   InfoLore.add(SplitDatas[i]);
	   		   }
	   		   
	   		   
	   		   InfosMeta.setLore(InfoLore);
	   		   Infos.setItemMeta(InfosMeta);
	   		   
	   		inventoryView.setItem(8, Infos);
	   		   
	   	   }
	   	   
	   	   if(true) {
	   		ItemStack LayOutCheck = new ItemStack(Material.IRON_BLOCK);
	   		if(plugin.getPositions().getLayout(Arena) == null) {
	   		 LayOutCheck = new ItemStack(Material.REDSTONE_BLOCK);
	   		 ItemMeta LayOutMeta = LayOutCheck.getItemMeta();
	   		 LayOutMeta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemLayoutNotFound"));
	   		 LayOutCheck.setItemMeta(LayOutMeta);
	   		} else {
	   		     LayOutCheck = new ItemStack(Material.EMERALD_BLOCK);
	      		 ItemMeta LayOutMeta = LayOutCheck.getItemMeta();
	      		 LayOutMeta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemLayoutFound"));
	      		 LayOutCheck.setItemMeta(LayOutMeta);
	   		}
	   		inventoryView.setItem(11, LayOutCheck);
	   	   }
	   	   
	   	   if(true) {
	      		ItemStack CopyPos = new ItemStack(Material.IRON_BLOCK);
	      		if(plugin.getPositions().getPos3(Arena) == null) {
	      		 CopyPos = new ItemStack(Material.REDSTONE_BLOCK);
	      		 ItemMeta CopyPosMeta = CopyPos.getItemMeta();
	      		 CopyPosMeta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemResetPosNotFound"));
	      		 CopyPos.setItemMeta(CopyPosMeta);
	      		} else {
	      			CopyPos = new ItemStack(Material.EMERALD_BLOCK);
	         		ItemMeta CopyPosMeta = CopyPos.getItemMeta();
	         		CopyPosMeta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemResetPosFound"));
	         		CopyPos.setItemMeta(CopyPosMeta);
	      		}
	      		inventoryView.setItem(12, CopyPos);
	      	   }
	   	   
	   	   if(true) {
	      		ItemStack Pos1 = new ItemStack(Material.IRON_BLOCK);
	      		if(plugin.getPositions().getArenaPos1(Arena) == null) {
	      			Pos1 = new ItemStack(Material.REDSTONE_BLOCK);
	      		 ItemMeta Pos1Meta = Pos1.getItemMeta();
	      		Pos1Meta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemPos1NotFound"));
	      		Pos1.setItemMeta(Pos1Meta);
	      		} else {
	      			Pos1 = new ItemStack(Material.EMERALD_BLOCK);
	         		 ItemMeta Pos1Meta = Pos1.getItemMeta();
	         		Pos1Meta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemPos1Found"));
	         		Pos1.setItemMeta(Pos1Meta);
	      		}
	      		inventoryView.setItem(13, Pos1);
	      	   }
	   	   
	   	   if(true) {
	   		   ItemStack Pos2 = new ItemStack(Material.IRON_BLOCK);
	         		if(plugin.getPositions().getArenaPos2(Arena) == null) {
	         			Pos2 = new ItemStack(Material.REDSTONE_BLOCK);
	         		 ItemMeta Pos2Meta = Pos2.getItemMeta();
	         		Pos2Meta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemPos2NotFound"));
	         		Pos2.setItemMeta(Pos2Meta);
	         		} else {
	         			Pos2 = new ItemStack(Material.EMERALD_BLOCK);
	            		 ItemMeta Pos2Meta = Pos2.getItemMeta();
	            		Pos2Meta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemPos2Found"));
	            		Pos2.setItemMeta(Pos2Meta);
	         		}
	         		inventoryView.setItem(14, Pos2);
	      	   }
	   	   
	   	   if(true) {
	   		   ItemStack Pos3 = new ItemStack(Material.IRON_BLOCK);
	        		if(plugin.getPositions().getArenaPos3(Arena) == null) {
	        			Pos3 = new ItemStack(Material.REDSTONE_BLOCK);
	        		 ItemMeta Pos3Meta = Pos3.getItemMeta();
	        		Pos3Meta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemPos3NotFound"));
	        		Pos3.setItemMeta(Pos3Meta);
	        		} else {
	        			Pos3 = new ItemStack(Material.EMERALD_BLOCK);
	           		 ItemMeta Pos3Meta = Pos3.getItemMeta();
	           		 Pos3Meta.setDisplayName(plugin.msgs.getMsg("arenaCheckItemPos3Found"));
	           		Pos3.setItemMeta(Pos3Meta);
	        		}
	        		inventoryView.setItem(15, Pos3);
	         	   }
	}
	
}
