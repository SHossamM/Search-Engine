package com.indexer;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        InvertedIndex invertedIndex = new InvertedIndex();

        //invertedIndex.buildInitialInvertedIndex("C:/Users/Ahmkel/Documents/CUFE/Semester_10/APT/GP/FullProject/Crawler/pages");
        invertedIndex.updateInvertedIndex("C:/Users/Ahmkel/Documents/CUFE/Semester_10/APT/GP/FullProject/Crawler/pages");

        invertedIndex.close();



        /*

        MongoClient mongoClient;
        MongoDatabase indexDB;

        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        indexDB = mongoClient.getDatabase("indexDB");
        MongoCollection<Document> invertedIndexTerms = indexDB.getCollection("invertedIndexTerms");
        MongoCollection<Document> forwardIndexDocuments = indexDB.getCollection("forwardIndexDocuments");
        MongoCollection<Document> indexInformation = indexDB.getCollection("indexInformation");

        */




    }
}