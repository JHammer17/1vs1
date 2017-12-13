package de.OnevsOne.Methods;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.configuration.file.YamlConfiguration;
import de.OnevsOne.main;

/**
 * Der Code ist von JHammer
 *
 * 24.05.2016 um 18:18:00 Uhr
 */
public class SignMethods {

	private static main plugin;

	
	@SuppressWarnings("static-access")
	public SignMethods(main plugin) {
		this.plugin = plugin;
	}
	
	public void reloadJoinSigns() {
		plugin.joinSigns.clear();
		YamlConfiguration cfg = plugin.getYaml("Signs");
		 if(cfg.getConfigurationSection("Signs.join") != null) {
		  for(String Schilder : cfg.getConfigurationSection("Signs.join").getKeys(false)) {
		   
			Location signLoc = null;
				  
			int X = cfg.getInt("Signs.join." + Schilder + ".X");
			int Y = cfg.getInt("Signs.join." + Schilder + ".Y");
			int Z = cfg.getInt("Signs.join." + Schilder + ".Z");
				  
			String worldName = cfg.getString("Signs.join." + Schilder + ".World");
				  
			if(worldName != null && Bukkit.getWorld(worldName) != null) {
			 signLoc = new Location(Bukkit.getWorld(worldName), X, Y, Z);
			 plugin.joinSigns.add(signLoc);
			}
		   }
		 }
	}
	
