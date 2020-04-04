package com.github.swapnil.word.readers.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.github.swapnil.word.readers.FileReader;

public class FileChunkReader implements FileReader {
	private static final Logger logger = Logger.getLogger(FileChunkReader.class);

	private static int CHUNK_SIZE = 2 * (1024 * 1024);

	private byte buffer[] = new byte[CHUNK_SIZE];

	private FileInputStream inputStream;

	public FileChunkReader() {}

	public FileChunkReader(String path) {
		setFileInputStream(path);
	}

	@Override
	public void setFileInputStreamIfNull(String path) {
		if (this.inputStream == null) {
			setFileInputStream(path);
		}
	}

	@Override
	public boolean hasNext() throws IOException {
		return inputStream.read(buffer) != -1;
	}

	@Override
	public String nextChunk() {
		return new String(buffer);
	}

	@Override
	public String[] nextChunkInWords() {
		return new String(buffer).split("\\s");
	}

	@Override
	public void close() {
		try {
			inputStream.close();
		} catch (Exception e) {
			logger.error("Error while closing the file resources. " + e.getMessage());
		}
	}

	private void setFileInputStream(String path) {
		try {
			this.inputStream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
