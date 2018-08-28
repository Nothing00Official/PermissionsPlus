package PermissionsPlus.lockable;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import PermissionsPlus.Nothing00.Config;
import PermissionsPlus.Nothing00.Main;
import PermissionsPlus.manager.GroupPlus;
import PermissionsPlus.manager.PermissionsManager;

public class PermLock implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String[] args) {

		if (sender instanceof Player) {

			if (cmd.getName().equalsIgnoreCase("permlock")) {
				Config config = new Config(Main.getPlugin().getConfig(),
						new File(Main.getPlugin().getDataFolder() + "/config.yml"));
				if (Boolean.parseBoolean(config.getString("Security.on-off-group-password"))) {
					if (args.length == 1) {
						if (args[0].equalsIgnoreCase("off")) {
							// switch off permissions
							UserLock ul = new UserLock((Player) sender);
							if (ul.isEnable()) {
								if (ul.hasPassword()) {
									if (ul.off()) {
										PermissionsManager.hashperms.get(ul.getPlayer().getUniqueId()).setlocked(true);
										sender.sendMessage(
												config.getString("Security.disabled-message").replaceAll("&", "§"));
									} else {
										if (ul.hasDefaultPermissions()) {
											sender.sendMessage(config.getString("Security.error-defaultgroup-message")
													.replaceAll("&", "§"));
											return true;
										}
										if (ul.getGroups().size() > 1) {
											sender.sendMessage(config.getString("Security.error-moregroup-message")
													.replaceAll("&", "§"));
											return true;
										}
										sender.sendMessage("§cUnknow error...");
									}
								} else {
									sender.sendMessage(
											config.getString("Security.password-message-join").replaceAll("&", "§"));
								}
							} else {
								sender.sendMessage("§cThis Permissions Security System is disabled from config.yml");
							}
						} else {
							if (sender.hasPermission("perm.lockable")) {
								UserLock ul = new UserLock((Player) sender);
								if (ul.isEnable()) {
									if (ul.hasDefaultPermissions()) {
										sender.sendMessage(config.getString("Security.error-defaultgroup-message")
												.replaceAll("&", "§"));
									} else if (ul.getGroups().size() > 1) {
										sender.sendMessage(config.getString("Security.error-moregroup-message")
												.replaceAll("&", "§"));
									} else {
										GroupPlus group = new GroupPlus(ul.getGroups().get(0));
										if (group.getPermissions().contains("perm.lockable")) {
											if (args[0].length() >= 4) {
												ul.setPassword(args[0]);
												sender.sendMessage(config.getString("Security.password-update-message")
														.replaceAll("&", "§"));
											} else {
												sender.sendMessage(config.getString("Security.password-length-message")
														.replaceAll("&", "§"));
											}
										} else {
											sender.sendMessage("§cUnknow error...");
										}
									}
								} else {
									sender.sendMessage(
											"§cThis Permissions Security System is disabled from config.yml");
								}

							} else {
								sender.sendMessage("§cYou do not have permission for this command!");
							}
						}
					} else if (args.length == 2) {
						if (args[0].equalsIgnoreCase("on")) {
							UserLock ul = new UserLock((Player) sender);
							if (ul.isEnable()) {
								if (ul.hasPassword()) {
									if (ul.matchPassword(args[1])) {
										if (ul.on()) {
											PermissionsManager.hashperms.get(ul.getPlayer().getUniqueId())
													.setlocked(false);
											sender.sendMessage(
													config.getString("Security.unlocked-message").replaceAll("&", "§"));
										} else {
											if (ul.hasDefaultPermissions()) {
												sender.sendMessage(
														config.getString("Security.error-defaultgroup-message")
																.replaceAll("&", "§"));
												return true;
											}
											if (ul.getGroups().size() > 1) {
												sender.sendMessage(config.getString("Security.error-moregroup-message")
														.replaceAll("&", "§"));
												return true;
											}
											sender.sendMessage("§cUnknow error...");
										}
									} else {
										sender.sendMessage(
												config.getString("Security.password-wrong").replaceAll("&", "§"));
									}
								} else {
									sender.sendMessage(
											config.getString("Security.password-message-join").replaceAll("&", "§"));
								}
							} else {
								sender.sendMessage("§cThis Permissions Security System is disabled from config.yml");
							}
						} else {
							sender.sendMessage("§cSyntax error");
						}
					} else {
						sender.sendMessage("§rUsage: /permlock <password>");
						sender.sendMessage("§rUsage: /permlock off");
						sender.sendMessage("§rUsage: /permlock on <password>");
					}
				} else {
					sender.sendMessage("§cThis Permissions Security System is disabled from config.yml");
				}
			}

		} else {
			System.out.println("YOU MUST BE A PLAYER!");
		} // check instance
		return false;
	}
}