package de.OnevsOne.Listener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.material.Dye;

import de.OnevsOne.main;
import de.OnevsOne.Commands.MainCommand;
import de.OnevsOne.Commands.VariableCommands.Kit;
import de.OnevsOne.Commands.VariableCommands.Manager.CommandTrigger1vs1;
import de.OnevsOne.Kit_Methods.Kit_Editor_Move;

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.States.PlayerState;
import net.md_5.bungee.api.ChatColor;

/**
 * Der Code ist von JHammer
 *
 * 06.05.2016 um 11:52:46 Uhr
 */
public class Blocked_Events implements Listener {

	private main plugin;
	private ItemStack lapis;
	
	public Blocked_Events(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		Dye d = new Dye();
	    d.setColor(DyeColor.BLUE);
	    this.lapis = d.toItemStack();
	    this.lapis.setAmount(3);
	}
	
	
	@EventHandler
	public void onJoinMsg(PlayerJoinEvent e) {
		
		if(plugin.overrideJoinLeaveMsg) {
			e.setJoinMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("serverJoinMsg").replaceAll("%Name%", e.getPlayer().getDisplayName())));
		}
		
		
	}
	
	@EventHandler
	public void onArmorStandInteract(PlayerInteractAtEntityEvent e) {
		
		if(e.getRightClicked() instanceof ArmorStand) {
		 if(plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId()) && 
				 plugin.getOneVsOnePlayer(e.getPlayer()).getpState() != PlayerState.Edit &&
			plugin.getOneVsOnePlayer(e.getPlayer()).getpState() != PlayerState.InArena) {
			 e.setCancelled(true);
		 }
		 if(plugin.kitStands.containsKey(e.getRightClicked().getUniqueId())) {
			 if(plugin.kitStandsKit.containsKey(e.getRightClicked().getUniqueId())) {
			  
			   if(plugin.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.InLobby || plugin.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.InKitEdit) {
					 String kit = plugin.kitStandsKit.get(e.getRightClicked().getUniqueId());
					 String[] args = {kit};
					 CommandTrigger1vs1 trigger = new CommandTrigger1vs1(e.getPlayer(), args, "kit");
				     Bukkit.getServer().getPluginManager().callEvent(trigger);
			   }
			  }

			 
			 e.setCancelled(true);
		 }
		}
	}
	
	@EventHandler
	public void onDamageArmorStand(EntityDamageEvent e) {
		if(e.getEntity() instanceof ArmorStand) {
			if(plugin.kitStands.containsKey(e.getEntity().getUniqueId())) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onLeaveMsg(PlayerQuitEvent e) {
		if(plugin.overrideJoinLeaveMsg) {
			e.setQuitMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("serverLeaveMsg").replaceAll("%Name%", e.getPlayer().getDisplayName())));
		}
		
	}
	
	@EventHandler
	public void onInterAct(PlayerInteractEvent e) {
	 Player p = e.getPlayer();
	 /*Sachen, wenn man nicht im Kit-Editor bereich ist*/
	 if(plugin.isInOneVsOnePlayers(p.getUniqueId())) {
		 if(e.getAction() == Action.RIGHT_CLICK_BLOCK && 
			Kit_Editor_Move.checkRegion(e.getClickedBlock().getLocation(), plugin.KitEditor1, plugin.KitEditor2)) {
				  
					
					
				   if(p.getGameMode() != GameMode.CREATIVE) {
				   if(e.getClickedBlock().getType() == Material.STONE_BUTTON || e.getClickedBlock().getType() == Material.WOOD_BUTTON) {} else {
					   e.setCancelled(true);
				   }
				   
				   
				   return; 
				  }
				 }
	 
	 
	 /*----------------------------*/
		
	 
//	 if(plugin.Players.get(p) == PlayerState.InKitEdit || plugin.Players.get(p) == PlayerState.InLobby) {
//		 if(e.getAction() == Action.RIGHT_CLICK_AIR) {
//			 e.setCancelled(true); 
//		 }
//	 }
	 
     /*Verzauberungstisch im Kit-Editor*/
	 if(plugin.isInOneVsOnePlayers(p.getUniqueId()) && 
			 plugin.getOneVsOnePlayer(p).getpState() != PlayerState.Edit &&
			 plugin.getOneVsOnePlayer(p).getpState() != PlayerState.InArena) {
	  
	  if(e.getAction() == Action.RIGHT_CLICK_BLOCK && plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit) {
	   
	   /*Virtueller Amboss*/
	   if(e.getClickedBlock().getType() == Material.ANVIL) {
		p.openInventory(Bukkit.createInventory(null, InventoryType.ANVIL));   
		return;
	   }
	   /*------------------*/
	   /*Werkbank*/
	   if(e.getClickedBlock().getType() == Material.WORKBENCH) return;
	   /*------------------*/
	   /*Verzauberungstisch*/
	   if(e.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE) return;
	   /*------------------*/
	  }
	  
	 
	 
	  if(e.getAction() == Action.RIGHT_CLICK_BLOCK 
		  && e.getItem() != null && e.getItem().getType() == Material.WRITTEN_BOOK 
		  && e.getClickedBlock().getType() != Material.TRAP_DOOR
		  && e.getClickedBlock().getType() != Material.STONE_BUTTON
		  && e.getClickedBlock().getType() != Material.WOOD_BUTTON
		  && e.getClickedBlock().getType() != Material.DISPENSER
		  && e.getClickedBlock().getType() != Material.DROPPER
		  && e.getClickedBlock().getType() != Material.FENCE_GATE
		  && e.getClickedBlock().getType() != Material.ACACIA_FENCE
		  && e.getClickedBlock().getType() != Material.BIRCH_FENCE_GATE
		  && e.getClickedBlock().getType() != Material.DARK_OAK_FENCE_GATE
		  && e.getClickedBlock().getType() != Material.JUNGLE_FENCE_GATE
		  && e.getClickedBlock().getType() != Material.SPRUCE_FENCE_GATE
		  && e.getClickedBlock().getType() != Material.TRAP_DOOR
		  && e.getClickedBlock().getType() != Material.ACACIA_DOOR
		  && e.getClickedBlock().getType() != Material.BIRCH_DOOR
		  && e.getClickedBlock().getType() != Material.DARK_OAK_DOOR
		  && e.getClickedBlock().getType() != Material.JUNGLE_DOOR
		  && e.getClickedBlock().getType() != Material.WOOD_DOOR
		  && e.getClickedBlock().getType() != Material.DAYLIGHT_DETECTOR
		  && e.getClickedBlock().getType() != Material.DAYLIGHT_DETECTOR_INVERTED
		  && e.getClickedBlock().getType() != Material.LEVER
		  && e.getClickedBlock().getType() != Material.HOPPER
		  && e.getClickedBlock().getType() != Material.FURNACE
		  && e.getClickedBlock().getType() != Material.WORKBENCH
		  && e.getClickedBlock().getType() != Material.ANVIL
		  && e.getClickedBlock().getType() != Material.ENDER_CHEST
		  && e.getClickedBlock().getType() != Material.TRAPPED_CHEST
		  && e.getClickedBlock().getType() != Material.CHEST
		  && e.getClickedBlock().getType() != Material.JUKEBOX
		  && e.getClickedBlock().getType() != Material.ENCHANTMENT_TABLE
		  && e.getClickedBlock().getType() != Material.ENDER_PORTAL_FRAME
		  && e.getClickedBlock().getType() != Material.BED
		  && e.getClickedBlock().getType() != Material.COMMAND) return;
	  else if(e.getAction() != Action.RIGHT_CLICK_AIR)e.setCancelled(true);
	 
	  
	  
	  
	  if(e.getItem() != null) {
		  if(e.getItem().getType() == Material.POTION) e.setCancelled(true);
		  if(e.getItem().getType() != Material.DIAMOND_SWORD &&
			 e.getItem().getType() != Material.IRON_SWORD &&
			 e.getItem().getType() != Material.STONE_SWORD &&
			 e.getItem().getType() != Material.WOOD_SWORD &&
			 e.getItem().getType() != Material.GOLD_SWORD) p.updateInventory();
			  
	  }
	 
	  
	  
	  
		 
	  
	  }
	 }
	 /*---------------------------------*/
	}
	
	
	
	@EventHandler
	public void openInventoryEvent(InventoryOpenEvent e) {
	 /*Verzauberungstisch mit Lapis*/
     if(e.getInventory() instanceof EnchantingInventory){
      if(plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
    	  e.getInventory().setItem(1, this.lapis);
    	  this.plugin.inventories.add((EnchantingInventory)e.getInventory());
      }
	 }
	}
	  
	@EventHandler
	public void closeInventoryEvent(InventoryCloseEvent e) {
	 /*Lapis aus Verzauberungstisch clearen*/
	 if((e.getInventory() instanceof EnchantingInventory)) {
	  if(this.plugin.inventories.contains((EnchantingInventory)e.getInventory())) {
	   e.getInventory().setItem(1, null);
	   this.plugin.inventories.remove((EnchantingInventory)e.getInventory());
      }
	 }
	}
	  
	@EventHandler
	public void inventoryClickEvent(InventoryClickEvent e) {
	 /*Herausnehmen des Lapis verhindern*/
     if(e.getClickedInventory() instanceof EnchantingInventory) {
	  if(this.plugin.inventories.contains((EnchantingInventory)e.getInventory()) &&
	    e.getSlot() == 1) {
	    e.setCancelled(true);
	  }
	 }
	}
	  
	@EventHandler
	public void enchantItemEvent(EnchantItemEvent e) {
	 /*Lapis nachlegen*/
     if(this.plugin.inventories.contains((EnchantingInventory)e.getInventory())) {
	  e.getInventory().setItem(1, this.lapis);
	 }
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
	 /*Item-Drops verhindern*/
     Player p = e.getPlayer();
	 if(plugin.isInOneVsOnePlayers(p.getUniqueId()) && plugin.getOneVsOnePlayer(p).getpState() != PlayerState.Edit && plugin.getOneVsOnePlayer(p).getpState() != PlayerState.InArena) {
	  e.setCancelled(true);
	 }
	}
	  
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
	 /*Block Zerstörung verhindern*/
	 Player p = e.getPlayer();
	 
	 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit) {
	  e.setCancelled(true);   
	 }
	 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby) {
	  e.setCancelled(true);   
	 }
	}
	  
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
	 /*Block Platzierung verhindern*/
	 Player p = e.getPlayer();
	 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit) {
	  e.setCancelled(true);   
	 }
	 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby) {
	  e.setCancelled(true);   
	 }
	}
	
	  @EventHandler
	  public void onDamage(EntityDamageEvent e) {
	   /*Schaden verhindern*/
	   if(e.getEntity() instanceof Player) {
		Player p = (Player) e.getEntity();
		
		 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit || plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby) {
			 e.setCancelled(true);
			 
			 p.setMaxHealth(20);
			 p.setFoodLevel(20);
			 p.setHealth(20);
			 
			 p.setFireTicks(0);
			 e.setDamage(0);
		 
		}
	   }
	  }
	  
	  @EventHandler
	  public void onHunger(FoodLevelChangeEvent e) {
		  /*Hungern verhindern*/
		  if(e.getEntity() instanceof Player) {
		   Player p =(Player) e.getEntity();
		   
			if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit || plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby) {
			 e.setCancelled(true);
			 
			 p.setFoodLevel(20);
			 p.setHealth(20);
			}
		   
		  }
	  }
	  
	  
	  @EventHandler
	  public void onBucketEmpty(PlayerBucketEmptyEvent e) {
		  /*Eimer leeren verhindern*/
		  Player p = e.getPlayer();
		  
		   if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit || 
				   plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby) 
			e.setCancelled(true); 
		   
		  
	  }
	  
	  @EventHandler
	  public void onBucketEmpty(PlayerBucketFillEvent e) {
		  /*Eimer füllen verhindern*/
		  Player p = e.getPlayer();
		  
		   if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit || 
				   plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby) 
			e.setCancelled(true); 
		   
		  
	  }
	  
	  
	  @EventHandler
	  public void onHit(EntityDamageByEntityEvent e) {
		 /*Schaden unter Spielern verhindern*/
		 if(e.getDamager() instanceof Player) {
          Player p = (Player) e.getDamager();
          if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit) {
           e.setCancelled(true);
           e.setDamage(0);
          }
          if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby) {
           e.setCancelled(true);
           e.setDamage(0);
          }
		 }
	  }
	  
	  @EventHandler
	  public void Sneak(PlayerToggleSneakEvent e) {
	   /*Automatische Warteschlange abbrechen*/
	   Player p = e.getPlayer();
	   if(plugin.isInOneVsOnePlayers(p.getUniqueId()) && plugin.getOneVsOnePlayer(p).isWasInQueue()) {
		if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby ||  plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit){
		 if(p.isSneaking()) {
			 
		  plugin.getOneVsOnePlayer(p).setWasInQueue(false);
		  
		  p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("cancelAutoQueque"), p.getDisplayName()));
		 }
		}
	   }
	  }
	  
	  
	  @EventHandler
	  public void onClick(InventoryClickEvent e) {
	   /*ArenaCheckInv Click verhindern*/  
	   if(e.getCurrentItem() != null && e.getInventory().getTitle().equalsIgnoreCase(plugin.msgs.getMsg("arenaCheckInvTitle"))) {
		e.setCancelled(true);
		if(e.getWhoClicked() instanceof Player) {
				  
		 Player p = (Player) e.getWhoClicked();
		  
		 SoundManager manager = new SoundManager(JSound.FIREWORK, p, 10.0F, 1.0F);
		 manager.play();
		}
	   }
	  }
	  
	  @EventHandler
	  public void onQuit(PlayerQuitEvent e) {
		  plugin.scoreAPI.removeBoard(e.getPlayer());
	  }
	  
	  
	  @EventHandler
	  public void onJoin(final PlayerJoinEvent e) {
		  
	   /*Join Regestrierung SQL*/
	   if(!plugin.useMySQL) {
		
		
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			
			@Override
			public void run() {
				
				if(!plugin.getDBMgr().isConnected()) {
					/*Keine Datenbank Verbindung*/
					e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
					return;
				}
				
				if(!plugin.getDBMgr().isUserExists(e.getPlayer().getUniqueId())) {
					 /*User ist neu und er wird Regetriert*/
					 plugin.addUser(e.getPlayer());
					} else {
					 /*User ist bekannt und seine Daten werden ggf. geupdated*/
					 plugin.getDBMgr().updateName(e.getPlayer().getUniqueId(), e.getPlayer().getName());
					 plugin.getDBMgr().updatePref(e.getPlayer().getUniqueId(),"");
					 plugin.getDBMgr().updatePref(e.getPlayer().getUniqueId(),"2");
					 plugin.getDBMgr().updatePref(e.getPlayer().getUniqueId(),"3");
					 plugin.getDBMgr().updatePref(e.getPlayer().getUniqueId(),"4");
					 plugin.getDBMgr().updatePref(e.getPlayer().getUniqueId(),"5");
					}  
				
			}
		});
		
	   }	
	   Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			
			@Override
			public void run() {
				 if(plugin.updateNotiJoin) {
					   String resource = "30355";
					    String spigotVersion = "";
					    
					    try {
					      HttpURLConnection con = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php").openConnection();
					      con.setDoOutput(true);
					      con.setRequestMethod("POST");
					      con.getOutputStream()
					        .write(("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + resource)
					        .getBytes("UTF-8"));
					      String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
					      
					      if(version.length() <= 20) spigotVersion = version;
					    } catch (Exception ex) {
					      plugin.getLogger().info("Failed");
					      return;
					    }
					    
					    
					    
					    
					    	if(!plugin.getDescription().getVersion().contains(spigotVersion)) {
						    	
						    	 if(e.getPlayer().hasPermission("1vs1.*") || e.getPlayer().hasPermission("1vs1.seeUpdate") || e.getPlayer().hasPermission("1vs1.Admin")) {
						    		 e.getPlayer().sendMessage("§7§m§l=============================================");
						    		 e.getPlayer().sendMessage("");
						    		 e.getPlayer().sendMessage("§7Eine neue Version des §6§l1vs1-Plugins §r§7von JHammer17");
						    		 e.getPlayer().sendMessage("§7steht nun zum §6§lDownload §r§7bereit!");
						    		 e.getPlayer().sendMessage("");
						    		 e.getPlayer().sendMessage("");
						    		 e.getPlayer().sendMessage("§7Download:");
						    		 e.getPlayer().sendMessage("§6https://www.spigotmc.org/resources/30355");
						    		 e.getPlayer().sendMessage("");
						    		 e.getPlayer().sendMessage("§7Neue Version: §6§l" + spigotVersion);
						    		 e.getPlayer().sendMessage("");
						    		 e.getPlayer().sendMessage("§7§m§l=============================================");
						    	 }
						      }
						    
					    
				   }
			}
	   
	   });
	  
	   
	  }
	  
	  @EventHandler
	  public void onClose(InventoryCloseEvent e) {
	   /*SettingsInv wird geschlossen*/
	   if(e.getInventory().getTitle().equalsIgnoreCase(plugin.msgs.getMsg("settingsInvTitle"))) {
		plugin.getOneVsOnePlayer((Player) e.getPlayer()).setPreferencesInv(null);;
	   }
	  }

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onLeaveGame(PlayerInteractEvent e) {
	 /*Spieler verlässt den 1vs1 Modus :(*/
     Player p = e.getPlayer();
	 if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
	  if(p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR && 
		 p.getItemInHand().getTypeId() == plugin.LeaveItemID && p.getItemInHand().hasItemMeta() && 
		 p.getItemInHand().getItemMeta().hasDisplayName() && 
		 p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(plugin.msgs.getMsg("leaveItemLobbyName").replaceAll("&", "§"))) {
		 
		 if(!plugin.BungeeMode) {
		  /*Bungeemode ist Inaktiv -> Spieler wird zum Exit-Punkt teleportiert*/
		  if(plugin.isInOneVsOnePlayers(p.getUniqueId()) && 
			 plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby) {
		   MainCommand.toggle1vs1(p, false, false);
		  }	
		 } else {
		  /*Bungeemode ist Aktiv -> Spieler wird zum angebenen Server teleportiert*/		 	
		  Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
		  ByteArrayOutputStream b = new ByteArrayOutputStream();
		  DataOutputStream out = new DataOutputStream(b);
		  
		  try {
		   out.writeUTF("Connect");
		   out.writeUTF(plugin.fallBackServer);
		  } catch (IOException e2) {
		   e2.printStackTrace();
		   e.getPlayer().sendMessage("§cError...");
		  }
		  
		  e.getPlayer().sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
					
		 }		
		}
	   }
	  }
	
	@EventHandler
	public void onClickLobby(InventoryClickEvent e) {
		if(e.getWhoClicked() instanceof Player) {
	     Player p = (Player) e.getWhoClicked();
		 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby && 
			e.getCurrentItem() != null && 
			e.getCurrentItem().getType() != Material.AIR) {
		  e.setCancelled(true);
		 }
		}
	}
	
	 @EventHandler
	 public void onSplash(PotionSplashEvent e) {
		 
		 for(Entity en : e.getAffectedEntities()) {
			if(en instanceof Player) {
			 Player p = (Player) en;
			 if(plugin.isInOneVsOnePlayers(p.getUniqueId())) {
			  ItemStack stack = e.getPotion().getItem();
			  PotionMeta meta = (PotionMeta) stack.getItemMeta();
			  
			  for(int i = 0; i < meta.getCustomEffects().size(); i++) {	
			   if(meta.getCustomEffects().get(i).getAmplifier() >= 252 || meta.getCustomEffects().get(i).getAmplifier() <= 0) {
				   e.setCancelled(true);
			   }			
			  }
			 }
			}
		 }
	 }
	 
	 @EventHandler
	 public void onShoot(EntityShootBowEvent e) {
		 if(e.getEntity() instanceof Player) {
			 Player p = (Player) e.getEntity();
			 if(Kit.hasKit.contains(p)) e.setCancelled(true);
			 if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit) e.setCancelled(true);
		 }
		 
		 
	 }
}
