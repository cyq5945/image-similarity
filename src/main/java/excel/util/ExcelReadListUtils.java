package excel.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

/**
 * 读取exce表格工具类（集合方式）
 * @author chenyanqing
 *
 */
public class ExcelReadListUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelReadBeanUtils.class);
	public static final String DATE_FORMAT_ARRAY = "yyyy-MM-dd";
	
	static String OFFICE_EXCEL_XLS = ".xls";

	static String OFFICE_EXCEL_XLSX = ".xlsx";


	/**
	 * 普通文件外部处理接口
	 *
	 * @param file
	 *            上传文件
	 *
	 * @return List<Object> 解析完成的信息
	 */
	public List<List<Object>> exce(File file, List<Object> valueSet) {
		if (file == null || !file.exists())
			throw new IllegalArgumentException("file is null");

		try (InputStream input = new FileInputStream(file)) {
			if (file.getName().endsWith(OFFICE_EXCEL_XLS))
				return readXLS(input, valueSet);
			else if (file.getName().endsWith(OFFICE_EXCEL_XLSX))
				return readXLSX(input, valueSet);
			else
				throw new IllegalArgumentException("file does not support:" + file.getName());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 *
	 * 最终处理方法
	 *
	 * @param input
	 *            文件流
	 *
	 * @return List 解析完成的信息
	 */
	private List<List<Object>> readXLS(InputStream input, List<Object> valueSet) {
		try {
			POIFSFileSystem fs = new POIFSFileSystem(input);
			HSSFWorkbook wb = new HSSFWorkbook(fs, true);

			return resolve(wb, valueSet);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException("文件解析错误");
		}
	}

	/**
	 *
	 * 最终处理方法
	 *
	 * @param input
	 *            文件流
	 *
	 * @return List解析完成的信息
	 */
	private List<List<Object>> readXLSX(InputStream input, List<Object> valueSet) {
		try {
			OPCPackage op = OPCPackage.open(input);
			XSSFWorkbook wb = new XSSFWorkbook(op);

			return resolve(wb, valueSet);
		} catch (InvalidFormatException | IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException("文件解析错误");
		}
	}

	/**
	 * EXCEL 文件解析
	 *
	 * @param wb
	 *            EXCEL 文件
	 *
	 * @return List<Object>
	 */
	@SuppressWarnings("unchecked")
	protected List<List<Object>> resolve(Workbook wb, List<Object> valueSet) {
		int sheets = wb.getNumberOfSheets();

		List<List<Object>> list = new ArrayList<>();

//		E obj;
		Sheet sheet;

		for (int i = 0; i < sheets; i++) {
			sheet = wb.getSheetAt(i);

			if (sheet == null)
				continue;

			for (Row row : sheet) {
				int curRows = row.getRowNum();
				List<Object> listObject = new ArrayList<>();
//				if ((i == 0 && curRows == 0)) // 第一行要不要取数据
//					continue;
				 if (row.getCell(0) == null)
					break;

//				Class<E> entityClass = (Class<E>) ((ParameterizedType) valueSet.getClass().getGenericSuperclass())
//						.getActualTypeArguments()[0];
				try {
//					obj = entityClass.getConstructor().newInstance();

					for (Cell cell : row)
						if (cell != null)
							cell(cell, i, curRows, listObject);

					list.add(listObject);
				} catch (Exception e) {
					LOGGER.error("反射错误", e);
					Assert.assertTrue(false, "系统错误");
				}
			}
		}

		return list;

	}

	private void cell(Cell cell, int curSheets, int curRows, List<Object> valueSet) {
		int curCal = cell.getColumnIndex();
		String str = getCellValue(cell);

		try {
			valueSet.add(str);
//			valueSet.value(curCal, str, obj);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			if (e instanceof IllegalArgumentException || e instanceof RuntimeException)
				throw new RuntimeException("消息错误：" + e.getMessage() + ";" + (curSheets + 1) + "页，"
						+ (curRows + 1) + "行，" + (curCal + 1) + "列 - 导入值:" + str);
			else
				throw new RuntimeException("消息错误：" + (curSheets + 1) + "页，" + (curRows + 1) + "行，"
						+ (curCal + 1) + "列 - 导入值:" + str);
		}
	}

	@SuppressWarnings("deprecation")
	public static String getCellValue(Cell cell) {
		Object obj = "";
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			obj = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			obj = cell.getNumericCellValue();
			break;
		case Cell.CELL_TYPE_FORMULA:
			obj = cell.getCellFormula();
			break;
		case Cell.CELL_TYPE_ERROR:
			obj = cell.getErrorCellValue();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			obj = cell.getBooleanCellValue();
			break;
		case Cell.CELL_TYPE_BLANK:
			break;
		}

		return String.valueOf(obj).trim();
	}

	/**
	 *
	 * 字符串转时间
	 *
	 * @param str
	 *            需要转换的字符串
	 *
	 * @return Date
	 */
	static Date getDateValue(String str) {
		try {
			return DateUtils.parseDateStrictly(str, DATE_FORMAT_ARRAY);
		} catch (ParseException e) {
			LOGGER.error("时间格式不支持：{}", str, e);
			throw new RuntimeException("时间格式不支持 ：" + str + "，支持格式： " + Arrays.asList(
					DATE_FORMAT_ARRAY));
		}
	}
	
	public static void main(String[] args) {
		File file= new File("D://mode2.xlsx");
		  
		 List<List<Object>> modes = new ExcelReadListUtils().exce(file, new ArrayList<Object>());
		 System.out.println("modes结果："+modes);
		 for (List<Object> list : modes) {
			for (Object object : list) {
				System.out.println("object结果："+object);
			}
		}
	}
}
