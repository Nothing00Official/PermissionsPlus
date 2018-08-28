package PermissionsPlus.commands;

import java.io.File;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import PermissionsPlus.Nothing00.Config;
import PermissionsPlus.Nothing00.Main;
import PermissionsPlus.Nothing00.Util;
import PermissionsPlus.manager.GroupPlus;
import PermissionsPlus.manager.ListPlus;
import PermissionsPlus.manager.PermissionsManager;
import PermissionsPlus.manager.UserPlus;
import PermissionsPlus.manager.WorldPlus;

public class UserCMDS implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			if (cmd.getName().equalsIgnoreCase("permuser")) {
				if (args.length == 3) {
					if (sender.hasPermission("perm.user")) {
						if (args[1].equalsIgnoreCase("addgroup")) {
							if (sender.hasPermission("perm.user.addgroup")) {

								if (GroupPlus.exists(args[2])) {
									UserPlus user = new UserPlus(args[0]);

									if (user.getGroups().contains(args[2])) {
										sender.sendMessage("§rThis user is already in this group!");
										return true;
									}

									user.setGroup(new GroupPlus(args[2]));
									sender.sendMessage(
											"§rUser " + args[0] + " succefully added to group " + args[2] + "!");
								} else {
									sender.sendMessage("§rThis group not exists!");
								}

							} else {
								sender.sendMessage("§cYou do not have permission for this command!");
							}
						} else if (args[1].equalsIgnoreCase("removegroup")) {
							if (sender.hasPermission("perm.user.removegroup")) {

								if (GroupPlus.exists(args[2])) {
									UserPlus user = new UserPlus(args[0]);

									if (user.getGroups().contains(args[2])) {

										user.removeGroup(new GroupPlus(args[2]));
										sender.sendMessage("§rUser " + args[0] + " succefully removed from group "
												+ args[2] + "!");

									} else {
										sender.sendMessage("§rThis user is not in group " + args[2] + "!");
									}

								} else {
									sender.sendMessage("§rThis group not exists!");
								}
							} else {
								sender.sendMessage("§cYou do not have permission for this command!");
							}

						} else if (args[1].equalsIgnoreCase("addperm")) {
							if (sender.hasPermission("perm.user.addperm")) {

								UserPlus user = new UserPlus(args[0]);

								if (ListPlus.isList(args[2])) {

									ListPlus list = new ListPlus(args[2].substring(5, args[2].length()));
									user.addPermission(list.getPermissions());

								} else {
									user.addPermission(args[2]);
								}
								sender.sendMessage("§rPermission " + args[2] + " added to user " + args[0] + "!");

							} else {
								sender.sendMessage("§cYou do not have permission for this command!");
							}
						} else if (args[1].equalsIgnoreCase("removeperm")) {
							if (sender.hasPermission("perm.user.removeperm")) {

								UserPlus user = new UserPlus(args[0]);

								if (ListPlus.isList(args[2])) {

									ListPlus list = new ListPlus(args[2].substring(5, args[2].length()));
									user.removePermission(list.getPermissions());

								} else {
									user.removePermission(args[2]);
								}
								sender.sendMessage("§rPermission " + args[2] + " removed to user " + args[0] + "!");

							} else {
								sender.sendMessage("§cYou do not have permission for this command!");
							}
						} else if (args[1].equalsIgnoreCase("prefix")) {
							if (sender.hasPermission("perm.user.prefix")) {

								UserPlus user = new UserPlus(args[0]);
								user.setPrefix(args[2]);
								sender.sendMessage("§rPrefix " + args[2] + " set to user " + args[0] + "!");

							} else {
								sender.sendMessage("§cYou do not have permission for this command!");
							}
						} else if (args[1].equalsIgnoreCase("suffix")) {
							if (sender.hasPermission("perm.user.suffix")) {

								UserPlus user = new UserPlus(args[0]);
								user.setSuffix(args[2]);
								sender.sendMessage("§rSuffix " + args[2] + " set to user " + args[0] + "!");

							} else {
								sender.sendMessage("§cYou do not have permission for this command!");
							}
						} else if (args[1].equalsIgnoreCase("find")) {
							if (sender.hasPermission("perm.user.find")) {
								PermissionsManager pm = new PermissionsManager(args[0]);
								List<String> find = pm.getUserRegex(args[2]);
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
						sender.sendMessage("§cYou do not have permission for this command!");
					}
				} else if (args.length == 0) {
					if (sender.hasPermission("perm.user")) {
						sender.sendMessage(UserPlus.getUsers());
					} else {
						sender.sendMessage("§cYou do not have permission for this command!");
					}
				} else if (args.length == 1) {
					if (sender.hasPermission("perm.user") && sender.hasPermission("perm.user.info")) {
						UserPlus user = new UserPlus(args[0]);
						sender.sendMessage(user.getInfo());
					} else {
						sender.sendMessage("§cYou do not have permission for this command!");
					}
				} else if (args.length == 4) {

					if (args[1].equalsIgnoreCase("addperm")) {
						if (sender.hasPermission("perm.user.addperm.world")) {

							UserPlus user = new UserPlus(args[0]);
							WorldPlus world = new WorldPlus(user, args[3]);
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
							sender.sendMessage(
									"§rPermission " + args[2] + " added to user " + args[0] + " in world " + args[3]);

						} else {
							sender.sendMessage("§cYou do not have permission for this command!");
						}
					} else if (args[1].equalsIgnoreCase("removeperm")) {
						if (sender.hasPermission("perm.user.removeperm.world")) {

							UserPlus user = new UserPlus(args[0]);
							WorldPlus world = new WorldPlus(user, args[3]);
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
							sender.sendMessage("§rPermission " + args[2] + " removed from user " + args[0]
									+ " in world " + args[3]);

						} else {
							sender.sendMessage("§cYou do not have permission for this command!");
						}
					} else {
						sender.sendMessage("§cSyntax error!");
					}

				} else if (args.length == 5) {
					if (args[1].equalsIgnoreCase("addperm") && args[3].equalsIgnoreCase("timed")) {
						if (sender.hasPermission("perm.user.addperm.timed")) {

							UserPlus user = new UserPlus(args[0]);
							if (user.getPlayer() == null) {
								sender.sendMessage("§cUser not found!");
								return true;
							}
							if (ListPlus.isList(args[2])) {

								ListPlus list = new ListPlus(args[2].substring(5, args[2].length()));

								user.addTimedPermission(list.getPermissions(), args[4]);

							} else {
								user.addTimedPermission(args[2], args[4]);
							}
							sender.sendMessage(
									"§rPermission " + args[2] + " added to user " + args[0] + " for " + args[4]);

						} else {
							sender.sendMessage("§cYou do not have permission for this command!");
						}
					}
					if (args[1].equalsIgnoreCase("addgroup") && args[3].equalsIgnoreCase("timed")) {
						if (sender.hasPermission("perm.user.addgroup.timed")) {

							if (GroupPlus.exists(args[2])) {
								UserPlus user = new UserPlus(args[0]);
								if (user.getGroups().contains(args[2])) {
									sender.sendMessage("§rThis user is already in this group!");
									return true;
								}

								Util.addTimeGroup(new GroupPlus(args[2]), args[4], args[0]);
								sender.sendMessage("§rUser " + args[0] + " succefully added to group " + args[2]
										+ " for " + args[4]);
							} else {
								sender.sendMessage("§rThis group not exists!");
							}

						} else {
							sender.sendMessage("§cYou do not have permission for this command!");
						}
					} else {
						sender.sendMessage("§cSyntax error!");
					}
				} else if (args.length == 2) {

					if (sender.hasPermission("perm.user")) {
						Config config = new Config(Main.getPlugin().getConfig(),
								new File(Main.getPlugin().getDataFolder() + "/config.yml"));
						if (args[0].equalsIgnoreCase("promote")) {
							UserPlus user = new UserPlus(args[1]);
							if (user.promote()) {
								if (user.getPlayer() != null) {
									user.getPlayer()
											.sendMessage(config.getString("promote-message").replaceAll("&", "§"));
								}
								sender.sendMessage("§r" + args[1] + " has been promoted!");
							} else {
								sender.sendMessage("§cThis user has max rank in his category!");
							}
						} else if (args[0].equalsIgnoreCase("demote")) {
							UserPlus user = new UserPlus(args[1]);
							if (user.demote()) {
								if (user.getPlayer() != null) {
									user.getPlayer()
											.sendMessage(config.getString("demote-message").replaceAll("&", "§"));
								}
								sender.sendMessage("§r" + args[1] + " has been demoted!");
							} else {
								sender.sendMessage("§cThis user has minium rank in his category!");
							}
						}

					} else {
						sender.sendMessage("§cYou do not have permission for this command!");
					}

				} else {
					if (sender.hasPermission("perm.user")) {
						sender.sendMessage(
								"§rUsage /permuser <user> addgroup/removegroup/addperm/removeperm/prefix/suffix <args>");
					} else {
						sender.sendMessage("§cYou do not have permission for this command!");
					}
				}
			}

		} // check instance
		return false;
	}

}
