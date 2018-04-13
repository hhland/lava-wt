package lava.wt.template;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import lava.rt.instance.MethodInstance;
import lava.wt.instance.SimpleDateFormatInstance;
import lava.wt.common.TextCommon;





public abstract  class LSSqlActionTemplate extends LSActionTemplate{

	  
	    
	    
	   public abstract Connection  createConnection() throws SQLException;
	   
	   public void onConnectionEnd(Connection connection) {
		    MethodInstance.close.invoke(connection);
	   }
	   
	    protected abstract String viewName();
	    
	    
	    protected abstract String createPageSql(String dataSql,int start, int limit);
	    
	     protected String columns() {
	        return "*";
	    }

	    protected String baseCondition() {
	        return "";
	    }

	    protected String[] baseOrderby() {
	        return new String[]{};
	    }
	    
	    protected String[] postOrderby(HttpServletRequest request) {
	        String[] orderby = new String[]{};
	        
	        String	order = request.getParameter(ParamAttr.orderby.name() );
	        if (!TextCommon.isNullOrEmpty(order) ) {
	            orderby=order.split(",");
	        }
	        return orderby;
	    }
	    
	    protected String baseOrderDir() {
	    	return "asc";
	    }
	    
	    
	    protected String postOrderDir(HttpServletRequest request) {
	    	String	dir = request.getParameter(ParamAttr.orderdir.name());
	    	if(TextCommon.isNullOrEmpty(dir))dir=baseOrderDir();
	    	return dir;
	    }
	    

	    protected String postCondition(HttpServletRequest request) {
	    	Map<String,String[]> parameters= request.getParameterMap();
	        String condition = "";
	        for (Iterator<String> it = this.getRequest().getParameterMap().keySet().iterator(); it.hasNext();) {
	            String name = it.next();
	            String value = parameters.get(name)[0];
	            if (TextCommon.isNullOrEmpty(value)) {
	                continue;
	            }
	            if (name.startsWith("qc_like_")) {
	                String colName = name.substring("qc_like_".length(), name.length());
	                condition += MessageFormat.format(" and {0} like '%{1}%'", colName, value);
	            } else if (name.startsWith("qc_eq_")) {
	                String colName = name.substring("qc_eq_".length(), name.length());
	                condition += MessageFormat.format(" and {0} = '{1}'", colName, value);
	            } else if (name.startsWith("qc_in_")) {
	                String colName = name.substring("qc_in_".length(), name.length());
	                condition += MessageFormat.format(" and {0} in ({1})", colName, value);
	            } else if (name.startsWith("qn_eq_")) {
	                String colName = name.substring("qn_eq_".length(), name.length());
	                condition += MessageFormat.format(" and {0} = {1}", colName, value);
	            } else if (name.startsWith("qn_in_")) {
	                String colName = name.substring("qn_in_".length(), name.length());
	                condition += MessageFormat.format(" and {0} in ({1})", colName, value);
	            } else if (name.startsWith("qn_from_")) {
	                String colName = name.substring("qn_from_".length(), name.length());
	                String toName = "qn_to_" + colName;
	                if (parameters.containsKey(toName)) {
	                    String toValue = parameters.get(toName)[0];
	                    condition += MessageFormat.format(" and {0} between {1} and {2}", colName, value, toValue);
	                } else {
	                    condition += MessageFormat.format(" and {0} > {1}", colName, value);
	                }
	            }
	        }
	        return condition;
	    }

	    protected final String createDataSql(HttpServletRequest request) {
	        String sql = "", sqlPattern = "select {0} from {1} {2} ";
	        String condition = "", columns = this.columns(), viewName = viewName(), baseCondition = this.baseCondition(), postCondition = this.postCondition(request);
	        if (!TextCommon.isNullOrEmpty(baseCondition,postCondition)) {
	            baseCondition = TextCommon.trim(baseCondition, ",", "and", "or", "and");
	            postCondition = TextCommon.trim(postCondition, ",", "and", "or", "and");
	            condition = MessageFormat.format(" where {0} and ( {1} ) ", baseCondition, postCondition);
	        } else if (!TextCommon.isNullOrEmpty(baseCondition)) {
	            baseCondition = TextCommon.trim(baseCondition, ",", "and", "or", "and");
	            condition = " where " + baseCondition;
	        } else if (!TextCommon.isNullOrEmpty(postCondition)) {
	            postCondition = TextCommon.trim(postCondition, ",", "and", "or", "and");
	            condition = " where " + postCondition;
	        }
	        sql = MessageFormat.format(sqlPattern,
	                columns,
	                viewName,
	                condition
	        );
	        return sql;

	    }

