package de.OnevsOne.States;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class TeamPlayer {

	private Player player;
	private ArrayList<Player> mates = new ArrayList<>();
	
	public TeamPlayer(Player p, ArrayList<Player> teamMates) {
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
	
	public void addPlayer(Player p) {
		this.mates.add(p);
	}
	
	public void removePlayer(Player p) {
		this.mates.remove(p);
	}
	
}
