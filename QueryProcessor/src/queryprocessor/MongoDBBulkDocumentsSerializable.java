package queryprocessor;



import org.bson.Document;

import java.util.List;

public interface MongoDBBulkDocumentsSerializable {
    List<Document> toBSONDocuments();
}
