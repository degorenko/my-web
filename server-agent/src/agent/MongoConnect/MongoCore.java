package agent.MongoConnect;

import agent.Utility.Notificator;
import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 5/2/14
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class MongoCore {
    private MongoClient connect;
    private final String mongoAddr;
    private final static int port = 27017;
    public static MongoCore core;

    public MongoCore(String ip) {
        this.mongoAddr = ip;
    }

    public void connectDB() {
        try {
            this.connect = new MongoClient(mongoAddr, port);
        } catch (UnknownHostException e) {
            Notificator.notificator(String.format("Unknown host %s", mongoAddr));
            e.printStackTrace();
        } catch (MongoException e) {
            Notificator.notificator(String.format("Can't connect to MongoDB host: %1$s, port: %2$d", mongoAddr, port));
            e.printStackTrace();
        }
    }

    public void insertData(String collection, String ip, HashMap<String, HashMap<String, String>> data) {
        DB db = connect.getDB("statistic");
        DBCollection db_coll = db.getCollection(collection);
        for(String file: data.keySet()){
            BasicDBObject obj = new BasicDBObject();
            obj.put("ipAddr", ip);
            for(String key: data.get(file).keySet()){
                obj.put(key, data.get(file).get(key));
            }
            obj.put("filePath", file);
            db_coll.insert(obj);
        }
    }
}
