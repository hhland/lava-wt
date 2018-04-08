package lava.instance;

import com.google.gson.Gson;

public enum GsonInstance {

	simple;
	
	private com.google.gson.Gson gson;
	
	GsonInstance(){
		this.gson=new Gson();	
	    
	}

	public synchronized Gson getGson() {
		return gson;
	}
	
	
}
