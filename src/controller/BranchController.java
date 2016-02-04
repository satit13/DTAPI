package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bean.BranchBean;
import connect.QueueConnect;

public class BranchController {
	private final QueueConnect ds = QueueConnect.INSTANCE;
	private String Textstring;
	List<BranchBean> listBranch = new ArrayList<BranchBean>();
	
public List<BranchBean> getBranch(String dbName) {
		
		try {
			// get connection
		    Statement stmt = ds.getStatement(dbName);
		    
		    Textstring="select b.id,b.code,b.name from Branch as b";
		    
		   	System.out.println(Textstring);
		    
		    ResultSet rs = stmt.executeQuery(Textstring);
		    
		    
			// fetch all events from database
		    listBranch.clear();
//		    int roworder=1;
		    
		    BranchBean evt;
			while (rs.next()) {
				evt = new BranchBean();	
				
				evt.setId(rs.getInt("id"));
				evt.setCode(rs.getString("code"));
				evt.setName(rs.getString("name"));
								
				listBranch.add(evt);
				
//				roworder++;
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		    ds.close();
		}
		
		
		return listBranch;
		
	}
	

}
