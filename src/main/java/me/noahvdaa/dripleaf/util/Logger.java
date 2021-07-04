package me.noahvdaa.dripleaf.util;

import me.noahvdaa.dripleaf.Dripleaf;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

	public static void debug(String message) {
		if (!Dripleaf.getServer().isDebugMode()) return;
		String date = new SimpleDateFormat("HH':'mm':'ss").format(new Date());
		System.out.println("[DEBUG] [" + date + "] [" + Thread.currentThread().getName() + "] " + message);
	}

	public static void info(String message) {
		String date = new SimpleDateFormat("HH':'mm':'ss").format(new Date());
		System.out.println("[INFO] [" + date + "] [" + Thread.currentThread().getName() + "] " + message);
	}

}
