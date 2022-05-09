import org.bson.Document;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class Artist {
    private Integer startingYear;
    private String id, name;
    ArrayList<String> albumList;

    public Artist(Document mongoDoc) {
        this.albumList = (ArrayList<String>) mongoDoc.get("albums");
        this.id = mongoDoc.getString("_id");
        this.name = mongoDoc.getString("name");
        this.startingYear = mongoDoc.getInteger("startingYear");
    }

    public void fillSqlInsert(PreparedStatement theInsert) throws SQLException {
        theInsert.setString(1, id);
        theInsert.setString(2, name);
        theInsert.setInt(3, startingYear);
    }

    public void addArtistAlbums(ArrayList<SimpleReference> artistAlbum) {
        ArrayList<String> addedAlbums = new ArrayList<>();
        for (String curAlbum : albumList) {
            if (!addedAlbums.contains(curAlbum)) {
                artistAlbum.add(new SimpleReference(this.id, curAlbum));
                addedAlbums.add(curAlbum);
            }
        }
    }

    public String getCypherInsert() {
        String result = "CREATE (at:artist {name: \"" + this.name + "\", startingYear: " + this.startingYear + "})";
        return result;
    }
}
