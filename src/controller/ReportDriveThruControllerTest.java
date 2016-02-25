package controller;

import static org.junit.Assert.*;

import org.junit.Test;

import bean.RP_Reqs_SearchQueBetweenDateBean;
import bean.RP_Resp_QueueMasterBean;

public class ReportDriveThruControllerTest {

	@Test
	public void test() {
		RP_Reqs_SearchQueBetweenDateBean search = new RP_Reqs_SearchQueBetweenDateBean();
		RP_Resp_QueueMasterBean dataList = new RP_Resp_QueueMasterBean();
		ReportDriveThruController rcl = new ReportDriveThruController();
		
		search.setBeginDate("2016-01-01");
		search.setEndDate("2016-01-10");
		
		dataList = rcl.QueueMaster("SmartQ", search);
		
		System.out.println("qID:"+dataList.getData().get(0).getqId());
		
	}

}
