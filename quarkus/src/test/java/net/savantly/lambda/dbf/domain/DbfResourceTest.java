package net.savantly.lambda.dbf.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DbfResourceTest {

	@Test
	public void testSimpleRename() {
		String dbfS3Key = "example/part/myfile.dbf";
		String keynameFind = "^(.*)\\.(dbf|DBF)$";
		String keynameReplace = "$1.csv";
		
		String actual = DbfResource.changeToCsvKey(dbfS3Key, keynameFind, keynameReplace);
		Assertions.assertEquals("example/part/myfile.csv", actual);
	}
	

	@Test
	public void testComplexRename() {
		String dbfS3Key = "gndxfer/123456/20210101/GNDITEM.dbf";
		String keynameFind = "^gndxfer/(\\d{5,8})/(\\d{8})/(\\w*)\\.(dbf|DBF)$";
		String keynameReplace = "gndxfer/$3.csv/dob=$2/pos=$1/1";
		
		String actual = DbfResource.changeToCsvKey(dbfS3Key, keynameFind, keynameReplace);
		Assertions.assertEquals("gndxfer/gnditem.csv/dob=20210101/pos=123456/1", actual);
	}
}
