package main;

import java.io.FileOutputStream;
import java.util.List;

import model.MiRandaModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import util.Util;

public class MainTool {
public static void creatExcel(String matureName,String startIndex){
	String threadUrl = "http://www.microrna.org/microrna/getTargets.do?matureName="+matureName+"&startIndex="+startIndex+"&organism=10116";
	String hostName = "MiRanda_"+matureName;

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(hostName);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 设置列宽    
	    sheet.setColumnWidth(0, 4500); 
	    sheet.setColumnWidth(1, 4500); 
	    
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式
		
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue("Gene Symbol");
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("RefSeq ID");
		cell.setCellStyle(style);

		
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		List list = Util.getMiRandaByURL(threadUrl);
		
		
		for (int i = 0; i < list.size(); i++)
		{
			row = sheet.createRow((int) i+1 );
			MiRandaModel base = (MiRandaModel) list.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell((short) 0).setCellValue(base.getGeneSymbol());
			row.createCell((short) 1).setCellValue(base.getRefSeqID());
		}
		int page = Integer.parseInt(startIndex)/50 +1;
		// 第六步，将文件存到指定位置
		String excelFileName = "E:\\gene0416\\124miRanda\\"+hostName+"_page"+page+".xls";
		try
		{
			FileOutputStream fout = new FileOutputStream(excelFileName);
			wb.write(fout);
			fout.close();
			System.out.println("哈哈，表格生成成功啦！");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	
			

}

	public static void main(String[] args) {
		String matureName = "rno-miR-124";
		int len = 1750;
		for(int m = 0;m<=len;m+=50){
			String startIndex = String.valueOf(m);
					System.out.println("matureName : "+matureName+"===========startIndex: "+startIndex);
					creatExcel(matureName, startIndex);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}
		System.out.println("============所有文件完成生成excel");

	}
}