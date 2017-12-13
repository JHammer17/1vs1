package de.OnevsOne.Listener;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import de.OnevsOne.main;
import de.OnevsOne.Listener.Manager.Preferences_Manager;

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.Methods.Queue.QuequePrefsMethods;
import de.OnevsOne.States.PlayerBestOfsPrefs;
import de.OnevsOne.States.PlayerPrefs;
import de.OnevsOne.States.PlayerQuequePrefs;
import net.md_5.bungee.api.ChatColor;

/**
 * Der Code ist von JHammer
 *
 * 27.05.2016 um 15:47:50 Uhr
 */
public class PlayerQueqeChangeSettings implements Listener {

	private main plugin;

	private HashMap<UUID, Long> lastChange = new HashMap<UUID, Long>();
	
	
	public PlayerQueqeChangeSettings(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onInterAct(PlayerInteractEntityEvent e) {
		/*Spieler macht einen Rechtsklick auf den Warteschlangen-Zombie*/
		if(e.getRightClicked() instanceof Zombie) {
		 if(e.getRightClicked().getCustomName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("quequeEntityName")))) {
		  if(plugin.getOneVsOnePlayer(e.getPlayer()).getPlayertournament() != null) {
		   e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentinTournament")));
		   return;
		  }
		  
		  if(!plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
		   return;
		  }
		  e.setCancelled(true);
		  QuequePrefsMethods.openInv(e.getPlayer()); //Inventar wird geöffnet
		 }
		}
	}
	
	@EventHandler
	public void onChangePref(InventoryClickEvent e) {
		/*Inventar wird verwaltet*/
		if(e.getWhoClicked() instanceof Player) {
	     if(e.getInventory().getName().equalsIgnoreCase(plugin.msgs.getMsg("quequeSettingsInvTitle")) && 
	    	e.getCurrentItem() != null) {
	    	 
	       e.setCancelled(true);
	       
	       if(!e.getClickedInventory().getTitle().equalsIgnoreCase(plugin.msgs.getMsg("quequeSettingsInvTitle"))) {
	    	   return;
	       }
	       
	       
	       Player p = (Player) e.getWhoClicked();
	       
	       if(e.isRightClick()) {
				Preferences_Manager.genSettingInv(p);
				return;
			}
	       
	     
	       /*Letzte Interaktion ist noch nicht lang genug her*/
	       if(lastChange.containsKey(p.getUniqueId())) {
	    	if(System.currentTimeMillis()-lastChange.get(p.getUniqueId()) < plugin.toggleCoolDown) {
	    	 p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("toggleCoolDown")));
	         return;
	    	}
	       }
	       /*---------------------*/
	       
	       lastChange.put(p.getUniqueId(), System.currentTimeMillis()); //In die Hashmap für den Check packen  
	       
	       
	       
	       
	       if(e.getSlot() == 3 || e.getSlot() == 4 || e.getSlot() == 5 ||
	    	  e.getSlot() == 3+9 || e.getSlot() == 4+9 || e.getSlot() == 5+9) {
	    	   
	    	
	    	/*Server nutzt MySQL*/
	    	
	    	 /*Keine Verbindung -> Nachricht*/
	    	 if(!plugin.getDBMgr().isConnected()) {
			  p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
			  return;
			 }
	    	
	    	
	   		
	    	
	   		
	   		if(e.getSlot() == 3 || e.getSlot() == 3+9) {
	   			
	   			plugin.getDBMgr().setQueuePref(p.getUniqueId(), PlayerQuequePrefs.ownKit);
	   		
	   			
	   			SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
				manager.play();
	   		}
	   		if(e.getSlot() == 4 || e.getSlot() == 4+9) {
	   			
	   			plugin.getDBMgr().setQueuePref(p.getUniqueId(), PlayerQuequePrefs.EnemieKit);
	   			
	   			
	   			SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
				manager.play();
	   		}
	   		if(e.getSlot() == 5 || e.getSlot() == 5+9) {
	   			
	   			plugin.getDBMgr().setQueuePref(p.getUniqueId(), PlayerQuequePrefs.RandomKit);
	   			
	   			
	   			SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
				manager.play();
	   		}
	   		
	   		QuequePrefsMethods.openInv(p);
	   		
	   		
	   		
	   	   /*Automatische Warteschlange*/
	       } else if(e.getSlot() == 19 || e.getSlot() == 20) {
	    	if(Preferences_Manager.getPref(p.getUniqueId(), PlayerPrefs.QUEUE,"")) {
	    		Preferences_Manager.setPref(p.getUniqueId(), PlayerPrefs.QUEUE, false,"");
	    		QuequePrefsMethods.openInv(p);	
		    } else {
		    	Preferences_Manager.setPref(p.getUniqueId(), PlayerPrefs.QUEUE, true,"");
	    		QuequePrefsMethods.openInv(p);	
		    }
	    	
	    	
	       } else if(e.getSlot() == 7 || e.getSlot() == 8 ||
	    		   e.getSlot() == 7+9 || e.getSlot() == 8+9 ||
	    		   e.getSlot() == 7+9*2 || e.getSlot() == 8+9*2){
	    	/*First wins / Best of 3 / Best of 5*/
	    	
	    		/*Keine MySQL Verbindung*/
	    		if(!plugin.getDBMgr().isConnected()) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
					return;
				}
	    		
	    		
	    		
	    		if(e.getSlot() == 7 || e.getSlot() == 8) {
	    			plugin.getDBMgr().setQueuePref2(p.getUniqueId(), PlayerBestOfsPrefs.BestOf1);
	    			QuequePrefsMethods.openInv(p);
	    			
	    			SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
					manager.play();
	    		}
	    		if(e.getSlot() == 7+9 || e.getSlot() == 8+9) {
	    			plugin.getDBMgr().setQueuePref2(p.getUniqueId(), PlayerBestOfsPrefs.BestOf3);
	    			QuequePrefsMethods.openInv(p);
	    			
	    			SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
					manager.play();
	    		}
	    		if(e.getSlot() == 7+9*2 || e.getSlot() == 8+9*2) {
	    			plugin.getDBMgr().setQueuePref2(p.getUniqueId(), PlayerBestOfsPrefs.BestOf5);
	    			QuequePrefsMethods.openInv(p);
	    			
	    			SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
					manager.play();
	    		}
	    		
	    		
	    	
	    	/*Warteschlange beitreten/verlassen*/
	       } else if(e.getSlot() == 0 || e.getSlot() == 1 || e.getSlot() == 9 || e.getSlot() == 10) {
	    	   if(plugin.getOneVsOnePlayer(p).getTeam() != null) {
	    		   if(!plugin.getOneVsOnePlayer(p).getTeam().getPlayer().getUniqueId().equals(p.getUniqueId())) {
	    			   p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamNoLeader")));
	    			   return;
	    		   }
	    	   }
	    	   
	    	   if(plugin.getOneVsOnePlayer(p).isInQueue()) {
					  plugin.getOneVsOnePlayer(p).setInQueue(false);
					  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("toggleQueque1"), p.getDisplayName()));
					  
					  SoundManager manager = new SoundManager(JSound.DRUM, p, 10.0F, 1.0F);
					  manager.play();
				  } else {
					  plugin.getOneVsOnePlayer(p).setInQueue(true);
					  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("toggleQueque2"), p.getDisplayName()));
					  
					  SoundManager manager = new SoundManager(JSound.ORB_PLING, p, 10.0F, 1.0F);
					  manager.play();
				  }  
	    	   	  QuequePrefsMethods.openInv(p);	
	    	   
	       }
	      }
	     }
		}
	}


