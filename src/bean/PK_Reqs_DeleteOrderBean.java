package bean;

public class PK_Reqs_DeleteOrderBean {
	private String accessToken;
	private int qId;
	
	
	public PK_Reqs_DeleteOrderBean() {

	}
	
	public PK_Reqs_DeleteOrderBean(String accessToken, int qId) {

		this.accessToken = accessToken;
		this.qId = qId;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public int getqId() {
		return qId;
	}
	public void setqId(int qId) {
		this.qId = qId;
	}
	
	
}
