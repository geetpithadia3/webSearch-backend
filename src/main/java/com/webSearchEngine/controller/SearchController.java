package com.webSearchEngine.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webSearchEngine.model.SearchResults;
import com.webSearchEngine.service.SearchService;

/**
 * The Class SearchController.
 */
@RestController
public class SearchController {

	/** The service. */
	@Autowired
	SearchService service;

	private static final Logger logger = Logger.getLogger(SearchController.class.getName());

	/**
	 * Gets the search results.
	 *
	 * @param query the query
	 * @return the search results
	 */
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping("/getSearchResults/{query}")
	public SearchResults getSearchResults(@PathVariable String query) {
		try {
			return service.getSearchResults(query.trim());
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.toString());
			throw e;
		}
	}

	/**
	 * Gets the suggestions.
	 *
	 * @param query the query
	 * @return the suggestions
	 */
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping("/getSuggestions/{query}")
	public List<String> getSuggestions(@PathVariable String query) {
		try {
			return service.getSuggestions(query.trim());
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.toString());
			throw e;
		}
	}

}
