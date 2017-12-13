package de.OnevsOne.MessageManager;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import com.google.common.io.Files;

import de.OnevsOne.main;

public class NewMsgLoader {

	
	private main plugin;

	public NewMsgLoader(main plugin) {
		this.plugin = plugin;
	}
	
	private HashMap<String, String> messages = new HashMap<>();
	
	
	public void reloadAllMessages() {
		File file = plugin.getPluginFile("language");
		
		YamlConfiguration cfg = new YamlConfiguration();
		
		String defaultPoint = "Messages";
		
		if(file.exists()) {
			
				try {
					cfg.loadFromString(Files.toString(file, Charset.forName("UTF-8")));
				} catch (InvalidConfigurationException e) {
					plugin.getLogger().log(Level.WARNING, "A error eccourd while loading language file!", e);
					return;
				} catch (Exception e) {
					plugin.getLogger().log(Level.WARNING, "A error eccourd while loading language file!", e);
					return;
				}
			
		}
		
		boolean forceReCreate = false;
		
		
		cfg.options().header("======================================== #\n		1vs1 - Like Timolia	[Messages]	   #\n 		 		by JHammer17	 		   #\n======================================== #");
		cfg.options().copyHeader(true);
		
		if(cfg.getConfigurationSection(defaultPoint) == null) {
			plugin.createLanguage(true);
			reloadAllMessages();
			return;
		}

		plugin.prefix = cfg.getString(defaultPoint + ".prefix").replaceAll("&", "§");
		
		for(String messageTyp : cfg.getConfigurationSection(defaultPoint).getKeys(false)) {
			
			String msg = cfg.getString(defaultPoint + "." + messageTyp);
			msg = ChatColor.translateAlternateColorCodes('&', msg).replaceAll("%n", "\n");
			msg = msg.replaceAll("%Prefix%", plugin.prefix);
			messages.put(messageTyp, msg);
			
		}
		
		
		if(messages.size() < 571) {forceReCreate = true;}
		
		
		plugin.prefix = messages.get("prefix");
		plugin.noPerms = messages.get("noPerms");
		plugin.tournamentPrefix = messages.get("tournamentPrefix");
		
		
	
		
		if(forceReCreate) {
			plugin.createLanguage(true);
			messages.clear();
			reloadAllMessages();
		}
		
		
	}
	
	public String getMsg(String name) {
		if(messages.get(name) == null) return null;
		return messages.get(name).replaceAll("%Prefix%", plugin.prefix);
	}
	
}
