package de.OnevsOne.Methods.Tournament;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import de.OnevsOne.main;
import de.OnevsOne.Arena.Manager.ArenaJoin;
import de.OnevsOne.Listener.Manager.ChallangeManager;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.Methods.getItems;
import de.OnevsOne.Methods.FightEnder.FightEnd;
import de.OnevsOne.Methods.Queue.QueueManager;
import de.OnevsOne.States.OneVsOnePlayer;
import de.OnevsOne.States.PlayerState;
import de.OnevsOne.States.TournamentState;
import net.md_5.bungee.api.ChatColor;

public class TournamentManager {

	private UUID owner;
	private String name;
	private boolean setDefault = true;
	private main plugin;
	private String kit;
	private String password;

	private int fightsKO = 1;
	private int fightsQu = 3;
	private int roundsQu = 3;
	private int startTimeMins = 3;
	private int startTimeSecs = 0;
	private int maxTimeQuMins = 1;
	private int maxTimeQuSecs = 0;
	private int maxTimeKoMins = 2;
	private int maxTimeKoSecs = 0;
	private int maxPlayers = -1;
	private int round = 0;
	private int startCounter = 0;
	private boolean isStarted = false;
	private boolean isOpened = false;
	private boolean ended = false;
	private TournamentState state = TournamentState.WAITING;

	private ArrayList<UUID> playerList = new ArrayList<>();
	private ArrayList<UUID> ignore = new ArrayList<>();

	private HashMap<UUID, Integer> statsWinsQ = new HashMap<UUID, Integer>();
	private HashMap<UUID, Integer> statsLosesQ = new HashMap<UUID, Integer>();
	private ArrayList<UUID> outT = new ArrayList<UUID>();
	private HashMap<Integer, HashMap<UUID, Integer>> statsWins = new HashMap<Integer, HashMap<UUID, Integer>>();
	private HashMap<Integer, HashMap<UUID, Integer>> statsLoses = new HashMap<Integer, HashMap<UUID, Integer>>();
	private HashMap<Integer, HashMap<String, String>> games = new HashMap<>();
	private HashMap<UUID, String> playerNames = new HashMap<>();
	private HashMap<Integer, HashMap<String, String>> games2 = new HashMap<>();

	private HashMap<UUID, ArrayList<UUID>> encounters = new HashMap<>();

	private ArrayList<TournamentFight> fights = new ArrayList<>();

	private String place1 = "-";
	private String place2 = "-";
	private String place3 = "-";
	private String place3_1 = "-";

	public TournamentManager(UUID owner, String name, main plugin) {
		this.owner = owner;
		this.name = name;
		this.plugin = plugin;
	}

	
	public void setEnded(boolean ended) {
		this.ended = ended;
	}
	
	public boolean isEnded() {
		return this.ended;
	}
	
	public String getKit() {
		return kit;
	}

	public String getName() {
		return name;
	}

	public UUID getOwnerUUID() {
		return owner;
	}

