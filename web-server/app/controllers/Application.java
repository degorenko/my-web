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

    public static void collections(String dbname) {
        Set<String> collections = core.getCollections(dbname);
        renderJSON(collections);
    }

    public static void fields(String dbname, String collectionname) {
       Set<String> fields = core.getFields(dbname, collectionname);
        renderJSON(fields);
    }

    public static void chart(String dbs, String collections, String fields, Boolean checkbox1, Boolean checkbox2) {
        render(dbs, collections, fields, checkbox1, checkbox2);
    }

    public static void getData(String dbs, String collections, String fields, String checkbox1, String checkbox2) {
        //ArrayList<Object> res = core.getValues(dbs, collections, fields);
        //   ArrayList<Object> res = core.getValues(dbs, collections, fields);
        ArrayList<Object> res = new ArrayList<Object>(2);
        ArrayList<String> list1 = new ArrayList<String>();
        ArrayList<Integer> list2 = new ArrayList<Integer>();
        if (checkbox1 != null) {
            list1.add("label1");
            list2.add(3);
        }
        if (checkbox2 != null) {
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