	public void refreshJoinSigns() {
	 for(Location loc : plugin.joinSigns) {
	  if(loc.getBlock().getType() == Material.SIGN_POST || loc.getBlock().getType() == Material.WALL_SIGN) {
		  
		 Sign s = (Sign) loc.getBlock().getState();
		 
		 s.setLine(0, plugin.msgs.getMsg("joinSignLine1").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
		 s.setLine(1, plugin.msgs.getMsg("joinSignLine2").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
		 s.setLine(2, plugin.msgs.getMsg("joinSignLine3").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
		 s.setLine(3, plugin.msgs.getMsg("joinSignLine4").replaceAll("%Players%", "" + plugin.getOneVsOnePlayerSize()));
		 s.update();
	  }
	 }
	}

	public void refreshTop5() {
		
		YamlConfiguration cfg = plugin.getYaml("Signs");
		
		if(cfg.getConfigurationSection("Top5.Signs") != null) {
		 for(String str : cfg.getConfigurationSection("Top5.Signs").getKeys(false)) {
		  
		  int Place = 0;
		  try {
			Place = Integer.parseInt(str);
		  } catch (NumberFormatException e) {
			continue;
		  }
			 
		  int X = cfg.getInt("Top5.Signs." + Place + ".X");
		  int Y = cfg.getInt("Top5.Signs." + Place + ".Y");
		  int Z = cfg.getInt("Top5.Signs." + Place + ".Z");
		  
		  String world = cfg.getString("Top5.Signs." + Place + ".world");
			 
		  if(world == null) {
			  continue;
		  }
		  
		  if(Bukkit.getWorld(world) == null) {
			  continue;
		  }
		  
		  Location loc = new Location(Bukkit.getWorld(world), X, Y, Z);
		  
		  if(loc.getBlock().getType() == Material.WALL_SIGN || loc.getBlock().getType() == Material.SIGN_POST) {
			  
			  Sign s = (Sign) loc.getBlock().getState();
					setSign(Place, s, false);
		  }
		  
		 
		 }
		}
		
		if(cfg.getConfigurationSection("Top5.Signs30") != null) {
			 for(String str : cfg.getConfigurationSection("Top5.Signs30").getKeys(false)) {
			  
			  int Place = 0;
			  try {
				Place = Integer.parseInt(str);
			  } catch (NumberFormatException e) {
				continue;}
				 
			  int X = cfg.getInt("Top5.Signs30." + Place + ".X");
			  int Y = cfg.getInt("Top5.Signs30." + Place + ".Y");
			  int Z = cfg.getInt("Top5.Signs30." + Place + ".Z");
			  
			  String world = cfg.getString("Top5.Signs30." + Place + ".world");
				 
			  if(world == null) continue;
			  
			  if(Bukkit.getWorld(world) == null) continue;
			  
			  Location loc = new Location(Bukkit.getWorld(world), X, Y, Z);
			  
			  if(loc.getBlock().getType() == Material.WALL_SIGN || loc.getBlock().getType() == Material.SIGN_POST) {
				  
				  Sign s = (Sign) loc.getBlock().getState();
						setSign(Place, s, true);//TODO
						
			  }
			  
			 
			 }
			}
		
		if(cfg.getConfigurationSection("Top5.Skulls") != null) {
			 for(String str : cfg.getConfigurationSection("Top5.Skulls").getKeys(false)) {
			  
			  int Place = 0;
			  try {
				Place = Integer.parseInt(str);
			  } catch (NumberFormatException e) {
				continue;
			  }
				 
			  int X = cfg.getInt("Top5.Skulls." + Place + ".X");
			  int Y = cfg.getInt("Top5.Skulls." + Place + ".Y");
			  int Z = cfg.getInt("Top5.Skulls." + Place + ".Z");
			  
			  String world = cfg.getString("Top5.Skulls." + Place + ".world");
				 
			  if(world == null) {
				  continue;
			  }
			  
			  if(Bukkit.getWorld(world) == null) {
				  continue;
			  }
			  
			  Location loc = new Location(Bukkit.getWorld(world), X, Y, Z);
			  
			  if(loc.getBlock().getType() == Material.SKULL) {
				  
				  Skull skull = (Skull) loc.getBlock().getState();
				  setSkull(Place, skull, getTop5Place(Place, false));
			  }
			  
			 
			 }
			} 
		    if(cfg.getConfigurationSection("Top5.Skulls30") != null) {
				 for(String str : cfg.getConfigurationSection("Top5.Skulls30").getKeys(false)) {
					  
					  int Place = 0;
					  try {
						Place = Integer.parseInt(str);
					  } catch (NumberFormatException e) {
						continue;
					  }
						 
					  int X = cfg.getInt("Top5.Skulls30." + Place + ".X");
					  int Y = cfg.getInt("Top5.Skulls30." + Place + ".Y");
					  int Z = cfg.getInt("Top5.Skulls30." + Place + ".Z");
					  
					  String world = cfg.getString("Top5.Skulls30." + Place + ".world");
						 
					  if(world == null) continue;
					  
					  if(Bukkit.getWorld(world) == null) continue;
					  
					  Location loc = new Location(Bukkit.getWorld(world), X, Y, Z);
					  
					  if(loc.getBlock().getType() == Material.SKULL) {
						  
						  Skull skull = (Skull) loc.getBlock().getState();
						  setSkull(Place, skull, getTop5Place(Place, true));
					  }
					  
					 
					 }
					}
			
		
	}

	private String getTop5Place(int Place, boolean timed) {

			HashMap<Integer, UUID> top5 = plugin.getDBMgr().Top5Players(timed);
			
			if(top5 == null) return "-";
			
			
			 if(top5.containsKey(Place)) return plugin.getDBMgr().getUserName(top5.get(Place));
			 else return "-";
	}
	
	private UUID getTop5PlaceUUID(int Place, boolean timed) {
		
			HashMap<Integer, UUID> top5 = plugin.getDBMgr().Top5Players(timed);
			
			if(top5 == null) return null;
			
			
			 if(top5.containsKey(Place)) return top5.get(Place);
			  else return null;
	}
	
	
	private void setSkull(int Place, Skull skull, String name) {
		if(name.equalsIgnoreCase("-")) {
			skull.setSkullType(SkullType.PLAYER);
			skull.setOwner("MHF_QUESTION");
			skull.update();
		} else {
			skull.setSkullType(SkullType.PLAYER);
			skull.setOwner(name);
			skull.update();
		}
	}
	
	private void setSign(int Place, Sign s, boolean timed) {
		  s.setLine(0, "#" + Place);
		  
		  if(getTop5Place(Place, timed) == null) {
			  s.setLine(1, "-");
			  s.setLine(2, "");
			  s.setLine(3, "");
			  s.update();
			  return;
		  } else if(getTop5Place(Place, timed).equalsIgnoreCase("-")){
			  s.setLine(1, "-");
			  s.setLine(2, "");
			  s.setLine(3, "");
			  s.update();
			  return;
		  } else {	  
			  s.setLine(1, getTop5Place(Place, timed));
			  s.setLine(2, "");
			  s.setLine(3, "");
			  s.update();
		  }
		  
		  
		  
			  if(getTop5PlaceUUID(Place, timed) == null) {
				  s.setLine(2, "Gewonnen: ?");
			  } else {
				  s.setLine(2, "Gewonnen: " + plugin.getDBMgr().getStats(getTop5PlaceUUID(Place, timed), "FightsWon", timed));
			  }
		  
		  
		  
			  if(getTop5PlaceUUID(Place, timed) == null) {
				  s.setLine(3, "Gespielt: ?");
			  } else {
				  s.setLine(3, "Gespielt: " + plugin.getDBMgr().getStats(getTop5PlaceUUID(Place, timed), "Fights", timed));
			  }
		 
		  s.update();
	}
}
