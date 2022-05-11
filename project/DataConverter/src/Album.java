import org.bson.Document;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Album {
    private Integer popularity, releaseYear;
    private String id, name;
    private String[] genres, formats;
    ArrayList<Review> reviews;
    ArrayList<Document> trackList; // = new ArrayList<>();

    public Album(Document mongoDoc) {
        String[] genres;
        if (mongoDoc.get("genres") instanceof ArrayList) {
            ArrayList<String> genreList = (ArrayList<String>) mongoDoc.get("genres");
            genres = genreList.toArray(new String[0]);
        }
        else {
            genres = new String[]{mongoDoc.getString("genres")};
        }

        ArrayList<String> formatList = (ArrayList<String>) mongoDoc.get("formats");
        String[] formats = formatList.toArray(new String[0]);

        this.trackList = (ArrayList<Document>) mongoDoc.get("tracks");

        this.id = mongoDoc.getString("_id");
        this.popularity = mongoDoc.getInteger("popularity");
        this.releaseYear = mongoDoc.getInteger("releaseYear");
        this.name = mongoDoc.getString("name");
        this.genres = genres;
        this.formats = formats;

        ArrayList<Document> mongoReviews = (ArrayList<Document>) mongoDoc.get("reviews");
        reviews = new ArrayList<>();

        if (mongoReviews != null && mongoReviews.size() > 0) {
            for (Document curReview: mongoReviews) {
                reviews.add(new Review(curReview, this.id));
            }
        }
    }

    public void fillSqlInsert(PreparedStatement theInsert) throws SQLException {
        theInsert.setString(1, id);
        theInsert.setString(2, name);
        theInsert.setInt(3, popularity);
        theInsert.setInt(4, releaseYear);
    }

    public void fillGenreSqlInsert(PreparedStatement theInsert) throws SQLException {
        for (String curGenre : genres) {
            theInsert.setString(1, this.id);
            theInsert.setString(2, curGenre);
            theInsert.addBatch();
        }
    }

    public void fillFormatSqlInsert(PreparedStatement theInsert) throws SQLException {
        for (String curFormat : formats) {
            theInsert.setString(1, this.id);
            theInsert.setString(2, curFormat);
            theInsert.addBatch();
        }
    }

    public boolean hasReviews() { return reviews.size() > 0; }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void addAlbumTracks(ArrayList<AlbumTrackReference> albumTrack){
        if (trackList != null && trackList.size() > 0) {
            for (Document curTrack : trackList) {
                albumTrack.add(new AlbumTrackReference(this.id, curTrack.getString("track_id"), curTrack.getInteger("track_number")));
            }
        }
    }

    public String getCypherInsert() {
        String formatString = "[";
        int idx = 0;
        for (String curFormat : formats) {
            if (idx > 0) formatString += ", ";
            formatString += "\"" + curFormat + "\"";
            idx++;
        }
        formatString += "]";

        String genreString = "[";
        idx = 0;
        for (String curGenre : genres) {
            if (idx > 0) genreString += ", ";
            genreString += "\"" + curGenre + "\"";
            idx++;
        }
        genreString += "]";

        String result = "CREATE (ab:album {id: \"" + this.id + "\", name: \"" + this.name + "\", popularity: " + this.popularity + ", releaseYear: " + this.releaseYear + ", formats: " + formatString + ", genres: " + genreString + "})";
        if (hasReviews()) {
            int index = 0;
            for (Review curReview : reviews) {
                result += ", (ab)-[:aggr_reviews]->" + curReview.getCypherDef(index);
                index++;
            }
        }
        return result;
    }
}
