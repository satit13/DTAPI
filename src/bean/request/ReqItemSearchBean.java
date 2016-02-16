package bean.request;

public class ReqItemSearchBean {
	private String accessToken;
	private String keyword;
	private int type;
	
	
	
	public ReqItemSearchBean(){}


	public ReqItemSearchBean(String accessToken, String keyword, int type ) {
		super();
		this.accessToken = accessToken;
		this.keyword = keyword;
		this.type = type;
	}

	public ReqItemSearchBean(String accessToken, String keyword ) {
		super();
		this.accessToken = accessToken;
		this.keyword = keyword;
		this.type = 0;
	}
	
	

	public String getAccessToken() {
		return accessToken;
	}


	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}


	public String getKeyword() {
		return keyword;
	}


	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}
	
	
		
}
