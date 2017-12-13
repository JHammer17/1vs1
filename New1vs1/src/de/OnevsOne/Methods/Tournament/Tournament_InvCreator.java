package de.OnevsOne.Methods.Tournament;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.OnevsOne.main;
import de.OnevsOne.Kit_Methods.KitManager;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.getItems;
import de.OnevsOne.States.TournamentState;

public class Tournament_InvCreator {

	
	private main plugin;

	public Tournament_InvCreator(main plugin) {
		this.plugin = plugin;
	}
	
	public void openInv(Player p) {
		
	 
	
		 TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
		 
		 if(tMgr.getDefault()) {
			   tMgr.setKit(new KitManager(plugin).getSelectedKit(p));
			   tMgr.setPassword("");
			   
			   tMgr.setFightsKo(1);
			   tMgr.setMaxTimeKoMins(2); // TODO SET TO 2
			   tMgr.setMaxTimeKoSecs(0); // TODO SET TO 0
			   
			   tMgr.setRoundsQu(3);//TODO SET TO 3
			   tMgr.setFightsQu(3);
			   
			   tMgr.setStartTimeMins(0); //TODO SET TO 0
			   tMgr.setStartTimeSecs(30); //TODO SET TO 30
			   
			   tMgr.setMaxTimeQuSecs(0); // TODO SET TO 0
			   tMgr.setMaxTimeQuMins(1);//TODO SET TO 1
			   
			   tMgr.setMaxPlayers(-1);
			   tMgr.setOpened(false);
			   
			   tMgr.setState(TournamentState.WAITING);
			   tMgr.setRound(0);
			   
			   tMgr.setSetDefault(false);
			   
			   
			   
			   
			   openInv(p);
			   return;
		 }
		 
		 Inventory inv = Bukkit.createInventory(null, 45, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentSettingsInvTitle")));
		   
		   ItemStack emptyPain = getItems.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§e", null);
		   ItemStack startTournament = getItems.createItem(Material.STAINED_GLASS_PANE, 5, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentstartTournament")), null);
		   ItemStack deleteTournament = getItems.createItem(Material.STAINED_GLASS_PANE, 14, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentdeleteTournament")), null);
		   ItemStack openTournament = getItems.createItem(Material.STAINED_GLASS_PANE, 13, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentpublishTournament")), null);
		   
		   					
		   ItemStack kit = getItems.createItem(Material.SIGN, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentInvKit")).replaceAll("%Kit%", tMgr.getKit()), null);
		   ItemStack password = getItems.createItem(Material.TRIPWIRE_HOOK, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentInvPassword")).replaceAll("%Password%", tMgr.getPassword()), null);
		   if(tMgr.getPassword().equalsIgnoreCase("")) {
			   password = getItems.createItem(Material.TRIPWIRE_HOOK, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentInvNoPassword")), null);
		   }
		   
		   
		   ItemStack addPlayer = getItems.createItem(Material.STONE_BUTTON, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentAddPlayer")), MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentAddPlayerLore")));
		   ItemStack removePlayer = getItems.createItem(Material.STONE_BUTTON, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentRemovePlayer")), MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentRemovePlayerLore")));
		   
		   ItemStack addTime = getItems.createItem(Material.STONE_BUTTON, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentadd10Seconds")), MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentadd30SecondsLore")));
		   ItemStack removeTime = getItems.createItem(Material.STONE_BUTTON, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentremove10Seconds")), MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentremove30SecondsLore")));
		   
		   
		   
		   
		   ItemStack qualliSettings = getItems.createItem(Material.EMERALD, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentQPSettings")), null);
		   ItemStack koSettings = getItems.createItem(Material.EXPLOSIVE_MINECART, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentKOPSettings")), null);
		   String players = "" + tMgr.getMaxPlayers();
		   if(tMgr.getMaxPlayers() <= -1) {
			   players = MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentinfinity")); 
		   }
		   ItemStack maxPlayers = getItems.createItem(Material.SKULL_ITEM, 3, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentSettingsMaxPlayers")).replaceAll("%MaxPlayers%", "" + players), null);
		   
		   
		   
		   
		   
		   String secs = "";
		   if(tMgr.getStartTimeSecs() < 10) {
			   secs = "0" + tMgr.getStartTimeSecs();
		   } else {
			   secs = "" + tMgr.getStartTimeSecs();
		   }
		   int mins = tMgr.getStartTimeMins();
		   ItemStack startTime = getItems.createItem(Material.WATCH, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentSettingsStartTime")).replaceAll("%TimeMins%", "" + mins).replaceAll("%TimeSecs%", secs.toString()), null);
		   
		   inv.setItem(0, kit);
		   
		   inv.setItem(2, deleteTournament);
		   inv.setItem(3, deleteTournament);
		   inv.setItem(4, deleteTournament);
		   
		   inv.setItem(6, emptyPain);
		   inv.setItem(8, password);
		   
		   for(int i = 0; i < 9; i++) inv.setItem(i+9, emptyPain);
		   
		   inv.setItem(15, openTournament);
		   
		   inv.setItem(21, addPlayer);
		   inv.setItem(22, addTime);
		   
		   inv.setItem(24, emptyPain);
		   
		   inv.setItem(25, startTournament);
		   inv.setItem(26, startTournament);
		   
		   inv.setItem(28, qualliSettings);
		   inv.setItem(29, koSettings);
		   inv.setItem(30, maxPlayers);
		   inv.setItem(31, startTime);
		   
		   inv.setItem(33, emptyPain);
		   
		   inv.setItem(34, startTournament);
		   inv.setItem(35, startTournament);
		   
		   inv.setItem(39, removePlayer);
		   inv.setItem(40, removeTime);
		  
		   
		   inv.setItem(42, emptyPain);
		   
		   inv.setItem(43, startTournament);
		   inv.setItem(44, startTournament);
		   
		   p.openInventory(inv);
		 return;
	 
	  
	   
	   /*TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
	 
	   */
	   
	}
	
	public void generateQualliInv(Player p) {
		TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
		
		Inventory inv = Bukkit.createInventory(null, 27, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentqualliInvTitle")));
		
		int fightsPerRound = tMgr.getFightsQu(); 
		int roundsI = tMgr.getRoundsQu();
		ItemStack rounds = getItems.createItem(Material.NETHER_STAR, 0, tMgr.getFightsQu(), MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentFightsPerRound")).replaceAll("%Fights%", "" + fightsPerRound), null);
		ItemStack qualli = getItems.createItem(Material.SAPLING, 0, tMgr.getRoundsQu(), MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentRoundsItem")).replaceAll("%Rounds%", "" + roundsI), null);
		String secs = "";
		if(tMgr.getMaxTimeQuSecs() < 10) {
		 secs = "0" + tMgr.getMaxTimeQuSecs();
		} else {
		 secs = "" + tMgr.getMaxTimeQuSecs();
		}
		int mins = tMgr.getMaxTimeQuMins();
		ItemStack time = getItems.createItem(Material.WATCH, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentmaxFightTimeQ")).replaceAll("%Mins%", "" + mins).replaceAll("%Secs%", secs), null);
		
		ItemStack addTime = getItems.createItem(Material.STONE_BUTTON, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentadd10Seconds")), MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentadd30SecondsLore")));
		ItemStack removeTime = getItems.createItem(Material.STONE_BUTTON, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentremove10Seconds")), MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentremove30SecondsLore")));
		
		ItemStack addRound = getItems.createItem(Material.STONE_BUTTON, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentadd1Round")), null);
		ItemStack removeRound = getItems.createItem(Material.STONE_BUTTON, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentremove1Round")), null);
		
		ItemStack exit = getItems.createItem(Material.BARRIER, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentgoBack")), null);
		
		inv.setItem(10, rounds);
		inv.setItem(4, addRound);
		inv.setItem(13, qualli);
		inv.setItem(22, removeRound);
		inv.setItem(16, time);
		inv.setItem(7, addTime);
		inv.setItem(25, removeTime);
		inv.setItem(18, exit);
		
		p.openInventory(inv);
	}
	
	public void generateKOInv(Player p) {
		TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
		
		Inventory inv = Bukkit.createInventory(null, 27, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentkOInvTitle")));
		
		int fights = tMgr.getFightsKo();
		ItemStack rounds = getItems.createItem(Material.NETHER_STAR, 0, tMgr.getFightsKo(), MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentFightsPerRound")).replaceAll("%Fights%", "" + fights), null);
		String secs = "";
		if(tMgr.getMaxTimeKoSecs() < 10) {
		 secs = "0" + tMgr.getMaxTimeKoSecs();
		} else {
		 secs = "" + tMgr.getMaxTimeKoSecs();
		}
		int mins = tMgr.getMaxTimeKoMins();
		ItemStack time = getItems.createItem(Material.WATCH, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentmaxFightTimeQ")).replaceAll("%Mins%", "" + mins).replaceAll("%Secs%", "" + secs), null);
		
		
		
		ItemStack addTime = getItems.createItem(Material.STONE_BUTTON, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentadd10Seconds")), MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentadd30SecondsLore")));
		ItemStack removeTime = getItems.createItem(Material.STONE_BUTTON, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentremove10Seconds")), MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentremove30SecondsLore")));
		
		ItemStack exit = getItems.createItem(Material.BARRIER, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentgoBack")), null);
		
		inv.setItem(10, rounds);
		inv.setItem(7, addTime);
		inv.setItem(25, removeTime);
		inv.setItem(16, time);
		inv.setItem(18, exit);
		
		p.openInventory(inv);
	}
	
	public void creatInfoInv(UUID Turnier, Player p) {
		
		if(plugin.InfoInv.containsKey(Turnier)) {
			Inventory inv = Bukkit.createInventory(null, plugin.InfoInv.get(Turnier).getSize(),MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentInfosInvName")));
			inv.setContents(plugin.InfoInv.get(Turnier).getContents());
			p.openInventory(inv);
		} else {
			generate(Turnier,27);
			Inventory inv = Bukkit.createInventory(null, plugin.InfoInv.get(Turnier).getSize(),MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentInfosInvName")));
			inv.setContents(plugin.InfoInv.get(Turnier).getContents());
			p.openInventory(inv);
		}
		
	}
	
	
	public void reGenerateInv(final UUID Turnier) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			
			@Override
			public void run() {
				generate(Turnier, 27);
				
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private void generate(UUID Turnier, int size) {
		Inventory inv = Bukkit.createInventory(null, size, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentInfosInvName")));
		//TODO Verbessern!
		ItemStack emptyPain = getItems.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§e", null);
		//States states = new States(Turnier, plugin);
		
		TournamentManager tMgr = plugin.tournaments.get(Turnier);
		
		if(tMgr == null) return;
		
		String secs1 = "";
		if(tMgr.getMaxTimeKoSecs() < 10) {
			secs1 = "0" + tMgr.getMaxTimeKoSecs();
		} else {
			secs1 = "" + tMgr.getMaxTimeKoSecs();
		}
		
		String secs2 = "";
		if(tMgr.getMaxTimeQuSecs() < 10) {
			secs2 = "0" + tMgr.getMaxTimeQuSecs();
		} else {
			secs2 = "" + tMgr.getMaxTimeQuSecs();
		}
		
		/*String Lore = "§7Turnierleiter: §6" + states.getOwner() + "\n"
				    + "§7Kit: §6" + states.getKit() + "\n"
				    + "§7Spieleranzahl: §6" + states.getPlayerList() + "\n"
				    + "§7--------------------------------\n"//32
				    + "§7Qualifikation:\n"
				    + "§7*Maximale Kampfzeit: §6" + states.getQTimeMins() + ":" + secs2 + "\n"
					+ "§7*Runden: §6" + states.getRoundsQualli() + "\n"
				    + "§7*Spiele: §6" + states.getFightsQualli() + "\n"
					+ "§7--------------------------------\n"//32
				    + "§7K.O.-Phase:\n"
				    + "§7*Spiele: §6" +  states.getFightsKO() + "\n"
					+ "§7*Maximale Kampfzeit: §6" +  states.getTimeMins() + ":" + secs1 + "\n"
					+ "§7--------------------------------\n" //32
					+ "§7Runde: " + states.getRoundName() + "\n"
					+ "§7Verbleibende Spieler: §6" + states.getRemainingPlayers().size(); */
		
		String Lore = MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentInfoLore")
									 .replaceAll("%Owner%", tMgr.getName())
									 .replaceAll("%Kit%", tMgr.getKit())
									 .replaceAll("%PlayersT%", "" + tMgr.getPlayerList().size())
									 .replaceAll("%QMins%", "" + tMgr.getMaxTimeQuMins())
									 .replaceAll("%QSecs%", secs2)
									 .replaceAll("%RoundsQ%", "" + tMgr.getRoundsQu())
									 .replaceAll("%GamesQ%", "" + tMgr.getFightsQu())
									 .replaceAll("%GamesKO%", "" + tMgr.getFightsKo())
									 .replaceAll("%KOMins%", "" + tMgr.getMaxTimeKoMins())
									 .replaceAll("%KOSecs%", "" + secs1)
									 .replaceAll("%Round%", "" + tMgr.getRound())
									 .replaceAll("%RemainingPlayers%", "" + tMgr.getRemainingPlayers()));
		
		ItemStack InfoSign = getItems.createItem(Material.SIGN, 0, 1, MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentInfosItem")), Lore);
	
		
		StringBuilder builder = new StringBuilder("");
		
		tMgr.sortMap();
		int place = 1;
		
		
		HashMap<UUID, Integer> statsQ = (HashMap<UUID, Integer>) tMgr.getStatsQ().clone();
		for(UUID uuid : statsQ.keySet()) {
			if(tMgr.isCompleteOut(uuid)) {
				builder.append("§f§l" + place + ". §7§l" + tMgr.getPlayerName(uuid) + "§r§7: §a" + tMgr.getQPointsWins(uuid) +"§7 ◄► §c" + tMgr.getQPointsLoses(uuid) +"\n");
			} else {
				builder.append("§f§l" + place + ". §b§l" + tMgr.getPlayerName(uuid) + "§r§7: §a" + tMgr.getQPointsWins(uuid) +"§7 ◄► §c" + tMgr.getQPointsLoses(uuid) +"\n");
			}//TODO Language.yml
			
			place++;
		}
		
		
		
		ItemStack QInfo = getItems.createItem(Material.SLIME_BALL, 0, 1, "§6Zusammenfassung der Qualifikation", builder.toString());
		//TODO Language.yml
		
		int slot = 12;
		
		for(int round = 1 ; tMgr.getRound() >= round ; round++) {
		 if(round > tMgr.getRoundsQu()) {
			 //KO-Phase
			 
			
			 
			 StringBuilder QuBuilder = new StringBuilder();
			 
			 HashMap<String, String> games = tMgr.getGames(round);
			 HashMap<String, String> games2 = tMgr.getGames2(round);
			 
			 if(games2 != null && games != null && tMgr.getStatsWins(round) != null && tMgr.getStatsWins(round) != null) {
				 
				 HashMap<UUID, Integer> statsW = tMgr.getStatsWins(round);
				 HashMap<UUID, Integer> statsL = tMgr.getStatsLoses(round);
				 
				 
				 
				 ArrayList<String> listed = new ArrayList<>();
				 
				 for(UUID uuid : statsW.keySet()) {
					 String name = tMgr.getPlayerName(uuid);
					 
					 int wins = 0;
					 int lost = 0;
					 
					 if(statsW.containsKey(uuid)) wins = statsW.get(uuid);
					 if(statsL.containsKey(uuid)) lost = statsL.get(uuid);
					 
					 
					 
					 if(games.containsKey(name)) {
						 String pos1 = name;
						 String pos2 = "-";
						 if(games.get(name) != null) pos2 = games.get(name); 
						
						 
						 if(listed.contains(pos1) || listed.contains(pos2)) continue;
						 
						 listed.add(pos1);
						 listed.add(pos2);
						 
						 int played = wins+lost;
						 
						 if(played >= tMgr.getFightsKo() || tMgr.getRound() > round || pos2.equalsIgnoreCase("-")) {
							 if(wins >= lost) {
								 QuBuilder.append("§a");
								 QuBuilder.append(pos1);
								 QuBuilder.append(" §7(");
								 QuBuilder.append(wins);
								 QuBuilder.append(") ◄► §c");
								 QuBuilder.append(pos2);
								 QuBuilder.append(" §7(");
								 QuBuilder.append(lost);
								 QuBuilder.append(")\n");
							 } else {
								 QuBuilder.append("§c");
								 QuBuilder.append(pos1);
								 QuBuilder.append(" §7(");
								 QuBuilder.append(wins);
								 QuBuilder.append(") ◄► §a");
								 QuBuilder.append(pos2);
								 QuBuilder.append(" §7(");
								 QuBuilder.append(lost);
								 QuBuilder.append(")\n");
							 }
						 } else {
							 QuBuilder.append("§e");
							 QuBuilder.append(pos1);
							 QuBuilder.append(" §7(");
							 QuBuilder.append(wins);
							 QuBuilder.append(") ◄► §e");
							 QuBuilder.append(pos2);
							 QuBuilder.append(" §7(");
							 QuBuilder.append(lost);
							 QuBuilder.append(")\n");
						 }
					 } else if(games2.containsKey(name)) {
						 String pos1 = "-";
						 String pos2 = name;
						 if(games2.get(name) != null) pos1 = games2.get(name); 
						 
						 if(listed.contains(pos1) || listed.contains(pos2)) continue;
						 
						 listed.add(pos1);
						 listed.add(pos2);
						 
						 
						 
						 
						 int played = wins+lost;
						 
						 if(played >= tMgr.getFightsKo() || tMgr.getRound() > round || pos1.equalsIgnoreCase("-")) {
							 if(wins >= lost) {
								 QuBuilder.append("§c");
								 QuBuilder.append(pos1);
								 QuBuilder.append(" §7(");
								 QuBuilder.append(lost);
								 QuBuilder.append(") ◄► §a");
								 QuBuilder.append(pos2);
								 QuBuilder.append(" §7(");
								 QuBuilder.append(wins);
								 QuBuilder.append(")\n");
							 } else {
								 QuBuilder.append("§a");
								 QuBuilder.append(pos1);
								 QuBuilder.append(" §7(");
								 QuBuilder.append(lost);
								 QuBuilder.append(") ◄► §c");
								 QuBuilder.append(pos2);
								 QuBuilder.append(" §7(");
								 QuBuilder.append(wins);
								 QuBuilder.append(")\n");
							 }
						 } else {
							 QuBuilder.append("§e");
							 QuBuilder.append(pos1);
							 QuBuilder.append(" §7(");
							 QuBuilder.append(lost);
							 QuBuilder.append(") ◄► §e");
							 QuBuilder.append(pos2);
							 QuBuilder.append(" §7(");
							 QuBuilder.append(wins);
							 QuBuilder.append(")\n");
						 }
						 
						 
						 
						 
						 
						 
					 }
					 
					 
					 
				 }
				 
				 
				 
			 }
			 
			 if(slot > 53) return;
			 if(slot > 45 && size < 54) {
				 generate(Turnier, 54);
				 return;
			 }
			 if(slot > 35 && size < 45 ) {
				 generate(Turnier, 45);
				 return;
			 }
			 if(slot > 26 && size < 36) {
				 generate(Turnier, 36);
				 return;
			 }
			 
			 ItemStack gameStatsKo = getItems.createItem(Material.SNOW_BALL, 0, round, "§6" + round + ". Runde (K.O.-Phase)", QuBuilder.toString());
			 inv.setItem(slot, gameStatsKo);
			 slot++;
			 
			 
			 
			 
			 if(slot == 18 || slot == 19 || slot == 20)  slot = 21;
			 
			 
		 } else {
			 //Qualli
			 StringBuilder QuBuilder = new StringBuilder();
			 
			 HashMap<String, String> games = tMgr.getGames(round);
			 HashMap<String, String> games2 = tMgr.getGames2(round);
			 
			 if(games2 != null && games != null && tMgr.getStatsWins(round) != null && tMgr.getStatsWins(round) != null) {
				 
				 HashMap<UUID, Integer> statsW = tMgr.getStatsWins(round);
				 HashMap<UUID, Integer> statsL = tMgr.getStatsLoses(round);
				 
				 
				 
				 ArrayList<String> listed = new ArrayList<>();
				 
				 for(UUID uuid : statsW.keySet()) {
					 
					 
					 
					 String name = tMgr.getPlayerName(uuid);
					 
					 int wins = 0;
					 int lost = 0;
					 
					 if(statsW.containsKey(uuid)) wins = statsW.get(uuid);
					 if(statsL.containsKey(uuid)) lost = statsL.get(uuid);
					 
					 
					
					 
					 if(games.containsKey(name)) {
						 String pos1 = name;
						 String pos2 = "-";
						 if(games.get(name) != null) pos2 = games.get(name); 
						 
						 if(listed.contains(pos1) || listed.contains(pos2)) continue;
						 
						 listed.add(pos1);
						 listed.add(pos2);
						 

						 
						 if((wins+lost) >= tMgr.getFightsQu() || tMgr.getRound() > round || pos2.equalsIgnoreCase("-")) {
							 
							if(wins >= lost) {
								 QuBuilder.append("§a");
								 QuBuilder.append(pos1);
								 QuBuilder.append(" §7(");
								 QuBuilder.append(wins);
								 QuBuilder.append(") ◄► §c");
								 QuBuilder.append(pos2);
								 QuBuilder.append(" §7(");
								 QuBuilder.append(lost);
								 QuBuilder.append(")\n");
							 } else {
								 QuBuilder.append("§c");
								 QuBuilder.append(pos1);
								 QuBuilder.append(" §7(");
								 QuBuilder.append(wins);
								 QuBuilder.append(") ◄► §a");
								 QuBuilder.append(pos2);
								 QuBuilder.append(" §7(");
								 QuBuilder.append(lost);
								 QuBuilder.append(")\n");
							 }
						 } else {
							 QuBuilder.append("§e");
							 QuBuilder.append(pos1);
							 QuBuilder.append(" §7(");
							 QuBuilder.append(wins);
							 QuBuilder.append(") ◄► §e");
							 QuBuilder.append(pos2);
							 QuBuilder.append(" §7(");
							 QuBuilder.append(lost);
							 QuBuilder.append(")\n");
						 }
					 } else if(games2.containsKey(name)) {
						 String pos1 = "-";
						 String pos2 = name;
						 if(games2.get(name) != null) pos1 = games2.get(name); 
						 
						 if(listed.contains(pos1) || listed.contains(pos2)) continue;
						 
						 listed.add(pos1);
						 listed.add(pos2);
						 
						 
						 if((wins+lost) >= tMgr.getFightsQu() || tMgr.getRound() > round || pos1.equalsIgnoreCase("-")) {
							 if(wins >= lost) {
								 QuBuilder.append("§c");
								 QuBuilder.append(pos1);
								 QuBuilder.append(" §7(");
								 QuBuilder.append(lost);
								 QuBuilder.append(") ◄► §a");
								 QuBuilder.append(pos2);
								 QuBuilder.append(" §7(");
								 QuBuilder.append(wins);
								 QuBuilder.append(")\n");
							 } else {
								 QuBuilder.append("§a");
								 QuBuilder.append(pos1);
								 QuBuilder.append(" §7(");
								 QuBuilder.append(lost);
								 QuBuilder.append(") ◄► §c");
								 QuBuilder.append(pos2);
								 QuBuilder.append(" §7(");
								 QuBuilder.append(wins);
								 QuBuilder.append(")\n");
							 }
						 } else {
							 QuBuilder.append("§e");
							 QuBuilder.append(pos1);
							 QuBuilder.append(" §7(");
							 QuBuilder.append(lost);
							 QuBuilder.append(") ◄► §e");
							 QuBuilder.append(pos2);
							 QuBuilder.append(" §7(");
							 QuBuilder.append(wins);
							 QuBuilder.append(")\n");
						 }
					 }
					 
					 
					 
				 }
				 
				 
				 
			 }
			 
			 
			 if(slot > 53) return;
			 if(slot >= 45 && size < 54) {
				 generate(Turnier, 54);
				 return;
			 }
			 if(slot > 35 && size < 45 ) {
				 generate(Turnier, 45);
				 return;
			 }
			 if(slot > 26 && size < 36) {
				 generate(Turnier, 36);
				 return;
			 }
			 ItemStack gameStatsQu = getItems.createItem(Material.SNOW_BALL, 0, round, "§6" + round + ". Runde (Qualifikations-Phase)", QuBuilder.toString());
			 inv.setItem(slot, gameStatsQu);
			 slot++;
			 
			 
			 if(slot == 18 || slot == 19 || slot == 20)  slot = 21;
		 }
		}
		
		
		
		
		inv.setItem(0, emptyPain);
		inv.setItem(1, emptyPain);
		inv.setItem(2, emptyPain);
		inv.setItem(3, QInfo);
		
		inv.setItem(9, emptyPain);
		inv.setItem(10, InfoSign);
		inv.setItem(11, emptyPain);
		
		inv.setItem(18, emptyPain);
		inv.setItem(19, emptyPain);
		inv.setItem(20, emptyPain);
		
		while(plugin.InfoInv.containsKey(Turnier)) plugin.InfoInv.remove(Turnier);
		
		plugin.InfoInv.put(Turnier, inv);
		
	}
	
}
