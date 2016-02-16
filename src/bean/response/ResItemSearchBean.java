package bean.response;

public class ResItemSearchBean {
	String barcode;
	String itemCode;
	String itemName;
	String itemCategory;
	Double itemPrice;
	String unitCode;
	String remark;
	String shortCode;
	String filePath;

	/**
	 * @param barcode
	 * @param itemCode
	 * @param itemName
	 * @param itemCategory
	 * @param itemPrice
	 * @param unitCode
	 * @param remark
	 * @param shortCode
	 * @param filePath
	 */
	
	public ResItemSearchBean() {}

	public ResItemSearchBean(String barcode, String itemCode, String itemName,
			String itemCategory, Double itemPrice, String unitCode,
			String remark, String shortCode, String filePath) {
		super();
		this.barcode = barcode;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.itemCategory = itemCategory;
		this.itemPrice = itemPrice;
		this.unitCode = unitCode;
		this.remark = remark;
		this.shortCode = shortCode;
		this.filePath = filePath;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
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

	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	
}
