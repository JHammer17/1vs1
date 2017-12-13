package de.OnevsOne.Methods.Tournament;

import java.util.UUID;

import de.OnevsOne.main;
import de.OnevsOne.States.TournamentState;

public class TournamentFight {

	private UUID pos1;
	private UUID pos2;
	private String tournament;
	private int fightNumber;
	private int pointsP1;
	private int pointsP2;
	private main plugin;

	public TournamentFight(UUID pos1, UUID pos2, String tournament, main plugin) {
		this.pos1 = pos1;
		this.pos2 = pos2;
		this.tournament = tournament;
		this.plugin = plugin;
	}
	
	public UUID getPos1() {
		return pos1;
	}
	
	public UUID getPos2() {
		return pos2;
	}
	
	public String getTournament() {
		return tournament;
	}
	
	public int getFightNumber() {
		return fightNumber;
	}

	public void setFightNumber(int fight) {
		fightNumber = fight;
	}
	
	public void setPointsP1(int p1) {
		pointsP1 = p1;
	}
	
	public void setPointsP2(int p2) {
		pointsP2 = p2;
	}
	
	public int getPointsP1() {
		return pointsP1;
	}
	
	public int getPointsP2() {
		return pointsP2;
	}

	@SuppressWarnings("unlikely-arg-type")
	public boolean ended() {
		int max = 1;
		int fights = getPointsP1()+getPointsP2();
		
		
		TournamentManager mgr = plugin.tournaments.get(getTournament());
		
		if(mgr.getState() == TournamentState.QUALLI) {
			max = mgr.getFightsQu();
			if(fights >= max) return true;
			
			
		} else if(mgr.getState() == TournamentState.KO) {
			max = mgr.getFightsKo();
			if(fights >= max) return true;
			
		}
		
		return false;
	}
	
	
}
