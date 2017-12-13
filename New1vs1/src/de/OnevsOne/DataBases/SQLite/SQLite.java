package de.OnevsOne.DataBases.SQLite;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import de.OnevsOne.DataBases.SQLite.Database; // import the database class.
import de.OnevsOne.main; // import your main class

public class SQLite {
	    String dbname;
		private main plugin;
	    public SQLite(main plugin){
	        this.plugin = plugin;
	        dbname = "KitDatabase";
	    }

	    public String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS KitDatabase (" + // make sure to put your table name in here too.
	            "`PlayerName` varchar(100) NOT NULL," + // This creates the different colums you will save data too. varchar(32) Is a string, int = integer
	            "`UUID` varchar(100) NOT NULL," +
	            "`KitInv` longtext NOT NULL," +
	            "`KitArmor` longtext NOT NULL," +
	            "`Settings` varchar(100) NOT NULL," +
	            "`QuequePrefs` varchar(150) NOT NULL," +
	            "`KitInv2` longtext NOT NULL," +
	            "`KitArmor2` longtext NOT NULL," +
	            "`KitSettings2` longtext NOT NULL," +
	            "`KitInv3` longtext NOT NULL," +
	            "`KitArmor3` longtext NOT NULL," +
	            "`KitSettings3` longtext NOT NULL," +
	            "`KitInv4` longtext NOT NULL," +
	            "`KitArmor4` longtext NOT NULL," +
	            "`KitSettings4` longtext NOT NULL," +
	            "`KitInv5` longtext NOT NULL," +
	            "`KitArmor5` longtext NOT NULL," +
	            "`KitSettings5` longtext NOT NULL," +
	            "`Fights` varchar(150) NOT NULL," +
	            "`FightsWon` varchar(150) NOT NULL," + //20
	            "`DefaultKit` varchar(150) NOT NULL," + //20
	            "`DisabledMaps` longtext," +
	            "`RankPoints` longtext," +
	            "`Fights30` longtext," +
	            "`FightsWon30` longtext," +
	            "`Kit1Plays` longtext," +
	            "`Kit1Plays30` longtext," +
	            "`Kit2Plays` longtext," +
	            "`Kit2Plays30` longtext," +
	            "`Kit3Plays` longtext," +
	            "`Kit3Plays30` longtext," +
	            "`Kit4Plays` longtext," +
	            "`Kit4Plays30` longtext," +
	            "`Kit5Plays` longtext," +
	            "`Kit5Plays30` longtext," +
	            "`Kit1Plays24h` longtext," +
	            "`Kit2Plays24h` longtext," +
	            "`Kit3Plays24h` longtext," +
	            "`Kit4Plays24h` longtext," +
	            "`Kit5Plays24h` longtext," +
	            "PRIMARY KEY (`UUID`)" +  
	            ");";


	    
	    public Connection getSQLConnection() {
	        File dataFolder = new File(plugin.getDataFolder(), dbname+".db");
	        if (!dataFolder.exists()){
	            try {
	                dataFolder.createNewFile();
	            } catch (IOException e) {
	                plugin.getLogger().log(Level.SEVERE, "File write error: "+dbname+".db");
	            }
	        }
	        try {
	            if(Database.connection!=null&&!Database.connection.isClosed()){
	                return Database.connection;
	            }
	            Class.forName("org.sqlite.JDBC");
	            Database.connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
	            return Database.connection;
	        } catch (SQLException ex) {
	            plugin.getLogger().log(Level.SEVERE,"SQLite exception on initialize", ex);
	        } catch (ClassNotFoundException ex) {
	            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
	        }
	        return null;
	    }

	    public void load() {
	    	new Database(plugin);
	    	Database.connection = getSQLConnection();
	        try {
	            Statement s = Database.connection.createStatement();
	            s.executeUpdate(SQLiteCreateTokensTable);
	            s.close();
	        } catch (SQLException e) {
	            
	        }
	        Database.initialize();
	    }
}
