package controller;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import bean.LoginBean;
import bean.LoginResponseBean;
import bean.CT_Resp_ResponseBean;
import bean.UserBean;
import bean.UserRegisterBean;
import bean.UserSearchBean;
import bean.UserSearchResponseBean;
import connect.QueueConnect;


public class UserController {
	private final QueueConnect ds = QueueConnect.INSTANCE;
	private String Textstring;
	
	CT_Resp_ResponseBean response = new CT_Resp_ResponseBean();
	UserBean user = new UserBean();
	List<UserBean> userList = new ArrayList<UserBean>();
	LoginResponseBean loginResponse = new LoginResponseBean();
	UserSearchResponseBean userSearchResponse = new UserSearchResponseBean();
	ExcecuteController excecute = new  ExcecuteController();
	
	private boolean isSuccess;
		public boolean isSuccess() {
			return isSuccess;
		}
		public void setSuccess(boolean isSuccess) {
			this.isSuccess = isSuccess;
		}
		
	private void loginResponseTemplage(String keyword) {
		response.setIsSuccess(false);
 		response.setProcessDesc("Not found :" +keyword);
 		
 		user.setId(-1);
		    user.setCode(null);
		    user.setName(null);
		    user.setPassword(null);
		    user.setPicturePath(null);
		    user.setEmail(null);
		    user.setRole(-1);
		    user.setActiveStatus(-1);
		    user.setIsConfirm(-1);
		    user.setCreatorCode(null);
		    user.setCreatedateTime(null);
		    user.setLasteditorCode(null);
		    user.setLasteditdateTime(null);
	}
	
	
	public LoginResponseBean login(String dbName,LoginBean login) {
		java.text.SimpleDateFormat dt= new java.text.SimpleDateFormat();
		dt.applyPattern("yyyy-MM-dd HH:mm:ss.S");
		Date dateNow = new Date();
		
		try {
			
			
			// get connection
			Statement stmt = ds.getStatement(dbName);
		    
		    Textstring="select * from User where code='"+login.getEmployeeCode()+"'";
		    
		   	System.out.println(Textstring);
		    
		    ResultSet rs = stmt.executeQuery(Textstring);
		    

		   
		    response.setProcess("login");
		        if ( rs.next() != false ) 
		        	{	
		        		response.setIsSuccess(true);
		        		response.setProcessDesc("successful");
		        		
		        		user.setId(rs.getInt("id"));
		    		    user.setCode(rs.getString("code"));
		    		    user.setName(rs.getString("name"));
		    		    user.setPassword(rs.getString("password"));
		    		    user.setPicturePath(rs.getString("picturePath"));
		    		    user.setEmail(rs.getString("email"));
		    		    user.setRole(rs.getInt("role"));
		    		    user.setActiveStatus(rs.getInt("activeStatus"));
		    		    user.setIsConfirm(rs.getInt("isConfirm"));
		    		    user.setCreatorCode(rs.getString("creatorCode"));
		    		    user.setCreatedateTime(rs.getString("createdateTime"));
		    		    user.setLasteditorCode(rs.getString("lasteditorCode"));
		    		    user.setLasteditdateTime(rs.getString("lasteditdateTime"));
		        		
		        		String uuid = UUID.randomUUID().toString(); 
		        		loginResponse.setAccessToken(uuid);
		    		    loginResponse.setAccessDatetime(dt.format(dateNow));
		        	} else 
		        	{
		        		loginResponseTemplage(login.getEmployeeCode());
		        	}
		        
	       	
		    loginResponse.setResponse(response); 
		    loginResponse.setUser(user); 
		    
		    System.out.println("uuid = " + loginResponse.getAccessToken() + " : "+ loginResponse.getResponse().getProcessDesc()+ "-"+loginResponse.getAccessDatetime() ); 

		    rs.close();
		    stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
		    ds.close();
		}
		
		// เก็บ log UserAccess
		Textstring="insert UserAccess(userId,userCode,userUUID,dateTimeStamp)"
				+" select '" +loginResponse.getUser().getId()+"','"+loginResponse.getUser().getCode()
					+"','"+loginResponse.getAccessToken()+"','"+loginResponse.getAccessDatetime()+"'";
		
						
		 System.out.println(Textstring);
		
		// return true;
		 try {
			isSuccess= excecute.execute(dbName,Textstring);
		} catch (Exception e) {
			isSuccess=false;
			loginResponseTemplage(login.getEmployeeCode());
		}
		
		
		return loginResponse;
		
	}
	
