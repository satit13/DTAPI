package bean.request;

public class ReqItemEditBean {
	private String accessToken ; 
	private String barCode;
	private String shortCode;
	private String remark ; 
	private String filePath;
	
	
	
	public ReqItemEditBean(String accessToken, String barCode,
			String shortCode, String remark, String filePath) {
		super();
		this.accessToken = accessToken;
		this.barCode = barCode;
		this.shortCode = shortCode;
		this.remark = remark;
		this.filePath = filePath;
	}
	
	public ReqItemEditBean() {}

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

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	
}
