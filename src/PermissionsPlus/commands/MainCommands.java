package PermissionsPlus.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import PermissionsPlus.Nothing00.Main;
import PermissionsPlus.Nothing00.PexConverter;
import PermissionsPlus.manager.ListPlus;
import PermissionsPlus.manager.PermissionsManager;

public class MainCommands implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		if (sender instanceof Player || sender instanceof ConsoleCommandSender) {

			if (cmd.getName().equalsIgnoreCase("PermissionsPlus")) {
				if (args.length == 0) {
					sender.sendMessage("§9PermissionsPlus> §7Plugin made by Nothing00");
					sender.sendMessage("§9PermissionsPlus> §7https://www.youtube.com/c/Nothing00");
					sender.sendMessage("§9PermissionsPlus> §7try §e/perm help");
				} else if (args.length == 1) {
					if (args[0].equalsIgnoreCase("reload")) {
						if (sender.hasPermission("perm.reload")) {

							Main.reloadPlugin();

							sender.sendMessage("§rPermissions reloaded!");

						} else {
							sender.sendMessage("§cYou do not have permission for this command!");
						}
					} else if (args[0].equalsIgnoreCase("list")) {
						if (sender.hasPermission("perm.list")) {
							String[] list = ListPlus.getLists().toArray(new String[0]);
							sender.sendMessage("§a" + String.join("§7, §a", list));
						} else {
							sender.sendMessage("§cYou do not have permission for this command!");
						}
					} else if (args[0].equalsIgnoreCase("help")) {
						if (sender.hasPermission("perm.help")) {
							sender.sendMessage("§r/perm export | export PermissionsEx configuration");
							sender.sendMessage("§r/permrank create <category> <firstgroup> | Create rank category");
							sender.sendMessage("§r/permrank delete <category> | Delete rank category");
							sender.sendMessage(
									"§r/permrank addgroup <category> <group1,group2,group3,...> | Add group list");
							sender.sendMessage(
									"§r/permrank removegroup <category> <group> | Remove group from rank category");
							sender.sendMessage("§r/permlock <password> | Set password for switch group permissions");
							sender.sendMessage("§r/permlock on <password> | switch-on group permissions");
							sender.sendMessage("§r/permlock off | switch-off group permissions");
							sender.sendMessage("§r/perm createlist <list> | Create a list of permissions");
							sender.sendMessage("§r/perm deletelist <list> | Delete a list of permissions");
							sender.sendMessage("§r/perm <list> addperm <perm> | Add permission to list");
							sender.sendMessage("§r/perm <list> removeperm <perm> | Remove permission to list");
							sender.sendMessage("§r/perm reload | Reload PermissionsPlus plugin");
							sender.sendMessage("§r/permuser | list of user in yml configuration");
							sender.sendMessage("§r/permuser <user> | Info about user");
							sender.sendMessage("§r/permuser <user> addgroup <group> | Add user to group");
							sender.sendMessage("§r/permuser <user> removegroup <group> | Add user to group");
							sender.sendMessage("§r/permuser <user> addperm <perm> [world] | Add permission to user");
							sender.sendMessage(
									"§r/permuser <user> removeperm <perm> [world] | Remove permission to user");
							sender.sendMessage("§r/permuser <user> prefix <prefix> | Set prefix to user");
							sender.sendMessage("§r/permuser <user> suffix <prefix> | Set suffix to user");
							sender.sendMessage("§r/permgroup create <group> | Create a group");
							sender.sendMessage("§r/permgroup delete <group> | Delete a group");
							sender.sendMessage("§r/permgroup <group> addperm <perm> [world] | Add permission to group");
							sender.sendMessage(
									"§r/permgroup <group> removeperm <perm> [world] | Remove permission to group");
							sender.sendMessage("§r/permgroup <group> suffix <suffix> | Set suffix to group");
							sender.sendMessage("§r/permgroup <group> prefix <prefix> | Set prefix to group");
							sender.sendMessage(
									"§r/permgroupgui addperm <perm> | Add permission to more than one group");
							sender.sendMessage(
									"§r/permgroupgui removeperm <perm> | Remove permission to more than one group");
							sender.sendMessage(
									"§r/permuser <user> addperm <perm> timed <time> | Add temporary permission to user");
							sender.sendMessage(
									"§r/permgroup <group> addperm <perm> timed <time> | Add temporary permission to group");
							sender.sendMessage("§r/perm finder | find permissions plugin on each event");
							sender.sendMessage("§r/permu <user> find <regex> | find permissions that mathes regex");
							sender.sendMessage("§r/permg <group> find <regex> | find permissions that mathes regex");

						} else {
							sender.sendMessage("§cYou do not have permission for this command!");
						}
					} else if (args[0].equalsIgnoreCase("finder")) {
						if (sender.hasPermission("perm.finder")) {
							if (!(sender instanceof Player))
								return true;
							Player p = (Player) sender;
							if (PermissionsManager.hashperms.get(p.getUniqueId()).hasFinder()) {
								PermissionsManager.hashperms.get(p.getUniqueId()).setFinder(false);
								sender.sendMessage("§rPermissions Finder §cdisabled§r!");
							} else {
								PermissionsManager.hashperms.get(p.getUniqueId()).setFinder(true);
								sender.sendMessage("§rPermissions Finder §aenabled§r!");
							}
						} else {
							sender.sendMessage("§cYou do not have permission for this command!");
						}
					} else if (args[0].equalsIgnoreCase("export")) {

						if (sender instanceof ConsoleCommandSender) {

							if (!PexConverter.pexActive()) {
								sender.sendMessage("§rError... PermissionsEx plugin not found");
								sender.sendMessage("§rInstall PEX configuration to import it");
								return true;
							}

							Main.convert = true;
							sender.sendMessage(
									"§cif you are sure to perform this action use /perm reload after 10 seconds");
							sender.sendMessage(
									"§cits is racommended to perform this when server is empty! do not use this with online players!");
							Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
								@Override
								public void run() {
									if (Main.convert) {
										Main.convert = false;
										sender.sendMessage("§cDismiss operation!");
									}
								}
							}, 200);
						} else {
							sender.sendMessage("§cperform this command by console!");
						}

					} else {
						sender.sendMessage("§cSyntax error!");
					}
				} else if (args.length == 2) {
					if (sender.hasPermission("perm.list")) {
						if (args[0].equalsIgnoreCase("createlist")) {
							if (sender.hasPermission("perm.list.create")) {
								ListPlus list = new ListPlus(args[1]);
								if (!list.exists()) {
									list.create();
									sender.sendMessage("§rPermissions list " + args[1] + " created!");
								} else {
									sender.sendMessage("§cThis list already exists!");
								}
							} else {
								sender.sendMessage("§cYou do not have permission for this command!");
							}
						} else if (args[0].equalsIgnoreCase("deletelist")) {
							if (sender.hasPermission("perm.list.delete")) {
								ListPlus list = new ListPlus(args[1]);
								if (list.exists()) {
									list.delete();
									sender.sendMessage("§rPermissions list " + args[1] + " removed!");
								} else {
									sender.sendMessage("§cThis list not exists!");
								}
							} else {
								sender.sendMessage("§cYou do not have permission for this command!");
							}
						} else {
							sender.sendMessage("§cSyntax error!");
						}
					} else {
						sender.sendMessage("§cYou do not have permission for this command!");
					}
				} else if (args.length == 3) {
					if (sender.hasPermission("perm.list")) {
						if (args[1].equalsIgnoreCase("addperm")) {
							if (sender.hasPermission("perm.list.addperm")) {
								ListPlus list = new ListPlus(args[0]);
								if (list.exists()) {
									list.addPermissions(args[2]);
									sender.sendMessage(
											"§rPermissions " + args[2] + " succefully added to list " + args[0] + "!");
								} else {
									sender.sendMessage("§cThis list not exists!");
								}
							} else {
								sender.sendMessage("§cYou do not have permission for this command!");
							}
						} else if (args[1].equalsIgnoreCase("removeperm")) {
							if (sender.hasPermission("perm.list.removeperm")) {
								ListPlus list = new ListPlus(args[0]);
								if (list.exists()) {
									list.removePermissions(args[2]);
									sender.sendMessage("§rPermissions " + args[2] + " succefully removed from list "
											+ args[0] + "!");
								} else {
									sender.sendMessage("§cThis list not exists!");
								}
							} else {
								sender.sendMessage("§cYou do not have permission for this command!");
							}
						} else {
							sender.sendMessage("§cSyntax error!");
						}
					} else {
						sender.sendMessage("§cYou do not have permission for this command!");
					}
				} // no args
			}

		} // check instance

		return false;
	}

}
