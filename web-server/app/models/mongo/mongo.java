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
        List<DBObject> listCommand = getRecordsQuery(field, skip, limit);
        Iterator<DBObject> resCursor = curCollection.aggregate(listCommand).results().iterator();
        HashSet<String> records = new HashSet<String>();
        while (resCursor.hasNext()) {
            DBObject obj = resCursor.next();
            String record = obj.get("_id").toString();
            records.add(record);
        }
        return records;
    }

    public ArrayList<Object> getValues(String dbs, String collections, String field, String criteria) {
        List<DBObject> listCommand;
        if (criteria.length() == 0) {
            listCommand = getValuesQuery(field, null);
        } else {
            listCommand = getValuesQuery(field, parseCriteria(criteria));
        }
        DBCollection collection = mongoClient.getDB(dbs).getCollection(collections);
        Iterator<DBObject> res = collection.aggregate(listCommand).results().iterator();
        List<String> values = new ArrayList<String>();
        List<Integer> values_count = new ArrayList<Integer>();
        while (res.hasNext()) {
            DBObject obj = res.next();
            String field_value = obj.get("_id").toString();
            values.add(field_value);
            int count = Integer.parseInt(obj.get("value").toString());
            values_count.add(count);
        }
        ArrayList<Object> final_res = new ArrayList<Object>(2);
        final_res.add(values);
        final_res.add(values_count);
        return final_res;
    }

    private HashMap<String, HashSet<String>> parseCriteria(String inboxCriteria){
        HashMap<String,HashSet<String>> res = new HashMap<String, HashSet<String>>();
        String[] criteria = inboxCriteria.split(",");
        for(String criterion:criteria){
            String[] tmp = criterion.split(":");
            if (!res.containsKey(tmp[0])) {
                HashSet<String> set = new HashSet<String>();
                set.add(tmp[1]);
                res.put(tmp[0], set);
            } else {
                res.get(tmp[0]).add(tmp[1]);
            }
        }
        return res;
    }

    private List<DBObject> getValuesQuery(String mainField, HashMap<String, HashSet<String>> criteria){
        List<DBObject> listCommand = new ArrayList<DBObject>();
        if (criteria != null) {
            BasicDBObject match = new BasicDBObject();
            BasicDBObject query = new BasicDBObject();
            for(String field:criteria.keySet()){
                ArrayList<String> list = new ArrayList<String>();
                for(String record: criteria.get(field)){
                    list.add(record);
                }
                query.put(field, new BasicDBObject("$in", list));
            }
            match.put("$match", query);
            listCommand.add(match);
        }
        BasicDBObject project = new BasicDBObject("$project",
                new BasicDBObject("field", "$" + mainField)
                .append("count", new BasicDBObject("$add", 1)));
        listCommand.add(project);
        BasicDBObject group = new BasicDBObject("$group",
                new BasicDBObject("_id", "$field")
                .append("value", new BasicDBObject("$sum", "$count")));
        listCommand.add(group);
        return listCommand;
    }

    private List<DBObject> getRecordsQuery(String mainField, int skip, int limit){
        List<DBObject> listCommand = new ArrayList<DBObject>();
        BasicDBObject project = new BasicDBObject("$project",
                new BasicDBObject("field", "$" + mainField));
        listCommand.add(project);
        BasicDBObject group = new BasicDBObject("$group",
                new BasicDBObject("_id", "$field"));
        listCommand.add(group);
        BasicDBObject sort = new BasicDBObject("$sort",
                new BasicDBObject("_id", 1));
        listCommand.add(sort);
        BasicDBObject skip_q = new BasicDBObject("$skip", skip);
        listCommand.add(skip_q);
        BasicDBObject limit_q = new BasicDBObject("$limit", limit);
        listCommand.add(limit_q);
        return listCommand;
    }
}
