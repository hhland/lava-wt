package lava.wt.template;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Types;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.print.attribute.ResolutionSyntax;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lava.wt.common.WebCommon;






public abstract class LSActionTemplate extends ActionTemplate{

	protected abstract String getRequestURI() ;
	protected abstract String toString(Date date);
	protected abstract Map<String,String> getParameterMap();

    protected enum JsonAttr{
    	msg,total,rows
    }
    
    protected enum ParamAttr{
    	start,limit,orderby,orderdir,action;
    	
    	protected int intValue(Map<String ,String> parameter) {
    		return Integer.parseInt(parameter.get(this.name()));
    	}
    	
    	protected float floatValue(Map<String ,String> parameter) {
    		return Float.parseFloat(parameter.get(this.name()));
    	}
    	
    	
    	
        protected String value(Map<String ,String> parameter) {
        	return parameter.get(this.name());
    	}
        
        protected boolean isContains(Map<String ,String> parameter) {
        	return parameter.containsKey(this.name());
        }
    }
    

    protected int baseStart() {
        return 0;
    }

    protected int baseLimit() {
        return 100;
    }


    protected int paramStart() {
    	Map<String,String> parameter=getParameterMap();
        return ParamAttr.start.isContains(parameter)?ParamAttr.start.intValue(parameter):baseStart();
        
    }

    protected int paramLimit() {
    	Map<String,String> parameter=getParameterMap();
    	return ParamAttr.limit.isContains(parameter)?ParamAttr.limit.intValue(parameter):baseLimit();
        
    }

    

    protected abstract String title();

    
    
    
    public String help() {
   	 return "";
    }

    protected String doc_src(JsonObject head) {
    	 StringBuffer html=new StringBuffer("");
	        
	        String durl="";//this.getRequest().getRequestURI().replace("/easyui_grid", "/grid");
			StringBuffer datagrid=new StringBuffer(""),datagrid_js=new StringBuffer("");
			datagrid.append("<table id=\"table-datagrid\" class=\"easyui-datagrid\" data-options=\"url:'"+durl+"',fit:true,toolbar:'#div-toolbar',idField:'_id'\"> \n");
			datagrid.append("<thead data-options=\"frozen:true\"><tr> </thead><thead><tr> \n");
			for(Iterator<String> it=head.keySet().iterator();it.hasNext();) {
			//for(int i=1;i<=rsmd.getColumnCount();i++){
				String field=it.next();
				int ct=head.get(field).getAsInt();
				String editor="type:''validatebox'',options:'{'required:true '}'";
				if(isIn(ct, Types.BIGINT,Types.INTEGER,Types.SMALLINT,Types.TINYINT)){
					editor="type:''numberspinner'',options:'{'required:true,precision:0 '}'";
				}else if(isIn(ct, Types.FLOAT,Types.DOUBLE)){
					editor="type:''numberbox'',options:'{'required:true,precision:2 '}'";
				}else if(isIn(ct, Types.DATE)){
					editor="type:''datebox'',options:'{'required:true '}'";
				}else if(isIn(ct, Types.TIMESTAMP)){
					editor="type:''datetimebox'',options:'{'required:true '}'"; 
				}
				datagrid.append(MessageFormat.format("\t<th data-options=\"field:''{0}'',width:180,title:''{0}'',editor:'{' "+editor+" '}'\">"+field+"</th> \n",field));
			}
			datagrid.append("</thead></tr> \n");
			datagrid.append("</table>\n")
			.append("<div id=\"div-toolbar\"></div>")
			;
			for(Iterator<String> it=head.keySet().iterator();it.hasNext();) {
			//for(int i=1;i<=rsmd.getColumnCount();i++){
				String field=it.next();
				int ct=head.get(field).getAsInt();
				String editor="type:\"validatebox\",options:'{'required:true '}'";
				if(isIn(ct, Types.BIGINT,Types.INTEGER,Types.SMALLINT,Types.TINYINT)){
					editor="type:\"numberspinner\",options:'{'required:true,precision:0 '}'";
				}else if(isIn(ct, Types.FLOAT,Types.DOUBLE)){
					editor="type:\"numberbox\",options:'{'required:true,precision:2 '}'";
				}else if(isIn(ct, Types.DATE)){
					editor="type:\"datebox\",options:'{'required:true '}'";
				}else if(isIn(ct, Types.TIMESTAMP)){
					editor="type:\"datetimebox\",options:'{'required:true '}'";
				}
				datagrid_js.append(MessageFormat.format("var col_{0}='{'field:''{0}'',width:180,title:''{0}'',editor:'{' "+editor+" '}' '}'; \n",field));
			 
			}
			datagrid_js.append("$(\"#table-datagrid_crud\").datagrid({url:\""+durl+"\",fit:true,toolbar:[],idField:\"_id\",")
			.append("frozenColumns:[],columns:[[ \n")
			;
			for(Iterator<String> it=head.keySet().iterator();it.hasNext();) {
			//for(int i=1;i<=rsmd.getColumnCount();i++){
				String field=it.next();
				datagrid_js.append("col_"+field);
			    
			}
			datagrid_js.append(" ]]\n")
			.append("} ) ;  //$(\"#table-datagrid_crud\").datagrid")
			//datagrid_js.append("</table>\n")
			//.append("<div id=\"div-toolbar\"></div>")
			;
			
			html
			//.append("<html><body>")
			.append("<fieldset>easyui-datagrid html<pre>"+WebCommon.htmlWapper(datagrid.toString())+"</pre>")
			.append("<fieldset>easyui-datagrid js<pre>"+datagrid_js.toString()+"</pre>")
			//.append("</body></html>")
			;
	                //Session.close(connection, ps, null);
	        return html.toString();
    }
    
    public String doc_table(JsonObject head,JsonArray rows,int total){
    	 StringBuffer html=new StringBuffer("");
	        
	     String durl=this.getRequestURI();
	     
	     StringBuffer table=new StringBuffer("<table border='1' >"),headTr=new StringBuffer("<tr>");
	     
	     for(Iterator<String> it=head.keySet().iterator();it.hasNext();) {
	    	 String key=it.next();
	    	
	    	 headTr.append("<th>"+key+"</th>");
	     }
	     headTr.append("</tr>");
	     table.append(headTr);
	     for(int i=0;i<rows.size();i++) {
	    	 JsonObject row=rows.get(i).getAsJsonObject();
	    	 StringBuffer rowTr=new StringBuffer("<tr>");
	    	 for(Iterator<String> it=row.keySet().iterator();it.hasNext();) {
		    	 String key=it.next();
		    	 Object value=row.get(key)==null?"null":row.get(key);
		    	 rowTr.append("<td>"+value+"</td>");
		     }
	    	 rowTr.append("</tr>");
	    	 
	    	 table.append(rowTr);
	     }
	     
	     
	     table.append("</table>");
	     
	     html.append(table);
	     
	     return html.toString();
    }
    
    

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
    
    public abstract String xhr_grid() throws IOException;
    
    public abstract String xhr_total() throws IOException;
    
    public abstract String xhr_rows() throws IOException;
    
    public abstract String xhr_tree() throws IOException;
    
    
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
