import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class PostgresConverter {
    public static void ConvertDatabase(MongoDatabase mongoDb) throws SQLException {
        String insertTrackSql = "INSERT INTO track (id, length, name, popularity, rating_score, rating_voters) VALUES (?, ?, ?, ?, ?, ?)";
        String insertTrackGenreSql = "INSERT INTO track_genre (track_fk, genre) VALUES (?, ?)";
        String insertArtistSql = "INSERT INTO artist (id, name, starting_year) VALUES (?, ?, ?)";
        String insertAlbumSql = "INSERT INTO album (id, name, popularity, release_year) VALUES (?, ?, ?, ?)";
        String insertAlbumGenreSql = "INSERT INTO album_genre (album_fk, genre) VALUES (?, ?)";
        String insertAlbumFormatSql =  "INSERT INTO album_format (album_fk, format) VALUES(?, ?)";
        String insertReview = "INSERT INTO review (album_fk, journalist, rank, stars, medium_name, medium_type, medium_url) VALUES (?, ?, ?, ?, ?, ?, ?)";

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
                if (cnt % 500 == 0) System.out.println(">>Added " + cnt + " records...");
                cnt++;

                if (curCollection.equals("track")) {
                    //System.out.println(curDoc.toJson());

                    ArrayList<Document> ratings = (ArrayList<Document>) curDoc.get("ratings");
                    Integer ratingScore = null, ratingVoters = null;
                    if (ratings != null && ratings.size() > 0) {
                        ratingScore = ratings.get(0).getInteger("score");
                        ratingVoters = ratings.get(0).getInteger("voters");
                    }

                    ArrayList<String> genreList = (ArrayList<String>) curDoc.get("genres");
                    String[] genres = genreList.toArray(new String[0]);

                    ArrayList<String> artistList = (ArrayList<String>) curDoc.get("artist_id");
                    for (String curArtist : artistList) {
                        trackArtist.add(new SimpleReference(curDoc.getString("_id"), curArtist));
                    }

                    Track curTrack = new Track(
                        curDoc.getString("_id"),
                        curDoc.getInteger("length"),
                        curDoc.getInteger("popularity"),
                        ratingScore,
                        ratingVoters,
                        curDoc.getString("name"),
                        genres
                    );

                    curTrack.fillInsert(myInsert);
                    //System.out.println(myInsert);
                    myInsert.execute();

                    PreparedStatement genreInsert = connection.prepareStatement(insertTrackGenreSql);
                    curTrack.fillGenreInsert(genreInsert);
                    genreInsert.executeBatch();
                }
                else if (curCollection.equals("artist")) {
                    //System.out.println(curDoc.toJson());

                    ArrayList<String> albumList = (ArrayList<String>) curDoc.get("albums");
                    for (String curAlbum : albumList) {
                        artistAlbum.add(new SimpleReference(curDoc.getString("_id"), curAlbum));
                    }

                    Artist curArtist = new Artist(
                        curDoc.getString("_id"),
                        curDoc.getString("name"),
                        curDoc.getInteger("startingYear")
                    );

                    curArtist.fillInsert(myInsert);
                    //System.out.println(myInsert);
                    myInsert.execute();
                }
                else if (curCollection.equals("album")) {
                    //System.out.println(curDoc.toJson());

                    Review review = null;
                    ArrayList<Document> reviews = (ArrayList<Document>) curDoc.get("reviews");
                    String reviewJournalist = null, reviewRank = null, mediumName = null, mediumType = null, mediumUrl = null;
                    Integer reviewStars = null;

                    if (reviews != null && reviews.size() > 0) {
                        reviewJournalist = reviews.get(0).getString("journalist");
                        reviewRank = reviews.get(0).getString("rank");
                        reviewStars = reviews.get(0).getInteger("stars");

                        ArrayList<Document> media = (ArrayList<Document>) reviews.get(0).get("media");
                        if (media != null && media.size() > 0) {
                            mediumName = media.get(0).getString("name");
                            mediumType = media.get(0).getString("type");
                            mediumUrl = media.get(0).getString("url");
                        }

                        review = new Review(curDoc.getString("_id"), reviewJournalist, reviewRank, reviewStars, mediumName, mediumType, mediumUrl);
                    }

                    String[] genres;
                    if (curDoc.get("genres") instanceof  ArrayList) {
                        ArrayList<String> genreList = (ArrayList<String>) curDoc.get("genres");
                        genres = genreList.toArray(new String[0]);
                    }
                    else {
                        genres = new String[]{curDoc.getString("genres")};
                    }

                    ArrayList<String> formatList = (ArrayList<String>) curDoc.get("formats");
                    String[] formats = formatList.toArray(new String[0]);

                    ArrayList<Document> trackList = (ArrayList<Document>) curDoc.get("tracks");
                    if (trackList != null && trackList.size() > 0) {
                        for (Document curTrack : trackList) {
                            albumTrack.add(new AlbumTrackReference(curDoc.getString("_id"), curTrack.getString("track_id"), curTrack.getInteger("track_number")));
                        }
                    }

                    Album curAlbum = new Album(
                        curDoc.getString("_id"),
                        curDoc.getString("name"),
                        curDoc.getInteger("popularity"),
                        curDoc.getInteger("releaseYear"),
                        genres,
                        formats
                    );

                    curAlbum.fillInsert(myInsert);
                    //System.out.println(myInsert);
                    myInsert.execute();

                    PreparedStatement genreInsert = connection.prepareStatement(insertAlbumGenreSql);
                    curAlbum.fillGenreInsert(genreInsert);
                    genreInsert.executeBatch();

                    PreparedStatement formatInsert = connection.prepareStatement(insertAlbumFormatSql);
                    curAlbum.fillFormatInsert(formatInsert);
                    formatInsert.executeBatch();

                    if (review != null) {
                        PreparedStatement reviewInsert = connection.prepareStatement(insertReview);
                        review.fillInsert(reviewInsert);
                        reviewInsert.execute();
                    }
                }
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
    }

    private static class Track {
        private Integer length, popularity, ratingScore, ratingVoters;
        private String id, name;
        private String[] genres;

        public Track(String id, Integer length, Integer popularity, Integer ratingScore, Integer ratingVoters, String name, String[] genres) {
            this.id = id;
            this.length = length;
            this.popularity = popularity;
            this.ratingScore = ratingScore;
            this.ratingVoters = ratingVoters;
            this.name = name;
            this.genres = genres;
        }

        public void fillInsert(PreparedStatement theInsert) throws SQLException {
            theInsert.setString(1, id);
            theInsert.setInt(2, length);
            theInsert.setString(3, name);
            theInsert.setInt(4, popularity);

            if (ratingScore != null) {
                theInsert.setInt(5, ratingScore);
            }
            else {
                theInsert.setNull(5, Types.NULL);
            }

            if (ratingVoters != null) {
                theInsert.setInt(6, ratingVoters);
            }
            else {
                theInsert.setNull(6, Types.NULL);
            }
        }

        public void fillGenreInsert(PreparedStatement theInsert) throws SQLException {
            for (String curGenre : genres) {
                theInsert.setString(1, this.id);
                theInsert.setString(2, curGenre);
                theInsert.addBatch();
            }
        }
    }

    private static class Artist {
        private Integer startingYear;
        private String id, name;

        public Artist(String id, String name, Integer startingYear) {
            this.id = id;
            this.name = name;
            this.startingYear = startingYear;
        }

        public void fillInsert(PreparedStatement theInsert) throws SQLException {
            theInsert.setString(1, id);
            theInsert.setString(2, name);
            theInsert.setInt(3, startingYear);
        }
    }

    private static class Album {
        private Integer popularity, releaseYear;
        private String id, name;
        private String[] genres, formats;

        public Album(String id, String name, Integer popularity, Integer releaseYear, String[] genres, String[] formats) {
            this.id = id;
            this.popularity = popularity;
            this.releaseYear = releaseYear;
            this.name = name;
            this.genres = genres;
            this.formats = formats;
        }

        public void fillInsert(PreparedStatement theInsert) throws SQLException {
            theInsert.setString(1, id);
            theInsert.setString(2, name);
            theInsert.setInt(3, popularity);
            theInsert.setInt(4, releaseYear);
        }

        public void fillGenreInsert(PreparedStatement theInsert) throws SQLException {
            for (String curGenre : genres) {
                theInsert.setString(1, this.id);
                theInsert.setString(2, curGenre);
                theInsert.addBatch();
            }
        }

        public void fillFormatInsert(PreparedStatement theInsert) throws SQLException {
            for (String curFormat : formats) {
                theInsert.setString(1, this.id);
                theInsert.setString(2, curFormat);
                theInsert.addBatch();
            }
        }
    }

    private static class Review {
        private Integer stars;
        private String albumFk, journalist, rank, mediumName, mediumType, mediumUrl;

        public Review(String albumFk, String journalist, String rank, Integer stars, String mediumName, String mediumType, String mediumUrl) {
            this.albumFk = albumFk;
            this.journalist = journalist;
            this.rank = rank;
            this.stars = stars;
            this.mediumName = mediumName;
            this.mediumType = mediumType;
            this.mediumUrl = mediumUrl;
        }

        //"INSERT INTO review (album_fk, journalist, rank, stars, medium_name, medium_type, medium_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
        public void fillInsert(PreparedStatement theInsert) throws SQLException {
            theInsert.setString(1, albumFk);
            theInsert.setString(2, journalist);
            theInsert.setString(3, rank);
            theInsert.setInt(4, stars);

            if (mediumName != null) {
                theInsert.setString(5, mediumName);
            }
            else {
                theInsert.setNull(5, Types.NULL);
            }

            if (mediumType != null) {
                theInsert.setString(6, mediumType);
            }
            else {
                theInsert.setNull(6, Types.NULL);
            }

            if (mediumUrl != null) {
                theInsert.setString(7, mediumUrl);
            }
            else {
                theInsert.setNull(7, Types.NULL);
            }
        }
    }

    private static class SimpleReference {
        String idFrom, idTo;

        public SimpleReference(String idFrom, String idTo) {
            this.idFrom = idFrom;
            this.idTo = idTo;
        }
   }

   private static class AlbumTrackReference {
        String idFrom, idTo;
        Integer trackNumber;

        public AlbumTrackReference(String idFrom, String idTo, Integer trackNumber) {
            this.idFrom = idFrom;
            this.idTo = idTo;
            this.trackNumber = trackNumber;
        }
   }
}
