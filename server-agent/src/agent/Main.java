package agent;

import agent.MongoConnect.MongoCore;
import agent.ServerCore.ServerAgent;
import agent.Utility.Notificator;

public class Main {

    public static void main(String args[])
    {
        String mongoServer = "localhost";
        int port = 27117;
        if (args.length >= 1) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception e) {
                Notificator.notificator("Wrong input parameter: port");
                e.printStackTrace();
            }
            if (args.length >= 2) {
                mongoServer = args[1];
            }
        }
        MongoCore.core = new MongoCore(mongoServer);
        MongoCore.core.connectDB();
        ServerAgent serverAgent = new ServerAgent(port);
        serverAgent.run();
    }
}