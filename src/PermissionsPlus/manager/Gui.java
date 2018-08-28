package PermissionsPlus.manager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import PermissionsPlus.commands.GuiCMDS;

public class Gui {

	private Player p;
	private boolean gui;
	private Inventory inv;
	private String perm;
	private String type;
	
	public Gui(Player p, String perm, String type) {
		this.p=p;
		this.gui=false;
		this.inv=null;
		this.perm=perm;
		this.type=type;
		createGui();
	}
	
	
	private void createGui() {
		if(this.type.equals("add")) {
			if(GroupGUI.getGroups().size()<=54){
				Inventory groupgui = Bukkit.createInventory(null, 54, "§2Select groups");
				for(int i=0; i<GroupGUI.getGroups().size(); i++){
					ItemStack item = new ItemStack(Material.STAINED_GLASS,1, (short) 13);
					ItemMeta meta = item.getItemMeta();
				    meta.setDisplayName("§b"+GroupGUI.getGroups().get(i));
				    item.setItemMeta(meta);
					groupgui.setItem(i, item);
				}
				this.gui=false;
				this.inv=groupgui;
			}else{
				this.gui=true;
			}
		}else {
			Inventory groupgui = Bukkit.createInventory(null, 54, "§4Select groups");
			if(GroupGUI.getGroups().size()<=54){
			for(int i=0; i<GroupGUI.getGroups().size(); i++){
				ItemStack item = new ItemStack(Material.STAINED_GLASS,1, (short) 14);
				ItemMeta meta = item.getItemMeta();
			    meta.setDisplayName("§c"+GroupGUI.getGroups().get(i));
			    item.setItemMeta(meta);
				groupgui.setItem(i, item);
			}
			this.gui=false;
			this.inv=groupgui;
			}else {
			  this.gui=true;	
			}
		}
	}
	
	public void open() {
		createGui(); 
		if(!this.gui) {
			this.p.openInventory(this.inv);
		}else {
			this.p.sendMessage("§rYou don't do this operation with more than 54 groups!"); 
		}		
	}
	
	@SuppressWarnings("deprecation")
	public void close() {
		if(!this.gui) {
			for(int i=0; i<this.inv.getSize(); i++){
				if(this.inv.getItem(i)==null) continue;
				if(this.inv.getItem(i).getData().getData()==14 && this.inv.getTitle().equals("§2Select groups")){
					ItemMeta meta = this.inv.getItem(i).getItemMeta();
					String name = meta.getDisplayName().substring(2, meta.getDisplayName().length());
					GroupGUI gg = new GroupGUI(name, this.perm);
					gg.addPermission();
				}else if(this.inv.getItem(i).getData().getData()==13  && this.inv.getTitle().equals("§4Select groups")) {
					ItemMeta meta = this.inv.getItem(i).getItemMeta();
					String name = meta.getDisplayName().substring(2, meta.getDisplayName().length());
					GroupGUI gg = new GroupGUI(name, this.perm);
					gg.removePermission();
				}
			}
			GuiCMDS.permgui.remove(this.p.getName());
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public void setSelected(ItemStack bool, int slot) {
		if(bool.getData().getData()==13){
			
			ItemStack item = new ItemStack(Material.STAINED_GLASS, 1, (short)14);
			ItemMeta metaitem = item.getItemMeta();
			ItemMeta metaclicked = bool.getItemMeta();
			metaitem.setDisplayName(metaclicked.getDisplayName());
			item.setItemMeta(metaitem);
			this.inv.setItem(slot, item);
			
		}else if(bool.getData().getData()==14){
			ItemStack item = new ItemStack(Material.STAINED_GLASS, 1, (short)13);
			ItemMeta metaitem = item.getItemMeta();
			ItemMeta metaclicked = bool.getItemMeta();
			metaitem.setDisplayName(metaclicked.getDisplayName());
			item.setItemMeta(metaitem);
			this.inv.setItem(slot, item);
		}
		this.p.updateInventory();
	}
	
}
