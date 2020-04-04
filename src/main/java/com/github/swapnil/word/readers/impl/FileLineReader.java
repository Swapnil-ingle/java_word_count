package com.github.swapnil.word.readers.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.github.swapnil.word.readers.FileReader;

public class FileLineReader implements FileReader {
	private static final Logger logger = Logger.getLogger(FileLineReader.class);

	private Scanner sc;

	private FileInputStream inputStream;

	public FileLineReader() {}

	public FileLineReader(String path) {
		setFileInputStream(path);
	}

	@Override
	public boolean hasNext() {
		return sc.hasNextLine();
	}

	@Override
	public String nextChunk() {
		return sc.nextLine();
	}

	@Override
	public String[] nextChunkInWords() {
		return sc.nextLine().split("\\s");
	}

	@Override
	public void close() {
		try {
			inputStream.close();
			sc.close();
		} catch (Exception e) {
			logger.error("Error while closing the file resources. " + e.getMessage());
		}
	}

	@Override
	public void setFileInputStreamIfNull(String path) {
		if (this.inputStream == null) {
			setFileInputStream(path);
		}
	}

	private void setFileInputStream(String path) {
		try {
			this.inputStream = new FileInputStream(path);
			this.sc = new Scanner(inputStream, "UTF-8");

			if (sc.ioException() != null) {
				throw sc.ioException();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
