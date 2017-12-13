package de.OnevsOne.Methods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.OnevsOne.main;
import de.OnevsOne.Kit_Methods.KitManager;

public class KitStands {

	private main plugin;

	public KitStands(main plugin) {
		this.plugin = plugin;
	}
	
	public void spawnStands() {
		removeCurrent();
		
		if(plugin.getYaml("spawns").getConfigurationSection("TopKitStands") != null) 
			spawnStandsRanking();
		
		
		
		if(plugin.getYaml("spawns").getConfigurationSection("KitStands") != null)  {
		 for(String stands : plugin.getYaml("spawns").getConfigurationSection("KitStands").getKeys(false))
		  spawnStand(stands);
		}
		
		
		removeOld();
		fillStands();
		
		
	}
	
	
	private void spawnStandsRanking() {
		YamlConfiguration cfg = plugin.getYaml("spawns");
		
		
		if(cfg.getConfigurationSection("TopKitStands.all") != null) {
		 HashMap<Integer, String> kits = plugin.getDBMgr().Top5Kits(0);
		 for(String str : cfg.getConfigurationSection("TopKitStands.all").getKeys(false)) {
			
			 int place = 0;
			 try {
	   	      place = Integer.parseInt(str);
	   	      if(place <= 0) continue;
	   	     } catch (NumberFormatException e) {continue;}
			 
			 if(kits == null || !kits.containsKey(place)) continue;
			 
			 
			 double X = cfg.getDouble("TopKitStands.all." + str + ".X");
			 int Y =  (int) cfg.getDouble("TopKitStands.all." + str + ".Y");
			 double Z = cfg.getDouble("TopKitStands.all." + str + ".Z");
				
			 double Yaw =  cfg.getDouble("TopKitStands.all." + str + ".Yaw");
			 double Pitch = cfg.getDouble("TopKitStands.all." + str + ".Pitch");
				
			 String World = cfg.getString("TopKitStands.all." + str + ".World");
				
			 if(World == null || Bukkit.getWorld(World) == null) continue;
			 
			 Location loc = new Location(Bukkit.getWorld(World), X, Y, Z, (float)Yaw, (float)Pitch);
			 	
			 final ArmorStand stand = Bukkit.getWorld(World).spawn(loc, ArmorStand.class);
				
			 stand.setArms(true);
			 stand.setCustomName("/kit §l" + kits.get(place) + ":" + 1 + "");
			 stand.setCustomNameVisible(true);
				
			 
				
				plugin.kitStands.put(stand.getUniqueId(), stand);
				plugin.kitStandsKit.put(stand.getUniqueId(), kits.get(place) + ":1");
				plugin.kitStandsName.put(stand.getUniqueId(), "TopKitStands.all." + place);
				plugin.topKitStands.put(stand.getUniqueId(), stand);
				
				final ArmorStand infoStand = loc.getWorld().spawn(loc.add(0,0.25,0), ArmorStand.class);
				
				
				final String fKit = kits.get(place);
				
				new SimpleAsync(new Runnable() {
					
					@Override
					public void run() {
						YamlConfiguration cfg = plugin.getYaml("spawns");
						
						int type = cfg.getInt(plugin.kitStandsName.get(stand.getUniqueId()) + ".statsType");
						
						if(type == 0) infoStand.setCustomName("§l" + plugin.getDBMgr().getStatsKit(fKit, 1, 0) + "x §rAll Time");		
						else if(type == 1) infoStand.setCustomName("§l" + plugin.getDBMgr().getStatsKit(fKit, 1, 1) + "x §rin 30 Tagen");		
						else infoStand.setCustomName("§l" + plugin.getDBMgr().getStatsKit(fKit, 1, 2) + "x §rin 24h");		
						
						
						infoStand.setCustomNameVisible(true);
						infoStand.setVisible(false);
						infoStand.setGravity(false);
					}
				}, plugin).start();
				
				
				
				plugin.kitStandsInfo.put(infoStand.getUniqueId(), infoStand);
				plugin.kitStandsCon.put(stand.getUniqueId(), infoStand.getUniqueId());
			 
			 
			 
		 }
		}
		
		if(cfg.getConfigurationSection("TopKitStands.30d") != null) {
			 HashMap<Integer, String> kits = plugin.getDBMgr().Top5Kits(1);
			 for(String str : cfg.getConfigurationSection("TopKitStands.30d").getKeys(false)) {
				
				 int place = 0;
				 try {
		   	      place = Integer.parseInt(str);
		   	      if(place <= 0) continue;
		   	     } catch (NumberFormatException e) {continue;}
				 
				 if(kits == null || !kits.containsKey(place)) continue;
				 
				 
				 double X =   cfg.getDouble("TopKitStands.30d." + str + ".X");
				 int Y =  (int) cfg.getDouble("TopKitStands.30d." + str + ".Y");
				 double Z =   cfg.getDouble("TopKitStands.30d." + str + ".Z");
					
				 double Yaw =  cfg.getDouble("TopKitStands.30d." + str + ".Yaw");
				 double Pitch = cfg.getDouble("TopKitStands.30d." + str + ".Pitch");
					
				 String World = cfg.getString("TopKitStands.30d." + str + ".World");
					
				 if(World == null || Bukkit.getWorld(World) == null) continue;
				 
				 Location loc = new Location(Bukkit.getWorld(World), X, Y, Z, (float)Yaw, (float)Pitch);
				 	
				 final ArmorStand stand = Bukkit.getWorld(World).spawn(loc, ArmorStand.class);
					
				 stand.setArms(true);
				 stand.setCustomName("/kit §l" + kits.get(place) + ":" + 1 + "");
				 stand.setCustomNameVisible(true);
					
				 
					
					plugin.kitStands.put(stand.getUniqueId(), stand);
					plugin.kitStandsKit.put(stand.getUniqueId(), kits.get(place) + ":1");
					plugin.kitStandsName.put(stand.getUniqueId(), "TopKitStands.30d." + place);
					plugin.topKitStands.put(stand.getUniqueId(), stand);
					
					final ArmorStand infoStand = loc.getWorld().spawn(loc.add(0,0.25,0), ArmorStand.class);
					
					
					final String fKit = kits.get(place);
					
					new SimpleAsync(new Runnable() {
						
						@Override
						public void run() {
							YamlConfiguration cfg = plugin.getYaml("spawns");
							
							int type = cfg.getInt(plugin.kitStandsName.get(stand.getUniqueId())  + ".statsType");
							
							if(type == 0) infoStand.setCustomName("§l" + plugin.getDBMgr().getStatsKit(fKit, 1, 0) + "x §rAll Time");		
							else if(type == 1) infoStand.setCustomName("§l" + plugin.getDBMgr().getStatsKit(fKit, 1, 1) + "x §rin 30 Tagen");		
							else infoStand.setCustomName("§l" + plugin.getDBMgr().getStatsKit(fKit, 1, 2) + "x §rin 24h");		
							
							
							infoStand.setCustomNameVisible(true);
							infoStand.setVisible(false);
							infoStand.setGravity(false);
						}
					}, plugin).start();
					
					
					
					plugin.kitStandsInfo.put(infoStand.getUniqueId(), infoStand);
					plugin.kitStandsCon.put(stand.getUniqueId(), infoStand.getUniqueId());
				 
				 
				 
			 }
			}
		
		if(cfg.getConfigurationSection("TopKitStands.24h") != null) {
			 HashMap<Integer, String> kits = plugin.getDBMgr().Top5Kits(2);
			 for(String str : cfg.getConfigurationSection("TopKitStands.24h").getKeys(false)) {
				
				 int place = 0;
				 try {
		   	      place = Integer.parseInt(str);
		   	      if(place <= 0) continue;
		   	     } catch (NumberFormatException e) {continue;}
				 
				 if(kits == null || !kits.containsKey(place)) continue;
				 
				 
				 double X =   cfg.getDouble("TopKitStands.24h." + str + ".X");
				 int Y =  (int) cfg.getDouble("TopKitStands.24h." + str + ".Y");
				 double Z =   cfg.getDouble("TopKitStands.24h." + str + ".Z");
					
				 double Yaw =  cfg.getDouble("TopKitStands.24h." + str + ".Yaw");
				 double Pitch = cfg.getDouble("TopKitStands.24h." + str + ".Pitch");
					
				 String World = cfg.getString("TopKitStands.24h." + str + ".World");
					
				 if(World == null || Bukkit.getWorld(World) == null) continue;
				 
				 Location loc = new Location(Bukkit.getWorld(World), X, Y, Z, (float)Yaw, (float)Pitch);
				 	
				 final ArmorStand stand = Bukkit.getWorld(World).spawn(loc, ArmorStand.class);
					
				 stand.setArms(true);
				 stand.setCustomName("/kit §l" + kits.get(place) + ":" + 1 + "");
				 stand.setCustomNameVisible(true);
					
				 
					
					plugin.kitStands.put(stand.getUniqueId(), stand);
					plugin.kitStandsKit.put(stand.getUniqueId(), kits.get(place) + ":1");
					plugin.kitStandsName.put(stand.getUniqueId(), "TopKitStands.24h." + place);
					plugin.topKitStands.put(stand.getUniqueId(), stand);
					
					final ArmorStand infoStand = loc.getWorld().spawn(loc.add(0,0.25,0), ArmorStand.class);
					
					
					final String fKit = kits.get(place);
					
					new SimpleAsync(new Runnable() {
						
						@Override
						public void run() {
							YamlConfiguration cfg = plugin.getYaml("spawns");
							
							int type = cfg.getInt(plugin.kitStandsName.get(stand.getUniqueId())  + ".statsType");
							
							if(type == 0) infoStand.setCustomName("§l" + plugin.getDBMgr().getStatsKit(fKit, 1, 0) + "x §rAll Time");		
							else if(type == 1) infoStand.setCustomName("§l" + plugin.getDBMgr().getStatsKit(fKit, 1, 1) + "x §rin 30 Tagen");		
							else infoStand.setCustomName("§l" + plugin.getDBMgr().getStatsKit(fKit, 1, 2) + "x §rin 24h");		
							
							
							infoStand.setCustomNameVisible(true);
							infoStand.setVisible(false);
							infoStand.setGravity(false);
						}
					}, plugin).start();
					
					
					
					plugin.kitStandsInfo.put(infoStand.getUniqueId(), infoStand);
					plugin.kitStandsCon.put(stand.getUniqueId(), infoStand.getUniqueId());
				 
				 
				 
			 }
			}
		
		
	}
	
	
	private void spawnStand(String Name) {
		
		YamlConfiguration cfg = plugin.getYaml("spawns");
		
		double X =   cfg.getDouble("KitStands." + Name + ".X");
		int Y =  (int) cfg.getDouble("KitStands." + Name + ".Y");
		double Z =   cfg.getDouble("KitStands." + Name + ".Z");
		
		double Yaw =  cfg.getDouble("KitStands." + Name + ".Yaw");
		double Pitch = cfg.getDouble("KitStands." + Name + ".Pitch");
		
		String World = cfg.getString("KitStands." + Name + ".World");
		
		if(World == null) return;
		
		String Kit = cfg.getString("KitStands." + Name + ".Kit");
		
		int subID = 1;
		if(Kit.contains(":")) {
			Kit = Kit.split(":")[0];
			if(Kit.split(":").length >= 2) {
				try {
					subID = Integer.parseInt(Kit.split(":")[1]);
				} catch (Exception e) {}
			}	
		}
		
		Location spawnL = new Location(Bukkit.getWorld(World), X, Y, Z,(float) Yaw,(float) Pitch);
		
		final ArmorStand stand = Bukkit.getWorld(World).spawn(spawnL, ArmorStand.class);
		
		stand.setArms(true);
		stand.setCustomName("/kit §l" + Kit + ":" + subID + "");
		stand.setCustomNameVisible(true);
		
		stand.setSmall(cfg.getBoolean("KitStands." + Name + ".Small"));
		
		plugin.kitStands.put(stand.getUniqueId(), stand);
		plugin.kitStandsKit.put(stand.getUniqueId(), Kit);
		plugin.kitStandsName.put(stand.getUniqueId(), Name);
		
		
		final ArmorStand infoStand = spawnL.getWorld().spawn(spawnL.add(0,0.25,0), ArmorStand.class);
		
		
		final String fKit = Kit;
		final int fSubID = subID;
		
		new SimpleAsync(new Runnable() {
			
			@Override
			public void run() {
				YamlConfiguration cfg = plugin.getYaml("spawns");
				
				int type = cfg.getInt("KitStands." + plugin.kitStandsName.get(stand.getUniqueId()) + ".statsType");
				
				if(type == 0) infoStand.setCustomName("§l" + plugin.getDBMgr().getStatsKit(fKit, fSubID, 0) + "x §rAll Time");		
				else if(type == 1) infoStand.setCustomName("§l" + plugin.getDBMgr().getStatsKit(fKit, fSubID, 1) + "x §rin 30 Tagen");		
				else infoStand.setCustomName("§l" + plugin.getDBMgr().getStatsKit(fKit, fSubID, 2) + "x §rin 24h");		
				
				
				infoStand.setCustomNameVisible(true);
				infoStand.setVisible(false);
				infoStand.setGravity(false);
			}
		}, plugin).start();
		
		
		
		plugin.kitStandsInfo.put(infoStand.getUniqueId(), infoStand);
		plugin.kitStandsCon.put(stand.getUniqueId(), infoStand.getUniqueId());
	}
	
	
	
