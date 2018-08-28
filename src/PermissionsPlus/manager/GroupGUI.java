package PermissionsPlus.manager;

public class GroupGUI extends GroupPlus{

	private String perm;
	
	public GroupGUI(String group, String perm) {
		super(group);
		this.perm = perm;
	}		
	
	public void addPermission() {
			if(ListPlus.isList(this.perm)) {
				ListPlus list = new ListPlus(this.perm.substring(5, this.perm.length()));
				super.addPermission(list.getPermissions());
			}else {
				super.addPermission(this.perm);
			}
	}
	
	public void removePermission() {
			if(ListPlus.isList(this.perm)) {
				ListPlus list = new ListPlus(this.perm.substring(5, this.perm.length()));
				super.removePermission(list.getPermissions());
			}else {
				super.removePermission(this.perm);
			}
	}
	
	

}
