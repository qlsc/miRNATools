package util;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import model.MiRandaModel;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Util {
	public static void main(String[] args) {
		String threadUrl = "http://www.microrna.org/microrna/getTargets.do?matureName=rno-miR-31&startIndex=0&organism=10116";
		getMiRandaByURL(threadUrl);
	}

	/**
	 * url = http://www.microrna.org/microrna/getTargets.do?matureName=rno-miR-31&startIndex=0&organism=10116
	 * 参数 startIndex += 50
	 * @param threadUrl
	 */
	public static List<MiRandaModel> getMiRandaByURL(String threadUrl) {
		List<MiRandaModel> list = new ArrayList<MiRandaModel>();
		try {
			// 解析url
			Document doc = readUrlFist(threadUrl);
			list = getMiRandaData(doc);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	public static List<MiRandaModel> getMiRandaData(Document doc) {
		List<MiRandaModel> list = new ArrayList<MiRandaModel>();
		try {
			for (Element ele : doc.getElementsByClass("resultHeader").select("tbody > tr")) {
				if (!ele.select("td").toString().equals("")) {
					MiRandaModel model = new MiRandaModel();
					String geneSymbol = ele.select("td").get(1).select("strong").text();
					String refSeqID = ele.select("td").get(3).select("strong").text();
					model.setGeneSymbol(geneSymbol);
					model.setRefSeqID(refSeqID);
					list.add(model);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
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
}
