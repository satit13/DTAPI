package bean;

public class PK_Reqs_EditOrderBean {
	private String accessToken;
	private String carNumber;
	private String carBrand;
	private int qId;
	private int status;
	
	
	public PK_Reqs_EditOrderBean() {

	}


	public PK_Reqs_EditOrderBean(String accessToken, String carNumber, String carBrand,int qId,int status) {
		this.accessToken = accessToken;
		this.carNumber = carNumber;
		this.carBrand = carBrand;
		this.qId = qId;
		this.status = status;
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
	
	public int getqId() {
		return qId;
	}


	public void setqId(int qId) {
		this.qId = qId;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}

}
