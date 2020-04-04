package com.github.swapnil.word.indexers;

import java.util.Collection;

import com.github.swapnil.word.readers.FileReader;

public interface Indexer {
	public void index(Collection<String> files);

	public int getWordCount(String word);

	public void printDict();

	public void setFileReaderImpl(Class<? extends FileReader> readerImpl);
}
