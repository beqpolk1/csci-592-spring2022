import org.bson.Document;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class Review {
    private Integer stars;
    private String albumFk, journalist, rank, mediumName, mediumType, mediumUrl;

    public Review(Document mongoDoc, String parent) {
        this.albumFk = parent;
        this.journalist = mongoDoc.getString("journalist");
        this.rank = mongoDoc.getString("rank");;
        this.stars = mongoDoc.getInteger("stars");

        ArrayList<Document> media = (ArrayList<Document>) mongoDoc.get("media");
        if (media != null && media.size() > 0) {
            this.mediumName = media.get(0).getString("name");
            this.mediumType = media.get(0).getString("type");
            this.mediumUrl = media.get(0).getString("url");
        }
    }

    //"INSERT INTO review (album_fk, journalist, rank, stars, medium_name, medium_type, medium_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
    public void fillSqlInsert(PreparedStatement theInsert) throws SQLException {
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

    public String getCypherDef(int index) {
        String cypherDef = "(rvw" + index + ":review {journalist: \"" + this.journalist + "\", rank: \"" + this.rank + "\", stars: " + this.stars + "})";

        if (mediumName != null) {
            cypherDef += "-[:aggr_media]->(md" + index + ":media {mediumName: \"" + this.mediumName + "\", mediumType: \"" + this.mediumType + "\", mediumUrl: \"" + mediumUrl + "\"})";
        }

        return cypherDef;
    }
}
