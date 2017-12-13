package de.OnevsOne.Methods;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import de.OnevsOne.main;
import de.OnevsOne.States.AllErrors;

/**
 * Der Code ist von JHammer
 *
 * 07.05.2016 um 09:52:12 Uhr
 */
public class PositionManager {

	
	private static main plugin;
	
	@SuppressWarnings("static-access")
	public PositionManager(main plugin) {
		this.plugin = plugin;
	}

	/*Position 1 des Layouts*/
	public Location getPos1(String Layout) {
		if(plugin.existFile("ArenaLayouts/" + Layout)) {
		     YamlConfiguration cfg = plugin.getYaml("ArenaLayouts/" + Layout);
	         Location loc = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
	 		 double x = cfg.getDouble("Arena.HX");
	 		 double y = cfg.getDouble("Arena.HY");
	 		 double z = cfg.getDouble("Arena.HZ");
	 		 String worldname = cfg.getString("Arena.world");
	 				
	 		 if(worldname == null || Bukkit.getWorld(worldname) == null) {
	 			saveErrorMethod.saveError(AllErrors.World, this.getClass().getName(), "Position 1 für Layout " + Layout + " nicht gefunden");
	 			return null;
	 		 }
	 		
	 		 World welt = Bukkit.getWorld(worldname);
	 				
	 				
	 		 loc.setX(x);
	 		 loc.setY(y);
	 		 loc.setZ(z);
	 		 loc.setWorld(welt);
	 		 
	 		 return loc;
	  }
		
		
		
	  return null; 
	}
	/*-----*/
	
	/*Position 2 des Layouts*/
	public Location getPos2(String Layout) {
		if(plugin.existFile("ArenaLayouts/" + Layout)) {
		     YamlConfiguration cfg = plugin.getYaml("ArenaLayouts/" + Layout);
	         Location loc = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
	 		 double x = cfg.getDouble("Arena.LX");
	 		 double y = cfg.getDouble("Arena.LY");
	 		 double z = cfg.getDouble("Arena.LZ");
	 		 String worldname = cfg.getString("Arena.world");
	 				
	 		 if(worldname == null) {
	 			saveErrorMethod.saveError(AllErrors.World, this.getClass().getName(), "Position 2 für Layout " + Layout + " nicht gefunden");
	 			return null;
	 		 }
	 		 World welt = Bukkit.getWorld(worldname);
	 				
	 				
	 		 loc.setX(x);
	 		 loc.setY(y);
	 		 loc.setZ(z);
	 		 loc.setWorld(welt);
	 		 
	 		 return loc;
	  }
		
		
		
	  return null; 
	}
	/*-----*/
	
	/*Die Reset-Position einer Arena*/
	public Location getPos3(String ArenaName) {
		
	  if(plugin.existFile("Arenen/" + ArenaName + "/config")) {
		     YamlConfiguration cfg = plugin.getYaml("Arenen/" + ArenaName + "/config");
	         Location loc = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
	 		 double x = cfg.getDouble("Arena.ResetX");
	 		 double y = cfg.getDouble("Arena.ResetY");
	 		 double z = cfg.getDouble("Arena.ResetZ");
	 		 String worldname = cfg.getString("Arena.ResetWorld");
	 				
	 		 if(worldname == null) {
	 			saveErrorMethod.saveError(AllErrors.World, this.getClass().getName(), "Position 3 (Resetposition) für Arena " + ArenaName + " nicht gefunden");
	 			return null;
	 		 }
	 		 World welt = Bukkit.getWorld(worldname);
	 				
	 				
	 		 loc.setX(x);
	 		 loc.setY(y);
	 		 loc.setZ(z);
	 		 loc.setWorld(welt);
	 		 
	 		 return loc;
	  }
		
	  return null;
	}
	/*-----*/
	
	/*Position 1 einer Arena*/
	public Location getArenaPos1(String ArenaName) {
		
		  if(plugin.existFile("Arenen/" + ArenaName + "/config")) {
			     YamlConfiguration cfg = plugin.getYaml("Arenen/" + ArenaName + "/config");
		         Location loc = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		 		 double x = cfg.getDouble("Arena.Position1.X");
		 		 double y = cfg.getDouble("Arena.Position1.Y");
		 		 double z = cfg.getDouble("Arena.Position1.Z");
		 		 double yaw = cfg.getDouble("Arena.Position1.Yaw");
		 		 double pitch = cfg.getDouble("Arena.Position1.Pitch");
		 		 String worldname = cfg.getString("Arena.Position1.World");
		 				
		 		 if(worldname == null) {
		 			saveErrorMethod.saveError(AllErrors.World, this.getClass().getName(), "Position 1 für Arena " + ArenaName + " nicht gefunden");
		 			return null;
		 		 }
		 		 World welt = Bukkit.getWorld(worldname);
		 		 
		 				
		 		 loc.setX(x);
		 		 loc.setY(y);
		 		 loc.setZ(z);
		 		 loc.setYaw((float) yaw);
		 		 loc.setPitch((float) pitch);
		 		 loc.setWorld(welt);
		 		 
		 		 return loc;
		  }
			
		  return null;
	}
	/*----*/
	
