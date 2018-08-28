package PermissionsPlus.Nothing00;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;

import PermissionsPlus.manager.PermissionsManager;

public class PermissionBukkit extends PermissibleBase {

	private Player op;

	public PermissionBukkit(Player opable) {
		super(opable);
		this.op = opable;
	}

	@Override
	public boolean hasPermission(String perm) {
		if (!Main.onlyOp()) {
			if (perm.length() > 5 && perm.substring(0, 5).equals("perm.")) {
				if (hasPerm("*") || hasPerm("'*'")) {
					if (super.isOp() || !super.isOp()) {
						return PermissionsManager.hashperms.get(this.op.getUniqueId()).sendFinder(perm, true);
					}
				}
				if (super.isOp() && !hasPerm(perm)) {
					return PermissionsManager.hashperms.get(this.op.getUniqueId()).sendFinder(perm, false);
				}
				if (!super.isOp() && hasPerm(perm)) {
					return PermissionsManager.hashperms.get(this.op.getUniqueId()).sendFinder(perm, true);
				}
				if (super.isOp() && hasPerm(perm)) {
					return PermissionsManager.hashperms.get(this.op.getUniqueId()).sendFinder(perm, true);
				}
				if (!super.isOp() && !hasPerm(perm)) {
					return PermissionsManager.hashperms.get(this.op.getUniqueId()).sendFinder(perm, false);
				}
			}
		}
		if (super.isOp() && (!hasPerm("-" + perm))) {
			return PermissionsManager.hashperms.get(this.op.getUniqueId()).sendFinder(perm, true);
		}

		if (super.hasPermission("'*'") || super.hasPermission("*")) {
			return PermissionsManager.hashperms.get(this.op.getUniqueId()).sendFinder(perm, true);
		}

		if (super.hasPermission("-" + perm)) {
			return PermissionsManager.hashperms.get(this.op.getUniqueId()).sendFinder(perm, false);
		}

		if (perm.contains(".")) {
			char[] c = perm.toCharArray();
			int i = 0;
			while (c[i] != '.') {
				i++;
			}
			String radice = perm.substring(0, i);
			if (super.hasPermission(radice + ".*")) {
				return PermissionsManager.hashperms.get(this.op.getUniqueId()).sendFinder(perm, true);
			}
		}

		return PermissionsManager.hashperms.get(this.op.getUniqueId()).sendFinder(perm, super.hasPermission(perm));

	}

	@SuppressWarnings("unchecked")
	private boolean hasPerm(String inName) {

		if (inName == null) {
			throw new IllegalArgumentException("Permission name cannot be null");
		}

		Map<String, PermissionAttachmentInfo> permissions = new HashMap<String, PermissionAttachmentInfo>();

		try {

			Field f = PermissibleBase.class.getDeclaredField("permissions");

			f.setAccessible(true);

			permissions = (Map<String, PermissionAttachmentInfo>) f.get(this);

			f.setAccessible(false);

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		String name = inName.toLowerCase();

		if (isPermissionSet(name)) {
			return permissions.get(name).getValue();
		} else {
			Permission perm = Bukkit.getServer().getPluginManager().getPermission(name);
			if (perm != null) {
				return perm.getDefault().getValue(false);
			} else {
				return Permission.DEFAULT_PERMISSION.getValue(false);
			}
		}
	}

}
