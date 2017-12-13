package de.OnevsOne.Commands.VariableCommands.Tournament;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.OnevsOne.main;
import de.OnevsOne.Commands.VariableCommands.Manager.CommandTrigger1vs1;
import de.OnevsOne.Methods.Tournament.TournamentManager;
import de.OnevsOne.States.TournamentState;

public class Start implements Listener {

	private main plugin;

	public Start(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onCommandExec(CommandTrigger1vs1 e) {
		if(e.getCMD().equalsIgnoreCase("start")) {
			e.setCancelled(true);
			Player p = (Player) e.getPlayer();
			
			if(plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId()) && plugin.getOneVsOnePlayer(e.getPlayer()).getPlayertournament() != null) {
				if(p.hasPermission("1vs1.command.start") || p.hasPermission("1vs1.Premium") || p.hasPermission("1vs1.*")) {
				
				TournamentManager mgr = plugin.tournaments.get(plugin.getOneVsOnePlayer(e.getPlayer()).getPlayertournament());
				if(mgr.getOwnerUUID().equals(p.getUniqueId())) {
					
					if(mgr.isStarted()) {
						p.sendMessage(plugin.tournamentPrefix + "§cDas Turnier ist bereits gestartet!");//TODO Language.yml
						return;
					}
					
					if(e.getArgs().length == 1) {
						if(mgr.isOpened()) mgr.openTournament(p);	
							int secs = 0;
							
							try {
								secs = Integer.parseInt(e.getArgs()[0]);
							} catch (NumberFormatException ex) {
								p.sendMessage("§cDu musst eine Zahl angeben!");//TODO Language.yml
								return;
							}
							
							if(secs < 0) {
								p.sendMessage("§cDu musst eine positive Zahl angeben!");//TODO Language.yml
								return;
							}
							
							mgr.setStartCounter(secs);
							mgr.setState(TournamentState.STARTING);
							
							p.sendMessage(plugin.tournamentPrefix + "§aTurnier wird gestartet!");//TODO Language.yml
						
					} else if(e.getArgs().length == 0) {
						if(mgr.isOpened()) mgr.openTournament(p);	
							mgr.setStartCounter(mgr.getStartTimeMins()*60+mgr.getStartTimeSecs());
							mgr.setState(TournamentState.STARTING);
							
							p.sendMessage(plugin.tournamentPrefix + "§aTurnier wird gestartet!");//TODO Language.yml
						
						
						
					} else {
						p.sendMessage("§cFalsche Verwendung: /start [Sekunden]");//TODO Language.yml
					}
					
					
					
					
					
				} else {
					p.sendMessage(plugin.tournamentPrefix + "§cDu bist nicht der Leiter dieses Turniers!");
				}
			  } else {
				  p.sendMessage(plugin.noPerms);
			  }
			} else {
				p.sendMessage(plugin.tournamentPrefix + "§cDu bist in keinen Turnier");
			}
			
		}
	}
}
