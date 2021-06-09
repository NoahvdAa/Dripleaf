package me.noahvdaa.dripleaf;

public class Dripleaf {

	private static DripleafServer server;

	public static void main(String[] args) {
		System.out.println("Welcome to Dripleaf. Please wait while the server is loading.");

		server = new DripleafServer(25565);
		server.start();
	}

	public static DripleafServer getServer() {
		return server;
	}

}
