package de.OnevsOne.Arena.Manager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import de.OnevsOne.main;
import de.OnevsOne.Commands.MainCommand;
import de.OnevsOne.Listener.KillEvent;
import de.OnevsOne.Listener.Manager.ChallangeManager;
import de.OnevsOne.Listener.Manager.TeamManager;
import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.Methods.FightEnder.FightEnd;
import de.OnevsOne.Methods.Tournament.TournamentManager;
import de.OnevsOne.States.ArenaTeamPlayer;
import de.OnevsOne.States.OneVsOnePlayer;
import de.OnevsOne.States.PlayerPrefs;
import de.OnevsOne.States.PlayerState;

/**
 * Der Code ist von JHammer
 *
 * 07.05.2016 um 14:18:16 Uhr
 */

/*
 * In dieser Klasse werden viele Events die auf Arenen zugeschnitten sind
 * gemanaged.
 * 
 * Methoden: public static boolean checkRegion(Location Check, Location Min,
 * Location Max);
 * 
 * Sonst nur Events.
 */

public class ArenaEvents implements Listener {

	private static main plugin;

	@SuppressWarnings("static-access")
	public ArenaEvents(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		startChecker();
	}

	// Werte:
	public static int minX, minY, minZ;
	public static int maxX, maxY, maxZ;
	// ------

	// Move Event, wenn die Arena noch nicht gestartet ist
	
	public void startChecker() {
		 final HashMap<UUID, Location> playerLocs = new HashMap<>();
		    new BukkitRunnable() {
				
				@Override
				public void run() {
					
					for(final OneVsOnePlayer p : plugin.getOneVsOnePlayersCopy().values()) {
						if(p == null) continue;
						if(playerLocs.containsKey(p.getPlayer().getUniqueId())) 
							if(playerLocs.get(p.getPlayer().getUniqueId()).getWorld().getUID().equals(p.getPlayer().getWorld().getUID()))
							if(playerLocs.get(p.getPlayer().getUniqueId()).distance(p.getPlayer().getLocation()) > 0) {
								new BukkitRunnable() {
									
									@Override
									public void run() {
										
										onMove(p.getPlayer());
										
										
									}
								}.runTask(plugin);
							}
								
					}
					playerLocs.clear();
					
					for(OneVsOnePlayer p : plugin.getOneVsOnePlayersCopy().values()) 
						playerLocs.put(p.getPlayer().getUniqueId(), p.getPlayer().getLocation());
					
					
					
				}
			}.runTaskTimerAsynchronously(plugin, 0,0);
	}
	
