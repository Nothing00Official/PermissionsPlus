package PermissionsPlus.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import PermissionsPlus.manager.Category;
import PermissionsPlus.manager.GroupPlus;

public class CategoryCMDS implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			if (cmd.getName().equalsIgnoreCase("permrank")) {
				if (sender.hasPermission("perm.category")) {
					if (args.length == 2) {
						if (args[0].equalsIgnoreCase("delete")) {
							if (sender.hasPermission("perm.category.delete")) {
								Category c = new Category(args[1]);
								if (c.exists()) {
									c.delete();
									sender.sendMessage("§rCategory " + args[1] + " has been deleted!");
								} else {
									sender.sendMessage("§cThis category does not exist!");
								}
							} else {
								sender.sendMessage("§cYou do not have permission for this command!");
							}
						} else {
							sender.sendMessage("§cSyntax error");
						}
					} else if (args.length == 3) {
						if (args[0].equalsIgnoreCase("create")) {
							if (sender.hasPermission("perm.category.create")) {
								Category c = new Category(args[1]);
								if (!c.exists()) {
									if (GroupPlus.exists(args[2])) {
										c.create(new GroupPlus(args[2]));
										sender.sendMessage("§rRank category " + args[1] + " created!");
									} else {
										sender.sendMessage("§cThis group does not exist!");
									}
								} else {
									sender.sendMessage("§cThis category already exists!");
								}
							} else {
								sender.sendMessage("§cYou do not have permission for this command!");
							}
						} else if (args[0].equalsIgnoreCase("addgroup")) {
							if (sender.hasPermission("perm.category.addgroup")) {
								Category c = new Category(args[1]);
								if (c.exists()) {
									c.addGroups(args[2]);
									sender.sendMessage("§rGroups added to category " + args[1]);
								} else {
									sender.sendMessage("§cThis category does not exist!");
								}
							} else {
								sender.sendMessage("§cYou do not have permission for this command!");
							}
						} else if (args[0].equalsIgnoreCase("removegroup")) {
							if (sender.hasPermission("perm.category.removegroup")) {
								Category c = new Category(args[1]);
								if (c.exists()) {
									if (GroupPlus.exists(args[2])) {
										c.removeGroup(new GroupPlus(args[2]));
										sender.sendMessage("§rGroup " + args[2] + " removed from category " + args[1]);
									} else {
										sender.sendMessage("§cThis group does not exist!");
									}
								} else {
									sender.sendMessage("§cThis category does not exist!");
								}
							} else {
								sender.sendMessage("§cYou do not have permission for this command!");
							}
						} else {
							sender.sendMessage("§cSyntax error");
						}
					} else {
						sender.sendMessage(
								"§a" + String.join("§r, §a", Category.getCategories().toArray(new String[0])));
					}
				} else {
					sender.sendMessage("§cYou do not have permission for this command!");
				}
			}

		}
		return false;
	}

}
