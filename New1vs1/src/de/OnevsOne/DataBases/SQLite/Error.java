package de.OnevsOne.DataBases.SQLite;

import java.util.logging.Level;

import de.OnevsOne.main;

public class Error {
	public static void execute(main plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(main plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}
