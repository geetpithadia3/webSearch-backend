package com.webSearchEngine.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.webSearchEngine.engine.Indexer;
import com.webSearchEngine.engine.SpellChecker;
import com.webSearchEngine.model.SearchResults;

/**
 * The Class SearchService.
 */
@Component
public class SearchService {

	/** The indexer. */
	@Autowired
	Indexer indexer;

	/** The spell checker. */
	@Autowired
	SpellChecker spellChecker;

	/**
	 * Initialize crawler and indexer.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@PostConstruct
	public void initializeCrawlerAndIndexer() throws IOException {
		indexer.indexDocuments();
	}

	/**
	 * Gets the search results.
	 *
	 * @param query the query
	 * @return the search results
	 */
	public SearchResults getSearchResults(String query) {
		String finalQuery = spellChecker.spellCheck(query);
		SearchResults searchResults = new SearchResults();
		searchResults.setQuery(finalQuery.trim());
		double startTime=System.nanoTime();
		searchResults.setResults(indexer.getFilteredDocuments(finalQuery));
		searchResults.setTimeTaken((System.nanoTime()-startTime)/1000);
		return searchResults;
	}

	/**
	 * Gets the suggestions.
	 *
	 * @param query the query
	 * @return the suggestions
	 */
	public List<String> getSuggestions(String query) {

		String[] tokens = query.split(" ");
		String suggestedQueryPrefix = "";
		if (tokens.length > 1)
			suggestedQueryPrefix = tokens[0];

		ArrayList<String> array = new ArrayList<String>();
		for (String key : indexer.getIndexedTerms().keys()) {
			if (key.startsWith(tokens[tokens.length - 1])) {
				array.add(suggestedQueryPrefix + " " + key);
			}
		}
		return array;
	}

}
