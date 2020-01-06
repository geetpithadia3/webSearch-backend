package com.webSearchEngine.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class Crawler {
	private List<String> pagesCrawledList = new ArrayList<String>();
	private List<Document> webPageList = new ArrayList<Document>();

	public List<Document> getWebPageList() {
		return webPageList;
	}

	public void setWebPageList(List<Document> webPageList) {
		this.webPageList = webPageList;
	}

	private int maxDepth;

	public Crawler() {
		webPageList = new ArrayList<Document>();
		pagesCrawledList = new ArrayList<String>();
		maxDepth = 2;
	}

	/**
	 * This method search for links in the web page and stores html in list
	 * 
	 * @param url   [web page url to be searched for urls]
	 * @param depth [depth of the urls to be crawled]
	 * @throws IOException
	 */
	public void startCrawler(String url, int depth) throws IOException {

		if (!pagesCrawledList.contains(url) && depth <= maxDepth) {

			Document document = Jsoup.connect(url).get();
			webPageList.add(document);
			depth++;
			if (depth < maxDepth) {
				Elements links = document.select("a[href]");
				for (Element page : links) {
					startCrawler(page.attr("abs:href"), depth);
					pagesCrawledList.add(page.attr("abs:href"));
				}
			}
		}

	}

}
