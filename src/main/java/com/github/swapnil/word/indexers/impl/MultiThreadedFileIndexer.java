package com.github.swapnil.word.indexers.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

public class MultiThreadedFileIndexer extends AbstractIndexer {
	private static final Logger logger = Logger.getLogger(MultiThreadedFileIndexer.class);

	private static Map<String, AtomicInteger> cacheMap = new ConcurrentHashMap<>();

	private static MultiThreadedFileIndexer instance;

	private MultiThreadedFileIndexer() {
	}

	public static synchronized MultiThreadedFileIndexer getInstance() {
		return instance == null ? new MultiThreadedFileIndexer() : instance;
	}

	@Override
	public void index(Collection<String> files) {
		logger.info("Indexing files...");
		ExecutorService executorService = Executors.newFixedThreadPool(10);

		List<Runnable> indexingTasks = new ArrayList<>();
		readyIndexingTasksQueue(files, indexingTasks);

		List<Future> indexingTasksResults = new ArrayList<>();
		submitIndexingTasks(executorService, indexingTasks, indexingTasksResults);

		waitForIndexingToComplete(indexingTasksResults);
		executorService.shutdown();
	}

	@Override
	public int getWordCount(String word) {
		AtomicInteger count = cacheMap.get(word);
		return count == null ? 0 : count.get();
	}

	@Override
	public void printDict() {
		System.out.println("Dictionary: ");
		System.out.println(cacheMap.toString());
	}

	private void readyIndexingTasksQueue(Collection<String> files, List<Runnable> indexingTasks) {
		files.forEach(file -> {
			indexingTasks.add(new Runnable() {
				@Override
				public void run() {
					index(file);
				}
			});
		});
	}

	public void index(String[] words) {
		Arrays.stream(words)
		.map(word -> word.toLowerCase().trim())
		.forEach(word -> {
			if (cacheMap.containsKey(word)) {
				cacheMap.get(word).incrementAndGet();
			} else {
				cacheMap.put(word, new AtomicInteger(1));
			}
		});
	}

	private void submitIndexingTasks(ExecutorService executorService, List<Runnable> indexingTasks,
			List<Future> indexingTasksResults) {
		indexingTasksResults
				.addAll(indexingTasks.stream().map(task -> executorService.submit(task)).collect(Collectors.toList()));
	}

	private void waitForIndexingToComplete(List<Future> indexingTasksResults) {
		logger.info("Waiting for completion of file indexing...");
		indexingTasksResults.forEach(result -> {
			try {
				result.get();
			} catch (InterruptedException e) {
				logger.error("File indexing task interrupted");
				e.printStackTrace();
			} catch (ExecutionException e) {
				logger.error("Error occured while executing indexing task");
				e.printStackTrace();
			}
		});
		logger.info("Indexing completed!");
	}
}
