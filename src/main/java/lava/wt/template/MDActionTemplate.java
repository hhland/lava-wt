package lava.wt.template;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import lava.wt.common.ReflectCommon;
import lava.wt.common.TextCommon;
import lava.wt.instance.GsonInstance;

public abstract  class  MDActionTemplate<M> implements ActionTemplate {

	protected Gson gson=GsonInstance.simple.getGson();
	
	private M m;

    protected String pk;

    public void setPk(String pk) {
        this.pk = pk;
    }

    public boolean hasPk() {
        return TextCommon.isNullOrEmpty(pk);
    }

    protected abstract Class<M> modelClass();

    protected abstract M readByPk(String pk) throws Exception;

    protected  M newModel() throws Exception{
    	M m= ReflectCommon.newInstance(modelClass());
    	return m;
    };

    protected abstract int create(M m) throws Exception;

    protected void onCreate(M m, ResultStruct re) throws Exception {
    }

    protected void onCreateError(M m, ResultStruct re, Exception ex) {
    }

    protected void onCreateSuccess(M m, ResultStruct re) throws Exception {
    }

    protected abstract int update(M m) throws Exception;

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

    public String create()  {
    	HttpServletRequest request=getRequest();
        ResultStruct result = new ResultStruct();
        M m = null;

        try {
            m = newModel();

            ReflectCommon.loadMap(toMap(request.getParameterMap()),m.getClass(),m);
            onCreate(m, result);
            int re=create(m);
            onCreateSuccess(m, result);

        } catch (Exception ex) {
            result.setCode(ResultStruct.CODE_EXCEPTION);
            result.setMsg("insert exception:" + ex.getMessage());
            onCreateError(m, result, ex);
        }
        return gson.toJson(result);
    }

    public String update()  {
    	HttpServletRequest request=getRequest();
        ResultStruct result = new ResultStruct();
        M source = null, post = null;
        try {
        	 post = newModel();
            source = readByPk(pk);
            
            ReflectCommon.loadMap(toMap(request.getParameterMap()),m.getClass(),m);
            ReflectCommon.copyFieldByMethod(post, source);
            onUpdate(source, post, result);
            int re=update(source);
            onUpdateSuccess(m, post, result);

        } catch (Exception ex) {
            result.setCode(ResultStruct.CODE_EXCEPTION);
            result.setMsg("update exception:" + ex.getMessage());
            onUpdateError(m, post, result, ex);
        }
        return gson.toJson(result);
    }

    public String delete()  {
    	HttpServletRequest request=getRequest();
        ResultStruct result = new ResultStruct();
        M m = null;

        try {
            m = readByPk(pk);
            ReflectCommon.loadMap(toMap(request.getParameterMap()),m.getClass(),m);
            onDelete(m, result);
            delete(m);
            onDeleteSuccess(m, result);

        } catch (Exception ex) {
            result.setCode(ResultStruct.CODE_EXCEPTION);
            result.setMsg("delete exception:" + ex.getMessage());
            onDeleteError(m, result, ex);
        }
        return gson.toJson(result);
       
    }

    public String save() throws IOException {
        if (hasPk()) {
           return this.update();
        } else {

           return this.create();
        }
    }

    public String read() throws Exception {
        M m = hasPk() ? readByPk(pk) : newModel();
        return gson.toJson(m);
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
