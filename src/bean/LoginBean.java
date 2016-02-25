package bean;

public class LoginBean {
	
	private String employeeCode;
	private int branchId;
	private String employeeName;
	
	public LoginBean() {}

	public LoginBean(String employeeCode, int branchId, String employeeName) {
		super();
		this.employeeCode = employeeCode;
		this.branchId = branchId;
		this.employeeName = employeeName;
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

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	
	

}
