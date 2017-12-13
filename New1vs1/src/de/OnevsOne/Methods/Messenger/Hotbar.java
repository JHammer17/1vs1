package de.OnevsOne.Methods.Messenger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Der Code ist von JHammer
 *
 * 07.05.2016 um 14:55:34 Uhr
 */

//net.minecraft.server.v1_9_R2.
public class Hotbar {

	
	public static void send(Player p, String msg) {
		if (getNMSVersion().startsWith("v1_12_")) {
			sendActionBar1_12(p, msg);
			return;
		}
		
		 // Title or subtitle, text, fade in (ticks), display time (ticks), fade out (ticks).
       try {
    	  
   		Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + msg + "\"}");
   		Constructor<?> titleConstructor = getNMSClass("PacketPlayOutChat").getConstructor(getNMSClass("IChatBaseComponent"),byte.class);
   	    Object packet = titleConstructor.newInstance(chat,(byte) 2);
   		sendPacket(p,packet);
       } catch (Exception e1) {
        e1.printStackTrace();
       }
	}
	
	
    public static void sendPacket(Player player, Object packet) {
	 try {
      Object handle = player.getClass().getMethod("getHandle").invoke(player);
      Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
      playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
	 } catch (Exception e) {
      e.printStackTrace();
	 }
	}

	public static Class<?> getNMSClass(String name) {
	 String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	 try {
      return Class.forName("net.minecraft.server." + version + "." + name);
	 } catch (ClassNotFoundException e) {
      e.printStackTrace();
      return null;
	 }
	}
	
	public static String getNMSVersion() {
	 return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}
	
	private static void sendActionBar1_12(Player player, String message)
	  {
	   
	    try {
	      Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + getNMSVersion() + ".entity.CraftPlayer");
	      Object craftPlayer = craftPlayerClass.cast(player);
	      
	      Class<?> c4 = Class.forName("net.minecraft.server." + getNMSVersion() + ".PacketPlayOutChat");
	      Class<?> c5 = Class.forName("net.minecraft.server." + getNMSVersion() + ".Packet");
	      Class<?> c2 = Class.forName("net.minecraft.server." + getNMSVersion() + ".ChatComponentText");
	      Class<?> c3 = Class.forName("net.minecraft.server." + getNMSVersion() + ".IChatBaseComponent");
	      Class<?> chatMessageTypeClass = Class.forName("net.minecraft.server." + getNMSVersion() + ".ChatMessageType");
	      Object[] chatMessageTypes = chatMessageTypeClass.getEnumConstants();
	      Object chatMessageType = null;
	      for (Object obj : chatMessageTypes) 
	        if (obj.toString().equals("GAME_INFO")) chatMessageType = obj;
	          
	        
	      Object o = c2.getConstructor(new Class[] { String.class }).newInstance(new Object[] { message });
	      Object ppoc = c4.getConstructor(new Class[] { c3, chatMessageTypeClass }).newInstance(new Object[] { o, chatMessageType });
	      Method m1 = craftPlayerClass.getDeclaredMethod("getHandle", new Class[0]);
	      Object h = m1.invoke(craftPlayer, new Object[0]);
	      Field f1 = h.getClass().getDeclaredField("playerConnection");
	      Object pc = f1.get(h);
	      Method m5 = pc.getClass().getDeclaredMethod("sendPacket", new Class[] { c5 });
	      m5.invoke(pc, new Object[] { ppoc });
	    }
	    catch (Exception ex) {
	      ex.printStackTrace();
	    }
	  }
	
	
}
