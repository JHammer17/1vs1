package de.OnevsOne.Arena.Reseter.Builder;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.event.Listener;

import de.OnevsOne.main;

/**
 * Der Code ist von JHammer
 *
 * 06.05.2016 um 21:52:21 Uhr
 */
public class CopyArena implements Listener {

	private main plugin;

	
	public CopyArena(main plugin) {
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
	
	public static void startReset(Location pos1, Location pos2, Location pos3, String ArenaID) {
		minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
		minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
		minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
		
		maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
		maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
		maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());
		
		int akX = 0;
		int akY = minY;
		int akZ = 0;
		
		String Welt = pos1.getWorld().getName();
		String PasteWelt = pos3.getWorld().getName();
		
		int XPaste = pos3.getBlockX();
		int YPaste = pos3.getBlockY();
		int ZPaste = pos3.getBlockZ();
		
		//						0 			1		  2			 3			4			5		  6 	    7         8         9          10             11         12            13				14
		String[] Datas = {"" + minX, "" + minY, "" + minZ, "" + maxX, "" + maxY, "" + maxZ, "" + akX, "" + akY, "" + akZ, "" + Welt, "" + XPaste, "" + YPaste, "" + ZPaste, "" + PasteWelt, "" + ArenaID};
		
		Resets.put(Resets.size(), Datas);
	}
	
