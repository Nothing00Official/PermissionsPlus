package PermissionsPlus.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import PermissionsPlus.Nothing00.Config;
import PermissionsPlus.Nothing00.Main;

public class Category {

	private String category;

	public Category(String name) {
		this.category = name;
	}

	public static List<String> getCategories() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		if (pconfig.get().get("Rank-Category") == null)
			return new ArrayList<String>();
		List<String> list = new ArrayList<String>();
		list.addAll(pconfig.getConfiguration("Rank-Category"));
		return list;
	}

	public void create(GroupPlus first) {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		List<String> list = new ArrayList<String>();
		first.setCategory(this);
		list.add(first.getName());
		pconfig.set("Rank-Category." + this.category, list);
		pconfig.save();
	}

	public void delete() {

		for (String g : getGroups()) {
			GroupPlus gr = new GroupPlus(g);
			gr.removeCategory();
		}

		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		pconfig.get().set("Rank-Category." + this.category, null);
		pconfig.save();
	}

	public List<String> getGroups() {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		return pconfig.getStringList("Rank-Category." + this.category);
	}

	public void addGroup(GroupPlus group) {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		List<String> list = pconfig.getStringList("Rank-Category." + this.category);
		list.add(group.getName());
		pconfig.set("Rank-Category." + this.category, list);
		pconfig.save();
	}

	public void removeGroup(GroupPlus group) {
		Config pconfig = new Config(new File(Main.getPlugin().getDataFolder() + "/permissions.yml"));
		List<String> list = pconfig.getStringList("Rank-Category." + this.category);
		if (list.contains(group.getName())) {
			group.removeCategory();
			list.remove(group.getName());
			pconfig.set("Rank-Category." + this.category, list);
			pconfig.save();
		}
	}

	public void addGroups(String groupList) {
		String[] split = groupList.split(",");
		for (String s : split) {
			if (!GroupPlus.exists(s) || getGroups().contains(s))
				continue;
			addGroup(new GroupPlus(s));
			GroupPlus group = new GroupPlus(s);
			group.setCategory(this);
		}
	}

	public String getName() {
		return this.category;
	}

	public GroupPlus getNext(GroupPlus group) {
		int i = 0;
		boolean t = false;
		while (i < getGroups().size() && !t) {
			if (group.getName().equalsIgnoreCase(getGroups().get(i))) {
				t = true;
			} else {
				i++;
			}
		}
		if (!t)
			return null;
		if (i + 1 > getGroups().size() - 1)
			return null;
		return new GroupPlus(getGroups().get(i + 1));
	}

	public GroupPlus getPrev(GroupPlus group) {
		int i = 0;
		boolean t = false;
		while (i < getGroups().size() && !t) {
			if (group.getName().equalsIgnoreCase(getGroups().get(i))) {
				t = true;
			} else {
				i++;
			}
		}
		if (!t)
			return null;
		if (i - 1 < 0)
			return null;
		return new GroupPlus(getGroups().get(i - 1));
	}

	public boolean exists() {
		return getCategories().contains(this.category);
	}

}
