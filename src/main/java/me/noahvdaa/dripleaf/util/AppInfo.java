package me.noahvdaa.dripleaf.util;

import me.noahvdaa.dripleaf.Dripleaf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppInfo {

	public static final String version;
	public static final String commit;

	static {
		Properties p = new Properties();
		InputStream inputStream;
		try {
			inputStream = Dripleaf.class.getClassLoader().getResourceAsStream("info.properties");
			p.load(inputStream);
		} catch (IOException e) {
			// Swallow exception.
		}
		version = p.getProperty("version", "?");
		commit = p.getProperty("commit", "?");
	}

}
