package com.indexer;


import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.UpdateOptions;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import org.bson.Document;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;


public class InvertedIndex implements MongoDBBulkDocumentsSerializable{

    private class PostingsList implements MongoDBDocumentSerializable {
        private final Integer documentId;
        private final List<Integer> termPositions;
        private final double termFrequency;
        private final boolean inTitle;
        private final boolean inMetaKeywords;

        public PostingsList(Integer documentId, List<Integer> termPositions, Boolean inTitle, Boolean inMetaKeywords, Integer documentLength) {
            this.documentId = documentId;
            this.termPositions = termPositions;
            this.termFrequency = (double)this.termPositions.size() / (double)documentLength;
            this.inTitle = inTitle;
            this.inMetaKeywords = inMetaKeywords;
        }

        @Override
        public Document toBSONDocument() {
            //System.out.println(documentId);
            return new Document("documentId", documentId)
                    .append("termPositions", termPositions)
                    .append("termFrequency", termFrequency)
                    .append("inTitle", inTitle)
                    .append("inMetaKeywords", inMetaKeywords);
        }
    }

    private class TermData implements MongoDBDocumentSerializable {
        private Double inverseDocumentFrequency;
        private List<PostingsList> postingsLists;

        private TermData() {
            postingsLists = new LinkedList<>();
            inverseDocumentFrequency = 0d;
        }

        private List<PostingsList> getPostingsLists(){
            return postingsLists;
        }

        private void setInverseDocumentFrequency(Double inverseDocumentFrequency) {
            this.inverseDocumentFrequency = inverseDocumentFrequency;
        }

        private void insertPostingsList(Integer documentId, List<Integer> termPositions, Boolean inTitle, Boolean inMetaKeywords, Integer documentLength) {
            postingsLists.add(new PostingsList(documentId, termPositions, inTitle, inMetaKeywords, documentLength));
        }

        @Override
        public Document toBSONDocument(){

            List<Document> postingsListsDocuments = postingsLists.stream().map(PostingsList::toBSONDocument).collect(Collectors.toList());

            //Build the term document
            return new Document("inverseDocumentFrequency", inverseDocumentFrequency)
                    .append("postingsLists", postingsListsDocuments);
        }
    }


    private Set<Integer> uniqueDocuments;
    private Map<String, TermData> invertedIndex = new HashMap<>();
    private MongoClient mongoClient;
    private MongoDatabase indexDB;
    private Integer totalNumOfDocuments;


    public InvertedIndex() {
        uniqueDocuments = new HashSet<>();
        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        indexDB = mongoClient.getDatabase("indexDB");
        totalNumOfDocuments = 0;
    }


    private void updateIDF(){
        for(Map.Entry<String, TermData> entry: invertedIndex.entrySet()){
            entry.getValue().setInverseDocumentFrequency(
                    -1d * Math.log(((double) entry.getValue().getPostingsLists().size()) / ((double) totalNumOfDocuments))
            );
        }
    }



    private void insertNewDocumentId(Integer documentId){
        this.uniqueDocuments.add(documentId);
    }

    private Boolean containsTerm(String term) {
        return invertedIndex.containsKey(term);
    }

    private void insertTerm(String term) {
        invertedIndex.put(term, new TermData());
    }

    private void _insertTermPositionsInDocument(String term, Integer documentId, List<Integer> termPositions, Boolean inTitle, Boolean inMetaKeywords, Integer documentLength) {
        invertedIndex.get(term).insertPostingsList(documentId, termPositions, inTitle, inMetaKeywords, documentLength);
    }

    private void insertTermPositionsInDocument(String term, Integer documentId, List<Integer> termPositions, Boolean inTitle, Boolean inMetaKeywords, Integer documentLength) {
        // If inverted index doesn't contain term add it.
        if (!containsTerm(term)) {
            //System.out.println(String.format("Term: %s - Count: %d", term, invertedIndex.size()));
            insertTerm(term);
        }
        insertNewDocumentId(documentId);
        //Add position of the token in the current document to the inverted index.
        _insertTermPositionsInDocument(term, documentId, termPositions, inTitle, inMetaKeywords, documentLength);
    }

