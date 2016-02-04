package view;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import controller.BillingController;
import controller.PickupController;

import bean.IV_Resp_SearchBankBean;
import bean.PK_Reqs_ManageItemBean;
import bean.PK_Reqs_DeleteOrderBean;
import bean.PK_Reqs_EditOrderBean;
import bean.CT_Reqs_NewDocNoBean;
import bean.PK_Resp_EditOrderBean;
import bean.PK_Resp_ManageItemBean;
import bean.PK_Resp_NewOrderHeaderBean;
import bean.PK_Resp_OrderPendingBean;
import bean.PK_Resp_SearchCarBrandBean;
import bean.SearchBean;
import bean.UserSearchBean;
import bean.orderPendingBean;

@Path(value="/pickup")
public class Pickup {

	@POST
	@Path("/search")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public orderPendingBean searchOrderPending(SearchBean keyword){
		orderPendingBean orderList = new orderPendingBean();
		PickupController pcl = new PickupController();
		
		orderList = pcl.searchOrderPending("SmartQ", keyword);
		
		return orderList;
	}
	
	@POST
	@Path("/new")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public PK_Resp_NewOrderHeaderBean newOrder(CT_Reqs_NewDocNoBean reqNew){
		PK_Resp_NewOrderHeaderBean newOrder = new PK_Resp_NewOrderHeaderBean();
		PickupController pcl = new PickupController();
		
		newOrder = pcl.newOrder("SmartQ", reqNew);
		
		return newOrder;
	}
	
	@POST
	@Path("/edit")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public PK_Resp_EditOrderBean editOrder(PK_Reqs_EditOrderBean reqEdit){
		PK_Resp_EditOrderBean editQue = new PK_Resp_EditOrderBean();
		PickupController pcl = new PickupController();
		
		editQue = pcl.editOrder("SmartQ", reqEdit);
		
		return editQue;
	}
	
	
	@POST
	@Path("/delete")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public PK_Resp_EditOrderBean deleteOrder(PK_Reqs_DeleteOrderBean reqDelete){
		PK_Resp_EditOrderBean delQue = new PK_Resp_EditOrderBean();
		PickupController pcl = new PickupController();
		
		delQue = pcl.deleteOrder("SmartQ", reqDelete);
		
		return delQue;
	}
	
	@POST
	@Path("/manage_product")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public PK_Resp_ManageItemBean manageItemOrder(PK_Reqs_ManageItemBean reqManageItem){
		PK_Resp_ManageItemBean resManageItem = new PK_Resp_ManageItemBean();
		PickupController pcl = new PickupController();
		
		resManageItem = pcl.insertItemPickup("SmartQ", reqManageItem);
		
		return resManageItem;
	}
	
	
	@POST
	@Path("/carbrand")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public PK_Resp_SearchCarBrandBean searchCarBrand(UserSearchBean search){
		PickupController bcl = new PickupController();
		PK_Resp_SearchCarBrandBean response = new PK_Resp_SearchCarBrandBean();
		
		response = bcl.searchCarBrand("SmartQ",search);
		
		return response;
	}
}
