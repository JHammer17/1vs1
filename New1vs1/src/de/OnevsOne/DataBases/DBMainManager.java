package de.OnevsOne.DataBases;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.OnevsOne.main;
import de.OnevsOne.DataBases.MySQL.MySQLManager;
import de.OnevsOne.DataBases.SQLite.Database;
import de.OnevsOne.Methods.SimpleAsync;
import de.OnevsOne.States.PlayerBestOfsPrefs;
import de.OnevsOne.States.PlayerQuequePrefs;

public class DBMainManager  {

	private main plugin;

	public DBMainManager(main plugin) {
		this.plugin = plugin;
	}

	public boolean isConnected() {
		if(plugin.useMySQL) {
			return MySQLManager.isConnected();
		} else {
			return Database.isConnected();
		}
	}
	
	public boolean isUserExists(UUID uuid) {
		if(plugin.useMySQL) {
			return MySQLManager.isUserExists(uuid);
		} else {
			return Database.isUserExists(uuid);
		}
	}

	public boolean isDefaultExists() {
		if(plugin.useMySQL) {
			return MySQLManager.isDefaultExists();
		} else {
			return Database.isDefaultExists();
		}
	}

	public boolean isNameRegistered(String Name) {
		if(plugin.useMySQL) {
			return MySQLManager.isNameRegistered(Name);
		} else {
			return Database.isNameRegistered(Name);
		}
	}

	public void updateName(final UUID uuid, final String newName) {
		new SimpleAsync(new Runnable() {
			
			@Override
			public void run() {
				if(plugin.useMySQL) {
					MySQLManager.updateName(uuid, newName);
				} else {
					Database.updateUserName(uuid, newName);
				}
				
			}
		}, plugin).start();
		
	}
	
	public void addUser(final UUID uuid, final String name) {
		new SimpleAsync(new Runnable() {
			
			@Override
			public void run() {
				if(plugin.useMySQL) {
					MySQLManager.addUser(uuid, name);
				} else {
					Database.addUser(uuid, name);
				}
			}
		}, plugin).start();
		
	}
	
	public void updatePref(final UUID uuid, final String KitID) {
		new SimpleAsync(new Runnable() {
			
		  @Override
		  public void run() {
		    if(plugin.useMySQL) {
			 MySQLManager.updatePref(uuid, KitID);
		 	} else {
			 Database.updatePref(uuid, KitID);
		    }
		  }
		}, plugin).start();
	}

	public void updatePrefDefault() {
		new SimpleAsync(new Runnable() {
			
			@Override
			public void run() {
				if(plugin.useMySQL) {
					MySQLManager.updatePrefDefault();
				} else {
					Database.updatePrefDefault();
				}
			}
		}, plugin).start();
		
	}
	
	public void addDefault() {
//		new SimpleAsync(new Runnable() {
//			
//			@Override
//			public void run() {
				if(plugin.useMySQL) {
					MySQLManager.addDefault();
				} else {
					Database.addDefault();
				}
//			}
//		}, plugin).start();
		
	}
	
	public String getUserName(UUID uuid) {
		
		if(plugin.getOneVsOnePlayer(uuid).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(uuid).getDataBaseData();
			if(datas.containsKey("Name") && datas.get("Name") != null) return (String) datas.get("Name");
		}
		
		if(plugin.useMySQL) {
			return MySQLManager.getUserName(uuid);
		} else {
			return Database.getUserName(uuid);
		}
	}
	
	public UUID getUUID(String Name) {
		if(plugin.getOneVsOnePlayer(Bukkit.getPlayer(Name)).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(Bukkit.getPlayer(Name)).getDataBaseData();
			if(datas.containsKey("UUID") && datas.get("UUID") != null) return (UUID) datas.get("UUID");
		}
		
		if(plugin.useMySQL) {
			return MySQLManager.getUserID(Name);
		} else {
			return Database.getUserID(Name);
		}
	}
	
	public boolean getPrefDefault(int pref) {
		if(plugin.useMySQL) {
			return MySQLManager.getPrefDefault(pref);
		} else {
			return Database.getPrefDefault(pref);
		}
	}

	public boolean getPref(UUID uuid, int pref, String KitID) {
		
		if(plugin.getOneVsOnePlayer(uuid).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(uuid).getDataBaseData();
			String subID = KitID;
			if(KitID.equalsIgnoreCase("d")) subID = getDefaultKit(uuid);
			if(KitID.equalsIgnoreCase("")) subID = "1";
			if(datas.containsKey("Kit" + subID + "Settings") && datas.get("Kit" + subID + "Settings") != null) {
				String[] prefs = (String[]) datas.get("Kit" + subID + "Settings");
				return prefs[pref].equalsIgnoreCase("t");
			}
		}
		
		if(plugin.useMySQL) {
			return MySQLManager.getPref(uuid,pref,KitID);
		} else {
			return Database.getPref(uuid,pref,KitID);
		}
	}

	public String[] getRawPrefDefault() {
		if(plugin.useMySQL) {
			return MySQLManager.getRawPrefDefault();
		} else {
			return Database.getRawPrefDefault();
		}
	}
	
