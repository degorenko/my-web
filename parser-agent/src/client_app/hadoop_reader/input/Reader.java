package client_app.hadoop_reader.input;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
    private String curPath;
    private String curLine;
    private FileSystem fs;
    private FileStatus[] status;
    private BufferedReader br;


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
        getNextFile();
        return new Text(curPath);
    }

    @Override
    public Text getCurrentValue() throws IOException, InterruptedException {
        getNextLine();
        return new Text(curLine);
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

    public void getNextFile() throws IOException {
        if (fs == null) {
            fs = FileSystem.get(new Configuration());
            curPath = path;
        }
        status = fs.listStatus(new Path(curPath));
        for (int i=0;i<status.length;i++){
            if (status[i].isDir()) {
                curPath = status[i].getPath().getName();
                getNextFile();
            }
            br=new BufferedReader(new InputStreamReader(fs.open(status[i].getPath())));
        }
    }

    public void getNextLine() throws IOException {
        if (br == null) {
            getNextFile();
            getNextLine();
        }
        String line = br.readLine();
        if (line != null) {
            curLine = line;
        }  else {
            br = null;
            getNextFile();
            getNextLine();
        }
    }
}
