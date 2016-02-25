package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import connect.QueueConnect;
import bean.CT_Resp_ResponseBean;
import bean.RP_Reqs_SearchQueBetweenDateBean;
import bean.RP_Resp_CountQueue;
import bean.RP_Resp_CountQueueBean;
import bean.RP_Resp_QueueDetails;
import bean.RP_Resp_QueueDetailsBean;
import bean.RP_Resp_QueueItem;
import bean.RP_Resp_QueueMaster;
import bean.RP_Resp_QueueMasterBean;
import bean.RP_Resp_SumCheckOutData;
import bean.RP_Resp_SumCheckOutDataBean;

public class ReportDriveThruController {

	QueueConnect ds = connect.QueueConnect.INSTANCE;
	CT_Resp_ResponseBean response = new CT_Resp_ResponseBean();
	
	List<RP_Resp_SumCheckOutData> listcheckout = new ArrayList<RP_Resp_SumCheckOutData>();
	RP_Resp_SumCheckOutDataBean checkout = new RP_Resp_SumCheckOutDataBean();
	
	List<RP_Resp_CountQueue> listdata = new ArrayList<RP_Resp_CountQueue>();
	RP_Resp_CountQueueBean queue = new RP_Resp_CountQueueBean();
	
	List<RP_Resp_QueueMaster> listqueue = new ArrayList<RP_Resp_QueueMaster>();
	RP_Resp_QueueMasterBean qmaster = new RP_Resp_QueueMasterBean();

	List<RP_Resp_QueueItem> listitem = new ArrayList<RP_Resp_QueueItem>();
	List<RP_Resp_QueueDetails> listqdetails = new ArrayList<RP_Resp_QueueDetails>();
	RP_Resp_QueueDetailsBean qdetails = new RP_Resp_QueueDetailsBean();

	
	String vQuery;
	String vQuerySub;
	
	
	public RP_Resp_CountQueueBean CountQueue(String dbName,RP_Reqs_SearchQueBetweenDateBean search){
		
		try {
			Statement stmt = ds.getStatement(dbName);
			
			vQuery = "call USP_DT_CountQuantityData ('"+search.getBeginDate()+"','"+search.getEndDate()+"')";
			
			System.out.println(vQuery);
			ResultSet rs = stmt.executeQuery(vQuery);
			
			RP_Resp_CountQueue evt;
			
			listdata.clear();
			while(rs.next()){
				evt = new RP_Resp_CountQueue();
				evt.setDocYear(rs.getString("docyear"));
				evt.setDocMonth(rs.getString("docmonth"));
				evt.setDocDate(rs.getString("docdate"));
				evt.setDocQty(rs.getInt("countdoc"));
				evt.setIsUsedQty(rs.getInt("isused"));
				evt.setIsCancelQty(rs.getInt("iscancel"));
				
				listdata.add(evt);
				
			}
			if(listdata.size()!=0){
				response.setProcess("CountQueue");
				response.setProcessDesc("Successful");
				response.setIsSuccess(true);
			}else{
					response.setProcess("CountQueue");
					response.setProcessDesc("No Data");
					response.setIsSuccess(false);
			}
			
			rs.close();
			stmt.close();
			
		} catch (SQLException e) {
			// TODO: handle exception
			
			response.setProcess("CountQueue");
			response.setProcessDesc(e.getMessage());
			response.setIsSuccess(false);
		}finally{
			ds.close();
		}
		queue.setResponse(response);
		queue.setData(listdata);
		
		return queue;
	}
	
	
public RP_Resp_SumCheckOutDataBean CheckOutData(String dbName,RP_Reqs_SearchQueBetweenDateBean search){
		
		try {
			Statement stmt = ds.getStatement(dbName);
			
			vQuery = "call USP_DT_CheckOutData ('"+search.getBeginDate()+"','"+search.getEndDate()+"')";
			
			System.out.println(vQuery);
			ResultSet rs = stmt.executeQuery(vQuery);
			
			RP_Resp_SumCheckOutData evt;
			
			listcheckout.clear();
			while(rs.next()){
				evt = new RP_Resp_SumCheckOutData();
				evt.setDocYear(rs.getString("docyear"));
				evt.setDocMonth(rs.getString("docmonth"));
				evt.setDocDate(rs.getString("docdate"));
				evt.setSumPickupQty(rs.getDouble("sumpickqty"));
				evt.setSumCheckOutQty(rs.getDouble("sumcheckoutqty"));
				evt.setCountItem(rs.getInt("countitem"));
				evt.setPickupAmount(rs.getDouble("pickamount"));
				evt.setCheckoutAmount(rs.getDouble("checkoutAmount"));
				evt.setBillAmount(rs.getDouble("billamount"));
				
				listcheckout.add(evt);
				
			}
			if(listdata.size()!=0){
				response.setProcess("CheckOutData");
				response.setProcessDesc("Successful");
				response.setIsSuccess(true);
			}else{
					response.setProcess("CheckOutData");
					response.setProcessDesc("No Data");
					response.setIsSuccess(false);
			}
			
			rs.close();
			stmt.close();
			
		} catch (SQLException e) {
			// TODO: handle exception
			
			response.setProcess("CheckOutData");
			response.setProcessDesc(e.getMessage());
			response.setIsSuccess(false);
		}finally{
			ds.close();
		}
		checkout.setResponse(response);
		checkout.setData(listcheckout);
		
		return checkout;
	}


