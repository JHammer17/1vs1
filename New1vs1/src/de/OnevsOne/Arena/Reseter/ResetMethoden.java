package de.OnevsOne.Arena.Reseter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import de.OnevsOne.main;
import de.OnevsOne.Arena.Reseter.Builder.DeleteArena;

/**
 * Der Code ist von JHammer
 *
 * 07.05.2016 um 12:44:38 Uhr
 */

/*
 * Diese Klasse enthält Reset Methoden für Arenen
 * 
 * Methoden:
 * 
 * resetAllArenas() -> Resetet alle Arenen
 * resetArena(String ArenaName) -> Resetet eine bestimmt Arena
 * resetArenaData(String ArenaName) -> Resetet Daten der Hashmaps und ArrayLists
 * 
 * 
 */
public class ResetMethoden {

	private static main plugin;
	
	@SuppressWarnings("static-access")
	public ResetMethoden(main plugin) {
		this.plugin = plugin;
	}

	public int resetAllArenas() {
		YamlConfiguration cfg = plugin.getYaml("Arenen");
		
		if(cfg.getConfigurationSection("Arenen") == null) return 0;
		
		
		
		for(String Arenen : cfg.getConfigurationSection("Arenen").getKeys(false))
		 if(cfg.getBoolean("Arenen." + Arenen)) resetArena(Arenen);
		return cfg.getConfigurationSection("Arenen").getKeys(false).size();
	}
	
	
	
