package me.noahvdaa.dripleaf;

import me.noahvdaa.dripleaf.util.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Console extends Thread {

	private final DripleafServer server;

	public Console(DripleafServer server) {
		this.server = server;
	}

	@Override
	public void run() {
		setName("console");

		BufferedReader reader = null;

		if (System.console() == null) {
			reader = new BufferedReader(new InputStreamReader(System.in));
		}

		while (true) {
			String input = "";
			if (reader == null) {
				input = System.console().readLine("> ");
			} else {
				try {
					input = reader.readLine();
				} catch (IOException e) {
					// Swallow.
				}
			}
			if (input.trim().equals("")) continue;

			String[] argsParsed = input.split(" ");
			String command = argsParsed[0].toLowerCase();
			String[] args = Arrays.stream(argsParsed).skip(1).toArray(String[]::new);

			switch (command) {
				case "help":
					Logger.info("Available commands:");
					Logger.info("- help: Shows this message");
					Logger.info("- stop: Stops the server.");
					break;
				case "end":
				case "shutdown":
				case "stop":
					// This should also nicely shut down the server.
					System.exit(0);
					break;
				default:
					Logger.info("Unknown command. Type \"help\" for help.");
					break;
			}
		}
	}

}
