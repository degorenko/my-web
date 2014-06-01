package client_app.simple_reader;

import java.io.File;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 6/1/14
 * Time: 7:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultiThreadFileReader {
    private ExecutorService pool;
    private HashSet<Future<?>> poolTaskStatus;

    public MultiThreadFileReader() {
        this.pool = Executors.newCachedThreadPool();
        this.poolTaskStatus = new HashSet<Future<?>>();
    }

    public void runReader(File path) {
        readFiles(path);
        waitForEnd();
    }

    private void waitForEnd() {
        int i = 0;
        while (i!= poolTaskStatus.size()) {
            i = 0;
            for(Future<?> f: poolTaskStatus) {
                if (f.isDone()) i++;
            }
        }
    }

    private void readFiles(File path)  {
        if (path.isDirectory()) {
            for(File f: path.listFiles()) {
                readFiles(f);
            }
        }
        else {
            Future<?> submit = pool.submit(new FileReader(path));
            poolTaskStatus.add(submit);
        }
    }
}
