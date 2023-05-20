import audit.DocumentAudit;
import com.arangodb.*;
import com.arangodb.entity.BaseDocument;
import com.arangodb.entity.BaseEdgeDocument;
import com.arangodb.entity.EdgeDefinition;
import com.arangodb.internal.audit.Audit;
import com.arangodb.mapping.ArangoJack;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class EdgeAuditTest {
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

    private BaseDocument createFather() {
        BaseDocument father = new BaseDocument();
        father.addAttribute("firstName", "Alexey");
        father.addAttribute("lastName", "Ponomarev");
        father.addAttribute("age", 22);
        father.addAttribute("profession", "hokey player");
        return father;
    }

    private BaseDocument createSon() {
        BaseDocument son = new BaseDocument();
        son.addAttribute("firstName", "Pavel");
        son.addAttribute("lastName", "Mishin");
        son.addAttribute("age", 12);
        son.addAttribute("profession", "son of A.Ponomarev");
        return son;
    }

    @Test
    void insertEdgeWithoutAudit() {
        ArangoEdgeCollection isFatherOf = db.graph("Family").edgeCollection("isFatherOf", null);
        BaseDocument father = createFather();
        BaseDocument son = createSon();

        BaseEdgeDocument edge = new BaseEdgeDocument();
        edge.setFrom(father.getId());
        edge.setTo(son.getId());
        for (int i = 0; i < 10000; i++) {
            isFatherOf.insertEdge(edge);
        }
    }

    @Test
    void insertEdgeWithAudit() {
        ArangoEdgeCollection isFatherOf = db.graph("Family").edgeCollection("isFatherOf", audit);
        BaseDocument father = createFather();
        BaseDocument son = createSon();

        BaseEdgeDocument edge = new BaseEdgeDocument();
        edge.setFrom(father.getId());
        edge.setTo(son.getId());
        for (int i = 0; i < 10000; i++) {
            isFatherOf.insertEdge(edge);
        }
    }

    @Test
    void replaceEdgeWithoutAudit() {
        ArangoEdgeCollection isFatherOf = db.graph("Family").edgeCollection("isFatherOf", null);
        BaseDocument father = createFather();
        BaseDocument son = createSon();

        BaseEdgeDocument edge = new BaseEdgeDocument();
        edge.setFrom(father.getId());
        edge.setTo(son.getId());
        for (int i = 0; i < 10000; i++) {
            isFatherOf.replaceEdge("1234", edge);
        }
    }

    @Test
    void replaceEdgeWithAudit() {
        ArangoEdgeCollection isFatherOf = db.graph("Family").edgeCollection("isFatherOf", audit);
        BaseDocument father = createFather();
        BaseDocument son = createSon();

        BaseEdgeDocument edge = new BaseEdgeDocument();
        edge.setFrom(father.getId());
        edge.setTo(son.getId());
        for (int i = 0; i < 10000; i++) {
            isFatherOf.replaceEdge("1234", edge);
        }
    }

    @Test
    void updateEdgeWithoutAudit() {
        ArangoEdgeCollection isFatherOf = db.graph("Family").edgeCollection("isFatherOf", null);
        BaseDocument father = createFather();
        BaseDocument son = createSon();

        BaseEdgeDocument edge = new BaseEdgeDocument();
        edge.setFrom(father.getId());
        edge.setTo(son.getId());
        for (int i = 0; i < 10000; i++) {
            isFatherOf.updateEdge("1234", edge);
        }
    }

    @Test
    void updateEdgeWithAudit() {
        ArangoEdgeCollection isFatherOf = db.graph("Family").edgeCollection("isFatherOf", audit);
        BaseDocument father = createFather();
        BaseDocument son = createSon();

        BaseEdgeDocument edge = new BaseEdgeDocument();
        edge.setFrom(father.getId());
        edge.setTo(son.getId());
        for (int i = 0; i < 10000; i++) {
            isFatherOf.updateEdge("1234", edge);
        }
    }
}
