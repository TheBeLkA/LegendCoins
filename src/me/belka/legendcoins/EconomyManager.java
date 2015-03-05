package me.belka.legendcoins;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

public class EconomyManager {

    private static List<EconomyPlayer> eplayers = new ArrayList<>();
    private static boolean dCoins = EconomyMain.plugin.getConfig().getBoolean("main.double-coins");
    
	public static List<EconomyPlayer> getEconomyPlayers() {
		return eplayers;
	}
	
	public EconomyPlayer makeAccount(String name) {
		
		try {
			if(EconomyMain.connect.isClosed()) {
				EconomyMain.sql.openConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(getPlayerByName(name) != null) {
			return getPlayerByName(name);
		}
			
		try {
			
			String backend = EconomyMain.plugin.getConfig().getString("backend");
			
			String query = "INSERT INTO coins (id, balance, name) SELECT * FROM (SELECT null, 0, \'" + name + "\') AS tmp WHERE NOT EXISTS (SELECT name FROM coins WHERE name = \'" + name + "\') LIMIT 1";
			
			EconomyMain.sql.execute(backend.equalsIgnoreCase("sqlite") ? "INSERT INTO coins (id, balance, name) SELECT null, 0, \'" + name + "\' WHERE NOT EXISTS (SELECT id, balance, name FROM coins WHERE name = \'"+ name + "\')" : query);
			
			ResultSet rs = EconomyMain.sql.executeQuery("SELECT id FROM coins WHERE name = \'" + name + "\'");
			
			if(rs == null) {
				return null;
			}
			
			EconomyPlayer ep = new EconomyPlayer(rs.getInt(1), 0, name);
			EconomyManager.getEconomyPlayers().add(ep);
			
			return ep;
			
		} catch(Exception e1) {
			e1.printStackTrace();
		}
		
		return null;
	}
	
	public void removeAccount(String name) {
		
		try {
			if(EconomyMain.connect.isClosed()) {
				EconomyMain.sql.openConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(getPlayerByName(name) == null) {
			return;
		}
			
		try {

			EconomyMain.sql.execute("DELETE FROM coins WHERE name = \'" + name + "\'");
			EconomyPlayer ep = getPlayerByName(name);
			EconomyManager.getEconomyPlayers().remove(ep);
			
		} catch(Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private void updateBalance(String name) {
		
		try {
			if(EconomyMain.connect.isClosed()) {
				EconomyMain.sql.openConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(getPlayerByName(name) == null) {
			return;
		}
			
		try {

			ResultSet rs = EconomyMain.sql.executeQuery("SELECT * FROM coins WHERE name = \'" + name + "\'");
			
			if(rs == null) {
				return;
			}
			
			EconomyPlayer ep = getPlayerByName(name);
			
			ep.setBalance(rs.getInt("balance"));
			
		} catch(Exception e1) {
			e1.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void addMoney(String name, int money) {
		
		try {
			if(EconomyMain.connect.isClosed()) {
				EconomyMain.sql.openConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(getPlayerByName(name) == null) {
			return;
		}
			
		try {
			
			if(dCoins) {
				money = money * 2;
			}
			
			ResultSet rs = EconomyMain.sql.executeQuery("SELECT balance FROM coins WHERE name = \'" + name + "\'");
			
			if(rs == null) {
				return;
			}
			
			EconomyMain.sql.execute("UPDATE coins SET balance = " + rs.getInt("balance") + " + " + money + " WHERE name = \'" + name + "\'");
			updateBalance(name);
			
			if(dCoins) {
				Bukkit.getPlayer(name).sendMessage("§6Double-Coins day! §a§l+" + money + " §acoins");
				return;
			}
			
			Bukkit.getPlayer(name).sendMessage("§a§l+" + money + " §acoins!");
			
		} catch(Exception e1) {
			e1.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void removeMoney(String name, int money) {
		
		try {
			if(EconomyMain.connect.isClosed()) {
				EconomyMain.sql.openConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(getPlayerByName(name) == null) {
			return;
		}
			
		try {
		
			ResultSet rs = EconomyMain.sql.executeQuery("SELECT balance FROM coins WHERE name = \'" + name + "\'");
			
			if(rs == null) {
				return;
			}
			
			EconomyMain.sql.execute("UPDATE coins SET balance = " + rs.getInt("balance") + " - " + money + " WHERE name = \'" + name + "\'");
			updateBalance(name);
			
			Bukkit.getPlayer(name).sendMessage("§c§l-" + money + " §ccoins");
			
		} catch(Exception e1) {
			e1.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void setMoney(String name, int money) {
		
		try {
			if(EconomyMain.connect.isClosed()) {
				EconomyMain.sql.openConnection();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(getPlayerByName(name) == null) {
			return;
		}
			
		try {
		
			EconomyMain.sql.execute("UPDATE coins SET balance = " + money + " WHERE name = \'" + name + "\'");
			updateBalance(name);
			Bukkit.getPlayer(name).sendMessage("§b§lBalance set§r: §a" + money + " coins!");
			
		} catch(Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public static void setDoubleCoins(boolean dcoins) {
		EconomyMain.plugin.getConfig().set("main.double-coins", dcoins);
		EconomyMain.plugin.saveConfig();
		EconomyMain.plugin.reloadConfig();
		dCoins = dcoins;
	}
	
	public static boolean getDoubleCoins() {
		return dCoins;
	}
	
	public EconomyPlayer getPlayerByName(String name) {
		for(EconomyPlayer ep : eplayers) {
			if(ep.getName().equals(name)) {
				return ep;
			}
		}
		return null;
	}
	
	public EconomyPlayer getPlayerById(int id) {
		for(EconomyPlayer ep : eplayers) {
			if(ep.getId() == id) {
				return ep;
			}
		}
		return null;
	}
	
}