    @Override
    public List<Document> toBSONDocuments() {
        List<Document> termDocuments = new ArrayList<>();

        for (Map.Entry<String, TermData> entry : invertedIndex.entrySet()) {

            //Build the term document
            Document termDocument = entry.getValue().toBSONDocument().append("term", entry.getKey());

            //Add the term document to the list of documents
            termDocuments.add(termDocument);
        }
        return termDocuments;
    }


    private static Document serializeDocumentTermsData(Integer documentId, Map<String, LinkedList<Integer>> termPositions){
        List<Document> documentTermsData = new LinkedList<>();
        Integer currTermFrequency;
        Integer maxTermFrequency=0;

        for(String term : termPositions.keySet()) {
            currTermFrequency = termPositions.get(term).size();
            maxTermFrequency = Math.max(maxTermFrequency, currTermFrequency);

            documentTermsData.add(new Document("term", term).append("termFrequency", currTermFrequency));
        }

        return new Document("documentId", documentId).append("maxTermFrequency", maxTermFrequency).append("terms", documentTermsData);
    }






    private Boolean checkCollectionExists(String collectionName){
        for(String _collectionName: indexDB.listCollectionNames()){
            if(collectionName.equalsIgnoreCase(_collectionName)){
                return true;
            }
        }
        return false;
    }


    private MongoCollection<Document> dropAndCreateCollection(String collectionName){
        if(checkCollectionExists(collectionName)){
            indexDB.getCollection(collectionName).drop();
        }
        indexDB.createCollection(collectionName);
        return indexDB.getCollection(collectionName);
    }


    private Document buildIndexInformationDocument(){
        return new Document("numOfTerms", invertedIndex.size())
                .append("numOfDocuments", totalNumOfDocuments)
                .append("lastUpdated",  new Date());
    }


    public void clearIndexDB(){
        // Drop and create indexer collections
        dropAndCreateCollection("indexInformation");
        dropAndCreateCollection("forwardIndexDocuments");

        MongoCollection<Document> invertedIndexTerms = dropAndCreateCollection("invertedIndexTerms");

        //Create inverted index indexes
        invertedIndexTerms.createIndex(
                new Document("term", 1)
        );
        invertedIndexTerms.createIndex(
                new Document("postingsLists.documentId", 1)
        );

    }


