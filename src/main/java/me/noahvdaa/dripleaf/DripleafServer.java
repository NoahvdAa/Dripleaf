package me.noahvdaa.dripleaf;

import me.noahvdaa.dripleaf.net.ConnectionHandler;
import me.noahvdaa.dripleaf.net.KeepAliver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DripleafServer {

	private final int port;

	public int activeThreads = 0;
	public List<ConnectionHandler> connections;

	private boolean running;

	public DripleafServer(int port) {
		this.port = port;
	}

	public void start() {
		if (running) return;
		running = true;

		connections = new ArrayList<>();

		// Create the socket server.
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Failed to create server socket:");
			e.printStackTrace();
			return;
		}

		System.out.println("Listening on :" + port + "!");

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

}
