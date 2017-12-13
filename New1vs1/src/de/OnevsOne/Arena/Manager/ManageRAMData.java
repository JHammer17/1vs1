package de.OnevsOne.Arena.Manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.configuration.file.YamlConfiguration;

import de.OnevsOne.main;

/**
 * Der Code ist von JHammer
 *
 * 07.05.2016 um 13:01:26 Uhr
 */

/*
 * Diese Klasse Managed den RAM [z.B. Löschen oder bearbeiten]
 * 
 * Methoden:
 * deleteRAMAll() -> Löscht die RAM.yml von allen Arenen.
 * deleteRAM(String Name) -> Löscht die RAM.yml einer Arena.
 * saveRAM(String Name, String token, String value) -> Speichert einen Wert in die RAM.yml einer Arena. 
 * 
 */
public class ManageRAMData {

	private static main plugin;

	@SuppressWarnings("static-access")
	public ManageRAMData(main plugin) {
		this.plugin = plugin;
	}
	
	//deleteRAMAll(): Lässt die RAM.yml alle Arenen löschen.
	public void deleteRAMAll() {
		YamlConfiguration cfg = plugin.getYaml("Arenen");

		
		if (cfg.getConfigurationSection("Arenen") == null) {
			return;
		}

		for (String Arenen : cfg.getConfigurationSection("Arenen").getKeys(
				false)) {
			if (cfg.getBoolean("Arenen." + Arenen)) {
				deleteRAM(Arenen);
			}
		}
	}
	//------------------------
	
	//deleteRAM(String Name): Lässt die RAM.yml einer Arena Löschen
	public void deleteRAM(String Name) {
		File file = plugin.getPluginFile("Arenen/" + Name + "/RAM");
		file.delete();
	}
	//---------------------------

	//saveRAM(String Name, String token, String value): Speichert einen Wert in die RAM.yml einer Arena
	public void saveRAM(String Name, String token, Object value) {
		YamlConfiguration cfg = plugin.getYaml("Arenen/" + Name + "/RAM");

		cfg.set("RAM." + token, value);

		try {
			cfg.save(plugin.getPluginFile("Arenen/" + Name + "/RAM"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

	}
	
	//------------------------------------------

	/**
	Checks if RAM.yml exists
	@param Arena: A Arena
	@return If the RAM.yml exists
	@throws Nothing
	*/
	public boolean existsRAM(String Arena) {
		if (plugin.existFile("Arenen/" + Arena + "/RAM")) return true;
		return false;
	}
	//---------------------------------------
	
	public boolean isACSArena(String Name) {
		YamlConfiguration cfg = plugin.getYaml("Arenen/" + Name + "/config");
		if(cfg == null) return false;
		
	
		if(cfg.getString("ACS.Id") == null) return false; 

		return true;
	}
	
	public String getACSID(String Name) {
		YamlConfiguration cfg = plugin.getYaml("Arenen/" + Name + "/config");
		
		if(cfg == null) return null;
		if(!isACSArena(Name)) return null;
		if(cfg.getString("ACS.Id") != null) return cfg.getString("ACS.Id");
		return null;
	}
	
	public ArrayList<String> getAllArenas(boolean mustEnabled) {
		ArrayList<String> arenas = new ArrayList<>();
		YamlConfiguration cfg = plugin.getYaml("Arenen");
		
		if(cfg.getConfigurationSection("Arenen") == null) return arenas;
		
		
		
		for(String Arenen : cfg.getConfigurationSection("Arenen").getKeys(false))
		 if(!mustEnabled || cfg.getBoolean("Arenen." + Arenen)) arenas.add(Arenen);
		
		return arenas;
	}
	
	
	
}
