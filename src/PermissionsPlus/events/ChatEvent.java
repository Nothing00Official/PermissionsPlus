package PermissionsPlus.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import PermissionsPlus.Nothing00.Main;
import PermissionsPlus.manager.GroupPlus;
import PermissionsPlus.manager.UserPlus;

@SuppressWarnings("deprecation")
public class ChatEvent implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onChatEvent(PlayerChatEvent e) {

		if (Main.getMainMC() != null && Main.getMainMC().isEnabled())
			return;

		UserPlus user = new UserPlus(e.getPlayer().getName());
		GroupPlus group;
		if (user.hasDefaultPermissions()) {
			group = new GroupPlus(GroupPlus.getDefault());
		} else {
			group = new GroupPlus(user.getGroups().get(0));
		}
		e.getPlayer().setDisplayName("§r" + group.getPrefix().replaceAll("&", "§") + user.getPrefix().replaceAll("&", "§")
				+ e.getPlayer().getName() + user.getSuffix().replaceAll("&", "§") + group.getSuffix().replaceAll("&", "§") + "§r");
	}

}
