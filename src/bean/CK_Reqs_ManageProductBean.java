package bean;

public class CK_Reqs_ManageProductBean {
	private String accessToken;
	private String barCode;
	private double qtyAfter;
	private int qId;
	private int isCheck;
	private int isCancel;
	
	
	public CK_Reqs_ManageProductBean() {

		// TODO Auto-generated constructor stub
	}
	
	public CK_Reqs_ManageProductBean(String accessToken, String barCode,
			double qtyAfter, int qId, int isCheck, int isCancel) {

		this.accessToken = accessToken;
		this.barCode = barCode;
		this.qtyAfter = qtyAfter;
		this.qId = qId;
		this.isCheck = isCheck;
		this.isCancel = isCancel;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public double getQtyAfter() {
		return qtyAfter;
	}
	public void setQtyAfter(double qtyAfter) {
		this.qtyAfter = qtyAfter;
	}
	public int getqId() {
		return qId;
	}
	public void setqId(int qId) {
		this.qId = qId;
	}
	public int getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}
	public int getIsCancel() {
		return isCancel;
	}
	public void setIsCancel(int isCancel) {
		this.isCancel = isCancel;
	}
	
	
}
