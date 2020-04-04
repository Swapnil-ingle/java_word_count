package com.github.swapnil.word;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.github.swapnil.word.indexers.Indexer;
import com.github.swapnil.word.indexers.impl.MultiThreadedFileIndexer;
import com.github.swapnil.word.readers.impl.FileChunkReader;

public class Driver {
	private static final Logger logger = Logger.getLogger(Driver.class);

	private static Indexer indexer = MultiThreadedFileIndexer.getInstance();

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		try {
			List<String> files = getFilesFromPath(args[0]);

			indexer.setFileReaderImpl(FileChunkReader.class);
			indexer.index(files);

			logger.debug("Total time: " + ((System.currentTimeMillis() - startTime) / 1000) + " secs");
			AppStarter.init();
		} catch (Exception e) {
			logger.error("Error while running application: ");
			e.printStackTrace();
		}
	}

	private static List<String> getFilesFromPath(String inputPath) {
		if (StringUtils.isEmpty(inputPath)) {
			logger.error("Please provide a directory to read");
		}

		File folder = new File(inputPath);
		return Arrays.stream(folder.listFiles(applyFilter())).map(file -> file.getAbsolutePath()).collect(Collectors.toList());
	}

	private static FilenameFilter applyFilter() {
		return new FilenameFilter() {
			@Override
			public boolean accept(File file, String name) {
				return name.endsWith(".txt");
			}
		};
	}

	private static class AppStarter {
		public static void init() {
			Scanner sc = new Scanner(System.in);

			System.out.println(WORD_COUNT_BANNER);
			System.out.println(HELP_TEXT);

			while (true) {
				System.out.println("Input the word for count:");
				String word = sc.next();

				if (word.equalsIgnoreCase("exitNow")) {
					System.out.println("Goodbye!");
					break;
				} else if (word.equalsIgnoreCase("printDict")) {
					System.out.print("Word Count ");
					indexer.printDict();
					continue;
				} else if (word.equalsIgnoreCase("getHelp")) {
					System.out.println(HELP_TEXT);
					continue;
				}

				System.out.println("Count: " + indexer.getWordCount(word.toLowerCase().trim()));
			}

			sc.close();
		}

		public static final String WORD_COUNT_BANNER = 
				"###################################################################################################\n" + 
				"##    ##      ##  #######  ########  ########      ######   #######  ##     ## ##    ## ######## ##\n" + 
				"##    ##  ##  ## ##     ## ##     ## ##     ##    ##    ## ##     ## ##     ## ###   ##    ##    ##\n" + 
				"##    ##  ##  ## ##     ## ##     ## ##     ##    ##       ##     ## ##     ## ####  ##    ##    ##\n" + 
				"##    ##  ##  ## ##     ## ########  ##     ##    ##       ##     ## ##     ## ## ## ##    ##    ##\n" + 
				"##    ##  ##  ## ##     ## ##   ##   ##     ##    ##       ##     ## ##     ## ##  ####    ##    ##\n" + 
				"##    ##  ##  ## ##     ## ##    ##  ##     ##    ##    ## ##     ## ##     ## ##   ###    ##    ##\n" + 
				"##     ###  ###   #######  ##     ## ########      ######   #######   #######  ##    ##    ##    ##\n" + 
				"###################################################################################################\n";

		public static final String HELP_TEXT = 
			"***HELP*** \n" +
			"  1. Type 'exitNow' to quit \n" +
			"  2. Type 'printDict' to print the entire words and count \n" +
			"***HELP***";
	}
}
