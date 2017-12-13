/**
 * 
 */
package de.OnevsOne.Arena.Manager.ACS;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import de.OnevsOne.main;
import de.OnevsOne.Commands.MainCommand;
import de.OnevsOne.Methods.saveErrorMethod;
import de.OnevsOne.Methods.Core.MainScheduler;
import de.OnevsOne.States.AllErrors;

/**
 * Der Code ist von JHammer
 *
 * 12.07.2017 um 18:43:53 Uhr
 * 
 */
public class Manager {

	private main plugin;

	public Manager(main plugin) {
		this.plugin = plugin;
	}
	
	public void genNextX(String Layout, String Name) {
		
		if(Name == null || Name.equalsIgnoreCase("null")) return;
		Location maxPos = getMaxPos(plugin.getPositions().getPos1(Layout), plugin.getPositions().getPos2(Layout), Name);
		Location minPos = getMinPos(plugin.getPositions().getPos1(Layout), plugin.getPositions().getPos2(Layout));
		
		if(maxPos == null || minPos == null) return;
		while(plugin.ACSX.containsKey(Name)) plugin.ACSX.remove(Name);
		plugin.ACSX.put(Name, plugin.ACSNextX);
		int x = (maxPos.getBlockX()-minPos.getBlockX())+1;
		x = x+plugin.ACSDistX+plugin.ACSNextX;
		plugin.ACSNextX = x;
		
	}
	
	public void genNextZ(String Layout, String Name) {
		if(!plugin.ACSZ.containsKey(Name)) plugin.ACSZ.put(Name, 0);
		
		
		if(Name == null || Name.equalsIgnoreCase("null")) return;
		Location maxPos = getMaxPos(plugin.getPositions().getPos1(Layout), plugin.getPositions().getPos2(Layout), Name);
		Location minPos = getMinPos(plugin.getPositions().getPos1(Layout), plugin.getPositions().getPos2(Layout));
		if(maxPos == null || minPos == null) return;
		int z = (maxPos.getBlockZ()-minPos.getBlockZ())+1;
		z = z+plugin.ACSDistZ+plugin.ACSZ.get(Name);
		while(plugin.ACSZ.containsKey(Name)) plugin.ACSZ.remove(Name);
		plugin.ACSZ.put(Name, z);
		
	}
	
	
	protected Location getMaxPos(Location pos1, Location pos2, String Name) {
		Location max = pos1;
		
		if(pos1 == null || pos2 == null) return max;
		
		int maxX,maxY,maxZ;
		maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
		maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
		maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
		
		max = new Location(pos1.getWorld(), maxX, maxY, maxZ);
		
		return max;
	}
	
	protected Location getMinPos(Location pos1, Location pos2) {
		Location min = pos1;
		if(pos1 == null || pos2 == null) return min;
		int minX,minY,minZ;
		
		minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
		minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
		minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
		
		min = new Location(pos1.getWorld(), minX, minY, minZ);
		
		return min;
	}
	
