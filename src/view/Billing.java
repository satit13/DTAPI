package view;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import controller.BillingController;
import controller.getDataFromData;

import bean.CT_Resp_ResponseBean;
import bean.IV_Reqs_BillingBean;
import bean.IV_Reqs_CouponBean;
import bean.IV_Reqs_InvoiceDataBean;
import bean.IV_Reqs_PrintSlipBean;
import bean.IV_Reqs_VerifyCouponBean;
import bean.IV_Resp_BillingBean;
import bean.IV_Resp_InvoiceDataBean;
import bean.IV_Resp_PrintSlipBean;
import bean.IV_Resp_SearchBankBean;
import bean.IV_Resp_SearchCreditTypeBean;
import bean.IV_Resp_VerifyCouponBean;
import bean.UserSearchBean;

@Path(value="/billing")
public class Billing {

	@POST
	@Path("/credittype")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IV_Resp_SearchCreditTypeBean searchCreditType(UserSearchBean search){
		BillingController bcl = new BillingController();
		IV_Resp_SearchCreditTypeBean response = new IV_Resp_SearchCreditTypeBean();
		
		response = bcl.searchCreditType("POS",search);
		
		return response;
	}

	
	@POST
	@Path("/bank")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IV_Resp_SearchBankBean searchBank(UserSearchBean search){
		BillingController bcl = new BillingController();
		IV_Resp_SearchBankBean response = new IV_Resp_SearchBankBean();
		
		response = bcl.searchBank("POS",search);
		
		return response;
	}
	
	@POST
	@Path("/done")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IV_Resp_BillingBean saveBilling(IV_Reqs_BillingBean reqsBill){
		IV_Resp_BillingBean billing = new IV_Resp_BillingBean();
		BillingController bcl = new BillingController();
		
		billing = bcl.PostBilling("POS", reqsBill);
		
		return billing;
	}
	
	
	@POST
	@Path("/verifycoupon")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IV_Resp_VerifyCouponBean verifyCoupon(IV_Reqs_VerifyCouponBean coupong){
		IV_Resp_VerifyCouponBean verify = new IV_Resp_VerifyCouponBean();
		BillingController bcl = new BillingController();
		
		verify = bcl.verifyCoupong("POS", coupong);
		
		return verify;
	}
	
	@POST
	@Path("/printslip")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IV_Resp_PrintSlipBean printSlip(IV_Reqs_PrintSlipBean req){
		IV_Resp_PrintSlipBean printslip = new IV_Resp_PrintSlipBean();
		BillingController bcl = new BillingController();
		
		printslip = bcl.printSlip("POS", req);
		
		return printslip;
	}
	
	@POST
	@Path("/invoice")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public IV_Resp_InvoiceDataBean invoiceData(IV_Reqs_InvoiceDataBean reqs){
		IV_Resp_InvoiceDataBean invoice = new IV_Resp_InvoiceDataBean();
		BillingController bcl = new BillingController();
		
		invoice = bcl.InvoiceDetails("192.168.0.7","bcnp", reqs);
		
		return invoice;
	}
}
