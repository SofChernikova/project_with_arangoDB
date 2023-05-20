import audit.DocumentAudit;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.ArangoVertexCollection;
import com.arangodb.DbName;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.EdgeDefinition;
import com.arangodb.internal.audit.Audit;
import com.arangodb.mapping.ArangoJack;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class VertexAuditTest {
    final ArangoDB arangoDB = new ArangoDB.Builder().user("root").password("1117").serializer(new ArangoJack()).build();
    final ArangoDatabase db = arangoDB.db(DbName.of("example_db"));
    final Audit audit = new DocumentAudit("test2", "error");


    List<EdgeDefinition> edgeDefinitions = createGraph();
    List<String> from = new ArrayList<>();
    List<String> to = new ArrayList<>();
    EdgeDefinition edgeDefinition = new EdgeDefinition();

    private List<EdgeDefinition> createGraph() {
        List<EdgeDefinition> edgeDefinitions = new ArrayList<>();
        edgeDefinition.collection("isFatherOf");
        from.clear();
        from.add("Father");
        edgeDefinition.from(from.get(0));
        to.clear();
        to.add("Child");
        edgeDefinition.to(to.get(0));
        edgeDefinitions.add(edgeDefinition);
        db.createGraph("Family", edgeDefinitions);
        return edgeDefinitions;
    }

    @Test
    void insertVertexWithoutAudit() {
        ArangoVertexCollection fathers = db.graph("Family").vertexCollection("Father");
        for (int i = 0; i < 10000; i++) {
            BaseDocument baseDocument = new BaseDocument();
            baseDocument.addAttribute("firstName", "Alexey");
            baseDocument.addAttribute("lastName", "Ponomarev");
            baseDocument.addAttribute("age", 22);
            baseDocument.addAttribute("profession", "hokey player");
            fathers.insertVertex(baseDocument);
        }
    }

    @Test
    void insertVertexWithAudit() {
        ArangoVertexCollection sons = db.graph("Family").vertexCollection("Child", audit);
        for (int i = 0; i < 10000; i++) {
            BaseDocument baseDocument = new BaseDocument();
            baseDocument.addAttribute("firstName", "Alexey");
            baseDocument.addAttribute("lastName", "Ponomarev");
            baseDocument.addAttribute("age", 22);
            baseDocument.addAttribute("profession", "hokey player");
            sons.insertVertex(baseDocument);
        }
    }

    @Test
    void replaceVertexWithoutAudit() {
        ArangoVertexCollection fathers = db.graph("Family").vertexCollection("Father");
        for (int i = 0; i < 10000; i++) {
            BaseDocument baseDocument = new BaseDocument();
            baseDocument.addAttribute("firstName", "Alexey");
            baseDocument.addAttribute("lastName", "Ponomarev");
            baseDocument.addAttribute("age", 22);
            baseDocument.addAttribute("profession", "hokey player");
            fathers.replaceVertex("1234", baseDocument);
        }
    }

    @Test
    void replaceVertexWithAudit() {
        ArangoVertexCollection sons = db.graph("Family").vertexCollection("Child", audit);
        for (int i = 0; i < 10000; i++) {
            BaseDocument baseDocument = new BaseDocument();
            baseDocument.addAttribute("firstName", "Alexey");
            baseDocument.addAttribute("lastName", "Ponomarev");
            baseDocument.addAttribute("age", 22);
            baseDocument.addAttribute("profession", "hokey player");
            sons.replaceVertex("1234", baseDocument);
        }
    }

    @Test
    void updateVertexWithoutAudit() {
        ArangoVertexCollection fathers = db.graph("Family").vertexCollection("Father");
        for (int i = 0; i < 10000; i++) {
            BaseDocument baseDocument = new BaseDocument();
            baseDocument.addAttribute("firstName", "Alexey");
            baseDocument.addAttribute("lastName", "Ponomarev");
            baseDocument.addAttribute("age", 22);
            baseDocument.addAttribute("profession", "hokey player");
            fathers.updateVertex("1234", baseDocument);
        }
    }

    @Test
    void updateVertexWithAudit() {
        ArangoVertexCollection sons = db.graph("Family").vertexCollection("Child", audit);
        for (int i = 0; i < 10000; i++) {
            BaseDocument baseDocument = new BaseDocument();
            baseDocument.addAttribute("firstName", "Alexey");
            baseDocument.addAttribute("lastName", "Ponomarev");
            baseDocument.addAttribute("age", 22);
            baseDocument.addAttribute("profession", "hokey player");
            sons.updateVertex("1234", baseDocument);
        }
    }
}
