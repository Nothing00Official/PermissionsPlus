package PermissionsPlus.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import PermissionsPlus.Nothing00.Config;
import PermissionsPlus.Nothing00.Main;
import PermissionsPlus.Nothing00.Util;
import PermissionsPlus.lockable.UserLock;

public class UserPlus {

	private String name;
	private Player p;

	public UserPlus(String name) {
		this.p = Bukkit.getPlayer(name);
		this.name = name.toLowerCase();
	}

	public static String getUsers() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		Set<String> userlist = pconfig.getConfiguration("Users");
		String[] array = userlist.toArray(new String[0]);
		return "§a" + String.join("§7, §a", array);
	}

	public static List<String> getUserList() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		Set<String> userlist = pconfig.getConfiguration("Users");
		List<String> ulist = new ArrayList<String>();
		for (String u : userlist) {
			ulist.add(u);
		}
		return ulist;
	}

	public boolean promote() {
		GroupPlus group = null;
		if (getGroups().isEmpty()) {
			group = new GroupPlus(GroupPlus.getDefault());
		} else {
			group = new GroupPlus(getGroups().get(0));
		}
		if (!group.isRank())
			return false;
		Category c = group.getCategory();
		GroupPlus next = c.getNext(group);
		if (next == null)
			return false;
		if (!getGroups().isEmpty())
			removeGroup(new GroupPlus(getGroups().get(0)));
		setGroup(next);
		return true;
	}

	public boolean demote() {
		GroupPlus group = null;
		if (getGroups().isEmpty())
			return false;

		group = new GroupPlus(getGroups().get(0));

		if (!group.isRank())
			return false;
		Category c = group.getCategory();
		GroupPlus prev = c.getPrev(group);
		if (prev == null)
			return false;
		removeGroup(new GroupPlus(getGroups().get(0)));
		setGroup(prev);
		return true;
	}

	public void addTimedPermission(String perm, String time) {

		if (this.p != null) {
			Util.addTime(perm, time, this.p);
		}

	}

	public void addTimedPermission(List<String> permlist, String time) {

		if (this.p != null) {
			for (String perm : permlist) {
				Util.addTime(perm, time, this.p);
			}
		}

	}

	public List<String> getWorlds() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		List<String> glist = new ArrayList<String>();
		if (pconfig.get().get("Users." + this.name + ".worlds") == null)
			return glist;
		Set<String> worldlist = pconfig.getConfiguration("Users." + this.name + ".worlds");
		glist.addAll(worldlist);
		return glist;
	}

	public String getName() {
		return this.name;
	}

	public List<String> getGroups() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (pconfig.getString("Users." + this.name + ".group") != null
				&& !pconfig.getStringList("Users." + this.name + ".group").isEmpty()) {
			return pconfig.getStringList("Users." + this.name + ".group");
		}
		return new ArrayList<String>();
	}

	public Player getPlayer() {
		return this.p;
	}

	public String getPrefix() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (pconfig.getString("Users." + this.name + ".prefix") != null) {
			return pconfig.getString("Users." + this.name + ".prefix");
		} else {
			return "";
		}
	}

	public String getSuffix() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (pconfig.getString("Users." + this.name + ".suffix") != null) {
			return pconfig.getString("Users." + this.name + ".suffix");
		} else {
			return "";
		}
	}

	public List<String> getPermissions() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (pconfig.getString("Users." + this.name + ".permissions") != null
				&& !pconfig.getStringList("Users." + this.name + ".permissions").isEmpty()) {
			return pconfig.getStringList("Users." + this.name + ".permissions");
		}
		return new ArrayList<String>();
	}

	public String getInfo() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (getUserList().contains(this.name)) {
			String nome = this.name;
			String group = null;
			String prefix = null;
			String suffix = null;
			String pexs = null;
			String uuid = null;
			String worlds = null;

			String[] groups = getGroups().toArray(new String[0]);
			if (getGroups().size() > 1) {
				group = "§2Groups: §r\n" + String.join("\n", groups);
			} else {
				if (getGroups().isEmpty()) {
					group = "§2Group: §r" + GroupPlus.getDefault();
				} else {
					group = "§2Group: §r" + String.join("", groups);
				}
			}

			prefix = getPrefix();
			if (!prefix.isEmpty()) {
				prefix = "§bPrefix: " + prefix;
			} else {
				prefix = null;
			}

			suffix = getSuffix();
			if (!suffix.isEmpty()) {
				suffix = "§bSuffix: " + suffix;
			} else {
				suffix = null;
			}

			if (!getPermissions().isEmpty()) {
				String[] perms = getPermissions().toArray(new String[0]);
				pexs = "§7Permissions: §r\n - §r" + String.join("\n - §r", perms);
			} else {
				pexs = "§7Permissions: §r[]";
			}

			if (!getWorlds().isEmpty()) {
				worlds = "";
				for (String world : getWorlds()) {
					WorldPlus wp = new WorldPlus(this, world);
					if (wp.getPermissions().isEmpty())
						continue;
					String[] perms = wp.getPermissions().toArray(new String[0]);
					worlds += "§2" + world + ":\n- §r§o" + String.join("\n - §r§o", perms) + "\n";
				}
			}

			if (pconfig.getString("Users." + nome + ".uuid") != null) {
				uuid = pconfig.getString("Users." + nome + ".uuid");
			} else {
				Util.setUUID(p.getName());
			}

			String retur = "§a" + nome + ":" + "\n" + group;
			if (prefix != null) {
				retur += "\n" + prefix;
			}
			if (suffix != null) {
				retur += "\n" + suffix;
			}
			if (pexs != null) {
				retur += "\n" + pexs;
			}
			if (uuid != null) {
				retur += "\n§cUUID: §r" + uuid;
			}
			if (worlds != null) {
				retur += "\n" + worlds;
			}
			return retur;

		} else {
			return "§cSyntax error!";
		}
	}

	public void setGroup(GroupPlus group) {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (pconfig.getStringList("Users." + this.name + ".group") == null) {
			pconfig.get().createSection("Users." + this.name + ".group");
		}
		List<String> groups = getGroups();
		if (this.p != null) {
			PermissionsManager.hashperms.get(this.p.getUniqueId()).addGroupPermToUser(group);
		}
		if (group.isDefault()) {
			pconfig.get().set("Users." + this.name + ".group", null);
		} else {
			groups.add(group.getName());
			pconfig.set("Users." + this.name + ".group", groups);
		}
		pconfig.save();

		Util.setUUID(this.name);

	}

	public void removeGroup(GroupPlus group) {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (!getGroups().isEmpty()) {
			List<String> groups = getGroups();

			groups.remove(group.getName());
			pconfig.set("Users." + this.name + ".group", groups);
			pconfig.save();

			if (this.p != null) {
				PermissionsManager.hashperms.get(this.p.getUniqueId()).removeGroupPermFromUser(group);
				if (UserLock.isLocked(group)) {
					UserLock ul = new UserLock(this.p);
					ul.removePassword();
				}
				PermissionsManager.hashperms.get(this.p.getUniqueId()).addGroupPerm();
			}

			if (this.p != null && groups.isEmpty()) {
				PermissionsManager.hashperms.get(this.p.getUniqueId()).addDefaultPermissions();
				UserLock ul = new UserLock(this.p);
				ul.removePassword();
			}

		}

	}

	public void addPermission(List<String> permlist) {

		Util.setUUID(this.name);

		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		List<String> perms = getPermissions();
		for (String perm : permlist) {
			if (!perms.contains(perm)) {
				perms.add(perm);
				if (this.p != null) {
					PermissionsManager.hashperms.get(this.p.getUniqueId()).addUserPerm(perm);
				}
			}
		}
		pconfig.set("Users." + this.name + ".permissions", perms);
		pconfig.save();
	}

	public void addPermission(String perm) {

		Util.setUUID(this.name);

		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		List<String> perms = getPermissions();

		if (!perms.contains(perm)) {
			perms.add(perm);
			if (this.p != null) {
				PermissionsManager.hashperms.get(this.p.getUniqueId()).addUserPerm(perm);
			}
		}

		pconfig.set("Users." + this.name + ".permissions", perms);
		pconfig.save();
	}

	public void removePermission(List<String> permlist) {

		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		List<String> perms = getPermissions();
		for (String perm : permlist) {
			if (perms.contains(perm)) {
				perms.remove(perm);
				if (this.p != null) {
					PermissionsManager.hashperms.get(this.p.getUniqueId()).removeUserPerm(perm);
				}
			}
		}
		pconfig.set("Users." + this.name + ".permissions", perms);
		pconfig.save();
	}

	public void removePermission(String perm) {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		List<String> perms = getPermissions();

		if (perms.contains(perm)) {
			perms.remove(perm);
			if (this.p != null) {
				PermissionsManager.hashperms.get(this.p.getUniqueId()).removeUserPerm(perm);
			}
		}

		pconfig.set("Users." + this.name + ".permissions", perms);
		pconfig.save();
	}

	public void setPrefix(String prefix) {
		Util.setUUID(this.name);

		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		pconfig.set("Users." + this.name + ".prefix", prefix.replace("##", " "));
		pconfig.save();
	}

	public void setSuffix(String suffix) {
		Util.setUUID(this.name);

		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		pconfig.set("Users." + this.name + ".suffix", suffix.replace("##", " "));
		pconfig.save();
	}

	public boolean hasDefaultPermissions() {
		if (getGroups().isEmpty()) {
			return true;
		}
		return false;
	}

}
