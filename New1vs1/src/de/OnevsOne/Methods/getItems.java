package de.OnevsOne.Methods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import de.OnevsOne.main;
import de.OnevsOne.Methods.Tournament.TournamentManager;
import de.OnevsOne.States.PlayerState;
import net.md_5.bungee.api.ChatColor;

/**
 * Der Code ist von JHammer
 *
 * 05.05.2016 um 22:04:55 Uhr
 */
public class getItems {
	
	private static main plugin;

	@SuppressWarnings("static-access")
	public getItems(main plugin) {
		this.plugin = plugin;
	}

	public static void getLobbyItems(final Player p, boolean clear) {
		if(clear) p.getInventory().clear();
		
		ItemStack Sword = createItem(plugin.ChallangerItemID, 0, 1, plugin.msgs.getMsg("challangeItemLobbyName"), plugin.msgs.getMsg("challangeItemLobbyDesc"));
		ItemMeta SwordMeta = Sword.getItemMeta();
		SwordMeta.spigot().setUnbreakable(true);
		Sword.setItemMeta(SwordMeta);
		
				
				
		
		ItemStack Spectate = createItem(plugin.SpectatorItemID, 0, 1, plugin.msgs.getMsg("specateItemLobbyName"), plugin.msgs.getMsg("specateItemLobbyDesc"));
		ItemStack Prefs = createItem(plugin.SettingsItemID, 0, 1, plugin.msgs.getMsg("settingsItemLobbyName"), plugin.msgs.getMsg("settingsItemLobbyDesc"));
		ItemStack Leave = createItem(plugin.LeaveItemID, 0, 1, plugin.msgs.getMsg("leaveItemLobbyName"), plugin.msgs.getMsg("leaveItemLobbyDesc"));
		
		ItemStack cake = createItem(Material.CAKE, 0, 1, "§6JHammer17's Geburtstagskuchen ^^", "§7JHammer17 hat am 26.01 Geburtstag ;)");
		
		SimpleDateFormat format = new SimpleDateFormat("dd.MM");
		if(format.format(new Date()).equals("26.01")) p.getInventory().setItem(22, cake);
		
		
		ItemStack rankItem = createItem(Material.STAINED_GLASS, 0, 1, "§7Dein Rang:§6 - (0)", null);
		
		
		
		
		
		if(plugin.RankItemSlot > 0) {
			int useSlot = plugin.RankItemSlot;
			useSlot--;
			p.getInventory().setItem(useSlot, rankItem);
		}
		if(plugin.saveStats) {
			Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
				
				@Override
				public void run() {
					
					ItemStack rankItem = createItem(Material.STAINED_GLASS, 0, 1, "§6Lade...", null);
					if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby) {
						if(plugin.RankItemSlot > 0) {
							int useSlot = plugin.RankItemSlot;
							useSlot--;
							p.getInventory().setItem(useSlot, rankItem);
						}
					}
					
					double minRank9 = plugin.rank9;
					double minRank8 = plugin.rank8;
					double minRank7 = plugin.rank7;
					double minRank6 = plugin.rank6;
					double minRank5 = plugin.rank5;
					double minRank4 = plugin.rank4;
					double minRank3 = plugin.rank3;
					double minRank2 = plugin.rank2;
					double minRank1 = plugin.rank1;
					
					
					double userRank = plugin.getDBMgr().getRankPoints(p.getUniqueId());
					
					rankItem = createItem(Material.STAINED_GLASS, 0, 1, "§6Lade...", null);
					
				
					
					if(isBetween(minRank1, Integer.MAX_VALUE, userRank)) {
						rankItem = createItem(Material.DIAMOND_BLOCK, 1, 1, "§7Dein Rang:§6 Diamant (10)", null);
					} else  if(isBetween(minRank2, minRank1, userRank)) {
						rankItem = createItem(Material.GOLD_BLOCK, 1, 1, "§7Dein Rang:§6 Gold (9)", null);
					} else  if(isBetween(minRank3, minRank2, userRank)) {
						rankItem = createItem(Material.IRON_BLOCK, 1, 1, "§7Dein Rang:§6 Eisen (8)", null);
					} else  if(isBetween(minRank4, minRank3, userRank)) {
						rankItem = createItem(Material.SEA_LANTERN, 1, 1, "§7Dein Rang:§6 Prismarin (7)", null);
					} else  if(isBetween(minRank5, minRank4, userRank)) {
						rankItem = createItem(Material.STAINED_CLAY, 15, 1, "§7Dein Rang:§6 Schwarz (6)", null);
					} else  if(isBetween(minRank6, minRank5, userRank)) {
						rankItem = createItem(Material.STAINED_CLAY, 14, 1, "§7Dein Rang:§6 Rot (5)", null);
					} else  if(isBetween(minRank7, minRank6, userRank)) {
						rankItem = createItem(Material.STAINED_CLAY, 11, 1, "§7Dein Rang:§6 Blau (4)", null);
					} else if(isBetween(minRank8, minRank7, userRank)) {
						rankItem = createItem(Material.STAINED_CLAY, 5, 1, "§7Dein Rang:§6 Grün (3)", null);
					} else if(isBetween(minRank9, minRank8, userRank)) {
						rankItem = createItem(Material.STAINED_CLAY, 7, 1, "§7Dein Rang:§6 Grau (2)", null);
					} else if(isBetween(Integer.MIN_VALUE, minRank9, userRank)) {
						rankItem = createItem(Material.STAINED_CLAY, 0, 1, "§7Dein Rang:§6 Weiß (1)", null);
					} 
					
					
					if(plugin.getOneVsOnePlayer(p).getpState() == PlayerState.InLobby) {
						if(plugin.RankItemSlot > 0) {
							int useSlot = plugin.RankItemSlot;
							useSlot--;
							p.getInventory().setItem(useSlot, rankItem);
						}
					}
					
					
					
				}
			});
		}
		
		
		
		ItemStack book = createItem(Material.WRITTEN_BOOK, 0, 1, "§61vs1", null);
		BookMeta meta = (BookMeta) book.getItemMeta();
		meta.setAuthor(p.getName());
		
		
		for(String page : plugin.book.values()) {
			
			if(page == null || page.equalsIgnoreCase("")) break;
			meta.addPage(page);
		}
		
		/*meta.addPage("\n\n\n\n§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n§4         ﴾ 1vs1 ﴿ \n   §0   by JHammer17\n§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●");
		
		meta.addPage("§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n       §cSpielverlauf\n§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n\n§0» Das Ziel ist es, seine(n) Gegner, welche(r) das gleiche Equipment haben zu besiegen.\n\n» Das funktioniert mit \n§4selbsterstellten Kits.");
		meta.addPage("§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n       §cSpielverlauf\n§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n\n§0» In den Kämpfen können das eigene oder die Kits von anderen Spielern genutzt werden.\n\n» Im Kit-Bereich kannst du dein §2eigens Kit §0erstellen oder bearbeiten");
		
		meta.addPage("§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n      §cHerausfordern\n§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n\n§0» Um einen Spieler herauszufordern oder eine Herausforderung anzunehmen, musst du deinen Gegner mit dem Diamantschwert schlagen.");
		meta.addPage("§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n      §cHerausfordern\n§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n\n§0» Um einen zufälligen Gegner zu bekommen, musst du den Warteschlangen Zombie schlagen. Wenn du diesen Rechtsklickst kannst du Einstellungen für die Suche vornehmen.");
		
		meta.addPage("§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n      §cTurniersystem\n§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n\n§0» Mit dem Befehl \n§9/create§0 können Spieler ein Turnier erstellen und einige Einstellungen vornehmen.\n\n» Bearbeiten kannst du diese mit:\n §9/create §0oder §9/modify");
		meta.addPage("§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n      §cTurniersystem\n§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n\n§0» Einen Turnier beitreten kannst du mit §9/join <Turnierowner> [Passwort]§0\n\n » Mit §9/start [Sekunden] §0kann der Turnierersteller das Turnier starten.");
		meta.addPage("§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n      §cTurniersystem\n§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n\n§0» Das Turnier startet mit einer qualifikations Phase. Nach dieser folgt eine §4K.O.-Phase §0bei der die Spieler durch eine Gesamtniederlage ausscheiden.");
		meta.addPage("§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n      §cTurniersystem\n§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n\n§0» Mit dem Befehl §9/t §0oder den Netherstern kannst du alle Turnierinformationen ansehen.\n\n» Mit §9/tLeave §0oder §9/tL §0verlässt du das Turnier.");
		
		meta.addPage("§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n         §cKitbereich\n§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n\n§0» Am §cSchwarzhändler §0\nim Kitbereich kannst du Items modifizieren.\n\n» Am §2Villager §0kannst du Einstellungen für dein(e) Kit(s) vornehmen.");
		
		meta.addPage("§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n          §cSonstiges\n§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n\n§0» Du kannst andere Spieler beim Kämpfen beobachten mit §9/spec <Name>. §0Mit den grauen Farbstoff gelangst du wieder in die Lobby zurück.");
		meta.addPage("§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n          §cSonstiges\n§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n\n§0» Mit dem Redstone Comparator in der Hotbar kannst du Einstellungen über Kits, Maps und die Warteschlange vornehmen.");
		meta.addPage("§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n          §cSonstiges\n§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n\n§0» Dir wird automatisch ein Rang von 1-10 zugeteilt. Solltest du §2gewinnen§0, so wird sich dein Wert erhöhen, solltest du dennoch verlieren, so\n §4verringert §0sich dein Wert und ggf. dein Rang.");
		
		meta.addPage("§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n     §cBefehlsammlung\n§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n\n§0» /surrender\n» /spec\n» /create\n» /modify\n» /start [Zeit]\n» /kit <Name>:[SubID]\n» /join <Name>\n» /endmatch\n» /stats");
		meta.addPage("§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n     §cBefehlsammlung\n§3●●●●●●●●●●●●●●●●●●●●●●●●●●●●●\n\n§0» /team leave\n» /team invite <Name>\n» /team join <Name>\n» /team info/list\n» /team accept <Name>\n» /team kick <Name>");
		*/
		
		book.setItemMeta(meta);
		
		
		
		
		
		
		
		
		
		
		
		
		if(plugin.ChallangerItemSlot > 0) {
			int useSlot = plugin.ChallangerItemSlot;
			useSlot--;
			p.getInventory().setItem(useSlot, Sword);
		}
		if(plugin.SpectatorItemSlot > 0) {
			int useSlot = plugin.SpectatorItemSlot;
			useSlot--;
			p.getInventory().setItem(useSlot, Spectate);
		}
		if(plugin.SettingsItemSlot > 0) {
			int useSlot = plugin.SettingsItemSlot;
			useSlot--;
			p.getInventory().setItem(useSlot, Prefs);
		}
		if(plugin.LeaveItemSlot > 0) {
			int useSlot = plugin.LeaveItemSlot;
			useSlot--;
			p.getInventory().setItem(useSlot, Leave);
		}
		if(plugin.TournamentItemSlot > 0) {
			int useSlot = plugin.TournamentItemSlot;
			useSlot--;
			p.getInventory().setItem(useSlot, createItem(plugin.TournamentItemID, 0, 1, "§6Turniere", "§7Rechtsklick, zum benutzen"));
		}
		if(plugin.BookItemSlot > 0) {
			int useSlot = plugin.BookItemSlot;
			useSlot--;
			p.getInventory().setItem(useSlot, book);
		}
	}
	
	public static void getSpectatorItems(Player p, boolean clear) {
		if(clear) p.getInventory().clear();
		
		
		if(plugin.getOneVsOnePlayer(p).getPlayertournament() != null) {
			ItemStack Spectate = createItem(plugin.SpectatorItemID, 0, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("specateItemLobbyName")), ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("specateItemLobbyDesc")));
			ItemStack infoItem = createItem(Material.NETHER_STAR, 0, 1, ChatColor.translateAlternateColorCodes('&', "§6Turnierinfos §7(/t)"), ChatColor.translateAlternateColorCodes('&', "§7Rechtsklick um das Info-Inventar zu öffnen!"));
			ItemStack leaveItem = createItem(Material.INK_SACK, 8, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("leaveSpec")), ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("leaveSpecLore")));
			
			ItemStack firstPlayer = createItem(Material.SKULL_ITEM, 3, 1, "§6-", null); 
			ItemStack secondPlayer = createItem(Material.SKULL_ITEM, 3, 1, "§6-", null);
			
			if(plugin.getOneVsOnePlayer(p).getSpecator() != null && !plugin.getOneVsOnePlayer(p).getSpecator().equalsIgnoreCase("") && plugin.ArenaPlayersP1.containsKey(plugin.getOneVsOnePlayer(p).getSpecator()) && plugin.ArenaPlayersP2.containsKey(plugin.getOneVsOnePlayer(p).getSpecator())) {
				if(plugin.ArenaPlayersP1.get(plugin.getOneVsOnePlayer(p).getSpecator()).size() >= 1 && plugin.ArenaPlayersP2.get(plugin.getOneVsOnePlayer(p).getSpecator()).size() >= 1) {
					String p1 = plugin.ArenaPlayersP1.get(plugin.getOneVsOnePlayer(p).getSpecator()).get(0).getDisplayName();
					String p2 = plugin.ArenaPlayersP2.get(plugin.getOneVsOnePlayer(p).getSpecator()).get(0).getDisplayName();
					
					firstPlayer = createItem(Material.SKULL_ITEM, 3, 1, "§6" + p1, null); 
					secondPlayer = createItem(Material.SKULL_ITEM, 3, 1, "§6" + p2, null);
					
					SkullMeta firstMeta = (SkullMeta) firstPlayer.getItemMeta();
					SkullMeta secondMeta = (SkullMeta) firstPlayer.getItemMeta();
				
					firstMeta.setOwner(p1);
					secondMeta.setOwner(p2);

					firstMeta.setDisplayName("§6" + p1);
					secondMeta.setDisplayName("§6" + p2);
					
					firstPlayer.setItemMeta(firstMeta);
					secondPlayer.setItemMeta(secondMeta);
					
				}
			}
			
			
			
			TournamentManager tMgr = plugin.tournaments.get(plugin.getOneVsOnePlayer(p).getPlayertournament());
			
			if(tMgr.isOut(p.getUniqueId())) {
				p.getInventory().setItem(8, leaveItem);
				p.getInventory().setItem(7, Spectate);
				p.getInventory().setItem(6, infoItem);
				p.getInventory().setItem(0, firstPlayer);
				p.getInventory().setItem(1, secondPlayer);
			} else {
				p.getInventory().setItem(8, infoItem);
				p.getInventory().setItem(7, Spectate);
				p.getInventory().setItem(0, firstPlayer);
				p.getInventory().setItem(1, secondPlayer);
			}
			
		} else {
			ItemStack Spectate = createItem(plugin.SpectatorItemID, 0, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("specateItemLobbyName")), ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("specateItemLobbyDesc")));
			ItemStack leaveItem = createItem(Material.INK_SACK, 8, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("leaveSpec")), ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("leaveSpecLore")));
			
			ItemStack firstPlayer = createItem(Material.SKULL_ITEM, 3, 1, "§6-", null); 
			ItemStack secondPlayer = createItem(Material.SKULL_ITEM, 3, 1, "§6-", null);
			
			if(plugin.ArenaPlayersP1.get(plugin.getOneVsOnePlayer(p).getSpecator()).size() >= 1 && plugin.ArenaPlayersP2.get(plugin.getOneVsOnePlayer(p).getSpecator()).size() >= 1) {
				String p1 = plugin.ArenaPlayersP1.get(plugin.getOneVsOnePlayer(p).getSpecator()).get(0).getDisplayName();
				String p2 = plugin.ArenaPlayersP2.get(plugin.getOneVsOnePlayer(p).getSpecator()).get(0).getDisplayName();
				
				firstPlayer = createItem(Material.SKULL_ITEM, 3, 1, "§6" + p1, null); 
				secondPlayer = createItem(Material.SKULL_ITEM, 3, 1, "§6" + p2, null);
				
				SkullMeta firstMeta = (SkullMeta) firstPlayer.getItemMeta();
				SkullMeta secondMeta = (SkullMeta) firstPlayer.getItemMeta();
			
				firstMeta.setDisplayName("§6" + p1);
				secondMeta.setDisplayName("§6" + p2);
				
				
				firstMeta.setOwner(p1);
				secondMeta.setOwner(p2);

				firstPlayer.setItemMeta(firstMeta);
				secondPlayer.setItemMeta(secondMeta);
			}
			
			
			
			
			
			
			p.getInventory().setItem(8, leaveItem);
			p.getInventory().setItem(7, Spectate);
			p.getInventory().setItem(0, firstPlayer);
			p.getInventory().setItem(1, secondPlayer);
		}
		
		
		
	}
	
	public static ItemStack createItem(Material mat, int SubID, int Amount, String Display, String Lore) {
		ItemStack itemstack = new ItemStack(mat);
		ItemMeta itemMeta = itemstack.getItemMeta();
		ArrayList<String> LoreList = new ArrayList<String>();
		if(Display != null) {
			itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Display));
		}
		
		if(Lore != null) {
			String[] Lines = ChatColor.translateAlternateColorCodes('&', Lore).split("\n");
			for(int i = 0; i < Lines.length;i++) {
				LoreList.add(Lines[i]);
			}
		}
		
		itemMeta.setLore(LoreList);
		itemstack.setItemMeta(itemMeta);
		itemstack.setAmount(Amount);
		itemstack.setDurability((short) SubID);
		return itemstack;
		
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack createItem(int id, int SubID, int Amount, String Display, String Lore) {
		ItemStack itemstack = new ItemStack(id);
		ItemMeta itemMeta = itemstack.getItemMeta();
		ArrayList<String> LoreList = new ArrayList<String>();
		if(Display != null) {
			itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Display));
		}
		
		if(Lore != null) {
			String[] Lines = ChatColor.translateAlternateColorCodes('&', Lore).split("\n");
			for(int i = 0; i < Lines.length;i++) {
				LoreList.add(Lines[i]);
			}
		}
		
		itemMeta.setLore(LoreList);
		itemstack.setItemMeta(itemMeta);
		itemstack.setAmount(Amount);
		itemstack.setDurability((short) SubID);
		return itemstack;
		
	}
	 
	public static ItemStack applyEnchant(ItemStack stack, Enchantment ench, int level) {
		if(level <= 0) {
			stack.removeEnchantment(ench);
			return stack;
		}
		stack.addUnsafeEnchantment(ench, level);
		
		return stack;
	}
	
	public static ItemStack applyEnchant(ItemStack stack, Enchantment ench, int level, int maxlvl) {
		if(level <= 0) {
			stack.removeEnchantment(ench);
			return stack;
		}
		if(level > maxlvl) {
			level = maxlvl;
		}
		stack.addUnsafeEnchantment(ench, level);
		
		return stack;
	}
	
	public static int getEnchLevel(ItemStack stack, Enchantment ench) {
		if(stack.hasItemMeta()) {
			if(stack.getItemMeta().hasEnchants()) {
				if(stack.getItemMeta().hasEnchant(ench)) {
					return stack.getEnchantmentLevel(ench);
				}
			}
		}
		return 0;
	}
	
	private static boolean isBetween(double min, double max, double check) {
		return ((min <= check) && (max >= check)) ? true : false;
	}
	
}
