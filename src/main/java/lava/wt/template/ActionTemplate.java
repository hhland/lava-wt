package lava.wt.template;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;





 abstract class  ActionTemplate {

	
	
	protected abstract Class<? extends ActionTemplate> thisClass();
	
	
	
	
	
	
	
	
	public static <T> boolean isEquals(T... objects) {
		for (int i = 0,j=1; j < objects.length; j++) {
			T objecti = objects[i],objectj=objects[j];
			if (objecti != null && !objecti.equals(objectj))
				return false;
			else if (objecti == null && objectj == null)
				continue;
		}
		return true;
	}
	
	
	protected boolean isAllBlank(Object...values) {
		   boolean ret=true;
		   
		   for(Object value: values) {
			   if(value!=null) {
				   if(value instanceof String) {
					   if(((String) value).trim().length()>0) {
						   ret=false;
						   break;
					   }
				   }else {
					   ret=false;
					   break;
				   }
			   }
		   }
		   
		   return ret;
	   }
	
	   
	   protected boolean isAnyBlank(Object...values) {
		   boolean ret=false;
		   
		   for(Object value: values) {
			   if(value==null) {
				   ret=true;
				   break;
			   }
			  
			   if(value instanceof String) {
				  if(((String) value).trim().length()==0) {
						   ret=true;
						   break;
			   }
			   }
			   
		   }
		   
		   return ret;
	   }
	
    public static String trimEnd(String value, String... suffixs) {
        
        String v = value.trim();
        for (String suffix : suffixs) {
            
            int index = 0;
            while (v.endsWith(suffix)) {
                index = v.lastIndexOf(suffix);
                v = v.substring(0, index);
            }

        }
        return v;
    }

    public static String trimStart(String value, String... prefixs) {
        
        String v = value.trim();
        for (String prefix : prefixs) {
            
            int index = 0;
            int len = prefix.length();
            while (v.startsWith(prefix)) {
                index = v.indexOf(prefix) + len;
                v = v.substring(index, v.length());
            }
        }
        return v;
    }

    public static String trim(String value, String... fixs) {
        String v = trimStart(value, fixs);
        v = trimEnd(v, fixs);
        return v;
    }

	
	
	
	protected static String firstCharToUpperCase(String name) {
		// TODO Auto-generated method stub
		return name.substring(0, 1).toUpperCase()+name.substring(1);
	}

	 
	
}