	@SuppressWarnings("static-access")
	public void generateBase(final int min, int max) {
		if(min <= 0 || max <= 0) return;
		if(min > max) return;
		
		if(Bukkit.getWorld(plugin.ACSWorld) == null) return;
		
		
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			
			@Override
			public void run() {
				World w = Bukkit.getWorld(plugin.ACSWorld);
				Location base = new Location(w, 0, 4, 0);
				
				int number = 0;
				int numberD = 0;
				
				
				if(new File("plugins/1vs1/ACS").listFiles() == null) return;
				
				for(File files : new File("plugins/1vs1/ACS").listFiles()) {
					
					while(number < min) {
						createArena(base, files.getName().replaceAll(".yml", ""), number, files.getName().replaceAll(".yml", ""), numberD);
						number++;
						genNextZ(new YamlConfiguration().loadConfiguration(files).getString("config.LayOut"), files.getName().replaceAll(".yml", ""));
						
						base = new Location(base.getWorld(), base.getBlockX(), base.getY(),  plugin.ACSZ.get(files.getName().replaceAll(".yml", "")));
						numberD++;
					}
					
					number = 0;
					
					genNextX(files.getName().replaceAll(".yml", ""), files.getName().replaceAll(".yml", ""));
					base = new Location(base.getWorld(), plugin.ACSNextX, base.getBlockY(), 0);
					
				}
				MainScheduler.ACSScheduler();
			}
		});
	}
	
	public void createArena(Location loc, String Name, int number, String preset, int numberD) {
		
		
	    String arena = plugin.msgs.getMsg("acsArenaNameDesign").replaceAll("%Name%", Name).replaceAll("%Number%", "" + number);
	   
	    YamlConfiguration presetCFG = plugin.getYaml("ACS/" + preset);

		Location to = loc;
		
		ArrayList<String> arenas = new ArrayList<>();
		
		if(plugin.ACSArenas.containsKey(Name)) arenas = plugin.ACSArenas.get(Name);
		
		arenas.add(arena);
		
		while(plugin.ACSArenas.containsKey(Name)) plugin.ACSArenas.remove(Name);
		
		plugin.ACSArenas.put(Name, arenas);
				
				
		String layout = presetCFG.getString("config.LayOut");
				
		int disSpawn1ResetX = presetCFG.getInt("Arena.Position1.X");
		int disSpawn1ResetY = presetCFG.getInt("Arena.Position1.Y");
		int disSpawn1ResetZ = presetCFG.getInt("Arena.Position1.Z");
				
		int disSpawn2ResetX = presetCFG.getInt("Arena.Position2.X");
		int disSpawn2ResetY = presetCFG.getInt("Arena.Position2.Y");
		int disSpawn2ResetZ = presetCFG.getInt("Arena.Position2.Z");
				
		int disSpawn3ResetX = presetCFG.getInt("Arena.Spectator.X");
		int disSpawn3ResetY = presetCFG.getInt("Arena.Spectator.Y");
		int disSpawn3ResetZ = presetCFG.getInt("Arena.Spectator.Z");
				
		Location to1 = to.clone().add(disSpawn1ResetX,disSpawn1ResetY,disSpawn1ResetZ);
		Location to2 = to.clone().add(disSpawn2ResetX,disSpawn2ResetY,disSpawn2ResetZ);
		Location to3 = to.clone().add(disSpawn3ResetX,disSpawn3ResetY,disSpawn3ResetZ);
				
		YamlConfiguration cfg = plugin.getYaml("Arenen/" + arena + "/config");
				
    	cfg.set("Arena.Position1.X", to1.getBlockX());
    	cfg.set("Arena.Position1.Y", to1.getBlockY());
		cfg.set("Arena.Position1.Z", to1.getBlockZ());
			  
			   cfg.set("Arena.Position1.Yaw", presetCFG.getDouble("Arena.Position1.Yaw"));
			   cfg.set("Arena.Position1.Pitch", presetCFG.getDouble("Arena.Position1.Pitch"));
			   cfg.set("Arena.Position1.World", to1.getWorld().getName());
			   
			   cfg.set("Arena.Position2.X", to2.getBlockX());
    		   cfg.set("Arena.Position2.Y", to2.getBlockY());
			   cfg.set("Arena.Position2.Z", to2.getBlockZ());
			   cfg.set("Arena.Position2.World", to2.getWorld().getName());
			   cfg.set("Arena.Position2.Yaw", presetCFG.getDouble("Arena.Position2.Yaw"));
			   cfg.set("Arena.Position2.Pitch", presetCFG.getDouble("Arena.Position2.Pitch"));
			   
			   cfg.set("Arena.Spectator.X", to3.getBlockX());
    		   cfg.set("Arena.Spectator.Y", to3.getBlockY());
			   cfg.set("Arena.Spectator.Z", to3.getBlockZ());
		cfg.set("Arena.Spectator.World", to3.getWorld().getName());
		cfg.set("Arena.Spectator.Yaw", presetCFG.getDouble("Arena.Spectator.Yaw"));
		cfg.set("Arena.Spectator.Pitch", presetCFG.getDouble("Arena.Spectator.Pitch"));
			   
		cfg.set("Arena.ResetX", to.getBlockX());
		cfg.set("Arena.ResetY", to.getBlockY());
		cfg.set("Arena.ResetZ", to.getBlockZ());
		cfg.set("Arena.ResetWorld", to.getWorld().getName());
			   
		cfg.set("config.LayOut", layout);
		
		cfg.set("ACS.Id", Name);
			   
		try {
		 cfg.save(plugin.getPluginFile("Arenen/" + arena + "/config"));
		} catch (IOException e1) {
		 e1.printStackTrace();
		 return;
		}
			   
			   cfg = plugin.getYaml("Arenen");
			   
			   cfg.set("Arenen." + arena, true);
			   
			   try {
					cfg.save(plugin.getPluginFile("Arenen"));
   			   } catch (IOException e1) {
					e1.printStackTrace();
					return;
   			   }
			   
			  // final String arenaF = arena;
			   plugin.FreeArenas.add(arena);
			   //TODO
//			   Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
//				
//				@Override
//				public void run() {
//					new ResetMethoden(plugin).resetArena(arenaF);
//					
//				}
//			  }, (20*3)*numberD);
			  
	}
	
	public void deleteACSArenas() {
		for(String arena : plugin.getRAMMgr().getAllArenas(false)) {
			if(!plugin.getRAMMgr().isACSArena(arena)) continue;
		    File folder = new File("plugins/1vs1/Arenen/" + arena);
			MainCommand.deleteFolder(folder);
			YamlConfiguration cfg2 = plugin.getYaml("Arenen");
   			
   			cfg2.set("Arenen." + arena, null);
   			
   			
   			try {
   				cfg2.save(plugin.getPluginFile("Arenen"));
   			} catch (IOException ee) {
   				saveErrorMethod.saveError(AllErrors.FileFail, getClass().getName(), plugin.msgs.getMsg("arenaYmlCouldntBeSaved"));
   				ee.printStackTrace();
   				return;
   			}   
		}

	}
	
	@SuppressWarnings("unchecked")
	public void generateNext(String Layout) {
		if(Layout == null) return;
		
		ArrayList<String> arenas = new ArrayList<>();
		if(plugin.ACSArenas.containsKey(Layout)) arenas = (ArrayList<String>) plugin.ACSArenas.get(Layout).clone();
		
		int amount = arenas.size();
		int x = 0;
		int z = 0;
		
		if(plugin.ACSX.containsKey(Layout)) x = plugin.ACSX.get(Layout);
		if(plugin.ACSZ.containsKey(Layout)) z = plugin.ACSZ.get(Layout);
		
		
		
		Location base = new Location(Bukkit.getWorld(plugin.ACSWorld), x, 4, z);
		
		createArena(base, Layout, amount, Layout, 0);
		genNextZ(Layout, Layout);
	}
	
	int asasas = 0;
	
	@SuppressWarnings("unchecked")
	public int getFreeACSArenasType(String type) {
		int arenas = 0;
		ArrayList<String> free = (ArrayList<String>) plugin.FreeArenas.clone();
		for(String arena : free) {
			if(plugin.getRAMMgr().isACSArena(arena)) {
				if(plugin.getAState().isFree(arena)) {
					arenas++;
				}
			}
		}
		  
		
		
		return arenas;
	}
	
	public ArrayList<String> getACSArenaTypes() {
		ArrayList<String> arenas = new ArrayList<>();
		
		if(new File("plugins/1vs1/ACS").listFiles() == null) return arenas;
		
		for(File files : new File("plugins/1vs1/ACS").listFiles()) {
			arenas.add(files.getName().replaceAll(".yml", ""));
			
		}
			
		
		
		return arenas;
	}
	
	public ArrayList<String> getACSArenasType(String type) {
		ArrayList<String> arenas = new ArrayList<>();
		
		if(new File("plugins/1vs1/ACS").listFiles() == null) return arenas;
		
		for(File files : new File("plugins/1vs1/ACS").listFiles()) 
		 if(files.getName().replaceAll(".yml", "").equalsIgnoreCase(type))
			arenas.add(files.getName().replaceAll(".yml", ""));
		
		
		
		
		return arenas;
	}
	
}
