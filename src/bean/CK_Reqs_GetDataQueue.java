package bean;

public class CK_Reqs_GetDataQueue {
	private String accessToken;
	private int qId;
	
	
	public CK_Reqs_GetDataQueue() {

	}


	public CK_Reqs_GetDataQueue(String accessToken, int qId) {
		
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
