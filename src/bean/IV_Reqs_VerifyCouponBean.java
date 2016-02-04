package bean;

public class IV_Reqs_VerifyCouponBean {
	private String accessToken;
	private String couponCode;
	
	
	public IV_Reqs_VerifyCouponBean() {
		super();
		// TODO Auto-generated constructor stub
	}


	public IV_Reqs_VerifyCouponBean(String accessToken, String couponCode) {
		super();
		this.accessToken = accessToken;
		this.couponCode = couponCode;
	}


	public String getAccessToken() {
		return accessToken;
	}


	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}


	public String getCouponCode() {
		return couponCode;
	}


	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	
	
}
