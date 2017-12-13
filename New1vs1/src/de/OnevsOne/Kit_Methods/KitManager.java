package de.OnevsOne.Kit_Methods;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import de.OnevsOne.main;
import de.OnevsOne.Listener.Manager.Preferences_Manager;

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.States.PlayerPrefs;
import net.md_5.bungee.api.ChatColor;

/**
 * Der Code ist von JHammer
 *
 * 06.05.2016 um 11:39:48 Uhr
 */
public class KitManager implements Listener {

	private  main plugin;
	
	public KitManager(main plugin) {
		this.plugin = plugin;
	}

	public  void Kitload(Player p, String ID, String load) {
		
			if(!plugin.getDBMgr().isConnected()) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
				return;
			}
			
			if(ID == null || load == null) return;
			
			
			if(!load.equalsIgnoreCase("d") && !load.equalsIgnoreCase("")) {
				try {
					int checkID = Integer.parseInt(load);
					if(checkID <= 0 || checkID >= 6) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("kitNotFound")).replaceAll("%Prefix%", plugin.prefix));
						return;
					}
				} catch (Exception e) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("kitNotFound")).replaceAll("%Prefix%", plugin.prefix));
					return;
				}
			}
			

			
			
			UUID useid = null;
			
			try {
				useid = UUID.fromString(ID);
			} catch(IllegalArgumentException e) {
				e.printStackTrace();
				p.sendMessage(MessageReplacer.replaceStrings(plugin.msgs.getMsg("UUIDconvertError"), p.getDisplayName()));
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				return;
			}
			
			if(!plugin.getDBMgr().isUserExists(useid)) {
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				return;
			}
			
			if(load.equalsIgnoreCase("d")) {
				load = plugin.getDBMgr().getDefaultKit(UUID.fromString(ID));
			}
			if(load.equalsIgnoreCase("1")) {
				load = "";
			}
			
			if(plugin.getDBMgr().getKit(useid, false, "") == null || plugin.getDBMgr().getKit(useid, false, load).equalsIgnoreCase("")) {
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				return;
			}
			
			try {
			    p.getInventory().setContents(fromBase64(plugin.getDBMgr().getKit(useid, false,load)).getContents());
				p.getInventory().setArmorContents(fromBase64Armor(plugin.getDBMgr().getKit(UUID.fromString(ID), true, load)));
			} catch (IOException e) {
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				e.printStackTrace();
			}
			p.updateInventory();
			
			return;
	
		
	}
	
	public void kitLoadCustomKit(String Name, Player p) {
		if(!plugin.getDBMgr().isConnected()) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
			return;
		}
		
		if(plugin.getDBMgr().isCustomKitExists(Name) != 1) {
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);
			return;
		}
		
		if(plugin.getDBMgr().loadCustomKitInv(Name) == null || plugin.getDBMgr().loadCustomKitInv(Name).equalsIgnoreCase("")) {
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);
			return;
		}
		
		try {
			
			p.getInventory().setContents(fromBase64(plugin.getDBMgr().loadCustomKitInv(Name)).getContents());
			p.getInventory().setArmorContents(fromBase64Armor(plugin.getDBMgr().loadCustomKitArmor(Name)));
		} catch (IOException e) {
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);
			e.printStackTrace();
		}
		p.updateInventory();
		
		return;
	}
	
	public void delKit(String ID, String load) {			
			if(!plugin.getDBMgr().isConnected()) {
				System.out.println(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
				return;
			}
			
			if(load.equalsIgnoreCase("d")) {
				load = plugin.getDBMgr().getDefaultKit(UUID.fromString(ID));
			}
			if(load.equalsIgnoreCase("1")) {
				load = "";
			}
			plugin.getDBMgr().setKit(UUID.fromString(ID), "", false, load);
			plugin.getDBMgr().setKit(UUID.fromString(ID), "", true, load);
			return;
		
	}
	
	public void KitSave(Inventory inv, ItemStack[] armor, String ID, String load) {
			if(!plugin.getDBMgr().isConnected()) {
				System.out.println(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
				return;
			}
			if(load.equalsIgnoreCase("d")) {
				load = plugin.getDBMgr().getDefaultKit(UUID.fromString(ID));
			}
			if(load.equalsIgnoreCase("1")) {
				load = "";
			}
			plugin.getDBMgr().setKit(UUID.fromString(ID), toBase64(inv), false, load);
			plugin.getDBMgr().setKit(UUID.fromString(ID), toBase64Armor(armor), true, load);
			return;
		
		
		
	}
	
	public String getkitAuthor(String ID) {
		String[] ID2 = ID.split(":");
		
		if(plugin.getDBMgr().isCustomKitExists(ID) == 1) {
			return ID;
		}
		
			
			if(!plugin.getDBMgr().isConnected()) {
				//p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection).replaceAll("%Prefix%", plugin.prefix));
				return "-";
			}
			
			
			UUID useid = null;
			
			try{
				useid = UUID.fromString(ID2[0]);
			}catch(Exception e) {
				e.printStackTrace();
				return "-";
			}
			
			if(plugin.getDBMgr().getUserName(useid) == null) {
				return "-";
			}
			
			if(plugin.getDBMgr().getUserName(useid).equalsIgnoreCase("null")) {
				return "-";
			}
			
			return plugin.getDBMgr().getUserName(useid);
		
	}
	
	public String toBase64(Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            
            dataOutput.writeInt(inventory.getSize());
      
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }
            
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Error.", e);
        }    
    }
	
	public String toBase64Armor(ItemStack[] Armor) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            
            for (int i = 0; i < Armor.length; i++) {
            	
                dataOutput.writeObject(Armor[i]);
            }
            
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Error.", e);
        }    
    }

    public Inventory fromBase64(String data) throws IOException {
    	
    	if(data == null) return null;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            dataInput.readInt();
            Inventory inventory = Bukkit.getServer().createInventory(null, 36);
            
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }
            dataInput.close();
            return inventory;
        } catch (Exception e) {
            //throw new IOException("Error.", e);
        	return null;
        }
    }
    
    public  ItemStack[] fromBase64Armor(String data) throws IOException {
    	if(data == null) return null;
    	try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            
            ItemStack[] Armor = {new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.AIR),new ItemStack(Material.AIR)};
            
            for (int i = 0; i < 4; i++) {
              Armor[i] = (ItemStack) dataInput.readObject();
            }
            dataInput.close();
            return Armor;
        } catch (Exception e) {
        	return null;
        }
    }
	
   public  ItemStack[] getKit(String id, Player p) {
	   
			if(!plugin.getDBMgr().isConnected()) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
				return null;
			}
			
			if(!plugin.getDBMgr().isUserExists(UUID.fromString(id))) return null;
			
			
			UUID useid = null;
			
			try{
				useid = UUID.fromString(id);
			}catch(IllegalArgumentException e) {
				e.printStackTrace();
				return null;
			}
			
			if(plugin.getDBMgr().getKit(useid, false, "") == null || plugin.getDBMgr().getKit(useid, false, "").equalsIgnoreCase("")) {
				return null;
			}
			
			try {
				return fromBase64(plugin.getDBMgr().getKit(useid, false, "")).getContents();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		
   }
   
   public  ItemStack[] getKitArmor(String id, Player p) {
	   
		   if(!plugin.getDBMgr().isConnected()) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
				return null;
			}
		   
			if(!plugin.getDBMgr().isUserExists(UUID.fromString(id))) {
				return null;
			}
			
			UUID useid = null;
			
			try{
				useid = UUID.fromString(id);
			}catch(IllegalArgumentException e) {
				e.printStackTrace();
				return null;
			}
			
			if(plugin.getDBMgr().getKit(useid, false, "") == null || plugin.getDBMgr().getKit(useid, true, "").equalsIgnoreCase("")) {
				return null;
			}
			
			try {
				return fromBase64(plugin.getDBMgr().getKit(useid, true, "")).getContents();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		
   }
   
   public  ItemStack[] getCustomKit(String Name) {
			if(!plugin.getDBMgr().isConnected()) return null;
			
			if(plugin.getDBMgr().isCustomKitExists(Name) != 1) return null;
			
			if(plugin.getDBMgr().loadCustomKitInv(Name) == null || plugin.getDBMgr().loadCustomKitInv(Name).equalsIgnoreCase("")) {
				return null;
			}
			
			try {
				return fromBase64(plugin.getDBMgr().loadCustomKitInv(Name)).getContents();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		
   }
   
   public  ItemStack[] getCustomKitArmor(String Name) {
	  
			
			if(!plugin.getDBMgr().isConnected()) {
				return null;
			}
			
			if(plugin.getDBMgr().isCustomKitExists(Name) != 1) {
				return null;
			}
			
			
			
			if(plugin.getDBMgr().loadCustomKitArmor(Name) == null || plugin.getDBMgr().loadCustomKitArmor(Name).equalsIgnoreCase("")) {
				return null;
			}
			
			try {
				return fromBase64(plugin.getDBMgr().loadCustomKitArmor(Name)).getContents();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return null;
		
   }
   
	public  void setSettingsArena(UUID uuid, String Arena,String subID) {
		
		  		if(!plugin.getDBMgr().isConnected()) {
					System.out.println(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
					return;
				}
		  		
				if(plugin.getDBMgr().isUserExists(uuid)) {
					ArrayList<PlayerPrefs> usePrefs = new ArrayList<PlayerPrefs>();
					for(PlayerPrefs prefs : PlayerPrefs.values()) {
					 if(subID.equalsIgnoreCase("d")){
					  subID = plugin.getDBMgr().getDefaultKit(uuid);
					 }
					 String pref = "" + plugin.getDBMgr().getPref(uuid, Preferences_Manager.getPrefID(prefs),subID);
							  		  
					 if(pref != null && pref.equalsIgnoreCase("true")) {
					  if(prefs == PlayerPrefs.BUILD) usePrefs.add(prefs);
					  if(prefs  == PlayerPrefs.HUNGER)  usePrefs.add(prefs);
					  if(prefs  == PlayerPrefs.InstantTnT) usePrefs.add(prefs);
					  if(prefs ==  PlayerPrefs.NoCrafting) usePrefs.add(prefs);
					  if(prefs  == PlayerPrefs.NoFallDamage) usePrefs.add(prefs);
					  if(prefs  == PlayerPrefs.NoTnTDamage) usePrefs.add(prefs);
					  if(prefs == PlayerPrefs.SoupReg) usePrefs.add(prefs);
					  if(prefs == PlayerPrefs.NoArrowPickup) usePrefs.add(prefs);
					  if(prefs == PlayerPrefs.SoupNoob) usePrefs.add(prefs);
		              if(prefs == PlayerPrefs.DoubleJump) usePrefs.add(prefs);
		              if(prefs == PlayerPrefs.NoRegneration) usePrefs.add(prefs);
		              if(prefs == PlayerPrefs.NoHitDelay)  usePrefs.add(prefs);
		              if(prefs == PlayerPrefs.NoItemDrops)  usePrefs.add(prefs);
		              if(prefs == PlayerPrefs.NoFriendlyFire)  usePrefs.add(prefs);
		              if(prefs == PlayerPrefs.WaterDamage)  usePrefs.add(prefs);
					 }   
				    }
				for(PlayerPrefs allPrefs : PlayerPrefs.values()) {
				 plugin.getRAMMgr().saveRAM(Arena, "Pref." + allPrefs, "" + usePrefs.contains(allPrefs));
				}
			   } else {
				for(PlayerPrefs allPrefs : PlayerPrefs.values()) {
				 plugin.getRAMMgr().saveRAM(Arena, "Pref." + allPrefs, "false");
				} 
			   }
		  
	}
	
	public  void setSettingsArenaCustomKit(String Name, String Arena) {
			
		  		if(!plugin.getDBMgr().isConnected()) {
					System.out.println(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
					return;
				}
		  		
				if(plugin.getDBMgr().isCustomKitExists(Name) == 1) {
					ArrayList<PlayerPrefs> usePrefs = new ArrayList<PlayerPrefs>();
					for(PlayerPrefs prefs : PlayerPrefs.values()) {
					 
					 String pref = plugin.getDBMgr().getCustomKitPref(Name, Preferences_Manager.getPrefID(prefs)) + "";
							  		  
					 if(pref != null && pref.equalsIgnoreCase("true")) {
					  if(prefs == PlayerPrefs.BUILD) usePrefs.add(prefs);
					  if(prefs  == PlayerPrefs.HUNGER)  usePrefs.add(prefs);
					  if(prefs  == PlayerPrefs.InstantTnT) usePrefs.add(prefs);
					  if(prefs ==  PlayerPrefs.NoCrafting) usePrefs.add(prefs);
					  if(prefs  == PlayerPrefs.NoFallDamage) usePrefs.add(prefs);
					  if(prefs  == PlayerPrefs.NoTnTDamage) usePrefs.add(prefs);
					  if(prefs == PlayerPrefs.SoupReg) usePrefs.add(prefs);
					  if(prefs == PlayerPrefs.NoArrowPickup) usePrefs.add(prefs);
					  if(prefs == PlayerPrefs.SoupNoob) usePrefs.add(prefs);
		              if(prefs == PlayerPrefs.DoubleJump) usePrefs.add(prefs);
		              if(prefs == PlayerPrefs.NoRegneration) usePrefs.add(prefs);
		              if(prefs == PlayerPrefs.NoHitDelay)  usePrefs.add(prefs);
		              if(prefs == PlayerPrefs.NoItemDrops)  usePrefs.add(prefs);
		              if(prefs == PlayerPrefs.NoFriendlyFire)  usePrefs.add(prefs);
		              if(prefs == PlayerPrefs.WaterDamage)  usePrefs.add(prefs);
					 }   
				    }
					for(PlayerPrefs allPrefs : PlayerPrefs.values()) {
						 plugin.getRAMMgr().saveRAM(Arena, "Pref." + allPrefs, "" + usePrefs.contains(allPrefs));
					}
				   } else {
					for(PlayerPrefs allPrefs : PlayerPrefs.values()) {
					 plugin.getRAMMgr().saveRAM(Arena, "Pref." + allPrefs, "false");
					} 
				  }
		  
	}
	
	public  boolean isKitExits(String Name) {
		 if(Name.contains(":")) {
			 String subID = Name.split(":")[1];
			 Name = Name.split(":")[0];
			 
			 try {
				 
				 int sub = Integer.parseInt(subID);
				 if(sub > 5 || sub < 5) return false; 
			 } catch (Exception e) {
				return false;
			}
		 }
		
		 
  		   if(plugin.getDBMgr().isNameRegistered(Name)) {
  			   UUID useid = null;
	    		   try{
	    			   useid = plugin.getDBMgr().getUUID(Name);
	    		   } catch(Exception e) {
	    			   return false;
	    		   }
	    		   
	    		   if(plugin.getDBMgr().isUserExists(useid)) {
			    	   return true;
			       }
  		   }  else {
  			   return false;
  		   }
		return false;
  		  
  	   
	}
	
	public  String getSelectedKit(Player p) {
		
		String subID = "d";
		
		if(plugin.getOneVsOnePlayer(p).getKitLoaded() != null) {
		 	if(plugin.getDBMgr().isCustomKitExists(plugin.getOneVsOnePlayer(p).getKitLoaded().split(":")[0]) == 1) {
		 		return plugin.getOneVsOnePlayer(p).getKitLoaded().split(":")[0];
		 	} else {
		 		
		 		String kit = plugin.getOneVsOnePlayer(p).getKitLoaded();
		 		if(kit.split(":").length >= 2) subID = kit.split(":")[1];
		 		kit = kit.split(":")[0];
		 		
		 		if(subID.equalsIgnoreCase("d")) subID = plugin.getDBMgr().getDefaultKit(p.getUniqueId());
				if(subID.equalsIgnoreCase("1")) subID = "";
				if(subID.equalsIgnoreCase("")) return new StringBuilder(kit).toString();
				
				return new StringBuilder(kit).append(":").append(subID).toString();
		 	}
		 	
		}
		
		if(subID.equalsIgnoreCase("d")) subID = plugin.getDBMgr().getDefaultKit(p.getUniqueId());
		if(subID.equalsIgnoreCase("1")) subID = "";
		
		return p.getName() + ":" + subID;
	}
}
