package com.gpower.util.sitehealthutil;


import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


import com.gpower.algorithm.simhash.SimHash;
import com.gpower.algorithm.simhash.modifystring.MetaString;


public class SiteHealthUtil {
	private Map<String, Integer> allUrl = new HashMap<String, Integer>();
	// roger
	private Map<String, Number> allUrlSimHash = new HashMap<String, Number>();
	private Map<String, String> sensitiveUrl = new HashMap<String, String>();
	private Map<String, String> notFoundUrl = new HashMap<String, String>();
	private Map<String, String> dangerUrl = new HashMap<String, String>();

	/**
	 * @author zhuyy 下面为新增的 关键词以及包含关键词的链接
	 */
	private Map<String, String> impwordUrl = new HashMap<String, String>();
	/**
	 * 判断是否含有设置的风险挂马链接
	 * 
	 */
	private boolean isTrojan = false;

	public Map<String, String> getDangerUrl() {
		return dangerUrl;
	}

	public void setDangerUrl(Map<String, String> dangerUrl) {
		this.dangerUrl = dangerUrl;
	}


	private String domain;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Map<String, Integer> getAllUrl() {
		return allUrl;
	}

	public void setAllUrl(Map<String, Integer> allUrl) {
		this.allUrl = allUrl;
	}

	public Map<String, String> getNotFoundUrl() {
		return notFoundUrl;
	}

	public void setNotFoundUrl(Map<String, String> notFoundUrl) {
		this.notFoundUrl = notFoundUrl;
	}

	public Map<String, String> getSensitiveUrl() {
		return sensitiveUrl;
	}

	public void setSensitiveUrl(Map<String, String> sensitiveUrl) {
		this.sensitiveUrl = sensitiveUrl;
	}


	public Map<String, String> getImpwordUrl() {
		return impwordUrl;
	}

	public void setImpwordUrl(Map<String, String> impwordUrl) {
		this.impwordUrl = impwordUrl;
	}


	public boolean isTrojan() {
		return isTrojan;
	}

	public void setTrojan(boolean isTrojan) {
		this.isTrojan = isTrojan;
	}

	public void doUrl(String url, String baseUrl) throws Exception {

		String uhost = null;
		String uhost1 = null;

		if (!url.startsWith(domain)) {
			return;
		}
		if (!url.startsWith("http:")) {
			return;
		}
		if (url.startsWith(domain + ":8080")) {
			return;
		}
		if (url.indexOf("void(")>0){
			return;
		}

		// roger
		try {
			URL u = new URL(url);
			System.out.println(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.connect();
			int i = conn.getResponseCode();
			if (200 != i && 301 != i && 302 != i && 403 != i) {
				this.getNotFoundUrl().put(url, baseUrl);
				return;
			}
			uhost = u.getHost();
		} catch (Exception e1) {
			// this.getNotFoundUrl().put(url, baseUrl);
			return;
		}

		String content = null;
		try {
			content = this.getUrlHtml(url);
		} catch (Exception e) {
			// this.getNotFoundUrl().put(url, baseUrl);
			return;
		}
		//Parser parser = Parser.createParser(content, "UTF-8");
		//NodeList list = parser.extractAllNodesThatMatch(new TagNameFilter("a"));
		for (int i = 0; i < 2; i++) {//list.size()
		//	LinkTag node = (LinkTag) list.elementAt(i);
			//String link = node.getLink();
			String link = null;
			/*
			 * URL u2 = new URL(link); uhost1=u2.getHost();
			 */
			if (link != null && !"".equals(link) && !"#".equals(link)
					&& !";".equals(link)) {
				/*
				 * if(!link.startsWith("http://") &&
				 * !link.startsWith("https://")){ link = url+"/"+link; }
				 */
				// roger
				URL cUrl = this.constructUrl(link, url, false);
				if (cUrl != null) {
					link = cUrl.toString();
				}

				// System.out.println(link);

				if (!this.getAllUrl().containsKey(link)) {
					if (link.startsWith(this.getDomain())) {
						this.getAllUrl().put(link, null);

						// roger
						this.getAllUrlSimHash().put(link, null);
					} else {
						if (this.getDangerUrl().entrySet().contains(link)) {
							String val = this.getDangerUrl().get(link)
									.concat("," + url);
							this.getDangerUrl().put(link, val);

						} else {
							this.getDangerUrl().put(link, url);
						}

					}
					this.doUrl(link, url);
				} else {
					if (this.getNotFoundUrl().entrySet().contains(link)) {
						String val = this.getNotFoundUrl().get(link)
								.concat("," + url);
						this.getNotFoundUrl().put(link, val);

					}
				}
			}
		}

	}

	public String getUrlHtml(String htmlurl) throws Exception {
		URL url;

		// roger

		String result;
		try {
			result = new MetaString(new URL(htmlurl)).getText();
			// System.out.println(result.length());
			// System.out.println(result);
			this.getAllUrl().put(htmlurl, result.hashCode());
			Long temp = SimHash.Hash64(result);
			this.getAllUrlSimHash().put(htmlurl, temp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return result;

	}


	public URL constructUrl(String link, String base, boolean strict) {
		String path;
		boolean modified;
		boolean absolute;
		int index;
		// roger
		URL url = null; // constructed URL combining relative link and base

		// Bug #1461473 Relative links starting with ?
		try {
			if (!strict && ('?' == link.charAt(0))) {
				// remove query part of base if any
				if (-1 != (index = base.lastIndexOf('?')))
					base = base.substring(0, index);
				url = new URL(base + link);
			} else
				url = new URL(new URL(base), link);
			path = url.getFile();
			modified = false;
			absolute = link.startsWith("/");
			if (!absolute) {
				// we prefer to fix incorrect relative links
				// this doesn't fix them all, just the ones at the start
				while (path.startsWith("/.")) {
					if (path.startsWith("/../")) {
						path = path.substring(3);
						modified = true;
					} else if (path.startsWith("/./") || path.startsWith("/.")) {
						path = path.substring(2);
						modified = true;
					} else
						break;
				}
			}
			// fix backslashes
			while (-1 != (index = path.indexOf("/\\"))) {
				path = path.substring(0, index + 1) + path.substring(index + 2);
				modified = true;
			}
			if (modified)
				url = new URL(url, path);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			//roger
			System.out.println("constructUrl error:" + link );

		}

		return (url);
	}

	// roger
	public Map<String, Number> getAllUrlSimHash() {
		return allUrlSimHash;
	}

	public void setAllUrlSimHash(Map<String, Number> allUrlSimHash) {
		this.allUrlSimHash = allUrlSimHash;
	}
}
