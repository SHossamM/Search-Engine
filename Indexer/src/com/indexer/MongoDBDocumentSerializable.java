package com.indexer;

import org.bson.Document;

public interface MongoDBDocumentSerializable {
    Document toBSONDocument();
}
