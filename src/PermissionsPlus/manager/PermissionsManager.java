package PermissionsPlus.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import PermissionsPlus.Nothing00.Config;
import PermissionsPlus.Nothing00.Main;

public class PermissionsManager {

	public static HashMap<UUID, EventManager> hashperms = new HashMap<UUID, EventManager>();

	private Player p;
	private String offline;
	private PermissionAttachment attachment;
	private boolean finder;

	public PermissionsManager(Player p) {
		this.p = p;
		this.offline = p.getName();
		this.attachment = p.addAttachment(Main.getPlugin());
		this.finder = false;
	}

	public PermissionsManager(String p) {
		this.p = Bukkit.getServer().getPlayer(p);
		this.offline = p;
		this.attachment = null;
		this.finder = false;
	}

	public void addUserPerm(String perm) {
		this.attachment.setPermission(perm, true);
	}

	public void removeUserPerm(String perm) {
		this.attachment.unsetPermission(perm);
	}

	public void removeAttach() {
		this.p.removeAttachment(this.attachment);
	}

	public void setFinder(boolean finder) {
		this.finder = finder;
	}

	public boolean hasFinder() {
		return this.finder;
	}

	public boolean sendFinder(String perm, boolean finder) {
		Config config = new Config(Main.getPlugin().getConfig(),
				new File(Main.getPlugin().getDataFolder() + "/config.yml"));

		if (hasFinder()) {
			if (finder) {
				if (Boolean.parseBoolean(config.getString("green-permissions")))
					this.p.sendMessage("§rPermission found for this action: " + "§a" + perm);
			} else {
				this.p.sendMessage("§rPermission found for this action: " + "§c" + perm);
			}
		}
		return finder;
	}

	public List<String> getUserRegex(String regex) {
		UserPlus user = new UserPlus(this.offline);
		List<String> result = new ArrayList<String>();
		int i = 0;
		boolean t = false;
		while (i < user.getPermissions().size() && !t) {
			if (user.getPermissions().get(i).equalsIgnoreCase(regex)) {
				result.add(user.getPermissions().get(i) + " - " + this.p.getName());
				t = true;
			} else if (user.getPermissions().get(i).toLowerCase().contains(regex.toLowerCase())) {
				result.add(user.getPermissions().get(i) + " - " + this.p.getName());
				i++;
			} else {
				i++;
			}
		}
		for (String world : user.getWorlds()) {
			t = false;
			i = 0;
			WorldPlus w = new WorldPlus(user, world);
			while (i < w.getPermissions().size() && !t) {
				if (w.getPermissions().get(i).equalsIgnoreCase(regex)) {
					result.add(w.getPermissions().get(i) + " - " + this.offline + ": " + world);
					t = true;
				} else if (w.getPermissions().get(i).toLowerCase().contains(regex.toLowerCase())) {
					result.add(w.getPermissions().get(i) + " - " + this.offline + ": " + world);
					i++;
				} else {
					i++;
				}
			}
		}
		if (!user.getGroups().isEmpty()) {
			for (String group : user.getGroups()) {
				result.addAll(getGroupRegex(new GroupPlus(group), regex));
			}
		} else {
			result.addAll(getGroupRegex(new GroupPlus(GroupPlus.getDefault()), regex));
		}
		return result;
	}

	public static List<String> getGroupRegex(GroupPlus group, String regex) {
		List<String> result = new ArrayList<String>();
		int i = 0;
		boolean t = false;
		while (i < group.getPermissions().size() && !t) {
			if (group.getPermissions().get(i).equalsIgnoreCase(regex)) {
				result.add(group.getPermissions().get(i) + " - " + group.getName());
				t = true;
			} else if (group.getPermissions().get(i).toLowerCase().contains(regex.toLowerCase())) {
				result.add(group.getPermissions().get(i) + " - " + group.getName());
				i++;
			} else {
				i++;
			}
		}
		for (String world : group.getWorlds()) {
			t = false;
			i = 0;
			WorldPlus w = new WorldPlus(group, world);
			while (i < w.getPermissions().size() && !t) {
				if (w.getPermissions().get(i).equalsIgnoreCase(regex)) {
					result.add(w.getPermissions().get(i) + " - " + group.getName() + ": " + world);
					t = true;
				} else if (w.getPermissions().get(i).toLowerCase().contains(regex.toLowerCase())) {
					result.add(w.getPermissions().get(i) + " - " + group.getName() + ": " + world);
					i++;
				} else {
					i++;
				}
			}
		}
		return result;
	}

