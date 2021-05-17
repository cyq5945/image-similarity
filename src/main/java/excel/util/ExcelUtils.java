package excel.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * excel工具类
 * @author chenyanqing
 *
 */
public class ExcelUtils {
	
	public static String OFFICE_EXCEL_XLS = ".xls";
	public static String OFFICE_EXCEL_XLSX = ".xlsx";
	

	/**
	 * 读取excel 返回实体类
	 * （可直接使用ExcelReadBeanUtils）
	 * @param file 文件
	 * @return
	 */
	public static List<ExcelMode> excelReadBean(File file){
		
		return new ExcelReadBeanUtils<ExcelMode>().exce(file, new ModeExceUtil());
	}
	
	
	/**
	 * 读取excel 返回集合类
	 * @param file 文件
	 * @return
	 */
	public static List<List<Object>> excelReadList(File file){
		
		return new ExcelReadListUtils().exce(file, new ArrayList<Object>());
	}
	
	
	/**
	 * 写入excel 参数是实体类 
	 * （可直接使用ExcelWriteBeanUtils）
     * @param filePath 路径
     * @param sheetName 表格名称
     * @param dtoList 数据 List<E> 可以更换
     * @param headersName 标题
     * @param headersId 实体字段
	 * @return
	 */
	public static void excelWriteBean(String filePath,String sheetName, List<ExcelMode> dtoList,String[] headersName,String[] headersId){
	       
        ExcelWriteBeanUtils<ExcelMode> em = new ExcelWriteBeanUtils<ExcelMode>();  
        //写入到excel 
        em.writeToBeanExcel(filePath,sheetName, dtoList, headersName, headersId);
	}
	
	
	/**
	 * 写入excel 参数是集合类
     * @param filePath 路径
     * @param sheetName 表格名称
     * @param dtoList 数据 List<E> 可以更换
     * @param headersName 标题
     * @param headersId 实体字段
	 * @return
	 */
	public static void excelWriteList(String filePath,String sheetName, List<Map<String, Object>> dtoList, List<String> headersName, List<String> headersId){
		
        ExcelWriteMapUtils exportExcelUtil = new ExcelWriteMapUtils();
        exportExcelUtil.exportExcel(filePath,sheetName, headersName, headersId,dtoList);
	}
	
	
}
