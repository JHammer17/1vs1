package de.OnevsOne.Methods.Messenger;


import java.lang.reflect.Constructor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Der Code ist von JHammer
 *
 * 11.05.2016 um 22:34:01 Uhr
 */
public class TitleAPI {

	
	public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
		 // Title or subtitle, text, fade in (ticks), display time (ticks), fade out (ticks).
      try {
   	   		  
              Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
              Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
             
              Object SubenumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
              Object Subchat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
              
              Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
              Object packet = titleConstructor.newInstance(enumTitle, chat, fadeIn, stay, fadeOut);
             
              Constructor<?> SubtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
              Object Subpacket = SubtitleConstructor.newInstance(SubenumTitle, Subchat, fadeIn, stay, fadeOut);
              
              sendPacket(player, packet);
              sendPacket(player, Subpacket);
      }
     
      catch (Exception e1) {
              e1.printStackTrace();
      	}
		}
	

	    public static void sendPacket(Player player, Object packet) {
		try {
        Object handle = player.getClass().getMethod("getHandle").invoke(player);
        Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
        playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
		}

		 catch (Exception e) {
       	e.printStackTrace();
		 }
		}

		public static Class<?> getNMSClass(String name) {
		// org.bukkit.craftbukkit.v1_8_R3...
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
        return Class.forName("net.minecraft.server." + version + "." + name);
		}

		catch (ClassNotFoundException e) {
        e.printStackTrace();
       return null;
			}
		}
	
	/*public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
		 
	    PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
	    
	    PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn.intValue(), stay.intValue(), fadeOut.intValue());
	    connection.sendPacket(packetPlayOutTimes);
	    if (subtitle != null) {
	      subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
	      subtitle = subtitle.replaceAll("&", "§");
	      IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
	      PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);
	      connection.sendPacket(packetPlayOutSubTitle);
	    }
	    if (title != null) {
	      title = title.replaceAll("%player%", player.getDisplayName());
	      title = title.replaceAll("&", "§");
	      IChatBaseComponent titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
	      PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleMain);
	      connection.sendPacket(packetPlayOutTitle);
	    }
	  }*/
}
