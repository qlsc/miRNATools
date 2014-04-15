package main;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import model.GTVo;
import model.MicrocosmVo;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import util.ReadExcelUtil;
import util.Util;

public class MainTool {
public static void creatExcel(String page,String transcript){
	String threadUrl = "http://www.ebi.ac.uk/enright-srv/microcosm/cgi-bin/targets/v5/detail_view.pl?transcript_id="+transcript;
	String filename = "rno-mir-124_page"+page;

		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(filename);
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 设置列宽    
	    sheet.setColumnWidth(0, 4500); 
	    sheet.setColumnWidth(1, 4500); 
	    
	    
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 创建一个居中格式

		
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		Map map = Util.getMicrocosmByURL(threadUrl);
		String microfile = ((List) map.get("filename")).get(0).toString();
		System.out.println(microfile);
		String geneName = microfile.split("_")[0];
		
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue(geneName);
		cell.setCellStyle(style);
		cell = row.createCell((short) 1);
		cell.setCellValue("microcosm");
		cell.setCellStyle(style);

		
		List list = (List) map.get("list");
		
		
		for (int i = 1; i < list.size(); i++)
		{
			row = sheet.createRow((int) i );
			MicrocosmVo base = (MicrocosmVo) list.get(i);
			// 第四步，创建单元格，并设置值
			row.createCell((short) 1).setCellValue(base.getValue0());
		}
		// 第六步，将文件存到指定位置
		String excelFileName = "E:\\gen\\124a\\microcosm0414\\Hit infomation\\page - 副本 ("+page+")\\"+microfile+".xls";
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
		int len = 12;
		String path = "E:\\gen\\124a\\microcosm0414\\rno-mir-124_page";
		for(int m = 8;m<=len;m++){
			String page = String.valueOf(m);
			String filePath = path+page+".xls";
			// String external_name = "N4bp3";
//			List urlList = ReadTxtUtil.readTxtFile(filePath);//txt
			List urlList = ReadExcelUtil.readExcel(filePath);//excel
			if (!urlList.isEmpty()) {
				for (int j = 0; j < urlList.size(); j++) {
					GTVo gtVo = (GTVo) urlList.get(j);
					String trans = gtVo.getTranscript();
					System.out.println("page["+page+"] 去掉序号: "+(j+1)+"===========序号: "+(j+2)+"    "+trans);
					creatExcel(page, trans);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			System.out.println("=======报告长官，rno-mir-124_page"+m+".txt 生成文件全部完成================");
		}
		System.out.println("============所有文件完成生成excel");

	}
}