package de.OnevsOne.Arena.Manager;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.OnevsOne.main;

/**
 * Der Code ist von JHammer
 *
 * 07.05.2016 um 22:15:44 Uhr
 */

/*
 * Diese Klasse lässt alle Entitys aus einer Arena entfernen.
 * 
 * Methoden:
 * removeArenaEntitys(String Arena) -> Löscht alle Entitys aus einer Arena.
 * [PRIVAT] checkRegion(Location Check, Location Min, Location Max) -> Checkt ob eine Location zwischen zwei anderen ist. [Returnt: boolean]
 * 
 */
public class RemoveEntitys {

	private static main plugin;

	@SuppressWarnings("static-access")
	public RemoveEntitys(main plugin) {
		this.plugin = plugin;
	}

	//Werte
	static int minX, minY, minZ = 0;
	static int maxX, maxY, maxZ = 0;
	//-----
	
	
	static int oldX = 0;
	
	static int oldZ = 0;
	static int durchlauf = -1;
	
	public static void removeArenaEntitysLoad(String Arena, Location Min, Location Max, World world, Location Paste) {
		int minX,minY,minZ = 0;
		int maxX,maxY,maxZ = 0;
		
		minX = Math.min(Min.getBlockX(), Max.getBlockX());
		minY = Math.min(Min.getBlockY(), Max.getBlockY());
		minZ = Math.min(Min.getBlockZ(), Max.getBlockZ());
		
		maxX = Math.max(Min.getBlockX(), Max.getBlockX());
		maxY = Math.max(Min.getBlockY(), Max.getBlockY());
		maxZ = Math.max(Min.getBlockZ(), Max.getBlockZ());
		
		Min = new Location(Min.getWorld(), minX, minY, minZ);
		Max = new Location(Max.getWorld(), maxX, maxY, maxZ);
		
		ArrayList<Chunk> chunks = new ArrayList<Chunk>();
			
			
			
		for(int x = Min.getBlockX() ; x <= Max.getBlockX() ; x++) {
		 for(int z = Min.getBlockZ() ; z <= Max.getBlockZ() ; z++) {
			 
		   Location ResetPos3 = new Location(world, Paste.getBlockX(), Paste.getBlockY(), Paste.getBlockZ());
				    
		   int offsetX = oldX-x;
           int offsetZ = oldZ-z;
							
           Location newLoc = ResetPos3;
				    	    
		   if(durchlauf != -1) {
			newLoc = newLoc.add(offsetX*-1,0,offsetZ*-1);
	       } else {					 
            oldX = Min.getBlockX();
			oldZ = Min.getBlockZ();
			durchlauf = 0;
		   }
				        	
		   if(!chunks.contains(newLoc.getChunk())) {
			chunks.add(newLoc.getChunk());
		   }  
		  }
		 }	
		 
		 removeEntity(Arena,chunks);	 
		 
		 oldX = 0;
		 oldZ = 0;
		 durchlauf = -1;	
	}
	
	
	
	public static void removeArenaEntitys(String Arena,World worlds) {
		   for(Entity en : worlds.getEntities()) {
			if(plugin.ArenaCorner1.containsKey(Arena) && plugin.ArenaCorner2.containsKey(Arena)) {
					
			  minX = Math.min(plugin.ArenaCorner1.get(Arena).getBlockX(),plugin.ArenaCorner2.get(Arena).getBlockX());
			  minY = Math.min(plugin.ArenaCorner1.get(Arena).getBlockY(),plugin.ArenaCorner2.get(Arena).getBlockY());
			  minZ = Math.min(plugin.ArenaCorner1.get(Arena).getBlockZ(),plugin.ArenaCorner2.get(Arena).getBlockZ());

			  maxX = Math.max(plugin.ArenaCorner1.get(Arena).getBlockX(),plugin.ArenaCorner2.get(Arena).getBlockX());
			  maxY = Math.max(plugin.ArenaCorner1.get(Arena).getBlockY(),plugin.ArenaCorner2.get(Arena).getBlockY());
			  maxZ = Math.max(plugin.ArenaCorner1.get(Arena).getBlockZ(),plugin.ArenaCorner2.get(Arena).getBlockZ());

			  Location Min = new Location(Bukkit.getWorld(plugin.ArenaCorner1.get(Arena).getWorld().getName()), minX, minY, minZ);
			  Location Max = new Location(Bukkit.getWorld(plugin.ArenaCorner1.get(Arena).getWorld().getName()), maxX, maxY, maxZ);
			 
			  if(checkRegion(en.getLocation(), Min, Max)) {
			   if(!(en instanceof Player)) {
			    en.remove();
			   }
			  }
			 }
			}
	}

	private static boolean checkRegion(Location Check, Location Min, Location Max) {

		if (Min == null || Max == null) {
			return false;
		}
		if (Min.getBlockY() > Check.getBlockY() || Max.getBlockY() < Check.getBlockY()) {
			return false;
		}
		if (!Check.getWorld().getName().equalsIgnoreCase(Min.getWorld().getName())) {
			return false;
		}

		int hx = Check.getBlockX();
		int hz = Check.getBlockZ();
		
		if (hx < Min.getBlockX()) return false;
		if (hx > Max.getBlockX()) return false;
		
		if (hz < Min.getBlockZ()) return false;
		if (hz > Max.getBlockZ()) return false;

		return true;

	}
	
	private static void removeEntity(String Arena, ArrayList<Chunk> chunk) {
		 for(Chunk c : chunk) {
		 
		  c.unload();
		  
		  if(plugin.loadChunks) {
			  c.load(true);
		  }
		  
		  for(Entity ent : c.getEntities()) {
			ent.getLocation();
		    
			if(plugin.ArenaCorner1.containsKey(Arena) && plugin.ArenaCorner2.containsKey(Arena)) {
				
				minX = Math.min(plugin.ArenaCorner1.get(Arena).getBlockX(),plugin.ArenaCorner2.get(Arena).getBlockX());
				minY = Math.min(plugin.ArenaCorner1.get(Arena).getBlockY(),plugin.ArenaCorner2.get(Arena).getBlockY());
				minZ = Math.min(plugin.ArenaCorner1.get(Arena).getBlockZ(),plugin.ArenaCorner2.get(Arena).getBlockZ());

				maxX = Math.max(plugin.ArenaCorner1.get(Arena).getBlockX(),plugin.ArenaCorner2.get(Arena).getBlockX());
				maxY = Math.max(plugin.ArenaCorner1.get(Arena).getBlockY(),plugin.ArenaCorner2.get(Arena).getBlockY());
				maxZ = Math.max(plugin.ArenaCorner1.get(Arena).getBlockZ(),plugin.ArenaCorner2.get(Arena).getBlockZ());

				Location Min = new Location(Bukkit.getWorld(plugin.ArenaCorner1.get(Arena).getWorld().getName()), minX, minY, minZ);
				Location Max = new Location(Bukkit.getWorld(plugin.ArenaCorner1.get(Arena).getWorld().getName()), maxX, maxY, maxZ);
				if(checkRegion(ent.getLocation(), Min, Max)) {
				 if(!(ent instanceof Player)) {
				  ent.remove();
				 }
				}
			}
		  } 
		 }	 
		}
}
