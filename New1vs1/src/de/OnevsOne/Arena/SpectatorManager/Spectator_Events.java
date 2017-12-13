package de.OnevsOne.Arena.SpectatorManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import de.OnevsOne.main;
import de.OnevsOne.Methods.getItems;
import de.OnevsOne.Methods.Tournament.TournamentManager;
import de.OnevsOne.Methods.Tournament.Tournament_InvCreator;
import de.OnevsOne.States.PlayerState;

/**
 * Der Code ist von JHammer
 *
 * 17.05.2016 um 18:42:28 Uhr
 */
public class Spectator_Events implements Listener {

	private main plugin;

	
	public Spectator_Events(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(plugin.getOneVsOnePlayer(p).getSpecator() != null) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(plugin.getOneVsOnePlayer(p).getSpecator() != null) {
			e.setCancelled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(plugin.getOneVsOnePlayer(p).getSpecator() != null) {
		 if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			 e.setCancelled(true); 
		 }
		 if(e.getItem() != null && e.getItem().getType() != Material.AIR) {
		  if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
		   if(e.getItem().getType() == Material.INK_SACK && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(plugin.msgs.getMsg("leaveSpec"))) {
			  if(plugin.getOneVsOnePlayer(p).getPlayertournament() != null) {
				  TournamentManager tMgr = plugin.tournaments.get(plugin.getOneVsOnePlayer(p).getPlayertournament());
				  tMgr.leaveTournament(p);
				  return;
			  }
			   e.setCancelled(true);
			   e.getPlayer().spigot().setCollidesWithEntities(true);
				  for(Player players : Bukkit.getOnlinePlayers()) {
					  players.showPlayer(e.getPlayer());
				  }
				  
				  e.getPlayer().setAllowFlight(false);
				  e.getPlayer().setFlying(false);
				  
				  plugin.getOneVsOnePlayer(p).setSpecator(null);
				  plugin.getOneVsOnePlayer(p).setpState(PlayerState.InLobby);
				  
				  
				  getItems.getLobbyItems(p, true);
				  plugin.getTeleporter().teleportMainSpawn(p);	  
		   }
		   
		   if(e.getItem().getType() == Material.NETHER_STAR && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§6Turnierinfos §7(/t)")) {
			   e.setCancelled(true);
			   if(plugin.getOneVsOnePlayer(p).getPlayertournament() != null) {
				   Tournament_InvCreator creator = new Tournament_InvCreator(plugin);
				   creator.creatInfoInv(plugin.getOneVsOnePlayer(p).getPlayertournament(), p);
			   }
			   
		   }
		   
		   if(e.getItem().getTypeId() == plugin.SpectatorItemID && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(plugin.msgs.getMsg("specateItemLobbyName"))) {
			   ArenaMenu.openMenu(e.getPlayer());	  
		   	}
		   
		   if(e.getPlayer().getInventory().getHeldItemSlot() == 0) {
			   
			   if(plugin.getOneVsOnePlayer(p).getSpecator() == null || !plugin.ArenaPlayersP1.containsKey(plugin.getOneVsOnePlayer(p).getSpecator())) {
				   return;
			   }
			   
			   if(plugin.ArenaPlayersP1.get(plugin.getOneVsOnePlayer(p).getSpecator()).size() < 1) {
				   return;
			   }
			   
			   
			   
			   Inventory inv = Bukkit.getPlayer(plugin.ArenaPlayersP1.get(plugin.getOneVsOnePlayer(p).getSpecator()).get(0).getUniqueId()).getInventory();
			   p.openInventory(inv);
		   }
		   if(e.getPlayer().getInventory().getHeldItemSlot() == 1) {
			   
			   if(plugin.getOneVsOnePlayer(p).getSpecator() == null || !plugin.ArenaPlayersP2.containsKey(plugin.getOneVsOnePlayer(p).getSpecator())) {
				   return;
			   }
			   
			   if(plugin.ArenaPlayersP2.get(plugin.getOneVsOnePlayer(p).getSpecator()).size() < 1) {
				   return;
			   }
			   Inventory inv = Bukkit.getPlayer(plugin.ArenaPlayersP2.get(plugin.getOneVsOnePlayer(p).getSpecator()).get(0).getUniqueId()).getInventory();
			   p.openInventory(inv);
		   }
		  }
		 }
		}
	}
	
	@EventHandler
	public void onPickupItem(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		if(plugin.getOneVsOnePlayer(p).getSpecator() != null) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDropItem(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if(plugin.getOneVsOnePlayer(p).getSpecator() != null) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if(plugin.getOneVsOnePlayer(p).getSpecator() != null) {
				e.setCancelled(true);
			}
		}
		
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getDamager() instanceof Player) {
			Player p = (Player) e.getDamager();
			if(plugin.getOneVsOnePlayer(p).getSpecator() != null) {
				e.setCancelled(true);
			}
		}
		
	}
	
	@EventHandler
	public void onTarget(EntityTargetEvent e) {
		if(e.getTarget() instanceof Player) {
			Player p = (Player) e.getTarget();
			if(plugin.getOneVsOnePlayer(p).getSpecator() != null) {
				e.setCancelled(true);
			}
		}
		
	}
	
	@EventHandler
	public void leave(PlayerQuitEvent e) {
	 if(plugin.getOneVsOnePlayer(e.getPlayer()).getSpecator() != null) {
	  e.getPlayer().spigot().setCollidesWithEntities(true);
	  for(Player players : Bukkit.getOnlinePlayers()) {
		  players.showPlayer(e.getPlayer());
	  }
	  plugin.getOneVsOnePlayer(e.getPlayer()).setSpecator(null);
	  e.getPlayer().setAllowFlight(false);
	  e.getPlayer().setFlying(false);
	 }
	}
	
	@EventHandler
	public void food(FoodLevelChangeEvent e) {
	 if(e.getEntity()instanceof Player) {
	  Player p = (Player) e.getEntity();
	  if(plugin.getOneVsOnePlayer(p).getSpecator() != null) {
	   p.setFoodLevel(20);
	   e.setCancelled(true);
	   p.setHealth(20);
	   p.setMaxHealth(20);
	  }
	 }
	}
	
	@EventHandler
	public void drop(PlayerDropItemEvent e) {
	  Player p = (Player) e.getPlayer();
	  if(plugin.getOneVsOnePlayer(p).getSpecator() != null) {
	   e.setCancelled(true);
	  }
	 
	}
	
	@EventHandler
	public void onC(InventoryClickEvent e) {

		if(plugin.getOneVsOnePlayer((Player)e.getWhoClicked()).getSpecator() != null) {
			  e.setCancelled(true);
			  Player p = (Player) e.getWhoClicked();
			  p.updateInventory();
			 
		  }
	}
	
}