	//User Search
	public UserSearchResponseBean userSearch(String dbName,UserSearchBean userSearch) {
		java.text.SimpleDateFormat dt= new java.text.SimpleDateFormat();
		dt.applyPattern("yyyy-MM-dd HH:mm:ss.S");
		Date dateNow = new Date();
		
		try {
			// get connection
			Statement stmt = ds.getStatement(dbName);
		    
		    Textstring="select u.code,u.role from  User as u inner join UserAccess as ua on u.code=ua.userCode and ua.userUUID='"+userSearch.getAccessToken()+"'"
		    		+" where u.role=1 order by ua.dateTimeStamp DESC LIMIT 1" ;
		    
		   	System.out.println(Textstring);
		    
		    ResultSet rs = stmt.executeQuery(Textstring);
		    
		    //template
		    response.setProcess("userSearch");
		    loginResponseTemplage(userSearch.getKeyword());
		    userSearchResponse.setResponse(response);
		    userList.clear();
		    userList.add(user);
		    userSearchResponse.setUser(userList);
		   
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
				Statement stmt = ds.getStatement(dbName);
			    
			    Textstring="select * from User where code like'%"+userSearch.getKeyword()+"%' or name like '%"+userSearch.getKeyword()+"%'";
			    
			   	System.out.println(Textstring);
			    
			    ResultSet rs = stmt.executeQuery(Textstring);
			    
			    
			    response.setProcess("userSearch");
			    
			    int roworder=0;
			    
			    userList.clear();
			   	while (rs.next()) {
			   		roworder++;
			   		
			   		user = new UserBean();	
			   		
					response.setIsSuccess(true);
	        		response.setProcessDesc("successful");
	        		
	        		user.setId(rs.getInt("id"));
	    		    user.setCode(rs.getString("code"));
	    		    user.setName(rs.getString("name"));
	    		    user.setPassword(rs.getString("password"));
	    		    user.setPicturePath(rs.getString("picturePath"));
	    		    user.setEmail(rs.getString("email"));
	    		    user.setRole(rs.getInt("role"));
	    		    user.setActiveStatus(rs.getInt("activeStatus"));
	    		    user.setIsConfirm(rs.getInt("isConfirm"));
	    		    user.setCreatorCode(rs.getString("creatorCode"));
	    		    user.setCreatedateTime(rs.getString("createdateTime"));
	    		    user.setLasteditorCode(rs.getString("lasteditorCode"));
	    		    user.setLasteditdateTime(rs.getString("lasteditdateTime"));
	        		
	    		    userList.add(user);
					
	    		    System.out.println(user.getCode());
				}
			   	if (roworder==0) { //ค้นหาไม่พบ
			   		loginResponseTemplage(userSearch.getKeyword());
			   		userList.clear();
			   		userList.add(user);
			   	}  
		       	userSearchResponse.setResponse(response);
			    userSearchResponse.setUser(userList);
			    
			    rs.close();
			    stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
			    ds.close();
			}
		}

