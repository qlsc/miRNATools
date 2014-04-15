package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import model.GTVo;

public class ReadExcelUtil {
	public static void main(String[] args) {   
		String fileName = "E:\\gen\\31\\microcosm0414\\rno-mir-31_page2.xls"; // Excel文件所在路径 
		readExcel(fileName);
    }  
	public static List<GTVo> readExcel(String filePath) {
		List list = new ArrayList();
		try {  
		InputStream iStream = null;
		Workbook workbook = null;
		iStream = new FileInputStream(filePath);
		workbook = Workbook.getWorkbook(iStream);
		// sheet row column 下标都是从0开始的
		Sheet sheet = workbook.getSheet(0);
		int column = sheet.getColumns();
		int rows = sheet.getRows();
		for (int i = 1; i < rows; i++) {
			Cell[] cells = sheet.getRow(i);
			if (cells.length >= 4) {
				GTVo base = new GTVo();
				base.setTranscript(cells[1].getContents());
				list.add(base);
			}
		} // 操作完成时，关闭对象，释放占用的内存空间
		
		if (iStream != null) iStream.close(); 
		if(workbook != null) workbook.close(); 
		 
		} catch (BiffException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } 
		return list;
		}
}
