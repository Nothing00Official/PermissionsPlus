package PermissionsPlus.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import PermissionsPlus.manager.GroupPlus;
import PermissionsPlus.manager.ListPlus;
import PermissionsPlus.manager.PermissionsManager;
import PermissionsPlus.manager.WorldPlus;

public class GroupCMDS implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			if (cmd.getName().equalsIgnoreCase("permgroup")) {
				if (sender.hasPermission("perm.group")) {
					if (args.length == 0) {
						String[] group = GroupPlus.getGroups().toArray(new String[0]);
						sender.sendMessage("§a" + String.join("§7, §a", group));
					} else if (args.length == 2) {
						if (args[0].equalsIgnoreCase("create")) {
							if (sender.hasPermission("perm.group.create")) {
								GroupPlus group = new GroupPlus(args[1]);
								if (!group.create()) {
									sender.sendMessage("§rThis group already exists!");
								} else {
									sender.sendMessage("§rGroup " + args[1] + " created!");
								}
							} else {
								sender.sendMessage("§cYou do not have permission for this command!");
							}
						} else if (args[0].equalsIgnoreCase("delete")) {
							if (sender.hasPermission("perm.group.delete")) {
								GroupPlus group = new GroupPlus(args[1]);
								if (group.delete()) {
									sender.sendMessage("§rGroup " + args[1] + " deleted!");
								} else {
									sender.sendMessage("§rThis group not exists!");
								}
							} else {
								sender.sendMessage("§cYou do not have permission for this command!");
							}
						} else if (args[0].equalsIgnoreCase("setdefault")) {
							if (sender.hasPermission("perm.group.setdefault")) {
								GroupPlus group = new GroupPlus(args[1]);
								if (GroupPlus.exists(args[1])) {
									group.setDefault();
									sender.sendMessage("§rGroup " + args[1] + " is now default group!");
									sender.sendMessage(
											"§cUse /perm reload or restart server for applicate this operation");
								} else {
									sender.sendMessage("§rThis group not exists!");
								}
							} else {
								sender.sendMessage("§cYou do not have permission for this command!");
							}
						} else {
							sender.sendMessage("§cSyntax error!");
						}
					} else if (args.length == 3) {

						if (GroupPlus.exists(args[0])) {
							if (args[1].equalsIgnoreCase("prefix")) {
								if (sender.hasPermission("perm.group.prefix")) {
									GroupPlus group = new GroupPlus(args[0]);
									group.setPrefix(args[2]);
									sender.sendMessage(
											"§rPrefix " + args[2] + " succefully added for group " + args[0]);
								} else {
									sender.sendMessage("§cYou do not have permission for this command!");
								}
							} else if (args[1].equalsIgnoreCase("suffix")) {
								if (sender.hasPermission("perm.group.suffix")) {
									GroupPlus group = new GroupPlus(args[0]);
									group.setSuffix(args[2]);
									sender.sendMessage(
											"§rSuffix " + args[2] + " succefully added for group " + args[0]);
								} else {
									sender.sendMessage("§cYou do not have permission for this command!");
								}
							} else if (args[1].equalsIgnoreCase("addperm")) {
								if (sender.hasPermission("perm.group.addperm")) {

									GroupPlus group = new GroupPlus(args[0]);

									if (ListPlus.isList(args[2])) {

										ListPlus list = new ListPlus(args[2].substring(5, args[2].length()));
										group.addPermission(list.getPermissions());

									} else {
										group.addPermission(args[2]);
									}

									sender.sendMessage(
											"Permissions " + args[2] + " succefully added to group " + args[0]);

								} else {
									sender.sendMessage("§cYou do not have permission for this command!");
								}
							} else if (args[1].equalsIgnoreCase("removeperm")) {
								if (sender.hasPermission("perm.group.removeperm")) {

									GroupPlus group = new GroupPlus(args[0]);

									if (ListPlus.isList(args[2])) {

										ListPlus list = new ListPlus(args[2].substring(5, args[2].length()));
										group.removePermission(list.getPermissions());

									} else {
										group.removePermission(args[2]);
									}

									sender.sendMessage(
											"Permissions " + args[2] + " succefully removed from group " + args[0]);

								} else {
									sender.sendMessage("§cYou do not have permission for this command!");
								}
							} else if (args[1].equalsIgnoreCase("find")) {
								if (sender.hasPermission("perm.group.find")) {
									List<String> find = PermissionsManager.getGroupRegex(new GroupPlus(args[0]),
											args[2]);
									if (find.isEmpty())
										sender.sendMessage("§rAny permission matches...");
									else
										sender.sendMessage(find.size() + " Permissions match:\n - "
												+ String.join("\n - ", find.toArray(new String[0])));
								} else {
									sender.sendMessage("§cYou do not have permission for this command!");
								}
							} else {
								sender.sendMessage("§cSyntax error!");
							}
						} else {
							sender.sendMessage("§rThis group not exists!");
						}

					} else if (args.length == 4) {

						if (GroupPlus.exists(args[0])) {
							if (args[1].equalsIgnoreCase("addperm")) {
								if (sender.hasPermission("perm.group.addperm.world")) {

									GroupPlus group = new GroupPlus(args[0]);
									WorldPlus world = new WorldPlus(group, args[3]);
									if (!world.exists()) {
										sender.sendMessage("§rThis world does not exist!");
										return true;
									}
									if (ListPlus.isList(args[2])) {

										ListPlus list = new ListPlus(args[2].substring(5, args[2].length()));
										world.addPermission(list.getPermissions());

									} else {
										world.addPermission(args[2]);
									}

									sender.sendMessage("Permissions " + args[2] + " succefully added to group "
											+ args[0] + " in world " + args[3]);

								} else {
									sender.sendMessage("§cYou do not have permission for this command!");
								}
							} else if (args[1].equalsIgnoreCase("removeperm")) {
								if (sender.hasPermission("perm.group.removeperm.world")) {

									GroupPlus group = new GroupPlus(args[0]);
									WorldPlus world = new WorldPlus(group, args[3]);
									if (!world.exists()) {
										sender.sendMessage("§rThis world does not exist!");
										return true;
									}
									if (ListPlus.isList(args[2])) {

										ListPlus list = new ListPlus(args[2].substring(5, args[2].length()));
										world.removePermission(list.getPermissions());

									} else {
										world.removePermission(args[2]);
									}

									sender.sendMessage("Permissions " + args[2] + " succefully removed from group "
											+ args[0] + " in world " + args[3]);

								} else {
									sender.sendMessage("§cYou do not have permission for this command!");
								}
							} else {
								sender.sendMessage("§cSyntax error!");
							}
						} else {
							sender.sendMessage("§rThis group not exists!");
						}

					} else if (args.length == 5) {

						if (GroupPlus.exists(args[0])) {
							if (args[1].equalsIgnoreCase("addperm") && args[3].equalsIgnoreCase("timed")) {
								if (sender.hasPermission("perm.group.addperm.timed")) {

									GroupPlus group = new GroupPlus(args[0]);

									if (ListPlus.isList(args[2])) {

										ListPlus list = new ListPlus(args[2].substring(5, args[2].length()));
										group.addTimedPermission(list.getPermissions(), args[4]);

									} else {
										group.addTimedPermission(args[2], args[4]);
									}

									sender.sendMessage("Permissions " + args[2] + " succefully added to group "
											+ args[0] + " for " + args[4]);

								} else {
									sender.sendMessage("§cYou do not have permission for this command!");
								}
							} else {
								sender.sendMessage("§cSyntax error!");
							}
						} else {
							sender.sendMessage("§rThis group not exists!");
						}

					} else {
						sender.sendMessage("§cSyntax error!");
					}
				} else {
					sender.sendMessage("§cYou do not have permission for this command!");
				}
			}

		} // check instance

		return false;
	}
}
