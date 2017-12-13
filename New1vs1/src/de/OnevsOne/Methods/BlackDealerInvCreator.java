package de.OnevsOne.Methods;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import de.OnevsOne.main;
import de.OnevsOne.MessageManager.MessageReplacer;

public class BlackDealerInvCreator {

	

	private static main plugin;




	@SuppressWarnings("static-access")
	public BlackDealerInvCreator(main plugin) {
		this.plugin = plugin;
	}

	public static void createInv(Player p) {
		
		ItemStack item = p.getItemInHand();
		
		Inventory inv = Bukkit.createInventory(null, 9*3,MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerMainInvTitle")));
		
		ItemStack empty = getItems.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§c", null);
		ItemStack amount = getItems.createItem(Material.MELON_SEEDS, 0, item.getAmount(), MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerMainInvAmount")).replaceAll("%Amount%", "" + item.getAmount()), null);
		//ItemStack rename = getItems.createItem(Material.SIGN, 0,1, "§cNamen ändern", null);
		ItemStack enchants = getItems.createItem(Material.ENCHANTMENT_TABLE, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerMainInvEnchant")), null);
		ItemStack unbreakable = getItems.createItem(Material.DIAMOND, 0, 1, "§6(Un)Zerstörbar machen", "§7Das Item kann nicht mehr zerstört werden!");
		
		ItemStack appleMod = getItems.createItem(Material.GOLDEN_APPLE, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerMainUHCApple")), null);
		ItemStack durability = getItems.createItem(Material.ANVIL, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerMainDurChange")), null);

		if(p.getItemInHand().getItemMeta().spigot().isUnbreakable()) {
			unbreakable = getItems.applyEnchant(unbreakable, Enchantment.ARROW_DAMAGE, 1);
			ItemMeta meta = unbreakable.getItemMeta();
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			unbreakable.setItemMeta(meta);
		}
		
		
		ItemStack add = getItems.createItem(Material.WOOD_BUTTON, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerMainInvAdd1")),  MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerMainInvAdd5Lore")));
		ItemStack remove = getItems.createItem(Material.WOOD_BUTTON, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerMainInvRem1")),  MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerMainInvRem5Lore")));
		
		
		inv.setItem(0, empty);
		inv.setItem(1, empty);
		inv.setItem(2, empty);
		
		inv.setItem(10, item);
	
		inv.setItem(0+9, empty);
		inv.setItem(2+9, empty);
		
		inv.setItem(0+9+9, empty);
		inv.setItem(1+9+9, empty);
		inv.setItem(2+9+9, empty);
		
		inv.setItem(13, amount);
		
		inv.setItem(14, enchants);
		
		inv.setItem(4, add);
		inv.setItem(4+9+9, remove);
		
		if(isApple(item)) {
		 inv.setItem(15, appleMod);
		}
		
		if(isDChangeable(item)) {
		 inv.setItem(15, durability);
		 inv.setItem(16, unbreakable);
		}
		
		p.openInventory(inv);
		
	}
	
	public static void createInvDur(Player p) {
		Inventory inv = Bukkit.createInventory(null, 27, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerDurInvTitle")));
		
		ItemStack empty = getItems.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§c", null);
		
		
		ItemStack item = p.getItemInHand();
		int dur = item.getType().getMaxDurability()-item.getDurability();
		ItemStack display = getItems.createItem(Material.ANVIL, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerDurInvInfoItem")).replaceAll("%Current%", "" + dur).replaceAll("%Max%", "" + item.getType().getMaxDurability()), null);
		
		
		ItemStack add = getItems.createItem(Material.WOOD_BUTTON, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerDurAdd1")),  MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerDurAdd10Per")));
		ItemStack remove = getItems.createItem(Material.WOOD_BUTTON, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerDurRem1")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerDurRem10Per")));
		
		
		inv.setItem(0, empty);
		inv.setItem(1, empty);
		inv.setItem(2, empty);
		
		inv.setItem(10, item);
	
		inv.setItem(0+9, empty);
		inv.setItem(2+9, empty);
		
		inv.setItem(0+9+9, empty);
		inv.setItem(1+9+9, empty);
		inv.setItem(2+9+9, empty);
		
		inv.setItem(6+9-1, display);
		inv.setItem(6-1, add);
		inv.setItem(6+9*2-1, remove);
		
