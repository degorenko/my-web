package agent.ServerCore;

import agent.MongoConnect.MongoCore;
import agent.Utility.Notificator;
import com.mongodb.Mongo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 5/2/14
 * Time: 2:07 PM
 * To change this template use File | Settings | File Templates.
 */

public class ServerAgent {
    private final int port;

    private ServerSocket providerSocket;
    private Socket connection = null;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String message;

    public ServerAgent(int port) {
        this.port = port;
    }

    public void run() {
        openSocket();
        processing();
    }

    private void openSocket() {
        try {
            providerSocket = new ServerSocket(port);
            Notificator.notificator(String.format("Waiting for connection on a port %d", port));
        }
        catch (IOException e) {
            Notificator.notificator(String.format("Can't open connection on a port %d", port));
            e.printStackTrace();
        }
    }

    private void processing() {
        do {
            try {
                connection = providerSocket.accept();
                Notificator.notificator(String.format("Connection received from %1$s, host address: %2$s",
                        connection.getInetAddress().getHostName(), connection.getInetAddress().getHostAddress()));
                out = new ObjectOutputStream(connection.getOutputStream());
                out.flush();
                in = new ObjectInputStream(connection.getInputStream());
                sendMessage("MongoDB_server-agent");
                message = (String)in.readObject();
                if (message.equals("LogStorageAgent"))  {
                    String collectionName = (String) in.readObject();
                    HashMap<String, HashMap<String, String>> res = null;
                    int hash = 0;
                    boolean dataReceived = false;
                    do {
                        hash = (Integer) in.readObject();
                        res = (HashMap<String, HashMap<String, String>>) in.readObject();
                        if (res.hashCode() == hash) {
                            dataReceived = true;
                            sendMessage("OK");
                            Notificator.notificator("Data has been received");
                        } else {
                            sendMessage("Repeat");
                        }
                    } while (!dataReceived);
                    MongoCore.core.insertData(collectionName,
                            connection.getInetAddress().getHostAddress(),  res);
                }
            } catch (IOException e){
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    connection.close();
                    in.close();
                    out.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } while (true);
    }

    void sendMessage(String msg)
    {
        try{
            out.writeObject(msg);
            out.flush();
        }
        catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
}
