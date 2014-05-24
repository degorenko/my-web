package controllers;

import models.mongo.mongo;
import play.*;
import play.mvc.*;

import java.net.UnknownHostException;
import java.util.*;

import models.*;

public class Application extends Controller {
    static mongo core = new mongo();

    public static void index() {
        List<String> dbs = core.getDBS();
        render(dbs);
    }

    public static void collections(String db) {
        Set<String> collections = core.getCollections(db);
        renderJSON(collections);
    }

    public static void fields(String db, String collection) {
       Set<String> fields = core.getFields(db, collection);
        renderJSON(fields);
    }

    public static void getRecords(String db, String collection,
                                  String field, int skip, int limit) {
        Set<String> records = core.getRecords(db, collection, field, skip, limit);
        renderJSON(records);
    }

    public static void getData(String dbs, String collections, String fields, String boxes) {
        ArrayList<Object> res = core.getValues(dbs, collections, fields, boxes);
        renderJSON(res);
    }
}