package de.OnevsOne.Arena.SpectatorManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import de.OnevsOne.main;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.ScoreBoardManager;
import de.OnevsOne.Methods.getItems;
import de.OnevsOne.States.PlayerState;

/**
 * Der Code ist von JHammer
 *
 * 17.05.2016 um 18:20:03 Uhr
 */
public class SpectateArena{

	private static main plugin;
	
	
	@SuppressWarnings("static-access")
	public SpectateArena(main plugin) {
		this.plugin = plugin;
		
	}

	public static boolean specArena(Player p, String Arena, boolean tournament) {
		if(plugin.isInOneVsOnePlayers(p.getUniqueId())) {
			
		
			 
			 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.Spec || plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby || tournament) {
				 if(Arena == null || Arena.equalsIgnoreCase("null")) {
					 if(tournament)  return true;
				 }
				 if(plugin.getAState().isFree(Arena)) {
					 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaNotIngame"), p.getDisplayName(), null,null, Arena));
					 return false;
				 }
				 if(plugin.getAState().isEnded(Arena)) {
				  if(!tournament) {
					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaNotIngame"), p.getDisplayName(), null,null, Arena));
					return false;
				  }	 
				 }
				 if(plugin.getAState().isDisabled(Arena)) {
					 if(!tournament) {
						 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaNotFound"), p.getDisplayName(), null,null, Arena));
						 return false;
					 }
				 }
				 
			 
			  
			  plugin.getOneVsOnePlayer(p).setSpecator(Arena);
			  plugin.getOneVsOnePlayer(p).setInQueue(false);
			  
			  
			  String pos1 = "-";
			  String pos2 = "-";
			  
			  p.spigot().setCollidesWithEntities(false);
			  
			  if(plugin.ArenaPlayersP1.containsKey(Arena) && plugin.ArenaPlayersP1.get(Arena).size() >= 1 && plugin.ArenaPlayersP2.containsKey(Arena) && plugin.ArenaPlayersP2.get(Arena).size() >=1) {
				  pos1 = plugin.ArenaPlayersP1.get(Arena).get(0).getDisplayName();
				  pos2 = plugin.ArenaPlayersP2.get(Arena).get(0).getDisplayName();
			  }
			  
			  if(plugin.arenaTeams.containsKey(Arena)) {
				  pos1 = plugin.arenaTeams.get(Arena).get(0).getTeamNameDiff();
				  pos2 = plugin.arenaTeams.get(Arena).get(1).getTeamNameDiff();
			  }
			  
			  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("startSpectate"), p.getDisplayName(), pos1, pos2, Arena));
			  
			  
			  
			  plugin.getOneVsOnePlayer(p).setpState(PlayerState.Spec);
			  p.teleport(plugin.getPositions().getArenaPos3(Arena));
			  
			  
			  for(Player players : Bukkit.getOnlinePlayers()) players.showPlayer(p);
			  for(Player players : Bukkit.getOnlinePlayers()) players.hidePlayer(p);
				
			  
			  ScoreBoardManager.updateBoard(p, false);
			  
				  p.getInventory().clear();
				  p.getInventory().setArmorContents(null);
				  p.setAllowFlight(true);
				  p.setFlying(true);
				  getItems.getSpectatorItems(p, true);
				  return true;
			 } else {
				
				 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("notInLobby"), p.getDisplayName(), null, null, Arena));
				 return false;
			 }		  
		 
		} else {
			
			 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("notInLobby"), p.getDisplayName(), null, null, Arena));
			 return false;
		}
	}
	
	
	
	
}
