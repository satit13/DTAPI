package controller;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bean.CK_Resp_AllSummaryBean;
import bean.CM_Resp_MemberListBean;
import bean.CT_Reqs_NewDocNoBean;
import bean.CT_Resp_ResponseBean;
import bean.IV_Reqs_CouponBean;
import bean.IV_Reqs_CreditCardBean;
import bean.IV_Resp_ARInvoiceSubBean;
import bean.IV_Resp_ValidateCreditCardBean;
import bean.LoginBean;
import bean.PK_Reqs_ManageItemBean;
import bean.PK_Resp_GetDataQueue;
import bean.PK_Resp_GetItemBarcodeBean;
import bean.PK_Resp_SaleCodeDetails;

import connect.NPSQLConn;
import connect.QueueConnect;
import connect.SQLConn;

public class getDataFromData {
	String vQuery ;
	String vQuerySub ;
	String vDocNo;

	String vGenNewDocNo;
	int getID;
	int getInspectID;
	
	private final QueueConnect ds = QueueConnect.INSTANCE;
	private final SQLConn sqlDS = SQLConn.INSTANCE;
	private final QueueConnect dsTK = QueueConnect.INSTANCE;
	private final NPSQLConn npDS = NPSQLConn.INSTANCE;

	private java.text.SimpleDateFormat dtDoc= new java.text.SimpleDateFormat();
	private java.text.SimpleDateFormat dt= new java.text.SimpleDateFormat();

	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	
	PK_Resp_GetItemBarcodeBean resItem = new PK_Resp_GetItemBarcodeBean();
	PK_Resp_GetDataQueue resQue = new PK_Resp_GetDataQueue();
	CK_Resp_AllSummaryBean allSum = new CK_Resp_AllSummaryBean();
	
	CM_Resp_MemberListBean ar = new CM_Resp_MemberListBean();
	
	IV_Resp_ARInvoiceSubBean queItem = new IV_Resp_ARInvoiceSubBean();
	List<IV_Resp_ARInvoiceSubBean> itemlist = new ArrayList<IV_Resp_ARInvoiceSubBean>();
	CT_Resp_ResponseBean creditcardRes = new CT_Resp_ResponseBean();
	CT_Resp_ResponseBean coupongRes = new CT_Resp_ResponseBean();
	
	PK_Resp_SaleCodeDetails sale = new PK_Resp_SaleCodeDetails();
	
	LoginBean userCode = new LoginBean();
	

	public PK_Resp_GetItemBarcodeBean searchItemCode(String barcode){

		try{
			Statement st = sqlDS.getSqlStatement("POS");
			//Statement st = npDS.getSqlStatement("192.168.0.7", "bcnp");
			
//			vQuery = "select a.itemcode,a.unitcode,rate,b.name1 as barcodename,isnull(d.expertcode,'') as expertcode, "+
//					" isnull(d.departmentcode,'') as departmentcode,isnull(d.department,'') as departname, "+
//					" isnull(d.categorycode,'') as categorycode,isnull(d.category,'') as category, "+
//					" isnull(e.salecode,'') as secCode,isnull(e.salename,'') as secName,isnull(b.averagecost,0) as averagecost "+
//					" from dbo.bcbarcodemaster a " +
//					" inner join dbo.bcitem  b on a.itemcode = b.code " +
//					" inner join dbo.bcstkpacking c on a.itemcode = c.itemcode and a.unitcode = c.unitcode " +
//					" left join nebula.npdb.dbo.tb_inc_itemextension d on b.code = d.itemcode" +
//					" left join nebula.npdb.dbo.tb_ic_sectiondepartment e on d.expertcode = e.catcode and year(getdate()) = e.atyear and month(getdate()) = e.atmonth and d.departmentcode = e.departmentcode " +
//					" where barcode = '"+barcode+"'";
			
			vQuery = "exec dbo.USP_DT_SearchBarCodeDetails '"+barcode+"'";

			ResultSet rs = st.executeQuery(vQuery);
			
			while(rs.next()){
				resItem.setCode(rs.getString("itemcode"));
				resItem.setRate1(rs.getInt("rate"));
				resItem.setUnitCode(rs.getString("unitcode"));
				resItem.setItemName(rs.getString("barcodename"));
				resItem.setExpertCode(rs.getString("expertcode"));
				resItem.setDepartmentCode(rs.getString("departmentcode"));
				resItem.setDepartmentName(rs.getString("departname"));
				resItem.setCategoryCode(rs.getString("categorycode"));
				resItem.setCategoryName(rs.getString("category"));
				resItem.setSecCode(rs.getString("secCode"));
				resItem.setSecName(rs.getString("secName"));
				resItem.setAverageCost(rs.getDouble("averagecost"));
			}
		    rs.close();
		    st.close();
		    
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			sqlDS.close();
		}

		return resItem;
	}
	
