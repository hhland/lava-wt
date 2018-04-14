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
import javax.servlet.http.HttpServletRequest;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lava.rt.common.LangCommon;
import lava.rt.common.ReflectCommon;
import lava.wt.common.TextCommon;
import lava.wt.instance.SimpleDateFormatInstance;





public abstract class LSActionTemplate implements ActionTemplate{


    protected enum JsonAttr{
    	msg,total,rows
    }
    
    protected enum ParamAttr{
    	start,limit,orderby,orderdir,action;
    	
    	protected int intValue(HttpServletRequest request) {
    		return Integer.parseInt(request.getParameter(this.name()));
    	}
    	
    	protected float floatValue(HttpServletRequest request) {
    		return Float.parseFloat(request.getParameter(this.name()));
    	}
    	
    	protected Date dateValue(HttpServletRequest request) throws ParseException {
    		return SimpleDateFormatInstance.tryParse(request.getParameter(this.name()));
    	}
    	
        protected String value(HttpServletRequest request) {
        	return request.getParameter(this.name());
    	}
        
        protected boolean isContains(HttpServletRequest request) {
        	return request.getParameterMap().containsKey(this.name());
        }
    }
    

    protected int baseStart() {
        return 0;
    }

    protected int baseLimit() {
        return 100;
    }


    protected int paramStart(HttpServletRequest request) {
        return ParamAttr.start.isContains(request)?ParamAttr.start.intValue(request):baseStart();
        
    }

    protected int paramLimit(HttpServletRequest request) {
    	return ParamAttr.limit.isContains(request)?ParamAttr.limit.intValue(request):baseLimit();
        
    }

    

    protected abstract String title();

    
    
    
    public String help() {
   	 return "";
    }

    protected String src_easyui(JsonObject head) {
    	 StringBuffer html=new StringBuffer("");
	        
	        String durl=this.getRequest().getRequestURI().replace("/easyui_grid", "/grid");
			StringBuffer datagrid=new StringBuffer(""),datagrid_js=new StringBuffer("");
			datagrid.append("<table id=\"table-datagrid\" class=\"easyui-datagrid\" data-options=\"url:'"+durl+"',fit:true,toolbar:'#div-toolbar',idField:'_id'\"> \n");
			datagrid.append("<thead data-options=\"frozen:true\"><tr> </thead><thead><tr> \n");
			for(Iterator<String> it=head.keySet().iterator();it.hasNext();) {
			//for(int i=1;i<=rsmd.getColumnCount();i++){
				String field=it.next();
				int ct=head.get(field).getAsInt();
				String editor="type:''validatebox'',options:'{'required:true '}'";
				if(LangCommon.isIn(ct, Types.BIGINT,Types.INTEGER,Types.SMALLINT,Types.TINYINT)){
					editor="type:''numberspinner'',options:'{'required:true,precision:0 '}'";
				}else if(LangCommon.isIn(ct, Types.FLOAT,Types.DOUBLE)){
					editor="type:''numberbox'',options:'{'required:true,precision:2 '}'";
				}else if(LangCommon.isIn(ct, Types.DATE)){
					editor="type:''datebox'',options:'{'required:true '}'";
				}else if(LangCommon.isIn(ct, Types.TIMESTAMP)){
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
				if(LangCommon.isIn(ct, Types.BIGINT,Types.INTEGER,Types.SMALLINT,Types.TINYINT)){
					editor="type:\"numberspinner\",options:'{'required:true,precision:0 '}'";
				}else if(LangCommon.isIn(ct, Types.FLOAT,Types.DOUBLE)){
					editor="type:\"numberbox\",options:'{'required:true,precision:2 '}'";
				}else if(LangCommon.isIn(ct, Types.DATE)){
					editor="type:\"datebox\",options:'{'required:true '}'";
				}else if(LangCommon.isIn(ct, Types.TIMESTAMP)){
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
			.append("<fieldset>easyui-datagrid html<pre>"+TextCommon.htmlWapper(datagrid.toString())+"</pre>")
			.append("<fieldset>easyui-datagrid js<pre>"+datagrid_js.toString()+"</pre>")
			//.append("</body></html>")
			;
	                //Session.close(connection, ps, null);
	        return html.toString();
    }
    
    public String html_table(JsonObject head,JsonArray rows,int total){
    	 StringBuffer html=new StringBuffer("");
	        
	     String durl=this.getRequest().getRequestURI();
	     
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
    
    public String html_test() {
    	StringBuffer html=new StringBuffer();
    	html.append("<!DOCTYPE html>")
    	.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
    	.append("<title>"+thisClass().getSimpleName()+":test</title>")
    	.append("</head><body>")
    	.append("<form id='test_form' target='test_frame' action='invoke'><fieldset><legend>µ÷ÊÔ²ÎÊý</legend>")
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

    	html.append("action:<select name='action' onchange=\"document.getElementById('test_form').submit()\">");
    	
    	Map<String,Method> openMethods= getOpenMethods();
    	
    	for(Iterator<String> it=openMethods.keySet().iterator();it.hasNext();) {
    		String name=it.next();
    		html.append("<option value='"+name+"' >"+name+"</option>");
    	}
    	html.append("</select>");
    	
    	html.append("</fieldset></form><iframe name='test_frame' style=\"width:100%;height:800px\" ></iframe>")
    	.append("</body></html>")
    	;
    	return html.toString();
    }
    
    public abstract String grid() throws IOException;
    
    public abstract String total() throws IOException;
    
    public abstract String rows() throws IOException;
    
    public abstract String tree() throws IOException;
    
    
    public  String invoke() {
    	HttpServletRequest request=getRequest();
    	String methodName=invokeMethod(request);
    	String result=null;
    	Map<String,Method> openMethods= getOpenMethods();
    	//for(Class cls=this.thisClass();!Object.class.equals(cls);cls=cls.getSuperclass()) {
    	
    	Method mth= openMethods.get(methodName);
    	    try {
				result=(String)mth.invoke(this);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				result=e.getMessage();
			}
    		 
    	return result;
    }

	
	protected String invokeMethod(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return ParamAttr.action.value(request);
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