	/*Position 2 einer Arena*/
	public Location getArenaPos2(String ArenaName) {
		
		  if(plugin.existFile("Arenen/" + ArenaName + "/config")) {
			     YamlConfiguration cfg = plugin.getYaml("Arenen/" + ArenaName + "/config");
		         Location loc = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		 		 double x = cfg.getDouble("Arena.Position2.X");
		 		 double y = cfg.getDouble("Arena.Position2.Y");
		 		 double z = cfg.getDouble("Arena.Position2.Z");
		 		 double yaw = cfg.getDouble("Arena.Position2.Yaw");
		 		 double pitch = cfg.getDouble("Arena.Position2.Pitch");
		 		 String worldname = cfg.getString("Arena.Position2.World");
		 				
		 		 if(worldname == null) {
		 			saveErrorMethod.saveError(AllErrors.World, this.getClass().getName(), "Position 2 für Arena " + ArenaName + " nicht gefunden");
		 			return null;
		 		 }
		 		 World welt = Bukkit.getWorld(worldname);
		 		 
		 				
		 		 loc.setX(x);
		 		 loc.setY(y);
		 		 loc.setZ(z);
		 		 loc.setYaw((float) yaw);
		 		 loc.setPitch((float) pitch);
		 		 loc.setWorld(welt);
		 		 
		 		 return loc;
		  }
			
		  return null;
	}
	/*-----*/
	
	/*Die SpectatorPosition einer Arena*/
	public Location getArenaPos3(String ArenaName) {
		
		  if(plugin.existFile("Arenen/" + ArenaName + "/config")) {
			     YamlConfiguration cfg = plugin.getYaml("Arenen/" + ArenaName + "/config");
		         Location loc = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		 		 double x = cfg.getDouble("Arena.Spectator.X");
		 		 double y = cfg.getDouble("Arena.Spectator.Y");
		 		 double z = cfg.getDouble("Arena.Spectator.Z");
		 		 double yaw = cfg.getDouble("Arena.Spectator.Yaw");
		 		 double pitch = cfg.getDouble("Arena.Spectator.Pitch");
		 		 String worldname = cfg.getString("Arena.Spectator.World");
		 				
		 		 if(worldname == null) {
		 			saveErrorMethod.saveError(AllErrors.World, this.getClass().getName(), "Spectator-Position (Mitte) für Arena " + ArenaName + " nicht gefunden");
		 			return null;
		 		 }
		 		 World welt = Bukkit.getWorld(worldname);
		 		 
		 				
		 		 loc.setX(x);
		 		 loc.setY(y);
		 		 loc.setZ(z);
		 		 loc.setYaw((float) yaw);
		 		 loc.setPitch((float) pitch);
		 		 loc.setWorld(welt);
		 		 
		 		 return loc;
		  }
			
		  return null;
	}
	/*-----*/
	
	/*Das Layout einer Arena*/
	public String getLayout(String ArenaName) {
		if(plugin.existFile("Arenen/" + ArenaName + "/config")) {
		     YamlConfiguration cfg = plugin.getYaml("Arenen/" + ArenaName + "/config");
		     if(cfg.getString("config.LayOut") == null) {
		    	 return null;
		     }
		     return cfg.getString("config.LayOut");
		}
		return null;
	}
	/*-----*/
	
	public Location getMainSpawn() {
		YamlConfiguration cfg = plugin.getYaml("spawns");
		
		Location loc = new Location(Bukkit.getWorld("world"), 0, 0, 0);
		double x = cfg.getDouble("MainSpawn.X");
		double y = cfg.getDouble("MainSpawn.Y");
		double z = cfg.getDouble("MainSpawn.Z");
		
		double yaw = cfg.getDouble("MainSpawn.Yaw");
		double pitch = cfg.getDouble("MainSpawn.Pitch");
		
		String worldname = cfg.getString("MainSpawn.world");
				
		if(worldname == null) {
			saveErrorMethod.saveError(AllErrors.World, this.getClass().getName(), "Mainspawn (Lobbyspawn) nicht gefunden");
			return null;
		}
		World welt = Bukkit.getWorld(worldname);
		
		loc.setX(x);
		loc.setY(y);
		loc.setZ(z);
		loc.setYaw((float) yaw);
		loc.setPitch((float)pitch);
		loc.setWorld(welt);
		loc.add(0,1.5,0);
		
		return loc;
	}
	
	
}
