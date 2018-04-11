package lava.wt.template;

import java.io.IOException;


import java.sql.Types;
import java.text.MessageFormat;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import lava.rt.common.LangCommon;
import lava.rt.common.ReflectCommon;
import lava.wt.common.TextCommon;





public abstract class LSActionTemplate implements ActionTemplate{


    protected enum JsonAttr{
    	msg,total,rows
    }
    
    protected enum ParamAttr{
    	start,limit,orderby,orderdir
    }
    

    protected int baseStart() {
        return 0;
    }

    protected int baseLimit() {
        return 100;
    }


    protected int postStart(HttpServletRequest request) {
        if(request.getParameterMap().containsKey(ParamAttr.start.name())) {
        	return Integer.parseInt(request.getParameter(ParamAttr.start.name()));
        }
        return baseStart();
    }

    protected int postLimit(HttpServletRequest request) {
    	if(request.getParameterMap().containsKey(ParamAttr.limit.name())) {
        	return Integer.parseInt(request.getParameter(ParamAttr.limit.name()));
        }
        return baseLimit();
    }

    

    protected abstract String title();

    
    
    
    public String help() {
   	 return "";
    }

    protected String easyui_grid(JsonObject head) {
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
	    	 Object value=head.get(key);
	    	 headTr.append("<th>"+value+"</th>");
	     }
	     headTr.append("</tr>");
	     
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
    	return "";
    }
    
    public abstract String grid() throws IOException;
    
    public abstract String total() throws IOException;
    
    public abstract String rows() throws IOException;
    
    
}
