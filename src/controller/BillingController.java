package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import connect.QueueConnect;
import connect.SQLConn;


import bean.CM_Resp_MemberListBean;
import bean.CT_Resp_ResponseBean;
import bean.IV_Reqs_BillingBean;
import bean.IV_Reqs_CouponBean;
import bean.IV_Reqs_CreditCardBean;
import bean.IV_Reqs_PrintSlipBean;
import bean.IV_Reqs_VerifyCouponBean;
import bean.IV_Resp_ARInvoiceBean;
import bean.IV_Resp_ARInvoiceSubBean;
import bean.IV_Resp_BankBean;
import bean.IV_Resp_BillingBean;
import bean.IV_Resp_CouponAmount;
import bean.IV_Resp_CreditTypeBean;
import bean.IV_Resp_InUpBillingBean;
import bean.IV_Resp_PrintSlipBean;
import bean.IV_Resp_PrintSlipSubBean;
import bean.IV_Resp_SearchBankBean;
import bean.IV_Resp_SearchCreditTypeBean;
import bean.IV_Resp_VerifyCouponBean;
import bean.LoginBean;
import bean.PK_Resp_GetDataQueue;
import bean.UserSearchBean;

public class BillingController {
	IV_Resp_InUpBillingBean bill = new IV_Resp_InUpBillingBean();
	IV_Resp_ARInvoiceBean header = new IV_Resp_ARInvoiceBean();
	List<IV_Resp_ARInvoiceSubBean> sub = new ArrayList<IV_Resp_ARInvoiceSubBean>();
	IV_Resp_BillingBean respBill = new IV_Resp_BillingBean();
	GenNewDocnoController genDoc = new GenNewDocnoController();
	List<IV_Reqs_CreditCardBean> crdCard = new ArrayList<IV_Reqs_CreditCardBean>();
	List<IV_Reqs_CouponBean> listCoupong = new ArrayList<IV_Reqs_CouponBean>();
	PK_Resp_GetDataQueue que = new PK_Resp_GetDataQueue();
	List<IV_Resp_ARInvoiceSubBean> listSub = new ArrayList<IV_Resp_ARInvoiceSubBean>();
	CT_Resp_ResponseBean response = new CT_Resp_ResponseBean();
	List<IV_Resp_BankBean> listBank = new ArrayList<IV_Resp_BankBean>();
	IV_Resp_SearchBankBean bank = new IV_Resp_SearchBankBean();
	IV_Resp_SearchCreditTypeBean creditType = new IV_Resp_SearchCreditTypeBean();
	List<IV_Resp_CreditTypeBean> listCreditType = new ArrayList<IV_Resp_CreditTypeBean>();
	
	CT_Resp_ResponseBean coupongRes = new CT_Resp_ResponseBean();
	
	IV_Resp_VerifyCouponBean verifyCou = new IV_Resp_VerifyCouponBean();
	
	IV_Resp_CouponAmount coupon = new IV_Resp_CouponAmount();
	
	getDataFromData data = new getDataFromData();
	
	CM_Resp_MemberListBean ar = new CM_Resp_MemberListBean();
	
	IV_Resp_PrintSlipBean printInv = new IV_Resp_PrintSlipBean();
	List<IV_Resp_PrintSlipSubBean> listInv = new ArrayList<IV_Resp_PrintSlipSubBean>();
	
	ExcecuteController excecute = new  ExcecuteController();
	SQLExecController sqlexec = new SQLExecController();
	
	private java.text.SimpleDateFormat dtDoc= new java.text.SimpleDateFormat();
	private java.text.SimpleDateFormat dt= new java.text.SimpleDateFormat();

	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	
	public String vQuery,vQuerySub,vQueryCreditCard,vQueryCoupong;
	String vPosNo;
	
	double sumCreditAmount=0;
	double sumCouponAmount=0;
	
	private SQLConn sqlDS = SQLConn.INSTANCE;
	private QueueConnect ds = QueueConnect.INSTANCE;
	
