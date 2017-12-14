package de.OnevsOne.Arena.Manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import de.OnevsOne.main;
import de.OnevsOne.Arena.Reseter.Builder.DeleteArena;
import de.OnevsOne.Kit_Methods.KitManager;
import de.OnevsOne.Listener.Manager.ChallangeManager;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.ScoreBoardManager;
import de.OnevsOne.Methods.Messenger.TitleAPI;
import de.OnevsOne.States.PlayerBestOfsPrefs;
import de.OnevsOne.States.PlayerState;
import de.OnevsOne.States.ArenaTeamPlayer;
import de.OnevsOne.States.OneVsOnePlayer;

/**
 * Der Code ist von JHammer
 *
 * 07.05.2016 um 14:07:58 Uhr
 */

/*
 * Diese Klasse sorgt für das Joinen einer Arena.
 * 
 * Methoden: 
 * joinArena(Player pos1, Player pos2, String ArenaID,boolean fromWarteschlange, String Kit) -> Sorgt dafür das man eine Arena beitrit
 * [PRIVAT] resetManager(String ArenaID) -> Startet einen Arenareset
 * 
 */
public class ArenaJoin {

	private static main plugin;

	@SuppressWarnings("static-access")
	public ArenaJoin(main plugin) {
		this.plugin = plugin;
	}
	
