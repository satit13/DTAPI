package bean;

import java.util.List;

public class IV_Reqs_BillingBean {
    private String accessToken;
    private String arCode;
    private int confirm;
    private int qId;
    private double cash;
    private List<IV_Reqs_CreditCardBean> creditCard;
    private List<IV_Reqs_CouponBean> couponCode;
    private double debtAmount;
    
    
	public IV_Reqs_BillingBean() {
		super();
		// TODO Auto-generated constructor stub
	}


	public IV_Reqs_BillingBean(String accessToken, String arCode, int confirm,
			int qId, double cash, List<IV_Reqs_CreditCardBean> creditCard,
			List<IV_Reqs_CouponBean> couponCode, double debtAmount) {
		super();
		this.accessToken = accessToken;
		this.arCode = arCode;
		this.confirm = confirm;
		this.qId = qId;
		this.cash = cash;
		this.creditCard = creditCard;
		this.couponCode = couponCode;
		this.debtAmount = debtAmount;
	}


	public String getAccessToken() {
		return accessToken;
	}


	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}


	public String getArCode() {
		return arCode;
	}


	public void setArCode(String arCode) {
		this.arCode = arCode;
	}


	public int getConfirm() {
		return confirm;
	}


	public void setConfirm(int confirm) {
		this.confirm = confirm;
	}


	public int getqId() {
		return qId;
	}


	public void setqId(int qId) {
		this.qId = qId;
	}


	public double getCash() {
		return cash;
	}


	public void setCash(double cash) {
		this.cash = cash;
	}


	public List<IV_Reqs_CreditCardBean> getCreditCard() {
		return creditCard;
	}


	public void setCreditCard(List<IV_Reqs_CreditCardBean> creditCard) {
		this.creditCard = creditCard;
	}


	public List<IV_Reqs_CouponBean> getCouponCode() {
		return couponCode;
	}


	public void setCouponCode(List<IV_Reqs_CouponBean> couponCode) {
		this.couponCode = couponCode;
	}


	public double getDebtAmount() {
		return debtAmount;
	}


	public void setDebtAmount(double debtAmount) {
		this.debtAmount = debtAmount;
	}

}
