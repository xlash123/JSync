package xlash.sync.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	public ArrayList<User> users;
	
	public ServerSocket server;
	
	public Server(int port) {
		try {
			server = new ServerSocket(port);
			users = new ArrayList<>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void listen() {
		while(!server.isClosed()) {
			try {
				Socket socket = server.accept();
				users.add(new User(socket));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
