package controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import bean.IV_Reqs_BillingBean;
import bean.IV_Reqs_CouponBean;
import bean.IV_Reqs_CreditCardBean;
import bean.IV_Reqs_PrintSlipBean;
import bean.IV_Resp_BillingBean;
import bean.IV_Resp_PrintSlipBean;
import bean.IV_Resp_SearchCreditTypeBean;
import bean.UserSearchBean;

public class BillingControllerTest {

	@Test
	public void test() {
		BillingController bc = new BillingController();
		IV_Reqs_BillingBean reqsBill = new IV_Reqs_BillingBean();
		IV_Resp_BillingBean respBill = new IV_Resp_BillingBean();
		
		List<IV_Reqs_CreditCardBean> crdCard = new ArrayList<IV_Reqs_CreditCardBean>();
		List<IV_Reqs_CouponBean> coupon = new ArrayList<IV_Reqs_CouponBean>();
		
		IV_Resp_PrintSlipBean slip = new IV_Resp_PrintSlipBean();
		
		IV_Reqs_PrintSlipBean print = new IV_Reqs_PrintSlipBean();
		
		
		IV_Reqs_CreditCardBean evt;
		
		IV_Reqs_CouponBean evt1;
		
		IV_Reqs_PrintSlipBean evt2;
		
		evt2 = new IV_Reqs_PrintSlipBean();
		evt2.setType(0);
		evt2.setInvoiceNo("13581119-2339");
		evt2.setArCode("");
		
		
//		evt = new IV_Reqs_CreditCardBean();
//		evt.setCardNo("1234");
//		evt.setAmount(500);
//		evt.setBankCode("BLL");
//		evt.setChargeAmount(0);
//		evt.setConfirmNo("3523432");
//		evt.setCreditType("CreditCard");
//		
//		crdCard.add(evt);
//		
//		evt1 = new IV_Reqs_CouponBean();
//		evt1.setCouponCode("WD5303-0001-1");
//		evt1.setAmount(100);
//		
//		coupon.add(evt1);
//		
//		reqsBill.setCreditCard(crdCard);
//		reqsBill.setCouponCode(coupon);
//		reqsBill.setAccessToken("");
//		reqsBill.setArCode("99999");
//		reqsBill.setqId(7);
//		reqsBill.setCash(0);
//		reqsBill.setConfirm(1);
//		
//		respBill = bc.PostBilling("POS", reqsBill);
//		
//		System.out.println("POSNOxxxxx : "+respBill.getResponse().getProcessDesc());
		
		
//		UserSearchBean search = new UserSearchBean();
//		IV_Resp_SearchCreditTypeBean credittype = new IV_Resp_SearchCreditTypeBean();
		
		
		slip = bc.printSlip("POS", evt2);
		
		System.out.println("Bill DocDate : "+slip.getDocDate());
	}

}
