import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.sql.*;
import java.util.ArrayList;

public class PostgresConverter {
    public static void ConvertDatabase(MongoDatabase mongoDb) throws SQLException {
        String insertTrackSql = "INSERT INTO track (id, length, name, popularity, rating_score, rating_voters) VALUES (?, ?, ?, ?, ?, ?)";
        String insertTrackGenreSql = "INSERT INTO track_genre (track_fk, genre) VALUES (?, ?)";
        String insertArtistSql = "INSERT INTO artist (id, name, starting_year) VALUES (?, ?, ?)";
        String insertAlbumSql = "INSERT INTO album (id, name, popularity, release_year) VALUES (?, ?, ?, ?)";
        String insertAlbumGenreSql = "INSERT INTO album_genre (album_fk, genre) VALUES (?, ?)";
        String insertAlbumFormatSql =  "INSERT INTO album_format (album_fk, format) VALUES(?, ?)";
        String insertReview = "INSERT INTO review (album_fk, journalist, rank, stars, medium_name, medium_type, medium_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertTrackArtist = "INSERT INTO track_artist(track_fk, artist_fk) VALUES (?, ?)";
        String insertArtistAlbum = "INSERT INTO artist_album(artist_fk, album_fk) VALUES (?, ?)";
        String insertAlbumTrack = "INSERT INTO album_track(album_fk, track_fk, track_number) VALUES (?, ?, ?)";

        System.out.println(System.lineSeparator() + "------CONVERTING TO POSTGRES------");

        Connection connection = DriverManager.getConnection("jdbc:postgresql://host.docker.internal/mongosongs", "postgres", "password");

        System.out.println("Connection made");

        ArrayList<SimpleReference> trackArtist = new ArrayList<>();
        ArrayList<SimpleReference> artistAlbum = new ArrayList<>();
        ArrayList<AlbumTrackReference> albumTrack = new ArrayList<>();

        for (String curCollection : mongoDb.listCollectionNames()) {
            System.out.println(System.lineSeparator() + "____Processing collection " + curCollection + "____");

            PreparedStatement myInsert = null;
            if (curCollection.equals("track")) {
                myInsert = connection.prepareStatement(insertTrackSql);
            }
            else if (curCollection.equals("artist")) {
                myInsert = connection.prepareStatement(insertArtistSql);
            }
            else if (curCollection.equals("album")) {
                myInsert = connection.prepareStatement(insertAlbumSql);
            }

            int cnt = 0;

            for(Document curDoc : mongoDb.getCollection(curCollection).find()) {
                cnt++;

                if (curCollection.equals("track")) {
                    //System.out.println(curDoc.toJson());
                    Track curTrack = new Track(curDoc);
                    curTrack.addTrackArtists(trackArtist);

                    curTrack.fillSqlInsert(myInsert);
                    //System.out.println(myInsert);
                    myInsert.execute();

                    PreparedStatement genreInsert = connection.prepareStatement(insertTrackGenreSql);
                    curTrack.fillGenreSqlInsert(genreInsert);
                    genreInsert.executeBatch();
                }
                else if (curCollection.equals("artist")) {
                    //System.out.println(curDoc.toJson());
                    Artist curArtist = new Artist(curDoc);
                    curArtist.addArtistAlbums(artistAlbum);

                    curArtist.fillSqlInsert(myInsert);
                    //System.out.println(myInsert);
                    myInsert.execute();
                }
                else if (curCollection.equals("album")) {
                    //System.out.println(curDoc.toJson());
                    Album curAlbum = new Album(curDoc);
                    curAlbum.addAlbumTracks(albumTrack);

                    curAlbum.fillSqlInsert(myInsert);
                    //System.out.println(myInsert);
                    myInsert.execute();

                    PreparedStatement genreInsert = connection.prepareStatement(insertAlbumGenreSql);
                    curAlbum.fillGenreSqlInsert(genreInsert);
                    genreInsert.executeBatch();

                    PreparedStatement formatInsert = connection.prepareStatement(insertAlbumFormatSql);
                    curAlbum.fillFormatSqlInsert(formatInsert);
                    formatInsert.executeBatch();

                    if (curAlbum.hasReview()) {
                        PreparedStatement reviewInsert = connection.prepareStatement(insertReview);
                        curAlbum.getReview().fillSqlInsert(reviewInsert);
                        reviewInsert.execute();
                    }
                }

                if (cnt % 500 == 0) System.out.println(">>Added " + cnt + " records...");
            }

            if (curCollection.equals("track")) {
                Statement select = connection.createStatement();
                ResultSet countResult = select.executeQuery("SELECT COUNT(*) cnt FROM track");
                countResult.next();
                int totalRecords = countResult.getInt("cnt");
                System.out.println("* Processed " + cnt + " track documents, found " + totalRecords + " in corresponding table");
            }
            else if (curCollection.equals("artist")) {
                Statement select = connection.createStatement();
                ResultSet countResult = select.executeQuery("SELECT COUNT(*) cnt FROM artist");
                countResult.next();
                int totalRecords = countResult.getInt("cnt");
                System.out.println("* Processed " + cnt + " artist documents, found " + totalRecords + " in corresponding table");
            }
            else if (curCollection.equals("album")) {
                Statement select = connection.createStatement();
                ResultSet countResult = select.executeQuery("SELECT COUNT(*) cnt FROM album");
                countResult.next();
                int totalRecords = countResult.getInt("cnt");
                System.out.println("* Processed " + cnt + " album documents, found " + totalRecords + " in corresponding table");
            }
        }

        System.out.println(System.lineSeparator() + "____Inserting entity references____");

        PreparedStatement referenceInsert = connection.prepareStatement(insertTrackArtist);
        fillSimpleReferenceInsert(referenceInsert, trackArtist);
        referenceInsert.executeBatch();

        Statement select = connection.createStatement();
        ResultSet countResult = select.executeQuery("SELECT COUNT(*) cnt FROM track_artist");
        countResult.next();
        int totalRecords = countResult.getInt("cnt");
        System.out.println("* Should have inserted " + trackArtist.size() + " track -> artist references, found " + totalRecords + " in corresponding table");

        referenceInsert = connection.prepareStatement(insertArtistAlbum);
        fillSimpleReferenceInsert(referenceInsert, artistAlbum);
        referenceInsert.executeBatch();

        connection.createStatement();
        countResult = select.executeQuery("SELECT COUNT(*) cnt FROM artist_album");
        countResult.next();
        totalRecords = countResult.getInt("cnt");
        System.out.println("* Should have inserted " + artistAlbum.size() + " artist -> album references, found " + totalRecords + " in corresponding table");

        referenceInsert = connection.prepareStatement(insertAlbumTrack);
        fillAlbumTrackReferenceInsert(referenceInsert, albumTrack);
        referenceInsert.executeBatch();

        connection.createStatement();
        countResult = select.executeQuery("SELECT COUNT(*) cnt FROM album_track");
        countResult.next();
        totalRecords = countResult.getInt("cnt");
        System.out.println("* Should have inserted " + albumTrack.size() + " album -> track references, found " + totalRecords + " in corresponding table");
    }

    private static void fillSimpleReferenceInsert(PreparedStatement theInsert, ArrayList<SimpleReference> references) throws SQLException {
        for (SimpleReference curReference : references) {
            theInsert.setString(1, curReference.getIdFrom());
            theInsert.setString(2, curReference.getIdTo());
            theInsert.addBatch();
        }
    }

    private static void fillAlbumTrackReferenceInsert(PreparedStatement theInsert, ArrayList<AlbumTrackReference> references) throws SQLException {
        for (AlbumTrackReference curReference : references) {
            theInsert.setString(1, curReference.getIdFrom());
            theInsert.setString(2, curReference.getIdTo());
            theInsert.setInt(3, curReference.getTrackNumber());
            theInsert.addBatch();
        }
    }
}