	/**
	Let two Players Join the Arena
	@param Pos1: Player1 | Pos2: Player2 | ArenaID: Arena | fromQueue: Is the join from Queue | Kit: Kit for the Arena
	*/
	public static void joinArena(Player pos1, Player pos2, String ArenaID,
			boolean fromQueue, String Kit, boolean fromTournament, String CSubID) {

		
		File file = new File("plugins/bStats/1vs1-Stats.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		int fights = cfg.getInt("Stats.Fights");
		
		fights++;
		
		cfg.set("Stats.Fights", fights);
		
		try {
			cfg.save(file);
		} catch (IOException e) {}
		
		
		
		ArrayList<UUID> listP1 = new ArrayList<>();
		listP1.add(pos1.getUniqueId());
		ArrayList<UUID> listP2 = new ArrayList<>();
		listP2.add(pos2.getUniqueId());
		
		while(plugin.allPlayersArenaP1.containsKey(ArenaID))
			plugin.allPlayersArenaP1.remove(ArenaID);
		while(plugin.allPlayersArenaP2.containsKey(ArenaID))
			plugin.allPlayersArenaP2.remove(ArenaID);
		
		plugin.allPlayersArenaP1.put(ArenaID, listP1);
		plugin.allPlayersArenaP2.put(ArenaID, listP2);
		
		
		String cKit = Kit;
		String subIDKit = "d"; 
		
		if (!fromQueue) {
			plugin.getOneVsOnePlayer(pos1).setWasInQueue(false);
			plugin.getOneVsOnePlayer(pos2).setWasInQueue(false);
		}
		de.OnevsOne.Commands.VariableCommands.Kit.hasKit.remove(pos1);
		de.OnevsOne.Commands.VariableCommands.Kit.hasKit.remove(pos2);
		plugin.getOneVsOnePlayer(pos1).setInQueue(false);
		plugin.getOneVsOnePlayer(pos2).setInQueue(false);
		

		plugin.getOneVsOnePlayer(pos1).setpState(PlayerState.InArena);
		plugin.getOneVsOnePlayer(pos2).setpState(PlayerState.InArena);

		
		 
		
		plugin.getRAMMgr().saveRAM(ArenaID, "Used", "true");
		while (plugin.FreeArenas.contains(ArenaID)) plugin.FreeArenas.remove(ArenaID);
		plugin.getRAMMgr().saveRAM(ArenaID, "Counter", "" + plugin.ArenaStartTimer);
		plugin.getRAMMgr().saveRAM(ArenaID, "Started", "false");
		
		
		if(cKit.contains(":")) subIDKit = cKit.split(":")[1];
		cKit = cKit.split(":")[0];
		
		if(plugin.getDBMgr().isCustomKitExists(cKit) == 1) {
			//Kit = CustomKit
			plugin.ArenaKit.put(ArenaID, Kit);
			
			plugin.getDBMgr().setStatsKit(Kit, 1, 1, 0);
			plugin.getDBMgr().setStatsKit(Kit, 1, 1, 1);
			plugin.getDBMgr().setStatsKit(Kit, 1, 1, 2);
		} else  {
			if(subIDKit == null || subIDKit.equalsIgnoreCase("d")) {
				subIDKit = plugin.getDBMgr().getDefaultKit(UUID.fromString(cKit));
				if(subIDKit.equalsIgnoreCase("")) {
					plugin.ArenaKit.put(ArenaID, cKit);
				} else {
					plugin.ArenaKit.put(ArenaID, cKit + ":" + subIDKit);
				}
				
				String uKit = plugin.ArenaKit.get(ArenaID);
				
				uKit = new KitManager(plugin).getkitAuthor(uKit);
				
				plugin.getDBMgr().setStatsKit(uKit, 1, Integer.parseInt(subIDKit), 0);
				plugin.getDBMgr().setStatsKit(uKit, 1, Integer.parseInt(subIDKit), 1);
				plugin.getDBMgr().setStatsKit(uKit, 1, Integer.parseInt(subIDKit), 2);
				
			} else {
				if(subIDKit.equalsIgnoreCase("")) {
					plugin.ArenaKit.put(ArenaID, cKit);
				} else {
					plugin.ArenaKit.put(ArenaID, cKit + ":" + subIDKit);
				}
				String uKit = plugin.ArenaKit.get(ArenaID);
				
				uKit = new KitManager(plugin).getkitAuthor(uKit);
				
				plugin.getDBMgr().setStatsKit(uKit, 1, Integer.parseInt(subIDKit), 0);
				plugin.getDBMgr().setStatsKit(uKit, 1, Integer.parseInt(subIDKit), 1);
				plugin.getDBMgr().setStatsKit(uKit, 1, Integer.parseInt(subIDKit), 2);
			}
		}
		
		
		plugin.getOneVsOnePlayer(pos1).setPosition(1);
		plugin.getOneVsOnePlayer(pos2).setPosition(2);
		
		plugin.getOneVsOnePlayer(pos1).setArena(ArenaID);
		plugin.getOneVsOnePlayer(pos2).setArena(ArenaID);
		
		plugin.getOneVsOnePlayer(pos1).setEnemy(pos2);
		plugin.getOneVsOnePlayer(pos2).setEnemy(pos1);
		
		

		pos1.setGameMode(GameMode.SURVIVAL);
		pos2.setGameMode(GameMode.SURVIVAL);

		if (!plugin.getAState().isReady(ArenaID) && plugin.getPositions().getArenaPos1(ArenaID) != null
				&& plugin.getPositions().getArenaPos1(ArenaID).add(0, -1, 0).getBlock()
						.getType() == Material.AIR) {
			plugin.getPositions().getArenaPos1(ArenaID).add(0, -1, 0).getBlock()
					.setType(Material.QUARTZ_BLOCK);
		}

		if (!plugin.getAState().isReady(ArenaID) && plugin.getPositions().getArenaPos1(ArenaID) != null
				&& plugin.getPositions().getArenaPos2(ArenaID).add(0, -1, 0).getBlock()
						.getType() == Material.AIR) {
			plugin.getPositions().getArenaPos2(ArenaID).add(0, -1, 0).getBlock()
					.setType(Material.QUARTZ_BLOCK);
		}

		if (!plugin.getAState().isReady(ArenaID) && !plugin.ResetingArenas.contains(ArenaID)) {
			resetManager(ArenaID);
		}

		pos1.teleport(plugin.getPositions().getArenaPos1(ArenaID));
		pos2.teleport(plugin.getPositions().getArenaPos2(ArenaID));

		
		
		
		if(plugin.getDBMgr().isCustomKitExists(cKit) == 1) {
			new KitManager(plugin).kitLoadCustomKit(cKit, pos1);
			new KitManager(plugin).kitLoadCustomKit(cKit, pos2);
			
		} else  {
			
			if(CSubID == null || CSubID.equalsIgnoreCase("d")) {
				new KitManager(plugin).Kitload(pos1, cKit.split(":")[0],"d");
				new KitManager(plugin).Kitload(pos2, cKit.split(":")[0],"d");
			} else {
				new KitManager(plugin).Kitload(pos1, cKit.split(":")[0],subIDKit);
				new KitManager(plugin).Kitload(pos2, cKit.split(":")[0],subIDKit);
			}
		}
		
		
		

		ArrayList<Player> ArenaPlayerP1 = new ArrayList<Player>();
		ArenaPlayerP1.add(pos1);
		ArrayList<Player> ArenaPlayerP2 = new ArrayList<Player>();
		ArenaPlayerP2.add(pos2);
		
		plugin.ArenaPlayersP1.remove(ArenaID);
		plugin.ArenaPlayersP2.remove(ArenaID);
		plugin.ArenaPlayersP1.put(ArenaID, ArenaPlayerP1);
		plugin.ArenaPlayersP2.put(ArenaID, ArenaPlayerP2);
		
		ChallangeManager.removePlayerComplete(pos1);
		ChallangeManager.removePlayerComplete(pos2);
		
		if(!fromTournament) {
			if(!plugin.BestOfSystem.containsKey(pos1.getUniqueId())) {
				 int Max = 1;
				 PlayerBestOfsPrefs prefState = PlayerBestOfsPrefs.BestOf1;
				 
				 prefState = plugin.getDBMgr().getQueuePrefState2(pos1.getUniqueId());
				 
			
				 if(prefState == PlayerBestOfsPrefs.BestOf1) {
					 
					 Max = 1;
				 } else if(prefState == PlayerBestOfsPrefs.BestOf3) {
					 Max = 3;
				 } else if(prefState == PlayerBestOfsPrefs.BestOf5) {
					 Max = 5;
				 }
				 
				 String[] data = {pos1.getUniqueId().toString(),pos2.getUniqueId().toString(),"" + Max,"0","0", Kit};
				 plugin.BestOfSystem.put(pos1.getUniqueId(), data);
				 if(Max != 1) {
					String title = plugin.msgs.getMsg("bestOf3Title");
					if(Max == 3) {
						title = plugin.msgs.getMsg("bestOf3Title");
					} else if(Max == 5) {
						title = plugin.msgs.getMsg("bestOf5Title");
					}
				
					TitleAPI.sendTitle(pos1, 10, 20*2, 10, title, plugin.msgs.getMsg("bestOfSubTitle").replaceAll("%Pos1%", pos1.getDisplayName()).replaceAll("%Pos2%", pos2.getDisplayName()).replaceAll("%Points1%", "0").replaceAll("%Points1%", "0"));
					TitleAPI.sendTitle(pos2, 10, 20*2, 10, title, plugin.msgs.getMsg("bestOfSubTitle").replaceAll("%Pos1%", pos1.getDisplayName()).replaceAll("%Pos2%", pos2.getDisplayName()).replaceAll("%Points1%", "0").replaceAll("%Points1%", "0"));
					
				 }
				
				}
		}


		
		ScoreBoardManager.updateBoard(pos1, false);
		ScoreBoardManager.updateBoard(pos2, false);
		
		plugin.getOneVsOnePlayer(pos1).setTeam(null);
		plugin.getOneVsOnePlayer(pos2).setTeam(null);
		
		while(plugin.arenaTeams.containsKey(ArenaID)) plugin.arenaTeams.remove(ArenaID);
		
		
		//0 Boots
		//1 Legs
		//2 Torso
		//3 Helmet
		
		
		
		
		ItemStack bootsP1 = pos1.getInventory().getArmorContents()[0];
		if(bootsP1.getType() == Material.LEATHER_BOOTS) {
			LeatherArmorMeta bootsP1Meta = (LeatherArmorMeta) bootsP1.getItemMeta();
			bootsP1Meta.setColor(Color.RED);
			bootsP1.setItemMeta(bootsP1Meta);
		}
		
		
		
		ItemStack legsP1 = pos1.getInventory().getArmorContents()[1];
		if(legsP1.getType() == Material.LEATHER_LEGGINGS) {
			LeatherArmorMeta legsP1Meta = (LeatherArmorMeta) legsP1.getItemMeta();
			legsP1Meta.setColor(Color.RED);
			legsP1.setItemMeta(legsP1Meta);
		}
		
		
		ItemStack torsoP1 = pos1.getInventory().getArmorContents()[2];
		if(torsoP1.getType() == Material.LEATHER_CHESTPLATE) {
			LeatherArmorMeta torsoP1Meta = (LeatherArmorMeta) torsoP1.getItemMeta();
			torsoP1Meta.setColor(Color.RED);
			torsoP1.setItemMeta(torsoP1Meta);
		}
		
		ItemStack helmetP1 = pos1.getInventory().getArmorContents()[3];
		
		if(helmetP1.getType() == Material.LEATHER_HELMET) {
			LeatherArmorMeta helmetP1Meta = (LeatherArmorMeta) helmetP1.getItemMeta();
			helmetP1Meta.setColor(Color.RED);
			helmetP1.setItemMeta(helmetP1Meta);
		}
		
		
		ItemStack bootsP2 = pos2.getInventory().getArmorContents()[0];
		if(bootsP2.getType() == Material.LEATHER_BOOTS) {
		 LeatherArmorMeta bootsP2Meta = (LeatherArmorMeta) bootsP2.getItemMeta();
		 bootsP2Meta.setColor(Color.BLUE);
		 bootsP2.setItemMeta(bootsP2Meta);
		}
		
		 ItemStack legsP2 = pos2.getInventory().getArmorContents()[1];
		if(legsP2.getType() == Material.LEATHER_LEGGINGS) {
		
		 LeatherArmorMeta legsP2Meta = (LeatherArmorMeta) legsP2.getItemMeta();
		 legsP2Meta.setColor(Color.BLUE);
		 legsP2.setItemMeta(legsP2Meta);
		}
		ItemStack torsoP2 = pos2.getInventory().getArmorContents()[2];
		if(torsoP2.getType() == Material.LEATHER_CHESTPLATE) {
			LeatherArmorMeta torsoP2Meta = (LeatherArmorMeta) torsoP2.getItemMeta();
			torsoP2Meta.setColor(Color.BLUE);
			torsoP2.setItemMeta(torsoP2Meta);
		}
		
		
		
		ItemStack helmetP2 = pos2.getInventory().getArmorContents()[3];
		if(helmetP2.getType() == Material.LEATHER_HELMET) {
			LeatherArmorMeta helmetP2Meta = (LeatherArmorMeta) helmetP2.getItemMeta();
			helmetP2Meta.setColor(Color.BLUE);
			helmetP2.setItemMeta(helmetP2Meta);
		}
		
		
	}
	//-----------------------------------------

	public static void joinArena(ArenaTeamPlayer pos1, ArenaTeamPlayer pos2, String ArenaID,
			boolean fromQueue, String Kit, boolean fromTournament, String CSubID) {

		File file = new File("plugins/bStats/1vs1-Stats.yml");
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		int fights = cfg.getInt("Stats.Fights");
		
		fights++;
		
		cfg.set("Stats.Fights", fights);
		
		try {
			cfg.save(file);
		} catch (IOException e) {}
		
		ArrayList<UUID> listP1 = new ArrayList<>();
		for(Player players : pos1.getAll()) 
			listP1.add(players.getUniqueId());
		
		ArrayList<UUID> listP2 = new ArrayList<>();
		for(Player players : pos2.getAll()) 
			listP2.add(players.getUniqueId());
		
		
		while(plugin.allPlayersArenaP1.containsKey(ArenaID))
			plugin.allPlayersArenaP1.remove(ArenaID);
		while(plugin.allPlayersArenaP2.containsKey(ArenaID))
			plugin.allPlayersArenaP2.remove(ArenaID);
		
		plugin.allPlayersArenaP1.put(ArenaID, listP1);
		plugin.allPlayersArenaP2.put(ArenaID, listP2);
		
		
		String cKit = Kit;
		String subIDKit = "d"; 
		
		for(Player players : pos1.getAll()) {
			OneVsOnePlayer ovoPlayer = plugin.getOneVsOnePlayer(players);
			if(ovoPlayer.getpState() == PlayerState.InArena) {
				for(Player playersx : pos1.getAll()) {
					playersx.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamOtherPlayerInArena")));
				}
				for(Player playersx : pos2.getAll()) {
					playersx.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamOtherPlayerInArena")));
				}
				return;
			}
			
		}
		for(Player players : pos2.getAll()) {
			OneVsOnePlayer ovoPlayer = plugin.getOneVsOnePlayer(players);
			if(ovoPlayer.getpState() == PlayerState.InArena) {
				for(Player playersx : pos1.getAll()) {
					playersx.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamOtherPlayerInArena")));
				}
				for(Player playersx : pos2.getAll()) {
					playersx.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("teamOtherPlayerInArena")));
				}
				return;
			}
			
		}
		
