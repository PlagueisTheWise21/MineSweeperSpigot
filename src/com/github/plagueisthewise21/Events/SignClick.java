package com.github.plagueisthewise21.Events;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignClick implements Listener {
	
	@EventHandler
	public void onSignClick(PlayerInteractEvent e) {
		if (!(e.getClickedBlock().getState() instanceof Sign)) {
			return;
		}

		Sign sign = (Sign) e.getClickedBlock().getState();
		String line_0 = ChatColor.RESET + sign.getLine(0);
		
		if (!line_0.equalsIgnoreCase("[minesweeper]")) {
			return;
		}
		
		Player p = e.getPlayer();
		
		// Start game...
	}

}
