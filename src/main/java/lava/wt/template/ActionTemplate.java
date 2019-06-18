package lava.wt.template;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;





public abstract class  ActionTemplate {

	
	
	protected abstract Class<? extends ActionTemplate> thisClass();
	
	
	protected class ResultStruct<E> {
    	public static final int CODE_EXCEPTION = -1;
    	public static final int CODE_FAIL = 0;
    	public static final int CODE_SUCCESS = 1;
		private String msg;
		
		private int code;
		
		private E result;
		
		private Object[] infos;

		

		public E getResult() {
			return result;
		}

		public void setResult(E result) {
			this.result = result;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
		
		

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public boolean isError() {
			return this.code<=0;
		}

		public void setInfos(Object... infos) {
			// TODO Auto-generated method stub
			this.infos=infos;
		}

		public Object[] getInfos() {
			return infos;
		}
    	
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

	 protected Map<String,String> toMap(Map<String,String[]> rmap){
	    	Map<String,String> map=new HashMap<String,String>();
			for(Iterator<String> it=rmap.keySet().iterator();it.hasNext();) {
				String rkey=it.next();
				String[] rvalues=rmap.get(rkey);
				String value=null;
				if(rvalues==null) {
					
				}else if(rvalues.length==1) {
					value=rvalues[0];
				}else {
					String.join(",", rvalues);
				}
				map.put(rkey, value);
			}
			return map;
	    }
	
}