	public RP_Resp_QueueMasterBean QueueMaster(String dbName,RP_Reqs_SearchQueBetweenDateBean search){
		
	try {
		Statement stmt = ds.getStatement(dbName);
		vQuery = "Call USP_DT_QueueMaster ('"+search.getBeginDate()+"','"+search.getEndDate()+"')";
		
		System.out.println(vQuery);
		ResultSet rs = stmt.executeQuery(vQuery);
		
		RP_Resp_QueueMaster evt;
		
		System.out.println("moo");
		
		listqueue.clear();
		while(rs.next()){
			System.out.println("moo");
			evt = new RP_Resp_QueueMaster();
			evt.setDocYear(rs.getString("docyear"));
			evt.setDocMonth(rs.getString("docmonth"));
			evt.setDocDate(rs.getString("docDate"));
			evt.setqId(rs.getInt("qid"));
			evt.setDocNo(rs.getString("docNo"));
			evt.setDocDate(rs.getString("docDate"));
			evt.setCarLicence(rs.getString("carLicence"));
			evt.setCarBrand(rs.getString("carBrand"));
			evt.setQueStatus(rs.getInt("status"));
			evt.setNumberOfItem(rs.getInt("numberOfItem"));
			evt.setPickAmount(rs.getDouble("pickAmount"));
			evt.setCheckoutAmount(rs.getDouble("checkoutAmount"));
			evt.setBillAmount(rs.getDouble("billAmount"));
			evt.setCreateDate(rs.getDate("createDate"));
			evt.setCreateTime(rs.getTime("createDate"));
			evt.setIsCancel(rs.getInt("isCancel"));
						
			listqueue.add(evt);
		}
		
		
		
		if(listqueue.size()!=0){
			response.setProcess("QueueMaster");
			response.setProcessDesc("Successful");
			response.setIsSuccess(true);
		}else{
				response.setProcess("QueueMaster");
				response.setProcessDesc("No Data");
				response.setIsSuccess(false);
		}
		
		rs.close();
		stmt.close();
		
	}
	catch (SQLException e) {
		// TODO: handle exception
		
		response.setProcess("QueueMaster");
		response.setProcessDesc(e.getMessage());
		response.setIsSuccess(false);
	}finally{
		ds.close();
	}
	
	qmaster.setResponse(response);
	qmaster.setData(listqueue);
	
	return qmaster;
	}
	
	
	public RP_Resp_QueueDetailsBean QueueDetails(String dbName,RP_Reqs_SearchQueBetweenDateBean search){
		
		try {
			Statement stmt = ds.getStatement(dbName);
			vQuery = "Call USP_DT_QueueDetails ('"+search.getBeginDate()+"','"+search.getEndDate()+"')";
			
			System.out.println(vQuery);
			ResultSet rs = stmt.executeQuery(vQuery);
			
			RP_Resp_QueueDetails evt;
			
			System.out.println("moo");
			
			listqueue.clear();
			while(rs.next()){

				evt = new RP_Resp_QueueDetails();
				evt.setDocYear(rs.getString("docYear"));// docYear;
				evt.setDocMonth(rs.getString("docMonth"));//String docMonth;
				evt.setDocWeek(rs.getString("docWeek"));//String docWeek;
				evt.setDocDate(rs.getString("docDate"));//String docDate;
				evt.setDocHour(rs.getString("docHour"));//String docHour;
				evt.setCreateDate(rs.getString("createDate"));//String createDate;
				evt.setqId(rs.getInt("qId"));//int qId;
				evt.setCarLicence(rs.getString("carLicence"));//String carLicence;
				evt.setCarBrand(rs.getString("carBrand"));//String carBrand;
				evt.setStatus(rs.getInt("status"));//int status;
				evt.setNumberOfItem(rs.getInt("numberOfItem"));//int numberOfItem;
				evt.setIsCancel(rs.getInt("isCancel"));
				
				evt.setItemCode(rs.getString("itemCode"));//String itemCode;
				evt.setItemName(rs.getString("itemName"));//String itemName;
				evt.setUnitCode(rs.getString("unitCode"));//String unitCode;
				evt.setPickQty(rs.getDouble("pickQty"));//double pickQty;
				evt.setCheckoutQty(rs.getDouble("checkoutQty"));//double checkoutQty;
				evt.setItemAmount(rs.getDouble("itemAmount"));//double itemAmount;
				evt.setCheckoutAmount(rs.getDouble("checkoutAmount"));//double checkoutAmount;
				evt.setBillAmount(rs.getDouble("billAmount"));//double billAmount;
				evt.setPickerCode(rs.getString("pickerCode"));//String pickerCode;
				evt.setCheckerCode(rs.getString("checkerCode"));//String checkerCode;
				evt.setPickerName(rs.getString("pickerName"));//String pickerName;
				evt.setCheckerName(rs.getString("checkerName"));//String checkerName;
				evt.setItemCancel(rs.getInt("itemCancel"));
				evt.setInvoiceNo(rs.getString("invoiceNo"));
				evt.setExpertCode(rs.getString("expertcode"));
				evt.setDepartmentCode(rs.getString("departcode"));
				evt.setDepartmentName(rs.getString("departname"));
				evt.setCategoryCode(rs.getString("catcode"));
				evt.setCategoryName(rs.getString("catname"));
				evt.setSecCode(rs.getString("secManCode"));
				evt.setSecName(rs.getString("secManName"));
				evt.setItemId(rs.getInt("itemid"));
//				
//				
//				listitem.clear();
//				try {
//					Statement stmtSub = ds.getStatement(dbName);
//					vQuerySub = "Call USP_DT_QueueItemDetails ('"+rs.getString("docdate")+"','"+rs.getString("docno")+"')";
//					
//					System.out.println(vQuerySub);
//					ResultSet rsSub = stmtSub.executeQuery(vQuerySub);
//					
//					listitem.clear();
//					
//					RP_Resp_QueueItem evtSub;
//					while(rsSub.next()){
//						evtSub = new RP_Resp_QueueItem();
//						evtSub.setItemCode(rsSub.getString("itemCode"));//String itemCode;
//						evtSub.setItemName(rsSub.getString("itemName"));//String itemName;
//						evtSub.setUnitCode(rsSub.getString("unitCode"));//String unitCode;
//						evtSub.setPickQty(rsSub.getDouble("pickQty"));//double pickQty;
//						evtSub.setCheckoutQty(rsSub.getDouble("checkoutQty"));//double checkoutQty;
//						evtSub.setItemAmount(rsSub.getDouble("itemAmount"));//double itemAmount;
//						evtSub.setCheckoutAmount(rsSub.getDouble("checkoutAmount"));//double checkoutAmount;
//						evtSub.setBillAmount(rsSub.getDouble("billAmount"));//double billAmount;
//						evtSub.setPickerCode(rsSub.getString("pickerCode"));//String pickerCode;
//						evtSub.setCheckerCode(rsSub.getString("checkerCode"));//String checkerCode;
//						evtSub.setPickerName(rsSub.getString("pickerName"));//String pickerName;
//						evtSub.setCheckerName(rsSub.getString("checkerName"));//String checkerName;
//						evtSub.setItemCancel(rsSub.getInt("itemCancel"));
//						
//						System.out.println("Line  :"+rsSub.getString("itemName"));
//						
//						listitem.add(evtSub);
//					}
//					rsSub.close();
//					stmtSub.close();
//					
//					
//					 
//				} catch (SQLException e) {
//					response.setProcess("QueueDetails");
//					response.setProcessDesc(e.getMessage());
//					response.setIsSuccess(false);
//				}
//				
//				evt.setqItem(listitem);
			
				listqdetails.add(evt);
			}
			
			
			
			if(listqueue.size()!=0){
				response.setProcess("QueueDetails");
				response.setProcessDesc("Successful");
				response.setIsSuccess(true);
			}else{
					response.setProcess("QueueDetails");
					response.setProcessDesc("No Data");
					response.setIsSuccess(false);
			}
			
			rs.close();
			stmt.close();
			
		}
		catch (SQLException e) {
			// TODO: handle exception
			
			response.setProcess("QueueDetails");
			response.setProcessDesc(e.getMessage());
			response.setIsSuccess(false);
		}finally{
			ds.close();
		}
		
		qdetails.setResponse(response);
		qdetails.setData(listqdetails);
		
		return qdetails;
		}
}
