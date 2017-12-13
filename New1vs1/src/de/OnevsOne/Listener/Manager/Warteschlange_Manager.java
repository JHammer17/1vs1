package de.OnevsOne.Listener.Manager;

import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import de.OnevsOne.main;

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.ScoreBoardManager;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.States.PlayerState;
import net.md_5.bungee.api.ChatColor;

/**
 * Der Code ist von JHammer
 *
 * 07.05.2016 um 11:56:31 Uhr
 */
public class Warteschlange_Manager implements Listener {

	private main plugin;
	
	public Warteschlange_Manager(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoinorLeave(EntityDamageByEntityEvent e) {
	 if(e.getEntity() instanceof Zombie) {
	  if(e.getDamager() instanceof Player) {
		  if(e.getEntity().getCustomName() != null && 
			 e.getEntity().getCustomName().equalsIgnoreCase(plugin.msgs.getMsg("quequeEntityName"))) {
			  e.setCancelled(true);
			  Player p = (Player) e.getDamager();
			  if(plugin.getOneVsOnePlayer(p).getPlayertournament() != null) {
				  p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentinTournament")));
				  return;
			  }
			  /*Check, ob der Spieler in der 1vs1-Lobby ist*/
			  if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby) {
				  /*Check, ob der Spieler das Schwert in der Hand hat*/
				  if(p.getItemInHand().getTypeId() == plugin.ChallangerItemID && 
					 p.getItemInHand().hasItemMeta() && 
					 p.getItemInHand().getItemMeta().hasDisplayName() && 
					 p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(plugin.msgs.getMsg("challangeItemLobbyName"))) {
				   
					  
				   /*Warteschlangen Check*/
				   if(plugin.getOneVsOnePlayer(p).isInQueue()) {
					/*Aus Warteschlange entfernen*/
					plugin.getOneVsOnePlayer(p).setInQueue(false);
					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("toggleQueque1"), p.getDisplayName()));
					SoundManager manager = new SoundManager(JSound.DRUM, p, 10.0F, 1.0F); 
					manager.play();
					ScoreBoardManager.updateBoard(p, true);
				   } else {
					if(plugin.getOneVsOnePlayer(p).getTeam() != null) {
					 if(!plugin.getOneVsOnePlayer(p).getTeam().getPlayer().getUniqueId().equals(p.getUniqueId())) {
						 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamNoLeader")));
						 return;
					 }
					}
					
					/*In die Warteschlange einfügen*/
					plugin.getOneVsOnePlayer(p).setInQueue(true);
					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("toggleQueque2"), p.getDisplayName()));
					SoundManager manager = new SoundManager(JSound.ORB_PLING, p, 10.0F, 1.0F);
					manager.play();
					ScoreBoardManager.updateBoard(p, true);
				   }  
			   }
			  }
		  }
	  }
	 }
	}
	
	
	
	
}
