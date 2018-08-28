package PermissionsPlus.manager;

import org.bukkit.entity.Player;

public class EventManager extends PermissionsManager {

	private Player p;
	private boolean locked;

	public EventManager(Player p) {
		super(p);
		this.p = p;
		this.locked = false;
	}

	public void permOnJoin() {
		addGroupPerm();
		addUserPerm();
	}

	public void addGroupPerm() {
		UserPlus user = new UserPlus(this.p.getName());
		if (user.getGroups().isEmpty()) {
			super.addDefaultPermissions();
			return;
		}
		for (String g : user.getGroups()) {
			GroupPlus group = new GroupPlus(g);
			for (String perm : group.getPermissions()) {
				super.addUserPerm(perm);
			}
		}
	}

	public void removeGroupPerm() {
		UserPlus user = new UserPlus(this.p.getName());
		if (user.getGroups().isEmpty())
			return;
		for (String g : user.getGroups()) {
			GroupPlus group = new GroupPlus(g);
			for (String perm : group.getPermissions()) {
				super.removeUserPerm(perm);
			}
		}
	}

	private void addUserPerm() {
		UserPlus user = new UserPlus(this.p.getName());
		for (String perm : user.getPermissions()) {
			super.addUserPerm(perm);
		}
	}

	public void addGroupPermToUser(GroupPlus group) {
		if (group.isDefault()) {
			removeGroupPerm();
			super.addDefaultPermissions();
			return;
		}
		for (String perm : group.getPermissions()) {
			super.addUserPerm(perm);
		}
	}

	public void removeGroupPermFromUser(GroupPlus group) {
		for (String perm : group.getPermissions()) {
			super.removeUserPerm(perm);
		}
	}

	public void changePermissionsWorld(String w) {
		UserPlus user = new UserPlus(this.p.getName());

		if (user.getWorlds().contains(w)) {
			WorldPlus wp = new WorldPlus(user, w);
			for (String perm : wp.getPermissions()) {
				super.addUserPerm(perm);
			}
		}
		for (String group : GroupPlus.getGroups()) {
			if (!user.getGroups().contains(group) && !GroupPlus.getDefault().equals(group))
				continue;
			GroupPlus gp = new GroupPlus(group);
			if (gp.getWorlds().contains(w)) {
				WorldPlus wp = new WorldPlus(gp, w);
				for (String perm : wp.getPermissions()) {
					super.addUserPerm(perm);

				}
			}
		}

		for (String world : user.getWorlds()) {
			if (world.equals(w))
				continue;
			WorldPlus wp = new WorldPlus(user, world);
			for (String perm : wp.getPermissions()) {
				super.removeUserPerm(perm);
			}
		}
		for (String group : GroupPlus.getGroups()) {
			if (!user.getGroups().contains(group) && !GroupPlus.getDefault().equals(group))
				continue;
			GroupPlus gp = new GroupPlus(group);
			for (String world : gp.getWorlds()) {
				if (world.equals(w))
					continue;
				WorldPlus wp = new WorldPlus(gp, world);

				for (String perm : wp.getPermissions()) {
					super.removeUserPerm(perm);
				}
			}
		}
	}

	public boolean isLocked() {
		return this.locked;
	}

	public void setlocked(boolean lock) {
		this.locked = lock;
	}

}
