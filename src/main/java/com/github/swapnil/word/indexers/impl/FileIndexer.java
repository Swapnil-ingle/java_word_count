package com.github.swapnil.word.indexers.impl;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class FileIndexer extends AbstractIndexer {
	private static final Logger logger = Logger.getLogger(FileIndexer.class);

	private static Map<String, Integer> cacheMap = new HashMap<>();

	private static FileIndexer instance;

	private FileIndexer() {
	}

	public static synchronized FileIndexer getInstance() {
		return instance == null ? new FileIndexer() : instance;
	}

	@Override
	public void index(Collection<String> files) {
		logger.info("Indexing files...");
		files.forEach(file -> index(file));
		logger.info("Indexing completed!");
	}

	@Override
	public int getWordCount(String word) {
		return cacheMap.getOrDefault(word.toLowerCase(), 0);
	}

	@Override
	public void printDict() {
		System.out.println("Dictionary: ");
		System.out.println(cacheMap.toString());
	}

	public void index(String[] words) {
		Arrays.stream(words).forEach(word -> cacheMap.merge(word.toLowerCase().trim(), 1, Integer::sum));
	}
}