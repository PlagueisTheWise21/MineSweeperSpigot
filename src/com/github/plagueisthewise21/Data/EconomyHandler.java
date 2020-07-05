package com.github.plagueisthewise21.Data;

import org.bukkit.plugin.RegisteredServiceProvider;

import com.github.plagueisthewise21.Source;

import net.milkbowl.vault.economy.Economy;

public class EconomyHandler{
	
	private Source plugin;
	private boolean enabled;
	private Economy econ;
	public EconomyHandler(Source plugin) {
		this.plugin = plugin;
		
		this.enabled = this.setup();
	}
	
	private boolean setup() {
        if (this.plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = this.plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        
        this.econ = rsp.getProvider();
        return this.econ != null;
    }
	
	public Economy getEconomy() {
		return this.econ;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}

}
