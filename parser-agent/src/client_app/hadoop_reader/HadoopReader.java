package client_app.hadoop_reader;

import client_app.Core;
import client_app.hadoop_reader.input.HadoopInputFormat;
import client_app.hadoop_reader.mapred.HadoopMapper;
import client_app.hadoop_reader.mapred.HadoopReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 6/1/14
 * Time: 8:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class HadoopReader {

    public void runReader(File path){
        try {
            Configuration conf = new Configuration();
            String jobUUID = UUID.randomUUID().toString();
            conf.set("jobUUID", jobUUID);
            conf.setInt("countKeys", 0);

            FileSystem fileSystem = FileSystem.get(conf);

            Job job = new Job(conf, "parsing");

            job.setInputFormatClass(HadoopInputFormat.class);
            HadoopInputFormat.setInputPaths(job, path.getAbsolutePath());

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

            job.setMapperClass(HadoopMapper.class);
            job.setReducerClass(HadoopReducer.class);

            job.setNumReduceTasks(1);
            job.waitForCompletion(true);

            Core.res = ReadFromHDFS(conf);
            fileSystem.deleteOnExit(new Path("/parsing/" + jobUUID));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap<Integer, HashMap<String, String>> ReadFromHDFS(Configuration conf) throws IOException, ClassNotFoundException {
        FileSystem fs = FileSystem.get(conf);
        Path path = new Path("/parsing" + conf.get("jobUUID") + "/out");
        ObjectInputStream in = new ObjectInputStream(fs.open(path));
        HashMap<Integer, HashMap<String, String>> res = (HashMap<Integer, HashMap<String, String>>)in.readObject();
        in.close();
        return res;
    }
}