	public String[] getRawPref(UUID uuid, String KitID) {
		if(plugin.getOneVsOnePlayer(uuid).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(uuid).getDataBaseData();
			String subID = KitID;
			if(KitID.equalsIgnoreCase("d")) subID = getDefaultKit(uuid);
			if(KitID.equalsIgnoreCase("")) subID = "1";
			if(datas.containsKey("Kit" + subID + "Settings") && datas.get("Kit" + subID + "Settings") != null) {
				String[] prefs = (String[]) datas.get("Kit" + subID + "Settings");
				return prefs;
			}
		}
		if(plugin.useMySQL) {
			return MySQLManager.getRawPref(uuid, KitID);
		} else {
			return Database.getRawPref(uuid, KitID);
		}
	}
	
	public void setDefaultKit(final String Kit, final boolean Armor) {
//		new SimpleAsync(new Runnable() {
//			
//			@Override
//			public void run() {
				if(plugin.useMySQL) {
					MySQLManager.setDefault(Kit, Armor);
				} else {
					Database.setKitDefault(Kit, Armor);
				}
//			}
//		}, plugin).start();
		
	}
	
	
	public void setKit(final UUID uuid, final String Kit, final boolean Armor, final String KitID) {
		
		if(plugin.getOneVsOnePlayer(uuid).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(uuid).getDataBaseData();
			String subID = KitID;
			if(KitID.equalsIgnoreCase("d")) subID = getDefaultKit(uuid);
			if(KitID.equalsIgnoreCase("")) subID = "1";
			if(!Armor) {
				if(datas.containsKey("Kit" + subID + "Inv") && datas.get("Kit" + subID + "Inv") != null) {
					datas.remove("Kit" + subID + "Inv");
					datas.put("Kit" + subID + "Inv", Kit);
					plugin.getOneVsOnePlayer(uuid).setDataBaseData(datas);
					plugin.getOneVsOnePlayer(uuid).setDataBaseDataName(datas);
				}
			} else {
				if(datas.containsKey("Kit" + subID + "Armor") && datas.get("Kit" + subID + "Armor") != null) {
					datas.remove("Kit" + subID + "Armor");
					datas.put("Kit" + subID + "Armor", Kit);
					
					plugin.getOneVsOnePlayer(uuid).setDataBaseData(datas);
					plugin.getOneVsOnePlayer(uuid).setDataBaseDataName(datas);
				}
			}
			
		}
		
		new SimpleAsync(new Runnable() {
			
			@Override
			public void run() {
				
				
				if(plugin.useMySQL) {
					MySQLManager.setKit(uuid, Kit, Armor, KitID);
				} else {
					Database.setKit(uuid, Kit, Armor, KitID);
				}
			}
		}, plugin).start();
		
	}
	
	public String getKitDefault(boolean Armor) {
		if(plugin.useMySQL) {
			return MySQLManager.getDefault(Armor);
		} else {
			return Database.getKitDefault(Armor);
		}
	}
	
	
	public String getKit(UUID uuid, boolean Armor, String KitID) {
		
		if(plugin.getOneVsOnePlayer(uuid).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(uuid).getDataBaseData();
			String subID = KitID;
			if(KitID.equalsIgnoreCase("d")) subID = getDefaultKit(uuid);
			if(KitID.equalsIgnoreCase("")) subID = "1";
			if(!Armor) {
				if(datas.containsKey("Kit" + subID + "Inv") && datas.get("Kit" + subID + "Inv") != null) {
					return (String) datas.get("Kit" + subID + "Inv");
				}
			} else {
				if(datas.containsKey("Kit" + subID + "Armor") && datas.get("Kit" + subID + "Armor") != null) {
					return (String) datas.get("Kit" + subID + "Armor");
				}
			}
			
		}
		
		if(plugin.useMySQL) {
			return MySQLManager.getKit(uuid, Armor, KitID);
		} else {
			return Database.getKit(uuid, Armor, KitID);
		}
	}
	
	public void setPrefDefault(final int Pref, final boolean state) {
//		new SimpleAsync(new Runnable() {
//			
//			@Override
//			public void run() {
				if(plugin.useMySQL) {
					MySQLManager.setPrefDefault(Pref, state);
				} else {
					Database.setPrefDefault(Pref, state);
				}
//			}
//		}, plugin).start();
		
	}
	
	public void setPref(final UUID uuid, final int Pref, final boolean state, final String KitID) {
		
		if(plugin.getOneVsOnePlayer(uuid).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(uuid).getDataBaseData();
			String subID = KitID;
			if(KitID.equalsIgnoreCase("d")) subID = getDefaultKit(uuid);
			if(KitID.equalsIgnoreCase("")) subID = "1";
			
				if(datas.containsKey("Kit" + subID + "Settings") && datas.get("Kit" + subID + "Settings") != null) {
					String[] prefs = getRawPref(uuid, subID);
					datas.remove("Kit" + subID + "Settings");
					prefs[Pref] = state ? "t" : "f";
					datas.put("Kit" + subID + "Settings", prefs);
					plugin.getOneVsOnePlayer(uuid).setDataBaseData(datas);
					plugin.getOneVsOnePlayer(uuid).setDataBaseDataName(datas);
				}
			
			
		}
		new SimpleAsync(new Runnable() {
			
			@Override
			public void run() {
				if(plugin.useMySQL) {
					MySQLManager.setPref(uuid, Pref, state, KitID);
				} else {
					Database.setPref(uuid, Pref, state, KitID);
				}
			}
		}, plugin).start();
		
	}
	
	
	
	public PlayerQuequePrefs getQuequePrefState(UUID uuid) {
		if(plugin.getOneVsOnePlayer(uuid).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(uuid).getDataBaseData();
			if(datas.containsKey("QueuePrefs") && datas.get("QueuePrefs") != null) {
			 return (PlayerQuequePrefs) datas.get("QueuePrefs");
			}
		}
		
		if(plugin.useMySQL) {
			return MySQLManager.getQuequePrefState(uuid);
		} else {
			return Database.getQuequePrefState(uuid);
		}
	}
	
