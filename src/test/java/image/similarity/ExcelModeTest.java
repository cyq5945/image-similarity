package image.similarity;


import com.baoquan.gmhelper.SM4Util;
import excel.util.*;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class ExcelModeTest {

	public final static String encoding ="UTF-8";
	
	public  String filePath =null;
	public  String localPath = "D://测试文档2021//TestResult" ;
	
    @BeforeTest
    public void ExcelModeTest() {
    	 excelModeCase("D://测试文档2021//test0514-1.xlsx");
    }

    /**
     * 单个测试用例demo
     */
    @Test
    @Parameters("filePath")
    public void excelModeCase(String filePath) {
		ImagePHash p = new ImagePHash();
    	File file= new File(filePath);
    			
    	List<ExcelMode> modes = new ExcelReadBeanUtils<ExcelMode>().exce(file, new ModeExceUtil());
    	System.out.println("结果ExcelMode长度："+modes.size());
		String baoquanUrl = "https://baoquan-p1.oss-cn-shenzhen.aliyuncs.com/";
		try {
			for (ExcelMode excelMode : modes) {
				String respStr = null;
				String id =  excelMode.getId();
				if (id.equals("id")) { // 过滤掉多个表格数据
					continue;
				}

				String testUrlNew = excelMode.getBaseUrl().contains("http")?excelMode.getBaseUrl():baoquanUrl+excelMode.getBaseUrl();
				String testUrlOld = excelMode.getInterfaceUrl().contains("http")?excelMode.getInterfaceUrl():baoquanUrl+excelMode.getInterfaceUrl();
				testUrlOld = testUrlOld.replaceAll("\\?", "%3F");
				testUrlNew = testUrlNew.replaceAll("\\?", "%3F");
				//				File testUrlNewFile =new File(testUrlNew);
//				FileInputStream downloadInput = new FileInputStream(testUrlNew);
//				byte[] fileInputStream = SM4Util.decrypt_Ecb_Padding(Base64.getDecoder().decode(excelMode.getMethod()), IOUtils.toByteArray(downloadInput));
				System.out.println(id+",startTime------>:" + System.currentTimeMillis());
				int score_test = p.distance(new URL(testUrlOld), new URL(testUrlNew));
				System.out.println(id+",endTime------>:" + System.currentTimeMillis()+",测试2testUrl::::score:" + score_test);
				excelMode.setResponse_actual(String.valueOf(score_test));
				if (score_test<10) {
					// 值越小相识度越高，10之内可以简单判断这两张图片内容一致
					excelMode.setResult(true);
				} else {
					excelMode.setResult(false);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//设置测试结果文件地址和文件名称
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String resName = "TestResult" + df.format(new Date());
		String excelName = localPath + "//" + resName + ".xlsx";
    	// 把modes 重新写入excel
    	ExcelWriteBeanUtils<ExcelMode> exBeanUtils = new ExcelWriteBeanUtils<ExcelMode>();
    	exBeanUtils.writeToBeanExcel(excelName, System.currentTimeMillis()+"测试结果", modes, null, null);
    	
    }

//    @Test
//    public  void  testTime(){
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		String resName = "TestResult" + df.format(new Date());
//		String ExcelName = localPath + "//" + resName;
//		System.out.println(ExcelName);
//	}


    /**
     * 集成测试用例demo
     */
    @Test
    @Parameters("filePath")
    public void excelModeIntegrationCase(String filePath) {
    	File file= new File(filePath);
    	List<ExcelMode> modes = new ExcelReadBeanUtils<ExcelMode>().exce(file, new ModeExceUtil());
    	System.out.println("IntegrationCase结果："+modes);
    	
    	boolean isLogin = false;
    	boolean login = false;
    	
//    	WebClient client= new WebClient();
//    	for (ExcelMode excelMode : modes) {
//			String respStr = null;
//			if (excelMode.getId().equals("id")) {
//				continue;
//			}
//			if (excelMode.getMethod().equals("get")) {
//				byte[] respResult = client.doGet(excelMode.getBaseUrl()+excelMode.getInterfaceUrl()) ;
//				respStr = new String(respResult);
//				if (excelMode.getTestCase().equals("test1")) { // 是否登陆用，执行第一条用例
//					JSONObject respJson = JSONObject.fromObject(respStr);
//					if (respJson.getBoolean("success")) {
//
//						JSONObject modelJson = JSONObject.fromObject(respJson.getString("model"));
//						isLogin = modelJson.getBoolean("login"); // 登陆状态
//						login = isLogin;
//					} else
//						break;
//				} else 	if (excelMode.getTestCase().equals("test3")&&login) { // 执行第三条用例
//					byte[] respResult2 = client.doGet(excelMode.getBaseUrl()+excelMode.getInterfaceUrl()) ;
//					respStr = new String(respResult2);
//				}
//				System.out.println(excelMode.getBaseUrl()+excelMode.getInterfaceUrl()+",get请求结果："+respStr);
//
//			} else { // post请求方法
//				if (excelMode.getTestCase().equals("test2")&& !login) { // 未登录下进行登陆，执行第二条用例
//					JSONObject reqJson = JSONObject.fromObject(excelMode.getRequests_param());
//					System.out.println("post请求入参:"+reqJson);
//					byte[] respResult = client.doPostByJson(excelMode.getBaseUrl()+excelMode.getInterfaceUrl(), reqJson, encoding, RequestConfig.custom().build());
//					respStr = new String(respResult);
//					System.out.println(excelMode.getBaseUrl()+excelMode.getInterfaceUrl()+",post请求结果："+respStr);
//
//					JSONObject respJson = JSONObject.fromObject(respStr);
//					login =respJson.getBoolean("success"); // 是否登陆
//				} else
//					break;
//			}
			// 把resStr 结果写入response_actual
//			excelMode.setResponse_actual(respStr);
//
//			if (StringUtils.isBlank(excelMode.getResponse_expect())) { // 没有填预计出参
//				continue;
//			}
//			if (excelMode.getResponse_expect().equals(respStr)) {
//				// 相等,result 结果为true
//				excelMode.setResult(true);
//			} else if (!StringUtils.isEmpty(excelMode.getRequests_param())) {
//				// 不相等，result 结果为false
//				excelMode.setResult(false);
//			}
//
//		}
//    	// 把modes 重新写入excel
//    	ExcelWriteBeanUtils<ExcelMode> exBeanUtils = new ExcelWriteBeanUtils<ExcelMode>();
//    	exBeanUtils.writeToBeanExcel(filePath, "测试结果", modes, null, null);
    }
    
    public static void main1(String[] args) {
    	File file= new File("D://mode.xlsx");
    	
    	 List<List<Object>> modes = new ExcelReadListUtils().exce(file, new ArrayList<Object>());
    	 System.out.println("结果："+modes);
    	 for (List<Object> list : modes) {
    		 if (list.equals(modes.get(0))) { // 第一行过滤
    			 System.out.println("第一行过滤");
				continue;
			}
			for (Object object : list) {
				System.out.println("object结果："+object);
			}
		}
    	 List<Object> list = new ArrayList<>();
    	 list.add("123");
    	 list.add("1234");
    	 list.add("12345");
    	 list.add("123456");
    	 list.add("1234567");
    	 list.add("12345678");
    	 ExcelMode mode = new ExcelMode();
    	 mode.setInterfaceUrl("111111111");
//    	 ExcelToListUtils.writeExcel("D://mode2.xlsx", list, Object.class);
    	 
    	 
//    	List<Object> modes = new ExceUtils<Object>().exce(file, new Object());
//    	for (Object excelMode : modes) {
//			System.out.println("----------------"+excelMode);
//			WebClient client= new WebClient();
//			byte[] res = client.doGet(excelMode.getUrl()+"?accountNo="+excelMode.getMessage()) ;
//			String resStr = new String(res);
//			System.out.println(resStr);
//		}
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
    	String baseUrl ="https://baoquan-p1.oss-cn-shenzhen.aliyuncs.com/";
    	String testUrlOld = baseUrl+"staging/trust/20201105/attachment/531066095893749761_????_531065960413536256.png";
		String old  = URLEncoder.encode(testUrlOld, "utf-8");
		String testUrlOld2  = testUrlOld.replaceAll("\\?", "%3F");
		System.out.println("-----猜测是"+old );
		System.out.println("-----猜测是testUrlOld:"+testUrlOld2 );
	}
	
}