		if (!fromQueue) {
			for(Player players : pos1.getAll()) plugin.getOneVsOnePlayer(players).setWasInQueue(false);
			for(Player players : pos2.getAll()) plugin.getOneVsOnePlayer(players).setWasInQueue(false);
			
		}
		de.OnevsOne.Commands.VariableCommands.Kit.hasKit.removeAll(pos1.getAll());
		de.OnevsOne.Commands.VariableCommands.Kit.hasKit.removeAll(pos2.getAll());
		
		for(Player players : pos1.getAll()) plugin.getOneVsOnePlayer(players).setInQueue(false);
		for(Player players : pos2.getAll()) plugin.getOneVsOnePlayer(players).setInQueue(false);
		
		
		
		
		
		
			
		for(Player players : pos1.getAll()) {
			plugin.getOneVsOnePlayer(players).setpState(PlayerState.InArena);
		}
		for(Player players : pos2.getAll()) {
			plugin.getOneVsOnePlayer(players).setpState(PlayerState.InArena);
		}
		 
		plugin.getRAMMgr().saveRAM(ArenaID, "Used", "true");
		while (plugin.FreeArenas.contains(ArenaID)) plugin.FreeArenas.remove(ArenaID);
		plugin.getRAMMgr().saveRAM(ArenaID, "Counter", "" + plugin.ArenaStartTimer);
		plugin.getRAMMgr().saveRAM(ArenaID, "Started", "false");
		
