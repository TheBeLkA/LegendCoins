package me.belka.legendcoins;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class EconomyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(cmd.getName().equalsIgnoreCase("coins")) {
				
				EconomyManager em = new EconomyManager();
				
				if(args.length == 0) {
					
					if(!sender.hasPermission("legendcoins.help")) {
						sender.sendMessage("§cYou have no permission to do this!");
						return false;
					}
					
					sender.sendMessage("§8§m-------------------------");
					sender.sendMessage(" ");
					sender.sendMessage("§6LegendCoins §8help");
					sender.sendMessage(" ");
					sender.sendMessage("§e/coins - Help menu");
					sender.sendMessage("§e/coins help - Help menu");
					sender.sendMessage("§e/coins get <NAME> - Returns <NAME> balance");
					sender.sendMessage("§e/coins delete <NAME> - Deleting <NAME> account");
					sender.sendMessage("§e/coins double-coins <TRUE/FALSE> - Setting double-coins mode");
					sender.sendMessage("§e/coins set <NAME> <MONEY> - Setting <NAME> balance");
					sender.sendMessage("§e/coins add <NAME> <MONEY> - Adding <NAME> some money");
					sender.sendMessage("§e/coins remove <NAME> <MONEY> - Removing <NAME> some money");
					sender.sendMessage(" ");
					sender.sendMessage("§8§m-------------------------");
					return false;
				}
				
				if(args.length == 1) {
					
					if(!sender.hasPermission("legendcoins.help")) {
						sender.sendMessage("§cYou have no permission to do this!");
						return false;
					}
					
					sender.sendMessage("§8§m-------------------------");
					sender.sendMessage(" ");
					sender.sendMessage("§6LegendCoins §8help");
					sender.sendMessage(" ");
					sender.sendMessage("§e/coins - Help menu");
					sender.sendMessage("§e/coins help - Help menu");
					sender.sendMessage("§e/coins get <NAME> - Returns <NAME> balance");
					sender.sendMessage("§e/coins delete <NAME> - Deleting <NAME> account");
					sender.sendMessage("§e/coins double-coins <TRUE/FALSE> - Setting double-coins mode");
					sender.sendMessage("§e/coins set <NAME> <MONEY> - Setting <NAME> balance");
					sender.sendMessage("§e/coins add <NAME> <MONEY> - Adding <NAME> some money");
					sender.sendMessage("§e/coins remove <NAME> <MONEY> - Removing <NAME> some money");
					sender.sendMessage(" ");
					sender.sendMessage("§8§m-------------------------");
					return false;
					
				}
				
				if(args.length == 2) {
					if(args[0].equalsIgnoreCase("get")) {
						
						if(args[1].equalsIgnoreCase(sender.getName())) {
							
							if(!sender.hasPermission("legendcoins.get")) {
								sender.sendMessage("§cYou have no permission to do this!");
								return false;
							}
						
							if(em.getPlayerByName(args[1]) == null) {
								sender.sendMessage("§cThis user doesn't exist!");
								return false;
							}
							
							sender.sendMessage("§b§lBalance§r: §a" + em.getPlayerByName(args[1]).getBalance());
							
						} else {
							
							if(!sender.hasPermission("legendcoins.others.get")) {
								sender.sendMessage("§cYou have no permission to do this!");
								return false;
							}
							
							if(em.getPlayerByName(args[1]) == null) {
								sender.sendMessage("§cThis user doesn't exist!");
								return false;
							}
						
							sender.sendMessage("§b§lBalance§r: §a" + em.getPlayerByName(args[1]).getBalance());
							
						}
					}
					
					if(args[0].equalsIgnoreCase("double-coins")) {
						
						if(!sender.hasPermission("legendcoins.admin.double")) {
							sender.sendMessage("§cYou have no permission to do this!");
							return false;
						}
						
						boolean dcoins = Boolean.parseBoolean(args[1]);
						EconomyManager.setDoubleCoins(dcoins);
						sender.sendMessage("§6Double-Coins now set to §a" + dcoins);
					}
					
					if(args[0].equalsIgnoreCase("delete")) {
						
						if(!sender.hasPermission("legendcoins.admin.delete")) {
							sender.sendMessage("§cYou have no permission to do this!");
							return false;
						}
						
						if(em.getPlayerByName(args[1]) == null) {
							sender.sendMessage("§cThis user doesn't exist!");
							return false;
						}
						
						em.removeAccount(args[1]);
					}
					
				}
				
				if(args.length == 3) {
					if(args[0].equalsIgnoreCase("set")) {
						
						if(!sender.hasPermission("legendcoins.admin.set")) {
							sender.sendMessage("§cYou have no permission to do this!");
							return false;
						}
						
						if(em.getPlayerByName(args[1]) == null) {
							sender.sendMessage("§cThis user doesn't exist!");
							return false;
						}
						
						em.setMoney(args[1], Integer.parseInt(args[2]));
					}
					
					if(args[0].equalsIgnoreCase("add")) {
						
						if(!sender.hasPermission("legendcoins.admin.add")) {
							sender.sendMessage("§cYou have no permission to do this!");
							return false;
						}
						
						if(em.getPlayerByName(args[1]) == null) {
							sender.sendMessage("§cThis user doesn't exist!");
							return false;
						}
						
						em.addMoney(args[1], Integer.parseInt(args[2]));
					}
					
					if(args[0].equalsIgnoreCase("remove")) {
						
						if(!sender.hasPermission("legendcoins.admin.remove")) {
							sender.sendMessage("§cYou have no permission to do this!");
							return false;
						}
						
						if(em.getPlayerByName(args[1]) == null) {
							sender.sendMessage("§cThis user doesn't exist!");
							return false;
						}
						
						em.removeMoney(args[1], Integer.parseInt(args[2]));
					}
				}
			}
		return false;
	}

}
