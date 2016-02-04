package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bean.IV_Resp_BankBean;
import bean.IV_Resp_SearchBankBean;
import bean.LoginBean;
import bean.PK_Resp_CarBrandBean;
import bean.PK_Resp_EditOrderBean;
import bean.PK_Resp_GetDataQueue;
import bean.PK_Reqs_EditOrderBean;
import bean.PK_Reqs_ManageItemBean;
import bean.CT_Reqs_NewDocNoBean;
import bean.PK_Resp_GetItemBarcodeBean;
import bean.PK_Resp_ManageItemBean;
import bean.PK_Resp_ManageItemListBean;
import bean.PK_Resp_NewOrderHeaderBean;
import bean.PK_Resp_OrderPendingBean;
import bean.CT_Resp_ResponseBean;
import bean.PK_Resp_SearchCarBrandBean;
import bean.SearchBean;
import bean.PK_Reqs_DeleteOrderBean;
import bean.UserSearchBean;
import bean.orderPendingBean;
import connect.QueueConnect;

public class PickupController {
	private final QueueConnect ds = QueueConnect.INSTANCE;
	private String Textstring;
	private String vQuery;
	
	CT_Resp_ResponseBean response = new CT_Resp_ResponseBean();
	PK_Resp_OrderPendingBean respPending = new PK_Resp_OrderPendingBean();
	List<PK_Resp_OrderPendingBean> orderList = new ArrayList<PK_Resp_OrderPendingBean>();
	orderPendingBean order = new orderPendingBean();
	PK_Resp_EditOrderBean editOrder = new PK_Resp_EditOrderBean();
	PK_Resp_ManageItemBean respItem = new PK_Resp_ManageItemBean();
	
	PK_Resp_NewOrderHeaderBean qIdOrder = new PK_Resp_NewOrderHeaderBean();
	GenNewDocnoController genNo = new GenNewDocnoController();
	
	List<PK_Resp_CarBrandBean> brand = new ArrayList<PK_Resp_CarBrandBean>();
	PK_Resp_SearchCarBrandBean carBrand = new PK_Resp_SearchCarBrandBean();
	
	ExcecuteController excecute = new  ExcecuteController();
	
	private java.text.SimpleDateFormat dtDoc= new java.text.SimpleDateFormat();
	private java.text.SimpleDateFormat dt= new java.text.SimpleDateFormat();

	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	//dt.applyPattern("yyyy-MM-dd HH:mm:ss.S");
	

