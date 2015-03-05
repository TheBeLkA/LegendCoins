package me.belka.legendcoins;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class SQLUtil
{
  public static Connection connection = null;
  public static ResultSet resultSet = null;
  private final String dbLocation;
  private final Plugin plugin;

  public SQLUtil(Plugin plugin, String dbLocation) {
	   this.plugin = plugin;
	   this.dbLocation = dbLocation;
  }
  
  public Connection openConnection() {
    try {
      if (!plugin.getDataFolder().mkdirs()) {
        plugin.getDataFolder().mkdirs();
      }
      String backend = plugin.getConfig().getString("backend");
      
      if (backend.equalsIgnoreCase("sqlite")) {
        Class.forName("org.sqlite.JDBC").newInstance();
        connection = DriverManager.getConnection("jdbc:sqlite://" + plugin.getDataFolder().getAbsolutePath() + "/" + dbLocation);
      } else if (backend.equalsIgnoreCase("mysql")){
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        connection = DriverManager.getConnection("jdbc:mysql://" + plugin.getConfig().getString("mysql.host") + 
        ":" + plugin.getConfig().getString("mysql.port") + "/" + plugin.getConfig().getString("mysql.database") + 
        "?useUnicode=true&characterEncoding=UTF-8&" + "user=" + plugin.getConfig().getString("mysql.user") + 
        "&password=" + plugin.getConfig().getString("mysql.pass"));
        
      }
      Bukkit.getLogger().info("Соединение с базой данных успешно установлено.");
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    return connection;
  }

  public boolean checkConnection() {
      try {
         return !connection.isClosed();
      } catch (Exception var1) {
         return false;
      }
   }

  public void execute(String query) {
	  if (!checkConnection()) {
		  openConnection();
	  } 

     try {
        connection.createStatement().execute(query);
     } catch (Exception var2) {
        var2.printStackTrace();
     }

  }

  public ResultSet executeQuery(String query) {
     if (!checkConnection()) {
   	  openConnection();
     }

     ResultSet rs = null;

     try {
        rs = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY).executeQuery(query);
     } catch (Exception var3) {
        var3.printStackTrace();
     }

     return rs;
  }

  public void closeConnection() {
      try {
    	  
         if(connection != null) {
            connection.close();
         }
         
      } catch (Exception var1) {
         var1.printStackTrace();
      }

   }
  
}