		if(cKit.contains(":")) subIDKit = cKit.split(":")[1];
		cKit = cKit.split(":")[0];
		
		if(plugin.getDBMgr().isCustomKitExists(cKit) == 1) {
			if(CSubID.equalsIgnoreCase("")) {
				plugin.ArenaKit.put(ArenaID, Kit);
			} else {
				plugin.ArenaKit.put(ArenaID, Kit + ":" + CSubID);
			}
			
		} else  {
			
			
			
			if(subIDKit == null || subIDKit.equalsIgnoreCase("d")) {
				subIDKit = plugin.getDBMgr().getDefaultKit(UUID.fromString(cKit));
				if(subIDKit.equalsIgnoreCase("")) {
					plugin.ArenaKit.put(ArenaID, cKit);
				} else {
					plugin.ArenaKit.put(ArenaID, cKit + ":" + subIDKit);
				}
			} else {
				if(subIDKit.equalsIgnoreCase("")) {
					plugin.ArenaKit.put(ArenaID, cKit);
				} else {
					plugin.ArenaKit.put(ArenaID, cKit + ":" + subIDKit);
				}
			}
			
			
			
		}
		
		
		
		for(Player players : pos1.getAll()) plugin.getOneVsOnePlayer(players).setPosition(1);
		for(Player players : pos2.getAll()) plugin.getOneVsOnePlayer(players).setPosition(2);
		
