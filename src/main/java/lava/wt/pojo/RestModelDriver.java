package lava.wt.pojo;

public abstract class RestModelDriver<M> {

	
	public abstract M get(Object pk) throws Exception;
	  
	public abstract int put(M m) throws Exception; 
	 
	public abstract int post(M m) throws Exception; 
	 
	public abstract int delete(Object pk) throws Exception;

}
