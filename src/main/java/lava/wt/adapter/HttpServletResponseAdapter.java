package lava.wt.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class HttpServletResponseAdapter extends BaseAdapter<HttpServletResponse> {

	public HttpServletResponseAdapter(HttpServletResponse _this) {
		super(_this);
		// TODO Auto-generated constructor stub
	}

	public void download(File file) throws IOException {
		 download(file.getName(), file);
	}
	
	public void download(String fileName,File file) throws IOException {
		

			//通过response响应返回给客户端

		   _this.setHeader("Content-Disposition","attachment;filename=\""+new String (fileName.getBytes("GBK"),"ISO8859-1")+"\"");//文件名称

			//开始写给客户端

			try(FileInputStream fis=new FileInputStream(file);){

			byte b[]=new byte[1024];

			int length;

			while((length=fis.read(b))>0){

				_this.getOutputStream().write(b,0,length);

			}

			

			_this.getOutputStream().flush();
			}
			
	}
	

}