	public void setKit(String kit) {
		this.kit = kit;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@SuppressWarnings("unchecked")
	public void delete() {
		Bukkit.getScheduler().runTask(plugin, new Runnable() {

			@Override
			public void run() {
				if (playerList != null) {
					ArrayList<UUID> playerListC = (ArrayList<UUID>) playerList.clone();
					for (UUID uuid : playerListC) {
						if (isCompleteOut(uuid))
							continue;
						if (Bukkit.getPlayer(uuid) != null) {

							Player player = Bukkit.getPlayer(uuid);

							player.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentKickDeleted")));
							while (playerList.contains(player.getUniqueId()))
								playerList.remove(player.getUniqueId());

							
							plugin.getOneVsOnePlayer(player).setPlayertournament(null);
							
							plugin.getTeleporter().teleportMainSpawn(player);
							FightEnd.resetPlayer(player, true);
							
							getItems.getLobbyItems(player, true);
							player.setAllowFlight(false);
							player.setFlying(false);
							player.spigot().setCollidesWithEntities(true);
							    for(PotionEffect effect : player.getActivePotionEffects()) player.removePotionEffect(effect.getType());
							    getItems.getLobbyItems(player, true);
							    for(Player Online : Bukkit.getOnlinePlayers()) Online.showPlayer(player);
							    
							    plugin.getOneVsOnePlayer(player).setpState(PlayerState.InLobby);
						}
					}

				}
				plugin.tournaments.remove(getOwnerUUID());

			}

		});

	}

	public void setFightsKo(int fights) {
		fightsKO = fights;
	}

	public int getFightsKo() {
		return fightsKO;
	}

	public void setFightsQu(int fights) {
		fightsQu = fights;
	}

	public int getFightsQu() {
		return fightsQu;
	}

	public int getRoundsQu() {
		return roundsQu;
	}

	public void setRoundsQu(int roundsQu) {
		this.roundsQu = roundsQu;
	}

	public int getStartTimeMins() {
		return startTimeMins;
	}

	public void setStartTimeMins(int startTimeMins) {
		this.startTimeMins = startTimeMins;
	}

	public int getStartTimeSecs() {
		return startTimeSecs;
	}

	public void setStartTimeSecs(int startTimeSecs) {
		this.startTimeSecs = startTimeSecs;
	}

	public int getMaxTimeQuMins() {
		return maxTimeQuMins;
	}

	public void setMaxTimeQuMins(int maxTimeQuMins) {
		this.maxTimeQuMins = maxTimeQuMins;
	}

	public int getMaxTimeQuSecs() {
		return maxTimeQuSecs;
	}

	public void setMaxTimeQuSecs(int maxTimeQuSecs) {
		this.maxTimeQuSecs = maxTimeQuSecs;
	}

	public int getMaxTimeKoMins() {
		return maxTimeKoMins;
	}

	public void setMaxTimeKoMins(int maxTimeKoMins) {
		this.maxTimeKoMins = maxTimeKoMins;
	}

	public int getMaxTimeKoSecs() {
		return maxTimeKoSecs;
	}

	public void setMaxTimeKoSecs(int maxTimeKoSecs) {
		this.maxTimeKoSecs = maxTimeKoSecs;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public boolean isStarted() {
		return isStarted;
	}

	public void setStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}

	public boolean isOpened() {
		return isOpened;
	}

	public void setOpened(boolean isOpened) {
		this.isOpened = isOpened;
	}

	public void addPlayer(Player p) {
		for (Player players : getPlayerList2())
		 if(players != null && Bukkit.getPlayer(players.getUniqueId()) != null)
			players.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentPlayerJoined").replaceAll("%Name%", p.getDisplayName())));
		playerList.add(p.getUniqueId());
		plugin.getOneVsOnePlayer(p).setPlayertournament(getOwnerUUID());
		setQPointsLoses(p.getUniqueId(), 0);
		setQPointsWins(p.getUniqueId(), 0);
		playerNames.put(p.getUniqueId(), p.getDisplayName());

	}

	public String getPlayerName(UUID uuid) {
		return playerNames.get(uuid);
	}

	public void removePlayer(Player p) {
		playerList.remove(p.getUniqueId());
		playerNames.remove(p.getUniqueId());
		plugin.getOneVsOnePlayer(p).setPlayertournament(null);
	}

	public void removePlayer(UUID uuid) {
		playerList.remove(uuid);
	}

	public ArrayList<UUID> getPlayerList() {
		return playerList;
	}

	public Player toPlayer(UUID uuid) {
		return Bukkit.getPlayer(uuid);
	}

	public ArrayList<Player> getPlayerList2() {
		ArrayList<Player> players = new ArrayList<>();
		@SuppressWarnings("unchecked")
		ArrayList<UUID> playerIDs = (ArrayList<UUID>) getPlayerList().clone();
		for (UUID uuid : playerIDs)
			if (toPlayer(uuid) != null)
				players.add(toPlayer(uuid));
		return players;
	}

	public int getQPointsWins(UUID uuid) {
		if (statsWinsQ.get(uuid) == null)
			return 0;
		return statsWinsQ.get(uuid);
	}

	public void setQPointsWins(UUID uuid, int points) {
		statsWinsQ.remove(uuid);
		statsWinsQ.put(uuid, points);
	}

	public int getQPointsLoses(UUID uuid) {

		if (statsLosesQ.get(uuid) == null)
			return 0;

		return statsLosesQ.get(uuid);
	}

	public void setQPointsLoses(UUID uuid, int points) {
		statsLosesQ.remove(uuid);
		statsLosesQ.put(uuid, points);
	}

	private <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list)
			result.put(entry.getKey(), entry.getValue());

