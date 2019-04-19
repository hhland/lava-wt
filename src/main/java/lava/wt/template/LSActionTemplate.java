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






public abstract class LSActionTemplate extends ActionTemplate{

	
	protected abstract String toString(Date date);
	protected abstract Map<String,String> getParameterMap();

    
    

    protected int baseStart() {
        return 0;
    }

    protected int baseLimit() {
        return 100;
    }


    protected abstract int paramStart();

    protected abstract int paramLimit() ;

    

    protected abstract String title();

    
    
    
    

	public String doc_test() {
    	StringBuffer html=new StringBuffer();
    	html.append("<!DOCTYPE html>")
    	.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
    	.append("<title>"+thisClass().getSimpleName()+":test</title>")
    	.append("</head><body>")
    	.append("<form id='test_form' target='test_frame' action='invoke'><fieldset><legend>param</legend>")
    	;
    	
    	for(Class cls=thisClass();!Object.class.equals(cls);cls=cls.getSuperclass()) {
    	   Method[] paramMethods=cls.getDeclaredMethods();
    	   for(Method paramMethod :paramMethods) {
              if(!paramMethod.getName().startsWith("param"))continue;
              String name=paramMethod.getName().substring("param".length()).toLowerCase();
              
              String type="text";
              if(int.class.equals(paramMethod.getReturnType())) {
            	  type="number";
              }else if(Date.class.equals(paramMethod.getReturnType())) {
            	  type="date";
              }
              html.append(name+":<input type='"+type+"' name='"+name+"' /></hr>");
    	   }
    	}
        
    	
    	
    	html.append("</fieldset>");
    	
         html.append("action:<select name='fun' onchange=\"document.getElementById('test_form').submit()\">");
    	
    	Map<String,Method> openMethods= getOpenMethods();
    	
    	for(Iterator<String> it=openMethods.keySet().iterator();it.hasNext();) {
    		String name=it.next();
    		html.append("<option value='"+name+"' >"+name+"</option>");
    	}
    	html.append("</select>");
    	html.append("<button type='submit'>reload</button>");
    	
    	html.append("</form><iframe name='test_frame' style=\"width:100%;height:800px\" ></iframe>")
    	.append("</body></html>")
    	;
    	return html.toString();
    }
    
    public String doc_qunit() {
    	StringBuffer doc=new StringBuffer("");
    	doc.append("<!DOCTYPE html>\r\n" + 
    			"	<html>\r\n" + 
    			"\r\n" + 
    			"	<head>\r\n" + 
    			"\r\n" + 
    			"	    <title>QUnit Test Suite</title>\r\n" + 
    			"\r\n" + 
    			"		 <link href=\"https://cdn.bootcss.com/qunit/2.4.1/qunit.min.css\" rel=\"stylesheet\">\r\n" + 
    			"	   <script src=\"https://cdn.bootcss.com/qunit/2.4.1/qunit.min.js\"></script>\r\n" + 
    			"");
    	
    	
    	doc.append("</head>\r\n" + 
    			"\r\n" + 
    			"	<body>\r\n" + 
    			"\r\n" + 
    			"	    <h1 id=\"qunit-header\">QUnit Test Suite</h1>\r\n" + 
    			"\r\n" + 
    			"	    <h2 id=\"qunit-banner\"></h2>\r\n" + 
    			"\r\n" + 
    			"	    <div id=\"qunit-testrunner-toolbar\"></div>\r\n" + 
    			"\r\n" + 
    			"	    <h2 id=\"qunit-userAgent\"></h2>\r\n" + 
    			"\r\n" + 
    			"	    <ol id=\"qunit-tests\"></ol>\r\n" + 
    			"\r\n" + 
    			"	</body>\r\n" + 
    			"\r\n" + 
    			"	</html>");
    	
    	return doc.toString();
    }
    
    
    
    protected abstract ResultStruct xhr_total() throws IOException;
    
    protected abstract String xhr_rows(int start,int limit) throws IOException;
    
    
    
    
    protected  String invoke(String fun) {
    	
    	
    	String result=null;
    	Map<String,Method> openMethods= getOpenMethods();
    	//for(Class cls=this.thisClass();!Object.class.equals(cls);cls=cls.getSuperclass()) {
    	
    	Method mth= openMethods.get(fun);
    	    try {
				result=(String)mth.invoke(this);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				result=e.getMessage();
			}
    		 
    	return result;
    }

	
	
    
    protected final Map<String,Method> getOpenMethods(){
    	Map<String,Method> openMethods=new HashMap<String,Method>();
    	for(Class cls=thisClass();!Object.class.equals(cls);cls=cls.getSuperclass()) {
     	   Method[] methods=cls.getMethods();
     	   for(Method method :methods) {
     		  if(openMethods.containsKey(method.getName()))continue;
              if(!method.isDefault()&&!String.class.equals(method.getReturnType()))continue;
     	      openMethods.put(method.getName(), method);
     	   }
     	   
     	}
    	return openMethods;
    }
}
