package de.OnevsOne.Methods;

import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.file.YamlConfiguration;
import de.OnevsOne.main;

/**
 * Der Code ist von JHammer
 *
 * 29.09.2017 um 18:27:35 Uhr
 * 
 */
public class configMgr {

	private main plugin;

	public configMgr(main plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings({ "unchecked", "static-access" })
	public void reloadData(boolean instantReset) {

		
		
		YamlConfiguration cfg = plugin.defaultYML();
		
		if (cfg.get("config.BungeeMode.Enabled") == null) {
			cfg.set("config.BungeeMode.Enabled", plugin.BungeeMode);
		} else {
			plugin.BungeeMode = cfg.getBoolean("config.BungeeMode.Enabled");
		}
		
		
		if (cfg.get("config.BungeeMode.LobbyServer") == null) {
			cfg.set("config.BungeeMode.LobbyServer", plugin.fallBackServer);
		} else {
			plugin.fallBackServer = cfg.getString("config.BungeeMode.LobbyServer");
		}
		
		
		if (cfg.get("config.ArenaStartTimer") == null) {
			cfg.set("config.ArenaStartTimer", plugin.ArenaStartTimer);
		} else {
			plugin.ArenaStartTimer = cfg.getInt("config.ArenaStartTimer");
		}
		
		
		if (cfg.get("config.Soupheal") == null) {
			cfg.set("config.Soupheal", plugin.Soupheal);
		} else {
			plugin.Soupheal = cfg.getDouble("config.Soupheal");
		}
		
		

		if (cfg.get("config.maxArenaEntitys") == null) {
			cfg.set("config.maxArenaEntitys", plugin.maxArenaEntitys);
		} else {
			plugin.maxArenaEntitys = cfg.getInt("config.maxArenaEntitys");
		}

		
		if (cfg.get("config.twoStepArenaReset") == null) {
			cfg.set("config.twoStepArenaReset", plugin.twoStepArenaReset);
		} else {
			plugin.twoStepArenaReset = cfg.getBoolean("config.twoStepArenaReset");
		}
		
		
		if (cfg.get("config.MySQL.Enabled") == null) {
			cfg.set("config.MySQL.Enabled", plugin.useMySQL);
		} else {
			plugin.useMySQL = cfg.getBoolean("config.MySQL.Enabled");
		}

		
		if (cfg.get("config.MySQL.Domain") == null) {
			cfg.set("config.MySQL.Domain", plugin.MySQLDomain);
		} else {
			plugin.host = cfg.getString("config.MySQL.Domain");
		}
		
		
		if (cfg.get("config.MySQL.Port") == null) {
			cfg.set("config.MySQL.Port", plugin.port);
		} else {
			plugin.port = "" + cfg.getInt("config.MySQL.Port");
		}
		
		
		if (cfg.get("config.MySQL.Database") == null) {
			cfg.set("config.MySQL.Database", plugin.MySQLDataBase);
		} else {
			plugin.database = cfg.getString("config.MySQL.Database");
		}
		
		
		if (cfg.get("config.MySQL.Username") == null) {
			cfg.set("config.MySQL.Username", plugin.username);
		} else {
			plugin.username = cfg.getString("config.MySQL.Username");
		}
		
		if (cfg.get("config.MySQL.Password") == null) {
			cfg.set("config.MySQL.Password", plugin.password);
		} else {
			plugin.password = cfg.getString("config.MySQL.Password");

			if (plugin.MySQLPassword.equalsIgnoreCase("-")) 
				plugin.MySQLPassword = "";
		}
		
		if (cfg.get("config.ArenaCheckTimer") == null) {
			cfg.set("config.ArenaCheckTimer", plugin.ArenaCheckTimer);
		} else {
			plugin.ArenaCheckTimer = cfg.getInt("config.ArenaCheckTimer");
		}
		
		if (cfg.get("config.NoFreeArenaMessageTimer") == null) {
			cfg.set("config.NoFreeArenaMessageTimer", plugin.NoFreeArenaMessageTimer);
		} else {
			plugin.NoFreeArenaMessageTimer = cfg.getInt("config.NoFreeArenaMessageTimer");
		}

		
		if (cfg.get("config.ArenaBuildDelay") == null) {
			cfg.set("config.ArenaBuildDelay", plugin.ArenaBuild);
		} else {
			plugin.ArenaBuild = cfg.getInt("config.ArenaBuildDelay");
		}
		
		if (cfg.get("config.ArenaDestroyDelay") == null) {
			cfg.set("config.ArenaDestroyDelay", plugin.ArenaDestroy);
		} else {
			plugin.ArenaDestroy = cfg.getInt("config.ArenaDestroyDelay");
		}

		
		if (cfg.get("config.minArenaBuildDistanceBottom") == null) {
			cfg.set("config.minArenaBuildDistanceBottom", plugin.minArenaBuildDistanceBottom);
		} else {
			plugin.minArenaBuildDistanceBottom = cfg.getInt("config.minArenaBuildDistanceBottom");
		}

		if (cfg.get("config.minArenaBuildDistanceTop") == null) {
			cfg.set("config.minArenaBuildDistanceTop", plugin.minArenaBuildDistanceTop);
		} else {
			plugin.minArenaBuildDistanceTop = cfg.getInt("config.minArenaBuildDistanceTop");
		}
		
		if (cfg.get("config.minArenaBuildDistanceWalls") == null) {
			cfg.set("config.minArenaBuildDistanceWalls", plugin.minArenaBuildDistanceWalls);
		} else {
			plugin.minArenaBuildDistanceWalls = cfg.getInt("config.minArenaBuildDistanceWalls");
		}
		
		if (cfg.get("config.saveStats") == null) {
			cfg.set("config.saveStats", plugin.saveStats);
		} else {
			plugin.saveStats = cfg.getBoolean("config.saveStats");
		}
		
		if (cfg.get("config.loadChunks") == null) {
			cfg.set("config.loadChunks", plugin.loadChunks);
		} else {
			plugin.loadChunks = cfg.getBoolean("config.loadChunks");
		}
		
		if (cfg.get("config.arenaRegionLeaveDamage") == null) {
			cfg.set("config.arenaRegionLeaveDamage", plugin.ArenaRegionLeaveDamage);
		} else {
			plugin.ArenaRegionLeaveDamage = cfg.getDouble("config.arenaRegionLeaveDamage");
		}
		
		if (cfg.get("config.showArenaNamesSpectatorGUI") == null) {
			cfg.set("config.showArenaNamesSpectatorGUI", plugin.showArenaNamesSpectatorGUI);
		} else {
			plugin.showArenaNamesSpectatorGUI = cfg.getBoolean("config.showArenaNamesSpectatorGUI");
		}
		
		if (cfg.get("config.toggleCoolDown") == null) {
			cfg.set("config.toggleCoolDown", plugin.toggleCoolDown);
		} else {
			plugin.toggleCoolDown = cfg.getInt("config.toggleCoolDown");
		}
		
		
		
		
		
		if (cfg.get("config.playAllBestOfGames") == null) {
			cfg.set("config.playAllBestOfGames", plugin.playAllBestOfGames);
		} else {
			plugin.playAllBestOfGames = cfg.getBoolean("config.playAllBestOfGames");
		}
		

		if (cfg.get("config.saveInvs") == null) {
			cfg.set("config.saveInvs", plugin.saveInvs);
		} else {
			plugin.saveInvs = cfg.getBoolean("config.saveInvs");
		}
		

		if (cfg.get("config.checkDatabaseConnection") == null) {
			cfg.set("config.checkDatabaseConnection", plugin.checkDatabaseConnection);
		} else {
			plugin.checkDatabaseConnection = cfg.getBoolean("config.checkDatabaseConnection");
		}

		if (cfg.get("config.updateNotification") == null) {
			cfg.set("config.updateNotification", plugin.updateNoti);
		} else {
			plugin.updateNoti = cfg.getBoolean("config.updateNotification");
		}
		
		if (cfg.get("config.updateNotificationJoin") == null) {
			cfg.set("config.updateNotificationJoin", plugin.updateNotiJoin);
		} else {
			plugin.updateNotiJoin = cfg.getBoolean("config.updateNotificationJoin");
		}
		
		if (cfg.get("config.autoEndmatch") == null) {
			cfg.set("config.autoEndmatch", plugin.autoEndmatch);
		} else {
			plugin.autoEndmatch = cfg.getInt("config.autoEndmatch");
		}	

		if (cfg.get("config.scoreBoardName") == null) {
			cfg.set("config.scoreBoardName", plugin.scoreBoardName);
		} else {
			plugin.scoreBoardName = cfg.getString("config.scoreBoardName");
		}

		if (cfg.get("config.overrideJoinMsg") == null) {
			cfg.set("config.overrideJoinMsg", plugin.overrideJoinLeaveMsg);
		} else {
			plugin.overrideJoinLeaveMsg = cfg.getBoolean("config.overrideJoinMsg");
		}
		
		if (cfg.get("config.saveOldScoreboard") == null) {
			cfg.set("config.saveOldScoreboard", plugin.saveOldScoreboard);
		} else {
			plugin.saveOldScoreboard = cfg.getBoolean("config.saveOldScoreboard");
		}

		plugin.voidTeleport = false;

		if (cfg.get("config.useScoreboard") == null) {
			cfg.set("config.useScoreboard", plugin.useScoreboard);
		} else {
			plugin.useScoreboard = cfg.getBoolean("config.useScoreboard");
		}
		
		if (cfg.get("config.useBlockyMapReset") == null) {
			cfg.set("config.useBlockyMapReset", plugin.useBlockyMapReset);
		} else {
			plugin.useBlockyMapReset = cfg.getBoolean("config.useBlockyMapReset");
		}
		
		if (cfg.get("config.maxBlocksPerTick") == null) {
			cfg.set("config.maxBlocksPerTick", plugin.maxBlocksPerTick);
		} else {
			plugin.maxBlocksPerTick = cfg.getInt("config.maxBlocksPerTick");
			if (plugin.maxBlocksPerTick <= 1) plugin.maxBlocksPerTick = 2;
			
		}

		if (cfg.get("config.maxTeamSizeUser") == null) {
			cfg.set("config.maxTeamSizeUser", plugin.maxTeamSizeUser);
			
		} else {
			plugin.maxTeamSizeUser = cfg.getInt("config.maxTeamSizeUser");
		}
		
		

		if (cfg.get("config.maxTeamSizePremium") == null) {
			cfg.set("config.maxTeamSizePremium", plugin.maxTeamSizePremium);
		} else {
			plugin.maxTeamSizePremium = cfg.getInt("config.maxTeamSizePremium");
		}

		if (cfg.get("config.maxTeamSizeSpecial") == null) {
			cfg.set("config.maxTeamSizeSpecial", plugin.maxTeamSizeSpecial);
		} else {
			plugin.maxTeamSizeSpecial = cfg.getInt("config.maxTeamSizeSpecial");
		}
		
		if (cfg.get("config.useEconomySystem") == null) {
			cfg.set("config.useEconomySystem", plugin.useEconomy);
		} else {
			plugin.cfgEconomy = cfg.getBoolean("config.useEconomySystem");
		}
		
		if (cfg.get("config.economyNormalWinAdmin") == null) {
			cfg.set("config.economyNormalWinAdmin", plugin.economyNormalWinAdmin);
		} else {
			plugin.economyNormalWinAdmin = cfg.getInt("config.economyNormalWinAdmin");
		}
		

		if (cfg.get("config.economyNormalWinPremium") == null) {
			cfg.set("config.economyNormalWinPremium", plugin.economyNormalWinPremium);
			
		} else {
			plugin.economyNormalWinPremium = cfg.getInt("config.economyNormalWinPremium");
		}
		

		if (cfg.get("config.economyNormalWinSpecial") == null) {
			cfg.set("config.economyNormalWinSpecial", plugin.economyNormalWinSpecial);
		} else {
			plugin.economyNormalWinSpecial = cfg.getInt("config.economyNormalWinSpecial");
		}
		

		if (cfg.get("config.economyNormalWinUser") == null) {
			cfg.set("config.economyNormalWinUser", plugin.economyNormalWinUser);
			
		} else {
			plugin.economyNormalWinUser = cfg.getInt("config.economyNormalWinUser");
		}
		
		if (cfg.get("config.economyTournamnetWinAdmin") == null) {
			cfg.set("config.economyTournamnetWinAdmin", plugin.economyTournamnetWinAdmin);
		} else {
			plugin.economyTournamnetWinAdmin = cfg.getInt("config.economyTournamnetWinAdmin");
		}
		

		if (cfg.get("config.economyTournamnetWinPremium") == null) {
			cfg.set("config.economyTournamnetWinPremium", plugin.economyTournamnetWinPremium);
		} else {
			plugin.economyTournamnetWinPremium = cfg.getInt("config.economyTournamnetWinPremium");
		}
		
		if (cfg.get("config.economyTournamnetWinSpecial") == null) {
			cfg.set("config.economyTournamnetWinSpecial", plugin.economyTournamnetWinSpecial);
		} else {
			plugin.economyTournamnetWinSpecial = cfg.getInt("config.economyTournamnetWinSpecial");
		}

		if (cfg.get("config.economyTournamnetWinUser") == null) {
			cfg.set("config.economyTournamnetWinUser", plugin.economyTournamnetWinUser);
		} else {
			plugin.economyTournamnetWinUser = cfg.getInt("config.economyTournamnetWinUser");
		}

		if (cfg.get("config.economyTournamnetKillAdmin") == null) {
			cfg.set("config.economyTournamnetKillAdmin", plugin.economyTournamnetKillAdmin);
		} else {
			plugin.economyTournamnetKillAdmin = cfg.getInt("config.economyTournamnetKillAdmin");
		}

		if (cfg.get("config.economyTournamnetKillPremium") == null) {
			cfg.set("config.economyTournamnetKillPremium", plugin.economyTournamnetKillPremium);
		} else {
			plugin.economyTournamnetKillPremium = cfg.getInt("config.economyTournamnetKillPremium");
		}

		if (cfg.get("config.economyTournamnetKillSpecial") == null) {
			cfg.set("config.economyTournamnetKillSpecial", plugin.economyTournamnetKillSpecial);
		} else {
			plugin.economyTournamnetKillSpecial = cfg.getInt("config.economyTournamnetKillSpecial");
		}
		
		if (cfg.get("config.economyTournamnetKillUser") == null) {
			cfg.set("config.economyTournamnetKillUser", plugin.economyTournamnetKillUser);
		} else {
			plugin.economyTournamnetKillUser = cfg.getInt("config.economyTournamnetKillUser");
		}
		
		if (cfg.get("config.maxTNTGame") == null) {
			cfg.set("config.maxTNTGame", plugin.maxTNTArenaGame);
		} else {
			plugin.maxTNTArenaGame = cfg.getInt("config.maxTNTGame");
		}

		if (cfg.get("config.silentBlackDealer") == null) {
			cfg.set("config.silentBlackDealer", plugin.silentBlackDealer);
		} else {
			plugin.silentBlackDealer = cfg.getBoolean("config.silentBlackDealer");
		}
		
		if (cfg.get("config.silentPrefVillager") == null) {
			cfg.set("config.silentPrefVillager", plugin.silentPrefVillager);
		} else {
			plugin.silentPrefVillager = cfg.getBoolean("config.silentPrefVillager");
		}

		if (cfg.get("config.silentQueue") == null) {
			cfg.set("config.silentQueue", plugin.silentQueue);
		} else {
			plugin.silentQueue = cfg.getBoolean("config.silentQueue");
		}
		
		if (cfg.get("config.msgMeWhenIStupid") == null) {
			cfg.set("config.msgMeWhenIStupid", plugin.msgMeWhenIStupid);
		} else {
			plugin.msgMeWhenIStupid = cfg.getBoolean("config.msgMeWhenIStupid");
		}
		
		if (cfg.get("config.Ranking.Rank9") == null) {
			cfg.set("config.Ranking.Rank9", plugin.rank9);
		} else {
			plugin.rank9 = cfg.getInt("config.Ranking.Rank9");
		}
		
		if (cfg.get("config.Ranking.Rank8") == null) {
			cfg.set("config.Ranking.Rank8", plugin.rank8);
		} else {
			plugin.rank8 = cfg.getInt("config.Ranking.Rank8");
		}
		
		if (cfg.get("config.Ranking.Rank7") == null) {
			cfg.set("config.Ranking.Rank7", plugin.rank7);
		} else {
			plugin.rank7 = cfg.getInt("config.Ranking.Rank7");
		}
		
		if (cfg.get("config.Ranking.Rank6") == null) {
			cfg.set("config.Ranking.Rank6", plugin.rank6);
		} else {
			plugin.rank6 = cfg.getInt("config.Ranking.Rank6");
		}
		
		if (cfg.get("config.Ranking.Rank5") == null) {
			cfg.set("config.Ranking.Rank5", plugin.rank5);
		} else {
			plugin.rank5 = cfg.getInt("config.Ranking.Rank5");
		}
		
		
		if (cfg.get("config.Ranking.Rank4") == null) {
			cfg.set("config.Ranking.Rank4", plugin.rank4);
		} else {
			plugin.rank4 = cfg.getInt("config.Ranking.Rank4");
		}
		
		
		if (cfg.get("config.Ranking.Rank3") == null) {
			cfg.set("config.Ranking.Rank3", plugin.rank3);
		} else {
			plugin.rank3 = cfg.getInt("config.Ranking.Rank3");
		}
		
		if (cfg.get("config.Ranking.Rank2") == null) {
			cfg.set("config.Ranking.Rank2", plugin.rank2);
			
		} else {
			plugin.rank2 = cfg.getInt("config.Ranking.Rank2");
		}
		
		if (cfg.get("config.Ranking.Rank1") == null) {
			cfg.set("config.Ranking.Rank1", plugin.rank1);
		} else {
			plugin.rank1 = cfg.getInt("config.Ranking.Rank1");
		}
		
		if (cfg.get("config.Ranking.rankPointsWins") == null) {
			cfg.set("config.Ranking.rankPointsWins", plugin.rankPointsWins);
		} else {
			plugin.rankPointsWins = cfg.getInt("config.Ranking.rankPointsWins");
			
		}
		
		if (cfg.get("config.Ranking.rankPointsLose") == null) {
			plugin.rankPointsLose = -1;
			cfg.set("config.Ranking.rankPointsLose", plugin.rankPointsLose);
		} else {
			plugin.rankPointsLose = cfg.getInt("config.Ranking.rankPointsLose");
		}
		
		if (cfg.get("config.ACS.Enabled") == null) {
			cfg.set("config.ACS.Enabled", plugin.ACSEnabled);
			plugin.ACSEnabled = true;
			cfg.set("config.ACS.Enabled", plugin.ACSEnabled);
		} else {
			plugin.ACSEnabled = cfg.getBoolean("config.ACS.Enabled");
		}
		
		if (cfg.get("config.ACS.minArenas") == null) {
			plugin.ACSMin = 1;
			cfg.set("config.ACS.minArenas", plugin.ACSMin);
		} else {
			plugin.ACSMin = cfg.getInt("config.ACS.minArenas");
		}

		if (cfg.get("config.ACS.maxArenas") == null) {
			plugin.ACSMax = 10;
			cfg.set("config.ACS.maxArenas", plugin.ACSMax);
			
		} else {
			plugin.ACSMax = cfg.getInt("config.ACS.maxArenas");
		}
		
		if (cfg.get("config.ACS.world") == null) {
			plugin.ACSWorld = "1vs1-ACS";
			cfg.set("config.ACS.world", plugin.ACSWorld);
		} else {
			plugin.ACSWorld = cfg.getString("config.ACS.world");
		}
		
		if (cfg.get("config.ACS.disX") == null) {
			plugin.ACSDistX = 0;
			cfg.set("config.ACS.disX", plugin.ACSDistX);
		} else {
			plugin.ACSDistX = cfg.getInt("config.ACS.disX");
		}

		
		
		if (cfg.get("config.ACS.disZ") == null) {
			plugin.ACSDistZ = 0;
			cfg.set("config.ACS.disZ", plugin.ACSDistZ);
		} else {
			plugin.ACSDistZ = cfg.getInt("config.ACS.disZ");
		}
		
		if (cfg.get("config.topPlaces") == null) {
			cfg.set("config.topPlaces", plugin.topPlaces);
		} else {
			plugin.topPlaces = cfg.getInt("config.topPlaces");
		}
		
		if(cfg.get("config.lastTimedStatReset") == null) {
			cfg.set("config.lastTimedStatReset", (long)plugin.lastTimedStatReset);
		} else {
			plugin.lastTimedStatReset = cfg.getLong("config.lastTimedStatReset");
		}
		
		if(cfg.get("config.lastTimedStatReset24h") == null) {
			cfg.set("config.lastTimedStatReset24h", (long)plugin.lastTimedStatReset24h);
		} else {
			plugin.lastTimedStatReset24h = cfg.getLong("config.lastTimedStatReset24h");
		}
		
		if(cfg.get("config.TimedStatResetTime") == null) {
			plugin.timedStatResetTime = 2592000000L;
			cfg.set("config.TimedStatResetTime", (long)plugin.timedStatResetTime);
		} else {
			plugin.timedStatResetTime = cfg.getLong("config.TimedStatResetTime");
		}
		
		// ----------------------------------------------

		if (cfg.get("config.Items.ChallangerItemID") == null) {
			cfg.set("config.Items.ChallangerItemID", plugin.ChallangerItemID);
		} else {
			plugin.ChallangerItemID = cfg.getInt("config.Items.ChallangerItemID");
		}
		
		if (cfg.get("config.Items.SpecatorItemID") == null) {
			cfg.set("config.Items.SpecatorItemID", plugin.SpectatorItemID);
		} else {
			plugin.SpectatorItemID = cfg.getInt("config.Items.SpecatorItemID");
		}

		if (cfg.get("config.Items.SettingsItemID") == null) {
			cfg.set("config.Items.SettingsItemID", plugin.SettingsItemID);
		} else {
			plugin.SettingsItemID = cfg.getInt("config.Items.SettingsItemID");
		}
		
		if (cfg.get("config.Items.LeaveItemID") == null) {
			cfg.set("config.Items.LeaveItemID", plugin.LeaveItemID);
		} else {
			plugin.LeaveItemID = cfg.getInt("config.Items.LeaveItemID");
		}
		
		if (cfg.get("config.Items.ChallangerItemSlot") == null) {
			cfg.set("config.Items.ChallangerItemSlot", plugin.ChallangerItemSlot);
		} else {
			plugin.ChallangerItemSlot = cfg.getInt("config.Items.ChallangerItemSlot");
		}

		if (cfg.get("config.Items.SpecatorItemSlot") == null) {
			cfg.set("config.Items.SpecatorItemSlot", plugin.SpectatorItemSlot);
		} else {
			plugin.SpectatorItemSlot = cfg.getInt("config.Items.SpecatorItemSlot");
		}
		
		if (cfg.get("config.Items.SettingsItemSlot") == null) {
			cfg.set("config.Items.SettingsItemSlot", plugin.SettingsItemSlot);
		} else {
			plugin.SettingsItemSlot = cfg.getInt("config.Items.SettingsItemSlot");
		}
		
		if (cfg.get("config.Items.LeaveItemSlot") == null) {
			cfg.set("config.Items.LeaveItemSlot", plugin.LeaveItemSlot);
		} else {
			plugin.LeaveItemSlot = cfg.getInt("config.Items.LeaveItemSlot");
		}
		
		if (cfg.get("config.Items.BookItemSlot") == null) {
			cfg.set("config.Items.BookItemSlot", plugin.BookItemSlot);
		} else {
			plugin.BookItemSlot = cfg.getInt("config.Items.BookItemSlot");
		}
		
		if (cfg.get("config.Items.TournamentItemSlot") == null) {
			cfg.set("config.Items.TournamentItemSlot", plugin.TournamentItemSlot);
		} else {
			plugin.TournamentItemSlot = cfg.getInt("config.Items.TournamentItemSlot");
		}
		
		if (cfg.get("config.Items.RankItemSlot") == null) {
			cfg.set("config.Items.RankItemSlot", plugin.RankItemSlot);
		} else {
			plugin.RankItemSlot = cfg.getInt("config.Items.RankItemSlot");
		}
		
		if (cfg.get("config.Items.TournamentItemID") == null) {
			cfg.set("config.Items.TournamentItemID", plugin.TournamentItemID);
		} else {
			plugin.TournamentItemID = cfg.getInt("config.Items.TournamentItemID");
		}
		
		if (cfg.get("config.blockedCommands") == null) {

			if (cfg.getConfigurationSection("config.blockedCommands") == null) {
				ArrayList<String> blockedCommands = new ArrayList<>();
				blockedCommands.add("spawn");
				blockedCommands.add("warp");
				blockedCommands.add("back");
				cfg.set("config.blockedCommands", blockedCommands);
			} else {
				cfg.set("config.blockedCommands", new ArrayList<String>());
			}
		} else {
			for (String commands : (ArrayList<String>) cfg.getList("config.blockedCommands"))
				plugin.blockedCommands.add(commands.toLowerCase());
		}

		if (cfg.get("config.useBlockedCmdsAsWhitelist") == null) {
			cfg.set("config.useBlockedCmdsAsWhitelist", plugin.useAsWhitelist);
			
		} else { 
			plugin.useAsWhitelist = cfg.getBoolean("config.useBlockedCmdsAsWhitelist");
		}
		
		if (cfg.get("config.reduceStartDebugInfo") == null) {
			cfg.set("config.reduceStartDebugInfo", plugin.reduceStartDebugInfo);
			
		} else { 
			plugin.reduceStartDebugInfo = cfg.getBoolean("config.reduceStartDebugInfo");
		}
		
		cfg.options().header(
				"================================ #\n		1vs1 - Like Timolia		   #\n 		 	by JHammer17		   #\n================================ #");
		cfg.options().copyHeader(true);

		try {
			cfg.save(plugin.defaultFile());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		
		
		
		
	}
	
	
}
