package com.github.swapnil.word.readers;

import java.io.IOException;

public interface FileReader {
	public boolean hasNext() throws IOException;

	public String nextChunk(); 

	public String[] nextChunkInWords();

	public void setFileInputStreamIfNull(String path);

	public void close();
}