		for(Player players : pos1.getAll()) plugin.getOneVsOnePlayer(players).setArena(ArenaID);
		for(Player players : pos2.getAll()) plugin.getOneVsOnePlayer(players).setArena(ArenaID);
		
		for(Player p1 : pos1.getAll()) {
		 for(Player p2 : pos2.getAll()) plugin.getOneVsOnePlayer(p1).setEnemy(p2);
		}
		for(Player p1 : pos2.getAll()) {
			for(Player p2 : pos1.getAll()) plugin.getOneVsOnePlayer(p1).setEnemy(p2);
		}
		
		for(Player players : pos1.getAll()) players.setGameMode(GameMode.SURVIVAL);
		for(Player players : pos2.getAll()) players.setGameMode(GameMode.SURVIVAL);

		if (!plugin.getAState().isReady(ArenaID)
			&& plugin.getPositions().getArenaPos1(ArenaID).add(0, -1, 0).getBlock().getType() == Material.AIR) {
			plugin.getPositions().getArenaPos1(ArenaID).add(0, -1, 0).getBlock().setType(Material.QUARTZ_BLOCK);
		}

		if (!plugin.getAState().isReady(ArenaID)
				&& plugin.getPositions().getArenaPos2(ArenaID).add(0, -1, 0).getBlock()
						.getType() == Material.AIR) {
			plugin.getPositions().getArenaPos2(ArenaID).add(0, -1, 0).getBlock()
					.setType(Material.QUARTZ_BLOCK);
		}

