package me.noahvdaa.dripleaf;

import me.noahvdaa.dripleaf.net.ConnectionHandler;
import me.noahvdaa.dripleaf.net.KeepAliver;
import me.noahvdaa.dripleaf.util.Logger;
import me.noahvdaa.dripleaf.util.Metrics;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DripleafServer {

	public int threadId = 0;
	public List<ConnectionHandler> connections;
	public Properties configuration;
	public SharedObjectCacher sharedObjectCacher;

	private boolean running;
	private boolean debugMode;
	private boolean bungeecordMode;

	public void start() {
		if (running) return;
		running = true;

		configuration = new Properties();
		try {
			configuration.load(new FileInputStream("./server.properties"));
		} catch (Exception e) {
			Logger.info("Failed to load server.properties file:");
			e.printStackTrace();
			return;
		}

		try {
			sharedObjectCacher = new SharedObjectCacher();
		} catch (IOException e) {
			Logger.info("Failed to cache shared objects:");
			e.printStackTrace();
			return;
		}

		// True/false settings
		debugMode = configuration.getProperty("debug-mode", "false").equalsIgnoreCase("true");
		bungeecordMode = configuration.getProperty("bungeecord", "false").equalsIgnoreCase("true");

		if (bungeecordMode) {
			Logger.info("Bungeecord mode enabled.");
		} else {
			Logger.info("Bungeecord mode is not enabled. If you're using bungeecord, you should enable it.");
		}

		connections = new ArrayList<>();

		// Create the socket server.
		ServerSocket serverSocket;
		InetAddress listenInterface;
		try {
			listenInterface = InetAddress.getByName(configuration.getProperty("server-ip", "0.0.0.0"));
			serverSocket = new ServerSocket(Integer.parseInt(configuration.getProperty("server-port", "25565")), 50, listenInterface);
		} catch (IOException e) {
			Logger.info("Failed to create server socket:");
			e.printStackTrace();
			return;
		}

		// bStats.
		Metrics.DripleafMetrics.start(this);

		Logger.info("Listening on " + serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort() + "!");

		Logger.info("Done (" + new DecimalFormat("#.00").format((System.currentTimeMillis() - Dripleaf.startedOn) / 1000) + "s)!  For help, type \"help\".");

		// Send out the keepalives.
		KeepAliver keepAliver = new KeepAliver();
		keepAliver.start();

		// Main server loop.
		while (true) {
			try {
				// Accept the socket.
				Socket socket = serverSocket.accept();
				ConnectionHandler ch = new ConnectionHandler(socket);
				ch.start();
			} catch (IOException e) {
				Logger.debug("Failed to accept connection:");
				if (debugMode)
					e.printStackTrace();
			}
		}
	}

	public boolean isDebugMode() {
		return this.debugMode;
	}

	public boolean isBungeecordModeMode() {
		return this.bungeecordMode;
	}

}
