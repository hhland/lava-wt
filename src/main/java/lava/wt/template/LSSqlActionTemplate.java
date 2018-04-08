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

import lava.common.TextCommon;
import lava.instance.SimpleDateFormatInstance;





public abstract  class LSSqlActionTemplate extends LSActionTemplate{

	 protected Connection connection;
	    
	    
	    
	   public void setConnection(Connection connection) {
	        this.connection = connection;
	        
	   }

	    protected abstract String viewName();
	    
	    
	    protected abstract String createPageSql(String dataSql,int start, int limit);
	    
	     protected String columns() {
	        return "*";
	    }

	    protected String baseCondition() {
	        return "";
	    }

	    protected String baseOrderby() {
	        return "";
	    }
	    
	    protected String postOrderby() {
            HttpServletRequest request=getRequest();
	        String orderby = "", sort = request.getParameter("sort"), 
	        		order = request.getParameter("order");
	        if (TextCommon.isNullOrEmpty(sort) && TextCommon.isNullOrEmpty(order)) {
	            orderby += sort + " " + order;
	        }
	        return orderby;
	    }

	    protected String postCondition() {
	    	Map<String,String[]> parameters= this.getRequest().getParameterMap();
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

	    protected final String createDataSql() {
	        String sql = "", sqlPattern = "select {0} from {1} {2} ";
	        String condition = "", columns = this.columns(), viewName = viewName(), baseCondition = this.baseCondition(), postCondition = this.postCondition();
	        if (TextCommon.isNullOrEmpty(baseCondition) && TextCommon.isNullOrEmpty(postCondition)) {
	            baseCondition = TextCommon.trim(baseCondition, ",", "and", "or", "and");
	            postCondition = TextCommon.trim(postCondition, ",", "and", "or", "and");
	            condition = MessageFormat.format(" where {0} and ( {1} ) ", baseCondition, postCondition);
	        } else if (TextCommon.isNullOrEmpty(baseCondition)) {
	            baseCondition = TextCommon.trim(baseCondition, ",", "and", "or", "and");
	            condition = " where " + baseCondition;
	        } else if (TextCommon.isNullOrEmpty(postCondition)) {
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

	    protected final String createOrderbySql() {
	        String sql = createDataSql(), orderby = " ", baseOrderby = TextCommon.trim(this.baseOrderby(), ","), postOrderby = TextCommon.trim(this.postOrderby(), ",");

	        if (TextCommon.isNullOrEmpty(baseOrderby,postOrderby)) {
	            orderby += MessageFormat.format(" order by {0},{1} ", TextCommon.trim(baseOrderby, ","), TextCommon.trim(postOrderby, ","));
	        } else if (TextCommon.isNullOrEmpty(baseOrderby)) {
	            orderby += " order by " + TextCommon.trim(baseOrderby, ",");
	        } else if (TextCommon.isNullOrEmpty(postOrderby)) {
	            orderby += " order by " + TextCommon.trim(postOrderby, ",");
	        }
	        sql += orderby;
	        return sql;
	    }

	    protected final String createCountSql() {
	        String sql = "", sqlPattern = "select count(*) from {0} {1} ";
	        String condition = "", baseCondition = this.baseCondition(), postCondition = this.postCondition(), viewName = viewName();
	        if (TextCommon.isNullOrEmpty(baseCondition,postCondition)) {
	            condition = MessageFormat.format(" where {0} and ( {1} ) ", baseCondition, postCondition);
	        } else if (TextCommon.isNullOrEmpty(baseCondition)) {
	            condition = " where " + baseCondition;
	        } else if (TextCommon.isNullOrEmpty(postCondition)) {
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
		public String count() throws IOException {
			// TODO Auto-generated method stub
			
			 String sql = createCountSql();
		        
		    JsonObject grid=new JsonObject();
			int count=0;
			try {
				count = selectInt(sql);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			grid.addProperty("count", count);
			return grid.toString();
		}

		@Override
	    public String grid() throws IOException {
	   

	        String sql = createPageSql(this.createOrderbySql(),start, limit);
	        
	        JsonObject grid=new JsonObject();
	        
	            JsonArray rows=null;
				try {
					rows = selectList(sql);
					 for (int i = 0; i < rows.size(); i++) {
			                rows.set(i, rowEach(i,rows.get(i).getAsJsonObject()));
			         }
			           
			            if (rows.size() < limit) {
			                grid.addProperty(totalName, rows.size());
			            } else {
			                String csql = createCountSql();
			                int count = selectInt(csql);
			                grid.addProperty(totalName, count);
			            }
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	           
	          return grid.toString();
	        
	        
	        
	    }
	    
	    
	    public String debug() throws IOException {
			
			JsonObject debug=new JsonObject();

			
				String dsql=this.createDataSql();
	                        String osql=this.createOrderbySql();
				String psql=this.createPageSql(dsql,start, limit);
				String csql=this.createCountSql();
				debug.addProperty("dataSql", dsql);
				debug.addProperty("pageSql", psql);
				debug.addProperty("countSql", csql);
	            debug.addProperty("orderbySql", osql);
			return debug.toString();
		    
		}
	    
	     public String easyui_grid() throws Exception{
	        
	        StringBuffer html=new StringBuffer("");
	        String sql="select * from "+this.viewName()+" where 1=2";
	        
			JsonObject head=selectMetaData(sql);
			html.append(easyui_grid(head));
			
	                
	        return html.toString();
			
		}
	    
	     
	     
	     public String html_table() {
	    	 return "";
	     }
	    

		protected int selectInt(String sql) throws SQLException {
			// TODO Auto-generated method stub
			int re=0;
			PreparedStatement preparedStatement= connection.prepareStatement(sql);
			ResultSet resultSet= preparedStatement.executeQuery(sql);
			if(resultSet.next()) {
			   re=resultSet.getInt(1);
			}
			resultSet.close();
			preparedStatement.close();
			return re;
		}

		protected JsonObject selectMetaData(String sql) throws SQLException {
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
			PreparedStatement preparedStatement= connection.prepareStatement(sql);
			ResultSet resultSet= preparedStatement.executeQuery(sql);
			ResultSetMetaData resultSetMetaData= preparedStatement.executeQuery().getMetaData();
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
						json.addProperty(field,SimpleDateFormatInstance.yyyyMMddHHmmss_en.format(dt) );
					}
				 }
				 jarr.add(json);
			}
			resultSet.close();
			preparedStatement.close();
			return jarr;
		}
	
}
