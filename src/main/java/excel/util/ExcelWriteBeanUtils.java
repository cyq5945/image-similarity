package excel.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.stereotype.Component;


/**
 *  写入exce表格工具类，封装类版
 * @author chenyanqing
 *
 * @param <E>
 */
//@Component
public class ExcelWriteBeanUtils<E> {
    private Workbook workbook = null;  
    
    
    
    
    /** 
     * 判断文件是否存在
     * @param filePath  文件路径 
     * @return 
     */  
    public boolean fileExist(String filePath){  
         boolean flag = false;  
         File file = new File(filePath);  
         flag = file.exists();  
         return flag;  
    }  
    
    /** 
     * 判断文件的sheet是否存在
     * @param filePath   文件路径 
     * @param sheetName  表格索引名 
     * @return 
     */  
    public boolean sheetExist(String filePath,String sheetName){  
         boolean flag = false;  
         File file = new File(filePath);  
         if(file.exists()){    //文件存在  
            //创建workbook  
             try {  
             	if (file.getName().endsWith(ExcelUtils.OFFICE_EXCEL_XLS)) {
             		workbook = new HSSFWorkbook(new FileInputStream(file));
             		
             	} else if(file.getName().endsWith("xlsx")) {
             		
             		workbook = new XSSFWorkbook(new FileInputStream(file));
             	} else {
             		System.out.println("文件格式不正确");
             		return flag;
             	}
                //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)  
                Sheet sheet = workbook.getSheet(sheetName);    
                if(sheet!=null)  
                    flag = true;  
            } catch (Exception e) {  
                e.printStackTrace();  
            }                 
         }else{    //文件不存在  
             flag = false;  
         }            
         return flag;  
    }
    /** 
     * 创建新Sheet并写入第一行数据
     * @param filePath  excel的路径 
     * @param sheetName 要创建的表格索引 
     * @param titleRow excel的第一行即表格头 
     * @throws IOException 
     * @throws FileNotFoundException 
     */  
    public void createSheet(String filePath,String sheetName,String titleRow[]) throws FileNotFoundException, IOException{ 
        FileOutputStream out = null;         
        File excel = new File(filePath);  // 读取文件
        FileInputStream in = new FileInputStream(excel); // 转换为流
        // 加载excel的 工作目录       
     	if (excel.getName().endsWith(ExcelUtils.OFFICE_EXCEL_XLS)) {
     		workbook = new HSSFWorkbook(in);
     	} else if(excel.getName().endsWith("xlsx")) {
     		workbook = new XSSFWorkbook(in);
     	}
                          
        workbook.createSheet(sheetName); // 添加一个新的sheet  
        //添加表头  
        Row row = workbook.getSheet(sheetName).createRow(0);    //创建第一行            
        try {              
            for(int i = 0;i < titleRow.length;i++){  
                Cell cell = row.createCell(i);  
                cell.setCellValue(titleRow[i]);  
            } 
            out = new FileOutputStream(filePath);  
            workbook.write(out);
       }catch (Exception e) {  
           e.printStackTrace();  
       }finally {    
           try {    
               out.close();    
           } catch (IOException e) {    
               e.printStackTrace();  
           }    
       }             
    }
    /** 
     * 创建新excel. 
     * @param filePath  excel的路径 
     * @param sheetName 要创建的表格索引 
     * @param titleRow excel的第一行即表格头 
     */  
    public void createExcel(String filePath,String sheetName,String titleRow[]){  
        //创建workbook  
    	if (filePath.endsWith(ExcelUtils.OFFICE_EXCEL_XLS)) {
    		workbook = new HSSFWorkbook();
    		
    	} else if(filePath.endsWith("xlsx")) {
    		
    		workbook = new XSSFWorkbook();
    	} else {
    		System.out.println("文件格式不正确");
    		return ;
    	}
        //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)  
        workbook.createSheet(sheetName);    
        //新建文件  
        FileOutputStream out = null;  
        try {  
            //添加表头  
            Row row = workbook.getSheet(sheetName).createRow(0);    //创建第一行    
            for(int i = 0;i < titleRow.length;i++){  
                Cell cell = row.createCell(i);  
                cell.setCellValue(titleRow[i]);  
            }               
            out = new FileOutputStream(filePath);  
            workbook.write(out);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {    
            try {    
                out.close();    
            } catch (IOException e) {    
                e.printStackTrace();  
            }    
        }    
    }  
    /** 
     * 删除文件. 
     * @param filePath  文件路径 
     */  
    public boolean deleteExcel(String filePath){  
        boolean flag = false;  
        File file = new File(filePath);  
        // 判断目录或文件是否存在    
        if (!file.exists()) {  
            return flag;    
        } else {    
            // 判断是否为文件    
            if (file.isFile()) {  // 为文件时调用删除文件方法    
                file.delete();  
                flag = true;  
            }   
        }  
        return flag;  
    }  
    /** 
     * 往excel中写入. 
     * @param filePath    文件路径 
     * @param sheetName  表格索引 
     * @param object 
     */  
    public void writeToExcel(String filePath,String sheetName, List<E> objects,String titleRow[]){  
        //创建workbook  
        File file = new File(filePath);  
        try {  
        	if (file.getName().endsWith(ExcelUtils.OFFICE_EXCEL_XLS)) {
        		workbook = new HSSFWorkbook(new FileInputStream(file));
        	} else if(file.getName().endsWith("xlsx")) {
        		workbook = new XSSFWorkbook(new FileInputStream(file));
        	}
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        FileOutputStream out = null;  
        Sheet sheet = workbook.getSheet(sheetName);  
        // 获取表格的总行数  
//        int rowCount = sheet.getLastRowNum() + 1; // 需要加一  
//        int rowCount = objects.size() + 1; // 需要加一  
        int zdRow = 1;//真正的数据记录的列序号
        try {  
        	 Iterator<E> titleFieldIt = objects.iterator();//总记录的迭代器
             while (titleFieldIt.hasNext()) {//记录的迭代器，遍历总记录
            	 Object object = titleFieldIt.next();//拿到一条记录
            	 Row row = sheet.createRow(zdRow);     //最新要添加的一行  
            	 zdRow++;
        		 //通过反射获得object的字段,对应表头插入  
        		 // 获取该对象的class对象  
        		 Class<? extends Object> class_ = object.getClass();              
        		 for(int i = 0;i < titleRow.length;i++){    
        			 String title = titleRow[i];
        			 String UTitle = Character.toUpperCase(title.charAt(0))+ title.substring(1, title.length()); // 使其首字母大写;  
        			 String methodName  = "get"+UTitle;  
        			 Method method = class_.getDeclaredMethod(methodName); // 设置要执行的方法  
        			 Object data = method.invoke(object);
        			 Cell cell = row.createCell(i);  
        			 cell.setCellValue(null!=data ? data.toString():"");  
        		 }           
        		 
        	 }
            out = new FileOutputStream(filePath);  
            workbook.write(out);  
            System.out.println("写入成功");
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {    
            try {    
                out.close();    
            } catch (IOException e) {    
                e.printStackTrace();  
            }    
        }    
    }  
    
    /**
     * 写入excel
     * @param filePath 路径
     * @param sheetName 表格名称
     * @param objects 数据
     * @param title 标题
     * @param titleRow 实体字段
     */
    public void writeToBeanExcel(String filePath,String sheetName, List<E> objects,String[] title,String[] titleRow){  
    	if (null==title) { // 设置默认值
            //Excel文件易车sheet页的第一行 
//    		title =  new String[]{"编号", "测试编号","方式","域名","接口","入参","预计出参","实际出参","结果"};
    		title = new String[]{"id", "testCase", "method", "baseUrl", "interfaceUrl", "requests_param", "response_expect", "response_actual", "result"};
		}
    	if (null==titleRow) { // 设置默认值
    		//Excel文件易车每一列对应的数据
    		titleRow = new String[]{"id", "testCase", "method", "baseUrl", "interfaceUrl", "requests_param", "response_expect", "response_actual", "result"};
		}
        //判断该名称的文件是否存在  
        boolean fileFlag = this.fileExist(filePath);        
        if(!fileFlag){
        	this.createExcel(filePath,sheetName,title);
        }  
        //判断该名称的Sheet是否存在  
        boolean sheetFlag = this.sheetExist(filePath,sheetName);
        //如果该名称的Sheet不存在，则新建一个新的Sheet
        if(!sheetFlag){
           try {
        	   this.createSheet(filePath,sheetName,title);
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }
        }  
        this.writeToExcel(filePath, sheetName, objects, titleRow);
	}
    
    
    
    
    public static void main(String[] args) {  
        String filePath = "D://file//数据汇总1.xlsx";
        String sheetName = "测试001";
        //Excel文件易车sheet页的第一行 
        String title[] = {"编号", "测试编号","方式","域名","接口","入参","预计出参","实际出参","结果"};
        //Excel文件易车每一列对应的数据
        String titleDate[] = {"id", "testCase", "method", "baseUrl", "interfaceUrl", "requests_param", "response_expect", "response_actual", "result"};
        List<ExcelMode> users = new ArrayList<>();  
        ExcelMode user = new ExcelMode();
        user.setId("123");
        user.setBaseUrl("http://www.baidu.com");
        user.setMethod("get");
        user.setInterfaceUrl("mando");
        user.setRequests_param("111111111");
        user.setResponse_actual("-----------");
        user.setResult(false);
        users.add(user);
        ExcelMode user2 = new ExcelMode();
        user2.setId("1233333");
        user2.setBaseUrl("http://www.baidu.com");
        user2.setMethod("post");
        users.add(user2);
        user2 = new ExcelMode();
        user2.setId("1233333222");
        user2.setBaseUrl("http://www.baidu.com");
        user2.setMethod("post222");
        users.add(user2);
        user2 = new ExcelMode();
        user2.setId("123333322222222");
        user2.setBaseUrl("http://www.baidu.com");
        user2.setMethod("post2222222222");
        users.add(user2);
        
        ExcelWriteBeanUtils<ExcelMode> em = new ExcelWriteBeanUtils<ExcelMode>();  
        //写入到excel 
        em.writeToBeanExcel(filePath, sheetName, users,title, titleDate);
    }
}