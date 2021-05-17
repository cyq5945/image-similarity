package image.similarity;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import excel.util.ExcelMode;
import excel.util.ExcelReadBeanUtils;
import excel.util.ExcelWriteBeanUtils;
import excel.util.ModeExceUtil;
import org.junit.Assert;

import junit.framework.TestCase;
import org.testng.annotations.Test;

public class ImgHistogramTest extends TestCase {

	ImageHistogram histogram = null;

	public void setUp() {
		histogram = new ImageHistogram();
	}

	public void testImageHistogram() {
		histogram = new ImageHistogram();
		try {
			double score = histogram.match(new File("doc/imgs/1.jpg"), new File("doc/imgs/1.jpg"));
			System.out.println("img1-->img1::::score : " + score);
			Assert.assertTrue(score >= 0.8);

			score = histogram.match(new File("doc/imgs/1.jpg"), new File("doc/imgs/2.jpg"));
			System.out.println("img1-->img2::::score : " + score);
			Assert.assertTrue(score >= 0.8);

			score = histogram.match(new File("doc/imgs/1.jpg"), new File("doc/imgs/3.jpg"));
			System.out.println("img1-->img3::::score : " + score);
			Assert.assertTrue(score >= 0.8);

			score = histogram.match(new File("doc/imgs/1.jpg"), new File("doc/imgs/4.jpg"));
			System.out.println("img1-->img4::::score : " + score);
			Assert.assertTrue(score < 0.8);

			score = histogram.match(new File("doc/imgs/5.jpg"), new File("doc/imgs/6.jpg"));
			System.out.println("img5-->img6::::score : " + score);
			Assert.assertTrue(score < 0.8); // incorrect

			score = histogram.match(new File("doc/imgs/1.jpg"), new File("doc/imgs/6.jpg"));
			System.out.println("img1-->img6::::score : " + score);
			Assert.assertTrue(score < 0.8);

			String srcUrl = "http://oarfc773f.bkt.clouddn.com/100000094nzslsdnswbb_1_1_r.jpg";
			score = histogram.match(new URL(srcUrl), new URL("https://img3.doubanio.com/lpic/s27140981.jpg"));
			System.out.println("url::::score:" + score);
			Assert.assertTrue(score < 0.8);	// incorrect

			score = histogram.match(new URL(srcUrl), new URL("https://img3.doubanio.com/lpic/s8966044.jpg"));
			System.out.println("url::::score:" + score);
			Assert.assertTrue(score < 0.8);	// incorrect

			String testUrl ="https://test-eagle.oss-cn-shenzhen.aliyuncs.com/staging/trust/20210513/attachment/599543388937003009_网页截图_599543349690900480.jpg";
			double score_test = histogram.match(new URL(testUrl), new URL("https://test-eagle.oss-cn-shenzhen.aliyuncs.com/staging/trust/20210512/attachment/599249331182510081_网页截图_599249291332427777.jpg"));
			System.out.println("测试testUrl::::score:" + score_test);
			Assert.assertTrue(score_test < 0.8);	// incorrect

		} catch (IOException e) {
			e.printStackTrace();
			Assert.assertFalse(false);
		}

	}

	public static void main(String[] args) {
		ImgHistogramTest test = new ImgHistogramTest();
		test.excelModeCase();
	}

	public void excelModeCase() {
		ImageHistogram histogram = new ImageHistogram();
		String filePath = "D://测试文档2021//prod_0514_73.xlsx";
		File file= new File(filePath);
		System.out.println(",startTime------>:" + System.currentTimeMillis());
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

				double score_test = histogram.match(new URL(testUrlOld), new URL(testUrlNew));
				System.out.println("url::::score:" + score_test);
//				Assert.assertTrue(score_test < 0.8);	// incorrect

				System.out.println(id+",endTime------>:" + System.currentTimeMillis()+",测试2testUrl::::score:" + score_test);
				excelMode.setResponse_actual(String.valueOf(score_test));
				if (score_test>=0.8) {
					// 返回值大于等于0.8可以简单判断这两张图片内容一致
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
		String resName = "TestResult733" + df.format(new Date());
		String excelName = localPath + "//" + resName + ".xlsx";
		// 把modes 重新写入excel
		ExcelWriteBeanUtils<ExcelMode> exBeanUtils = new ExcelWriteBeanUtils<ExcelMode>();
		exBeanUtils.writeToBeanExcel(excelName, System.currentTimeMillis()+"测试结果", modes, null, null);
		System.out.println(",结束时间------>:" + System.currentTimeMillis());
	}

	public  String localPath = "D://测试文档2021//TestResult" ;
}
