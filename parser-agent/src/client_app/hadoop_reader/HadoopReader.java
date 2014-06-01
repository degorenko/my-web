package client_app.hadoop_reader;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;

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
            Job job = new Job();
            //job.setJarByClass(WordCount.class);
            job.setJobName("Parse logs");
            FileInputFormat.setInputPaths(job, path.getAbsolutePath());
            //FileOutputFormat.setOutputPath(job, new Path(args[1]));
            job.setMapperClass(HadoopMapper.class);
            job.setReducerClass(HadoopReducer.class);
            //job.setOutputKeyClass(Text.class);
            //job.setOutputValueClass(IntWritable.class);
            job.waitForCompletion(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
