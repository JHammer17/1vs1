package de.OnevsOne.Kit_Methods;

import java.util.UUID;

import org.bukkit.entity.Player;

import de.OnevsOne.main;
import de.OnevsOne.Listener.Manager.Preferences_Manager;

import de.OnevsOne.MessageManager.MessageReplacer;
import de.OnevsOne.States.PlayerPrefs;
import net.md_5.bungee.api.ChatColor;

/**
 * Der Code ist von JHammer
 *
 * 19.06.2016 um 15:18:12 Uhr
 */
public class KitMessages {

	private static main plugin;
	
	@SuppressWarnings("static-access")
	public KitMessages(main plugin) {
		this.plugin = plugin;
	}

	public static void sendAllPrefs(UUID uuid, Player p, String subID) {
		  StringBuilder KitEinstellungen = new StringBuilder();
		
		  if(!subID.equalsIgnoreCase("d") && !subID.equalsIgnoreCase("")) {
			  try {
					 int checkID = Integer.parseInt(subID);
					 if(checkID <= 0 || checkID >= 6) return;
				} catch (Exception e) {
					return;
				}
		  }
		  
		 
		  		if(!plugin.getDBMgr().isConnected()) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
					return;
				}
				if(plugin.getDBMgr().isUserExists(uuid)) {
				 for(PlayerPrefs prefs : PlayerPrefs.values()) {
				  if(subID.equalsIgnoreCase("d")) {
				   subID = plugin.getDBMgr().getDefaultKit(uuid);
				  }
				  
				  String pref = new StringBuilder().append(plugin.getDBMgr().getPref(uuid, Preferences_Manager.getPrefID(prefs),subID)).toString();
							  		  
                  if(pref.equalsIgnoreCase("true")) {			   
                   if(prefs == PlayerPrefs.BUILD) KitEinstellungen.append(plugin.msgs.getMsg("prefBuild").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.HUNGER) KitEinstellungen.append(plugin.msgs.getMsg("prefNoHunger").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.InstantTnT) KitEinstellungen.append(plugin.msgs.getMsg("prefInstantTnT").replaceAll("&", "§"));
				   if(prefs ==  PlayerPrefs.NoCrafting) KitEinstellungen.append(plugin.msgs.getMsg("prefNoCrafting").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.NoFallDamage) KitEinstellungen.append(plugin.msgs.getMsg("prefNoFallDamage").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.NoTnTDamage) KitEinstellungen.append(plugin.msgs.getMsg("prefNoExplosions").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.SoupReg) KitEinstellungen.append(plugin.msgs.getMsg("prefSoupReg").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.NoArrowPickup) KitEinstellungen.append(plugin.msgs.getMsg("prefNoArrowPickup").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.SoupNoob) KitEinstellungen.append(plugin.msgs.getMsg("prefSoupNoob").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.DoubleJump) KitEinstellungen.append(plugin.msgs.getMsg("prefDoubleJump").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.NoHitDelay) KitEinstellungen.append(plugin.msgs.getMsg("prefNoHitDelay").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.NoRegneration) KitEinstellungen.append(plugin.msgs.getMsg("prefNoRegeneration").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.NoItemDrops) KitEinstellungen.append(MessageReplacer.replaceStrings(plugin.msgs.getMsg("prefNoItemDrops")));
				   if(prefs == PlayerPrefs.NoFriendlyFire) KitEinstellungen.append(MessageReplacer.replaceStrings(plugin.msgs.getMsg("prefNoFriendlyFire")));
				   if(prefs == PlayerPrefs.WaterDamage) KitEinstellungen.append(MessageReplacer.replaceStrings(plugin.msgs.getMsg("prefWaterDmg")));
				 }		   
				}  
			   }
		  
		  
		  if(KitEinstellungen.toString().equalsIgnoreCase("")) {
			  KitEinstellungen.append(plugin.msgs.getMsg("noKitAuthor").replaceAll("&", "§")); 
		  } else {
			  KitEinstellungen = new StringBuilder().append(KitEinstellungen.toString().replaceFirst("§7, ", ""));
		  }
		  
		  String Kitauthor = new KitManager(plugin).getkitAuthor("" + uuid);
		  if(Kitauthor == null) {
			  Kitauthor = plugin.msgs.getMsg("noKitAuthor").replaceAll("&", "§");
		  }
		  