	    protected final String createOrderbySql(HttpServletRequest request) {
	    	
	        String sql = createDataSql(request), orderby = " ",dir=this.postOrderDir(request);
	        String []
	        		baseOrderby = this.baseOrderby(), 
	        		postOrderby = this.postOrderby(request);

	        if (baseOrderby.length>0&&postOrderby.length >0) {
	            orderby += MessageFormat.format(" order by {0},{1} ", 
	            		String.join(",", baseOrderby) , String.join(",", postOrderby))+" "+dir;
	        } else if (baseOrderby.length>0) {
	            orderby += " order by " + String.join(",", baseOrderby)+" "+dir ;
	        } else if (postOrderby.length >0) {
	            orderby += " order by " + String.join(",", postOrderby)+" "+dir ;
	        }
	        sql += orderby;
	        return sql;
	    }

	    protected final String createCountSql(HttpServletRequest request) {
	        String sql = "", sqlPattern = "select count(*) from {0} {1} ";
	        String condition = "", baseCondition = this.baseCondition(), postCondition = this.postCondition(request), viewName = viewName();
	        if (!TextCommon.isNullOrEmpty(baseCondition,postCondition)) {
	            condition = MessageFormat.format(" where {0} and ( {1} ) ", baseCondition, postCondition);
	        } else if (!TextCommon.isNullOrEmpty(baseCondition)) {
	            condition = " where " + baseCondition;
	        } else if (!TextCommon.isNullOrEmpty(postCondition)) {
	            condition = " where " + postCondition;
	        }
	       
	        sql = MessageFormat.format(sqlPattern,
	                viewName,
	                condition
	        );
	        return sql;
	    }

	    protected JsonObject rowEach(int i,JsonObject row) {
			return row;
	   }

	    
	    
	    