		return userSearchResponse;
		
	}
	
	//register method
	public CT_Resp_ResponseBean register(String dbName,UserRegisterBean userRegister) {
		java.text.SimpleDateFormat dt= new java.text.SimpleDateFormat();
		dt.applyPattern("yyyy-MM-dd HH:mm:ss.S");
		Date dateNow = new Date();
		
		try {
			
			
			// get connection
			Statement stmt = ds.getStatement(dbName);
		    
		    Textstring="select u.code,u.role from  User as u inner join UserAccess as ua on u.code=ua.userCode and ua.userUUID='"+userRegister.getAccessToken()+"'"
		    		+" where u.role=1 order by ua.dateTimeStamp DESC LIMIT 1" ;
		    
		   	System.out.println(Textstring);
		    
		    ResultSet rs = stmt.executeQuery(Textstring);
		    

		   
		    response.setProcess("register");
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
		        	}
		        
			    rs.close();
			    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		try {
						
			// get connection
			Statement stmt = ds.getStatement(dbName);
		    
		    Textstring="select * from  User as u where code='"+userRegister.getUser().getCode()+"'";
		    
		   	System.out.println(Textstring);
		    
		    ResultSet rs = stmt.executeQuery(Textstring);
		    

		   
		    response.setProcess("register");
		        if ( rs.next() != false ) 
		        	{	
		        		response.setIsSuccess(false);
			        	response.setProcessDesc("duplicate user!");
		        	}
		        
			    rs.close();
			    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		if (response.getIsSuccess()==true) {
			// register
			Textstring="insert User(code, name, password, picturePath, email, role,"
							+"creatorCode, createdateTime)" //, lasteditorCode, lasteditdateTime
					+" select '" +userRegister.getUser().getCode()+"','"+userRegister.getUser().getName()+"','"+userRegister.getUser().getPassword()+"','"
					+userRegister.getUser().getPicturePath()+"','"+userRegister.getUser().getEmail()+"',"+userRegister.getUser().getRole()
					+",'"+userRegister.getUser().getCreatorCode()+"','"
					+dt.format(dateNow)+"'";
			
			System.out.println(Textstring);
			
			//return true;
			 try {
				isSuccess= excecute.execute(dbName,Textstring);
				if (isSuccess==false) {
					response.setIsSuccess(false);
		    		response.setProcessDesc("register error on save!");
				};
			} catch (Exception e) {
//				System.out.println("yyy");
				isSuccess=false;
				response.setIsSuccess(false);
	    		response.setProcessDesc("register error on save!");
			}
		}
		
		return response;
		
	}
	
	//user edit
	public CT_Resp_ResponseBean userEdit(String dbName,UserRegisterBean userRegister) {
		java.text.SimpleDateFormat dt= new java.text.SimpleDateFormat();
		dt.applyPattern("yyyy-MM-dd HH:mm:ss.S");
		Date dateNow = new Date();
		
		try {
			
			
			// get connection
			Statement stmt = ds.getStatement(dbName);
		    
		    Textstring="select u.code,u.role from  User as u inner join UserAccess as ua on u.code=ua.userCode and ua.userUUID='"+userRegister.getAccessToken()+"'"
		    		+" where u.role=1 order by ua.dateTimeStamp DESC LIMIT 1" ;
		    
		   	System.out.println(Textstring);
		    
		    ResultSet rs = stmt.executeQuery(Textstring);
		    

		   
		    response.setProcess("edit");
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
		        	}
		        
			    rs.close();
			    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		try {
						
			// get connection
			Statement stmt = ds.getStatement(dbName);
		    
		    Textstring="select * from  User as u where id="+userRegister.getUser().getId();
		    
		   	System.out.println(Textstring);
		    
		    ResultSet rs = stmt.executeQuery(Textstring);
		    

		   
		    response.setProcess("register");
		        if ( rs.next() == false ) 
		        	{	
		        		response.setIsSuccess(false);
			        	response.setProcessDesc("not found user for edit!");
		        	}
		        
			    rs.close();
			    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		if (response.getIsSuccess()==true) {
			// register
			Textstring="update User set code='"+userRegister.getUser().getCode()+"',name='"+userRegister.getUser().getName()
						+"', password='"+userRegister.getUser().getPassword()+"',picturePath='"+userRegister.getUser().getPicturePath()
						+"', email='"+userRegister.getUser().getEmail()+"',role="+userRegister.getUser().getRole()
						+",activeStatus="+userRegister.getUser().getActiveStatus()+", isConfirm="+userRegister.getUser().getIsConfirm()
						+", lasteditorCode='"+userRegister.getUser().getLasteditorCode()+"', lasteditdateTime='"+dt.format(dateNow)+"'"
						+" where id="+userRegister.getUser().getId() ;
					
			
							
			 System.out.println(Textstring);
			
			// return true;
			 try {
				isSuccess= excecute.execute(dbName,Textstring);
			} catch (Exception e) {
				isSuccess=false;
				response.setIsSuccess(false);
	    		response.setProcessDesc("edit error on save!");
			}
		}
		
		return response;
		
	}
		
}