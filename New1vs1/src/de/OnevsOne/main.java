package de.OnevsOne;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import com.google.common.io.Files;

import de.OnevsOne.Arena.Manager.ArenaEvents;
import de.OnevsOne.Arena.Manager.ArenaJoin;
import de.OnevsOne.Arena.Manager.ArenaState;
import de.OnevsOne.Arena.Manager.ManageRAMData;
import de.OnevsOne.Arena.Manager.RemoveEntitys;
import de.OnevsOne.Arena.Manager.ACS.Manager;
import de.OnevsOne.Arena.Reseter.ResetMethoden;
import de.OnevsOne.Arena.Reseter.Builder.BlockMapReset;
import de.OnevsOne.Arena.Reseter.Builder.CopyArena;
import de.OnevsOne.Arena.Reseter.Builder.DeleteArena;
import de.OnevsOne.Arena.SpectatorManager.ArenaMenu;
import de.OnevsOne.Arena.SpectatorManager.SpectateArena;
import de.OnevsOne.Arena.SpectatorManager.SpectateArenaItemManager;
import de.OnevsOne.Arena.SpectatorManager.Spectator_Events;
import de.OnevsOne.Commands.MainCommand;
import de.OnevsOne.Commands.VariableCommands.Endmatch;
import de.OnevsOne.Commands.VariableCommands.Kit;
import de.OnevsOne.Commands.VariableCommands.KitStats;
import de.OnevsOne.Commands.VariableCommands.Leave;
import de.OnevsOne.Commands.VariableCommands.Spec;
import de.OnevsOne.Commands.VariableCommands.Stats;
import de.OnevsOne.Commands.VariableCommands.Surrender;
import de.OnevsOne.Commands.VariableCommands.Team;
import de.OnevsOne.Commands.VariableCommands.Win;
import de.OnevsOne.Commands.VariableCommands.Manager.Command_Manager;
import de.OnevsOne.Commands.VariableCommands.Tournament.Create;
import de.OnevsOne.Commands.VariableCommands.Tournament.Join;
import de.OnevsOne.Commands.VariableCommands.Tournament.Start;
import de.OnevsOne.Commands.VariableCommands.Tournament.TLeave;
import de.OnevsOne.Commands.VariableCommands.Tournament.Tournament;
import de.OnevsOne.DataBases.DBMainManager;
import de.OnevsOne.DataBases.MySQL.MySQLManager;
import de.OnevsOne.DataBases.SQLite.Database;
import de.OnevsOne.DataBases.SQLite.SQLite;
import de.OnevsOne.Guide.ArenaInv;
import de.OnevsOne.Guide.BaseInv;
import de.OnevsOne.Guide.Inv_Opener;
import de.OnevsOne.Guide.LayoutInv;
import de.OnevsOne.Guide.Other.OtherInv;
import de.OnevsOne.Guide.Other.OtherSignInv;
import de.OnevsOne.Guide.Other.OtherSkullInv;
import de.OnevsOne.Kit_Methods.KitMessages;
import de.OnevsOne.Kit_Methods.Kit_Editor_Move;
import de.OnevsOne.Kit_Methods.Multi_Kit_Manager;
import de.OnevsOne.Kit_Methods.loadKitEdit;
import de.OnevsOne.Listener.Blocked_Events;
import de.OnevsOne.Listener.KillEvent;
import de.OnevsOne.Listener.PlayerQueqeChangeSettings;
import de.OnevsOne.Listener.Region_Edit;
import de.OnevsOne.Listener.Manager.BlackDealerInvManager;
import de.OnevsOne.Listener.Manager.ChallangeManager;
import de.OnevsOne.Listener.Manager.DisableMapsManager;
import de.OnevsOne.Listener.Manager.KitStandsInvManger;
import de.OnevsOne.Listener.Manager.Preferences_Manager;
import de.OnevsOne.Listener.Manager.SignManager;
import de.OnevsOne.Listener.Manager.TeamManager;
import de.OnevsOne.Listener.Manager.Warteschlange_Manager;
import de.OnevsOne.Listener.Manager.Tournament.LobbyTournamentItemManager;
import de.OnevsOne.Listener.Manager.Tournament.Tournament_Creator_InvManager;
import de.OnevsOne.Listener.Manager.Tournament.Tournament_Events;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.MessageManager.NewMsgLoader;
import de.OnevsOne.Methods.BlackDealerInvCreator;
import de.OnevsOne.Methods.PositionManager;
import de.OnevsOne.Methods.KitStands;
import de.OnevsOne.Methods.ScoreBoardManager;
import de.OnevsOne.Methods.ScoreboardAPI;
import de.OnevsOne.Methods.SignMethods;
import de.OnevsOne.Methods.Teleport;
import de.OnevsOne.Methods.configMgr;
import de.OnevsOne.Methods.getItems;
import de.OnevsOne.Methods.openArenaCheckInv;
import de.OnevsOne.Methods.saveErrorMethod;
import de.OnevsOne.Methods.Core.MainScheduler;
import de.OnevsOne.Methods.FightEnder.FightEnd;
import de.OnevsOne.Methods.FightEnder.FightEndTeam;
import de.OnevsOne.Methods.Mobs.spawnBlackDealer;
import de.OnevsOne.Methods.Mobs.spawnPrefVillager;
import de.OnevsOne.Methods.Mobs.spawnQueque;
import de.OnevsOne.Methods.Queue.QuequePrefsMethods;
import de.OnevsOne.Methods.Queue.QueueManager;
import de.OnevsOne.Methods.Tournament.TournamentManager;
import de.OnevsOne.Metrics.Metrics;
import de.OnevsOne.States.AllErrors;
import de.OnevsOne.States.ArenaTeamPlayer;
import de.OnevsOne.States.OneVsOnePlayer;
import de.OnevsOne.States.PlayerPrefs;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

/**
 * Der Code ist von JHammer17
 *
 * 05.05.2016 um 20:45:09 Uhr
 */
public class main extends JavaPlugin implements Listener {

	
	//Stuff
	 public ScoreboardAPI scoreAPI = new ScoreboardAPI();
	 public Permission permissionmgr;
	
	 HashMap<UUID, OneVsOnePlayer> OneVSOnePlayers = new HashMap<>();
	 public HashMap<UUID, TournamentManager> tournaments = new HashMap<>();
	 public HashMap<UUID, Inventory> InfoInv = new HashMap<>();
	
	 public NewMsgLoader msgs;
	 public int topPlaces = 10;
	
	 public String oldNameQueque = "";
	 public String oldNamePrefse = "";
	
	 public SQLite sql = null;
	 private static main ins;
	
	 public HashMap<UUID, String[]> BestOfSystem = new HashMap<UUID, String[]>();
	 
	 PositionManager getPos;
	 ManageRAMData ramMgr;
	 DBMainManager DBMgr;
	 ArenaState aState;
	 Teleport teleport;
	 //---
	
	
	//ACS:
	 public HashMap<String, ArrayList<String>> ACSArenas = new HashMap<>();
	 
	 public int ACSNextX = 0;

	 public int ACSDistX = 0;
	 public int ACSDistZ = 0;
	 public HashMap<String, Integer> ACSZ = new HashMap<>();
	 public HashMap<String, Integer> ACSX = new HashMap<>();
	

