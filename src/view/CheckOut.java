package view;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import controller.CheckOutController;

import bean.CK_Reqs_GetDataQueue;
import bean.CK_Reqs_ManageProductBean;
import bean.CK_Resp_ManageItemBean;
import bean.CK_Resp_PendingProductListBean;
import bean.CT_Resp_ResponseBean;
import bean.PK_Resp_ManageItemBean;

@Path(value="/pending")
public class CheckOut {
	
	@POST
	@Path("/productlist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public CK_Resp_PendingProductListBean searchPendingProductList(CK_Reqs_GetDataQueue reqQueue){
		CheckOutController ccl = new CheckOutController();
		CK_Resp_PendingProductListBean queueList = new CK_Resp_PendingProductListBean();
		
		queueList = ccl.searchDataQueue("SmartQ", reqQueue);
		
		return queueList;
	}
	
	@POST
	@Path("/manage_product")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CK_Resp_ManageItemBean manageProduct(CK_Reqs_ManageProductBean reqQueue){
		CheckOutController ccl = new CheckOutController();
		CK_Resp_ManageItemBean respQueue = new CK_Resp_ManageItemBean();
		
		respQueue = ccl.ManageProductCheckOut("SmartQ", reqQueue);
		
		return respQueue;
	}

}
