import com.mongodb.MongoClient;
import com.mongodb.client.*;

import java.io.IOException;
import java.sql.SQLException;

public class DataConverter {
    public static void main(String[] args) {
        System.out.println("======CONNECTING TO MONGODB======");
        MongoClient mongoClient = new MongoClient("host.docker.internal", 27017);
        MongoDatabase mongoSongsDB = mongoClient.getDatabase("mongoSongs");
        System.out.println("Connection made");

        try {
            PostgresConverter.ConvertDatabase(mongoSongsDB);
            Neo4jConverter.ConvertDatabase(mongoSongsDB);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        /*try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        System.out.println("goodbye!!!");
    }
}