	 public boolean ACSEnabled = true;
	 public int ACSMin = 1;
	 public int ACSMax = 10;
	 public String ACSWorld = "1vs1-ACS";
	//----
	
	// Important Messages
	 public String prefix = "§7";
	 public String tournamentPrefix = "§bTurnier> §7";
	 public String noPerms = "§cDu hast nicht die benötigten Berechtigungen diesen Command zu nutzen!";
	 public String noPermsUseThisKit = "§cDieses Kit darfst du nicht benutzen!";
	 public String scoreBoardName = "1vs1";
	 public static String Version;
	// ----------------------------------

	// ---------

	// Locations and Stuff
	 public Location KitEditor1;
	 public Location KitEditor2;

	 public int minX, minY, minZ;
	 public int maxX, maxY, maxZ;
	// ----------

	// Config Daten
	 
	 // Other:
	  public Economy economy = null;
	 // ------

	 // BungeMode:
	  public boolean BungeeMode = false;
	  public String fallBackServer = "Lobby";
	 // ---------

	 // MySQL
	  public int MySQLPort = 3306;
	  public String MySQLDomain = "localhost";
	  public String MySQLDataBase = "1vs1";
	  public String MySQLUserName = "root";
	  public String MySQLPassword = "";
	 // ----------------------------

	 // Booleans
	  public boolean twoStepArenaReset = true;
	  public boolean useMySQL = false;
	  public boolean resetAllArenasOnStart = false;
	  public boolean saveStats = true;
	  public boolean loadChunks = false;
	  public boolean showArenaNamesSpectatorGUI = true;
	  public boolean cfgEconomy = true;
	  public boolean playAllBestOfGames = false;
	  public boolean saveInvs = true;
	  public boolean checkDatabaseConnection = true;
	  public boolean asyncMapReset = false;
	  public boolean useScoreboard = true;
	  public boolean updateNoti = true;
	  public boolean updateNotiJoin = true;
	  public boolean voidTeleport = false;
	  public boolean overrideJoinLeaveMsg = false;
	  public boolean saveOldScoreboard = true;
	  public boolean useBlockyMapReset = true;
	  public boolean silentQueue = true;
	  public boolean silentPrefVillager = true;
	  public boolean silentBlackDealer = true;
	  public boolean reduceStartDebugInfo = true;
	 // ------------------

	 //Ints and doubles
	  public int maxArenaEntitys = 16;
	  public int ArenaStartTimer = 3;
	  public int ArenaCheckTimer = 10;
	  public int NoFreeArenaMessageTimer = 5;
	  public int ArenaDestroy = 5;
	  public int ArenaBuild = 5;
	  public double Soupheal = 3.5;
	  public double ArenaRegionLeaveDamage = 1.5;
	  public int toggleCoolDown = 0;
	  public int minArenaBuildDistanceBottom = 1;
	  public int minArenaBuildDistanceTop = 2;
	  public int minArenaBuildDistanceWalls = 2;
	  public int autoEndmatch = 300;

	  public int ChallangerItemSlot = 1;
	  public int SpectatorItemSlot = 2;
	  public int BookItemSlot = 3;
	  public int TournamentItemSlot = 5;
	  public int RankItemSlot = 7;
	  public int SettingsItemSlot = 8;
	  public int LeaveItemSlot = 9;

	  public int ChallangerItemID = 276;
	  public int SpectatorItemID = 370;
	  public int TournamentItemID = 399;
	  public int SettingsItemID = 404;
	  public int LeaveItemID = 347;

	  public int maxBlocksPerTick = 500;
	  public int maxTeamSizeUser = 2;
	  public int maxTeamSizePremium = 4;
	  public int maxTeamSizeSpecial = 17;
	  public int economyNormalWinUser = 1;
	  public int economyNormalWinPremium = 2;
	  public int economyNormalWinSpecial = 4;
	  public int economyNormalWinAdmin = 8; 

	  public int economyTournamnetWinUser = 15;
	  public int economyTournamnetWinPremium = 30;
	  public int economyTournamnetWinSpecial = 60;
	  public int economyTournamnetWinAdmin = 100;

	  public int economyTournamnetKillUser = 2;
	  public int economyTournamnetKillPremium = 4;
	  public int economyTournamnetKillSpecial = 8;
	  public int economyTournamnetKillAdmin = 16;

	  public int maxTNTArenaGame = 64;
	  public long lastTimedStatReset = 0;
	  public long lastTimedStatReset24h = 0;
	  public long timedStatResetTime = 2592000000L;
	 //----------------

	 // MySQL Daten
	  public static String host = "localhost";
	  public static String port = "3306";
	  public static String database = "1vs1";
	  public static String username = "root";
	  public static String password = "";
	  public static Connection con;
	 // -----------

	

	

	// Book
	 public HashMap<Integer, String> book = new HashMap<>();
	// ----



	// ArrayLists:

	// Stuff

   	 public int defaultKitPrefs = 16;// 16
	 public String defaultPlayerQueuePrefs = "2 1";
	 public boolean spigot = true;
	 public boolean connected = false; 

	 public boolean useEconomy = false;
	 public boolean msgMeWhenIStupid = true;
	 public boolean useAsWhitelist = false; 

	 public int rank9 = 20;
     public int rank8 = 50;
     public int rank7 = 120; 
     public int rank6 = 150;
     public int rank5 = 380;
     public int rank4 = 450; 
     public int rank3 = 500; 
     public int rank2 = 750;
     public int rank1 = 1000;
	 public int rankPointsWins = 1;
	 public int rankPointsLose = -1; 

	 public ArrayList<EnchantingInventory> inventories = new ArrayList<EnchantingInventory>();
	 public ArrayList<Location> joinSigns = new ArrayList<Location>();
	 public ArrayList<String> blockedCommands = new ArrayList<>();

	// -----

	// Arena Stuff
	 public ArrayList<String> FreeArenas = new ArrayList<String>();
	 public ArrayList<String> ResetingArenas = new ArrayList<String>();
	 public ArrayList<String> protectedWordls = new ArrayList<String>();
	// -----------

	 public HashMap<String, ArrayList<ArenaTeamPlayer>> arenaTeams = new HashMap<>();
	 // -----------

	// Entity Stuff
	 public HashMap<String, ArrayList<Entity>> Entitys = new HashMap<String, ArrayList<Entity>>();
	 public HashMap<String, Integer> EntityCount = new HashMap<String, Integer>();
	 
	 public HashMap<UUID, ArmorStand> kitStands = new HashMap<>();
	 public HashMap<UUID, ArmorStand> topKitStands = new HashMap<>();
	 public HashMap<UUID, ArmorStand> kitStandsInfo = new HashMap<>();
	 public HashMap<UUID, UUID> kitStandsCon = new HashMap<>();
	 public HashMap<UUID, String> kitStandsKit = new HashMap<>();
	 public HashMap<UUID, String> kitStandsName = new HashMap<>();
	// ------------

