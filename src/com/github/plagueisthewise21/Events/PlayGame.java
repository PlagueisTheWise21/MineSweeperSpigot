package com.github.plagueisthewise21.Events;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.github.plagueisthewise21.Source;
import com.github.plagueisthewise21.Data.MineSweeper;
import com.github.plagueisthewise21.Nms.TitleUpdater;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class PlayGame extends MineSweeper implements Listener{
	
	private Source plugin;
	public PlayGame(Source main) {
		this.plugin = main;
	}

	Multimap<Integer,Integer> checked = ArrayListMultimap.create();

	boolean hasWon = false;
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if(e.getView().getTitle().contains("Mines Remaining: ")) {
			UUID id = e.getPlayer().getUniqueId();
			mineTemplates.remove(id);
			minesLeft.remove(id);
			hasWon = false;
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getView().getTitle().contains("Mines Remaining: ")) {
			e.setCancelled(true);			
			if(e.getCurrentItem() !=  null) {

				Player player = (Player) e.getWhoClicked();
				UUID id = player.getUniqueId();
				int slot = e.getSlot();

				int totalMines = Integer.parseInt(e.getView().getTitle().substring(17));

				Inventory gameUI = e.getInventory();

				int[][] template = mineTemplates.get(id);

				//reveals slot
				if(e.isLeftClick()) {
					int valueClicked = template[slot / template[0].length][slot % template[0].length];
					//converts slot to 2d coords
					//switch possible values, 9 = mine, everything else is proximity
					switch(valueClicked) {
					case 9:					
						//checks if game is active
						if(hasWon) {
							break;
						}
						
						//show all items
						for(int i = 0; i < template.length; i++) {
							for(int j = 0; j < template[0].length; j++) {
								//game is over. Show all slots
								int allSlot = i * 9 + j;
								switch(template[i][j]) {									
								case 9:
									gameUI.setItem(allSlot, mine);
									player.updateInventory();
									break;
								case 0:
									gameUI.setItem(allSlot, new ItemStack(Material.AIR));
									player.updateInventory();
									break;
								default:
									gameUI.setItem(allSlot, customItem(new ItemStack(Material.CYAN_SHULKER_BOX, template[i][j]), " "));
									player.updateInventory();
									break;
								}
							}
							//set "game over" as inv title
							TitleUpdater.update(player, ChatColor.DARK_RED + "Game Over");
						}
						break;
					case 0:
						gameUI.setItem(slot, new ItemStack(Material.AIR));
						//reveal neighbouring squares
						revealAir(template, gameUI, slot, player);
						checked.clear();
						//check to see if game is won
						if(checkIfWin(gameUI, totalMines)) {
							TitleUpdater.update(player, ChatColor.DARK_GREEN + "You Win!");
							//show all cells
							for(int i = 0; i < template.length; i++) {
								for(int j = 0; j < template[0].length; j++) {
									//game is over. Show all slots
									int allSlot = i * 9 + j;
									switch(template[i][j]) {									
									case 9:
										gameUI.setItem(allSlot, mine);
										player.updateInventory();
										break;
									case 0:
										gameUI.setItem(allSlot, new ItemStack(Material.AIR));
										player.updateInventory();
										break;
									default:
										gameUI.setItem(allSlot, customItem(new ItemStack(Material.CYAN_SHULKER_BOX, template[i][j]), " "));
										player.updateInventory();
										break;
									}
								}
							}
							winGame(id);
						}
						break;
					default:
						//show prox mines
						gameUI.setItem(slot, customItem(new ItemStack(Material.CYAN_SHULKER_BOX, valueClicked), " "));
						//check to see if game is won
						if(checkIfWin(gameUI, totalMines)) {
							
							TitleUpdater.update(player, ChatColor.DARK_GREEN + "You Win!");
							//Show all cells
							for(int i = 0; i < template.length; i++) {
								for(int j = 0; j < template[0].length; j++) {
									//game is over. Show all slots
									int allSlot = i * 9 + j;
									switch(template[i][j]) {
									case 9:
										gameUI.setItem(allSlot, mine);
										player.updateInventory();
										break;
									case 0:
										gameUI.setItem(allSlot, new ItemStack(Material.AIR));
										player.updateInventory();
										break;
									default:
										gameUI.setItem(allSlot, customItem(new ItemStack(Material.CYAN_SHULKER_BOX, template[i][j]), " "));
										player.updateInventory();
										break;
									}
								}
							}
							winGame(id);
						}
						break;
					}
				}


				//flag slot as mine or unflag slot
				if(e.isRightClick()) {
					if(e.getCurrentItem().equals(flag)) {
						gameUI.setItem(slot, unsearched);
						int x = minesLeft.get(id);
						minesLeft.put(id, x + 1);
						TitleUpdater.update(player, "Mines Remaining: " + minesLeft.get(id));
					}else
						if(e.getCurrentItem().equals(unsearched)) {
							gameUI.setItem(slot, flag);
							int x = minesLeft.get(id);
							minesLeft.put(id, x - 1);
							TitleUpdater.update(player, "Mines Remaining: " + minesLeft.get(id));
						}
				}
			}
		}
	}

	public boolean checkIfWin(Inventory gameUI, int total_mines) {
		//check if tiles called "unsearched" and "mine flagged" = total mines
		int tiles_left = 0;
		for(int i = 0; i < gameUI.getSize(); i++) {
			if(gameUI.getItem(i) != null) {
				if(gameUI.getItem(i).equals(unsearched) || gameUI.getItem(i).equals(flag)) {
					tiles_left++;
				}
			}
		}

		if(tiles_left == total_mines) {
			return true;
		}		

		return false;
	}

	public void revealAir(int[][] template, Inventory gameUI, int slot, Player p) {
		//loop through neighbors
		
		int x = (slot / template[0].length);
		int y = (slot % template[0].length);

		for(int i = x-1; i <= x+1; i++) {
			for(int j = y-1; j <= y+1; j++) {

				if(i >= 0 && j >= 0 && i < template.length && j < template[0].length) {
					if(checked.containsEntry(i, j)) {
						continue;
					}
					checked.put(i, j);
					int newSlot = (i * 9) + j;
					
					if(template[i][j] == 0) {
						gameUI.setItem(newSlot, new ItemStack(Material.AIR));
						revealAir(template, gameUI, newSlot, p);
					}else {
						gameUI.setItem(newSlot, customItem(new ItemStack(Material.CYAN_SHULKER_BOX, template[i][j]), " "));
					}
				}
			}
		}
	}

	public void winGame(UUID id) {
		//check if rewards are active in config
		hasWon = true;
		Player player = Bukkit.getPlayer(id);
		
		if (!player.hasPermission("minesweeper.reward")) {
			return;
		}
		
		if(!plugin.getConfig().getBoolean("give-rewards")) {
			return;
		}
		
		List<String> commands = plugin.getConfig().getStringList("rewards");
		for(String command : commands) {
			String playerName = player.getName();
			String s = command.replaceAll("%player%", playerName);
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), s);
		}
	}
	
//	public void sendPacket(Player player, Object packet) {
//		try {
//			Object handle = player.getClass().getMethod("getHandle").invoke(player);
//			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
//			playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
//			
//			
//		} 
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public Class<?> getNMSClass(String name){
//		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
//		System.out.println(version);
//		try {
//			return Class.forName("net.minecraft.server." + version + "." + name);
//		} 
//		catch (ClassNotFoundException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	
}
