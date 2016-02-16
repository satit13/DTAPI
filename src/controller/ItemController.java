package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bean.*;
import bean.request.ReqItemEditBean;
import bean.request.ReqItemSearchBean;
import bean.response.ApiItemEditBean;
import bean.response.ApiItemSearchBean;
import bean.response.ResItemSearchBean;
import connect.QueueConnect;
import connect.SQLConn;

public class ItemController {
	private final QueueConnect ds = QueueConnect.INSTANCE;
	private final SQLConn ds1 = SQLConn.INSTANCE;
	private String Textstring;
	
	
	// Bean Response ที่ใช้เป็น Standard
	ResponseBean response = new ResponseBean();
	
	// Bean for ItemSearch
	
	ResItemSearchBean item = new ResItemSearchBean();
	List<ResItemSearchBean> itemlist = new ArrayList<ResItemSearchBean>();
	ApiItemSearchBean apiResponse = new ApiItemSearchBean();

	// Bean for itemEdit
	ApiItemEditBean apiItemEdit_Response = new ApiItemEditBean();
	ReqItemEditBean reqItemEdit = new ReqItemEditBean();
	
	
	
	
	ExcecuteController excecute = new  ExcecuteController();
	
	private boolean isSuccess;
		public boolean isSuccess() {
			return isSuccess;
		}
		public void setSuccess(boolean isSuccess) {
			this.isSuccess = isSuccess;
		}
		
	private void ResponseInit(String keyword) {
		response.setIsSuccess(false);
	 	response.setProcessDesc("Not found :" +keyword);
	 		 		
		    item.setBarcode(null);
		    item.setItemCode(null);
		    item.setItemName(null);
		    item.setItemPrice(null);
		    item.setUnitCode(null);
		    item.setShortCode(null);
		    item.setRemark(null);
		    item.setItemCategory(null);
		    item.setFilePath(null);
		   
		}
	
