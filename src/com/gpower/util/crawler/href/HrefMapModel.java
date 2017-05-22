package com.gpower.util.crawler.href;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HrefMapModel {

	public static final int PROCESS_ERR = 2; // error;
	public static final int PROCESS_ED = 1; // has been processED;
	public static final int PROCESS_EL = 1001; // extern link
												// IGnored;
	public static final int PROCESS_TP = -1; // To Process;

	private Map<String, HrefMapModel> childrenHrefMap = null;
	private List<IHrefDocProcessor> hrefDocProcessor = null;
	private String href = null;
	private Number simHashCode = 0L;
	private int status = PROCESS_TP;

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

	public List<IHrefDocProcessor> getHrefDocProcessor() {
		return hrefDocProcessor;
	}

	public void setHrefDocProcessor(List<IHrefDocProcessor> hrefDocProcessor) {
		this.hrefDocProcessor = hrefDocProcessor;
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

		if (this.getStatus() != HrefMapModel.PROCESS_TP) {
			return;
		}

		try {
			doc = Jsoup.connect(this.href).get();
			System.out.println(this.href);

			for (IHrefDocProcessor hdp : this.hrefDocProcessor) {
				try {
					hdp.process(doc, this);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			this.setStatus(PROCESS_ED);
		} catch (IOException e1) {
			// System.out.println("thisisdoc");
			// System.out.println(e1.getMessage() + " " + this.href);
			// 待添加错链断链处理...doc.xls处理
			this.setStatus(HrefMapModel.PROCESS_ERR);
		}
	}

	public void deepFirst(HrefMapModel hrefMapModel) {
		// stop to avoid circle grabing href!
		if (hrefMapModel.getStatus() != HrefMapModel.PROCESS_TP) {
			return;
		}

		hrefMapModel.hrefDocProcess();
		if (hrefMapModel.getChildrenHrefMap() != null) {
			for (HrefMapModel h : hrefMapModel.getChildrenHrefMap().values()) {
				deepFirst(h);
			}
		}
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
	
	public static void main(String[] args) {
		HrefMapModelBuilder hmmb = new HrefMapModelBuilder(
				"http://dzb.bucea.edu.cn/");
		HrefMapModel hmm = hmmb.build("http://dzb.bucea.edu.cn/");

		hmm.deepFirst(hmm);
		ChildrenHrefProcessor chp = (ChildrenHrefProcessor) hmmb
				.getHrefDocProcessor().get(0);
		Map<String, HrefMapModel> encounteredHref = chp.getEncounteredHref();
		for (String str : encounteredHref.keySet()) {
			if (encounteredHref.get(str).getStatus() != HrefMapModel.PROCESS_ED) {
				encounteredHref.remove(str);
			}
		}
		System.out.println(chp.getEncounteredHref().size());
	}

}
