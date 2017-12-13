package de.OnevsOne.DataBases.SQLite;

import de.OnevsOne.States.PlayerBestOfsPrefs;
import de.OnevsOne.States.PlayerQuequePrefs;
import de.OnevsOne.main;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.Bukkit;

public class Database {
	
	public static SQLite sql = null;
	public static main plugin;
	public static Connection connection;
	public static String table = "KitDatabase";
	public int tokens = 0;

	public Database(main instance) {
		plugin = instance;
	}

	//public abstract void load();

	public String getDBName() {
		return table;
	}

	
	
	public static void useCommand(String cmd) {
		try {
			PreparedStatement ps = connection.prepareStatement(cmd);
			ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection getCon() {
		return connection;
	}
	
	public static void initialize() {
		
		
		connection = plugin.sql.getSQLConnection();
		try {
			connection = plugin.sql.getSQLConnection();
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " LIMIT 1");
			ResultSet rs = ps.executeQuery();
			close(ps, rs);
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Unable to retreive connection", ex);
		}
	}

	public static boolean isConnected() {
		if (!plugin.checkDatabaseConnection) return true;
		if (plugin.sql.getSQLConnection() == null) return false;
		try {
			Connection conn = plugin.sql.getSQLConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM " + table);
			ps.executeQuery();
			closeStatments(ps, conn);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void close(PreparedStatement ps, ResultSet rs) {
		try {
			if (ps != null) ps.close();
			if (rs != null) rs.close();
		} catch (SQLException ex) {
			Error.close(plugin, ex);
		}
	}

	public static boolean isUserExists(UUID uuid) {
		Connection conn = connection;
		PreparedStatement ps = null;
		
		try {
			uuid.toString();
		} catch (Exception e) {
			return false;
		}
		
		try {
			if (sql == null) {
				sql = new SQLite(plugin);
			}
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("SELECT PlayerName FROM " + table + " WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			
			boolean data = rs.next();
			closeStatments(ps, conn);
			return data;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			closeStatments(ps, conn);
		}
		return false;
	}

	public static boolean isDefaultExists() {
		Connection conn = connection;
		PreparedStatement ps = null;
		try {
			if (sql == null) {
				sql = new SQLite(plugin);
			}
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("SELECT PlayerName FROM " + table + " WHERE UUID = ?");
			ps.setString(1, "default");
			ResultSet rs = ps.executeQuery();
			boolean data = rs.next();
			closeStatments(ps, conn);
			return data;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			closeStatments(ps, conn);
		}
		return false;
	}

	public static boolean isNameRegistered(String Name) {
		Connection conn = connection;
		PreparedStatement ps = null;
		try {
			if (sql == null) {
				sql = new SQLite(plugin);
			}
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("SELECT UUID FROM " + table + " WHERE PlayerName = ?");
			ps.setString(1, Name);

			
			
			ResultSet rs = ps.executeQuery();
			
			
			if(rs.next()) {
				String str = rs.getString("UUID");
				//closeStatments(ps, conn);
				if(str.startsWith("-") || str.startsWith("CUSTOM")) return false;
			} else {
				return false;
			}
			
			return true;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			closeStatments(ps, conn);
		}
		return false;
	}

	public static void addUser(UUID uuid, String name) {
		StringBuilder defaultKitPrefsBuilder = new StringBuilder();
		for (int i = 0; i < plugin.defaultKitPrefs; i++) {
			if (i == 0) {
				defaultKitPrefsBuilder.append("f");
			} else {
				defaultKitPrefsBuilder.append(" f");
			}
		}
		if (isDefaultExists()) {
			Connection conn = connection;
			PreparedStatement ps = null;
			try {
				String[] defaultKit = getRawPrefDefault();

				boolean first = true;
				StringBuilder Data = new StringBuilder();
				for (int i = 0; i < defaultKit.length; i++) {
					if (first) {
						first = false;
						Data = Data.append(defaultKit[i]);
					} else {
						Data.append(" ");
						Data.append(defaultKit[i]);
					}
				}

				if (sql == null) {
					sql = new SQLite(plugin);
				}
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("INSERT INTO " + table + " (" + "PlayerName" 
						+ ",UUID" 
						+ ",KitInv"
						+ ",KitArmor" 
						+ ",Settings" 
						+ ",QuequePrefs" 
						+ ",KitInv2" 
						+ ",KitArmor2" 
						+ ",KitSettings2"
						+ ",KitInv3" 
						+ ",KitArmor3" 
						+ ",KitSettings3" 
						+ ",KitInv4" 
						+ ",KitArmor4" 
						+ ",KitSettings4"
						+ ",KitInv5" 
						+ ",KitArmor5" 
						+ ",KitSettings5" 
						+ ",Fights" 
						+ ",FightsWon" 
						+ ",DefaultKit"
						+ ",DisabledMaps"//22
						+ ",Fights30"
						+ ",FightsWon30"
						+ ",Kit1Plays"  
						+ ",Kit1Plays30"  
						+ ",Kit2Plays" 
						+ ",Kit2Plays30" 
						+ ",Kit3Plays"  
						+ ",Kit3Plays30"  
						+ ",Kit4Plays" 
						+ ",Kit4Plays30" 
						+ ",Kit5Plays"  
						+ ",Kit5Plays30" 
						+ ",Kit1Plays24h" 
						+ ",Kit2Plays24h" 
						+ ",Kit3Plays24h" 
						+ ",Kit4Plays24h"  
						+ ",Kit5Plays24h"//38
						+ ")" 
						+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				ps.setString(1, name);
				ps.setString(2, uuid.toString());
				ps.setString(3, getKitDefault(false));
				ps.setString(4, getKitDefault(true));
				ps.setString(5, Data.toString());
				ps.setString(6, "2 1");
				ps.setString(7, "");
				ps.setString(8, "");
				ps.setString(9, "" + defaultKitPrefsBuilder.toString());
				ps.setString(10, "");
				ps.setString(11, "");
				ps.setString(12, "" + defaultKitPrefsBuilder.toString());
				ps.setString(13, "");
				ps.setString(14, "");
				ps.setString(15, "" + defaultKitPrefsBuilder.toString());
				ps.setString(16, "");
				ps.setString(17, "");
				ps.setString(18, "" + defaultKitPrefsBuilder.toString());
				ps.setString(19, "0");
				ps.setString(20, "0");
				ps.setString(21, "1");
				ps.setString(22, "1");
				
				ps.setString(23, "0");
				ps.setString(24, "0");
				ps.setString(25, "0");
				ps.setString(26, "0");
				ps.setString(27, "0");
				ps.setString(28, "0");
				ps.setString(29, "0");
				ps.setString(30, "0");
				ps.setString(31, "0");
				ps.setString(32, "0");
				ps.setString(33, "0");
				ps.setString(34, "0");
				ps.setString(35, "0");
				ps.setString(36, "0");
				ps.setString(37, "0");
				ps.setString(38, "0");
				ps.setString(39, "0");
				ps.executeUpdate();
				closeStatments(ps, conn);
				return;
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
			} finally {
				closeStatments(ps, conn);
			}
			return;
		}
		Connection conn = connection;
		PreparedStatement ps = null;
		try {
			if (sql == null) {
				sql = new SQLite(plugin);
			}
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("INSERT INTO " + table + " (" + "PlayerName" 
					+ ",UUID" 
					+ ",KitInv"
					+ ",KitArmor" 
					+ ",Settings" 
					+ ",QuequePrefs" 
					+ ",KitInv2" 
					+ ",KitArmor2" 
					+ ",KitSettings2"
					+ ",KitInv3" 
					+ ",KitArmor3" 
					+ ",KitSettings3" 
					+ ",KitInv4" 
					+ ",KitArmor4" 
					+ ",KitSettings4"
					+ ",KitInv5" 
					+ ",KitArmor5" 
					+ ",KitSettings5" 
					+ ",Fights" 
					+ ",FightsWon" 
					+ ",DefaultKit"
					+ ",DisabledMaps"//22
					+ ",Fights30"
					+ ",FightsWon30"
					+ ",Kit1Plays"  
					+ ",Kit1Plays30"  
					+ ",Kit2Plays" 
					+ ",Kit2Plays30" 
					+ ",Kit3Plays"  
					+ ",Kit3Plays30"  
					+ ",Kit4Plays" 
					+ ",Kit4Plays30" 
					+ ",Kit5Plays"  
					+ ",Kit5Plays30" 
					+ ",Kit1Plays24h" 
					+ ",Kit2Plays24h" 
					+ ",Kit3Plays24h" 
					+ ",Kit4Plays24h"  
					+ ",Kit5Plays24h"//38
					+ ")" 
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			ps.setString(1, name);
			ps.setString(2, uuid.toString());
			ps.setString(3, "");
			ps.setString(4, "");
			ps.setString(5, "");
			ps.setString(6, "2 1");
			ps.setString(7, "");
			ps.setString(8, "");
			ps.setString(9, "" + defaultKitPrefsBuilder.toString());
			ps.setString(10, "");
			ps.setString(11, "");
			ps.setString(12, "" + defaultKitPrefsBuilder.toString());
			ps.setString(13, "");
			ps.setString(14, "");
			ps.setString(15, "" + defaultKitPrefsBuilder.toString());
			ps.setString(16, "");
			ps.setString(17, "");
			ps.setString(18, "" + defaultKitPrefsBuilder.toString());
			ps.setString(19, "0");
			ps.setString(20, "0");
			ps.setString(21, "1");
			ps.setString(22, "1");
			
			ps.setString(23, "0");
			ps.setString(24, "0");
			ps.setString(25, "0");
			ps.setString(26, "0");
			ps.setString(27, "0");
			ps.setString(28, "0");
			ps.setString(29, "0");
			ps.setString(30, "0");
			ps.setString(31, "0");
			ps.setString(32, "0");
			ps.setString(33, "0");
			ps.setString(34, "0");
			ps.setString(35, "0");
			ps.setString(36, "0");
			ps.setString(37, "0");
			ps.setString(38, "0");
			ps.setString(39, "0");
			ps.executeUpdate();
			closeStatments(ps, conn);
			return;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			closeStatments(ps, conn);
		}
	}

	public static void addDefault() {
		StringBuilder defaultKitPrefsBuilder = new StringBuilder();
		for (int i = 0; i < plugin.defaultKitPrefs; i++) {
			if (i == 0) {
				defaultKitPrefsBuilder.append("f");
			} else {
				defaultKitPrefsBuilder.append(" f");
			}
		}
		Connection conn = connection;
		PreparedStatement ps = null;
		try {
			if (sql == null) {
				sql = new SQLite(plugin);
			}
			conn = plugin.sql.getSQLConnection();
			
			ps = conn.prepareStatement("INSERT INTO " + table + " (" + "PlayerName" 
					+ ",UUID" 
					+ ",KitInv"
					+ ",KitArmor" 
					+ ",Settings" 
					+ ",QuequePrefs" 
					+ ",KitInv2" 
					+ ",KitArmor2" 
					+ ",KitSettings2"
					+ ",KitInv3" 
					+ ",KitArmor3" 
					+ ",KitSettings3" 
					+ ",KitInv4" 
					+ ",KitArmor4" 
					+ ",KitSettings4"
					+ ",KitInv5" 
					+ ",KitArmor5" 
					+ ",KitSettings5" 
					+ ",Fights" 
					+ ",FightsWon" 
					+ ",DefaultKit"
					+ ",DisabledMaps"//22
					+ ",Fights30"
					+ ",FightsWon30"
					+ ",Kit1Plays"  
					+ ",Kit1Plays30"  
					+ ",Kit2Plays" 
					+ ",Kit2Plays30" 
					+ ",Kit3Plays"  
					+ ",Kit3Plays30"  
					+ ",Kit4Plays" 
					+ ",Kit4Plays30" 
					+ ",Kit5Plays"  
					+ ",Kit5Plays30" 
					+ ",Kit1Plays24h" 
					+ ",Kit2Plays24h" 
					+ ",Kit3Plays24h" 
					+ ",Kit4Plays24h"  
					+ ",Kit5Plays24h"//38
					+ ")" 
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			ps.setString(1, "-");
			ps.setString(2, "default");
			ps.setString(3, "");
			ps.setString(4, "");
			ps.setString(5, "");
			ps.setString(6, "2 1");
			ps.setString(7, "");
			ps.setString(8, "");
			ps.setString(9, "" + defaultKitPrefsBuilder.toString());
			ps.setString(10, "");
			ps.setString(11, "");
			ps.setString(12, "" + defaultKitPrefsBuilder.toString());
			ps.setString(13, "");
			ps.setString(14, "");
			ps.setString(15, "" + defaultKitPrefsBuilder.toString());
			ps.setString(16, "");
			ps.setString(17, "");
			ps.setString(18, "" + defaultKitPrefsBuilder.toString());
			ps.setString(19, "0");
			ps.setString(20, "0");
			ps.setString(21, "1");
			ps.setString(22, "1");
			
			ps.setString(23, "0");
			ps.setString(24, "0");
			ps.setString(25, "0");
			ps.setString(26, "0");
			ps.setString(27, "0");
			ps.setString(28, "0");
			ps.setString(29, "0");
			ps.setString(30, "0");
			ps.setString(31, "0");
			ps.setString(32, "0");
			ps.setString(33, "0");
			ps.setString(34, "0");
			ps.setString(35, "0");
			ps.setString(36, "0");
			ps.setString(37, "0");
			ps.setString(38, "0");
			ps.setString(39, "0");
			ps.executeUpdate();
			closeStatments(ps, conn);
			return;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			closeStatments(ps, conn);
		}
	}

	public static void updateUserName(UUID uuid, String Name) {
		Connection conn = connection;
		PreparedStatement ps = null;
		try {
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("UPDATE " + table + " SET PlayerName = ? WHERE UUID = ?");
			ps.setString(2, uuid.toString());
			ps.setString(1, Name);
			ps.executeUpdate();
			closeStatments(ps, conn);
			return;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			closeStatments(ps, conn);
		}
	}

	public static String getUserName(UUID uuid) {
		Connection conn = connection;
		PreparedStatement ps = null;
		try {
			if (sql == null) {
				sql = new SQLite(plugin);
			}
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("SELECT PlayerName FROM " + table + " WHERE UUID = ?");
			ps.setString(1, uuid.toString());

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String data = rs.getString("PlayerName");
				closeStatments(ps, conn);
				return data;
			}
			return null;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			closeStatments(ps, conn);
		}
		return null;
	}

	public static UUID getUserID(String Name) {
		Connection conn = connection;
		PreparedStatement ps = null;
		try {
			if (sql == null) {
				sql = new SQLite(plugin);
			}
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("SELECT UUID FROM " + table + " WHERE PlayerName = ?");
			ps.setString(1, Name);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				try {
					UUID uuid = UUID.fromString(rs.getString("UUID"));
					closeStatments(ps, conn);
					return uuid;
				} catch (Exception e) {

					return null;
				}

			}
			return null;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			closeStatments(ps, conn);
		}
		return null;
	}

	public static boolean getPrefDefault(int pref) {
		Connection conn = connection;
		PreparedStatement ps = null;
		try {
			if (sql == null) {
				sql = new SQLite(plugin);
			}
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("SELECT Settings FROM " + table + " WHERE UUID = ?");

			ps.setString(1, "default");

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String[] prefs = rs.getString("Settings").split(" ");
				if ((pref >= prefs.length) || (pref < 0)) {
					closeStatments(ps, conn);
					return false;
				}
				if (prefs[pref].equalsIgnoreCase("t")) {
					closeStatments(ps, conn);
					return true;
				}
				return false;
			}
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			closeStatments(ps, conn);
		}
		
		return false;
	}

	public static boolean getPref(UUID uuid, int pref, String KitID) {
		Connection conn = connection;
		PreparedStatement ps = null;
		try {
			if (sql == null) {
				sql = new SQLite(plugin);
			}
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("SELECT Settings FROM " + table + " WHERE UUID = ?");
			if ((!KitID.equalsIgnoreCase("")) && (!KitID.equalsIgnoreCase("1"))) {
				ps = conn.prepareStatement("SELECT KitSettings" + KitID + " FROM " + table + " WHERE UUID = ?");
			}
			ps.setString(1, uuid.toString());

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				if ((!KitID.equalsIgnoreCase("")) && (!KitID.equalsIgnoreCase("1"))) {
					String[] prefs = rs.getString("KitSettings" + KitID).split(" ");
					if ((pref >= prefs.length) || (pref < 0)) {
						closeStatments(ps, conn);
						return false;
					}
					if (prefs[pref].equalsIgnoreCase("t")) {
						closeStatments(ps, conn);
						return true;
					}
					return false;
				}
				String[] prefs = rs.getString("Settings").split(" ");
				if ((pref >= prefs.length) || (pref < 0)) {
					closeStatments(ps, conn);
					return false;
				}
				if (prefs[pref].equalsIgnoreCase("t")) {
					closeStatments(ps, conn);
					return true;
				}
				return false;
			}
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			closeStatments(ps, conn);
		}
		return false;
		
	}

	public static String[] getRawPrefDefault() {
		Connection conn = connection;
		PreparedStatement ps = null;
		try {
			if (sql == null) {
				sql = new SQLite(plugin);
			}
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("SELECT Settings FROM " + table + " WHERE UUID = ?");

			ps.setString(1, "default");

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String[] prefs = rs.getString("Settings").split(" ");
				closeStatments(ps, conn);
				return prefs;
			}
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			closeStatments(ps, conn);
		}
		
		return null;
	}

	public static String[] getRawPref(UUID uuid, String KitID) {
		Connection conn = connection;
		PreparedStatement ps = null;
		try {
			if (sql == null) {
				sql = new SQLite(plugin);
			}
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("SELECT Settings FROM " + table + " WHERE UUID = ?");
			if (!KitID.equalsIgnoreCase("")) {
				ps = conn.prepareStatement("SELECT KitSettings" + KitID + " FROM " + table + " WHERE UUID = ?");
			}
			ps.setString(1, uuid.toString());

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {

				if (KitID.equalsIgnoreCase("")) {
					String[] prefs = rs.getString("Settings").split(" ");
					return prefs;
				}
				String[] prefs = rs.getString("KitSettings" + KitID).split(" ");
				closeStatments(ps, conn);
				return prefs;
			}
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			closeStatments(ps, conn);
		}
		
		return null;
	}

	public static boolean setStats(UUID uuid, int Higher, String Stat, boolean timed) {
		Connection conn = connection;
		PreparedStatement ps = null;
		try {
			if(!timed) {
				if (Stat.equalsIgnoreCase("FightsWon")) {
					
					conn = plugin.sql.getSQLConnection();
					ps = conn.prepareStatement("UPDATE " + table + " SET FightsWon = ? WHERE UUID = ?");
					ps.setString(2, uuid.toString());
					int newStat = Integer.parseInt(getStats(uuid, Stat, timed));
					newStat += Higher;
					ps.setString(1, "" + newStat);
					ps.executeUpdate();
				} else if (Stat.equalsIgnoreCase("Fights")) {
					
					conn = plugin.sql.getSQLConnection();
					ps = conn.prepareStatement("UPDATE " + table + " SET Fights = ? WHERE UUID = ?");
					ps.setString(2, uuid.toString());
					int newStat = Integer.parseInt(getStats(uuid, Stat, timed));
					newStat += Higher;
					ps.setString(1, "" + newStat);
					ps.executeUpdate();
				}
			} else {
				if (Stat.equalsIgnoreCase("FightsWon")) {
					
					conn = plugin.sql.getSQLConnection();
					ps = conn.prepareStatement("UPDATE " + table + " SET FightsWon30 = ? WHERE UUID = ?");
					ps.setString(2, uuid.toString());
					int newStat = 0;
					if(getStats(uuid, Stat, true) != null)
						newStat = Integer.parseInt(getStats(uuid, Stat, true));
					newStat += Higher;
					ps.setString(1, "" + newStat);
					ps.executeUpdate();
				} else if (Stat.equalsIgnoreCase("Fights")) {
					
					conn = plugin.sql.getSQLConnection();
					ps = conn.prepareStatement("UPDATE " + table + " SET Fights30 = ? WHERE UUID = ?");
					ps.setString(2, uuid.toString());
					int newStat = 0;
					if(getStats(uuid, Stat, true) != null)
						newStat = Integer.parseInt(getStats(uuid, Stat, true));
					
					newStat += Higher;
					ps.setString(1, "" + newStat);
					ps.executeUpdate();
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatments(ps, conn);
		}
		return true;
	}

	public static boolean setPrefDefault(int Pref, boolean state) {
		Connection conn = connection;
		PreparedStatement ps = null;
		if (isDefaultExists()) {
			try {
				if (Pref >= getRawPrefDefault().length) updatePrefDefault();
				
				String[] setThis = getRawPrefDefault();
				if (state) {
					setThis[Pref] = "t";
				} else {
					setThis[Pref] = "f";
				}
				boolean first = true;
				String build = "";
				for (int i = 0; i < setThis.length; i++) {
					if (first) {
						build = setThis[i];
						first = false;
					} else {
						build = build + " " + setThis[i];
					}
				}
				if (sql == null) {
					sql = new SQLite(plugin);
				}
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("UPDATE " + table + " SET Settings = ? WHERE UUID = ?");

				ps.setString(2, "default");
				ps.setString(1, build);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeStatments(ps, conn);
			}
			return true;
		}
		addDefault();
		try {
			if (Pref > getRawPrefDefault().length) {
				return false;
			}
			String[] setThis = getRawPrefDefault();
			if (state) {
				setThis[Pref] = "t";
			} else {
				setThis[Pref] = "f";
			}
			if (sql == null) {
				sql = new SQLite(plugin);
			}
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("UPDATE " + table + " SET Settings = ? WHERE UUID = ?");
			ps.setString(2, "default");
			ps.setString(1, setThis.toString());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatments(ps, conn);
		}
		return false;
	}

	public static boolean setPref(UUID uuid, int Pref, boolean state, String KitID) {
		Connection conn = connection;
		PreparedStatement ps = null;
		if (isUserExists(uuid)) {
			try {
				if (Pref >= getRawPref(uuid, KitID).length) {
					return false;
				}

				String[] setThis = getRawPref(uuid, KitID);
				if (state) {
					setThis[Pref] = "t";
				} else {
					setThis[Pref] = "f";
				}
				boolean first = true;
				String build = "";
				for (int i = 0; i < setThis.length; i++) {
					if (first) {
						build = setThis[i];
						first = false;
					} else {
						build = build + " " + setThis[i];
					}
				}
				if (sql == null) {
					sql = new SQLite(plugin);
				}
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("UPDATE " + table + " SET Settings = ? WHERE UUID = ?");
				if (!KitID.equalsIgnoreCase("")) {
					ps = conn.prepareStatement("UPDATE " + table + " SET KitSettings" + KitID + " = ? WHERE UUID = ?");
				}
				ps.setString(2, uuid.toString());
				ps.setString(1, build);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeStatments(ps, conn);
			}
			return true;
		}
		if (Bukkit.getPlayer(uuid) != null) {
			addUser(uuid, Bukkit.getPlayer(uuid).getName());
			try {
				if (Pref >= getRawPref(uuid, KitID).length) {
					return false;
				}

				String[] setThis = getRawPref(uuid, KitID);
				if (state) {
					setThis[Pref] = "t";
				} else {
					setThis[Pref] = "f";
				}
				if (sql == null) {
					sql = new SQLite(plugin);
				}
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("UPDATE " + table + " SET Settings = ? WHERE UUID = ?");
				if (!KitID.equalsIgnoreCase("")) {
					ps = conn.prepareStatement("UPDATE " + table + " SET KitSettings" + KitID + " = ? WHERE UUID = ?");
				}
				ps.setString(2, uuid.toString());
				ps.setString(1, setThis.toString());
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeStatments(ps, conn);
			}
		}
		return false;
	}

	public static PlayerQuequePrefs getQuequePrefState(UUID uuid) {
		Connection conn = connection;
		PreparedStatement ps = null;
		try {
			if (sql == null) {
				sql = new SQLite(plugin);
			}
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("SELECT QuequePrefs FROM " + table + " WHERE UUID = ?");

			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String State = rs.getString("QuequePrefs").split(" ")[0];
				if (State == null) {
					return null;
				}
				if (State.equalsIgnoreCase("1")) {
					closeStatments(ps, conn);
					return PlayerQuequePrefs.ownKit;
				}
				if (State.equalsIgnoreCase("2")) {
					closeStatments(ps, conn);
					return PlayerQuequePrefs.EnemieKit;
				}
				if (State.equalsIgnoreCase("3")) {
					closeStatments(ps, conn);
					return PlayerQuequePrefs.RandomKit;
				}
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("resource")
	public static boolean setQuequePref(UUID uuid, PlayerQuequePrefs state) {
		Connection conn = connection;
		PreparedStatement ps = null;
		if (isUserExists(uuid)) {
			try {
				if (state == null) {
					return false;
				}
				int id = 2;
				if (state == PlayerQuequePrefs.ownKit) {
					id = 1;
				} else if (state == PlayerQuequePrefs.EnemieKit) {
					id = 2;
				} else if (state == PlayerQuequePrefs.RandomKit) {
					id = 3;
				} else {
					return false;
				}
				if (sql == null) {
					sql = new SQLite(plugin);
				}
				conn = plugin.sql.getSQLConnection();

				ps = conn.prepareStatement("SELECT QuequePrefs FROM " + table + " WHERE UUID = ?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();

				String save = rs.getString("QuequePrefs");

				String[] saveData = save.split(" ");
				saveData[0] = "" + id;
				save = "";
				for (int i = 0; saveData.length > i; i++) {
					save = save + saveData[i] + " ";
				}
				ps = conn.prepareStatement("UPDATE " + table + " SET QuequePrefs = ? WHERE UUID = ?");
				ps.setString(2, uuid.toString());
				ps.setString(1, save);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeStatments(ps, conn);
			}
			return true;
		}
		if (Bukkit.getPlayer(uuid) != null) {
			addUser(uuid, Bukkit.getPlayer(uuid).getName());
			try {
				if (state == null) {
					return false;
				}
				int id = 2;
				if (state == PlayerQuequePrefs.ownKit) {
					id = 1;
				} else if (state == PlayerQuequePrefs.EnemieKit) {
					id = 2;
				} else if (state == PlayerQuequePrefs.RandomKit) {
					id = 3;
				} else {
					return false;
				}
				if (sql == null) {
					sql = new SQLite(plugin);
				}
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("UPDATE " + table + " SET QuequePrefs = ? WHERE UUID = ?");
				ps.setString(2, uuid.toString());
				ps.setString(1, "" + id);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeStatments(ps, conn);
			}
		}
		return false;
	}

	public static boolean setKit(UUID uuid, String Kit, boolean Armor, String KitID) {
		Connection conn = connection;
		PreparedStatement ps = null;
		if (!Armor) {
			if (isUserExists(uuid)) {
				try {
					if (sql == null) {
						sql = new SQLite(plugin);
					}
					conn = plugin.sql.getSQLConnection();
					ps = conn.prepareStatement("UPDATE " + table + " SET KitInv" + KitID + " = ? WHERE UUID = ?");
					ps.setString(2, uuid.toString());
					ps.setString(1, Kit);
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					closeStatments(ps, conn);
				}
				return true;
			}
			if (Bukkit.getPlayer(uuid) != null) {
				addUser(uuid, Bukkit.getPlayer(uuid).getName());
				try {
					if (sql == null) {
						sql = new SQLite(plugin);
					}
					conn = plugin.sql.getSQLConnection();
					ps = conn.prepareStatement("UPDATE " + table + " SET KitInv" + KitID + " = ? WHERE UUID = ?");
					ps.setString(2, uuid.toString());
					ps.setString(1, Kit);
					ps.executeUpdate();
					closeStatments(ps, conn);
					return true;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return false;
		}
		if (isUserExists(uuid)) {
			try {
				if (sql == null) {
					sql = new SQLite(plugin);
				}
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("UPDATE " + table + " SET KitArmor" + KitID + " = ? WHERE UUID = ?");
				ps.setString(2, uuid.toString());
				ps.setString(1, Kit);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeStatments(ps, conn);
			}
			return true;
		}
		if (Bukkit.getPlayer(uuid) != null) {
			addUser(uuid, Bukkit.getPlayer(uuid).getName());
			try {
				if (sql == null) {
					sql = new SQLite(plugin);
				}
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("UPDATE " + table + " SET KitArmor" + KitID + " = ? WHERE UUID = ?");
				ps.setString(2, uuid.toString());
				ps.setString(1, Kit);
				ps.executeUpdate();
				closeStatments(ps, conn);
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean setKitDefault(String Kit, boolean Armor) {
		Connection conn = connection;
		PreparedStatement ps = null;
		if (!Armor) {
			if (isDefaultExists()) {
				try {
					if (sql == null) {
						sql = new SQLite(plugin);
					}
					conn = plugin.sql.getSQLConnection();
					ps = conn.prepareStatement("UPDATE " + table + " SET KitInv = ? WHERE UUID = ?");
					ps.setString(2, "default");
					ps.setString(1, Kit);
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					closeStatments(ps, conn);
				}
				return true;
			}
			addDefault();
			try {
				if (sql == null) {
					sql = new SQLite(plugin);
				}
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("UPDATE " + table + " SET KitInv = ? WHERE UUID = ?");
				ps.setString(2, "default");
				ps.setString(1, Kit);
				ps.executeUpdate();
				closeStatments(ps, conn);
				return true;
			} catch (SQLException e) {
				e.printStackTrace();

				return false;
			}
		}
		if (isDefaultExists()) {
			try {
				if (sql == null) {
					sql = new SQLite(plugin);
				}
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("UPDATE " + table + " SET KitArmor = ? WHERE UUID = ?");
				ps.setString(2, "default");
				ps.setString(1, Kit);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeStatments(ps, conn);
			}
			return true;
		}
		addDefault();
		try {
			if (sql == null) {
				sql = new SQLite(plugin);
			}
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("UPDATE " + table + " SET KitArmor = ? WHERE UUID = ?");
			ps.setString(2, "default");
			ps.setString(1, Kit);
			ps.executeUpdate();
			closeStatments(ps, conn);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getKitDefault(boolean Armor) {
		Connection conn = connection;
		PreparedStatement ps = null;
		if (!Armor) {
			try {
				if (sql == null) {
					sql = new SQLite(plugin);
				}
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("SELECT KitInv FROM " + table + " WHERE UUID = ?");

				ps.setString(1, "default");
				ResultSet rs = ps.executeQuery();
				if (!rs.next()) {
					return null;
				}
				String data = rs.getString("KitInv");
				closeStatments(ps, conn);
				return data;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				if (sql == null) {
					sql = new SQLite(plugin);
				}
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("SELECT KitArmor FROM " + table + " WHERE UUID = ?");
				ps.setString(1, "default");
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					String data = rs.getString("KitArmor");
					closeStatments(ps, conn);
					return data;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return "";
	}

	public static String getKit(UUID uuid, boolean Armor, String KitID) {
		Connection conn = connection;
		PreparedStatement ps = null;
		if (!Armor) {
			try {
				if (sql == null) {
					sql = new SQLite(plugin);
				}
				
				if(uuid == null) return "";
				conn = plugin.sql.getSQLConnection();

				ps = conn.prepareStatement("SELECT KitInv" + KitID + " FROM " + table + " WHERE UUID = ?");

				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				if (!rs.next()) {
					return null;
				}
				String data = rs.getString("KitInv" + KitID);
				closeStatments(ps, conn);
				return data;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				if (sql == null) {
					sql = new SQLite(plugin);
				}
				if(uuid == null) return "";
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("SELECT KitArmor" + KitID + " FROM " + table + " WHERE UUID = ?");
				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					String data = rs.getString("KitArmor" + KitID);
					closeStatments(ps, conn);
					return data;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return "";
	}

	public static String getStats(UUID uuid, String Typ, boolean timed) {
		Connection conn = connection;
		PreparedStatement ps = null;
		if(!timed) {
			if (Typ.equalsIgnoreCase("Fights")) {
				try {
					if (sql == null) {
						sql = new SQLite(plugin);
					}
					conn = plugin.sql.getSQLConnection();
					ps = conn.prepareStatement("SELECT Fights FROM " + table + " WHERE UUID = ?");
					ps.setString(1, uuid.toString());
					ResultSet rs = ps.executeQuery();
					if (!rs.next()) return "0";
					String data = rs.getString("Fights");
					closeStatments(ps, conn);
					return data;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (Typ.equalsIgnoreCase("FightsWon")) {
				try {
					if (sql == null) sql = new SQLite(plugin);
					conn = plugin.sql.getSQLConnection();
					ps = conn.prepareStatement("SELECT FightsWon FROM " + table + " WHERE UUID = ?");
					ps.setString(1, uuid.toString());
					ResultSet rs = ps.executeQuery();
					if (rs.next()) {
						String data = rs.getString("FightsWon");
						closeStatments(ps, conn);
						return data;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} 
			}
		} else {
			if (Typ.equalsIgnoreCase("Fights")) {
				try {
					if (sql == null) {
						sql = new SQLite(plugin);
					}
					conn = plugin.sql.getSQLConnection();
					ps = conn.prepareStatement("SELECT Fights30 FROM " + table + " WHERE UUID = ?");
					ps.setString(1, uuid.toString());
					ResultSet rs = ps.executeQuery();
					if (rs.next()) {
						String data = rs.getString("Fights30");;
						closeStatments(ps, conn);
						return data;
					}
					String data = rs.getString("Fights30");
					closeStatments(ps, conn);
					return data;
				} catch (SQLException e) {
					//e.printStackTrace();
				}
			} else if (Typ.equalsIgnoreCase("FightsWon")) {
				try {
					if (sql == null) {
						sql = new SQLite(plugin);
					}
					conn = plugin.sql.getSQLConnection();
					ps = conn.prepareStatement("SELECT FightsWon30 FROM " + table + " WHERE UUID = ?");
					ps.setString(1, uuid.toString());
					ResultSet rs = ps.executeQuery();
					if (rs.next()) {
						String data = rs.getString("FightsWon30");
						closeStatments(ps, conn);
						return data;
					}
				} catch (SQLException e) {
					//e.printStackTrace();
				} 
			}
		}

		return "0";
	}

	public static void updatePref(UUID uuid, String KitID) {
		Connection conn = null;
		PreparedStatement ps = null;
		if (isUserExists(uuid)) {
			try {
				if (getRawPref(uuid, KitID).length != plugin.defaultKitPrefs) {
					if (sql == null) {
						sql = new SQLite(plugin);
					}
					conn = plugin.sql.getSQLConnection();

					String[] data = new String[plugin.defaultKitPrefs];
					String[] saveData = getRawPref(uuid, KitID);

					int filled = 0;

					for (String str : saveData) {
						data[filled] = str;
						filled++;
					}

					while (filled < plugin.defaultKitPrefs) {
						data[filled] = "f";
						filled++;
					}

					StringBuilder builder = new StringBuilder();
					boolean first = true;
					for (String str : data) {
						if (first) {
							builder.append(str);
							first = false;
						} else {
							builder.append(" ");
							builder.append(str);
						}

					}

					conn = plugin.sql.getSQLConnection();
					ps = conn.prepareStatement("UPDATE " + table + " SET Settings = ? WHERE UUID = ?");
					if (!KitID.equalsIgnoreCase("")) {
						ps = conn.prepareStatement(
								"UPDATE " + table + " SET KitSettings" + KitID + " = ? WHERE UUID = ?");
					}
					ps.setString(2, uuid.toString());

					ps.setString(1, builder.toString());
					ps.executeUpdate();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeStatments(ps, conn);
			}
		}
	}

	public static void updatePrefDefault() {
		Connection conn = connection;
		PreparedStatement ps = null;
		if (isDefaultExists()) {
			try {
				if (getRawPrefDefault().length != plugin.defaultKitPrefs) {
					if (sql == null) {
						sql = new SQLite(plugin);
					}

					String[] data = new String[plugin.defaultKitPrefs];
					String[] saveData = getRawPrefDefault();

					int filled = 0;

					for (String str : saveData) {
						data[filled] = str;
						filled++;
					}

					while (filled < plugin.defaultKitPrefs) {
						data[filled] = "f";
						filled++;
					}

					StringBuilder builder = new StringBuilder();
					boolean first = true;
					for (String str : data) {
						if (first) {
							builder.append(str);
							first = false;
						} else {
							builder.append(" ");
							builder.append(str);
						}

					}

					conn = plugin.sql.getSQLConnection();
					ps = conn.prepareStatement("UPDATE " + table + " SET Settings = ? WHERE UUID = ?");

					ps.setString(2, "default");
					ps.setString(1, "" + builder.toString());
					ps.executeUpdate();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeStatments(ps, conn);
			}
		}
	}

	public static String getDefaultKit(UUID uuid) {
		Connection conn = connection;
		PreparedStatement ps = null;
		try {
			if (sql == null) {
				sql = new SQLite(plugin);
			}
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("SELECT DefaultKit FROM " + table + " WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String data = rs.getString("DefaultKit");
				closeStatments(ps, conn);
				return data;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatments(ps, conn);
		}
		return "1";
	}

	public static void setDefaultKit(UUID uuid, String ID) {
		Connection conn = connection;
		PreparedStatement ps = null;
		if (isUserExists(uuid)) {
			try {
				if (sql == null) {
					sql = new SQLite(plugin);
				}
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("UPDATE " + table + " SET DefaultKit = ? WHERE UUID = ?");
				ps.setString(2, uuid.toString());
				ps.setString(1, ID);
				ps.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeStatments(ps, conn);
			}
		}
	}

	public static PlayerBestOfsPrefs getQuequePrefState2(UUID uuid) {
		Connection conn = connection;
		PreparedStatement ps = null;
		try {
			if (sql == null) {
				sql = new SQLite(plugin);
			}
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("SELECT QuequePrefs FROM " + table + " WHERE UUID = ?");

			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				if (rs.getString("QuequePrefs").split(" ").length >= 2) {
					String State = rs.getString("QuequePrefs").split(" ")[1];
					if (State == null) {
						return null;
					}
					if (State.equalsIgnoreCase("1")) {
						return PlayerBestOfsPrefs.BestOf1;
					}
					if (State.equalsIgnoreCase("2")) {
						return PlayerBestOfsPrefs.BestOf3;
					}
					if (State.equalsIgnoreCase("3")) {
						return PlayerBestOfsPrefs.BestOf5;
					}
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeStatments(ps, conn);
		}
		return null;
	}

	@SuppressWarnings("resource")
	public static boolean setQuequePref2(UUID uuid, PlayerBestOfsPrefs state) {
		Connection conn = connection;
		PreparedStatement ps = null;
		if (isUserExists(uuid)) {
			try {
				if (state == null) {
					return false;
				}
				int id = 2;
				if (state == PlayerBestOfsPrefs.BestOf1) {
					id = 1;
				} else if (state == PlayerBestOfsPrefs.BestOf3) {
					id = 2;
				} else if (state == PlayerBestOfsPrefs.BestOf5) {
					id = 3;
				} else {
					return false;
				}
				if (sql == null) {
					sql = new SQLite(plugin);
				}
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("SELECT QuequePrefs FROM " + table + " WHERE UUID = ?");

				ps.setString(1, uuid.toString());
				ResultSet rs = ps.executeQuery();

				String used = rs.getString("QuequePrefs");
				if (used.split(" ").length >= 2) {
					sql = new SQLite(plugin);
					conn = plugin.sql.getSQLConnection();

					ps = conn.prepareStatement("UPDATE " + table + " SET QuequePrefs = ? WHERE UUID = ?");
					ps.setString(2, uuid.toString());

					String save = used;

					String[] saveData = save.split(" ");
					saveData[1] = "" + id;
					save = "";
					for (int i = 0; saveData.length > i; i++) {
						save = save + saveData[i] + " ";
					}
					ps.setString(1, save);
					ps.executeUpdate();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeStatments(ps, conn);
			}
			return true;
		}
		if (Bukkit.getPlayer(uuid) != null) {
			addUser(uuid, Bukkit.getPlayer(uuid).getName());
			setQuequePref2(uuid, state);
			return true;
		}
		return false;
	}

	public static HashMap<Integer, UUID> Top5Players(boolean timed) {
		HashMap<Integer, UUID> Top5 = new HashMap<>();
		if(!timed) {
			try {
				if (!isConnected())
					return null;

				Connection con = plugin.sql.getSQLConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * from " + table + " order by CAST(FightsWon AS UNSIGNED) desc LIMIT " + plugin.topPlaces);

				
				ResultSet rs = ps.executeQuery();
				int Platz = 1;
				while (rs.next()) {
					String uuid = rs.getString("UUID");
					if (!uuid.equalsIgnoreCase("default") && !uuid.toLowerCase().contains("custom")) {
						try {
							Top5.put(Integer.valueOf(Platz), UUID.fromString(uuid));
							Platz++;
						} catch (Exception localException) {}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				if (!isConnected())
					return null;

				Connection con = plugin.sql.getSQLConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * from " + table + " order by CAST(FightsWon30 AS UNSIGNED) desc LIMIT " + plugin.topPlaces);

				
				ResultSet rs = ps.executeQuery();
				int Platz = 1;
				while (rs.next()) {
					String uuid = rs.getString("UUID");
					if (!uuid.equalsIgnoreCase("default") && !uuid.toLowerCase().contains("custom")) {
						try {
							Top5.put(Integer.valueOf(Platz), UUID.fromString(uuid));
							Platz++;
						} catch (Exception localException) {}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return Top5;
	}

	public static Integer getPositionKit(String name, int type) {
		
		try {
			
			if(type == 0) {
				Connection con = plugin.sql.getSQLConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * from " + table + " order by CAST(Kit1Plays AS UNSIGNED) desc");

				ResultSet rs = ps.executeQuery();
				int Platz = 1;
				while (rs.next()) {
					String id = rs.getString("PlayerName");
					if (id.equalsIgnoreCase(name)) {
						closeStatments(ps, con);
						return Platz;
					}
					
					if (!id.equalsIgnoreCase("default") && !id.toLowerCase().contains("custom")) {
						Platz++;
					}
				}
				return Platz;
			} else if(type == 1) {
				Connection con = plugin.sql.getSQLConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * from " + table + " order by CAST(Kit1Plays30 AS UNSIGNED) desc");

				ResultSet rs = ps.executeQuery();
				int Platz = 1;
				while (rs.next()) {
					String id = rs.getString("PlayerName");
					if (id.equalsIgnoreCase(name)) {
						closeStatments(ps, con);
						return Platz;
					}
					
					if (!id.equalsIgnoreCase("default") && !id.toLowerCase().contains("custom")) {
						Platz++;
					}
				}
				return Platz;
			} else if(type == 2) {
				Connection con = plugin.sql.getSQLConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * from " + table + " order by CAST(Kit1Plays24h AS UNSIGNED) desc");

				ResultSet rs = ps.executeQuery();
				int Platz = 1;
				while (rs.next()) {
					String id = rs.getString("PlayerName");
					if (id.equalsIgnoreCase(name)) {
						closeStatments(ps, con);
						return Platz;
					}
					
					if (!id.equalsIgnoreCase("default") && !id.toLowerCase().contains("custom")) {
						Platz++;
					}
				}
				return Platz;
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Integer.valueOf(-1);
	}

	public static HashMap<Integer, String> Top5Kits(int type) {
		HashMap<Integer, String> Top5 = new HashMap<>();
		if(type == 0) {
			try {
				if (!isConnected()) return Top5;

				Connection con = plugin.sql.getSQLConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * from " + table + " order by CAST(Kit1Plays AS UNSIGNED) desc LIMIT " + plugin.topPlaces);

				
				ResultSet rs = ps.executeQuery();
				int Platz = 1;
				while (rs.next()) {
					String uuid = rs.getString("PlayerName");
					if (!uuid.equalsIgnoreCase("default")) {
						try {
							Top5.put(Integer.valueOf(Platz), uuid);
							Platz++;
						} catch (Exception localException) {}
					}
				}
			} catch (SQLException e) {
				return Top5;
			}
		} else if(type == 1) {
			try {
				if (!isConnected())
					return Top5;

				Connection con = plugin.sql.getSQLConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * from " + table + " order by CAST(Kit1Plays30 AS UNSIGNED) desc LIMIT " + plugin.topPlaces);

				
				ResultSet rs = ps.executeQuery();
				int Platz = 1;
				while (rs.next()) {
					
					String uuid = rs.getString("PlayerName");
					if (!uuid.equalsIgnoreCase("default") ) {
						try {
							Top5.put(Integer.valueOf(Platz), uuid);
							Platz++;
						} catch (Exception localException) {}
					}
				}
			} catch (SQLException e) {
				return Top5;
			}
		} else if(type == 2) {
			try {
				if (!isConnected())
					return Top5;

				Connection con = plugin.sql.getSQLConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * from " + table + " order by CAST(Kit1Plays24h AS UNSIGNED) desc LIMIT " + plugin.topPlaces);

				
				ResultSet rs = ps.executeQuery();
				int Platz = 1;
				while (rs.next()) {
					String uuid = rs.getString("PlayerName");
					if (!uuid.equalsIgnoreCase("default") ) {
						try {
							Top5.put(Integer.valueOf(Platz), uuid);
							Platz++;
						} catch (Exception localException) {}
					}
				}
			} catch (SQLException e) {
				return Top5;
			}
		}
		
		return Top5;
	}

	
	
	
	public static Integer getPosition(UUID uuid, boolean timed) {
		try {
			if(!timed) {
				Connection con = plugin.sql.getSQLConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * from " + table + " order by CAST(FightsWon AS UNSIGNED) desc");

				ResultSet rs = ps.executeQuery();
				int Platz = 1;
				while (rs.next()) {
					String id = rs.getString("UUID");
					if (id.equalsIgnoreCase(uuid.toString())) {
						closeStatments(ps, con);
						return Platz;
					}
					
					if (!id.equalsIgnoreCase("default") && !id.toLowerCase().contains("custom")) {
						Platz++;
					}
				}
				return Platz;
			} else {
				Connection con = plugin.sql.getSQLConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * from " + table + " order by CAST(FightsWon30 AS UNSIGNED) desc");

				ResultSet rs = ps.executeQuery();
				int Platz = 1;
				while (rs.next()) {
					String id = rs.getString("UUID");
					if (id.equalsIgnoreCase(uuid.toString())) {
						closeStatments(ps, con);
						return Platz;
					}
				
					if (!id.equalsIgnoreCase("default") && !id.toLowerCase().contains("custom")) {
						Platz++;
					}
				}
				return Platz;
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Integer.valueOf(-1);
	}
	
	
	
	public static boolean isMapDisabled(UUID uuid, String MapName) {
		PreparedStatement ps = null;
		Connection con = plugin.sql.getSQLConnection();
		try {

			ps = con.prepareStatement("SELECT DisabledMaps FROM " + table + " WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String Inv = rs.getString("DisabledMaps");
				String[] InvS = Inv.split(" ");
				for (int i = 0; i < InvS.length; i++) {
					if (InvS[i].equalsIgnoreCase(MapName)) {
						return true;
					}
				}
				return false;
			}
		} catch (Exception localException) {
		} finally {
			closeStatments(ps, con);
		}
		return false;
	}

	public static String getDisabledMaps(UUID uuid) {
		PreparedStatement ps = null;
		Connection con = plugin.sql.getSQLConnection();
		try {
			ps = con.prepareStatement("SELECT DisabledMaps FROM " + table + " WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				String Inv = rs.getString("DisabledMaps");
				if (Inv.equalsIgnoreCase("null")) {
					createDisabled(uuid);
					return "";
				}
				return Inv;
			}
		} catch (Exception localException) {
		} finally {
			closeStatments(ps, con);
		}
		return "";
	}

	public static void setMapDisabled(UUID uuid, String MapName, boolean disabled) {
		PreparedStatement ps = null;
		Connection con = plugin.sql.getSQLConnection();
		try {

			ps = con.prepareStatement("UPDATE " + table + " SET DisabledMaps = ? WHERE UUID = ?");
			ps.setString(2, uuid.toString());

			String[] disabledList = getDisabledMaps(uuid).split(" ");
			for (int i = 0; i < disabledList.length; i++) {
				if (disabledList[i].equalsIgnoreCase(MapName)) {
					disabledList[i] = "";
				}
			}
			String disabledMaps = "";
			for (int i = 0; i < disabledList.length; i++) {
				disabledMaps = disabledMaps + disabledList[i] + " ";
			}
			if (disabled) {
				disabledMaps = disabledMaps + MapName;
			}
			ps.setString(1, disabledMaps);
			ps.executeUpdate();
		} catch (SQLException localSQLException) {
		} finally {
			closeStatments(ps, con);
		}
	}

	public static void createDisabled(UUID uuid) {
		PreparedStatement ps = null;
		Connection con = null;
		try {
			con = plugin.sql.getSQLConnection();
			ps = con.prepareStatement("UPDATE " + table + " SET DisabledMaps = ? WHERE UUID = ?");
			ps.setString(2, uuid.toString());
			ps.setString(1, "");
			ps.executeUpdate();
		} catch (SQLException localSQLException) {
		} finally {
			closeStatments(ps, con);
		}
	}

	public static void closeStatments(PreparedStatement ps, Connection con) {
		try {
			if (ps != null)
				ps.close();
			
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
		}
	}
	
	public static int isCustomKitExits(String Name) {
		if(isNameRegistered(Name)) {
			return 2;
		}
		
		Connection conn = connection;
		PreparedStatement ps = null;
		try {
			if (sql == null) {
				sql = new SQLite(plugin);
			}
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("SELECT PlayerName FROM " + table + " WHERE PlayerName = ?");
			ps.setString(1, Name);
			ResultSet rs = ps.executeQuery();
			boolean data = rs.next();
			closeStatments(ps, conn);
			if(data) return 1;
			return 0;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			closeStatments(ps, conn);
		}
		return 0;
	}
	
	@SuppressWarnings("resource")
	public static void addCustomKit(String Name, String Inv, String Armor, String[] Prefs) {
		
			Connection conn = connection;
			PreparedStatement ps = null;
		
			if(isCustomKitExits(Name) == 1) {
				
				try {
					if (sql == null) {
						sql = new SQLite(plugin);
					}
					
					String[] prefs = Prefs;

					boolean first = true;
					StringBuilder Data = new StringBuilder();
					for (int i = 0; i < prefs.length; i++) {
						if (first) {
							first = false;
							Data = Data.append(prefs[i]);
						} else {
							Data.append(" ");
							Data.append(prefs[i]);
						}
					}
					
					conn = plugin.sql.getSQLConnection();
					ps = conn.prepareStatement("UPDATE " + table + " SET KitInv = ? WHERE PlayerName = ?");
					ps.setString(2, Name);
					ps.setString(1, Inv);
					ps.executeUpdate();
					
					
					conn = plugin.sql.getSQLConnection();
					ps = conn.prepareStatement("UPDATE " + table + " SET KitArmor = ? WHERE PlayerName = ?");
					ps.setString(2, Name);
					ps.setString(1, Armor);
					ps.executeUpdate();
					closeStatments(ps, conn);
					
					conn = plugin.sql.getSQLConnection();
					ps = conn.prepareStatement("UPDATE " + table + " SET Settings = ? WHERE PlayerName = ?");
					ps.setString(2, Name);
					ps.setString(1, Data.toString());
					ps.executeUpdate();
					closeStatments(ps, conn);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					closeStatments(ps, conn);
				}
				
				return;
			}
			
				
				try {
					String[] prefs = Prefs;

					boolean first = true;
					StringBuilder Data = new StringBuilder();
					for (int i = 0; i < prefs.length; i++) {
						if (first) {
							first = false;
							Data = Data.append(prefs[i]);
						} else {
							Data.append(" ");
							Data.append(prefs[i]);
						}
					}


					conn = plugin.sql.getSQLConnection();
					ps = conn.prepareStatement("INSERT INTO " + table + " (" + "PlayerName" + ",UUID" + ",KitInv"
							+ ",KitArmor" + ",Settings" + ",QuequePrefs" + ",KitInv2" + ",KitArmor2" + ",KitSettings2"
							+ ",KitInv3" + ",KitArmor3" + ",KitSettings3" + ",KitInv4" + ",KitArmor4" + ",KitSettings4"
							+ ",KitInv5" + ",KitArmor5" + ",KitSettings5" + ",Fights" + ",FightsWon" + ",DefaultKit"
							+ ",DisabledMaps)" + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					ps.setString(1, Name);
					ps.setString(2, "CUSTOM " + UUID.randomUUID() + UUID.randomUUID() + UUID.randomUUID());
					ps.setString(3, Inv);
					ps.setString(4, Armor);
					ps.setString(5, Data.toString());
					ps.setString(6, "2 1");
					ps.setString(7, "");
					ps.setString(8, "");
					ps.setString(9, "");
					ps.setString(10, "");
					ps.setString(11, "");
					ps.setString(12, "");
					ps.setString(13, "");
					ps.setString(14, "");
					ps.setString(15, "");
					ps.setString(16, "");
					ps.setString(17, "");
					ps.setString(18, "");
					ps.setString(19, "0");
					ps.setString(20, "0");
					ps.setString(21, "1");
					ps.setString(22, "1");
					ps.executeUpdate();
					closeStatments(ps, conn);
					return;
				} catch (SQLException ex) {
					plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
				} finally {
					closeStatments(ps, conn);
				}
				return;
			
			
		
	}
	
	public static String loadCustomKit(String Name, boolean Armor) {
		Connection conn = connection;
		PreparedStatement ps = null;
		if (!Armor) {
			try {
				if (sql == null) {
					sql = new SQLite(plugin);
				}
				conn = plugin.sql.getSQLConnection();

				ps = conn.prepareStatement("SELECT KitInv FROM " + table + " WHERE PlayerName = ?");

				ps.setString(1, Name);
				ResultSet rs = ps.executeQuery();
				if (!rs.next()) {
					return null;
				}
				String data = rs.getString("KitInv");
				closeStatments(ps, conn);
				return data;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				if (sql == null) {
					sql = new SQLite(plugin);
				}
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("SELECT KitArmor FROM " + table + " WHERE PlayerName = ?");
				ps.setString(1, Name);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					
					String data = rs.getString("KitArmor");
					
					closeStatments(ps, conn);
					return data;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return "";
	}
	
	public static boolean getCustomKitPref(String Name, int id) {
		
			Connection conn = connection;
			PreparedStatement ps = null;
			try {
				if (sql == null) {
					sql = new SQLite(plugin);
				}
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("SELECT Settings FROM " + table + " WHERE PlayerName = ?");
				
				ps.setString(1, Name);

				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					
					
					String[] prefs = rs.getString("Settings").split(" ");
					if ((id >= prefs.length) || (id < 0)) {
						closeStatments(ps, conn);
						return false;
					}
					if (prefs[id].equalsIgnoreCase("t")) {
						closeStatments(ps, conn);
						return true;
					}
					return false;
				}
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
			} finally {
				closeStatments(ps, conn);
			}
			return false;
			
		
	}
	
	public static String[] getCustomKitRawPref(String Name) {
		Connection conn = connection;
		PreparedStatement ps = null;
		try {
			if (sql == null) {
				sql = new SQLite(plugin);
			}
			conn = plugin.sql.getSQLConnection();
			ps = conn.prepareStatement("SELECT Settings FROM " + table + " WHERE PlayerName = ?");
			
			ps.setString(1, Name);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {

				
				String[] prefs = rs.getString("Settings").split(" ");
				closeStatments(ps, conn);
				return prefs;
			}
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		} finally {
			closeStatments(ps, conn);
		}
		
		return null;
	}
	
	public static void deleteCustomKit(String Name) {
		
		Connection conn = connection;
		PreparedStatement ps = null;
	
		if(isCustomKitExits(Name) == 1) {
			
			try {
				if (sql == null) sql = new SQLite(plugin);
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("DELETE FROM " + table + " WHERE PlayerName = ?");
				
				ps.setString(1, Name);
				ps.executeUpdate();
				
				
				
				closeStatments(ps, conn);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeStatments(ps, conn);
			}
			
			return;
		}
			return;
		
	}
	
	public  static Integer getAllUserEntrys() {
		
		
		try {//SELECT * from KitDatabase order by CAST(FightsWon AS UNSIGNED) desc limit 5 1vs1Kits
			Connection conn = plugin.sql.getSQLConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT * from " + table +" order by CAST(FightsWon AS UNSIGNED) desc");
			
			ResultSet rs = ps.executeQuery();
			int Platz = 0;
			while(rs.next()) {
				String place = rs.getString("UUID");
				if(!place.toLowerCase().contains("default") && !place.toLowerCase().contains("custom")) {
					Platz++;
				}
			}
			closeStatments(ps, conn);
			return Platz;
		} catch (SQLException e) {
			return -1;
		}
		
    }
	
	public static void updateRankPoints(final UUID uuid, final int amount) {
    	Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			
			@Override
			public void run() {
				PreparedStatement ps = null;
		    	try {
					
					
					ps = plugin.sql.getSQLConnection().prepareStatement("UPDATE " + table + " SET RankPoints = ? WHERE UUID = ?");
					
					ps.setString(2, uuid.toString());
					int points = getRankPoints(uuid)+amount;
					if(points <= -1) points = 0;
					ps.setString(1, "" + points);
					ps.executeUpdate();
					
					
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				} 
				
			}
		});
    	
		
		return;
    }
	
    public static int getRankPoints(UUID uuid) {
		try {
			PreparedStatement ps = null;
	    	ps = plugin.sql.getSQLConnection().prepareStatement("SELECT RankPoints FROM " + table + " WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				return Integer.parseInt(rs.getString("RankPoints"));
			}
		} catch (NumberFormatException e) {
			return 0;
		} catch (SQLException e) {
			return 0;
		}
		return 0;
    }
	
    public static HashMap<Integer, UUID> Top5PlayersRankPoints() {
		HashMap<Integer, UUID> Top5 = new HashMap<>();
		try {
			if (!isConnected())
				return null;

			Connection con = plugin.sql.getSQLConnection();
			PreparedStatement ps = con
					.prepareStatement("SELECT * from " + table + " order by CAST(RankPoints AS UNSIGNED) desc");

			ResultSet rs = ps.executeQuery();
			int Platz = 1;
			while (rs.next() && Platz <= 5) {
				
				String uuid = rs.getString("UUID");
				
				if (!uuid.equalsIgnoreCase("default") && !uuid.toLowerCase().contains("custom")) {
					try {
						Top5.put(Integer.valueOf(Platz), UUID.fromString(uuid));
						Platz++;
					} catch (Exception localException) {}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Top5;
	}

	public static Integer getPositionRankPoints(UUID uuid) {
		
		try {
			Connection con = plugin.sql.getSQLConnection();
			PreparedStatement ps = con
					.prepareStatement("SELECT * from " + table + " order by CAST(RankPoints AS UNSIGNED) desc");

			ResultSet rs = ps.executeQuery();
			int Platz = 1;
			while (rs.next()) {
				String id = rs.getString("UUID");
				if (id.equalsIgnoreCase(uuid.toString())) {
					closeStatments(ps, con);
					
					return Platz;
				}
				
				if (!id.equalsIgnoreCase("default") && !id.toLowerCase().contains("custom")) {
					Platz++;
				}
			}
			return Platz;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Integer.valueOf(-1);
	}
	
	public static boolean exists(String row) {
    	try {
			if (!isConnected()) return false;

			
			PreparedStatement ps = plugin.sql.getSQLConnection().prepareStatement("SELECT "+ row + " FROM " + table);
			
			boolean result = ps.executeQuery().next();
			
			ps.close();
			
			return result;
		} catch (SQLException e) {
			return false;
		}
    }
	
	public static void reset30DayStats() {
		//
		
		try {
			if (!isConnected()) return;

			
			PreparedStatement ps = plugin.sql.getSQLConnection().prepareStatement("UPDATE " + table + " SET Fights30 = 0");
			
			ps.executeUpdate();
			ps = plugin.sql.getSQLConnection().prepareStatement("UPDATE " + table + " SET FightsWon30 = 0");
			
			ps.executeUpdate();
			
			for(int i = 1; i < 5; i++) {
				ps = plugin.sql.getSQLConnection().prepareStatement("UPDATE " + table + " SET Kit" + i + "Plays30 = 0");
				
				ps.executeUpdate();
			}
			
			return;
		} catch (SQLException e) {
			return;
		}
	}
	
	public static void reset24hStats() {
		//
		
		try {
			if (!isConnected()) return;

			for(int i = 1; i < 5; i++) {
				PreparedStatement ps = plugin.sql.getSQLConnection().prepareStatement("UPDATE " + table + " SET Kit" + i + "Plays24h = 0");
				
				ps.executeUpdate();
			}
			
			
			
			return;
		} catch (SQLException e) {
			return;
		}
	}
	
	public static void setStatsKit(String Name, int Higher, int kit, int type) {
		Connection conn = connection;
		PreparedStatement ps = null;
		
		try {
			if(type == 0) {
				if (kit > 5 || kit < 0) return;
				
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("UPDATE " + table + " SET Kit" + kit + "Plays = ? WHERE PlayerName = ?");
				//ps = conn.prepareStatement("UPDATE " + table + " SET Kit" + kit + "Plays30 = ? WHERE PlayerName = ?");
				ps.setString(2, Name);//TODO
				int newStat = 0;
				if(getStatsKit(Name, kit, type) != null)
					newStat = Integer.parseInt(getStatsKit(Name, kit, type));
				newStat += Higher;
				ps.setString(1, "" + newStat);
				ps.executeUpdate();
				
			} else if(type == 1){
				if (kit > 5 || kit < 0) return;
					
					conn = plugin.sql.getSQLConnection();
					ps = conn.prepareStatement("UPDATE " + table + " SET Kit" + kit + "Plays30 = ? WHERE PlayerName = ?");
					//ps = conn.prepareStatement("UPDATE " + table + " SET Kit" + kit + "Plays30 = ? WHERE PlayerName = ?");
					ps.setString(2, Name);//TODO
					int newStat = 0;
					if(getStatsKit(Name, kit, type) != null)
						newStat = Integer.parseInt(getStatsKit(Name, kit, type));
					newStat += Higher;
					ps.setString(1, "" + newStat);
					ps.executeUpdate();
					
			 
			} else if(type == 2){
				if (kit > 5 || kit < 0) return;
				
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("UPDATE " + table + " SET Kit" + kit + "Plays24h = ? WHERE PlayerName = ?");
				//ps = conn.prepareStatement("UPDATE " + table + " SET Kit" + kit + "Plays30 = ? WHERE PlayerName = ?");
				ps.setString(2, Name);//TODO
				int newStat = 0;
				if(getStatsKit(Name, kit, type) != null)
					newStat = Integer.parseInt(getStatsKit(Name, kit, type));
				newStat += Higher;
				ps.setString(1, "" + newStat);
				ps.executeUpdate();
				
		 
			}
		
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeStatments(ps, conn);
		}
		return;
	}

	public static String getStatsKit(String Name, int kit, int type) {
	 Connection conn = connection;
	 PreparedStatement ps = null;
	 if(type == 0) {
		 if (kit > 5 || kit < 0) return "0";
			try {
				
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("SELECT Kit" + kit + "Plays FROM " + table + " WHERE PlayerName = ?");
				ps.setString(1, Name);
				ResultSet rs = ps.executeQuery();
				if (!rs.next()) return "0";
				String data = rs.getString("Kit" + kit + "Plays");
				closeStatments(ps, conn);
				if(data == null || data.equalsIgnoreCase("null")) return "0";
				return data;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		 
	  } else if(type == 1) {
		 if (kit > 5 || kit < 0) return "0";
			try {
				
				conn = plugin.sql.getSQLConnection();
				ps = conn.prepareStatement("SELECT Kit" + kit + "Plays30 FROM " + table + " WHERE PlayerName = ?");
				ps.setString(1, Name);
				ResultSet rs = ps.executeQuery();
				if(!rs.next()) return "0";
				String data = rs.getString("Kit" + kit + "Plays30");
				closeStatments(ps, conn);
				if(data == null || data.equalsIgnoreCase("null")) return "0";
				return data;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else if(type == 2) {
			 if (kit > 5 || kit < 0) return "0";
				try {
					
					conn = plugin.sql.getSQLConnection();
					ps = conn.prepareStatement("SELECT Kit" + kit + "Plays24h FROM " + table + " WHERE PlayerName = ?");
					ps.setString(1, Name);
					ResultSet rs = ps.executeQuery();
					if(!rs.next()) return "0";
					String data = rs.getString("Kit" + kit + "Plays24h");
					closeStatments(ps, conn);
					if(data == null || data.equalsIgnoreCase("null")) return "0";
					return data;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} 
	 

	 return "0";
	}
	
	
}
