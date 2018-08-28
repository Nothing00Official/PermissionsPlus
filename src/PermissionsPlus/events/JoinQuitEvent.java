package PermissionsPlus.events;

import java.io.File;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import PermissionsPlus.Nothing00.Config;
import PermissionsPlus.Nothing00.Main;
import PermissionsPlus.Nothing00.PermissionBukkit;
import PermissionsPlus.Nothing00.Util;
import PermissionsPlus.lockable.UserLock;
import PermissionsPlus.manager.EventManager;
import PermissionsPlus.manager.GroupPlus;
import PermissionsPlus.manager.PermissionsManager;

public class JoinQuitEvent implements Listener {

	@EventHandler(priority=EventPriority.NORMAL)
	public void onJoin(PlayerJoinEvent e) {
		UserLock ul = new UserLock(e.getPlayer());
		Config config = new Config(Main.getPlugin().getConfig(),
				new File(Main.getPlugin().getDataFolder() + "/config.yml"));
		if (!ul.isEnable())
			return;
		if (ul.getGroups().isEmpty())
			return;
		if (UserLock.isLocked(new GroupPlus(ul.getGroups().get(0)))) {
			if (ul.hasPassword()) {
				e.getPlayer().sendMessage(config.getString("Security.unlock-message-join").replaceAll("&", "§"));
			} else {
				e.getPlayer().sendMessage(config.getString("Security.password-message-join").replaceAll("&", "§"));
			}
		}

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent e) {
		if (PermissionsManager.hashperms.containsKey(e.getPlayer().getUniqueId())) {
			PermissionsManager.hashperms.get(e.getPlayer().getUniqueId()).removeAttach();
			PermissionsManager.hashperms.remove(e.getPlayer().getUniqueId());
		}
		Util.deop(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onLogin(PlayerLoginEvent e) throws ClassNotFoundException {
		Class<?> classe = Class
				.forName(Bukkit.getServer().getClass().getPackage().getName() + ".entity.CraftHumanEntity");
		try {
			Field f = classe.getDeclaredField("perm");
			f.setAccessible(true);
			f.set(e.getPlayer(), new PermissionBukkit(e.getPlayer()));
			f.setAccessible(false);

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e1) {
			Bukkit.getPluginManager().disablePlugin(Main.getPlugin());
			e1.printStackTrace();
		}

		EventManager em = new EventManager(e.getPlayer());
		PermissionsManager.hashperms.put(e.getPlayer().getUniqueId(), em);
		UserLock ul = new UserLock(e.getPlayer());

		if (ul.isEnable()) {
			if (ul.getGroups().isEmpty()) {
				em.permOnJoin();
				PermissionsManager.hashperms.get(e.getPlayer().getUniqueId())
						.changePermissionsWorld(e.getPlayer().getLocation().getWorld().getName());
			} else {
				if (UserLock.isLocked(new GroupPlus(ul.getGroups().get(0)))) {
					if (ul.hasPassword()) {
						PermissionsManager.hashperms.get(e.getPlayer().getUniqueId()).setlocked(true);
						em.addDefaultPermissions();
					} else {
						em.permOnJoin();
						PermissionsManager.hashperms.get(e.getPlayer().getUniqueId())
								.changePermissionsWorld(e.getPlayer().getLocation().getWorld().getName());
					}
				} else {
					PermissionsManager.hashperms.get(e.getPlayer().getUniqueId())
							.changePermissionsWorld(e.getPlayer().getLocation().getWorld().getName());
					em.permOnJoin();
				}
			}
		} else {
			PermissionsManager.hashperms.get(e.getPlayer().getUniqueId())
					.changePermissionsWorld(e.getPlayer().getLocation().getWorld().getName());
			em.permOnJoin();
		}

		Util.setUUID(e.getPlayer().getName());

		if (Util.protecteduuid(e.getPlayer())) {
			e.getPlayer().kickPlayer(Main.getPlugin().getConfig()
					.getString("Security.uuid-protection-message").replaceAll("&", "§"));
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onWorld(PlayerChangedWorldEvent e) {
		if (PermissionsManager.hashperms.get(e.getPlayer().getUniqueId()).isLocked())
			return;

		PermissionsManager.hashperms.get(e.getPlayer().getUniqueId())
				.changePermissionsWorld(e.getPlayer().getLocation().getWorld().getName());
	}

}
