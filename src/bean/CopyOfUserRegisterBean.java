package bean;

import bean.request.ReqUserRegisterBean;

public class CopyOfUserRegisterBean {
	private String accessToken;
	private ReqUserRegisterBean user;
	
	public CopyOfUserRegisterBean(){}

	public CopyOfUserRegisterBean(String accessToken, ReqUserRegisterBean user) {
		this.accessToken = accessToken;
		this.user = user;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public ReqUserRegisterBean getUser() {
		return user;
	}

	public void setUser(ReqUserRegisterBean user) {
		this.user = user;
	}
	
	

}
