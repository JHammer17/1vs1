package de.OnevsOne.Commands.VariableCommands.Tournament;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.OnevsOne.main;
import de.OnevsOne.Commands.VariableCommands.Manager.CommandTrigger1vs1;
import de.OnevsOne.Methods.Tournament.TournamentManager;
import de.OnevsOne.States.PlayerState;

public class TLeave implements Listener {

	private main plugin;

	public TLeave(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onCommandExec(CommandTrigger1vs1 e) {
		if(e.getCMD().equalsIgnoreCase("tleave") || e.getCMD().equalsIgnoreCase("tl")) {
			if(!plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) return;
			e.setCancelled(true);
			Player p = (Player) e.getPlayer();
			if(p.hasPermission("1vs1.command.tLeave") || p.hasPermission("1vs1.User") || p.hasPermission("1vs1.*")) {
			
			if(plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId()) && plugin.getOneVsOnePlayer(e.getPlayer()).getPlayertournament() != null) {
				TournamentManager mgr = plugin.tournaments.get(plugin.getOneVsOnePlayer(e.getPlayer()).getPlayertournament());
				if(mgr != null) {
					if(plugin.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.InArena) {
						 p.sendMessage(plugin.tournamentPrefix + "§cDu bist momentan in einen Kampf!");//TODO Language.yml
						 return;
					 }
					mgr.leaveTournament(p);
				} else {
					p.sendMessage(plugin.tournamentPrefix + "§cTurnier nicht gefunden!");//TODO Language.yml
				}
			} else {
				p.sendMessage(plugin.noPerms);
			}
			
			} else {
				p.sendMessage(plugin.tournamentPrefix + "§cDu bist in keinen Turnier");//TODO Language.yml
			}
			
		}
	}

}
