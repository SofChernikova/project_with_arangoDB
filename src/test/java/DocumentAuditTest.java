import audit.DocumentAudit;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.DbName;
import com.arangodb.entity.BaseDocument;
import com.arangodb.internal.audit.Audit;
import com.arangodb.mapping.ArangoJack;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

class DocumentAuditTest {
    final ArangoDB arangoDB = new ArangoDB.Builder().user("root").password("1117").serializer(new ArangoJack()).build();
    final ArangoDatabase db = arangoDB.db(DbName.of("example_db"));
    final Audit audit = new DocumentAudit("test2", "error");
    final Collection<BaseDocument> collection = init();

    private Collection<BaseDocument> init() {
        Collection<BaseDocument> collection = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            BaseDocument baseDocument = new BaseDocument();
            baseDocument.addAttribute("firstName", "Alexey");
            baseDocument.addAttribute("lastName", "Ponomarev");
            baseDocument.addAttribute("age", 22);
            collection.add(baseDocument);
        }
        return collection;
    }

    @Test
    void insertDocumentWithAudit() {
        for (int i = 0; i < 10000; i++) {
            BaseDocument baseDocument = new BaseDocument();
            baseDocument.addAttribute("firstName", "Alexey");
            baseDocument.addAttribute("lastName", "Ponomarev");
            baseDocument.addAttribute("age", 22);
            db.collection("firstCollection", audit).insertDocument(baseDocument);
        }
    }

    @Test
    void insertDocumentWithoutAudit() {
        for (int i = 0; i < 10000; i++) {
            BaseDocument baseDocument = new BaseDocument();
            baseDocument.addAttribute("firstName", "Alexey");
            baseDocument.addAttribute("lastName", "Ponomarev");
            baseDocument.addAttribute("age", 22);
            db.collection("firstCollection", null).insertDocument(baseDocument);
        }
    }
    @Test
    void insertDocumentsWithAudit() {
        db.collection("firstCollection", audit).insertDocuments(collection);

    }
    @Test
    void insertDocumentsWithoutAudit() {
        db.collection("firstCollection", null).insertDocuments(collection);
    }

    @Test
    void updateDocumentWithAudit() throws ExecutionException, InterruptedException {
        for (int i = 0; i < 10000; i++) {
            BaseDocument baseDocument = new BaseDocument();
            baseDocument.addAttribute("firstName", "Alexey");
            baseDocument.addAttribute("lastName", "Ponomarev");
            baseDocument.addAttribute("age", 22);
            db.collection("firstCollection", audit).updateDocument("1234", baseDocument);
        }
    }
    @Test
    void updateDocumentWithoutAudit() throws ExecutionException, InterruptedException {
        for (int i = 0; i < 10000; i++) {
            BaseDocument baseDocument = new BaseDocument();
            baseDocument.addAttribute("firstName", "Alexey");
            baseDocument.addAttribute("lastName", "Ponomarev");
            baseDocument.addAttribute("age", 22);
            db.collection("firstCollection", null).updateDocument("1234", baseDocument);
        }
    }
    @Test
    void updateDocumentsWithAudit() {
        db.collection("firstCollection", audit).updateDocuments(collection);

    }
    @Test
    void updateDocumentsWithoutAudit() {
        db.collection("firstCollection", null).updateDocuments(collection);
    }

    @Test
    void replaceDocumentWithAudit() throws ExecutionException, InterruptedException {
        for (int i = 0; i < 10000; i++) {
            BaseDocument baseDocument = new BaseDocument();
            baseDocument.addAttribute("firstName", "Alexey");
            baseDocument.addAttribute("lastName", "Ponomarev");
            baseDocument.addAttribute("age", 22);
            db.collection("firstCollection", audit).replaceDocument("1234", baseDocument);
        }
    }
    @Test
    void replaceDocumentWithoutAudit() throws ExecutionException, InterruptedException {
        for (int i = 0; i < 10000; i++) {
            BaseDocument baseDocument = new BaseDocument();
            baseDocument.addAttribute("firstName", "Alexey");
            baseDocument.addAttribute("lastName", "Ponomarev");
            baseDocument.addAttribute("age", 22);
            db.collection("firstCollection", null).replaceDocument("1234", baseDocument);
        }
    }
    @Test
    void replaceDocumentsWithAudit() {
        db.collection("firstCollection", audit).replaceDocuments(collection);

    }
    @Test
    void replaceDocumentsWithoutAudit() {
        db.collection("firstCollection", null).replaceDocuments(collection);
    }

}