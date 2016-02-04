package bean;

public class IV_Reqs_PrintSlipBean {
	private String accessToken;
	private int type;
	private String invoiceNo;
	private String arCode;
	
	public IV_Reqs_PrintSlipBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public IV_Reqs_PrintSlipBean(String accessToken, int type,
			String invoiceNo, String arCode) {
		super();
		this.accessToken = accessToken;
		this.type = type;
		this.invoiceNo = invoiceNo;
		this.arCode = arCode;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getArCode() {
		return arCode;
	}

	public void setArCode(String arCode) {
		this.arCode = arCode;
	}

	
	
	
}
