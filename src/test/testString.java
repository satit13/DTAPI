package test;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Test;

public class testString {

	@Test
	public void test() {
			String Textstring ; 
		   Textstring="select 	a.code as itemCode,	a.name1 as itemName,b.unitCode ,b.shortCode,";
		    Textstring = Textstring + "'' as remark , '' as filePath, b.barcode,'' as itemCategory,c.price ";
		    Textstring = Textstring + "from Item a left outer join itemBarcode b on a.code = b.code " ;
		    Textstring = Textstring + "left outer join Price c on b.code=c.itemCode and b.unitcode=c.unitCode";
		    Textstring = Textstring + " where a.code like '%X%' ";
		    Textstring = Textstring + " or b.barcode like  '%X%'";
		    Textstring = Textstring + " or a.name1 like '%X%'";
		    Textstring = Textstring + " or b.shortcode like  '%X%' limit 5";
	
		    System.out.println(Textstring);
		  //  Assert.assertEquals(expected, actual);
		    
	}
	

}
