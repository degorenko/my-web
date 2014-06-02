package client_app.hadoop_reader.output;

import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 6/3/14
 * Time: 12:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class HadoopOutputFormat extends FileOutputFormat {

    @Override
    public RecordWriter getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return new Writer();
    }
}
