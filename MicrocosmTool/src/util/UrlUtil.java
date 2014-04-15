package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.GTVo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class UrlUtil {
	public static void main(String[] args) {
		String threadUrl = "http://www.ebi.ac.uk/enright-srv/microcosm/cgi-bin/targets/v5/hit_list.pl?genome_id=5171&mirna_id=&external_name=Mdh1&gene_id=&go_class=function&go_term=&logic=phrase&terms=";
		getMicrocosmByURL(threadUrl,null);
	}
	public static List<GTVo> getMicrocosmByURL(String threadUrl,String external_name) {
		List<GTVo> list = new ArrayList<GTVo>();
		Document doc = null;
		try {
			// url
			 doc = (Document) Jsoup.connect(threadUrl).get();
		} catch (Exception e) {
			System.out.println("Socket time out try again");
			try {
				doc = (Document) Jsoup.connect(threadUrl).timeout(5000).get();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		try {
			for (Element ele : doc.getElementsByTag("TABLE").get(1).select("tr")) {
				if (!ele.select("td").toString().equals("")) {
					if(ele.select("td").size()>=2){
						if(ele.select("td").get(1).select("a").attr("href").startsWith("http://")){
							GTVo vo = new GTVo();
							vo.setTranscript(ele.select("td").get(1).select("a").text());
							list.add(vo);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
