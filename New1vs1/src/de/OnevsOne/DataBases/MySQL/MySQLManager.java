package de.OnevsOne.DataBases.MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import de.OnevsOne.main;
import de.OnevsOne.DataBases.SQLite.Errors;
import de.OnevsOne.States.PlayerBestOfsPrefs;
import de.OnevsOne.States.PlayerQuequePrefs;

/**
 * Der Code ist von JHammer
 *
 * 14.05.2016 um 20:35:54 Uhr
 */

public class MySQLManager implements Listener {

	private static main plugin;
	
	@SuppressWarnings("static-access")
	public MySQLManager(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	private static void close(PreparedStatement ps) {
		try {
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void close(ResultSet rs) {
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void close(PreparedStatement ps, ResultSet rs) {
		
		try {
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static  void checkConnect() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				disconnect();
				connect();
				
			}
		}, 20*60*60, 20*60*60);
	}

   
	@SuppressWarnings("static-access")
	public  static void connect() {
    	
    	if(!isConnected()) {
    		try {
    			
    			if(plugin.password == null || plugin.password.equalsIgnoreCase("-")) plugin.password = "";
//    			 = DriverManager.getConnection("jdbc:mysql://" + plugin.host + ":" + plugin.port + "/" + plugin.database, plugin.username, plugin.password);
				
    			MysqlDataSource dataSource = new MysqlDataSource();
    			dataSource.setUser(plugin.username);
    			dataSource.setPassword(plugin.password);
    			dataSource.setServerName(plugin.host);
    			dataSource.setPort(Integer.parseInt(plugin.port));
    			dataSource.setDatabaseName(plugin.database);
    			plugin.con = dataSource.getConnection();
    			
				plugin.getLogger().log(Level.INFO, "[MySQL] Verbindung aufgebaut");
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    	if(!plugin.checkDatabaseConnection) {
    		try {
    			
    			if(plugin.password == null || plugin.password.equalsIgnoreCase("-")) {
    				plugin.password = "";
    			}
    			plugin.con = DriverManager.getConnection("jdbc:mysql://" + plugin.host + ":" + plugin.port + "/" + plugin.database, plugin.username, plugin.password);
    			plugin.getLogger().log(Level.INFO, "[MySQL] Verbindung aufgebaut");
    			
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    }
    
   
	@SuppressWarnings("static-access")
	public static  void disconnect() {
    	if(isConnected()) {
    		try {
    			plugin.con.close();
    			plugin.getLogger().log(Level.INFO, "[MySQL] Verbindung geschlossen");
			} catch (SQLException e) {
				e.printStackTrace();
			}
    		
    	}
    }
    
    
    public static  boolean isConnected() {
    	//if(!plugin.checkDatabaseConnection) return true;
    	
    	try {	
    		Connection con = getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM 1vs1Kits");
			ps.executeQuery();
			close(ps);
			
			return true;
		} catch (Exception e) {
			
		}
    	return false;
    }
    
   
	@SuppressWarnings("static-access")
	public static Connection getConnection() {
    	return plugin.con;
    }
	
   
    
    @EventHandler
	public void JoinReg(final PlayerJoinEvent e) {
		if(!plugin.useMySQL) return;
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			
			@Override
			public void run() {
				if(isUserExists(e.getPlayer().getUniqueId())) {
					//Bukkit.broadcastMessage("Bekannt...");
					updateName(e.getPlayer().getUniqueId(), e.getPlayer().getName());
					updatePref(e.getPlayer().getUniqueId(),"");
					updatePref(e.getPlayer().getUniqueId(),"2");
					updatePref(e.getPlayer().getUniqueId(),"3");
					updatePref(e.getPlayer().getUniqueId(),"4");
					updatePref(e.getPlayer().getUniqueId(),"5");
				} else {
					plugin.addUser(e.getPlayer());
				}
				
			}
		});
		
			//Bukkit.broadcastMessage("Fertig ;)");
	}
		
		
	
  
    public static  boolean isUserExists(UUID uuid) {
    	if(uuid == null) return false;
    	try {
			PreparedStatement ps = getConnection().prepareStatement("SELECT PlayerName FROM 1vs1Kits WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			boolean exists = rs.next();
			close(ps, rs);
			
			return exists;
		} catch (SQLException e) {
			return false;
		}
    }
  
    public  static boolean isDefaultExists() {
    	try {
			PreparedStatement ps = getConnection().prepareStatement("SELECT * FROM 1vs1Kits WHERE UUID = ?");
			ps.setString(1, "default");
			ResultSet rs = ps.executeQuery();
			
			boolean result = rs.next();
			
			close(ps, rs);
			
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return false;
    }
    
    public static  boolean isNameRegistered(String Name) {
    	try {
			PreparedStatement ps = getConnection().prepareStatement("SELECT UUID FROM 1vs1Kits WHERE PlayerName = ?");
			ps.setString(1, Name);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				String str = rs.getString("UUID");
				close(ps,rs);
				if(str.startsWith("-") || str.startsWith("CUSTOM")) return false;
			} else {
				close(ps,rs);
				return false;
			}
			close(ps,rs);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return false;
    }
    
    public  static void updateName(UUID uuid, String NewName) {
    	try {
			PreparedStatement ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET PlayerName = ? WHERE UUID = ?");
			ps.setString(2, uuid.toString());
			ps.setString(1, NewName);
			ps.executeUpdate();
			close(ps);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return;
    }
    
    public static  void updatePref(UUID uuid,String KitID) {
    	try {
    		
    		if (getRawPref(uuid, KitID).length != plugin.defaultKitPrefs) {
				
    			PreparedStatement ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET KitSettings" + KitID +" = ? WHERE UUID = ?");
    			if(KitID.equalsIgnoreCase("")) 
    			 ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET Settings = ? WHERE UUID = ?");
    			
    			
				String[] data = new String[plugin.defaultKitPrefs];
				String[] saveData = getRawPref(uuid, KitID);

				int filled = 0;

				
				for(String str : saveData) {
					if(filled < data.length) {
						data[filled] = str;
						filled++;
					}
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

				ps.setString(2, uuid.toString());
    			ps.setString(1, builder.toString());
    			ps.executeUpdate();
    			close(ps);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return;
    }
    
    public  static void updatePrefDefault() {
    	try {
    		if(getRawPrefDefault() != null && getRawPrefDefault().length < plugin.defaultKitPrefs) {
    			String[] data = new String[plugin.defaultKitPrefs];
			      String[] saveData = getRawPrefDefault();
			      
			      int filled = 0;
			      
			      for(String str : saveData) {
			    	 data[filled] = str;
			    	 filled++;
			      }
			      
			      
			      
			      while(filled < plugin.defaultKitPrefs) {
			    	  data[filled] = "f";
			    	  filled++;
			      }
				     
			      StringBuilder builder = new StringBuilder();
			      boolean first = true;
			      for(String str : data) {
			    	  if(first) {
			    		  builder.append(str);
			    		  first = false;
			    	  } else {
			    		  builder.append(" " + str);
			    	  }
			    	 
			      }
    			
    			PreparedStatement ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET Settings = ? WHERE UUID = ?");
    			ps.setString(2, "default");
    			ps.setString(1, builder.toString());
    			ps.executeUpdate();
    			close(ps);
    		}
    		
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return;
    }
     
    public  static boolean addUser(UUID uuid, String name) {
    	if(isDefaultExists()) {
    		try {
	        	
	        	
	        	 StringBuilder Data = new StringBuilder();
		            String[] defaultKit = getRawPrefDefault();
		            for (int i = 0; i < defaultKit.length; i++) {
		              if (i == 0) {
		                Data = Data.append(defaultKit[i]);
		              } else {
		                Data = Data.append(" ").append(defaultKit[i]);
		              }
		            }
	        	
	        	PreparedStatement ps = getConnection().prepareStatement("INSERT INTO 1vs1Kits ("
	            		+ "PlayerName"
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
	            		+ ",FightsWon"//20
	            		+ ",DefaultKit"
	            		+ ",DisabledMaps"
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
						+ ",Kit5Plays24h"//39
						+ ")" 
						+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	    		
	    		int counter = 0;
	            StringBuilder builder = new StringBuilder();
	            while(counter < plugin.defaultKitPrefs) {
	            	if(counter == 0) { 
	            		builder.append("f");
	            	} else {
	            		builder.append(" f");
	            	}
	            	
	            	counter++;
	            }
	    		
	    		ps.setString(1, name); //Name
				ps.setString(2, uuid.toString()); //UUID
				ps.setString(3, getDefault(false)); //KitInv
				ps.setString(4, getDefault(true)); //KitArmor
				ps.setString(5, Data.toString()); //Settings
				ps.setString(6, "2 1"); //Queque
				ps.setString(7, ""); //KitInv2
				ps.setString(8, ""); //KitArmor2
				ps.setString(9, builder.toString()); //KitSettings2
				ps.setString(10, ""); //KitInv3
				ps.setString(11, ""); //KitArmor3
				ps.setString(12, builder.toString()); //KitSettings3
				ps.setString(13, ""); //KitInv4
				ps.setString(14, ""); //KitArmor4
				ps.setString(15, builder.toString()); //KitSettings4
				ps.setString(16, ""); //KitSettings5
				ps.setString(17, ""); //KitSettings5
				ps.setString(18, builder.toString()); //KitSettings5
				ps.setString(19, "0"); //FightsWon
				ps.setString(20, "0"); //Fights
				ps.setString(21, "1"); //DefaultKit
				ps.setString(22, "");
				
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
				close(ps);
				plugin.getOneVsOnePlayer(uuid).setDataBaseData(null);
				return true;
	           
	        } catch (SQLException ex) {
	            return false;
	        }
    	} else {
    		try {
    			PreparedStatement ps = getConnection().prepareStatement("INSERT INTO 1vs1Kits ("
	            		+ "PlayerName"
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
	            		+ ",FightsWon"//20
	            		+ ",DefaultKit"
	            		+ ",DisabledMaps"
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
						+ ",Kit5Plays24h"//39
						+ ")" 
						+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	    		
	    		int counter = 0;
	            StringBuilder builder = new StringBuilder();
	            while(counter < plugin.defaultKitPrefs) {
	            	if(counter == 0) { 
	            		builder.append("f");
	            	} else {
	            		builder.append(" f");
	            	}
	            	
	            	counter++;
	            }
	            
	           
	            
	    		
	    		ps.setString(1, name); //Name
				ps.setString(2, uuid.toString()); //UUID
				ps.setString(3, ""); //KitInv
				ps.setString(4, ""); //KitArmor
				ps.setString(5, builder.toString()); //Settings
				ps.setString(6, "2 1"); //Queque
				ps.setString(7, ""); //KitInv2
				ps.setString(8, ""); //KitArmor2
				ps.setString(9, builder.toString()); //KitSettings2
				ps.setString(10, ""); //KitInv3
				ps.setString(11, ""); //KitArmor3
				ps.setString(12, builder.toString()); //KitSettings3
				ps.setString(13, ""); //KitInv4
				ps.setString(14, ""); //KitArmor4
				ps.setString(15, builder.toString()); //KitSettings4
				ps.setString(16, ""); //KitSettings5
				ps.setString(17, ""); //KitSettings5
				ps.setString(18, builder.toString()); //KitSettings5
				ps.setString(19, "0"); //FightsWon
				ps.setString(20, "0"); //Fights
				ps.setString(21, "1"); //DefaultKit
				ps.setString(22, "");
				
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
    			close(ps);
    			plugin.getOneVsOnePlayer(uuid).setDataBaseData(null);
    		    return true;	
        	} catch (SQLException e) {
    			e.printStackTrace();
    		}
        	return false;
    	}
    	
    	
    }
    
    public  static boolean addDefault() {
    	try {
    		PreparedStatement ps = getConnection().prepareStatement("INSERT INTO 1vs1Kits ("
            		+ "PlayerName"
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
            		+ ",FightsWon"//20
            		+ ",DefaultKit"
            		+ ",DisabledMaps"
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
					+ ",Kit5Plays24h"//39
					+ ")" 
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
    		
    		int counter = 0;
            StringBuilder builder = new StringBuilder();
            while(counter < plugin.defaultKitPrefs) {
            	if(counter == 0) { 
            		builder.append("f");
            	} else {
            		builder.append(" f");
            	}
            	
            	counter++;
            }

            
            
            
    		ps.setString(1, "-"); //Name
			ps.setString(2, "default"); //UUID
			ps.setString(3, ""); //KitInv
			ps.setString(4, ""); //KitArmor
			ps.setString(5, builder.toString()); //Settings
			ps.setString(6, "2 1"); //Queque
			ps.setString(7, ""); //KitInv2
			ps.setString(8, ""); //KitArmor2
			ps.setString(9, builder.toString()); //KitSettings2
			ps.setString(10, ""); //KitInv3
			ps.setString(11, ""); //KitArmor3
			ps.setString(12, builder.toString()); //KitSettings3
			ps.setString(13, ""); //KitInv4
			ps.setString(14, ""); //KitArmor4
			ps.setString(15, builder.toString()); //KitSettings4
			ps.setString(16, ""); //KitSettings5
			ps.setString(17, ""); //KitSettings5
			ps.setString(18, builder.toString()); //KitSettings5
			ps.setString(19, "0"); //FightsWon
			ps.setString(20, "0"); //Fights
			ps.setString(21, "1"); //DefaultKit
			ps.setString(22, "");
			
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
			close(ps);
		    return true;	
    	} catch (SQLException e) {
			e.printStackTrace();
		}
    	return false;
    	
    }
    
    
    public static  String getUserName(UUID uuid) {
    	try {
			PreparedStatement ps = getConnection().prepareStatement("SELECT PlayerName FROM 1vs1Kits WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) { 
				String result = rs.getString("PlayerName");
			    close(ps,rs);
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return "NULL";
    }
  
    public  static UUID getUserID(String Name) {
    	try {
			PreparedStatement ps = getConnection().prepareStatement("SELECT UUID FROM 1vs1Kits WHERE PlayerName = ?");
			ps.setString(1, Name);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				UUID result = UUID.fromString(rs.getString("UUID"));
				close(ps,rs);
				return result;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
 
    public static  boolean setDefault(String Kit,boolean Armor) {
    	if(!Armor) {
    		if(isDefaultExists()) {
    			
        		try {
        			PreparedStatement ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET KitInv = ? WHERE UUID = ?");
        			ps.setString(2, "default");
        			ps.setString(1, Kit);
        			ps.executeUpdate();
        			close(ps);
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}
        		return true;
        	} else {
        		addDefault();
        			try {
            			PreparedStatement ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET KitInv = ? WHERE UUID = ?");
    					ps.setString(2, "default");
    					ps.setString(1, Kit);
            			ps.executeUpdate();
            			close(ps);
            			return true;
    				} catch (SQLException e) {
    					e.printStackTrace();
    				}
            		
        		
        		return false;
        		
        	}
    	} else {
    		if(isDefaultExists()) {
        		try {
        			PreparedStatement ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET KitArmor = ? WHERE UUID = ?");
        			ps.setString(2, "default");
        			ps.setString(1, Kit);
        			ps.executeUpdate();
        			close(ps);
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}
        		return true;
        	} else {
        		
        			addDefault();
        			try {
            			PreparedStatement ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET KitArmor = ? WHERE UUID = ?");
    					ps.setString(2, "default");
    					ps.setString(1, Kit);
            			ps.executeUpdate();
            			close(ps);
            			return true;
    				} catch (SQLException e) {
    					e.printStackTrace();
    				}
            		
        		
        		return false;
        		
        	}
    	}
    }
    
    
    public  static boolean setKit(UUID uuid, String Kit, boolean Armor, String kit) {
    	if(!Armor) {
    		if(isUserExists(uuid)) {
        		try {
        			PreparedStatement ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET KitInv" + kit + " = ? WHERE UUID = ?");
        			ps.setString(2, uuid.toString());
        			ps.setString(1, Kit);
        			ps.executeUpdate();
        			close(ps);
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}
        		return true;
        	} else {
        		if(Bukkit.getPlayer(uuid) != null) {
        			addUser(uuid, Bukkit.getPlayer(uuid).getName());
        			try {
            			PreparedStatement ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET KitInv" + kit + " = ? WHERE UUID = ?");
    					ps.setString(2, uuid.toString());
    					ps.setString(1, Kit);
            			ps.executeUpdate();
            			close(ps);
            			return true;
    				} catch (SQLException e) {
    					e.printStackTrace();
    				}
            		
        		}
        		return false;
        		
        	}
    	} else {
    		if(isUserExists(uuid)) {
        		try {
        			PreparedStatement ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET KitArmor" + kit + " = ? WHERE UUID = ?");
        			ps.setString(2, uuid.toString());
        			ps.setString(1, Kit);
        			ps.executeUpdate();
        			close(ps);
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}
        		return true;
        	} else {
        		if(Bukkit.getPlayer(uuid) != null) {
        			addUser(uuid, Bukkit.getPlayer(uuid).getName());
        			try {
            			PreparedStatement ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET KitArmor" + kit + " = ? WHERE UUID = ?");
    					ps.setString(2, uuid.toString());
    					ps.setString(1, Kit);
            			ps.executeUpdate();
            			close(ps);
            			return true;
    				} catch (SQLException e) {
    					e.printStackTrace();
    				}
            		
        		}
        		return false;
        		
        	}
    	}
    }
    
    public  static String getDefault(boolean Armor) {
    	if(!Armor) {
    		try {
    			PreparedStatement ps = getConnection().prepareStatement("SELECT KitInv FROM 1vs1Kits WHERE UUID = ?");
    			ps.setString(1, "default");
    			ResultSet rs = ps.executeQuery();
    			
    			while(rs.next()) {
    				String Inv = rs.getString("KitInv");
    				close(ps,rs);
    				return Inv;
    			}
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	} else {
    		try {
    			PreparedStatement ps = getConnection().prepareStatement("SELECT KitArmor FROM 1vs1Kits WHERE UUID = ?");
    			ps.setString(1, "default");
    			ResultSet rs = ps.executeQuery();
    			while(rs.next()) {
    				String result = rs.getString("KitArmor");
    				close(ps,rs);
    				return result;
    			}
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    	
    	
    	return "";
    }
    
    public  static String getKit(UUID uuid, boolean Armor, String kit) {
    	if(!Armor) {
    		try {
    			PreparedStatement ps = getConnection().prepareStatement("SELECT KitInv" + kit + " FROM 1vs1Kits WHERE UUID = ?");
    			ps.setString(1, uuid.toString());
    			ResultSet rs = ps.executeQuery();
    			
    			while(rs.next()) {
    				String Inv = rs.getString("KitInv" + kit);
    				close(ps,rs);
    				return Inv;
    			}
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	} else {
    		try {
    			PreparedStatement ps = getConnection().prepareStatement("SELECT KitArmor" + kit + " FROM 1vs1Kits WHERE UUID = ?");
    			ps.setString(1, uuid.toString());
    			ResultSet rs = ps.executeQuery();
    			while(rs.next()) {
    				String result = rs.getString("KitArmor" + kit);
    				close(ps,rs);
    				return result;
    			}
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    	
    	
    	return "";
    }
    
    public  static boolean getPrefDefault(int pref) {
    	
    	try {
    		
			PreparedStatement ps = getConnection().prepareStatement("SELECT Settings FROM 1vs1Kits WHERE UUID = ?");
			ps.setString(1, "default");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
			 String[] prefs = rs.getString("Settings").split(" ");
					
			 if(pref >= prefs.length) {
				 close(ps,rs);
				 return false;
			 }
					
			 if(prefs[pref].equalsIgnoreCase("t")) {
				 close(ps,rs);
				 return true;
			 } else {
				 close(ps,rs);
				 return false;
			 }
			 
			}
			close(ps,rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	
    	return false;
    }
    
    public  static boolean getPref(UUID uuid, int pref, String KitID) {
    	
    	try {
    		
			PreparedStatement ps = getConnection().prepareStatement("SELECT Settings FROM 1vs1Kits WHERE UUID = ?");
			if(!KitID.equalsIgnoreCase("") && !KitID.equalsIgnoreCase("1")) {
	          	ps = getConnection().prepareStatement("SELECT KitSettings" + KitID + " FROM 1vs1Kits WHERE UUID = ?");
	    	}
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				
				if(KitID.equalsIgnoreCase("") || KitID.equalsIgnoreCase("1")) {
					String[] prefs = rs.getString("Settings").split(" ");
					
					
					if(pref >= prefs.length) {
						close(ps,rs);
						return false;
					}
					
					if(prefs[pref].equalsIgnoreCase("t")) {
						close(ps,rs);
						return true;
					} else {
						close(ps,rs);
						return false;
					}
				} else {
					String[] prefs = rs.getString("KitSettings" + KitID).split(" ");
					
					
					if(pref >= prefs.length) {
						close(ps,rs);
						return false;
					}
					
					if(prefs[pref].equalsIgnoreCase("t")) {
						close(ps,rs);
						return true;
					} else {
						close(ps,rs);
						return false;
					}
				}
				
				
			}
			close(ps,rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	
    	return false;
    }
   
    public static  String[] getRawPrefDefault() {
    	try {
			PreparedStatement ps = getConnection().prepareStatement("SELECT Settings FROM 1vs1Kits WHERE UUID = ?");
			
			ps.setString(1, "default");
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
			 String[] prefs = rs.getString("Settings").split(" ");
			 close(ps,rs);
			 return prefs;
			}
			close(ps,rs);
		} catch (SQLException e) {}
    	
    	
    	return null;
    }
    
    public  static String[] getRawPref(UUID uuid,String KitID) {
    	try {
			PreparedStatement ps = getConnection().prepareStatement("SELECT Settings FROM 1vs1Kits WHERE UUID = ?");
			if(!KitID.equalsIgnoreCase("")) {
	          	ps = getConnection().prepareStatement("SELECT KitSettings" + KitID + " FROM 1vs1Kits WHERE UUID = ?");
	    	}
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				if(KitID.equalsIgnoreCase("")) {
					String[] prefs = rs.getString("Settings").split(" ");
					close(ps,rs);
					return prefs;
				} else {
					String[] prefs = rs.getString("KitSettings" + KitID).split(" ");
					close(ps,rs);
					return prefs;
				}
				
			}
			close(ps,rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	
    	return null;
    }
    
    public  String getPrefDataStringe(UUID uuid,String KitID) {
    	try {
			PreparedStatement ps = getConnection().prepareStatement("SELECT Settings FROM 1vs1Kits WHERE UUID = ?");
			if(!KitID.equalsIgnoreCase("")) 
	          ps = getConnection().prepareStatement("SELECT KitSettings" + KitID + " FROM 1vs1Kits WHERE UUID = ?");
	    	
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				if(KitID.equalsIgnoreCase("")) {
					String prefs = rs.getString("Settings");
					close(ps,rs);
					return prefs;
				} else {
					String prefs = rs.getString("KitSettings" + KitID);
					close(ps,rs);
					return prefs;
				}
			}
			close(ps,rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	
    	return null;
    }
    
    public static boolean setPrefDefault(int Pref, boolean state) {
		if(isDefaultExists()) {
    		try {
    			if(Pref >= getRawPrefDefault().length)
    			 return false;
    			else {
    				
    				String[] setThis = getRawPrefDefault();
    				
    				if(state) {
    					setThis[Pref] = "t";
    				} else {
    					setThis[Pref] = "f";
    				}
    				
    				boolean first = true;
    				String build = "";
    				for(int i = 0; i < setThis.length; i++) {
    				 if(first) {
    				  build = setThis[i];
    				  first = false;
    				 } else {
    				  build = build + " " + setThis[i];
    				 }
    				}
    				
    				
    				PreparedStatement ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET Settings = ? WHERE UUID = ?");
    				
        			ps.setString(2, "default");
        			ps.setString(1, build);
        			ps.executeUpdate();
        			close(ps);
    			}
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    		return true;
    	} else {
    			addDefault();
    			try {
    				
    				if(Pref > getRawPrefDefault().length)
        				return false;
        			else {
        				
        				String[] setThis = getRawPrefDefault();
        				
        				if(state) {
        					setThis[Pref] = "t";
        				} else {
        					setThis[Pref] = "f";
        				}
        				
        				
        				
        				
        				PreparedStatement ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET Settings = ? WHERE UUID = ?");
        				
            			ps.setString(2, "default");
            			ps.setString(1, setThis.toString());
            			ps.executeUpdate();
            			close(ps);
        			}
				} catch (SQLException e) {
					e.printStackTrace();
				}
    		return false;
    		
    	}
	
    }
    
    public  static boolean setPref(UUID uuid, int Pref, boolean state, String KitID) {
    		if(isUserExists(uuid)) {
        		try {
        			
        			if(Pref >= getRawPref(uuid,KitID).length)
        				return false;
        			else {
        				
        				String[] setThis = getRawPref(uuid,KitID);
        				
        				if(state) {
        					setThis[Pref] = "t";
        				} else {
        					setThis[Pref] = "f";
        				}
        				
        				boolean first = true;
        				String build = "";
        				for(int i = 0; i < setThis.length; i++) {
        				 if(first) {
        				  build = setThis[i];
        				  first = false;
        				 } else {
        				  build = build + " " + setThis[i];
        				 }
        				}
        				
        				
        				PreparedStatement ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET Settings = ? WHERE UUID = ?");
        				if(!KitID.equalsIgnoreCase("")) {
        					ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET KitSettings" + KitID + " = ? WHERE UUID = ?");
        				}
            			ps.setString(2, uuid.toString());
            			ps.setString(1, build);
            			ps.executeUpdate();
            			close(ps);
        			}
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}
        		return true;
        	} else {
        		if(Bukkit.getPlayer(uuid) != null) {
        			addUser(uuid, Bukkit.getPlayer(uuid).getName());
        			try {
        				
        				if(Pref > getRawPref(uuid,KitID).length) {
            				return false;
            			} else {
            				
            				String[] setThis = getRawPref(uuid,KitID);
            				
            				if(state) {
            					setThis[Pref] = "t";
            				} else {
            					setThis[Pref] = "f";
            				}
            				
            				
            				
            				
            				PreparedStatement ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET Settings = ? WHERE UUID = ?");
            				if(KitID.equalsIgnoreCase("")) {
            					ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET KitSettings" + KitID + " = ? WHERE UUID = ?");
            				}
                			ps.setString(2, uuid.toString());
                			ps.setString(1, setThis.toString());
                			ps.executeUpdate();
                			close(ps);
            			}
    				} catch (SQLException e) {
    					e.printStackTrace();
    				}
            		
        		}
        		return false;
        		
        	}
    	
    }

    public  static PlayerQuequePrefs getQuequePrefState(UUID uuid) {
    	
    	Connection conn = null;
        PreparedStatement ps = null;
    	try {
    		
	    	conn = getConnection();
			ps = conn.prepareStatement("SELECT QuequePrefs FROM 1vs1Kits WHERE UUID = ?");
			
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				
				String State = rs.getString("QuequePrefs").split(" ")[0];
				
				
				if(State == null) {
					close(ps,rs);
					return null;
				}
				
				if(State.equalsIgnoreCase("1")) {
					close(ps,rs);
					return PlayerQuequePrefs.ownKit;
				} else if(State.equalsIgnoreCase("2")) {
					close(ps,rs);
					return PlayerQuequePrefs.EnemieKit;
				} else if(State.equalsIgnoreCase("3")) {
					close(ps,rs);
					return PlayerQuequePrefs.RandomKit;
				} else {
					close(ps,rs);
					return null;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	
    	return null;
    }
    
    
    public  static  boolean setQuequePref(UUID uuid, PlayerQuequePrefs state) {
    	
    	Connection conn = null;
    	PreparedStatement ps = null;
    		if(isUserExists(uuid)) {
        		try {
        			
        			if(state == null) {
        				close(ps);
        				return false;
        			} else {
        				
        				int id = 2;
        				
        				
        				if(state == PlayerQuequePrefs.ownKit) {
        					id = 1;
        				} else if(state == PlayerQuequePrefs.EnemieKit) {
        					id = 2;
        				} else if(state == PlayerQuequePrefs.RandomKit) {
        					id = 3;
        				} else {
        					close(ps);
        					return false;
        				}
        				
        				conn = getConnection();
        		    	
        		    	ps = conn.prepareStatement("SELECT QuequePrefs FROM 1vs1Kits WHERE UUID = ?");
        		    	ps.setString(1, uuid.toString());
        		    	ResultSet rs = ps.executeQuery();
    					rs.next();
        		    	
        		    	String save = rs.getString("QuequePrefs");
        		    	
        		    	String[] saveData = save.split(" ");
        		    	saveData[0] = "" + id;
        		    	save = "";
        		    	for(int i = 0; saveData.length > i; i++) {
        		    		save = save+saveData[i]+" ";
        		    	}
        		    	
        		    	
        		    	
        				ps = conn.prepareStatement("UPDATE 1vs1Kits SET QuequePrefs = ? WHERE UUID = ?");
            			ps.setString(2, uuid.toString());
            			ps.setString(1, "" + save);
            			ps.executeUpdate();
            			close(ps,rs);
        			}
        			
        			
        			
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}
        		return true;
        	} else {
        		if(Bukkit.getPlayer(uuid) != null) {
        			addUser(uuid, Bukkit.getPlayer(uuid).getName());
        			try {
        				
        				if(state == null) {
        					close(ps);
            				return false;
            			} else {
            				
            				int id = 2;
            				
            				if(state == PlayerQuequePrefs.ownKit) {
            					id = 1;
            				} else if(state == PlayerQuequePrefs.EnemieKit) {
            					id = 2;
            				} else if(state == PlayerQuequePrefs.RandomKit) {
            					id = 3;
            				} else {
            					close(ps);
            					return false;
            				}
            				
            				conn = getConnection();
            				ps = conn.prepareStatement("UPDATE 1vs1Kits SET QuequePrefs = ? WHERE UUID = ?");
                			ps.setString(2, uuid.toString());
                			ps.setString(1, "" + id);
                			ps.executeUpdate();
                			close(ps);
            			}
        				
            			
    				} catch (SQLException e) {
    					e.printStackTrace();
    				}
            		
        		}
        		return false;
        		
        	}
    }
    
    public  static boolean isMapDisabled(UUID uuid, String MapName) {
    	
    	PreparedStatement ps = null;
    	
    	try {
    		ps = getConnection().prepareStatement("SELECT DisabledMaps FROM 1vs1Kits WHERE UUID = ?");
    		ps.setString(1, uuid.toString());
    		ResultSet rs = ps.executeQuery();
    		
    		
    		
    		
    		while(rs.next()) {
    			String Inv = rs.getString("DisabledMaps");
    			String[] InvS = Inv.split(" ");
    			for(int i = 0; i < InvS.length; i++) {
    				if(InvS[i].equalsIgnoreCase(MapName)) {
    					close(ps,rs);
    					return true;
    				}
    			}
    			close(ps,rs);
    			return false;
    		}
    	} catch (Exception e) {}
    
		return false;
    }
    
    public  static String getDisabledMaps(UUID uuid) {
    	
    	PreparedStatement ps = null;
    	
    	try {
    		ps = getConnection().prepareStatement("SELECT DisabledMaps FROM 1vs1Kits WHERE UUID = ?");
    		ps.setString(1, uuid.toString());
    		ResultSet rs = ps.executeQuery();
    		
    		while(rs.next()) {
    			String Inv = rs.getString("DisabledMaps");
    			close(ps,rs);
    			return Inv;
    		}
    		close(ps,rs);
    	} catch (Exception e) {}
    
		return "";
    }
    
    public  static void setMapDisabled(UUID uuid, String MapName, boolean disabled) {
    	try {
			PreparedStatement ps = null;
	    	ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET DisabledMaps = ? WHERE UUID = ?");
			ps.setString(2, uuid.toString());
			
			String[] disabledList = getDisabledMaps(uuid).split(" ");
			
			for(int i = 0; i < disabledList.length; i++)
			 if(disabledList[i].equalsIgnoreCase(MapName)) disabledList[i] = "";
			
			
			String disabledMaps = "";
			
			
			 
			 for(int i = 0; i < disabledList.length; i++) 
			  disabledMaps = disabledMaps+disabledList[i]+" ";	  
			 
			if(disabled) disabledMaps = disabledMaps+MapName;
			
			
			
			ps.setString(1, disabledMaps);
			ps.executeUpdate();
			close(ps);
		} catch (SQLException e) {}
    }
    
    public  static boolean setStats(UUID uuid, int Higher, String Stat) {
    	
        PreparedStatement ps = null;
      
		if(isUserExists(uuid)) {
    		try {
    			if(Stat.equalsIgnoreCase("FightsWon")) {
    				ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET FightsWon = ? WHERE UUID = ?");
        			ps.setString(2, uuid.toString());
        			int newStat = Integer.parseInt(getStats(uuid, Stat));
        			newStat = newStat+Higher;
        			ps.setString(1, "" + newStat);
        			ps.executeUpdate();
    			} else if(Stat.equalsIgnoreCase("Fights")) {
    				ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET Fights = ? WHERE UUID = ?");
        			ps.setString(2, uuid.toString());
        			int newStat = Integer.parseInt(getStats(uuid, Stat));
        			newStat = newStat+Higher;
        			ps.setString(1, "" + newStat);
        			ps.executeUpdate();
    			}
    			close(ps);
    			
    			
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    		return true;
    	} else {
    		if(Bukkit.getPlayer(uuid) != null) {
    			addUser(uuid, Bukkit.getPlayer(uuid).getName());
    			try {
    				if(Stat.equalsIgnoreCase("FightsWon")) {
        				
        		    	
        				ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET FightsWon = ? WHERE UUID = ?");
            			ps.setString(2, uuid.toString());
            			int newStat = Integer.parseInt(getStats(uuid, Stat));
            			newStat = newStat+Higher;
            			ps.setString(1, "" + newStat);
            			ps.executeUpdate();
        			} else if(Stat.equalsIgnoreCase("Fights")) {
        				
        		    	
        				ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET Fights = ? WHERE UUID = ?");
            			ps.setString(2, uuid.toString());
            			int newStat = Integer.parseInt(getStats(uuid, Stat));
            			newStat = newStat+Higher;
            			ps.setString(1, "" + newStat);
            			ps.executeUpdate();
        			}
    				close(ps);
				} catch (SQLException e) {
					e.printStackTrace();
				}
        		
    		}
    		return false;
    		
    	}
	
    }
    
    public static  String getStats(UUID uuid, String Typ) {
    	
    	PreparedStatement ps = null;
    	if(Typ.equalsIgnoreCase("Fights")) {
    		
    		try {
    			
    			
		    	
    			ps = getConnection().prepareStatement("SELECT Fights FROM 1vs1Kits WHERE UUID = ?");
    			ps.setString(1, uuid.toString());
    			ResultSet rs = ps.executeQuery();
    			
    			
    			
    			
    			while(rs.next()) {
    				
    				String Inv = rs.getString("Fights");
    				close(ps,rs);
    				return Inv;
    			}
    			close(ps,rs);
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	} else if(Typ.equalsIgnoreCase("FightsWon")){
    		try {
    			
		    	
    			ps = getConnection().prepareStatement("SELECT FightsWon FROM 1vs1Kits WHERE UUID = ?");
    			ps.setString(1, uuid.toString());
    			ResultSet rs = ps.executeQuery();
    			
    			while(rs.next()) {
    				String Inv = rs.getString("FightsWon");
    				close(ps,rs);
    				return Inv;
    			}
    			close(ps,rs);
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	}
    		
    	
    	
    	
    	return "0";
    }
    
    public  static boolean setDefaultKit(UUID uuid, String DefaultKit) {
    	
        PreparedStatement ps = null;
      
		if(isUserExists(uuid)) {
    		try {
    			
    				
    		    	
    				ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET DefaultKit = ? WHERE UUID = ?");
        			ps.setString(2, uuid.toString());
        			
        			ps.setString(1, "" + DefaultKit);
        			ps.executeUpdate();
        			close(ps);
    			
    			
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    		return true;
    	} else {
    		if(Bukkit.getPlayer(uuid) != null) {
    			addUser(uuid, Bukkit.getPlayer(uuid).getName());
    			try {
        				ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET DefaultKit = ? WHERE UUID = ?");
            			ps.setString(2, uuid.toString());
            			ps.setString(1, "" + DefaultKit);
            			ps.executeUpdate();
            			close(ps);
				} catch (SQLException e) {
					e.printStackTrace();
				}
        		
    		}
    		return false;
    		
    	}
	
    }
    
    public  static String getDefaultKit(UUID uuid) {
    	PreparedStatement ps = null;
    		try {
    			
    			ps = getConnection().prepareStatement("SELECT DefaultKit FROM 1vs1Kits WHERE UUID = ?");
    			ps.setString(1, uuid.toString());
    			ResultSet rs = ps.executeQuery();
    			

    			while(rs.next()) {
    				
    				String Inv = rs.getString("DefaultKit");
    				close(ps,rs);
    				return Inv;
    			}
    			close(ps,rs);
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	
    		
    	
    	
    	
    	return "1";
    }
    
	public  static boolean setQuequePref2(UUID uuid, PlayerBestOfsPrefs state) {
    	Connection conn = null;
    	PreparedStatement ps = null;
    		if(isUserExists(uuid)) {
        		try {
        			
        			if(state == null) {
        				close(ps);
        				return false;
        			} else {
        				
        				int id = 2;
        				
        				
        				if(state == PlayerBestOfsPrefs.BestOf1) {
        					id = 1;
        				} else if(state == PlayerBestOfsPrefs.BestOf3) {
        					id = 2;
        				} else if(state == PlayerBestOfsPrefs.BestOf5) {
        					id = 3;
        				} else {
        					return false;
        				}
        				
        		    	conn = getConnection();
    					ps = conn.prepareStatement("SELECT QuequePrefs FROM 1vs1Kits WHERE UUID = ?");
    					
    					ps.setString(1, uuid.toString());
    					
    					ResultSet rs = ps.executeQuery();
    					
    					rs.next();
    					String used = rs.getString("QuequePrefs");
    					if(used.split(" ").length >= 2) {
    						//Bukkit.broadcastMessage("test");
    						
            		    	conn = getConnection();
            		    	
	        				ps = conn.prepareStatement("UPDATE 1vs1Kits SET QuequePrefs = ? WHERE UUID = ?");
	            			ps.setString(2, uuid.toString());
	            			
	            			String save = used;
            		    	//Bukkit.broadcastMessage("" + save);
            		    	String[] saveData = save.split(" ");
            		    	saveData[1] = "" + id;
            		    	save = "";
            		    	for(int i = 0; saveData.length > i; i++) {
            		    		save = save+saveData[i]+" ";
            		    	}
            		    	ps.setString(1, save);
	            			ps.executeUpdate();
	            			close(ps,rs);
    					}
        				
        				
        			}
        			close(ps);
        			
        			
        		} catch (SQLException e) {
        			e.printStackTrace();
        		}
        		return true;
        	} else {
        		if(Bukkit.getPlayer(uuid) != null) {
        			addUser(uuid, Bukkit.getPlayer(uuid).getName());
        			setQuequePref2(uuid, state);
        			return true;
        			
            		
        		}
        		return false;
        		
        	}
    	
    }
	
	public  static PlayerBestOfsPrefs getQuequePrefState2(UUID uuid) {
	 	Connection conn = null;
        PreparedStatement ps = null;
    	try {
    		
	    	conn = getConnection();
			ps = conn.prepareStatement("SELECT QuequePrefs FROM 1vs1Kits WHERE UUID = ?");
			
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				
				if(rs.getString("QuequePrefs").split(" ").length >= 2) {
					String State = rs.getString("QuequePrefs").split(" ")[1];
					
					
					if(State == null) {
						close(ps,rs);
						return null;
					}
					
					if(State.equalsIgnoreCase("1")) {
						close(ps,rs);
						return PlayerBestOfsPrefs.BestOf1;
					} else if(State.equalsIgnoreCase("2")) {
						close(ps,rs);
						return PlayerBestOfsPrefs.BestOf3;
					} else if(State.equalsIgnoreCase("3")) {
						close(ps,rs);
						return PlayerBestOfsPrefs.BestOf5;
					} else {
						close(ps,rs);
						return null;
					}
					
				}
				
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	
    	return null;
      }

	
	public  static HashMap<Integer, UUID> Top5Players(boolean timed) {
    	HashMap<Integer, UUID> Top5 = new HashMap<Integer, UUID>();
    	
    	try {
			//ps.setString(1, Name);
			Statement stmt = getConnection().createStatement(
	                   ResultSet.TYPE_SCROLL_INSENSITIVE, //or ResultSet.TYPE_FORWARD_ONLY
	                   ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = stmt.executeQuery("SELECT * from 1vs1Kits order by CAST(FightsWon AS UNSIGNED) desc LIMIT " + plugin.topPlaces);
			int Platz = 1;
			while(rs.next()) {
				
				//Bukkit.broadcastMessage(Platz +". " + rs.getString("SpielerName") + " " + rs.getString("GamesWon"));
				String uuid = rs.getString("UUID");
			   try {
				if(!uuid.toLowerCase().contains("default") && !uuid.toLowerCase().contains("custom")) {
					Top5.put(Platz, UUID.fromString(uuid));
					
					Platz = Platz+1;
				}
				
			   } catch (Exception e) {}
				
				
			   
			}
			close(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
		return Top5;
    }
	
	//POSITION 
	
//	public  static Integer getPosition(UUID uuid, boolean timed) {
//		
//		try {
//			Statement stmt = getConnection().createStatement(
//	                   ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
//			ResultSet rs = stmt.executeQuery("SELECT * from 1vs1Kits order by CAST(FightsWon AS UNSIGNED) desc");
//			int Platz = 1;
//			while(rs.next()) {
//				
//				if(rs.getString("UUID").equalsIgnoreCase(uuid.toString())) {
//					close(rs);
//					return Platz;
//				}
//				String place = rs.getString("UUID");
//				if(!place.toLowerCase().contains("default") && !place.toLowerCase().contains("custom")) {
//					Platz++;
//				}
//			}
//			return -1;
//		} catch (SQLException e) {
//			return -1;
//		}
//		
//    }
	
	public static Integer getPosition(UUID uuid, boolean timed) {
		try {
			if(!timed) {
				Connection con = getConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * from 1vs1Kits order by CAST(FightsWon AS UNSIGNED) desc");

				ResultSet rs = ps.executeQuery();
				int Platz = 1;
				while (rs.next()) {
					String id = rs.getString("UUID");
					if (id.equalsIgnoreCase(uuid.toString())) {
						return Platz;
					}
					
					if (!id.equalsIgnoreCase("default") && !id.toLowerCase().contains("custom")) {
						Platz++;
					}
				}
				return Platz;
			} else {
				Connection con = getConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * from 1vs1Kits order by CAST(FightsWon30 AS UNSIGNED) desc");

				ResultSet rs = ps.executeQuery();
				int Platz = 1;
				while (rs.next()) {
					String id = rs.getString("UUID");
					if (id.equalsIgnoreCase(uuid.toString())) 
						return Platz;
					
				
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
	
	public  static Integer getAllUserEntrys() {
		try {//SELECT * from KitDatabase order by CAST(FightsWon AS UNSIGNED) desc limit 5 1vs1Kits
			
			Statement stmt = getConnection().createStatement(
	                   ResultSet.TYPE_SCROLL_INSENSITIVE, //or ResultSet.TYPE_FORWARD_ONLY
	                   ResultSet.CONCUR_READ_ONLY);

			
			ResultSet rs = stmt.executeQuery("SELECT * from 1vs1Kits order by CAST(FightsWon AS UNSIGNED) desc");
			int Platz = 0;
			while(rs.next()) {
				String place = rs.getString("UUID");
				if(!place.toLowerCase().contains("default") && !place.toLowerCase().contains("custom")) {
					Platz++;
				}
			}
			close(rs);
			return Platz;
		} catch (SQLException e) {
			return -1;
		}
		
    }
	
	public static int isCustomKitExits(String Name) {
		if(isNameRegistered(Name)) {
			return 2;
		}
		
		
		PreparedStatement ps = null;
		try {
			
			
			ps = getConnection().prepareStatement("SELECT PlayerName FROM 1vs1Kits WHERE PlayerName = ?");
			ps.setString(1, Name);
			ResultSet rs = ps.executeQuery();
			boolean data = rs.next();
			ps.close();
			rs.close();
			if(data) {
				close(ps,rs);
				return 1;
			}
			close(ps,rs);
			return 0;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		}
		return 0;
	}
	
	public static void addCustomKit(String Name, String Inv, String Armor, String[] Prefs) {
		
			
			PreparedStatement ps = null;
		
			if(isCustomKitExits(Name) == 1) {
				
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
					
					
					ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET KitInv = ? WHERE PlayerName = ?");
					ps.setString(2, Name);
					ps.setString(1, Inv);//TODO
					ps.executeUpdate();
					
					
					
					ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET KitArmor = ? WHERE PlayerName = ?");
					ps.setString(2, Name);
					ps.setString(1, Armor);
					ps.executeUpdate();
					
					
					
					ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET Settings = ? WHERE PlayerName = ?");
					ps.setString(2, Name);
					ps.setString(1, Data.toString());
					ps.executeUpdate();
					close(ps);
				} catch (SQLException e) {
					e.printStackTrace();
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


					
					ps = getConnection().prepareStatement("INSERT INTO 1vs1Kits (" + "PlayerName" + ",UUID" + ",KitInv"
							+ ",KitArmor" + ",Settings" + ",QuequePrefs" + ",KitInv2" + ",KitArmor2" + ",KitSettings2"
							+ ",KitInv3" + ",KitArmor3" + ",KitSettings3" + ",KitInv4" + ",KitArmor4" + ",KitSettings4"
							+ ",KitInv5" + ",KitArmor5" + ",KitSettings5" + ",Fights" + ",FightsWon" + ",DefaultKit"
							+ ",DisabledMaps)" + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					ps.setString(1, Name);
					ps.setString(2, "CUSTOM " + UUID.randomUUID() + UUID.randomUUID());
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
					close(ps);
					return;
				} catch (SQLException ex) {
					plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
				}
				return;
			
			
		
	}
	
	public static String loadCustomKit(String Name, boolean Armor) {
	
		PreparedStatement ps = null;
		if (!Armor) {
			try {
				

				ps = getConnection().prepareStatement("SELECT KitInv FROM 1vs1Kits WHERE PlayerName = ?");

				ps.setString(1, Name);
				ResultSet rs = ps.executeQuery();
				if (!rs.next()) {
					return null;
				}
				String data = rs.getString("KitInv");
				close(ps,rs);
				return data;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			try {
				
				
				ps = getConnection().prepareStatement("SELECT KitArmor FROM 1vs1Kits WHERE PlayerName = ?");
				ps.setString(1, Name);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					
					String data = rs.getString("KitArmor");
					
					close(ps, rs);
					return data;
				}
				close(ps,rs);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return "";
	}
	
	public static boolean getCustomKitPref(String Name, int id) {
		
			
			PreparedStatement ps = null;
			try {
				
				ps = getConnection().prepareStatement("SELECT Settings FROM 1vs1Kits WHERE PlayerName = ?");
				
				ps.setString(1, Name);

				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					
					
					String[] prefs = rs.getString("Settings").split(" ");
					if (id >= prefs.length || id < 0) {
						close(ps,rs);
						return false;
					}
					if (prefs[id].equalsIgnoreCase("t")) {
						close(ps,rs);
						return true;
					}
					close(ps,rs);
					return false;
				}
			} catch (SQLException ex) {
				plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
			}
			return false;
			
		
	}
	
	public static String[] getCustomKitRawPref(String Name) {
		
		PreparedStatement ps = null;
		try {
			
			ps = getConnection().prepareStatement("SELECT Settings FROM 1vs1Kits WHERE PlayerName = ?");
			
			ps.setString(1, Name);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {

				
				String[] prefs = rs.getString("Settings").split(" ");
				close(ps,rs);
				return prefs;
			}
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
		}
		
		return null;
	}
	
	public static void deleteCustomKit(String Name) {
		
		
		PreparedStatement ps = null;
	
		if(isCustomKitExits(Name) == 1) {
			
			try {
				
				
				ps = getConnection().prepareStatement("DELETE FROM 1vs1Kits WHERE PlayerName = ?");
				
				ps.setString(1, Name);
				ps.executeUpdate();
				
				
				close(ps);
			} catch (SQLException e) {
				e.printStackTrace();
			} 
			
			return;
		}
			return;
		
	}
	
    public static void updateRankPoints(final UUID uuid, final int amount) {
    	Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			
			@Override
			public void run() {
				PreparedStatement ps = null;
		    	try {
					
					
					ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET RankPoints = ? WHERE UUID = ?");
					
					ps.setString(2, uuid.toString());
					int points = getRankPoints(uuid)+amount;
					if(points <= -1) points = 0;
					ps.setString(1, "" + points);
					ps.executeUpdate();
					
					
					close(ps);
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
	    	ps = getConnection().prepareStatement("SELECT RankPoints FROM 1vs1Kits WHERE UUID = ?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int result = Integer.parseInt(rs.getString("RankPoints"));
				close(ps,rs);
				return result;
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

			Connection con = getConnection();
			
			Statement stmt = con.createStatement(
	                   ResultSet.TYPE_SCROLL_INSENSITIVE, //or ResultSet.TYPE_FORWARD_ONLY
	                   ResultSet.CONCUR_READ_ONLY);

			ResultSet rs = stmt.executeQuery("SELECT * from 1vs1Kits order by CAST(RankPoints AS UNSIGNED) desc");
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
			close(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Top5;
	}
    
    public static Integer getPositionRankPoints(UUID uuid) {
		
		try {
			Connection con = getConnection();

			Statement stmt = con.createStatement(
	                   ResultSet.TYPE_SCROLL_INSENSITIVE, //or ResultSet.TYPE_FORWARD_ONLY
	                   ResultSet.CONCUR_READ_ONLY);
			
			
			ResultSet rs = stmt.executeQuery("SELECT * from 1vs1Kits order by CAST(RankPoints AS UNSIGNED) desc");
			int Platz = 1;
			while (rs.next()) {
				String id = rs.getString("UUID");
				if (id.equalsIgnoreCase(uuid.toString())) {
					close(rs);
					return Platz;
				}
				
				if (!id.equalsIgnoreCase("default") && !id.toLowerCase().contains("custom")) {
					Platz++;
				}
			}
			close(rs);
			return Platz;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Integer.valueOf(-1);
	}
	
    public static boolean exists(String row) {
    	try {
			if (!isConnected()) return false;

			
			PreparedStatement ps = getConnection().prepareStatement("SELECT "+ row + " FROM 1vs1Kits");
			
			ResultSet rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			return false;
		}
    }
    
    public static void reset30DayStats() {
		//
		
		try {
			if (!isConnected()) return;

			
			PreparedStatement ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET Fights30 = 0");
			
			ps.executeUpdate();
			ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET FightsWon30 = 0");
			
			ps.executeUpdate();
			
			for(int i = 1; i < 5; i++) {
				ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET Kit" + i + "Plays30 = 0");
				
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
				PreparedStatement ps = getConnection().prepareStatement("UPDATE 1vs1Kits SET Kit" + i + "Plays24h = 0");
				
				ps.executeUpdate();
			}
			
			
			
			return;
		} catch (SQLException e) {
			return;
		}
	}
    
    public static HashMap<Integer, String> Top5Kits(int type) {
		HashMap<Integer, String> Top5 = new HashMap<>();
		if(type == 0) {
			try {
				if (!isConnected()) return null;

				Connection con = getConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * from 1vs1Kits order by CAST(Kit1Plays AS UNSIGNED) desc LIMIT " + plugin.topPlaces);

				
				ResultSet rs = ps.executeQuery();
				int Platz = 1;
				while (rs.next()) {
					String uuid = rs.getString("PlayerName");
					String id = rs.getString("UUID");
					if (!id.equalsIgnoreCase("default")) {
						
						try {
							Top5.put(Integer.valueOf(Platz), uuid);
							Platz++;
						} catch (Exception localException) {}
					}
				}
			} catch (SQLException e) {
				
			}
		} else if(type == 1) {
			try {
				if (!isConnected())
					return null;

				Connection con = getConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * from 1vs1Kits order by CAST(Kit1Plays30 AS UNSIGNED) desc LIMIT " + plugin.topPlaces);

				
				ResultSet rs = ps.executeQuery();
				int Platz = 1;
				while (rs.next()) {
					
					String uuid = rs.getString("PlayerName");
					String id = rs.getString("UUID");
					if (!id.equalsIgnoreCase("default") ) {
						try {
							Top5.put(Integer.valueOf(Platz), uuid);
							Platz++;
						} catch (Exception localException) {}
					}
				}
			} catch (SQLException e) {
				
			}
		} else if(type == 2) {
			try {
				if (!isConnected())
					return null;

				Connection con = getConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * from 1vs1Kits order by CAST(Kit1Plays24h AS UNSIGNED) desc LIMIT " + plugin.topPlaces);

				
				ResultSet rs = ps.executeQuery();
				int Platz = 1;
				while (rs.next()) {
					String uuid = rs.getString("PlayerName");
					String id = rs.getString("UUID");
					if (!id.equalsIgnoreCase("default") ) {
						try {
							Top5.put(Integer.valueOf(Platz), uuid);
							Platz++;
						} catch (Exception localException) {}
					}
				}
			} catch (SQLException e) {
				
			}
		}
		
		return Top5;
	}
    
    public static Integer getPositionKit(String name, int type) {
		
		try {
			
			if(type == 0) {
				Connection con = getConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * from 1vs1Kits order by CAST(Kit1Plays AS UNSIGNED) desc");

				ResultSet rs = ps.executeQuery();
				int Platz = 1;
				while (rs.next()) {
					String id = rs.getString("PlayerName");
					if (id.equalsIgnoreCase(name)) {
						
						return Platz;
					}
					
					if (!id.equalsIgnoreCase("default") && !id.toLowerCase().contains("custom")) {
						Platz++;
					}
				}
				return Platz;
			} else if(type == 1) {
				Connection con = getConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * from 1vs1Kits order by CAST(Kit1Plays30 AS UNSIGNED) desc");

				ResultSet rs = ps.executeQuery();
				int Platz = 1;
				while (rs.next()) {
					String id = rs.getString("PlayerName");
					if (id.equalsIgnoreCase(name)) {
						
						return Platz;
					}
					
					if (!id.equalsIgnoreCase("default") && !id.toLowerCase().contains("custom")) {
						Platz++;
					}
				}
				return Platz;
			} else if(type == 2) {
				Connection con = getConnection();
				PreparedStatement ps = con
						.prepareStatement("SELECT * from 1vs1Kits order by CAST(Kit1Plays24h AS UNSIGNED) desc");

				ResultSet rs = ps.executeQuery();
				int Platz = 1;
				while (rs.next()) {
					String id = rs.getString("PlayerName");
					if (id.equalsIgnoreCase(name)) {
						
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
    
    public static void setStatsKit(String Name, int Higher, int kit, int type) {
		
		PreparedStatement ps = null;
		Connection conn = getConnection();
		
		try {
			if(type == 0) {
				if (kit > 5 || kit < 0) return;
				
				conn = getConnection();
				ps = conn.prepareStatement("UPDATE 1vs1Kits SET Kit" + kit + "Plays = ? WHERE PlayerName = ?");
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
					
					
					ps = conn.prepareStatement("UPDATE 1vs1Kits SET Kit" + kit + "Plays30 = ? WHERE PlayerName = ?");
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
				
				conn = getConnection();
				ps = conn.prepareStatement("UPDATE 1vs1Kits SET Kit" + kit + "Plays24h = ? WHERE PlayerName = ?");
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
		} 
		return;
	}
    
    public static String getStatsKit(String Name, int kit, int type) {
   	 Connection conn = getConnection();
   	 PreparedStatement ps = null;
   	 if(type == 0) {
   		 if (kit > 5 || kit < 0) return "0";
   			try {
   				
   				conn = getConnection();
   				ps = conn.prepareStatement("SELECT Kit" + kit + "Plays FROM 1vs1Kits WHERE PlayerName = ?");
   				ps.setString(1, Name);
   				ResultSet rs = ps.executeQuery();
   				if (!rs.next()) return "0";
   				String data = rs.getString("Kit" + kit + "Plays");
   				
   				if(data == null || data.equalsIgnoreCase("null")) return "0";
   				return data;
   			} catch (SQLException e) {
   				e.printStackTrace();
   			}
   		 
   	  } else if(type == 1) {
   		 if (kit > 5 || kit < 0) return "0";
   			try {
   				
   				conn = getConnection();
   				ps = conn.prepareStatement("SELECT Kit" + kit + "Plays30 FROM 1vs1Kits WHERE PlayerName = ?");
   				ps.setString(1, Name);
   				ResultSet rs = ps.executeQuery();
   				if(!rs.next()) return "0";
   				String data = rs.getString("Kit" + kit + "Plays30");
   				
   				if(data == null || data.equalsIgnoreCase("null")) return "0";
   				return data;
   			} catch (SQLException e) {
   				e.printStackTrace();
   			}
   		} else if(type == 2) {
   			 if (kit > 5 || kit < 0) return "0";
   				try {
   					
   					conn = getConnection();
   					ps = conn.prepareStatement("SELECT Kit" + kit + "Plays24h FROM 1vs1Kits WHERE PlayerName = ?");
   					ps.setString(1, Name);
   					ResultSet rs = ps.executeQuery();
   					if(!rs.next()) return "0";
   					String data = rs.getString("Kit" + kit + "Plays24h");
   					
   					if(data == null || data.equalsIgnoreCase("null")) return "0";
   					return data;
   				} catch (SQLException e) {
   					e.printStackTrace();
   				}
   			} 
   	 

   	 return "0";
   	}
    
}
