package com.gpower.util.crawler.href;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ChildrenHrefProcessor implements IHrefDocProcessor {

	@Override
	public void process(Document doc,HrefMapModel hrefMapModel) {
		System.out.println(doc.toString());
		Elements es = doc.getElementsByTag("a");
		for (Element e : es) {
			String absHref = e.attr("abs:href");
			System.out.println(absHref);
		}
	}

}
