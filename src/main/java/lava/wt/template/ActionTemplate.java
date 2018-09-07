package lava.wt.template;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.google.gson.Gson;



public abstract class  ActionTemplate {

	
	
	protected abstract Class<? extends ActionTemplate> thisClass();
	
	
	
	public static <T> boolean isIn(T obj1, T... objs) {
		for (T obj : objs) {
			if (isEquals(obj1, obj)) {
				return true;
			}
		}
		return false;
	}
	
	
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
	
	
	public static boolean isNullOrEmpty( String... strings) {
		for (String string :strings) {
			if(string!=null&&string.trim().length()>0)
				return false;
		}
	    return true;
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

	
	protected static int close(Object... objs) {
		int re=0;
		
		for(Object obj :objs) {
			
			try {
				obj.getClass().getMethod("close").invoke(obj);
				re++;
			}catch(Exception ex) {}
			
		}
		
		return re;
	}	
	
	protected static String firstCharToUpperCase(String name) {
		// TODO Auto-generated method stub
		return name.substring(0, 1).toUpperCase()+name.substring(1);
	}

	private static Gson GSON=new Gson();
    protected String toJson(Object obj) {
    	return GSON.toJson(obj);
    };
	
	
}