		  if(!subID.equalsIgnoreCase("1") && !subID.equalsIgnoreCase("d")) {
			  Kitauthor = Kitauthor + ":" + subID;
		  }
		  
		  if(subID.equalsIgnoreCase("d")) {
		   
		  
			if(!plugin.getDBMgr().isConnected()) {
			 p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
			 return;
			}
            
			
		  }
		  p.sendMessage(MessageReplacer.replaceStringsKit(plugin.msgs.getMsg("kitMessage"), p.getDisplayName(), Kitauthor, KitEinstellungen.toString()));
	}
	
	public static void sendAllPrefsCustomKit(String Name, Player p) {
		  StringBuilder KitEinstellungen = new StringBuilder();
		  
		  
		  		if(!plugin.getDBMgr().isConnected()) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.msgs.getMsg("noMySQLConnection")).replaceAll("%Prefix%", plugin.prefix));
					return;
				}
				if(plugin.getDBMgr().isCustomKitExists(Name) == 1) {
				 for(PlayerPrefs prefs : PlayerPrefs.values()) {
				  
				  
				  String pref = new StringBuilder().append(plugin.getDBMgr().getCustomKitPref(Name, Preferences_Manager.getPrefID(prefs))).toString();
							  		  
                if(pref.equalsIgnoreCase("true")) {			   
                 if(prefs == PlayerPrefs.BUILD) KitEinstellungen.append(plugin.msgs.getMsg("prefBuild").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.HUNGER) KitEinstellungen.append(plugin.msgs.getMsg("prefNoHunger").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.InstantTnT) KitEinstellungen.append(plugin.msgs.getMsg("prefInstantTnT").replaceAll("&", "§"));
				   if(prefs ==  PlayerPrefs.NoCrafting) KitEinstellungen.append(plugin.msgs.getMsg("prefNoCrafting").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.NoFallDamage) KitEinstellungen.append(plugin.msgs.getMsg("prefNoFallDamage").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.NoTnTDamage) KitEinstellungen.append(plugin.msgs.getMsg("prefNoExplosions").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.SoupReg) KitEinstellungen.append(plugin.msgs.getMsg("prefSoupReg").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.NoArrowPickup) KitEinstellungen.append(plugin.msgs.getMsg("prefNoArrowPickup").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.SoupNoob) KitEinstellungen.append(plugin.msgs.getMsg("prefSoupNoob").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.DoubleJump) KitEinstellungen.append(plugin.msgs.getMsg("prefDoubleJump").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.NoHitDelay) KitEinstellungen.append(plugin.msgs.getMsg("prefNoHitDelay").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.NoRegneration) KitEinstellungen.append(plugin.msgs.getMsg("prefNoRegeneration").replaceAll("&", "§"));
				   if(prefs == PlayerPrefs.NoItemDrops) KitEinstellungen.append(MessageReplacer.replaceStrings(plugin.msgs.getMsg("prefNoItemDrops")));
				   if(prefs == PlayerPrefs.NoFriendlyFire) KitEinstellungen.append(MessageReplacer.replaceStrings(plugin.msgs.getMsg("prefNoFriendlyFire")));
				   if(prefs == PlayerPrefs.WaterDamage) KitEinstellungen.append(MessageReplacer.replaceStrings(plugin.msgs.getMsg("prefWaterDmg")));
				 }		   
				}  
			   }
		  
		  
		  if(KitEinstellungen.toString().equalsIgnoreCase("")) {
			  KitEinstellungen.append(plugin.msgs.getMsg("noKitAuthor").replaceAll("&", "§")); 
		  } else {
			  KitEinstellungen = new StringBuilder().append(KitEinstellungen.toString().replaceFirst("§7, ", ""));
		  }
		  
		  String Kitauthor = new KitManager(plugin).getkitAuthor("" + Name);
		  if(Kitauthor == null) {
			  Kitauthor = plugin.msgs.getMsg("noKitAuthor").replaceAll("&", "§");
		  }
		  
		  p.sendMessage(MessageReplacer.replaceStringsKit(plugin.msgs.getMsg("kitMessage"), p.getDisplayName(), Kitauthor, KitEinstellungen.toString()));
	}
}
