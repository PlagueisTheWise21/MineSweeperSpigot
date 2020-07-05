package com.github.plagueisthewise21.Nms;

import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_15_R1.ChatMessage;
import net.minecraft.server.v1_15_R1.Containers;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutOpenWindow;


public abstract class TitleUpdater {


	public void update(Player p, String title){
	    EntityPlayer ep = ((CraftPlayer)p).getHandle();
	    IChatBaseComponent invTitle = new ChatMessage(title);
	    PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(ep.activeContainer.windowId, Containers.GENERIC_9X6, invTitle);
	    ep.playerConnection.sendPacket(packet);
	    ep.updateInventory(ep.activeContainer);
	  }

}