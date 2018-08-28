package PermissionsPlus.commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import PermissionsPlus.manager.Gui;

public class GuiCMDS implements CommandExecutor{

	public static HashMap<String, Gui> permgui = new HashMap<String, Gui>();
	
public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {
		
		if(sender instanceof Player){
		
			if(cmd.getName().equalsIgnoreCase("permgroupgui")){
			
				  if(sender.hasPermission("perm.group.gui")){
				   if(args.length==2){
					if(args[0].equalsIgnoreCase("addperm")){
						if(sender.hasPermission("perm.group.gui.addperm")){
							
							if(sender instanceof Player){
								Gui gui = new Gui((Player) sender, args[1], "add");
								gui.open();
								permgui.put(sender.getName(), gui);
							}
						}else{
						  sender.sendMessage("§cYou do not have permission for this command!");
						}
					}else if(args[0].equalsIgnoreCase("removeperm")){
						if(sender.hasPermission("perm.group.gui.removeperm")){						
							
							if(sender instanceof Player){
								Gui gui = new Gui((Player) sender, args[1], "remove");
								gui.open();
								permgui.put(sender.getName(), gui);
							}
						}else{
					      sender.sendMessage("§cYou do not have permission for this command!");	
						}
					}else{
					  sender.sendMessage("§cSyntax error!");
					}
				   }else{
						sender.sendMessage("§rUsage: /permgroupgui addperm/removeperm <perm>");
					}
				  }else{
					sender.sendMessage("§cYou do not have permission for this command!");
				  }	
			}
			
		}else {
			System.out.println("YOU MUST BE A PLAYER!");
		}
   return false;
}
}

