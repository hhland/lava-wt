package lava.wt.adapter;

import javax.servlet.http.HttpSession;

public class HttpSessionAdapter extends BaseAdapter<HttpSession>{

	
	public HttpSessionAdapter(HttpSession _this) {
		super(_this);
		// TODO Auto-generated constructor stub
	}

	public void setAttribute(Object value) {
		if(value==null)return;
		_this.setAttribute(value.getClass().getName(), value);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String name) {
		// TODO Auto-generated method stub
		return (T)_this.getAttribute(name);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(Class<T> cls) {
		// TODO Auto-generated method stub
		return (T)_this.getAttribute(cls.getName());
	}
	
	public void removeAttribute(Object value) {
		if(value==null)return;
		else if(value instanceof String )return;
		else removeAttribute(value.getClass());
	}
	
	public void removeAttribute(Class cls) {
		_this.removeAttribute(cls.getName());
		
	}
}