		return result;
	}

	public void sortMap() {
		HashMap<UUID, Integer> sorted = (HashMap<UUID, Integer>) sortByValue(statsWinsQ);
		for (UUID uuid : ignore)
			sorted.remove(uuid);
		for (UUID uuid : ignore)
			sorted.put(uuid, statsWinsQ.get(uuid));
		statsWinsQ = sorted;
		new Tournament_InvCreator(plugin).reGenerateInv(getOwnerUUID());
	}

	public HashMap<UUID, Integer> getStatsQ() {
		return statsWinsQ;
	}

	public boolean isOut(UUID uuid) {
		return outT.contains(uuid);
	}

	public void setOut(UUID uuid, boolean out) {
		while (outT.contains(uuid))
			outT.remove(uuid);
		if (out)
			outT.add(uuid);
	}

	public ArrayList<UUID> getAllOut() {
		return outT;
	}

	public ArrayList<Player> getAllOut2() {
		ArrayList<Player> players = new ArrayList<>();
		for (UUID uuid : getAllOut())
			players.add(toPlayer(uuid));
		return players;
	}

	public void setState(TournamentState state) {
		this.state = state;
		encounters.clear();
	}

	public TournamentState getState() {
		return this.state;
	}

	public int getStatsWinsRound(int round, UUID uuid) {
		if (statsWins.containsKey(round) && statsWins.get(round).containsKey(uuid)) {
			return statsWins.get(round).get(uuid);
		}
		return 0;
	}

	@SuppressWarnings("unlikely-arg-type")
	public void setStatsWinsRound(int round, UUID uuid, int amount) {
		HashMap<UUID, Integer> data = statsWins.get(round);
		if (data == null)
			data = new HashMap<UUID, Integer>();
		while (data.containsKey(round))
			data.remove(round);
		while (statsWins.containsKey(uuid))
			statsWins.remove(round);

		data.put(uuid, amount);

		statsWins.put(round, data);

	}

	@SuppressWarnings("unlikely-arg-type")
	public void setStatsLosesRound(int round, UUID uuid, int amount) {
		HashMap<UUID, Integer> data = statsLoses.get(round);
		if (data == null)
			data = new HashMap<UUID, Integer>();
		while (data.containsKey(round))
			data.remove(round);
		while (statsLoses.containsKey(uuid))
			statsLoses.remove(round);

		data.put(uuid, amount);

		statsLoses.put(round, data);

	}

	public int getStatsLosesRound(int round, UUID uuid) {
		if (statsLoses.containsKey(round) && statsLoses.get(round).containsKey(uuid)) {
			return statsLoses.get(round).get(uuid);
		}
		return 0;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public boolean getDefault() {
		return setDefault;
	}

	public void setSetDefault(boolean setter) {
		setDefault = setter;
	}

	public void leaveTournament(Player p) {

		setOut(p.getUniqueId(), true);
		setCompleteOut(p.getUniqueId(), true);
		new FightEndManager(plugin).reSetTournament(getOwnerUUID(), this);

		plugin.getTeleporter().teleportMainSpawn(p);
		
		plugin.getOneVsOnePlayer(p).setPlayertournament(null);
		plugin.getOneVsOnePlayer(p).setpState(PlayerState.InLobby);

		FightEnd.resetPlayer(p, true);

		new FightEndManager(plugin).tournamentEnded(this);

		for (Player players : getPlayerList2()) {
			if(!isCompleteOut(players.getUniqueId())) {
				players.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentPlayerLefT").replaceAll("%Name%", p.getDisplayName())));
			}
		}
		getItems.getLobbyItems(p, true);
			
		p.setAllowFlight(false);
		p.setFlying(false);
		p.spigot().setCollidesWithEntities(true);
		    for(PotionEffect effect : p.getActivePotionEffects()) p.removePotionEffect(effect.getType());
		    getItems.getLobbyItems(p, true);
		    for(Player Online : Bukkit.getOnlinePlayers()) Online.showPlayer(p);
		  
	}

	public boolean isInTournament(UUID uuid) {
		return playerList.contains(uuid);
	}

	public void setStartCounter(int counter) {
		startCounter = counter;
	}

	public int getStartCounter() {
		return startCounter;
	}

	@SuppressWarnings("unchecked")
	public void joinAll() {
		try {
			ArrayList<Player> freePlayers = new ArrayList<>();

			for (Player tPlayers : getPlayerList2()) {

				if (tPlayers != null && !isOut(tPlayers.getUniqueId())) {
					if (plugin.getOneVsOnePlayer(tPlayers).getpState() != PlayerState.InArena) {
						freePlayers.add(tPlayers);
					}
				}
				
			}

			

			int freePlayersS = freePlayers.size();
			while (freePlayersS > 1) {
				Random r = new Random();
				
				if(freePlayers.size() <= 0) break;
				int use = r.nextInt(freePlayers.size());

				Player pos1 = freePlayers.get(use);
				freePlayers.remove(use);

				ArrayList<Player> encountered = new ArrayList<>();
				ArrayList<Player> freePlayers2 = (ArrayList<Player>) freePlayers.clone();

				if (encounters.containsKey(pos1.getUniqueId())) {
					for (UUID uuid : encounters.get(pos1.getUniqueId())) {
						encountered.add(toPlayer(uuid));
						freePlayers2.removeAll(encountered);
						if (freePlayers2.size() <= 0) {
							encountered.clear();
							freePlayers2 = (ArrayList<Player>) freePlayers.clone();
							encounters.remove(pos1.getUniqueId());
						}

					}
				}

				use = r.nextInt(freePlayers2.size());

				Player pos2 = freePlayers2.get(use);
				freePlayers.remove(pos2);

				String subId = "d";
				String kit = this.kit;
				if (kit.contains(":") && this.kit.split(":").length >= 2) {
					subId = this.kit.split(":")[1];
					kit = this.kit.split(":")[0];
				} else if (this.kit.split(":").length == 1)
					kit = kit.replaceAll(":", "");

				
				if(plugin.FreeArenas.size() <= 0) {
					
					pos1.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentnoFreeArenaFound")));
					pos2.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentnoFreeArenaFound")));
					new FightEndManager(plugin).kickTournament(pos1, this);
					new FightEndManager(plugin).kickTournament(pos2, this);
					//TODO Auf freie Arena warten!
					continue;
				}
				
				
				String map = QueueManager.getRandomMap(pos1, pos2);

				if (map == null) map = QueueManager.getRandomMap(pos1);

				if(map == null) {
					Random random = new Random();
					if(plugin.FreeArenas.size() > 1) {
						map = plugin.FreeArenas.get(random.nextInt(plugin.FreeArenas.size()));
					}
				}
				
				
				if (plugin.getDBMgr().isCustomKitExists(kit) == 1) {
					
					ArenaJoin.joinArena(pos1, pos2, map, false, kit, true, subId);
					
				} else {
					
					ArenaJoin.joinArena(pos1, pos2, map, false, plugin.getDBMgr().getUUID(kit).toString(), true, subId);
				
				}

				TournamentFight fight = new TournamentFight(pos1.getUniqueId(), pos2.getUniqueId(),
						getOwnerUUID().toString(), plugin);
				fights.add(fight);

				freePlayersS = freePlayers.size();

				HashMap<String, String> fightList = games.get(getRound());
				if (fightList == null)
					fightList = new HashMap<>();
				fightList.put(pos1.getDisplayName(), pos2.getDisplayName());

				while (games.containsKey(getRound()))
					games.remove(getRound());

				games.put(getRound(), fightList);

				fightList = games2.get(getRound());
				if (fightList == null)
					fightList = new HashMap<>();
				fightList.put(pos2.getDisplayName(), pos1.getDisplayName());

				while (games2.containsKey(getRound()))
					games2.remove(getRound());

				games2.put(getRound(), fightList);

				setStatsWinsRound(getRound(), pos1.getUniqueId(), 0);
				setStatsWinsRound(getRound(), pos2.getUniqueId(), 0);

				setStatsLosesRound(getRound(), pos1.getUniqueId(), 0);
				setStatsLosesRound(getRound(), pos2.getUniqueId(), 0);

				ArrayList<UUID> c = encounters.get(pos1.getUniqueId());
				if (c == null)
					c = new ArrayList<>();
				c.add(pos2.getUniqueId());
				encounters.put(pos1.getUniqueId(), c);

				c = encounters.get(pos2.getUniqueId());
				if (c == null)
					c = new ArrayList<>();
				c.add(pos1.getUniqueId());
				encounters.put(pos2.getUniqueId(), c);

			}

			if (freePlayersS == 1) {
				
				if (new FightEndManager(plugin).tournamentEnded(this))
					return;

				HashMap<String, String> fightList = games.get(getRound());
				if (fightList == null)
					fightList = new HashMap<>();
				fightList.put(freePlayers.get(0).getDisplayName(), null);

				while (games.containsKey(getRound()))
					games.remove(getRound());

				games.put(getRound(), fightList);

				freePlayers.get(0).sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentnoEnemieFound")));
				
				FightEndManager.letSpec(freePlayers.get(0), this, plugin);

				setStatsWinsRound(getRound(), freePlayers.get(0).getUniqueId(), 0);
				setStatsLosesRound(getRound(), freePlayers.get(0).getUniqueId(), 0);
				
				
				
			}

			for (Player players : getAllOut2()) {

				if (players != null) {
					if (isCompleteOut(players.getUniqueId())) continue;
					
					FightEndManager.letSpec(players, this, plugin);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Turnierersteller: " + Bukkit.getPlayer(getOwnerUUID()).getName() + " Spieler " + getRemainingPlayers());
			//Bukkit.broadcastMessage("§cEin Fehler beim Joinen ist aufgetreten! //Zeile 668");
			delete();
			Bukkit.getPlayer(getOwnerUUID()).sendMessage("§cEin Fehler bei deinem Turnier ist aufgetreten!");
			Bukkit.getPlayer(getOwnerUUID()).sendMessage("§cBitte sage einen Admin, er soll in den Fehler in der Konsole an JHammer17 schicken!");
			
		}
		
	}

	public void removeFight(UUID pos) {
		for (TournamentFight fight : fights) {
			if (fight.getPos1() == pos) {
				fights.remove(fight);
				return;
			}
			if (fight.getPos2() == pos) {
				fights.remove(fight);
				return;
			}
		}
	}

	public boolean allFightsEnded() {
		return fights.isEmpty();
	}

	public ArrayList<TournamentFight> getAllFights() {
		return fights;
	}

	public String getKitId() {
		
		String kit = this.kit;
		if (kit.contains(":"))
			kit = this.kit.split(":")[0];

		return plugin.getDBMgr().getUUID(kit).toString();
	}

	public String getKitSubId() {
		String subId = "d";
		String kit = this.kit;
		if (kit.contains(":"))
			subId = this.kit.split(":")[1];
		return subId;
	}

	public void addFight(Player pos1, Player pos2, TournamentManager tMgr, main plugin) {
		TournamentFight fight = new TournamentFight(pos1.getUniqueId(), pos2.getUniqueId(),
				tMgr.getOwnerUUID().toString(), plugin);
		fights.add(fight);
	}

	public int getRemainingPlayers() {
		return getPlayerList().size() - getAllOut().size();
	}

	public HashMap<String, String> getGames(int round) {
		return games.get(round);
	}

	public HashMap<String, String> getGames2(int round) {
		return games2.get(round);
	}

	public HashMap<UUID, Integer> getStatsWins(int round) {
		return statsWins.get(round);
	}

	public HashMap<UUID, Integer> getStatsLoses(int round) {
		return statsLoses.get(round);
	}

	public void setPlace(String Name, int i) {
		if (i == 1)
			place1 = Name;
		if (i == 2)
			place2 = Name;
		if (i == 3)
			place3 = Name;
		if (i == 4)
			place3_1 = Name;
	}

	public String getPlace(int i) {
		if (i == 1)return place1;
		if (i == 2)return place2;
		if (i == 3)return place3;
		if (i == 4)return place3_1;
		return "-";
	}

	public void setCompleteOut(UUID uuid, boolean out) {
		if (out) {
			while (ignore.contains(uuid))
				ignore.remove(uuid);
			ignore.add(uuid);
		} else
			while (ignore.contains(uuid))
				ignore.remove(uuid);
	}

	public boolean isCompleteOut(UUID uuid) {
		return ignore.contains(uuid);
	}
	
	public void openTournament(Player owner) {
		 if(!isOpened()) {
			  
			  ChallangeManager.removePlayerComplete(owner);
			  
			  setOpened(true);
			  
			  if(getPassword() == null || getPassword().equalsIgnoreCase("")) {
			   for(OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) {
				   	 if(!players.isIn1vs1()) continue;
			    	 players.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentpublicTournamentOpened").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix).replaceAll("%Name%", owner.getName())));
			    	 SoundManager manager = new SoundManager(JSound.ENDER_DRAGON, players.getPlayer(), 300, 1);
			    	 manager.play();
              
			   }
			   addPlayer(owner);
			   owner.closeInventory();
			  } else {
				  owner.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentprivateTournamentOpened").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix).replaceAll("%Name%", owner.getName()).replaceAll("%Password%", getPassword())));
				  SoundManager manager = new SoundManager(JSound.LEVEL, owner, 10.0F, 1.0F);
				  manager.play();
				  owner.closeInventory();
				  addPlayer(owner);
			  }
			  
			  reSetData(owner.getUniqueId(), this);
			  
			  Tournament_InvCreator creator1 = new Tournament_InvCreator(plugin);
         	  creator1.reGenerateInv(getOwnerUUID());
		  }
		 
		
		 
	}
	 public void reSetData(UUID tournament, TournamentManager tMgr) {
			while(plugin.tournaments.containsKey(tournament)) plugin.tournaments.remove(tournament);
			plugin.tournaments.put(tournament, tMgr);
		 }
}