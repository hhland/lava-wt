package lava.wt.template;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;






public abstract  class  MDActionTemplate<M> extends ActionTemplate {

	
	

    

    

    protected abstract M readByPk(Object pk) throws Exception;
    
    protected abstract Object  getPk(M m);

    

    protected abstract int _create(M m) throws Exception;
    
    

    protected void onCreate(M m, ResultStruct re) throws Exception {
    }

    protected void onCreateError(M m, ResultStruct re, Exception ex) {
    }

    protected void onCreateSuccess(M m, ResultStruct re) throws Exception {
    }

    protected abstract int _update(M m) throws Exception;

    protected void onUpdate(M m, M post, ResultStruct re) throws Exception {
    }

    protected void onUpdateError(M m, M post, ResultStruct re, Exception ex) {
    }

    protected void onUpdateSuccess(M m, M post, ResultStruct re) throws Exception {
    }

    protected abstract int delete(M m) throws Exception;

    protected void onDelete(M m, ResultStruct re) throws Exception {
    }

    protected void onDeleteError(M m, ResultStruct re, Exception ex) {
    }

    protected void onDeleteSuccess(M m, ResultStruct re) throws Exception {
    }

    public String create(M post)  {
    	
        ResultStruct result = new ResultStruct();

        try {
            
            onCreate(post, result);
            int re=_create(post);
            onCreateSuccess(post, result);

        } catch (Exception ex) {
            result.setCode(ResultStruct.CODE_EXCEPTION);
            result.setMsg("insert exception:" + ex.getMessage());
            onCreateError(post, result, ex);
        }
        return toJson(result);
    }

    public String update(M post)  {
    	
        ResultStruct result = new ResultStruct();
        M source = null;
        Object id=getPk(post);
        try {
        	
            source = readByPk(id);
            onUpdate(source, post, result);
            int re=_update(source);
            onUpdateSuccess(source, post, result);

        } catch (Exception ex) {
            result.setCode(ResultStruct.CODE_EXCEPTION);
            result.setMsg("update exception:" + ex.getMessage());
            onUpdateError(source, post, result, ex);
        }
        return toJson(result);
    }

   

	

	public String delete(String id)  {
    	
        ResultStruct result = new ResultStruct();
        M m = null;

        try {
            m = readByPk(id);
            
            onDelete(m, result);
            delete(m);
            onDeleteSuccess(m, result);

        } catch (Exception ex) {
            result.setCode(ResultStruct.CODE_EXCEPTION);
            result.setMsg("delete exception:" + ex.getMessage());
            onDeleteError(m, result, ex);
        }
        return toJson(result);
       
    }

    public String save(M post) throws Exception {
        String re=""; 
    	try {
        	re= create(post);
         }catch(Exception ex) {
        	re= update( post);
         }
    	return re;
    }

    public String read(String id) throws Exception {
        M m = readByPk(id);
        return toJson(m);
    }

    

    
    protected class ResultStruct {
    	public static final int CODE_EXCEPTION = -1;
		private String msg;
		
		private int code;

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
    	
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