	public void setQueuePref(final UUID uuid, final PlayerQuequePrefs state) {
		if(plugin.getOneVsOnePlayer(uuid).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(uuid).getDataBaseData();
			
			
			
				if(datas.containsKey("QueuePrefs") && datas.get("QueuePrefs") != null) {
					datas.remove("QueuePrefs");
					datas.put("QueuePrefs", state);
					plugin.getOneVsOnePlayer(uuid).setDataBaseData(datas);
					plugin.getOneVsOnePlayer(uuid).setDataBaseDataName(datas);
				}
			
			
		}
		new SimpleAsync(new Runnable() {
			
			@Override
			public void run() {
				if(plugin.useMySQL) {
					MySQLManager.setQuequePref(uuid, state);
				} else {
					Database.setQuequePref(uuid, state);
				}
			}
		}, plugin).start();
		
	}
	
	public boolean isMapDisabled(UUID uuid, String MapName) {
		if(plugin.getOneVsOnePlayer(uuid).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(uuid).getDataBaseData();
			if(datas.containsKey("DisabledMaps") && datas.get("DisabledMaps") != null) {
			 String[] maps = ((String) datas.get("DisabledMaps")).split(" ");
			 for(int i = 0; i < maps.length; i++) if(maps[i].equalsIgnoreCase(MapName)) return true;
			 return false;
			}
		}
		
		if(plugin.useMySQL) {
			return MySQLManager.isMapDisabled(uuid, MapName);
		} else {
			return Database.isMapDisabled(uuid, MapName);
		}
	}
	
	public String getDisabledMaps(UUID uuid) {
		if(plugin.getOneVsOnePlayer(uuid).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(uuid).getDataBaseData();
			if(datas.containsKey("DisabledMaps") && datas.get("DisabledMaps") != null) {
			 return ((String) datas.get("DisabledMaps"));
			}
		}
		if(plugin.useMySQL) {
			return MySQLManager.getDisabledMaps(uuid);
		} else {
			return Database.getDisabledMaps(uuid);
		}
	}
	
	public void setMapDisabled(final UUID uuid, final String MapName, final boolean disabled) {
		
		if(plugin.getOneVsOnePlayer(uuid).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(uuid).getDataBaseData();
			
			
			
				if(datas.containsKey("DisabledMaps") && datas.get("DisabledMaps") != null) {
					
					datas.remove("DisabledMaps");

					String[] disabledList = getDisabledMaps(uuid).split(" ");
					
					for(int i = 0; i < disabledList.length; i++) {
					 if(disabledList[i].equalsIgnoreCase(MapName)) disabledList[i] = "";
					}
					
					String disabledMaps = "";
					
					for(int i = 0; i < disabledList.length; i++) disabledMaps = disabledMaps+disabledList[i]+" ";	  
					
					if(disabled) {
					 disabledMaps = disabledMaps+MapName;
					}
				
					datas.put("DisabledMaps", disabledMaps);
					plugin.getOneVsOnePlayer(uuid).setDataBaseData(datas);
					plugin.getOneVsOnePlayer(uuid).setDataBaseDataName(datas);
				}
			
			
		}
		new SimpleAsync(new Runnable() {
			
			@Override
			public void run() {
				if(plugin.useMySQL) {
					MySQLManager.setMapDisabled(uuid, MapName, disabled);
				} else {
					Database.setMapDisabled(uuid, MapName, disabled);
				}
			}
		}, plugin).start();
		
	}
	
	/**
	 * 
	 * @param uuid
	 * @param Higher
	 * @param Stat (FightsWon and Fights can be used) 
	 */
	public void setStats(final UUID uuid, final int Higher, final String Stat, final boolean timed) {
		if(plugin.getOneVsOnePlayer(uuid).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(uuid).getDataBaseData();
			
				if(Stat.equalsIgnoreCase("FightsWon")) {
					if(datas.containsKey("Wins") && datas.get("Wins") != null) {
						int wins = 0;
						if(getStats(uuid, "FightsWon", timed) != null) 
							wins = Integer.parseInt(getStats(uuid, "FightsWon", timed));
						
							
						
						if(!timed) {
							datas.remove("Wins");
							wins += Higher;
							datas.put("Wins", "" + wins);
							plugin.getOneVsOnePlayer(uuid).setDataBaseData(datas);
							plugin.getOneVsOnePlayer(uuid).setDataBaseDataName(datas);
						} else {
							datas.remove("Wins30");
							wins += Higher;
							datas.put("Wins30", "" + wins);
							
							plugin.getOneVsOnePlayer(uuid).setDataBaseData(datas);
							plugin.getOneVsOnePlayer(uuid).setDataBaseDataName(datas);
						}
						
					}
				} else {
					if(datas.containsKey("Fights") && datas.get("Fights") != null) {
						
						int wins = 0;
						if(getStats(uuid, "Fights", timed) != null)
							wins = Integer.parseInt(getStats(uuid, "Fights", timed));
						
						
						if(!timed) {
							datas.remove("Fights");
							wins += Higher;
							datas.put("Fights", "" + wins);
							plugin.getOneVsOnePlayer(uuid).setDataBaseData(datas);
							plugin.getOneVsOnePlayer(uuid).setDataBaseDataName(datas);
						} else {
							datas.remove("Fights30");
							wins += Higher;
							datas.put("Fights30", "" + wins);
							plugin.getOneVsOnePlayer(uuid).setDataBaseData(datas);
							plugin.getOneVsOnePlayer(uuid).setDataBaseDataName(datas);
						}
						
					}
				}
			
				
		}
		
		new SimpleAsync(new Runnable() {
			
			@Override
			public void run() {
				if(plugin.useMySQL) {
					MySQLManager.setStats(uuid, Higher, Stat);
				} else {
					Database.setStats(uuid, Higher, Stat, timed);
				}
			}
		}, plugin).start();
		
	}
	
