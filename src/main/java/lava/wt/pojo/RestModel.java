package lava.wt.pojo;

public abstract class RestModel<M> {

	
	public abstract M readByPk(Object pk) throws Exception;
	  
	public abstract int doCreate(M m) throws Exception; 
	 
	public abstract int doUpdate(M m) throws Exception; 
	 
	public abstract int doDelete(M m) throws Exception;

	public abstract Object getPk(M post) ;
}
