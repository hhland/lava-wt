package lava.wt.instance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public enum SimpleDateFormatInstance {
	 YMD("yyyy-MM-dd"), ymd("yyyy/MM/dd"), dym("dd/MM/yyyy"), 
	 YMDHMSS("yyyy-MM-dd HH:mm:ss.S"), YMDHMS("yyyy-MM-dd HH:mm:ss");
	
	private SimpleDateFormat simpleDateFormat;
	
	private SimpleDateFormatInstance(String pattern) {
		// TODO Auto-generated constructor stub
	    simpleDateFormat=new SimpleDateFormat(pattern);
	}

	public SimpleDateFormat getSimpleDateFormat() {
		return simpleDateFormat;
	}
	
	public static Date tryParse(String dateString) throws ParseException {

        Date dateReturn = null;
        for (SimpleDateFormatInstance dateFormat : SimpleDateFormatInstance.values()) {
            try {
            	dateReturn=dateFormat.getSimpleDateFormat().parse(dateString);
            }finally {}
            if(dateReturn==null)throw new ParseException(dateString,0);
        }
        return dateReturn;
    }
}
