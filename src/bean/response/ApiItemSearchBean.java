package bean.response;

import java.util.List;

import bean.ResponseBean;


public class ApiItemSearchBean {
	private ResponseBean response;
	private List<ResItemSearchBean> item;
	/**
	 * @param response
	 * @param item
	 */
	
	public ApiItemSearchBean() {}
	public ApiItemSearchBean(ResponseBean response, List<ResItemSearchBean> item) {
		super();
		this.response = response;
		this.item = item;
	}
	public ResponseBean getResponse() {
		return response;
	}
	public void setResponse(ResponseBean response) {
		this.response = response;
	}
	public List<ResItemSearchBean> getItem() {
		return item;
	}
	public void setItem(List<ResItemSearchBean> item) {
		this.item = item;
	};
	
	
}
