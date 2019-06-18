package lava.wt.pojo;

public class RestResult<E> {

	
	public static final int CODE_EXCEPTION = -1;
	public static final int CODE_FAIL = 0;
	public static final int CODE_SUCCESS = 1;
	private String msg;
	
	private int code;
	
	private E result;
	
	private Object[] infos;

	

	public E getResult() {
		return result;
	}

	public void setResult(E result) {
		this.result = result;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public boolean isError() {
		return this.code<=0;
	}

	public void setInfos(Object... infos) {
		// TODO Auto-generated method stub
		this.infos=infos;
	}

	public Object[] getInfos() {
		return infos;
	}
	
	
}
