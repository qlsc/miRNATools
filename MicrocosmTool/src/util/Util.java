package util;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.GenBase;
import model.MicrocosmVo;
import model.ResultVo;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Util {
	public static void main(String[] args) {
		String threadUrl = "http://www.ebi.ac.uk/enright-srv/microcosm/cgi-bin/targets/v5/detail_view.pl?transcript_id=ENSRNOT00000034401";
		getMicrocosmByURL(threadUrl);
	}

	public static Map<String, List<?>> getMicrocosmByURL(String threadUrl) {
		Map<String, List<?>> map = new HashMap<String, List<?>>();
		try {
			// 解析url
			Document doc = readUrlFist(threadUrl);
			map = getData(doc);
		} catch (Exception e) {
			System.out.println("第三步，取得表格数据出错！");
			e.printStackTrace();
		}
		return map;
	}

	public static Map<String, List<?>> getData(Document doc) {
		Map<String, List<?>> map = new HashMap<String, List<?>>();
		List<String> filenamelist = new ArrayList<String>();
		List<MicrocosmVo> microlist = new ArrayList<MicrocosmVo>();
		try {
		
			String genename = doc.getElementsByTag("TABLE").get(1).select("TR")
					.get(1).select("TD").get(1).text();
			String transcript = doc.getElementsByTag("h2").get(0).text()
					.substring(19);
			filenamelist.add(genename + "_" + transcript.trim());
			// 表格标题
//			String tableTitle = doc.getElementsByTag("title").get(0).text();
			// 取得表格数据
			for (Element ele : doc.getElementsByTag("TABLE").get(3)
					.select("tr")) {
				if (!ele.select("td").toString().equals("")) {
					MicrocosmVo vo = new MicrocosmVo();
					int len = ele.select("td").size();
					if (len >= 8 && len <= 9) {
						vo.setValue0(ele.select("td").get(0).text());
					}
					microlist.add(vo);
				}
			}
		} catch (Exception e) {
			System.out.println("重试获取数据！");
			if ((e instanceof IndexOutOfBoundsException)||(e instanceof ArrayIndexOutOfBoundsException)) {
				map = getData(doc);
			}
		}
		map.put("filename", filenamelist);
		map.put("list", microlist);
		return map;
	}
	/**
	 * jsoup 连接不上重连
	 * @param url
	 * @return
	 */
	public static Document readUrlFist(String url) {
		Document doc = null;
		Connection conn = Jsoup.connect(url);
		conn.header(
				"User-Agent",
				"Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2 Googlebot/2.1");
		try {
			doc = conn.timeout(5 * 1000).get();
		} catch (IOException e) {
			System.out.println("重试连接！");
			if ((e instanceof UnknownHostException)
					|| (e instanceof SocketTimeoutException)) {
				doc = readUrlFist(url);
			}

		}
		return doc;
	}
	public static List<ResultVo> getResult(String genName,String filename) {
		List<ResultVo> list = new ArrayList<ResultVo>();
		try {
			 //解析url  
//	        String threadUrl = "http://www.targetscan.org/cgi-bin/vert_61/view_gene.cgi?rs=NM_002055&taxid=10116&members=&showcnc=1&shownc=1&showncf=0";  
//	        Document doc = (Document) Jsoup.connect(threadUrl).get();  
//	        System.out.println(doc);  
			// 解析html文档
			String filepath = "E:\\gen\\microcosm\\"+filename+".txt";
			File input = new File(filepath);
			Document doc = Jsoup.parse(input, "UTF-8");
			// System.out.println(doc.getElementsByTag("TABLE").get(1).select("TR"));
			for (Element ele : doc.getElementsByTag("div").get(1).select("map")) {
				if (!ele.select("area").toString().equals("")) {
					
					int len = ele.select("area").size();
					for(int i=0;i<len;i++){
						ResultVo result = new ResultVo();
						result.setGenName(genName);
						System.out.println(ele.select("area").get(i).attr("href").replace("#", ""));
						result.setResult(ele.select("area").get(i).attr("href").replace("#", ""));
						
						list.add(result);
					}
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public static List<GenBase> getGen(String filename) {
		List<GenBase> list = new ArrayList<GenBase>();
		try {
			// 解析html文档
			String path = "E:\\gen\\" + filename + ".txt";
			File input = new File(path);
			Document doc = Jsoup.parse(input, "UTF-8");
			// System.out.println(doc.getElementsByTag("TABLE").get(1).select("TR"));
			for (Element ele : doc.getElementsByTag("TABLE").select("TR")) {
				if (!ele.select("td").toString().equals("")) {
					GenBase gen = new GenBase();
					String geneName = ele.select("td").get(0).text();
					String transcript = ele.select("td").get(1).text();
					String description = ele.select("td").get(2).text();
					String score = ele.select("td").get(6).text();
					String energy = ele.select("td").get(7).text();
					String pvalue = ele.select("td").get(8).text();
					gen.setGeneName(geneName);
					gen.setTranscript(transcript);
					gen.setDescription(description);
					gen.setScore(score);
					gen.setEnergy(energy);
					gen.setPvalue(pvalue);
					list.add(gen);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}
