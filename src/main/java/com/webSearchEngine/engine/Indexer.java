package com.webSearchEngine.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.webSearchEngine.model.DocFrequency;
import com.webSearchEngine.model.DocInfo;
import com.webSearchEngine.model.DocRank;

import textprocessing.TST;

@Component
public class Indexer {

	@Autowired
	Crawler crawler;

	private static final Logger logger = Logger.getLogger(Indexer.class.getName());

	private TST<List<DocFrequency>> indexedTerms;
	private HashMap<Integer, DocInfo> documentIdNameMap;
	String stemmedWord = "";
	int id = 0;

	public Indexer() {
		indexedTerms = new TST<List<DocFrequency>>();
		documentIdNameMap = new HashMap<Integer, DocInfo>();
	}

	public void indexDocuments() throws IOException {
		if (crawler.getWebPageList().size() <= 0) {
			logger.log(Level.INFO, "Crawling Started");
			crawler.startCrawler("https://towardsdatascience.com/data-science/home", 0);
			logger.log(Level.INFO, "Pages Crawled:" + crawler.getWebPageList().size());
		}

		List<String> tokenWords;
		logger.log(Level.INFO, "Indexing Started");
		for (Document doc : crawler.getWebPageList()) {
			double documentLength = doc.body().text().toLowerCase().length();
			documentIdNameMap.put(id, new DocInfo(doc.head().getElementsByTag("title").text(), doc.baseUri()));
			tokenWords = Parser.parse(doc);

			tokenWords.stream().filter(word1 -> word1.trim().length() > 1 || word1.length() > 1).forEach(word -> {
				stemmedWord = word;
				if (null == indexedTerms.get(stemmedWord)) {
					indexedTerms.put(stemmedWord, new ArrayList<DocFrequency>());
					indexedTerms.get(stemmedWord).add(new DocFrequency(id, 1, documentLength));
				} else {
					List<DocFrequency> docList = indexedTerms.get(stemmedWord);
					if (docList.contains(new DocFrequency(id))) {
						DocFrequency docFreqObj = docList.get(docList.indexOf((new DocFrequency(id, documentLength))));
						docFreqObj.addOccurrence();
					} else {
						DocFrequency newDoc = new DocFrequency(id, documentLength);
						newDoc.addOccurrence();
						docList.add(newDoc);
					}
				}

			});
			id++;
		}
		logger.log(Level.INFO, "Indexing Completed");
	}

	public List<DocRank> tfIdf(String term) {
		List<DocRank> docRankList = new ArrayList<DocRank>();
		if (indexedTerms.get(term) != null) {
			double docListLength = indexedTerms.get(term).size();
			for (DocFrequency doc : indexedTerms.get(term)) {
				docRankList
						.add(new DocRank(doc.getDocumentId(), documentIdNameMap.get(doc.getDocumentId()).getDocTitle(),
								documentIdNameMap.get(doc.getDocumentId()).getDocLink(),
								doc.getTermFrequency() * (docListLength / 100)));
			}
		}
		return docRankList;
	}

	public List<DocRank> getFilteredDocuments(String query) {
		String[] queryTokens = query.split(" ");

		List<DocRank> filteredDocumentsList = tfIdf(queryTokens[0]);
		for (int i = 1; i < queryTokens.length; i++) {
			for (DocRank doc : tfIdf(queryTokens[i])) {
				if (filteredDocumentsList.contains(doc)) {
					filteredDocumentsList.get(filteredDocumentsList.indexOf(doc)).addTfIdf(doc.getTfIdf());
				}
			}
		}
		filteredDocumentsList.sort((c1, c2) -> Double.compare(c2.getTfIdf(), c1.getTfIdf()));
		return filteredDocumentsList;
	}

	public TST<List<DocFrequency>> getIndexedTerms() {
		return indexedTerms;
	}

//	public static void main(String args[]) throws IOException {
//		new Indexer().indexDocuments();
//
//		String query = "authoriny tool";
//
////		query = spellCheck(query);
//
//		List<DocRank> docs = new ArrayList<DocRank>();
//		docs = getFilteredDocuments(query);
//
//		for (DocRank doc : docs) {
//			System.out.println(documentIdNameMap.get(doc.getDocumenId()).getDocTitle() + ":" + doc.getTfIdf());
//		}

//	}

}