	public void setStatsKit(final String name, final int Higher, final int kit, final int type) {
		new SimpleAsync(new Runnable() {
			
			@Override
			public void run() {
				String fName = "";
				if(name.contains(":")) fName = name.split(":")[0];
				else fName = name;
				if(plugin.useMySQL) {
					MySQLManager.setStatsKit(fName, Higher, kit, type);
				} else {
					Database.setStatsKit(fName, Higher, kit, type);
				}
			}
		}, plugin).start();
		
	}
	
	/**
	 * @param uuid - UUID of Player
	 * @param Typ - 0: Normal 1: 30d 2: 24h
	 * @return Stats
	 */
	public String getStatsKit(String name, int kit, int type) {
		if(name.contains(":")) name = name.split(":")[0];
		
		if(plugin.useMySQL) {
			return MySQLManager.getStatsKit(name, kit, type);
		} else {
			return Database.getStatsKit(name, kit, type);
		}
		
	}
	
	
	/**
	 * @param uuid - UUID of Player
	 * @param Typ - FightsWon and Fights
	 * @return Stats
	 */
	public String getStats(UUID uuid, String Typ, boolean timed) {
		if(plugin.getOneVsOnePlayer(uuid).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(uuid).getDataBaseData();
			
			if(!timed) {
				if(Typ.equalsIgnoreCase("FightsWon")) {
					if(datas.containsKey("Wins") && datas.get("Wins") != null) 
						return (String)  datas.get("Wins");
					
					
				} else {
					if(datas.containsKey("Fights") && datas.get("Fights") != null)
						return (String) datas.get("Fights");
					
					
				}
			} else {
				
				if(Typ.equalsIgnoreCase("FightsWon")) {
					if(datas.containsKey("Wins30") && datas.get("Wins30") != null) 
						return (String)  datas.get("Wins30");
					
					
				} else {
					if(datas.containsKey("Fights30") && datas.get("Fights30") != null) 
						
						return (String) datas.get("Fights30");
					
					
				}
			}
			
			
		}	
		
			
		if(plugin.useMySQL) {
			return MySQLManager.getStats(uuid, Typ);
		} else {
			return Database.getStats(uuid, Typ, timed);
		}
	}
	
	public void setDefaultKit(final UUID uuid, final String ID) {
		
			
			
		if(plugin.getOneVsOnePlayer(uuid).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(uuid).getDataBaseData();
				String subID = ID;
				if(ID.equalsIgnoreCase("d")) subID = getDefaultKit(uuid);
				if(ID.equalsIgnoreCase("")) subID = "1";
				
				if(datas.containsKey("DefaultKit") && datas.get("DefaultKit") != null) {
				 datas.remove("DefaultKit");
				 datas.put("DefaultKit", subID);
				 plugin.getOneVsOnePlayer(uuid).setDataBaseData(datas);
				 plugin.getOneVsOnePlayer(uuid).setDataBaseDataName(datas);
				}
					
			}
			
		new SimpleAsync(new Runnable() {
				
			@Override
			public void run() {
				if(plugin.useMySQL) {
					MySQLManager.setDefaultKit(uuid, ID);
				} else {
					Database.setDefaultKit(uuid, ID);
				}
			}
		}, plugin).start();
		
	}
	
	public String getDefaultKit(UUID uuid) {
		if(plugin.getOneVsOnePlayer(uuid).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(uuid).getDataBaseData();
			if(datas.containsKey("DefaultKit") && datas.get("DefaultKit") != null) {
				return (String) datas.get("DefaultKit");
			}
		}
		if(plugin.useMySQL) {
			return MySQLManager.getDefaultKit(uuid);
		} else {
			return Database.getDefaultKit(uuid);
		}
	}
	
	public void setQueuePref2(final UUID uuid, final PlayerBestOfsPrefs state) {
		if(plugin.getOneVsOnePlayer(uuid).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(uuid).getDataBaseData();
		 if(datas.containsKey("QueueBestOf") && datas.get("QueueBestOf") != null) {
			datas.remove("QueueBestOf");
			datas.put("QueueBestOf", state);
			plugin.getOneVsOnePlayer(uuid).setDataBaseData(datas);
			plugin.getOneVsOnePlayer(uuid).setDataBaseDataName(datas);
		 }
		}
			
		new SimpleAsync(new Runnable() {
			
			@Override
			public void run() {
				if(plugin.useMySQL) {
					MySQLManager.setQuequePref2(uuid, state);
				} else {
					Database.setQuequePref2(uuid, state);
				}
			}
		}, plugin).start();
		
	}
	
