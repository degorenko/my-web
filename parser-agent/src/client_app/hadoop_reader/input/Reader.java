package client_app.hadoop_reader.input;

import client_app.hadoop_reader.HadoopReader;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 6/2/14
 * Time: 11:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class Reader extends RecordReader<Text, Text> {
    private volatile boolean finished = false;
    private final String path;

    public Reader(String path) {
        this.path = path;
    }

    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        // No op
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        return !finished;
    }

    @Override
    public Text getCurrentKey() throws IOException, InterruptedException {
        return new Text(HadoopReader.key);
    }

    @Override
    public Text getCurrentValue() throws IOException, InterruptedException {
        finished = true;
        return new Text(path);
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        if (!finished) {
            return 0;
        }
        return 1;
    }

    @Override
    public void close() throws IOException {
        // No op
    }
}
