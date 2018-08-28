package PermissionsPlus.lockable;

import java.io.File;

import org.bukkit.entity.Player;

import PermissionsPlus.Nothing00.Config;
import PermissionsPlus.Nothing00.Main;
import PermissionsPlus.manager.GroupPlus;
import PermissionsPlus.manager.PermissionsManager;
import PermissionsPlus.manager.UserPlus;

public class UserLock extends UserPlus {

	private boolean enabled;
	private Player player;
	private String name;

	public UserLock(Player player) {
		super(player.getName());
		this.player = player;
		this.name = player.getName().toLowerCase();
		Config config = new Config(Main.getPlugin().getConfig(),
				new File(Main.getPlugin().getDataFolder() + "/config.yml"));
		this.enabled = Boolean.parseBoolean(config.getString("Security.on-off-group-password"));
	}

	public boolean isEnable() {
		return this.enabled;
	}

	public boolean hasPassword() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (pconfig.getString("Users." + this.name + ".password") != null) {
			return true;
		}
		return false;
	}

	public void setPassword(String psw) {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (this.enabled) {
			pconfig.get().set("Users." + this.name + ".password", psw);
			pconfig.save();
		}
	}

	public void removePassword() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (hasPassword()) {
			pconfig.get().set("Users." + this.name + ".password", null);
			pconfig.save();
		}
	}

	public boolean matchPassword(String psw) {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (psw.equals(pconfig.getString("Users." + this.player.getName() + ".password"))) {
			return true;
		}
		return false;
	}

	public boolean on() {
		if (super.getGroups().isEmpty() || super.getGroups().size() > 1) {
			removePassword();
			return false;
		}
		GroupPlus group = new GroupPlus(super.getGroups().get(0));
		if (group.getPermissions().contains("perm.lockable")) {
			for (String perm : group.getPermissions()) {
				PermissionsManager.hashperms.get(this.player.getUniqueId()).addUserPerm(perm);
			}
		} else {
			removePassword();
			return false;
		}
		return true;
	}

	public boolean off() {
		if (super.getGroups().isEmpty() || super.getGroups().size() > 1) {
			return false;
		}
		GroupPlus group = new GroupPlus(super.getGroups().get(0));
		for (String perm : group.getPermissions()) {
			PermissionsManager.hashperms.get(this.player.getUniqueId()).removeUserPerm(perm);
		}
		PermissionsManager.hashperms.get(this.player.getUniqueId()).addDefaultPermissions();
		return true;
	}

	public static boolean isLocked(GroupPlus group) {
		if (group.getPermissions().contains("perm.lockable")) {
			return true;
		}
		return false;
	}

}
