package de.OnevsOne.Arena.SpectatorManager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.OnevsOne.main;

import de.OnevsOne.States.PlayerState;
import net.md_5.bungee.api.ChatColor;

/**
 * Der Code ist von JHammer
 *
 * 23.05.2016 um 16:56:40 Uhr
 */
public class SpectateArenaItemManager implements Listener {

	private main plugin;
	
	public SpectateArenaItemManager(main plugin) {
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onUse(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
		 if(e.getItem() != null && e.getItem().getType() != Material.AIR) {
		  if(e.getItem().getTypeId() == plugin.SpectatorItemID && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() && e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(plugin.msgs.getMsg("specateItemLobbyName"))) {
		   if(plugin.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.InLobby) {
			   if(plugin.getOneVsOnePlayer(e.getPlayer()).getPlayertournament() != null) {
				e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentinTournament")));
				return;
			   }
			   
			   e.setCancelled(true);
			   ArenaMenu.openMenu(e.getPlayer());
		   }
		  }
		 }
		}
	}
	
	@EventHandler
	public void specArena(InventoryClickEvent e) {
		
		if(plugin.getOneVsOnePlayer((Player)e.getWhoClicked()).getSpecator() != null) 
			 e.setCancelled(true);
		  
		if(e.getInventory() != null && e.getCurrentItem() != null) {
		 Player p = (Player) e.getWhoClicked();
		 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.Spec) 
			 e.setCancelled(true);
		 if(e.getInventory().getTitle().equalsIgnoreCase(plugin.msgs.getMsg("spectatorArenaInvTitle")) && plugin.isInOneVsOnePlayers(p.getUniqueId())) {
		  if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby || plugin.getOneVsOnePlayer(p).getpState() == PlayerState.Spec) {
			
			  e.setCancelled(true);
			  if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore() && e.getClickedInventory().getTitle().equalsIgnoreCase(plugin.msgs.getMsg("spectatorArenaInvTitle"))) {
				  SpectateArena.specArena(p, ArenaMenu.ArenaSlots.get(e.getSlot()), false);  
			  }
		  }
		 }
		}
		
	}
	
	
	
}
