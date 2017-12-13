package de.OnevsOne.Listener.Manager.Tournament;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import de.OnevsOne.main;
import de.OnevsOne.Listener.Manager.ChallangeManager;
import de.OnevsOne.Listener.Manager.Tournament.AnvilGUI.AnvilClickEvent;

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.JSound;
import de.OnevsOne.Methods.ScoreBoardManager;
import de.OnevsOne.Methods.SoundManager;
import de.OnevsOne.Methods.getItems;
import de.OnevsOne.Methods.Tournament.TournamentManager;
import de.OnevsOne.Methods.Tournament.Tournament_InvCreator;
import de.OnevsOne.States.OneVsOnePlayer;
import de.OnevsOne.States.TournamentState;
import net.md_5.bungee.api.ChatColor;

public class Tournament_Creator_InvManager implements Listener {

	private main plugin;



	public Tournament_Creator_InvManager(main plugin) {
		plugin.getServer().getPluginManager().registerEvents(this,plugin);
		this.plugin = plugin;
	}
	
	String title = "";
	
	
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
	 if(e.getWhoClicked() instanceof Player) {
	  final Player p = (Player) e.getWhoClicked();	
	  if(e.getClickedInventory() != null) {
	   if(e.getInventory().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentSettingsInvTitle").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix)))) {
		e.setCancelled(true);
		if(e.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentSettingsInvTitle").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix)))) {
		 if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
		  //YamlConfiguration cfg = plugin.getYaml("/Tournaments/" + p.getName());
		  
		  
		  int Slot = e.getSlot();
		  
		  if(Slot == 0) {
			  AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {

				@Override
				public void onAnvilClick(final AnvilClickEvent event) {
					if(event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT){
                        event.setWillClose(true);
                        event.setWillDestroy(true);
                        
                        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
							
							@Override
							public void run() {
								 
			                   	 
								 String Kit = event.getName();
								 Kit = Kit.split(":")[0];
								 
								
								 TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
								 
								 if(plugin.getDBMgr().isCustomKitExists(Kit) == 0) {
									 p.sendMessage(plugin.msgs.getMsg("kitNotFound").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix));
									 SoundManager manager = new SoundManager(JSound.DRUM, p, 10.0F, 0.5F);
									 manager.play();
									 reSetData(p.getUniqueId(), tMgr);
									 Tournament_InvCreator creator = new Tournament_InvCreator(plugin);
					             	 creator.openInv(p);
					             	 Tournament_InvCreator creator1 = new Tournament_InvCreator(plugin);
					             	 creator1.reGenerateInv(p.getUniqueId());
					             	 
					                
					             	 
									 return;
								 } else {
									 
									 tMgr.setKit(event.getName());
									 
									 SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1F);
									  manager.play();
				                     
									  reSetData(p.getUniqueId(), tMgr);
									  Tournament_InvCreator creator = new Tournament_InvCreator(plugin);
				             		 creator.openInv(p);
				             		 creator.reGenerateInv(tMgr.getOwnerUUID());
								 }
								 
								 
								 
								
							}
						}, 1);
					}else{
                        event.setWillClose(false);
                        event.setWillDestroy(false);
					}
				}
          });
			  ItemStack i = getItems.createItem(Material.PAPER, 0, 1, "Kit", null);
              
             
              gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, i);
              SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1F);
			  manager.play();
              gui.open();	  
			  
			
		  }
			 
		  if(Slot == 2 || Slot == 3 || Slot == 4) {
			  
			  p.closeInventory();
			 TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
			 tMgr.delete();
			 for(OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) ScoreBoardManager.updateBoard(players.getPlayer(), true);
			  return;
		  }
		  
		  if(Slot == 8) {
			  AnvilGUI gui = new AnvilGUI(p, new AnvilGUI.AnvilClickEventHandler() {

				@Override
				public void onAnvilClick(final AnvilClickEvent event) {
					if(event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT){
                        event.setWillClose(true);
                        event.setWillDestroy(true);
                        
                        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
							
							@Override
							public void run() {
								 
								 TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
								 
								
								 tMgr.setPassword(event.getName());;
								 
				                 SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 0.5F);
								  manager.play();
								  reSetData(p.getUniqueId(), tMgr);
			                     Tournament_InvCreator creator = new Tournament_InvCreator(plugin);
			             		 creator.openInv(p);
			             		 Tournament_InvCreator creator1 = new Tournament_InvCreator(plugin);
				             	 creator1.reGenerateInv(tMgr.getOwnerUUID());
							}
						}, 1);
					}else{
                        event.setWillClose(false);
                        event.setWillDestroy(false);
					}
				}
          });
			  ItemStack i = getItems.createItem(Material.PAPER, 0, 1, "Passwort", null);
              
             
              gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, i);
              SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1F);
			  manager.play();
              gui.open();	  
			  
			
		  }
		  
		  if(Slot == 28) {
			  Tournament_InvCreator creator = new Tournament_InvCreator(plugin);
			  creator.generateQualliInv(p);
			  SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
			   manager.play();
		  }
		  
		  if(Slot == 22) {
			 
			  
			  TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
				 
               
			  
			  int Secs = tMgr.getStartTimeSecs();
			  int Mins = tMgr.getStartTimeMins();
			  
			  
			  if(e.isShiftClick()) {
				  Secs = Secs+30;
				  while(Secs >= 60) {
					  Secs = Secs-60;
					  Mins++;
				  }
			  } else {
				  Secs = Secs+10;
				  while(Secs >= 60) {
					  Secs = Secs-60;
					  Mins++;
				  }
			  }
			  
			  if(Mins >= 10) {
				  Mins = 10;
				  Secs = 0;
				  SoundManager manager = new SoundManager(JSound.DRUM, p, 10.0F, 0.1F);
				  manager.play();
			  } else {
				  SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
				  manager.play();
			  }
			  
			  tMgr.setStartTimeMins(Mins);
			  tMgr.setStartTimeSecs(Secs);
			  
				 
              String secs = "";
   		      if(tMgr.getStartTimeSecs() < 10) {
   			   secs = "0" + tMgr.getStartTimeSecs();
   		      } else {
   			   secs = "" + tMgr.getStartTimeSecs();
   		      }
   		      ItemStack startTime = getItems.createItem(Material.WATCH, 0, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentSettingsStartTime").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix).replaceAll("%TimeMins%", "" + tMgr.getStartTimeMins()).replaceAll("%TimeSecs%", secs)), null);
              
   		      e.getInventory().setItem(31, startTime);
				 
   		      
   		      reSetData(p.getUniqueId(), tMgr);
   		      
   		      Tournament_InvCreator creator1 = new Tournament_InvCreator(plugin);
   		      
      	      creator1.reGenerateInv(tMgr.getOwnerUUID());
      		 return;
		  }
		  
		  if(Slot == 40) {
			  
			  TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
            	 
			  int Secs = tMgr.getStartTimeSecs();
			  int Mins = tMgr.getStartTimeMins();
			  
			  
			  if(e.isShiftClick()) {
				  //10 - 30 = -20*-1 = 20*2 
				  //20 - 30 = -10*-1 = 10*2
				  Secs = Secs-30;
				  while(Secs < 0) {
					  if(Secs == -20) {
						  Secs = 40;
					  }
					  if(Secs == -10) {
						  Secs = 50;
					  }
					  Mins--;
					  if(Secs < 0) {
						  Secs = 0;
					  }
				  }
			  } else {
				  Secs = Secs-10;
				  while(Secs < 0) {
					  Secs = 50;
					  Mins--;
				  }
			  }
			  
			  if(Mins < 0) {
				  Mins = 0;
				  Secs = 0;
				  SoundManager manager = new SoundManager(JSound.DRUM, p, 10.0F, 0.1F);
				  manager.play();
			  } else {
				  SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
				  manager.play();
			  }
			  
			  tMgr.setStartTimeSecs(Secs);
			  tMgr.setStartTimeMins(Mins);
			  
              	 
              
				 
              String secs = "";
   		      if(tMgr.getStartTimeSecs() < 10) {
   			   secs = "0" + tMgr.getStartTimeSecs();
   		      } else {
   			   secs = "" + tMgr.getStartTimeSecs();
   		      }
   		      ItemStack startTime = getItems.createItem(Material.WATCH, 0, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentSettingsStartTime").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix).replaceAll("%TimeMins%", "" + tMgr.getStartTimeMins()).replaceAll("%TimeSecs%", secs)), null);
              
   		      e.getInventory().setItem(31, startTime);
				 
   		      reSetData(p.getUniqueId(), tMgr);
   		      
   		      Tournament_InvCreator creator1 = new Tournament_InvCreator(plugin);
   		      creator1.reGenerateInv(tMgr.getOwnerUUID());
      		 return;
		  }
		  
		  if(Slot == 21) {
			
			  TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
			  
			  int Player = tMgr.getMaxPlayers();
			 
			  if(!e.isShiftClick()) {
				  Player++;
				  SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1F);
				  manager.play();
			  } else {
				  Player = Player+5;
				  SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1F);
				  manager.play();
			  }
			  
			  if(Player <= 0) {
				  Player = 2;
				  SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1F);
				  manager.play();
			  }
			  
			  tMgr.setMaxPlayers(Player);
			  
			  
              	 
              
				 
              String players = "";
   		      if(tMgr.getMaxPlayers() < 0) {
   		    	players = plugin.msgs.getMsg("tournamentinfinity");
   		      } else {
   		    	players = "" + tMgr.getMaxPlayers();
   		      }
   		      ItemStack maxPlayers = getItems.createItem(Material.SKULL_ITEM, 3, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentSettingsMaxPlayers").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix).replaceAll("%MaxPlayers%", players)), null);
              
   		      e.getInventory().setItem(30, maxPlayers);
				 
   		      reSetData(p.getUniqueId(), tMgr);
   		      
   		      Tournament_InvCreator creator1 = new Tournament_InvCreator(plugin);
   		      creator1.reGenerateInv(tMgr.getOwnerUUID());
      		 return;
		  }
		  
		  if(Slot == 39) {
			  
			 
			  TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
         	 
			  int Player = tMgr.getMaxPlayers();
			 
			  if(e.isShiftClick()) {
			   if(Player <= -1) {
				   Player = -1;
				   SoundManager manager = new SoundManager(JSound.DRUM, p, 10.0F, 0.5F);
				   manager.play();
			   } else {
				   Player = Player-5;
				   SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1F);
					  manager.play();
				   
			   }
			  } else {
			   Player--;  
			   if(Player < -1) {
				   SoundManager manager = new SoundManager(JSound.DRUM, p, 10.0F, 0.5F);
				   manager.play();
			   } else {
				   SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1F);
					  manager.play();
			   }
			   
			  }
			  
			  
			  if(Player <= 1) {
				  Player = -1;
				  
			  }
			  if(Player < -1) {
				  Player = -1;
				  SoundManager manager = new SoundManager(JSound.DRUM, p, 10.0F, 0.5F);
				  manager.play();
			  }
			  
			  tMgr.setMaxPlayers(Player);
			  
              	 
              
				 
              String players = "";
   		      if(tMgr.getMaxPlayers() < 0) {
   		    	players = plugin.msgs.getMsg("tournamentinfinity");
   		      } else {
   		    	players = "" + tMgr.getMaxPlayers();
   		      }
   		      
   		     
   		      ItemStack maxPlayers = getItems.createItem(Material.SKULL_ITEM, 3, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentSettingsMaxPlayers").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix).replaceAll("%MaxPlayers%", players)), null);
              
   		      e.getInventory().setItem(30, maxPlayers);
   		      reSetData(p.getUniqueId(), tMgr);
   		      Tournament_InvCreator creator1 = new Tournament_InvCreator(plugin);
   		   	  creator1.reGenerateInv(tMgr.getOwnerUUID());
      		 return;
		  }
		  
		  if(Slot == 29) {
			  Tournament_InvCreator creator = new Tournament_InvCreator(plugin);
			  creator.generateKOInv(p);
			  SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 0.5F);
			  manager.play();
			  Tournament_InvCreator creator1 = new Tournament_InvCreator(plugin);
			  TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
         	  creator1.reGenerateInv(tMgr.getOwnerUUID());
		  }
		  
		  if(Slot == 15) {
			  TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
			  if(!tMgr.isOpened()) {
				  
				  ChallangeManager.removePlayerComplete(p);
				  
				  tMgr.setOpened(true);
				  
				  
				  
				  if(tMgr.getPassword() == null || tMgr.getPassword().equalsIgnoreCase("")) {
				   for(OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) {
					
				    	 players.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentpublicTournamentOpened").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix).replaceAll("%Name%", p.getName())));
				    	 SoundManager manager = new SoundManager(JSound.ENDER_DRAGON, players.getPlayer(), 300, 1);
				    	 manager.play();
                   
				   }
				   tMgr.addPlayer(p);
				   p.closeInventory();
				  } else {
					  p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentprivateTournamentOpened").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix).replaceAll("%Name%", p.getName()).replaceAll("%Password%", tMgr.getPassword())));
					  SoundManager manager = new SoundManager(JSound.LEVEL, p, 10.0F, 1.0F);
					  manager.play();
					  p.closeInventory();
					  tMgr.addPlayer(p);
				  }
				  
				  reSetData(p.getUniqueId(), tMgr);
				  
				  Tournament_InvCreator creator1 = new Tournament_InvCreator(plugin);
	          	  creator1.reGenerateInv(tMgr.getOwnerUUID());
	          	  
	          	for(OneVsOnePlayer players : plugin.getOneVsOnePlayersCopy().values()) ScoreBoardManager.updateBoard(players.getPlayer(), true);
			  } else {
				  p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentOpened").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix)));
			  }
		  }
		  
		  if(Slot == 25 || Slot == 26 || Slot == 34 || Slot == 35 || Slot == 43 || Slot == 44) {
			  p.closeInventory();
			  TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
			  if(tMgr.getState() != TournamentState.STARTING) {
				  if(tMgr.isOpened()) {
					  tMgr.setStartCounter(tMgr.getStartTimeMins()*60+tMgr.getStartTimeSecs());
					  tMgr.setState(TournamentState.STARTING);
					  p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentStartActivated").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix))); 
					  reSetData(p.getUniqueId(), tMgr);
					  Tournament_InvCreator creator1 = new Tournament_InvCreator(plugin);
		         	  creator1.reGenerateInv(tMgr.getOwnerUUID());
				  } else {
					  p.sendMessage(MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentNotOpened")));
				  }
				  
			  } else {
				  p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentAlreadystarting").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix)));
			  }
			  
		  }
		 }
		}
	   }
	  }
     }
	}
	/*
	 *  YamlConfiguration cfgx = plugin.getYaml("/Tournaments/" + p.getName());
            	 
			  int Secs = cfg.getInt("Settings.TimeSecs");
			  int Mins = cfg.getInt("Settings.TimeMins");
			  
			  
			  if(e.isShiftClick()) {
				  //10 - 30 = -20*-1 = 20*2 
				  //20 - 30 = -10*-1 = 10*2
				  Secs = Secs-30;
				  while(Secs < 0) {
					  if(Secs == -20) {
						  Secs = 40;
					  }
					  if(Secs == -10) {
						  Secs = 50;
					  }
					  Mins--;
					  if(Secs < 0) {
						  Secs = 0;
					  }
				  }
			  } else {
				  Secs = Secs-10;
				  while(Secs < 0) {
					  Secs = 50;
					  Mins--;
				  }
			  }
			  
			  if(Mins < 0) {
				  Mins = 0;
				  Secs = 0;
				  SoundManager manager = new SoundManager(JSound.DRUM, p, 10.0F, 0.1F);
				  manager.play();
			  } else {
				  SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
				  manager.play();
			  }
			  
			  cfgx.set("Settings.TimeSecs", Secs);
			  cfgx.set("Settings.TimeMins", Mins);
              	 
              try {
            	  cfgx.save(plugin.getPluginFile("/Tournaments/" + p.getName()));
              } catch (IOException ee) {
               p.sendMessage("§cError");
               return;
              }
				 
              String secs = "";
   		      if(cfgx.getInt("Settings.TimeSecs") < 10) {
   			   secs = "0" + cfgx.getInt("Settings.TimeSecs");
   		      } else {
   			   secs = "" + cfgx.getInt("Settings.TimeSecs");
   		      }
   		      ItemStack startTime = getItems.createItem(Material.WATCH, 0, 1, "§6Startzeit: §7§7" + cfgx.getInt("Settings.TimeMins") + ":" + secs, null);
              
   		      e.getInventory().setItem(30, startTime);
	 */
	@EventHandler
	public void onClick2(InventoryClickEvent e) {
	 if(e.getWhoClicked() instanceof Player) {
	  final Player p = (Player) e.getWhoClicked();	
	  if(e.getClickedInventory() != null) {
	   if(e.getInventory().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentqualliInvTitle").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix)))) {
		e.setCancelled(true);
		if(e.getClickedInventory().getTitle().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentqualliInvTitle").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix)))) {
		 if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
		  
		  int Slot = e.getSlot();
		  
		  if(Slot == 10) {
			  TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
		   
		   
		   int fights = tMgr.getFightsQu();
		   if(fights == 1) {
			   tMgr.setFightsQu(3);
		   } else if(fights == 3) {
			   tMgr.setFightsQu(5);
		   } else if(fights == 5) {
			   tMgr.setFightsQu(1);
		   }
		   
		   
		   fights = tMgr.getFightsQu();
		   ItemStack rounds = getItems.createItem(Material.NETHER_STAR, 0, tMgr.getFightsQu(), ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentFightsPerRound").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix).replaceAll("%Fights%", "" + tMgr.getFightsQu())), null);
		   
		   e.getInventory().setItem(10, rounds);
		   
		   SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
		   manager.play();
		   
		   reSetData(p.getUniqueId(), tMgr);
		   
		  }
		  
		  if(Slot == 18) {
			  Tournament_InvCreator creator = new Tournament_InvCreator(plugin);
			  creator.openInv(p);
			  SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
			   manager.play();
		  }
		  
		  if(Slot == 4) {
			  
			  TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
			  
			  int rounds = tMgr.getRoundsQu();
			  rounds++;
			  if(rounds >= 10) {
				  rounds = 10;
				  SoundManager manager = new SoundManager(JSound.DRUM, p, 10.0F, 0.5F);
				  manager.play();
			  } else {
				  SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
				  manager.play();
			  }
			  
			  
			 
			  
			  
			  tMgr.setRoundsQu(rounds);
			  
			  
			  
			  ItemStack qualli = getItems.createItem(Material.SAPLING, 0, tMgr.getRoundsQu(), "§6Runden: §7" + tMgr.getRoundsQu(), null);
			  
			  e.getInventory().setItem(13, qualli);
			  reSetData(p.getUniqueId(), tMgr);
			 
		  }
		  
		  if(Slot == 22) {
			  TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
			  
			  int rounds = tMgr.getRoundsQu();
			  rounds--;
			  if(rounds <= 1) {
				  rounds = 1;
				  SoundManager manager = new SoundManager(JSound.DRUM, p, 10.0F, 0.5F);
				  manager.play();
			  } else {
				  SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
				  manager.play();
			  }
			  
			  
			 
			  
			  
			  tMgr.setRoundsQu(rounds);
			  
			 
			  ItemStack qualli = getItems.createItem(Material.SAPLING, 0, tMgr.getRoundsQu(), ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentRoundsItem").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix).replaceAll("%Rounds%", "" + tMgr.getRoundsQu())), null);
			  
			  e.getInventory().setItem(13, qualli);
			  reSetData(p.getUniqueId(), tMgr);
			 
		  }
		  
		  if(Slot == 7) {
			 
			  TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
            	 
			  int Secs = tMgr.getMaxTimeQuSecs();
			  int Mins = tMgr.getMaxTimeQuMins();
			  
			  
			  if(e.isShiftClick()) {
				  Secs = Secs+30;
				  while(Secs >= 60) {
					  Secs = Secs-60;
					  Mins++;
				  }
			  } else {
				  Secs = Secs+10;
				  while(Secs >= 60) {
					  Secs = Secs-60;
					  Mins++;
				  }
			  }
			  
			  if(Mins >= 10) {
				  Mins = 10;
				  Secs = 0;
				  SoundManager manager = new SoundManager(JSound.DRUM, p, 10.0F, 0.1F);
				  manager.play();
			  } else {
				  SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
				  manager.play();
			  }
			  
			  tMgr.setMaxTimeQuMins(Mins);
			  tMgr.setMaxTimeQuSecs(Secs);
			  
				 
              String secs = "";
   		      if(tMgr.getMaxTimeQuSecs() < 10) {
   			   secs = "0" + tMgr.getMaxTimeQuSecs();
   		      } else {
   			   secs = "" + tMgr.getMaxTimeQuSecs();
   		      }
   		      int mins = tMgr.getMaxTimeQuMins();
   		     
   		      ItemStack time = getItems.createItem(Material.WATCH, 0, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentmaxFightTimeQ").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix).replaceAll("%Mins%", "" + mins).replaceAll("%Secs%", "" + secs)), null);
   		   reSetData(p.getUniqueId(), tMgr);
   		      e.getInventory().setItem(16, time);
   		   Tournament_InvCreator creator1 = new Tournament_InvCreator(plugin);
   		   creator1.reGenerateInv(tMgr.getOwnerUUID());
             
      		 return;
		  }
		  
		  if(Slot == 25) {
			  
			  TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
            	 
			  int Secs = tMgr.getMaxTimeQuSecs();
			  int Mins = tMgr.getMaxTimeQuMins();
			  
			  
			  if(e.isShiftClick()) {
				  //10 - 30 = -20*-1 = 20*2 
				  //20 - 30 = -10*-1 = 10*2
				  Secs = Secs-30;
				  while(Secs < 0) {
					  if(Secs == -20) {
						  Secs = 40;
					  }
					  if(Secs == -10) {
						  Secs = 50;
					  }
					  Mins--;
					  if(Secs < 0) {
						  Secs = 0;
					  }
				  }
			  } else {
				  Secs = Secs-10;
				  while(Secs < 0) {
					  Secs = 50;
					  Mins--;
				  }
			  }
			  
			  if(Mins < 1) {
				  Mins = 1;
				  Secs = 0;
				  SoundManager manager = new SoundManager(JSound.DRUM, p, 10.0F, 0.1F);
				  manager.play();
			  } else {
				  SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
				  manager.play();
			  }
			  
			  tMgr.setMaxTimeQuSecs(Secs);
			  tMgr.setMaxTimeQuMins(Mins);
              	 
              
				 
              String secs = "";
   		      if(tMgr.getMaxTimeQuSecs() < 10) {
   			   secs = "0" + tMgr.getMaxTimeQuSecs();
   		      } else {
   			   secs = "" + tMgr.getMaxTimeQuSecs();
   		      }
   		      
   		      int mins = tMgr.getMaxTimeQuMins();
   		      ItemStack time = getItems.createItem(Material.WATCH, 0, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentmaxFightTimeQ").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix).replaceAll("%Mins%", "" + mins).replaceAll("%Secs%", secs)), null);
              
   		      e.getInventory().setItem(16, time);
   		   reSetData(p.getUniqueId(), tMgr);
   		   Tournament_InvCreator creator1 = new Tournament_InvCreator(plugin);
   		   creator1.reGenerateInv(tMgr.getOwnerUUID());
             
      		 return;
		  }
		  
		  
		 }
		}
	   }
	  }
	 }
	}
	
	@EventHandler
	public void onClick3(InventoryClickEvent e) {
	 if(e.getWhoClicked() instanceof Player) {
	  final Player p = (Player) e.getWhoClicked();	
	  if(e.getClickedInventory() != null) {
	   if(e.getInventory().getTitle().equalsIgnoreCase(plugin.msgs.getMsg("tournamentkOInvTitle"))) {
		e.setCancelled(true);
		if(e.getClickedInventory().getTitle().equalsIgnoreCase(plugin.msgs.getMsg("tournamentkOInvTitle"))) {
		 if(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
		  
		  int Slot = e.getSlot();
		  
		  if(Slot == 10) {
			 
			  TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
		   
		   
		   int fights = tMgr.getFightsKo();
		   if(fights == 1) {
			   tMgr.setFightsKo(3);
		   } else if(fights == 3) {
			   tMgr.setFightsKo(5);
		   } else if(fights == 5) {
			   tMgr.setFightsKo(1);
		   }
		   
		   
		   fights = tMgr.getFightsKo();//
		   ItemStack rounds = getItems.createItem(Material.NETHER_STAR, 0, tMgr.getFightsKo(), ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentFightsPerRound").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix).replaceAll("%Fights%", "" + tMgr.getFightsKo())), null);
		   
		   e.getInventory().setItem(10, rounds);
		   
		   SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
		   manager.play();
		   
		   reSetData(p.getUniqueId(), tMgr);
		   
		  }
		  
		  if(Slot == 18) {
			  Tournament_InvCreator creator = new Tournament_InvCreator(plugin);
			  creator.openInv(p);
			  SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
			   manager.play();
		  }
		  
		  
		  
		  if(Slot == 7) {
			  
			  TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
			  
			  
			  int Secs = tMgr.getMaxTimeKoSecs();
			  int Mins = tMgr.getMaxTimeKoMins();
			  
			  
			  if(e.isShiftClick()) {
				  Secs = Secs+30;
				  while(Secs >= 60) {
					  Secs = Secs-60;
					  Mins++;
				  }
			  } else {
				  Secs = Secs+10;
				  while(Secs >= 60) {
					  Secs = Secs-60;
					  Mins++;
				  }
			  }
			  
			  if(Mins >= 10) {
				  Mins = 10;
				  Secs = 0;
				  SoundManager manager = new SoundManager(JSound.DRUM, p, 10.0F, 0.1F);
				  manager.play();
			  } else {
				  SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
				  manager.play();
			  }
			  
			  tMgr.setMaxTimeKoSecs(Secs);
			  tMgr.setMaxTimeKoMins(Mins);
              	 
              
				 
              String secs = "";
   		      if(tMgr.getMaxTimeKoSecs() < 10) {
   			   secs = "0" + tMgr.getMaxTimeKoSecs();
   		      } else {
   			   secs = "" + tMgr.getMaxTimeKoSecs();
   		      } 
   		     
   		      ItemStack time = getItems.createItem(Material.WATCH, 0, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentmaxFightTimeQ").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix).replaceAll("%Mins%", "" +tMgr.getMaxTimeKoMins()).replaceAll("%Secs%", secs)), null);
              
   		      e.getInventory().setItem(16, time);
				 
   		   reSetData(p.getUniqueId(), tMgr);
   		      
   		   Tournament_InvCreator creator1 = new Tournament_InvCreator(plugin);
      	  creator1.reGenerateInv(tMgr.getOwnerUUID());
      		 return;
		  }
		  
		  if(Slot == 25) {
			  
			  TournamentManager tMgr = plugin.tournaments.get(p.getUniqueId());
			  
			  int Secs = tMgr.getMaxTimeKoSecs();
			  int Mins = tMgr.getMaxTimeKoMins();
			  
			  
			  if(e.isShiftClick()) {
				  //10 - 30 = -20*-1 = 20*2 
				  //20 - 30 = -10*-1 = 10*2
				  Secs = Secs-30;
				  while(Secs < 0) {
					  if(Secs == -20) {
						  Secs = 40;
					  }
					  if(Secs == -10) {
						  Secs = 50;
					  }
					  Mins--;
					  if(Secs < 0) {
						  Secs = 0;
					  }
				  }
			  } else {
				  Secs = Secs-10;
				  while(Secs < 0) {
					  Secs = 50;
					  Mins--;
				  }
			  }
			  
			  if(Mins < 1) {
				  Mins = 1;
				  Secs = 0;
				  SoundManager manager = new SoundManager(JSound.DRUM, p, 10.0F, 0.1F);
				  manager.play();
			  } else {
				  SoundManager manager = new SoundManager(JSound.CLICK, p, 10.0F, 1.0F);
				  manager.play();
			  }
			  
			  tMgr.setMaxTimeKoSecs(Secs);
			  tMgr.setMaxTimeKoMins(Mins);
              	 
             
				 
              String secs = "";
   		      if(tMgr.getMaxTimeKoSecs() < 10) {
   			   secs = "0" + tMgr.getMaxTimeKoSecs();
   		      } else {
   			   secs = "" + tMgr.getMaxTimeKoSecs();
   		      }
   		      ItemStack time = getItems.createItem(Material.WATCH, 0, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentmaxFightTimeQ").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix).replaceAll("%Mins%", "" +tMgr.getMaxTimeKoMins()).replaceAll("%Secs%", secs)), null);
   		      reSetData(p.getUniqueId(), tMgr);
   		      e.getInventory().setItem(16, time);
   		      Tournament_InvCreator creator1 = new Tournament_InvCreator(plugin);
      	      creator1.reGenerateInv(tMgr.getOwnerUUID());
             
      		 return;
		  }
		  
		  
		 }
		}
	   }
	  }
	 }
	}
	
	@EventHandler
	public void onClickInfoInv(InventoryClickEvent e) {
	 if(e.getWhoClicked() != null && e.getInventory() != null) {
	   if(e.getInventory().getName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("tournamentInfosInvName").replaceAll("%Prefix%", plugin.prefix).replaceAll("%TPrefix%", plugin.tournamentPrefix)))) {
		e.setCancelled(true);
		
	   }
	 }
	}
	
	private void reSetData(UUID tournament, TournamentManager tMgr) {
		while(plugin.tournaments.containsKey(tournament)) plugin.tournaments.remove(tournament);
		plugin.tournaments.put(tournament, tMgr);
	}
}
