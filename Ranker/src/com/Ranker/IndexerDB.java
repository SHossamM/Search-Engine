package com.Ranker;

import com.mongodb.BulkWriteException;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.bulk.BulkWriteError;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class IndexerDB {
    MongoClient mongoClient;
    MongoDatabase indexDB;
    MongoCollection<Document> forwardIndexDocuments;

    public IndexerDB() {
        mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        indexDB = mongoClient.getDatabase("indexDB");
        forwardIndexDocuments = indexDB.getCollection("forwardIndexDocuments");
    }

    public void updatePageRank(HashMap<Integer, Double> pageRanks) {
        if(pageRanks != null && pageRanks.keySet().isEmpty()) {
            return;
        }
        List<WriteModel<Document>> updateDocuments = new ArrayList<>();
        Document filterDocument;
        Document updateDocument;
        UpdateOptions updateOptions = new UpdateOptions();
        updateOptions.upsert(true);

        for (Integer id : pageRanks.keySet()) {
            filterDocument = new Document("documentId", id);
            updateDocument = new Document(
                    "$set",
                    new Document("pageRank", pageRanks.get(id))
            );
            updateDocuments.add(
                    new UpdateOneModel<Document>(
                            filterDocument,
                            updateDocument,
                            updateOptions
                    )
            );
        }

        BulkWriteOptions bulkWriteOptions = new BulkWriteOptions();
        bulkWriteOptions.ordered(false); //False to allow parallel execution
        bulkWriteOptions.bypassDocumentValidation(true);

        try {
            forwardIndexDocuments.bulkWrite(updateDocuments, bulkWriteOptions);
        } catch (BulkWriteException e) {
            System.out.println("Error in updating indexerDB page ranks!");
        }
    }

    public void close() {
        mongoClient.close();
    }
}
