package de.OnevsOne.Methods.Queue;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.OnevsOne.main;
import de.OnevsOne.Listener.Manager.Preferences_Manager;
import de.OnevsOne.Methods.getItems;
import de.OnevsOne.States.PlayerBestOfsPrefs;
import de.OnevsOne.States.PlayerPrefs;
import de.OnevsOne.States.PlayerQuequePrefs;
import net.md_5.bungee.api.ChatColor;

/**
 * Der Code ist von JHammer
 *
 * 27.05.2016 um 15:58:54 Uhr
 */
public class QuequePrefsMethods {

	private static main plugin;
	
	@SuppressWarnings("static-access")
	public QuequePrefsMethods(main plugin) {
		this.plugin = plugin;
	}
	
	private static ItemStack getOwnKit() {
		ItemStack ownKit = new ItemStack(Material.MINECART);
		ItemMeta ownKitMeta = ownKit.getItemMeta();
		
		ownKitMeta.setDisplayName(plugin.msgs.getMsg("quequeOwnKitSettingName"));
		
		ArrayList<String> Lore = new ArrayList<String>();
		String[] Lines = plugin.msgs.getMsg("quequeOwnKitSettingDesc").split("\n");
		for(int i = 0; i < Lines.length;i++) {
			Lore.add(Lines[i]);
		}
		ownKitMeta.setLore(Lore);
		
		ownKit.setItemMeta(ownKitMeta);
		return ownKit;
	}
	
    private static ItemStack getEnemieKit() {

		ItemStack enemieKit = new ItemStack(Material.POWERED_MINECART);
		ItemMeta enemieKitMeta = enemieKit.getItemMeta();
		
		enemieKitMeta.setDisplayName(plugin.msgs.getMsg("quequeEnemieKitSettingName"));
		
		ArrayList<String> Lore = new ArrayList<String>();
		String[] Lines = plugin.msgs.getMsg("quequeEnemieKitSettingDesc").split("\n");
		for(int i = 0; i < Lines.length;i++) {
			Lore.add(Lines[i]);
		}
		enemieKitMeta.setLore(Lore);
		
		enemieKit.setItemMeta(enemieKitMeta);
		return enemieKit;
	}
	
    private static ItemStack getRandomKit() {
		ItemStack randomKit = new ItemStack(Material.EXPLOSIVE_MINECART);
		ItemMeta randomKitMeta = randomKit.getItemMeta();
		
		randomKitMeta.setDisplayName(plugin.msgs.getMsg("quequeRandomKitSettingName"));
		
		ArrayList<String> Lore = new ArrayList<String>();
		String[] Lines = plugin.msgs.getMsg("quequeRandomKitSettingDesc").split("\n");
		for(int i = 0; i < Lines.length;i++) {
			Lore.add(Lines[i]);
		}
		randomKitMeta.setLore(Lore);
		
		randomKit.setItemMeta(randomKitMeta);
		return randomKit;
	}
	
	@SuppressWarnings("unused")
	private static ItemStack getError() {
		ItemStack ErrorItem = new ItemStack(Material.BARRIER);
		ItemMeta ErrorMeta = ErrorItem.getItemMeta();
		
		ErrorMeta.setDisplayName(plugin.msgs.getMsg("quequeErrorItemName"));
		
		ErrorItem.setItemMeta(ErrorMeta);
		return ErrorItem;
	}
	
	private static ItemStack getAutoQueque() {
		ItemStack AutoQueque = new ItemStack(Material.QUARTZ);
		ItemMeta AutoQuequeMeta = AutoQueque.getItemMeta();
		
		AutoQuequeMeta.setDisplayName(plugin.msgs.getMsg("quequeAutoQuequeName"));
		
		ArrayList<String> autoQueueLore = new ArrayList<String>();
		String[] Lines = plugin.msgs.getMsg("quequeAutoQuequeDesc").split("\n");
		for(int i = 0; i < Lines.length;i++) {
			autoQueueLore.add(Lines[i]);
		}
		AutoQuequeMeta.setLore(autoQueueLore);
		
		AutoQueque.setItemMeta(AutoQuequeMeta);
		return AutoQueque;
	}
	
	
	private static PlayerBestOfsPrefs getBestOfType(UUID uuid) {
		PlayerBestOfsPrefs pref = PlayerBestOfsPrefs.BestOf1;
		
			if(!plugin.getDBMgr().isConnected()) {
				System.out.println(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
				return pref;
			}
			pref = plugin.getDBMgr().getQueuePrefState2(uuid);
		
		
		return pref;
	}
	
	private static ItemStack getQueueState(Player p) {
		ItemStack stack = getItems.createItem(Material.STAINED_GLASS_PANE, 15, 1, plugin.msgs.getMsg("error"), null);
		
		if(!plugin.getOneVsOnePlayer(p).isInQueue()) {
			stack = getItems.createItem(Material.STAINED_GLASS_PANE, 5, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("joinQueueInv")), null);
		} else {
			stack = getItems.createItem(Material.STAINED_GLASS_PANE, 14, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("leaveQueueInv")), null);
		}
		
