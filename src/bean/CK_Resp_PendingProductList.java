package bean;

public class CK_Resp_PendingProductList {
	private String barCode;
	private double totalPrice;
	private String itemCode;
	private String filePath; 
	private String itemName; 
	private String itemCategory; 
	private double qtyBefore;
	private double qtyAfter;
	private double totalPriceBefore; 
	private double totalPriceAfter; 
	private String unitCode; 
	private String pickupStaffName;
	private int isCheck;
	private int isCancel;
	
	
	public CK_Resp_PendingProductList() {

	}


	public CK_Resp_PendingProductList(String barCode, double totalPrice,
			String itemCode, String filePath, String itemName,
			String itemCategory, double qtyBefore, double qtyAfter,
			double totalPriceBefore, double totalPriceAfter, String unitCode,
			String pickupStaffName, int isCheck, int isCancel) {

		this.barCode = barCode;
		this.totalPrice = totalPrice;
		this.itemCode = itemCode;
		this.filePath = filePath;
		this.itemName = itemName;
		this.itemCategory = itemCategory;
		this.qtyBefore = qtyBefore;
		this.qtyAfter = qtyAfter;
		this.totalPriceBefore = totalPriceBefore;
		this.totalPriceAfter = totalPriceAfter;
		this.unitCode = unitCode;
		this.pickupStaffName = pickupStaffName;
		this.isCheck = isCheck;
		this.isCancel = isCancel;
	}


	public String getBarCode() {
		return barCode;
	}


	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}


	public double getTotalPrice() {
		return totalPrice;
	}


	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}


	public String getItemCode() {
		return itemCode;
	}


	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}


	public String getFilePath() {
		return filePath;
	}


	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}


	public String getItemName() {
		return itemName;
	}


	public void setItemName(String itemName) {
		this.itemName = itemName;
	}


	public String getItemCategory() {
		return itemCategory;
	}


	public void setItemCategory(String itemCategory) {
		this.itemCategory = itemCategory;
	}


	public double getQtyBefore() {
		return qtyBefore;
	}


	public void setQtyBefore(double qtyBefore) {
		this.qtyBefore = qtyBefore;
	}


	public double getQtyAfter() {
		return qtyAfter;
	}


	public void setQtyAfter(double qtyAfter) {
		this.qtyAfter = qtyAfter;
	}


	public double getTotalPriceBefore() {
		return totalPriceBefore;
	}


	public void setTotalPriceBefore(double totalPriceBefore) {
		this.totalPriceBefore = totalPriceBefore;
	}


	public double getTotalPriceAfter() {
		return totalPriceAfter;
	}


	public void setTotalPriceAfter(double totalPriceAfter) {
		this.totalPriceAfter = totalPriceAfter;
	}


	public String getUnitCode() {
		return unitCode;
	}


	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}


	public String getPickupStaffName() {
		return pickupStaffName;
	}


	public void setPickupStaffName(String pickupStaffName) {
		this.pickupStaffName = pickupStaffName;
	}


	public int getIsCheck() {
		return isCheck;
	}


	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}


	public int getIsCancel() {
		return isCancel;
	}


	public void setIsCancel(int isCancel) {
		this.isCancel = isCancel;
	}
	
	
	
}
