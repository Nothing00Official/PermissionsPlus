package PermissionsPlus.Nothing00;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import PermissionsPlus.commands.CategoryCMDS;
import PermissionsPlus.commands.GroupCMDS;
import PermissionsPlus.commands.GuiCMDS;
import PermissionsPlus.commands.MainCommands;
import PermissionsPlus.commands.UserCMDS;
import PermissionsPlus.events.ChatEvent;
import PermissionsPlus.events.GuiEvent;
import PermissionsPlus.events.JoinQuitEvent;
import PermissionsPlus.lockable.PermLock;
import PermissionsPlus.manager.EventManager;
import PermissionsPlus.manager.PermissionsManager;

public class Main extends JavaPlugin {

	public static Main plugin;
	public static boolean convert;

	public void onEnable() {
		plugin = this;
		convert = false;
		System.out.println("PermissionsPlus enabled!");
		getCommand("PermissionsPlus").setExecutor(new MainCommands());
		getCommand("permgroup").setExecutor(new GroupCMDS());
		getCommand("permgroupgui").setExecutor(new GuiCMDS());
		getCommand("permuser").setExecutor(new UserCMDS());
		getCommand("permlock").setExecutor(new PermLock());
		getCommand("permrank").setExecutor(new CategoryCMDS());
		Bukkit.getPluginManager().registerEvents(new GuiEvent(), this);
		Bukkit.getPluginManager().registerEvents(new JoinQuitEvent(), this);
		Bukkit.getPluginManager().registerEvents(new ChatEvent(), this);

		saveDefaultConfig();

		// create file permissions
		File permissions = new File(getDataFolder() + "/permissions.yml");
		FileConfiguration perm = YamlConfiguration.loadConfiguration(permissions);
		List<String> defperm = new ArrayList<String>();
		if (!permissions.exists()) {
			try {
				permissions.createNewFile();
				perm.createSection("Groups");
				perm.createSection("Users");
				perm.set("Groups.default.default", true);
				defperm.add("modifyworld.*");
				perm.set("Groups.default.permissions", defperm);
				perm.save(permissions);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (!PermissionsManager.thereIsDefault()) {
			System.out.println("FATAL ERROR: you haven't to have more than one default group permissions");
			Bukkit.getPluginManager().getPlugin("PermissionsPlus").getServer().getPluginManager()
					.disablePlugin(Bukkit.getPluginManager().getPlugin("PermissionsPlus"));
		}

		PermissionsManager.refreshPermissions();
	}

	public void onDisable() {
		for (Player players : Bukkit.getServer().getOnlinePlayers()) {
			PermissionsManager.hashperms.get(players.getUniqueId()).removeAttach();
			Util.deop(players);
		}
		PermissionsManager.hashperms.clear();
		System.out.println("PermissionsPlus disabled!");
	}

	public static Main getPlugin() {
		return Main.plugin;
	}

	public static boolean onlyOp() {
		return Boolean.parseBoolean(plugin.getConfig().getString("Security.onlyOp-usePerm"));
	}

	public static Plugin getMainMC() {
		return Bukkit.getPluginManager().getPlugin("MainMC");
	}

	public static void reloadPlugin() {
		if (Main.convert) {
			PexConverter pex = new PexConverter();
			pex.export();
			System.out.println("PermissionsEx integration complete!");
			Bukkit.getLogger().info("§cuninstall PermissionsEx!");
		}

		plugin.reloadConfig();
		plugin.getServer().getPluginManager().disablePlugin(plugin);
		plugin.getServer().getPluginManager().enablePlugin(plugin);

		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			EventManager em = new EventManager(p);
			PermissionsManager.hashperms.put(p.getUniqueId(), em);
		}
		PermissionsManager.refreshPermissions();
	}
}
