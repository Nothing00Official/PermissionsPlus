package PermissionsPlus.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import PermissionsPlus.commands.GuiCMDS;

public class GuiEvent implements Listener{

	@EventHandler(priority = EventPriority.NORMAL)
	public void onCloseInv(InventoryCloseEvent event){
		if(event.getInventory().getTitle().equalsIgnoreCase("§2Select groups")){
			if(GuiCMDS.permgui.containsKey(event.getPlayer().getName())) {
				GuiCMDS.permgui.get(event.getPlayer().getName()).close();
				event.getPlayer().sendMessage("§rPermission added to all selected groups!");
			}
		}
		
		if(event.getInventory().getTitle().equalsIgnoreCase("§4Select groups")){
			if(GuiCMDS.permgui.containsKey(event.getPlayer().getName())) {
				GuiCMDS.permgui.get(event.getPlayer().getName()).close();
				event.getPlayer().sendMessage("§rPermission removed from all selected groups!");
			}
		}
		
	}

	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onInvClick(InventoryClickEvent event){
		
		ItemStack clicked = event.getCurrentItem();
		Player p = (Player) event.getWhoClicked();
		
		if(event.getInventory().getTitle().equalsIgnoreCase("§2Select groups")){

			if(GuiCMDS.permgui.containsKey(p.getName())) {
				GuiCMDS.permgui.get(p.getName()).setSelected(clicked, event.getSlot());
			}
			
			event.setCancelled(true);
		}
		
		
		if(event.getInventory().getTitle().equalsIgnoreCase("§4Select groups")){
			
			if(GuiCMDS.permgui.containsKey(p.getName())) {
				GuiCMDS.permgui.get(p.getName()).setSelected(clicked, event.getSlot());
			}
			
			event.setCancelled(true);
		}
		
	}
	
}
