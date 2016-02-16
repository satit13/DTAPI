package bean;

public class PK_Resp_GetDataQueue {
	private String docNo;
	private int isCancel;
	private int status;
	private String carLicense;
	private String saleCode;
	

	public PK_Resp_GetDataQueue() {

	}


	public PK_Resp_GetDataQueue(String docNo, int isCancel, int status, String carLicense, String saleCode) {
		super();
		this.docNo = docNo;
		this.isCancel = isCancel;
		this.status = status;
		this.carLicense = carLicense;
		this.saleCode = saleCode;
	}


	public String getDocNo() {
		return docNo;
	}


	public void setDocNo(String docNo) {
		this.docNo = docNo;
	}


	public int getIsCancel() {
		return isCancel;
	}


	public void setIsCancel(int isCancel) {
		this.isCancel = isCancel;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}
	
	public String getCarLicense(){
		return carLicense;
	}

	public void setCarLicense(String carLicense){
		this.carLicense = carLicense;
	}
	
	public void setSaleCode(String saleCode){
		this.saleCode = saleCode;
	}
	
	public String getSaleCode(){
		return saleCode;
	}
}
