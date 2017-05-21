package com.gpower.util.crawler.href;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HrefMapModel {

	private Map<String, HrefMapModel> childrenHrefMap = new HashMap<String, HrefMapModel>();
	private List<IHrefDocProcessor> hrefDocProcessor = new ArrayList<IHrefDocProcessor>();
	private String href = null;
	private Number simHashCode = 0L;

	public Number getSimHashCode() {
		return simHashCode;
	}

	public void setSimHashCode(Number simHashCode) {
		this.simHashCode = simHashCode;
	}

	public String getHref() {
		return href;
	}

	public Map<String, HrefMapModel> getChildrenHrefMap() {
		return childrenHrefMap;
	}

	public void setChildrenHrefMap(Map<String, HrefMapModel> childrenHrefMap) {
		this.childrenHrefMap = childrenHrefMap;
	}

	public void mountChilrenHref(String href, HrefMapModel hrefMapModel) {

	}

	public void addHrefDocProcessor(IHrefDocProcessor hrefDocProcessor) {
		this.hrefDocProcessor.add(hrefDocProcessor);
	}

	public void setHref(String href) {
		this.href = href;
	}

	public HrefMapModel(String href) {
		super();
		this.href = href;
	}

	public HrefMapModel() {
	}

	public void hrefDocProcess() {
		Document doc = null;
		try {
			doc = Jsoup.connect(this.href).get();
			for (IHrefDocProcessor hdp : this.hrefDocProcessor) {
				try {
					hdp.process(doc, this);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			System.out.println(e1.getMessage() + " " + this.href);
		}
	}

	public static void main(String[] args) {
		HrefMapModel hmm = new HrefMapModel("http://dzb.bucea.edu.cn/");
		hmm.addHrefDocProcessor(new ChildrenHrefProcessor());
		hmm.hrefDocProcess();
	}

}
