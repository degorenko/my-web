package client_app.hadoop_reader;

import client_app.hadoop_reader.input.HadoopInputFormat;
import client_app.hadoop_reader.mapred.HadoopMapper;
import client_app.hadoop_reader.mapred.HadoopReducer;
import client_app.hadoop_reader.output.HadoopOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.File;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 6/1/14
 * Time: 8:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class HadoopReader {
    public static final String key = "ololo";

    public void runReader(File path){
        try {
            Configuration conf = new Configuration();
            String jobUUID = UUID.randomUUID().toString();
            conf.set("jobUUID", jobUUID);
            FileSystem fileSystem = FileSystem.get(conf);

            Job job = new Job(conf, "parsing");

            job.setInputFormatClass(HadoopInputFormat.class);
            FileInputFormat.setInputPaths(job, path.getAbsolutePath());

            job.setOutputFormatClass(HadoopOutputFormat.class);
            Path outputPath = new Path("/parsing" + jobUUID + "/out");
            HadoopOutputFormat.setOutputPath(job, outputPath);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

            job.setMapperClass(HadoopMapper.class);
            job.setReducerClass(HadoopReducer.class);

            job.waitForCompletion(true);

            fileSystem.copyToLocalFile(false, new Path("/parsing/" + jobUUID + "/out.res"), new Path("/tmp/out"));

            fileSystem.deleteOnExit(new Path("/parsing/" + jobUUID));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
