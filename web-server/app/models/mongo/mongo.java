package models.mongo;

import com.mongodb.*;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 5/5/14
 * Time: 11:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class mongo {
    private MongoClient mongoClient;

    public mongo() {
        try {
            this.mongoClient = new MongoClient("localhost", 27017);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getDBS() {
        List<String> list = mongoClient.getDatabaseNames();
        list.remove("local");
        return list;
    }

    public Set<String> getCollections (String db) {
        Set<String> collections =  mongoClient.getDB(db).getCollectionNames();
        collections.remove("system.indexes");
        return  collections;
    }

    public Set<String> getFields(String db, String col) {
        Set<String> fields = mongoClient.getDB(db).getCollection(col).findOne().keySet();
        fields.remove("_id");
        return fields;
    }

    public HashSet<String> getRecords(String db, String collection, String field, int skip, int limit){
        DBCollection curCollection = mongoClient.getDB(db).getCollection(collection);
        DBObject obj = new BasicDBObject().append(field, 1).append("_id", 0);
        DBCursor resCursor = curCollection.find(null, obj);
        HashSet<String> result = new HashSet<String>();
        while(resCursor.hasNext()){
            result.add((String) resCursor.next().get(field));
        }
        return result;
    }

    public ArrayList<Object> getValues(String dbs, String collections, String fields) {
        DBCollection collection = mongoClient.getDB(dbs).getCollection(collections);
        List<DBObject> listCommand = new ArrayList<DBObject>();
        BasicDBObject project = new BasicDBObject();
        BasicDBObject var = new BasicDBObject("field", "$" + fields);
        var.put("count", new BasicDBObject("$add", 1));
        project.put("$project", var);
        BasicDBObject group = new BasicDBObject();
        BasicDBObject var1 = new BasicDBObject("_id", "$field");
        var1.put("value", new BasicDBObject("$sum", "$count"));
        group.put("$group", var1);
        listCommand.add(project);
        listCommand.add(group);
        Iterator<DBObject> res = collection.aggregate(listCommand).results().iterator();
        List<String> resN = new ArrayList<String>();
        List<Integer> resC = new ArrayList<Integer>();
        while (res.hasNext()) {
            DBObject obj = res.next();
            String name = obj.get("_id").toString();
            resN.add(name);
            int count = Integer.parseInt(obj.get("value").toString());
            resC.add(count);
        }
        ArrayList<Object> final_res = new ArrayList<Object>(2);
        final_res.add(resN);
        final_res.add(resC);
        return final_res;
    }
}
