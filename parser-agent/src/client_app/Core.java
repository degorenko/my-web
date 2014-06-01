package client_app;

import client_app.hadoop_reader.HadoopReader;
import client_app.parser.JSONFileParser;
import client_app.simple_reader.MultiThreadFileReader;
import client_app.sender.Sender;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

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
    private final boolean useHadoop;

    public static HashMap<Integer, HashMap<String, String>> res;
    public static HashMap<String, String[]> conditions;

    public Core(String logs, String conditions, String info, boolean hadoop){
        this.logs = new File(logs);
        this.JSONConditions = new File(conditions);
        this.JSONInfo = new File(info);
        this.res = new HashMap<Integer, HashMap<String, String>>();
        this.useHadoop = hadoop;
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
        if (useHadoop) {
            HadoopReader reader = new HadoopReader();
            reader.runReader(logs);
        }   else {
            MultiThreadFileReader reader = new MultiThreadFileReader();
            reader.runReader(logs);
        }
        notificator("Sending data...");
        sender.sendData(serverInfo.get("name"));
        notificator("Data have been successfully send");
    }

    private void notificator(String line){
        System.out.println(line);
    }
}
