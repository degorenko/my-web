package client_app.hadoop_reader.output;

import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 6/3/14
 * Time: 12:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class Writer extends RecordWriter {

    @Override
    public void write(Object o, Object o2) throws IOException, InterruptedException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}