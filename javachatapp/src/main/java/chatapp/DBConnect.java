package chatapp;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import io.github.cdimascio.dotenv.*;

public class DBConnect extends Thread {
    ConnectionString ConnectionString;
    MongoClient mongoClient;
    MongoDatabase db;
    String DBName;
    boolean isConnected = false;

    DBConnect() {
        Dotenv dotenv = Dotenv.load();
        String connectionString = dotenv.get("CONNECTION_URI");
        ConnectionString = new ConnectionString(connectionString);
        DBName = "JavaChatDB";
    }

    public void run() {
        try {
            Thread.currentThread().setName("DB Connect Thread");

            CodecRegistry pojoreg = fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build()));
            mongoClient = MongoClients.create(
                    MongoClientSettings.builder()
                            .applyConnectionString(ConnectionString)
                            .codecRegistry(pojoreg)
                            .build());

            // mongoClient = MongoClients.create(ConnectionString);
            db = mongoClient.getDatabase(DBName);

            synchronized (this) {
                Document result = db.runCommand(new Document("ping", 1));
                System.out.println(
                        "Pinged your deployment. You successfully connected to MongoDB!\nRESULT:" + result.toJson());
                isConnected = true;
            }
            // MsgPacket msg = new MsgPacket("hitu", "hi", "john");

            // insertMsg(msg);
        } catch (MongoException e) {
            System.err.println("Error in Connecting to the DataBase!!");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error in DBConnect Thread: " + e);
        }
    }

    MongoDatabase getDb() {
        if (!isConnected) {
            System.err.println("\nDB is not connected.");
            return null;
        }
        System.out.println("\nConnected to : " + db.getName());
        return db;
    }

    // void insertMsg(MsgPacket msg) {
    // MongoCollection<MsgPacket> col = db.getCollection("chats", MsgPacket.class);
    // col.insertOne(msg);
    // }

    // public static void main(String[] args) {
    //     DBConnect dbc = new DBConnect();
    //     dbc.start();
    // }

}