    public void buildInitialInvertedIndex(String pagesPath) throws Exception {

        // Create instant objects to debuggine indexer performance
        Instant s, e;
        s = Instant.now();


        // Drop and create indexer collections
        MongoCollection<Document> forwardIndexDocuments = dropAndCreateCollection("forwardIndexDocuments");
        MongoCollection<Document> invertedIndexTerms = dropAndCreateCollection("invertedIndexTerms");
        MongoCollection<Document> indexInformation = dropAndCreateCollection("indexInformation");


        // Create a data reader object to read pages that need to be indexed sequentially
        DataReader dataReader = new DataReader(pagesPath);
        // An object to hold page data
        DataDocument dataDocument;
        // A counter to show the indexer progress
        totalNumOfDocuments = 0;


        // Tokenizer and stemmer to parse page data
        WhitespaceTokenizer wsTokenizer = WhitespaceTokenizer.INSTANCE;
        PorterStemmer porterStemmer = new PorterStemmer();

        // Queue of indexed urls
        List<Integer> indexedUrls = new ArrayList<>();

        while ((dataDocument = dataReader.getNextDocument()) != null) {
            System.out.println(String.format("%d - %s", totalNumOfDocuments, dataDocument.getId()));

            Set<String> titleKeywords = dataDocument.getTitleKeywords();
            Set<String> metaKeywords = dataDocument.getMetaKeywords();
            String[] tokens = wsTokenizer.tokenize(dataDocument.getText());

            Map<String, LinkedList<Integer>> termPositions = new HashMap<>();
            for (Integer tokenIdx = 0; tokenIdx < tokens.length; tokenIdx++) {
                String originalTerm = tokens[tokenIdx];
                String stemmedTerm = porterStemmer.stem(originalTerm);

                //Add original term for phrase searching
                if (!termPositions.containsKey(originalTerm)) {
                    termPositions.put(originalTerm, new LinkedList<>());
                }
                termPositions.get(originalTerm).add(tokenIdx);

                //Add stemmed term for normal searching if it is different than original
                if(!stemmedTerm.equals(originalTerm)) {
                    if (!termPositions.containsKey(stemmedTerm)) {
                        termPositions.put(stemmedTerm, new LinkedList<>());
                    }
                    termPositions.get(stemmedTerm).add(tokenIdx);
                }
            }


            for (String term : termPositions.keySet()) {
                insertTermPositionsInDocument(
                        term,
                        dataDocument.getId(),
                        termPositions.get(term),
                        titleKeywords.contains(term),
                        metaKeywords.contains(term),
                        tokens.length);
            }

            Document documentTermsData = InvertedIndex.serializeDocumentTermsData(dataDocument.getId(), termPositions);
            forwardIndexDocuments.insertOne(documentTermsData);

            indexedUrls.add(dataDocument.getId());
            totalNumOfDocuments++;
        }

        // Update IDF for the inserted terms
        updateIDF();

        e = Instant.now();
        System.out.println(String.format("Building the inverted index took: %d milliseconds",Duration.between(s, e).toMillis()));

        // Insert the inverted index documents into the collection
        invertedIndexTerms.insertMany(toBSONDocuments());


        //Create inverted index indexes
        invertedIndexTerms.createIndex(
                new Document("term", 1)
        );
        invertedIndexTerms.createIndex(
                new Document("postingsLists.documentId", 1)
        );


        //Insert index information
        indexInformation.insertOne(buildIndexInformationDocument());

        //Update crawlerDB with indexed urls
        dataReader.finalize(indexedUrls);

        e = Instant.now();
        System.out.println(String.format("Total time: %d milliseconds", Duration.between(s, e).toMillis()));


    }


    private void _removeUrlsFromInvertedIndexById(List<Integer> urlIds, MongoCollection<Document> invertedIndexTerms){
        //remove from inverted index
        invertedIndexTerms.updateMany(
                Filters.in("postingsLists.documentId", urlIds),
                new Document(
                        "$pull",
                        new Document(
                                "postingsLists",
                                new Document(
                                        "documentId",
                                        new Document(
                                                "$in",
                                                urlIds
                                        )
                                )
                        )
                )
        );
    }

    private void _addToTermPostingsLists(String term, List<Document> newPostingsLists, MongoCollection<Document> invertedIndexTerms) {
        invertedIndexTerms.updateOne(
                Filters.eq("term", term),
                new Document(
                        "$push",
                        new Document(
                                "postingsLists",
                                new Document(
                                        "$each",
                                        newPostingsLists
                                )
                        )
                )
        );

    }