	public int resetAllArenasUsed() {
		YamlConfiguration cfg = plugin.getYaml("Arenen");
		
		if(cfg.getConfigurationSection("Arenen") == null) return 0;
		
		
	
		int number = 0;
		for(String Arenen : cfg.getConfigurationSection("Arenen").getKeys(false)) {
		 if(cfg.getBoolean("Arenen." + Arenen) && plugin.getAState().exists(Arenen)) {
			 
		  if(plugin.getRAMMgr().existsRAM(Arenen)) {
			  
		   if(!plugin.getAState().isReady(Arenen) || !plugin.getAState().isFree(Arenen)) {
			   plugin.getRAMMgr().saveRAM(Arenen, "Used", "false");
			   plugin.getRAMMgr().deleteRAM(Arenen);
			   resetArena(Arenen);  
			   number++;
		   } else {
				if(plugin.getAState().checkState(Arenen, "Arena.World") == null) {
					plugin.getRAMMgr().deleteRAM(Arenen);
					plugin.getRAMMgr().saveRAM(Arenen, "Ready", "false");
					plugin.getRAMMgr().saveRAM(Arenen, "Used", "false");
					resetArena(Arenen);  
					 number++;
					
				} else if(plugin.getAState().checkState(Arenen, "Arena.World").equalsIgnoreCase("null")) {
					plugin.getRAMMgr().deleteRAM(Arenen);
					plugin.getRAMMgr().saveRAM(Arenen, "Ready", "false");
					plugin.getRAMMgr().saveRAM(Arenen, "Used", "false");
					resetArena(Arenen);  
					 number++;
					
				} else {
					

					try {
						int Corner1X = Integer.parseInt(plugin.getAState().checkState(Arenen, "Corner1.X"));
						int Corner1Y = Integer.parseInt(plugin.getAState().checkState(Arenen, "Corner1.Y"));
						int Corner1Z = Integer.parseInt(plugin.getAState().checkState(Arenen, "Corner1.Z"));
						
						int Corner2X = Integer.parseInt(plugin.getAState().checkState(Arenen, "Corner2.X"));
						int Corner2Y = Integer.parseInt(plugin.getAState().checkState(Arenen, "Corner2.Y"));
						int Corner2Z = Integer.parseInt(plugin.getAState().checkState(Arenen, "Corner2.Z"));
						
						int Pos1X = Integer.parseInt(plugin.getAState().checkState(Arenen, "Pos1.X"));
						int Pos1Y = Integer.parseInt(plugin.getAState().checkState(Arenen, "Pos1.Y"));
						int Pos1Z = Integer.parseInt(plugin.getAState().checkState(Arenen, "Pos1.Z"));
						
						int Pos2X = Integer.parseInt(plugin.getAState().checkState(Arenen, "Pos2.X"));
						int Pos2Y = Integer.parseInt(plugin.getAState().checkState(Arenen, "Pos2.Y"));
						int Pos2Z = Integer.parseInt(plugin.getAState().checkState(Arenen, "Pos2.Z"));
						
						String World = plugin.getAState().checkState(Arenen, "Arena.World");
						
						

						plugin.getRAMMgr().deleteRAM(Arenen);
						plugin.getRAMMgr().saveRAM(Arenen, "Ready", "true");
						
						plugin.getRAMMgr().saveRAM(Arenen, "Corner1.X", "" + Corner1X);
						plugin.getRAMMgr().saveRAM(Arenen, "Corner1.Y", "" + Corner1Y);
						plugin.getRAMMgr().saveRAM(Arenen, "Corner1.Z", "" + Corner1Z);
						
						plugin.getRAMMgr().saveRAM(Arenen, "Corner2.X", "" + Corner2X);
						plugin.getRAMMgr().saveRAM(Arenen, "Corner2.Y", "" + Corner2Y);
						plugin.getRAMMgr().saveRAM(Arenen, "Corner2.Z", "" + Corner2Z);

						plugin.getRAMMgr().saveRAM(Arenen, "Pos1.X", "" + Pos1X);
						plugin.getRAMMgr().saveRAM(Arenen, "Pos1.Y", "" + Pos1Y);
						plugin.getRAMMgr().saveRAM(Arenen, "Pos1.Z", ""+ Pos1Z);
						
						plugin.getRAMMgr().saveRAM(Arenen, "Pos2.X", "" + Pos2X);
						plugin.getRAMMgr().saveRAM(Arenen, "Pos2.Y", "" + Pos2Y);
						plugin.getRAMMgr().saveRAM(Arenen, "Pos2.Z", ""+ Pos2Z);
						
						plugin.getRAMMgr().saveRAM(Arenen, "Arena.World", World);
						
						Location Corner1 = new Location(Bukkit.getWorld(World), Corner1X, Corner1Y, Corner1Z);
						Location Corner2 = new Location(Bukkit.getWorld(World), Corner2X, Corner2Y, Corner2Z);
						Location Pos1 = new Location(Bukkit.getWorld(World), Pos1X, Pos1Y, Pos1Z);
						Location Pos2 = new Location(Bukkit.getWorld(World), Pos2X, Pos2Y, Pos2Z);
						
						plugin.ArenaCorner1.put(Arenen, Corner1);
						plugin.ArenaCorner2.put(Arenen, Corner2);
						plugin.ArenaPos1.put(Arenen, Pos1);
						plugin.ArenaPos2.put(Arenen, Pos2);
					} catch (NumberFormatException e) {
						if(Arenen != null)
						resetArena(Arenen);
						number++;
					}

					
				}
		   }
					
		   } else {
			   plugin.getRAMMgr().saveRAM(Arenen, "Used", "false");
			   plugin.getRAMMgr().deleteRAM(Arenen);
			   resetArena(Arenen);  
			   number++;
			   
		   }
		  } 
		  
		 }
		return number;
	}
	
	public void resetArena(String ArenaName) {
		
		String Layout = plugin.getPositions().getLayout(ArenaName);
		if(Layout != null) {
   		 Location pos1 = plugin.getPositions().getPos1(Layout);
   		 Location pos2 = plugin.getPositions().getPos2(Layout);
   		 Location pos3 = plugin.getPositions().getPos3(ArenaName);
   		 
   		 if(pos1 == null || pos2 == null || pos3 == null) return;
   		 
   		 DeleteArena.startReset(pos1, pos2, pos3, ArenaName);
	 }
	}
	
	public void resetArenaData(String ArenaName) {
		plugin.ArenaPos1.remove(ArenaName);
		plugin.ArenaPos2.remove(ArenaName);
		plugin.ArenaCorner1.remove(ArenaName);
		plugin.ArenaCorner2.remove(ArenaName);
		
		plugin.getRAMMgr().deleteRAM(ArenaName);
		plugin.getAState().checkArena(ArenaName);
		
		plugin.EntityCount.remove(ArenaName);
		plugin.Entitys.remove(ArenaName);
		while(plugin.tntArena.containsKey(ArenaName)) plugin.tntArena.remove(ArenaName);
	}
	
}
