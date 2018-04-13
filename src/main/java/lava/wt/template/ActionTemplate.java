package lava.wt.template;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface ActionTemplate {

	HttpServletRequest getRequest();
	
	HttpServletResponse getResponse();
	
	HttpSession getSession();
	
	Class<? extends ActionTemplate> thisClass();
}
