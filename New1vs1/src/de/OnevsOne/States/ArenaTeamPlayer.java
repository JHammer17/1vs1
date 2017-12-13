package de.OnevsOne.States;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class ArenaTeamPlayer {

	private Player player;
	private ArrayList<Player> mates = new ArrayList<>();
	
	public ArenaTeamPlayer(Player p, ArrayList<Player> teamMates) {
		this.player = p;
		this.mates = teamMates;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public ArrayList<Player> getTeamMates() {
		return this.mates;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Player> getAll() {
		ArrayList<Player> x = (ArrayList<Player>) mates.clone();
		
		x.add(player);
		return x;
	}
	
	public String getTeamName(boolean upperCase) {
		if(getAll().size() > 2) {
			if(upperCase) {
				return "Das Team von " + player.getDisplayName();
			}
			return "das Team von " + player.getDisplayName();
		}
		StringBuilder builder = new StringBuilder();
		
		if(getAll().size() == 1) {
			builder.append(getAll().get(0).getDisplayName());
		} else if(getAll().size() <= 0) {
			builder.append("-");
		} else {
			builder.append(getAll().get(0).getDisplayName()).append(" & ").append(getAll().get(1).getDisplayName());
		}
		
		return builder.toString();
		
	}
	
	public String getTeamNameDiff() {
		if(getAll().size() > 2) {
			return "Team von " + player.getDisplayName();
		}
		StringBuilder builder = new StringBuilder();
		if(getAll().size() == 1) {
			builder.append(getAll().get(0).getDisplayName());
		} else if(getAll().size() <= 0) {
			builder.append("-");
		} else {
			builder.append(getAll().get(0).getDisplayName()).append(" & ").append(getAll().get(1).getDisplayName());
		}
		return builder.toString();
		
	}
	
	public boolean big() {
		return getAll().size() > 2 ? true : false;
	}
}