	private boolean isSuccess;
	private boolean isSuccessSub;
	
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
	public boolean isSuccessSub() {
		return isSuccessSub;
	}
	public void setSuccessSub(boolean isSuccessSub) {
		this.isSuccessSub = isSuccessSub;
	}
	public orderPendingBean searchOrderPending(String dbName,SearchBean keyword){
		dtDoc.applyPattern("yyyy-MM-dd");
		dt.applyPattern("yyyy-MM-dd HH:mm:ss.S");
		Date dateNow = new Date();
		
		int vCountToken=0;
		
			try {
					Statement stmt = ds.getStatement(dbName);
					Statement stmt1 = ds.getStatement("SmartConfig");
					
					vQuery = "select count(userUUID) as vCount from UserAccess where userUUID = '"+ keyword.getAccessToken() +"'";
					System.out.println("vCountToken : "+vQuery);
					ResultSet rs1 = stmt1.executeQuery(vQuery);
					while (rs1.next()) {
						vCountToken = 1;//rs1.getInt("vCount");
					}
				
				if (vCountToken>0){
			    
					if (keyword.getKeyword().equals("")){
						Textstring="select * from Queue where docDate = '"+dateFormat.format(dateNow)+"' order by createDate ";
					}else{
						Textstring="select * from Queue where docDate = '"+dateFormat.format(dateNow)+"' and (docno like'%"+keyword.getKeyword()+"%' or carLicence like '%"+keyword.getKeyword()+"%') order by createDate";
					}
					
				   	System.out.println(Textstring);
				    
				    ResultSet rs = stmt.executeQuery(Textstring);
				    
				    int roworder=0;
				    
				    PK_Resp_OrderPendingBean evt; 
				    
				    orderList.clear();
				   	while (rs.next()) {
				   		roworder++;
				   		
				   		evt = new PK_Resp_OrderPendingBean();	
	
				   		evt.setqId(rs.getInt("qId"));
				   		evt.setCarNumber(rs.getString("carLicence"));
				   		evt.setCarBrand(rs.getString("carBrand"));
				   		evt.setNumberOfItem(rs.getInt("numberOfItem"));
				   		evt.setTimeCreate(rs.getString("createDate"));
				   		evt.setStatus(rs.getInt("status"));
				   		evt.setIscancel(rs.getInt("iscancel"));
	
		    		    orderList.add(evt);
		    		    
		    		    order.setOrder(orderList);
						
		    		    //System.out.println(order.getCarNumber());
					}
				   	if (roworder==0) { //¤é¹ËÒäÁè¾º
		    		    order.setOrder(orderList);
				   	}  
				}else{
					order.setOrder(orderList);
				}
				
				rs1.close();
				stmt1.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
			    ds.close();
			}
			return order;
		}
	
	
	public PK_Resp_NewOrderHeaderBean newOrder(String dbName,CT_Reqs_NewDocNoBean reqOrder){
		String docDate;
		String vGenNewDocNo = "";
		boolean success;
		int qId;
		int vCountToken=0;
		
		dtDoc.applyPattern("yyyy-MM-dd");
		dt.applyPattern("yyyy-MM-dd HH:mm:ss.S");
		Date dateNow = new Date();
		
		CT_Reqs_NewDocNoBean reqNewDocNo = new CT_Reqs_NewDocNoBean();
		
		try {
			Statement stmt = ds.getStatement("SmartConfig");
			
			vQuery = "select count(userUUID) as vCount from UserAccess where userUUID = '"+ reqOrder.getAccessToken() +"'";
			System.out.println("vCountToken : "+vQuery);
			ResultSet rs1 = stmt.executeQuery(vQuery);
			while (rs1.next()) {
				vCountToken = 1;//rs1.getInt("vCount");
			}
			
		    rs1.close();
		    stmt.close();
			
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}finally{
			ds.close();
		}
		
		System.out.println("vCountToken : "+vCountToken);
		
		if (vCountToken>0){

		vGenNewDocNo = genNo.genDocNo(0);
		qId = genNo.genqId();
		
		qIdOrder = new PK_Resp_NewOrderHeaderBean();
		
		//reqNewDocNo.setCarNumber("4712");
		
		if(reqOrder.getCarNumber()!=null && reqOrder.getCarNumber()!=""){
		
		vQuery ="insert into Queue(docNo,docDate,status,isCancel,customerId,warehouseId,customerCode,whCode,carLicence,carBrand,creatorCode,createDate,qId) values("+"'"+vGenNewDocNo+"','"+dateFormat.format(dateNow)+"',0,0,'99999',0,'99999','S1-B','"+reqOrder.getCarNumber()+"','"+reqOrder.getCarBrand()+"','admin',CURRENT_TIMESTAMP,"+qId+")";
		System.out.println(vQuery); 
		try {
				isSuccess= excecute.execute(dbName,vQuery);
				
				response.setIsSuccess(true);
				response.setProcess("newPickup");
				response.setProcessDesc("Successfull");
				qIdOrder.setqId(qId);
				qIdOrder.setResponse(response);
			} catch (Exception e) {
				response.setIsSuccess(false);
				response.setProcess("newPickup");
				response.setProcessDesc("Error : "+e.getMessage());
				qIdOrder.setqId(0);
				qIdOrder.setResponse(response);
			}finally{
				ds.close();
			}
		}else{
			
			response.setIsSuccess(false);
			response.setProcess("newPickup");
			response.setProcessDesc("Error : No Have CarLicence");
			qIdOrder.setqId(0);
			qIdOrder.setResponse(response);
		}
		}else{
			response.setIsSuccess(false);
			response.setProcess("newPickup");
			response.setProcessDesc("Error : Access is deny !!!!!");
			qIdOrder.setqId(0);
			qIdOrder.setResponse(response);
		}
		return qIdOrder;
	}
	
	
	public PK_Resp_EditOrderBean  editOrder(String dbName,PK_Reqs_EditOrderBean reqEdit){
		PK_Resp_GetDataQueue getQueue = new PK_Resp_GetDataQueue();
		getDataFromData getData = new getDataFromData();
		
		getQueue = getData.searchQueue(reqEdit.getqId());
		int vCountToken =0;
		
		try {
			Statement stmt = ds.getStatement("SmartConfig");
			
			vQuery = "select count(userUUID) as vCount from UserAccess where userUUID = '"+ reqEdit.getAccessToken() +"'";
			ResultSet rs1 = stmt.executeQuery(vQuery);
			while (rs1.next()) {
				vCountToken = 1;//rs1.getInt("vCount");
			}
		    rs1.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}finally{
			ds.close();
		}
		
		if (vCountToken > 0){
		if (reqEdit.getCarNumber()!=null && reqEdit.getCarNumber()!=""){
			if(getQueue.getIsCancel()==0){
				
				if(getQueue.getStatus()!=3){

					vQuery ="update Queue set status ="+reqEdit.getStatus()+" ,carLicence = '"+reqEdit.getCarNumber()+"',"+"carBrand= '"+reqEdit.getCarBrand()+"',editorCode='admin',editDate = CURRENT_TIMESTAMP where qid ="+reqEdit.getqId();
					System.out.println(vQuery); 
					try {
							isSuccess= excecute.execute(dbName,vQuery);
							response.setIsSuccess(true);
							response.setProcess("editPickup");
							response.setProcessDesc("Successfull");
							editOrder.setResponse(response);
							
						} catch (Exception e) {
							isSuccess=false;
							response.setIsSuccess(false);
							response.setProcess("editPickup");
							response.setProcessDesc("Error : "+e.getMessage());
							editOrder.setResponse(response);
						}	finally{
							ds.close();
						}
				}else{
					isSuccess=false;
					response.setIsSuccess(false);
					response.setProcess("editPickup");
					response.setProcessDesc("Error : Queue status is used");
					editOrder.setResponse(response);
				}

			}else{
				isSuccess=false;
				response.setIsSuccess(false);
				response.setProcess("editPickup");
				response.setProcessDesc("Error : Queue is cancel");
				editOrder.setResponse(response);			
			}
			
			}else{
				isSuccess=false;
				response.setIsSuccess(false);
				response.setProcess("editPickup");
				response.setProcessDesc("Error : No Have CarLicence");
				editOrder.setResponse(response);
			}
		
		}else{
			isSuccess=false;
			response.setIsSuccess(false);
			response.setProcess("editPickup");
			response.setProcessDesc("Error : Access is deny !!!!!");
			editOrder.setResponse(response);
		}
		
		return editOrder;
	}
		

