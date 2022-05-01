import com.mongodb.MongoClient;
import com.mongodb.client.*;

import java.io.IOException;

public class DataConverter {
    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient("192.168.1.10", 27017);

        MongoDatabase mongoSongsDB = mongoClient.getDatabase("mongoSongs");

        for (String curCollection : mongoSongsDB.listCollectionNames()) {
            System.out.println("Collection " + curCollection);
        }

        /*try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        System.out.println("goodbye!!!");
    }
}