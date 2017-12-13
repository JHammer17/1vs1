package de.OnevsOne.Methods;

import org.bukkit.Bukkit;
import de.OnevsOne.main;

public class SimpleAsync {

	private Runnable rb;
	private main plugin;
	
	
	public SimpleAsync(Runnable rb, main plugin) {
		this.rb = rb;
		this.plugin = plugin;
	}
	
	public void start() {
		if(rb == null) return;
		Bukkit.getScheduler().runTaskAsynchronously(plugin, rb);
	}
}
