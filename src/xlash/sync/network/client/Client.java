package xlash.sync.network.client;

import java.awt.AWTException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import xlash.sync.network.server.PacketType;

public class Client {
	
	public Socket socket;
	public Rectangle[] screens;
	
	public Client(String ip, int port) {
		try {
			socket = new Socket(ip, port);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendScreenData() {
		GraphicsDevice[] screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		ByteBuffer payload = ByteBuffer.allocate(4 * screens.length * 4);
		
		for(GraphicsDevice screen : screens) {
			Rectangle rect = screen.getDefaultConfiguration().getBounds();
			payload.putInt(rect.x).putInt(rect.y).putInt(rect.width).putInt(rect.height); //Set x, y, width, and height
		}
		
		DataOutputStream os;
		try {
			os = new DataOutputStream(socket.getOutputStream());
			os.writeByte(PacketType.SCREENS.id);
			os.write(payload.array());
			
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void listen() {
		while(socket.isConnected()) {
			try {
				DataInputStream dis = new DataInputStream(socket.getInputStream());
				
				PacketType packetType = PacketType.getPacketType(dis.readByte());
				
				switch(packetType) {
				case MOUSE:
					setMouseByOffset(dis.readInt(), dis.readInt(), dis.readInt());
					break;
				case SCREENS:
					updateScreens(dis);
					break;
				case KEYDOWN:
				case KEYUP:
					processKeys(packetType, dis);
					break;
					default:
						System.out.println("Invalid packet type.");
				}
				
			} catch (IOException | AWTException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void processKeys(PacketType pt, DataInputStream dis) throws IOException, AWTException {
		Robot r = new Robot();
		if(pt == PacketType.KEYDOWN) {
			for(int key=dis.readInt(); dis.available()>0;) {
				r.keyPress(key);
			}
		}else {
			for(int key=dis.readInt(); dis.available()>0;) {
				r.keyRelease(key);
			}
		}
	}
	
	public void updateScreens(DataInputStream dis) throws IOException {
		synchronized (screens) {
			screens = new Rectangle[dis.available()/16]; //16 is the size of 4 integers, which makes up one rectangle
			
			for(int i=0; i<screens.length; i++) {
				screens[i] = new Rectangle(dis.readInt(), dis.readInt(), dis.readInt(), dis.readInt());
			}
		}
	}
	
	public void setMouseByOffset(int x, int y, int monitor) {
		Point p = new Point(x, y);
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice[] gs = ge.getScreenDevices();
	    try {
            Robot r = new Robot(gs[monitor]);
            r.mouseMove(p.x, p.y);
        } catch (AWTException e) {
            e.printStackTrace();
        }
	}

}
