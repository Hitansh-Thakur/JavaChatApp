package chatapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DBHandler {

    void saveChats(MsgPacket msg) throws InterruptedException{
        DBConnect dbc = new DBConnect();
        dbc.start();
        dbc.join();
        MongoDatabase db = dbc.getDb();
        MongoCollection<MsgPacket> chats = db.getCollection("chats", MsgPacket.class);
        chats.insertOne(msg);
    }
}
