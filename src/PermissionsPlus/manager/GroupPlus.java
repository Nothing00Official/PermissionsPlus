package PermissionsPlus.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import PermissionsPlus.Nothing00.Config;
import PermissionsPlus.Nothing00.Main;
import PermissionsPlus.Nothing00.Util;

public class GroupPlus {

	private String group;

	public GroupPlus(String group) {
		this.group = group.toLowerCase();
	}

	public static boolean exists(String group) {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (pconfig.getString("Groups." + group) != null) {
			return true;
		}
		return false;
	}

	public boolean isRank() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		return pconfig.getString("Groups." + this.group + ".category") != null;
	}

	public void setCategory(Category c) {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		pconfig.set("Groups." + this.group + ".category", c.getName());
		pconfig.save();
	}

	public void removeCategory() {
		if (!isRank())
			return;
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		pconfig.get().set("Groups." + this.group + ".category", null);
		pconfig.save();
	}

	public Category getCategory() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		Category c = new Category(pconfig.getString("Groups." + this.group + ".category"));
		if (c.exists())
			return c;
		return null;
	}

	public void setDefault() {
		GroupPlus def = new GroupPlus(GroupPlus.getDefault());
		def.delete();
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		pconfig.get().set("Groups." + this.group + ".default", true);
		pconfig.save();
	}

	public void addTimedPermission(String perm, String time) {

		if (this.isDefault()) {
			for (String user : UserPlus.getUserList()) {
				UserPlus u = new UserPlus(user);
				if (u.getGroups().isEmpty() && u.getPlayer() != null) {
					Util.addTime(perm, time, u.getPlayer());
				}
			}
			return;
		}

		for (String user : UserPlus.getUserList()) {
			UserPlus u = new UserPlus(user);
			if (u.getGroups().contains(this.group) && u.getPlayer() != null) {
				Util.addTime(perm, time, u.getPlayer());
			}
		}
	}

	public void addTimedPermission(List<String> permlist, String time) {

		if (this.isDefault()) {
			for (String user : UserPlus.getUserList()) {
				UserPlus u = new UserPlus(user);
				if (u.getGroups().isEmpty() && u.getPlayer() != null) {
					for (String perm : permlist) {
						Util.addTime(perm, time, u.getPlayer());
					}
				}
			}
			return;
		}

		for (String user : UserPlus.getUserList()) {
			UserPlus u = new UserPlus(user);
			if (u.getGroups().contains(this.group) && u.getPlayer() != null) {
				for (String perm : permlist) {
					Util.addTime(perm, time, u.getPlayer());
				}
			}
		}
	}

	public static String getDefault() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		Set<String> grouplist = pconfig.getConfiguration("Groups");
		String[] grouparray = grouplist.toArray(new String[0]);
		int i = 0, pos = 0;
		boolean t = false;
		while (i < grouparray.length && !t) {
			if (pconfig.getString("Groups." + grouparray[i] + ".default") != null
					&& Boolean.parseBoolean(pconfig.getString("Groups." + grouparray[i] + ".default"))) {
				pos = i;
				t = true;
			} else {
				i++;
			}
		}
		if (t) {
			return grouparray[pos];
		} else {
			return null;
		}
	}

	public static List<String> getGroups() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		Set<String> grouplist = pconfig.getConfiguration("Groups");
		List<String> glist = new ArrayList<String>();
		for (String g : grouplist) {
			glist.add(g);
		}
		return glist;
	}

	public List<String> getWorlds() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		List<String> glist = new ArrayList<String>();
		if (pconfig.get().get("Groups." + this.group + ".worlds") == null)
			return glist;
		Set<String> worldlist = pconfig.getConfiguration("Groups." + this.group + ".worlds");

		glist.addAll(worldlist);
		return glist;
	}

	public String getPrefix() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (pconfig.getString("Groups." + this.group + ".prefix") != null) {
			return pconfig.getString("Groups." + this.group + ".prefix");
		} else {
			return "";
		}
	}

	public String getSuffix() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (pconfig.getString("Groups." + this.group + ".suffix") != null) {
			return pconfig.getString("Groups." + this.group + ".suffix");
		} else {
			return "";
		}
	}

	public List<String> getPermissions() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (pconfig.getString("Groups." + this.group + ".permissions") != null
				&& !pconfig.getStringList("Groups." + this.group + ".permissions").isEmpty()) {
			return pconfig.getStringList("Groups." + this.group + ".permissions");
		}
		return new ArrayList<String>();
	}

	public boolean isDefault() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (pconfig.getString("Groups." + this.group + ".default") != null
				&& Boolean.parseBoolean(pconfig.getString("Groups." + this.group + ".default"))) {
			return true;
		}
		return false;
	}

	public String getName() {
		return this.group;
	}

	public boolean create() {
		if (!exists(this.group)) {
			Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
			pconfig.get().createSection("Groups." + this.group + ".permissions");
			pconfig.save();
			return true;
		}
		return false;
	}

	public boolean delete() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (exists(this.group)) {
			pconfig.get().set("Groups." + this.group, null);
			pconfig.save();
			for (String u : UserPlus.getUserList()) {
				UserPlus user = new UserPlus(u);

				if (user.getGroups().contains(this.group)) {
					user.removeGroup(this);
				}
			}
			return true;
		}
		return false;
	}

	public void setPrefix(String prefix) {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		pconfig.set("Groups." + this.group + ".prefix", prefix.replace("##", " "));
		pconfig.save();
	}

	public void setSuffix(String suffix) {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		pconfig.set("Groups." + this.group + ".suffix", suffix.replace("##", " "));
		pconfig.save();
	}

	public void addPermission(List<String> permlist) {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		List<String> perms = getPermissions();
		for (String perm : permlist) {
			if (!perms.contains(perm)) {
				perms.add(perm);
				PermissionsManager.addPermInGroup(this, perm);
			}
		}
		pconfig.set("Groups." + this.group + ".permissions", perms);
		pconfig.save();
	}

	public void addPermission(String perm) {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		List<String> perms = getPermissions();

		if (!perms.contains(perm)) {
			perms.add(perm);
			PermissionsManager.addPermInGroup(this, perm);
		}

		pconfig.set("Groups." + this.group + ".permissions", perms);
		pconfig.save();
	}

	public void removePermission(List<String> permlist) {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		List<String> perms = getPermissions();
		for (String perm : permlist) {
			if (perms.contains(perm)) {
				perms.remove(perm);
				PermissionsManager.removePermInGroup(this, perm);
			}
		}
		pconfig.set("Groups." + this.group + ".permissions", perms);
		pconfig.save();
	}

	public void removePermission(String perm) {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		List<String> perms = getPermissions();

		if (perms.contains(perm)) {
			perms.remove(perm);
			PermissionsManager.removePermInGroup(this, perm);
		}

		pconfig.set("Groups." + this.group + ".permissions", perms);
		pconfig.save();
	}

}
