package de.OnevsOne.Methods.Mobs;


import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import de.OnevsOne.main;
import de.OnevsOne.Kit_Methods.Multi_Kit_Manager;
import de.OnevsOne.Listener.Manager.Preferences_Manager;
import de.OnevsOne.Methods.saveErrorMethod;
import de.OnevsOne.States.AllErrors;
import de.OnevsOne.States.OneVsOnePlayer;
import de.OnevsOne.States.PlayerState;
import net.md_5.bungee.api.ChatColor;

/**
 * Der Code ist von JHammer
 *
 * 17.05.2016 um 13:07:20 Uhr
 */
public class spawnPrefVillager implements Listener {

	private static main plugin;

	public static Location WarteLoc;
	static UUID WarteUUID;
	
	@SuppressWarnings("static-access")
	public spawnPrefVillager(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		startChecker();
	}

	@SuppressWarnings("static-access")
	public static void spawnNewPrefVillager() {
		YamlConfiguration cfg = plugin.getYaml("spawns");
		
		Location loc = new Location(Bukkit.getWorld("world"), 0, 0, 0);
		double x = cfg.getDouble("PrefVillager.X");
		double y = cfg.getDouble("PrefVillager.Y");
		double z = cfg.getDouble("PrefVillager.Z");
		String worldname = cfg.getString("PrefVillager.world");
				
		if(worldname == null) {
			saveErrorMethod.saveError(AllErrors.World, spawnPrefVillager.class.getName(), "Position für den Einstellungs-Villager nicht gefunden");
			return;
		}
		World welt = Bukkit.getWorld(worldname);
		
				
		loc.setX(x);
		loc.setY(y);
		loc.setZ(z);
		loc.setWorld(welt);
		
		
		try {
			loc.getChunk().load(true);
		} catch (Exception e) {
			return;
		}
		
		Villager v = (Villager) loc.getWorld().spawnEntity(loc, EntityType.VILLAGER);
		
		try {
			if(plugin.silentPrefVillager) {
				if(plugin.getServerVersion().toLowerCase().contains("1.8") || plugin.getServerVersion().toLowerCase().contains("1_8")) {
					Object handle = v.getClass().getMethod("getHandle").invoke(v);
					handle.getClass().getMethod("b", boolean.class).invoke(handle, true);
				} else if(plugin.getServerVersion().toLowerCase().contains("1.9") || plugin.getServerVersion().toLowerCase().contains("1_9")) {
					Object handle = v.getClass().getMethod("getHandle").invoke(v);
					handle.getClass().getMethod("c", boolean.class).invoke(handle, true);
				} else {
					v.getClass().getMethod("setSilent", boolean.class).invoke(v, true);
				}
			}
			
			
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		
		
		
		v.setAdult();
		v.setRemoveWhenFarAway(false);
		v.setMaxHealth(500);
		v.setHealth(500);
		try {
			v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 100, false , false));
		} catch (Exception e) {}
		
		v.setCanPickupItems(false);
		v.setCustomName(plugin.msgs.getMsg("settingsEntityName"));
		v.setCustomNameVisible(true);
		v.setProfession(Profession.BLACKSMITH);
		
		
		
