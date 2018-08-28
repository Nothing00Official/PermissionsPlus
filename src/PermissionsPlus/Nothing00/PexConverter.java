package PermissionsPlus.Nothing00;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import PermissionsPlus.manager.GroupPlus;
import PermissionsPlus.manager.UserPlus;
import PermissionsPlus.manager.WorldPlus;

public class PexConverter {

	private Set<String> groups;
	private Set<String> users;
	private FileConfiguration perm;

	public PexConverter() {
		this.perm = YamlConfiguration.loadConfiguration(new File(getPex().getDataFolder() + "/permissions.yml"));
		this.groups = this.perm.getConfigurationSection("groups").getKeys(false);
		if (this.perm.get("users") != null)
			this.users = this.perm.getConfigurationSection("users").getKeys(false);
		else
			this.users = null;
	}

	public static boolean pexActive() {
		return Bukkit.getPluginManager().getPlugin("PermissionsEx") != null
				&& Bukkit.getPluginManager().getPlugin("PermissionsEx").isEnabled();
	}

	private Plugin getPex() {
		return Bukkit.getPluginManager().getPlugin("PermissionsEx");
	}

	private List<String> getPermissions(String ug) {
		if (this.groups.contains(ug) && this.perm.getStringList("groups." + ug + ".permissions") != null) {
			return this.perm.getStringList("groups." + ug + ".permissions");
		}
		if (this.users == null)
			return new ArrayList<String>();
		if (this.users.contains(ug) && this.perm.getStringList("users." + ug + ".permissions") != null) {
			return this.perm.getStringList("users." + ug + ".permissions");
		}
		return new ArrayList<String>();
	}

	private String getGroupPrefix(String ug) {
		return this.perm.getString("groups." + ug + ".options.prefix");
	}

	private String getUserPrefix(String ug) {
		return this.perm.getString("users." + ug + ".options.prefix");
	}

	private String getGroupSuffix(String ug) {
		return this.perm.getString("groups." + ug + ".options.suffix");
	}

	private String getUserSuffix(String ug) {
		return this.perm.getString("users." + ug + ".options.suffix");
	}

	private boolean hasPrefix(String ug) {
		if (this.groups.contains(ug) && this.perm.getString("groups." + ug + ".options.prefix") != null) {
			return true;
		}
		if (this.users == null)
			return false;
		if (this.users.contains(ug) && this.perm.getString("users." + ug + ".options.prefix") != null) {
			return true;
		}
		return false;
	}

	private boolean hasSuffix(String ug) {
		if (this.groups.contains(ug) && this.perm.getString("groups." + ug + ".options.suffix") != null) {
			return true;
		}
		if (this.users == null)
			return false;
		if (this.users.contains(ug) && this.perm.getString("users." + ug + ".options.suffix") != null) {
			return true;
		}
		return false;
	}

	private Set<String> getWorlds(String ug) {
		if (this.groups.contains(ug) && this.perm.get("groups." + ug + ".worlds") != null) {
			return this.perm.getConfigurationSection("groups." + ug + ".worlds").getKeys(false);
		}
		if (this.users == null)
			return null;
		if (this.users.contains(ug) && this.perm.get("users." + ug + ".worlds") != null) {
			return this.perm.getConfigurationSection("users." + ug + ".worlds").getKeys(false);
		}
		return null;
	}

	private List<String> getUserWorldPermissions(String u, String w) {
		return this.perm.getStringList("users." + u + ".worlds." + w + ".permissions");
	}

	private List<String> getGroupWorldPermissions(String g, String w) {
		return this.perm.getStringList("groups." + g + ".worlds." + w + ".permissions");
	}

	private List<String> getGroups(String u) {
		if (this.perm.getStringList("users." + u + ".group") != null)
			return this.perm.getStringList("users." + u + ".group");
		return new ArrayList<String>();
	}

	private boolean isDefault(String g) {
		return this.perm.getString("groups." + g + ".options.default") != null
				&& Boolean.parseBoolean(this.perm.getString("groups." + g + ".options.default"));
	}

	public void export() {
		for (String g : this.groups) {
			GroupPlus group = new GroupPlus(g);
			group.create();
			if (isDefault(g))
				group.setDefault();

			group.addPermission(getPermissions(g));
			if (hasPrefix(g))
				group.setPrefix(getGroupPrefix(g));
			if (hasSuffix(g))
				group.setPrefix(getGroupSuffix(g));
			if (getWorlds(g) == null)
				continue;
			for (String w : getWorlds(g)) {
				WorldPlus world = new WorldPlus(group, w);
				world.addPermission(getGroupWorldPermissions(g, w));
			}
		}
		if (this.users == null)
			return;
		for (String u : this.users) {
			UserPlus user = new UserPlus(u);
			user.addPermission(getPermissions(u));
			if (hasPrefix(u))
				user.setPrefix(getUserPrefix(u));
			if (hasSuffix(u))
				user.setPrefix(getUserSuffix(u));
			if (getWorlds(u) == null)
				continue;
			for (String w : getWorlds(u)) {
				WorldPlus world = new WorldPlus(user, w);
				world.addPermission(getUserWorldPermissions(u, w));
			}
			for (String g : getGroups(u)) {
				if (!GroupPlus.exists(g))
					continue;
				user.setGroup(new GroupPlus(g));
			}
		}
		Bukkit.getPluginManager().disablePlugin(getPex());
	}

}