		@Override
		public String total() throws IOException {
			// TODO Auto-generated method stub
			HttpServletRequest request=getRequest();
			 String sql = createCountSql(request);
		        
		    JsonObject grid=new JsonObject();
			int count=0;
			try {
				count = selectInt(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			grid.addProperty(JsonAttr.total.name(), count);
			return grid.toString();
		}

		@Override
	    public String grid() throws IOException {
	        HttpServletRequest request=getRequest();
            int start=postStart(request),limit=postLimit(request);
	        String sql = createPageSql(this.createOrderbySql(request),start, limit);
	        
	        
	        int count=0;
	        JsonArray rows=null;
				try {
					rows = selectList(sql);
					 for (int i = 0; i < rows.size(); i++) {
			                rows.set(i, rowEach(i,rows.get(i).getAsJsonObject()));
			         }
			            
			            if (rows.size() < limit) {
			                count=(start*limit)+rows.size();
			            } else {
			                String csql = createCountSql(request);
			                count = selectInt(csql);
			            }
				} catch (SQLException e) {
					e.printStackTrace();
				}
			JsonObject grid=createGrid(rows, count);
	           
	        return grid.toString();
	        
	        
	        
	    }
		
		
		
	    
		@Override
		public String rows() throws IOException {
			// TODO Auto-generated method stub
			HttpServletRequest request=getRequest();
            int start=postStart(request),limit=postLimit(request);
	        String sql = createPageSql(this.createOrderbySql(request),start, limit);
	        JsonArray rows=null;
				try {
					rows = selectList(sql);
					 for (int i = 0; i < rows.size(); i++) {
			                rows.set(i, rowEach(i,rows.get(i).getAsJsonObject()));
			         }
			           
				} catch (SQLException e) {
					e.printStackTrace();
				}
		
	           
	        return rows.toString();
		}

		protected JsonObject createGrid(JsonArray rows,int total) {
			JsonObject grid=new JsonObject();
			grid.add(JsonAttr.rows.name(), rows);
			grid.addProperty(JsonAttr.total.name(), total);
			return grid;
		}
	    
	    public String debug() throws IOException {
	    	HttpServletRequest request=getRequest();
            int start=postStart(request),limit=postLimit(request);
	        String sql = createPageSql(this.createOrderbySql(request),start, limit);
	    	
			JsonObject debug=new JsonObject();

			
				String dsql=this.createDataSql(request);
	                        String osql=this.createOrderbySql(request);
				String psql=this.createPageSql(dsql,start, limit);
				String csql=this.createCountSql(request);
				debug.addProperty("dataSql", dsql);
				debug.addProperty("pageSql", psql);
				debug.addProperty("countSql", csql);
	            debug.addProperty("orderbySql", osql);
	            debug.addProperty("gridSql", sql);
			return debug.toString();
		    
		}
	    
	     public String src_easyui() throws Exception{
	        
	        StringBuffer html=new StringBuffer("");
	        String sql="select * from "+this.viewName()+" where 1=2";
	        
			JsonObject head=selectMetaData(sql);
			html.append(src_easyui(head));
			
	                
	        return html.toString();
			
		}
	    
	     
	     
	     public String html_table() {
	    	 HttpServletRequest request=getRequest();
	            int start=postStart(request),limit=postLimit(request);
		        String sql = createPageSql(this.createOrderbySql(request),start, limit);
		        String csql=createCountSql(request);
		        JsonObject head=null;
		        JsonArray rows=null;
		        int total=0;
				try {
					head=selectMetaData(sql);
					rows = selectList(sql);
					total=selectInt(csql);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        
	    	 return html_table(head,rows,total);
	     }
	    

		protected int selectInt(String sql) throws SQLException {
			// TODO Auto-generated method stub
			int re=0;
			Connection connection=createConnection();
			PreparedStatement preparedStatement= connection.prepareStatement(sql);
			ResultSet resultSet= preparedStatement.executeQuery(sql);
			if(resultSet.next()) {
			   re=resultSet.getInt(1);
			}
			MethodInstance.close.invoke(preparedStatement,resultSet);
			return re;
		}

		protected JsonObject selectMetaData(String sql) throws SQLException {
			Connection connection=createConnection();
			PreparedStatement ps= connection.prepareStatement(sql);
			ResultSetMetaData rsmd= ps.executeQuery().getMetaData();
			JsonObject head=new JsonObject();
			for(int i=1;i<=rsmd.getColumnCount();i++){
				String field=rsmd.getColumnLabel(i);
				int ct=rsmd.getColumnType(i);
				head.addProperty(field, ct);
			}
			ps.close();
			return head;
		}
		
		protected JsonArray selectList(String sql) throws SQLException {
			// TODO Auto-generated method stub
			JsonArray jarr=new JsonArray();
			Connection connection=createConnection();
			PreparedStatement preparedStatement= connection.prepareStatement(sql);
		
			ResultSet resultSet= preparedStatement.executeQuery(sql);
			ResultSetMetaData resultSetMetaData=resultSet.getMetaData();
			
			while(resultSet.next()) {
				 JsonObject json=new JsonObject();
				 for(int i=1;i<=resultSetMetaData.getColumnCount();i++){
					String field=resultSetMetaData.getColumnLabel(i);
					Object value=resultSet.getObject(i);
					if(value==null) {
					   json.add(field,JsonNull.INSTANCE);
					}
					else  if (value instanceof String){
						json.addProperty(field, (String)value);
					}
					else  if (value instanceof Integer){
						json.addProperty(field, (int)value);
					}else if (value instanceof Long) {
						json.addProperty(field, (Long)value);
					}else if (value instanceof Double) {
						json.addProperty(field, (Double)value);
					}else if (value instanceof Date) {
						Date dt=(Date)value;
						json.addProperty(field,toString(dt));
					}
				 }
				 jarr.add(json);
			}
			
			MethodInstance.close.invoke(preparedStatement,resultSet);
			return jarr;
		}
		
		protected String toString(Date date) {
			return SimpleDateFormatInstance.YMDHMSS.getSimpleDateFormat().format(date); 
		}
	
}
