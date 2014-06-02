package client_app.hadoop_reader.mapred;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 6/1/14
 * Time: 7:42 PM
 * To change this template use File | Settings | File Templates.
 */
import java.io.IOException;
import java.util.HashMap;

import client_app.Core;
import javafx.util.Pair;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class HadoopReducer extends Reducer<Integer, Pair<String, String>, Text, Text> {
    private HashMap<Integer, HashMap<String, String>> res;
    @Override
    public void reduce(Integer key, Iterable<Pair<String, String>> values, Context context)
            throws IOException, InterruptedException {
        res = new HashMap<Integer, HashMap<String, String>>();
        HashMap<String, String> map = new HashMap<String, String>();
        for (Pair<String, String> value : values) {
            map.put(value.getKey(), value.getValue());
        }
        res.put(key, map);
    }
}