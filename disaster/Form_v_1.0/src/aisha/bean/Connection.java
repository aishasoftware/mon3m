package aisha.bean;

import java.util.ArrayList;

public class Connection extends BasicBean {

	private int innerId;
	private int outerId;
	private String relationType;
	private String connectionName;
	
	public static ArrayList<String> getTableFields() {
		ArrayList<String> tableFields = new ArrayList<String>();
		tableFields.add(0, "relationType");
		tableFields.add(1, "accept");
		tableFields.add(2, "status");
		tableFields.add(3, "connectionName");
		return tableFields;
	}
	
	public static ArrayList<String> getPublicFields() {

		ArrayList<String> tableFields = new ArrayList<String>();
		tableFields.add(0, "innerId");
		tableFields.add(1, "outerId");
		tableFields.add(2, "status");
		return tableFields;

	}
	
	public static ArrayList<String> getPrivateFields() {

		ArrayList<String> tableFields = new ArrayList<String>();
		tableFields.add(0, "innerId");
		tableFields.add(1, "outerId");
		tableFields.add(2, "relationType");
		tableFields.add(3, "status");
		return tableFields;

	}
	
	public static ArrayList<String> getConnectionsFields() {

		ArrayList<String> tableFields = new ArrayList<String>();
		tableFields.add(0, "innerId");
		tableFields.add(1, "outerId");
		tableFields.add(2, "status");
		return tableFields;

	}
	
	public static ArrayList<String> getAdminFields() {

		ArrayList<String> tableFields = new ArrayList<String>();
		tableFields.add(0, "innerId");
		tableFields.add(1, "outerId");
		tableFields.add(2, "status");
		return tableFields;

	}
	
	public static ArrayList<String> getAddFields() {

		ArrayList<String> tableFields = new ArrayList<String>();
		tableFields.add(0, "innerId");
		tableFields.add(1, "outerId");
		tableFields.add(2, "status");
		return tableFields;

	}
	
	public static ArrayList<String> getViewFields() {

		ArrayList<String> tableFields = new ArrayList<String>();
		tableFields.add(0, "innerId");
		tableFields.add(1, "outerId");
		tableFields.add(2, "status");
		return tableFields;

	}
	
	public int getInnerId() {
		return innerId;
	}
	public void setInnerId(int innerId) {
		this.innerId = innerId;
	}
	public int getOuterId() {
		return outerId;
	}
	public void setOuterId(int outerId) {
		this.outerId = outerId;
	}
	public String getRelationType() {
		return relationType;
	}
	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	@Override
	public String toString() {
		return "Connection [innerId=" + innerId + ", outerId=" + outerId
				+ ", relationType=" + relationType + ", connectionName="
				+ connectionName + "]";
	}


}
