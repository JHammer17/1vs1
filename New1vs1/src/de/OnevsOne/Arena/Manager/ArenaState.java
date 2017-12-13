package de.OnevsOne.Arena.Manager;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;

import de.OnevsOne.main;

/**
 * Der Code ist von JHammer
 *
 * 07.05.2016 um 13:14:40 Uhr
 */

/*
 * Diese Klasse fragt den Arenastatus ab.
 * 
 * Methoden:
 * isFree(String Arena) -> Fragt ab ob die Arena frei ist. [Returnt: boolean]
 * isReady(String Arena) -> Fragt ab ob die Arena fertig resetet ist. [Returnt: boolean];
 * checkAllArenas() -> Lässt alle Arenen checken.
 * checkState(String Arena, String State) -> Checkt einen bestimmten Status einer Arena. [Returnt: String]
 * checkArena(String Name) -> Fügt Arena zu FreeArenas (Main) hinzu oder holt sie raus. 
 * 
 */

public class ArenaState {

	private main plugin;

	public ArenaState(main plugin) {
		this.plugin = plugin;
	}
	
	/**
	Checks if a Arena is reseted
	@param Arena: A Arena
	@return If the Arena is ready
	@throws Nothing
	*/
	public boolean isReady(String Arena) {
		
		if (plugin.existFile("Arenen/" + Arena + "/RAM")) {
			if (plugin.getYaml("Arenen/" + Arena + "/RAM").getString(
					"RAM.Ready") != null
					&& plugin.getYaml("Arenen/" + Arena + "/RAM")
							.getString("RAM.Ready").equalsIgnoreCase("true")) {
				return true;
			}
		}
		return false;
	}
	//------------------------------------

	/**
	Checks if the endmatch is started
	@param Arena: A arena
	@return If the endmatch is started
	@throws Nothing
	*/
	public boolean isEndMatch(String Arena) {
		if (plugin.existFile("Arenen/" + Arena + "/RAM")) {
			if (plugin.getYaml("Arenen/" + Arena + "/RAM").getString(
					"RAM.endmatch") != null
					&& plugin.getYaml("Arenen/" + Arena + "/RAM")
							.getString("RAM.endmatch").equalsIgnoreCase("true")) {
				return true;
			}
		}
		return false;
	}
	//------------------------------------
	
