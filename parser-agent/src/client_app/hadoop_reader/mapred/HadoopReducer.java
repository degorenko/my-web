package client_app.hadoop_reader.mapred;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 6/1/14
 * Time: 7:42 PM
 * To change this template use File | Settings | File Templates.
 */
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javafx.util.Pair;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.fs.Path;

public class HadoopReducer extends Reducer<Integer, Pair<String, String>, Text, Text> {
    private final HashMap<Integer, HashMap<String, String>> res = new HashMap<Integer, HashMap<String, String>>();
    private int count = 0;
    @Override
    public void reduce(Integer key, Iterable<Pair<String, String>> values, Context context)
            throws IOException, InterruptedException {
        HashMap<String, String> map = new HashMap<String, String>();
        for (Pair<String, String> value : values) {
            map.put(value.getKey(), value.getValue());
        }
        res.put(key, map);
        count++;

        if (count == context.getConfiguration().getInt("countKeys", 0)) {
            writeToHDFS(context);
        }
    }

    private void writeToHDFS(Context context) throws IOException {
        FileSystem fs = FileSystem.get(context.getConfiguration());
        Path path = new Path("/parsing" + context.getConfiguration().get("jobUUID") + "/out");
        ObjectOutputStream out = new ObjectOutputStream(fs.create(path));
        out.writeObject(res);
        out.close();
    }
}