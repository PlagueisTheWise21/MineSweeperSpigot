package com.github.plagueisthewise21.Events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.github.plagueisthewise21.Source;

public class SignCreate implements Listener {

	protected Source plugin;
	public SignCreate (Source plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onSignCreate(SignChangeEvent e) {
		
		// Line is correct?
		if (!e.getLine(0).equalsIgnoreCase("[minesweeper]")) {
			return;
		}
		
		// Player has permission?
		Player player = e.getPlayer();
		if (!player.hasPermission("minesweeper.sign.create")) {
			player.sendMessage(ChatColor.DARK_RED + "You are not allowed to do this!");
			return;
		}
		
		e.setLine(0, ChatColor.GRAY + "[" + ChatColor.AQUA + "MineSweeper" + ChatColor.GRAY + "]");
		e.setLine(1, ChatColor.WHITE + "Click to play!");
		
		if (this.plugin.getEconomy().isEnabled()) {
			e.setLine(2, ChatColor.DARK_RED + this.plugin.getConfig().getString("cost.vault.amount") + " " + this.plugin.getConfig().getString("cost.vault.currency"));
		}
		
		player.sendMessage(ChatColor.GREEN + "You have created a MineSweeper sign!");
		
	}

	
}
