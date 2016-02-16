package bean;

public class IV_Reqs_InvoiceDataBean {
	private String accessToken;
	private String invoiceNo;
	
	
	public IV_Reqs_InvoiceDataBean() {
		super();
		// TODO Auto-generated constructor stub
	}


	public IV_Reqs_InvoiceDataBean(String accessToken, String invoiceNo) {
		super();
		this.accessToken = accessToken;
		this.invoiceNo = invoiceNo;
	}


	public String getAccessToken() {
		return accessToken;
	}


	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}


	public String getInvoiceNo() {
		return invoiceNo;
	}


	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	
	
}
