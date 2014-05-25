package client_app.parser;

import client_app.parser.file_stream.MultiThreadFileReader;
import client_app.sender.Sender;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 4/26/14
 * Time: 7:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Core {
    private final File logs;
    private final File JSONConditions;
    private final File JSONInfo;
    private ExecutorService pool;
    private HashSet<Future<?>> poolTaskStatus;
    public static HashMap<String, HashMap<String, String>> res;
    public static HashMap<String, String[]> conditions;

    public Core(String logs, String conditions, String info){
        this.logs = new File(logs);
        this.JSONConditions = new File(conditions);
        this.JSONInfo = new File(info);
        this.pool = Executors.newCachedThreadPool();
        this.res = new HashMap<String, HashMap<String, String>>();
        this.poolTaskStatus = new HashSet<Future<?>>();
    }

    public void run() throws IOException, ParseException, ClassNotFoundException {
        if (!logs.exists()) {
            throw new IOException("Path " + logs.getPath() + " doesn't exist!");
        }
        if (!JSONInfo.exists()) {
            throw new IOException("File " + JSONInfo.getPath() + " doesn't exist!");
        }
        if (!JSONConditions.exists()) {
            throw new IOException("File " + JSONConditions.getPath() + " doesn't exist!");
        }
        HashMap<String, String> serverInfo = JSONFileParser.parseJsonInfo(JSONInfo);
        notificator("Try to establish a connection...");
        Sender sender = new Sender(serverInfo.get("ip"), Integer.parseInt(serverInfo.get("port")));
        sender.establishConnection();
        Core.conditions = JSONFileParser.parseJsonConditions(JSONConditions);
        notificator("Parsing log files, waiting...");
        readFiles(logs);
        waitForEnd();
        notificator("Sending data...");
        sender.sendData(serverInfo.get("name"));
        notificator("Data have been successfully send");
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
            Future<?> submit = pool.submit(new MultiThreadFileReader(path));
            poolTaskStatus.add(submit);
        }
    }

    private void notificator(String line){
        System.out.println(line);
    }
}
