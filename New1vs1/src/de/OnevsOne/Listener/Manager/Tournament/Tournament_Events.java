package de.OnevsOne.Listener.Manager.Tournament;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.OnevsOne.main;
import de.OnevsOne.Methods.Tournament.FightEndManager;
import de.OnevsOne.Methods.Tournament.TournamentManager;

public class Tournament_Events implements Listener {

	private main plugin;

	public Tournament_Events(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onQuitT(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if(plugin.isInOneVsOnePlayers(p.getUniqueId())) {
			if(plugin.getOneVsOnePlayer(p).getPlayertournament() != null) {
				
				TournamentManager mgr = plugin.tournaments.get(plugin.getOneVsOnePlayer(p).getPlayertournament());
				if(mgr != null) {
					mgr.setCompleteOut(p.getUniqueId(), true);
					mgr.setOut(p.getUniqueId(), true);
					new FightEndManager(plugin).tournamentEnded(plugin.tournaments.get(plugin.getOneVsOnePlayer(p).getPlayertournament()));
					plugin.tournaments.remove(p.getUniqueId());
				}
				
			}
			
		}
		
	}

	
	
	
}
