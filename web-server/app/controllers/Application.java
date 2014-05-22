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

    public static void chart(String dbs, String collections, String fields, String boxes) {
        render(dbs, collections, fields, boxes);
    }

    public static void getData(String dbs, String collections, String fields, String boxes) {
        //ArrayList<Object> res = core.getValues(dbs, collections, fields);
        //   ArrayList<Object> res = core.getValues(dbs, collections, fields);
        ArrayList<Object> res = new ArrayList<Object>(2);
        ArrayList<String> list1 = new ArrayList<String>();
        ArrayList<Integer> list2 = new ArrayList<Integer>();
        List<String> labels = Arrays.asList(boxes.split(","));
        if (labels.contains("label1")) {
            list1.add("label1");
            list2.add(3);
        }
        if (labels.contains("label2")) {
            list1.add("label2");
            list2.add(1);
        }
        list1.add("label3");
        list2.add(2);
        res.add(list1);
        res.add(list2);
        renderJSON(res);
    }
}