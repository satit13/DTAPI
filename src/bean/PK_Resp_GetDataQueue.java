package bean;

public class PK_Resp_GetDataQueue {
	private String docNo;
	private int isCancel;
	private int status;
	

	public PK_Resp_GetDataQueue() {

	}


	public PK_Resp_GetDataQueue(String docNo, int isCancel, int status) {
		super();
		this.docNo = docNo;
		this.isCancel = isCancel;
		this.status = status;
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


}
