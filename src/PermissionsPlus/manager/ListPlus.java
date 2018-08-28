package PermissionsPlus.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import PermissionsPlus.Nothing00.Config;
import PermissionsPlus.Nothing00.Main;

public class ListPlus {

	private String name;

	public ListPlus(String name) {
		this.name = name;
	}

	public static List<String> getLists() {
		Config config = new Config(Main.getPlugin().getConfig(),
				new File(Main.getPlugin().getDataFolder() + "/config.yml"));
		Set<String> lists = config.getConfiguration("PermissionsLists");
		List<String> list = new ArrayList<String>();
		for (String l : lists) {
			list.add(l);
		}
		return list;
	}

	public List<String> getPermissions() {
		Config config = new Config(Main.getPlugin().getConfig(),
				new File(Main.getPlugin().getDataFolder() + "/config.yml"));
		return config.getStringList("PermissionsLists." + this.name);
	}

	public void create() {
		Config config = new Config(Main.getPlugin().getConfig(),
				new File(Main.getPlugin().getDataFolder() + "/config.yml"));
		config.get().createSection("PermissionsLists." + this.name);
		config.save();
	}

	public void delete() {
		Config config = new Config(Main.getPlugin().getConfig(),
				new File(Main.getPlugin().getDataFolder() + "/config.yml"));
		config.get().set("PermissionsLists." + this.name, null);
		config.save();
	}

	public boolean exists() {
		Config config = new Config(Main.getPlugin().getConfig(),
				new File(Main.getPlugin().getDataFolder() + "/config.yml"));
		if (config.get().get("PermissionsLists." + this.name) != null) {
			return true;
		}
		return false;
	}

	public void addPermissions(String perm) {
		Config config = new Config(Main.getPlugin().getConfig(),
				new File(Main.getPlugin().getDataFolder() + "/config.yml"));
		List<String> list = getPermissions();
		list.add(perm);
		config.set("PermissionsLists." + this.name, list);
		config.save();
	}

	public void removePermissions(String perm) {
		Config config = new Config(Main.getPlugin().getConfig(),
				new File(Main.getPlugin().getDataFolder() + "/config.yml"));
		List<String> list = getPermissions();
		if (list.contains(perm)) {
			list.remove(perm);
			config.set("PermissionsLists." + this.name, list);
			config.save();
		}
	}

	public static boolean isList(String str) {
		if (str.length() > 5 && str.substring(0, 5).equalsIgnoreCase("list:")
				&& getLists().contains(str.substring(5, str.length()))) {
			return true;
		}
		return false;
	}

}
