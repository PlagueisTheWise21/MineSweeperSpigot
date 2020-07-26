package com.github.plagueisthewise21.Command;

import java.util.List; 

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.plagueisthewise21.Source;
import com.github.plagueisthewise21.Data.MineSweeper;

import net.milkbowl.vault.economy.EconomyResponse;


public class OpenUI extends MineSweeper implements CommandExecutor{
	
	private Source plugin;
	public OpenUI(Source main) {
		this.plugin = main;
	}

	@Override
	public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args) {

		if(sender instanceof Player) {
			Player player = (Player) sender;

			if(args.length == 1) {
				if (!player.hasPermission("minesweeper.use.amount")) {
					player.sendMessage(ChatColor.DARK_RED + "You do not have permission to do this!");
					return false;
				}
				
				try {
					int bombs = Integer.parseInt(args[0]);

					if(bombs > 0 && bombs < 54) {
						//Begin game of minesweeper					
						this.startGame(player, bombs);

					}else {
						player.sendMessage(ChatColor.DARK_RED + "Please enter a number between 0 and 53");
					}

				}catch(NumberFormatException e) {
					player.sendMessage(ChatColor.DARK_RED + "Make sure you are using a number, nothing else");

				}
			} else if (args.length == 0) {
				//Begin game of minesweeper		
				int bombs = plugin.getConfig().getInt("default-bombs");
				this.startGame(player, bombs);
			} else {
				player.sendMessage(ChatColor.RED + "Usage: /minesweeper (<amount of mines>)");
			}

		} else {
			sender.sendMessage("You are not able to execute this command!");
		}
		
		
		return false;
	}
	
	@SuppressWarnings({ "deprecation", "unused" })
	public void startGame(Player player, int bombs) {
		
		// Permission check
		if (!player.hasPermission("minesweeper.use")) {
			player.sendMessage(ChatColor.DARK_RED + "You do not have permission to do this!");
			return;
		}
		
		// Check buy in available
		if(plugin.getConfig().getBoolean("buy-in.enable")) {
			
			String buy_in_type = plugin.getConfig().getString("buy-in.type");
			
			if (buy_in_type.equalsIgnoreCase("items")) {
			    List<String> list = plugin.getConfig().getStringList("cost.items");
			    
				if(!this.hasInInv(player, list)) {
					player.sendMessage(ChatColor.DARK_RED + "You do not have enough buy in");
					return;
				}
				
				for(ItemStack i : player.getInventory().getContents()) {
					// Setting a solid amount on 1 for now, customisable in the feature?
					i.setAmount(1);
					
					// Remove the found items
					if(i != null) {
			            boolean found = false;
			            for(String st : list) {
			                if(st.startsWith("" + i.getType().getId())) {
			                    if(st.startsWith(i.getType().getId() + ":")) {
			                        try {
			                            byte data = Byte.parseByte(st.replaceFirst(i.getType().getId() + ":", ""));
			                            if(data == i.getDurability()) {
			                            	player.getInventory().remove(i);;
			                            }
			                        } catch(Exception ex) {
			                        	System.out.println("Error in Config: " + st + " is not a valid Item");
			                        	return;
			                        }
			                    }
			                }
			            }
					}
				}
			} else if (buy_in_type.equalsIgnoreCase("vault")) {
				// Buy in of VAULT
				
				// Plugin enabled?
				if (!this.plugin.getEconomy().isEnabled()) {
					System.out.println("The buy in type is VAULT, but Vault is not enabled as plugin!");
					plugin.getServer().getPluginManager().disablePlugin(plugin);
					return;
				}
				
				// Has amount on bank account?
				if(!this.plugin.getEconomy().getEconomy().has(player, plugin.getConfig().getDouble("cost.vault.amount"))) {
					player.sendMessage(ChatColor.DARK_RED + "You do not have " + plugin.getConfig().getDouble("cost.vault.amount") + " " + plugin.getConfig().getString("cost.vault.currency") + " to buy in");
					return;
				}
				
				// Withdraw succesful?
				EconomyResponse response = this.plugin.getEconomy().getEconomy().withdrawPlayer(player, plugin.getConfig().getDouble("cost.vault.amount"));
				if (!response.transactionSuccess()) {
					player.sendMessage(ChatColor.DARK_RED + "You do not have " + plugin.getConfig().getDouble("cost.vault.amount") + " to buy in");
					return;
				}
			} else {
				player.sendMessage(ChatColor.DARK_RED + "The configuration of this plugin is wrong or outdated! Please check 'buy-in.type' in config");
			}
		}
		
		//Begin game of minesweeper	

		//Generate template to run in background
		minesLeft.put(player.getUniqueId(), bombs);
		
		createTemplate(player, bombs);
		
		Inventory gameUI = createGame(player, bombs);
		
		
		//Open User Interface for player to interact with
		player.openInventory(gameUI);
	}
	
	@SuppressWarnings("deprecation")
	private boolean hasInInv(Player p, List<String> list) {
	    for(ItemStack i : p.getInventory().getContents()) {
	        if(i != null) {
	            boolean found = false;
	            for(String st : list) {
	                if(st.startsWith("" + i.getType().getId())) {
	                    if(st.startsWith(i.getType().getId() + ":")) {
	                        try {
	                            byte data = Byte.parseByte(st.replaceFirst(i.getType().getId() + ":", ""));
	                            if(data != i.getDurability()) {
	                                continue;
	                            } else {
	                                found = true;
	                                break;
	                            }
	                        } catch(Exception ex) {
	                            System.out.println("Error in Config: " + st + " is not a valid Item");
	                            return false;
	                        }
	                    }
	                    found = true;
	                    break;
	                }
	            }
	            if(!found) {
	                return false;
	            }
	        }
	    }
	    return true;
	}
}
