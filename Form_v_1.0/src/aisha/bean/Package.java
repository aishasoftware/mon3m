package aisha.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Package extends BasicBean {

	private String packageName;
	private Set<Resource> resources = new HashSet<Resource>();
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Set<Resource> getResources() {
		return resources;
	}
	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}
	
	
	public static ArrayList<String> getPublicFields() {

		ArrayList<String> publicFields = new ArrayList<String>();
	
		publicFields.add(0, "field1");
		return publicFields;

	}
	
	public static ArrayList<String> getViewFields() {

		ArrayList<String> addFields = new ArrayList<String>();
		addFields.add(0, "field1");
		addFields.add(1, "field2");
		addFields.add(2, "field3");
		addFields.add(3, "field4");
		addFields.add(4, "field5");
		addFields.add(5, "field6");
		addFields.add(6, "field7");
		addFields.add(7, "field8");
		addFields.add(8, "field9");
		addFields.add(9, "field10");
		addFields.add(10, "talents");
		addFields.add(11, "investors");
		return addFields;

	}

	
	public static ArrayList<String> getAdminFields() {

		ArrayList<String> adminFields = new ArrayList<String>();
	
		adminFields.add(0, "status");
		return adminFields;

	}
	public static ArrayList<String> getPrivateFields() {

		ArrayList<String> privateFields = new ArrayList<String>();
	
		privateFields.add(0, "field2");
		return privateFields;

	}
	
	public static ArrayList<String> getConnectionsFields() {

		ArrayList<String> privateFields = new ArrayList<String>();
	
		privateFields.add(0, "field3");
		return privateFields;

	}
	
	public static Map<String,String> getLinks() {

		Map<String,String> links = new HashMap<String, String>();
		links.put("My Profile","/Form_v_1.0/StartupController/viewMyProfile");
		//links.put("My Applications","/Form_v_1.0/StartupController/getApplicationsList");
		//links.put("My Co-founders","/Form_v_1.0/StartupController/getTalentsList");
		return links;
		}
		
	public static ArrayList<String> getSearchFields() {

		ArrayList<String> searchFields = new ArrayList<String>();
		searchFields.add(0, "fromCreate");
		searchFields.add(1, "toCreate");
		searchFields.add(2, "field1");
		searchFields.add(3, "field2");
		return searchFields;

	}
	
	
	public static ArrayList<String> getTableFields() {

		ArrayList<String> tableFields = new ArrayList<String>();
		tableFields.add(0, "field1");
		tableFields.add(1, "field2");
		tableFields.add(2, "connect");
		tableFields.add(3, "creationTime");
		return tableFields;

	}
	
	public static ArrayList<String> getAddFields() {
		ArrayList<String> addFields = new ArrayList<String>();
		addFields.add(0, "packageName");
		addFields.add(1, "resources");
		return addFields;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((packageName == null) ? 0 : packageName.hashCode());
		result = prime * result
				+ ((resources == null) ? 0 : resources.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Package other = (Package) obj;
		if (packageName == null) {
			if (other.packageName != null)
				return false;
		} else if (!packageName.equals(other.packageName))
			return false;
		if (resources == null) {
			if (other.resources != null)
				return false;
		} else if (!resources.equals(other.resources))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Package [packageName=" + packageName + ", resources="
				+ resources + "]";
	}
	
}
