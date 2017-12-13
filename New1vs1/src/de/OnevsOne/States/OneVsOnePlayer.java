/**
 * 
 */
package de.OnevsOne.States;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Der Code ist von JHammer
 *
 * 24.11.2017 um 20:40:20 Uhr
 * 
 */
public class OneVsOnePlayer {

	boolean in1vs1 = true;
	boolean editMode = false;
	boolean inQueue = false;
	boolean wasInQueue = false;
	boolean doubleJumpUsed = false;

	Location pos1;
	Location pos2;
	Location pos3;

	String arena;

	Player enemy;

	int position;

	PlayerState pState = PlayerState.Unknown;

	ArrayList<Player> challangedBy = new ArrayList<>();
	ArrayList<Player> challanged = new ArrayList<>();

	String arenaView;
	String specator;
	String preferencesInv;
	ItemStack[] playerInv;
	ItemStack[] playerArmor;

	int playerLvl;
	float playerXP;

	UUID playertournament;

	Scoreboard oldBoard;

	ArenaTeamPlayer team;

	ArrayList<Player> teamInvitedBy = new ArrayList<>();
	ArrayList<Player> teamInvited = new ArrayList<>();

	TeamPlayer playerTeam;

	String kitLoaded;

	HashMap<String, Object> dataBaseData = new HashMap<>();
	HashMap<String, Object> dataBaseDataName = new HashMap<>();

	Player player;

	public OneVsOnePlayer(Player p) {
		this.player = p;
	}

	public void init() {
		in1vs1 = true;
		pState = PlayerState.InLobby;
	}
	
	
	public boolean isIn1vs1() {
		return in1vs1;
	}

	public void setIn1vs1(boolean in1vs1) {
		this.in1vs1 = in1vs1;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public boolean isInQueue() {
		return inQueue;
	}

	public void setInQueue(boolean inQueue) {
		this.inQueue = inQueue;
	}

	public boolean isWasInQueue() {
		return wasInQueue;
	}

	public void setWasInQueue(boolean wasInQueue) {
		this.wasInQueue = wasInQueue;
	}

	public boolean isDoubleJumpUsed() {
		return doubleJumpUsed;
	}

	public void setDoubleJumpUsed(boolean doubleJumpUsed) {
		this.doubleJumpUsed = doubleJumpUsed;
	}

	public Location getPos1() {
		return pos1;
	}

	public void setPos1(Location pos1) {
		this.pos1 = pos1;
	}

	public Location getPos2() {
		return pos2;
	}

	public void setPos2(Location pos2) {
		this.pos2 = pos2;
	}

	public Location getPos3() {
		return pos3;
	}

	public void setPos3(Location pos3) {
		this.pos3 = pos3;
	}

	public String getArena() {
		return arena;
	}

	public void setArena(String arena) {
		this.arena = arena;
	}

	public Player getEnemy() {
		return enemy;
	}

	public void setEnemy(Player enemy) {
		this.enemy = enemy;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public PlayerState getpState() {
		return pState;
	}

	public void setpState(PlayerState pState) {
		this.pState = pState;
	}

	public ArrayList<Player> getChallangedBy() {
		return challangedBy;
	}

	public void setChallangedBy(ArrayList<Player> challangedBy) {
		this.challangedBy = challangedBy;
	}

	public ArrayList<Player> getChallanged() {
		return challanged;
	}

	public void setChallanged(ArrayList<Player> challanged) {
		this.challanged = challanged;
	}

	public String getArenaView() {
		return arenaView;
	}

	public void setArenaView(String arenaView) {
		this.arenaView = arenaView;
	}

	public String getSpecator() {
		return specator;
	}

	public void setSpecator(String specator) {
		this.specator = specator;
	}

	public String getPreferencesInv() {
		return preferencesInv;
	}

	public void setPreferencesInv(String preferencesInv) {
		this.preferencesInv = preferencesInv;
	}

	public ItemStack[] getPlayerInv() {
		return playerInv;
	}

	public void setPlayerInv(ItemStack[] playerInv) {
		this.playerInv = playerInv;
	}

	public ItemStack[] getPlayerArmor() {
		return playerArmor;
	}

	public void setPlayerArmor(ItemStack[] playerArmor) {
		this.playerArmor = playerArmor;
	}

	public int getPlayerLvl() {
		return playerLvl;
	}

	public void setPlayerLvl(int playerLvl) {
		this.playerLvl = playerLvl;
	}

	public float getPlayerXP() {
		return playerXP;
	}

	public void setPlayerXP(float playerXP) {
		this.playerXP = playerXP;
	}

	public UUID getPlayertournament() {
		return playertournament;
	}

	public void setPlayertournament(UUID playertournament) {
		this.playertournament = playertournament;
	}

	public Scoreboard getOldBoard() {
		return oldBoard;
	}

	public void setOldBoard(Scoreboard oldBoard) {
		this.oldBoard = oldBoard;
	}

	public ArenaTeamPlayer getTeam() {
		return team;
	}

	public void setTeam(ArenaTeamPlayer team) {
		this.team = team;
	}

	public ArrayList<Player> getTeamInvitedBy() {
		return teamInvitedBy;
	}

	public void setTeamInvitedBy(ArrayList<Player> teamInvitedBy) {
		this.teamInvitedBy = teamInvitedBy;
	}

	public ArrayList<Player> getTeamInvited() {
		return teamInvited;
	}

	public void setTeamInvited(ArrayList<Player> teamInvited) {
		this.teamInvited = teamInvited;
	}

	public TeamPlayer getPlayerTeam() {
		return playerTeam;
	}

	public void setPlayerTeam(TeamPlayer playerTeam) {
		this.playerTeam = playerTeam;
	}

	public String getKitLoaded() {
		return kitLoaded;
	}

	public void setKitLoaded(String kitLoaded) {
		this.kitLoaded = kitLoaded;
	}

	public HashMap<String, Object> getDataBaseData() {
		return dataBaseData;
	}

	public void setDataBaseData(HashMap<String, Object> dataBaseData) {
		this.dataBaseData = dataBaseData;
	}

	public HashMap<String, Object> getDataBaseDataName() {
		return dataBaseDataName;
	}

	public void setDataBaseDataName(HashMap<String, Object> dataBaseDataName) {
		this.dataBaseDataName = dataBaseDataName;
	}

	public Player getPlayer() {
		return player;
	}


}
