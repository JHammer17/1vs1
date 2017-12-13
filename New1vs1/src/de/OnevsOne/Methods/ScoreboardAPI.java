/**
 * 
 */
package de.OnevsOne.Methods;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import de.OnevsOne.main;

/**
 * Der Code ist von JHammer
 *
 * 01.10.2017 um 20:11:27 Uhr
 * 
 */
public class ScoreboardAPI {

	/*List for Boards*/
	private HashMap<UUID, Scoreboard> sboard = new HashMap<>();
	
	/**
	 * 
	 * @param player Player for which the Scoreboard should be updated
	 * @param objName Object Name
	 * 
	 * Updates the Scoreboard data or creates the Scoreboard for a Player (You don't need it)
	 * 
	 */
	public void updateBoard(final Player player, final String objName, main ins) {
		if(!sboard.containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().runTask(ins, new Runnable() {
				
				@Override
				public void run() {
					createBoard(player, objName);
					
				}
			});
			
		}
	}
	
	/**
	 * 
	 * @param player Player
	 * 
	 * Removes the Scoreboard completely from the Player
	 */
	public void removeBoard(Player player) {
		player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		while(sboard.containsKey(player.getUniqueId())) sboard.remove(player.getUniqueId());
	}

	/**
	 * 
	 * @param player Player who should be checked
	 * @return if the player has a scoreboard
	 * 
	 * Check if the Player has a Scoreboard by the API
	 */
	public boolean hasBoard(Player player) {
		return sboard.containsKey(player.getUniqueId());
	}
	
	/**
	 * 
	 * @param player Player
	 * @param objName Object Name
	 * @param Name The Name for the Title
	 * 
	 * Sets the Title of the Scoreboard
	 * 
	 */
	public void setDisplayName(Player player, String objName, String Name, main ins) {
		if(!hasBoard(player)) createBoard(player, objName);
		
		
		if(Name.length() > 32) Name = Name.substring(0,32);
		
		Scoreboard board = sboard.get(player.getUniqueId());
		Objective obj = board.getObjective(objName);
		
		obj.setDisplayName(Name);
		
		updateBoard(player, objName, ins);
	}
	
	
	/**
	 * 
	 * @param player Playerscoreboard
	 * @param objName Object Name
	 * @param main Line Name (don't duplicate)
	 * @param first First 16 Chars
	 * @param middle Middle 16 Chars
	 * @param last Last 16 Chars
	 * @param score Score for the Line
	 * 
	 * Adds a Line to the Scoreboard (can't duplicate)
	 */ 
	public void addLine(Player player, String objName, String name, String first, String middle, String last, int score, main ins) {
		if(!hasBoard(player)) createBoard(player, objName);
		Scoreboard board = sboard.get(player.getUniqueId());
		Objective obj = board.getObjective(objName);
		
		if(name.length() > 16) name = name.substring(0, 16);
		if(first.length() > 16) first = first.substring(0, 16);
		if(middle.length() > 16) middle = middle.substring(0, 16);
		if(last.length() > 16) last = last.substring(0, 16);
		
		if(board.getTeam(name) == null) {
			
			Team t = board.registerNewTeam(name);
			t.addEntry(middle);
			t.setPrefix(first);
			t.setSuffix(last);
			
			obj.getScore(middle).setScore(score);
			updateBoard(player, objName, ins);
		}
	}
	
	/**
	 * 
	 * @param player Player
	 * @param objName Object Name
	 * @param name Line Name
	 * @param first First 16 Chars
	 * @param middle Middle 16 Chars
	 * @param last Last 16 Chars
	 * @param score Score for the Line
	 * 
	 * Updates a Line of the Scoreboard
	 */
	public void updateLine(Player player, String objName, String name, String first, String middle, String last, int score, main ins) {
		Scoreboard board = sboard.get(player.getUniqueId());
		Objective obj = board.getObjective(objName);
		
		if(name.length() > 16) name = name.substring(0, 16);
		if(first.length() > 16) first = first.substring(0, 16);
		if(middle.length() > 16) middle = middle.substring(0, 16);
		if(last.length() > 16) last = last.substring(0, 16);
		
		if(board.getTeam(name) != null) {
			
			boolean cEn = false;
			for(String en : board.getTeam(name).getEntries()) {
				if(en.equalsIgnoreCase(middle)) {
					cEn = true;
					
					break;
				}
				board.getTeam(name).removeEntry(en);
				board.resetScores(en);
			}
			
			Team t = board.getTeam(name);
			
			if(!cEn) {
				t.addEntry(middle);
			}
			t.setPrefix(first);
			t.setSuffix(last);
			
			obj.getScore(middle).setScore(score);
			updateBoard(player, objName, ins);
		}
	}
	
	/**
	 * 
	 * @param player Player
	 * @param objName Object Name
	 * @param name Name of the Line
	 * 
	 * Removes Line from the Scoreboard
	 */
	public void removeLine(Player player, String objName, String name, main ins) {
		Scoreboard board = sboard.get(player.getUniqueId());
		
		
		if(name.length() > 16) name = name.substring(0, 16);
		
		if(board.getTeam(name) != null) {
			
			
			for(String en : board.getTeam(name).getEntries()) {
				board.getTeam(name).removeEntry(en);
				board.resetScores(en);
			}
			
		}
		
		if(board.getTeam(name) != null) board.getTeam(name).unregister();
		updateBoard(player, objName, ins);
	}
	