	public PlayerBestOfsPrefs getQueuePrefState2(UUID uuid) {
		
		if(plugin.getOneVsOnePlayer(uuid).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(uuid).getDataBaseData();
			if(datas.containsKey("QueueBestOf") && datas.get("QueueBestOf") != null) {
			 return (PlayerBestOfsPrefs) datas.get("QueueBestOf");
			}
		}
		
		if(plugin.useMySQL) {
			return MySQLManager.getQuequePrefState2(uuid);
		} else {
			return Database.getQuequePrefState2(uuid);
		}
	}
	
	public HashMap<Integer, UUID> Top5Players(boolean timed) {
		if(plugin.useMySQL) {
			return MySQLManager.Top5Players(timed);
		} else {
			return Database.Top5Players(timed);
		}
	}
	
	public int getPosition(UUID uuid, boolean timed) {
		
		if(plugin.useMySQL) {
			return MySQLManager.getPosition(uuid, timed);
		} else {
			return Database.getPosition(uuid, timed);
		}
	}
	
	public HashMap<Integer, String> Top5Kits(int type) {
		try {
			if(plugin.useMySQL) {
				return MySQLManager.Top5Kits(type);
			} else {
				return Database.Top5Kits(type);
			}
		} catch (Exception e) {}
		return new HashMap<Integer, String>();
		
		
	}
	
	public int getPositionKit(String name, int type) {
		
		if(plugin.useMySQL) {
			return MySQLManager.getPositionKit(name, type);
		} else {
			return Database.getPositionKit(name, type);
		}
		
	}
	
	public void createCustomKit(final String Name, final String Items, final String Armor, final String[] prefs) {
		new SimpleAsync(new Runnable() {
			
			@Override
			public void run() {
				if(plugin.useMySQL) {
					MySQLManager.addCustomKit(Name, Items, Armor, prefs);
				} else {
					Database.addCustomKit(Name, Items, Armor, prefs);
				}
			}
		}, plugin).start();
		
			
	}
	
	/**
	 * @return:
	 *  0: Custom Kit not found!
	 *  1: Custom Kit found! 
	 *  2: Kit of an User!
	 */
	public int isCustomKitExists(String Name) {
		if(plugin.useMySQL) {
			return MySQLManager.isCustomKitExits(Name);
		} else {
			return Database.isCustomKitExits(Name);
		}
		
		
	}
	
	public String loadCustomKitInv(String Name) {
		if(plugin.useMySQL) {
			return MySQLManager.loadCustomKit(Name, false);
		} else {
			return Database.loadCustomKit(Name, false);
		}
	
	}
	
	public String loadCustomKitArmor(String Name) {
		if(plugin.useMySQL) {
			return MySQLManager.loadCustomKit(Name, true);
		} else {
			return Database.loadCustomKit(Name, true);
		}
		
	}

	public boolean getCustomKitPref(String Name, int pref) {
		if(plugin.useMySQL) {
			return MySQLManager.getCustomKitPref(Name, pref);
		} else {
			return Database.getCustomKitPref(Name, pref);
		}
		
	}
	
	public String[] loadCustomKitRawPref(String Name) {
		if(plugin.useMySQL) {
			return MySQLManager.getCustomKitRawPref(Name);
		} else {
			return Database.getCustomKitRawPref(Name);
		}
		
	}
	
	public void deleteCustomKit(final String Name) {
		
		new SimpleAsync(new Runnable() {
			
			@Override
			public void run() {
				if(plugin.useMySQL) {
					MySQLManager.deleteCustomKit(Name);
				} else {
					Database.deleteCustomKit(Name);
				}
			}
		}, plugin).start();
	}
	
	public void deleteKit(final UUID Name, final String SubID) {
		
		new SimpleAsync(new Runnable() {
			
			@Override
			public void run() {
				String subID = SubID;
				if(SubID.equalsIgnoreCase("d")) subID = getDefaultKit(Name);
				if(SubID.equalsIgnoreCase("1")) subID = "";
				setKit(Name, "", false, subID);
				setKit(Name, "", true, subID);
				
				
				for(int x = 0; x < plugin.defaultKitPrefs; x++) setPref(Name, x, false, subID);
			}
		}, plugin).start();
			
		
		
	}
	
	public int getAllUserEntrys() {
		if(plugin.useMySQL) {
			return MySQLManager.getAllUserEntrys();
		} else {
			return Database.getAllUserEntrys();
		}
	}
	
	public void updateRankPoints(final UUID uuid, final int amount) {
		
		if(plugin.getOneVsOnePlayer(uuid).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(uuid).getDataBaseData();
			
			
				if(datas.containsKey("RankPoints") && datas.get("RankPoints") != null) {
					int points = getRankPoints(uuid);
					datas.remove("RankPoints");
					
					points +=amount;
					if(points <= 0) {
						datas.put("RankPoints", 0);
						return;
					}
					datas.put("RankPoints", points);
					plugin.getOneVsOnePlayer(uuid).setDataBaseData(datas);
					plugin.getOneVsOnePlayer(uuid).setDataBaseDataName(datas);
				}
					
		}
		new SimpleAsync(new Runnable() {
			
			@Override
			public void run() {
				if(plugin.useMySQL) {
					MySQLManager.updateRankPoints(uuid, amount);
				} else {
					Database.updateRankPoints(uuid, amount);
				}
			}
		}, plugin).start();
		
	}
	
	public int getRankPoints(UUID uuid) {
		
		if(plugin.getOneVsOnePlayer(uuid).getDataBaseData() != null) {
			HashMap<String, Object> datas = plugin.getOneVsOnePlayer(uuid).getDataBaseData();	
		 if(datas.containsKey("RankPoints") && datas.get("RankPoints") != null)  {
			 return (int) datas.get("RankPoints"); 
		 }
		}
		
		if(plugin.useMySQL) {
			return MySQLManager.getRankPoints(uuid);
		} else {
			return Database.getRankPoints(uuid);
		}
	}
	