	public void removeOld() {
		for(ArmorStand stand : plugin.kitStands.values()) {
			for(Entity en : stand.getNearbyEntities(1, 1, 1)) {
				 if(en instanceof ArmorStand) {
				  ArmorStand foundStand = (ArmorStand) en;
				  if(!plugin.kitStands.containsKey(foundStand.getUniqueId()) &&
					 !plugin.kitStandsInfo.containsKey(foundStand.getUniqueId())) foundStand.remove();
				 }
				}
		}
		
	}
	
	public void removeCurrent() {
		ArrayList<ArmorStand> stands = new ArrayList<>();
		
		for(ArmorStand stand : plugin.kitStands.values()) stands.add(stand);
		for(ArmorStand stand : plugin.kitStandsInfo.values()) stands.add(stand);
		for(ArmorStand stand : stands) {
			plugin.kitStands.remove(stand.getUniqueId());
			plugin.kitStandsKit.remove(stand.getUniqueId());
			plugin.kitStandsName.remove(stand.getUniqueId());
			
			plugin.kitStandsInfo.remove(stand.getUniqueId());
			plugin.kitStandsCon.remove(stand.getUniqueId());
			
			stand.remove();
			
		}
	}

	public void fillStands() {
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				YamlConfiguration cfg = plugin.getYaml("spawns");
				
				try {
					for(ArmorStand stand : plugin.kitStands.values()) {
						 if(plugin.kitStandsKit.containsKey(stand.getUniqueId())) {
							
						  String Kit = plugin.kitStandsKit.get(stand.getUniqueId());
						  String subID = "d";
						  if(Kit.contains(":")) {
							   if(Kit.split(":").length >= 2) subID = Kit.split(":")[1];
								  Kit = Kit.split(":")[0];  
							   }
						       
						  
						  if(plugin.getDBMgr().isCustomKitExists(Kit) == 2) {
							try {

							       if(subID.equalsIgnoreCase("d")) subID = plugin.getDBMgr().getDefaultKit(plugin.getDBMgr().getUUID(Kit));
							  
							  	   if(subID.equalsIgnoreCase("1")) subID = "";
								
							  	if(plugin.getDBMgr().getKit(plugin.getDBMgr().getUUID(Kit.split(":")[0]), true, subID).equalsIgnoreCase(""))continue;
							  	   
								ItemStack[] armor = new KitManager(plugin).fromBase64Armor(plugin.getDBMgr().getKit(plugin.getDBMgr().getUUID(Kit.split(":")[0]), true, subID));
							
								stand.setHelmet(armor[3]);
								stand.setChestplate(armor[2]);
								stand.setLeggings(armor[1]);
								stand.setBoots(armor[0]);
								
								 Inventory inv = new KitManager(plugin).fromBase64(plugin.getDBMgr().getKit(plugin.getDBMgr().getUUID(Kit.split(":")[0]), false, subID));
								 if(plugin.topKitStands.containsKey(stand.getUniqueId())) {
								 	stand.setItemInHand(inv.getItem(cfg.getInt(plugin.kitStandsName.get(stand.getUniqueId()) + ".customSlot")));
								 } else {
									stand.setItemInHand(inv.getItem(cfg.getInt("KitStands." + plugin.kitStandsName.get(stand.getUniqueId()) + ".customSlot")));
								 }
								
								
								
							} catch (IOException e) {
								e.printStackTrace();
							}
							  
						  } else if(plugin.getDBMgr().isCustomKitExists(Kit) == 1) {
							  try {
								  	if(subID.equalsIgnoreCase("1")) subID = "";
								  
									ItemStack[] armor = new KitManager(plugin).fromBase64Armor(plugin.getDBMgr().loadCustomKitArmor(Kit));
								
									stand.setHelmet(armor[3]);
									stand.setChestplate(armor[2]);
									stand.setLeggings(armor[1]);
									stand.setBoots(armor[0]);
									
									Inventory inv = new KitManager(plugin).fromBase64(plugin.getDBMgr().loadCustomKitInv(Kit));
									
									
									if(plugin.topKitStands.containsKey(stand.getUniqueId())) {
										stand.setItemInHand(inv.getItem(cfg.getInt(plugin.kitStandsName.get(stand.getUniqueId()) + ".customSlot")));
									} else {
										stand.setItemInHand(inv.getItem(cfg.getInt("KitStands." + plugin.kitStandsName.get(stand.getUniqueId()) + ".customSlot")));
									}
									
									
								} catch (IOException e) {
									e.printStackTrace();
								}
						  }
						 }
						}
					
				} catch (ConcurrentModificationException e) {}
			}
				
		});
		
		t.start();
		
	}
	
	public boolean standsExits() {
		for(ArmorStand stand : plugin.kitStands.values()) {
			if(stand == null) return false;
			 else if(stand.isDead()) return false;
		}
		
		return true;
	}
	
}
