import org.bson.Document;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class Track {
    private Integer length, popularity, ratingScore, ratingVoters;
    private String id, name;
    private String[] genres;
    ArrayList<String> artistList;

    public Track(Document mongoDoc) {
        ArrayList<Document> ratings = (ArrayList<Document>) mongoDoc.get("ratings");
        Integer ratingScore = null, ratingVoters = null;
        if (ratings != null && ratings.size() > 0) {
            ratingScore = ratings.get(0).getInteger("score");
            ratingVoters = ratings.get(0).getInteger("voters");
        }

        ArrayList<String> genreList = (ArrayList<String>) mongoDoc.get("genres");
        String[] genres = genreList.toArray(new String[0]);

        this.artistList = (ArrayList<String>) mongoDoc.get("artist_id");

        this.id = mongoDoc.getString("_id");
        this.length = mongoDoc.getInteger("length");
        this.popularity = mongoDoc.getInteger("popularity");
        this.ratingScore = ratingScore;
        this.ratingVoters = ratingVoters;
        this.name = mongoDoc.getString("name");
        this.genres = genres;
    }

    public void fillSqlInsert(PreparedStatement theInsert) throws SQLException {
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

    public void fillGenreSqlInsert(PreparedStatement theInsert) throws SQLException {
        for (String curGenre : genres) {
            theInsert.setString(1, this.id);
            theInsert.setString(2, curGenre);
            theInsert.addBatch();
        }
    }

    public void addTrackArtists(ArrayList<SimpleReference> trackArtist) {
        ArrayList<String> addedArtists = new ArrayList<>();
        for (String curArtist : artistList) {
            if (!addedArtists.contains(curArtist)) {
                trackArtist.add(new SimpleReference(this.id, curArtist));
                addedArtists.add(curArtist);
            }
        }
    }
}
