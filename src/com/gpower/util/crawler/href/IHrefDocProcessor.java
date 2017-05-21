package com.gpower.util.crawler.href;
import org.jsoup.nodes.Document;
public interface IHrefDocProcessor {
	public void process(Document doc,HrefMapModel hrefMapModel);
}
