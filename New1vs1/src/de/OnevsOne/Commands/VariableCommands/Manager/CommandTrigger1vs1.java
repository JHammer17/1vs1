package de.OnevsOne.Commands.VariableCommands.Manager;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CommandTrigger1vs1 extends Event implements Cancellable {

	private boolean cancelled = false;
	private final static HandlerList handlers = new HandlerList();
	private Player player;
	private String[] args;
	private String command;
	
	
	public CommandTrigger1vs1(Player player, String[] args, String command) {
		this.player = player;
		this.args = args;
		this.command = command;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public HandlerList getHandlers() {
		return this.handlers;
	}

	public static HandlerList getHandlerList() {
        return handlers;
    }

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public String[] getArgs() {
		return this.args;
	}
	
	public String getCMD() {
		return this.command;
	}

}
