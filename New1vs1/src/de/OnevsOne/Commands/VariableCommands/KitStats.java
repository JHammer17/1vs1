/**
 * 
 */
package de.OnevsOne.Commands.VariableCommands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.OnevsOne.main;
import de.OnevsOne.Commands.VariableCommands.Manager.CommandTrigger1vs1;
import de.OnevsOne.MessageManager.MessageReplacer;

/**
 * Der Code ist von JHammer
 *
 * 03.10.2017 um 09:54:54 Uhr
 * 
 */
public class KitStats implements Listener {

	private main plugin;

	/**
	 * @param main
	 */
	public KitStats(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onCommandExec(CommandTrigger1vs1 e) {		
		if(e.getCMD().equalsIgnoreCase("kitstats") && plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
			
		
			final Player p = (Player) e.getPlayer();
			final String[] args = e.getArgs();
			
			if(!p.hasPermission("1vs1.command.kitstats") && !p.hasPermission("1vs1.*") && !p.hasPermission("1vs1.User")) {
	    		plugin.sendNoPermsMessage(p);
	    		return;
	    	}
			
			if(args.length == 1) {
				Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
					
					@Override
					public void run() {
						
						String kit = args[0];
						int sub = 1;
						
						if(kit.contains(":")) {
							
							String[] kitS = kit.split(":");
							kit = kitS[0];
							if(kitS.length >= 2) {
								try {
									sub = Integer.parseInt(kitS[1]);
								} catch (Exception e) {
									p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("numberAsSubID"), p.getName()));
									return;
								}
							}
							
							
						}
						
						
						
						if(plugin.getDBMgr().isCustomKitExists(kit) != 0) {
							//╔═╠●╚
							
							//╔════════════════════
							//╠
							//╠ ● §f§l0§r§7x §6All-Time
							//╠ ● §fPlatz §6X §fim Ranking
							//╠
							//╠════════════════════
							//╠
							//╠ ● §f§l0§r§7x §6Letzte 30 Tage
							//╠ ● §fPlatz §6X §fim Ranking
							//╠
							//╠════════════════════
							//╠
							//╠ ● §f§l0§r§7x §6Letzte 24h
							//╠ ● §fPlatz §6X §fim Ranking
							//╠
							//╚════════════════════
							
							p.sendMessage("§6╔══════════ §fKit: §6" + kit + ":" + sub + " §6══════════");
							//p.sendMessage("§6╠");
							p.sendMessage("§6╠ ● §f§l" + plugin.getDBMgr().getStatsKit(kit, sub, 0) + "§r§7x §6All-Time");
							p.sendMessage("§6╠ ● §fPlatz §6" + plugin.getDBMgr().getPositionKit(kit, 0) + " §fim Ranking §6(" + kit + ":1)");
							//p.sendMessage("§6╠");
							p.sendMessage("§6╠════════════════════");
							//p.sendMessage("§6╠");
							p.sendMessage("§6╠ ● §f§l" + plugin.getDBMgr().getStatsKit(kit, sub, 1) + "§r§7x §6in den letzten 30 Tagen");
							p.sendMessage("§6╠ ● §fPlatz §6" + plugin.getDBMgr().getPositionKit(kit, 1) + " §fim Ranking §6(" + kit + ":1)");
							//p.sendMessage("§6╠");
							p.sendMessage("§6╠════════════════════");
							//p.sendMessage("§6╠");
							p.sendMessage("§6╠ ● §f§l" + plugin.getDBMgr().getStatsKit(kit, sub, 2) + "§r§7x §6in den letzten 24h");
							p.sendMessage("§6╠ ● §fPlatz §6" + plugin.getDBMgr().getPositionKit(kit, 2) + " §fim Ranking §6(" + kit + ":1)");
							//p.sendMessage("§6╠");
							p.sendMessage("§6╚════════════════════");
							
						} else {
							p.sendMessage(plugin.prefix + "§cKit nicht gefunden!");
						}
						
					}
				});
			} else {
				p.sendMessage(plugin.prefix + "§cNutze: /kitstats [Kit]:{SubID}");
			}
			
			
			
			
		}
	}
	
	
}
