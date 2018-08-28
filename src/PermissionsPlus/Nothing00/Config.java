package PermissionsPlus.Nothing00;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

	private FileConfiguration config;
	private File yml;
	
	public Config(FileConfiguration config, File yml) {
		this.config = config;
		this.yml = yml;
		
		if(this.config!=Main.getPlugin().getConfig()) {
			this.config=YamlConfiguration.loadConfiguration(this.yml);
		}
	}
	
	public Config(File yml) {
		this.yml = yml;
	    this.config=YamlConfiguration.loadConfiguration(this.yml);
	}
	
	public void set(String index, String value) {
		config.set(index, value);
	}
	
	public void set(String index, List<String> value) {
		config.set(index, value);
	}
	
	public FileConfiguration get() {
		return this.config;
	}
	
	public String getString(String index) {
		return this.config.getString(index);
	}
	
	public List<String> getStringList(String index) {
		return this.config.getStringList(index);
	}
	
	public Set<String> getConfiguration(String index) {
		return this.config.getConfigurationSection(index).getKeys(false);
	}
	
	public void save() {
		if(this.config==Main.getPlugin().getConfig()) {
			Main.getPlugin().saveConfig();
		}else {
			try {
				this.config.save(this.yml);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void reload() {
		if(this.config==Main.getPlugin().getConfig()) {
			Main.getPlugin().reloadConfig();
		}
	}
	
}