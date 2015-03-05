package me.belka.legendcoins;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExchangeCommand implements CommandExecutor {

	private static int rateSell;
	private static int rateBuy;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("change")) {
			
			if(sender instanceof Player) {
			
			EconomyManager em = new EconomyManager();
			Player p = (Player) sender;
			
			if(EconomyMain.getEconomy() == null) {
				p.sendMessage("§c§lError: §cVault plugin not found!");
				return false;
			}
			
			if(args.length == 0) {
				
				if(!sender.hasPermission("legendcoins.change.help")) {
					sender.sendMessage("§cYou have no permission to do this!");
					return false;
				}
				
				sender.sendMessage("§8§m-------------------------");
				sender.sendMessage(" ");
				sender.sendMessage("§a§lWelcome to the coins changer!");
				sender.sendMessage(" ");
				sender.sendMessage("§e/change - Help menu");
				sender.sendMessage("§e/change help - Help menu");
				sender.sendMessage("§e/change coins - Change coins to money");
				sender.sendMessage("§e/change money - Change money to coins");
				sender.sendMessage(" ");
				sender.sendMessage("§8§m-------------------------");
			}
			
			if(args.length == 1) {
				
				if(args[0].equalsIgnoreCase("help")) {
					
					if(!sender.hasPermission("legendcoins.change.help")) {
						sender.sendMessage("§cYou have no permission to do this!");
						return false;
					}
					
					sender.sendMessage("§8§m-------------------------");
					sender.sendMessage(" ");
					sender.sendMessage("§a§lWelcome to the coins changer!");
					sender.sendMessage(" ");
					sender.sendMessage("§e/change - Help menu");
					sender.sendMessage("§e/change help - Help menu");
					sender.sendMessage("§e/change coins - Change coins to money");
					sender.sendMessage("§e/change money - Change money to coins");
					sender.sendMessage(" ");
					sender.sendMessage("§8§m-------------------------");
				}
				
				if(args[0].equalsIgnoreCase("coins")) {
					
					if(!sender.hasPermission("legendcoins.change.coins")) {
						sender.sendMessage("§cYou have no permission to do this!");
						return false;
					}
					
					int money = em.getPlayerByName(p.getName()).getBalance();
					int give = money * rateSell;
					
					if(money <= 0) {
						p.sendMessage("§cYou haven't got enough coins to change!");
						return false;
					}
					
					EconomyResponse r = EconomyMain.getEconomy().depositPlayer(p, give);
					EconomyPlayer ep = em.getPlayerByName(p.getName());
					
		            if(r.transactionSuccess()) {
		            	p.sendMessage(String.format("§8[§6LegendCoins§8] §fYou changed §a§l" + ep.getBalance() + " §fcoins to money for §a§l" + rateSell +"§f! Your money balance now: §a§l%s§f!", EconomyMain.getEconomy().format(r.balance)));
		            } else {
		                Bukkit.getLogger().info(String.format("An error occured: %s", r.errorMessage));
		            }
		            
					em.removeMoney(ep.getName(), ep.getBalance());
					
				}
				
				if(args[0].equalsIgnoreCase("money")) {
					
					if(!sender.hasPermission("legendcoins.change.money")) {
						sender.sendMessage("§cYou have no permission to do this!");
						return false;
					}
					
					double money = EconomyMain.getEconomy().getBalance(p);
					
					if(money < rateBuy) {
						p.sendMessage("§cYou haven't got enough money to change!");
						return false;
					}
					
					int give = (int) (money / rateBuy);
					int get = give * rateBuy;
					
					EconomyResponse r = EconomyMain.getEconomy().withdrawPlayer(p, get);
					
					EconomyPlayer ep = em.getPlayerByName(p.getName());
					em.addMoney(ep.getName(), give);
					
		            if(r.transactionSuccess()) {
		            	p.sendMessage(String.format("§8[§eLegendCoins§8] §fYou changed §a§l%s §fmoney to coins for §a§l" + rateBuy + "§f! Coins balance now: §a§l" + ep.getBalance() + "§f!", EconomyMain.getEconomy().format(r.amount)));
		            } else {
		                Bukkit.getLogger().info(String.format("An error occured: %s", r.errorMessage));
		            }
					
				}
				}	
			}
		}
 		return false;
	}

	public static int getRateBuy() {
		return rateBuy;
	}

	public static void setRateBuy(int rateBuy) {
		ExchangeCommand.rateBuy = rateBuy;
	}

	public static int getRateSell() {
		return rateSell;
	}

	public static void setRateSell(int rateSell) {
		ExchangeCommand.rateSell = rateSell;
	}

}
