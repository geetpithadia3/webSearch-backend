package com.webSearchEngine.engine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;

public class Parser {

	private static List<String> stopWords = new ArrayList<String>();

	public static void buildStopWordsList() {
		stopWords.add("a");
		stopWords.add("the");
		stopWords.add("and");
		stopWords.add("are");
		stopWords.add("an");
		stopWords.add("as");
		stopWords.add("at");
		stopWords.add("be");
		stopWords.add("by");
		stopWords.add("for");
		stopWords.add("from");
		stopWords.add("has");
		stopWords.add("he");
		stopWords.add("is");
		stopWords.add("its");
		stopWords.add("of");
		stopWords.add("on");
		stopWords.add("it");
		stopWords.add("that");
		stopWords.add("was");
		stopWords.add("to");
		stopWords.add("were");
		stopWords.add("will");
		stopWords.add("with");

	}

	// removing the stop words from the tokens
	public static void removeStopWords(List<String> inputList) {
		inputList.removeIf(ip -> stopWords.contains(ip));
	}

	public static List<String> parse(Document doc) throws IOException {
		List<String> tokenList = new ArrayList<String>();
		if (stopWords.isEmpty())
			buildStopWordsList();

		String bodyTextStr = new String(doc.body().text().toLowerCase());
		String[] tokens = bodyTextStr.toString().split("[^a-zA-Z0-9'-]");
		for (String token : tokens)
			tokenList.add(token);
		removeStopWords(tokenList);
		return tokenList;

	}

	public static void main(String args[]) {
		String r[] = "Hello how-art you o'neil".split("[^a-zA-Z0-9'-]");
		for (String a : r)
			System.out.println(a);
	}
}
