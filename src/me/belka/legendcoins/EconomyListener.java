package me.belka.legendcoins;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EconomyListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
		Player p = e.getPlayer();
		
		if(!p.hasPermission("legendcoins.make")) {
			return;
		}
		
		EconomyManager em = new EconomyManager();
		
		em.makeAccount(p.getName());
	}
	
}