	public PK_Resp_GetDataQueue searchQueue(int qId){
		dtDoc.applyPattern("yyyy-MM-dd");
		dt.applyPattern("yyyy-MM-dd HH:mm:ss.S");
		
		
		Date dateNow = new Date();
		try{
			Statement st = ds.getStatement("SmartQ");
			
			vQuery = "select docno,iscancel,status,carLicence,salecode from Queue where qId = "+qId +" and docdate = '"+dateFormat.format(dateNow)+"' limit 1";
			System.out.println("GetDocno :"+vQuery);
			ResultSet rs = st.executeQuery(vQuery);
			while(rs.next()){
				resQue.setDocNo(rs.getString("docno"));
				resQue.setIsCancel(rs.getInt("iscancel"));
				resQue.setStatus(rs.getInt("status"));
				resQue.setCarLicense(rs.getString("carLicence"));
				resQue.setSaleCode(rs.getString("saleCode"));
			}
		    rs.close();
		    st.close();
		    
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ds.close();
		}

		return resQue;
	}
	
	
	public PK_Resp_SaleCodeDetails searchTopSaleCode(int getQ){
		dtDoc.applyPattern("yyyy-MM-dd");
		dt.applyPattern("yyyy-MM-dd HH:mm:ss.S");
		
		
		Date dateNow = new Date();
		
		sale.setIsExist(0);
		sale.setSaleCode("");
		sale.setSaleName("");
		
		try{
			Statement st = ds.getStatement("SmartQ");
			
			vQuery = "call USP_DT_SelectTopSaleCode ("+getQ +")";
			System.out.println("GetDocno :"+vQuery);
			ResultSet rs = st.executeQuery(vQuery);
			while(rs.next()){
				sale.setIsExist(1);
				sale.setSaleCode(rs.getString("code"));
				sale.setSaleName(rs.getString("name"));
			}
		    rs.close();
		    st.close();
		    
		}catch(Exception e){
			sale.setIsExist(0);
			sale.setSaleCode("");
			sale.setSaleName("");
			e.printStackTrace();
		}finally{
			ds.close();
		}

		return sale;
	}
	
	
	public CK_Resp_AllSummaryBean searchQueueSummary(int qId){
		dtDoc.applyPattern("yyyy-MM-dd");
		dt.applyPattern("yyyy-MM-dd HH:mm:ss.S");
		
		
		Date dateNow = new Date();
		try{
			Statement st = ds.getStatement("SmartQ");
			
			vQuery = "select sum(pickqty*price) as totalBeforeAmount,sum(checkoutqty*price) as totalAfterAmount  from Queue a inner join QItem b on a.qid = b.qid and a.docno = b.docno  where a.qId = "+qId +" and a.docdate = '"+dateFormat.format(dateNow)+"' and a.iscancel = 0 and b.iscancel = 0";
			System.out.println("GetDocno :"+vQuery);
			ResultSet rs = st.executeQuery(vQuery);
			while(rs.next()){
				allSum.setTotalBeforePrice(rs.getDouble("totalBeforeAmount"));
				allSum.setTotalAfterPrice(rs.getDouble("totalAfterAmount"));
			}
			
		    rs.close();
		    st.close();
		    
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ds.close();
		}

		return allSum;
	}
	
	
	public double searchQueueCheckOutAmount(int qId){
		double totalAmount = 0.0;
		dtDoc.applyPattern("yyyy-MM-dd");
		dt.applyPattern("yyyy-MM-dd HH:mm:ss.S");
		
		
		Date dateNow = new Date();
		try{
			Statement st = ds.getStatement("SmartQ");
			
			vQuery = "select sum(checkoutqty*price) as totalamount from Queue a inner join QItem b on a.qid = b.qid and a.docno = b.docno  where a.qId = "+qId +" and a.docdate = '"+dateFormat.format(dateNow)+"' and a.iscancel = 0 and b.iscancel = 0 and b.isCheckOut = 1 ";
			System.out.println("GetDocno :"+vQuery);
			ResultSet rs = st.executeQuery(vQuery);
			while(rs.next()){
				totalAmount = rs.getDouble("totalamount");
			}
			
		    rs.close();
		    st.close();
		    
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ds.close();
		}

		return totalAmount;
	}
	
	
	
