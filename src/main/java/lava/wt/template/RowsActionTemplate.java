package lava.wt.template;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Types;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lava.wt.pojo.RestResult;






public abstract class RowsActionTemplate extends ActionTemplate{

	
	
	protected abstract Map<String,String> getParameterMap(HttpServletRequest request);

	
	


    protected abstract int paramStart(HttpServletRequest request);

    protected abstract int paramLimit(HttpServletRequest request) ;

    

   

    
    protected abstract RestResult<Integer> xhr_total(HttpServletRequest request) throws IOException;
    
    protected abstract RestResult<String> xhr_rows(HttpServletRequest request) throws IOException;
    
    
    
    
    
}
