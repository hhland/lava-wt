package lava.wt.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class HttpServletRequestAdapter extends BaseAdapter<HttpServletRequest> {

	
	
	
	
	
	
	public HttpServletRequestAdapter(HttpServletRequest _this) {
		super(_this);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String name) {
		// TODO Auto-generated method stub
		return (T)_this.getAttribute(name);
	}
	
	public <T> T getAttribute(Class<T> cls) {
		// TODO Auto-generated method stub
		return (T)_this.getAttribute(cls.getName());
	}
	
	
	public void setAttribute(Object val) {
		 if(val==null)return;
		 _this.setAttribute(val.getClass().getName(), val);
	}
	
	
	
	public void removeAttribute(Object value) {
		if(value==null)return;
		else if(value instanceof String )return;
		else removeAttribute(value.getClass());
	}
	
	public void removeAttribute(Class cls) {
		_this.removeAttribute(cls.getName());
	}
	
	
	public Boolean getBoolParameter(String name) {
		// TODO Auto-generated method stub
		return "true".equals(_this.getParameter(name));
	}

	
	 
	
	
	public String getContextPathUrl() {
		String path = _this.getContextPath();
	    String url = _this.getScheme() + "://"  + _this.getServerName() +  ":" + _this.getServerPort() + path;
	    return url;
	}
	
	
	public Map<String,String> getParameterMap(){
		Map<String,String> ret=new HashMap<>();

		
		Map map = _this.getParameterMap();
		
		for (Object key:map.keySet()) {
			
			Object value=map.get(key);
			
			if (value instanceof String[]) {
				value = String.join(",", value.toString().split(","));
				key=key.toString().replace("[]", "");
			}
			ret.put(key.toString(), value.toString());
		}
		return ret;
	}
	

}
