package de.OnevsOne.Methods.Mobs;


import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import de.OnevsOne.main;
import de.OnevsOne.Methods.BlackDealerInvCreator;
import de.OnevsOne.Methods.getItems;
import de.OnevsOne.States.PlayerState;
import net.md_5.bungee.api.ChatColor;

public class spawnBlackDealer implements Listener {

	private static main plugin;

	public static Location DealerLoc;
	static UUID DealerUUID;
	
	@SuppressWarnings("static-access")
	public spawnBlackDealer(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@SuppressWarnings("static-access")
	public static void spawnBlackDealerE() {
		YamlConfiguration cfg = plugin.getYaml("spawns");
		
		Location loc = new Location(Bukkit.getWorld("world"), 0, 0, 0);
		double x = cfg.getDouble("BlackDealer.X");
		double y = cfg.getDouble("BlackDealer.Y");
		double z = cfg.getDouble("BlackDealer.Z");
		String worldname = cfg.getString("BlackDealer.world");
				
		if(worldname == null) return;
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
		
		Skeleton v = (Skeleton) loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
		
		try {
			if(plugin.silentBlackDealer) {
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
		
		v.setRemoveWhenFarAway(false);
		v.setMaxHealth(500);
		v.setHealth(500);
		try {
			v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 100, false , false));
		} catch (Exception e) {}
		
		v.setCanPickupItems(false);
		v.setCustomName(plugin.msgs.getMsg("blackDealerEntityName"));
		v.setCustomNameVisible(true);
		v.getEquipment().setItemInHand(getItems.createItem(Material.SUGAR,0,1,"",null));
		v.getEquipment().setHelmet(getItems.createItem(Material.SKULL_ITEM, 0, 1, "", null));
		
		
		
		
		DealerUUID = v.getUniqueId();
		DealerLoc = loc;
	}
	
	
	
	
	public static void respawnVillager() {
		despawnBlackDealer();
		spawnBlackDealerE();
		
	}
	
	public static void despawnBlackDealer() {
		if (DealerLoc != null && DealerUUID != null) {
			DealerLoc.getChunk().load();

			for (int i = 0; i < DealerLoc.getChunk().getEntities().length; i++) {

				if (DealerLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(DealerUUID.toString())) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
				if (DealerLoc.getChunk().getEntities()[i].getCustomName() != null
						&& ((LivingEntity) DealerLoc.getChunk().getEntities()[i]).getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("blackDealerEntityName"))) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}

			}

			Location newChunk1 = DealerLoc;

			newChunk1 = newChunk1.add(16, 0, 0);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (DealerLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(DealerUUID.toString())) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
				if (DealerLoc.getChunk().getEntities()[i].getCustomName() != null
						&& DealerLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("blackDealerEntityName"))) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
			}

			newChunk1 = newChunk1.add(-32, 0, 0);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (DealerLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(DealerUUID.toString())) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
				if (DealerLoc.getChunk().getEntities()[i].getCustomName() != null
						&& DealerLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("blackDealerEntityName"))) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
			}

			newChunk1 = newChunk1.add(16, 0, 16);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (DealerLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(DealerUUID.toString())) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
				if (DealerLoc.getChunk().getEntities()[i].getCustomName() != null
						&& DealerLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("blackDealerEntityName"))) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
			}

			newChunk1 = newChunk1.add(0, 0, -32);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (DealerLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(DealerUUID.toString())) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
				if (DealerLoc.getChunk().getEntities()[i].getCustomName() != null
						&& DealerLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("blackDealerEntityName"))) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(0, 0, 16);

			newChunk1 = newChunk1.add(16, 0, 16);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (DealerLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(DealerUUID.toString())) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
				if (DealerLoc.getChunk().getEntities()[i].getCustomName() != null
						&& DealerLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("blackDealerEntityName"))) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(-16, 0, -16);

			newChunk1 = newChunk1.add(-16, 0, 16);

			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (DealerLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(DealerUUID.toString())) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
				if (DealerLoc.getChunk().getEntities()[i].getCustomName() != null
						&& DealerLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("blackDealerEntityName"))) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(16, 0, -16);

			newChunk1 = newChunk1.add(16, 0, -16);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (DealerLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(DealerUUID.toString())) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
				if (DealerLoc.getChunk().getEntities()[i].getCustomName() != null
						&& DealerLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("blackDealerEntityName"))) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(-16, 0, 16);

			newChunk1 = newChunk1.add(-16, 0, -16);

			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (DealerLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(DealerUUID.toString())) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
				if (DealerLoc.getChunk().getEntities()[i].getCustomName() != null
						&& DealerLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("blackDealerEntityName"))) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(16, 0, 16);

		} else if (DealerUUID == null && DealerLoc != null) {
			DealerLoc.getChunk().load();

			for (int i = 0; i < DealerLoc.getChunk().getEntities().length; i++) {

				if (DealerLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(DealerUUID.toString())) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
				if (DealerLoc.getChunk().getEntities()[i].getCustomName() != null
						&& DealerLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("blackDealerEntityName"))) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}

			}

			Location newChunk1 = DealerLoc;

			newChunk1 = newChunk1.add(16, 0, 0);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {

				if (DealerLoc.getChunk().getEntities()[i].getCustomName() != null
						&& DealerLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("blackDealerEntityName"))) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
			}

			newChunk1 = newChunk1.add(-32, 0, 0);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {

				if (DealerLoc.getChunk().getEntities()[i].getCustomName() != null
						&& DealerLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("blackDealerEntityName"))) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
			}

			newChunk1 = newChunk1.add(16, 0, 16);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {

				if (DealerLoc.getChunk().getEntities()[i].getCustomName() != null
						&& DealerLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("blackDealerEntityName"))) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
			}

			newChunk1 = newChunk1.add(0, 0, -32);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {

				if (DealerLoc.getChunk().getEntities()[i].getCustomName() != null
						&& DealerLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("blackDealerEntityName"))) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(0, 0, 16);

			newChunk1 = newChunk1.add(16, 0, 16);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {

				if (DealerLoc.getChunk().getEntities()[i].getCustomName() != null
						&& DealerLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("blackDealerEntityName"))) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(-16, 0, -16);

			newChunk1 = newChunk1.add(-16, 0, 16);

			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {

				if (DealerLoc.getChunk().getEntities()[i].getCustomName() != null
						&& DealerLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("blackDealerEntityName"))) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(16, 0, -16);

			newChunk1 = newChunk1.add(16, 0, -16);
			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {

				if (DealerLoc.getChunk().getEntities()[i].getCustomName() != null
						&& DealerLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("blackDealerEntityName"))) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(-16, 0, 16);

			newChunk1 = newChunk1.add(-16, 0, -16);

			if (!newChunk1.getChunk().isLoaded()) {
				//newChunk1.getChunk().load();
			}
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {

				if (DealerLoc.getChunk().getEntities()[i].getCustomName() != null
						&& DealerLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("blackDealerEntityName"))) {
					DealerLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(16, 0, 16);
		}
	}
	
	private static void teleportBack() {
		try {
			for(World worlds : Bukkit.getWorlds()) {
				 for(Entity en : worlds.getEntities()) {
				  if(en instanceof Skeleton) {
				   if(en.getCustomName() != null && en.getCustomName().equalsIgnoreCase(plugin.msgs.getMsg("blackDealerEntityName"))) {
					   YamlConfiguration cfg = plugin.getYaml("spawns");
						
						Location loc = new Location(Bukkit.getWorld("world"), 0, 0, 0);
						double x = cfg.getDouble("BlackDealer.X");
						double y = cfg.getDouble("BlackDealer.Y");
						double z = cfg.getDouble("BlackDealer.Z");
						String worldname = cfg.getString("BlackDealer.world");
								
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
				 despawnBlackDealer();
				 spawnBlackDealerE();
			     respawntime = 120;
			 } else {
				 respawntime--;
			 }
			 
			 teleportBack();
			
			
	}
	
	
	
	
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Skeleton) {
			if(e.getEntity().getCustomName() != null && e.getEntity().getCustomName().equalsIgnoreCase(plugin.msgs.getMsg("blackDealerEntityName"))) {
				e.setCancelled(true);
				e.setDamage(0);
			}
			
		}
	}

	
	
	@EventHandler
	public void onDamage(EntityTargetEvent e) {
		if(e.getTarget() instanceof Player) {
			Player p = (Player) e.getTarget();
			
			 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit || plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby || plugin.getOneVsOnePlayer(p).getpState() == PlayerState.Spec) {
				 if(e.getEntity() instanceof Player) return;
				 
				 e.setCancelled(true);
			 }
			
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e) {
	 if(e.getRightClicked() instanceof Skeleton) {
	  if(e.getRightClicked().getCustomName() != null && e.getRightClicked().getCustomName().equalsIgnoreCase(plugin.msgs.getMsg("blackDealerEntityName"))) {
		  e.setCancelled(true);
		  
		  ItemStack[] material = e.getPlayer().getInventory().getArmorContents();
		  
		  
		  
			  if(plugin.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.InKitEdit) {
				  Player p = e.getPlayer();
				  if(plugin.getOneVsOnePlayer(p).getPlayertournament() != null) {
					  p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentinTournament")));
					  return;
				  }
				  if(p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR) {
					  p.getInventory().setArmorContents(material);
					  p.updateInventory();
					  BlackDealerInvCreator.createInv(p);
					  
				  } else {
					  p.sendMessage("§cDu musst ein Item in der Hand haben!"); //TODO
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
