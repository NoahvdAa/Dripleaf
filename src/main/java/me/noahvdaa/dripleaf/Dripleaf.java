package me.noahvdaa.dripleaf;

import me.noahvdaa.dripleaf.util.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class Dripleaf {

	private static DripleafServer server;

	public final static long startedOn = System.currentTimeMillis();
	public final static int PROTOCOL_VERSION = 756;
	public final static String PROTOCOL_VERSION_STRING = "1.17.1";
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

		String[] resourcesToCopy = new String[]{"dimensions.nbt", "server.properties"};
		for (String resource : resourcesToCopy) {
			File resourceFile = new File(resourcesFolder.getPath() + File.separator + resource);
			if (resource.equals("server.properties")) {
				resourceFile = new File(resourcesFolder.getParent() + File.separator + resource);
			}

			// Check if the file exists.
			if (resourceFile.exists()) continue;

			InputStream inputStream = Dripleaf.class.getClassLoader().getResourceAsStream(resource);

			Logger.info("Copying resource " + resource + "...");

			try {
				Files.copy(inputStream, resourceFile.toPath());
			} catch (IOException e) {
				Logger.info("Failed to copy resource " + resource + ":");
				e.printStackTrace();
				return;
			}
			Logger.info("Copied resource " + resource + "!");
		}

		server = new DripleafServer();
		server.start();
	}

	public static DripleafServer getServer() {
		return server;
	}

}
