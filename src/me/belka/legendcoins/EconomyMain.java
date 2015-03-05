package me.belka.legendcoins;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class EconomyMain extends JavaPlugin {

	public static Connection connect;
	public static EconomyMain plugin;
	public static SQLUtil sql;
	private static Economy econ = null;
	private Random r = new Random();
	
	public void onEnable() {
		
		plugin = this;
		
		try {
			Metrics metrics = new Metrics(this); metrics.start();
		} catch (IOException e) { // Failed to submit the stats :-(
			System.out.println("Error Submitting stats!");
		}
		
		saveDefaultConfig();
		
		getCommand("coins").setExecutor(new EconomyCommand());
		getCommand("change").setExecutor(new ExchangeCommand());
		
		Bukkit.getPluginManager().registerEvents(new EconomyListener(), this);
		
		connect();
		
		if(setupEconomy()) {
			getLogger().info("Vault and Economy plugins found!");
		} else {
			getLogger().info("Vault and Economy plugins not found! Disabling Vault functions...");
		}
		
		int sell = getConfig().getInt("rate.sell");
		int buy = getConfig().getInt("rate.buy");
		
		if(sell != 0) {
			ExchangeCommand.setRateSell(sell);
		} else {
			int random = r.nextInt(20);
			random += 30;
			ExchangeCommand.setRateSell(random);
		}
		
		if(buy != 0) {
			ExchangeCommand.setRateBuy(buy);
		} else {
			int b = ExchangeCommand.getRateSell() + 5;
			ExchangeCommand.setRateBuy(b);
		}
		
		List<Integer> days = getConfig().getIntegerList("main.double-coins-days");
		
		if(days.size() == 0) {
			return;
		}
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		
		if(dayOfWeek == 0) {
			dayOfWeek = 7;
		}
		
		boolean yep = false;
		
		for(int i = 0; i < days.size(); i++) {
			if(dayOfWeek == days.get(i)) {
				EconomyManager.setDoubleCoins(true);
				yep = true;
				break;
			}
		}
		
		if(!yep) {
			EconomyManager.setDoubleCoins(false);
		}
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				Bukkit.broadcastMessage("§c§lSell rate§f§l: §f§l" + ExchangeCommand.getRateSell() + ".00");
				Bukkit.broadcastMessage("§a§lBuy rate§f§l: §f§l" + ExchangeCommand.getRateBuy() + ".00");
			}
			
		}, 0, 20 * 180);
		
	}
	
	public void onDisable() {
		try {
			connect.close();
			sql.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connect = null;
		sql = null;
	}
	
	public void connect() {
		
		SQLUtil sq = new SQLUtil(this, "coins.db");
		
		try {
			connect = sq.openConnection();
			
			sql = sq;
			
			String backend = plugin.getConfig().getString("backend");
			
			sql.execute("CREATE TABLE IF NOT EXISTS coins (id INTEGER PRIMARY KEY " + (backend.equalsIgnoreCase("sqlite") ? "AUTOINCREMENT" : "AUTO_INCREMENT") + " NOT NULL, balance INT NOT NULL , name VARCHAR(32) NOT NULL UNIQUE)");
			
			ResultSet res = connect.createStatement().executeQuery("SELECT count(*) FROM coins");
			
			if(res.next()) {
				
			int count = res.getInt(1);
			
			if(count == 0) {
				return;
			}
			
			ResultSet rs = sql.executeQuery("SELECT * FROM coins");
			
			if(rs == null) {
				return;
			}	
			
			EconomyPlayer rp = new EconomyPlayer(rs.getInt("id"), rs.getInt("balance"), rs.getString("name"));
			EconomyManager.getEconomyPlayers().add(rp);
			
			while(rs.next()) {
				EconomyPlayer ep = new EconomyPlayer(rs.getInt("id"), rs.getInt("balance"), rs.getString("name"));
				EconomyManager.getEconomyPlayers().add(ep);
			}
			
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
	
	public static Economy getEconomy() {
		return econ;
	}
	
}