	public static void addPermInGroup(GroupPlus group, String perm) {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			UserPlus user = new UserPlus(player.getName());
			if (group.isDefault()) {
				if (user.getGroups().isEmpty()) {
					hashperms.get(player.getUniqueId()).addUserPerm(perm);
				}
			}
			if (user.getGroups().contains(group.getName())) {
				hashperms.get(player.getUniqueId()).addUserPerm(perm);
			}
		}
	}

	public static void removePermInGroup(GroupPlus group, String perm) {
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			UserPlus user = new UserPlus(player.getName());
			if (group.isDefault()) {
				if (user.getGroups().isEmpty()) {
					hashperms.get(player.getUniqueId()).removeUserPerm(perm);
				}
			}
			if (user.getGroups().contains(group.getName())) {
				hashperms.get(player.getUniqueId()).removeUserPerm(perm);
			}
		}
	}

	public void addDefaultPermissions() {
		GroupPlus group = new GroupPlus(GroupPlus.getDefault());
		for (String perm : group.getPermissions()) {
			addUserPerm(perm);
		}
	}

	public static void addPermissionGroupInWorld(GroupPlus group, WorldPlus wp, String perm) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			UserPlus user = new UserPlus(p.getName());
			if (user.getGroups().contains(group.getName()) || group.isDefault()) {
				if (user.getWorlds().contains(wp.getWorld().getName())
						&& p.getLocation().getWorld().equals(wp.getWorld())) {
					hashperms.get(p.getUniqueId()).addUserPerm(perm);
				}
			}
		}
	}

	public static void removePermissionGroupInWorld(GroupPlus group, WorldPlus wp, String perm) {
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			UserPlus user = new UserPlus(p.getName());
			if (user.getGroups().contains(group.getName())) {
				if (user.getWorlds().contains(wp.getWorld().getName())
						&& p.getLocation().getWorld().equals(wp.getWorld())) {
					hashperms.get(p.getUniqueId()).removeUserPerm(perm);
				}
			}
		}
	}

	public static void addPermissionUserInWorld(UserPlus user, WorldPlus wp, String perm) {
		if (user.getWorlds().contains(wp.getWorld().getName()) && user.getPlayer() != null
				&& user.getPlayer().getLocation().getWorld().equals(wp.getWorld())) {
			hashperms.get(user.getPlayer().getUniqueId()).addUserPerm(perm);
		}
	}

	public static void removePermissionUserInWorld(UserPlus user, WorldPlus wp, String perm) {
		if (user.getWorlds().contains(wp.getWorld().getName()) && user.getPlayer() != null
				&& user.getPlayer().getLocation().getWorld().equals(wp.getWorld())) {
			hashperms.get(user.getPlayer().getUniqueId()).removeUserPerm(perm);
		}
	}

	public static void refreshPermissions() {
		if (Bukkit.getServer().getOnlinePlayers().isEmpty())
			return;
		hashperms.clear();
		for (Player players : Bukkit.getServer().getOnlinePlayers()) {

			EventManager em = new EventManager(players);
			hashperms.put(players.getUniqueId(), em);
			UserPlus user = new UserPlus(players.getName());
			for (String perm : user.getPermissions()) {
				em.addUserPerm(perm);
			}
			if (user.getGroups().isEmpty()) {
				em.addDefaultPermissions();
				continue;
			}
			for (String g : user.getGroups()) {
				GroupPlus group = new GroupPlus(g);
				for (String gperm : group.getPermissions()) {
					em.addUserPerm(gperm);
				}
			}
		}
	}

	public static boolean thereIsDefault() {
		Config pconfig = new Config(
				new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		int i = 0;
		if (GroupPlus.getDefault() == null) {

			return false;
		} else {
			for (String g : GroupPlus.getGroups()) {
				if (pconfig.getString("Groups." + g + ".default") != null
						&& Boolean.parseBoolean(pconfig.getString("Groups." + g + ".default"))) {
					i++;
				}
			}
			if (i > 1) {
				return false;
			}
		}
		return true;
	}

}
