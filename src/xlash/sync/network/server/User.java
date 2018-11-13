package xlash.sync.network.server;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import xlash.sync.listeners.GKeyListener;

public class User {
	
	public Socket socket;
	public Point prevPoint;
	
	/**
	 * The server's way of communicating with clients.
	 * @param socket
	 */
	public User(Socket socket) {
		this.socket = socket;
	}
	
	public void listen() {
		while(socket.isConnected()) {
			
		}
	}
	
	public void send() {
		while(socket.isConnected()) {
			
		}
	}
	
	public void sendMouseOffset(int dx, int dy) {
		ByteBuffer bb = ByteBuffer.allocate(8);
		bb.putInt(dx).putInt(dy); //Set dx and dy
		sendPayload(PacketType.MOUSE, bb.array());
	}
	
	public void sendGlobalMousePosition(int x, int y) {
		//Monitor 1 on server should be (0,0).
		//Based on server defined position, each monitor will have a different global offset
		
	}
	
	public void sendScreens() {
		
	}
	
	public void sendKeystrokes() {
		synchronized(GKeyListener.keysPressed) {
			synchronized (GKeyListener.keysReleased) {
				//Compile and send key downs
				ByteBuffer bb = ByteBuffer.allocate(4 * GKeyListener.keysPressed.size());
				for(int key : GKeyListener.keysPressed) {
					bb.putInt(key);
				}
				sendPayload(PacketType.KEYDOWN, bb.array());
				
				//Compile and send key ups
				bb = ByteBuffer.allocate(4 * GKeyListener.keysReleased.size());
				for(int key : GKeyListener.keysReleased) {
					bb.putInt(key);
				}
				sendPayload(PacketType.KEYUP, bb.array());
				
				GKeyListener.clearKeys();
			}
		}
	}
	
	public void sendPayload(PacketType packetType, byte[] payload) {
		DataOutputStream os;
		try {
			os = new DataOutputStream(socket.getOutputStream());
			os.writeByte(packetType.id);
			os.write(payload);
			
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
