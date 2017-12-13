package de.OnevsOne.Kit_Methods;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.OnevsOne.main;
import de.OnevsOne.Listener.Manager.ChallangeManager;

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.Methods.getItems;
import de.OnevsOne.States.OneVsOnePlayer;
import de.OnevsOne.States.PlayerState;
import net.md_5.bungee.api.ChatColor;

/**
 * Der Code ist von JHammer
 *
 * 06.05.2016 um 10:32:07 Uhr
 */
public class Kit_Editor_Move {
	
	
	private main plugin;

	public Kit_Editor_Move(main plugin) {
		this.plugin = plugin;
		startChecker();
	}
	
	public void startChecker() {
		 final HashMap<UUID, Location> playerLocs = new HashMap<>();
		    new BukkitRunnable() {
				
				@Override
				public void run() {
					
					for(final OneVsOnePlayer p : plugin.getOneVsOnePlayersCopy().values()) 
						if(playerLocs.containsKey(p.getPlayer().getUniqueId())) 
							if(playerLocs.get(p.getPlayer().getUniqueId()).getWorld().getUID().equals(p.getPlayer().getWorld().getUID()))
							if(playerLocs.get(p.getPlayer().getUniqueId()).distance(p.getPlayer().getLocation()) > 0) {
								new BukkitRunnable() {
									
									@Override
									public void run() {
										moveMgr(p.getPlayer());
										
									}
								}.runTask(plugin);
							}
								
					
					playerLocs.clear();
					
					for(OneVsOnePlayer p : plugin.getOneVsOnePlayersCopy().values()) 
						playerLocs.put(p.getPlayer().getUniqueId(), p.getPlayer().getLocation());
					
					
					
				}
			}.runTaskTimerAsynchronously(plugin, 0,0);
	}
	
	
	public void moveMgr(Player p) {
	     
		 if(plugin.isInOneVsOnePlayers(p.getUniqueId())) {
		 
		  if(p.getLocation().getY() <= -10 && plugin.getOneVsOnePlayer(p.getUniqueId()).getpState() == PlayerState.InLobby) {
			  plugin.getTeleporter().teleportMainSpawn(p);
			  return;
		  }
		  
		  if(checkRegion(p.getLocation(), plugin.KitEditor1, plugin.KitEditor2)) {
		   if(plugin.getOneVsOnePlayer(p).getPlayertournament() != null) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentinTournament")));
			plugin.getTeleporter().teleportMainSpawn(p);
			return;
		   }	  
			  
		   if(plugin.getOneVsOnePlayer(p).getpState() != PlayerState.InKitEdit) {
			   ChallangeManager.removePlayerComplete(p);
			
			 if(!plugin.getDBMgr().isConnected()) {
			  p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
			  plugin.getTeleporter().teleportMainSpawn(p);
			  return;
			 }
		    
			   
			p.setGameMode(GameMode.CREATIVE);
			p.setAllowFlight(false);
			p.setFlying(false);
			p.getInventory().setArmorContents(null);
			p.getInventory().clear();
			
			plugin.getOneVsOnePlayer(p).setpState(PlayerState.InKitEdit);
			p.closeInventory();

			plugin.getOneVsOnePlayer(p).setInQueue(false);
			
			final Player p2 = p;
			
			Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
				
			 @Override
			 public void run() {
				new KitManager(plugin).Kitload(p2, p2.getUniqueId().toString(),"d");	
				plugin.getOneVsOnePlayer(p2).setKitLoaded(p2.getName()+":d");
			 }
			});
		   }
		  } else {
		   if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit) {
			
			 if(!plugin.getDBMgr().isConnected()) {
			  p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
			  return;
			 }
			
			 
			p.setGameMode(GameMode.ADVENTURE);
			p.setAllowFlight(false);
			p.setFlying(false);
			
			SoundManager manager = new SoundManager(JSound.ANVIL, p, 10.0F, 1.0F);
			manager.play();
			plugin.getOneVsOnePlayer(p).setpState(PlayerState.InLobby);
			p.closeInventory();
			 
			final Player p2 = p;
			final Inventory inv = p2.getInventory();
			final ItemStack[] armor = p2.getInventory().getArmorContents();
			Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
					
				@Override
				public void run() {		
					new KitManager(plugin).KitSave(inv,armor, p2.getUniqueId().toString(),"d");
					p2.getInventory().setArmorContents(null);
					getItems.getLobbyItems(p2, true);
					p2.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("kitSaved")));
				}
			 });
			}
		  }
		 }
			
	
	}
	
	
	
	public static boolean checkRegion(Location Playerloc, Location Min, Location Max) {
		    
			
		
		    if(Min == null || Max == null) return false;
		
		    if(Min.getWorld() == null || Max.getWorld() == null) return false;
		    
		    
	        if(Min.getBlockY() > Playerloc.getBlockY() || Max.getBlockY() < Playerloc.getBlockY()) {
	        	return false;
	        }
	        if (!Playerloc.getWorld().getName().equalsIgnoreCase(Min.getWorld().getName())) {
	            return false;
	        }
	        int hx = Playerloc.getBlockX();
	        int hz = Playerloc.getBlockZ();
	        if (hx < Min.getBlockX()) return false;
	        if (hx > Max.getBlockX()) return false;
	        if (hz < Min.getBlockZ()) return false;
	        if (hz > Max.getBlockZ()) return false;
	        return true;
	    
	}
}
