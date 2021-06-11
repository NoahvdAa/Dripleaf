package me.noahvdaa.dripleaf;

import me.noahvdaa.dripleaf.net.ConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DripleafServer {

	private final int port;

	public int activeThreads = 0;

	private boolean running;

	public DripleafServer(int port) {
		this.port = port;
	}

	public void start() {
		if (running) return;
		running = true;

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
