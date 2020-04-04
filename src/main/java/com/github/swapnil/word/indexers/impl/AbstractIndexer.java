package com.github.swapnil.word.indexers.impl;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.github.swapnil.word.indexers.Indexer;
import com.github.swapnil.word.readers.FileReader;

public abstract class AbstractIndexer implements Indexer {
	private static final Logger logger = Logger.getLogger(AbstractIndexer.class);

	private Class<? extends FileReader> readerImpl;

	@Override
	public void setFileReaderImpl(Class<? extends FileReader> readerImpl) {
		this.readerImpl = readerImpl;
	}

	protected void index(String file) {
		logger.debug("Indexing the file " + file);
		long startTime = System.currentTimeMillis();
		FileReader reader = null;

		try {
			reader = readerImpl.newInstance();
			reader.setFileInputStreamIfNull(file);
			while (reader.hasNext()) {
				index(reader.nextChunkInWords());
			}
		} catch (Exception e) {
			logger.error("Error while indexing file: " + file);
			e.printStackTrace();
		} finally {
			closeQuitely(reader);
			logger.debug("Finished Indexing file: " + file + " (" + ((System.currentTimeMillis() - startTime) / 1000) + " secs)");
		}
	}

	private void closeQuitely(FileReader reader) {
		if (reader != null) {
			reader.close();
		}
	}

	public abstract void index(String[] nextChunkInWords);

	public abstract void index(Collection<String> files);
}