	// Arena Stuff
	 public HashMap<String, String> ArenaKit = new HashMap<String, String>();
	 public HashMap<String, ArrayList<Player>> ArenaPlayersP1 = new HashMap<String, ArrayList<Player>>();
	 public HashMap<String, ArrayList<Player>> ArenaPlayersP2 = new HashMap<String, ArrayList<Player>>();
	 public HashMap<String, Location> ArenaPos1 = new HashMap<String, Location>();
	 public HashMap<String, Location> ArenaPos2 = new HashMap<String, Location>();
	 public HashMap<String, Location> ArenaCorner1 = new HashMap<String, Location>();
	 public HashMap<String, Location> ArenaCorner2 = new HashMap<String, Location>();
	 public HashMap<String, Integer> tntArena = new HashMap<>();
	 public HashMap<String, BlockMapReset> resetMgrArena = new HashMap<>();
	 public HashMap<String, ArrayList<UUID>> allPlayersArenaP1 = new HashMap<>();
	 public HashMap<String, ArrayList<UUID>> allPlayersArenaP2 = new HashMap<>();
	// -----------
	



	// @EventHandler//TODO Muss noch richtig eingebaut werden!
	// public void onClick(PlayerInteractEvent e) {
	// if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType()
	// == Material.SKULL) {
	// GameProfile gpf = ((CraftPlayer)Bukkit.getPlayer("JHammer17")).getProfile();
	// Bukkit.broadcastMessage("" + gpf.getProperties());
	// Iterator<Property> iterator = gpf.getProperties().get( "textures"
	// ).iterator();
	//
	// if( iterator.hasNext() ) {
	// Property prop = iterator.next();
	// //String uuid = gpf.getId().toString();
	// //String name = gpf.getName();
	// String signature = prop.getSignature();
	// String value = prop.getValue();
	//
	// Bukkit.broadcastMessage("" + signature + " §a" + value);
	//
	// //Bukkit.getPlayer("JHammer17").getLocation().getBlock().setType(Material.SKULL);
	// //Bukkit.getPlayer("JHammer17").getLocation().getBlock().setData((byte) 3);
	//
	// ItemStack skull =
	// SkullXX.getSkull("eyJ0aW1lc3RhbXAiOjE0OTc3ODU3NjY0NTMsInByb2ZpbGVJZCI6IjE4M2IzYzY1NzZhMjQ5ZmFiMWE4NzE3OTY1NjVhOWQzIiwicHJvZmlsZU5hbWUiOiJ4anVsaWFueSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzU3ZTQxYjY3NjcyYWIzMDZhYmI3NjYyNWQ2M2JiMWZkOGMxZTUyNTY2ZDQ0ZTQ2NGJmNDFiYTgyYjYxNThkZCJ9fX0=");
	// Bukkit.getPlayer("JHammer17").getInventory().addItem(skull);
	// SkullPlacer.setBlock(e.getClickedBlock().getLocation(),
	// "eyJ0aW1lc3RhbXAiOjE0OTc4MTg4ODAwMDEsInByb2ZpbGVJZCI6IjA2OWE3OWY0NDRlOTQ3MjZhNWJlZmNhOTBlMzhhYWY1IiwicHJvZmlsZU5hbWUiOiJOb3RjaCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTExNmU2OWE4NDVlMjI3ZjdjYTFmZGRlOGMzNTdjOGM4MjFlYmQ0YmE2MTkzODJlYTRhMWY4N2Q0YWU5NCJ9LCJDQVBFIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWVjM2NhYmZhZWVkNWRhZmU2MWM2NTQ2Mjk3ZTg1M2E1NDdjMzllYzIzOGQ3YzQ0YmY0ZWI0YTQ5ZGMxZjJjMCJ9fX0=");
	//
	// }
	// }
	//
	// }

	 
	 
	 
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		ins = this;
		
	

