package bean;

public class LoginBean {
	
	private String employeeCode;
	private int branchId;
	
	public LoginBean() {}

	public LoginBean(String employeeCode, int branchId) {
		this.employeeCode = employeeCode;
		this.branchId = branchId;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}
	
	
	
	

}
