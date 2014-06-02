package client_app.hadoop_reader.input;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 6/2/14
 * Time: 11:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class HadoopInputFormat extends FileInputFormat<Text,Text> {
    @Override
    protected boolean isSplitable(JobContext context, Path filename) {
        return false;
    }

    @Override
    public RecordReader<Text, Text> createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        FileSplit fileSplit = (FileSplit) inputSplit;
        return new Reader(fileSplit.getPath().toString());
    }
}