	//Item Search
	// dbName = ฐานข้อมูลที่ใช้งาน "SmartQ" 
	public ApiItemSearchBean itemSearch(String dbName,ReqItemSearchBean reqItemSearch) {
			java.text.SimpleDateFormat dt= new java.text.SimpleDateFormat();
			dt.applyPattern("yyyy-MM-dd HH:mm:ss.S");
			@SuppressWarnings("unused")
			Date dateNow = new Date();
			
			try {
				// get connection
				Statement stmt = ds.getStatement(dbName);
			    
			 // Check Permission from Access_token ;
			    Textstring="select u.code,u.role from  SmartConfig.User as u ";
			    Textstring=Textstring+"inner join SmartConfig.UserAccess as ua on u.code=ua.userCode ";
			    Textstring=Textstring+"and ua.userUUID='"+reqItemSearch.getAccessToken()+"'";
			    Textstring=Textstring+" order by ua.dateTimeStamp DESC LIMIT 1" ;
			   	System.out.println(Textstring);
			    
			    ResultSet rs = stmt.executeQuery(Textstring);
			    
			    //template
			    response.setProcess("itemSearch Events");
			    ResponseInit(reqItemSearch.getKeyword());
			    apiResponse.setResponse(response);
			    itemlist.clear();
			    itemlist.add(item);
			    apiResponse.setItem(itemlist);

			    if ( rs.next() != false ) 
	        	{	
	        		if (rs.getInt("role")!=0) {
	        			response.setIsSuccess(true);
		        		response.setProcessDesc("successful");
		        		
	        			
	        		} else {
	        			response.setIsSuccess(false);
		        		response.setProcessDesc("User not allowed!");
		        		
	        			
	        		}
	        		
	        	} else 
	        	{
	        		response.setIsSuccess(false);
	        		response.setProcessDesc("Not found user assign!");
	        		System.out.println(response.getProcessDesc());
	        	}
			        
			    rs.close();
			    stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
			    ds.close();
			}
			if (response.getIsSuccess()==true)  { // ตรวจสอบ security
				try {
					
					// get connection
					Statement stmt = ds1.getSqlStatement("POS");
				    
					
//					if (reqItemSearch.getType() == 0 ) // search by keyword
//					{
////						System.out.println("Search ตาม Keyword");
////					    Textstring="select distinct 	a.code as itemCode,	a.name1 as itemName,b.unitCode ,b.shortCode,";
////					    Textstring = Textstring + "'' as remark , '' as filePath, b.barcode,'' as itemCategory,c.price as price ";
////					    Textstring = Textstring + "from Item a inner  join itemBarcode b on a.code = b.code " ;
////					    Textstring = Textstring + "left  join Price c on a.code=c.itemCode and b.unitcode=c.unitCode";
////					    Textstring = Textstring + " where a.code like '%"+reqItemSearch.getKeyword()+"%' ";
////					    Textstring = Textstring + " or b.barcode like  '%"+reqItemSearch.getKeyword()+"%'";
////					 //   Textstring = Textstring + " where  b.barcode =  '"+reqItemSearch.getKeyword()+"' limit 5";
////					    Textstring = Textstring + " or a.name1 like '%"+reqItemSearch.getKeyword()+"%'";
////					   Textstring = Textstring + " or b.shortcode like  '%"+reqItemSearch.getKeyword()+"%' limit 5";
//
//					    Textstring=" set dateformat dmy select distinct top 20	a.itemcode,a.barcode,isnull(b.name1,'')  as itemname,isnull(a.unitcode,'') as UnitCode, "
//					    +"isnull(c.saleprice1 ,0) as price,isnull(b.shortname,'') as shortcode,isnull(b.picfilename1,'') as  filePath  "
//					    +"from 	dbo.bcbarcodemaster a "
//					    +"left join dbo.bcitem b on a.itemcode = b.code "
//					    +"left join dbo.bcpricelist c on saletype = 0 and transporttype = 0 and c.itemcode = b.code and c.unitcode = a.unitcode and cast(rtrim(day(getdate()))+'/'+rtrim(month(getdate()))+'/'+rtrim(year(getdate())) as datetime) >= cast(rtrim(day(startdate))+'/'+rtrim(month(startdate))+'/'+rtrim(year(startdate)) as datetime) and  cast(rtrim(day(getdate()))+'/'+rtrim(month(getdate()))+'/'+rtrim(year(getdate())) as datetime) <= cast(rtrim(day(stopdate))+'/'+rtrim(month(stopdate))+'/'+rtrim(year(stopdate)) as datetime) "
//					    +"where  a.barcode like  '%"+reqItemSearch.getKeyword()+"%' or b.name1 like '%"+reqItemSearch.getKeyword() +"%'  or isnull(b.shortname,'') like '%"+reqItemSearch.getKeyword()+"%' order by a.itemcode,a.barcode ";
//						
//					} 
//					else  // search หา ตาม barcode อย่างเดียวก
//					{
//						//System.out.println("Scan หาตาม barcode อย่างเดียว");
//					    //Textstring="select distinct 	a.code as itemCode,	a.name1 as itemName,b.unitCode ,b.shortCode,";
//					    //Textstring = Textstring + "'' as remark , '' as filePath, b.barcode,'' as itemCategory,c.price as price ";
//					    //Textstring = Textstring + "from Item a inner  join itemBarcode b on a.code = b.code " ;
//					    //Textstring = Textstring + "left  join Price c on a.code=c.itemCode and b.unitcode=c.unitCode";
//						//Textstring = Textstring + " where  b.barcode =  '"+reqItemSearch.getKeyword()+"' limit 1";
//						
//					    Textstring=" set dateformat dmy select 	distinct a.itemcode as itemCode,a.barcode,isnull(b.name1,'')  as itemName,isnull(a.unitcode,'') as UnitCode, "
//					    +"isnull(c.saleprice1 ,0) as price,isnull(b.shortname,'') as shortcode,isnull(b.picfilename1,'') as  filePath "
//					    +"from 	dbo.bcbarcodemaster a "
//					    +"left join dbo.bcitem b on a.itemcode = b.code "
//					    +"left join dbo.bcpricelist c on saletype = 0 and transporttype = 0 and c.itemcode = b.code and c.unitcode = a.unitcode and cast(rtrim(day(getdate()))+'/'+rtrim(month(getdate()))+'/'+rtrim(year(getdate())) as datetime) >= cast(rtrim(day(startdate))+'/'+rtrim(month(startdate))+'/'+rtrim(year(startdate)) as datetime) and  cast(rtrim(day(getdate()))+'/'+rtrim(month(getdate()))+'/'+rtrim(year(getdate())) as datetime) <= cast(rtrim(day(stopdate))+'/'+rtrim(month(stopdate))+'/'+rtrim(year(stopdate)) as datetime) "
//					    +"where  a.barcode =  '"+reqItemSearch.getKeyword()+"'";
//					
//					
//					} 
					
					Textstring="exec dbo.USP_POS_SearchItem "+reqItemSearch.getType()+",'"+reqItemSearch.getKeyword()+"'";
					
				   	System.out.println(Textstring);
				    ResultSet rs = stmt.executeQuery(Textstring);
				    response.setProcess("itemSearch");
				    int roworder=0;
				    itemlist.clear();
				   	while (rs.next()) {
				   		roworder++;
				   		item = new ResItemSearchBean();	
						response.setIsSuccess(true);
		        		response.setProcessDesc("successful");
		        		
		        		item.setBarcode(rs.getString("barcode"));
		        		item.setItemCode(rs.getString("itemCode"));
		        		item.setItemName(rs.getString("itemName"));
		        		item.setItemPrice(rs.getDouble("price"));
		        		item.setUnitCode(rs.getString("unitCode"));
		        		item.setRemark("");
		        		item.setShortCode(rs.getString("shortCode"));
		        		item.setFilePath(rs.getString("filePath"));
		    		    itemlist.add(item);
						
		    		    System.out.println(item.getItemCode());
		    		    System.out.println(item.getItemPrice());

					}
				   	if (roworder==0) { //ค้นหาไม่พบ
				   		ResponseInit(reqItemSearch.getKeyword());
				   		itemlist.clear();
				   		//itemlist.add(item);
				   	}  
				   	apiResponse.setResponse(response);
				   	apiResponse.setItem(itemlist);
				   	
				    rs.close();
				    stmt.close();
				    
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
				    ds.close();
				}
			}
			
			
			
			return apiResponse;
			
		}
	