	public void reset30DayStats() {
		if(plugin.useMySQL) {
			MySQLManager.reset30DayStats();
		} else {
			Database.reset30DayStats();
		}
	}
	
	public void reset24hStats() {
		if(plugin.useMySQL) {
			MySQLManager.reset24hStats();
		} else {
			Database.reset24hStats();
		}
	}
	
	
	
	
	public void loadUserData(final Player p) {
		new SimpleAsync(new Runnable() {
			public void run() {
				
				HashMap<String, Object> data = new HashMap<>();
				
				String Name = p.getName();
				UUID uuid = p.getUniqueId();
				String Kit1Inv = getKit(p.getUniqueId(), false, "");
				String Kit1Armor = getKit(p.getUniqueId(), true, "");
				String[] Kit1Settings = getRawPref(p.getUniqueId(), "");
				PlayerQuequePrefs QueuePrefs = getQuequePrefState(p.getUniqueId());
				PlayerBestOfsPrefs QueueBestOf = getQueuePrefState2(p.getUniqueId());
				String Kit2Inv = getKit(p.getUniqueId(), false, "2");
				String Kit2Armor = getKit(p.getUniqueId(), true, "2");
				String[] Kit2Settings = getRawPref(p.getUniqueId(), "2");
				String Kit3Inv = getKit(p.getUniqueId(), false, "3");
				String Kit3Armor = getKit(p.getUniqueId(), true, "3");
				String[] Kit3Settings = getRawPref(p.getUniqueId(), "3");
				String Kit4Inv = getKit(p.getUniqueId(), false, "4");;
				String Kit4Armor = getKit(p.getUniqueId(), true, "4");
				String[] Kit4Settings = getRawPref(p.getUniqueId(), "4");
				String Kit5Inv = getKit(p.getUniqueId(), false, "5");
				String Kit5Armor = getKit(p.getUniqueId(), true, "5");
				String[] Kit5Settings = getRawPref(p.getUniqueId(), "5");
				String Fights = getStats(p.getUniqueId(), "Fights", false);
				String Wins = getStats(p.getUniqueId(), "FightsWon", false);
				String Fights30 = getStats(p.getUniqueId(), "Fights", true);
				String Wins30 = getStats(p.getUniqueId(), "FightsWon", true);
				String DefaultKit = getDefaultKit(p.getUniqueId());
				String DisabledMaps = getDisabledMaps(p.getUniqueId());
				int RankPoints = getRankPoints(p.getUniqueId());
				
				data.put("Name", Name);
				data.put("UUID", uuid);
				data.put("Kit1Inv", Kit1Inv);
				data.put("Kit1Armor", Kit1Armor);
				data.put("Kit1Settings", Kit1Settings);
				data.put("QueuePrefs", QueuePrefs);
				data.put("QueueBestOf", QueueBestOf);
				data.put("Kit2Inv", Kit2Inv);
				data.put("Kit2Armor", Kit2Armor);
				data.put("Kit2Settings", Kit2Settings);
				data.put("Kit3Inv", Kit3Inv);
				data.put("Kit3Armor", Kit3Armor);
				data.put("Kit3Settings", Kit3Settings);
				data.put("Kit4Inv", Kit4Inv);
				data.put("Kit4Armor", Kit4Armor);
				data.put("Kit4Settings", Kit4Settings);
				data.put("Kit5Inv", Kit5Inv);
				data.put("Kit5Armor", Kit5Armor);
				data.put("Kit5Settings", Kit5Settings);
				data.put("Fights", Fights);
				data.put("Wins", Wins);
				data.put("DefaultKit", DefaultKit);
				data.put("DisabledMaps", DisabledMaps);
				data.put("RankPoints", RankPoints);
				data.put("Wins30", Wins30);
				data.put("Fights30", Fights30);
				
				plugin.getOneVsOnePlayer(uuid).setDataBaseData(data);
				plugin.getOneVsOnePlayer(uuid).setDataBaseDataName(data);
			}
		}, plugin).start();
	}
	
