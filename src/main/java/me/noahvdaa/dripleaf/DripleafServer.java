package me.noahvdaa.dripleaf;

import me.noahvdaa.dripleaf.net.ConnectionHandler;
import me.noahvdaa.dripleaf.net.KeepAliver;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DripleafServer {

	public int activeThreads = 0;
	public List<ConnectionHandler> connections;
	public Properties configuration;

	private boolean running;
	private boolean debugMode;

	public void start() {
		if (running) return;
		running = true;

		configuration = new Properties();
		try {
			configuration.load(new FileInputStream("./server.properties"));
		} catch (Exception e) {
			System.out.println("Failed to load server.properties file:");
			e.printStackTrace();
			return;
		}

		// Check if debug mode should be enabled.
		debugMode = configuration.getProperty("debug-mode", "false").equalsIgnoreCase("true");

		connections = new ArrayList<>();

		// Create the socket server.
		ServerSocket serverSocket;
		InetAddress listenInterface;
		try {
			listenInterface = InetAddress.getByName(configuration.getProperty("server-ip", "0.0.0.0"));
			serverSocket = new ServerSocket(Integer.parseInt(configuration.getProperty("server-port", "25565")), 50, listenInterface);
		} catch (IOException e) {
			System.out.println("Failed to create server socket:");
			e.printStackTrace();
			return;
		}

		System.out.println("Listening on " + serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort() + "!");

		// Send out the keepalives.
		KeepAliver keepAliver = new KeepAliver();
		keepAliver.run();

		// Main server loop.
		while (true) {
			try {
				// Accept the socket.
				Socket socket = serverSocket.accept();
				ConnectionHandler ch = new ConnectionHandler(socket);
				ch.run();
			} catch (IOException e) {
				System.out.println("Failed to accept connection:");
				e.printStackTrace();
			}
		}
	}

	public boolean isDebugMode() {
		return this.debugMode;
	}

}
