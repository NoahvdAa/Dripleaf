package me.noahvdaa.dripleaf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Dripleaf {

	private static DripleafServer server;

	public final static int PROTOCOL_VERSION = 755;
	public final static String PROTOCOL_VERSION_STRING = "1.17";
	public final static String BRAND = "Dripleaf";
	public final static String BRAND_COLOR = "#83aa3b";
	public final static String BRAND_LEGACY_COLOR = "§a";

	public static void main(String[] args) {
		System.out.println("Welcome to Dripleaf. Please wait while the server is loading.");

		File resourcesFolder = new File("./resources/");
		// Make resources folder if it doesn't exist yet.
		if (!resourcesFolder.exists()) {
			resourcesFolder.mkdir();
		}
		String[] resourcesToCopy = new String[]{"dimensions.nbt"};
		for (String resource : resourcesToCopy) {
			File resourceFile = new File(resourcesFolder.getPath() + File.separator + resource);

			// Check if the file exists.
			if (resourceFile.exists()) continue;

			InputStream inputStream = Dripleaf.class.getClassLoader().getResourceAsStream("dimensions.nbt");

			System.out.println("Creating resource " + resource + "...");

			try {
				Files.copy(inputStream, resourceFile.toPath());
			} catch (IOException e) {
				System.out.println("Failed to create resource " + resource + ":");
				e.printStackTrace();
				return;
			}
			System.out.println("Created resource " + resource + "!");
		}


		server = new DripleafServer(25565);
		server.start();
	}

	public static DripleafServer getServer() {
		return server;
	}

}