	public boolean checkAllRowsExists() {
		if(plugin.useMySQL) {
			if(!MySQLManager.exists("PlayerName")) return false;
			if(!MySQLManager.exists("UUID")) return false;
			if(!MySQLManager.exists("KitInv")) return false;
			if(!MySQLManager.exists("KitArmor")) return false;
			if(!MySQLManager.exists("Settings")) return false;
			if(!MySQLManager.exists("QuequePrefs")) return false;
			if(!MySQLManager.exists("KitInv2")) return false;
			if(!MySQLManager.exists("KitArmor2")) return false;
			if(!MySQLManager.exists("KitSettings2")) return false;
			if(!MySQLManager.exists("KitInv3")) return false;
			if(!MySQLManager.exists("KitArmor3")) return false;
			if(!MySQLManager.exists("KitSettings3")) return false;
			if(!MySQLManager.exists("KitInv4")) return false;
			if(!MySQLManager.exists("KitArmor4")) return false;
			if(!MySQLManager.exists("KitSettings4")) return false;
			if(!MySQLManager.exists("KitInv5")) return false;
			if(!MySQLManager.exists("KitArmor5")) return false;
			if(!MySQLManager.exists("KitSettings5")) return false;
			if(!MySQLManager.exists("Fights")) return false;
			if(!MySQLManager.exists("FightsWon")) return false;
			if(!MySQLManager.exists("DefaultKit")) return false;
			if(!MySQLManager.exists("DisabledMaps")) return false;
			if(!MySQLManager.exists("RankPoints")) return false;
			
			if(!MySQLManager.exists("Fights30")) return false;
			if(!MySQLManager.exists("FightsWon30")) return false;
			
			if(!MySQLManager.exists("Kit1Plays")) return false;
			if(!MySQLManager.exists("Kit1Plays30")) return false;
			if(!MySQLManager.exists("Kit2Plays")) return false;
			if(!MySQLManager.exists("Kit2Plays30")) return false;
			if(!MySQLManager.exists("Kit3Plays")) return false;
			if(!MySQLManager.exists("Kit3Plays30")) return false;
			if(!MySQLManager.exists("Kit4Plays")) return false;
			if(!MySQLManager.exists("Kit4Plays30")) return false;
			if(!MySQLManager.exists("Kit5Plays")) return false;
			if(!MySQLManager.exists("Kit5Plays30")) return false;
			
			if(!MySQLManager.exists("kit1plays24h")) return false;
			if(!MySQLManager.exists("kit2plays24h")) return false;
			if(!MySQLManager.exists("kit3plays24h")) return false;
			if(!MySQLManager.exists("kit4plays24h")) return false;
			if(!MySQLManager.exists("kit5plays24h")) return false;
		} else {
			if(!Database.exists("PlayerName")) return false;
			if(!Database.exists("UUID")) return false;
			if(!Database.exists("KitInv")) return false;
			if(!Database.exists("KitArmor")) return false;
			if(!Database.exists("Settings")) return false;
			if(!Database.exists("QuequePrefs")) return false;
			if(!Database.exists("KitInv2")) return false;
			if(!Database.exists("KitArmor2")) return false;
			if(!Database.exists("KitSettings2")) return false;
			if(!Database.exists("KitInv3")) return false;
			if(!Database.exists("KitArmor3")) return false;
			if(!Database.exists("KitSettings3")) return false;
			if(!Database.exists("KitInv4")) return false;
			if(!Database.exists("KitArmor4")) return false;
			if(!Database.exists("KitSettings4")) return false;
			if(!Database.exists("KitInv5")) return false;
			if(!Database.exists("KitArmor5")) return false;
			if(!Database.exists("KitSettings5")) return false;
			if(!Database.exists("Fights")) return false;
			if(!Database.exists("FightsWon")) return false;
			if(!Database.exists("DefaultKit")) return false;
			if(!Database.exists("DisabledMaps")) return false;
			if(!Database.exists("RankPoints")) return false;
			
			if(!Database.exists("Fights30")) return false;
			if(!Database.exists("FightsWon30")) return false;
			
			if(!Database.exists("Kit1Plays")) return false;
			if(!Database.exists("Kit1Plays30")) return false;
			if(!Database.exists("Kit2Plays")) return false;
			if(!Database.exists("Kit2Plays30")) return false;
			if(!Database.exists("Kit3Plays")) return false;
			if(!Database.exists("Kit3Plays30")) return false;
			if(!Database.exists("Kit4Plays")) return false;
			if(!Database.exists("Kit4Plays30")) return false;
			if(!Database.exists("Kit5Plays")) return false;
			if(!Database.exists("Kit5Plays30")) return false;
			
			if(!Database.exists("kit1plays24h")) return false;
			if(!Database.exists("kit2plays24h")) return false;
			if(!Database.exists("kit3plays24h")) return false;
			if(!Database.exists("kit4plays24h")) return false;
			if(!Database.exists("kit5plays24h")) return false;
			
		}
		
		
		return true;
	}
	
