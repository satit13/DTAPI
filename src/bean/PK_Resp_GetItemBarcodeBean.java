package bean;

public class PK_Resp_GetItemBarcodeBean {
	private String code;
	private int rate1;
	private String unitCode;
	private String itemName;
	
	public PK_Resp_GetItemBarcodeBean() {

	}
	
	public PK_Resp_GetItemBarcodeBean(String code, int rate1,String unitCode,String itemName) {

		this.code = code;
		this.rate1 = rate1;
		this.unitCode = unitCode;
		this.itemName = itemName;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getRate1() {
		return rate1;
	}
	public void setRate1(int rate1) {
		this.rate1 = rate1;
	}
	
	public String getUnitCode(){
		return unitCode;
	}
	
	public void setUnitCode(String unitCode){
		this.unitCode = unitCode;
	}
	
	public String getItemName(){
		return itemName;
	}
	
	public void setItemName(String itemName){
		this.itemName= itemName;
	}
}