		p.openInventory(inv);
		
	}
	
	public static void createEnchantInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*5, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerEnchantItemInv")));
		
		ItemStack empty = getItems.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§c", null);
		
		
		ItemStack item = p.getItemInHand();
		
		
		
		inv.setItem(0, empty);
		inv.setItem(1, empty);
		inv.setItem(2, empty);
		
		inv.setItem(10, item);
	
		inv.setItem(0+9, empty);
		inv.setItem(2+9, empty);
		
		inv.setItem(0+9+9, empty);
		inv.setItem(1+9+9, empty);
		inv.setItem(2+9+9, empty);
		
		inv.setItem(0+9*2, empty);
		inv.setItem(0+9*3, empty);
		inv.setItem(0+9*4, empty);
		inv.setItem(1+9*2, empty);
		inv.setItem(1+9*3, empty);
		inv.setItem(1+9*4, empty);
		inv.setItem(2+9*2, empty);
		inv.setItem(2+9*3, empty);
		inv.setItem(2+9*4, empty);
		
		inv.setItem(3, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerEnchantExplosionProt")), null));
		inv.setItem(4, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerEnchantArrowProt")), null));
		inv.setItem(5, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerEnchantBreath")), null));
		inv.setItem(6, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerWaterEff")), null));
		inv.setItem(7, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerThorns")), null));
		inv.setItem(8, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerWaterWalker")), null));
		
		inv.setItem(3+9, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerKnockback")), null));
		inv.setItem(4+9, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerFire")), null));
		inv.setItem(5+9, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerLuckMobs")), null));
		inv.setItem(6+9, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerBowStrength")), null));
		inv.setItem(7+9, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerBowFlame")), null));
		inv.setItem(8+9, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerArrowKnockback")), null));
		
		inv.setItem(3+9*2, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerDur")), null));
		inv.setItem(4+9*2, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerLuckOres")), null));
		inv.setItem(5+9*2, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerLure")), null));
		inv.setItem(6+9*2, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerLuckOfSee")), null));
		inv.setItem(7+9*2, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerArrowInf")), null));
		inv.setItem(8+9*2, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerSharpness")), null));
		
		inv.setItem(3+9*3, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerNemsis")), null));
		inv.setItem(4+9*3, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerFireProt")), null));
		inv.setItem(5+9*3, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerBann")), null));
		inv.setItem(6+9*3, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerSilkTouch")), null));
		inv.setItem(7+9*3, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerProtection")), null));
		inv.setItem(8+9*3, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerEffiz")), null));
		
		inv.setItem(3+9*4, getItems.createItem(Material.ENCHANTED_BOOK, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerFeatherfall")), null));

		
		p.openInventory(inv);
	}
	
	public static void addEnchantInv(Player p,Enchantment ench) {
		Inventory inv = Bukkit.createInventory(null, 9*3, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerEnchantUnderInvTitle")));
		
		ItemStack empty = getItems.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§c", null);
		
		
		ItemStack item = p.getItemInHand();
		
		
		
		ItemStack add = getItems.createItem(Material.WOOD_BUTTON, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerEnchantUnderInvAdd1")),  MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerEnchantUnderInvAdd5Lore")));
		ItemStack remove = getItems.createItem(Material.WOOD_BUTTON, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerEnchantUnderInvRem1")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerEnchantUnderInvRem5Lore")));
		
		
		inv.setItem(0, empty);
		inv.setItem(1, empty);
		inv.setItem(2, empty);
		
		inv.setItem(10, item);
	
		inv.setItem(0+9, empty);
		inv.setItem(2+9, empty);
		
		inv.setItem(0+9+9, empty);
		inv.setItem(1+9+9, empty);
		inv.setItem(2+9+9, empty);
		
		
		inv.setItem(5, add);
		inv.setItem(5+9*2, remove);
		
		ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) book.getItemMeta();
		meta.addStoredEnchant(ench, ench.getStartLevel(), true);
		book.setItemMeta(meta);
		
		inv.setItem(14, book);
		
		p.openInventory(inv);
	}
	
	public static void openAppleInv(Player p) {
		Inventory inv = Bukkit.createInventory(null, 9*3, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerGoldenHeadInvTitle")));
		
		ItemStack empty = getItems.createItem(Material.STAINED_GLASS_PANE, 15, 1, "§c", null);
		ItemStack display = getItems.createItem(Material.GOLDEN_APPLE, 0, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerGoldenHeadInvItemInfo")), MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerGoldenHeadInvItemInfoLore")));
		
		ItemStack item = p.getItemInHand();
		
		
		
		ItemStack enabled = getItems.createItem(Material.INK_SACK, 10, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("activated")), null);
		ItemStack disabled = getItems.createItem(Material.INK_SACK, 8, 1, MessageReplacer.replaceStrings(plugin.msgs.getMsg("disabled")), null);
		
		
		inv.setItem(0, empty);
		inv.setItem(1, empty);
		inv.setItem(2, empty);
		
		inv.setItem(10, item);
	
		inv.setItem(0+9, empty);
		inv.setItem(2+9, empty);
		
		inv.setItem(0+9+9, empty);
		inv.setItem(1+9+9, empty);
		inv.setItem(2+9+9, empty);
		
		inv.setItem(14, display);
		inv.setItem(15, disabled);
		if(p.getItemInHand().getType() == Material.GOLDEN_APPLE && p.getItemInHand().hasItemMeta() &&
		   p.getItemInHand().getItemMeta().hasDisplayName() && p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("goldenHeadName")))) {
			inv.setItem(15, enabled);
		}
		p.openInventory(inv);
		
	}
	
	public static boolean isApple(ItemStack stack) {
		return stack.getType() == Material.GOLDEN_APPLE ? true : false;
	}
	
	public static boolean isDChangeable(ItemStack stack) {
		
		if(stack.getType() == Material.WOOD_AXE ||
		   stack.getType() == Material.WOOD_HOE ||
		   stack.getType() == Material.WOOD_PICKAXE ||
		   stack.getType() == Material.WOOD_SPADE ||
		   stack.getType() == Material.WOOD_SWORD ||
		   stack.getType() == Material.LEATHER_BOOTS ||
		   stack.getType() == Material.LEATHER_CHESTPLATE ||
		   stack.getType() == Material.LEATHER_HELMET ||
		   stack.getType() == Material.LEATHER_LEGGINGS) {
			return true;
		}
		
		if(stack.getType() == Material.GOLD_AXE ||
				   stack.getType() == Material.GOLD_HOE ||
				   stack.getType() == Material.GOLD_PICKAXE ||
				   stack.getType() == Material.GOLD_SPADE ||
				   stack.getType() == Material.GOLD_SWORD ||
				   stack.getType() == Material.GOLD_HELMET ||
				   stack.getType() == Material.GOLD_CHESTPLATE ||
				   stack.getType() == Material.GOLD_BOOTS ||
				   stack.getType() == Material.GOLD_LEGGINGS) {
					return true;
				}
		
		if(stack.getType() == Material.STONE_AXE ||
				   stack.getType() == Material.STONE_HOE ||
				   stack.getType() == Material.STONE_PICKAXE ||
				   stack.getType() == Material.STONE_SPADE ||
				   stack.getType() == Material.STONE_SWORD||
				   stack.getType() == Material.CHAINMAIL_HELMET ||
				   stack.getType() == Material.CHAINMAIL_CHESTPLATE ||
				   stack.getType() == Material.CHAINMAIL_BOOTS ||
				   stack.getType() == Material.CHAINMAIL_LEGGINGS) {
					return true;
				}
		
		if(stack.getType() == Material.IRON_AXE ||
				   stack.getType() == Material.IRON_HOE ||
				   stack.getType() == Material.IRON_PICKAXE ||
				   stack.getType() == Material.IRON_SPADE ||
				   stack.getType() == Material.IRON_SWORD||
				   stack.getType() == Material.IRON_HELMET ||
				   stack.getType() == Material.IRON_CHESTPLATE ||
				   stack.getType() == Material.IRON_BOOTS ||
				   stack.getType() == Material.IRON_LEGGINGS) {
					return true;
				}
		if(stack.getType() == Material.DIAMOND_AXE ||
				   stack.getType() == Material.DIAMOND_HOE ||
				   stack.getType() == Material.DIAMOND_PICKAXE ||
				   stack.getType() == Material.DIAMOND_SPADE ||
				   stack.getType() == Material.DIAMOND_SWORD||
				   stack.getType() == Material.DIAMOND_HELMET ||
				   stack.getType() == Material.DIAMOND_CHESTPLATE ||
				   stack.getType() == Material.DIAMOND_BOOTS ||
				   stack.getType() == Material.DIAMOND_LEGGINGS ||
				   stack.getType() == Material.BOW ||
				   stack.getType() == Material.FISHING_ROD ||
				   stack.getType() == Material.SHEARS||
				   stack.getType() == Material.FLINT_AND_STEEL) {
					return true;
				}
		
		
		return false;
	}
	
	
	
}
