## Problem Statement
**Input:**
1. Folder with many text files
2. Each file can be very large
3. There can be many files in the folder running into GBs
E.g. lots of eBooks in .txt format

**Write a program to:**
1. Count per unique word across all books
2. Given a word, show the count of the word

**Restrictions:**
1. Cannot use DB

## How to setup

1. `git clone https://github.com/Swapnil-ingle/java_word_count`

2. `gradle clean`

3. `gradle installApp`

## How to run

The shell script and batch script for running the program are generated in the dir **$Java_Word_Count/build/install/word-counter/bin** after the plugin is setup.

1. `cd $Java_Word_Count/build/install/word-counter/bin`

**Linux:**
> 2. `./tkids-merge-xls-script <ABSOLUTE-PATH-TO-INPUT-FILES>`

**Windows:**
> 2. `tkids-merge-xls-script <ABSOLUTE-PATH-TO-INPUT-FILES>`

**Note:**

1. This was tested on Gradle v2.0

2. The shell script and batch script for the program are in $Java_Word_Count/build/install/word-counter/bin.

## Iteration 1: Using Scanner Reader (Reading the file line-by-line)

**Performance Benchmark**
* 170 MB --> 12 Secs (~200 MB)
* 480 MB --> 30 Secs (~500 MB)
* 854 MB --> 60 Secs (~900MB)
* 1.1 GB --> 70 Secs
* 1.57 GB --> 103 Secs (~1.6 GB)
* 2.0 GB --> 125 Secs

> **Cons:** Reading line-by-line will cause much Disk IO overhead resulting in slower overall operation.

## Iteration 2: Using Buffered Reader

**Performance Benchmark**
* 400 MB --> 12 Secs (1 MB Chunk Size)
* 1.1 GB --> 35 Secs (1 MB Chunk Size)
* 2.0 GB --> 65 Secs (1 MB Chunk Size)

> **Pros:** Reading n bytes from the file is efficient than reading line-by-line and causes lower Disk IO operation.

> **Cons:** This will still procedurally read all the files one-by-one. This means the total time taken would be Summation of time required to process each of the underlying file.

## Iteration 3: Using Buffered Reader (With Multi-threaded Indexer)

**Performance Benchmark**
* 400 MB --> 10 Secs
* 1.0 GB --> 30 Secs
* 2.0 GB --> 65 Secs

**Upgrades:**
> This reads the underlying files parallelly. This means the total time taken would be equal to maximum time required for one of the underlying file.

**Note:** One more possible upgrade over this could be splitting large files (>750 MB) into sections and reading each section in separate thread.