	/**
	Sets the Time of the Endmatch
	@param Arena: A Arena | Timer: The Timer of the endmatch
	@return Nothing
	@throws Nothing
	*/
	public void setEndMatch(String Arena, int Timer) {
		if (plugin.existFile("Arenen/" + Arena + "/RAM")) {
		 YamlConfiguration cfg = plugin.getYaml("Arenen/" + Arena + "/RAM");
		 
		 cfg.set("RAM.endmatch", "true");
		 cfg.set("RAM.endCounter", Timer);
		 
		 try {
			cfg.save(plugin.getPluginFile("Arenen/" + Arena + "/RAM"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		}
		return;
	}
	//------------------------------------
	
	/**
	Gets the endmatch counter
	@param Arena: A arena
	@return The Timer
	@throws Nothing
	*/
	public int getEndMatch(String Arena) {
		if (plugin.existFile("Arenen/" + Arena + "/RAM")) {
		 YamlConfiguration cfg = plugin.getYaml("Arenen/" + Arena + "/RAM");
			 
		 return cfg.getInt("RAM.endCounter");
		}
		return 0;
	}
	//------------------------------------
	
	
	/**
	Checks if the Arena is disabled
	@param Arena: A arena
	@return If the Arena is enabled
	@throws Nothing
	*/
	public boolean isDisabled(String Arena) {
	 YamlConfiguration cfg = plugin.getYaml("Arenen");

	 if (cfg.getConfigurationSection("Arenen") == null) return true;
	 if (cfg.getBoolean("Arenen." + Arena)) return false;
	 
	 return true;
	}
	//------------------------------------
	
	/**
	Checks if the Arena is started
	@param Arena: A arena
	@return If the Arena is started
	@throws Nothing
	*/
	public boolean isStarted(String Arena) {
		if (plugin.existFile("Arenen/" + Arena + "/RAM")) {
			if (plugin.getYaml("Arenen/" + Arena + "/RAM").getString(
					"RAM.Started") != null
					&& plugin.getYaml("Arenen/" + Arena + "/RAM").getString("RAM.Started").equalsIgnoreCase("true")) {
				return true;
			}
		}
	 	return false;
	 }
	 //------------------------------------
	
	/**
	Gets the Destroy Delay of a Arena
	@param Arena: A arena
	@return The Destroy Delay
	@throws Nothing
	*/
	 public Integer getArenaDestroyDelay(String Arena) {
	  if(plugin.existFile("Arenen/" + Arena + "/config")) {
	   if(plugin.getYaml("Arenen/" + Arena + "/config").getString("config.Destroy") != null) {
	 	try {
	 		return Integer.parseInt(plugin.getYaml("Arenen/" + Arena + "/config").getString("config.Destroy"));
	 	} catch(NumberFormatException e) {
	 		return plugin.ArenaDestroy;
	 	}
	   }
	  }
      return plugin.ArenaDestroy;
	 }
	 //------------------------------------
	 
	 /**
	 Gets the Build Delay of a Arena
	 @param Arena: A arena
	 @return The Build Delay
	 @throws Nothing
	 */
	 public Integer getArenaBuildDelay(String Arena) {
	  if(plugin.existFile("Arenen/" + Arena + "/config")) {
	   if(plugin.getYaml("Arenen/" + Arena + "/config").getString("config.Build") != null) {
	 	try {
	 		return Integer.parseInt(plugin.getYaml("Arenen/" + Arena + "/config").getString("config.Build"));
	 	} catch(NumberFormatException e) {
	 		return plugin.ArenaBuild;
	 	}
	   }
	  }
      return plugin.ArenaBuild;
	 }
	 //------------------------------------
		
	 /**
	 Sets the Arena Destroy Delay
	 @param Arena: A arena | Delay: The new Destroy Delay in Ticks
	 @return Nothing
	 @throws Nothing
	 */
	 public void setArenaDestroyDelay(String Arena, int Delay) {
	  if(plugin.existFile("Arenen/" + Arena + "/config")) {
		YamlConfiguration cfg = plugin.getYaml("Arenen/" + Arena + "/config");
		cfg.set("config.Destroy", "" + Delay);
		try {
			cfg.save(plugin.getPluginFile("Arenen/" + Arena + "/config"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	  }
	     return;
	 }
	 //------------------------------------
		 
	 /**
	 Sets the Build Delay for an Arena
	 @param Arena: A Arenas | Delay: The new Build Delay in Ticks
	 @return Nothing
	 @throws Nothing
	 */
	 public void setArenaBuildDelay(String Arena, int Delay) {
	  if(plugin.existFile("Arenen/" + Arena + "/config")) {
	   YamlConfiguration cfg = plugin.getYaml("Arenen/" + Arena + "/config");
	   cfg.set("config.Build", "" + Delay);
	   try {
		cfg.save(plugin.getPluginFile("Arenen/" + Arena + "/config"));
	   } catch (IOException e) {
		e.printStackTrace();
       }   
	  }
	     return;
	 }
	//------------------------------------
	 
	 
	 
	 /**
	 Checks if a Arena is free
	 @param Arena: Arena
	 @return If the Arena is free
	 @throws Nothing
	 */
	public  boolean isFree(String Arena) {
		if (plugin.existFile("Arenen/" + Arena + "/RAM")) {
			if (plugin.getYaml("Arenen/" + Arena + "/RAM")
					.getString("RAM.Used") != null
					&& plugin.getYaml("Arenen/" + Arena + "/RAM")
							.getString("RAM.Used").equalsIgnoreCase("true")) {
				return false;
			}
		}
		return true;
	}
	//-----------------------------
	
	
	
	/**
	Checks if a Arena is Ended
	@param Arena: Arena
	@return If a Arena is Ended
	@throws Nothing
	*/
	public  boolean isEnded(String Arena) {
		if (plugin.existFile("Arenen/" + Arena + "/RAM")) {
			if (plugin.getYaml("Arenen/" + Arena + "/RAM")
					.getString("RAM.Ended") != null
					&& plugin.getYaml("Arenen/" + Arena + "/RAM")
							.getString("RAM.Ended").equalsIgnoreCase("true")) {
				return true;
			}
		}
		return false;
	}
	//-----------------------------
	
	
	/**
	Checks all Arenas
	@param Nothing
	@return Nothing
	@throws Nothing
	*/
	public  void checkAllArenas() {
		plugin.FreeArenas.clear();

		YamlConfiguration cfg = plugin.getYaml("Arenen");

		if (cfg.getConfigurationSection("Arenen") == null) {
			return;
		}

		for (String Arenen : cfg.getConfigurationSection("Arenen").getKeys(false)) {
			if (cfg.getBoolean("Arenen." + Arenen)) {
				checkArena(Arenen);
			} else {
				while (plugin.FreeArenas.contains(Arenen)) {
					plugin.FreeArenas.remove(Arenen);
				}
			}
		}

	}
	//------------------------------------------
	
	/**
	Checks a Arena
	@param Name: A arena
	@return Nothing
	@throws Nothing
	*/
	public  void checkArena(String Name) {

		if (isFree(Name) && isArenaOk(Name)) {
		 if(plugin.ArenaPlayersP1.containsKey(Name) && plugin.ArenaPlayersP1.get(Name).size() != 0 && plugin.ArenaPlayersP2.containsKey(Name) && plugin.ArenaPlayersP2.get(Name).size() != 0) {
			 return;
		 }
			while (plugin.FreeArenas.contains(Name)) {
				plugin.FreeArenas.remove(Name);
			}

			plugin.FreeArenas.add(Name);

		} else {
			while (plugin.FreeArenas.contains(Name)) {
				plugin.FreeArenas.remove(Name);
			}
		}
	}
	//---------------------------
	
	/**
	Lets Check the RAM.yml of an Arena
	@param Arena: A arena | State: What should be checked
	@return The state of the checked thing
	@throws Nothing
	*/
	public  String checkState(String Arena, String State) {
		if(plugin.existFile("Arenen/" + Arena + "/RAM")) {
		 if(plugin.getYaml("Arenen/" + Arena + "/RAM").getString("RAM." + State) != null) {
		  return plugin.getYaml("Arenen/" + Arena + "/RAM").getString("RAM." + State);
		 }
		}
		return null;
	}
	//---------------------------------------
	
	
	public boolean isOut(String Arena, UUID uuid) {
		if(plugin.existFile("Arenen/" + Arena + "/RAM")) {
		 if(plugin.getYaml("Arenen/" + Arena + "/RAM").get("RAM.Out." + uuid) != null) {
			 
		  return plugin.getYaml("Arenen/" + Arena + "/RAM").getBoolean("RAM.Out." + uuid);
		 }
		}
		
		return false;
	}
	
	
	/**
	Checks if Arena exists
	@param Arena: A Arena
	@return If the Arena exists
	@throws Nothing
	*/
	public  boolean exists(String Arena) {
		if (plugin.existFile("Arenen/" + Arena + "/config")) {
			return true;
		}
		return false;
	}
	//---------------------------------------
	
	
		
	// isArenaOk(String Arena): Fragt ob eine Arena alle Punkte hat.
	public  boolean isArenaOk(String Arena) {
		if(plugin.getPositions().getLayout(Arena) == null ||
		 plugin.getPositions().getPos3(Arena) == null ||
		 plugin.getPositions().getArenaPos1(Arena) == null ||
		 plugin.getPositions().getArenaPos2(Arena) == null ||
		 plugin.getPositions().getArenaPos3(Arena) == null) {
			
		 return false;
		}
		return true;
	}
   // --------------------------------------
		
}
