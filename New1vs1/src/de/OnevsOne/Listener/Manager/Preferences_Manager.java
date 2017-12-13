package de.OnevsOne.Listener.Manager;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import de.OnevsOne.main;
import de.OnevsOne.Kit_Methods.Multi_Kit_Manager;

import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.Methods.getItems;
import de.OnevsOne.Methods.Queue.QuequePrefsMethods;
import de.OnevsOne.States.PlayerPrefs;
import de.OnevsOne.States.PlayerState;
import net.md_5.bungee.api.ChatColor;

/**
 * Der Code ist von JHammer
 *
 * 11.05.2016 um 21:08:02 Uhr
 */
public class Preferences_Manager implements Listener {

	private static main plugin;
	
	
	@SuppressWarnings("static-access")
	public Preferences_Manager(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
	}
	
	private HashMap<UUID, Long> lastChange = new HashMap<UUID, Long>();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
		 if(e.getItem() != null && e.getItem().getTypeId() == plugin.SettingsItemID &&
		    e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasDisplayName() &&
		    e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(plugin.msgs.getMsg("settingsItemLobbyName")) && 
		    plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId())) {
		  
		  Player p = e.getPlayer();
		  
		  if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby || 
			 plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InKitEdit) {
		   if(plugin.getOneVsOnePlayer(p).getPlayertournament() != null) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentinTournament")));
			return;
		   }
		   genSettingInv(p);
		  }
		 }
		}
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		
		if(e.getInventory().getTitle().equalsIgnoreCase(plugin.msgs.getMsg("mainSettingsInvTitle")) 
		   && e.getCurrentItem() != null) {
		 e.setCancelled(true);
		 
		 if(e.getClickedInventory().getTitle().equalsIgnoreCase(plugin.msgs.getMsg("mainSettingsInvTitle"))) {
			
		  Player p = (Player) e.getWhoClicked();
		  
		  if(e.getSlot() == 10) {
		   if(p.hasPermission("1vs1.useMultiplyKits") || p.hasPermission("1vs1.MultiplyKits.*") 
			  || p.hasPermission("1vs1.*") 
			  || p.hasPermission("1vs1.Premium")) {
				  
			  Multi_Kit_Manager.genKitSelector(p);
			  
			  SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
			  manager.play();
			} else {	  
			 
			 p.openInventory(genPrefsInv(p,"",null));
			 SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
			 manager.play();
			}  
		  } else if(e.getSlot() == 16) {
			QuequePrefsMethods.openInv(p);
			SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
			manager.play(); 
		  } else if(e.getSlot() == 13) {
			DisableMapsManager.openInv(p,0);  
			SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
			manager.play(); 
		  }
		 }
		}
	}
	
	public static void genSettingInv(Player p) {
	 Inventory inv = Bukkit.createInventory(null, 27,plugin.msgs.getMsg("mainSettingsInvTitle"));
		
	 ItemStack KitSettings = getItems.createItem(Material.SIGN, 0, 1, plugin.msgs.getMsg("settingsInvKitItem"), null);
	 ItemStack DisabledMaps = getItems.createItem(Material.PAPER, 0, 1, plugin.msgs.getMsg("settingsInvMapsItemName"), null);
	 ItemStack QueqeSettings = getItems.createItem(Material.DIAMOND_SWORD, 0, 1, plugin.msgs.getMsg("settingsInvQuequeItem"), null);
		
	 inv.setItem(10, KitSettings);
	 inv.setItem(13, DisabledMaps);
	 inv.setItem(16, QueqeSettings);
		
	 p.openInventory(inv);
	}
	
	
	public static Inventory genPrefsInv(Player p, String KitID,Inventory inv) {
		plugin.getOneVsOnePlayer(p).setPreferencesInv(KitID);
		
		   
		if(inv == null) {
		 inv = Bukkit.createInventory(null, 36, plugin.msgs.getMsg("settingsInvTitle"));
		}
		
		if(!plugin.isInOneVsOnePlayers(p.getUniqueId())) {
		 p.closeInventory();
		 return inv;
		}
		
		if(plugin.getOneVsOnePlayer(p).getpState() != PlayerState.InLobby && plugin.getOneVsOnePlayer(p).getpState() != PlayerState.InKitEdit) {
			p.closeInventory();
			return inv;
		}
		
		//Build Zeug
	    ItemStack Build = getItems.createItem(Material.IRON_PICKAXE, 0, 1, plugin.msgs.getMsg("settingBuildName"), plugin.msgs.getMsg("settingBuildDesc"));
		ItemStack BuildState = getItems.createItem(Material.INK_SACK, 8, 1, plugin.msgs.getMsg("disabled"), null);
			
		if(getPref(p.getUniqueId(), PlayerPrefs.BUILD,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
		 BuildState = getItems.createItem(Material.INK_SACK, 10, 1, plugin.msgs.getMsg("activated"), null);
		} 
		
		inv.setItem(0, Build);
		inv.setItem(1, BuildState);
		//-----
		
		//Hunger Zeug
		ItemStack Hunger = getItems.createItem(Material.COOKED_BEEF, 0, 1, plugin.msgs.getMsg("settingNoHungerName"), plugin.msgs.getMsg("settingNoHungerDesc"));
		ItemStack HungerState = getItems.createItem(Material.INK_SACK, 8, 1, plugin.msgs.getMsg("disabled"), null);
		
		if(getPref(p.getUniqueId(), PlayerPrefs.HUNGER,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
			HungerState = getItems.createItem(Material.INK_SACK, 10, 1, plugin.msgs.getMsg("activated"), null);
		}
		
		inv.setItem(3, Hunger);
		inv.setItem(2, HungerState);
		//-----
		
		//TNT Zeug
		ItemStack TNT = getItems.createItem(Material.TNT, 0, 1, plugin.msgs.getMsg("settingInstantTnTName"), plugin.msgs.getMsg("settingInstantTnTDesc"));
		ItemStack TNTState = getItems.createItem(Material.INK_SACK, 8, 1, plugin.msgs.getMsg("disabled"), null);
			
		if(getPref(p.getUniqueId(), PlayerPrefs.InstantTnT,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
			TNTState = getItems.createItem(Material.INK_SACK, 10, 1, plugin.msgs.getMsg("activated"), null);
		}
		
		inv.setItem(9, TNT);
		inv.setItem(10, TNTState);
		//-----
		
		//Explosion Zeug
		ItemStack Explosion = getItems.createItem(Material.PRISMARINE_SHARD, 0, 1, plugin.msgs.getMsg("settingTnTBlockDamageName"), plugin.msgs.getMsg("settingTnTBlockDamageDesc"));
		ItemStack ExplosionState = getItems.createItem(Material.INK_SACK, 8, 1, plugin.msgs.getMsg("disabled"), null);
		
		if(getPref(p.getUniqueId(), PlayerPrefs.NoTnTDamage,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
			ExplosionState = getItems.createItem(Material.INK_SACK, 10, 1, plugin.msgs.getMsg("activated"), null);
		} 
		
		inv.setItem(12, Explosion);
		inv.setItem(11, ExplosionState);
		//-----
		
		//Crafting Zeug
		ItemStack Crafting = getItems.createItem(Material.WORKBENCH, 0, 1, plugin.msgs.getMsg("settingNoCraftingName"), plugin.msgs.getMsg("settingNoCraftingDesc"));
		ItemStack CraftingState = getItems.createItem(Material.INK_SACK, 8, 1, plugin.msgs.getMsg("disabled"), null);
			
		if(getPref(p.getUniqueId(), PlayerPrefs.NoCrafting,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
			CraftingState = getItems.createItem(Material.INK_SACK, 10, 1, plugin.msgs.getMsg("activated"), null);
		}
		
		inv.setItem(18, Crafting);
		inv.setItem(19, CraftingState);
		//-----
		
		//Suppen Zeug
		ItemStack SoupHeal = getItems.createItem(Material.MUSHROOM_SOUP, 0, 1, plugin.msgs.getMsg("settingSoupRegName"), plugin.msgs.getMsg("settingSoupRegDesc"));
		ItemStack SoupHealState = getItems.createItem(Material.INK_SACK, 8, 1, plugin.msgs.getMsg("disabled"), null);
			
		if(getPref(p.getUniqueId(), PlayerPrefs.SoupReg,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
			SoupHealState = getItems.createItem(Material.INK_SACK, 10, 1, plugin.msgs.getMsg("activated"), null);
		}
		
		inv.setItem(21, SoupHeal);
		inv.setItem(20, SoupHealState);
		//-----
		
		//Fallschaden Zeug
		ItemStack NoFallDamage = getItems.createItem(Material.IRON_BOOTS, 0, 1, plugin.msgs.getMsg("settingNoFallDamageName"), plugin.msgs.getMsg("settingNoFallDamageDesc"));
		ItemStack NoFallDamageState = getItems.createItem(Material.INK_SACK, 8, 1, plugin.msgs.getMsg("disabled"), null);
			
		if(getPref(p.getUniqueId(), PlayerPrefs.NoFallDamage,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
			NoFallDamageState = getItems.createItem(Material.INK_SACK, 10, 1, plugin.msgs.getMsg("activated"), null);
		}
		
		inv.setItem(5, NoFallDamage);
		inv.setItem(6, NoFallDamageState);
		//-----
		
		//Pfeil Zeug
		ItemStack NoArrowPickup = getItems.createItem(Material.ARROW, 0, 1, plugin.msgs.getMsg("settingNoArrowPickupName"), plugin.msgs.getMsg("settingNoArrowPickupDesc"));
		ItemStack NoArrowPickupState = getItems.createItem(Material.INK_SACK, 8, 1, plugin.msgs.getMsg("disabled"), null);
			
		if(getPref(p.getUniqueId(), PlayerPrefs.NoArrowPickup, plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
			 NoArrowPickupState = getItems.createItem(Material.INK_SACK, 10, 1, plugin.msgs.getMsg("activated"), null);
		}
		
		inv.setItem(8, NoArrowPickup);
		inv.setItem(7, NoArrowPickupState);
		//-----
		
		//Suppen-Noob Zeug
		ItemStack SoupNoob = getItems.createItem(Material.BOWL, 0, 1, plugin.msgs.getMsg("settingSoupNoobName"), plugin.msgs.getMsg("settingSoupNoobDesc"));
		ItemStack SoupNoobState = getItems.createItem(Material.INK_SACK, 8, 1, plugin.msgs.getMsg("disabled"), null);
		
		if(getPref(p.getUniqueId(), PlayerPrefs.SoupNoob, plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
			SoupNoobState = getItems.createItem(Material.INK_SACK, 10, 1, plugin.msgs.getMsg("activated"), null);
		} 
		
		inv.setItem(14, SoupNoob);
		inv.setItem(15, SoupNoobState);
		//-----
		
		//NoHitDelay Zeug
		ItemStack NoHitDelay = getItems.createItem(Material.SUGAR, 0, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("settingNoHitDelayName")), ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("settingNoHitDelayDesc")).replaceAll("%n", "\n"));
		ItemStack NoHitDelayState = getItems.createItem(Material.INK_SACK, 8, 1, plugin.msgs.getMsg("disabled"), null);
			
		if(getPref(p.getUniqueId(), PlayerPrefs.NoHitDelay, plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
			NoHitDelayState = getItems.createItem(Material.INK_SACK, 10, 1, plugin.msgs.getMsg("activated"), null);
		} 
		
		inv.setItem(17, NoHitDelay);
        inv.setItem(16, NoHitDelayState);
		//-----
			
        //Regeneration Zeug
		ItemStack NaturalRegneration = getItems.createItem(Material.POTION, 8193, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("settingNoNaturalRegenerationName")), ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("settingNoNaturalRegenerationDesc")).replaceAll("%n", "\n"));
		ItemStack NaturalRegnerationState = getItems.createItem(Material.INK_SACK, 8, 1, plugin.msgs.getMsg("disabled"), null);
			
		if(getPref(p.getUniqueId(), PlayerPrefs.NoRegneration, plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
			NaturalRegnerationState = getItems.createItem(Material.INK_SACK, 10, 1, plugin.msgs.getMsg("activated"), null);
		} 
		
		inv.setItem(23, NaturalRegneration);
		inv.setItem(24, NaturalRegnerationState);
		//-----
		
		//DoubleJump Zeug
		ItemStack DoubleJump = getItems.createItem(Material.FIREWORK, 0, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("settingDoubleJumpName")), ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("settingDoubleJumpDesc")).replaceAll("%n", "\n"));
		ItemStack DoubleJumpState = getItems.createItem(Material.INK_SACK, 8, 1, plugin.msgs.getMsg("disabled"), null);
		
		if(getPref(p.getUniqueId(), PlayerPrefs.DoubleJump, plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
			DoubleJumpState = getItems.createItem(Material.INK_SACK, 10, 1, plugin.msgs.getMsg("activated"), null);
		} 
		
		inv.setItem(26, DoubleJump);
		inv.setItem(25, DoubleJumpState);
		//-----
		
		//DoubleJump Zeug 
		ItemStack NoItemDrops = getItems.createItem(Material.BARRIER, 0, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("settingNoItemDropsName")), ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("settingNoItemDropsDesc")).replaceAll("%n", "\n"));
		ItemStack NoItemDropsState = getItems.createItem(Material.INK_SACK, 8, 1, plugin.msgs.getMsg("disabled"), null);
			
		if(getPref(p.getUniqueId(), PlayerPrefs.NoItemDrops, plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
			NoItemDropsState = getItems.createItem(Material.INK_SACK, 10, 1, plugin.msgs.getMsg("activated"), null);
		} 
				
		inv.setItem(27, NoItemDrops);
		inv.setItem(28, NoItemDropsState);
		//-----
		
		//DoubleJump Zeug 
		ItemStack NoFriendlyFire = getItems.createItem(Material.BLAZE_POWDER, 0, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("settingNoFriendlyFireName")), ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("settingNoFriendlyFireDesc")).replaceAll("%n", "\n"));
		ItemStack NoFriendlyFireState = getItems.createItem(Material.INK_SACK, 8, 1, plugin.msgs.getMsg("disabled"), null);
					
		if(getPref(p.getUniqueId(), PlayerPrefs.NoFriendlyFire, plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
			NoFriendlyFireState = getItems.createItem(Material.INK_SACK, 10, 1, plugin.msgs.getMsg("activated"), null);
		} 
		
		inv.setItem(29, NoFriendlyFireState);
		inv.setItem(30, NoFriendlyFire);
		
		//DoubleJump Zeug 
		ItemStack WaterDmg = getItems.createItem(Material.WATER_BUCKET, 0, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("settingWaterDmgName")), ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("settingWaterDmgDesc")).replaceAll("%n", "\n"));
		ItemStack WaterDmgState = getItems.createItem(Material.INK_SACK, 8, 1, plugin.msgs.getMsg("disabled"), null);
						
		if(getPref(p.getUniqueId(), PlayerPrefs.WaterDamage, plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
			WaterDmgState = getItems.createItem(Material.INK_SACK, 10, 1, plugin.msgs.getMsg("activated"), null);
		} 
		
						
		inv.setItem(32, WaterDmg);
		inv.setItem(33, WaterDmgState);
		//-----
			
			
		//Platzhalter
		inv.setItem(4, getItems.createItem(Material.STAINED_GLASS_PANE, 7, 1, "§a", null));
		inv.setItem(13, getItems.createItem(Material.STAINED_GLASS_PANE, 7, 1, "§a", null));
		inv.setItem(22, getItems.createItem(Material.STAINED_GLASS_PANE, 7, 1, "§a", null));
		inv.setItem(31, getItems.createItem(Material.STAINED_GLASS_PANE, 7, 1, "§a", null));
		//-----	
		return inv;
	}
	
	@EventHandler
	public void changeState(final InventoryClickEvent e) {
		if(e.getWhoClicked() instanceof Player) {
			 if(e.getCurrentItem() != null && e.getInventory().getTitle().equalsIgnoreCase(plugin.msgs.getMsg("settingsInvTitle").replaceAll("&", "§"))) {
			  if(!plugin.isInOneVsOnePlayers(e.getWhoClicked().getUniqueId())) return;
			  
			  if(plugin.getOneVsOnePlayer(e.getWhoClicked().getUniqueId()).getpState() != PlayerState.InLobby && 
				 plugin.getOneVsOnePlayer(e.getWhoClicked().getUniqueId()).getpState() != PlayerState.InKitEdit) 
			   return;
			  
			  e.setCancelled(true);
			 }
			 
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			
			@Override
			public void run() {
				
	 if(e.getWhoClicked() instanceof Player) {
		 if(e.getCurrentItem() != null && e.getInventory().getTitle().equalsIgnoreCase(plugin.msgs.getMsg("settingsInvTitle").replaceAll("&", "§"))) {
		  if(!plugin.isInOneVsOnePlayers(e.getWhoClicked().getUniqueId())) return;
		  
		  if(plugin.getOneVsOnePlayer(e.getWhoClicked().getUniqueId()).getpState() != PlayerState.InLobby && 
			 plugin.getOneVsOnePlayer(e.getWhoClicked().getUniqueId()).getpState() != PlayerState.InKitEdit) 
		   return;
		  
		  e.setCancelled(true);
		 }
		 
		
		 
	  if(e.getCurrentItem() != null && 
		 e.getInventory().getTitle().equalsIgnoreCase(plugin.msgs.getMsg("settingsInvTitle")) && 
		 e.getClickedInventory().getTitle().equalsIgnoreCase(plugin.msgs.getMsg("settingsInvTitle"))) {
		 
		  
		  final Player p2 = (Player) e.getWhoClicked();
		  e.setCancelled(true);
		  if(e.isRightClick()) {
			  if(p2.hasPermission("1vs1.useMultiplyKits") || p2.hasPermission("1vs1.MultiplyKits.*") 
					  || p2.hasPermission("1vs1.*") 
					  || p2.hasPermission("1vs1.Premium")) {
				
				
					 
						  Bukkit.getScheduler().runTask(plugin, new Runnable() {
							
							@Override
							public void run() {
								Multi_Kit_Manager.genKitSelector(p2);
								
							}
						});
					 
						 
					 
							
						  
					  SoundManager manager = new SoundManager(JSound.CLICK, p2, 10.0F, 1.0F);
					  manager.play();
					  return;
				   }
			 
			  Bukkit.getScheduler().runTask(plugin, new Runnable() {
					
					@Override
					public void run() {
						Preferences_Manager.genSettingInv(p2);
						
					}
				});
				   
				
				return;
			}
		  
		  if(lastChange.containsKey(p2.getUniqueId())) {
		   if(System.currentTimeMillis()-lastChange.get(p2.getUniqueId()) < plugin.toggleCoolDown) {
		  	p2.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("toggleCoolDown")));
		  	return;
		   }
		  }
			  
	   lastChange.put(p2.getUniqueId(), System.currentTimeMillis());
	   
	   //Bauen switch
	   if(e.getSlot() == 0 || e.getSlot() == 1) {
		Player p = (Player) e.getWhoClicked();
		if(getPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.BUILD,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.BUILD, false,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		} else {
		 
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.BUILD, true,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		}
	  }
	  //-----
	   
	  //Hunger switch
	   if(e.getSlot() == 2 || e.getSlot() == 3) {
		Player p = (Player) e.getWhoClicked();
		if(getPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.HUNGER,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.HUNGER, false,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		} else {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.HUNGER, true,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		}
	  }
	  //-----
	   
	  //Instant TnT switch
	   if(e.getSlot() == 9 || e.getSlot() == 10) {
		Player p = (Player) e.getWhoClicked();
		if(getPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.InstantTnT,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.InstantTnT, false,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		} else {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.InstantTnT, true,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		}	
	   }
	   //-----
	   
	   //No TnT Damage switch
	   if(e.getSlot() == 11 || e.getSlot() == 12) {
		Player p = (Player) e.getWhoClicked();
		if(getPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoTnTDamage,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoTnTDamage, false,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		} else {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoTnTDamage, true,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		}	
	   }
	   //------
	   
	   //No Crafting switch
	   if(e.getSlot() == 18 || e.getSlot() == 19) {
		Player p = (Player) e.getWhoClicked();
		if(getPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoCrafting,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoCrafting, false,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		} else {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoCrafting, true,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		}	
	   }
	   //-----
	   
	   //SoupReg switch
	   if(e.getSlot() == 20 || e.getSlot() == 21) {
		Player p = (Player) e.getWhoClicked();
		if(getPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.SoupReg,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.SoupReg, false,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		} else {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.SoupReg, true,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		}	
	   }
	   //-----
	   
	   //NoFall switch
	   if(e.getSlot() == 5 || e.getSlot() == 6) {
		Player p = (Player) e.getWhoClicked();
		if(getPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoFallDamage,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoFallDamage, false,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		} else {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoFallDamage, true,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		}	
	   }
	   //-----
	   
	   //No Arrow Pickup switch
	   if(e.getSlot() == 7 || e.getSlot() == 8) {
		Player p = (Player) e.getWhoClicked();
		if(getPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoArrowPickup,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoArrowPickup, false,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		} else {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoArrowPickup, true,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		}
	   }
	   //-----
	   
	   //SoupNoob switch
	   if(e.getSlot() == 14 || e.getSlot() == 15) {
		Player p = (Player) e.getWhoClicked();
		if(getPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.SoupNoob,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.SoupNoob, false,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		} else {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.SoupNoob, true,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		}
	   }
	   //-----
	   
	   //No Hit Delay switch
	   if(e.getSlot() == 16 || e.getSlot() == 17) {
		Player p = (Player) e.getWhoClicked();
		if(getPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoHitDelay,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoHitDelay, false,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		} else {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoHitDelay, true,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		}
	   }
	   //-----
	   
	   //NoRegeneration switch
	   if(e.getSlot() == 23 || e.getSlot() == 24) {
		Player p = (Player) e.getWhoClicked();
		if(getPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoRegneration,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoRegneration, false,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		} else {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoRegneration, true,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		}
	   }
	   //-----
	   
	   //DoubleJump switch
	   if(e.getSlot() == 25 || e.getSlot() == 26) {
		Player p = (Player) e.getWhoClicked();
		if(getPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.DoubleJump,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.DoubleJump, false,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		} else {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.DoubleJump, true,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		}
	   }
	   //-----
	   
	 //NoItemDrops switch
	   if(e.getSlot() == 27 || e.getSlot() == 28) {
		Player p = (Player) e.getWhoClicked();
		if(getPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoItemDrops,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoItemDrops, false,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		} else {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoItemDrops, true,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		}
	   }
	   //-----
	   
	 //NoFriendlyFire switch
	   if(e.getSlot() == 29 || e.getSlot() == 30) {
		Player p = (Player) e.getWhoClicked();
		if(getPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoFriendlyFire,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoFriendlyFire, false,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		} else {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.NoFriendlyFire, true,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		}
	   }
	   //-----
	   
	 //NoFriendlyFire switch
	   if(e.getSlot() == 32 || e.getSlot() == 33) {
		Player p = (Player) e.getWhoClicked();
		if(getPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.WaterDamage,plugin.getOneVsOnePlayer(p).getPreferencesInv())) {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.WaterDamage, false,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		} else {
		 setPref(e.getWhoClicked().getUniqueId(), PlayerPrefs.WaterDamage, true,plugin.getOneVsOnePlayer(p).getPreferencesInv());
		 genPrefsInv(p,plugin.getOneVsOnePlayer(p).getPreferencesInv(),e.getClickedInventory());
		}
	   }
	   //-----
	   
	   e.setCancelled(true);  
	  }
	 }
			}
		});
	}
	
	public static boolean getPref(UUID uuid, PlayerPrefs pref, String KitID) {
		
		
		 if(!plugin.getDBMgr().isConnected()) {
		  System.out.println(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
		  return false;
		 }
		 
		 if(KitID.equalsIgnoreCase("d")) {
		  KitID = plugin.getDBMgr().getDefaultKit(uuid);
		 }
		 
		 return plugin.getDBMgr().getPref(uuid, getPrefID(pref),KitID);
		
	}
	
	public static void setPref(UUID uuid, PlayerPrefs pref, boolean state,String KitID) {
		 if(!plugin.getDBMgr().isConnected()) {
		  System.out.println(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
		  return;
	     }
		 
		 if(KitID.equalsIgnoreCase("d")) KitID = plugin.getDBMgr().getDefaultKit(uuid);
		 
		 
		 plugin.getDBMgr().setPref(uuid, getPrefID(pref), state,KitID);
		 
		 playSound(Bukkit.getPlayer(uuid));
		 return;
		
	}
	
	public static int getPrefID(PlayerPrefs pref) {
		if(pref == PlayerPrefs.QUEUE) {
			return 0;
		}
		if(pref == PlayerPrefs.BUILD) {
			return 1;
		}
		if(pref == PlayerPrefs.HUNGER) {
			return 2;
		}
		if(pref == PlayerPrefs.InstantTnT) {
			return 3;
		}
		if(pref == PlayerPrefs.NoTnTDamage) {
			return 4;
		}
		if(pref == PlayerPrefs.NoCrafting) {
			return 5;
		}
		if(pref == PlayerPrefs.SoupReg) {
			return 6;
		}
		if(pref == PlayerPrefs.NoFallDamage) {
			return 7;
		}
		if(pref == PlayerPrefs.NoArrowPickup) {
			return 8;
		}
		if(pref == PlayerPrefs.SoupNoob) {
			return 9;
		}
		if(pref == PlayerPrefs.DoubleJump) {
			return 10;
		}
		if(pref == PlayerPrefs.NoHitDelay) {
			return 11;
		}
		if(pref == PlayerPrefs.NoRegneration) {
			return 12;
		}
		if(pref == PlayerPrefs.NoItemDrops) {
			return 13;
		}
		if(pref == PlayerPrefs.NoFriendlyFire) {
			return 14;
		}
		if(pref == PlayerPrefs.WaterDamage) {
			return 15;
		}
		
		return -1;
	}

	private static void playSound(Player p) {
		SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
		manager.play(); 
	}
}
