package lava.wt.template;





public abstract  class  MDActionTemplate<M> extends ActionTemplate {

	
	

    

    

    protected abstract M readByPk(Object pk) throws Exception;
    
    protected abstract Object  getPk(M m);

    

    protected abstract int doCreate(M m) throws Exception;
    
    

    protected void onCreate(M m, ResultStruct re) throws Exception {
    }

    protected void onCreateError(M m, ResultStruct re, Exception ex) {
    }

    protected void onCreateSuccess(M m, ResultStruct re) throws Exception {
    }

    protected abstract int doUpdate(M m) throws Exception;

    protected void onUpdate(M m, M post, ResultStruct re) throws Exception {
    }

    protected void onUpdateError(M m, M post, ResultStruct re, Exception ex) {
    }

    protected void onUpdateSuccess(M m, M post, ResultStruct re) throws Exception {
    }

    protected void onReadError(Object pk,ResultStruct re, Exception ex) {
    }
    
    protected abstract int doDelete(M m) throws Exception;

    protected void onDelete(M m, ResultStruct re) throws Exception {
    }

    protected void onDeleteError(M m, ResultStruct re, Exception ex) {
    }

    protected void onDeleteSuccess(M m, ResultStruct re) throws Exception {
    }

    protected final ResultStruct create(M post)  {
    	
        ResultStruct result = new ResultStruct();

        try {
            
            onCreate(post, result);
            int re=doCreate(post);
            result.setCode(re);
            onCreateSuccess(post, result);

        } catch (Exception ex) {
            result.setCode(ResultStruct.CODE_EXCEPTION);
            result.setMsg("insert exception:" + ex.getMessage());
            onCreateError(post, result, ex);
        }
        return result;
    }

    protected final ResultStruct update(M post)  {
    	
        ResultStruct result = new ResultStruct();
        M source = null;
        Object id=getPk(post);
        try {
        	
            source = readByPk(id);
            onUpdate(source, post, result);
            int re=doUpdate(source);
            result.setCode(re);
            onUpdateSuccess(source, post, result);

        } catch (Exception ex) {
            result.setCode(ResultStruct.CODE_EXCEPTION);
            result.setMsg("update exception:" + ex.getMessage());
            onUpdateError(source, post, result, ex);
        }
        return result;
    }

   

	

	protected final ResultStruct delete(M m)  {
    	
        ResultStruct result = new ResultStruct();
        try {
         
            onDelete(m, result);
            int re= doDelete(m);
            result.setCode(re);
            onDeleteSuccess(m, result);

        } catch (Exception ex) {
            result.setCode(ResultStruct.CODE_EXCEPTION);
            result.setMsg("delete exception:" + ex.getMessage());
            onDeleteError(m, result, ex);
        }
        return result;
       
    }

	
    protected final ResultStruct read(Object pk)  {
    	
        ResultStruct result = new ResultStruct();
        M m=null;
        try {
         
            m=readByPk(pk);
            if(m!=null) {
            	result.setInfos(m);
            	result.setCode(ResultStruct.CODE_SUCCESS);
            }else {
            	result.setCode(ResultStruct.CODE_FAIL);
            }

        } catch (Exception ex) {
            result.setCode(ResultStruct.CODE_EXCEPTION);
            result.setMsg("delete exception:" + ex.getMessage());
            onReadError(pk,result, ex);
        }
        return result;
       
    }
    
    
	
	protected final ResultStruct save(M post) throws Exception {
    	ResultStruct re=isAllBlank(getPk(post))?create(post):update(post); 
    	return re;
    }

	

    

    
    
    
   
    
    
    
    
    

}
