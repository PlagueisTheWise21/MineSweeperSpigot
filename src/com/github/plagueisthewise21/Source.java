package com.github.plagueisthewise21;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.plagueisthewise21.Command.OpenUI;
import com.github.plagueisthewise21.Data.EconomyHandler;
import com.github.plagueisthewise21.Events.PlayGame;
import com.github.plagueisthewise21.Events.SignCreate;

public class Source extends JavaPlugin implements Listener{

	private EconomyHandler economy;
	
	public void onEnable() {
		economy = new EconomyHandler(this);
		
        getConfig().options().copyDefaults(true);
		saveConfig();
		
		Bukkit.getPluginCommand("minesweeper").setExecutor(new OpenUI(this));
		
		Bukkit.getPluginManager().registerEvents(new PlayGame(this), this);
		Bukkit.getPluginManager().registerEvents(new SignCreate(this), this);
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	public void onDisable() {
		
	}
	
	public EconomyHandler getEconomy() {
		return this.economy;
	}
	
	@EventHandler
	public void onJoin(PlayerInteractEvent e) {
		
		
		
	}

}
