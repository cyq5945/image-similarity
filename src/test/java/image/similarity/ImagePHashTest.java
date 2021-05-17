package image.similarity;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import excel.util.ExcelMode;
import excel.util.ExcelReadBeanUtils;
import excel.util.ExcelWriteBeanUtils;
import excel.util.ModeExceUtil;
import org.junit.Assert;
import org.junit.Before;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.testng.annotations.Parameters;

/**
 * Unit test for simple App.
 */
public class ImagePHashTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public ImagePHashTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(ImagePHashTest.class);
	}

	ImagePHash p = null;

	@Before
	public void setUp() {
		p = new ImagePHash();
	}

	public void testImgPHash() {
		try {
			int dis = p.distance(new File("doc/imgs/1.jpg"), new File("doc/imgs/1.jpg"));
			System.out.println("img1-->img1::::distance:" + dis);
			Assert.assertTrue(dis < 10);

			dis = p.distance(new File("doc/imgs/1.jpg"), new File("doc/imgs/2.jpg"));
			System.out.println("img1-->img2::::distance:" + dis);
			Assert.assertTrue(dis > 10); // incorrect

			dis = p.distance(new File("doc/imgs/1.jpg"), new File("doc/imgs/3.jpg"));
			System.out.println("img1-->img3::::distance:" + dis);
			Assert.assertTrue(dis > 10); // incorrect

			dis = p.distance(new File("doc/imgs/2.jpg"), new File("doc/imgs/3.jpg"));
			System.out.println("img2-->img3::::distance:" + dis);
			Assert.assertTrue(dis < 10);

			dis = p.distance(new File("doc/imgs/2.jpg"), new File("doc/imgs/4.jpg"));
			System.out.println("img2-->img4::::distance:" + dis);
			Assert.assertTrue(dis > 10);

			dis = p.distance(new File("doc/imgs/1.jpg"), new File("doc/imgs/4.jpg"));
			System.out.println("img2-->img3::::distance:" + dis);
			Assert.assertTrue(dis > 10);

			String srcUrl = "http://oarfc773f.bkt.clouddn.com/100000094nzslsdnswbb_1_1_r.jpg";
//			dis = p.distance(new URL("https://img3.doubanio.com/lpic/s27140981.jpg"), new URL(srcUrl));
//			System.out.println("url::::distance:" + dis);
//			Assert.assertTrue(dis < 10);

			dis = p.distance(new URL("https://img3.doubanio.com/lpic/s8966044.jpg"), new URL(srcUrl));
			System.out.println("url::::distance:" + dis);
			Assert.assertTrue(dis < 10);


			String testUrl ="https://test-eagle.oss-cn-shenzhen.aliyuncs.com/staging/trust/20210513/attachment/599543388937003009_网页截图_599543349690900480.jpg";
			int score_test = p.distance(new URL(testUrl), new URL("https://test-eagle.oss-cn-shenzhen.aliyuncs.com/staging/trust/20210512/attachment/599249331182510081_网页截图_599249291332427777.jpg"));
			System.out.println("测试testUrl::::score:" + score_test);
//			Assert.assertTrue(score_test < 10);

			String testUrl2 ="https://www.baoquan.com/ws/attestations/55383490E9D24B0E9C03639CB3C930BF/images";
			int score_test2 = p.distance(new URL(testUrl2), new URL("https://www.baoquan.com/ws/attestations/55383490E9D24B0E9C03639CB3C930BF/images"));
			System.out.println("测试2testUrl::::score:" + score_test2);
			Assert.assertTrue(score_test2 < 10);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static void main(String[] args) {
		ImagePHashTest test = new ImagePHashTest("图片测试");
		test.testImgPHash();
	}

}
