package com.github.plagueisthewise21.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.plagueisthewise21.Nms.TitleUpdater;

public abstract class MineSweeper{
	
	
	
	protected static ItemStack flag;
	protected ItemStack mine = customItem(new ItemStack(Material.TNT), ChatColor.GOLD + "That's A Mine! " + ChatColor.DARK_RED + "BOOM!");
	protected static ItemStack unsearched;
	
	public static HashMap<UUID, int[][]> mineTemplates = new HashMap<UUID, int[][]>(); 
	public static HashMap<UUID, Integer> minesLeft = new HashMap<UUID, Integer>();
	
	
	@SuppressWarnings("deprecation")
	public static void loadItems() {
		
		if((Integer.parseInt(TitleUpdater.version.substring(3,5)) < 14)) {
			try {
			Class<?> materialClass = getClass("Material");
			Object enumSign = materialClass.getField("SIGN").get(null);
			Object enumPane = materialClass.getField("STAINED_GLASS_PANE").get(null);
			
			flag = customItem(new ItemStack((Material) enumSign), ChatColor.RED + "Mine Flagged");
			
			unsearched = customItem(new ItemStack((Material) enumPane, 1, (byte) 15), ChatColor.GRAY + "Unsearched");
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}else {
			flag = customItem(new ItemStack(Material.OAK_SIGN), ChatColor.RED + "Mine Flagged");
			unsearched = customItem(new ItemStack(Material.BLACK_STAINED_GLASS_PANE), ChatColor.GRAY + "Unsearched");
		}
		
		
		
	}
	
	
	public static Class<?> getClass(String name){
		try {
			return Class.forName("org.bukkit." + name);
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static final int MINE_PRESENT = 9;
	

	public static ItemStack customItem(ItemStack is, String name) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);		
		return is;
	}

	public void createTemplate(Player player, Integer mine_amount) {
		Inventory template = Bukkit.createInventory(player, 54, "Minesweeper: Click A Tile");

		int[][] inv2D = new int[6][9];
		ArrayList<Integer> mine_loc = new ArrayList<Integer>();
		for(int i = 0; i < template.getSize(); i++) {
			mine_loc.add(i);
		}
		Collections.shuffle(mine_loc);

		//populate array
		for(int i = 0; i < inv2D.length; i++) {
			for(int j = 0; j < inv2D[0].length; j++) {
				inv2D[i][j] = 0;
			}
		}
		
		//set mine locations
		for(int i = 0; i < mine_amount; i++) {
			int xindex = mine_loc.get(i) / inv2D[0].length;
			int yindex = mine_loc.get(i) % inv2D[0].length;
			inv2D[xindex][yindex] = MINE_PRESENT;
		}


		//System.out.println(inv2D.length); //returns 3
		//System.out.println(inv2D[0].length); //returns 9
		
		//loop through all cells in index
		for(int r = 0; r < inv2D.length; r++) {
			for(int c = 0; c < inv2D[0].length; c++) {
				int slot = r * 9 + c;
				//check if item is 9 (mine)
				if(inv2D[r][c] == 9) {
					template.setItem(slot, mine);
					//loop through column associated with element
					for(int i = -1; i <= 1; i++) {
						//loop through rows associated with element
						for(int j = -1; j <= 1; j++) {
							if(i == 0 && j == 0) {
								continue;
							}
							//row index
							int xnew = r + i;
							// column index
							int ynew = c + j;
							//works up to this point
							if(xnew >= 0 && ynew >= 0 && xnew < inv2D.length && ynew < inv2D[0].length) {
								if(inv2D[xnew][ynew] != 9) {
									inv2D[xnew][ynew]++;
								}
							}
						}
					}
				}
			}
		}
		mineTemplates.put(player.getUniqueId(), inv2D);
	}
	
	
	public Inventory createGame(Player player, Integer mine_amount) {
		Inventory inv = Bukkit.createInventory(player, 54, "Mines Remaining: " + mine_amount);
		
		for(int slots = 0; slots < inv.getSize(); slots++) {
			inv.setItem(slots, unsearched);
		}
		
		return inv;
	}
}