	/**
	 * 
	 * @param player Player
	 * @param objName Object Name
	 * @param name Name of the Line
	 * @return The Score of a Line
	 * 
	 * Gets the Score of a Line
	 */
	public int getScore(Player player, String objName, String name) {
		Scoreboard board = sboard.get(player.getUniqueId());
		Objective obj = board.getObjective(objName);
		
		if(name.length() > 16) name = name.substring(0, 16);
		
		if(board.getTeam(name) != null) 
			return obj.getScore(name).getScore();
		
		return 0;
	}
	
	/**
	 * 
	 * @param player Player
	 * @param objName Object Name
	 * @param name Name of the Line
	 * @return Returns the first part of the Line (max. 16 Chars)
	 * 
	 * Gets the first part of a Line (max. 16 Chars)
	 */
	public String getFirst(Player player, String objName, String name) {
		Scoreboard board = sboard.get(player.getUniqueId());
		
		if(name.length() > 16) name = name.substring(0, 16);
		
		if(board.getTeam(name) != null) 
			return board.getTeam(name).getPrefix();
		
		return "";
	}
	
	/**
	 * 
	 * @param player Player
	 * @param objName Object Name
	 * @param name Name of the Line
	 * @return Returns the first last of the Line (max. 16 Chars)
	 * 
	 * Gets the last part of a Line (max. 16 Chars)
	 */
	public String getLast(Player player, String objName, String name) {
		Scoreboard board = sboard.get(player.getUniqueId());
		
		if(name.length() > 16) name = name.substring(0, 16);
		
		if(board.getTeam(name) != null) 
			return board.getTeam(name).getSuffix();
		
		return "";
	}
	
	/**
	 * 
	 * @param player Player
	 * @param objName Object Name
	 * 
	 * Creates a Scoreboard (You don't need)
	 */
	private void createBoard(Player player, String objName) {
		Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
		Objective obj = board.registerNewObjective(objName, "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		player.setScoreboard(board);
		
		while(sboard.containsKey(player.getUniqueId())) sboard.remove(player.getUniqueId());
		sboard.put(player.getUniqueId(), board);
		
	}
	
	public boolean isLineExists(Player player, String objName, String name) {
		Scoreboard board = sboard.get(player.getUniqueId());
		
		if(name.length() > 16) name = name.substring(0, 16);
		
		return (board.getTeam(name) != null);
	}
	
	public void removedTablistTeams(Player player, String objName, main ins) {
		if(!hasBoard(player)) createBoard(player, objName);
		Scoreboard board = sboard.get(player.getUniqueId());
		
		for(Team teams : board.getTeams()) {
			if(teams.hasEntry(player.getName())) {
				teams.removeEntry(player.getName());
				updateBoard(player, objName, ins);
			}
		}
		
		
	}
	
	/**
	 * 
	 * @param player Player
	 * @param objName Object Name
	 * @param name Line Name
	 * @param first First 16 Chars
	 * @param middle Middle 16 Chars
	 * @param last Last 16 Chars
	 * @param score Score for the Line
	 * 
	 * Updates a Line of the Scoreboard
	 */
	public void updateTablistTeam(Player player, String objName, String name, String first, String middle, String last, main ins) {
		Scoreboard board = sboard.get(player.getUniqueId());
		
		if(board == null || name == null || objName == null) return;
		
		if(name.length() > 16) name = name.substring(0, 16);
		if(first.length() > 16) first = first.substring(0, 16);
		if(middle.length() > 16) middle = middle.substring(0, 16);
		if(last.length() > 16) last = last.substring(0, 16);
		
		if(board.getTeam(name) != null) {
			
			
			
			Team t = board.getTeam(name);
			
			if(!t.hasEntry(middle)) t.addEntry(middle);
			t.setPrefix(first);
			t.setSuffix(last);
			
			updateBoard(player, objName, ins);
		}
	}
	
	public void addTablistTeams(Player player, String objName, String name, String first, String middle, String last, main ins) {
		if(!hasBoard(player)) createBoard(player, objName);
		Scoreboard board = sboard.get(player.getUniqueId());
		
		if(board == null || name == null || objName == null) return;
		
		
		if(name.length() > 16) name = name.substring(0, 16);
		if(first.length() > 16) first = first.substring(0, 16);
		if(middle.length() > 16) middle = middle.substring(0, 16);
		if(last.length() > 16) last = last.substring(0, 16);
		
		if(board.getTeam(name) == null) {
			
			Team t = board.registerNewTeam(name);
			t.addEntry(middle);
			t.setPrefix(first);
			t.setSuffix(last);
			
			updateBoard(player, objName, ins);
		} else {
			
			updateTablistTeam(player, objName, name, first, middle, last, ins);
		}
	}
	
}
