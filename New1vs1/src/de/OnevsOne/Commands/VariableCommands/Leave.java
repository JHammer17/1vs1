package de.OnevsOne.Commands.VariableCommands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.OnevsOne.main;
import de.OnevsOne.Commands.MainCommand;
import de.OnevsOne.Commands.VariableCommands.Manager.CommandTrigger1vs1;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.States.PlayerState;

public class Leave implements Listener {

	private main plugin;

	public Leave(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	
	@EventHandler
	public void onCommandExec(CommandTrigger1vs1 e) {
		if(e.getCMD().equalsIgnoreCase("l") || e.getCMD().equalsIgnoreCase("leave") || e.getCMD().equalsIgnoreCase("hub")) {
		 if(plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
			 e.setCancelled(true);
				Player p = (Player) e.getPlayer();
				
				if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena) {
        			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("notInLobby"), p.getDisplayName(), null, null, null));
        			return;
        		}
				
				if(plugin.BungeeMode) {
					Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
					  ByteArrayOutputStream b = new ByteArrayOutputStream();
					  DataOutputStream out = new DataOutputStream(b);
					  
					  try {
					   out.writeUTF("Connect");
					   out.writeUTF(plugin.fallBackServer);
					  } catch (IOException e2) {
					   e2.printStackTrace();
					   e.getPlayer().sendMessage("§cError...");
					  }
					  
					  e.getPlayer().sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
				} else {
					MainCommand.toggle1vs1(p, false, false);
				}
		 }
		}
				
	}
}
