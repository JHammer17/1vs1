package de.OnevsOne.Methods.Mobs;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.OnevsOne.main;
import de.OnevsOne.Methods.saveErrorMethod;
import de.OnevsOne.States.AllErrors;

/**
 * Der Code ist von JHammer
 *
 * 06.05.2016 um 15:13:04 Uhr
 */
public class spawnQueque implements Listener {

	private static main plugin;

	public static Location WarteLoc;
	static UUID WarteUUID;

	@SuppressWarnings("static-access")
	public spawnQueque(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@SuppressWarnings({ "static-access" })
	public static void spawnQuequeZombie() {
		YamlConfiguration cfg = plugin.getYaml("spawns");

		Location loc = new Location(Bukkit.getWorld("world"), 0, 0, 0);
		double x = cfg.getDouble("Queque.X");
		double y = cfg.getDouble("Queque.Y");
		double z = cfg.getDouble("Queque.Z");
		String worldname = cfg.getString("Queque.world");

		if (worldname == null) {
			saveErrorMethod.saveError(AllErrors.World,spawnQueque.class.getName(), "Position für die Warteschlange nicht gefunden!");
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

		Zombie zombie = (Zombie) loc.getWorld().spawnEntity(loc,
				EntityType.ZOMBIE);
		
		
		
		try {
			if(plugin.silentQueue) {
				if(plugin.getServerVersion().toLowerCase().contains("1.8") || plugin.getServerVersion().toLowerCase().contains("1_8")) {
					Object handle = zombie.getClass().getMethod("getHandle").invoke(zombie);
					handle.getClass().getMethod("b", boolean.class).invoke(handle, true);
				} else if(plugin.getServerVersion().toLowerCase().contains("1.9") || plugin.getServerVersion().toLowerCase().contains("1_9")) {
					Object handle = zombie.getClass().getMethod("getHandle").invoke(zombie);
					handle.getClass().getMethod("c", boolean.class).invoke(handle, true);
				} else {
					zombie.getClass().getMethod("setSilent", boolean.class).invoke(zombie, true);
				}
			}
			
			
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		
		if(zombie.isInsideVehicle()) {
			zombie.getVehicle().remove();
			zombie.remove();
			despawnQuequeZombie();
			spawnQuequeZombie();
			return;
		}
		
		if (zombie.isBaby()) {
			for(Entity en : zombie.getNearbyEntities(1.5, 1.5, 1.5)) {
				if(en.getType() == EntityType.CHICKEN) {
					en.remove();
				}
			}
			zombie.remove();
			
			despawnQuequeZombie();
			spawnQuequeZombie();
			return;
		}

		if(plugin.getVersion().equalsIgnoreCase("1.8")) zombie.setVillager(false);
		
		zombie.setRemoveWhenFarAway(false);
		zombie.setMaxHealth(500);
		zombie.setHealth(500);
		
		zombie.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE,100, false, false));
		
		
		zombie.setCanPickupItems(false);
		zombie.setCustomName(plugin.msgs.getMsg("quequeEntityName"));
		zombie.setCustomNameVisible(true);

		ItemStack Helm = new ItemStack(Material.SKULL_ITEM);
		Helm.setDurability((short) 2);
		ItemStack Brust = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
		ItemStack Hose = new ItemStack(Material.CHAINMAIL_LEGGINGS);
		ItemStack Schuhe = new ItemStack(Material.CHAINMAIL_BOOTS);

		ItemStack Schwert = new ItemStack(Material.STONE_SWORD);

		zombie.getEquipment().setHelmet(Helm);
		zombie.getEquipment().setChestplate(Brust);
		zombie.getEquipment().setLeggings(Hose);
		zombie.getEquipment().setBoots(Schuhe);

		zombie.getEquipment().setItemInHand(Schwert);

		
		
		//import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
		
		/*((CraftZombie)zombie).getHandle().b(true);
		try {
			//Bukkit.broadcastMessage("" + getNMSClass("entity.CraftZombie")..length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
		
		plugin.oldNameQueque = zombie.getCustomName();

		WarteUUID = zombie.getUniqueId();
		WarteLoc = loc;
		
		
		
			
		
		
		
		
	}
	
	

	public static Class<?> getNMSClass(String name) {
	// org.bukkit.craftbukkit.v1_8_R3...
	String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	try {
     return Class.forName("org.bukkit.craftbukkit." + version + "." + name);
	} catch (Exception e) {
		// TODO: handle exception
	}
	return null;
	}
	
	public static void respawnZombie() {
		despawnQuequeZombie();
		spawnQuequeZombie();

	}

	public static void despawnQuequeZombie() {
		if (WarteLoc != null && WarteUUID != null) {
			//WarteLoc.getChunk().load();

			for (int i = 0; i < WarteLoc.getChunk().getEntities().length; i++) {

				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(WarteUUID.toString())) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("quequeEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}

			}

			Location newChunk1 = WarteLoc;

			newChunk1 = newChunk1.add(16, 0, 0);
			//if (!newChunk1.getChunk().isLoaded()) //newChunk1.getChunk().load();
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(WarteUUID.toString())) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("quequeEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}

			newChunk1 = newChunk1.add(-32, 0, 0);
			//if (!newChunk1.getChunk().isLoaded()) //newChunk1.getChunk().load();
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(WarteUUID.toString())) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("quequeEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}

			newChunk1 = newChunk1.add(16, 0, 16);
			//if (!newChunk1.getChunk().isLoaded()) //newChunk1.getChunk().load();
			
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(WarteUUID.toString())) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("quequeEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}

			newChunk1 = newChunk1.add(0, 0, -32);
			//if (!newChunk1.getChunk().isLoaded()) //newChunk1.getChunk().load();
			
			for (int i = 0; i < newChunk1.getChunk().getEntities().length; i++) {
				if (WarteLoc.getChunk().getEntities()[i].getUniqueId()
						.toString().equalsIgnoreCase(WarteUUID.toString())) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
				if (WarteLoc.getChunk().getEntities()[i].getCustomName() != null
						&& WarteLoc.getChunk().getEntities()[i].getCustomName()
								.equalsIgnoreCase(
										plugin.msgs.getMsg("quequeEntityName"))) {
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
										plugin.msgs.getMsg("quequeEntityName"))) {
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
										plugin.msgs.getMsg("quequeEntityName"))) {
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
										plugin.msgs.getMsg("quequeEntityName"))) {
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
										plugin.msgs.getMsg("quequeEntityName"))) {
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
										plugin.msgs.getMsg("quequeEntityName"))) {
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
										plugin.msgs.getMsg("quequeEntityName"))) {
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
										plugin.msgs.getMsg("quequeEntityName"))) {
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
										plugin.msgs.getMsg("quequeEntityName"))) {
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
										plugin.msgs.getMsg("quequeEntityName"))) {
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
										plugin.msgs.getMsg("quequeEntityName"))) {
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
										plugin.msgs.getMsg("quequeEntityName"))) {
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
										plugin.msgs.getMsg("quequeEntityName"))) {
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
										plugin.msgs.getMsg("quequeEntityName"))) {
					WarteLoc.getChunk().getEntities()[i].remove();
				}
			}
			newChunk1 = newChunk1.add(16, 0, 16);
		}
	}

	private static void teleportBack() {
		try {
			for (World worlds : Bukkit.getWorlds()) {
				for (Entity en : worlds.getEntities()) {
					if (en instanceof Zombie) {
						if (en.getCustomName() != null
								&& en.getCustomName().equalsIgnoreCase(
										plugin.msgs.getMsg("quequeEntityName"))) {
							YamlConfiguration cfg = plugin.getYaml("spawns");

							Location loc = new Location(Bukkit.getWorld("world"),
									0, 0, 0);
							double x = cfg.getDouble("Queque.X");
							double y = cfg.getDouble("Queque.Y");
							double z = cfg.getDouble("Queque.Z");
							String worldname = cfg.getString("Queque.world");

							if (worldname == null) {
								//saveErrorMethod.saveError(AllErrors.World,spawnQueque.class.getName(), "Position für die Warteschlange nicht gefunden!");
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

	@EventHandler
	public void onTarget(EntityTargetEvent e) {
		if (e.getEntity() instanceof Zombie) {
			if (e.getEntity().getCustomName() != null
					&& e.getEntity()
							.getCustomName()
							.equalsIgnoreCase(
									plugin.msgs.getMsg("quequeEntityName"))) {
				if(e.getEntity() instanceof Player) return;
				e.setCancelled(true);
			}
		}

	}

	static int respawntime = 0;

	public static void respawner() {

		
				if (respawntime <= 0) {
					despawnQuequeZombie();
					spawnQuequeZombie();
					respawntime = 120;
				} else {
					respawntime--;
				}
				teleportBack();

			
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Zombie) {
			if (e.getEntity().getCustomName() != null
					&& e.getEntity()
							.getCustomName()
							.equalsIgnoreCase(
									plugin.msgs.getMsg("quequeEntityName"))) {
				e.setCancelled(true);
				e.setDamage(0);
			}

		}
	}

	
	
	

}