		WarteUUID = v.getUniqueId();
		WarteLoc = loc;
	}
	
	public static void respawnVillager() {
		despawnPrefVillager();
		spawnNewPrefVillager();
		
	}
	
	public static void despawnPrefVillager() {
		if (WarteLoc != null && WarteUUID != null) {
			WarteLoc.getChunk().load();

			for (int i = 0; i < WarteLoc.getChunk().getEntities().length; i++) {

				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(WarteUUID.toString())) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& ((LivingEntity) WarteLoc.getChunk().getEntities()[i]).getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("settingsEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}

			}

			Location newChunk1 = WarteLoc;

			newChunk1 = newChunk1.add(16, 0, 0);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(WarteUUID.toString())) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("settingsEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}

			newChunk1 = newChunk1.add(-32, 0, 0);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(WarteUUID.toString())) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("settingsEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}

			newChunk1 = newChunk1.add(16, 0, 16);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(WarteUUID.toString())) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("settingsEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}

			newChunk1 = newChunk1.add(0, 0, -32);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(WarteUUID.toString())) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("settingsEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(0, 0, 16);

			newChunk1 = newChunk1.add(16, 0, 16);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(WarteUUID.toString())) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("settingsEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(-16, 0, -16);

			newChunk1 = newChunk1.add(-16, 0, 16);

			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(WarteUUID.toString())) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("settingsEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(16, 0, -16);

			newChunk1 = newChunk1.add(16, 0, -16);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(WarteUUID.toString())) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("settingsEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(-16, 0, 16);

			newChunk1 = newChunk1.add(-16, 0, -16);

			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(WarteUUID.toString())) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("settingsEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(16, 0, 16);

		} else if (WarteUUID == null && WarteLoc != null) {
			WarteLoc.getChunk().load();

			for (int i = 0; i < WarteLoc.getChunk().getEntities().length; i++) {

				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(WarteUUID.toString())) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("settingsEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}

			}

			Location newChunk1 = WarteLoc;

			newChunk1 = newChunk1.add(16, 0, 0);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {

				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("settingsEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}

			newChunk1 = newChunk1.add(-32, 0, 0);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {

				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("settingsEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}

			newChunk1 = newChunk1.add(16, 0, 16);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {

				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("settingsEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}

			newChunk1 = newChunk1.add(0, 0, -32);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {

				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("settingsEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(0, 0, 16);

			newChunk1 = newChunk1.add(16, 0, 16);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {

				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("settingsEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(-16, 0, -16);

			newChunk1 = newChunk1.add(-16, 0, 16);

			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {

				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("settingsEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(16, 0, -16);

			newChunk1 = newChunk1.add(16, 0, -16);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {

				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("settingsEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(-16, 0, 16);

			newChunk1 = newChunk1.add(-16, 0, -16);

			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {

				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("settingsEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(16, 0, 16);
		}
	}
	
	private static void teleportBack() {
		try {
			for(World worlds : Bukkit.getWorlds()) {
				 for(Entity en : worlds.getEntities()) {
				  if(en instanceof Villager) {
				   if(en.getCustomName() != null && en.getCustomName().equalsIgnoreCase(plugin.msgs.getMsg("settingsEntityName"))) {
					   YamlConfiguration cfg = plugin.getYaml("spawns");
						
						Location loc = new Location(Bukkit.getWorld("world"), 0, 0, 0);
						double x = cfg.getDouble("PrefVillager.X");
						double y = cfg.getDouble("PrefVillager.Y");
						double z = cfg.getDouble("PrefVillager.Z");
						String worldname = cfg.getString("PrefVillager.world");
								
						if(worldname == null) {
							//saveErrorMethod.saveError(AllErrors.World, spawnPrefVillager.class.getName(), "Position für den Einstellungsvillager nicht gefunden");
							return;
						}
						World welt = Bukkit.getWorld(worldname);
								
						
						
						loc.setX(x);
						loc.setY(y);
						loc.setZ(z);
						loc.setWorld(welt);
						loc.setYaw(en.getLocation().getYaw());
						loc.setPitch(en.getLocation().getPitch());
						if(en.getLocation().distance(loc) > 1.5) en.teleport(loc);
						
				   }
				  }
				 }
				}
		} catch (Exception e) {}
		
	}
	
	
	
	static int respawntime = 0;
	
	public static void respawner() {
		
		if(respawntime <= 0) {
			 despawnPrefVillager();
			 spawnNewPrefVillager();
		     respawntime = 120;
		 } else {
			 respawntime--;
		 }
		 
		 teleportBack();
	}
	
	
	
	
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Villager) {
			if(e.getEntity().getCustomName() != null && e.getEntity().getCustomName().equalsIgnoreCase(plugin.msgs.getMsg("settingsEntityName"))) {
				e.setCancelled(true);
				e.setDamage(0);
			}
			
		}
	}

	
	public void startChecker() {
		 final HashMap<UUID, Location> playerLocs = new HashMap<>();
		    new BukkitRunnable() {
				
				@Override
				public void run() {
					
					for(final Player p : Bukkit.getOnlinePlayers())  {
						if(p == null || playerLocs == null) continue;
						if(playerLocs.containsKey(p.getUniqueId())) 
							//TODO Nullpointer https://hastebin.com/peruxutoqu.bash
							if(playerLocs.get(p.getUniqueId()).getWorld().getUID().equals(p.getWorld().getUID()))
							if(playerLocs.get(p.getUniqueId()).distance(p.getLocation()) > 0) {
								new BukkitRunnable() {
									
									@Override
									public void run() {
										onMove(p);
										
									}
								}.runTask(plugin);
							}
					}
						
								
					
					playerLocs.clear();
					
					for(OneVsOnePlayer p : plugin.getOneVsOnePlayersCopy().values()) 
						playerLocs.put(p.getPlayer().getUniqueId(), p.getPlayer().getLocation());
					
					
					
				}
			}.runTaskTimerAsynchronously(plugin, 0,1);
	}
	
	
	public void onMove(final Player p) {
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			
			@Override
			public void run() {
				
				if(WarteLoc != null && p.getWorld().getName().equalsIgnoreCase(WarteLoc.getWorld().getName())) {
					if(p.getLocation().distance(WarteLoc) <= 3 ) {
						p.spigot().setCollidesWithEntities(false);
						
						return;
					} else {
						p.spigot().setCollidesWithEntities(true);
						
					}
				} else {
					p.spigot().setCollidesWithEntities(true);
				}
				if(spawnQueque.WarteLoc != null && p.getWorld().getName().equalsIgnoreCase(spawnQueque.WarteLoc.getWorld().getName())) {
					if(p.getLocation().distance(spawnQueque.WarteLoc) <= 3) {
						p.spigot().setCollidesWithEntities(false);
						
						return;
					} else {
						p.spigot().setCollidesWithEntities(true);
						
					}
				} else {
					p.spigot().setCollidesWithEntities(true);
				}
				if(spawnBlackDealer.DealerLoc != null && p.getWorld().getName().equalsIgnoreCase(spawnBlackDealer.DealerLoc.getWorld().getName())) {
					if(p.getLocation().distance(spawnBlackDealer.DealerLoc) <= 3) {
						p.spigot().setCollidesWithEntities(false);
						
						return;
					} else {
						p.spigot().setCollidesWithEntities(true);
						
					}
				}else {
					p.spigot().setCollidesWithEntities(true);
				}
				
				if(WarteLoc != null && p.getWorld().getName().equalsIgnoreCase(WarteLoc.getWorld().getName())) {
					if(p.getLocation().distance(WarteLoc) <= 3) {
						p.spigot().setCollidesWithEntities(false);
						
						return;
					} else {
						p.spigot().setCollidesWithEntities(true);
						
					}
				} else {
					p.spigot().setCollidesWithEntities(true);
				}
				
			}
		});
		
	
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e) {
	 if(e.getRightClicked() instanceof Villager) {
	  if(e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equalsIgnoreCase(plugin.msgs.getMsg("settingsEntityName"))) {
		  e.setCancelled(true);
		  
			  if(plugin.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.InKitEdit || plugin.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.InLobby) {
				  Player p = e.getPlayer();
				  if(plugin.getOneVsOnePlayer(p).getPlayertournament() != null) {
					  p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentinTournament")));
					  return;
				  }
				  if(p.hasPermission("1vs1.useMultiplyKits") || p.hasPermission("1vs1.useMultiplyKits.*") || p.hasPermission("1vs1.*") || p.hasPermission("1vs1.Premium")) {
					   Multi_Kit_Manager.genKitSelector(p);
				   } else {
					   p.openInventory(Preferences_Manager.genPrefsInv(p,"",null));
				   }
				  return;
			  }
		  
		   
	  }
	  if(plugin.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.InKitEdit) {
		  e.setCancelled(true);
	  }
	 }
	}
	
	
	
}