	double checkSumCreditAmount=0;
	double checkSumCouponAmount=0;
	double checkCashAmount=0;
	double checkRemainAmount=0;
	double checkChangeAmount=0;
	double checkRemain=0;
	
	
	public IV_Resp_BillingBean PostBilling(String dbName,IV_Reqs_BillingBean reqsBill){
		boolean isSuccess;
		double totalAmount;
		double beforeTaxAmount;
		double taxAmount;
		LoginBean userCode = new LoginBean();
		
		CT_Resp_ResponseBean validateCreditCard = new CT_Resp_ResponseBean();
		CT_Resp_ResponseBean verifyCoupong = new CT_Resp_ResponseBean();
		
		dtDoc.applyPattern("yyyy-MM-dd");
		dt.applyPattern("yyyy-MM-dd HH:mm:ss.S");
		Date dateNow = new Date();
		
		System.out.println("Confirm : "+reqsBill.getConfirm());
		
		PK_Resp_GetDataQueue qIdStatus = new PK_Resp_GetDataQueue();
		
		qIdStatus = data.searchQueue(reqsBill.getqId());
		
		totalAmount = data.searchQueueCheckOutAmount(reqsBill.getqId());
		
		
		int vCountToken = 0;
		
		try {
			Statement stmt = ds.getStatement("SmartConfig");
			
			vQuery = "select count(userUUID) as vCount from UserAccess where userUUID = '"+ reqsBill.getAccessToken() +"'";
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
		
		if (vCountToken>0) {
		
		if (qIdStatus.getStatus()==2){
		
		if (reqsBill.getConfirm()==0){
			totalAmount = data.searchQueueCheckOutAmount(reqsBill.getqId());	

			if (reqsBill.getCreditCard().size()!=0){
				
				validateCreditCard = data.validateCreditCard(reqsBill.getCreditCard());
				
				System.out.println("validate creditcard : "+validateCreditCard.getProcessDesc());
				
				for(int x=0;x<reqsBill.getCreditCard().size();x++){
					System.out.println("CreditCardNo : "+reqsBill.getCreditCard().get(x).getCardNo());
					
					IV_Reqs_CreditCardBean evt;
					
					evt = new IV_Reqs_CreditCardBean();
					
					evt.setCardNo(reqsBill.getCreditCard().get(x).getCardNo());
					
					evt.setAmount(reqsBill.getCreditCard().get(x).getAmount());
					evt.setBankCode(reqsBill.getCreditCard().get(x).getBankCode());
					evt.setChargeAmount(reqsBill.getCreditCard().get(x).getChargeAmount());
					evt.setConfirmNo(reqsBill.getCreditCard().get(x).getConfirmNo());
					evt.setCreditType(reqsBill.getCreditCard().get(x).getCreditType());
					
					crdCard.add(evt);
				}		

			}else{
				validateCreditCard.setIsSuccess(true);
				validateCreditCard.setProcess("validate creditcard");
				validateCreditCard.setProcessDesc("none validate creditcard");
			}			
			
			if(reqsBill.getCouponCode().size()!=0){
				verifyCoupong = data.verifyCoupong(reqsBill.getCouponCode());
				
				
				System.out.println("verify coupon :"+verifyCoupong.getProcessDesc());
				for(int z=0;z<reqsBill.getCouponCode().size();z++){
					
					IV_Reqs_CouponBean evt1;
					
					evt1 = new IV_Reqs_CouponBean();
					
					evt1.setCouponCode(reqsBill.getCouponCode().get(z).getCouponCode());
					evt1.setAmount(reqsBill.getCouponCode().get(z).getAmount());
					
					listCoupong.add(evt1);
				}	
				
			}else{
				verifyCoupong.setIsSuccess(true);
				verifyCoupong.setProcess("verify coupong");
				verifyCoupong.setProcessDesc("none verify coupong");
			}
			
			
			if(crdCard.size()!=0){
				
				for(int a=0;a<crdCard.size();a++){
				
					checkSumCreditAmount = checkSumCreditAmount+crdCard.get(a).getAmount();
				}
			}
		
			if(listCoupong.size()!=0){
				for(int b=0;b<listCoupong.size();b++){
					checkSumCouponAmount = checkSumCouponAmount+listCoupong.get(b).getAmount();
					System.out.println("CouponAmount : "+listCoupong.get(b).getAmount());
				}
			}

			checkCashAmount = reqsBill.getCash();
			checkRemain = ((totalAmount -checkSumCreditAmount)-checkSumCouponAmount);
			if (checkRemain-checkCashAmount <0){
				checkChangeAmount = -1*(checkRemain-checkCashAmount);
			}else{
				checkChangeAmount = 0;
			}
			checkRemainAmount = checkRemain-checkCashAmount+checkChangeAmount;
			
			System.out.println("total :"+totalAmount);
			System.out.println("cash :"+checkCashAmount);
			System.out.println("credit :"+checkSumCreditAmount);
			System.out.println("coupon :"+checkSumCouponAmount);
			System.out.println("remain :"+checkRemainAmount);
			
			System.out.println(validateCreditCard.getProcessDesc());
			System.out.println(data.verifyCoupong(reqsBill.getCouponCode()).getProcessDesc());
			
			if (validateCreditCard.getIsSuccess()==false){
				respBill.setResponse(validateCreditCard);
				respBill.setCashAmount(checkCashAmount);
				respBill.setChangeAmount(checkChangeAmount);
				respBill.setCoupongAmount(checkSumCouponAmount);
				respBill.setCreditAmount(0);
				respBill.setInvoiceNo("Can not save bill");
				respBill.setTotalAmount(totalAmount);
				//System.out.println("Yes0");
			}
			
			if (verifyCoupong.getIsSuccess()==false){
				respBill.setResponse(verifyCoupong);
				respBill.setCashAmount(checkCashAmount);
				respBill.setChangeAmount(checkChangeAmount);
				respBill.setCoupongAmount(0);
				respBill.setCreditAmount(checkSumCreditAmount);
				respBill.setInvoiceNo("Can not save bill");
				respBill.setTotalAmount(totalAmount);
				//System.out.println("Yes1");
			} 
			
			if (validateCreditCard.getIsSuccess()==true && verifyCoupong.getIsSuccess()==true && checkRemainAmount != 0){
				response.setIsSuccess(false);
				response.setProcess("Validate Bill Data");
				response.setProcessDesc("This payment is have remaining !!!!");
				respBill.setResponse(response);
				respBill.setCashAmount(checkCashAmount);
				respBill.setChangeAmount(checkChangeAmount);
				respBill.setCoupongAmount(checkSumCouponAmount);
				respBill.setCreditAmount(checkSumCreditAmount);
				respBill.setInvoiceNo("Can not save bill");
				respBill.setTotalAmount(totalAmount);
				//System.out.println("Yes2");
			}
			
			if (validateCreditCard.getIsSuccess()==true && verifyCoupong.getIsSuccess()==true && checkRemainAmount == 0){
				response.setIsSuccess(true);
				response.setProcess("Validate Bill Data");
				response.setProcessDesc("This payment is aleady for billing");
				respBill.setResponse(response);
				respBill.setCashAmount(checkCashAmount);
				respBill.setChangeAmount(checkChangeAmount);
				respBill.setCoupongAmount(checkSumCouponAmount);
				respBill.setCreditAmount(checkSumCreditAmount);
				respBill.setInvoiceNo("This queue aleady to bill");
				respBill.setTotalAmount(totalAmount);
				//System.out.println("Yes3");
			}
			
		}else{
			
				
				vPosNo = genDoc.genPOSNo("13");
				ar = data.searchCustomerName(reqsBill.getArCode());
				totalAmount = data.searchQueueCheckOutAmount(reqsBill.getqId());
				userCode = data.searchUserAccessToken(reqsBill.getAccessToken());
				que = data.searchQueue(reqsBill.getqId());

				if (reqsBill.getCreditCard().size()!=0){
					validateCreditCard = data.validateCreditCard(reqsBill.getCreditCard());
					System.out.println("�ӹǹ:"+reqsBill.getCreditCard().size());
					for(int x=0;x<reqsBill.getCreditCard().size();x++){
						
						IV_Reqs_CreditCardBean evt;
						
						evt = new IV_Reqs_CreditCardBean();
						
						evt.setCardNo(reqsBill.getCreditCard().get(x).getCardNo());
						
						evt.setAmount(reqsBill.getCreditCard().get(x).getAmount());
						evt.setBankCode(reqsBill.getCreditCard().get(x).getBankCode());
						evt.setChargeAmount(reqsBill.getCreditCard().get(x).getChargeAmount());
						evt.setConfirmNo(reqsBill.getCreditCard().get(x).getConfirmNo());
						evt.setCreditType(reqsBill.getCreditCard().get(x).getCreditType());
						
						checkSumCreditAmount = checkSumCreditAmount+reqsBill.getCreditCard().get(x).getAmount();
						crdCard.add(evt);
					}
				}else{
					validateCreditCard.setIsSuccess(true);
					checkSumCreditAmount = 0;
					IV_Reqs_CreditCardBean evt1;
					
					evt1 = new IV_Reqs_CreditCardBean();
					
					evt1.setCardNo("");
					
					evt1.setAmount(0);
					evt1.setBankCode("");
					evt1.setChargeAmount(0);
					evt1.setConfirmNo("");
					evt1.setCreditType("");
					
					crdCard.add(evt1);
				}
				
				if(reqsBill.getCouponCode().size()!=0){
					verifyCoupong = data.verifyCoupong(reqsBill.getCouponCode());
					
					for(int y=0;y<reqsBill.getCouponCode().size();y++){
						IV_Reqs_CouponBean evt;
						
						evt = new IV_Reqs_CouponBean();
						
						evt.setCouponCode(reqsBill.getCouponCode().get(y).getCouponCode());
						evt.setAmount(reqsBill.getCouponCode().get(y).getAmount());
						
						checkSumCouponAmount=checkSumCouponAmount+reqsBill.getCouponCode().get(y).getAmount();
						
						listCoupong.add(evt);
						
					}
				}else{
					verifyCoupong.setIsSuccess(true);
					checkSumCouponAmount=0;
					IV_Reqs_CouponBean evt1;
					
					evt1 = new IV_Reqs_CouponBean();
					
					evt1.setCouponCode("");
					evt1.setAmount(0);
					
					listCoupong.add(evt1);
				}
				
				
				//checkCashAmount = reqsBill.getCash();
				//checkRemainAmount = ((totalAmount -checkSumCreditAmount)-checkSumCouponAmount)-checkCashAmount;
				
				
				checkCashAmount = reqsBill.getCash();
				checkRemain = ((totalAmount -checkSumCreditAmount)-checkSumCouponAmount);
				if (checkRemain-checkCashAmount <0){
					checkChangeAmount = -1*(checkRemain-checkCashAmount);
				}else{
					checkChangeAmount = 0;
				}
				checkRemainAmount = checkRemain-checkCashAmount+checkChangeAmount;
				
				System.out.println(checkRemainAmount);
				
				beforeTaxAmount = (totalAmount*100)/107;
				taxAmount = totalAmount-beforeTaxAmount;
				
				System.out.println("save ok"+validateCreditCard.getIsSuccess());
				
				if (validateCreditCard.getIsSuccess()==true){
					System.out.println("savecardit ok");
					if (verifyCoupong.getIsSuccess()==true){
						System.out.println("savecoupong ok");
						if (checkRemainAmount ==0){
							System.out.println("saveremain ok");
						try{	
							
							Statement st = sqlDS.getSqlStatement(dbName);
							sumCreditAmount=0;
							sumCouponAmount=0;
							
							if(crdCard.size()!=0){
								for(int a=0;a<crdCard.size();a++){
								
								sumCreditAmount = sumCreditAmount+crdCard.get(a).getAmount();
								System.out.println("CreditCardAmount : "+crdCard.get(a).getAmount());
								}
							}
						
							if(listCoupong.size()!=0){
								for(int b=0;b<listCoupong.size();b++){
									sumCouponAmount = sumCouponAmount+listCoupong.get(b).getAmount();
									System.out.println("CouponAmount : "+listCoupong.get(b).getAmount());
								}
							}
							
							header.setDocNo(vPosNo);
							header.setDocDate(dateFormat.format(dateNow));
							header.setArCode(reqsBill.getArCode());
							header.setArName(ar.getArName());
							header.setTaxNo("");
							header.setTaxType(1);
							header.setArAddress(ar.getArAddress());
							header.setCashierCode(userCode.getEmployeeCode());
							header.setMachineNo("");
							header.setMachineCode("");
							header.setPosStatus(1);
							header.setCreditType(crdCard.get(0).getCreditType());
							header.setCreditNo(crdCard.get(0).getCardNo());
							header.setConfirmNo(crdCard.get(0).getConfirmNo());
							header.setChargeWord("");

							header.setCreditBaseAmount(crdCard.get(0).getAmount());
							header.setChargeAmount(crdCard.get(0).getChargeAmount());
							header.setGrandTotal(totalAmount);
							header.setChangeAmount(checkChangeAmount);
							header.setDepartCode("S01-00-00");
							header.setCreditDay(0);
							header.setDueDate("");
							header.setSaleCode(userCode.getEmployeeCode());
							header.setTaxRate(7);
							header.setIsConfirm(0);
							header.setMyDescription("DriveThru");
							header.setBillType(0);
							header.setBillGroup("");
							header.setRefDocNo(que.getDocNo());
							header.setSumOfItemAmount(totalAmount);
							header.setDiscountWord("");
							header.setDiscountAmount(0);
							header.setAfterDiscount(totalAmount);
							header.setBeforeTaxAmount(beforeTaxAmount);
							header.setTaxAmount(taxAmount);
							header.setTotalAmount(totalAmount);
							header.setZeroTaxAmount(0);
							header.setExceptTaxAmount(0);
							header.setSumCashAmount(reqsBill.getCash());
									
							header.setSumChqAmount(0);
							header.setSumCreditAmount(sumCreditAmount);
							header.setCoupongAmount(sumCouponAmount);
							header.setSumBankAmount(0);
							header.setDepositIncTax(0);
							header.setSumOfDeposit1(0);
							header.setSumOfDeposit2(0);
							header.setSumOfWTax(0);
							header.setNetDebtAmount(totalAmount);
							header.setHomeAmount(totalAmount);
							header.setOtherIncome(0);
							header.setOtherExpense(0);
							header.setExcessAmount1(0);
							header.setExcessAmount2(0);
							header.setBillBalance(totalAmount);
							header.setExchangeRate(1);
							header.setIsCancel(0);
							header.setIsCompleteSave(1);
							header.setIsPostGL(0);
							header.setPayBillStatus(0);
							header.setAllocateCode("");
							header.setProjectCode("");
							header.setIsConditionSend(0);
							header.setPayBillAmount(0);
							header.setSoRefNo("");
							header.setShiftCode("��ҧ�ѹ");

							
							//data.searchQueueCheckOutItem(reqsBill.getqId());
							
							IV_Resp_ARInvoiceSubBean listInv;
							double itemAmount=0;
							double netAmount=0;
							double qty=0;
							double price=0;
									
							
							sub.clear();
							if(data.searchQueueCheckOutItem(reqsBill.getqId()).size()!=0){
								for(int m =0;m<data.searchQueueCheckOutItem(reqsBill.getqId()).size();m++){
									listInv = new IV_Resp_ARInvoiceSubBean();
									qty = data.searchQueueCheckOutItem(reqsBill.getqId()).get(m).getQty();
									price = data.searchQueueCheckOutItem(reqsBill.getqId()).get(m).getPrice();
									itemAmount = qty*price;
									netAmount = (itemAmount*100)/107;
									System.out.println(data.searchQueueCheckOutItem(reqsBill.getqId()).get(m).getItemCode());
									listInv.setItemCode(data.searchQueueCheckOutItem(reqsBill.getqId()).get(m).getItemCode());
									listInv.setItemName(data.searchQueueCheckOutItem(reqsBill.getqId()).get(m).getItemName());
									listInv.setBarCode(data.searchQueueCheckOutItem(reqsBill.getqId()).get(m).getBarCode());
									listInv.setQty(data.searchQueueCheckOutItem(reqsBill.getqId()).get(m).getQty());
									listInv.setPrice(data.searchQueueCheckOutItem(reqsBill.getqId()).get(m).getPrice());
									listInv.setUnitCode(data.searchQueueCheckOutItem(reqsBill.getqId()).get(m).getUnitCode());
									listInv.setPackingRate1(data.searchQueueCheckOutItem(reqsBill.getqId()).get(m).getPackingRate1());
									listInv.setDiscountAmount(0);
									listInv.setWhCode("S1-B");
									listInv.setShelfCode("-");
									listInv.setMachineCode("50011-91-00547");
									listInv.setMachineNo("13");
									listInv.setShiftNo(0);
									listInv.setShiftCode("��ҧ�ѹ");
									listInv.setAmount(itemAmount);
									listInv.setNetAmount(netAmount);
									listInv.setHomeAmount(netAmount);
									sub.add(listInv);
								}
							}
							
							bill.setBillHeader(header);
							bill.setBillSub(sub);
							
							vQuery = "set dateformat ymd  insert into dbo.BCARInvoice_Test(docNo,docDate,taxNo,taxType,arCode,arName,arAddress,cashierCode,"
							+"machineNo,machineCode,posStatus,billTime,creditType,creditNo,cofirmNo,chargeWord,creditBaseAmount,"
							+"chargeAmount,grandTotal,coupongAmount,changeAmount,departCode,creditDay,dueDate,saleCode,taxRate,"
							+"isConfirm,myDescription,billType,billGroup,refDocNo,sumOfItemAmount,discountWord,discountAmount,"
							+"afterDiscount,beforeTaxAmount,taxAmount,totalAmount,zeroTaxAmount,exceptTaxAmount,sumCashAmount,"
							+"sumChqAmount,sumCreditAmount,sumBankAmount,depositIncTax,sumOfDeposit1,sumOfDeposit2,sumOfWTax,"
							+"netDebtAmount,homeAmount,otherIncome,otherExpense,excessAmount1,excessAmount2,billBalance,exchangeRate,"
							+"isCancel,isCompleteSave,isPostGL,payBillStatus,allocateCode,projectCode,creatorCode,isConditionSend,"
							+"payBillAmount,sORefNo,shiftCode,createdatetime) values( "
							+" '"+bill.getBillHeader().getDocNo()+"','"+bill.getBillHeader().getDocDate()+"','"+bill.getBillHeader().getTaxNo()+"',"
							+" "+bill.getBillHeader().getTaxType()+",'"+bill.getBillHeader().getArCode()+"','"+bill.getBillHeader().getArName()+"',"
							+" '"+bill.getBillHeader().getArAddress()+"','"+bill.getBillHeader().getCashierCode()+"','"+bill.getBillHeader().getMachineNo()+"',"
							+" '"+bill.getBillHeader().getMachineCode()+"',"+bill.getBillHeader().getPosStatus()+","+"cast(datepart(hour,GETDATE()) as varchar(2))+':'+ cast(datepart(minute,GETDATE())as varchar(2))"+","
							+" '"+bill.getBillHeader().getCreditType()+"','"+bill.getBillHeader().getCreditNo()+"','"+bill.getBillHeader().getConfirmNo()+"',"
							+" '"+bill.getBillHeader().getChargeWord()+"',"+bill.getBillHeader().getCreditBaseAmount()+","+bill.getBillHeader().getChargeAmount()+","
							+" "+bill.getBillHeader().getGrandTotal()+","+bill.getBillHeader().getCoupongAmount()+","+bill.getBillHeader().getChangeAmount()+","
							+" '"+bill.getBillHeader().getDepartCode()+"',"+bill.getBillHeader().getCreditDay()+",'"+bill.getBillHeader().getDueDate()+"',"
							+" '"+bill.getBillHeader().getSaleCode()+"',"+bill.getBillHeader().getTaxRate()+","+bill.getBillHeader().getIsConfirm()+","
							+" '"+bill.getBillHeader().getMyDescription()+"',"+bill.getBillHeader().getBillType()+",'"+bill.getBillHeader().getBillGroup()+"',"
							+" '"+bill.getBillHeader().getRefDocNo()+"',"+bill.getBillHeader().getSumOfItemAmount()+",'"+bill.getBillHeader().getDiscountWord()+"',"
							+" "+bill.getBillHeader().getDiscountAmount()+","+bill.getBillHeader().getAfterDiscount()+","+bill.getBillHeader().getBeforeTaxAmount()+","
							+" "+bill.getBillHeader().getTaxAmount()+","+bill.getBillHeader().getTotalAmount()+","+bill.getBillHeader().getZeroTaxAmount()+","
							+" "+bill.getBillHeader().getExceptTaxAmount()+","+bill.getBillHeader().getSumCashAmount()+","+bill.getBillHeader().getSumChqAmount()+","
							+" "+bill.getBillHeader().getSumCreditAmount()+","+bill.getBillHeader().getSumBankAmount()+","+bill.getBillHeader().getDepositIncTax()+","
							+" "+bill.getBillHeader().getSumOfDeposit1()+","+bill.getBillHeader().getSumOfDeposit2()+","+bill.getBillHeader().getSumOfWTax()+","
							+" "+bill.getBillHeader().getNetDebtAmount()+","+bill.getBillHeader().getHomeAmount()+","+bill.getBillHeader().getOtherIncome()+","
							+" "+bill.getBillHeader().getOtherExpense()+","+bill.getBillHeader().getExcessAmount1()+","+bill.getBillHeader().getExcessAmount2()+","
							+" "+bill.getBillHeader().getBillBalance()+","+bill.getBillHeader().getExchangeRate()+","+bill.getBillHeader().getIsCancel()+","
							+" "+bill.getBillHeader().getIsCompleteSave()+","+bill.getBillHeader().getIsPostGL()+","+bill.getBillHeader().getPayBillAmount()+","
							+" '"+bill.getBillHeader().getAllocateCode()+"','"+bill.getBillHeader().getProjectCode()+"','"+userCode.getEmployeeCode()+"',"
							+" "+bill.getBillHeader().getIsConditionSend()+","+bill.getBillHeader().getPayBillAmount()+",'"+bill.getBillHeader().getSoRefNo()+"','"
							+" "+bill.getBillHeader().getShiftCode()+"',getdate() "
							+" )";
							System.out.println(vQuery);
							isSuccess = sqlexec.executeSql(dbName, vQuery);
							
							
							System.out.println("InvoiceSub :"+sub.size());
							
							for(int i=0;i<sub.size();i++){
								
								vQuerySub=	"set dateformat ymd  insert into dbo.BCARInvoiceSub_Test(docNo,taxNo,taxType,itemCode,docDate,arCode,departCode,"
											+"saleCode,myDescription,itemName,whCode,shelfCode,cnQty,qty,price,discountWord,"
											+"discountAmount,amount,netAmount,homeAmount,sumOfCost,balanceAmount,unitCode,"
											+"soRefNo,poRefNo,stockType,lineNumber,refLineNumber,isCancel,allocateCode,projectCode,"
											+"exchangeRate,barCode,machineNo,machineCode,billTime,cashierCode,shiftNo,posStatus,"+
											"isConditionSend,taxRate,packingRate1) "
								+" values( '"+bill.getBillHeader().getDocNo()+"','"+bill.getBillHeader().getTaxNo()+"',"+bill.getBillHeader().getTaxType()+","
								+" '"+bill.getBillSub().get(i).getItemCode()+"','"+bill.getBillHeader().getDocDate()+"','"+bill.getBillHeader().getArCode()+"',"
								+" '"+bill.getBillHeader().getDepartCode()+"','"+bill.getBillHeader().getSaleCode()+"','"+bill.getBillSub().get(i).getMyDescription()+"',"
								+" '"+bill.getBillSub().get(i).getItemName()+"','"+bill.getBillSub().get(i).getWhCode()+"','"+bill.getBillSub().get(i).getShelfCode()+"',"
								+" "+bill.getBillSub().get(i).getQty()+","+bill.getBillSub().get(i).getQty()+","+bill.getBillSub().get(i).getPrice()+","
								+" '"+bill.getBillSub().get(i).getDiscountWord()+"',"+bill.getBillSub().get(i).getDiscountAmount()+","+bill.getBillSub().get(i).getAmount()+","
								+" "+bill.getBillSub().get(i).getNetAmount()+","+bill.getBillSub().get(i).getHomeAmount()+","+bill.getBillSub().get(i).getSumOfCost()+","
								+" "+bill.getBillSub().get(i).getBalanceAmount()+",'"+bill.getBillSub().get(i).getUnitCode()+"','"+bill.getBillSub().get(i).getSoRefNo()+"',"
								+" '"+bill.getBillSub().get(i).getPoRefNo()+"',"+bill.getBillSub().get(i).getStockType()+","+i+",0,"+bill.getBillSub().get(i).getIsCancel()+","
								+" '"+bill.getBillSub().get(i).getAllocateCode()+"','"+bill.getBillSub().get(i).getProjectCode()+"',"+bill.getBillSub().get(i).getExchangeRate()+","
								+" '"+bill.getBillSub().get(i).getBarCode()+"','"+bill.getBillSub().get(i).getMachineNo()+"','"+bill.getBillSub().get(i).getMachineCode()+"',"
								+" "+"cast(datepart(hour,GETDATE()) as varchar(2))+':'+ cast(datepart(minute,GETDATE())as varchar(2))"+",'"+bill.getBillSub().get(i).getCashierCode()+"',"
								+" '"+bill.getBillSub().get(i).getShiftNo()+"',"+bill.getBillHeader().getPosStatus()+","+bill.getBillHeader().getIsConditionSend()+","
								+" "+bill.getBillHeader().getTaxRate()+","+bill.getBillSub().get(i).getPackingRate1()
								+")";
								System.out.println(vQuerySub);
								isSuccess = sqlexec.executeSql(dbName, vQuerySub);
								
							}
							
							if(crdCard.size()!=0 && crdCard.get(0).getCardNo()!=""){
								for(int a=0;a<crdCard.size();a++){
								
									vQueryCreditCard = "set dateformat ymd insert into dbo.BCCreditCard_Test(BankCode,CreditCardNo,DocNo,ArCode,ReceiveDate,DueDate,Status,SaveFrom,Amount,MyDescription,ExchangeRate,CreditType,ConfirmNo,ChargeAmount,CreatorCode,CreateDateTime) values( "
									+" '"+crdCard.get(a).getBankCode()+"','"+crdCard.get(a).getCardNo()+"','"+bill.getBillHeader().getDocNo()+"','"+bill.getBillHeader().getArCode()+"',"
									+" '"+bill.getBillHeader().getDocDate()+"','"+bill.getBillHeader().getDocDate()+"',"+"0,1"+","+crdCard.get(a).getAmount()+",'"+"���˹����ҹ"+"',"
									+" "+"1.0000000000"+",'"+crdCard.get(a).getCreditType()+"','"+crdCard.get(a).getConfirmNo()+"',"+crdCard.get(a).getChargeAmount()+",'"+userCode.getEmployeeCode()+"',getdate()"
									+" )";
									System.out.println(vQueryCreditCard);
									isSuccess = sqlexec.executeSql(dbName, vQueryCreditCard);
								}
							}
						
							if(listCoupong.size()!=0 && listCoupong.get(0).getCouponCode()!= ""){
								for(int b=0;b<listCoupong.size();b++){
									vQueryCoupong="set dateformat ymd insert into dbo.bccouponreceive_Test(COUPONCODE,COUPONTYPE,COUPONNO,TOCOUPONNO,COUPONCOUNT,DOCNO,BOOK,COUPONVAL,LINENUMBER,CREATORCODE,CREATEDATETIME) values( "
									+" '"+listCoupong.get(b).getCouponCode()+"',1,'"+listCoupong.get(b).getCouponCode()+"','"+listCoupong.get(b).getCouponCode()+"',1,'"+bill.getBillHeader().getDocNo()+"',"
									+" '"+listCoupong.get(b).getCouponCode()+"',"+listCoupong.get(b).getAmount()+","+b+",'"+userCode.getEmployeeCode()+"',getdate()"
									+" )";
									System.out.println(vQueryCoupong);
									isSuccess = sqlexec.executeSql(dbName, vQueryCoupong);
								}
							}
							
							vQuery="update Queue set status = 3 where docNo ='"+que.getDocNo()+"'";
							isSuccess= excecute.execute("SmartQ",vQuery);
							
							respBill.setInvoiceNo(bill.getBillHeader().getDocNo());
							respBill.setTotalAmount(reqsBill.getDebtAmount());
							respBill.setCashAmount(reqsBill.getCash());
							respBill.setChangeAmount(checkChangeAmount);
							respBill.setCoupongAmount(0);
							respBill.setCreditAmount(sumCreditAmount);
							response.setIsSuccess(true);
							response.setProcess("Save bill");
							response.setProcessDesc("Successfully");
							
							respBill.setResponse(response);
									
						}catch(SQLException e){
							e.printStackTrace();
							System.out.println("Remain No");
							respBill.setInvoiceNo("Can not save bill");
							respBill.setTotalAmount(reqsBill.getDebtAmount());
							respBill.setCashAmount(reqsBill.getCash());
							respBill.setChangeAmount(checkChangeAmount);
							respBill.setCoupongAmount(0);
							respBill.setCreditAmount(sumCreditAmount);
							response.setIsSuccess(false);
							response.setProcess("Save bill");
							response.setProcessDesc(e.getLocalizedMessage());
							
							respBill.setResponse(response);
						}finally{
							ds.close();
							sqlDS.close();
						}
						
						}else{
							
							System.out.println("Remain No");
							respBill.setInvoiceNo("Can not save bill");
							respBill.setTotalAmount(reqsBill.getDebtAmount());
							respBill.setCashAmount(reqsBill.getCash());
							respBill.setChangeAmount(checkChangeAmount);
							respBill.setCoupongAmount(0);
							respBill.setCreditAmount(sumCreditAmount);
							response.setIsSuccess(false);
							response.setProcess("Save bill");
							response.setProcessDesc("This payment have remain <> 0");
							
							respBill.setResponse(response);
						}
					}else{
						respBill.setInvoiceNo("Can not save bill");
						respBill.setTotalAmount(reqsBill.getDebtAmount());
						respBill.setCashAmount(reqsBill.getCash());
						respBill.setChangeAmount(checkChangeAmount);
						respBill.setCoupongAmount(0);
						respBill.setCreditAmount(sumCreditAmount);
						respBill.setResponse(verifyCoupong);
					}
				}else{
					respBill.setInvoiceNo("Can not save bill");
					respBill.setTotalAmount(reqsBill.getDebtAmount());
					respBill.setCashAmount(reqsBill.getCash());
					respBill.setChangeAmount(checkChangeAmount);
					respBill.setCoupongAmount(0);
					respBill.setCreditAmount(0);
					respBill.setResponse(verifyCoupong);
				}	
				
			}
		}else{
			response.setIsSuccess(false);
			response.setProcess("Validate Bill Data");
			response.setProcessDesc("This queue is bill aready");
			respBill.setInvoiceNo("Can not save bill");
			respBill.setTotalAmount(reqsBill.getDebtAmount());
			respBill.setCashAmount(reqsBill.getCash());
			respBill.setChangeAmount(checkChangeAmount);
			respBill.setCoupongAmount(0);
			respBill.setCreditAmount(0);
			respBill.setResponse(response);
		}
		}else{
			response.setIsSuccess(false);
			response.setProcess("Validate Bill Data");
			response.setProcessDesc("Access is deny !!!!!");
			respBill.setInvoiceNo("Can not save bill");
			respBill.setTotalAmount(reqsBill.getDebtAmount());
			respBill.setCashAmount(reqsBill.getCash());
			respBill.setChangeAmount(checkChangeAmount);
			respBill.setCoupongAmount(0);
			respBill.setCreditAmount(0);
			respBill.setResponse(response);
		}
		System.out.println(respBill.getResponse().getProcessDesc());
		return respBill;
	}
	
	
	public IV_Resp_SearchBankBean searchBank(String dbName,UserSearchBean search){
		int countrow=0;
		
		try{
			Statement st = sqlDS.getSqlStatement(dbName);
			IV_Resp_BankBean evt;
			IV_Resp_BankBean evt1;
			
			if (search.getKeyword()== null || search.getKeyword()==""){
				vQuery = "select distinct code,name from dbo.bcbank order by code";
			}else{
				vQuery = "select distinct code,name from dbo.bcbank where code like '%"+search.getKeyword()+"%' or name like '%"+search.getKeyword()+"%'order by code";
			}
			System.out.println(vQuery);
			ResultSet rs = st.executeQuery(vQuery);
			
			listBank.clear();
			while(rs.next()){
				countrow++;
				
				evt = new IV_Resp_BankBean();
				evt.setBankCode(rs.getString("code"));
				evt.setBankName(rs.getString("name"));
				listBank.add(evt);
			}
			
			response.setIsSuccess(true);
			response.setProcess("Search Bank");
			response.setProcessDesc("Successfully");
			bank.setResponse(response);
			bank.setBank(listBank);
			
			if (countrow==0){
				evt1 = new IV_Resp_BankBean();
				listBank.add(evt1);
				
				response.setIsSuccess(true);
				response.setProcess("Search Bank");
				response.setProcessDesc("No list bank");
				bank.setResponse(response);
			}
			
		    rs.close();
		    st.close();
		    
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			sqlDS.close();
		}
		
		return bank;
	}
	
	public IV_Resp_SearchCreditTypeBean searchCreditType(String dbName,UserSearchBean search){
		int countrow=0;
		try{
			Statement st = sqlDS.getSqlStatement(dbName);
			IV_Resp_CreditTypeBean evt;
			IV_Resp_CreditTypeBean evt1;
			
			if (search.getKeyword()== null || search.getKeyword()==""){
				vQuery = "select distinct code,name from dbo.BPSCreditType order by code";
			}else{
				vQuery = "select distinct code,name from dbo.BPSCreditType where code like '%"+search.getKeyword()+"%' or name like '%"+search.getKeyword()+"%'order by code";
			}
			System.out.println(vQuery);
			ResultSet rs = st.executeQuery(vQuery);
			
			listCreditType.clear();
			while(rs.next()){
				countrow++;
				
				System.out.println("CreditType : "+rs.getString("code"));
				
				evt = new IV_Resp_CreditTypeBean();
				evt.setCode(rs.getString("code"));
				evt.setName(rs.getString("name"));
				listCreditType.add(evt);
			}
			
			response.setIsSuccess(true);
			response.setProcess("Search CreditType");
			response.setProcessDesc("Successfully");
			creditType.setResponse(response);
			creditType.setCrdType(listCreditType);
			
			System.out.println("Number Count:"+countrow);
			if (countrow==0){
				evt1 = new IV_Resp_CreditTypeBean();
				listCreditType.add(evt1);
				
				System.out.println("CreditType : False");
				
				response.setIsSuccess(true);
				response.setProcess("Search CreditType");
				response.setProcessDesc("No list CreditType");
				creditType.setResponse(response);
			}
			
		    rs.close();
		    st.close();
		    
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			sqlDS.close();
		}
		
		return creditType;
	}
	
	
	public IV_Resp_VerifyCouponBean verifyCoupong(String dbName,IV_Reqs_VerifyCouponBean coupong){
		int counterr=0;
		int checkexist=0;
		int checkused=0;
			

		if (coupong.getCouponCode()!="" && coupong.getCouponCode()!= null){
				try{
					Statement st = sqlDS.getSqlStatement(dbName);
					vQuery = "set dateformat dmy select code,couponval from dbo.bccoupon_test where code = '"+coupong.getCouponCode()+"' and code not in (select couponcode from dbo.bccouponreceive_test where docno in (select docno from dbo.bcarinvoice_test where iscancel = 0)) and cast(rtrim(day(getdate()))+'/'+rtrim(month(getdate()))+'/'+rtrim(year(getdate())) as datetime) <= expiredate";
					ResultSet rs = st.executeQuery(vQuery);
					
					System.out.println("Verify Coupong :"+vQuery);
					while(rs.next()){
						checkexist++;
						coupon.setAmount(rs.getDouble("couponval"));
					}
					
					vQuery = "set dateformat dmy select code,couponval from dbo.bccoupon_test where code = '"+coupong.getCouponCode()+"' and code in (select couponcode from dbo.bccouponreceive_test )";
					ResultSet rs1 = st.executeQuery(vQuery);
					
					System.out.println("Verify Coupong :"+vQuery);
					while(rs1.next()){
						checkused++;
						coupon.setAmount(rs1.getDouble("couponval"));
					}
					rs.close();
				    rs1.close();
				    st.close();
						
				}catch(SQLException e){
					e.printStackTrace();
					counterr++;
				}finally{
					sqlDS.close();
				}
				
		}else{
			counterr++;
			//error Code
		}
		
		
		if(counterr==0 && checkexist !=0){
			coupongRes.setIsSuccess(true);
			coupongRes.setProcess("verifyCoupong");
			coupongRes.setProcessDesc("Coupong is pass");
		}else{
			if (counterr==0 && checkexist ==0){
				if ((checkused != 0)){
					coupon.setAmount(0);
					coupongRes.setIsSuccess(false);
					coupongRes.setProcess("verifyCoupong");
					coupongRes.setProcessDesc("Coupong is used");
				}else{
					coupon.setAmount(0);
					coupongRes.setIsSuccess(false);
					coupongRes.setProcess("verifyCoupong");
					coupongRes.setProcessDesc("Coupong is not exist or is expire or value invalid");
				}
			}else{
				coupon.setAmount(0);
				coupongRes.setIsSuccess(false);
				coupongRes.setProcess("verifyCoupong");
				coupongRes.setProcessDesc("Coupong data error");
			}
		}
		
		verifyCou.setResponse(coupongRes);
		verifyCou.setCoupon(coupon);
		
		return verifyCou;
	}
	
	public IV_Resp_PrintSlipBean printSlip(String dbName,IV_Reqs_PrintSlipBean req){
		Date  billDocDate ;
		String getBillDate;
		
		int vCountToken = 0;
		double pointBal = 0;
		String pointDesc; 
		
		try {
			Statement stmt = ds.getStatement("SmartConfig");
			
			vQuery = "select count(userUUID) as vCount from UserAccess where userUUID = '"+ req.getAccessToken() +"'";
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
			
			pointBal = data.calcPointInvoice(req.getInvoiceNo());
			
			
			pointDesc = "�͡��ù����ӹǹ��� :"+ pointBal+" ���";
		
		try {
			Statement st = sqlDS.getSqlStatement(dbName);
			
			vQuery = "exec dbo.USP_NP_InvoicePrintDetails "+req.getType()+", '"+req.getInvoiceNo()+"','"+req.getArCode()+"'";
			System.out.println(vQuery);
			ResultSet rs = st.executeQuery(vQuery);
			
			listInv.clear();
			while(rs.next()){
				System.out.println(req.getAccessToken());
				System.out.println(rs.getString("companyname"));
				
				billDocDate = rs.getDate("docdate");
				
				//getBillDate = billDocDate.getDay()+"/"+billDocDate.getMonth()+"/"+billDocDate.getYear();
				
				printInv.setDocNo(rs.getString("docno"));
				printInv.setDocDate(billDocDate.toString());
				printInv.setCompanyName(rs.getString("companyname"));
				printInv.setTaxId(rs.getString("taxid"));
				printInv.setPosId(rs.getString("posid"));
				printInv.setCashier(rs.getString("cashiercode"));
				printInv.setSaleCode(rs.getString("salecode"));
				printInv.setBillTime(rs.getString("billtime"));
				printInv.setTotalAmount(rs.getDouble("totalamount"));
				printInv.setTax(rs.getInt("taxrate"));
				printInv.setTaxAmount(rs.getDouble("taxamount"));
				printInv.setCashAmount(rs.getDouble("sumcashamount"));
				printInv.setCreditAmount(rs.getDouble("sumcreditamount"));
				printInv.setChange(rs.getDouble("changeamount"));
				printInv.setGreeting1(rs.getString("greeting1"));
				printInv.setGreeting2(rs.getString("greeting2"));
				printInv.setGreeting3(rs.getString("greeting3"));
				printInv.setGreeting4(rs.getString("greeting4"));
				printInv.setGreeting5(rs.getString("greeting5"));
				printInv.setRemark("");
				printInv.setPromotionDesc1(pointDesc);
				printInv.setPromotionDesc2(rs.getString("promotionDesc2"));
				printInv.setPromotionDesc3(rs.getString("promotionDesc3"));
				printInv.setPromotionDesc4(rs.getString("promotionDesc4"));
				printInv.setPromotionDesc5(rs.getString("promotionDesc5"));
				printInv.setPoint(rs.getInt("point"));

				
				
				
					IV_Resp_PrintSlipSubBean evt;
					evt = new IV_Resp_PrintSlipSubBean();
					evt.setItemCode(rs.getString("itemcode"));
					evt.setItemName(rs.getString("itemname"));
					evt.setQty(rs.getInt("qty"));
					evt.setPrice(rs.getDouble("price"));
					evt.setAmount(rs.getDouble("amount"));
					evt.setUnitCode(rs.getString("unitcode"));
					
					System.out.println(rs.getString("itemcode"));
					listInv.add(evt);
					
				}
				response.setIsSuccess(true);
				response.setProcess("Search Print Slip");
				response.setProcessDesc("Successfully");
				printInv.setItem(listInv);
				printInv.setResponse(response);
				
				System.out.println("CashierCode : "+printInv.getCashier());
		
			    rs.close();
			    st.close();
			
		} catch (SQLException e) {
			response.setIsSuccess(false);
			response.setProcess("Search Print Slip");
			response.setProcessDesc(e.getMessage());
			
			printInv.setItem(listInv);
			printInv.setResponse(response);
		}finally{
			sqlDS.close();
		}
		
		} else{
			response.setIsSuccess(false);
			response.setProcess("Search Print Slip");
			response.setProcessDesc("Error : Access i deny !!!!!");
			
			printInv.setItem(listInv);
			printInv.setResponse(response);
		}
		
		return printInv;
		
	}
}