	public String getNotExistingRows() {
		StringBuilder missing = new StringBuilder();
		if(plugin.useMySQL) {
			if(!MySQLManager.exists("PlayerName")) missing.append(", PlayerName");
			if(!MySQLManager.exists("UUID")) missing.append(", UUID");
			if(!MySQLManager.exists("KitInv")) missing.append(", KitInv");
			if(!MySQLManager.exists("KitArmor")) missing.append(", KitArmor");
			if(!MySQLManager.exists("Settings")) missing.append(", Settings");
			if(!MySQLManager.exists("QuequePrefs")) missing.append("QuequePrefs");
			if(!MySQLManager.exists("KitInv2")) missing.append(", KitInv2");
			if(!MySQLManager.exists("KitArmor2")) missing.append(", KitArmor2");
			if(!MySQLManager.exists("KitSettings2")) missing.append(", KitSettings2");
			if(!MySQLManager.exists("KitInv3")) missing.append(", KitInv3");
			if(!MySQLManager.exists("KitArmor3")) missing.append(", KitArmor3");
			if(!MySQLManager.exists("KitSettings3")) missing.append(", KitSettings3");
			if(!MySQLManager.exists("KitInv4")) missing.append(", KitInv4");
			if(!MySQLManager.exists("KitArmor4")) missing.append(", KitArmor4");
			if(!MySQLManager.exists("KitSettings4")) missing.append(", KitSettings4");
			if(!MySQLManager.exists("KitInv5")) missing.append(", KitInv5");
			if(!MySQLManager.exists("KitArmor5")) missing.append(", KitArmor5");
			if(!MySQLManager.exists("KitSettings5")) missing.append(", KitSettings5");
			if(!MySQLManager.exists("Fights")) missing.append(", Fights");
			if(!MySQLManager.exists("FightsWon")) missing.append(", FightsWon");
			if(!MySQLManager.exists("DefaultKit")) missing.append(", DefaultKit");
			if(!MySQLManager.exists("DisabledMaps")) missing.append(", DisabledMaps");
			if(!MySQLManager.exists("RankPoints")) missing.append(", RankPoints");
			
			if(!MySQLManager.exists("Fights30")) missing.append(", Fights30");
			if(!MySQLManager.exists("FightsWon30")) missing.append(", FightsWon30");
			
			if(!MySQLManager.exists("Kit1Plays")) missing.append(", Kit1Plays");
			if(!MySQLManager.exists("Kit1Plays30")) missing.append(", Kit1Plays30");
			if(!MySQLManager.exists("Kit2Plays")) missing.append(", Kit2Plays");
			if(!MySQLManager.exists("Kit2Plays30")) missing.append(", Kit2Plays30");
			if(!MySQLManager.exists("Kit3Plays")) missing.append(", Kit3Plays");
			if(!MySQLManager.exists("Kit3Plays30")) missing.append(", Kit3Plays30");
			if(!MySQLManager.exists("Kit4Plays")) missing.append(", Kit4Plays");
			if(!MySQLManager.exists("Kit4Plays30")) missing.append(", Kit4Plays30");
			if(!MySQLManager.exists("Kit5Plays")) missing.append(", Kit5Plays");
			if(!MySQLManager.exists("Kit5Plays30")) missing.append(", Kit5Plays30");
			
			if(!MySQLManager.exists("Kit1Plays24h")) missing.append(", Kit1Plays24h");
			if(!MySQLManager.exists("Kit2Plays24h")) missing.append(", Kit2Plays24h");
			if(!MySQLManager.exists("Kit3Plays24h")) missing.append(", Kit3Plays24h");
			if(!MySQLManager.exists("Kit4Plays24h")) missing.append(", Kit4Plays24h");
			if(!MySQLManager.exists("Kit5Plays24h")) missing.append(", Kit5Plays24h");
			
		} else {
			if(!Database.exists("PlayerName")) missing.append("PlayerName, ");
			if(!Database.exists("UUID")) missing.append(", UUID");
			if(!Database.exists("KitInv")) missing.append(", KitInv");
			if(!Database.exists("KitArmor")) missing.append(", KitArmor");
			if(!Database.exists("Settings")) missing.append(", Settings");
			if(!Database.exists("QuequePrefs")) missing.append("QuequePrefs");
			if(!Database.exists("KitInv2")) missing.append(", KitInv2");
			if(!Database.exists("KitArmor2")) missing.append(", KitArmor2");
			if(!Database.exists("KitSettings2")) missing.append(", KitSettings2");
			if(!Database.exists("KitInv3")) missing.append(", KitInv3");
			if(!Database.exists("KitArmor3")) missing.append(", KitArmor3");
			if(!Database.exists("KitSettings3")) missing.append(", KitSettings3");
			if(!Database.exists("KitInv4")) missing.append(", KitInv4");
			if(!Database.exists("KitArmor4")) missing.append(", KitArmor4");
			if(!Database.exists("KitSettings4")) missing.append(", KitSettings4");
			if(!Database.exists("KitInv5")) missing.append(", KitInv5");
			if(!Database.exists("KitArmor5")) missing.append(", KitArmor5");
			if(!Database.exists("KitSettings5")) missing.append(", KitSettings5");
			if(!Database.exists("Fights")) missing.append(", Fights");
			if(!Database.exists("FightsWon")) missing.append(", FightsWon");
			if(!Database.exists("DefaultKit")) missing.append(", DefaultKit");
			if(!Database.exists("DisabledMaps")) missing.append(", DisabledMaps");
			if(!Database.exists("RankPoints")) missing.append(", RankPoints");
			
			if(!Database.exists("Fights30")) missing.append(", Fights30");
			if(!Database.exists("FightsWon30")) missing.append(", FightsWon30");
			
			if(!Database.exists("Kit1Plays")) missing.append(", Kit1Plays");
			if(!Database.exists("Kit1Plays30")) missing.append(", Kit1Plays30");
			if(!Database.exists("Kit2Plays")) missing.append(", Kit2Plays");
			if(!Database.exists("Kit2Plays30")) missing.append(", Kit2Plays30");
			if(!Database.exists("Kit3Plays")) missing.append(", Kit3Plays");
			if(!Database.exists("Kit3Plays30")) missing.append(", Kit3Plays30");
			if(!Database.exists("Kit4Plays")) missing.append(", Kit4Plays");
			if(!Database.exists("Kit4Plays30")) missing.append(", Kit4Plays30");
			if(!Database.exists("Kit5Plays")) missing.append(", Kit5Plays");
			if(!Database.exists("Kit5Plays30")) missing.append(", Kit5Plays30");
			
			if(!Database.exists("Kit1Plays24h")) missing.append(", Kit1Plays24h");
			if(!Database.exists("Kit2Plays24h")) missing.append(", Kit2Plays24h");
			if(!Database.exists("Kit3Plays24h")) missing.append(", Kit3Plays24h");
			if(!Database.exists("Kit4Plays24h")) missing.append(", Kit4Plays24h");
			if(!Database.exists("Kit5Plays24h")) missing.append(", Kit5Plays24h");
			
		}
		
		return missing.toString().replaceFirst(", ", "");
	}
}
