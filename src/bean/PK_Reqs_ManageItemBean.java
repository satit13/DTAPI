package bean;

public class PK_Reqs_ManageItemBean {
	private String accessToken;
	private String barCode;
	private int qtyBefore;
	private int qId;
	private int isCancel;
	
	
	public PK_Reqs_ManageItemBean() {

	}
	
	
	public PK_Reqs_ManageItemBean(String accessToken, String barCode,
			int qtyBefore, int qId, int isCancel) {

		this.accessToken = accessToken;
		this.barCode = barCode;
		this.qtyBefore = qtyBefore;
		this.qId = qId;
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
	public int getQtyBefore() {
		return qtyBefore;
	}
	public void setQtyBefore(int qtyBefore) {
		this.qtyBefore = qtyBefore;
	}
	public int getqId() {
		return qId;
	}
	public void setqId(int qId) {
		this.qId = qId;
	}
	public int getIsCancel() {
		return isCancel;
	}
	public void setIsCancel(int isCancel) {
		this.isCancel = isCancel;
	}
	
	
}
