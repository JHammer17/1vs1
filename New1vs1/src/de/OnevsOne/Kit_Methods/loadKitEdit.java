package de.OnevsOne.Kit_Methods;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import de.OnevsOne.main;
import de.OnevsOne.Methods.saveErrorMethod;
import de.OnevsOne.States.AllErrors;

/**
 * Der Code ist von JHammer
 *
 * 06.05.2016 um 10:03:34 Uhr
 */
public class loadKitEdit {

	private static main plugin;
	
	@SuppressWarnings("static-access")
	public loadKitEdit(main plugin) {
		this.plugin = plugin;
	}

	public static void loadKitEditRegion() {
		YamlConfiguration cfg = plugin.getYaml("spawns");
		
		Location Min = new Location(Bukkit.getWorld("world"), 0, 0, 0);
		Location Max = new Location(Bukkit.getWorld("world"), 0, 0, 0);
		double Minx = cfg.getDouble("KitEdit.LX");
		double Miny = cfg.getDouble("KitEdit.LY");
		double Minz = cfg.getDouble("KitEdit.LZ");
		
		double Maxx = cfg.getDouble("KitEdit.HX");
		double Maxy = cfg.getDouble("KitEdit.HY");
		double Maxz = cfg.getDouble("KitEdit.HZ");
		
		String worldname = cfg.getString("KitEdit.world");
				
		if(worldname == null) {
			saveErrorMethod.saveError(AllErrors.World, loadKitEdit.class.getName(), "Die Kit-Editor Region wurde nicht richtig gesetzt");
			return;
		}
		World welt = Bukkit.getWorld(worldname);
				
				
		Min.setX(Minx);
		Min.setY(Miny);
		Min.setZ(Minz);
		
		Max.setX(Maxx);
		Max.setY(Maxy);
		Max.setZ(Maxz);
		
		Max.setWorld(welt);
		Min.setWorld(welt);
		
		plugin.KitEditor1 = Min;
		plugin.KitEditor2 = Max;
	}
	
}
