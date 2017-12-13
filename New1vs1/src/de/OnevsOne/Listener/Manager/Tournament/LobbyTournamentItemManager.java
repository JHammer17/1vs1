package de.OnevsOne.Listener.Manager.Tournament;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.OnevsOne.main;
import de.OnevsOne.Commands.VariableCommands.Manager.CommandTrigger1vs1;

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.getItems;
import de.OnevsOne.Methods.Tournament.TournamentManager;
import de.OnevsOne.States.PlayerState;

public class LobbyTournamentItemManager implements Listener {

	private main plugin;

	public LobbyTournamentItemManager(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		
		if(plugin.isInOneVsOnePlayers(e.getPlayer().getUniqueId()) && plugin.getOneVsOnePlayer(e.getPlayer()).getpState() == PlayerState.InLobby) {
			ItemStack item = e.getItem();
			if(item != null 
			   && item.getTypeId() == plugin.TournamentItemID
			   && item.hasItemMeta() 
			   && item.getItemMeta().hasDisplayName() 
			   && item.getItemMeta().getDisplayName().equalsIgnoreCase("§6Turniere")) {
				
				Player p = e.getPlayer();
				
				if(plugin.getOneVsOnePlayer(p).getPlayertournament() != null) {
				 if(plugin.tournaments.containsKey(plugin.getOneVsOnePlayer(p).getPlayertournament()) && plugin.tournaments.get(plugin.getOneVsOnePlayer(p).getPlayertournament()).getOwnerUUID().equals(p.getUniqueId())){
					 Bukkit.getServer().getPluginManager().callEvent(new CommandTrigger1vs1(p, null, "create"));
						return;
				 }
					Bukkit.getServer().getPluginManager().callEvent(new CommandTrigger1vs1(p, null, "t"));
					return;
				}
				
				if(plugin.tournaments.containsKey(p.getUniqueId())) {
					Bukkit.getServer().getPluginManager().callEvent(new CommandTrigger1vs1(p, null, "create"));
					return;
				}
				
				
				Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "Turniere");
				
				ItemStack create;
				
				if(p.hasPermission("1vs1.command.createT") || p.hasPermission("1vs1.Premium") || p.hasPermission("1vs1.*")) {
					create = getItems.createItem(Material.EMERALD, 0, 1, "§6Turnier erstellen", "§7Rechtsklick, um ein Turnier zu erstellen.");
				} else {
					create = getItems.createItem(Material.DIAMOND, 0, 1, "§6Turnier erstellen", "§cDu hast nicht die benötigten Berechtigungen, \n§cum ein Turnier erstellen zu können!");
				}
				
				inv.setItem(1, create);
				
				ItemStack join = getItems.createItem(Material.PAPER, 0, 1, "§6Turnier beitreten", "§7Rechtsklick, um einen Turnier bei zu treten!");
				
				inv.setItem(3, join);
				
				p.openInventory(inv);
				
			}
		}
		
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		
		if(e.getInventory() != null && e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
		 
			
			if(e.getInventory().getTitle().equalsIgnoreCase("Turniere")) {
				e.setCancelled(true);
				
				if(e.getCurrentItem() != null && !e.getClickedInventory().getTitle().equalsIgnoreCase("Turniere")) return;
				if(e.getSlot() == 1) {
					if(p.hasPermission("1vs1.command.createT") || p.hasPermission("1vs1.Premium") || p.hasPermission("1vs1.*")) {
						CommandTrigger1vs1 trigger = new CommandTrigger1vs1(p, null, "create");
						Bukkit.getServer().getPluginManager().callEvent(trigger);
					}
				} else if(e.getSlot() == 3) {
					openTournamentListInv(p);
				}
			}
		}
	}
	
	
	HashMap<UUID, HashMap<Integer, UUID>> tournamentInv = new HashMap<>();
	
	@SuppressWarnings("unused")
	public void openTournamentListInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*6, "Turnier beitreten");
		
		ArrayList<TournamentManager> tournaments = new ArrayList<>();
		
		ArrayList<TournamentManager> starting = new ArrayList<>();
		ArrayList<TournamentManager> ingame = new ArrayList<>();
		
		for(TournamentManager mgr : plugin.tournaments.values()) {
			tournaments.add(mgr);
			if(mgr.isStarted() && mgr.isOpened()) ingame.add(mgr);
			if(!mgr.isStarted() && mgr.isOpened()) starting.add(mgr);
		}
		
		
		ItemStack empty = getItems.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§c", null);
		
		int tournament = 0;
		int tSlot = 0;
		
		HashMap<Integer, UUID> tournamentList = new HashMap<>(); 
		while(tournament < 9) {
			if(!(starting.size() > (tournament))) break;
			TournamentManager mgr = starting.get(tournament);
			
			
			
			if(!mgr.getPassword().equalsIgnoreCase("")) {
				tournament++;
				continue;
			}
			
			tournamentList.put(tSlot, mgr.getOwnerUUID());
			
			/*String Lore = "§7Turnierleiter: §6" + states.getOwner() + "\n"
		    + "§7Kit: §6" + states.getKit() + "\n"
		    + "§7Spieleranzahl: §6" + states.getPlayerList() + "\n"
		    + "§7--------------------------------\n"//32
		    + "§7Qualifikation:\n"
		    + "§7*Maximale Kampfzeit: §6" + states.getQTimeMins() + ":" + secs2 + "\n"
			+ "§7*Runden: §6" + states.getRoundsQualli() + "\n"
		    + "§7*Spiele: §6" + states.getFightsQualli() + "\n"
			+ "§7--------------------------------\n"//32
		    + "§7K.O.-Phase:\n"
		    + "§7*Spiele: §6" +  states.getFightsKO() + "\n"
			+ "§7*Maximale Kampfzeit: §6" +  states.getTimeMins() + ":" + secs1 + "\n"
			+ "§7--------------------------------\n" //32
			+ "§7Runde: " + states.getRoundName() + "\n"
			+ "§7Verbleibende Spieler: §6" + states.getRemainingPlayers().size(); */
			
			if(mgr == null) {
				tournament++;
				continue;
			}
			
			String secs1 = "";
			if(mgr.getMaxTimeKoSecs() < 10) {
				secs1 = "0" + mgr.getMaxTimeKoSecs();
			} else {
				secs1 = "" + mgr.getMaxTimeKoSecs();
			}
			
			String secs2 = "";
			if(mgr.getMaxTimeQuSecs() < 10) {
				secs2 = "0" + mgr.getMaxTimeQuSecs();
			} else {
				secs2 = "" + mgr.getMaxTimeQuSecs();
			}
			

			String Lore = MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentInfoLore")
					 .replaceAll("%Owner%", mgr.getName())
					 .replaceAll("%Kit%", mgr.getKit())
					 .replaceAll("%PlayersT%", "" + mgr.getPlayerList().size())
					 .replaceAll("%QMins%", "" + mgr.getMaxTimeQuMins())
					 .replaceAll("%QSecs%", secs2)
					 .replaceAll("%RoundsQ%", "" + mgr.getRoundsQu())
					 .replaceAll("%GamesQ%", "" + mgr.getFightsQu())
					 .replaceAll("%GamesKO%", "" + mgr.getFightsKo())
					 .replaceAll("%KOMins%", "" + mgr.getMaxTimeKoMins())
					 .replaceAll("%KOSecs%", "" + secs1)
					 .replaceAll("%Round%", "" + mgr.getRound())
					 .replaceAll("%RemainingPlayers%", "" + mgr.getRemainingPlayers()));
			
			
			inv.setItem(tSlot, getItems.createItem(Material.STAINED_CLAY, 5, 1, "§6Turnier von " + mgr.getName(), Lore)); //TODO
			tournament++;
			tSlot++;
		}
		
		tournament = 18;
		tSlot = 18;
		for(TournamentManager mgr : ingame) {
			if(tournament > 53) break;
			
			
			if(mgr == null) {
				tournament++;
				continue;
			}
			
			String secs1 = "";
			if(mgr.getMaxTimeKoSecs() < 10) {
				secs1 = "0" + mgr.getMaxTimeKoSecs();
			} else {
				secs1 = "" + mgr.getMaxTimeKoSecs();
			}
			
			String secs2 = "";
			if(mgr.getMaxTimeQuSecs() < 10) {
				secs2 = "0" + mgr.getMaxTimeQuSecs();
			} else {
				secs2 = "" + mgr.getMaxTimeQuSecs();
			}
			
			String Lore = MessageReplacer.replaceT(plugin.msgs.getMsg("tournamentInfoLore")
					 .replaceAll("%Owner%", mgr.getName())
					 .replaceAll("%Kit%", mgr.getKit())
					 .replaceAll("%PlayersT%", "" + mgr.getPlayerList().size())
					 .replaceAll("%QMins%", "" + mgr.getMaxTimeQuMins())
					 .replaceAll("%QSecs%", secs2)
					 .replaceAll("%RoundsQ%", "" + mgr.getRoundsQu())
					 .replaceAll("%GamesQ%", "" + mgr.getFightsQu())
					 .replaceAll("%GamesKO%", "" + mgr.getFightsKo())
					 .replaceAll("%KOMins%", "" + mgr.getMaxTimeKoMins())
					 .replaceAll("%KOSecs%", "" + secs1)
					 .replaceAll("%Round%", "" + mgr.getRound())
					 .replaceAll("%RemainingPlayers%", "" + mgr.getRemainingPlayers()));
			
			
			
			tournamentList.put(tSlot, mgr.getOwnerUUID());
			inv.setItem(tSlot, getItems.createItem(Material.STAINED_CLAY, 14, 1, "§6Turnier von " + mgr.getName(), Lore));//TODO MSG
			tournament++;
			tSlot++;
		}
		
		while(tournamentInv.containsKey(p.getUniqueId())) tournamentInv.remove(p.getUniqueId());
		tournamentInv.put(p.getUniqueId(), tournamentList);
		
		
		for(int i = 9; i < 18; i++) inv.setItem(i, empty);
		
		p.openInventory(inv);
		
	}
	
	@EventHandler
	public void onClick2(InventoryClickEvent e) {
		if(e.getClickedInventory() != null && e.getCurrentItem() != null && e.getInventory() != null && e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			
			if(e.getClickedInventory().getTitle().equalsIgnoreCase("Turnier beitreten")) {
				e.setCancelled(true);
				if(tournamentInv.containsKey(p.getUniqueId())) {
					HashMap<Integer, UUID> tournamentList = tournamentInv.get(p.getUniqueId());
					
					if(tournamentList.containsKey(e.getSlot())) {
						if(plugin.tournaments.containsKey(tournamentList.get(e.getSlot()))) {
							TournamentManager mgr = plugin.tournaments.get(tournamentList.get(e.getSlot()));
							
							String[] ali = {mgr.getName()};
							CommandTrigger1vs1 trigger = new CommandTrigger1vs1(p, ali, "join");
							
							if(!mgr.isStarted()) Bukkit.getServer().getPluginManager().callEvent(trigger);
							p.closeInventory();
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if(e.getInventory().getTitle().equalsIgnoreCase("Turnier beitreten")) {
			while(tournamentInv.containsKey(e.getPlayer())) tournamentInv.remove(e.getPlayer());
		}
	}
	
}