		if (!plugin.getAState().isReady(ArenaID) && !plugin.ResetingArenas.contains(ArenaID)) {
			resetManager(ArenaID);
		}

		
		for(Player players : pos1.getAll()) players.teleport(plugin.getPositions().getArenaPos1(ArenaID));
		for(Player players : pos2.getAll()) players.teleport(plugin.getPositions().getArenaPos2(ArenaID));

		
		if(plugin.getDBMgr().isCustomKitExists(cKit) == 1) {
			for(Player players : pos1.getAll()) new KitManager(plugin).kitLoadCustomKit(cKit, players);
			for(Player players : pos2.getAll()) new KitManager(plugin).kitLoadCustomKit(cKit, players);
			
		} else  {
			
			if(CSubID == null || CSubID.equalsIgnoreCase("d")) {
				for(Player players : pos1.getAll()) new KitManager(plugin).Kitload(players, cKit.split(":")[0],"d");
				for(Player players : pos2.getAll()) new KitManager(plugin).Kitload(players, cKit.split(":")[0],"d");
			} else {
				for(Player players : pos1.getAll()) new KitManager(plugin).Kitload(players, cKit.split(":")[0],subIDKit);
				for(Player players : pos2.getAll()) new KitManager(plugin).Kitload(players, cKit.split(":")[0],subIDKit);
			}
		}
		
		
		
		
		ArrayList<Player> ArenaPlayerP1 = pos1.getAll();
		ArrayList<Player> ArenaPlayerP2 = pos2.getAll();
		
		plugin.ArenaPlayersP1.remove(ArenaID);
		plugin.ArenaPlayersP2.remove(ArenaID);
		plugin.ArenaPlayersP1.put(ArenaID, ArenaPlayerP1);
		plugin.ArenaPlayersP2.put(ArenaID, ArenaPlayerP2);
		
		for(Player players : pos1.getAll()) ChallangeManager.removePlayerComplete(players);
		for(Player players : pos2.getAll()) ChallangeManager.removePlayerComplete(players);
		
		
		
