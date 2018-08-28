package PermissionsPlus.Nothing00;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import PermissionsPlus.manager.GroupPlus;
import PermissionsPlus.manager.PermissionsManager;
import PermissionsPlus.manager.UserPlus;

public class Util {

	public static void addTime(String perm, String time, Player p) {
		int t=0;
		if(time.contains("s")) {
			if(!NumberUtils.isNumber(time.replaceAll("s", ""))) return;
			t=Integer.parseInt(time.replaceAll("s", ""));
		}else if(time.contains("m")){
			if(!NumberUtils.isNumber(time.replaceAll("m", ""))) return;
			t=Integer.parseInt(time.replaceAll("m", ""))*60;
		}else if(time.contains("h")) {
			if(!NumberUtils.isNumber(time.replaceAll("h", ""))) return;
			t=Integer.parseInt(time.replaceAll("h", ""))*60*60;
		}
		if(t<=0) return;
		PermissionsManager.hashperms.get(p.getUniqueId()).addUserPerm(perm);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable(){
		      @Override
		       public void run(){
		    	  PermissionsManager.hashperms.get(p.getUniqueId()).removeUserPerm(perm);
		       }
	     }, t*20);
	}
	
	public static void addTimeGroup(GroupPlus group, String time, String p) {
		int t=0;
		if(time.contains("s")) {
			if(!NumberUtils.isNumber(time.replaceAll("s", ""))) return;
			t=Integer.parseInt(time.replaceAll("s", ""));
		}else if(time.contains("m")){
			if(!NumberUtils.isNumber(time.replaceAll("m", ""))) return;
			t=Integer.parseInt(time.replaceAll("m", ""))*60;
		}else if(time.contains("h")) {
			if(!NumberUtils.isNumber(time.replaceAll("h", ""))) return;
			t=Integer.parseInt(time.replaceAll("h", ""))*60*60;
		}
		if(t<=0) return;
		UserPlus user = new UserPlus(p);
		user.setGroup(group);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable(){
		      @Override
		       public void run(){
		    	  user.removeGroup(group);
		       }
	     }, t*20);
	}
	
	
private static File permissions = new File(Main.getPlugin().getDataFolder()+"/permissions.yml");
	
	
	//setUUID
	public static void setUUID(String p){
		FileConfiguration perm = YamlConfiguration.loadConfiguration(permissions);
		if(perm.getString("Users."+p)!=null && perm.getString("Users."+p+".uuid")==null){
		Player player = Bukkit.getServer().getPlayer(p);
		if(player!=null){
			perm.set("Users."+p+".uuid", player.getUniqueId().toString());
			try {
				perm.save(permissions);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
	}
	
    //UUID protection
	public static boolean protecteduuid(Player p){
	  if(Boolean.parseBoolean(Main.getPlugin().getConfig().getString("Security.UUID-protection"))){
		FileConfiguration perm = YamlConfiguration.loadConfiguration(permissions);
		Set<String> userlist = perm.getConfigurationSection("Users").getKeys(false);
		//create list of UUID
		List<String> uuidlist = new ArrayList<String>();
		String[] users = userlist.toArray(new String[0]);
		for(int i=0; i<users.length; i++){
			if(perm.getString("Users."+p.getName()+".uuid")!=null){
				uuidlist.add(perm.getString("Users."+p.getName()+".uuid"));
			}
		}
		
		//check
		//controllo se l'uuid corrisponde all'utente
		boolean uuid_diverso=false;
		if(userlist.contains(p.getName())){
			if(perm.getString("Users."+p.getName()+".uuid")!=null){
				if(p.getUniqueId().toString().equals(perm.getString("Users."+p.getName()+".uuid"))){
					uuid_diverso=false;
				}else{
					uuid_diverso=true;
				}
			}
		}
		
		//controllo se l'uuid corrisponde ma il nome è diverso
		String nome=null;
		boolean nome_diverso=false;
		if(uuidlist.contains(p.getUniqueId().toString())){
			for(int i=0; i<users.length; i++){
				if(perm.getString("Users."+users[i]+".uuid")!=null && perm.getString("Users."+users[i]+".uuid").equals(p.getUniqueId().toString())){
					nome=users[i];
				}
			}
			if(nome!=null && !p.getName().equals(nome) ){
				nome_diverso=true;
			}else{
				nome_diverso=false;
			}
		}
		
		//result
		if(nome_diverso && uuid_diverso){
			return true;
		}else if(nome_diverso || uuid_diverso){
			return true;
		}else if(!nome_diverso && !uuid_diverso){
			return false;
		}else{
			return false;
		}
	  }else{
		  return false;
	  }
	}
	
	public static void deop(Player p){
		if(Boolean.parseBoolean(Main.getPlugin().getConfig().getString("Security.deop-on-quit"))){
			if(p.isOp()){
				p.setOp(false);
			}
		}
	}
	
	public static void removePSW(String g, String u, FileConfiguration perm) {
		if(perm.getStringList("Groups."+g+".permissions").contains("perm.lockable") || perm.getStringList("Groups."+g+".permissions").contains("*") || perm.getStringList("Groups."+g+".permissions").contains("'*'")) {
			List<String> gs = perm.getStringList("Users."+u+".group");
			
			int i=0;
			if(gs.size()>0) {
			for(String gg : gs) {
			 List<String> gsp = perm.getStringList(("Groups."+gg+".permissions"));
			 if(!gsp.contains("perm.lockable") && !gsp.contains("*") && !gsp.contains("'*'")) {
				 i++;
			 }
			}
			}
			if(i==0 || i==gs.size()) {
			perm.set("Users."+u+".password", null);
			}
		}
	}
	
	public static World findWorld(String string){
    	Iterator<World> worlds = Main.getPlugin().getServer().getWorlds().iterator();
    	while(worlds.hasNext()){
    	World world = worlds.next();
    	if(world.getName().equalsIgnoreCase(string)){
    	return world;
    	}
    	}
    	return null;
    }
	
}
