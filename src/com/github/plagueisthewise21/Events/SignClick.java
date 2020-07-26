package com.github.plagueisthewise21.Events;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.github.plagueisthewise21.Source;
import com.github.plagueisthewise21.Command.OpenUI;

public class SignClick implements Listener {
	
	protected Source plugin;
	public SignClick(Source plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onSignClick(PlayerInteractEvent e) {
		if (!(e.getClickedBlock().getState() instanceof Sign)) {
			return;
		}
		Sign sign = (Sign) e.getClickedBlock().getState();
		String line_0 = sign.getLine(0);
		
		if (!line_0.equalsIgnoreCase(ChatColor.GRAY + "[" + ChatColor.AQUA + "MineSweeper" + ChatColor.GRAY + "]")) {
			System.out.println("HELLO");
			return;
		}
		
		Player player = e.getPlayer();
		
		// Start game...
		OpenUI game = new OpenUI(plugin);
		game.startGame(player, plugin.getConfig().getInt("default-bombs"));
	}

}
