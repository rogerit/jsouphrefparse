package com.gpower.util.sitehealthutil;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DoUrlWidthFirst {
	public static void main(String[] args) {
		DoUrlWidthFirst duwf = new DoUrlWidthFirst();
		String rootsite = "http://dzb.bucea.edu.cn/";
		String siteurl = "http://localhost:9090/saron_Client";
		Document doc;
		duwf.parseHtmlInfo(rootsite,rootsite);

	}

	private void parseHtmlInfo(String siteurl,String basePath) {
		Document doc = null;
		try {
			doc = Jsoup.connect(siteurl).get();
			Elements es = doc.getElementsByTag("a");
			for (Element e : es) {
				String absHref = e.attr("abs:href");
				if (absHref.startsWith(basePath)) {
					System.out.println("~~~~~~~~~~~~~~~~~" + absHref);
				} else {
					System.out.println(absHref);
				}
			}
			
		} catch (IOException e1) {
			// e1.printStackTrace();
			System.out.println(e1.getMessage() + " " + siteurl);
		}
		
	}

}
