package client_app.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 4/26/14
 * Time: 8:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class JSONFileParser {
    public static HashMap<String, String[]> parseJsonConditions(File JSON) throws ParseException, IOException {
        HashMap<String, String[]> map = new HashMap<String, String[]>();
        JSONParser parser = new JSONParser();
        JSONObject json_obj = (JSONObject) parser.parse(read_json(JSON));
        for(Object s: json_obj.keySet()){
            String[] keys = new String[2];
            keys[0]= (String) ((JSONArray) json_obj.get(s)).get(0);
            keys[1]= (String) ((JSONArray) json_obj.get(s)).get(1);
            map.put((String)s, keys);
        }
        return map;
    }

    public static HashMap<String, String> parseJsonInfo(File JSON) throws ParseException, IOException {
        JSONParser parser = new JSONParser();
        JSONObject json_obj = (JSONObject) parser.parse(read_json(JSON));
        if (json_obj.keySet().contains("port") && json_obj.keySet().contains("ip")
                && json_obj.keySet().contains("name") && json_obj.keySet().size() == 3){
            HashMap<String, String> info = new HashMap<String, String>();
            for(Object s: json_obj.keySet()){
                info.put((String) s, (String) json_obj.get(s));
            }
            return info;
        } else {
            throw new IOException("Wrong structure of infoJSON file");
        }
    }

    private static String read_json(File jsonfile) throws IOException {
        FileInputStream json = new FileInputStream(jsonfile);
        byte[] str = new byte[json.available()];
        json.read(str);
        return new String(str);
    }
}