	@SuppressWarnings("deprecation")
	public void onMove(Player p) {
		
		if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena) {
		 if(plugin.getOneVsOnePlayer(p).getArena() == null) return;
		 if(plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(), "Started") == null || plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(),"Started").equalsIgnoreCase("false")) {
		  if(plugin.getOneVsOnePlayer(p).getPosition() == 1) {
		   if(p != null && !p.getLocation().getWorld().getName().equalsIgnoreCase(plugin.getPositions().getArenaPos1(plugin.getOneVsOnePlayer(p).getArena()).getWorld().getName())) {
			p.teleport(plugin.getPositions().getArenaPos1(plugin.getOneVsOnePlayer(p).getArena()));
			return;   
		   }
		   if(p != null && p.getLocation().distance(plugin.getPositions().getArenaPos1(plugin.getOneVsOnePlayer(p).getArena())) > 1) {
			 if(plugin.getPositions().getArenaPos1(plugin.getOneVsOnePlayer(p).getArena()).add(0, -1, 0).getBlock().getType() == Material.AIR) {
				plugin.getPositions().getArenaPos1(plugin.getOneVsOnePlayer(p).getArena()).add(0, -1, 0).getBlock().setType(Material.QUARTZ_BLOCK);
			 }
			 p.teleport(plugin.getPositions().getArenaPos1(plugin.getOneVsOnePlayer(p).getArena()));
			}
		   } else {
			if(plugin.getOneVsOnePlayer(p).getPosition() == 0) return;
			if(p != null && !p.getLocation().getWorld().getName().equalsIgnoreCase(plugin.getPositions().getArenaPos2(plugin.getOneVsOnePlayer(p).getArena()).getWorld().getName())) {
			 p.teleport(plugin.getPositions().getArenaPos2(plugin.getOneVsOnePlayer(p).getArena()));
			 return;   
			}
			if(p != null && p.getLocation().distance(plugin.getPositions().getArenaPos2(plugin.getOneVsOnePlayer(p).getArena())) > 1) {
			 if(plugin.getPositions().getArenaPos2(plugin.getOneVsOnePlayer(p).getArena()).add(0, -1, 0).getBlock().getType() == Material.AIR) {
			  plugin.getPositions().getArenaPos2(plugin.getOneVsOnePlayer(p).getArena()).add(0, -1, 0).getBlock().setType(Material.QUARTZ_BLOCK);
			 }
			 p.teleport(plugin.getPositions().getArenaPos2(plugin.getOneVsOnePlayer(p).getArena()));
			}
		   }
		  } else {
		   if(plugin.getOneVsOnePlayer(p).isDoubleJumpUsed()) {
			if(p.isOnGround()) {
			 
				plugin.getOneVsOnePlayer(p).setDoubleJumpUsed(false);
			  p.setAllowFlight(true);
			   
			}
		   }
		   
		   if(plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(), "Pref."+ PlayerPrefs.WaterDamage) != null 
			  && plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(),"Pref." + PlayerPrefs.WaterDamage).equalsIgnoreCase("true")) {
			  if(!plugin.getAState().isEnded(plugin.getOneVsOnePlayer(p).getArena())) {
				  if(p.getLocation().getBlock().getType() == Material.WATER ||
						  p.getLocation().getBlock().getType() == Material.STATIONARY_WATER) 
						   p.damage(1.5);
			  }
			   
			   
			   
			   
		  }
		   
		   
		  }
		 }
	}
	// ---------------------

	
	@EventHandler
	public void onTeamHit(EntityDamageByEntityEvent e) {
		
		if(e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
		 Player hitter = (Player) e.getDamager();
		 Player hittet = (Player) e.getEntity();
		 if(plugin.isInOneVsOnePlayers(hitter.getUniqueId()) && plugin.isInOneVsOnePlayers(hittet.getUniqueId())) {
		  if(plugin.getOneVsOnePlayer(hittet).getpState() == PlayerState.InArena && 
			 plugin.getOneVsOnePlayer(hitter).getpState() == PlayerState.InArena) {
			  
			  String ArenaHitter = plugin.getOneVsOnePlayer(hitter).getArena();
			  String ArenaHittet = plugin.getOneVsOnePlayer(hittet).getArena();
			  
			  if(plugin.getAState().checkState(ArenaHitter, "Pref."+ PlayerPrefs.NoFriendlyFire) != null 
				 && plugin.getAState().checkState(ArenaHitter,"Pref." + PlayerPrefs.NoFriendlyFire).equalsIgnoreCase("true")) {
				  return;
			  }
			  
			  if(ArenaHitter.equalsIgnoreCase(ArenaHittet)) {
				if(plugin.arenaTeams.containsKey(ArenaHittet)) {
					ArenaTeamPlayer teamPlayer = plugin.arenaTeams.get(ArenaHitter).get(0);
					
					if(teamPlayer.getAll().contains(hitter) && teamPlayer.getAll().contains(hittet)) {
						e.setCancelled(true);
						e.setDamage(0);
						return;
					}
					teamPlayer = plugin.arenaTeams.get(ArenaHitter).get(1);
					if(teamPlayer.getAll().contains(hitter) && teamPlayer.getAll().contains(hittet)) {
						e.setCancelled(true);
						e.setDamage(0);
						return;
					}
				}
			  }
		  }
		    
		 }
		}
		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			 Player p = (Player) e.getEntity();
				
			 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena) {
			  if(plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(), "Pref." + PlayerPrefs.NoHitDelay) != null
			   && plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(),"Pref." + PlayerPrefs.NoHitDelay).equalsIgnoreCase("true")) {
				  p.setNoDamageTicks(0);
				  p.setMaximumNoDamageTicks(0);
			   } else {
				   p.setMaximumNoDamageTicks(20);
			   }
			  }
			}
		if(e.getDamager() instanceof Player) {
			  Player p = (Player) e.getDamager();
			  if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena) {
		       if(!plugin.getAState().isStarted(plugin.getOneVsOnePlayer(p).getArena()) || 
		    		   plugin.getAState().isEnded(plugin.getOneVsOnePlayer(p).getArena())) {
				e.setCancelled(true);
			   }
			  }
			 }
	}
	
	
	@EventHandler
	public void onEatApple(final PlayerItemConsumeEvent e) {
		Player p = e.getPlayer();
		if (plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena) {
			if (!plugin.getAState().isStarted(plugin.getOneVsOnePlayer(p).getArena())) e.setCancelled(true);
				
				
				if(e.getItem().getType() == Material.GOLDEN_APPLE) {
				 if(e.getItem().getDurability() == 0 && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName()) {
				  if(e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("goldenHeadName")))) {
					  Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
							
							@Override
							public void run() {
								e.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
								e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 9*20, 1));
								
							}
						}, 1);			
				  }
				 }
		 }
		}
	}
	
	// Interact Event: Hier werden alle Interaktionen, die vor dem Start gemacht
	// werden blockiert.
	@EventHandler
	public void onInterAct(PlayerInteractEvent e) {
		if(plugin.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.InArena) {
			Player p = e.getPlayer();
			if (!plugin.getAState().isStarted(plugin.getOneVsOnePlayer(p).getArena()))  e.setCancelled(true);

			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			 if(e.getClickedBlock().getType() == Material.ENDER_CHEST) e.setCancelled(true);
			}
			
			
			if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			 String Arena = plugin.getOneVsOnePlayer(p).getArena();
					
			 if(plugin.ArenaCorner1.containsKey(Arena) && plugin.ArenaCorner2.containsKey(Arena)) {
					
			  minX = Math.min(plugin.ArenaCorner1.get(Arena).getBlockX(), plugin.ArenaCorner2.get(Arena).getBlockX());
			  minY = Math.min(plugin.ArenaCorner1.get(Arena).getBlockY(), plugin.ArenaCorner2.get(Arena).getBlockY());
			  minZ = Math.min(plugin.ArenaCorner1.get(Arena).getBlockZ(), plugin.ArenaCorner2.get(Arena).getBlockZ());

			  maxX = Math.max(plugin.ArenaCorner1.get(Arena).getBlockX(), plugin.ArenaCorner2.get(Arena).getBlockX());
			  maxY = Math.max(plugin.ArenaCorner1.get(Arena).getBlockY(), plugin.ArenaCorner2.get(Arena).getBlockY());
			  maxZ = Math.max(plugin.ArenaCorner1.get(Arena).getBlockZ(), plugin.ArenaCorner2.get(Arena).getBlockZ());

			  Location Min = new Location(Bukkit.getWorld(plugin.ArenaCorner1.get(Arena).getWorld().getName()), minX, minY,minZ);
			  Location Max = new Location(Bukkit.getWorld(plugin.ArenaCorner1.get(Arena).getWorld().getName()), maxX, maxY,maxZ);
					
			  if(!checkRegion(e.getClickedBlock().getLocation(), Min, Max) || !checkRegion(e.getPlayer().getLocation(), Min, Max)) {
			   e.setCancelled(true);
					 
			  }
			 }
			}
			  

		if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena) {
		 if(plugin.getAState().isStarted(plugin.getOneVsOnePlayer(p).getArena())) {
		  if(plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(), "Pref."+ PlayerPrefs.SoupReg) != null 
			 && plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(),"Pref." + PlayerPrefs.SoupReg).equalsIgnoreCase("true")) {
           if(e.getItem() != null && e.getItem().getType() != null && e.getItem().getType() == Material.MUSHROOM_SOUP) {
			if(p.getMaxHealth() > p.getHealth()) {
			 if(p.getMaxHealth() < p.getHealth()+ plugin.Soupheal*2) {
			  p.setHealth(p.getMaxHealth());
			 } else {
			  p.setHealth(p.getHealth() + plugin.Soupheal*2);
			 }
			} else {		
			 p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("noHealNeeded"), p.getDisplayName(), null,null, plugin.getOneVsOnePlayer(p).getArena()));
			 return;
			}
			 p.getItemInHand().setType(Material.BOWL);
			 p.getItemInHand().setAmount(1);
           }
		  }
		 }
		 if(plugin.getAState().isEnded(plugin.getOneVsOnePlayer(p).getArena())) {
		   e.setCancelled(true);
		   return;
		 }
		}
	   }
		
		Player p = (Player) e.getPlayer();
		 if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			 if(e.getClickedBlock().getType() == Material.ANVIL) {
				 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena) {
					  if(plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(), "Pref." + PlayerPrefs.NoCrafting) != null
						 && plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(),"Pref." + PlayerPrefs.NoCrafting).equalsIgnoreCase("true")) {
						  e.setCancelled(true);
					  }
					 }
			 }
		 }
	}

	// -------------------------------------------

	// Bed Enter: Verhindert, dass Spieler in der Arena ins Bett gehen können.
	@EventHandler
	public void Bed(PlayerBedEnterEvent e) {
		Player p = e.getPlayer();
		if(plugin.isInOneVsOnePlayers(p.getUniqueId())) {
		 e.setCancelled(true);
		}
	}

	// -----------------------------------



	// Block Break: Verhindert, dass vor den Start oder auch nach den Start
	// Blöcke innerhalb oder außerhalb der Arena zerstört werden können.
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (plugin.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.InArena) {
			Player p = e.getPlayer();
			
			if(!plugin.getAState().isStarted(plugin.getOneVsOnePlayer(p).getArena())) e.setCancelled(true);
			
			
			if(plugin.getAState().isStarted(plugin.getOneVsOnePlayer(p).getArena())) {
			 minX = Math.min(plugin.ArenaPos1.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockX(),plugin.ArenaPos2.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockX());
			 minY = Math.min(plugin.ArenaPos1.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockY(),plugin.ArenaPos2.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockY());
			 minZ = Math.min(plugin.ArenaPos1.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockZ(),plugin.ArenaPos2.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockZ());

			 maxX = Math.max(plugin.ArenaPos1.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockX(),plugin.ArenaPos2.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockX());
			 maxY = Math.max(plugin.ArenaPos1.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockY(),plugin.ArenaPos2.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockY());
			 maxZ = Math.max(plugin.ArenaPos1.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockZ(),plugin.ArenaPos2.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockZ());

			 Location minLoc = new Location(plugin.ArenaPos1.get(plugin.getOneVsOnePlayer(p).getArena()).getWorld(), minX, minY, minZ);
			 Location maxLoc = new Location(plugin.ArenaPos1.get(plugin.getOneVsOnePlayer(p).getArena()).getWorld(), maxX, maxY, maxZ);

			 if(plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(), "Pref." + PlayerPrefs.BUILD) == null) {
			  e.setCancelled(true);
			  return;
			 } else if (plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(),"Pref." + PlayerPrefs.BUILD) != null
					    && plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(),"Pref." + PlayerPrefs.BUILD).equalsIgnoreCase("false")) {
			   e.setCancelled(true);
			   return;
			 }

			 if (!checkRegion(e.getBlock().getLocation(), minLoc, maxLoc)) {
			  e.setCancelled(true);
			 }

			}
		}
	}
	//------------------------------------------------------------------
	
	//Block Place: Siehe BlockBreak
	@EventHandler
	public void onBreak(BlockPlaceEvent e) {
		if (plugin.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.InArena) {
			Player p = e.getPlayer();
			if (!plugin.getAState().isStarted(plugin.getOneVsOnePlayer(p).getArena())) {
				e.setCancelled(true);
			}
			if (plugin.getAState().isStarted(plugin.getOneVsOnePlayer(p).getArena())) {

			 minX = Math.min(plugin.ArenaPos1.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockX(),plugin.ArenaPos2.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockX());
			 minY = Math.min(plugin.ArenaPos1.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockY(),plugin.ArenaPos2.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockY());
			 minZ = Math.min(plugin.ArenaPos1.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockZ(),plugin.ArenaPos2.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockZ());
			 	
			 maxX = Math.max(plugin.ArenaPos1.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockX(),plugin.ArenaPos2.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockX());
			 maxY = Math.max(plugin.ArenaPos1.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockY(),plugin.ArenaPos2.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockY());
			 maxZ = Math.max(plugin.ArenaPos1.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockZ(),plugin.ArenaPos2.get(plugin.getOneVsOnePlayer(p).getArena()).getBlockZ());

			 Location minLoc = new Location(plugin.ArenaPos1.get(plugin.getOneVsOnePlayer(p).getArena()).getWorld(), minX, minY, minZ);
			 Location maxLoc = new Location(plugin.ArenaPos1.get(plugin.getOneVsOnePlayer(p).getArena()).getWorld(), maxX, maxY, maxZ);

			 if(plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(), "Pref." + PlayerPrefs.BUILD) == null) {
			  e.setCancelled(true);
			  return;
			 } else if (plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(),"Pref." + PlayerPrefs.BUILD) != null 
					    && plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(),"Pref." + PlayerPrefs.BUILD).equalsIgnoreCase("false")) {
			  e.setCancelled(true);
			  return;
			 }

			 if (!checkRegion(e.getBlock().getLocation(), minLoc, maxLoc)) {
			  e.setCancelled(true);
			  return;
			 }
				
			 if(e.getBlock().getType() == Material.TNT) {
				 
				 if(plugin.maxTNTArenaGame > 0) {
					 if(plugin.tntArena.containsKey(plugin.getOneVsOnePlayer(p).getArena()) &&
								plugin.tntArena.get(plugin.getOneVsOnePlayer(p).getArena()) > plugin.maxTNTArenaGame) {
								 e.setCancelled(true);
								 p.sendMessage(plugin.msgs.getMsg("cantUseMoreTNT").replaceAll("%Amount%", "" + plugin.maxTNTArenaGame));
								 return;
							 }
							 
							 if(plugin.tntArena.containsKey(plugin.getOneVsOnePlayer(p).getArena())) {
								 int save =  plugin.tntArena.get(plugin.getOneVsOnePlayer(p).getArena());
								 while(plugin.tntArena.containsKey(plugin.getOneVsOnePlayer(p).getArena())) plugin.tntArena.remove(plugin.getOneVsOnePlayer(p).getArena());
								 save++;
								 plugin.tntArena.put(plugin.getOneVsOnePlayer(p).getArena(), save);
							 } else {
								 plugin.tntArena.put(plugin.getOneVsOnePlayer(p).getArena(), 1);
							 }
							 
				 }
				 
				 
				if(plugin.maxTNTArenaGame <= 0) {
					p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("tntNotAllowed")));
					e.setCancelled(true);
					return;
				}
				 
			  if(plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(),"Pref." + PlayerPrefs.InstantTnT) != null
			   && plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(),"Pref." + PlayerPrefs.InstantTnT).equalsIgnoreCase("true")) {
			   TNTPrimed tnt = (TNTPrimed) e.getBlock().getWorld().spawnEntity(e.getBlock().getLocation(),EntityType.PRIMED_TNT);
			   tnt.setFuseTicks(20 * 4);
			   e.getBlock().setType(Material.AIR);
			   return;
			  }
			 }
			}
		}
	}
	//---------------------------

	
	
	
	
	//Damage: Verhindert Schaden vor Arena Start und ende
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
		 Player p = (Player) e.getEntity();
		 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena) {
		  if(plugin.getAState().isEnded(plugin.getOneVsOnePlayer(p).getArena())) {
		   e.setCancelled(true);
		   p.setMaxHealth(20);
		   p.setFoodLevel(20);
		   p.setHealth(20);
		   p.setFireTicks(0);
		   e.setDamage(0);
		   e.setCancelled(true);
		   p.setNoDamageTicks(0);
		  }	
		  
		  if(!plugin.getAState().isStarted(plugin.getOneVsOnePlayer(p).getArena())) {
		   e.setCancelled(true);
		   p.setMaxHealth(20);
		   p.setFoodLevel(20);
		   p.setHealth(20);
		   p.setFireTicks(0);
		   e.setDamage(0);
		   e.setCancelled(true);
		  }

		  if(e.getCause() == DamageCause.FALL && !e.isCancelled()) {
		   if(plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(),"Pref." + PlayerPrefs.NoFallDamage) != null 
			  && plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(),"Pref." + PlayerPrefs.NoFallDamage).equalsIgnoreCase("true")) {
			   e.setCancelled(true);
			   e.setDamage(0);
			  }
			 }
			}
		}
	}
	//--------------------------------------------
	
	//FoodChange: Verhindert, dass das FoodLevel in der Arena sich unter umständen sich verändert.
	@EventHandler
	public void onHunger(FoodLevelChangeEvent e) {
		if(e.getEntity() instanceof Player) {
		 Player p = (Player) e.getEntity();
		 
		  if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit || plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby) {
		   e.setCancelled(true);
		   p.setFoodLevel(20);
		   p.setHealth(20);
		  }
				
		 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena) {
		  if(!plugin.getAState().isStarted(plugin.getOneVsOnePlayer(p).getArena())) {
		   e.setCancelled(true);
		   p.setFoodLevel(20);
		   p.setHealth(20);
		  }
		 }
			 
			 
		 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena) {
		  if(plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(), "Pref." + PlayerPrefs.HUNGER) != null 
			 && plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(),"Pref." + PlayerPrefs.HUNGER).equalsIgnoreCase("true")) {
		   e.setCancelled(true);
		   p.setFoodLevel(20);
		  }
		 }
		
	   }

	}
	//----------------------------------------------------------

	//Bukket Empty + Bukket fill: Verhindert, dass ein Eimer ausgeleert/befüllt wird vorm Start 
	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent e) {
		if(plugin.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.InArena) {
		 Player p = e.getPlayer();
		 if(!plugin.getAState().isStarted(plugin.getOneVsOnePlayer(p).getArena())) {
		  e.setCancelled(true);
		 }
		}
	}
	
	@EventHandler
	public void onBucketEmpty(PlayerBucketFillEvent e) {
		if(plugin.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.InArena) {
		 Player p = e.getPlayer();
		 if(!plugin.getAState().isStarted(plugin.getOneVsOnePlayer(p).getArena())) e.setCancelled(true);
		 
		}
	}
	//-----------------------------
	
	
	
	
	//PickupItem: Verhindert, dass man Items vor den Start aufheben kann.
	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
	 if(plugin.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.InArena) {
	  Player p = e.getPlayer();
	  
	  if(!plugin.getAState().isStarted(plugin.getOneVsOnePlayer(p).getArena())) e.setCancelled(true);
	  
	  
	 }
	}
	//------------------------------

	//ProjectileHitEvent: Verhindert ggf., dass Projektile auf den Boden aufkommen
	@EventHandler
    public void onArrow(ProjectileHitEvent e) {
	 if(e.getEntity() instanceof Arrow) {
		 Arrow arrow = (Arrow) e.getEntity();
		 if(arrow.getShooter() instanceof Player) {
			 Player p = (Player) e.getEntity().getShooter();
			 if(plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(), "Pref." + PlayerPrefs.NoArrowPickup) != null 
			    && plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(),"Pref." + PlayerPrefs.NoArrowPickup).equalsIgnoreCase("true")) {
				 arrow.remove();
			 }
			 
		 } 
	 }
	}
	//----------------------------------
	
	
	
	//onDropBowl: Verhindert, dass falsche Items gedroppt werden bei Kiteinstellung Soup-Noob
	@EventHandler
	public void onDropBowl(PlayerDropItemEvent e) {
	 Player p = (Player) e.getPlayer();
	 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena) {
	  if(plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(), "Pref." + PlayerPrefs.SoupNoob) != null 
		 && plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(),"Pref." + PlayerPrefs.SoupNoob).equalsIgnoreCase("true")) {
	   if(e.getItemDrop().getItemStack().getType() == Material.WOOD_SWORD 
		  || e.getItemDrop().getItemStack().getType() == Material.STONE_SWORD 
		  || e.getItemDrop().getItemStack().getType() == Material.GOLD_SWORD 
		  || e.getItemDrop().getItemStack().getType() == Material.IRON_SWORD 
		  || e.getItemDrop().getItemStack().getType() == Material.DIAMOND_SWORD 
		  || e.getItemDrop().getItemStack().getType() == Material.MUSHROOM_SOUP) {
		 e.setCancelled(true);
	   }
	  }
	  
	 }
	 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena) { 
	  if(!plugin.getAState().isStarted(plugin.getOneVsOnePlayer(p).getArena())) e.setCancelled(true);
	 }
	}
	//---------------------------------------
	
	//Explode Event: Verhindert ggf. Explosionschaden
	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		YamlConfiguration cfg = plugin.getYaml("Arenen");

		if (cfg.getConfigurationSection("Arenen") == null) {
			return;
		}
		
		
		if(!plugin.protectedWordls.isEmpty() && !plugin.protectedWordls.contains(e.getLocation().getWorld().getName())) {
			return;
		}

		String Arena = getArena(e.getEntity().getLocation());
			 if(plugin.getAState().isStarted(Arena) || plugin.getAState().isEnded(Arena)) {
				 
			  if(plugin.ArenaPos1.containsKey(Arena) && plugin.ArenaPos2.containsKey(Arena)) {
			   minX = Math.min(plugin.ArenaPos1.get(Arena).getBlockX(), plugin.ArenaPos2.get(Arena).getBlockX());
			   minY = Math.min(plugin.ArenaPos1.get(Arena).getBlockY(), plugin.ArenaPos2.get(Arena).getBlockY());
			   minZ = Math.min(plugin.ArenaPos1.get(Arena).getBlockZ(), plugin.ArenaPos2.get(Arena).getBlockZ());

			   maxX = Math.max(plugin.ArenaPos1.get(Arena).getBlockX(), plugin.ArenaPos2.get(Arena).getBlockX());
			   maxY = Math.max(plugin.ArenaPos1.get(Arena).getBlockY(), plugin.ArenaPos2.get(Arena).getBlockY());
			   maxZ = Math.max(plugin.ArenaPos1.get(Arena).getBlockZ(), plugin.ArenaPos2.get(Arena).getBlockZ());

			   Location Min = new Location(Bukkit.getWorld(plugin.ArenaPos1.get(Arena).getWorld().getName()), minX, minY,minZ);
			   Location Max = new Location(Bukkit.getWorld(plugin.ArenaPos1.get(Arena).getWorld().getName()), maxX, maxY,maxZ);

			   boolean Nodamage = false;

			   if(plugin.getAState().checkState(Arena, "Pref." + PlayerPrefs.NoTnTDamage) != null && 
					   plugin.getAState().checkState(Arena,"Pref." + PlayerPrefs.NoTnTDamage).equalsIgnoreCase("true")) {
				Nodamage = true;
			   }
						
			   if(plugin.getAState().isEnded(Arena)) Nodamage = true;
			   
			   			
			   ArrayList<Block> Blocks = new ArrayList<Block>();
			   for(Block b : e.blockList()) {
				Blocks.add(b);
			   }
			    
			   for(Block b : Blocks) {
				if(!checkRegion(b.getLocation(), Min, Max)) {
				 e.blockList().remove(b);
				} else {
				 if(Nodamage) {
				  e.blockList().remove(b);
				 }
				}
			   }
			  }
			 
			}
		}
	//---------------------------
	
	//checkRegion(Check, Min, Max): Prüft ob Location Check zwischen Min und Max liegt [Min und Max müssen richtig angegeben sein!]
	public static boolean checkRegion(Location Check, Location Min, Location Max) {

		if (Min == null || Max == null) { return false; }

		if (Min.getBlockY() > Check.getBlockY() || Max.getBlockY() < Check.getBlockY()) { return false; }

		if (!Check.getWorld().getName().equalsIgnoreCase(Min.getWorld().getName())) { return false; }

		int hx = Check.getBlockX();
		int hz = Check.getBlockZ();
		
		if (hx < Min.getBlockX()) return false;

		if (hx > Max.getBlockX()) return false;

		if (hz < Min.getBlockZ()) return false;

		if (hz > Max.getBlockZ()) return false;

		return true;

	}
	//--------------------------------------------------
	
	
	//PistionExtend/Retract: Verhindert, dass Pistons die Wände der Arena bewegen können
	@EventHandler
	public void onExtend(BlockPistonExtendEvent e) {
	 if(!plugin.protectedWordls.isEmpty() && !plugin.protectedWordls.contains(e.getBlock().getWorld().getName())) {
	  return;
	 }
		
	 YamlConfiguration cfg = plugin.getYaml("Arenen");
		
	 if(cfg.getConfigurationSection("Arenen") == null) {
	  return;
	 }

	 for(String Arenen : cfg.getConfigurationSection("Arenen").getKeys(false)) {
	  if(cfg.getBoolean("Arenen." + Arenen)) {
	   if(plugin.getAState().isStarted(Arenen)) {
	    if(plugin.ArenaPos1.containsKey(Arenen) && plugin.ArenaPos2.containsKey(Arenen)) {
	   	 minX = Math.min(plugin.ArenaPos1.get(Arenen).getBlockX(), plugin.ArenaPos2.get(Arenen).getBlockX());
		 minY = Math.min(plugin.ArenaPos1.get(Arenen).getBlockY(), plugin.ArenaPos2.get(Arenen).getBlockY());
		 minZ = Math.min(plugin.ArenaPos1.get(Arenen).getBlockZ(), plugin.ArenaPos2.get(Arenen).getBlockZ());
		 
		 maxX = Math.max(plugin.ArenaPos1.get(Arenen).getBlockX(), plugin.ArenaPos2.get(Arenen).getBlockX());
		 maxY = Math.max(plugin.ArenaPos1.get(Arenen).getBlockY(), plugin.ArenaPos2.get(Arenen).getBlockY());
		 maxZ = Math.max(plugin.ArenaPos1.get(Arenen).getBlockZ(), plugin.ArenaPos2.get(Arenen).getBlockZ());

		 Location Min = new Location(Bukkit.getWorld(plugin.ArenaPos1.get(Arenen).getWorld().getName()), minX, minY,minZ);
		 Location Max = new Location(Bukkit.getWorld(plugin.ArenaPos1.get(Arenen).getWorld().getName()), maxX, maxY,maxZ);

		 ArrayList<Block> Blocks = new ArrayList<Block>();
						
		 for(Block b : e.getBlocks()) Blocks.add(b);
		 

		 for(Block b : Blocks) {
		  if(!checkRegion(b.getLocation(), Min, Max)) {
		   e.setCancelled(true);
		  }
		 }
		}
	   }
	  }
     }
	}

	@EventHandler
	public void onRetract(BlockPistonRetractEvent e) {
		if(!plugin.protectedWordls.isEmpty() && !plugin.protectedWordls.contains(e.getBlock().getWorld().getName())) {
		 return;
		}
		
		YamlConfiguration cfg = plugin.getYaml("Arenen");
		
		if (cfg.getConfigurationSection("Arenen") == null) {
		 return;
		}

		for (String Arenen : cfg.getConfigurationSection("Arenen").getKeys(false)) {
		 if (cfg.getBoolean("Arenen." + Arenen)) {
		  if (plugin.getAState().isStarted(Arenen)) {
           if (plugin.ArenaPos1.containsKey(Arenen) && plugin.ArenaPos2.containsKey(Arenen)) {
			minX = Math.min(plugin.ArenaPos1.get(Arenen).getBlockX(), plugin.ArenaPos2.get(Arenen).getBlockX());
			minY = Math.min(plugin.ArenaPos1.get(Arenen).getBlockY(), plugin.ArenaPos2.get(Arenen).getBlockY());
			minZ = Math.min(plugin.ArenaPos1.get(Arenen).getBlockZ(), plugin.ArenaPos2.get(Arenen).getBlockZ());

			maxX = Math.max(plugin.ArenaPos1.get(Arenen).getBlockX(), plugin.ArenaPos2.get(Arenen).getBlockX());
			maxY = Math.max(plugin.ArenaPos1.get(Arenen).getBlockY(), plugin.ArenaPos2.get(Arenen).getBlockY());
			maxZ = Math.max(plugin.ArenaPos1.get(Arenen).getBlockZ(), plugin.ArenaPos2.get(Arenen).getBlockZ());

			Location Min = new Location(Bukkit.getWorld(plugin.ArenaPos1.get(Arenen).getWorld().getName()), minX, minY,minZ);
			Location Max = new Location(Bukkit.getWorld(plugin.ArenaPos1.get(Arenen).getWorld().getName()), maxX, maxY,maxZ);

			ArrayList<Block> Blocks = new ArrayList<Block>();
			for(Block b : e.getBlocks()) {
			 Blocks.add(b);
			}
						
			for(Block b : Blocks) {
			 if(!checkRegion(b.getLocation(), Min, Max)) {
			  e.setCancelled(true);
			 }
			}
		   }
		  }
		 }
		}
	}
	
	//------------------------------------------------------------------

	//EntityChangeBlock: Verhindert, dass z.B. Wither Blöcke zerstören können.
	@EventHandler
	public void onChangeEntity(EntityChangeBlockEvent e) {
		if(plugin.protectedWordls.contains(e.getBlock().getWorld().getName())) {
		 e.setCancelled(true);
		}
	}
	//-------------------------------------------
	
	
	//EntitySpawn: Verhindert, dass mehr als angegebende Entitys spawnen können.
	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent e) {
		Location loc = e.getEntity().getLocation();
		
	    if(isInArena(loc)) if(!plugin.getAState().isStarted(getArena(loc))) e.setCancelled(true);
		
		if(isInArena(loc)) {
		 String Arena = getArena(loc);
		 if(plugin.getAState().isStarted(getArena(loc))) {
		  if(plugin.EntityCount.containsKey(Arena) && plugin.EntityCount.get(Arena) > plugin.maxArenaEntitys) {
		   if(e.getEntity() instanceof Item) {
			return;
		   }
			
		   e.setCancelled(true);
		 	
		   } else {
			if(!plugin.EntityCount.containsKey(Arena) && plugin.maxArenaEntitys <= 0) {
			 if(e.getEntity() instanceof Item) {
			  return;
			 }
			 e.setCancelled(true);
			 return;
			}
					
			ArrayList<Entity> allEntitys = new ArrayList<Entity>();
			if (plugin.Entitys.containsKey(Arena)) {
			 allEntitys = plugin.Entitys.get(Arena);
			}

			allEntitys.add(e.getEntity());
			plugin.Entitys.remove(Arena);
			plugin.Entitys.put(Arena, allEntitys);

			if (e.getEntity() instanceof Item) {
			 return;
			}

			int all = 0;
			 
			if (plugin.EntityCount.containsKey(Arena)) {
			 all = plugin.EntityCount.get(Arena);
			}
			 
			all++;
			 
			plugin.EntityCount.remove(Arena);
			plugin.EntityCount.put(Arena, all);

		  }
		 }
		}
		
		
	}
	//------------------------------------
	
	//EntityDeath: Wenn eine Entity stirbt kann man eine weiter in der Arena spawnen
	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		Location loc = e.getEntity().getLocation();
		
		if(isInArena(loc)) {
		 String Arena = getArena(loc);
		 if(plugin.getAState().isStarted(getArena(loc))) {
		  if(plugin.getAState().isEnded(getArena(loc))) {
		   if(plugin.Entitys.containsKey(Arena)) {
			
			int all = 0;
			if(plugin.EntityCount.containsKey(Arena)) {
			 all = plugin.EntityCount.get(Arena);
			}
			
			all--;
			
			plugin.EntityCount.remove(Arena);
			plugin.EntityCount.put(Arena, all);

			e.setDroppedExp(0);
		   }
		  }	   
		 } else {
		  if(plugin.Entitys.containsKey(Arena)) {
		   int all = 0;
		   if(plugin.EntityCount.containsKey(Arena)) {
		    all = plugin.EntityCount.get(Arena);
		   }
			   
		   all--;
		   plugin.EntityCount.remove(Arena);
		   plugin.EntityCount.put(Arena, all);

		   e.setDroppedExp(0);
		  }
		 }
		}

	}
	//-------------------------------------------------------
	
	//PlayerQuit: Managed, wenn ein Spieler den Server verlässt
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		
		TeamManager.deleteTeam(p);
		TeamManager.removePlayerTeam(p);
		
		
		plugin.getOneVsOnePlayer(p).setArenaView(null);
		
		if(plugin.isInOneVsOnePlayers(p.getUniqueId())) {
			if(plugin.BestOfSystem.containsKey(p.getUniqueId())) 
				 plugin.BestOfSystem.remove(p.getUniqueId());
			 
			
			if(plugin.getOneVsOnePlayer(p).getEnemy() != null)
			 plugin.BestOfSystem.remove(plugin.getOneVsOnePlayer(p).getEnemy().getUniqueId());
			 
		}
		
			
		if (plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena) {
		 KillEvent.simulateDeath(e.getPlayer(), false);
		 
		 plugin.getTeleporter().teleportExit(p);
		 FightEnd.resetPlayer(p, true);
		}
		if (plugin.isInOneVsOnePlayers(p.getUniqueId())) {
		 TournamentManager mgr = plugin.tournaments.get(p.getUniqueId());
		 if(mgr != null) mgr.delete();
		 
		 while(plugin.tournaments.containsKey(p.getUniqueId())) plugin.tournaments.remove(p.getUniqueId());
		 p.setNoDamageTicks(20 * 3);
		 plugin.getOneVsOnePlayer(p).setWasInQueue(false);
		 ChallangeManager.removePlayerComplete(p);
		 p.setNoDamageTicks(20 * 3);
		 p.setHealth(20);
		 p.setFoodLevel(20);
		 p.setExp(0);
		 p.setLevel(0);
		 
		 
		

		 p.getInventory().clear();
		 p.getInventory().setArmorContents(null);
		 plugin.getTeleporter().teleportExit(p);
		 p.setAllowFlight(false);
		 p.setFlying(false);
		 p.setMaximumNoDamageTicks(0);
		 
		 if(plugin.saveInvs) {
 			if(plugin.getOneVsOnePlayer(p.getUniqueId()).getPlayerInv() != null) {
     			p.getInventory().setContents(plugin.getOneVsOnePlayer(p.getUniqueId()).getPlayerInv());
     		}
     		if(plugin.getOneVsOnePlayer(p.getUniqueId()).getPlayerArmor() != null) {
     			p.getInventory().setArmorContents(plugin.getOneVsOnePlayer(p.getUniqueId()).getPlayerArmor());
     		}
 		}
		 while(plugin.isInOneVsOnePlayers(p.getUniqueId())) plugin.removePlayer(p.getUniqueId());
		 
	  }

	}
	//----------------------------------------------

	//PlayerJoin: Managed wenn der Bungeecordmode aktiv ist das der Spieler automatisch in 1vs1 Modus ist.
	@EventHandler
	public void onJoin(final PlayerJoinEvent e) {
		if(plugin.BungeeMode) {
		 Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {

		  @Override
		  public void run() {
		   
		   MainCommand.toggle1vs1(e.getPlayer(), true, false);
		  }
		 }, 10);
		}
	}
	//----------------------------------------

	//CraftItem: Verhindert, dass man Craften kann wenn die Kit-Einstellung dies nicht zulässt
	@EventHandler
	public void onCraft(CraftItemEvent e) {
		if(e.getWhoClicked() instanceof Player) {
		 Player p = (Player) e.getWhoClicked();
		 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena) {
		  if(plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(), "Pref." + PlayerPrefs.NoCrafting) != null
			 && plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(),"Pref." + PlayerPrefs.NoCrafting).equalsIgnoreCase("true")) {
			  e.setCancelled(true);
		  }
		 }
		}
	}
	

	//Verhindert, dass Dispenser sachen "Dispensen" können
	@EventHandler
	public void onDispense(BlockDispenseEvent e) {
		
		Location loc = e.getBlock().getLocation();
		
		if(isInArena(loc)) {
		 if(plugin.getAState().isStarted(getArena(loc))) {
		  if(plugin.getAState().isEnded(getArena(loc))) {
		   e.setCancelled(true);
		   if(e.getBlock().getType() == Material.DISPENSER) {
				Dispenser dispenser = (Dispenser) e.getBlock().getState();
				dispenser.getInventory().clear();
				e.getBlock().setType(Material.AIR);
			} else if(e.getBlock().getType() == Material.DROPPER) {
				Dropper dropper = (Dropper) e.getBlock().getState();
				dropper.getInventory().clear();
				e.getBlock().setType(Material.AIR);
			}
		  }	   
		 } else {
		  e.setCancelled(true);
		  if(e.getBlock().getType() == Material.DISPENSER) {
				Dispenser dispenser = (Dispenser) e.getBlock().getState();
				dispenser.getInventory().clear();
				e.getBlock().setType(Material.AIR);
		  } else if(e.getBlock().getType() == Material.DROPPER) {
				Dropper dropper = (Dropper) e.getBlock().getState();
				dropper.getInventory().clear();
				e.getBlock().setType(Material.AIR);
		  }
		 }
		}	
	}
	//---------------------------------------------
	
	
	//Verhindert, dass Redstone nach dem Arena-Ende funktioniert
