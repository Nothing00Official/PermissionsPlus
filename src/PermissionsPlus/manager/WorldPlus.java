package PermissionsPlus.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

import PermissionsPlus.Nothing00.Config;
import PermissionsPlus.Nothing00.Main;
import PermissionsPlus.Nothing00.Util;

public class WorldPlus {

	private UserPlus user;
	private World world;
	private GroupPlus group;

	public WorldPlus(UserPlus user, String world) {
		this.user = user;
		this.world = Util.findWorld(world);
		this.group = null;
	}

	public WorldPlus(GroupPlus group, String world) {
		this.user = null;
		this.world = Util.findWorld(world);
		this.group = group;
	}

	public boolean exists() {
		return Bukkit.getServer().getWorlds().contains(this.world);
	}

	public List<String> getPermissions() {
		Config pconfig = new Config(
				new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (!this.hasPermissions())
			return new ArrayList<String>();
		if (this.user == null) {
			return pconfig.getStringList(
					"Groups." + this.group.getName() + ".worlds." + this.world.getName() + ".permissions");
		} else if (group == null) {
			return pconfig
					.getStringList("Users." + this.user.getName() + ".worlds." + this.world.getName() + ".permissions");
		}
		return new ArrayList<String>();
	}

	public boolean hasPermissions() {
		Config pconfig = new Config(
				new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (this.user == null) {
			return pconfig.getStringList(
					"Groups." + this.group.getName() + ".worlds." + this.world.getName() + ".permissions") != null;
		} else if (group == null) {
			return pconfig.getStringList(
					"Users." + this.user.getName() + ".worlds." + this.world.getName() + ".permissions") != null;
		}
		return false;
	}

	public void addPermission(String perm) {
		List<String> perms = this.getPermissions();
		if (perms.contains(perm))
			return;
		perms.add(perm);
		Config pconfig = new Config(
				new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (this.user == null) {
			PermissionsManager.addPermissionGroupInWorld(this.group, this, perm);
			pconfig.get().set("Groups." + this.group.getName() + ".worlds." + this.world.getName() + ".permissions",
					perms);
		} else if (group == null) {
			PermissionsManager.addPermissionUserInWorld(this.user, this, perm);
			pconfig.get().set("Users." + this.user.getName() + ".worlds." + this.world.getName() + ".permissions",
					perms);
		}
		pconfig.save();
	}

	public void addPermission(List<String> permlist) {
		Config pconfig = new Config(
				new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		List<String> perms = getPermissions();
		for (String perm : permlist) {
			if (perms.contains(perm))
				continue;
			perms.add(perm);
			if (this.user == null) {
				PermissionsManager.addPermissionGroupInWorld(this.group, this, perm);
			} else if (group == null) {
				PermissionsManager.addPermissionUserInWorld(this.user, this, perm);
			}
		}
		if (this.user == null) {
			pconfig.get().set("Groups." + this.group.getName() + ".worlds." + this.world.getName() + ".permissions",
					perms);
		} else if (group == null) {
			pconfig.get().set("Users." + this.user.getName() + ".worlds." + this.world.getName() + ".permissions",
					perms);
		}
		pconfig.save();
	}

	public void removePermission(String perm) {
		List<String> perms = this.getPermissions();
		if (!perms.contains(perm))
			return;
		perms.remove(perm);
		Config pconfig = new Config(
				new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (this.user == null) {
			PermissionsManager.removePermissionGroupInWorld(this.group, this, perm);
			pconfig.get().set("Groups." + this.group.getName() + ".worlds." + this.world.getName() + ".permissions",
					perms);
		} else if (group == null) {
			PermissionsManager.removePermissionUserInWorld(this.user, this, perm);
			pconfig.get().set("Users." + this.user.getName() + ".worlds." + this.world.getName() + ".permissions",
					perms);
		}
		pconfig.save();
	}

	public void removePermission(List<String> permlist) {
		Config pconfig = new Config(
				new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		List<String> perms = getPermissions();
		for (String perm : permlist) {
			if (!perms.contains(perm))
				continue;
			perms.remove(perm);
			if (this.user == null) {
				PermissionsManager.removePermissionGroupInWorld(this.group, this, perm);
			} else if (group == null) {
				PermissionsManager.removePermissionUserInWorld(this.user, this, perm);
			}
		}
		if (this.user == null) {
			pconfig.get().set("Groups." + this.group.getName() + ".worlds." + this.world.getName() + ".permissions",
					perms);
		} else if (group == null) {
			pconfig.get().set("Users." + this.user.getName() + ".worlds." + this.world.getName() + ".permissions",
					perms);
		}
		pconfig.save();
	}

	public World getWorld() {
		return this.world;
	}

}