		if(!fromTournament) {
			if(!plugin.BestOfSystem.containsKey(pos1.getPlayer().getUniqueId())) {
				 int Max = 1;
				 PlayerBestOfsPrefs prefState = PlayerBestOfsPrefs.BestOf1;
				 
				 prefState = plugin.getDBMgr().getQueuePrefState2(pos1.getPlayer().getUniqueId());
				 
				 if(prefState == PlayerBestOfsPrefs.BestOf1) Max = 1;
				  else if(prefState == PlayerBestOfsPrefs.BestOf3) Max = 3;
				  else if(prefState == PlayerBestOfsPrefs.BestOf5) Max = 5;
				 
				 
				 String[] data = {pos1.getPlayer().getUniqueId().toString(),pos2.getPlayer().getUniqueId().toString(),"" + Max,"0","0", Kit};
				 plugin.BestOfSystem.put(pos1.getPlayer().getUniqueId(), data);
				 if(Max != 1) {
					String title = "§cBest of 3";
					if(Max == 3) title = "§cBest of 3";
					 else if(Max == 5) title = "§cBest of 5";
					
					for(Player playersP1 : pos1.getAll()) {
						TitleAPI.sendTitle(playersP1, 10, 20*2, 10, title, plugin.msgs.getMsg("bestOfSubTitle").replaceAll("%Pos1%", pos1.getPlayer().getDisplayName()).replaceAll("%Pos2%", pos2.getPlayer().getDisplayName()).replaceAll("%Points1%", "0").replaceAll("%Points1%", "0"));
					}
					for(Player playersP2 : pos2.getAll()) {
						TitleAPI.sendTitle(playersP2, 10, 20*2, 10, title, plugin.msgs.getMsg("bestOfSubTitle").replaceAll("%Pos1%", pos1.getPlayer().getDisplayName()).replaceAll("%Pos2%", pos2.getPlayer().getDisplayName()).replaceAll("%Points1%", "0").replaceAll("%Points1%", "0"));
					}
					
					
				 }
				
				}
		}

		
		for(Player players : pos1.getAll())ScoreBoardManager.updateBoard(players, false);
		for(Player players : pos2.getAll())ScoreBoardManager.updateBoard(players, false);
		
		
		while(plugin.arenaTeams.containsKey(ArenaID)) plugin.arenaTeams.remove(ArenaID);
		
		
		ArrayList<ArenaTeamPlayer> players = new ArrayList<>();
		
		players.add(pos1);
		players.add(pos2);
		
		plugin.arenaTeams.put(ArenaID, players);
		
		
		
		
		
		
		for(Player t1 : pos1.getAll()) {
			//0 Boots
			//1 Legs
			//2 Torso
			//3 Helmet
			
			ItemStack bootsP1 = t1.getInventory().getArmorContents()[0];
			if(bootsP1.getType() == Material.LEATHER_BOOTS) {
				LeatherArmorMeta bootsP1Meta = (LeatherArmorMeta) bootsP1.getItemMeta();
				bootsP1Meta.setColor(Color.RED);
				bootsP1.setItemMeta(bootsP1Meta);
			}
			
			
			
			ItemStack legsP1 = t1.getInventory().getArmorContents()[1];
			if(legsP1.getType() == Material.LEATHER_LEGGINGS) {
				LeatherArmorMeta legsP1Meta = (LeatherArmorMeta) legsP1.getItemMeta();
				legsP1Meta.setColor(Color.RED);
				legsP1.setItemMeta(legsP1Meta);
			}
			
			
			ItemStack torsoP1 = t1.getInventory().getArmorContents()[2];
			if(torsoP1.getType() == Material.LEATHER_CHESTPLATE) {
				LeatherArmorMeta torsoP1Meta = (LeatherArmorMeta) torsoP1.getItemMeta();
				torsoP1Meta.setColor(Color.RED);
				torsoP1.setItemMeta(torsoP1Meta);
			}
			
			ItemStack helmetP1 = t1.getInventory().getArmorContents()[3];
			
			if(helmetP1.getType() == Material.LEATHER_HELMET) {
				LeatherArmorMeta helmetP1Meta = (LeatherArmorMeta) helmetP1.getItemMeta();
				helmetP1Meta.setColor(Color.RED);
				helmetP1.setItemMeta(helmetP1Meta);
			}
		}
		