	public List<IV_Resp_ARInvoiceSubBean> searchQueueCheckOutItem(int qId){
		dtDoc.applyPattern("yyyy-MM-dd");
		dt.applyPattern("yyyy-MM-dd HH:mm:ss.S");
		
		Date dateNow = new Date();
		try{
			Statement st = ds.getStatement("SmartQ");
			
			vQuery = "select a.docno,a.docdate,b.itemcode,b.barcode,b.itemName as name1,b.unitCode,b.checkoutQty,b.price,b.rate1,ifnull(b.averageCost,0) as averagecost,ifnull(b.checkoutAmount,0) as checkoutAmount,b.salecode from Queue a inner join QItem b on a.qid = b.qid and a.docno = b.docno where a.qId = "+qId +" and a.docdate = '"+dateFormat.format(dateNow)+"' and a.iscancel = 0 and b.iscancel = 0 and b.isCheckOut = 1 ";
			System.out.println("GetDocno :"+vQuery);
			ResultSet rs = st.executeQuery(vQuery);
			
			itemlist.clear();
			IV_Resp_ARInvoiceSubBean evt;
			while(rs.next()){
				evt = new IV_Resp_ARInvoiceSubBean();
				evt.setItemCode(rs.getString("itemcode"));
				
				System.out.println("Item CheckOut :"+rs.getString("itemcode"));
				evt.setItemName(rs.getString("name1"));
				evt.setBarCode(rs.getString("barcode"));
				evt.setQty(rs.getDouble("checkoutQty"));
				evt.setPrice(rs.getDouble("price"));
				evt.setUnitCode(rs.getString("unitcode"));
				evt.setPackingRate1(rs.getInt("rate1"));
				evt.setSaleCode(rs.getString("saleCode"));
				evt.setSumOfCost(rs.getDouble("averagecost"));
				itemlist.add(evt);
			}	
		    rs.close();
		    st.close();
		    
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ds.close();
		}

		System.out.println("Item Qty : "+itemlist.size());
		return itemlist;
	}
	
	
	public LoginBean searchUserAccessToken(String accessToken){

		try{
			Statement st = ds.getStatement("SmartConfig");
			
		    vQuery="select u.code,u.name,u.role from  User as u inner join UserAccess as ua on u.code=ua.userCode and ua.userUUID='"+accessToken+"'"
		    		+" order by ua.dateTimeStamp DESC LIMIT 1" ;
		    
			ResultSet rs = st.executeQuery(vQuery);
			while(rs.next()){
				userCode.setEmployeeCode(rs.getString("code"));
				userCode.setEmployeeName(rs.getString("name"));
			}
			
		    rs.close();
		    st.close();
		    
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ds.close();
		}

		return userCode;
	}
	
	
	public PK_Resp_SaleCodeDetails searchSaleCode(String saleCode){
		try {
			
			sale.setIsExist(0);
			sale.setSaleCode("");
			sale.setSaleName("");
			
			Statement st = ds.getStatement("SmartQ");
			vQuery = "call USP_DT_SearchSale ('"+saleCode+"')";
			
			System.out.println("Search Sale :"+vQuery);
			
			ResultSet rs = st.executeQuery(vQuery);
			while(rs.next()){
				sale.setIsExist(1);
				sale.setSaleCode(rs.getString("code"));
				sale.setSaleName(rs.getString("name"));
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			// TODO: handle exception
			sale.setIsExist(0);
			sale.setSaleCode("");
			sale.setSaleName("");
		}finally{
			ds.close();
		}
		
		return sale;
	}
	
	public int checkItemExistQueue(PK_Reqs_ManageItemBean reqItem){
		PK_Resp_GetItemBarcodeBean getItemCode = new PK_Resp_GetItemBarcodeBean();
		dtDoc.applyPattern("yyyy-MM-dd");
		dt.applyPattern("yyyy-MM-dd HH:mm:ss.S");
		
		
		Date dateNow = new Date();
		
		int vExist=0;
		
		getItemCode = this.searchItemCode(reqItem.getBarCode());
		
		try{
			Statement st = ds.getStatement("SmartQ");

		    vQuery = "select ifnull(count(itemcode),0) as vCount from QItem where docDate = '"+ dateFormat.format(dateNow)+"' and qId='"+ reqItem.getqId()+"' and itemcode = '"+ getItemCode.getCode() +"' and unitcode = '"+getItemCode.getUnitCode()+"' ";
		    System.out.println("ExistQueue :"+vQuery);
			ResultSet rs = st.executeQuery(vQuery);
			while(rs.next()){
				vExist = rs.getInt("vCount");
			}
			
		    rs.close();
		    st.close();
		    
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ds.close();
		}

		return vExist;
	}
	
	public double searchItemPrice(String itemCode,String barCode,String unitCode){
		 double price=0.0;
		
		try{
			Statement st = sqlDS.getSqlStatement("POS");
			//vQuery = "select price from Price where itemCode='"+itemCode+"' and unitCode='"+unitCode+"' limit 1";
			vQuery = "set dateformat dmy select top 1 saleprice1 as price from BCPriceList  where itemCode='"+itemCode+"' and unitCode='"+unitCode+"' and saletype = 0 and transporttype = 0 and cast(rtrim(day(getdate()))+'/'+rtrim(month(getdate()))+'/'+rtrim(year(getdate())) as datetime) >= cast(rtrim(day(startdate))+'/'+rtrim(month(startdate))+'/'+rtrim(year(startdate)) as datetime) and  cast(rtrim(day(getdate()))+'/'+rtrim(month(getdate()))+'/'+rtrim(year(getdate())) as datetime) <= cast(rtrim(day(stopdate))+'/'+rtrim(month(stopdate))+'/'+rtrim(year(stopdate)) as datetime)";
			ResultSet rs = st.executeQuery(vQuery);
			while(rs.next()){
				price = rs.getDouble("price");
			}
			
		    rs.close();
		    st.close();
		    
			}catch(SQLException e){
				e.printStackTrace();
				price = 0;
			}finally{
				sqlDS.close();
			}
		
			return price;
	}
	
	
	public CM_Resp_MemberListBean searchCustomerName(String arCode){
		
		try{
			Statement st = sqlDS.getSqlStatement("POS");
			vQuery = "select top 1 code,name1,billAddress,isnull(telephone,'') as telephone,isnull(taxNo,'') as taxId,isnull(emailaddress,'') as email,sumofmark1 as point,isnull(memberId,'') as memberId from BCAR where code='"+arCode+"'";
			ResultSet rs = st.executeQuery(vQuery);
			
			while(rs.next()){
				ar.setArName(rs.getString("name1"));
				ar.setArAddress(rs.getString("billAddress"));
				ar.setArId(rs.getString("taxid"));
				ar.setArPhone(rs.getString("telephone"));
				ar.setArPoint(rs.getDouble("point"));
			}
			
		    rs.close();
		    st.close();
		    
			}catch(SQLException e){
				e.printStackTrace();
				ar.setArName("");
				ar.setArAddress("");
				ar.setArId("");
				ar.setArPhone("");
				ar.setArPoint(0);
			}finally{
				sqlDS.close();
			}
		
			return ar;
	}	
	
	
	public CT_Resp_ResponseBean validateCreditCard(List<IV_Reqs_CreditCardBean> crdCard){
		int counterr=0;
		int isUsed = 0;
		
		if (crdCard.size()!=0){
			for(int x=0;x<crdCard.size();x++){
				
				System.out.println("carditcard No :"+crdCard.get(x).getCardNo());
				
				if (crdCard.get(x).getCardNo() != "" && crdCard.get(x).getCardNo() != null){
					if (crdCard.get(x).getAmount()!=0){
						if(crdCard.get(x).getBankCode()!= "" && crdCard.get(x).getBankCode()!= null){
							if (crdCard.get(x).getConfirmNo() != "" && crdCard.get(x).getConfirmNo() != null){
								if (crdCard.get(x).getCreditType()!= "" && crdCard.get(x).getCreditType()!= null){

									try{
										Statement st = sqlDS.getSqlStatement("POS");
										
										vQuery = "set dateformat dmy select count(ConfirmNo) as vCount from dbo.bccreditcard where CreditCardNo = '"+crdCard.get(x).getCardNo()+"' and ConfirmNo = '"+crdCard.get(x).getConfirmNo()+"'";
										ResultSet rs = st.executeQuery(vQuery);
										
										System.out.println("Verify Coupong :"+vQuery);
										while(rs.next()){
											isUsed = rs.getInt("vCount");
										}
										
										if (isUsed == 0){
											creditcardRes.setIsSuccess(true);
											creditcardRes.setProcess("validate creditcard");
											creditcardRes.setProcessDesc("CreditCard is ok");
										}else{
											creditcardRes.setIsSuccess(false);
											creditcardRes.setProcess("validate creditcard");
											creditcardRes.setProcessDesc("CreditCard is used");
										}
										
									    rs.close();
									    st.close();
									    
									}catch(SQLException e){
										counterr++;
										creditcardRes.setIsSuccess(false);
										creditcardRes.setProcess("validate creditcard");
										creditcardRes.setProcessDesc(e.getMessage());
									}
									finally{
										sqlDS.close();
									}

								}else{
									counterr++;
									creditcardRes.setIsSuccess(false);
									creditcardRes.setProcess("validate creditcard");
									creditcardRes.setProcessDesc("CreditType is null or empty");
								}
							}else{
								counterr++;
								creditcardRes.setIsSuccess(false);
								creditcardRes.setProcess("validate creditcard");
								creditcardRes.setProcessDesc("ConfirmNo is null or empty");
							}
						}else{
							creditcardRes.setIsSuccess(false);
							creditcardRes.setProcess("validate creditcard");
							creditcardRes.setProcessDesc("Bank is null or empty");
						}
					}else{
						counterr++;
						creditcardRes.setIsSuccess(false);
						creditcardRes.setProcess("validate creditcard");
						creditcardRes.setProcessDesc("CreditAmount = 0");
					}
				}else{
					counterr++;
					creditcardRes.setIsSuccess(false);
					creditcardRes.setProcess("validate creditcard");
					creditcardRes.setProcessDesc("CreditcardNo is null or empty");
				}
			}
		}
		return creditcardRes; 
		
	}
	
	
	public CT_Resp_ResponseBean verifyCoupong(List<IV_Reqs_CouponBean> coupong){
		int counterr=0;
		int checkexist=0;
			
		if(coupong.size()!=0){
			for(int y=0;y<coupong.size();y++){
				if (coupong.get(y).getCouponCode()!="" && coupong.get(y).getCouponCode()!= null){
					if (coupong.get(y).getAmount()!= 0){
						try{
							Statement st = sqlDS.getSqlStatement("POS");
							vQuery = "set dateformat dmy select count(code) as vCount from dbo.bccoupon where code = '"+coupong.get(y).getCouponCode()+"' and couponval = "+coupong.get(y).getAmount()+"and code not in (select couponcode from dbo.bccouponreceive where docno in (select docno from dbo.bcarinvoice where iscancel = 0)) and cast(rtrim(day(getdate()))+'/'+rtrim(month(getdate()))+'/'+rtrim(year(getdate())) as datetime) <= expiredate";
							ResultSet rs = st.executeQuery(vQuery);
							
							System.out.println("Verify Coupong :"+vQuery);
							while(rs.next()){
								checkexist = rs.getInt("vCount");
							}
							
						    rs.close();
						    st.close();
						    
						}catch(SQLException e){
							e.printStackTrace();
							counterr++;
						}finally{
							sqlDS.close();
						}
						
					}else{
						counterr++;
						//error Amount;
					}
						
				}else{
					counterr++;
					//error Code
				}
			}
		}
		
		System.out.println("coupongsize :"+coupong.size()+","+counterr+","+checkexist);
		
		if(counterr==0 && checkexist !=0){
			coupongRes.setIsSuccess(true);
			coupongRes.setProcess("verifyCoupong");
			coupongRes.setProcessDesc("Coupong is pass");
		}else{
			if (counterr==0 && checkexist ==0){
				coupongRes.setIsSuccess(false);
				coupongRes.setProcess("verifyCoupong");
				coupongRes.setProcessDesc("Coupong not exist or expire or value invalid or isUsed");
			}else{
				coupongRes.setIsSuccess(false);
				coupongRes.setProcess("verifyCoupong");
				coupongRes.setProcessDesc("Coupong data error");
			}
		}
		
		
		return coupongRes;
	}
	
	
	public double calcPointInvoice(String vInvNo){
		double point=0;
		
		try {
			Statement st = sqlDS.getSqlStatement("POS");
			
			vQuery = "exec dbo.USP_VP_CheckInvoicePoint '" + vInvNo +"'";
			
			ResultSet rs = st.executeQuery(vQuery);
			
			while(rs.next()){
				point = rs.getDouble("pointbal");
			}
			
		    rs.close();
		    st.close();
		    
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}finally{
			sqlDS.close();
		}
		
		return point;
	}
	
	
	public int CheckAccessToken(String accessToken){
		int access=0;
		
		
		try {
			Statement stmtTK = dsTK.getStatement("SmartConfig");
			
			vQuery = "select count(userUUID) as vCount from UserAccess where userUUID = '"+ accessToken +"'";
			System.out.println("vCountToken : "+vQuery);
			ResultSet rsTK = stmtTK.executeQuery(vQuery);
			while (rsTK.next()) {
				access = rsTK.getInt("vCount");
			}
			rsTK.close();
			stmtTK.close();
			
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}finally{
			dsTK.close();
		}
		
		 return access;
	}
	
	
//	public String UserMoveFile(String userCode)
//	{	
//	    File file = new File("filename");
//	    
//	    // Destination directory
//	    File dir = new File("directoryname");
//	    
//	    // Move file to new directory
//	    boolean success = file.renameTo(new File(dir, file.getName()));
//	    if (!success) {
//	        // File was not successfully moved
//	    }
//    
//	}
}
	