	public ApiItemEditBean itemEdit(String dbName,ReqItemEditBean reqItemEdit)
	{
		java.text.SimpleDateFormat dt= new java.text.SimpleDateFormat();
		dt.applyPattern("yyyy-MM-dd HH:mm:ss.S");
		@SuppressWarnings("unused")
		Date dateNow = new Date();
		
		//Security check by token
		try {
			// get connection
			Statement stmt = ds.getStatement(dbName);
		    
		 // Check Permission from Access_token ;
		    Textstring="select u.code,u.role from  SmartConfig.User as u ";
		    Textstring=Textstring+"inner join SmartConfig.UserAccess as ua on u.code=ua.userCode ";
		    Textstring=Textstring+"and ua.userUUID='"+reqItemEdit.getAccessToken()+"'";
		    Textstring=Textstring+" where u.role=1 order by ua.dateTimeStamp DESC LIMIT 1" ;
		   	System.out.println(Textstring);
		    
		    ResultSet rs = stmt.executeQuery(Textstring);
		    //template
		    response.setProcess("itemEdit Events");
		    //ResponseInit(reqItemSearch.getKeyword());
		    apiResponse.setResponse(response);
		   // itemlist.clear();
		   // itemlist.add(item);
		    apiItemEdit_Response.setResponse(response);

		    if ( rs.next() != false ) 
        	{	
        		if (rs.getInt("role")==1) {
        			response.setIsSuccess(true);
	        		response.setProcessDesc("successful");
	        		
        			
        		} else {
        			response.setIsSuccess(false);
	        		response.setProcessDesc("User not allowed!");
	        		
        			
        		}
        		
        	} else 
        	{
        		response.setIsSuccess(false);
        		response.setProcessDesc("Not found user assign!");
        		System.out.println(response.getProcessDesc());
        	}
		        
	       	
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		
		
		//  Edit item part 
		if (response.getIsSuccess()==true)  { // ตรวจสอบ security
			try {
				
				
				// get connection
				Statement stmt = ds.getStatement(dbName);
			    
				// Update Shortcode reference by barcode 
			   Textstring="update SmartQ.itemBarcode set shortcode=  '"+reqItemEdit.getShortCode()+"'  ";
			   Textstring=Textstring +" where barcode = '"+reqItemEdit.getBarCode()+"'";
			   			  		    
			   	System.out.println(Textstring);
			    int rs = stmt.executeUpdate(Textstring);
			    
			    //update filename & Remark in Item Table
			    
			    Textstring="update SmartQ.Item set filePath=  '"+reqItemEdit.getFilePath()+"',  ";
				Textstring=Textstring +" remark = '"+reqItemEdit.getRemark()+"'";
				Textstring=Textstring +" where code in (select code from SmartQ.itemBarcode where barcode='"+reqItemEdit.getBarCode()+"' )";
				   			  		    
				System.out.println(Textstring);
				int rs1 = stmt.executeUpdate(Textstring);
			    response.setProcess("itemEdit");
			    response.setIsSuccess(true);
			    response.setProcessDesc("Update Item");

			   
			} catch (SQLException e) {
				response.setProcess("itemEdit");
			    response.setIsSuccess(false);
			    response.setProcessDesc("Update Item");
				e.printStackTrace();
			} finally {
			    ds.close();
			}
		}
		
		apiItemEdit_Response.setResponse(response);
		return apiItemEdit_Response;
		
		
	}
		
	
}