	public PK_Resp_EditOrderBean deleteOrder(String dbName,PK_Reqs_DeleteOrderBean delOrder){
		PK_Resp_GetDataQueue getQueue = new PK_Resp_GetDataQueue();
		getDataFromData getData = new getDataFromData();	
		String vQuerySub;
		dtDoc.applyPattern("yyyy-MM-dd");
		dt.applyPattern("yyyy-MM-dd HH:mm:ss.S");
		Date dateNow = new Date();
		int vCountToken=0;
		
		try {
			Statement stmt = ds.getStatement("SmartConfig");
			
			vQuery = "select count(userUUID) as vCount from UserAccess where userUUID = '"+ delOrder.getAccessToken() +"'";
			ResultSet rs1 = stmt.executeQuery(vQuery);
			while (rs1.next()) {
				vCountToken = 1;//rs1.getInt("vCount");
			}
			
		    rs1.close();
		    stmt.close();
			
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}finally{
			ds.close();
		}
		
		if (vCountToken>0){
		if (delOrder.getqId() != 0){
			
			getQueue = getData.searchQueue(delOrder.getqId());
			
			if (getQueue.getIsCancel()==0){
				
				if(getQueue.getStatus()==0){
					vQuery ="update Queue set isCancel = 1,cancelCode='admin',cancelDate = CURRENT_TIMESTAMP where  docDate ='"+dateFormat.format(dateNow)+"' and status <> 3 and qid ="+delOrder.getqId();
					System.out.println(vQuery); 
					try {
							isSuccess= excecute.execute(dbName,vQuery);
							
							vQuerySub ="update QItem set isCancel = 1,cancelCode='admin',cancelDate = CURRENT_TIMESTAMP where docDate ='"+dateFormat.format(dateNow)+"' and isCheckOut = 0 and qid ="+delOrder.getqId();
							isSuccess= excecute.execute(dbName,vQuerySub);
							
							response.setIsSuccess(true);
							response.setProcess("delPickup");
							response.setProcessDesc("Successfull");
							editOrder.setResponse(response);
						} catch (Exception e) {
							isSuccess=false;
							response.setIsSuccess(false);
							response.setProcess("delPickup");
							response.setProcessDesc("Error : "+e.getMessage());
							editOrder.setResponse(response);
						}finally{
							ds.close();
						}
				}else{
					isSuccess=false;
					response.setIsSuccess(false);
					response.setProcess("delPickup");
					response.setProcessDesc("Error : Queue status is uesd");
					editOrder.setResponse(response);
				}
					
				
			}else{
				isSuccess=false;
				response.setIsSuccess(false);
				response.setProcess("delPickup");
				response.setProcessDesc("Error : Queue is cancel");
				editOrder.setResponse(response);
			}

			}else{
				isSuccess=false;
				response.setIsSuccess(false);
				response.setProcess("delPickup");
				response.setProcessDesc("Error : No Have Queue ID");
				editOrder.setResponse(response);
			}
		}else{
			isSuccess=false;
			response.setIsSuccess(false);
			response.setProcess("delPickup");
			response.setProcessDesc("Error : Access is deny !!!!!");
			editOrder.setResponse(response);
		}
		
		return editOrder;
	}
	
	
	public PK_Resp_ManageItemBean insertItemPickup(String dbName,PK_Reqs_ManageItemBean reqItem){
		getDataFromData getData = new getDataFromData();
		PK_Resp_GetItemBarcodeBean getBarData = new PK_Resp_GetItemBarcodeBean();
		PK_Resp_GetDataQueue getQueue = new PK_Resp_GetDataQueue();
		PK_Resp_ManageItemListBean itemList = new PK_Resp_ManageItemListBean();
		LoginBean userCode = new LoginBean();
		int vCheckExistItem=0;
		double itemPrice=0.0;
		double itemAmount=0.0;
		String vQuerySub;
		
		dtDoc.applyPattern("yyyy-MM-dd");
		dt.applyPattern("yyyy-MM-dd HH:mm:ss.S");
		Date dateNow = new Date();
		int vCountToken = 0 ;
		
		try {
			Statement stmt = ds.getStatement("SmartConfig");
			
			vQuery = "select count(userUUID) as vCount from UserAccess where userUUID = '"+ reqItem.getAccessToken() +"'";
			ResultSet rs1 = stmt.executeQuery(vQuery);
			while (rs1.next()) {
				vCountToken = 1;//rs1.getInt("vCount");
			}
		    rs1.close();
		    stmt.close();
		    
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}finally{
			ds.close();
		}
		
		if (vCountToken>0){
		if (reqItem.getqId()!=0){
			if (reqItem.getBarCode()!=null && reqItem.getBarCode()!=""){
				System.out.println("1");
					getQueue = getData.searchQueue(reqItem.getqId());
					
					if (getQueue.getIsCancel()==0){
						
						System.out.println("Error : "+getQueue.getStatus());
						
						if(getQueue.getStatus() < 2) {
							getBarData = getData.searchItemCode(reqItem.getBarCode());
							userCode = getData.searchUserAccessToken(reqItem.getAccessToken());
							vCheckExistItem = getData.checkItemExistQueue(reqItem);
							itemPrice = getData.searchItemPrice(getBarData.getCode(),reqItem.getBarCode(), getBarData.getUnitCode());

							itemAmount = itemPrice*reqItem.getQtyBefore();
							
							if (getBarData.getCode()!=null && getBarData.getCode()!=""){

							if (vCheckExistItem==0){
								vQuery = "insert into QItem(qId,docNo,docDate,itemCode,itemName,unitCode,barCode,qty,pickQty,loadQty,checkoutQty,price,itemAmount,rate1,pickerCode,pickDate,isCancel,lineNumber) "+ "values("
									+reqItem.getqId()+",'"+getQueue.getDocNo()+"','"+dateFormat.format(dateNow)+"','"+ getBarData.getCode()+"','"+getBarData.getItemName()+"','"+getBarData.getUnitCode()+"','"+reqItem.getBarCode()+"',"+ reqItem.getQtyBefore()+","+reqItem.getQtyBefore()+",0,0,"+itemPrice+","+itemAmount+","+getBarData.getRate1()+",'"+userCode.getEmployeeCode()+ "',CURRENT_TIMESTAMP,"+reqItem.getIsCancel()+",0)";
							}else{
								if(reqItem.getIsCancel()==0 && reqItem.getQtyBefore()!= 0){
									vQuery = "update QItem set qty ="+reqItem.getQtyBefore()+",pickQty="+reqItem.getQtyBefore()+",price ="+itemPrice+",itemAmount="+itemAmount+",isCancel=0 where qId = "+reqItem.getqId()+" and docNo ='"+getQueue.getDocNo()+"' and itemCode='"+getBarData.getCode()+"' and barCode ='"+reqItem.getBarCode()+"' and unitCode ='"+getBarData.getUnitCode()+"'";
								}else{
									vQuery = "update QItem set qty=0,pickQty = 0,itemAmount=0,isCancel=1,cancelCode='admin',cancelDate = CURRENT_TIMESTAMP where qId = "+reqItem.getqId()+" and docNo ='"+getQueue.getDocNo()+"' and itemCode='"+getBarData.getCode()+"' and barCode ='"+reqItem.getBarCode()+"' and unitCode ='"+getBarData.getUnitCode()+"'";
								}
								
								System.out.println("Update Cancel :"+vQuery);
								
							}
							System.out.println(vQuery);

							try{
								isSuccess= excecute.execute(dbName,vQuery);
								
								vQuerySub = "update  Queue set numberofitem = (select count(itemCode) as countItem from QItem where docNo = '"+getQueue.getDocNo()+"') where docNo ='"+getQueue.getDocNo()+"'";
								isSuccess= excecute.execute(dbName,vQuerySub);
								
								itemList.setBarCode(reqItem.getBarCode());
								itemList.setFilePath("");
								itemList.setItemCategory("");
								itemList.setItemCode(getBarData.getCode());
								itemList.setItemName(getBarData.getItemName());
								itemList.setItemPrice(itemPrice);
								itemList.setRemark("");
								itemList.setShortCode(reqItem.getBarCode());
								itemList.setUnitCode(getBarData.getUnitCode());
								
								response.setIsSuccess(true);
								response.setProcess("insertItemPickup");
								response.setProcessDesc("Successfull");
								
								respItem.setItem(itemList);
								respItem.setResponse(response);
								
							}catch(Exception e){
								isSuccess=false;
								response.setIsSuccess(false);
								response.setProcess("insertItemPickup");
								response.setProcessDesc("Error : "+e.getMessage());
								
								respItem.setItem(itemList);
								respItem.setResponse(response);
							}finally{
								ds.close();
							}
							}else{
								
								isSuccess=false;
								response.setIsSuccess(false);
								response.setProcess("insertItemPickup");
								response.setProcessDesc("Error : "+"No have barcode");

								respItem.setItem(itemList);
								respItem.setResponse(response);
							}
						}else{
							isSuccess=false;
							response.setIsSuccess(false);
							response.setProcess("insertItemPickup");
							response.setProcessDesc("Error : Queue status is used");

							respItem.setItem(itemList);
							respItem.setResponse(response);
						}
						
					}else{
						isSuccess=false;
						response.setIsSuccess(false);
						response.setProcess("insertItemPickup");
						response.setProcessDesc("Error : Queue is cancel");

						respItem.setItem(itemList);
						respItem.setResponse(response);
					}
			}else{
				//==============================
				isSuccess=false;
				response.setIsSuccess(false);
				response.setProcess("insertItemPickup");
				response.setProcessDesc("Error : Barcode is null");

				respItem.setItem(itemList);
				respItem.setResponse(response);
			}
		}else{
			//===================================
			isSuccess=false;
			response.setIsSuccess(false);
			response.setProcess("insertItemPickup");
			response.setProcessDesc("Error : qID not generate");

			respItem.setItem(itemList);
			respItem.setResponse(response);
		}
		
		}else{
			isSuccess=false;
			response.setIsSuccess(false);
			response.setProcess("insertItemPickup");
			response.setProcessDesc("Error : Access is deny !!!!!");

			respItem.setItem(itemList);
			respItem.setResponse(response);
		}
			
		
		return respItem;
		
	}
	
	
	