    public void updateInvertedIndex(String pagesPath) throws Exception {
        // Create instant objects to debuggine indexer performance
        Instant s, e;
        s = Instant.now();

        // Drop and create indexer collections
        MongoCollection<Document> forwardIndexDocuments = indexDB.getCollection("forwardIndexDocuments");
        MongoCollection<Document> invertedIndexTerms = indexDB.getCollection("invertedIndexTerms");
        MongoCollection<Document> indexInformation = indexDB.getCollection("indexInformation");


        // Create a data reader object to read pages that need to be indexed sequentially
        DataReader dataReader = new DataReader(pagesPath);
        // An object to hold page data
        DataDocument dataDocument;
        // A counter to show the indexer progress
        totalNumOfDocuments = 0;


        // Tokenizer and stemmer to parse page data
        WhitespaceTokenizer wsTokenizer = WhitespaceTokenizer.INSTANCE;
        PorterStemmer porterStemmer = new PorterStemmer();

        // List of indexed urls
        List<Integer> indexedUrls = new ArrayList<>();

        // Build new postings lists and update forward index
        while ((dataDocument = dataReader.getNextDocument()) != null) {
            System.out.println(String.format("%d - %s", totalNumOfDocuments, dataDocument.getId()));

            Set<String> titleKeywords = dataDocument.getTitleKeywords();
            Set<String> metaKeywords = dataDocument.getMetaKeywords();
            String[] tokens = wsTokenizer.tokenize(dataDocument.getText());

            Map<String, LinkedList<Integer>> termPositions = new HashMap<>();
            for (Integer tokenIdx = 0; tokenIdx < tokens.length; tokenIdx++) {
                String originalTerm = tokens[tokenIdx];
                String stemmedTerm = porterStemmer.stem(originalTerm);

                //Add original term for phrase searching
                if (!termPositions.containsKey(originalTerm)) {
                    termPositions.put(originalTerm, new LinkedList<>());
                }
                termPositions.get(originalTerm).add(tokenIdx);

                //Add stemmed term for normal searching if it is different than original
                if(!stemmedTerm.equals(originalTerm)) {
                    if (!termPositions.containsKey(stemmedTerm)) {
                        termPositions.put(stemmedTerm, new LinkedList<>());
                    }
                    termPositions.get(stemmedTerm).add(tokenIdx);
                }
            }


            for (String term : termPositions.keySet()) {
                insertTermPositionsInDocument(
                        term,
                        dataDocument.getId(),
                        termPositions.get(term),
                        titleKeywords.contains(term),
                        metaKeywords.contains(term),
                        tokens.length);
            }


            // Update old documents terms or insert new in forward index
            Document documentTermsData = InvertedIndex.serializeDocumentTermsData(dataDocument.getId(), termPositions);
            forwardIndexDocuments.replaceOne(
                    Filters.eq("documentId", dataDocument.getId()),
                    documentTermsData,
                    new UpdateOptions().upsert(true)
            );

            indexedUrls.add(dataDocument.getId());
            totalNumOfDocuments++;
        }
        System.out.println(Duration.between(s, Instant.now()).toMillis());




        // Start updating inverted index
        //// 1- For each updated url -> remove any existing postings lists information
        System.out.println("Start removing existing postings lists");
        _removeUrlsFromInvertedIndexById(indexedUrls, invertedIndexTerms);
        System.out.println(Duration.between(s, Instant.now()).toMillis());


        //// 2- Do cleaning -> Remove terms with 0 postings lists
        System.out.println("Clean unneeded terms");
        invertedIndexTerms.deleteMany(Filters.eq("postingsLists", Collections.emptyList()));
        System.out.println(Duration.between(s, Instant.now()).toMillis());


        //// 3- For each term -> add new postings lists
        System.out.println("Add new terms or postings lists");
        for (Map.Entry<String, TermData> entry : invertedIndex.entrySet()) {
            String term = entry.getKey();
            List<Document> newPostingsLists = entry.getValue().getPostingsLists().stream().map(PostingsList::toBSONDocument).collect(Collectors.toList());
            if (invertedIndexTerms.count(Filters.eq("term", term)) > 0) {
                _addToTermPostingsLists(term, newPostingsLists, invertedIndexTerms);
            } else {
                invertedIndexTerms.insertOne(entry.getValue().toBSONDocument().append("term", term));
            }
        }
        System.out.println(Duration.between(s, Instant.now()).toMillis());





        // Update index information and idf
        System.out.println("Update Info");
        indexDB.runCommand(new Document("eval","updateInformationAndIdf()"));
        System.out.println(Duration.between(s, Instant.now()).toMillis());

        e = Instant.now();
        System.out.println(String.format("Updating the inverted index took: %d milliseconds",Duration.between(s, e).toMillis()));


        //Update crawlerDB with indexed urls
        dataReader.finalize(indexedUrls);

        e = Instant.now();
        System.out.println(String.format("Total time: %d milliseconds",Duration.between(s, e).toMillis()));

    }


    public void close(){
        mongoClient.close();
    }
}
