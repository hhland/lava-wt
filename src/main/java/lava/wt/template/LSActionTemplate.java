package lava.wt.template;

import java.io.IOException;


import java.sql.Types;
import java.text.MessageFormat;
import java.util.Iterator;

import com.google.gson.JsonObject;

import lava.common.ReflectCommon;





public abstract class LSActionTemplate implements ActionTemplate{


    protected Integer start=0, limit=Integer.MAX_VALUE ;
    protected  String rowsName="rows",totalName="total",errTypeName="errtype",errMsgName="errmsg";

    protected void setStart(Integer start) {
        this.start = start;
    }

    protected void setLimit(Integer limit) {
        this.limit = limit;
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
				if(ReflectCommon.isIn(ct, Types.BIGINT,Types.INTEGER,Types.SMALLINT,Types.TINYINT)){
					editor="type:''numberspinner'',options:'{'required:true,precision:0 '}'";
				}else if(ReflectCommon.isIn(ct, Types.FLOAT,Types.DOUBLE)){
					editor="type:''numberbox'',options:'{'required:true,precision:2 '}'";
				}else if(ReflectCommon.isIn(ct, Types.DATE)){
					editor="type:''datebox'',options:'{'required:true '}'";
				}else if(ReflectCommon.isIn(ct, Types.TIMESTAMP)){
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
				if(ReflectCommon.isIn(ct, Types.BIGINT,Types.INTEGER,Types.SMALLINT,Types.TINYINT)){
					editor="type:\"numberspinner\",options:'{'required:true,precision:0 '}'";
				}else if(ReflectCommon.isIn(ct, Types.FLOAT,Types.DOUBLE)){
					editor="type:\"numberbox\",options:'{'required:true,precision:2 '}'";
				}else if(ReflectCommon.isIn(ct, Types.DATE)){
					editor="type:\"datebox\",options:'{'required:true '}'";
				}else if(ReflectCommon.isIn(ct, Types.TIMESTAMP)){
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
			.append("<fieldset>easyui-datagrid html<pre>"+datagrid.toString()+"</pre>")
			.append("<fieldset>easyui-datagrid js<pre>"+datagrid_js.toString()+"</pre>")
			//.append("</body></html>")
			;
	                //Session.close(connection, ps, null);
	        return html.toString();
    }
    
    public abstract String grid() throws IOException;
    
    public abstract String count() throws IOException;
    
    
}