		return stack;
	}
	
	private static ItemStack getBestOf1() {
		return getItems.createItem(Material.GOLD_CHESTPLATE, 0, 1, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("bestOf1ItemName")), null);
	}
	
	private static ItemStack getBestOf3() {
		return getItems.createItem(Material.IRON_CHESTPLATE, 0, 3, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("bestOf3ItemName")), null);
	}
	
	private static ItemStack getBestOf5() {
		return getItems.createItem(Material.DIAMOND_CHESTPLATE, 0, 5, ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("bestOf5ItemName")), null);
	}
	
	
	public static void openInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*3, plugin.msgs.getMsg("quequeSettingsInvTitle"));
		
		PlayerQuequePrefs pref = PlayerQuequePrefs.EnemieKit;
		
			if(!plugin.getDBMgr().isConnected()) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
				return;
			}
			pref = plugin.getDBMgr().getQuequePrefState(p.getUniqueId());
		
		
		
		inv.setItem(3, getOwnKit());
		inv.setItem(4, getEnemieKit());
		inv.setItem(5, getRandomKit());
		
		
		inv.setItem(3+9, getInActive());
		inv.setItem(4+9, getInActive());
		inv.setItem(5+9, getInActive());
		
		if(pref != null && pref == PlayerQuequePrefs.ownKit) {
			inv.setItem(3+9, getActive());
		} else if(pref != null && pref == PlayerQuequePrefs.EnemieKit) {
			inv.setItem(4+9, getActive());
		} else if(pref != null && pref == PlayerQuequePrefs.RandomKit) {
			inv.setItem(5+9, getActive());
		}
		
	
		
		
		inv.setItem(19, getAutoQueque());
		
		ItemStack QueueState = new ItemStack(Material.INK_SACK);
		ItemMeta QueueStateMeta = QueueState.getItemMeta();
		if(Preferences_Manager.getPref(p.getUniqueId(), PlayerPrefs.QUEUE,"")) {
		 QueueState.setDurability((short) 10);
		 QueueStateMeta.setDisplayName(plugin.msgs.getMsg("activated"));
		} else {
		 QueueState.setDurability((short) 8);
		 QueueStateMeta.setDisplayName(plugin.msgs.getMsg("disabled"));
		}
		QueueState.setItemMeta(QueueStateMeta);
		inv.setItem(20, QueueState);
		
		
		ItemStack queueState = getQueueState(p);
		
		inv.setItem(0, queueState);
		inv.setItem(1, queueState);
		inv.setItem(9, queueState);
		inv.setItem(10, queueState);
		
		inv.setItem(8, getBestOf1());
		inv.setItem(8+9, getBestOf3());
		inv.setItem(8+9*2, getBestOf5());
		
		inv.setItem(7, getInActive());
		inv.setItem(7+9, getInActive());
		inv.setItem(7+9*2, getInActive());
		
		if(getBestOfType(p.getUniqueId()) == PlayerBestOfsPrefs.BestOf1) {
			inv.setItem(7, getActive());
		} else if(getBestOfType(p.getUniqueId()) == PlayerBestOfsPrefs.BestOf3) {
			inv.setItem(7+9, getActive());
		} else {
			inv.setItem(7+9*2, getActive());
		}
		
	
		p.openInventory(inv);
	}
	
	public static ItemStack getActive() {
		ItemStack active = new ItemStack(Material.INK_SACK);
		ItemMeta activeMeta = active.getItemMeta();
		
			active.setDurability((short) 10);
			activeMeta.setDisplayName(plugin.msgs.getMsg("activated"));
		
		 
		
			active.setItemMeta(activeMeta);
		return active;
	}
	
	public static ItemStack getInActive() {
		ItemStack inactive = new ItemStack(Material.INK_SACK);
		ItemMeta inactiveMeta = inactive.getItemMeta();
		
		inactive.setDurability((short) 8);
		inactiveMeta.setDisplayName(plugin.msgs.getMsg("disabled"));
		 
		inactive.setItemMeta(inactiveMeta);
			return inactive;
	}
	
	
	public static void setQuequePref(Player p, PlayerQuequePrefs pref) {
		
		plugin.getDBMgr().setQueuePref(p.getUniqueId(), pref);
	}
	
	public static PlayerQuequePrefs getQuequePref(Player p) {
		
		PlayerQuequePrefs State = plugin.getDBMgr().getQuequePrefState(p.getUniqueId());
		
		if(State == null) return PlayerQuequePrefs.EnemieKit;
		
		return State;
		
		
	}
	
}
