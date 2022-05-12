import com.google.gson.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.summary.Plan;
import org.neo4j.driver.summary.ResultSummary;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TestQueryRunner {
    public static void main(String[] args) throws SQLException {
        System.out.println("======CONNECTING TO MONGODB======");
        MongoClient mongoClient = MongoClients.create("mongodb://host.docker.internal");//("host.docker.internal", 27017);
        MongoDatabase mongoSongsDB = mongoClient.getDatabase("mongoSongs");
        System.out.println("Connection made");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(System.lineSeparator() + "======CONNECTING TO NEO4J======");
        Driver neo4jDriver = GraphDatabase.driver("bolt://host.docker.internal:7687");
        Session neo4jSession = neo4jDriver.session();
        System.out.println("Connection made");

        System.out.println(System.lineSeparator() + "======CONNECTING TO POSTGRES======");
        Connection pgConnection = DriverManager.getConnection("jdbc:postgresql://host.docker.internal/mongosongs", "postgres", "password");
        System.out.println("Connection made");


        System.out.println(System.lineSeparator() + "______MONGO______");

        String aggrCount = "pipeline: [\n" +
                "    { $unwind: { path: \"$reviews\", preserveNullAndEmptyArrays: true } },\n" +
                "    { $unwind: { path: \"$tracks\", preserveNullAndEmptyArrays: false } },\n" +
                "    { $match: { $or: [ {$and: [{\"popularity\": { $gt: 60 }}, {reviews: {$exists: false}}]}, {\"reviews.stars\": { $gt: 70 }}] } },\n" +
                "    { $project: { _id: 0, name: 1, releaseYear: 1, popularity: 1, \"journalist\": \"$reviews.journalist\", \"stars\": \"$reviews.stars\", \"track_id\": \"$tracks.track_id\", \"track_number\": \"$tracks.track_number\" } },\n" +
                "    { $lookup: { from: \"track\", localField: \"track_id\", foreignField: \"_id\", as: \"track_doc\" } },\n" +
                "    { $unwind: \"$track_doc\" },\n" +
                "    { $project: { _id: 0, name: 1, releaseYear: 1, popularity: 1, \"journalist\": 1, \"stars\": 1, \"track_name\": \"$track_doc.name\", \"track_number\": 1 } },\n" +
                "    { $sort: { name: 1, track_number: 1 } },\n" +
                "    { $count: \"total_docs\" }" +
                "]";

        String aggrCommand = "{aggregate: \"album\", " + aggrCount + ", cursor: {} }";
        Document aggrDoc = Document.parse(aggrCommand);

        //System.out.println(explainCommand.toJson());
        Document count = mongoClient.getDatabase("mongoSongs").runCommand(aggrDoc);
        System.out.println("Total records from query: " + count.toJson());

        String aggrOnly = "pipeline: [\n" +
                "    { $unwind: { path: \"$reviews\", preserveNullAndEmptyArrays: true } },\n" +
                "    { $unwind: { path: \"$tracks\", preserveNullAndEmptyArrays: false } },\n" +
                "    { $match: { $or: [ {$and: [{\"popularity\": { $gt: 60 }}, {reviews: {$exists: false}}]}, {\"reviews.stars\": { $gt: 70 }}] } },\n" +
                "    { $project: { _id: 0, name: 1, releaseYear: 1, popularity: 1, \"journalist\": \"$reviews.journalist\", \"stars\": \"$reviews.stars\", \"track_id\": \"$tracks.track_id\", \"track_number\": \"$tracks.track_number\" } },\n" +
                "    { $lookup: { from: \"track\", localField: \"track_id\", foreignField: \"_id\", as: \"track_doc\" } },\n" +
                "    { $unwind: \"$track_doc\" },\n" +
                "    { $project: { _id: 0, name: 1, releaseYear: 1, popularity: 1, \"journalist\": 1, \"stars\": 1, \"track_name\": \"$track_doc.name\", \"track_number\": 1 } },\n" +
                "    { $sort: { name: 1, track_number: 1 } }\n" +
                "]";

        aggrCommand = "{aggregate: \"album\", " + aggrOnly + ", cursor: {} }";
        aggrDoc = Document.parse(aggrCommand);

        Document explainCommand = new Document();
        explainCommand.put("explain", aggrDoc);
        explainCommand.put("verbosity", "executionStats");

        //System.out.println(explainCommand.toJson());
        Document explain = mongoClient.getDatabase("mongoSongs").runCommand(explainCommand);
        //System.out.println("Execution plan: " + explain.toJson());
        extractMongoPlan(explain);


        System.out.println(System.lineSeparator() + "______NEO4J______");

        String neoQueryCount = "MATCH(ab:album)-[abtr:album_track]-(tr:track)\n" +
                "OPTIONAL MATCH (ab)-[:aggr_reviews]-(rv:review)\n" +
                "WITH ab, abtr, tr, rv\n" +
                "WHERE (ab.popularity > 60 AND rv.stars IS NULL) OR rv.stars > 70\n" +
                "RETURN COUNT(*);";

        Result neo4jResult = neo4jSession.run(neoQueryCount);
        System.out.println("Total records from query: " + neo4jResult.next().toString());

        String neoQueryWithExplain = "EXPLAIN\n" +
                "MATCH(ab:album)-[abtr:album_track]-(tr:track)\n" +
                "OPTIONAL MATCH (ab)-[:aggr_reviews]-(rv:review)\n" +
                "WITH ab, abtr, tr, rv\n" +
                "WHERE (ab.popularity > 60 AND rv.stars IS NULL) OR rv.stars > 70\n" +
                "RETURN tr.name, abtr.trackNumber, ab.name, ab.releaseYear, ab.popularity, rv.journalist, rv.stars\n" +
                "ORDER BY ab.name, abtr.trackNumber;";

        neo4jResult = neo4jSession.run(neoQueryWithExplain);
        ResultSummary neo4jSummary = neo4jResult.consume();
        Plan neo4jPlan = neo4jSummary.plan();
        //System.out.println("Execution plan: " + neo4jPlan.toString());
        extractNeo4jPlan(neo4jPlan);


        System.out.println(System.lineSeparator() + "______POSTGRES______");

        String sqlQueryCount = "SELECT COUNT(*) cnt FROM (\n" +
                "SELECT\n" +
                "track.name, album_track.track_number,\n" +
                "album.name, album.release_year, album.popularity,\n" +
                "review.journalist, review.stars\n" +
                "FROM album\n" +
                "INNER JOIN album_track ON album_track.album_fk = album.id\n" +
                "INNER JOIN track ON track.id = album_track.track_fk\n" +
                "LEFT JOIN review ON review.album_fk = album.id\n" +
                "WHERE\n" +
                "review.stars > 70\n" +
                "OR (review.stars IS NULL AND album.popularity > 60)\n" +
                ") AS sub;";

        PreparedStatement pgQuery = pgConnection.prepareStatement(sqlQueryCount);
        ResultSet countResults = pgQuery.executeQuery();
        countResults.next();
        System.out.println("Total records from query: " + countResults.getString("cnt"));

        String sqlQueryWithExplain = "EXPLAIN (ANALYZE true, FORMAT xml) SELECT\n" +
                "track.name, album_track.track_number,\n" +
                "album.name, album.release_year, album.popularity,\n" +
                "review.journalist, review.stars\n" +
                "FROM album\n" +
                "INNER JOIN album_track ON album_track.album_fk = album.id\n" +
                "INNER JOIN track ON track.id = album_track.track_fk\n" +
                "LEFT JOIN review ON review.album_fk = album.id\n" +
                "WHERE\n" +
                "review.stars > 70\n" +
                "OR (review.stars IS NULL AND album.popularity > 60)\n" +
                "ORDER BY album.name, album_track.track_number;";

        pgQuery = pgConnection.prepareStatement(sqlQueryWithExplain);
        ResultSet planResults = pgQuery.executeQuery();
        planResults.next();
        //System.out.println("Execution plan: ");
        //System.out.println(planResults.getString(1));

        try {
            DocumentBuilder xmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document xmlPlan = xmlBuilder.parse(new InputSource(new StringReader(planResults.getString(1))));
            extractPostgresPlan(xmlPlan);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    private static void extractMongoPlan(Document plan) {
        JsonObject planJson = new JsonParser().parse(plan.toJson()).getAsJsonObject();

        JsonArray execStages = planJson.get("stages").getAsJsonArray();
        JsonObject returnStats = new JsonObject();

        returnStats.addProperty("numberReturned", 0);
        returnStats.addProperty("totalExamined", 0);
        returnStats.addProperty("executionTime", 0);
        returnStats.addProperty("works", 0);
        returnStats.addProperty("needTime", 0);

        for (Iterator<JsonElement> it = execStages.iterator(); it.hasNext(); ) {
            JsonObject curStage = it.next().getAsJsonObject();

            for (String curKey : curStage.keySet()) {
                JsonObject obj = curStage.get(curKey).getAsJsonObject();

                if (obj.has("executionStats")) {
                    JsonObject executionStats = obj.get("executionStats").getAsJsonObject();

                    int numberReturned = executionStats.get("nReturned").getAsInt();
                    int totalExamined = executionStats.get("totalDocsExamined").getAsInt();
                    int executionTime = executionStats.get("executionStages").getAsJsonObject().get("executionTimeMillisEstimate").getAsInt();
                    int works = executionStats.get("executionStages").getAsJsonObject().get("works").getAsInt();
                    int needTime = executionStats.get("executionStages").getAsJsonObject().get("needTime").getAsInt();

                    returnStats.addProperty("numberReturned", returnStats.get("numberReturned").getAsInt() + numberReturned);
                    returnStats.addProperty("totalExamined", returnStats.get("totalExamined").getAsInt() + totalExamined);
                    returnStats.addProperty("executionTime", returnStats.get("executionTime").getAsInt() + executionTime);
                    returnStats.addProperty("works", returnStats.get("works").getAsInt() + works);
                    returnStats.addProperty("needTime", returnStats.get("needTime").getAsInt() + needTime);
                }
            }
        }

        System.out.println("Relevant statistics: " + returnStats.toString());
    }

    private static void extractNeo4jPlan(Plan plan) {
        JsonObject returnStats = new JsonObject();

        if (plan.arguments().containsKey("EstimatedRows")) {
            returnStats.addProperty("estimatedRows", plan.arguments().get("EstimatedRows").asDouble());
        }
        else {
            returnStats.addProperty("estimatedRows", 0);
        }

        List<Plan> childrenToCheck = new ArrayList<>(plan.children());

        //bfs
        while (childrenToCheck.size() > 0) {
            Plan curChild = childrenToCheck.get(0);

            if (plan.arguments().containsKey("EstimatedRows")) {
                returnStats.addProperty("estimatedRows", returnStats.get("estimatedRows").getAsDouble() + plan.arguments().get("EstimatedRows").asDouble());
            }

            List<Plan> nextChildren = new ArrayList<>(curChild.children());
            childrenToCheck.addAll(nextChildren);

            childrenToCheck.remove(0);
        }

        System.out.println("Relevant statistics: " + returnStats.toString());
    }

    private static void extractPostgresPlan(org.w3c.dom.Document plan) {
        List<Node> nodesToCheck = new ArrayList<>();
        nodesToCheck.add(plan.getFirstChild());

        double maxStartupCost = 0, maxTotalCost = 0, maxRows = 0, maxStartupTime = 0, maxTotalTime = 0;

        //bfs
        while (nodesToCheck.size() > 0) {
            Node curNode = nodesToCheck.get(0);
            //System.out.println(curNode.getNodeName());

            if (curNode.getNodeName().equals("Plan")) {
                NodeList planStats = curNode.getChildNodes();

                for (int i = 0; i < planStats.getLength(); i++) {
                    Node curStat = planStats.item(i);

                    if (curStat.getNodeName().equals("Startup-Cost") && Double.parseDouble(curStat.getTextContent()) > maxStartupCost) {
                        maxStartupCost = Double.parseDouble(curStat.getTextContent());
                    }
                    else if (curStat.getNodeName().equals("Total-Cost") && Double.parseDouble(curStat.getTextContent()) > maxTotalCost) {
                        maxTotalCost = Double.parseDouble(curStat.getTextContent());
                    }
                    else if (curStat.getNodeName().equals("Actual-Rows") && Double.parseDouble(curStat.getTextContent()) > maxRows) {
                        maxRows = Double.parseDouble(curStat.getTextContent());
                    }
                    else if (curStat.getNodeName().equals("Actual-Startup-Time") && Double.parseDouble(curStat.getTextContent()) > maxStartupTime) {
                        maxStartupTime = Double.parseDouble(curStat.getTextContent());
                    }
                    else if (curStat.getNodeName().equals("Actual-Total-Time") && Double.parseDouble(curStat.getTextContent()) > maxTotalTime) {
                        maxTotalTime = Double.parseDouble(curStat.getTextContent());
                    }
                }
            }

            if (curNode.hasChildNodes()) {
                NodeList allChildren = curNode.getChildNodes();
                for (int i = 0; i < allChildren.getLength(); i++) {
                    if (!allChildren.item(i).getNodeName().equals("#text")) nodesToCheck.add(allChildren.item(i));
                }
            }

            nodesToCheck.remove(0);
        }

        JsonObject returnStats = new JsonObject();
        returnStats.addProperty("maxStartupCost", maxStartupCost);
        returnStats.addProperty("maxTotalCost", maxTotalCost);
        returnStats.addProperty("maxRows", maxRows);
        returnStats.addProperty("maxStartupTime", maxStartupTime);
        returnStats.addProperty("maxTotalTime", maxTotalTime);

        System.out.println("Relevant statistics: " + returnStats.toString());
    }
}
