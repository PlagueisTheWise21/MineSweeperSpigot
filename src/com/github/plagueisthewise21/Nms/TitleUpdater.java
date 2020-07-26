package com.github.plagueisthewise21.Nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;



public abstract class TitleUpdater {

	public static String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

	public static void update(Player p, String newTitle){
		
		switch(version.substring(0, 5)) {
		case "v1_12":
		case "v1_13":
			try {
				//Create EntityPlayer Object
				Object entityPlayerObject = p.getClass().getMethod("getHandle").invoke(p);	

				//Create windowId object		
				Class<?> entityPlayerClass = getNMSClass("EntityPlayer");
				Field activeContainerField = entityPlayerClass.getField("activeContainer");
				Object activeContainerObject = activeContainerField.get(entityPlayerObject);

				Class<?> containerClass = getNMSClass("Container");
				Field windowIdField = containerClass.getField("windowId");
				Object windowIdObject = windowIdField.get(activeContainerObject);

				//Create ChatMessage Object Of New Title
				Class<?> chatMessageClass = getNMSClass("ChatMessage");
				Constructor<?> chatMessageConstructor = chatMessageClass.getConstructor(String.class, Object[].class);
				Object invTitleObject = chatMessageConstructor.newInstance(newTitle, new Object[] {});


				//Create PacketContructor
				Constructor<?> titleConstructor  = getNMSClass("PacketPlayOutOpenWindow").getConstructor(int.class, String.class, getNMSClass("IChatBaseComponent"), int.class);

				Object packet = titleConstructor.newInstance(windowIdObject, "minecraft:generic_9x6", invTitleObject, p.getOpenInventory().getTopInventory().getSize());

				sendPacket(p, packet);
				//Invoke Update For Inventory Title
				Method updateInventoryMethod = entityPlayerClass.getMethod("updateInventory", containerClass);
				updateInventoryMethod.invoke(entityPlayerObject, activeContainerObject);
			}
			catch(Exception e) {
				e.printStackTrace();
			}

			break;
		case "v1_14":
		case "v1_15":
			try {				
				//Create EntityPlayer Object
				Object entityPlayerObject = p.getClass().getMethod("getHandle").invoke(p);	

				//Create windowId object		
				Class<?> entityPlayerClass = getNMSClass("EntityPlayer");
				Field activeContainerField = entityPlayerClass.getField("activeContainer");
				Object activeContainerObject = activeContainerField.get(entityPlayerObject);

				Class<?> containerClass = getNMSClass("Container");
				Field windowIdField = containerClass.getField("windowId");
				Object windowIdObject = windowIdField.get(activeContainerObject);

				//Create ChatMessage Object Of New Title
				Class<?> chatMessageClass = getNMSClass("ChatMessage");
				Constructor<?> chatMessageConstructor = chatMessageClass.getConstructor(String.class, Object[].class);
				Object invTitleObject = chatMessageConstructor.newInstance(newTitle, new Object[] {});

				//Create Enum Object For Container Size and Type
				Object enumContainer = getNMSClass("Containers").getField("GENERIC_9X6").get(null);		

				//Create PacketContructor
				Constructor<?> titleConstructor  = getNMSClass("PacketPlayOutOpenWindow").getConstructor(int.class, getNMSClass("Containers"), getNMSClass("IChatBaseComponent"));

				Object packet = titleConstructor.newInstance(windowIdObject, enumContainer, invTitleObject);

				sendPacket(p, packet);

				//Invoke Update For Inventory Title
				Method updateInventoryMethod = entityPlayerClass.getMethod("updateInventory", containerClass);
				updateInventoryMethod.invoke(entityPlayerObject, activeContainerObject);

			}
			catch(Exception e) {
				e.printStackTrace();
			}

			break;
		default:
			System.out.println("Server Version Not Compatible With MineSweeper Plugin");
			break;					
		}
	}


