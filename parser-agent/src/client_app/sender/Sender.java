package client_app.sender;

import client_app.parser.Core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created with IntelliJ IDEA.
 * User: denis
 * Date: 4/27/14
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class Sender {
    private String serverIp;
    private int serverPort;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Sender(String ip, int port){
        this.serverIp = ip;
        this.serverPort = port;
    }

    public void sendData(String nameCol) throws IOException, ClassNotFoundException {
        String received;
        out.writeObject(nameCol);
        out.flush();
        do {
            out.writeObject(Core.res.hashCode());
            out.flush();
            out.writeObject(Core.res);
            out.flush();
            received = (String)in.readObject();
        } while (!received.equals("OK"));
        in.close();
        out.close();
        socket.close();
    }

    public void establishConnection() throws IOException, ClassNotFoundException {
        socket = new Socket(serverIp, serverPort);
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(socket.getInputStream());
        String message = (String)in.readObject();
        if (message.equals("MongoDB_server-agent")) {
            out.writeObject("LogStorageAgent");
            out.flush();
        } else {
            throw new IOException("Specified ip address is wrong");
        }
    }
}
