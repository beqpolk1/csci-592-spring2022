import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.neo4j.driver.*;
import org.neo4j.driver.Driver;

import java.util.ArrayList;

public class Neo4jConverter {
    public static void ConvertDatabase(MongoDatabase mongoDb) {

        System.out.println(System.lineSeparator() + "------CONVERTING TO NEO4J------");

        Driver neo4jDriver = GraphDatabase.driver("bolt://localhost:7687");
        Session session = neo4jDriver.session();

        System.out.println("Connection made");

        ArrayList<SimpleReference> trackArtist = new ArrayList<>();
        ArrayList<SimpleReference> artistAlbum = new ArrayList<>();
        ArrayList<AlbumTrackReference> albumTrack = new ArrayList<>();

        for (String curCollection : mongoDb.listCollectionNames()) {
            System.out.println(System.lineSeparator() + "____Processing collection " + curCollection + "____");

            int cnt = 0;

            for(Document curDoc : mongoDb.getCollection(curCollection).find()) {
                if (cnt % 500 == 0) System.out.println(">>Added " + cnt + " records...");
                cnt++;

                if (curCollection.equals("track")) {
                    //System.out.println(curDoc.toJson());
                    Track curTrack = new Track(curDoc);
                    curTrack.addTrackArtists(trackArtist);

                    String cypherInsert = curTrack.getCypherInsert();
                    //System.out.println(cypherInsert);
                    session.run(cypherInsert);
                }
                else if (curCollection.equals("artist")) {
                    //System.out.println(curDoc.toJson());
                    Artist curArtist = new Artist(curDoc);
                    curArtist.addArtistAlbums(artistAlbum);

                    String cypherInsert = curArtist.getCypherInsert();
                    //System.out.println(cypherInsert);
                    session.run(cypherInsert);
                }
                else if (curCollection.equals("album")) {
                    //System.out.println(curDoc.toJson());
                    Album curAlbum = new Album(curDoc);
                    curAlbum.addAlbumTracks(albumTrack);

                    String cypherInsert = curAlbum.getCypherInsert();
                    //System.out.println(cypherInsert);
                    session.run(cypherInsert);
                }
            }
        }

        session.close();
        neo4jDriver.close();
    }
}