	private static void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);			
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static Class<?> getNMSClass(String name){
		try {
			return Class.forName("net.minecraft.server." + version + "." + name);
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}



	//	public static void updateInventoryName(Player player, String newTitle) {
	//		// EntityPlayer ep = ((CraftPlayer) player).getHandle();
	//		// IChatBaseComponent invTitle = new ChatMessage(title);
	//		// PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(ep.activeContainer.windowId, Containers.GENERIC_9X6, invTitle);
	//		// ep.playerConnection.sendPacket(packet);
	//		// ep.updateInventory(ep.activeContainer);
	//
	//		try {
	//			// Start of EntityPlayer ep = ((CraftPlayer) player).getHandle();
	//			Class<?> craftPlayerClass = ReflectionUtils.getCraftClass("entity.CraftPlayer");
	//			Object craftPlayerObject = craftPlayerClass.cast(player);
	//			Method getHandleMethod = craftPlayerClass.getMethod("getHandle");
	//			Object entityPlayerObject = getHandleMethod.invoke(craftPlayerObject);
	//			// End of EntityPlayer ep = ((CraftPlayer) player).getHandle();
	//
	//			// Start of IChatBaseComponent invTitle = new ChatMessage(title);
	//			Class<?> chatMessageClass = ReflectionUtils.getNMSClass("ChatMessage");
	//			Constructor<?> chatMessageConstructor = chatMessageClass.getConstructor(String.class);
	//			Object invTitleObject = chatMessageConstructor.newInstance(newTitle);
	//			// End of IChatBaseComponent invTitle = new ChatMessage(title);
	//
	//			// Start of PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(ep.activeContainer.windowId, Containers.GENERIC_9X6, invTitle);
	//			Class<?> iChatBaseComponentClass = ReflectionUtils.getNMSClass("IChatBaseComponent");
	//			Class<?> packetPlayOutOpenWindowClass = ReflectionUtils.getNMSClass("PacketPlayOutOpenWindow");
	//			Class<?> containersClass = ReflectionUtils.getNMSClass("Containers");
	//			Constructor<?> packetPlayOutOpenWindowConstructor = packetPlayOutOpenWindowClass
	//					.getConstructor(int.class, containersClass/*1.13 and below this is a string*/, iChatBaseComponentClass);
	//			Class<?> entityPlayerClass = ReflectionUtils.getNMSClass("EntityPlayer");
	//			Field activeContainerField = entityPlayerClass.getField("activeContainer");
	//			Object activeContainerObject = activeContainerField.get(entityPlayerObject);
	//			Class<?> containerClass = ReflectionUtils.getNMSClass("Container");
	//			Field windowIdField = containerClass.getField("windowId");
	//			Object windowIdObject = windowIdField.get(activeContainerObject);
	//			Object packetPlayOutOpenWindowObject = packetPlayOutOpenWindowConstructor.newInstance(windowIdObject,
	//					ContainerType.getContainerType(activeContainerObject).getObject(), invTitleObject);
	//			// End of PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(ep.activeContainer.windowId, Containers.GENERIC_9X6, invTitle);
	//
	//			// Start of ep.playerConnection.sendPacket(packet);
	//			ReflectionUtils.sendPacket(player, packetPlayOutOpenWindowObject);
	//			// End of ep.playerConnection.sendPacket(packet);
	//
	//			// Start of ep.updateInventory(ep.activeContainer);
	//			Method updateInventoryMethod = entityPlayerClass.getMethod("updateInventory", containerClass);
	//			updateInventoryMethod.invoke(entityPlayerObject, activeContainerObject);
	//			// End of ep.updateInventory(ep.activeContainer);
	//		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException |
	//				InvocationTargetException | InstantiationException | NoSuchFieldException e) {
	//			// So many errors :(
	//			e.printStackTrace();
	//		}
	//	}
	//	
	//	
	//
	//	public static enum ContainerType {
	//		GENERIC_9X1(14, "GENERIC_9X1", "CHEST"),
	//		GENERIC_9X2(14, "GENERIC_9X2", "CHEST"),
	//		GENERIC_9X3(14, "GENERIC_9X3", "CHEST"),
	//		GENERIC_9X4(14, "GENERIC_9X4", "CHEST"),
	//		GENERIC_9X5(14, "GENERIC_9X5", "CHEST"),
	//		GENERIC_9X6(14, "GENERIC_9X6", "CHEST"),
	//		GENERIC_3X3(14, "GENERIC_3X3", "DISPENSER", "DROPPER"),
	//		ANVIL(14, "ANVIL", "ANVIL"),
	//		BEACON(14, "BEACON", "BEACON"),
	//		BLAST_FURNACE(14, "BLAST_FURNACE", "BLAST_FURNACE"),
	//		BREWING_STAND(14, "BREWING_STAND", "BREWING"),
	//		CRAFTING(14, "CRAFTING", "CRAFTING"),
	//		ENCHANTMENT(14, "ENCHANTMENT", "ENCHANTING"),
	//		FURNACE(14, "FURNACE", "FURNACE"),
	//		GRINDSTONE(14, "GRINDSTONE", "GRINDSTONE"),
	//		HOPPER(14, "HOPPER", "HOPPER"),
	//		LECTERN(14, "LECTERN", "LECTERN"),
	//		LOOM(14, "LOOM", "LOOM"),
	//		MERCHANT(14, "MERCHANT", "MERCHANT"),
	//		SHULKER_BOX(14, "SHULKER_BOX", "SHULKER_BOX"),
	//		SMOKER(14, "SMOKER", "SMOKER"),
	//		CARTOGRAPHY(14, "CARTOGRAPHY", "CARTOGRAPHY"),
	//		STONECUTTER(14, "STONECUTTER", "STONECUTTER"),
	//		SMITHING(16, "SMITHING", "SMITHING");
	//
	//		private final static Class<?> CONTAINER_CLASS = ReflectionUtils.getNMSClass("Containers");
	//
	//		private int version;
	//		private String fieldName;
	//		private String[] bukkitIntTypeName;
	//
	//		private ContainerType(int version, String fieldName, String... invType) {
	//			this.version = version;
	//			this.fieldName = fieldName;
	//			this.bukkitIntTypeName = invType;
	//		}
	//
	//		public static ContainerType getContainerType(Object containerObject) {
	//			InventoryView view = null;
	//			try {
	//				Method getBukkitViewMethod = ReflectionUtils.getNMSClass("Container").getMethod("getBukkitView");
	//				Object containerObjectBukkitViewObject = getBukkitViewMethod.invoke(containerObject);
	//				if (!(containerObjectBukkitViewObject instanceof InventoryView))
	//					return null;
	//				view = (InventoryView) containerObjectBukkitViewObject;
	//			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException |
	//					InvocationTargetException e) {
	//				// So many errors :(
	//				e.printStackTrace();
	//			}
	//
	//			// Comapre with string and not the InventoryType because all inventory types
	//			// do not exist in all versions
	//			String type = view.getTopInventory().getType().name();
	//
	//			if (type.equals("CHEST"))
	//				return ContainerType.valueOf("GENERIC_9X" + view.getTopInventory().getSize() / 9);
	//
	//			for (ContainerType containerType : values())
	//				for (String bukkitIntTypeName : containerType.bukkitIntTypeName)
	//					if (bukkitIntTypeName.equals(type))
	//						return containerType;
	//
	//			return null;
	//		}
	//
	//		/**
	//		 * @return The instance of the containers class
	//		 */
	//		public Object getObject() {
	//			try {
	//				Field f = CONTAINER_CLASS.getField(getFieldName());
	//				return f.get(null);
	//			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
	//				e.printStackTrace();
	//			}
	//			return null;
	//		}
	//
	//		public int getVersion() {
	//			return version;
	//		}
	//
	//		public String getFieldName() {
	//			return fieldName;
	//		}
	//	}

}