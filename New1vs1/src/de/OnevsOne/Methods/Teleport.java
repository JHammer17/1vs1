package de.OnevsOne.Methods;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.OnevsOne.main;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.States.AllErrors;

/**
 * Der Code ist von JHammer
 *
 * 05.05.2016 um 21:43:56 Uhr
 */
public class Teleport {

	private  main plugin;
	
	public Teleport(main plugin) {
		this.plugin = plugin;
	}

	public  void teleportMainSpawn(Player p) {
		if(p == null) return;
		YamlConfiguration cfg = plugin.getYaml("spawns");
		
		Location loc = new Location(Bukkit.getWorld("world"), 0, 0, 0);
		double x = cfg.getDouble("MainSpawn.X");
		double y = cfg.getDouble("MainSpawn.Y");
		double z = cfg.getDouble("MainSpawn.Z");
		
		double yaw = cfg.getDouble("MainSpawn.Yaw");
		double pitch = cfg.getDouble("MainSpawn.Pitch");
		
		String worldname = cfg.getString("MainSpawn.world");
				
		if(worldname == null || Bukkit.getWorld(worldname) == null) {
			saveErrorMethod.saveError(AllErrors.World, this.getClass().getName(), "Mainspawn nicht gefunden");
			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName()));
			return;
		}
		World w = Bukkit.getWorld(worldname);
		
		loc.setX(x);
		loc.setY(y);
		loc.setZ(z);
		loc.setYaw((float) yaw);
		loc.setPitch((float)pitch);
		loc.setWorld(w);
		loc.add(0,1.5,0);
		
		p.teleport(loc);
		
	}
	
	public final void teleportExit(Player p) {
		if(p == null) return;
		YamlConfiguration cfg = plugin.getYaml("spawns");
		
		Location loc = new Location(Bukkit.getWorld("world"), 0, 0, 0);
		double x = cfg.getDouble("ExitSpawn.X");
		double y = cfg.getDouble("ExitSpawn.Y");
		double z = cfg.getDouble("ExitSpawn.Z");
		
		double yaw = cfg.getDouble("ExitSpawn.Yaw");
		double pitch = cfg.getDouble("ExitSpawn.Pitch");
		
		String worldname = cfg.getString("ExitSpawn.world");
				
		if(worldname == null) {
			saveErrorMethod.saveError(AllErrors.World, this.getClass().getName(), "Exitspawn nicht gefunden");
			p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("checkConsoleError"), p.getDisplayName()));
			return;
		}
		
		World welt = Bukkit.getWorld(worldname);
				
		loc.setX(x);
		loc.setY(y);
		loc.setZ(z);
		loc.setYaw((float) yaw);
		loc.setPitch((float)pitch);
		loc.setWorld(welt);
		loc.add(0,0.5,0);
		
		p.teleport(loc);
		
	}
}
