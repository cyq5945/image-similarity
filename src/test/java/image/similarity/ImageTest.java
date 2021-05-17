package image.similarity;


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

public class ImageTest {

    public final static String encoding ="UTF-8";

    public  String filePath =null;
    public  String localPath = "D://测试文档2021//TestResult";

    @BeforeTest
    public void ExcelModeTest() {
        excelModeCase2("D://测试文档2021//test0514-2.xlsx");
    }

    /**
     * 单个测试用例demo
     */
    @Test
    @Parameters("filePath")
    public void excelModeCase2(String filePath) {
        ImagePHash p = new ImagePHash();
        File file= new File(filePath);

        List<ExcelMode> modes = new ExcelReadBeanUtils<ExcelMode>().exce(file, new ModeExceUtil());
        System.out.println("结果ExcelMode长度："+modes.size());
        String baoquanUrl = "https://baoquan-p1.oss-cn-shenzhen.aliyuncs.com/";
        int i = 0;
        try {
            for (ExcelMode excelMode : modes) {
                String respStr = null;
                String id =  excelMode.getId();
                if (id.equals("id")) { // 过滤掉多个表格数据
                    continue;
                }
                i=i+1;
                String testUrlNew = excelMode.getBaseUrl().contains("http")?excelMode.getBaseUrl():baoquanUrl+excelMode.getBaseUrl();
                String testUrlOld = excelMode.getInterfaceUrl().contains("http")?excelMode.getInterfaceUrl():baoquanUrl+excelMode.getInterfaceUrl();
                testUrlOld = testUrlOld.replaceAll("\\?", "%3F");
                testUrlNew = testUrlNew.replaceAll("\\?", "%3F");
                //				File testUrlNewFile =new File(testUrlNew);
//				FileInputStream downloadInput = new FileInputStream(testUrlNew);
//				byte[] fileInputStream = SM4Util.decrypt_Ecb_Padding(Base64.getDecoder().decode(excelMode.getMethod()), IOUtils.toByteArray(downloadInput));
                System.out.println(i+","+id+",startTime------>:" + System.currentTimeMillis());
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
        String resName = "TestResult383-" + System.currentTimeMillis();
        String excelName = localPath + "//" + resName + ".xlsx";
        // 把modes 重新写入excel
        ExcelWriteBeanUtils<ExcelMode> exBeanUtils = new ExcelWriteBeanUtils<ExcelMode>();
        exBeanUtils.writeToBeanExcel(excelName, System.currentTimeMillis()+"测试结果", modes, null, null);

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
