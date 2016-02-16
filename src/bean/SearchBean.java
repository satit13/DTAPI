package bean;

public class SearchBean {
	private String accessToken;
	private String keyword;
	
	
	public SearchBean() {

	}
	public SearchBean(String accessToken, String keyword) {
		this.accessToken = accessToken;
		this.keyword = keyword;
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

	
}
