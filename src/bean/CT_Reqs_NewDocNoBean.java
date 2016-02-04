package bean;

public class CT_Reqs_NewDocNoBean {
	private String accessToken;
	private String carNumber;
	private String carBrand;
	
	
	public CT_Reqs_NewDocNoBean() {

	}


	public CT_Reqs_NewDocNoBean(String accessToken, String carNumber, String carBrand) {
		this.accessToken = accessToken;
		this.carNumber = carNumber;
		this.carBrand = carBrand;
	}

	public String getAccessToken() {
		return accessToken;
	}


	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}


	public String getCarNumber() {
		return carNumber;
	}


	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}


	public String getCarBrand() {
		return carBrand;
	}


	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}
	

}
