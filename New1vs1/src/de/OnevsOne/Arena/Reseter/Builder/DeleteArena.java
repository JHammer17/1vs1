package de.OnevsOne.Arena.Reseter.Builder;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import de.OnevsOne.main;
import de.OnevsOne.Arena.Manager.RemoveEntitys;
import de.OnevsOne.Arena.Reseter.ResetMethoden;

/**
 * Der Code ist von JHammer
 *
 * 06.05.2016 um 21:52:04 Uhr
 */
@SuppressWarnings("deprecation")
public class DeleteArena {

	private static main plugin;

	
	@SuppressWarnings("static-access")
	public DeleteArena(main plugin) {
	 this.plugin = plugin; 
	 ResetScheduler();
	}

	public static HashMap<Integer, String[]> Resets = new HashMap<Integer, String[]>();
	
	
	public static int minX;
	public static int minY;
	public static int minZ;
	public static int maxX,maxY,maxZ;
	
	public static String Name = "-";
	
	boolean Aktiv = false;
	boolean resetY = false;
		
	int Delayer = 0;
	int nextResetDelay = 3;
	
	public static void startReset(Location pos1, Location pos2, Location pos3, String Name) {
		
		
		
		
		if(plugin.useBlockyMapReset) {
			if(plugin.ResetingArenas.contains(Name)) return;
			
			
			boolean inUse = plugin.getAState().isFree(Name);
			
			
			
			new ResetMethoden(plugin).resetArenaData(Name);
			
			
			
			if(!inUse) plugin.getRAMMgr().saveRAM(Name, "Used", "true");
			
			plugin.getRAMMgr().saveRAM(Name, "Ready", "false");
			
			plugin.ArenaPos1.remove(Name);
			plugin.ArenaPos2.remove(Name);
			
			plugin.ResetingArenas.add(Name);
			
			
			BlockMapReset reset = new BlockMapReset(pos1, pos2, pos3, plugin, Name, plugin.maxBlocksPerTick);
			reset.copy();
			return;
		}
		
		
		
		
		
		if(!plugin.twoStepArenaReset) {
			
			
			
			boolean inUse = plugin.getAState().isFree(Name);
			
			if(!plugin.protectedWordls.contains(pos3.getWorld().getName())) {
				plugin.protectedWordls.add(pos3.getWorld().getName());
			}
			
			new ResetMethoden(plugin).resetArenaData(Name);
			
			
			if(!inUse) plugin.getRAMMgr().saveRAM(Name, "Used", "true");
			
			plugin.getRAMMgr().saveRAM(Name, "Ready", "false");
			
			plugin.ArenaPos1.remove(Name);
			plugin.ArenaPos2.remove(Name);
			
			plugin.ResetingArenas.add(Name);
			
			RemoveEntitys.removeArenaEntitysLoad(Name, pos1, pos2, pos3.getWorld(), pos3);
			CopyArena.startReset(pos1, pos2,pos3, Name);
			
			return;
		} else {
			if(plugin.ResetingArenas.contains(Name)) {
				return;
			}
			
			boolean inUse = plugin.getAState().isFree(Name);
			
			if(!plugin.protectedWordls.contains(pos3.getWorld().getName())) {
				plugin.protectedWordls.add(pos3.getWorld().getName());
			}
			
			if(!inUse) plugin.getRAMMgr().saveRAM(Name, "Used", "true");
			
			
			plugin.getRAMMgr().saveRAM(Name, "Ready", "false");
			
			plugin.ArenaPos1.remove(Name);
			plugin.ArenaPos2.remove(Name);
			
			plugin.ResetingArenas.add(Name);
			
			int Warteschlange = Resets.size();
			Warteschlange = Warteschlange+1;
			
			minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
			minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
			minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
			
			maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
			maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
			maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
			
			int akX = 0;
			int akY = maxY;
			int akZ = 0;
			
			String Welt = pos1.getWorld().getName();
			String PasteWelt = pos3.getWorld().getName();
			
			int XPaste = pos3.getBlockX();
			int YPaste = pos3.getBlockY();
			int ZPaste = pos3.getBlockZ();
			
			int useY = maxY-minY;
			YPaste = YPaste+useY;
			
			//						0 			1		  2			 3			4			5		  6 	    7         8         9          10             11         12            13	          14
			String[] Datas = {"" + minX, "" + minY, "" + minZ, "" + maxX, "" + maxY, "" + maxZ, "" + akX, "" + akY, "" + akZ, "" + Welt, "" + XPaste, "" + YPaste, "" + ZPaste, "" + PasteWelt, "" + Name};
		
			RemoveEntitys.removeArenaEntitysLoad(Datas[14], pos1, pos2, pos3.getWorld(), pos3);
			Resets.put(Resets.size(), Datas);
		}
	}
	
	
	
	
	public void ResetScheduler() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				if(!Aktiv && Resets.containsKey(0)) {
				 String[] Datas = Resets.get(0);
				 
				 boolean inUse = plugin.getAState().isFree(Datas[14]);
				 plugin.getRAMMgr().deleteRAM(Datas[14]);
				 if(!plugin.protectedWordls.contains(Datas[13])) {
					plugin.protectedWordls.add(Datas[13]);
				 }
				 if(!inUse) {
					 plugin.getRAMMgr().saveRAM(Datas[14], "Used", "true");
				 }
						
				 Name = Datas[14];
				 reset(Datas);
				 
				 Aktiv = true;	
				}
			}
		}, 0, 20);
		
	}
	
	
	int oldX = 0;
	
	int oldZ = 0;
	int durchlauf = -1;
	
	
	public void reset(String[] Datas) {
		ArrayList<Chunk> chunks = new ArrayList<Chunk>();
		boolean used = true;
		
		for(int x = Integer.parseInt(Datas[0]) ; x <= Integer.parseInt(Datas[3]) ; x++) {
		 for(int z = Integer.parseInt(Datas[2]) ; z <= Integer.parseInt(Datas[5]) ; z++) {
			 
			    Location ResetPos3 = new Location(Bukkit.getWorld(Datas[13]), Integer.parseInt(Datas[10]), Integer.parseInt(Datas[11]), Integer.parseInt(Datas[12]));
			    
				int offsetX = oldX-x;
				int offsetZ = oldZ-z;
				
			    Location newLoc = ResetPos3;
			    
			    if(durchlauf != -1) {
			    	  newLoc = newLoc.add(offsetX*-1,0,offsetZ*-1);
			    } else {					 
					 oldX = Integer.parseInt(Datas[0]);
					 oldZ = Integer.parseInt(Datas[2]);
					 durchlauf = 0;
			    }
			    
				
			    if(Datas[4].equalsIgnoreCase(Datas[7])) {	
				 if(!chunks.contains(newLoc.getChunk())) {
				  chunks.add(newLoc.getChunk());
				 }  
				} else {
					used = false;
				}
			    
				if(newLoc.getBlock().getType() == Material.CHEST || newLoc.getBlock().getType() == Material.TRAPPED_CHEST) {
				 Chest c = (Chest) newLoc.getBlock().getState();
				 c.getInventory().clear();
				}
				
				if(newLoc.getBlock().getType() == Material.DISPENSER) {
				 Dispenser d = (Dispenser) newLoc.getBlock().getState();
				 d.getInventory().clear();
				}
				
				if(newLoc.getBlock().getType() == Material.DROPPER) {
				 Dropper d = (Dropper) newLoc.getBlock().getState();
				 d.getInventory().clear();
				}
				
				if(newLoc.getBlock().getType() == Material.FURNACE) {
				 Furnace f = (Furnace) newLoc.getBlock().getState();
				 f.getInventory().clear();
				}
				
				if(newLoc.getBlock().getType() == Material.BREWING_STAND) {
				 BrewingStand b = (BrewingStand) newLoc.getBlock().getState();
				 b.getInventory().clear();
				}
				
				if(newLoc.getBlock().getType() == Material.HOPPER) {
				 Hopper h = (Hopper) newLoc.getBlock().getState();
				 h.getInventory().clear();
				}
				
				Location XLOC = newLoc;
				XLOC = XLOC.add(1,0,0);
				if(XLOC.getBlock().getType() == Material.TORCH || XLOC.getBlock().getType() == Material.REDSTONE_TORCH_OFF || XLOC.getBlock().getType() == Material.REDSTONE_TORCH_ON || XLOC.getBlock().getType() == Material.TRAP_DOOR || XLOC.getBlock().getType() == Material.IRON_TRAPDOOR || XLOC.getBlock().getType() == Material.BED_BLOCK || XLOC.getBlock().getType() == Material.WOOD_BUTTON || XLOC.getBlock().getType() == Material.STONE_BUTTON || XLOC.getBlock().getType() == Material.LEVER || XLOC.getBlock().getType() == Material.LADDER || XLOC.getBlock().getType() == Material.WALL_SIGN) {
					XLOC.getBlock().setType(Material.AIR);
				}
				
				XLOC = newLoc;
				XLOC = XLOC.add(-1,0,0);
				if(XLOC.getBlock().getType() == Material.TORCH || XLOC.getBlock().getType() == Material.REDSTONE_TORCH_OFF || XLOC.getBlock().getType() == Material.REDSTONE_TORCH_ON || XLOC.getBlock().getType() == Material.TRAP_DOOR || XLOC.getBlock().getType() == Material.IRON_TRAPDOOR || XLOC.getBlock().getType() == Material.BED_BLOCK || XLOC.getBlock().getType() == Material.WOOD_BUTTON || XLOC.getBlock().getType() == Material.STONE_BUTTON || XLOC.getBlock().getType() == Material.LEVER || XLOC.getBlock().getType() == Material.LADDER || XLOC.getBlock().getType() == Material.WALL_SIGN) {
					XLOC.getBlock().setType(Material.AIR);
				}
				
				XLOC = newLoc;
				XLOC = XLOC.add(0,0,-1);
				if(XLOC.getBlock().getType() == Material.TORCH || XLOC.getBlock().getType() == Material.REDSTONE_TORCH_OFF || XLOC.getBlock().getType() == Material.REDSTONE_TORCH_ON || XLOC.getBlock().getType() == Material.TRAP_DOOR || XLOC.getBlock().getType() == Material.IRON_TRAPDOOR || XLOC.getBlock().getType() == Material.BED_BLOCK || XLOC.getBlock().getType() == Material.WOOD_BUTTON || XLOC.getBlock().getType() == Material.STONE_BUTTON || XLOC.getBlock().getType() == Material.LEVER || XLOC.getBlock().getType() == Material.LADDER || XLOC.getBlock().getType() == Material.WALL_SIGN) {
					XLOC.getBlock().setType(Material.AIR);
				}
				
				XLOC = newLoc;
				XLOC = XLOC.add(0,0,1);
				if(XLOC.getBlock().getType() == Material.TORCH || XLOC.getBlock().getType() == Material.REDSTONE_TORCH_OFF || XLOC.getBlock().getType() == Material.REDSTONE_TORCH_ON || XLOC.getBlock().getType() == Material.TRAP_DOOR || XLOC.getBlock().getType() == Material.IRON_TRAPDOOR || XLOC.getBlock().getType() == Material.BED_BLOCK || XLOC.getBlock().getType() == Material.WOOD_BUTTON || XLOC.getBlock().getType() == Material.STONE_BUTTON || XLOC.getBlock().getType() == Material.LEVER || XLOC.getBlock().getType() == Material.LADDER || XLOC.getBlock().getType() == Material.WALL_SIGN) {
					XLOC.getBlock().setType(Material.AIR);
				}
				
				final Location nNewLoc = newLoc;
				nNewLoc.getBlock().setType(Material.AIR);
				nNewLoc.getBlock().setData((byte) 0);
			    
		 }
		}
		 
		 if(used) {
			 removeEntity(Datas[14],chunks);	 
		 }
		 
		 durchlauf++;
		 int y = Integer.parseInt(Datas[11]);
		 y--;
		 
		 int x = Integer.parseInt(Datas[0]);
		 int z = Integer.parseInt(Datas[2]);
		 
		 
		 oldX = x;
		 oldZ = z;
		 
		 
		 Datas[11] = "" + y;
		 
		 addY(Datas);
			
		}
	
	private void removeEntity(String Arena, ArrayList<Chunk> chunk) {
	 for(Chunk c : chunk) {
	 
	  if(plugin.loadChunks) {
		  c.load(true);
	  }
	 
	  for(Entity ent : c.getEntities()) {
		ent.getLocation();
		
		if (plugin.ArenaCorner1.containsKey(Arena)
				&& plugin.ArenaCorner2.containsKey(Arena)) {
			
			minX = Math.min(plugin.ArenaCorner1.get(Arena).getBlockX(),
					plugin.ArenaCorner2.get(Arena).getBlockX());
			minY = Math.min(plugin.ArenaCorner1.get(Arena).getBlockY(),
					plugin.ArenaCorner2.get(Arena).getBlockY());
			minZ = Math.min(plugin.ArenaCorner1.get(Arena).getBlockZ(),
					plugin.ArenaCorner2.get(Arena).getBlockZ());

			maxX = Math.max(plugin.ArenaCorner1.get(Arena).getBlockX(),
					plugin.ArenaCorner2.get(Arena).getBlockX());
			maxY = Math.max(plugin.ArenaCorner1.get(Arena).getBlockY(),
					plugin.ArenaCorner2.get(Arena).getBlockY());
			maxZ = Math.max(plugin.ArenaCorner1.get(Arena).getBlockZ(),
					plugin.ArenaCorner2.get(Arena).getBlockZ());

			Location Min = new Location(
					Bukkit.getWorld(plugin.ArenaCorner1.get(Arena)
							.getWorld().getName()), minX, minY, minZ);
			Location Max = new Location(
					Bukkit.getWorld(plugin.ArenaCorner1.get(Arena)
							.getWorld().getName()), maxX, maxY, maxZ);
			if (checkRegion(ent.getLocation(), Min, Max)) {
				if (!(ent instanceof Player)) {
					ent.remove();
				}

			}
		}
		
	  }  
	 }		 
	}
	
	private static boolean checkRegion(Location Check, Location Min, Location Max) {

		if (Min == null || Max == null) {
			return false;
		}

		if (Min.getBlockY() > Check.getBlockY()
				|| Max.getBlockY() < Check.getBlockY()) {

			return false;
		}

		if (!Check.getWorld().getName()
				.equalsIgnoreCase(Min.getWorld().getName())) {
			return false;
		}

		int hx = Check.getBlockX();
		int hz = Check.getBlockZ();
		if (hx < Min.getBlockX())
			return false;

		if (hx > Max.getBlockX())
			return false;

		if (hz < Min.getBlockZ())
			return false;

		if (hz > Max.getBlockZ())
			return false;

		return true;

	}
	
	
	public void addY(final String[] Datas) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				
				if(Integer.parseInt(Datas[1]) >= Integer.parseInt(Datas[7])) {
					
					String Layout = plugin.getPositions().getLayout(Datas[14]);
					final Location pos1 = plugin.getPositions().getPos1(Layout);
					final Location pos2 = plugin.getPositions().getPos2(Layout);
					final Location pos3 = plugin.getPositions().getPos3(Datas[14]);
					
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						
						@Override
						public void run() {
							CopyArena.startReset(pos1, pos2,pos3, Datas[14]);
						}
					},20*Delayer);
					
					int Tasks = Resets.size();
					Tasks = Tasks-1;
					
					
					nextWarteschlange();
					return;
				}
				
				int newY = Integer.parseInt(Datas[7]);
				newY--;
				
				Datas[7] = "" + newY;
				
				reset(Datas);
				
			}
		},  plugin.getAState().getArenaDestroyDelay(Datas[14]));
	}
	
	
	
	 
	
	private void nextWarteschlange() {
		
		 Name = "-";
		 Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				@Override
				public void run() {
					if(Resets.size() != 0) {
				    	 Resets.remove(0);
				    	 
				    	 if(Resets.isEmpty()) {
				    		 Aktiv = false;
				    		 oldX = 0;
				    		 oldZ = 0;
				    		  
				    		 durchlauf = -1;
				    		 
				    		 return;
				    	 }
				    	 
				    	 HashMap<Integer, String[]> newData = new HashMap<Integer, String[]>();
				    	 
				    	 HashMap<Integer, String[]> preferData = new HashMap<Integer, String[]>();
				    	 HashMap<Integer, String[]> noPreferData = new HashMap<Integer, String[]>();
				    	 
				    	 for(Integer ints : Resets.keySet()) {
				    	  if(plugin.ArenaPlayersP1.containsKey(Resets.get(ints)[14])) {
				    		  preferData.put(preferData.size()+1, Resets.get(ints));
				    	  } else {
				    		  noPreferData.put(noPreferData.size(), Resets.get(ints));
				    	  }
				    	 }
				    	 
				    	 for(Integer ints : preferData.keySet()) {
				    		 newData.put(ints-1, preferData.get(ints));
				    	 }
				    	 
				    	 for(Integer ints : noPreferData.keySet()) {
				    		 newData.put(newData.size(), noPreferData.get(ints));
				    	 }
				    	 
				    	 Resets.clear();
				    	 Resets = newData;
				    	 
				    	 Aktiv = false;
						 oldX = 0;
						 oldZ = 0;
						  
						 durchlauf = -1;
					 } else {
						 Aktiv = false;
						 oldX = 0;
						 oldZ = 0;
						  
						 durchlauf = -1;
					 }
		  }
		 },20*Delayer);
	}
}
