package client_app.hadoop_reader.mapred;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 6/1/14
 * Time: 7:40 PM
 * To change this template use File | Settings | File Templates.
 */
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client_app.Core;
import javafx.util.Pair;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class HadoopMapper extends Mapper<Text, Text, String, Pair<String, String>> {

    @Override
    public void map(Text key, Text line, Context context)
            throws IOException, InterruptedException {
        String[] keys = context.getConfiguration().getStrings("keys");
        for (String conditionKey: keys){
            Pattern p = Pattern.compile(context.getConfiguration().getStrings(conditionKey)[0], Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(line.toString());
            if (m.find()) {
                p = Pattern.compile(context.getConfiguration().getStrings(conditionKey)[1], Pattern.CASE_INSENSITIVE);
                m = p.matcher(line.toString());
                m.find();
                context.write(key.toString(), new Pair<String, String>(conditionKey, m.group(0)));
                boolean countKey = context.getConfiguration().getBoolean(key.toString(), false);
                if (!countKey) {
                    context.getConfiguration().setBoolean(key.toString(), true);
                    int count = context.getConfiguration().getInt("countKeys", 0);
                    context.getConfiguration().setInt("countKeys", count++);
                }
            }
        }
    }
}