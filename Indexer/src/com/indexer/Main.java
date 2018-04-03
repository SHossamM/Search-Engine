package com.indexer;

import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.FileIterator;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.Alphabet;
import cc.mallet.types.IDSorter;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Main {

    static final String pagesPath = "C:/Users/Ahmkel/Documents/CUFE/Semester_10/APT/FinalPhase1/Search-Engine/Crawler/pages";

    public static void main(String[] args) throws Exception {

        InvertedIndex invertedIndex;

        if(args.length > 0 && args[0].equals("-init")) {
            System.out.println("Clearing the index database");
            invertedIndex = new InvertedIndex();
            invertedIndex.clearIndexDB();
            invertedIndex.close();
        }

        while (true) {
            System.out.println("Updating the index database");
            invertedIndex = new InvertedIndex();
            invertedIndex.updateInvertedIndex(pagesPath);
            invertedIndex.close();
            Thread.sleep(60000);
        }
    }
}