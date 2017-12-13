package de.OnevsOne.Listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;



import de.OnevsOne.main;

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.States.PlayerState;

/**
 * Der Code ist von JHammer
 *
 * 06.05.2016 um 09:44:08 Uhr
 */
public class Region_Edit implements Listener {

	private main plugin;
	
	
	public Region_Edit(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onSetPoint(PlayerInteractEvent e) {
		/*Position wird gesetzt*/
		Player p = e.getPlayer();
		if(p.hasPermission("1vs1.RegionWand") && plugin.getOneVsOnePlayer(p).getpState() == PlayerState.Edit) {
			if(e.getPlayer().getItemInHand().getType() == Material.STONE_AXE) {
	            if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
	            	/*Position 1 setzten*/
	            	
	            	plugin.getOneVsOnePlayer(p).setPos1(e.getClickedBlock().getLocation());
					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("pos1Setted"), p.getDisplayName()));
	            	
					e.setCancelled(true);
				}
	            if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
	            	/*Position 2 setzten*/
	            	plugin.getOneVsOnePlayer(p).setPos2(e.getClickedBlock().getLocation());
					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("pos2Setted"), p.getDisplayName()));
					
					e.setCancelled(true);
				}
			}	
		}
		if(p.hasPermission("1vs1.RegionWand") && plugin.getOneVsOnePlayer(p).getpState() == PlayerState.Edit) {
			/*Position 3 setzten*/
			if(e.getPlayer().getItemInHand().getType() == Material.DIAMOND) {
	            if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
	            	
	            	plugin.getOneVsOnePlayer(p).setPos3(e.getClickedBlock().getLocation());
					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("pos3Setted"), p.getDisplayName()));
	            	
					e.setCancelled(true);
				}
			}	
		}
	}

}