		Bukkit.getScheduler().runTaskLater(this, new Runnable() {

			@Override
			public void run() {
				System.out.println(" __          __");
				System.out.println("/_ |        /_ |");
				System.out.println(" | |_   _____| |");
				System.out.println(" | \\ \\ / / __| |");
				System.out.println(" | |\\ V /\\__ \\ |");
				System.out.println(" |_| \\_/ |___/_|");

				System.out.println("----------[1vs1]----------");
				System.out.println("Lade Systeme...");
				System.out.println("Pruefe Version...");

				if (!isRightVersion()) {
					System.out.println("Fail! Diese Version ist nicht kompatibel!");

					System.out.println("Plugin wird abgeschaltet!");
					Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(getName()));
					return;
				}
				if (!isSpigot()) {
					System.out.println("Fail! Dieser Server verwendet kein Spigot!");
					System.out.println("Installiere bitte Spigot!");
					System.out.println("Plugin wird abgeschaltet!");
					Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin(getName()));
					return;
				}
				
				System.out.println("Ok! Pruefe Config...");
				defaultFile();//TODO
				new configMgr(ins).reloadData(false);
				
				if(!reduceStartDebugInfo) System.out.println("Ok! Regestriere Listener...");
				regListener();
				if(!reduceStartDebugInfo) System.out.println("Ok! Regestriere Commands...");
				regCommands();
				if(!reduceStartDebugInfo) System.out.println("Ok! Regestriere Methoden...");
				regMethoden();// OK
				if(!reduceStartDebugInfo) System.out.println("Ok! Regestriere Guide...");
				regGuide();
				if(!reduceStartDebugInfo) System.out.println("Ok! Regestriere Arenen...");
				regArena();
				if(!reduceStartDebugInfo) System.out.println("Ok! Lade Nachrichten...");
				if (!getPluginFile("language").exists()) createLanguage(false);
				if (!getPluginFile("book").exists()) createBook(false);
				if (!getPluginFile("scoreboard").exists()) createScoreboardFile(false);
				
				reloadBook();
				reloadScoreboardFile();

				if(!reduceStartDebugInfo) System.out.println("Ok! Pruefe, ob eine neue Version vefuegbar ist...");
				getNewVersion();
				if(!reduceStartDebugInfo) System.out.println("Ok! Alle Dateien geladen!");

				if(!reduceStartDebugInfo) System.out.println("Lösche alte ACS-Arenen");
				new Manager(ins).deleteACSArenas();

				if (resetAllArenasOnStart) {
					if(!reduceStartDebugInfo) System.out.println("Ok! Resete alle Arenen...");
					getRAMMgr().deleteRAMAll();
					new ResetMethoden(ins).resetAllArenas();
					
				} else {
					if(!reduceStartDebugInfo) System.out.println("Ok! Resete alle gebrauchten Arenen...");

					Bukkit.getScheduler().runTaskAsynchronously(ins, new Runnable() {

						@Override
						public void run() {

							int resetet = new ResetMethoden(ins).resetAllArenasUsed();
							if (resetet == 1)
								if(!reduceStartDebugInfo) System.out.println("Eine Arena wird zurückgesetzt!");
							else
								if(!reduceStartDebugInfo) System.out.println(resetet + " Arenen werden zurueckgesetzt!");

						}
					});
					
				}
				if(!reduceStartDebugInfo) System.out.println("Ok! Lade Kiteditor...");
				loadKitEdit.loadKitEditRegion();
				if(!reduceStartDebugInfo) System.out.println("Ok! Lade Warteschlange und Kit-Einstellungen...");

				spawnQueque.respawner();
				spawnQueque.despawnQuequeZombie();
				spawnQueque.spawnQuequeZombie();

				spawnPrefVillager.respawner();
				spawnPrefVillager.despawnPrefVillager();
				spawnPrefVillager.spawnNewPrefVillager();

				spawnBlackDealer.respawner();
				spawnBlackDealer.despawnBlackDealer();
				spawnBlackDealer.spawnBlackDealerE();
				
				
				if(!reduceStartDebugInfo) System.out.println("Ok! Pruefe auf Vault...");
				
				
				if(!reduceStartDebugInfo) System.out.println(" - Vault ist installiert!");
					if (getServer().getPluginManager().getPlugin("Vault") != null) { 
					
				 if (cfgEconomy) {
					
					
						
						if (checkEconomyPlugin()) {
							if(!reduceStartDebugInfo) System.out.println(" - Ein Economy Plugin ist installiert!");
							setupEconomy();
							useEconomy = true;
						} else {
							if(!reduceStartDebugInfo) System.out.println(" - Es ist kein Economy Plugin installiert!");
						}
					 
				} else {
					useEconomy = false;
				}
				 
				 if (checkPermPlugin()) {
					 if(!reduceStartDebugInfo) System.out.println(" - Ein Permissions Plugin ist installiert!");
						setupPermission();
						
					} else {
						if(!reduceStartDebugInfo) System.out.println(" - Es ist kein Permissions Plugin installiert!");
					}
			   } else {
				   if(!reduceStartDebugInfo) System.out.println("Vault ist nicht installiert!");
			  }

					if(!reduceStartDebugInfo) System.out.println("Ok! Checke und lade Arenen...");
				getAState().checkAllArenas();
				
				if(!reduceStartDebugInfo) System.out.println("Ok! Starte Core...");
				MainScheduler.startMainSchedule();
				
				if (useMySQL) {
					if(!reduceStartDebugInfo) System.out.println("Ok! Versuche eine Verbindung zur MySQL Datenbank aufzubauen...");

					new MySQLManager(ins);

					MySQLManager.connect();
					
					MySQLManager.checkConnect();

					checkData();
				} else {
					if(!reduceStartDebugInfo) System.out.println("Ok! Verbinde mit SQL Datenbank...");

					startSQLLite();
					
					checkData();

				}
				
				new BukkitRunnable() {
					
					@Override
					public void run() {
						addSQLRows();
						for (Player players : Bukkit.getOnlinePlayers()) {
							addUser(players);
							updatePrefs(players.getUniqueId());
						}
						
					}
				}.runTaskAsynchronously(ins);
				
				
				new SignMethods(ins).reloadJoinSigns();
				new SignMethods(ins).refreshJoinSigns();
				
				Version = getServerVersion();

				
				updateDefaultPref();

				if(!reduceStartDebugInfo) System.out.println("Ok! Starte Metrics...");
				startMetrics();
				if(!reduceStartDebugInfo) System.out.println("Ok! Spawne KitStands...");
				new KitStands(ins).spawnStands();
				if (BungeeMode) {
					if(!reduceStartDebugInfo) System.out.println("Ok! Teleportiere alle Spieler in die 1vs1 Lobby...");
					for (Player p : Bukkit.getOnlinePlayers())
						MainCommand.toggle1vs1(p, true, false);
				}

				

				if (ACSEnabled) {
					if(!reduceStartDebugInfo) System.out.println("Ok! Prüfe auf ACS-Welt");
					if (Bukkit.getWorld(ACSWorld) == null) {
						if(!reduceStartDebugInfo) System.out.println("Keine Welt gefunden... Erstelle neue!");
						Bukkit.createWorld(new WorldCreator(ACSWorld).type(WorldType.FLAT).generateStructures(false));
						Bukkit.getWorld(ACSWorld).setGameRuleValue("doMobSpawning", "false");
						Bukkit.getWorld(ACSWorld).setGameRuleValue("doDaylightCycle", "false");
						Bukkit.getWorld(ACSWorld).setTime(6000);
						for (Entity en : Bukkit.getWorld(ACSWorld).getEntities())
							if (!(en instanceof Player)) en.remove();
						if(!reduceStartDebugInfo) System.out.println("ACS-Welt erstellt!");
					}
					if(!reduceStartDebugInfo) System.out.println("Ok! ACS-Welt gefunden!");
					if(!reduceStartDebugInfo) System.out.println("Generiere ACS-Arenen");
					Manager mgr = new Manager(ins);
					mgr.generateBase(ACSMin, ACSMax);
				}
				if(!reduceStartDebugInfo) System.out.println("Ok! Checke, ob die zeitlichen Statistiken zurueckgesetzt werden muessen!");
				
				long current = System.currentTimeMillis();
				
				long saved24h = lastTimedStatReset24h;
				long saved30d = lastTimedStatReset;
				
				long dayLength = (long)86400000*(long)30;
				long toCheck = saved30d+dayLength;
				
				if(current>toCheck) {
					if(!reduceStartDebugInfo) System.out.println("30 Tage Statistiken werden Resetet...");
					
					YamlConfiguration cfg = defaultYML();
					cfg.set("config.lastTimedStatReset", (long)System.currentTimeMillis());
					try {
						timedStatResetTime = (long)System.currentTimeMillis();
						cfg.save(defaultFile());
						Bukkit.getScheduler().runTaskAsynchronously(ins, new Runnable() {
							
							@Override
							public void run() {
								getDBMgr().reset30DayStats();
								
							}
						});
					} catch (Exception e) {}
					
				} 
				
				dayLength = (long)86400000;
				toCheck = saved24h+dayLength;
				
				
				
				if(current>toCheck) {
					if(!reduceStartDebugInfo) System.out.println("24h Statistiken werden Resetet...");
					
					YamlConfiguration cfg = defaultYML();
					cfg.set("config.lastTimedStatReset24h", (long)System.currentTimeMillis());
					try {
						timedStatResetTime = (long)System.currentTimeMillis();
						cfg.save(defaultFile());
						Bukkit.getScheduler().runTaskAsynchronously(ins, new Runnable() {
							
							@Override
							public void run() {
								getDBMgr().reset24hStats();
								
							}
						});
					} catch (Exception e) {}
					
				} 
				
				
				System.out.println("Ok!");

				System.out.println("Alle Systeme wurden erfolgreich geladen!");
				System.out.println("----------[1vs1]----------");
				
				
				
			}
		}, 1);
	}

	public static main instance() {
		return ins;
	}

	private void addSQLRows() {
		if(!getDBMgr().checkAllRowsExists()) {
		 if(useMySQL) {
			PreparedStatement ps = null;
			try {
			 if(getDBMgr().getNotExistingRows().toLowerCase().contains("disabledmaps")) {
			  ps = MySQLManager.getConnection().prepareStatement("ALTER TABLE `1vs1Kits` ADD `DisabledMaps` LONGTEXT NOT NULL AFTER `DefaultKit`");
			  ps.executeUpdate();
			 }
			} catch (SQLException e) {}
							
			try {
			 if (getDBMgr().getNotExistingRows().toLowerCase().contains("rankpoints")) {
			  ps = MySQLManager.getConnection().prepareStatement("ALTER TABLE `1vs1Kits` ADD `RankPoints` LONGTEXT NOT NULL AFTER `DisabledMaps`");
			  ps.executeUpdate();
			 }			
			} catch (SQLException e) {}
			
			try {
			 if (getDBMgr().getNotExistingRows().toLowerCase().contains("fights30")) {
			  ps = MySQLManager.getConnection().prepareStatement("ALTER TABLE `1vs1Kits` ADD `Fights30` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("fightswon30")) {
				
								ps = MySQLManager.getConnection().prepareStatement(
										"ALTER TABLE `1vs1Kits` ADD `FightsWon30` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if(getDBMgr().getNotExistingRows().toLowerCase().contains("kit1plays")) {
								
								ps = MySQLManager.getConnection().prepareStatement(
										"ALTER TABLE `1vs1Kits` ADD `Kit1Plays` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit1plays30")) {
								
								ps = MySQLManager.getConnection().prepareStatement(
										"ALTER TABLE `1vs1Kits` ADD `Kit1Plays30` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit2plays")) {
								
								ps = MySQLManager.getConnection().prepareStatement(
										"ALTER TABLE `1vs1Kits` ADD `Kit2Plays` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit2plays30")) {
								
								ps = MySQLManager.getConnection().prepareStatement(
										"ALTER TABLE `1vs1Kits` ADD `Kit2Plays30` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit3plays")) {
								
								ps = MySQLManager.getConnection().prepareStatement(
										"ALTER TABLE `1vs1Kits` ADD `Kit3Plays` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit3plays30")) {
								
								ps = MySQLManager.getConnection().prepareStatement(
										"ALTER TABLE `1vs1Kits` ADD `Kit3Plays30` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit4plays")) {
								
								ps = MySQLManager.getConnection().prepareStatement(
										"ALTER TABLE `1vs1Kits` ADD `Kit4Plays` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit4plays30")) {
								
								ps = MySQLManager.getConnection().prepareStatement(
										"ALTER TABLE `1vs1Kits` ADD `Kit4Plays30` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit5plays")) {
								
								ps = MySQLManager.getConnection().prepareStatement(
										"ALTER TABLE `1vs1Kits` ADD `Kit5Plays` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit5plays30")) {
								
								ps = MySQLManager.getConnection().prepareStatement(
										"ALTER TABLE `1vs1Kits` ADD `Kit5Plays30` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit1plays24h")) {
								
								ps = MySQLManager.getConnection().prepareStatement(
										"ALTER TABLE `1vs1Kits` ADD `Kit1Plays24h` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit2plays24h")) {
								
								ps = MySQLManager.getConnection().prepareStatement(
										"ALTER TABLE `1vs1Kits` ADD `Kit2Plays24h` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit3plays24h")) {
								
								ps = MySQLManager.getConnection().prepareStatement(
										"ALTER TABLE `1vs1Kits` ADD `Kit3Plays24h` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit4plays24h")) {
								
								ps = MySQLManager.getConnection().prepareStatement(
										"ALTER TABLE `1vs1Kits` ADD `Kit4Plays24h` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit5plays24h")) {
								
								ps = MySQLManager.getConnection().prepareStatement(
										"ALTER TABLE `1vs1Kits` ADD `Kit5Plays24h` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
							
							if(ps != null) {
								try {
									ps.close();
								} catch (SQLException e) {}
							}
					} else {
						PreparedStatement ps = null;
						
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("disabledmaps")) {
								ps = Database.getCon().prepareStatement(
										"ALTER TABLE `KitDatabase` ADD `DisabledMaps` LONGTEXT NOT NULL AFTER `DefaultKit`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
							
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("rankpoints")) {
								ps = Database.getCon().prepareStatement(
										"ALTER TABLE `KitDatabase` ADD `RankPoints` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("fights30")) {
								ps = Database.getCon().prepareStatement(
										"ALTER TABLE `KitDatabase` ADD `Fights30` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("fightswon30")) {
				
								ps = Database.getCon().prepareStatement(
										"ALTER TABLE `KitDatabase` ADD `FightsWon30` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if(getDBMgr().getNotExistingRows().toLowerCase().contains("kit1plays")) {
								
								ps = Database.getCon().prepareStatement(
										"ALTER TABLE `KitDatabase` ADD `Kit1Plays` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit1plays30")) {
								
								ps = Database.getCon().prepareStatement(
										"ALTER TABLE `KitDatabase` ADD `Kit1Plays30` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit2plays")) {
								
								ps = Database.getCon().prepareStatement(
										"ALTER TABLE `KitDatabase` ADD `Kit2Plays` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit2plays30")) {
								
								ps = Database.getCon().prepareStatement(
										"ALTER TABLE `KitDatabase` ADD `Kit2Plays30` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit3plays")) {
								
								ps = Database.getCon().prepareStatement(
										"ALTER TABLE `KitDatabase` ADD `Kit3Plays` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit3plays30")) {
								
								ps = Database.getCon().prepareStatement(
										"ALTER TABLE `KitDatabase` ADD `Kit3Plays30` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit4plays")) {
								
								ps = Database.getCon().prepareStatement(
										"ALTER TABLE `KitDatabase` ADD `Kit4Plays` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit4plays30")) {
								
								ps = Database.getCon().prepareStatement(
										"ALTER TABLE `KitDatabase` ADD `Kit4Plays30` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit5plays")) {
								
								ps = Database.getCon().prepareStatement(
										"ALTER TABLE `KitDatabase` ADD `Kit5Plays` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit5plays30")) {
								
								ps = Database.getCon().prepareStatement(
										"ALTER TABLE `KitDatabase` ADD `Kit5Plays30` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit1plays24h")) {
								
								ps = Database.getCon().prepareStatement(
										"ALTER TABLE `KitDatabase` ADD `Kit1Plays24h` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit2plays24h")) {
								
								ps = Database.getCon().prepareStatement(
										"ALTER TABLE `KitDatabase` ADD `Kit2Plays24h` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit3plays24h")) {
								
								ps = Database.getCon().prepareStatement(
										"ALTER TABLE `KitDatabase` ADD `Kit3Plays24h` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit4plays24h")) {
								
								ps = Database.getCon().prepareStatement(
										"ALTER TABLE `KitDatabase` ADD `Kit4Plays24h` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
						try {
							if (getDBMgr().getNotExistingRows().toLowerCase().contains("kit5plays24h")) {
								
								ps = Database.getCon().prepareStatement(
										"ALTER TABLE `KitDatabase` ADD `Kit5Plays24h` LONGTEXT NOT NULL AFTER `DisabledMaps`");
								ps.executeUpdate();
							}
						} catch (SQLException e) {}
							
							if(ps != null) {
								try {
									ps.close();
								} catch (SQLException e) {}
							}
								
							
						
					}
				}

			

	}

	public void startMetrics() {
		Metrics metrics = new Metrics(this);
	
		metrics.addCustomChart(new Metrics.SingleLineChart("fights") {
			
			@Override
			public int getValue() {
				File file = new File("plugins/bStats/1vs1-Stats.yml");
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				
				return cfg.getInt("Stats.Fights");
			}
		});
		
	}

	public void updateDefaultPref() {
		getDBMgr().updatePrefDefault();
	}

	public void updatePrefs(UUID uuid) {
		getDBMgr().updatePref(uuid, "");
		getDBMgr().updatePref(uuid, "2");
		getDBMgr().updatePref(uuid, "3");
		getDBMgr().updatePref(uuid, "4");
		getDBMgr().updatePref(uuid, "5");
	}

	private void startSQLLite() {
		sql = new SQLite(this);
		sql.load();
	}

	private void checkData() {
		if (useMySQL) {
			Bukkit.getScheduler().runTaskLaterAsynchronously(this, new Runnable() {

				@Override
				public void run() {
					try {
						PreparedStatement ps = MySQLManager.getConnection()
								.prepareStatement("CREATE TABLE IF NOT EXISTS 1vs1Kits " + "(PlayerName VARCHAR(100)"
										+ ",UUID VARCHAR(100)" 
										+ ",KitInv LONGTEXT" 
										+ ",KitArmor LONGTEXT"
										+ ",Settings VARCHAR(150)" 
										+ ",QuequePrefs VARCHAR(150)" 
										+ ",KitInv2 LONGTEXT"
										+ ",KitArmor2 LONGTEXT" 
										+ ",KitSettings2 LONGTEXT" 
										+ ",KitInv3 LONGTEXT"
										+ ",KitArmor3 LONGTEXT" 
										+ ",KitSettings3 LONGTEXT" 
										+ ",KitInv4 LONGTEXT"
										+ ",KitArmor4 LONGTEXT" 
										+ ",KitSettings4 LONGTEXT" 
										+ ",KitInv5 LONGTEXT"
										+ ",KitArmor5 LONGTEXT" 
										+ ",KitSettings5 LONGTEXT" 
										+ ",Fights LONGTEXT"
										+ ",FightsWon LONGTEXT" 
										+ ",DefaultKit LONGTEXT" 
										+ ",DisabledMaps LONGTEXT" 
										+ "`RankPoints` longtext," 
							            + "`Fights30` longtext," 
							            + "`FightsWon30` longtext," 
							            + "`Kit1Plays` longtext," 
							            + "`Kit1Plays30` longtext," 
							            +  "`Kit2Plays` longtext," 
							            + "`Kit2Plays30` longtext,"
							            + "`Kit3Plays` longtext," 
							            + "`Kit3Plays30` longtext," 
							            + "`Kit4Plays` longtext," 
							            + "`Kit4Plays30` longtext," 
							            + "`Kit5Plays` longtext," 
							            + "`Kit5Plays30` longtext," 
							            + "`Kit1Plays24h` longtext," 
							            + "`Kit2Plays24h` longtext," 
							            + "`Kit3Plays24h` longtext," 
							            + "`Kit4Plays24h` longtext," 
							            + "`Kit5Plays24h` longtext)");
						ps.executeUpdate();
						//
						ps = MySQLManager.getConnection().prepareStatement(
								"ALTER TABLE 1vs1Kits ENGINE=MyISAM ROW_FORMAT=COMPRESSED  KEY_BLOCK_SIZE=8");
						ps.executeUpdate();
						ps = MySQLManager.getConnection().prepareStatement(
								"ALTER TABLE `1vs1kits` ADD `DisabledMaps` LONGTEXT NOT NULL AFTER `DefaultKit`");
						ps.executeUpdate();
					} catch (SQLException e) {
					}
					try {
						PreparedStatement ps = MySQLManager.getConnection()
								.prepareStatement("ALTER TABLE `1vs1kits` ADD `RankPoints` LONGTEXT NOT NULL");
						ps.executeUpdate();
					} catch (Exception e) {
					}
				}
			}, 0);
		} else {
			Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {

				@Override
				public void run() {
					try {
						PreparedStatement ps = sql.getSQLConnection().prepareStatement("ALTER TABLE " + Database.table
								+ " ADD `DisabledMaps` LONGTEXT NOT NULL AFTER `DefaultKit`");
						ps.executeUpdate();

					} catch (Exception e) {
					}
					try {
						PreparedStatement ps = sql.getSQLConnection()
								.prepareStatement("ALTER TABLE " + Database.table + " ADD `RankPoints` NOT NULL LONGTEXT");
						ps.executeUpdate();
					} catch (Exception e) {
					}
				}

			});

		}
	}

	@Override
	public void onDisable() {
		System.out.println("----------[1vs1]----------");
		System.out.println("Telepotiere Spieler aus Arenen und co...");
		for (Player players : Bukkit.getOnlinePlayers()) {
			if (OneVSOnePlayers.containsKey(players.getUniqueId()) && OneVSOnePlayers.get(players.getUniqueId()).getSpecator() != null) {
				players.setAllowFlight(false);
				players.setFlying(false);
			}
			if (this.OneVSOnePlayers.containsKey(players.getUniqueId())) {
				
				MainCommand.toggle1vs1(players, false, true);
			}
		}
		System.out.println("Ok! Schliesse Verbindung mit (My)SQL-Datebank...");
		if (useMySQL) {
			try {
				MySQLManager.getConnection().close();
			} catch (SQLException e) {
				System.out.println("Error while closing MySQL Connection");
			}
			MySQLManager.disconnect();
		} else {

			if (sql != null && sql.getSQLConnection() != null) {
				try {
					sql.getSQLConnection().close();
					sql = null;
				} catch (SQLException e) {
				}
			}

		}

		System.out.println("Ok! Loesche Enchant Inventare...");
		for (EnchantingInventory ei : this.inventories)
			ei.setItem(1, null);
		this.inventories = null;

		System.out.println("Ok! Spieler werden zurückgesetzt");
		
		for(OneVsOnePlayer players : OneVSOnePlayers.values()) {
			players.getPlayer().closeInventory();
			players.getPlayer().spigot().setCollidesWithEntities(true);
		}
		
		
		
		System.out.println("Ok! Despawne QuequeZombie und Settings-Villager...");
		spawnPrefVillager.despawnPrefVillager();
		spawnQueque.despawnQuequeZombie();
		spawnBlackDealer.despawnBlackDealer();
		System.out.println("Ok! Despawne KitStands...");
		new KitStands(this).removeCurrent();
		System.out.println("Ok! Letzter Cleanup...");
		

		System.out.println("Ok!");
		System.out.println("Alles erledigt fahre Plugin herunter... zZZZZ");

		System.out.println("----------[1vs1]----------");

	}

	

	private void regCommands() {
		new Command_Manager(this);
		new MainCommand(this);
		getCommand("1vs1").setExecutor(new MainCommand(this));
		new Endmatch(this);
		new Kit(this);
		new Surrender(this);
		new Spec(this);
		new Stats(this);
		new Tournament(this);
		new Join(this);
		new Create(this);
		new Leave(this);
		new Win(this);
		new Team(this);
		new TLeave(this);
		new Start(this);
		new KitStats(this);
	}

	private void regListener() {
		new Region_Edit(this);
		new Kit_Editor_Move(this);
		new Blocked_Events(this);
		new Warteschlange_Manager(this);
		new KillEvent(this);
		new SpectateArenaItemManager(this);
		new SignManager(this);
		new PlayerQueqeChangeSettings(this);
		new Multi_Kit_Manager(this);
		new ChallangeManager(this);
		new Tournament_Creator_InvManager(this);
		new Tournament_Events(this);
		new DisableMapsManager(this);
		new TeamManager(this);
		new KitStandsInvManger(this);
		new LobbyTournamentItemManager(this);

		msgs = new NewMsgLoader(this);
		msgs.reloadAllMessages();
	}

	public void regGuide() {
		new Inv_Opener(this);
		new BaseInv(this);
		new LayoutInv(this);
		new ArenaInv(this);
		new OtherInv(this);
		new OtherSignInv(this);
		new OtherSkullInv(this);
	}

	private void regArena() {
		new CopyArena(this);
		new DeleteArena(this);

		new ArenaJoin(this);
		new ArenaEvents(this);
		new RemoveEntitys(this);
		new Preferences_Manager(this);
		new openArenaCheckInv(this);
		new Spectator_Events(this);
		new ArenaMenu(this);
		new BlackDealerInvCreator(this);
		new BlackDealerInvManager(this);

	}

	private void regMethoden() {
		new loadKitEdit(this);
		new spawnQueque(this);
		new MainScheduler(this);
		new FightEnd(this);
		new FightEndTeam(this);
		new spawnPrefVillager(this);
		new SpectateArena(this);
		new SignMethods(this);
		new QuequePrefsMethods(this);
		new MessageReplacer(this);
		new KitMessages(this);
		new getItems(this);
		new ScoreBoardManager(this);
		new QueueManager(this);
		new spawnBlackDealer(this);
	}

	public YamlConfiguration defaultYML() {
		if (defaultFile() != null) {
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(defaultFile());
			return cfg;
		} else
			return null;
	}

	public File defaultFile() {
		
		File file = new File("plugins/1vs1/config.yml");
		
		if (!file.exists()) {
			
			try {

				file.createNewFile();
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

				file = new File("plugins/1vs1/config.yml");
				cfg = YamlConfiguration.loadConfiguration(file);

				generateDefaultFileConfig(false);

				cfg.save(file);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		return file;
	}

	public void generateDefaultFileConfig(boolean reset) {
		
		if (!reset) {
			File file = new File("plugins/1vs1/config.yml");
			if (!file.exists())
				saveResource("config.yml", reset);

		} else
			saveResource("config.yml", reset);
	}
	
	public File getPluginFile(String Pfad) {
		File file = new File("plugins/1vs1/" + Pfad + ".yml");
		if (!file.exists() || file == null) {
			try {
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				cfg.save(file);
			} catch (IOException e) {
				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), "File: " + Pfad);
				e.printStackTrace();
				return null;
			}
		}
		return file;
	}

	public boolean existFile(String Pfad) {
		File file = new File("plugins/1vs1/" + Pfad + ".yml");
		try {
			if (!file.exists() || file == null)
				return false;
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public YamlConfiguration getYaml(String Pfad) {
		if (getPluginFile(Pfad) != null) {
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(getPluginFile(Pfad));
			return cfg;
		}
		return null;
	}

	private boolean isRightVersion() {
		if (getVersion().equalsIgnoreCase("1.7"))return false;
		return true;
	}

	public static String getVersion() {
		String Version = Bukkit.getVersion();
		if (Version.contains("1.7"))
			return "1.7";
		if (Version.contains("1.8") || Version.contains("1.8.1") || Version.contains("1.8.2")
				|| Version.contains("1.8.3") || Version.contains("1.8.4") || Version.contains("1.8.5")
				|| Version.contains("1.8.6") || Version.contains("1.8.7") || Version.contains("1.8.8")
				|| Version.contains("1.8.9"))
			return "1.8";
		else return "Weder 1.7 noch 1.8";
	}

	private boolean isSpigot() {
		String Version = Bukkit.getVersion();
		String[] splitVersion1 = Version.split("-");

		if (splitVersion1.length >= 2)
			if (splitVersion1[1].toLowerCase().contains("spigot") || splitVersion1[1].toLowerCase().contains("paperspigot"))
				return true;
		return false;
	}

	public void createLanguage(boolean overWrite) {
		if (overWrite) {
			try {
				InputStream is = null;
				OutputStream os = null;
				try {
					is = new FileInputStream(getPluginFile("language"));
					os = new FileOutputStream(getPluginFile("old_language"));
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0)
						os.write(buffer, 0, length);
				} finally {
					is.close();
					os.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			saveResource("language.yml", true);
		} else {
			saveResource("language.yml", false);
		}
	}

	public void createBook(boolean overWrite) {
		if (overWrite) {
			try {
				InputStream is = null;
				OutputStream os = null;
				try {
					is = new FileInputStream(getPluginFile("book"));
					os = new FileOutputStream(getPluginFile("old_book"));
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0)
						os.write(buffer, 0, length);
				} finally {
					is.close();
					os.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			saveResource("book.yml", true);
		} else {
			saveResource("book.yml", false);
		}
	}

	public void createScoreboardFile(boolean overWrite) {
		if (overWrite) {
			try {
				InputStream is = null;
				OutputStream os = null;
				try {
					is = new FileInputStream(getPluginFile("scoreboard"));
					os = new FileOutputStream(getPluginFile("old_scoreboard"));
					byte[] buffer = new byte[1024];
					int length;
					while ((length = is.read(buffer)) > 0)
						os.write(buffer, 0, length);
				} finally {
					is.close();
					os.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			saveResource("scoreboard.yml", true);
		} else {
			saveResource("scoreboard.yml", false);
		}
	}
	
	@SuppressWarnings("static-access")
	public void reloadBook() {
		File file = getPluginFile("book");

		YamlConfiguration cfg = new YamlConfiguration().loadConfiguration(file);

		if (file.exists()) {

			try {
				cfg.loadFromString(Files.toString(file, Charset.forName("UTF-8")));
			} catch (InvalidConfigurationException e) {
				getLogger().log(Level.WARNING, "A error eccourd while loading language file!", e);
				return;
			} catch (Exception e) {
				getLogger().log(Level.WARNING, "A error eccourd while loading language file!", e);
				return;
			}

		}
		if (cfg.getConfigurationSection("Book") != null) {
			int pageN = 1;

			for (String page : cfg.getConfigurationSection("Book").getKeys(false)) {

				String p = cfg.getString("Book." + page);
				if (p == null || p.equalsIgnoreCase(""))
					p = null;
				book.put(pageN, p);
				pageN++;
			}
		} else {
			createBook(true);
			reloadBook();
		}
	}

	@SuppressWarnings("static-access")
	public void reloadScoreboardFile() {
		File file = getPluginFile("scoreboard");

		YamlConfiguration cfg = new YamlConfiguration().loadConfiguration(file);

		if (file.exists()) {

			try {
				cfg.loadFromString(Files.toString(file, Charset.forName("UTF-8")));
			} catch (InvalidConfigurationException e) {
				getLogger().log(Level.WARNING, "A error eccourd while loading language file!", e);
				return;
			} catch (Exception e) {
				getLogger().log(Level.WARNING, "A error eccourd while loading language file!", e);
				return;
			}

		}
		if (cfg.getConfigurationSection("Groups") == null) {
			cfg.set("Groups.exmp.prefix", "&f");
			cfg.set("Groups.exmp.suffix", "&f");
			cfg.set("Groups.exmp.permission", "1vs1.sb.exmp");
			cfg.set("Groups.exmp.group", "exmpgroup");
			cfg.set("Groups.exmp.default", true);
			
			try {
				cfg.save(file);
			} catch (IOException e) {}
		} 
	}
	
	public void sendNoPermsMessage(Player p) {
		p.sendMessage(noPerms);
	}

	public String getServerVersion() {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		return version;
	}

	public void addUser(final Player p) {
		Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {

			@Override
			public void run() {
				if (useMySQL) {
					if (!MySQLManager.isUserExists(p.getUniqueId())) {

						MySQLManager.addUser(p.getUniqueId(), p.getName());
						MySQLManager.updatePref(p.getUniqueId(), "");
						MySQLManager.updatePref(p.getUniqueId(), "2");
						MySQLManager.updatePref(p.getUniqueId(), "3");
						MySQLManager.updatePref(p.getUniqueId(), "4");
						MySQLManager.updatePref(p.getUniqueId(), "5");
						MySQLManager.setKit(p.getUniqueId(), MySQLManager.getDefault(false), false, "");
						MySQLManager.setKit(p.getUniqueId(), MySQLManager.getDefault(true), true, "");

						for (PlayerPrefs pref : PlayerPrefs.values()) {
							String prefs = "" + MySQLManager.getPrefDefault(Preferences_Manager.getPrefID(pref));

							boolean state = false;
							if (prefs.equalsIgnoreCase("true") || prefs.equalsIgnoreCase("t")) {
								state = true;
							}

							MySQLManager.setPref(p.getUniqueId(), Preferences_Manager.getPrefID(pref), state, "");
						}
					}
				} else {
					if (!Database.isUserExists(p.getUniqueId())) {
						Database.addUser(p.getUniqueId(), p.getName());
						Database.updatePref(p.getUniqueId(), "");
						Database.updatePref(p.getUniqueId(), "2");
						Database.updatePref(p.getUniqueId(), "3");
						Database.updatePref(p.getUniqueId(), "4");
						Database.updatePref(p.getUniqueId(), "5");
					}
				}
			}
		});
	}

	public void getNewVersion() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				String resource = "30355";
				String spigotVersion = "";

				try {
					HttpURLConnection con = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php")
							.openConnection();
					con.setDoOutput(true);
					con.setRequestMethod("POST");
					con.getOutputStream()
							.write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + resource)
									.getBytes("UTF-8"));
					String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();

					if (version.length() <= 20)
						spigotVersion = version;
				} catch (Exception ex) {
					getLogger().info("Failed to get Version String...");
					return;
				}

				if (updateNoti) {
					if (!getDescription().getVersion().contains(spigotVersion)) {
						for (Player players : Bukkit.getOnlinePlayers()) {
							if (players.hasPermission("1vs1.*") || players.hasPermission("1vs1.seeUpdate")
									|| players.hasPermission("1vs1.Admin")) {
								players.sendMessage("§7§m§l=============================================");
								players.sendMessage("");
								players.sendMessage("§7Eine neue Version des §6§l1vs1-Plugins §r§7von JHammer17");
								players.sendMessage("§7steht nun zum §6§lDownload §r§7bereit!");
								players.sendMessage("");
								players.sendMessage("");
								players.sendMessage("§7Download:");
								players.sendMessage("§6https://www.spigotmc.org/resources/30355");
								players.sendMessage("");
								players.sendMessage("§7Neue Version: §6§l" + spigotVersion);
								players.sendMessage("");
								players.sendMessage("§7§m§l=============================================");
							}
						}
					}
				}

				if (!getDescription().getVersion().contains(spigotVersion)) {
					Bukkit.getScheduler().runTaskLater(ins, new Runnable() {

						@Override
						public void run() {
							getLogger().info("Eine neue Version des Timolia-1vs1 Plugins ist verfuegbar!");
						}
					}, 20);
				}
				
			}
		}.runTaskAsynchronously(this);
		
	}

	private boolean checkEconomyPlugin() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		return economyProvider != null ? true : false;
	}
	
	private boolean checkPermPlugin() {
		RegisteredServiceProvider<Permission> permProvider = getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.permission.Permission.class);
		return permProvider != null ? true : false;
	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null)
			economy = economyProvider.getProvider();
		return (economy != null);
	}
	
	private boolean setupPermission() {
		RegisteredServiceProvider<Permission> permProvider = getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.permission.Permission.class);
		
		if (permProvider != null)
			permissionmgr = permProvider.getProvider();//TODO
		return (permissionmgr != null);
	}

	public PositionManager getPositions() {
		if (getPos == null)
			getPos = new PositionManager(this);
		return this.getPos;
	}

	public ManageRAMData getRAMMgr() {
		if (ramMgr == null)
			ramMgr = new ManageRAMData(this);
		return this.ramMgr;
	}

	public DBMainManager getDBMgr() {
		if (DBMgr == null)
			DBMgr = new DBMainManager(this);
		return this.DBMgr;
	}

	public ArenaState getAState() {
		if (aState == null)
			aState = new ArenaState(this);
		return this.aState;
	}

	public Teleport getTeleporter() {
		if (teleport == null)
			teleport = new Teleport(this);
		return this.teleport;
	}

	public int calcPerc(int a, int max) {
		double perc = ((double) a / (double) max) * 100;
		return (int) perc;
	}
	
	public YamlConfiguration getScoreboardData() {
		return getYaml("scoreboard");
	}

	public String colorByPercent(int a, int max, final String msg, String defaultColor, String colorColor) {
		if(a == max) return new String(colorColor + msg);
		int ln = new String(msg).length();
		String[] split = new String(msg).split("");
		
		double percent =  (((double)a/(double)max)*100.0);
		
		double toColor = ((double)ln/(double)100)*percent;
		
		
		if(toColor >= split.length) return new String(colorColor + msg);
		
		split[(int) toColor] = defaultColor + split[(int) toColor];
		
		boolean first = true;
		StringBuilder builder = new StringBuilder();
		for(String str : split) {
			if(first) {
				builder.append(colorColor);
				first = false;
			}
			builder.append(str);
		}
		return builder.toString();
	}
	
	public OneVsOnePlayer getOneVsOnePlayer(Player p) {
		if(p == null) return new OneVsOnePlayer(p);
		if(OneVSOnePlayers.containsKey(p.getUniqueId())) {
			return OneVSOnePlayers.get(p.getUniqueId());
		}
		
		return new OneVsOnePlayer(p);
	}
	
	public OneVsOnePlayer getOneVsOnePlayer(UUID uuid) {
		if(Bukkit.getPlayer(uuid) == null) return new OneVsOnePlayer(Bukkit.getPlayer(uuid));
		if(OneVSOnePlayers.containsKey(Bukkit.getPlayer(uuid).getUniqueId())) 
			return OneVSOnePlayers.get(Bukkit.getPlayer(uuid).getUniqueId());
		return new OneVsOnePlayer(Bukkit.getPlayer(uuid));
	}
	
	public int getOneVsOnePlayerSize() {
		return OneVSOnePlayers.size();
	}
	
	public void removePlayer(UUID uuid) {
		while(OneVSOnePlayers.containsKey(uuid)) OneVSOnePlayers.remove(uuid);
	}
	
	public boolean isInOneVsOnePlayers(UUID uuid) {
		
		return OneVSOnePlayers.containsKey(uuid);
	}
	
	public void addPlayer(UUID uuid) {
		
		if(Bukkit.getPlayer(uuid) != null) {
			OneVsOnePlayer player = new OneVsOnePlayer(Bukkit.getPlayer(uuid));
			player.init();
			OneVSOnePlayers.put(uuid, player);
		}
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<UUID, OneVsOnePlayer> getOneVsOnePlayersCopy() {
		return (HashMap<UUID, OneVsOnePlayer>) OneVSOnePlayers.clone();
	}
	
}