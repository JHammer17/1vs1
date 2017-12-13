package de.OnevsOne.Listener.Manager;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import de.OnevsOne.main;

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.Methods.BlackDealerInvCreator;
import de.OnevsOne.Methods.getItems;

public class BlackDealerInvManager implements Listener {

	private static main plugin;

	@SuppressWarnings("static-access")
	public BlackDealerInvManager(main plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getWhoClicked() instanceof Player) {
		 Player p = (Player) e.getWhoClicked();
		 if(plugin.isInOneVsOnePlayers(p.getUniqueId())) {
			
		  if(e.getInventory().getTitle().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerMainInvTitle")))) {
			e.setCancelled(true);
			if(e.getCurrentItem() == null) return;
			
			
			
		   if(e.getClickedInventory().getTitle().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerMainInvTitle")))) {
			if(e.getCurrentItem() != null) {
				
			 if(e.getSlot() == 4) {
			   if(e.isShiftClick()) {
				  p.getItemInHand().setAmount(p.getItemInHand().getAmount()+5);
				   if(p.getItemInHand().getAmount() > 64) {
					  p.getItemInHand().setAmount(64);
				   }
			      } else {
			    	  p.getItemInHand().setAmount(p.getItemInHand().getAmount()+1);
				  if(p.getItemInHand().getAmount() > 64) {
					  p.getItemInHand().setAmount(64);
				  }
				  
			     }
			   BlackDealerInvCreator.createInv(p);
			   } else if(e.getSlot() == 4+9+9) {
				   
				   if(e.isShiftClick()) {
					   if(p.getItemInHand().getAmount() == 5) {
						   p.getItemInHand().setAmount(1);
						   BlackDealerInvCreator.createInv(p);
						   return;
					   }
						  p.getItemInHand().setAmount(p.getItemInHand().getAmount()-5);
						   if(p.getItemInHand().getAmount() <= 1) {
							  p.getItemInHand().setAmount(1);
						   }
					      } else {
					    	  p.getItemInHand().setAmount(p.getItemInHand().getAmount()-1);
						  if(p.getItemInHand().getAmount() < 1) {
							  p.getItemInHand().setAmount(1);
						  }
					     }  
				 BlackDealerInvCreator.createInv(p);
			   } else if (e.getSlot() == 15) {
				   if(e.getCurrentItem().getType() == Material.ANVIL) {
					   BlackDealerInvCreator.createInvDur(p);
				   } else if(e.getCurrentItem().getType() == Material.GOLDEN_APPLE) {
					   BlackDealerInvCreator.openAppleInv(p);
				   }
			   } else if (e.getSlot() == 16) {
				   if(e.getCurrentItem().getType() == Material.DIAMOND) {
					   ItemStack item = p.getItemInHand();
					   ItemMeta iMeta = item.getItemMeta();
					   iMeta.spigot().setUnbreakable(!iMeta.spigot().isUnbreakable());
					   p.getInventory().getItemInHand().setItemMeta(iMeta);
					   BlackDealerInvCreator.createInv(p);
				   } 
			   } else if(e.getSlot() == 14) {
				   BlackDealerInvCreator.createEnchantInv(p);
			   }
			}
		   }
		  } else if(e.getInventory().getTitle().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerDurInvTitle")))) {
				e.setCancelled(true);
				if(e.isRightClick()) {
					BlackDealerInvCreator.createInv(p);
					return;
				}
				if(e.getCurrentItem() == null) return;
				
			   if(e.getClickedInventory().getTitle().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerDurInvTitle")))) {
				if(e.getCurrentItem() != null) {
					if(e.getSlot() == 6-1) {
						if(e.isShiftClick()) {
							int curr = p.getItemInHand().getType().getMaxDurability()-p.getItemInHand().getDurability();
							if(curr == 0) curr += p.getItemInHand().getType().getMaxDurability()/100*17.3444;
							curr = (int) (curr*1.1);
							
							if(curr >= p.getItemInHand().getType().getMaxDurability()) {
								curr = p.getItemInHand().getType().getMaxDurability();
							}
							int setter = p.getItemInHand().getType().getMaxDurability()-curr;
							p.getItemInHand().setDurability((short) setter);
							BlackDealerInvCreator.createInvDur(p);
							return;
						} else {
							int curr = p.getItemInHand().getType().getMaxDurability()-p.getItemInHand().getDurability();
							curr++;
							if(curr >= p.getItemInHand().getType().getMaxDurability()) {
								curr = p.getItemInHand().getType().getMaxDurability();
							}
							int setter = p.getItemInHand().getType().getMaxDurability()-curr;
							p.getItemInHand().setDurability((short) setter);
						}
						BlackDealerInvCreator.createInvDur(p);
					} else if(e.getSlot() == 6+9*2-1) {
						if(e.isShiftClick()) {
							if(e.isShiftClick()) {
								int curr = p.getItemInHand().getType().getMaxDurability()-p.getItemInHand().getDurability();
								curr = curr/100*90;
								if(curr <= 0) curr = 0;
								int setter = p.getItemInHand().getType().getMaxDurability()-curr;
								p.getItemInHand().setDurability((short) setter);
							}
						} else {
							int curr = p.getItemInHand().getType().getMaxDurability()-p.getItemInHand().getDurability();
							curr--;
							if(curr <= 0) curr = 0;
							int setter = p.getItemInHand().getType().getMaxDurability()-curr;
							p.getItemInHand().setDurability((short) setter);
						}
						BlackDealerInvCreator.createInvDur(p);
					}
				}
			   }
		  } else if(e.getInventory().getTitle().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerEnchantItemInv")))) {
				e.setCancelled(true);
				if(e.isRightClick()) {
					BlackDealerInvCreator.createInv(p);
					return;
				}
				if(e.getCurrentItem() == null) return;
				
			   if(e.getClickedInventory().getTitle().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerEnchantItemInv")))) {
				 
				 if(e.getCurrentItem() != null) {
					int slot = e.getSlot();
					
					if(slot == 3) BlackDealerInvCreator.addEnchantInv(p, Enchantment.PROTECTION_EXPLOSIONS);
					if(slot == 4) BlackDealerInvCreator.addEnchantInv(p, Enchantment.PROTECTION_PROJECTILE);
					if(slot == 5) BlackDealerInvCreator.addEnchantInv(p, Enchantment.OXYGEN);
					if(slot == 6) BlackDealerInvCreator.addEnchantInv(p, Enchantment.WATER_WORKER);
					if(slot == 7) BlackDealerInvCreator.addEnchantInv(p, Enchantment.THORNS);
					if(slot == 8) BlackDealerInvCreator.addEnchantInv(p, Enchantment.DEPTH_STRIDER);
				
					if(slot == 3+9) BlackDealerInvCreator.addEnchantInv(p, Enchantment.KNOCKBACK);
					if(slot == 4+9) BlackDealerInvCreator.addEnchantInv(p, Enchantment.FIRE_ASPECT);
					if(slot == 5+9) BlackDealerInvCreator.addEnchantInv(p, Enchantment.LOOT_BONUS_MOBS);
					if(slot == 6+9) BlackDealerInvCreator.addEnchantInv(p, Enchantment.ARROW_DAMAGE);
					if(slot == 7+9) BlackDealerInvCreator.addEnchantInv(p, Enchantment.ARROW_FIRE);
					if(slot == 8+9) BlackDealerInvCreator.addEnchantInv(p, Enchantment.ARROW_KNOCKBACK);
					
					if(slot == 3+9*2) BlackDealerInvCreator.addEnchantInv(p, Enchantment.DURABILITY);
					if(slot == 4+9*2) BlackDealerInvCreator.addEnchantInv(p, Enchantment.LOOT_BONUS_BLOCKS);
					if(slot == 5+9*2) BlackDealerInvCreator.addEnchantInv(p, Enchantment.LURE);
					if(slot == 6+9*2) BlackDealerInvCreator.addEnchantInv(p, Enchantment.LUCK);
					if(slot == 7+9*2) BlackDealerInvCreator.addEnchantInv(p, Enchantment.ARROW_INFINITE);
					if(slot == 8+9*2) BlackDealerInvCreator.addEnchantInv(p, Enchantment.DAMAGE_ALL);
					
					if(slot == 3+9*3) BlackDealerInvCreator.addEnchantInv(p, Enchantment.DAMAGE_ARTHROPODS);
					if(slot == 4+9*3) BlackDealerInvCreator.addEnchantInv(p, Enchantment.PROTECTION_FIRE);
					if(slot == 5+9*3) BlackDealerInvCreator.addEnchantInv(p, Enchantment.DAMAGE_UNDEAD);
					if(slot == 6+9*3) BlackDealerInvCreator.addEnchantInv(p, Enchantment.SILK_TOUCH);
					if(slot == 7+9*3) BlackDealerInvCreator.addEnchantInv(p, Enchantment.PROTECTION_ENVIRONMENTAL);
					if(slot == 8+9*3) BlackDealerInvCreator.addEnchantInv(p, Enchantment.DIG_SPEED);
					
					if(slot == 3+9*4) BlackDealerInvCreator.addEnchantInv(p, Enchantment.PROTECTION_FALL);
				}
			   }
		   } else if(e.getInventory().getTitle().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerEnchantUnderInvTitle")))) {
				e.setCancelled(true);
				if(e.isRightClick()) {
					BlackDealerInvCreator.createEnchantInv(p);
					return;
				}
				if(e.getCurrentItem() == null) return;
				
			   if(e.getClickedInventory().getTitle().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerEnchantUnderInvTitle")))) {
				if(e.getCurrentItem() != null) {
					if(e.getSlot() == 6-1) {
						if(e.isShiftClick()) {
							Enchantment ench = Enchantment.ARROW_DAMAGE;
							
							if(e.getInventory().getItem(14).getType() != Material.ENCHANTED_BOOK) {
								return;
							}
							
							EnchantmentStorageMeta meta = (EnchantmentStorageMeta) e.getInventory().getItem(14).getItemMeta();
							
							
							for(Enchantment ench1 : meta.getStoredEnchants().keySet()) {
								ench = ench1;
								break;
							}
							getItems.applyEnchant(p.getItemInHand(), ench, getItems.getEnchLevel(p.getItemInHand(), ench)+5, 10);
							BlackDealerInvCreator.addEnchantInv(p, ench);
						} else {
							Enchantment ench = Enchantment.ARROW_DAMAGE;
							
							if(e.getInventory().getItem(14).getType() != Material.ENCHANTED_BOOK) {
								return;
							}
							
							EnchantmentStorageMeta meta = (EnchantmentStorageMeta) e.getInventory().getItem(14).getItemMeta();
							
							
							for(Enchantment ench1 : meta.getStoredEnchants().keySet()) {
								ench = ench1;
								break;
							}
							getItems.applyEnchant(p.getItemInHand(), ench, getItems.getEnchLevel(p.getItemInHand(), ench)+1, 10);
							BlackDealerInvCreator.addEnchantInv(p, ench);
						}
					} else if(e.getSlot() == 6+9+9-1) {
						if(e.isShiftClick()) {
							Enchantment ench = Enchantment.ARROW_DAMAGE;
							
							if(e.getInventory().getItem(14).getType() != Material.ENCHANTED_BOOK) {
								return;
							}
							
							EnchantmentStorageMeta meta = (EnchantmentStorageMeta) e.getInventory().getItem(14).getItemMeta();
							
							
							for(Enchantment ench1 : meta.getStoredEnchants().keySet()) {
								ench = ench1;
								break;
							}
							getItems.applyEnchant(p.getItemInHand(), ench, getItems.getEnchLevel(p.getItemInHand(), ench)-5, 10);
							BlackDealerInvCreator.addEnchantInv(p, ench);
						} else {
							Enchantment ench = Enchantment.ARROW_DAMAGE;
							
							if(e.getInventory().getItem(14).getType() != Material.ENCHANTED_BOOK) {
								return;
							}
							
							EnchantmentStorageMeta meta = (EnchantmentStorageMeta) e.getInventory().getItem(14).getItemMeta();
							
							
							for(Enchantment ench1 : meta.getStoredEnchants().keySet()) {
								ench = ench1;
								break;
							}
							getItems.applyEnchant(p.getItemInHand(), ench, getItems.getEnchLevel(p.getItemInHand(), ench)-1, 10);
							BlackDealerInvCreator.addEnchantInv(p, ench);
						}
						
					}
				}
			   }
		   } else if(e.getInventory().getTitle().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerGoldenHeadInvTitle")))) {
				e.setCancelled(true);
				if(e.isRightClick()) {
					BlackDealerInvCreator.createInv(p);
					return;
				}
				if(e.getCurrentItem() == null) return;
				
			   if(e.getClickedInventory().getTitle().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("blackDealerGoldenHeadInvTitle")))) {
				if(e.getCurrentItem() != null) {
				 if(e.getSlot() == 15 || e.getSlot() == 14) {
					 if(p.getItemInHand().getType() == Material.GOLDEN_APPLE && p.getItemInHand().hasItemMeta() &&
						p.getItemInHand().getItemMeta().hasDisplayName() && p.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(MessageReplacer.replaceStrings(plugin.msgs.getMsg("goldenHeadName")))) {
						 p.setItemInHand(getItems.createItem(Material.GOLDEN_APPLE, 0, p.getItemInHand().getAmount(), null, null));
					 } else {
						 p.setItemInHand(getItems.createItem(Material.GOLDEN_APPLE, 0, p.getItemInHand().getAmount(), MessageReplacer.replaceStrings(plugin.msgs.getMsg("goldenHeadName")), null));
					 }
					 BlackDealerInvCreator.openAppleInv(p);
					 
				 }
				} 
			   }
		   }
		 
		 }
		}
		
		
	}

	
	
}
