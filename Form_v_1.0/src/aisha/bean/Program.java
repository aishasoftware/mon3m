package aisha.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Program extends BasicBean{
	
	
	private String programName;
	private long investorId;
	private Date startTime;
	private Date endTime;
	private String description;
	private String appFile1;//application form
	private String appFile2;
	private String appFile3;
	private String appFile4;
	
	public static ArrayList<String> getPublicFields() {

		ArrayList<String> publicFields = new ArrayList<String>();
	
		publicFields.add(0, "field1");
		return publicFields;

	}
	
	public static ArrayList<String> getSearchFields() {

		ArrayList<String> searchFields = new ArrayList<String>();
		searchFields.add(0, "fromCreate");
		searchFields.add(1, "toCreate");
		searchFields.add(2, "programName");
		searchFields.add(3, "investorId");
		return searchFields;

	}
	
	public static ArrayList<String> getViewFields() {

		ArrayList<String> addFields = new ArrayList<String>();
		addFields.add(0, "programName");
		addFields.add(1, "description");
		addFields.add(2, "startTime");
		addFields.add(3, "endTime");
		addFields.add(4, "investorId");
		//addFields.add(5, "status");
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
	
		
	
/*	public Set<Program> getPrograms() {
		return programs;
	}


	public void setPrograms(Set<Program> programs) {
		this.programs = programs;
	}

*/
	public static ArrayList<String> getAddFields() {

		ArrayList<String> addFields = new ArrayList<String>();
		addFields.add(0, "programName");
		addFields.add(1, "description");
		addFields.add(2, "startTime");
		addFields.add(3, "endTime");

		
/*		addFields.add(6, "field7");
		addFields.add(7, "field8");
		addFields.add(8, "field9");
		addFields.add(9, "field10");
		addFields.add(10, "talents");*/
		return addFields;

	}
	
	
	public static ArrayList<String> getTableFields() {

		ArrayList<String> addFields = new ArrayList<String>();
		addFields.add(0, "programName");
		//addFields.add(1, "description");
		addFields.add(1, "investorId");
		return addFields;
	}
	public static Map<String,String> getLinks() {

	Map<String,String> links = new HashMap<String, String>();
	links.put("Manage Users","/Form_v_1.0/SystemUserController/getPlatformUserList");
	links.put("View My Startups","/Form_v_1.0/InvestorController/listStartups");
	links.put("View my Applications","/Form_v_1.0/InvestorController/listApplications");
	links.put("Add Complain","/Form_v_1.0/InvestorController/addComplain");
	links.put("Add Resource","/Form_v_1.0/AssetController/addResource");
	links.put("Add Program","/Form_v_1.0/InvestorController/addProgram");
	return links;
	}
	
	
	public String getProgramName() {
		return programName;
	}
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	public long getInvestorId() {
		return investorId;
	}
	public void setInvestorId(long investorId) {
		this.investorId = investorId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

	public String getAppFile1() {
		return appFile1;
	}
	public void setAppFile1(String appFile1) {
		this.appFile1 = appFile1;
	}
	public String getAppFile2() {
		return appFile2;
	}
	public void setAppFile2(String appFile2) {
		this.appFile2 = appFile2;
	}
	public String getAppFile3() {
		return appFile3;
	}
	public void setAppFile3(String appFile3) {
		this.appFile3 = appFile3;
	}
	public String getAppFile4() {
		return appFile4;
	}
	public void setAppFile4(String appFile4) {
		this.appFile4 = appFile4;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((appFile1 == null) ? 0 : appFile1.hashCode());
		result = prime * result
				+ ((appFile2 == null) ? 0 : appFile2.hashCode());
		result = prime * result
				+ ((appFile3 == null) ? 0 : appFile3.hashCode());
		result = prime * result
				+ ((appFile4 == null) ? 0 : appFile4.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + (int) (investorId ^ (investorId >>> 32));
		result = prime * result
				+ ((programName == null) ? 0 : programName.hashCode());
		result = prime * result
				+ ((startTime == null) ? 0 : startTime.hashCode());
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
		Program other = (Program) obj;
		if (appFile1 == null) {
			if (other.appFile1 != null)
				return false;
		} else if (!appFile1.equals(other.appFile1))
			return false;
		if (appFile2 == null) {
			if (other.appFile2 != null)
				return false;
		} else if (!appFile2.equals(other.appFile2))
			return false;
		if (appFile3 == null) {
			if (other.appFile3 != null)
				return false;
		} else if (!appFile3.equals(other.appFile3))
			return false;
		if (appFile4 == null) {
			if (other.appFile4 != null)
				return false;
		} else if (!appFile4.equals(other.appFile4))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (investorId != other.investorId)
			return false;
		if (programName == null) {
			if (other.programName != null)
				return false;
		} else if (!programName.equals(other.programName))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Program [programName=" + programName + ", investorId="
				+ investorId + ", startTime=" + startTime + ", endTime="
				+ endTime + ", description=" + description + ", appFile1="
				+ appFile1 + ", appFile2=" + appFile2 + ", appFile3="
				+ appFile3 + ", appFile4=" + appFile4 + "]";
	}
	
}
