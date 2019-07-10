package lava.wt.template;

import lava.wt.pojo.RestModelDriver;
import lava.wt.pojo.RestResult;

public abstract  class  ModelDriverActionTemplate<M> extends ActionTemplate {

	
	

    protected abstract RestModelDriver<M> getModel();

    

    //protected abstract M readByPk(Object pk) throws Exception;
    
    //protected abstract Object  getPk(M m);

    

    //protected abstract int doCreate(M m) throws Exception;
    
    

    protected void onCreate(M m, RestResult re) throws Exception {
    }

    protected void onCreateError(M m, RestResult re, Exception ex) {
    }

    protected void onCreateSuccess(M m, RestResult re) throws Exception {
    }

    //protected abstract int doUpdate(M m) throws Exception;

    protected void onUpdate(M m, M post, RestResult re) throws Exception {
    }

    protected void onUpdateError(M m, M post, RestResult re, Exception ex) {
    }

    protected void onUpdateSuccess(M m, M post, RestResult re) throws Exception {
    }

    protected void onReadError(Object pk,RestResult re, Exception ex) {
    }
    
    //protected abstract int doDelete(M m) throws Exception;

    protected void onDelete(M m, RestResult re) throws Exception {
    }

    protected void onDeleteError(M m, RestResult re, Exception ex) {
    }

    protected void onDeleteSuccess(M m, RestResult re) throws Exception {
    }

    protected final RestResult put(M post)  {
    	RestModelDriver<M> md=getModel();
        RestResult result = new RestResult();

        try {
            
            onCreate(post, result);
            int re=md.put(post);
            result.setCode(re);
            onCreateSuccess(post, result);

        } catch (Exception ex) {
            result.setCode(RestResult.CODE_EXCEPTION);
            result.setMsg("insert exception:" + ex.getMessage());
            onCreateError(post, result, ex);
        }
        return result;
    }

    protected final RestResult post(Object pk,M post)  { 
    	RestModelDriver<M> md=getModel();
        RestResult result = new RestResult();
        M source = null;
        
        try {
        	
            source = md.get(pk);
            onUpdate(source, post, result);
            int re=md.post(source);
            result.setCode(re);
            onUpdateSuccess(source, post, result);

        } catch (Exception ex) {
            result.setCode(RestResult.CODE_EXCEPTION);
            result.setMsg("update exception:" + ex.getMessage());
            onUpdateError(source, post, result, ex);
        }
        return result;
    }

   

	

	protected final RestResult delete(M m)  {
		RestModelDriver<M> md=getModel();
        RestResult result = new RestResult();
        try {
         
            onDelete(m, result);
            int re=md.delete(m);
            result.setCode(re);
            onDeleteSuccess(m, result);

        } catch (Exception ex) {
            result.setCode(RestResult.CODE_EXCEPTION);
            result.setMsg("delete exception:" + ex.getMessage());
            onDeleteError(m, result, ex);
        }
        return result;
       
    }

	
    protected final RestResult get(Object pk)  {
        RestModelDriver<M> md=getModel();
        RestResult result = new RestResult();
        M m=null;
        try {
         
            m=md.get(pk);
            if(m!=null) {
            	result.setInfos(m);
            	result.setCode(RestResult.CODE_SUCCESS);
            }else {
            	result.setCode(RestResult.CODE_FAIL);
            }

        } catch (Exception ex) {
            result.setCode(RestResult.CODE_EXCEPTION);
            result.setMsg("delete exception:" + ex.getMessage());
            onReadError(pk,result, ex);
        }
        return result;
       
    }
    
    
	
	
	

    

    
    
    
   
    
    
    
    
    

}
