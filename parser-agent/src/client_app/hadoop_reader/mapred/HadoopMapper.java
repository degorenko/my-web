package client_app.hadoop_reader.mapred;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 6/1/14
 * Time: 7:40 PM
 * To change this template use File | Settings | File Templates.
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client_app.Core;
import javafx.util.Pair;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class HadoopMapper extends Mapper<Text, File, Integer, Pair<String, String>> {

    @Override
    public void map(Text key, File file, Context context)
            throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new java.io.FileReader(file));
        String line;

        while ((line = reader.readLine()) != null)  {
            for (String conditionKey: Core.conditions.keySet()){
                Pattern p = Pattern.compile(Core.conditions.get(conditionKey)[0], Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(line);
                if (m.find()) {
                    p = Pattern.compile(Core.conditions.get(conditionKey)[1], Pattern.CASE_INSENSITIVE);
                    m = p.matcher(line);
                    m.find();
                    context.write(file.hashCode(), new Pair<String, String>(conditionKey, m.group(0)));
                }
            }
        }
    }
}