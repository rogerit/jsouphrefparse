package com.gpower.util.crawler.href;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HrefMapModelBuilder {

	private List<IHrefDocProcessor> hrefDocProcessor = null;

	//private Map<String, HrefMapModel> encounteredHref = new HashMap<String, HrefMapModel>();

	public List<IHrefDocProcessor> getHrefDocProcessor() {
		return hrefDocProcessor;
	}

	public void setHrefDocProcessor(List<IHrefDocProcessor> hrefDocProcessor) {
		this.hrefDocProcessor = hrefDocProcessor;
	}


	/*public Map<String, HrefMapModel> getEncounteredHref() {
		return encounteredHref;
	}

	public void setEncounteredHref(Map<String, HrefMapModel> encounteredHref) {
		this.encounteredHref = encounteredHref;
	}*/

	public HrefMapModelBuilder(String basePath) {

		this.hrefDocProcessor = new ArrayList<IHrefDocProcessor>();
		/*
		 * process children href:
		 * 
		 * @para: builder Use a HMM builder to builds all of the children HMM;
		 * 
		 * @basePath: Filter the HMM that not start with basePath;
		 */
		ChildrenHrefProcessor childrenHrefProcessor = new ChildrenHrefProcessor(
				basePath, this);
		//childrenHrefProcessor.setEncounteredHref(this.encounteredHref);
		childrenHrefProcessor.setEncounteredHref(new HashMap<String, HrefMapModel>());
		this.hrefDocProcessor.add(childrenHrefProcessor);

		// to add more hrefDocProcessor
	}

	public HrefMapModel build(String href) {

		HrefMapModel hmm = new HrefMapModel(href);
		hmm.setHrefDocProcessor(this.hrefDocProcessor);
		return hmm;

	}

}
