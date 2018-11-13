package xlash.sync.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import xlash.sync.network.client.Client;
import xlash.sync.network.server.Server;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Window extends JFrame {

	private static final long serialVersionUID = 9015794276060922179L;
	private JPanel contentPane;
	
	public static Server server;
	public static Client client;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Window frame = new Window();
		frame.setVisible(true);
		
		Thread window = new Thread("Window") {
			public void run() {
				try {
					while(true) {
						frame.getContentPane().repaint();
						Thread.sleep(1000/60);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		window.start();
	}

	/**
	 * Create the frame.
	 */
	public Window() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 704, 439);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmStartServer = new JMenuItem("Start Server");
		mnFile.add(mntmStartServer);
		
		JMenuItem mntmConnect = new JMenuItem("Connect");
		mntmStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmStartServer.setEnabled(false);
				mntmConnect.setEnabled(false);
				Thread serverThread = new Thread("Server") {
					public void run() {
						server = new Server(12345);
						server.listen();
						mntmStartServer.setEnabled(true);
						mntmConnect.setEnabled(true);
					}
				};
				serverThread.start();
			}
		});
		mntmConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mntmStartServer.setEnabled(false);
				mntmConnect.setEnabled(false);
				Thread connect = new Thread("Connect") {
					public void run() {
						client = new Client("127.0.0.1", 12345);
						client.listen();
						mntmStartServer.setEnabled(true);
						mntmConnect.setEnabled(true);
					}
				};
				connect.start();
			}
		});
		mnFile.add(mntmConnect);
		
		JMenuItem mntmCloseConnections = new JMenuItem("Close Connections");
		mnFile.add(mntmCloseConnections);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[grow][]"));
		
		ScreenPanel panel = new ScreenPanel();
		contentPane.add(panel, "cell 0 0 1 2,grow");
	}
	
}
