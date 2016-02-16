package controller;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import connect.QueueConnect;
import connect.SQLConn;

public class GenNewDocnoController {

	String vQuery ;
	String vQuerySub ;
	String vDocNo;
	String vPosNo;

	String vGenNewDocNo;
	int getID;
	int getInspectID;
	
	private final QueueConnect ds = QueueConnect.INSTANCE;
	private final SQLConn dsSql = SQLConn.INSTANCE;

	private java.text.SimpleDateFormat dtDoc= new java.text.SimpleDateFormat();
	private java.text.SimpleDateFormat dt= new java.text.SimpleDateFormat();

	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	
	public String genDocNo(int docType){
		int maxNo;
		
		int numMax;
		String header;
		String gDocNo;

		int nowYearInt;
		int nowYearInt1;
		
		String genNo;
		String genNo1;
		
		String  nowMonth;
		String  nowDay;
		
		String nowYear;
		String nowYear2;
		String nowYear3;
		
		genNo1 = "";
		
		try{
			Statement st = ds.getStatement("SmartQ");
			
			if (docType==0){
				vQuery = "select ifnull(right(MAX(docno),4),0)  as maxno from Queue where year(createdate) = year(CURRENT_DATE) and month(createdate) = month(CURRENT_DATE) and day(createdate) = day(CURRENT_DATE)";
			}
			if (docType==1){
				vQuery = "select ifnull(right(MAX(docno),4),0)  as maxno from Queue where year(createdate) = year(CURRENT_DATE) and month(createdate) = month(CURRENT_DATE) and day(createdate) = day(CURRENT_DATE)";
			}
			
			
			ResultSet rs = st.executeQuery(vQuery);
			while(rs.next()){
				maxNo = rs.getInt("maxno");
				numMax = maxNo+1;
				genNo = String.valueOf(numMax);
				if(genNo.length() == 1){
					genNo1 = "000"+genNo;
				}
				if(genNo.length() == 2){
					genNo1 = "00"+genNo;
				}
				if(genNo.length() == 3){
					genNo1 = "0"+genNo;
				}
				if(genNo.length() == 4){
					genNo1 = genNo;
				}
				
				System.out.println(genNo1);
			}
			
		    rs.close();
		    st.close();
		    
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ds.close();
		}
		
		
		Date date = new Date();
		
		System.out.println(dateFormat.format(date));
		
		nowYear = dateFormat.format(date).substring(0, 4);
		nowYearInt = Integer.parseInt(nowYear);
		nowYearInt1 = nowYearInt+543;
		nowYear2 = String.valueOf(nowYearInt1);
		nowYear3 = nowYear2.substring(2, 4);
		
		nowMonth = dateFormat.format(date).substring(5, 7);
		nowDay = dateFormat.format(date).substring(8, 10);
		
		header = "S01-QUE";

		gDocNo = header+nowYear3+nowMonth+nowDay+"-"+genNo1;
		System.out.println("Date : "+gDocNo);
		
		vDocNo = gDocNo;
		//vDocNo = "[{"+"\"genNewDocNo\":\""+gDocNo+"\"}]";
		
		System.out.println(vDocNo);
		
		
		return vDocNo;
	}
	
	
	public String genPOSNo(String MachineNo){
		int maxNo;
		
		int numMax;
		String header;
		String gDocNo;

		int nowYearInt;
		int nowYearInt1;
		
		String genNo;
		String genNo1;
		
		String  nowMonth;
		String  nowDay;
		
		String nowYear;
		String nowYear2;
		String nowYear3;
		
		genNo1 = "";
		
		try{
			Statement st = dsSql.getSqlStatement("POS");
			
			vQuery = "select right(MAX(docno),4) as maxno from dbo.BCARINVOICE where DocNo like '"+MachineNo+"%'";
			System.out.println("Search MaxNo :"+vQuery);
			ResultSet rs = st.executeQuery(vQuery);
			while(rs.next()){
				maxNo = rs.getInt("maxno");
				numMax = maxNo+1;
				genNo = String.valueOf(numMax);
				if(genNo.length() == 1){
					genNo1 = "000"+genNo;
				}
				if(genNo.length() == 2){
					genNo1 = "00"+genNo;
				}
				if(genNo.length() == 3){
					genNo1 = "0"+genNo;
				}
				if(genNo.length() == 4){
					genNo1 = genNo;
				}
				
				System.out.println("Bill GenNo :"+genNo1);
			}
			
		    rs.close();
		    st.close();
		    
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			dsSql.close();
		}
		
		
		Date date = new Date();
		
		//System.out.println(dateFormat.format(date));
		
		nowYear = dateFormat.format(date).substring(0, 4);
		nowYearInt = Integer.parseInt(nowYear);
		nowYearInt1 = nowYearInt+543;
		nowYear2 = String.valueOf(nowYearInt1);
		nowYear3 = nowYear2.substring(2, 4);
		
		nowMonth = dateFormat.format(date).substring(5, 7);
		nowDay = dateFormat.format(date).substring(8, 10);
		
		header = MachineNo;

		gDocNo = header+nowYear3+nowMonth+nowDay+"-"+genNo1;
		System.out.println("Date : "+gDocNo);
		
		vPosNo= gDocNo;
		//vDocNo = "[{"+"\"genNewDocNo\":\""+gDocNo+"\"}]";
		
		System.out.println("POSNO: "+vPosNo);
		
		
		return vPosNo;
	}
	
	
	
	public int genqId(){
		int qId;
		
		qId = 0;
		try{
			Statement st = ds.getStatement("SmartQ");
			
			vQuery = "select ifnull(MAX(qId),0)+1  as qId from Queue where year(createdate) = year(CURRENT_DATE) and month(createdate) = month(CURRENT_DATE) and day(createdate) = day(CURRENT_DATE)";

			ResultSet rs = st.executeQuery(vQuery);
			while(rs.next()){
				qId = rs.getInt("qId");

				System.out.println(qId);
			}
		    rs.close();
		    st.close();
		    
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			ds.close();
		}
		
		System.out.println(vDocNo);
		
		
		return qId;
	}
}