	public PK_Resp_SearchCarBrandBean searchCarBrand(String dbName,UserSearchBean search){
		int countrow=0;
		
		try{
			Statement stmt = ds.getStatement(dbName);
			PK_Resp_CarBrandBean evt;
			PK_Resp_CarBrandBean evt1;
			
			if (search.getKeyword()== null || search.getKeyword()==""){
				vQuery = "select distinct carbrand from Queue order by carBrand";
			}else{
				vQuery = "select distinct carbrand from Queue  where carBrand like '%"+search.getKeyword()+"%'order by carBrand";
			}
			System.out.println(vQuery);
			ResultSet rs = stmt.executeQuery(vQuery);
			
			brand.clear();
			while(rs.next()){
				countrow++;
				
				evt = new PK_Resp_CarBrandBean();
				evt.setName(rs.getString("carBrand"));
				brand.add(evt);
			}
			
			response.setIsSuccess(true);
			response.setProcess("Search Brand");
			response.setProcessDesc("Successfully");
			carBrand.setResponse(response);
			carBrand.setBrand(brand);
			
			if (countrow==0){
				evt1 = new PK_Resp_CarBrandBean();
				brand.add(evt1);
				
				response.setIsSuccess(true);
				response.setProcess("Search Brand");
				response.setProcessDesc("No list brand");
				carBrand.setResponse(response);
			}
			
		    rs.close();
		    stmt.close();
		    
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			ds.close();
		}
		
		return carBrand;
	}

}



