package excel.util;

import java.io.Serializable;

/**
 * excel模板
 * @author chenyanqing
 *
 */
public class ExcelMode/* implements Serializable */{

	/**
	 * id, testCase, method, baseUrl, interfaceUrl, requests_param, response_expect, response_actual, result
	 */
	private static final long serialVersionUID = 1L;
	
	private String id; // 编号
	
	private String testCase; // 测试编号
	
	private String method; // 请求方式，get，post

	private String baseUrl; // 域名
	
	private String interfaceUrl; // 接口地址
	
	private String requests_param; // 入参

	private String response_expect; // 预计出参

	private String response_actual; // 实际出参
	
	private Boolean result; // 运行结果

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTestCase() {
		return testCase;
	}

	public void setTestCase(String testCase) {
		this.testCase = testCase;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getInterfaceUrl() {
		return interfaceUrl;
	}

	public void setInterfaceUrl(String interfaceUrl) {
		this.interfaceUrl = interfaceUrl;
	}

	public String getRequests_param() {
		return requests_param;
	}

	public void setRequests_param(String requests_param) {
		this.requests_param = requests_param;
	}

	public String getResponse_expect() {
		if (response_expect!=null) {
			return response_expect.replaceAll(" ", "");
		}
		return response_expect;
	}

	public void setResponse_expect(String response_expect) {
		this.response_expect = response_expect;
	}

	public String getResponse_actual() {
		return response_actual;
	}

	public void setResponse_actual(String response_actual) {
		this.response_actual = response_actual;
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}
	
}