		for(Player t2 : pos2.getAll()) {
			ItemStack bootsP2 = t2.getInventory().getArmorContents()[0];
			if(bootsP2.getType() == Material.LEATHER_BOOTS) {
			 LeatherArmorMeta bootsP2Meta = (LeatherArmorMeta) bootsP2.getItemMeta();
			 bootsP2Meta.setColor(Color.BLUE);
			 bootsP2.setItemMeta(bootsP2Meta);
			}
			
			 ItemStack legsP2 = t2.getInventory().getArmorContents()[1];
			if(legsP2.getType() == Material.LEATHER_LEGGINGS) {
			
			 LeatherArmorMeta legsP2Meta = (LeatherArmorMeta) legsP2.getItemMeta();
			 legsP2Meta.setColor(Color.BLUE);
			 legsP2.setItemMeta(legsP2Meta);
			}
			ItemStack torsoP2 = t2.getInventory().getArmorContents()[2];
			if(torsoP2.getType() == Material.LEATHER_CHESTPLATE) {
				LeatherArmorMeta torsoP2Meta = (LeatherArmorMeta) torsoP2.getItemMeta();
				torsoP2Meta.setColor(Color.BLUE);
				torsoP2.setItemMeta(torsoP2Meta);
			}
			
			
			
			ItemStack helmetP2 = t2.getInventory().getArmorContents()[3];
			if(helmetP2.getType() == Material.LEATHER_HELMET) {
				LeatherArmorMeta helmetP2Meta = (LeatherArmorMeta) helmetP2.getItemMeta();
				helmetP2Meta.setColor(Color.BLUE);
				helmetP2.setItemMeta(helmetP2Meta);
			}
		}
		
		
	}
	
	
	//resetManager(String ArenaID): Resetet die Arena, falls es nötig ist.
	private static void resetManager(String ArenaID) {
		
		if (plugin.existFile("Arenen/" + ArenaID + "/config")) {

			String Layout = plugin.getPositions().getLayout(ArenaID);
			if (!Layout.equalsIgnoreCase("null")) {
				Location pos1 = plugin.getPositions().getPos1(Layout);
				Location pos2 = plugin.getPositions().getPos2(Layout);
				Location pos3 = plugin.getPositions().getPos3(ArenaID);

				if (pos1 == null) {
					System.out.println(MessageReplacer.replaceStrings(plugin.msgs.getMsg("errorPos1NotFound"), null, null,null, ArenaID));
					return;
				}

				if (pos2 == null) {
					System.out.println(MessageReplacer.replaceStrings(plugin.msgs.getMsg("errorPos2NotFound"), null, null,null, ArenaID));
					return;
				}

				if (pos3 == null) {
					System.out.println(MessageReplacer.replaceStrings(plugin.msgs.getMsg("errorPos3NotFound"), null, null,null, ArenaID));
					return;
				}
				DeleteArena.startReset(pos1, pos2, pos3, ArenaID);
				System.out.println(MessageReplacer.replaceStrings(plugin.msgs.getMsg("startResetAfterBug"), null, null,null, ArenaID));

			} else {
				System.out.println(MessageReplacer.replaceStrings(plugin.msgs.getMsg("layoutNotFound"), null, null,null, ArenaID));
			}

		} else {
			System.out.println(MessageReplacer.replaceStrings(plugin.msgs.getMsg("arenaNotFound"), null, null,null, ArenaID));
		}
	}
	//-------------------------------------------------------
}