//	//@EventHandler
//	public void onRedstone(BlockRedstoneEvent e) {
//		Location loc = e.getBlock().getLocation();
//		
//		if(isInArena(loc)) {
//			ArenaState state = new ArenaState(plugin);
//		 if(state.isStarted(getArena(loc))) {
//		  if(state.isEnded(getArena(loc))) {
//		   e.setNewCurrent(0);
//		  }	   
//		 } else {
//		  e.setNewCurrent(0);
//		  
//		 }
//		}
//	}
	//-------------------------------------------------------------
	
	
	//Verhindert, dass ein Spawner unter umständen was Spawnt
	@EventHandler
	public void onSpawnerSpawn(SpawnerSpawnEvent e) {
		Location loc = e.getSpawner().getLocation();
		
		if(isInArena(loc)) {
		 if(plugin.getAState().isStarted(getArena(loc))) {
		  if(plugin.getAState().isEnded(getArena(loc))) {
		   e.setCancelled(true);
		  }	   
		 } else {
		  e.setCancelled(true);
		 }
		}
	}
	//-------------------------------------------------------------
	
	//Verhindert das Anzünden eines Blockes
	@EventHandler
	public void onIgnite(BlockIgniteEvent e) {
		Location loc = e.getBlock().getLocation();
		
		if(isInArena(loc)) {
		 if(plugin.getAState().isStarted(getArena(loc))) {
		  if(plugin.getAState().isEnded(getArena(loc))) {
		   e.setCancelled(true);
		  }	   
		 } else {
		  e.setCancelled(true);
		 }
		}
	}
	//-------------------------------------------------------------
	
	//Verhindert, dass Explosionen kommen
	@EventHandler
	public void onPrime(ExplosionPrimeEvent e) {

		Location loc = e.getEntity().getLocation();
		
		if(isInArena(loc)) {
		 if(plugin.getAState().isStarted(getArena(loc))) {
		  if(plugin.getAState().isEnded(getArena(loc))) {
		   e.setCancelled(true);
		   e.getEntity().remove();
		  }	   
		 } else {
		  e.setCancelled(true);
		  e.getEntity().remove();
		 }
		}
	}
	//-------------------------------------------------------------
	
	
	
	
	//NoRegneration Kit-Einstellung
	@EventHandler
	public void onReg(EntityRegainHealthEvent e) {	
	 if(e.getRegainReason() == RegainReason.SATIATED) {
      if(e.getEntity() instanceof Player) {
	   Player p = (Player) e.getEntity();
	   if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena) {
		if(plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(), "Pref." + PlayerPrefs.NoRegneration) != null
		 && plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(),"Pref." + PlayerPrefs.NoRegneration).equalsIgnoreCase("true")) {
		 e.setCancelled(true);
		 e.setAmount(0);
		}
	   }
	  }
	 }
	}
	//-------------------------------------------------------------
	
	//DoubleJump Einstellung
	@EventHandler
	public void onFly(PlayerToggleFlightEvent e) {	
	 Player p = e.getPlayer();
	
	 if(e.isFlying()) {
	  if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InArena) {
	   if(plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(), "Pref." + PlayerPrefs.DoubleJump) != null
		&& plugin.getAState().checkState(plugin.getOneVsOnePlayer(p).getArena(),"Pref." + PlayerPrefs.DoubleJump).equalsIgnoreCase("true")) {
		e.setCancelled(true);
		plugin.getOneVsOnePlayer(e.getPlayer()).setDoubleJumpUsed(true);
		p.setAllowFlight(false);
		p.setFlying(false);
		p.setVelocity(p.getLocation().getDirection().multiply(1.3D).setY(0.9D));
		SoundManager manager = new SoundManager(JSound.FIREWORK, p, 20.0F, 1.0F);
		manager.play();
	   }
	  }
	 }
	}
	//-------------------------------------------------------------
	
	//Verhindert Enderman teleport
	@EventHandler
	public void onTp(EntityTeleportEvent e) {	
	 if(isInArena(e.getFrom())) {
	  if(e.getEntity() instanceof Enderman) {
	   if(!isInArena(e.getTo())) {
		e.setCancelled(true);
	   }
	  }
	 }
	}
	//------------------------------
	
	
	  
	
	/**
	 Checks, if a Location is in a Arena
	 @param Location
	 @return Location is in a Arena
	*/
	public boolean isInArena(Location loc) {
		YamlConfiguration cfg = plugin.getYaml("Arenen");

		if (cfg.getConfigurationSection("Arenen") == null) return false;

		for (String Arenen : cfg.getConfigurationSection("Arenen").getKeys(false)) {
			if (cfg.getBoolean("Arenen." + Arenen)) {
				

					if (plugin.ArenaPos1.containsKey(Arenen) && plugin.ArenaPos2.containsKey(Arenen)) {
						minX = Math.min(plugin.ArenaPos1.get(Arenen).getBlockX(), plugin.ArenaPos2.get(Arenen).getBlockX());
						minY = Math.min(plugin.ArenaPos1.get(Arenen).getBlockY(), plugin.ArenaPos2.get(Arenen).getBlockY());
						minZ = Math.min(plugin.ArenaPos1.get(Arenen).getBlockZ(), plugin.ArenaPos2.get(Arenen).getBlockZ());

						maxX = Math.max(plugin.ArenaPos1.get(Arenen).getBlockX(), plugin.ArenaPos2.get(Arenen).getBlockX());
						maxY = Math.max(plugin.ArenaPos1.get(Arenen).getBlockY(), plugin.ArenaPos2.get(Arenen).getBlockY());
						maxZ = Math.max(plugin.ArenaPos1.get(Arenen).getBlockZ(), plugin.ArenaPos2.get(Arenen).getBlockZ());

						
						if(!plugin.ArenaPos1.containsKey(Arenen) || !plugin.ArenaPos2.containsKey(Arenen)) {
							return false;
						}
						if(plugin.ArenaPos1.get(Arenen).getWorld() == null || plugin.ArenaPos2.get(Arenen).getWorld() == null) {
							return false;
						}
						Location Min = new Location(Bukkit.getWorld(plugin.ArenaPos1.get(Arenen).getWorld().getName()), minX, minY,minZ);
						Location Max = new Location(Bukkit.getWorld(plugin.ArenaPos1.get(Arenen).getWorld().getName()), maxX, maxY,maxZ);
						if (checkRegion(loc, Min, Max)) return true;
					}

				
			}
		}
		return false;
	}
	//------------------------------------------
	
	/**
	Get the Arena by a Location
	@param Location
	@return The Arena where the Location is
	*/
    public static String getArena(Location loc) {
		YamlConfiguration cfg = plugin.getYaml("Arenen");

		int maxX,maxY,maxZ;
		int minX,minY,minZ;
		
		if (cfg.getConfigurationSection("Arenen") == null) {
			return null;
		}

		for (String Arenen : cfg.getConfigurationSection("Arenen").getKeys(false)) {
			if (cfg.getBoolean("Arenen." + Arenen)) {
				

					if (plugin.ArenaCorner1.containsKey(Arenen) && plugin.ArenaCorner2.containsKey(Arenen)) {
						
						
						minX = Math.min(plugin.ArenaCorner1.get(Arenen).getBlockX(), plugin.ArenaCorner2.get(Arenen).getBlockX());
						minY = Math.min(plugin.ArenaCorner1.get(Arenen).getBlockY(), plugin.ArenaCorner2.get(Arenen).getBlockY());
						minZ = Math.min(plugin.ArenaCorner1.get(Arenen).getBlockZ(), plugin.ArenaCorner2.get(Arenen).getBlockZ());

						maxX = Math.max(plugin.ArenaCorner1.get(Arenen).getBlockX(), plugin.ArenaCorner2.get(Arenen).getBlockX());
						maxY = Math.max(plugin.ArenaCorner1.get(Arenen).getBlockY(), plugin.ArenaCorner2.get(Arenen).getBlockY());
						maxZ = Math.max(plugin.ArenaCorner1.get(Arenen).getBlockZ(), plugin.ArenaCorner2.get(Arenen).getBlockZ());

						Location Min = new Location(Bukkit.getWorld(plugin.ArenaCorner1.get(Arenen).getWorld().getName()), minX, minY,minZ);
						Location Max = new Location(Bukkit.getWorld(plugin.ArenaCorner1.get(Arenen).getWorld().getName()), maxX, maxY,maxZ);
						if (checkRegion(loc, Min, Max)) {
							
							return Arenen;
						}
					}

				}
			}
		return null;
	}
	
    public boolean isInArenaCorner(Location loc,String Arena) {
		YamlConfiguration cfg = plugin.getYaml("Arenen");


			if (cfg.getBoolean("Arenen." + Arena)) {
				

					if (plugin.ArenaCorner1.containsKey(Arena) && plugin.ArenaCorner2.containsKey(Arena)) {
						minX = Math.min(plugin.ArenaCorner1.get(Arena)
								.getBlockX(), plugin.ArenaCorner2.get(Arena)
								.getBlockX());
						minY = Math.min(plugin.ArenaCorner1.get(Arena)
								.getBlockY(), plugin.ArenaCorner2.get(Arena)
								.getBlockY());
						minZ = Math.min(plugin.ArenaCorner1.get(Arena)
								.getBlockZ(), plugin.ArenaCorner2.get(Arena)
								.getBlockZ());

						maxX = Math.max(plugin.ArenaCorner1.get(Arena)
								.getBlockX(), plugin.ArenaCorner2.get(Arena)
								.getBlockX());
						maxY = Math.max(plugin.ArenaCorner1.get(Arena)
								.getBlockY(), plugin.ArenaCorner2.get(Arena)
								.getBlockY());
						maxZ = Math.max(plugin.ArenaCorner1.get(Arena)
								.getBlockZ(), plugin.ArenaCorner2.get(Arena)
								.getBlockZ());

						Location Min = new Location(
								Bukkit.getWorld(plugin.ArenaCorner1.get(Arena)
										.getWorld().getName()), minX, minY,
								minZ);
						Location Max = new Location(
								Bukkit.getWorld(plugin.ArenaCorner1.get(Arena)
										.getWorld().getName()), maxX, maxY,
								maxZ);
						if (checkRegion(loc, Min, Max)) {
							return true;
						}
					}

				
			}
		
		return false;
	}
    //--------------------------------------
	
    /**
    Checks if a Location is in a Arena you specified
    @param Location and Arena
    @return If the Location is in the Arena
    */
    public boolean isInArena(Location loc,String Arena) {
		YamlConfiguration cfg = plugin.getYaml("Arenen");


			if (cfg.getBoolean("Arenen." + Arena)) {
				

					if (plugin.ArenaPos1.containsKey(Arena) && plugin.ArenaPos2.containsKey(Arena)) {
						minX = Math.min(plugin.ArenaPos1.get(Arena)
								.getBlockX(), plugin.ArenaPos2.get(Arena)
								.getBlockX());
						minY = Math.min(plugin.ArenaPos1.get(Arena)
								.getBlockY(), plugin.ArenaPos2.get(Arena)
								.getBlockY());
						minZ = Math.min(plugin.ArenaPos1.get(Arena)
								.getBlockZ(), plugin.ArenaPos2.get(Arena)
								.getBlockZ());

						maxX = Math.max(plugin.ArenaPos1.get(Arena)
								.getBlockX(), plugin.ArenaPos2.get(Arena)
								.getBlockX());
						maxY = Math.max(plugin.ArenaPos1.get(Arena)
								.getBlockY(), plugin.ArenaPos2.get(Arena)
								.getBlockY());
						maxZ = Math.max(plugin.ArenaPos1.get(Arena)
								.getBlockZ(), plugin.ArenaPos2.get(Arena)
								.getBlockZ());

						Location Min = new Location(
								Bukkit.getWorld(plugin.ArenaPos1.get(Arena)
										.getWorld().getName()), minX, minY,
								minZ);
						Location Max = new Location(
								Bukkit.getWorld(plugin.ArenaPos1.get(Arena)
										.getWorld().getName()), maxX, maxY,
								maxZ);
						if (checkRegion(loc, Min, Max)) {
							return true;
						}
					}

				
			}
		
		return false;
	}
    //------------------------------------------------------------
	
}
