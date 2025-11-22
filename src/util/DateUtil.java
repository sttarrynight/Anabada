package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static java.sql.Date convertSqlDate(Date dt) {
		
		java.sql.Date dt2 = new java.sql.Date(dt.getTime());
		return dt2;
	}
	
	public static java.sql.Date convertSqlDate(String dateStr) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = null;
		
		try {
			dt = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		java.sql.Date result = new java.sql.Date(dt.getTime());
		return result;
	}
}
