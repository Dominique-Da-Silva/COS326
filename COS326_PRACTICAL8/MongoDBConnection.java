package mongopractical7;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoException;

public class MongoDBConnection {

    // Method to connect to MongoDB and return the database
    public static MongoDatabase connect() {
        try {
            MongoClient mongo = MongoClients.create("mongodb://localhost:27017/?serverSelectionTimeoutMS=5000");
            MongoDatabase dbConnection = mongo.getDatabase("ecommercedb");
            
            // System.out.println("Connected to the database");
            return dbConnection;  // Return the connection if successful
            
        } catch (MongoException e) {
            System.err.println("Error: Unable to connect to the database.");
            return null;  // Return null if there's a connection issue
        }
    }
}
