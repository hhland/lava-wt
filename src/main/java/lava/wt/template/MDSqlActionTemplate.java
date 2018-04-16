package lava.wt.template;

import java.sql.Connection;
import java.sql.SQLException;

import lava.rt.instance.MethodInstance;
import lava.rt.linq.Table;

public abstract class MDSqlActionTemplate<M> extends MDActionTemplate<M> {

	   
	   public abstract Table<M> getTable();
	   
	   
	   public void onConnectionEnd(Table<M> table) {
		    
	   }


	

	@Override
	protected M readByPk(String pk) throws Exception {
		// TODO Auto-generated method stub
		Table<M> table=getTable();
		M m =table.load(pk);
		return m;
	}


	


	@Override
	protected int create(M m) throws Exception {
		// TODO Auto-generated method stub
		Table<M> table=getTable();
		return (int)table.insert(m);
	}


	@Override
	protected int update(M m) throws Exception {
		// TODO Auto-generated method stub
		Table<M> table=getTable();
		return (int)table.update(m);
	}


	@Override
	protected int delete(M m) throws Exception {
		// TODO Auto-generated method stub
		Table<M> table=getTable();
		return (int)table.delete(m);
	}


	
	   
	   
	   
	
}