	public void ResetScheduler() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				if(!Aktiv && Resets.containsKey(0)) {
					String[] Datas = Resets.get(0);
					reset(Datas);
					Aktiv = true;
					Name = Datas[14];
				}
				
			}
		}, 0, 20);
	}
	
	int oldX = 0;
	
	int oldZ = 0;
	int durchlauf = -1;
	
	int maxOffsetX = 0;
	int maxOffsetZ = 0;
	
	Location Ecke1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
	Location Ecke2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
	
	@SuppressWarnings("deprecation")
	public void reset(String[] Datas) {
		for(int x = Integer.parseInt(Datas[0]) ; x <= Integer.parseInt(Datas[3]) ; x++) {
		 for(int z = Integer.parseInt(Datas[2]) ; z <= Integer.parseInt(Datas[5]) ; z++) {
			 
			  int y = Integer.parseInt(Datas[7]);
			    
			    Location ResetPos3 = new Location(Bukkit.getWorld(Datas[13]), Integer.parseInt(Datas[10]), Integer.parseInt(Datas[11]), Integer.parseInt(Datas[12]));
				Block b = Bukkit.getWorld(Datas[9]).getBlockAt(x,y,z);
				
				int offsetX = oldX-x;
				int offsetZ = oldZ-z;
				
			    Location newLoc = ResetPos3;
			    
			    if(durchlauf != -1) {
			    	if(offsetX > 0) {
			    		if(maxOffsetX <= offsetX) {
			    			maxOffsetX = offsetX;
			    		}
			    	} else {
			    		if(maxOffsetX >= offsetX) {
			    			maxOffsetX = offsetX;
			    		}
			    	}
			    	
			    	if(offsetZ > 0) {
			    		if(maxOffsetZ <= offsetZ) {
			    			maxOffsetZ = offsetZ;
			    		}
			    	} else {
			    		if(maxOffsetZ >= offsetZ) {
			    			maxOffsetZ = offsetZ;
			    		}
			    	}
			    	
			    	  newLoc = newLoc.add(offsetX*-1,0,offsetZ*-1);
			    } else {					 
					 oldX = Integer.parseInt(Datas[0]);
					 oldZ = Integer.parseInt(Datas[2]);
					 durchlauf = 0;
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
					 BrewingStand br = (BrewingStand) newLoc.getBlock().getState();
					 br.getInventory().clear();
					}
					
					if(newLoc.getBlock().getType() == Material.HOPPER) {
					 Hopper h = (Hopper) newLoc.getBlock().getState();
					 h.getInventory().clear();
					}
				
				
				if(b.getType() == Material.TORCH 
						|| b.getType() == Material.REDSTONE_TORCH_OFF 
						|| b.getType() == Material.REDSTONE_TORCH_ON 
						|| b.getType() == Material.TRAP_DOOR 
						|| b.getType() == Material.IRON_TRAPDOOR 
						|| b.getType() == Material.BED_BLOCK 
						|| b.getType() == Material.WOOD_BUTTON 
						|| b.getType() == Material.STONE_BUTTON 
						|| b.getType() == Material.LEVER 
						|| b.getType() == Material.LADDER 
						|| b.getType() == Material.WALL_SIGN
						|| b.getType() == Material.WALL_BANNER
						|| b.getType() == Material.STANDING_BANNER
						|| b.getType() == Material.VINE) {
					placeBlockLater(newLoc, b);
				} else {
					newLoc.getBlock().setType(b.getType());
				    newLoc.getBlock().setData(b.getData());
				    
				    if(b.getType() == Material.SIGN_POST && newLoc.getBlock().getType() == Material.SIGN_POST) {
				    	Sign copy = (Sign) b.getState();
				    	Sign paste = (Sign) newLoc.getBlock().getState();
				    	
				    	paste.setLine(0, copy.getLine(0));
				    	paste.setLine(1, copy.getLine(1));
				    	paste.setLine(2, copy.getLine(2));
				    	paste.setLine(3, copy.getLine(3));
				    	
				    	paste.update();
				    	
				    }
				    
				    if(b.getType() == Material.SKULL && newLoc.getBlock().getType() == Material.SKULL) {
				    	Skull copy = (Skull) b.getState();
				    	Skull paste = (Skull) newLoc.getBlock().getState();
				    	
				    	paste.setSkullType(copy.getSkullType());
				    	paste.setOwner(copy.getOwner());
				    	paste.setRotation(copy.getRotation());
				    	
				    	paste.update();
				    	
				    }
				}
				int USEY = Integer.parseInt(Datas[1]);
				if(plugin.minArenaBuildDistanceWalls == 0) {
					USEY = -1;
				}
				USEY = USEY + plugin.minArenaBuildDistanceBottom;
				
				 if(offsetX == plugin.minArenaBuildDistanceWalls && offsetZ == plugin.minArenaBuildDistanceWalls && USEY == y) {
				   	Ecke1 = newLoc;
				 }
			    
				 if(offsetX == -1*plugin.minArenaBuildDistanceWalls && offsetZ == plugin.minArenaBuildDistanceWalls && USEY == y) {
					   	Ecke1 = newLoc;
					 }
				 
				 if(offsetX == plugin.minArenaBuildDistanceWalls && offsetZ == -1*plugin.minArenaBuildDistanceWalls && USEY == y) {
					   	Ecke1 = newLoc;
					 }
				 
				 if(offsetX == -1*plugin.minArenaBuildDistanceWalls && offsetZ == -1*plugin.minArenaBuildDistanceWalls && USEY == y) {
					   	Ecke1 = newLoc;
					 }
				
				 if(offsetX == 0 && offsetZ == 0 && USEY == Integer.parseInt(Datas[7])) {
					 Location putLoc = newLoc;
					 putLoc = putLoc.add(0,-1,0);
					 plugin.ArenaCorner1.put(Datas[14], putLoc);
				 }
		 }
		}
		 
		 durchlauf++;
		 int y = Integer.parseInt(Datas[11]);
		 y++;
		 
		 int x = Integer.parseInt(Datas[0]);
		 int z = Integer.parseInt(Datas[2]);
		 
		 
		 oldX = x;
		 oldZ = z;
		 
		 
		 Datas[11] = "" + y;
		 
		 Resets.remove(0);
		 Resets.put(0, Datas);
		 
		
		 
		 addY(Datas);
			
		}
	 
	@SuppressWarnings("deprecation")
	private void placeBlockLater(final Location newLoc, final Block b) {
		
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
			
			@Override
			public void run() {
				newLoc.getBlock().setTypeIdAndData(b.getTypeId(), b.getData(), true);
				
				if(b.getType() == Material.WALL_SIGN && newLoc.getBlock().getType() == Material.WALL_SIGN) {
			    	Sign copy = (Sign) b.getState();
			    	Sign paste = (Sign) newLoc.getBlock().getState();
			    	
			    	paste.setLine(0, copy.getLine(0));
			    	paste.setLine(1, copy.getLine(1));
			    	paste.setLine(2, copy.getLine(2));
			    	paste.setLine(3, copy.getLine(3));
			    	
			    	paste.update();
			    	
			    }
			    
			    if((b.getType() == Material.WALL_BANNER && newLoc.getBlock().getType() == Material.WALL_BANNER) || (b.getType() == Material.STANDING_BANNER && newLoc.getBlock().getType() == Material.STANDING_BANNER)) {
			    	
			    	Banner copy = (Banner) b.getState();
			    	Banner paste = (Banner) newLoc.getBlock().getState();
			    	
			    	paste.setBaseColor(copy.getBaseColor());
			    	paste.setPatterns(copy.getPatterns());
			    	paste.setData(copy.getData());
			    	
			    	
			    	paste.update();
			    	
			    }
			   
			    
			    if(b.getType() == Material.SKULL && newLoc.getBlock().getType() == Material.SKULL) {
			    	Skull copy = (Skull) b.getState();
			    	Skull paste = (Skull) newLoc.getBlock().getState();
			    	
			    	paste.setSkullType(copy.getSkullType());
			    	paste.setOwner(copy.getOwner());
			    	paste.setRotation(copy.getRotation());
			    	
			    	paste.update();
			    	
			    }
				
			}
		}, 8);
	}
	
	public void addY(final String[] Datas) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				
				if(Integer.parseInt(Datas[7]) >= Integer.parseInt(Datas[4])) {
					
					String ArenaName = Datas[14];
					
					int EckeY = Integer.parseInt(Datas[11]);
					EckeY--;
					EckeY = EckeY-plugin.minArenaBuildDistanceTop;
					
					int EckeX = maxOffsetX;
					int EckeZ = maxOffsetZ;
					
					int CornerX = maxOffsetX;
					int CornerZ = maxOffsetZ;
					
					if(EckeX > 0) {
						EckeX = EckeX-plugin.minArenaBuildDistanceWalls;
					} else {
						EckeX = EckeX+plugin.minArenaBuildDistanceWalls;
					}
					
					if(EckeZ > 0) {
						EckeZ = EckeZ-plugin.minArenaBuildDistanceWalls;
					} else {
						EckeZ = EckeZ+plugin.minArenaBuildDistanceWalls;
					}
					
					int X = Integer.parseInt(Datas[10]);
					int Z = Integer.parseInt(Datas[12]);
					
					Location newLoc = new Location(Bukkit.getWorld(Datas[13]), X, EckeY, Z);
					Location Corner2 = new Location(Bukkit.getWorld(Datas[13]), X, Integer.parseInt(Datas[11]), Z);
					
					EckeX = EckeX*-1;
					EckeZ = EckeZ*-1;
					
					CornerX = CornerX*-1;
					CornerZ = CornerZ*-1;
					
					
					newLoc.add(EckeX,0,EckeZ);
					
					Corner2 = Corner2.add(CornerX,-1,CornerZ);
					
					while(plugin.ResetingArenas.contains(Datas[14])) {
						plugin.ResetingArenas.remove(Datas[14]);
					}
					
					Ecke2 = newLoc;
					
					plugin.ArenaPos1.put(Datas[14], Ecke1);
					plugin.ArenaPos2.put(Datas[14], Ecke2);
					
					plugin.ArenaCorner2.put(Datas[14], Corner2);
					
					
					plugin.getRAMMgr().saveRAM(ArenaName, "Ready", "true");
					
					plugin.getRAMMgr().saveRAM(ArenaName, "Corner1.X", "" + plugin.ArenaCorner1.get(ArenaName).getBlockX());
					plugin.getRAMMgr().saveRAM(ArenaName, "Corner1.Y", "" + plugin.ArenaCorner1.get(ArenaName).getBlockY());
					plugin.getRAMMgr().saveRAM(ArenaName, "Corner1.Z", "" + plugin.ArenaCorner1.get(ArenaName).getBlockZ());
					
					plugin.getRAMMgr().saveRAM(ArenaName, "Corner2.X", "" + plugin.ArenaCorner2.get(ArenaName).getBlockX());
					plugin.getRAMMgr().saveRAM(ArenaName, "Corner2.Y", "" + plugin.ArenaCorner2.get(ArenaName).getBlockY());
					plugin.getRAMMgr().saveRAM(ArenaName, "Corner2.Z", "" + plugin.ArenaCorner2.get(ArenaName).getBlockZ());
					
					plugin.getRAMMgr().saveRAM(ArenaName, "Pos1.X", "" + plugin.ArenaPos1.get(ArenaName).getBlockX());
					plugin.getRAMMgr().saveRAM(ArenaName, "Pos1.Y", "" + plugin.ArenaPos1.get(ArenaName).getBlockY());
					plugin.getRAMMgr().saveRAM(ArenaName, "Pos1.Z", "" + plugin.ArenaPos1.get(ArenaName).getBlockZ());
					
					plugin.getRAMMgr().saveRAM(ArenaName, "Pos2.X", "" + plugin.ArenaPos2.get(ArenaName).getBlockX());
					plugin.getRAMMgr().saveRAM(ArenaName, "Pos2.Y", "" + plugin.ArenaPos2.get(ArenaName).getBlockY());
					plugin.getRAMMgr().saveRAM(ArenaName, "Pos2.Z", "" + plugin.ArenaPos2.get(ArenaName).getBlockZ());
					
					plugin.getRAMMgr().saveRAM(ArenaName, "Arena.World", "" + plugin.ArenaCorner1.get(ArenaName).getWorld().getName());
					
					int Tasks = Resets.size();
					Tasks = Tasks-1;
					
					nextWarteschlange();
					
					return;
				}
				
				
				int newY = Integer.parseInt(Datas[7]);
				newY = newY+1;
				
				Datas[7] = "" + newY;
				
				Resets.remove(0);
				Resets.put(0, Datas);
				reset(Resets.get(0));
				
			}
		}, plugin.getAState().getArenaBuildDelay(Datas[14]));
	}
	
	 
	
	private void nextWarteschlange() {
			Name = "-";
			if(Resets.size() != 0) {
		    	 Resets.remove(0);
		    	 
		    	 if(Resets.isEmpty()) {
		    		 resetData();
		    		
		    		 
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
		    	 
		    	 
		    	 
		    	 resetData();
			 } else {
				 resetData();
			 }
		
     
	}
	
	private void resetData() {
		 Aktiv = false;
		 oldX = 0;
		 oldZ = 0;
		 
		 maxOffsetX = 0;
		 maxOffsetZ = 0;
		  
		 durchlauf = -1;
		 Ecke1 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
		 Ecke2 = new Location(Bukkit.getWorlds().get(0), 0, 0, 0);
	}
}